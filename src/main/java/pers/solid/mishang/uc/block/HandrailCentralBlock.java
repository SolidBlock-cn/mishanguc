package pers.solid.mishang.uc.block;

import com.mojang.serialization.MapCodec;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.*;
import net.minecraft.block.enums.BlockHalf;
import net.minecraft.data.client.*;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.Item;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.registry.tag.BlockTags;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import pers.solid.mishang.uc.MishangUtils;

import java.util.Map;

/**
 * <p>类似于 {@link HandrailBlock}，但是方块对准的是正中间，而不是方块边缘的位置。</p>
 * <p>{@code HandrailBlock} 有一个抽象方法 {@link HandrailBlock#central() central()}，返回的就是这个示例。</p>
 *
 * @param <T> 其基础栏杆方块的类型。
 */
public abstract class HandrailCentralBlock<T extends HandrailBlock> extends HorizontalConnectingBlock implements MishangucBlock, Handrails {
  /**
   * 该方块的基础的栏杆方块。
   */
  public final @NotNull T baseHandrail;

  protected HandrailCentralBlock(@NotNull T baseBlock, float radius1, float radius2, float boundingHeight1, float boundingHeight2, float collisionHeight, Settings settings) {
    super(radius1, radius2, boundingHeight1, boundingHeight2, collisionHeight, settings);
    this.setDefaultState(getDefaultState()
        .with(WEST, true).with(EAST, true)
        .with(NORTH, false).with(SOUTH, false)
        .with(WATERLOGGED, false));
    this.baseHandrail = baseBlock;
  }

  @SuppressWarnings("deprecation")
  @Override
  public BlockState getStateForNeighborUpdate(BlockState state, Direction direction, BlockState neighborState, WorldAccess world, BlockPos pos, BlockPos neighborPos) {
    if (state.get(WATERLOGGED)) {
      world.scheduleFluidTick(pos, Fluids.WATER, Fluids.WATER.getTickRate(world));
    }
    BlockState stateForNeighborUpdate = super.getStateForNeighborUpdate(state, direction, neighborState, world, pos, neighborPos);
    stateForNeighborUpdate = updateSideStates(stateForNeighborUpdate, world, pos);
    return stateForNeighborUpdate;
  }

  public static boolean connectsTo(BlockState state, Direction direction, BlockState neighborState, WorldAccess world, BlockPos pos, BlockPos neighborPos) {
    if (cannotConnect(neighborState)) {
      return false;
    } else if (neighborState.isSideSolidFullSquare(world, neighborPos, direction.getOpposite())) {
      return true;
    } else if (connectsHandrailTo(direction, neighborState)) {
      return true;
    } else {
      return neighborState.isIn(BlockTags.FENCES) || (neighborState.getBlock() instanceof FenceGateBlock && FenceGateBlock.canWallConnect(neighborState, direction)) || neighborState.isIn(BlockTags.WALLS) || neighborState.getBlock() instanceof PaneBlock;
    }
  }


  public static boolean connectsHandrailTo(Direction direction, BlockState neighborState) {
    return neighborState.getBlock() instanceof HandrailStairBlock && neighborState.get(HandrailStairBlock.POSITION) == HandrailStairBlock.Position.CENTER && neighborState.get(HandrailStairBlock.FACING).getAxis() == direction.getAxis() || neighborState.getBlock() instanceof HandrailCentralBlock;
  }

  @SuppressWarnings("deprecation")
  @Override
  public boolean isSideInvisible(BlockState state, BlockState stateFrom, Direction direction) {
    final Block block = stateFrom.getBlock();
    if (direction.getAxis().isHorizontal() && block instanceof final Handrails handrails) {
      return block.asItem() == asItem()
          && handrails.connectsIn(stateFrom, direction.getOpposite(), null);
    }
    return super.isSideInvisible(state, stateFrom, direction);
  }

  public HandrailCentralBlock(@NotNull T baseBlock, Settings settings) {
    this(baseBlock, 1f, 1f, 16f, 16f, 16f, settings);
  }

  public HandrailCentralBlock(@NotNull T baseBlock) {
    this(baseBlock, FabricBlockSettings.copyOf(baseBlock));
  }

  @Override
  public Item asItem() {
    return baseHandrail.asItem();
  }


  @Override
  protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
    super.appendProperties(builder);
    builder.add(WEST, EAST, NORTH, SOUTH, WATERLOGGED);
  }

  @Nullable
  @Override
  public BlockState getPlacementState(ItemPlacementContext ctx) {
    BlockState placementState = super.getPlacementState(ctx);

    if (placementState == null) return null;

    final Direction playerFacing = ctx.getHorizontalPlayerFacing();
    final Direction.Axis playerFacingAxis = playerFacing.getAxis();

    // 初始化设置该 placementState 为玩家水平横向的方向。
    for (Map.Entry<Direction, BooleanProperty> entry : FACING_PROPERTIES.entrySet()) {
      final Direction direction = entry.getKey();
      final BooleanProperty property = entry.getValue();
      placementState = placementState.with(property, direction.getAxis() != playerFacingAxis);
    }
    final World world = ctx.getWorld();
    final BlockPos blockPos = ctx.getBlockPos();
    final boolean waterlogged = world.getFluidState(blockPos).getFluid() == Fluids.WATER;
    placementState = updateSideStates(placementState, world, blockPos);
    for (final Direction direction : new Direction[]{Direction.NORTH, Direction.EAST}) {
      for (Map.Entry<Direction, BooleanProperty> entry : FACING_PROPERTIES.entrySet()) {
        Direction facing = entry.getKey();
        BooleanProperty property = entry.getValue();
        if (placementState.get(property) != (facing.getAxis() == direction.getAxis())) continue;
        // 确保此时该方块有且只有单轴连接。

        final BlockState stateInCW = world.getBlockState(blockPos.offset(facing.rotateYClockwise()));
        final boolean isStairsInCW = stateInCW.getBlock() instanceof StairsBlock && stateInCW.contains(StairsBlock.FACING) && stateInCW.get(StairsBlock.FACING) == facing.rotateYClockwise() && stateInCW.contains(StairsBlock.HALF) && stateInCW.get(StairsBlock.HALF) == BlockHalf.BOTTOM;
        final BlockState stateInCCW = world.getBlockState(blockPos.offset(facing.rotateYCounterclockwise()));
        final boolean isStairsInCCW = stateInCCW.getBlock() instanceof StairsBlock && stateInCCW.contains(StairsBlock.FACING) && stateInCCW.get(StairsBlock.FACING) == facing.rotateYCounterclockwise() && stateInCCW.contains(StairsBlock.HALF) && stateInCCW.get(StairsBlock.HALF) == BlockHalf.BOTTOM;
        if (isStairsInCW != isStairsInCCW) {
          final BlockState stairState = baseHandrail.stair().getDefaultState();
          return stairState
              .with(WATERLOGGED, waterlogged)
              .with(HandrailStairBlock.FACING, isStairsInCW ? facing.rotateYClockwise() : facing.rotateYCounterclockwise())
              .with(HandrailStairBlock.SHAPE, HandrailStairBlock.Shape.BOTTOM)
              .with(HandrailStairBlock.POSITION, HandrailStairBlock.Position.CENTER);
        }
      }
    }
    return placementState.with(WATERLOGGED, waterlogged);
  }

  public static BlockState updateSideStates(BlockState state, WorldAccess world, BlockPos blockPos) {
    Direction mayBeOnlyInitialConnected = null;
    Direction mayBeOnlyConnected = null;
    int initialConnectedNumber = 0;
    int connectedNumber = 0;
    for (Map.Entry<Direction, BooleanProperty> entry : FACING_PROPERTIES.entrySet()) {
      Direction facing = entry.getKey();
      BooleanProperty property = entry.getValue();
      if (state.get(property)) {
        mayBeOnlyInitialConnected = facing;
        initialConnectedNumber += 1;
      }
      final BlockPos neighborPos = blockPos.offset(facing);
      final boolean connectsTo = connectsTo(state, facing, world.getBlockState(neighborPos), world, blockPos, neighborPos);
      state = state.with(property, connectsTo);
      if (connectsTo) {
        mayBeOnlyConnected = facing;
        connectedNumber += 1;
      }
    }
    if (connectedNumber == 1) {
      state = state.with(FACING_PROPERTIES.get(mayBeOnlyConnected.getOpposite()), true);
    } else if (connectedNumber == 0 && mayBeOnlyInitialConnected != null && initialConnectedNumber <= 2) {
      state = state
          .with(FACING_PROPERTIES.get(mayBeOnlyInitialConnected), true)
          .with(FACING_PROPERTIES.get(mayBeOnlyInitialConnected.getOpposite()), true);
    }
    return state;
  }

  public @NotNull BlockStateSupplier createBlockStates(Identifier postId, Identifier postSideId, Identifier sideId) {
    final MultipartBlockStateSupplier blockStateSupplier = MultipartBlockStateSupplier.create(this)
        .with(BlockStateVariant.create().put(VariantSettings.MODEL, postId));
    Direction.Type.HORIZONTAL.forEach(facing -> {
      final BooleanProperty property = FACING_PROPERTIES.get(facing);
      blockStateSupplier.with(When.create().set(property, true), BlockStateVariant.create().put(VariantSettings.MODEL, sideId).put(MishangUtils.DIRECTION_Y_VARIANT, facing).put(VariantSettings.UVLOCK, true));
      blockStateSupplier.with(When.create().set(property, false), BlockStateVariant.create().put(VariantSettings.MODEL, postSideId).put(MishangUtils.DIRECTION_Y_VARIANT, facing).put(VariantSettings.UVLOCK, true));
    });
    return blockStateSupplier;
  }

  @Override
  public @Nullable Block baseBlock() {
    return baseHandrail.baseBlock();
  }

  @Override
  public boolean connectsIn(@NotNull BlockState blockState, @NotNull Direction direction, @Nullable Direction offsetFacing) {
    return offsetFacing == null && direction.getAxis().isHorizontal() && blockState.get(FACING_PROPERTIES.get(direction));
  }

  @Override
  protected abstract MapCodec<? extends HandrailCentralBlock<?>> getCodec();
}
