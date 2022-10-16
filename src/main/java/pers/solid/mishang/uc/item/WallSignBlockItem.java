package pers.solid.mishang.uc.item;

import com.google.common.collect.ImmutableList;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtList;
import net.minecraft.nbt.NbtString;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.Unmodifiable;
import pers.solid.mishang.uc.blockentity.WallSignBlockEntity;
import pers.solid.mishang.uc.text.TextContext;
import pers.solid.mishang.uc.util.TextBridge;

import java.util.List;

/**
 * 类似于一般的方块物品，但是会读取 BlockEntityTag 中的内容来显示文字。
 *
 * @see pers.solid.mishang.uc.blockentity.WallSignBlockEntity#fromTag(BlockState, NbtCompound)
 */
public class WallSignBlockItem extends NamedBlockItem {
  public WallSignBlockItem(Block block, Settings settings) {
    super(block, settings);
  }

  /**
   * 根据 nbt 数据返回文本内容。
   *
   * @param nbt 物品的 nbt 数据，通常为 {@code BlockEntityTag} 的值。
   * @return 该 nbt 对应的 {@code List<}{@code TextContext>}。
   */
  protected static @NotNull @Unmodifiable List<TextContext> getTextContextsFromNbt(
      @NotNull NbtCompound nbt) {
    final NbtElement nbtText = nbt.get("text");
    if (nbtText instanceof NbtString) {
      return ImmutableList.of(TextContext.fromNbt(nbt, WallSignBlockEntity.DEFAULT_TEXT_CONTEXT.clone()));
    } else if (nbtText instanceof NbtCompound) {
      return ImmutableList.of(
          TextContext.fromNbt(nbtText, WallSignBlockEntity.DEFAULT_TEXT_CONTEXT.clone()));
    } else if (nbtText instanceof NbtList) {
      ImmutableList.Builder<TextContext> builder = new ImmutableList.Builder<>();
      for (NbtElement nbtElement : ((NbtList) nbtText)) {
        builder.add(TextContext.fromNbt(nbtElement, WallSignBlockEntity.DEFAULT_TEXT_CONTEXT.clone()));
      }
      return builder.build();
    }
    return ImmutableList.of();
  }

  @Environment(EnvType.CLIENT)
  @Override
  public void appendTooltip(
      ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
    super.appendTooltip(stack, world, tooltip, context);
    final NbtCompound nbt = stack.getSubTag("BlockEntityTag");
    if (nbt == null) return;
    final List<MutableText> texts = ImmutableList.copyOf(
        getTextContextsFromNbt(nbt).stream()
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
    final NbtCompound nbt = stack.getSubTag("BlockEntityTag");
    if (nbt == null) return super.getName(stack);
    final MutableText text = super.getName(stack).copy();
    final List<MutableText> texts = ImmutableList.copyOf(
        getTextContextsFromNbt(nbt).stream()
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
