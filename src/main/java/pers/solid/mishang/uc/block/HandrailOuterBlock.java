package pers.solid.mishang.uc.block;

import com.google.common.collect.Maps;
import net.devtech.arrp.api.RuntimeResourcePack;
import net.devtech.arrp.json.blockstate.JState;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.ShapeContext;
import net.minecraft.block.Waterloggable;
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
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.minecraft.world.WorldAccess;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import pers.solid.mishang.uc.MishangUtils;
import pers.solid.mishang.uc.MishangucProperties;
import pers.solid.mishang.uc.arrp.ARRPGenerator;
import pers.solid.mishang.uc.util.HorizontalCornerDirection;

import java.util.Map;

/**
 * 栏杆外部角落的方块。当两个栏杆方块靠边直角围起来的时候，可以设置一个这样的外部角落方块，以填补两个栏杆之间的空隙。<br>
 * 同 {@link HandrailCornerBlock} 一样，本方块也是水平角落朝向的，默认朝向为西南方。
 */
public abstract class HandrailOuterBlock<T extends HandrailBlock> extends Block implements Waterloggable, ARRPGenerator, Handrails {
  public static final EnumProperty<HorizontalCornerDirection> FACING = MishangucProperties.HORIZONTAL_CORNER_FACING;
  public static final BooleanProperty WATERLOGGED = Properties.WATERLOGGED;
  public static final Map<HorizontalCornerDirection, VoxelShape> SHAPES = Util.make(() -> {

    final Map<Direction, @Nullable VoxelShape> map1 = MishangUtils.createHorizontalDirectionToShape(0, 0, 14, 2, 14, 15);
    final Map<Direction, @Nullable VoxelShape> map2 = MishangUtils.createHorizontalDirectionToShape(1, 0, 15, 2, 14, 16);
    return Direction.Type.HORIZONTAL.stream().collect(Maps.toImmutableEnumMap(direction -> HorizontalCornerDirection.fromDirections(direction, direction.rotateYClockwise()), direction -> VoxelShapes.union(map1.get(direction), map2.get(direction))));
  });
  public final @NotNull T baseRail;

  public HandrailOuterBlock(@NotNull T baseRail, Settings settings) {
    super(settings);
    this.baseRail = baseRail;
    setDefaultState(stateManager.getDefaultState().with(WATERLOGGED, false).with(FACING, HorizontalCornerDirection.SOUTH_WEST));
  }

  public HandrailOuterBlock(@NotNull T baseRail) {
    this(baseRail, FabricBlockSettings.copyOf(baseRail));
  }

  @Override
  protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
    super.appendProperties(builder);
    builder.add(WATERLOGGED, FACING);
  }

  @Override
  public @Nullable Block baseBlock() {
    return baseRail.baseBlock();
  }

  @SuppressWarnings("deprecation")
  @Override
  public BlockState rotate(BlockState state, BlockRotation rotation) {
    return super.rotate(state, rotation).with(FACING, state.get(FACING).rotate(rotation));
  }

  @SuppressWarnings("deprecation")
  @Override
  public BlockState mirror(BlockState state, BlockMirror mirror) {
    return super.mirror(state, mirror).with(FACING, state.get(FACING).mirror(mirror));
  }

  @SuppressWarnings("deprecation")
  @Override
  public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
    return SHAPES.get(state.get(FACING));
  }

  @SuppressWarnings("deprecation")
  @Override
  public FluidState getFluidState(BlockState state) {
    return state.get(WATERLOGGED) ? Fluids.WATER.getStill(false) : super.getFluidState(state);
  }

  @Nullable
  @Override
  public BlockState getPlacementState(ItemPlacementContext ctx) {
    final BlockState placementState = super.getPlacementState(ctx);
    if (placementState == null) return null;
    return placementState.with(FACING, HorizontalCornerDirection.fromRotation(ctx.getPlayerYaw())).with(WATERLOGGED, ctx.getWorld().getFluidState(ctx.getBlockPos()).getFluid() == Fluids.WATER);
  }

  @Environment(EnvType.CLIENT)
  @Override
  public MutableText getName() {
    final Block block = baseBlock();
    return block == null ? super.getName() : new TranslatableText("block.mishanguc.handrail_outer", block.getName());
  }

  @SuppressWarnings("deprecation")
  @Override
  public BlockState getStateForNeighborUpdate(BlockState state, Direction direction, BlockState neighborState, WorldAccess world, BlockPos pos, BlockPos neighborPos) {
    if (state.get(WATERLOGGED)) {
      world.getFluidTickScheduler().schedule(pos, Fluids.WATER, Fluids.WATER.getTickRate(world));
    }
    return super.getStateForNeighborUpdate(state, direction, neighborState, world, pos, neighborPos);
  }

  @Environment(EnvType.CLIENT)
  @SuppressWarnings("deprecation")
  @Override
  public boolean isSideInvisible(BlockState state, BlockState stateFrom, Direction direction) {
    if (direction.getAxis().isHorizontal() && stateFrom.getBlock() instanceof Handrails) {
      final Handrails block = (Handrails) stateFrom.getBlock();
      return block.baseBlock() == this.baseBlock()
          && block.connectsIn(stateFrom, direction.getOpposite(), state.get(FACING).getDirectionInAxis(direction.rotateYClockwise().getAxis()).getOpposite());
    }
    return super.isSideInvisible(state, stateFrom, direction);
  }

  @Override
  public boolean connectsIn(@NotNull BlockState blockState, @NotNull Direction direction, @Nullable Direction offsetFacing) {
    final HorizontalCornerDirection facing = blockState.get(FACING);
    return offsetFacing != null && facing.hasDirection(direction) && facing.hasDirection(offsetFacing.getOpposite());
  }

  @Override
  public Item asItem() {
    return baseRail.asItem();
  }

  @Environment(EnvType.CLIENT)
  @Override
  public @NotNull JState getBlockStates() {
    return ARRPGenerator.stateForHorizontalCornerFacingBlock(getBlockModelIdentifier());
  }

  @Override
  public abstract void writeBlockModel(RuntimeResourcePack pack);
}
