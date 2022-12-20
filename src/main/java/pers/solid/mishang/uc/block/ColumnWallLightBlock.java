package pers.solid.mishang.uc.block;

import com.google.common.collect.ImmutableMap;
import net.minecraft.block.BlockState;
import net.minecraft.block.ShapeContext;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;

import java.util.Map;

/**
 * 柱形灯块。
 */
public class ColumnWallLightBlock extends WallLightBlock {
  private final int sizeType;
  public static final Map<Direction.Axis, VoxelShape> SHAPES7 = createColumnShapes(7);
  public static final Map<Direction.Axis, VoxelShape> SHAPES6 = createColumnShapes(6);
  public static final Map<Direction.Axis, VoxelShape> SHAPES5 = createColumnShapes(5);
  public static final Map<Direction.Axis, VoxelShape> SHAPES4 = createColumnShapes(4);

  public ColumnWallLightBlock(String lightColor, Settings settings, int sizeType) {
    super(lightColor, settings, sizeType == 2);
    this.sizeType = sizeType;
  }

  @Override
  public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
    return (sizeType >= 2 ? SHAPES4 : sizeType == 1 ? SHAPES5 : SHAPES6).get(state.get(FACING).getAxis());
  }

  @SuppressWarnings("deprecation")
  @Override
  public VoxelShape getCollisionShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
    return (sizeType >= 2 ? SHAPES5 : sizeType == 1 ? SHAPES6 : SHAPES7).get(state.get(FACING).getAxis());
  }

  private static Map<Direction.Axis, VoxelShape> createColumnShapes(int min) {
    return ImmutableMap.of(
        Direction.Axis.X, createCuboidShape(0, min, min, 16, 16 - min, 16 - min),
        Direction.Axis.Y, createCuboidShape(min, 0, min, 16 - min, 16, 16 - min),
        Direction.Axis.Z, createCuboidShape(min, min, 0, 16 - min, 16 - min, 16)
    );
  }
}
