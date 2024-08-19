package pers.solid.mishang.uc.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.data.client.*;
import net.minecraft.data.server.recipe.CraftingRecipeJsonBuilder;
import net.minecraft.data.server.recipe.RecipeJsonProvider;
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
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import pers.solid.mishang.uc.blocks.RoadBlocks;
import pers.solid.mishang.uc.blocks.RoadSlabBlocks;
import pers.solid.mishang.uc.data.MishangucModels;
import pers.solid.mishang.uc.util.LineColor;
import pers.solid.mishang.uc.util.LineType;

import java.util.List;
import java.util.function.Consumer;

public abstract class AbstractRoadBlock extends Block implements Road {
  protected final LineColor lineColor;
  protected final LineType lineType;

  public AbstractRoadBlock(Settings settings, LineColor lineColor, LineType lineType) {
    super(settings);
    this.lineColor = lineColor;
    this.lineType = lineType;
  }

  @Override
  public LineType getLineType(BlockState state, Direction direction) {
    return lineType;
  }

  @Override
  public void appendProperties(StateManager.Builder<Block, BlockState> builder) {
    super.appendProperties(builder);
    appendRoadProperties(builder);
  }

  @Nullable
  @Override
  public BlockState getPlacementState(ItemPlacementContext ctx) {
    return withPlacementState(super.getPlacementState(ctx), ctx);
  }

  @SuppressWarnings("deprecation")
  @Override
  public BlockState mirror(BlockState state, BlockMirror mirror) {
    return mirrorRoad(super.mirror(state, mirror), mirror);
  }

  @SuppressWarnings("deprecation")
  @Override
  public BlockState rotate(BlockState state, BlockRotation rotation) {
    return rotateRoad(super.rotate(state, rotation), rotation);
  }

  @SuppressWarnings("deprecation")
  @Override
  public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
    ActionResult result = super.onUse(state, world, pos, player, hand, hit);
    if (result == ActionResult.FAIL) {
      return result;
    }
    return onUseRoad(state, world, pos, player, hand, hit);
  }

  @SuppressWarnings("deprecation")
  @Override
  public void neighborUpdate(
      BlockState state, World world, BlockPos pos, Block sourceBlock, BlockPos sourcePos, boolean notify) {
    super.neighborUpdate(state, world, pos, sourceBlock, sourcePos, notify);
    neighborRoadUpdate(state, world, pos, sourceBlock, sourcePos, notify);
  }

  @SuppressWarnings("deprecation")
  @Override
  public BlockState getStateForNeighborUpdate(BlockState state, Direction direction, BlockState neighborState, WorldAccess world, BlockPos pos, BlockPos neighborPos) {
    return withStateForNeighborUpdate(super.getStateForNeighborUpdate(state, direction, neighborState, world, pos, neighborPos), direction, neighborState, world, pos, neighborPos);
  }

  @Override
  public LineColor getLineColor(BlockState state, Direction direction) {
    return lineColor;
  }

  @Override
  public void appendTooltip(
      ItemStack stack, @Nullable BlockView world, List<Text> tooltip, TooltipContext options) {
    super.appendTooltip(stack, world, tooltip, options);
    appendDescriptionTooltip(tooltip, options);
    appendRoadTooltip(stack, world, tooltip, options);
  }

  @ApiStatus.AvailableSince("1.1.0")
  @Contract(pure = true)
  public final AbstractRoadSlabBlock getRoadSlab() {
    return RoadSlabBlocks.BLOCK_TO_SLABS.get(this);
  }

  @Override
  public void writeRecipes(Consumer<RecipeJsonProvider> exporter) {
    Road.super.writeRecipes(exporter);
    final CraftingRecipeJsonBuilder paintingRecipe = getPaintingRecipe(RoadBlocks.ROAD_BLOCK, this);
    if (paintingRecipe != null) {
      paintingRecipe.group(getRecipeGroup()).offerTo(exporter, getPaintingRecipeId());
    }
  }

  @Override
  public final void registerModels(ModelProvider modelProvider, BlockStateModelGenerator blockStateModelGenerator) {
    registerBaseOrSlabModels(this, blockStateModelGenerator);
    blockStateModelGenerator.registerParentedItemModel(this, ModelIds.getBlockModelId(this));
  }

  /**
   * 注册基础方块或台阶方块的模型。此方块应该由基础方块调用，即 {@code this} 应该是基础方块，然而 {@code road} 可能是基础方块，也可能是台阶。
   */
  protected abstract <B extends Block & Road> void registerBaseOrSlabModels(B road, BlockStateModelGenerator blockStateModelGenerator);

  @Override
  public String getModelName(String suffix) {
    return "road" + suffix;
  }

  @Override
  public Identifier uploadModel(String suffix, TextureMap textureMap, BlockStateModelGenerator blockStateModelGenerator, TextureKey... textureKeys) {
    return MishangucModels.createBlock(getModelName(suffix), textureKeys).upload(this, textureMap, blockStateModelGenerator.modelCollector);
  }

  @Override
  public Identifier uploadModel(String suffix, String variant, TextureMap textureMap, BlockStateModelGenerator blockStateModelGenerator, TextureKey... textureKeys) {
    return MishangucModels.createBlock(getModelName(suffix), variant, textureKeys).upload(this, textureMap, blockStateModelGenerator.modelCollector);
  }

  @Override
  public BlockStateSupplier composeState(@NotNull BlockStateSupplier stateForFull) {
    return stateForFull;
  }
}
