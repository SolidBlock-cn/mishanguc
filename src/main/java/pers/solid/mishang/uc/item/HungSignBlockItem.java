package pers.solid.mishang.uc.item;

import com.google.common.collect.ImmutableList;
import net.minecraft.ChatFormatting;
import net.minecraft.core.Direction;
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
import java.util.Map;

/**
 * 类似于一般的方块物品，但是会读取 BlockEntityTag 中的内容来显示文字。
 *
 * @see pers.solid.mishang.uc.blockentity.HungSignBlockEntity#loadAdditional
 */
public class HungSignBlockItem extends NamedBlockItem implements WithMishangTooltip {
  public HungSignBlockItem(Block block, Properties settings) {
    super(block, settings);
  }

  @Override
  public void getMishangTooltip(ItemStack stack, TooltipContext context, List<Component> tooltip, TooltipFlag options) {
    final Map<Direction, List<TextContext>> map = stack.get(MishangucComponents.TEXT_MAP);
    if (map == null || !stack.getOrDefault(DataComponents.TOOLTIP_DISPLAY, TooltipDisplay.DEFAULT).shows(MishangucComponents.TEXT_MAP)) return;
    map.forEach(
        (direction, textContexts) -> {
          tooltip.add(
              Component.translatable("block.mishanguc.tooltip.hung_sign_block", Component.translatable("direction.mishanguc." + direction.getSerializedName()))
                  .withStyle(ChatFormatting.GRAY));
          textContexts.forEach(
              textContext -> {
                final MutableComponent mutableText = textContext.asStyledText();
                tooltip.add(mutableText);
              });
        });
  }

  @Override
  public Component getName(ItemStack stack) {
    final MutableComponent text = super.getName(stack).copy();
    final Map<Direction, List<TextContext>> map = stack.get(MishangucComponents.TEXT_MAP);
    if (map == null) return text;
    final ImmutableList.Builder<Component> appendable = new ImmutableList.Builder<>();
    map.forEach((direction, textContexts) ->
        textContexts.forEach(
            textContext -> {
              final MutableComponent styledText = textContext.asStyledText();
              appendable.add(styledText);
            }));
    final ImmutableList<Component> build = appendable.build();
    if (!build.isEmpty()) {
      final MutableComponent appendableText = Component.literal("");
      build.forEach(t -> appendableText.append(" ").append(t));
      text.append(
          Component.literal(" -" + appendableText.getString(20)).withStyle(ChatFormatting.GRAY));
    }
    return text;
  }
}
