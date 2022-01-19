package pers.solid.mishang.uc.block;

import net.minecraft.block.BlockState;
import net.minecraft.util.math.Direction;

/** 适用于墙上的（包括角落的）灯方块的接口，以判断该方块在某个面上的特定方向是否存在连接。 */
public interface LightConnectable {
  /**
   * 判断灯方块在 {@code side} 侧是否往 {@code direction} 方向已有连接，以用于临近方块判断。
   *
   * @param blockState 需要检测的方块状态，必须属于该方块。
   * @param facing 检测方块的该侧。如果该侧不存在方块，则一定返回false。
   * @param direction 该侧的指定方向。
   * @return 布尔值。
   */
  boolean isConnectedIn(BlockState blockState, Direction facing, Direction direction);
}
