package pers.solid.mishang.uc.item;

import com.google.common.collect.Collections2;
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
import java.util.stream.Stream;

public class StandingSignBlockItem extends NamedBlockItem {
  public StandingSignBlockItem(Block block, Settings settings) {
    super(block, settings);
  }

  @Override
  public void appendTooltip(ItemStack stack, TooltipContext context, List<Text> tooltip, TooltipType type) {
    super.appendTooltip(stack, context, tooltip, type);
    final List<TextContext> frontTexts = stack.getOrDefault(MishangucComponents.FRONT_TEXTS, List.of());
    if (!frontTexts.isEmpty()) {
      tooltip.add(TextBridge.translatable("block.mishanguc.tooltip.standing_sign_block_front").formatted(Formatting.GRAY));
      tooltip.addAll(Collections2.transform(frontTexts, TextContext::asStyledText));
    }
    final List<TextContext> backTexts = stack.getOrDefault(MishangucComponents.BACK_TEXTS, List.of());
    if (!backTexts.isEmpty()) {
      tooltip.add(TextBridge.translatable("block.mishanguc.tooltip.standing_sign_block_back").formatted(Formatting.GRAY));
      tooltip.addAll(Collections2.transform(backTexts, TextContext::asStyledText));
    }
  }


  @Override
  public Text getName(ItemStack stack) {
    final MutableText text = super.getName(stack).copy();
    final List<TextContext> frontTexts = stack.getOrDefault(MishangucComponents.FRONT_TEXTS, List.of());
    final List<TextContext> backTexts = stack.getOrDefault(MishangucComponents.BACK_TEXTS, List.of());
    final List<MutableText> texts = Stream.concat(frontTexts.stream(), backTexts.stream()).map(TextContext::asStyledText).limit(20).toList();
    if (!texts.isEmpty()) {
      MutableText appendable = TextBridge.empty();
      texts.forEach(t -> appendable.append(" ").append(t));
      text.append(
          TextBridge.literal(" -" + appendable.asTruncatedString(25)).formatted(Formatting.GRAY));
    }
    return text;
  }
}
