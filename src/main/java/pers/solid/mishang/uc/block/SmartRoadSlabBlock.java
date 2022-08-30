package pers.solid.mishang.uc.block;

import net.devtech.arrp.json.blockstate.JBlockStates;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemStack;
import net.minecraft.state.StateManager;
import net.minecraft.text.Text;
import net.minecraft.util.*;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;
import org.jetbrains.annotations.Nullable;
import pers.solid.mishang.uc.arrp.BRRPHelper;
import pers.solid.mishang.uc.util.LineColor;
import pers.solid.mishang.uc.util.LineType;
import pers.solid.mishang.uc.util.RoadConnectionState;

import java.util.List;

/**
 * 根据其基础方块来生成台阶方块。
 *
 * @param <T> 基础方块类型。
 */
public class SmartRoadSlabBlock<T extends AbstractRoadBlock> extends AbstractRoadSlabBlock {
  private static Block cachedBaseBlock;
  public final T baseBlock;

  public SmartRoadSlabBlock(T baseBlock) {
    super(Util.make(() -> {
      cachedBaseBlock = baseBlock;
      return FabricBlockSettings.copyOf(baseBlock);
    }));
    this.baseBlock = baseBlock;
  }

  @Override
  public void appendProperties(StateManager.Builder<Block, BlockState> builder) {
    super.appendProperties(builder);
    // 由于该方法是在构造方法中执行的，所以可能存在 null 的情况。
    (baseBlock == null ? cachedBaseBlock : baseBlock)
        .getStateManager()
        .getProperties()
        .forEach(builder::add);
  }

  @Override
  public LineColor getLineColor(BlockState blockState, Direction direction) {
    return baseBlock.getLineColor(blockState, direction);
  }

  @Override
  public LineType getLineType(BlockState blockState, Direction direction) {
    return baseBlock.getLineType(blockState, direction);
  }

  @Override
  public void appendDescriptionTooltip(List<Text> tooltip, TooltipContext options) {
    baseBlock.appendDescriptionTooltip(tooltip, options);
  }

  @Nullable
  @Override
  public BlockState getPlacementState(ItemPlacementContext ctx) {
    BlockPos blockPos = ctx.getBlockPos();
    BlockState blockState = ctx.getWorld().getBlockState(blockPos);
    if (blockState.isOf(this)) {
      return super.getPlacementState(ctx);
    } else {
      return baseBlock.withPlacementState(super.getPlacementState(ctx), ctx);
    }
  }

  @Override
  public BlockState rotate(BlockState state, BlockRotation rotation) {
    return baseBlock.rotate(state, rotation);
  }

  @Override
  public BlockState mirror(BlockState state, BlockMirror mirror) {
    return baseBlock.mirror(state, mirror);
  }

  @Override
  public ActionResult onUse(
      BlockState state,
      World world,
      BlockPos pos,
      PlayerEntity player,
      Hand hand,
      BlockHitResult hit) {
    final ActionResult result = super.onUse(state, world, pos, player, hand, hit);
    if (result == ActionResult.FAIL) {
      return result;
    } else {
      return onUseRoad(state, world, pos, player, hand, hit);
    }
  }

  @Override
  public void neighborUpdate(
      BlockState state, World world, BlockPos pos, Block block, BlockPos sourcePos, boolean notify) {
    baseBlock.neighborUpdate(state, world, pos, block, sourcePos, notify);
  }

  @Override
  public BlockState getStateForNeighborUpdate(BlockState state, Direction direction, BlockState neighborState, WorldAccess world, BlockPos pos, BlockPos neighborPos) {
    return getStateWithProperties(baseBlock.getStateWithProperties(state).getStateForNeighborUpdate(direction, neighborState, world, pos, neighborPos))
        .with(TYPE, state.get(TYPE))
        .with(WATERLOGGED, state.get(WATERLOGGED));
  }

  @Override
  public void appendRoadTooltip(
      ItemStack stack, @Nullable BlockView world, List<Text> tooltip, TooltipContext options) {
    baseBlock.appendRoadTooltip(stack, world, tooltip, options);
  }

  @Override
  public RoadConnectionState getConnectionStateOf(BlockState state, Direction direction) {
    return baseBlock.getConnectionStateOf(state, direction);
  }

  @Environment(EnvType.CLIENT)
  @Override
  public @Nullable JBlockStates getBlockStates() {
    final JBlockStates baseStates = baseBlock.getBlockStates();
    return baseStates == null ? null : BRRPHelper.composeStateForSlab(baseStates);
  }
}
