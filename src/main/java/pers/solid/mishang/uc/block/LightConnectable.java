package pers.solid.mishang.uc.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.WorldAccess;

/**
 * 适用于墙上的（包括角落的）灯方块的接口，以判断该方块在某个面上的特定方向是否存在连接。
 */
public interface LightConnectable {
  /**
   * 判断灯方块在 {@code side} 侧是否往 {@code direction} 方向已有连接，以用于临近方块判断。
   *
   * @param blockState 需要检测的方块状态，必须属于该方块。
   * @param facing     检测方块的该侧。如果该侧不存在方块，则一定返回false。
   * @param direction  该侧的指定方向。
   * @return 布尔值。
   */
  boolean isConnectedIn(BlockState blockState, Direction facing, Direction direction);

  /**
   * 放置方块后，将与之不毗邻但可能产生连接的 {@link AutoConnectWallLightBlock} 进行连接。该方法将会由 {@link Block#prepare} 调用。
   *
   * @see WallLightBlock#prepare(BlockState, WorldAccess, BlockPos, int, int)
   * @see CornerLightBlock#prepare(BlockState, WorldAccess, BlockPos, int, int)
   */
  @SuppressWarnings({"AlibabaAbstractMethodOrInterfaceMethodMustUseJavadoc", "deprecation"})
  default void prepareConnection(
      BlockState state,
      WorldAccess world,
      BlockPos pos,
      int flags,
      int maxUpdateDepth,
      Direction facing) {
    if (state.getBlock() instanceof LightConnectable) {
      for (Direction direction : Direction.values()) {
        if (((LightConnectable) state.getBlock()).isConnectedIn(state, facing, direction)) {
          final BlockPos neighborPos2 = pos.offset(direction).offset(facing.getOpposite());
          final BlockState neighborState2 = world.getBlockState(neighborPos2);
          if (neighborState2.getBlock() instanceof AutoConnectWallLightBlock) {
            Block.replace(
                neighborState2,
                neighborState2.getStateForNeighborUpdate(
                    facing,
                    world.getBlockState(neighborPos2.offset(facing)),
                    world,
                    neighborPos2,
                    pos),
                world,
                neighborPos2,
                flags,
                maxUpdateDepth);
          }
        }
      }
    }
  }
}
