package pers.solid.mishang.uc.item;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.rendering.v1.world.WorldExtractionContext;
import net.minecraft.ChatFormatting;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.state.BlockOutlineRenderState;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.component.DataComponents;
import net.minecraft.data.recipes.RecipeBuilder;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.network.chat.*;
import net.minecraft.network.chat.contents.PlainTextContents;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.TooltipDisplay;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.SignBlockEntity;
import net.minecraft.world.level.block.entity.SignText;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.Unmodifiable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pers.solid.mishang.uc.MishangUtils;
import pers.solid.mishang.uc.block.HungSignBlock;
import pers.solid.mishang.uc.block.StandingSignBlock;
import pers.solid.mishang.uc.blockentity.HungSignBlockEntity;
import pers.solid.mishang.uc.blockentity.StandingSignBlockEntity;
import pers.solid.mishang.uc.blockentity.WallSignBlockEntity;
import pers.solid.mishang.uc.components.MishangucComponents;
import pers.solid.mishang.uc.components.TextCopyToolComponent;
import pers.solid.mishang.uc.render.state.MishangRenderState;
import pers.solid.mishang.uc.text.TextContext;
import pers.solid.mishang.uc.util.RoadConnectionState;
import pers.solid.mishang.uc.util.TextBridge;
import pers.solid.mishang.uc.util.WithMishangTooltip;

import java.util.*;

/**
 * 用于复制粘贴文本的工具。持有该工具，“攻击”（默认左键）告示牌（含原版告示牌、悬挂告示牌和墙上的告示牌）可以将文本复制到物品中，"使用"（默认右键）告示牌可将文本粘贴上去。
 */
public class TextCopyToolItem extends BlockToolItem implements MishangucItem, WithMishangTooltip {
  // 1.18.1 之前用 apache 的 Logger，自 1.18.2 用 slf4j 的 Logger。
  public static final Logger LOGGER = LoggerFactory.getLogger(TextCopyToolItem.class);

  public TextCopyToolItem(Properties settings, @Nullable Boolean includesFluid) {
    super(settings, includesFluid);
  }

  @Override
  public void getMishangTooltip(ItemStack stack, TooltipContext context, List<Component> tooltip, TooltipFlag options) {
    tooltip.add(TextBridge.translatable("item.mishanguc.text_copy_tool.tooltip.1", TextBridge.keybind("key.attack").withStyle(style -> style.withColor(0xdddddd))).withStyle(ChatFormatting.GRAY));
    tooltip.add(TextBridge.translatable("item.mishanguc.text_copy_tool.tooltip.2", TextBridge.keybind("key.use").withStyle(style -> style.withColor(0xdddddd))).withStyle(ChatFormatting.GRAY));

    final List<TextContext> texts = stack.get(MishangucComponents.TEXTS);

    if (texts != null && !texts.isEmpty() && stack.getOrDefault(DataComponents.TOOLTIP_DISPLAY, TooltipDisplay.DEFAULT).shows(MishangucComponents.TEXTS)) {
      tooltip.add(TextBridge.translatable("item.mishanguc.text_copy_tool.tooltip.3").withStyle(ChatFormatting.GRAY));
      texts.stream().map(TextContext::asStyledText).peek(text -> {
        final TextColor color = text.getStyle().getColor();
        if (color != null && color.getValue() == 0) {
          // 考虑黑色的文本看不清楚，因此这种情况依然显示为灰色。
          text.withStyle(ChatFormatting.GRAY);
        }
      }).forEach(tooltip::add);
    }
  }

  @Override
  public Component getName(ItemStack stack) {
    final List<TextContext> textContexts = stack.get(MishangucComponents.TEXTS);
    if (textContexts == null || textContexts.isEmpty()) {
      return super.getName(stack);
    }
    final MutableComponent text = super.getName(stack).copy();
    final List<MutableComponent> texts = ImmutableList.copyOf(
        textContexts.stream()
            .map(TextContext::asStyledText)
            .iterator());
    if (!texts.isEmpty()) {
      MutableComponent appendable = TextBridge.empty();
      texts.forEach(t -> appendable.append(" ").append(t));
      text.append(
          TextBridge.literal(" -" + appendable.getString(25)).withStyle(ChatFormatting.GRAY));
    }
    return text;
  }

  @Override
  public InteractionResult useOnBlock(ItemStack stack, Player player, Level world, BlockHitResult blockHitResult, InteractionHand hand, boolean fluidIncluded) {
    final BlockPos blockPos = blockHitResult.getBlockPos();
    final BlockState blockState = world.getBlockState(blockPos);
    final BlockEntity blockEntity = world.getBlockEntity(blockPos);
    final List<TextContext> textContexts = stack.get(MishangucComponents.TEXTS);
    if (textContexts == null) {
      player.displayClientMessage(TextBridge.translatable("item.mishanguc.text_copy_tool.message.fail.null_tag", TextBridge.keybind("key.attack").withStyle(Style.EMPTY.withColor(0xdeb305))).withStyle(ChatFormatting.RED), true);
      return InteractionResult.FAIL;
    }
    try {
      if (blockEntity instanceof SignBlockEntity signBlockEntity) {
        if (world.isClientSide()) return InteractionResult.SUCCESS;
        final SignText textFacing = signBlockEntity.isFacingFrontText(player) ? signBlockEntity.getFrontText() : signBlockEntity.getBackText();
        final Component[] messagesUnfiltered = textFacing.getMessages(false);
        Arrays.fill(messagesUnfiltered, CommonComponents.EMPTY);
        @Nullable DyeColor color = null;
        for (int i = 0; i < textContexts.size(); i++) {
          final TextContext textContext = textContexts.get(i);
          final MutableComponent styledText = textContext.asStyledText();
          if (i < messagesUnfiltered.length) {
            // 设置告示牌文字
            messagesUnfiltered[i] = styledText;

            // 设置告示牌颜色
            final DyeColor possibleColor = MishangUtils.colorBySignColor(textContext.color);
            if (possibleColor != null) {
              if (color == null) {
                color = possibleColor;
              }
            }
          } else {
            player.displayClientMessage(TextBridge.translatable("item.mishanguc.text_copy_tool.message.warn.outOfBound", styledText, messagesUnfiltered.length).withStyle(ChatFormatting.YELLOW), false);
          }
        }
        signBlockEntity.setText(color == null ? textFacing : textFacing.setColor(color), signBlockEntity.isFacingFrontText(player));
        blockEntity.setChanged();
        world.sendBlockUpdated(blockPos, blockState, blockState, 3);
        player.displayClientMessage(TextBridge.translatable("item.mishanguc.text_copy_tool.message.success.paste", Math.min(textContexts.size(), 4)), true);
        stack.hurtAndBreak(1, player, hand.asEquipmentSlot());
        return InteractionResult.SUCCESS;
      } else if (blockEntity instanceof WallSignBlockEntity wallSignBlockEntity) {
        if (world.isClientSide()) return InteractionResult.SUCCESS;
        wallSignBlockEntity.textContexts = ImmutableList.copyOf(textContexts);
        if (stack.getOrDefault(MishangucComponents.TEXT_COPY_TOOL_PROPERTIES, TextCopyToolComponent.DEFAULT).fromVanillaSign()) {
          MishangUtils.rearrange(wallSignBlockEntity.textContexts);
        }
        blockEntity.setChanged();
        world.sendBlockUpdated(blockPos, blockState, blockState, 3);
        player.displayClientMessage(TextBridge.translatable("item.mishanguc.text_copy_tool.message.success.paste", wallSignBlockEntity.textContexts.size()), true);
        stack.hurtAndBreak(1, player, hand.asEquipmentSlot());
        return InteractionResult.SUCCESS;
      } else if (blockEntity instanceof HungSignBlockEntity hungSignBlockEntity) {
        if (world.isClientSide())
          return InteractionResult.SUCCESS;
        final Direction hitSide = blockHitResult.getDirection();
        final Direction.Axis axis = blockState.getValue(HungSignBlock.AXIS);
        if (!axis.test(hitSide)) {
          final Iterator<Direction> validDirections = Arrays.stream(Direction.values()).filter(axis).iterator();
          // 如果点击的方向不正确，则无法复制和粘贴文本。
          player.displayClientMessage(TextBridge.translatable("item.mishanguc.text_copy_tool.message.fail.wrong_side", RoadConnectionState.text(hitSide).withStyle(style -> style.withColor(0xeecc44)), RoadConnectionState.text(validDirections.next()).withStyle(style -> style.withColor(0xb3ee45)), RoadConnectionState.text(validDirections.next()).withStyle(style -> style.withColor(0xb3ee45))).withStyle(ChatFormatting.RED), true);
          return InteractionResult.FAIL;
        }
        final HashMap<Direction, @Unmodifiable List<TextContext>> newTexts = new HashMap<>(hungSignBlockEntity.texts);
        final ImmutableList<TextContext> newTextsThisSide = ImmutableList.copyOf(textContexts);
        if (stack.getOrDefault(MishangucComponents.TEXT_COPY_TOOL_PROPERTIES, TextCopyToolComponent.DEFAULT).fromVanillaSign()) {
          MishangUtils.rearrange(newTextsThisSide);
        }
        if (newTextsThisSide.isEmpty()) {
          newTexts.remove(hitSide);
        } else {
          newTexts.put(hitSide, newTextsThisSide);
        }
        hungSignBlockEntity.texts = ImmutableMap.copyOf(newTexts);
        blockEntity.setChanged();
        world.sendBlockUpdated(blockPos, blockState, blockState, 3);
        player.displayClientMessage(TextBridge.translatable("item.mishanguc.text_copy_tool.message.success.paste", newTextsThisSide.size()), true);
        stack.hurtAndBreak(1, player, hand.asEquipmentSlot());
        return InteractionResult.SUCCESS;
      } else if (blockEntity instanceof StandingSignBlockEntity standingSignBlockEntity) {
        if (world.isClientSide())
          return InteractionResult.SUCCESS;
        final Boolean isFront = StandingSignBlock.getHitSide(blockState, blockHitResult);
        if (isFront != null) {
          standingSignBlockEntity.setTextsOnSide(isFront, textContexts);
          if (stack.getOrDefault(MishangucComponents.TEXT_COPY_TOOL_PROPERTIES, TextCopyToolComponent.DEFAULT).fromVanillaSign()) {
            MishangUtils.rearrange(standingSignBlockEntity.getTextsOnSide(isFront));
          }
          blockEntity.setChanged();
          world.sendBlockUpdated(blockPos, blockState, blockState, 3);
          player.displayClientMessage(TextBridge.translatable("item.mishanguc.text_copy_tool.message.success.paste", standingSignBlockEntity.getTextsOnSide(isFront).size()), true);
          stack.hurtAndBreak(1, player, hand.asEquipmentSlot());
          return InteractionResult.SUCCESS;
        }
      } else {
        // 点击的方块不是可以识别的告示牌方块。
        player.displayClientMessage(TextBridge.translatable("item.mishanguc.text_copy_tool.message.fail.not_sign").withStyle(ChatFormatting.RED), true);
        return InteractionResult.FAIL;
      }
    } catch (Throwable throwable) {
      player.displayClientMessage(TextBridge.translatable("item.mishanguc.text_copy_tool.message.fail.unexpected").withStyle(ChatFormatting.RED), true);
      LOGGER.error("Unexpected error found when pasting text", throwable);
    }
    return InteractionResult.PASS;
  }

  /**
   * 持有该物品，左键（攻击，默认为左键）点击告示牌可复制其文字。如果被点击的告示牌不是文字，则不产生效果。若点击悬挂的告示牌，则只会复制其中一边的文字。
   */
  @Override
  public InteractionResult beginAttackBlock(ItemStack stack, Player player, Level world, InteractionHand hand, BlockPos pos, Direction direction, boolean fluidIncluded) {
    // 本方法仅限在服务器上使用。
    final BlockEntity blockEntity = world.getBlockEntity(pos);
    if (blockEntity instanceof SignBlockEntity signBlockEntity) {
      if (world.isClientSide()) {
        return InteractionResult.SUCCESS;
      }
      // 原版的告示牌
      final List<TextContext> textContexts = new ArrayList<>();
      final SignText textFacing = signBlockEntity.isFacingFrontText(player) ? signBlockEntity.getFrontText() : signBlockEntity.getBackText();
      for (int i = 0; i < 4; i++) {
        final TextContext textContext = new TextContext();
        textContext.text = textFacing.getMessage(i, false).copy();
        if (TextBridge.isEmpty(textContext.text)) {
          continue;
        }
        textContext.color = textFacing.getColor().getTextColor();

        final Style style = textContext.text.getStyle();
        if (textContext.text.getContents() instanceof PlainTextContents && textContext.text.getSiblings().isEmpty() && style.getClickEvent() == null && style.getHoverEvent() == null && style.getFont() == null && style.getInsertion() == null) {
          // 对于文本为 literalText 的情况，应该将其 style 对象中的属性转化为 textContent 中的属性，除非 style 中有无法转换的部分。
          textContext.bold = style.isBold();
          textContext.italic = style.isItalic();
          textContext.strikethrough = style.isStrikethrough();
          textContext.underline = style.isUnderlined();
          textContext.obfuscated = style.isObfuscated();
          if (style.getColor() != null) {
            textContext.color = style.getColor().getValue();
          }
          textContext.text = TextBridge.literal(((PlainTextContents) textContext.text.getContents()).text());
        }
        textContexts.add(textContext);
      }
      stack.set(MishangucComponents.TEXTS, textContexts);
      stack.set(MishangucComponents.TEXT_COPY_TOOL_PROPERTIES, new TextCopyToolComponent(true));
      player.displayClientMessage(TextBridge.translatable("item.mishanguc.text_copy_tool.message.success.copy", textContexts.size()), true);
      return InteractionResult.SUCCESS;
    } else if (blockEntity instanceof WallSignBlockEntity wallSignBlockEntity) {
      if (world.isClientSide()) {
        return InteractionResult.SUCCESS;
      }
      // 迷上城建模组的墙上告示牌方块
      final ImmutableList<TextContext> textContexts = ImmutableList.copyOf(wallSignBlockEntity.textContexts);
      stack.set(MishangucComponents.TEXTS, textContexts);
      stack.set(MishangucComponents.TEXT_COPY_TOOL_PROPERTIES, new TextCopyToolComponent(false));
      player.displayClientMessage(TextBridge.translatable("item.mishanguc.text_copy_tool.message.success.copy", textContexts.size()), true);
      return InteractionResult.SUCCESS;
    } else {
      final BlockState blockState = world.getBlockState(pos);
      if (blockEntity instanceof HungSignBlockEntity hungSignBlockEntity) {
        if (world.isClientSide()) {
          return InteractionResult.SUCCESS;
        }
        final Direction.Axis axis = blockState.getValue(HungSignBlock.AXIS);
        if (!axis.test(direction)) {
          final Iterator<Direction> validDirections = Arrays.stream(Direction.values()).filter(axis).iterator();
          // 如果点击的方向不正确，则无法复制和粘贴文本。
          player.displayClientMessage(TextBridge.translatable("item.mishanguc.text_copy_tool.message.fail.wrong_side", RoadConnectionState.text(direction).withStyle(style -> style.withColor(0xeecc44)), RoadConnectionState.text(validDirections.next()).withStyle(style -> style.withColor(0xb3ee45)), RoadConnectionState.text(validDirections.next()).withStyle(style -> style.withColor(0xb3ee45))).withStyle(ChatFormatting.RED), true);
          return InteractionResult.FAIL;
        }
        final ImmutableList<TextContext> textContexts = ImmutableList.copyOf(hungSignBlockEntity.texts.getOrDefault(direction, ImmutableList.of()));
        stack.set(MishangucComponents.TEXTS, textContexts);
        stack.set(MishangucComponents.TEXT_COPY_TOOL_PROPERTIES, new TextCopyToolComponent(false));
        player.displayClientMessage(TextBridge.translatable("item.mishanguc.text_copy_tool.message.success.copy", textContexts.size()), true);
        return InteractionResult.SUCCESS;
      } else if (blockEntity instanceof StandingSignBlockEntity standingSignBlockEntity) {
        Boolean hitSide = StandingSignBlock.getHitSide(blockState, direction);
        if (hitSide == null) {
          final HitResult raycast0 = player.pick(4.5, 0, includesFluid(stack, false));
          if (raycast0 instanceof BlockHitResult)
            hitSide = StandingSignBlock.getHitSide(blockState, (BlockHitResult) raycast0);
        }
        if (hitSide == null) {
          return world.isClientSide() ? InteractionResult.PASS : InteractionResult.FAIL;
        }
        final ImmutableList<TextContext> textContexts = ImmutableList.copyOf(standingSignBlockEntity.getTextsOnSide(hitSide));
        stack.set(MishangucComponents.TEXTS, textContexts);
        stack.set(MishangucComponents.TEXT_COPY_TOOL_PROPERTIES, new TextCopyToolComponent(false));
        player.displayClientMessage(TextBridge.translatable("item.mishanguc.text_copy_tool.message.success.copy", textContexts.size()), true);
        return InteractionResult.SUCCESS;
      } else {
        if (world.isClientSide()) {
          return InteractionResult.SUCCESS;
        }
        // 点击的方块不是可以识别的告示牌方块。
        player.displayClientMessage(TextBridge.translatable("item.mishanguc.text_copy_tool.message.fail.not_sign").withStyle(ChatFormatting.RED), true);
        return InteractionResult.FAIL;
      }
    }
  }

  @Environment(EnvType.CLIENT)
  @Override
  public @Nullable MishangRenderState getMishangRenderState(LocalPlayer player, InteractionHand hand, ItemStack stack, WorldExtractionContext context, @Nullable HitResult result) {
    final BlockOutlineRenderState outlineRenderState = context.worldState().blockOutlineRenderState;
    if (outlineRenderState == null) return null;

    final BlockEntity blockEntity = player.level().getBlockEntity(outlineRenderState.pos());
    if (blockEntity instanceof SignBlockEntity || blockEntity instanceof HungSignBlockEntity || blockEntity instanceof WallSignBlockEntity || blockEntity instanceof StandingSignBlockEntity) {
      return super.getMishangRenderState(player, hand, stack, context, result);
    } else {
      return null;
    }
  }

  @Override
  public RecipeBuilder getCraftingRecipe(RecipeProvider recipeGenerator) {
    return recipeGenerator.shaped(RecipeCategory.TOOLS, this)
        .pattern("SPS")
        .pattern(" / ")
        .pattern(" / ")
        .define('P', Items.PAPER)
        .define('S', Items.SLIME_BALL)
        .define('/', Items.STICK)
        .unlockedBy("has_paper", recipeGenerator.has(Items.PAPER))
        .unlockedBy("has_slime_ball", recipeGenerator.has(Items.SLIME_BALL));
  }
}
