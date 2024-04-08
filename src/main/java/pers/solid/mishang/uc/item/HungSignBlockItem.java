package pers.solid.mishang.uc.item;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
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
import net.minecraft.util.math.Direction;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Unmodifiable;
import pers.solid.mishang.uc.blockentity.HungSignBlockEntity;
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

  /**
   * 根据 nbt 数据返回对应的 {@code Map<}{@code Direction, List<}{@code TextContext>>}。
   */
  protected static @Unmodifiable Map<Direction, @Unmodifiable List<TextContext>> getTextContextMapFromNbt(@NotNull NbtCompound nbt, RegistryWrapper.WrapperLookup registryLookup) {
    ImmutableMap.Builder<Direction, List<TextContext>> builder = new ImmutableMap.Builder<>();
    for (Direction direction : Direction.Type.HORIZONTAL) {
      final NbtElement element = nbt.get(direction.asString());
      if (element == null) continue;
      if (element instanceof NbtList) {
        ImmutableList.Builder<TextContext> listBuilder = new ImmutableList.Builder<>();
        for (NbtElement nbtElement : ((NbtList) element)) {
          TextContext textContext = TextContext.fromNbt(nbtElement, HungSignBlockEntity.DEFAULT_TEXT_CONTEXT.clone(), registryLookup);
          listBuilder.add(textContext);
        }
        final ImmutableList<TextContext> build = listBuilder.build();
        if (!build.isEmpty()) builder.put(direction, build);
      } else {
        builder.put(direction, ImmutableList.of(TextContext.fromNbt(element, HungSignBlockEntity.DEFAULT_TEXT_CONTEXT.clone(), registryLookup)));
      }
    }
    return builder.build();
  }

  @Override
  public void appendTooltip(ItemStack stack, TooltipContext context, List<Text> tooltip, TooltipType type) {
    super.appendTooltip(stack, context, tooltip, type);
    final NbtComponent nbtComponent = stack.get(DataComponentTypes.CUSTOM_DATA);
    final NbtCompound nbt = nbtComponent == null ? null : nbtComponent.copyNbt();
    // todo components
    if (nbt == null) return;
    final Map<Direction, List<TextContext>> map =
        getTextContextMapFromNbt(nbt.getCompound("BlockEntityTag"), context.getRegistryLookup());
    map.forEach(
        (direction, textContexts) -> {
          tooltip.add(
              TextBridge.translatable("block.mishanguc.tooltip.hung_sign_block", TextBridge.translatable("direction." + direction.asString()))
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
    final NbtComponent nbtComponent = stack.get(DataComponentTypes.CUSTOM_DATA);
    final NbtCompound nbt = nbtComponent == null ? null : nbtComponent.copyNbt();
    if (nbt == null) return text;
    final ImmutableList.Builder<Text> appendable = new ImmutableList.Builder<>();
    final Map<Direction, List<TextContext>> map = /*getTextContextMapFromNbt(nbt.getCompound("BlockEntityTag"), )*/ Map.of(); // todo use components
    map.forEach(
        (direction, textContexts) ->
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
