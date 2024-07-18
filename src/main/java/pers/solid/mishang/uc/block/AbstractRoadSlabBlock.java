package pers.solid.mishang.uc.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.data.server.recipe.CraftingRecipeJsonBuilder;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemStack;
import net.minecraft.item.tooltip.TooltipType;
import net.minecraft.recipe.book.RecipeCategory;
import net.minecraft.state.StateManager;
import net.minecraft.text.Text;
import net.minecraft.util.*;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;
import org.jetbrains.annotations.Nullable;
import pers.solid.brrp.v1.api.RuntimeResourcePack;
import pers.solid.brrp.v1.generator.BRRPSlabBlock;
import pers.solid.mishang.uc.blocks.RoadBlocks;

import java.util.List;

public abstract class AbstractRoadSlabBlock extends BRRPSlabBlock implements Road {
  public AbstractRoadSlabBlock(Block baseBlock, Settings settings) {
    super(baseBlock, settings);
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

  @Override
  public BlockState rotate(BlockState state, BlockRotation rotation) {
    return rotateRoad(super.rotate(state, rotation), rotation);
  }

  @Override
  public BlockState mirror(BlockState state, BlockMirror mirror) {
    return mirrorRoad(super.mirror(state, mirror), mirror);
  }

  @Override
  public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, BlockHitResult hit) {
    ActionResult result = super.onUse(state, world, pos, player, hit);
    if (result == ActionResult.FAIL) {
      return result;
    }
    return onUseRoad(state, world, pos, player, hit);
  }

  @Override
  protected ItemActionResult onUseWithItem(ItemStack stack, BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
    final ItemActionResult result = super.onUseWithItem(stack, state, world, pos, player, hand, hit);
    if (result == ItemActionResult.FAIL) {
      return result;
    }
    return onUseRoadWithItem(stack, state, world, pos, player, hand, hit);
  }

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

  @Override
  public void appendTooltip(ItemStack stack, Item.TooltipContext context, List<Text> tooltip, TooltipType options) {
    super.appendTooltip(stack, context, tooltip, options);
    appendDescriptionTooltip(tooltip, context);
    appendRoadTooltip(stack, context, tooltip, options);
  }

  @Override
  public @Nullable RecipeCategory getRecipeCategory() {
    return RecipeCategory.BUILDING_BLOCKS;
  }

  @Override
  public void writeRecipes(RuntimeResourcePack pack) {
    super.writeRecipes(pack);
    final CraftingRecipeJsonBuilder paintingRecipe = getPaintingRecipe(RoadBlocks.ROAD_BLOCK.getRoadSlab(), this);
    if (paintingRecipe != null) {
      pack.addRecipeAndAdvancement(getPaintingRecipeId(), paintingRecipe);
    }
  }
}
