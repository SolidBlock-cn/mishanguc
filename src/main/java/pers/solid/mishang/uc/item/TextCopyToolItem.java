package pers.solid.mishang.uc.item;

import com.google.common.collect.Collections2;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderContext;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.SignBlockEntity;
import net.minecraft.block.entity.SignText;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.data.client.Models;
import net.minecraft.data.client.TextureKey;
import net.minecraft.data.server.recipe.CraftingRecipeJsonBuilder;
import net.minecraft.data.server.recipe.ShapedRecipeJsonBuilder;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtByte;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtList;
import net.minecraft.recipe.book.RecipeCategory;
import net.minecraft.text.*;
import net.minecraft.util.ActionResult;
import net.minecraft.util.DyeColor;
import net.minecraft.util.Formatting;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.Unmodifiable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pers.solid.brrp.v1.generator.ItemResourceGenerator;
import pers.solid.brrp.v1.model.ModelJsonBuilder;
import pers.solid.mishang.uc.MishangUtils;
import pers.solid.mishang.uc.block.HungSignBlock;
import pers.solid.mishang.uc.block.StandingSignBlock;
import pers.solid.mishang.uc.blockentity.HungSignBlockEntity;
import pers.solid.mishang.uc.blockentity.StandingSignBlockEntity;
import pers.solid.mishang.uc.blockentity.WallSignBlockEntity;
import pers.solid.mishang.uc.text.TextContext;
import pers.solid.mishang.uc.util.RoadConnectionState;
import pers.solid.mishang.uc.util.TextBridge;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

/**
 * 用于复制粘贴文本的工具。持有该工具，“攻击”（默认左键）告示牌（含原版告示牌、悬挂告示牌和墙上的告示牌）可以将文本复制到物品中，"使用"（默认右键）告示牌可将文本粘贴上去。
 */
public class TextCopyToolItem extends BlockToolItem implements ItemResourceGenerator {
  // 1.18.1 之前用 apache 的 Logger，自 1.18.2 用 slf4j 的 Logger。
  public static final Logger LOGGER = LoggerFactory.getLogger(TextCopyToolItem.class);

  public TextCopyToolItem(Settings settings, @Nullable Boolean includesFluid) {
    super(settings, includesFluid);
  }

  @Override
  public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
    super.appendTooltip(stack, world, tooltip, context);
    tooltip.add(TextBridge.translatable("item.mishanguc.text_copy_tool.tooltip.1", TextBridge.keybind("key.attack").styled(style -> style.withColor(0xdddddd))).formatted(Formatting.GRAY));
    tooltip.add(TextBridge.translatable("item.mishanguc.text_copy_tool.tooltip.2", TextBridge.keybind("key.use").styled(style -> style.withColor(0xdddddd))).formatted(Formatting.GRAY));
    final NbtCompound tag = stack.getNbt();
    if (tag != null && tag.contains("texts", NbtElement.LIST_TYPE)) {
      final NbtList texts = tag.getList("texts", NbtElement.COMPOUND_TYPE);
      if (!texts.isEmpty()) {
        tooltip.add(TextBridge.translatable("item.mishanguc.text_copy_tool.tooltip.3").formatted(Formatting.GRAY));
        texts.stream().map(TextContext::fromNbt).map(TextContext::asStyledText).peek(text -> {
          final TextColor color = text.getStyle().getColor();
          if (color != null && color.getRgb() == 0) {
            // 考虑黑色的文本看不清楚，因此这种情况依然显示为灰色。
            text.formatted(Formatting.GRAY);
          }
        }).forEach(tooltip::add);
      }
    }
  }

  @Override
  public Text getName(ItemStack stack) {
    final NbtCompound nbt = stack.getNbt();
    if (nbt == null || !nbt.contains("texts", NbtElement.LIST_TYPE))
      return super.getName(stack);
    final MutableText text = super.getName(stack).copy();
    final List<MutableText> texts = ImmutableList.copyOf(
        nbt.getList("texts", NbtElement.COMPOUND_TYPE).stream()
            .map(TextContext::fromNbt)
            .map(TextContext::asStyledText)
            .iterator());
    if (!texts.isEmpty()) {
      MutableText appendable = TextBridge.empty();
      texts.forEach(t -> appendable.append(" ").append(t));
      text.append(
          TextBridge.literal(" -" + appendable.asTruncatedString(25)).formatted(Formatting.GRAY));
    }
    return text;
  }

  @Override
  public ActionResult useOnBlock(ItemStack stack, PlayerEntity player, World world, BlockHitResult blockHitResult, Hand hand, boolean fluidIncluded) {
    final BlockPos blockPos = blockHitResult.getBlockPos();
    final BlockState blockState = world.getBlockState(blockPos);
    final BlockEntity blockEntity = world.getBlockEntity(blockPos);
    final NbtList texts;
    final NbtCompound tag = stack.getNbt();
    if (tag == null || !tag.contains("texts", NbtElement.LIST_TYPE)) {
      player.sendMessage(TextBridge.translatable("item.mishanguc.text_copy_tool.message.fail.null_tag", TextBridge.keybind("key.attack").fillStyle(Style.EMPTY.withColor(0xdeb305))).formatted(Formatting.RED), true);
      return ActionResult.FAIL;
    } else {
      texts = tag.getList("texts", NbtElement.COMPOUND_TYPE);
    }
    try {
      if (blockEntity instanceof SignBlockEntity signBlockEntity) {
        if (world.isClient)
          return ActionResult.SUCCESS;
        final SignText textFacing = signBlockEntity.getTextFacing(player);
        final Text[] messagesUnfiltered = textFacing.getMessages(false);
        @Nullable DyeColor color = null;
        for (int i = 0; i < texts.size(); i++) {
          if (i < 4) {
            // 设置告示牌文字
            final TextContext textContext = TextContext.fromNbt(texts.get(i));
            messagesUnfiltered[i] = (textContext.asStyledText());

            // 设置告示牌颜色
            final DyeColor possibleColor = MishangUtils.colorBySignColor(textContext.color);
            if (possibleColor != null) {
              if (color == null) {
                color = possibleColor;
              }
            }
            if (color == null) {
              // 由于只支持部分颜色，故这些颜色没有使用。
              player.sendMessage(TextBridge.translatable("item.mishanguc.text_copy_tool.message.warn.colorSelectionLimit").formatted(Formatting.YELLOW), false);
            }
          } else {
            player.sendMessage(TextBridge.translatable("item.mishanguc.text_copy_tool.message.warn.outOfBound").formatted(Formatting.YELLOW), false);
          }
        }
        signBlockEntity.setText(color == null ? textFacing : textFacing.withColor(color), signBlockEntity.isPlayerFacingFront(player));
        blockEntity.markDirty();
        world.updateListeners(blockPos, blockState, blockState, 3);
        player.sendMessage(TextBridge.translatable("item.mishanguc.text_copy_tool.message.success.paste", Math.min(texts.size(), 4)), true);
        stack.damage(1, player, p -> p.sendToolBreakStatus(hand));
        return ActionResult.SUCCESS;
      } else if (blockEntity instanceof WallSignBlockEntity wallSignBlockEntity) {
        if (world.isClient)
          return ActionResult.SUCCESS;
        wallSignBlockEntity.textContexts = ImmutableList.copyOf(texts.stream().map(nbtElement -> TextContext.fromNbt(nbtElement, wallSignBlockEntity.createDefaultTextContext())).iterator());
        if (stack.getOrCreateNbt().getBoolean("fromVanillaSign")) {
          MishangUtils.rearrange(wallSignBlockEntity.textContexts);
        }
        blockEntity.markDirty();
        world.updateListeners(blockPos, blockState, blockState, 3);
        player.sendMessage(TextBridge.translatable("item.mishanguc.text_copy_tool.message.success.paste", wallSignBlockEntity.textContexts.size()), true);
        stack.damage(1, player, p -> p.sendToolBreakStatus(hand));
        return ActionResult.SUCCESS;
      } else if (blockEntity instanceof HungSignBlockEntity hungSignBlockEntity) {
        if (world.isClient)
          return ActionResult.SUCCESS;
        final Direction hitSide = blockHitResult.getSide();
        final Direction.Axis axis = blockState.get(HungSignBlock.AXIS);
        if (!axis.test(hitSide)) {
          final Iterator<Direction> validDirections = Arrays.stream(Direction.values()).filter(axis).iterator();
          // 如果点击的方向不正确，则无法复制和粘贴文本。
          player.sendMessage(TextBridge.translatable("item.mishanguc.text_copy_tool.message.fail.wrong_side", RoadConnectionState.text(hitSide).styled(style -> style.withColor(0xeecc44)), RoadConnectionState.text(validDirections.next()).styled(style -> style.withColor(0xb3ee45)), RoadConnectionState.text(validDirections.next()).styled(style -> style.withColor(0xb3ee45))).formatted(Formatting.RED), true);
          return ActionResult.FAIL;
        }
        final HashMap<@NotNull Direction, @Unmodifiable @NotNull List<@NotNull TextContext>> newTexts = new HashMap<>(hungSignBlockEntity.texts);
        final ImmutableList<@NotNull TextContext> newTextsThisSide = ImmutableList.copyOf(texts.stream().map(nbtElement -> TextContext.fromNbt(nbtElement, hungSignBlockEntity.createDefaultTextContext())).iterator());
        if (stack.getOrCreateNbt().getBoolean("fromVanillaSign")) {
          MishangUtils.rearrange(newTextsThisSide);
        }
        if (newTextsThisSide.isEmpty()) {
          newTexts.remove(hitSide);
        } else {
          newTexts.put(hitSide, newTextsThisSide);
        }
        hungSignBlockEntity.texts = ImmutableMap.copyOf(newTexts);
        blockEntity.markDirty();
        world.updateListeners(blockPos, blockState, blockState, 3);
        player.sendMessage(TextBridge.translatable("item.mishanguc.text_copy_tool.message.success.paste", newTextsThisSide.size()), true);
        stack.damage(1, player, p -> p.sendToolBreakStatus(hand));
        return ActionResult.SUCCESS;
      } else if (blockEntity instanceof StandingSignBlockEntity standingSignBlockEntity) {
        if (world.isClient)
          return ActionResult.SUCCESS;
        final Boolean isFront = StandingSignBlock.getHitSide(blockState, blockHitResult);
        if (isFront != null) {
          standingSignBlockEntity.setTextsOnSide(isFront, texts.stream().map(nbtElement -> TextContext.fromNbt(nbtElement, standingSignBlockEntity.createDefaultTextContext())).collect(ImmutableList.toImmutableList()));
          if (stack.getOrCreateNbt().getBoolean("fromVanillaSign")) {
            MishangUtils.rearrange(standingSignBlockEntity.getTextsOnSide(isFront));
          }
          blockEntity.markDirty();
          world.updateListeners(blockPos, blockState, blockState, 3);
          player.sendMessage(TextBridge.translatable("item.mishanguc.text_copy_tool.message.success.paste", standingSignBlockEntity.getTextsOnSide(isFront).size()), true);
          stack.damage(1, player, p -> p.sendToolBreakStatus(hand));
          return ActionResult.SUCCESS;
        }
      } else {
        if (world.isClient)
          return ActionResult.PASS;
        // 点击的方块不是可以识别的告示牌方块。
        player.sendMessage(TextBridge.translatable("item.mishanguc.text_copy_tool.message.fail.not_sign").formatted(Formatting.RED), true);
        return ActionResult.FAIL;
      }
    } catch (
        Throwable throwable) {
      player.sendMessage(TextBridge.translatable("item.mishanguc.text_copy_tool.message.fail.unexpected").formatted(Formatting.RED), true);
      LOGGER.error("Unexpected error found when pasting text", throwable);
    }
    return ActionResult.PASS;
  }

  /**
   * 持有该物品，左键（攻击，默认为左键）点击告示牌可复制其文字。如果被点击的告示牌不是文字，则不产生效果。若点击悬挂的告示牌，则只会复制其中一边的文字。
   */
  @Override
  public ActionResult beginAttackBlock(ItemStack stack, PlayerEntity player, World world, Hand hand, BlockPos pos, Direction direction, boolean fluidIncluded) {
    // 本方法仅限在服务器上使用。
    final BlockEntity blockEntity = world.getBlockEntity(pos);
    if (blockEntity instanceof SignBlockEntity signBlockEntity) {
      if (world.isClient)
        return ActionResult.SUCCESS;
      // 原版的告示牌
      final NbtList texts = new NbtList();
      final SignText textFacing = signBlockEntity.getTextFacing(player);
      for (int i = 0; i < 4; i++) {
        final TextContext textContext = new TextContext();
        textContext.text = textFacing.getMessage(i, false).copy();
        if (TextBridge.isEmpty(textContext.text))
          continue;
        textContext.color = textFacing.getColor().getSignColor();

        final Style style = textContext.text.getStyle();
        if (textContext.text.getContent() instanceof LiteralTextContent && textContext.text.getSiblings().isEmpty() && style.getClickEvent() == null && style.getHoverEvent() == null && style.getFont() == Style.DEFAULT_FONT_ID && style.getInsertion() == null) {
          // 对于文本为 literalText 的情况，应该将其 style 对象中的属性转化为 textContent 中的属性，除非 style 中有无法转换的部分。
          textContext.bold = style.isBold();
          textContext.italic = style.isItalic();
          textContext.strikethrough = style.isStrikethrough();
          textContext.underline = style.isUnderlined();
          textContext.obfuscated = style.isObfuscated();
          if (style.getColor() != null) {
            textContext.color = style.getColor().getRgb();
          }
          textContext.text = TextBridge.literal(((LiteralTextContent) textContext.text.getContent()).string());
        }
        final NbtCompound nbt0 = textContext.createNbt();
        nbt0.remove("size"); // 原版告示牌的文本没有 size
        texts.add(nbt0);
      }
      stack.setSubNbt("fromVanillaSign", NbtByte.of(true));
      stack.setSubNbt("texts", texts);
      player.sendMessage(TextBridge.translatable("item.mishanguc.text_copy_tool.message.success.copy", texts.size()), true);
      return ActionResult.SUCCESS;
    } else if (blockEntity instanceof WallSignBlockEntity wallSignBlockEntity) {
      if (world.isClient)
        return ActionResult.SUCCESS;
      // 迷上城建模组的墙上告示牌方块
      final NbtList texts = new NbtList();
      for (TextContext textContext : wallSignBlockEntity.textContexts) {
        texts.add(textContext.createNbt());
      }
      stack.setSubNbt("texts", texts);
      stack.setSubNbt("fromVanillaSign", NbtByte.of(false));
      player.sendMessage(TextBridge.translatable("item.mishanguc.text_copy_tool.message.success.copy", texts.size()), true);
      return ActionResult.SUCCESS;
    } else {
      final BlockState blockState = world.getBlockState(pos);
      if (blockEntity instanceof HungSignBlockEntity hungSignBlockEntity) {
        if (world.isClient)
          return ActionResult.SUCCESS;
        final Direction.Axis axis = blockState.get(HungSignBlock.AXIS);
        if (!axis.test(direction)) {
          final Iterator<Direction> validDirections = Arrays.stream(Direction.values()).filter(axis).iterator();
          // 如果点击的方向不正确，则无法复制和粘贴文本。
          player.sendMessage(TextBridge.translatable("item.mishanguc.text_copy_tool.message.fail.wrong_side", RoadConnectionState.text(direction).styled(style -> style.withColor(0xeecc44)), RoadConnectionState.text(validDirections.next()).styled(style -> style.withColor(0xb3ee45)), RoadConnectionState.text(validDirections.next()).styled(style -> style.withColor(0xb3ee45))).formatted(Formatting.RED), true);
          return ActionResult.FAIL;
        }
        final List<@NotNull TextContext> textContexts = hungSignBlockEntity.texts.getOrDefault(direction, ImmutableList.of());
        final NbtList texts = new NbtList();
        for (TextContext textContext : textContexts) {
          texts.add(textContext.createNbt());
        }
        stack.setSubNbt("texts", texts);
        stack.setSubNbt("fromVanillaSign", NbtByte.of(false));
        player.sendMessage(TextBridge.translatable("item.mishanguc.text_copy_tool.message.success.copy", texts.size()), true);
        return ActionResult.SUCCESS;
      } else if (blockEntity instanceof StandingSignBlockEntity standingSignBlockEntity) {
        Boolean hitSide = StandingSignBlock.getHitSide(blockState, direction);
        if (hitSide == null) {
          final HitResult raycast0 = player.raycast(4.5, 0, includesFluid(stack, false));
          if (raycast0 instanceof BlockHitResult)
            hitSide = StandingSignBlock.getHitSide(blockState, (BlockHitResult) raycast0);
        }
        if (hitSide == null)
          return world.isClient ? ActionResult.PASS : ActionResult.FAIL;
        final List<TextContext> textContexts = standingSignBlockEntity.getTextsOnSide(hitSide);
        final NbtList texts = new NbtList();
        texts.addAll(Collections2.transform(textContexts, TextContext::createNbt));
        stack.setSubNbt("texts", texts);
        stack.setSubNbt("fromVanillaSign", NbtByte.of(false));
        player.sendMessage(TextBridge.translatable("item.mishanguc.text_copy_tool.message.success.copy", texts.size()), true);
        return ActionResult.SUCCESS;
      } else {
        if (world.isClient)
          return ActionResult.SUCCESS;
        // 点击的方块不是可以识别的告示牌方块。
        player.sendMessage(TextBridge.translatable("item.mishanguc.text_copy_tool.message.fail.not_sign").formatted(Formatting.RED), true);
        return ActionResult.FAIL;
      }
    }
  }

  @Environment(EnvType.CLIENT)
  @Override
  public boolean renderBlockOutline(PlayerEntity player, ItemStack itemStack, WorldRenderContext worldRenderContext, WorldRenderContext.BlockOutlineContext blockOutlineContext, Hand hand) {
    final BlockEntity blockEntity = worldRenderContext.world().getBlockEntity(blockOutlineContext.blockPos());
    if (blockEntity instanceof SignBlockEntity || blockEntity instanceof HungSignBlockEntity || blockEntity instanceof WallSignBlockEntity || blockEntity instanceof StandingSignBlockEntity) {
      return super.renderBlockOutline(player, itemStack, worldRenderContext, blockOutlineContext, hand);
    } else {
      return false;
    }
  }

  @Environment(EnvType.CLIENT)
  @Override
  public @Nullable ModelJsonBuilder getItemModel() {
    return ModelJsonBuilder.create(Models.HANDHELD).addTexture(TextureKey.LAYER0, getTextureId());
  }

  @Override
  public RecipeCategory getRecipeCategory() {
    return RecipeCategory.TOOLS;
  }

  @Override
  public @NotNull CraftingRecipeJsonBuilder getCraftingRecipe() {
    return ShapedRecipeJsonBuilder.create(getRecipeCategory(), this)
        .patterns(
            "SPS",
            " / ",
            " / "
        )
        .input('P', Items.PAPER)
        .input('S', Items.SLIME_BALL)
        .input('/', Items.STICK)
        .criterionFromItem("has_paper", Items.PAPER)
        .criterionFromItem("has_slime_ball", Items.SLIME_BALL);
  }
}
