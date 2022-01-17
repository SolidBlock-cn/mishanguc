package pers.solid.mishang.uc.block;

import net.minecraft.block.*;
import net.minecraft.fluid.FluidState;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.Properties;
import net.minecraft.util.BlockMirror;
import net.minecraft.util.BlockRotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;
import net.minecraft.world.WorldAccess;
import net.minecraft.world.WorldView;

import java.util.Map;

public class WallLightBlock extends FacingBlock implements Waterloggable {
  private final Map<Direction, VoxelShape> shapePerDirection;

  public WallLightBlock(Settings settings, Map<Direction, VoxelShape> shapePerDirection) {
    super(settings);
    this.shapePerDirection = shapePerDirection;
    this.setDefaultState(
        this.stateManager
            .getDefaultState()
            .with(Properties.WATERLOGGED, false)
            .with(FACING, Direction.UP));
  }

  @SuppressWarnings("deprecation")
  @Override
  public boolean canPlaceAt(BlockState state, WorldView world, BlockPos pos) {
    Direction direction = state.get(FACING).getOpposite();
    BlockPos blockPos = pos.offset(direction);
    return world
        .getBlockState(blockPos)
        .isSideSolidFullSquare(world, blockPos, direction.getOpposite());
  }

  @Override
  protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
    builder.add(FACING, Properties.WATERLOGGED);
  }

  @SuppressWarnings("deprecation")
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

    return super.getStateForNeighborUpdate(
        state, direction, neighborState, world, pos, neighborPos);
  }

  @SuppressWarnings({"deprecation"})
  @Override
  public BlockState rotate(BlockState state, BlockRotation rotation) {
    return state.with(FACING, rotation.rotate(state.get(FACING)));
  }

  @Override
  @SuppressWarnings({"deprecation"})
  public BlockState mirror(BlockState state, BlockMirror mirror) {
    return state.with(FACING, mirror.apply(state.get(FACING)));
  }

  @Override
  public BlockState getPlacementState(ItemPlacementContext ctx) {
    Direction direction = ctx.getSide();
    BlockState blockState =
        ctx.getWorld().getBlockState(ctx.getBlockPos().offset(direction.getOpposite()));
    if (blockState.isOf(this)) {
      blockState.get(FACING);
    }
    return this.getDefaultState()
        .with(FACING, direction)
        .with(
            Properties.WATERLOGGED,
            ctx.getWorld().getBlockState(ctx.getBlockPos()).getFluidState().getFluid()
                == Fluids.WATER);
  }

  @SuppressWarnings("deprecation")
  @Override
  public FluidState getFluidState(BlockState state) {
    return state.get(Properties.WATERLOGGED)
        ? Fluids.WATER.getStill(false)
        : super.getFluidState(state);
  }

  @Override
  @SuppressWarnings({"deprecation"})
  public VoxelShape getOutlineShape(
      BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
    return shapePerDirection.get(state.get(FACING));
  }
}
