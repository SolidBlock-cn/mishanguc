package pers.solid.mishang.uc.item;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import net.minecraft.block.Block;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtList;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.Unmodifiable;
import pers.solid.mishang.uc.blockentity.HungSignBlockEntity;
import pers.solid.mishang.uc.text.TextContext;
import pers.solid.mishang.uc.util.TextBridge;

import java.util.List;
import java.util.Map;

/**
 * 类似于一般的方块物品，但是会读取 BlockEntityTag 中的内容来显示文字。
 *
 * @see pers.solid.mishang.uc.blockentity.HungSignBlockEntity#readNbt(NbtCompound)
 */
public class HungSignBlockItem extends NamedBlockItem {
  public HungSignBlockItem(Block block, Settings settings) {
    super(block, settings);
  }

  /**
   * 根据 nbt 数据返回对应的 {@code Map<}{@code Direction, List<}{@code TextContext>>}。
   */
  protected static @Unmodifiable Map<Direction, @Unmodifiable List<TextContext>>
  getTextContextMapFromNbt(@NotNull NbtCompound nbt) {
    ImmutableMap.Builder<Direction, List<TextContext>> builder = new ImmutableMap.Builder<>();
    for (Direction direction : Direction.Type.HORIZONTAL) {
      final NbtElement element = nbt.get(direction.asString());
      if (element == null) continue;
      if (element instanceof NbtList) {
        ImmutableList.Builder<TextContext> listBuilder = new ImmutableList.Builder<>();
        for (NbtElement nbtElement : ((NbtList) element)) {
          TextContext textContext = TextContext.fromNbt(nbtElement, HungSignBlockEntity.DEFAULT_TEXT_CONTEXT.clone());
          listBuilder.add(textContext);
        }
        final ImmutableList<TextContext> build = listBuilder.build();
        if (!build.isEmpty()) builder.put(direction, build);
      } else {
        builder.put(
            direction,
            ImmutableList.of(
                TextContext.fromNbt(element, HungSignBlockEntity.DEFAULT_TEXT_CONTEXT.clone())));
      }
    }
    return builder.build();
  }

  @Override
  public void appendTooltip(
      ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
    super.appendTooltip(stack, world, tooltip, context);
    final NbtCompound nbt = stack.getNbt();
    if (nbt == null) return;
    final Map<Direction, List<TextContext>> map =
        getTextContextMapFromNbt(nbt.getCompound("BlockEntityTag"));
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
    final NbtCompound nbt = stack.getNbt();
    if (nbt == null) return text;
    final ImmutableList.Builder<Text> appendable = new ImmutableList.Builder<>();
    final Map<Direction, List<TextContext>> map =
        getTextContextMapFromNbt(nbt.getCompound("BlockEntityTag"));
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
