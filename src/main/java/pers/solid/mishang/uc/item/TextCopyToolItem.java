package pers.solid.mishang.uc.item;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import net.devtech.arrp.generator.ItemResourceGenerator;
import net.devtech.arrp.json.models.JModel;
import net.devtech.arrp.json.recipe.JRecipe;
import net.devtech.arrp.json.recipe.JShapedRecipe;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderContext;
import net.fabricmc.fabric.api.util.NbtType;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.SignBlockEntity;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtByte;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtList;
import net.minecraft.text.MutableText;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.text.TextColor;
import net.minecraft.util.ActionResult;
import net.minecraft.util.DyeColor;
import net.minecraft.util.Formatting;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.Unmodifiable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pers.solid.mishang.uc.MishangUtils;
import pers.solid.mishang.uc.block.HungSignBlock;
import pers.solid.mishang.uc.blockentity.HungSignBlockEntity;
import pers.solid.mishang.uc.blockentity.WallSignBlockEntity;
import pers.solid.mishang.uc.text.TextContext;

import java.util.*;

/**
 * 用于复制粘贴文本的工具。持有该工具，“攻击”（默认左键）告示牌（含原版告示牌、悬挂告示牌和墙上的告示牌）可以将文本复制到物品中，"使用"（默认右键）告示牌可将文本粘贴上去。
 */
public class TextCopyToolItem extends BlockToolItem implements ItemResourceGenerator {
  // 1.18.1 之前用 apache 的 Logger，自 1.18.2 用 slf4j 的 Logger。
  public static final Logger LOGGER = LoggerFactory.getLogger(TextCopyToolItem.class);

  public TextCopyToolItem(Settings settings, @Nullable Boolean includesFluid) {
    super(settings, includesFluid);
  }

  @Environment(EnvType.CLIENT)
  @Override
  public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
    super.appendTooltip(stack, world, tooltip, context);
    tooltip.add(new TranslatableText("item.mishanguc.text_copy_tool.tooltip.1", new KeybindText("key.attack").styled(style -> style.withColor(TextColor.fromRgb(0xdddddd)))).formatted(Formatting.GRAY));
    tooltip.add(new TranslatableText("item.mishanguc.text_copy_tool.tooltip.2", new KeybindText("key.use").styled(style -> style.withColor(TextColor.fromRgb(0xdddddd)))).formatted(Formatting.GRAY));
    final NbtCompound tag = stack.getTag();
    if (tag != null && tag.contains("texts", NbtType.LIST)) {
      final NbtList texts = tag.getList("texts", NbtType.COMPOUND);
      if (!texts.isEmpty()) {
        tooltip.add(new TranslatableText("item.mishanguc.text_copy_tool.tooltip.3").formatted(Formatting.GRAY));
        texts.stream().map(TextContext::fromNbt).map(TextContext::asStyledText).filter(Objects::nonNull).peek(text -> {
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
    final NbtCompound nbt = stack.getTag();
    if (nbt == null || !nbt.contains("texts", NbtType.LIST)) return super.getName(stack);
    final MutableText text = super.getName(stack).copy();
    final List<MutableText> texts = ImmutableList.copyOf(
        nbt.getList("texts", NbtType.COMPOUND).stream()
            .map(TextContext::fromNbt)
            .map(TextContext::asStyledText)
            .filter(Objects::nonNull)
            .iterator());
    if (!texts.isEmpty()) {
      MutableText appendable = new LiteralText("");
      texts.forEach(t -> appendable.append(" ").append(t));
      text.append(
          new LiteralText(" -" + appendable.asTruncatedString(25)).formatted(Formatting.GRAY));
    }
    return text;
  }

  @Override
  public ActionResult useOnBlock(PlayerEntity player, World world, BlockHitResult blockHitResult, Hand hand, boolean fluidIncluded) {
    if (world.isClient) return ActionResult.SUCCESS;
    final ItemStack stack = player.getStackInHand(hand);
    final BlockPos blockPos = blockHitResult.getBlockPos();
    final BlockState blockState = world.getBlockState(blockPos);
    final BlockEntity blockEntity = world.getBlockEntity(blockPos);
    final NbtList texts;
    final NbtCompound tag = stack.getTag();
    if (tag == null || !tag.contains("texts", NbtType.LIST)) {
      player.sendMessage(new TranslatableText("item.mishanguc.text_copy_tool.message.fail.null_tag", new KeybindText("key.attack").fillStyle(Style.EMPTY.withColor(TextColor.fromRgb(0xdeb305)))).formatted(Formatting.RED), false);
      return ActionResult.FAIL;
    } else {
      texts = tag.getList("texts", NbtType.COMPOUND);
    }
    try {
      if (blockEntity instanceof SignBlockEntity) {
        SignBlockEntity signBlockEntity = (SignBlockEntity) blockEntity;
        @Nullable DyeColor color = null;
        for (int i = 0; i < texts.size(); i++) {
          if (i < 4) {
            // 设置告示牌文字
            final TextContext textContext = TextContext.fromNbt(texts.get(i));
            signBlockEntity.setTextOnRow(i, textContext.text);

            // 设置告示牌颜色
            final DyeColor possibleColor = MishangUtils.colorBySignColor(textContext.color);
            if (possibleColor != null) {
              if (color == null) {
                color = possibleColor;
                signBlockEntity.setTextColor(possibleColor);
              } else if (possibleColor != color) {
                // possible != null, color != null
                // 由于只支持一个颜色，故仅使用第一个颜色。
                player.sendMessage(new TranslatableText("item.mishanguc.text_copy_tool.message.warn.colorConsistencyLimit").formatted(Formatting.YELLOW), false);
              }
            }
            if (color == null) {
              // 由于只支持部分颜色，故这些颜色没有使用。
              player.sendMessage(new TranslatableText("item.mishanguc.text_copy_tool.message.warn.colorSelectionLimit").formatted(Formatting.YELLOW), false);
            }
          } else {
            player.sendMessage(new TranslatableText("item.mishanguc.text_copy_tool.message.warn.outOfBound").formatted(Formatting.YELLOW), false);
          }
        }
        blockEntity.markDirty();
        world.updateListeners(blockPos, blockState, blockState, 3);
        player.sendMessage(new TranslatableText("item.mishanguc.text_copy_tool.message.success.paste", Math.min(texts.size(), 4)), false);
        stack.damage(1, player, p -> p.sendToolBreakStatus(hand));
        return ActionResult.SUCCESS;
      } else if (blockEntity instanceof WallSignBlockEntity) {
        WallSignBlockEntity wallSignBlockEntity = (WallSignBlockEntity) blockEntity;
        wallSignBlockEntity.textContexts = ImmutableList.copyOf(texts.stream().map(nbtElement -> TextContext.fromNbt(nbtElement, wallSignBlockEntity.getDefaultTextContext())).iterator());
        if (stack.getOrCreateTag().getBoolean("fromVanillaSign")) {
          MishangUtils.rearrange(wallSignBlockEntity.textContexts);
        }
        blockEntity.markDirty();
        world.updateListeners(blockPos, blockState, blockState, 3);
        player.sendMessage(new TranslatableText("item.mishanguc.text_copy_tool.message.success.paste", wallSignBlockEntity.textContexts.size()), false);
        stack.damage(1, player, p -> p.sendToolBreakStatus(hand));
        return ActionResult.SUCCESS;
      } else if (blockEntity instanceof HungSignBlockEntity) {
        HungSignBlockEntity hungSignBlockEntity = (HungSignBlockEntity) blockEntity;
        final Direction hitSide = blockHitResult.getSide();
        final Direction.Axis axis = blockState.get(HungSignBlock.AXIS);
        if (!axis.test(hitSide)) {
          final Iterator<Direction> validDirections = Arrays.stream(Direction.values()).filter(axis).iterator();
          // 如果点击的方向不正确，则无法复制和粘贴文本。
          player.sendMessage(new TranslatableText("item.mishanguc.text_copy_tool.message.fail.wrong_side",
              // 无效的一侧：
              new TranslatableText("direction." + hitSide.getName()).setStyle(Style.EMPTY.withColor(TextColor.fromRgb(0xeecc44))),
              // 有效的两侧：
              new TranslatableText("direction." + validDirections.next()).setStyle(Style.EMPTY.withColor(TextColor.fromRgb(0xb3ee45))),
              new TranslatableText("direction." + validDirections.next()).setStyle(Style.EMPTY.withColor(TextColor.fromRgb(0xb3ee45)))
          ).formatted(Formatting.RED), false);
          return ActionResult.FAIL;
        }
        final HashMap<@NotNull Direction, @Unmodifiable @NotNull List<@NotNull TextContext>> newTexts = new HashMap<>(hungSignBlockEntity.texts);
        final ImmutableList<@NotNull TextContext> newTextsThisSide = ImmutableList.copyOf(texts.stream().map(nbtElement -> TextContext.fromNbt(nbtElement, hungSignBlockEntity.getDefaultTextContext())).iterator());
        if (stack.getOrCreateTag().getBoolean("fromVanillaSign")) {
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
        player.sendMessage(new TranslatableText("item.mishanguc.text_copy_tool.message.success.paste", newTextsThisSide.size()), false);
        stack.damage(1, player, p -> p.sendToolBreakStatus(hand));
        return ActionResult.SUCCESS;
      } else {
        // 点击的方块不是可以识别的告示牌方块。
        player.sendMessage(new TranslatableText("item.mishanguc.text_copy_tool.message.fail.not_sign").formatted(Formatting.RED), false);
      }
    } catch (Throwable throwable) {
      player.sendMessage(new TranslatableText("item.mishanguc.text_copy_tool.message.fail.unexpected").formatted(Formatting.RED), false);
      LOGGER.error("Unexpected error found when pasting text", throwable);
    }
    return ActionResult.PASS;
  }

  /**
   * 持有该物品，左键（攻击，默认为左键）点击告示牌可复制其文字。如果被点击的告示牌不是文字，则不产生效果。若点击悬挂的告示牌，则只会复制其中一边的文字。
   */
  @Override
  public ActionResult beginAttackBlock(PlayerEntity player, World world, Hand hand, BlockPos pos, Direction direction, boolean fluidIncluded) {
    if (world.isClient) return ActionResult.SUCCESS;
    // 本方法仅限在服务器上使用。
    final BlockEntity blockEntity = world.getBlockEntity(pos);
    final ItemStack stack = player.getStackInHand(hand);
    if (blockEntity instanceof SignBlockEntity) {
      // 原版的告示牌
      SignBlockEntity signBlockEntity = (SignBlockEntity) blockEntity;
      final NbtList texts = new NbtList();
      for (int i = 0; i < 4; i++) {
        final TextContext textContext = new TextContext();
        textContext.text = signBlockEntity.getTextOnRow(i).shallowCopy();
        if (textContext.text.equals(LiteralText.EMPTY)) continue;
        textContext.color = signBlockEntity.getTextColor().getSignColor();
        final NbtCompound nbt0 = textContext.writeNbt(new NbtCompound());
        nbt0.remove("size"); // 原版告示牌的文本没有 size
        texts.add(nbt0);
      }
      stack.putSubTag("fromVanillaSign", NbtByte.of(true));
      stack.putSubTag("texts", texts);
      player.sendMessage(new TranslatableText("item.mishanguc.text_copy_tool.message.success.copy", texts.size()), false);
      return ActionResult.SUCCESS;
    } else if (blockEntity instanceof WallSignBlockEntity) {
      // 迷上城建模组的墙上告示牌方块
      WallSignBlockEntity wallSignBlockEntity = (WallSignBlockEntity) blockEntity;
      final NbtList texts = new NbtList();
      for (TextContext textContext : wallSignBlockEntity.textContexts) {
        texts.add(textContext.writeNbt(new NbtCompound()));
      }
      stack.putSubTag("texts", texts);
      stack.putSubTag("fromVanillaSign", NbtByte.of(false));
      player.sendMessage(new TranslatableText("item.mishanguc.text_copy_tool.message.success.copy", texts.size()), false);
      return ActionResult.SUCCESS;
    } else if (blockEntity instanceof HungSignBlockEntity) {
      HungSignBlockEntity hungSignBlockEntity = (HungSignBlockEntity) blockEntity;
      final Direction.Axis axis = world.getBlockState(pos).get(HungSignBlock.AXIS);
      if (!axis.test(direction)) {
        final Iterator<Direction> validDirections = Arrays.stream(Direction.values()).filter(axis).iterator();
        // 如果点击的方向不正确，则无法复制和粘贴文本。
        player.sendMessage(new TranslatableText("item.mishanguc.text_copy_tool.message.fail.wrong_side",
            // 无效的一侧：
            new TranslatableText("direction." + direction.getName()).setStyle(Style.EMPTY.withColor(TextColor.fromRgb(0xeecc44))),
            // 有效的两侧：
            new TranslatableText("direction." + validDirections.next()).setStyle(Style.EMPTY.withColor(TextColor.fromRgb(0xb3ee45))),
            new TranslatableText("direction." + validDirections.next()).setStyle(Style.EMPTY.withColor(TextColor.fromRgb(0xb3ee45)))
        ).formatted(Formatting.RED), false);
        return ActionResult.FAIL;
      }
      final List<@NotNull TextContext> textContexts = hungSignBlockEntity.texts.getOrDefault(direction, ImmutableList.of());
      final NbtList texts = new NbtList();
      for (TextContext textContext : textContexts) {
        texts.add(textContext.writeNbt(new NbtCompound()));
      }
      stack.putSubTag("texts", texts);
      stack.putSubTag("fromVanillaSign", NbtByte.of(false));
      player.sendMessage(new TranslatableText("item.mishanguc.text_copy_tool.message.success.copy", texts.size()), false);
      return ActionResult.SUCCESS;
    } else {
      // 点击的方块不是可以识别的告示牌方块。
      player.sendMessage(new TranslatableText("item.mishanguc.text_copy_tool.message.fail.not_sign").formatted(Formatting.RED), false);
    }
    return ActionResult.FAIL;
  }

  @Environment(EnvType.CLIENT)
  @Override
  public boolean renderBlockOutline(PlayerEntity player, ItemStack itemStack, WorldRenderContext worldRenderContext, WorldRenderContext.BlockOutlineContext blockOutlineContext, Hand hand) {
    final BlockEntity blockEntity = worldRenderContext.world().getBlockEntity(blockOutlineContext.blockPos());
    if (blockEntity instanceof SignBlockEntity || blockEntity instanceof HungSignBlockEntity || blockEntity instanceof WallSignBlockEntity) {
      return super.renderBlockOutline(player, itemStack, worldRenderContext, blockOutlineContext, hand);
    } else {
      return false;
    }
  }

  @Environment(EnvType.CLIENT)
  @Override
  public @Nullable JModel getItemModel() {
    return null;
  }

  @Override
  public @Nullable JRecipe getCraftingRecipe() {
    return new JShapedRecipe(this)
        .pattern(
            "SPS",
            " / ",
            " / "
        )
        .addKey("P", Items.PAPER)
        .addKey("S", Items.SLIME_BALL)
        .addKey("/", Items.STICK)
        .addInventoryChangedCriterion("has_paper", Items.PAPER)
        .addInventoryChangedCriterion("has_slime_ball", Items.SLIME_BALL);
  }
}
