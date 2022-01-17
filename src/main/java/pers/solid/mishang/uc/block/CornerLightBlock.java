package pers.solid.mishang.uc.block;

import net.minecraft.block.*;
import net.minecraft.block.enums.BlockHalf;
import net.minecraft.fluid.FluidState;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.state.property.EnumProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;
import net.minecraft.world.WorldAccess;
import net.minecraft.world.WorldView;
import org.jetbrains.annotations.Nullable;

import java.util.Map;

import static net.minecraft.fluid.Fluids.WATER;

public class CornerLightBlock extends HorizontalFacingBlock implements Waterloggable {
  private static final EnumProperty<BlockHalf> BLOCK_HALF = Properties.BLOCK_HALF;
  private static final BooleanProperty WATERLOGGED = Properties.WATERLOGGED;
  private final Map<Direction, VoxelShape> directionToShapeWhenBottom;
  private final Map<Direction, VoxelShape> directionToShapeWhenTop;

  protected CornerLightBlock(
      Settings settings,
      Map<Direction, VoxelShape> directionToShapeWhenBottom,
      Map<Direction, VoxelShape> directionToShapeWhenTop) {
    super(settings);
    this.directionToShapeWhenBottom = directionToShapeWhenBottom;
    this.directionToShapeWhenTop = directionToShapeWhenTop;
    this.setDefaultState(
        this.stateManager
            .getDefaultState()
            .with(WATERLOGGED, false)
            .with(BLOCK_HALF, BlockHalf.BOTTOM));
  }

  @Nullable
  @Override
  public BlockState getPlacementState(ItemPlacementContext ctx) {
    final BlockState placementState = super.getPlacementState(ctx);
    if (placementState == null) {
      return null;
    }
    final Direction side = ctx.getSide();
    return placementState
        .with(WATERLOGGED, ctx.getWorld().getFluidState(ctx.getBlockPos()).getFluid() == WATER)
        .with(
            BLOCK_HALF,
            side == Direction.DOWN || ctx.getHitPos().y - ctx.getBlockPos().getY() > 0.5
                ? BlockHalf.TOP
                : BlockHalf.BOTTOM)
        .with(
            FACING,
            Direction.Type.HORIZONTAL.test(side) ? side : ctx.getPlayerFacing().getOpposite());
  }

  @Override
  protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
    super.appendProperties(builder);
    builder.add(FACING, BLOCK_HALF, WATERLOGGED);
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

  @SuppressWarnings("deprecation")
  @Override
  public FluidState getFluidState(BlockState state) {
    return state.get(WATERLOGGED) ? WATER.getStill(false) : super.getFluidState(state);
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
    if (state.get(WATERLOGGED)) {
      world.getFluidTickScheduler().schedule(pos, WATER, WATER.getTickRate(world));
    }

    return super.getStateForNeighborUpdate(
        state, direction, neighborState, world, pos, neighborPos);
  }

  @SuppressWarnings("deprecation")
  @Override
  public VoxelShape getOutlineShape(
      BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
    switch (state.get(BLOCK_HALF)) {
      case BOTTOM:
        return directionToShapeWhenBottom.get(state.get(FACING));
      case TOP:
        return directionToShapeWhenTop.get(state.get(FACING));
      default:
        throw new IllegalStateException("Unexpected value: " + state.get(BLOCK_HALF));
    }
  }
}
