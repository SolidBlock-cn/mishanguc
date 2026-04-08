package pers.solid.mishang.uc.item;

import com.google.common.collect.ImmutableList;
import net.minecraft.ChatFormatting;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.TooltipDisplay;
import net.minecraft.world.level.block.Block;
import pers.solid.mishang.uc.components.MishangucComponents;
import pers.solid.mishang.uc.text.TextContext;
import pers.solid.mishang.uc.util.WithMishangTooltip;

import java.util.List;

/**
 * 类似于一般的方块物品，但是会读取 BlockEntityTag 中的内容来显示文字。
 *
 * @see pers.solid.mishang.uc.blockentity.WallSignBlockEntity#loadAdditional
 */
public class WallSignBlockItem extends NamedBlockItem implements WithMishangTooltip {
  public WallSignBlockItem(Block block, Properties settings) {
    super(block, settings);
  }

  @Override
  public void getMishangTooltip(ItemStack stack, TooltipContext context, List<Component> tooltip, TooltipFlag options) {
    final TooltipDisplay displayComponent = stack.getOrDefault(DataComponents.TOOLTIP_DISPLAY, TooltipDisplay.DEFAULT);
    if (!displayComponent.shows(MishangucComponents.TEXTS)) return;
    final List<TextContext> textContexts = stack.get(MishangucComponents.TEXTS);
    if (textContexts == null || textContexts.isEmpty()) return;
    final List<MutableComponent> texts = ImmutableList.copyOf(
        textContexts.stream()
            .map(TextContext::asStyledText)
            .iterator());
    if (!texts.isEmpty()) {
      tooltip.add(
          Component.translatable("block.mishanguc.tooltip.wall_sign_block")
              .withStyle(ChatFormatting.GRAY));
      tooltip.addAll(texts);
    }
  }

  @Override
  public Component getName(ItemStack stack) {
    final List<TextContext> textContexts = stack.get(MishangucComponents.TEXTS);
    if (textContexts == null || textContexts.isEmpty()) return super.getName(stack);
    final MutableComponent text = super.getName(stack).copy();
    final List<MutableComponent> texts = ImmutableList.copyOf(
        textContexts.stream()
            .map(TextContext::asStyledText)
            .limit(20)
            .iterator());
    if (!texts.isEmpty()) {
      MutableComponent appendable = Component.empty();
      texts.forEach(t -> appendable.append(" ").append(t));
      text.append(
          Component.literal(" -" + appendable.getString(25)).withStyle(ChatFormatting.GRAY));
    }
    return text;
  }
}
