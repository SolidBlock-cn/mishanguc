package pers.solid.mishang.uc.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.Properties;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;

import java.util.Map;

public class AutoConnectWallLightBlock extends WallLightBlock implements LightConnectable {

  public AutoConnectWallLightBlock(
      Settings settings, Map<Direction, VoxelShape> shapePerDirection) {
    super(settings, shapePerDirection);
    this.setDefaultState(
        this.stateManager
            .getDefaultState()
            .with(WEST, false)
            .with(EAST, false)
            .with(SOUTH, false)
            .with(NORTH, false)
            .with(UP, false)
            .with(DOWN, false));
  }

  @Override
  protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
    super.appendProperties(builder);
    builder.add(WEST, EAST, SOUTH, NORTH, UP, DOWN);
  }

  @Override
  public BlockState getStateForNeighborUpdate(
      BlockState state,
      Direction direction,
      BlockState neighborState,
      WorldAccess world,
      BlockPos pos,
      BlockPos neighborPos) {
    if (state.get(Properties.WATERLOGGED)) {
      world.getFluidTickScheduler().schedule(pos, Fluids.WATER, Fluids.WATER.getTickRate(world));
    }
    final BlockState newState =
        super.getStateForNeighborUpdate(state, direction, neighborState, world, pos, neighborPos);
    final Direction facing = state.get(FACING);
    final Block neighborBlock = neighborState.getBlock();
    boolean connect = false;

    // 检查该方向上与之直接毗邻的 LightConnectable 方块，如果符合，则修改自身。
    if (neighborBlock instanceof LightConnectable) {
      connect =
          ((LightConnectable) neighborBlock)
              .isConnectedIn(neighborState, facing, direction.getOpposite());
    }

    // 检查该方向上不与之毗邻（与毗邻位置往 facing.getOpposite() 方向偏移一格）的方块。
    final BlockPos neighborPos2 = pos.offset(direction).offset(facing.getOpposite());
    final BlockState neighborState2 = world.getBlockState(neighborPos2);
    final Block neighborBlock2 = neighborState2.getBlock();
    if (neighborBlock2 instanceof LightConnectable) {
      connect =
          connect
              || ((LightConnectable) neighborBlock2)
                  .isConnectedIn(neighborState2, direction, facing);
    }
    return newState.with(DIRECTION_TO_PROPERTY.get(direction), connect);
  }

  @Override
  public BlockState getPlacementState(ItemPlacementContext ctx) {
    BlockState placementState = super.getPlacementState(ctx);
    if (placementState == null) {
      return null;
    }
    final Direction facing = placementState.get(FACING);
    for (Direction direction : Direction.values()) {
      if (direction.getAxis() == facing.getAxis()) {
        continue;
      }
      final BlockPos blockPos = ctx.getBlockPos();
      final World world = ctx.getWorld();
      final BlockPos offsetBlockPos = blockPos.offset(direction);
      placementState =
          getStateForNeighborUpdate(
              placementState,
              direction,
              world.getBlockState(offsetBlockPos),
              world,
              blockPos,
              offsetBlockPos);
    }
    return placementState;
  }

  @Override
  public boolean isConnectedIn(BlockState blockState, Direction facing, Direction direction) {
    return blockState.get(FACING) == facing;
  }
}
