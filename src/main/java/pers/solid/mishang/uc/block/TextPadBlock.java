package pers.solid.mishang.uc.block;

import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.fluid.FluidState;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.DyeItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemStack;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.util.*;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;
import org.jetbrains.annotations.Nullable;
import pers.solid.mishang.uc.MishangUtils;
import pers.solid.mishang.uc.blockentity.TextPadBlockEntity;

import java.util.Map;

public class TextPadBlock extends HorizontalFacingBlock
    implements Waterloggable, BlockEntityProvider {
  public final DyeColor color;
  public final Block baseBlock;
  private static final BooleanProperty WATERLOGGED = Properties.WATERLOGGED;
  private static final Map<Direction, @Nullable VoxelShape> SHAPE_PER_DIRECTION =
      MishangUtils.createHorizontalDirectionToShape(0, 0, 0, 16, 16, 2);

  public TextPadBlock(Block baseBlock, @Nullable DyeColor color) {
    super(FabricBlockSettings.copyOf(baseBlock));
    this.baseBlock = baseBlock;
    this.color = color;
    this.setDefaultState(
        this.stateManager.getDefaultState().with(FACING, Direction.SOUTH).with(WATERLOGGED, false));
  }

  @Override
  protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
    super.appendProperties(builder);
    builder.add(FACING, WATERLOGGED);
  }

  @Nullable
  @Override
  public BlockState getPlacementState(ItemPlacementContext ctx) {
    final BlockState state = super.getPlacementState(ctx);
    return state == null
        ? null
        : state
            .with(FACING, ctx.getPlayerFacing().getOpposite())
            .with(
                WATERLOGGED,
                ctx.getWorld().getFluidState(ctx.getBlockPos()).getFluid() == Fluids.WATER);
  }

  @Nullable
  @Override
  public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
    return new TextPadBlockEntity(pos, state);
  }

  @SuppressWarnings("deprecation")
  @Override
  public VoxelShape getOutlineShape(
      BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
    return SHAPE_PER_DIRECTION.get(state.get(FACING));
  }

  @SuppressWarnings("deprecation")
  @Override
  public ActionResult onUse(
      BlockState state,
      World world,
      BlockPos pos,
      PlayerEntity player,
      Hand hand,
      BlockHitResult hit) {
    final ItemStack stackInHand = player.getStackInHand(hand);
    final Item item = stackInHand.getItem();
    if (item instanceof DyeItem) {
      final DyeColor color = ((DyeItem) item).getColor();
      final BlockEntity blockEntity = world.getBlockEntity(pos);
      if (blockEntity instanceof TextPadBlockEntity) {
        ((TextPadBlockEntity) blockEntity).textContext.color = color.getSignColor();
      }
    }
    return super.onUse(state, world, pos, player, hand, hit);
  }

  @SuppressWarnings("deprecation")
  @Override
  public FluidState getFluidState(BlockState state) {
    return state.get(WATERLOGGED) ? Fluids.WATER.getStill(false) : super.getFluidState(state);
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
      world.getFluidTickScheduler().schedule(pos, Fluids.WATER, Fluids.WATER.getTickRate(world));
    }

    return super.getStateForNeighborUpdate(
        state, direction, neighborState, world, pos, neighborPos);
  }

  @Override
  public BlockState mirror(BlockState state, BlockMirror mirror) {
    return super.mirror(state, mirror).with(FACING, mirror.apply(state.get(FACING)));
  }

  @Override
  public BlockState rotate(BlockState state, BlockRotation rotation) {
    return super.rotate(state, rotation).with(FACING, rotation.rotate(state.get(FACING)));
  }
}
