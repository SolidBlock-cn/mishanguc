package pers.solid.mishang.uc.item;

import com.google.common.collect.ImmutableList;
import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import net.minecraft.item.tooltip.TooltipType;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import pers.solid.mishang.uc.components.MishangucComponents;
import pers.solid.mishang.uc.text.TextContext;
import pers.solid.mishang.uc.util.TextBridge;

import java.util.List;

/**
 * 类似于一般的方块物品，但是会读取 BlockEntityTag 中的内容来显示文字。
 *
 * @see pers.solid.mishang.uc.blockentity.WallSignBlockEntity#readNbt
 */
public class WallSignBlockItem extends NamedBlockItem {
  public WallSignBlockItem(Block block, Settings settings) {
    super(block, settings);
  }

  @Override
  public void appendTooltip(ItemStack stack, TooltipContext context, List<Text> tooltip, TooltipType type) {
    super.appendTooltip(stack, context, tooltip, type);
    final List<TextContext> textContexts = stack.get(MishangucComponents.TEXTS);
    if (textContexts == null || textContexts.isEmpty()) return;
    final List<MutableText> texts = ImmutableList.copyOf(
        textContexts.stream()
            .map(TextContext::asStyledText)
            .iterator());
    if (!texts.isEmpty()) {
      tooltip.add(
          TextBridge.translatable("block.mishanguc.tooltip.wall_sign_block")
              .formatted(Formatting.GRAY));
      tooltip.addAll(texts);
    }
  }

  @Override
  public Text getName(ItemStack stack) {
    final List<TextContext> textContexts = stack.get(MishangucComponents.TEXTS);
    if (textContexts == null || textContexts.isEmpty()) return super.getName(stack);
    final MutableText text = super.getName(stack).copy();
    final List<MutableText> texts = ImmutableList.copyOf(
        textContexts.stream()
            .map(TextContext::asStyledText)
            .limit(20)
            .iterator());
    if (!texts.isEmpty()) {
      MutableText appendable = TextBridge.empty();
      texts.forEach(t -> appendable.append(" ").append(t));
      text.append(
          TextBridge.literal(" -" + appendable.asTruncatedString(25)).formatted(Formatting.GRAY));
    }
    return text;
  }
}
