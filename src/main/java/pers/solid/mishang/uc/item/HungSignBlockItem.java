package pers.solid.mishang.uc.item;

import com.google.common.collect.ImmutableList;
import net.minecraft.block.Block;
import net.minecraft.client.item.TooltipType;
import net.minecraft.item.ItemStack;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.math.Direction;
import pers.solid.mishang.uc.components.MishangucComponents;
import pers.solid.mishang.uc.text.TextContext;
import pers.solid.mishang.uc.util.TextBridge;

import java.util.List;
import java.util.Map;

/**
 * 类似于一般的方块物品，但是会读取 BlockEntityTag 中的内容来显示文字。
 *
 * @see pers.solid.mishang.uc.blockentity.HungSignBlockEntity#readNbt
 */
public class HungSignBlockItem extends NamedBlockItem {
  public HungSignBlockItem(Block block, Settings settings) {
    super(block, settings);
  }

  @Override
  public void appendTooltip(ItemStack stack, TooltipContext context, List<Text> tooltip, TooltipType type) {
    super.appendTooltip(stack, context, tooltip, type);
    final Map<Direction, List<TextContext>> map = stack.get(MishangucComponents.TEXT_MAP);
    if (map == null) return;
    map.forEach(
        (direction, textContexts) -> {
          tooltip.add(
              TextBridge.translatable("block.mishanguc.tooltip.hung_sign_block", TextBridge.translatable("direction.mishanguc." + direction.asString()))
                  .formatted(Formatting.GRAY));
          textContexts.forEach(
              textContext -> {
                final MutableText mutableText = textContext.asStyledText();
                tooltip.add(mutableText);
              });
        });
  }

  @Override
  public Text getName(ItemStack stack) {
    final MutableText text = super.getName(stack).copy();
    final Map<Direction, List<TextContext>> map = stack.get(MishangucComponents.TEXT_MAP);
    if (map == null) return text;
    final ImmutableList.Builder<Text> appendable = new ImmutableList.Builder<>();
    map.forEach((direction, textContexts) ->
        textContexts.forEach(
            textContext -> {
              final MutableText styledText = textContext.asStyledText();
              appendable.add(styledText);
            }));
    final ImmutableList<Text> build = appendable.build();
    if (!build.isEmpty()) {
      final MutableText appendableText = TextBridge.literal("");
      build.forEach(t -> appendableText.append(" ").append(t));
      text.append(
          TextBridge.literal(" -" + appendableText.asTruncatedString(20)).formatted(Formatting.GRAY));
    }
    return text;
  }
}
