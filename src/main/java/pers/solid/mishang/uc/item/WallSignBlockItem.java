package pers.solid.mishang.uc.item;

import com.google.common.collect.ImmutableList;
import net.minecraft.block.Block;
import net.minecraft.client.item.TooltipType;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.NbtComponent;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtList;
import net.minecraft.nbt.NbtString;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Unmodifiable;
import pers.solid.mishang.uc.blockentity.WallSignBlockEntity;
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

  /**
   * 根据 nbt 数据返回文本内容。
   *
   * @param nbt            物品的 nbt 数据，通常为 {@code BlockEntityTag} 的值。
   * @param registryLookup
   * @return 该 nbt 对应的 {@code List<}{@code TextContext>}。
   */
  protected static @NotNull @Unmodifiable List<TextContext> getTextContextsFromNbt(
      @NotNull NbtCompound nbt, RegistryWrapper.WrapperLookup registryLookup) {
    final NbtElement nbtText = nbt.get("text");
    if (nbtText instanceof NbtString) {
      return ImmutableList.of(TextContext.fromNbt(nbt, WallSignBlockEntity.DEFAULT_TEXT_CONTEXT.clone(), registryLookup));
    } else if (nbtText instanceof NbtCompound) {
      return ImmutableList.of(
          TextContext.fromNbt(nbtText, WallSignBlockEntity.DEFAULT_TEXT_CONTEXT.clone(), registryLookup));
    } else if (nbtText instanceof NbtList) {
      ImmutableList.Builder<TextContext> builder = new ImmutableList.Builder<>();
      for (NbtElement nbtElement : ((NbtList) nbtText)) {
        builder.add(TextContext.fromNbt(nbtElement, WallSignBlockEntity.DEFAULT_TEXT_CONTEXT.clone(), registryLookup));
      }
      return builder.build();
    }
    return ImmutableList.of();
  }

  @Override
  public void appendTooltip(ItemStack stack, TooltipContext context, List<Text> tooltip, TooltipType type) {
    super.appendTooltip(stack, context, tooltip, type);
    final NbtComponent nbtComponent = stack.get(DataComponentTypes.BLOCK_ENTITY_DATA);
    if (nbtComponent == null) return;
    final NbtCompound nbt = nbtComponent.copyNbt();
    if (nbt == null) return;
    final List<MutableText> texts = ImmutableList.copyOf(
        getTextContextsFromNbt(nbt, context.getRegistryLookup()).stream()
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
    final NbtComponent nbtComponent = stack.get(DataComponentTypes.BLOCK_ENTITY_DATA);
    final NbtCompound nbt = nbtComponent == null ? null : nbtComponent.copyNbt();
    if (nbt == null) return super.getName(stack);
    final MutableText text = super.getName(stack).copy();
    final List<MutableText> texts = /*ImmutableList.copyOf(
        getTextContextsFromNbt(nbt, ).stream()
            .map(TextContext::asStyledText)
            .limit(20)
            .iterator());*/List.of(); // todo wrapper lookup?
    if (!texts.isEmpty()) {
      MutableText appendable = TextBridge.empty();
      texts.forEach(t -> appendable.append(" ").append(t));
      text.append(
          TextBridge.literal(" -" + appendable.asTruncatedString(25)).formatted(Formatting.GRAY));
    }
    return text;
  }
}
