package pers.solid.mishang.uc.item;

import com.google.common.collect.Collections2;
import net.minecraft.block.Block;
import net.minecraft.client.item.TooltipType;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.NbtComponent;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtList;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import pers.solid.mishang.uc.blockentity.StandingSignBlockEntity;
import pers.solid.mishang.uc.text.TextContext;
import pers.solid.mishang.uc.util.TextBridge;

import java.util.List;
import java.util.stream.Stream;

public class StandingSignBlockItem extends NamedBlockItem {
  public StandingSignBlockItem(Block block, Settings settings) {
    super(block, settings);
  }

  protected static @NotNull Stream<TextContext> getTextContextsFromNbt(@Nullable NbtElement nbt, RegistryWrapper.WrapperLookup registryLookup) {
    if (nbt == null) return Stream.empty();
    else if (nbt instanceof NbtList nbtList) {
      return nbtList.stream().map(nbt1 -> TextContext.fromNbt(nbt1, StandingSignBlockEntity.DEFAULT_TEXT_CONTEXT.clone(), registryLookup));
    } else {
      return Stream.of(TextContext.fromNbt(nbt, StandingSignBlockEntity.DEFAULT_TEXT_CONTEXT.clone(), registryLookup));
    }
  }

  @Override
  public void appendTooltip(ItemStack stack, TooltipContext context, List<Text> tooltip, TooltipType type) {
    super.appendTooltip(stack, context, tooltip, type);
    final NbtComponent nbtComponent = stack.get(DataComponentTypes.BLOCK_ENTITY_DATA);
    final NbtCompound nbt = nbtComponent == null ? null : nbtComponent.copyNbt();
    if (nbt == null) return;
    final List<TextContext> frontTexts = getTextContextsFromNbt(nbt.get("frontTexts"), context.getRegistryLookup()).toList();
    if (!frontTexts.isEmpty()) {
      tooltip.add(TextBridge.translatable("block.mishanguc.tooltip.standing_sign_block_front").formatted(Formatting.GRAY));
      tooltip.addAll(Collections2.transform(frontTexts, TextContext::asStyledText));
    }
    final List<TextContext> backTexts = getTextContextsFromNbt(nbt.get("backTexts"), context.getRegistryLookup()).toList();
    if (!backTexts.isEmpty()) {
      tooltip.add(TextBridge.translatable("block.mishanguc.tooltip.standing_sign_block_back").formatted(Formatting.GRAY));
      tooltip.addAll(Collections2.transform(backTexts, TextContext::asStyledText));
    }
  }


  @Override
  public Text getName(ItemStack stack) {
    final NbtComponent nbtComponent = stack.get(DataComponentTypes.BLOCK_ENTITY_DATA);
    final NbtCompound nbt = nbtComponent == null ? null : nbtComponent.copyNbt();
    if (nbt == null) return super.getName(stack);
    final MutableText text = super.getName(stack).copy();
    final List<MutableText> texts = /*Stream.concat(getTextContextsFromNbt(nbt.get("frontTexts"), ), getTextContextsFromNbt(nbt.get("backTexts"), )).map(TextContext::asStyledText).limit(20).toList();*/ List.of(); // todo complete
    if (!texts.isEmpty()) {
      MutableText appendable = TextBridge.empty();
      texts.forEach(t -> appendable.append(" ").append(t));
      text.append(
          TextBridge.literal(" -" + appendable.asTruncatedString(25)).formatted(Formatting.GRAY));
    }
    return text;
  }
}
