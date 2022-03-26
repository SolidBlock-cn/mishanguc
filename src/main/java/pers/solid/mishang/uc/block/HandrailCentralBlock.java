package pers.solid.mishang.uc.block;

import net.devtech.arrp.api.RuntimeResourcePack;
import net.devtech.arrp.json.blockstate.JBlockModel;
import net.devtech.arrp.json.blockstate.JMultipart;
import net.devtech.arrp.json.blockstate.JState;
import net.devtech.arrp.json.blockstate.JWhen;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.HorizontalConnectingBlock;
import net.minecraft.block.StairsBlock;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.Item;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.text.MutableText;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import pers.solid.mishang.uc.MishangUtils;
import pers.solid.mishang.uc.arrp.ARRPGenerator;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * <p>类似于 {@link HandrailBlock}，但是方块对准的是正中间，而不是方块边缘的位置。</p>
 * <p>{@code HandrailBlock} 有一个抽象方法 {@link HandrailBlock#central() central()}，返回的就是这个示例。</p>
 *
 * @param <T> 其基础栏杆方块的类型。
 */
public abstract class HandrailCentralBlock<T extends HandrailBlock> extends HorizontalConnectingBlock implements ARRPGenerator, Handrails {
  /**
   * 该方块的基础的栏杆方块。
   */
  public final @NotNull T baseHandrail;

  protected HandrailCentralBlock(@NotNull T baseBlock, float radius1, float radius2, float boundingHeight1, float boundingHeight2, float collisionHeight, Settings settings) {
    super(radius1, radius2, boundingHeight1, boundingHeight2, collisionHeight, settings);
    this.setDefaultState(stateManager.getDefaultState()
        .with(WEST, true).with(EAST, true)
        .with(NORTH, false).with(SOUTH, false)
        .with(WATERLOGGED, false));
    this.baseHandrail = baseBlock;
  }

  @SuppressWarnings("deprecation")
  @Deprecated
  @Override
  public BlockState getStateForNeighborUpdate(BlockState state, Direction direction, BlockState neighborState, WorldAccess world, BlockPos pos, BlockPos neighborPos) {
    if (state.get(WATERLOGGED)) {
      world.getFluidTickScheduler().schedule(pos, Fluids.WATER, Fluids.WATER.getTickRate(world));
    }
    BlockState stateForNeighborUpdate = super.getStateForNeighborUpdate(state, direction, neighborState, world, pos, neighborPos);
    stateForNeighborUpdate = updateSideStates(stateForNeighborUpdate, world, pos);
    return stateForNeighborUpdate;
  }

  public static boolean connectsTo(BlockState state, Direction direction, BlockState neighborState, WorldAccess world, BlockPos pos, BlockPos neighborPos) {
    if (cannotConnect(neighborState.getBlock())) {
      return false;
    } else if (neighborState.isSideSolidFullSquare(world, neighborPos, direction.getOpposite())) {
      return true;
    } else if (neighborState.getBlock() instanceof HandrailStairBlock) {
      return neighborState.get(HandrailStairBlock.POSITION) == HandrailStairBlock.Position.CENTER && neighborState.get(HandrailStairBlock.FACING).getAxis() == direction.getAxis();
    } else return neighborState.getBlock() instanceof HandrailCentralBlock;
  }

  @Environment(EnvType.CLIENT)
  @Override
  public MutableText getName() {
    final Block block = baseBlock();
    return block == null ? super.getName() : new TranslatableText("block.mishanguc.handrail_central", block.getName());
  }

  @Environment(EnvType.CLIENT)
  @SuppressWarnings("deprecation")
  @Override
  public boolean isSideInvisible(BlockState state, BlockState stateFrom, Direction direction) {
    if (stateFrom.getBlock() instanceof Handrails) {
      final Handrails block = (Handrails) stateFrom.getBlock();
      return block.baseBlock() == this.baseBlock()
          && block.connectsIn(stateFrom, direction.getOpposite(), null);
    }
    return super.isSideInvisible(state, stateFrom, direction);
  }

  public HandrailCentralBlock(@NotNull T baseBlock, Settings settings) {
    this(baseBlock, 0.5f, 0.5f, 14f, 14f, 14, settings);
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

    final Direction playerFacing = ctx.getPlayerFacing();
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
        final boolean isStairsInCW = stateInCW.getBlock() instanceof StairsBlock && stateInCW.contains(StairsBlock.FACING) && stateInCW.get(StairsBlock.FACING) == facing.rotateYClockwise();
        final BlockState stateInCCW = world.getBlockState(blockPos.offset(facing.rotateYCounterclockwise()));
        final boolean isStairsInCCW = stateInCCW.getBlock() instanceof StairsBlock && stateInCCW.contains(StairsBlock.FACING) && stateInCCW.get(StairsBlock.FACING) == facing.rotateYCounterclockwise();
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

  @Environment(EnvType.CLIENT)
  @Override
  public @NotNull JState getBlockStates() {
    final List<JMultipart> parts = new ArrayList<>(5);
    final Identifier modelId = getBlockModelIdentifier();
    final Identifier postId = MishangUtils.identifierSuffix(modelId, "_post");
    final Identifier sideId = MishangUtils.identifierSuffix(modelId, "_side");
    parts.add(new JMultipart().addModel(new JBlockModel(postId)));
    FACING_PROPERTIES.forEach((facing, property) -> parts.add(new JMultipart()
        .addModel(new JBlockModel(sideId).y(((int) facing.asRotation())))
        .when(new JWhen().add(property.getName(), "true"))));
    return JState.state(parts.toArray(new JMultipart[5]));
  }

  @Environment(EnvType.CLIENT)
  @Override
  public abstract void writeBlockModel(RuntimeResourcePack pack);

  @Override
  public @Nullable Block baseBlock() {
    return baseHandrail.baseBlock();
  }

  @Override
  public boolean connectsIn(@NotNull BlockState blockState, @NotNull Direction direction, @Nullable Direction offsetFacing) {
    return offsetFacing == null && blockState.get(FACING_PROPERTIES.get(direction));
  }
}
