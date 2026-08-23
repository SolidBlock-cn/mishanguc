package pers.solid.mishang.uc.item;

import com.google.common.collect.Collections2;
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
import pers.solid.mishang.uc.util.TextBridge;
import pers.solid.mishang.uc.util.WithMishangTooltip;

import java.util.List;
import java.util.stream.Stream;

public class StandingSignBlockItem extends NamedBlockItem implements WithMishangTooltip {
  public StandingSignBlockItem(Block block, Properties settings) {
    super(block, settings);
  }

  @Override
  public void getMishangTooltip(ItemStack stack, TooltipContext context, List<Component> tooltip, TooltipFlag options) {
    final TooltipDisplay displayComponent = stack.getOrDefault(DataComponents.TOOLTIP_DISPLAY, TooltipDisplay.DEFAULT);
    final List<TextContext> frontTexts = stack.getOrDefault(MishangucComponents.FRONT_TEXTS, List.of());
    if (!frontTexts.isEmpty() && displayComponent.shows(MishangucComponents.FRONT_TEXTS)) {
      tooltip.add(TextBridge.translatable("block.mishanguc.tooltip.standing_sign_block_front").withStyle(ChatFormatting.GRAY));
      tooltip.addAll(Collections2.transform(frontTexts, TextContext::asStyledText));
    }
    final List<TextContext> backTexts = stack.getOrDefault(MishangucComponents.BACK_TEXTS, List.of());
    if (!backTexts.isEmpty() && displayComponent.shows(MishangucComponents.BACK_TEXTS)) {
      tooltip.add(TextBridge.translatable("block.mishanguc.tooltip.standing_sign_block_back").withStyle(ChatFormatting.GRAY));
      tooltip.addAll(Collections2.transform(backTexts, TextContext::asStyledText));
    }
  }


  @Override
  public Component getName(ItemStack stack) {
    final MutableComponent text = super.getName(stack).copy();
    final List<TextContext> frontTexts = stack.getOrDefault(MishangucComponents.FRONT_TEXTS, List.of());
    final List<TextContext> backTexts = stack.getOrDefault(MishangucComponents.BACK_TEXTS, List.of());
    final List<MutableComponent> texts = Stream.concat(frontTexts.stream(), backTexts.stream()).map(TextContext::asStyledText).limit(20).toList();
    if (!texts.isEmpty()) {
      MutableComponent appendable = TextBridge.empty();
      texts.forEach(t -> appendable.append(" ").append(t));
      text.append(
          TextBridge.literal(" -" + appendable.getString(25)).withStyle(ChatFormatting.GRAY));
    }
    return text;
  }
}
