package pers.solid.mishang.uc.block;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.SlabBlock;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.data.server.loottable.VanillaBlockLootTableGenerator;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.LootTable;
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
import pers.solid.mishang.uc.util.TextBridge;

import java.util.List;

public abstract class AbstractRoadSlabBlock extends SlabBlock implements Road {

  public AbstractRoadSlabBlock(Settings settings) {
    super(settings);
  }

  @Override
  public void appendProperties(StateManager.Builder<Block, BlockState> builder) {
    super.appendProperties(builder);
    appendRoadProperties(builder);
  }

  @Nullable
  @Override
  public BlockState getPlacementState(ItemPlacementContext ctx) {
    BlockPos blockPos = ctx.getBlockPos();
    BlockState blockState = ctx.getWorld().getBlockState(blockPos);
    if (blockState.isOf(this)) {
      return super.getPlacementState(ctx);
    } else {
      return withPlacementState(super.getPlacementState(ctx), ctx);
    }
  }

  @SuppressWarnings("deprecation")
  @Override
  public BlockState rotate(BlockState state, BlockRotation rotation) {
    return rotateRoad(super.rotate(state, rotation), rotation);
  }

  @SuppressWarnings("deprecation")
  @Override
  public BlockState mirror(BlockState state, BlockMirror mirror) {
    return mirrorRoad(super.mirror(state, mirror), mirror);
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
    ActionResult result = super.onUse(state, world, pos, player, hand, hit);
    if (result == ActionResult.FAIL) {
      return result;
    } else {
      return onUseRoad(state, world, pos, player, hand, hit);
    }
  }

  @SuppressWarnings("deprecation")
  @Override
  public void neighborUpdate(
      BlockState state, World world, BlockPos pos, Block block, BlockPos sourcePos, boolean notify) {
    super.neighborUpdate(state, world, pos, block, sourcePos, notify);
    neighborRoadUpdate(state, world, pos, block, sourcePos, notify);
  }

  @Override
  public BlockState getStateForNeighborUpdate(BlockState state, Direction direction, BlockState neighborState, WorldAccess world, BlockPos pos, BlockPos neighborPos) {
    return withStateForNeighborUpdate(super.getStateForNeighborUpdate(state, direction, neighborState, world, pos, neighborPos), direction, neighborState, world, pos, neighborPos);
  }

  @Environment(EnvType.CLIENT)
  @Override
  public void appendTooltip(
      ItemStack stack, @Nullable BlockView world, List<Text> tooltip, TooltipContext options) {
    super.appendTooltip(stack, world, tooltip, options);
    appendDescriptionTooltip(tooltip, options);
    tooltip.add(TextBridge.translatable("block.mishanguc.tooltip.road").formatted(Formatting.GRAY));
    appendRoadTooltip(stack, world, tooltip, options);
  }

  @Override
  public LootTable.Builder getLootTable() {
    return new VanillaBlockLootTableGenerator().slabDrops(this);
  }
}
