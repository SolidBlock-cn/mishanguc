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
  public static final Map<Direction.Axis, VoxelShape> SMALL_SHAPES = ImmutableMap.of(
      Direction.Axis.X, createCuboidShape(0, 5, 5, 16, 11, 11),
      Direction.Axis.Y, createCuboidShape(5, 0, 5, 11, 16, 11),
      Direction.Axis.Z, createCuboidShape(5, 5, 0, 11, 11, 16)
  );
  public static final Map<Direction.Axis, VoxelShape> LARGE_SHAPES = ImmutableMap.of(
      Direction.Axis.X, createCuboidShape(0, 4, 4, 16, 12, 12),
      Direction.Axis.Y, createCuboidShape(4, 0, 4, 12, 16, 12),
      Direction.Axis.Z, createCuboidShape(4, 4, 0, 12, 12, 16)
  );

  public ColumnWallLightBlock(String lightColor, Settings settings, boolean largeShape) {
    super(lightColor, settings, largeShape);
  }

  @Override
  public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
    return (largeShape ? LARGE_SHAPES : SMALL_SHAPES).get(state.get(FACING).getAxis());
  }
}
