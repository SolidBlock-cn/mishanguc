package pers.solid.mishang.uc.block;

import com.google.common.collect.Maps;
import net.devtech.arrp.json.blockstate.JState;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.ShapeContext;
import net.minecraft.block.Waterloggable;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.fluid.FluidState;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.Item;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.state.property.EnumProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.text.MutableText;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.BlockMirror;
import net.minecraft.util.BlockRotation;
import net.minecraft.util.Util;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import pers.solid.mishang.uc.MishangUtils;
import pers.solid.mishang.uc.MishangucProperties;
import pers.solid.mishang.uc.arrp.ARRPGenerator;
import pers.solid.mishang.uc.util.HorizontalCornerDirection;

import java.util.Map;

public abstract class HandrailCornerBlock<T extends HandrailBlock> extends Block implements Waterloggable, ARRPGenerator, Handrails {
  /**
   * 该方块的基础的栏杆方块。
   */
  public static final EnumProperty<HorizontalCornerDirection> FACING = MishangucProperties.HORIZONTAL_CORNER_FACING;
  public static final BooleanProperty WATERLOGGED = Properties.WATERLOGGED;
  public final @NotNull T baseHandrail;
  public static final Map<HorizontalCornerDirection, VoxelShape> SHAPES = Util.make(() -> {
    final Map<Direction, @Nullable VoxelShape> shapes1 = MishangUtils.createHorizontalDirectionToShape(0, 0, 1, 15, 14, 2);
    final Map<Direction, @Nullable VoxelShape> shapes2 = MishangUtils.createHorizontalDirectionToShape(1, 0, 1, 16, 14, 2);
    return Direction.Type.HORIZONTAL.stream().collect(Maps.toImmutableEnumMap(direction -> HorizontalCornerDirection.fromDirections(direction, direction.rotateYClockwise()), direction -> VoxelShapes.union(shapes1.get(direction), shapes2.get(direction.rotateYClockwise()))));
  });

  public HandrailCornerBlock(@NotNull T baseHandrail, Settings settings) {
    super(settings);
    setDefaultState(stateManager.getDefaultState().with(WATERLOGGED, false).with(FACING, HorizontalCornerDirection.SOUTH_WEST));
    this.baseHandrail = baseHandrail;
  }

  public HandrailCornerBlock(@NotNull T baseHandrail) {
    this(baseHandrail, FabricBlockSettings.copyOf(baseHandrail));
  }

  @Override
  protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
    super.appendProperties(builder);
    builder.add(WATERLOGGED, FACING);
  }

  @Nullable
  @Override
  public BlockState getPlacementState(ItemPlacementContext ctx) {
    final BlockState placementState = super.getPlacementState(ctx);
    if (placementState == null) return null;
    return placementState.with(FACING, HorizontalCornerDirection.fromRotation(ctx.getPlayerYaw())).with(WATERLOGGED, ctx.getWorld().getFluidState(ctx.getBlockPos()).getFluid() == Fluids.WATER);
  }

  @SuppressWarnings("deprecation")
  @Override
  public FluidState getFluidState(BlockState state) {
    return state.get(WATERLOGGED) ? Fluids.WATER.getStill(false) : super.getFluidState(state);
  }

  @SuppressWarnings("deprecation")
  @Override
  public BlockState getStateForNeighborUpdate(BlockState state, Direction direction, BlockState neighborState, WorldAccess world, BlockPos pos, BlockPos neighborPos) {
    if (state.get(WATERLOGGED)) {
      world.createAndScheduleFluidTick(pos, Fluids.WATER, Fluids.WATER.getTickRate(world));
    }
    return super.getStateForNeighborUpdate(state, direction, neighborState, world, pos, neighborPos);
  }

  @Override
  public Item asItem() {
    return baseHandrail.asItem();
  }

  @Environment(EnvType.CLIENT)
  @Override
  public @NotNull JState getBlockStates() {
    return ARRPGenerator.stateForHorizontalCornerFacingBlock(getBlockModelIdentifier());
  }

  @SuppressWarnings("deprecation")
  @Override
  public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
    return SHAPES.get(state.get(FACING));
  }

  private static Direction clientCachedHitSide;
  private static Direction serverCachedHitSide;

  @Override
  public void onBreak(World world, BlockPos pos, BlockState state, PlayerEntity player) {
    super.onBreak(world, pos, state, player);
    final HitResult raycast = player.raycast(20f, 0, false);
    if (!(raycast instanceof BlockHitResult blockHitResult)) return;
    if (world.isClient()) clientCachedHitSide = blockHitResult.getSide();
    else serverCachedHitSide = blockHitResult.getSide();
  }

  @Override
  public void onBroken(WorldAccess world, BlockPos pos, BlockState state) {
    super.onBroken(world, pos, state);
    final Direction hitSide;
    if (world.isClient()) {
      hitSide = clientCachedHitSide;
      clientCachedHitSide = null;
    } else {
      hitSide = serverCachedHitSide;
      serverCachedHitSide = null;
    }
    final HorizontalCornerDirection facing = state.get(FACING);
    final Direction dir1 = facing.getDirectionInAxis(Direction.Axis.X);
    final Direction dir2 = facing.getDirectionInAxis(Direction.Axis.Z);
    final HandrailCornerBlock<? extends HandrailBlock> block = (HandrailCornerBlock<? extends HandrailBlock>) state.getBlock();
    switch (hitSide.getAxis()) {
      case X -> world.setBlockState(pos, block.baseHandrail.getDefaultState().with(WATERLOGGED, state.get(WATERLOGGED)).with(HandrailBlock.FACING, dir2), 0);
      case Z -> world.setBlockState(pos, block.baseHandrail.getDefaultState().with(WATERLOGGED, state.get(WATERLOGGED)).with(HandrailBlock.FACING, dir1), 0);
    }
  }

  @SuppressWarnings("deprecation")
  @Override
  public BlockState rotate(BlockState state, BlockRotation rotation) {
    return super.rotate(state, rotation)
        .with(FACING, state.get(FACING).rotate(rotation));
  }

  @SuppressWarnings("deprecation")
  @Override
  public BlockState mirror(BlockState state, BlockMirror mirror) {
    return super.mirror(state, mirror)
        .with(FACING, state.get(FACING).mirror(mirror));
  }

  @SuppressWarnings("deprecation")
  @Override
  public boolean isSideInvisible(BlockState state, BlockState stateFrom, Direction direction) {
    if (direction.getAxis().isHorizontal() && stateFrom.getBlock() instanceof final Handrails block) {
      return block.baseBlock() == this.baseBlock()
          && block.connectsIn(stateFrom, direction.getOpposite(), state.get(FACING).getDirectionInAxis(direction.rotateYClockwise().getAxis()));
    }
    return super.isSideInvisible(state, stateFrom, direction);
  }

  @Override
  public MutableText getName() {
    final Block block = baseBlock();
    return block == null ? super.getName() : new TranslatableText("block.mishanguc.handrail_corner", block.getName());
  }

  @Override
  public @Nullable Block baseBlock() {
    return baseHandrail.baseBlock();
  }

  @Override
  public boolean connectsIn(@NotNull BlockState blockState, @NotNull Direction direction, @Nullable Direction offsetFacing) {
    final HorizontalCornerDirection facing = blockState.get(FACING);
    return offsetFacing != null && facing.hasDirection(direction) && facing.hasDirection(offsetFacing);
  }
}
