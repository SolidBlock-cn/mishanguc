package pers.solid.mishang.uc.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.util.math.Direction;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * 所有栏杆方块共用的接口。
 */
public interface Handrails extends MishangucBlock {
  /**
   * @return 该楼梯方块的基础方块。如有需要可以为 {@code null}。注意该方法通常直接返回一个字段的值。
   */
  @Contract(pure = true)
  @Nullable
  Block baseBlock();

  /**
   * 该方块状态是否能在特定方向上以特定偏移的方式水平连接。通常用于渲染。
   *
   * @param blockState   方块状态。
   * @param direction    该方块毗邻的方向。通常是水平方向。
   * @param offsetFacing 该方块需要连接时，连接位置的偏移。通常应该是水平方向且与 direction 垂直。若为 {@code null}，则表示不便宜。
   * @return 该方块是否能往旁边连接。
   */
  boolean connectsIn(@NotNull BlockState blockState, @NotNull Direction direction, @Nullable Direction offsetFacing);

  @Override
  default String customRecipeCategory() {
    return "handrails";
  }
}
