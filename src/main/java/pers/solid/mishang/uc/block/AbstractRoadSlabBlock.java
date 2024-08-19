package pers.solid.mishang.uc.block;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.SlabBlock;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.data.client.*;
import net.minecraft.data.server.recipe.CraftingRecipeJsonBuilder;
import net.minecraft.data.server.recipe.RecipeExporter;
import net.minecraft.data.server.recipe.RecipeProvider;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.Ingredient;
import net.minecraft.recipe.book.RecipeCategory;
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
import pers.solid.mishang.uc.blocks.RoadBlocks;
import pers.solid.mishang.uc.data.MishangucModels;

import java.util.List;

public abstract class AbstractRoadSlabBlock extends SlabBlock implements Road {
private final Block baseBlock;
  public AbstractRoadSlabBlock(Block baseBlock, Settings settings) {
    super(settings);
    this.baseBlock = baseBlock;
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
  public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
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
    appendRoadTooltip(stack, world, tooltip, options);
  }

  @Override
  public CraftingRecipeJsonBuilder getCraftingRecipe() {
    return RecipeProvider.createSlabRecipe(RecipeCategory.BUILDING_BLOCKS, this, Ingredient.ofItems(baseBlock))
        .criterion(RecipeProvider.hasItem(baseBlock), RecipeProvider.conditionsFromItem(baseBlock));
  }

  @Override
  public void writeRecipes(RecipeExporter exporter) {
    Road.super.writeRecipes(exporter);
    final CraftingRecipeJsonBuilder paintingRecipe = getPaintingRecipe(RoadBlocks.ROAD_BLOCK.getRoadSlab(), this);
    if (paintingRecipe != null) {
      paintingRecipe.group(getRecipeGroup()).offerTo(exporter, getPaintingRecipeId());
    }
  }

  @Override
  public String getModelName(String suffix) {
    return "road_slab" + suffix;
  }

  @Override
  public final void registerModels(ModelProvider modelProvider, BlockStateModelGenerator blockStateModelGenerator) {
    ((AbstractRoadBlock) baseBlock).registerBaseOrSlabModels(this, blockStateModelGenerator);
    blockStateModelGenerator.registerParentedItemModel(this, ModelIds.getBlockModelId(this));
  }

  @Override
  public Identifier uploadModel(String suffix, TextureMap textureMap, BlockStateModelGenerator blockStateModelGenerator, TextureKey... textureKeys) {
    final Model slabModel = MishangucModels.createBlock(getModelName(suffix), textureKeys);
    final Model slabTopModel = MishangucModels.createBlock(getModelName(suffix + "_top"), "_top", textureKeys);
    final Identifier slabModelId = slabModel.upload(this, textureMap, blockStateModelGenerator.modelCollector);
    slabTopModel.upload(this, textureMap, blockStateModelGenerator.modelCollector);
    return slabModelId;
  }

  @Override
  public Identifier uploadModel(String suffix, String variant, TextureMap textureMap, BlockStateModelGenerator blockStateModelGenerator, TextureKey... textureKeys) {
    final Model slabModel = MishangucModels.createBlock(getModelName(suffix), variant, textureKeys);
    final Model slabTopModel = MishangucModels.createBlock(getModelName(suffix + "_top"), variant + "_top", textureKeys);
    final Identifier slabModelId = slabModel.upload(this, textureMap, blockStateModelGenerator.modelCollector);
    slabTopModel.upload(this, textureMap, blockStateModelGenerator.modelCollector);
    return slabModelId;
  }
}
