package pers.solid.mishang.uc.block;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.data.models.BlockModelGenerators;
import net.minecraft.client.data.models.ModelProvider;
import net.minecraft.client.data.models.model.ModelLocationUtils;
import net.minecraft.client.data.models.model.ModelTemplate;
import net.minecraft.client.data.models.model.TextureMapping;
import net.minecraft.client.data.models.model.TextureSlot;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.data.loot.BlockLootSubProvider;
import net.minecraft.data.recipes.RecipeBuilder;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.ScheduledTickAccess;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.SlabBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.redstone.Orientation;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.Nullable;
import pers.solid.mishang.uc.blocks.RoadBlocks;
import pers.solid.mishang.uc.data.MishangucModels;

import java.util.List;

public abstract class AbstractRoadSlabBlock extends SlabBlock implements Road {
  private final Block baseBlock;

  public AbstractRoadSlabBlock(Block baseBlock, Properties settings) {
    super(settings);
    this.baseBlock = baseBlock;
  }

  @Override
  public LootTable.Builder getLootTable(BlockLootSubProvider blockLootTableGenerator) {
    return blockLootTableGenerator.createSlabItemTable(this);
  }

  @Override
  public void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
    super.createBlockStateDefinition(builder);
    appendRoadProperties(builder);
  }

  @Nullable
  @Override
  public BlockState getStateForPlacement(BlockPlaceContext ctx) {
    BlockPos blockPos = ctx.getClickedPos();
    BlockState blockState = ctx.getLevel().getBlockState(blockPos);
    if (blockState.is(this)) {
      return super.getStateForPlacement(ctx);
    } else {
      return withPlacementState(super.getStateForPlacement(ctx), ctx);
    }
  }

  @Override
  public BlockState rotate(BlockState state, Rotation rotation) {
    return rotateRoad(super.rotate(state, rotation), rotation);
  }

  @Override
  public BlockState mirror(BlockState state, Mirror mirror) {
    return mirrorRoad(super.mirror(state, mirror), mirror);
  }

  @Override
  public InteractionResult useWithoutItem(BlockState state, Level world, BlockPos pos, Player player, BlockHitResult hit) {
    InteractionResult result = super.useWithoutItem(state, world, pos, player, hit);
    if (result == InteractionResult.FAIL) {
      return result;
    }
    return onUseRoad(state, world, pos, player, hit);
  }

  @Override
  protected InteractionResult useItemOn(ItemStack stack, BlockState state, Level world, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
    final InteractionResult result = super.useItemOn(stack, state, world, pos, player, hand, hit);
    if (result == InteractionResult.FAIL) {
      return result;
    }
    return onUseRoadWithItem(stack, state, world, pos, player, hand, hit);
  }

  @Override
  protected void neighborChanged(BlockState state, Level world, BlockPos pos, Block sourceBlock, @Nullable Orientation wireOrientation, boolean notify) {
    super.neighborChanged(state, world, pos, sourceBlock, wireOrientation, notify);
    neighborRoadUpdate(state, world, pos, sourceBlock, wireOrientation, notify);
  }

  @Override
  protected BlockState updateShape(BlockState state, LevelReader world, ScheduledTickAccess tickView, BlockPos pos, Direction direction, BlockPos neighborPos, BlockState neighborState, RandomSource random) {
    return withStateForNeighborUpdate(super.updateShape(state, world, tickView, pos, direction, neighborPos, neighborState, random), tickView, world, pos, direction, neighborPos, neighborState, random);
  }

  @Override
  public void getMishangTooltip(ItemStack stack, Item.TooltipContext context, List<Component> tooltip, TooltipFlag options) {
    appendDescriptionTooltip(tooltip, context);
    appendRoadTooltip(stack, context, tooltip, options);
  }

  @Override
  public RecipeBuilder getCraftingRecipe(RecipeProvider recipeGenerator) {
    return recipeGenerator.slabBuilder(RecipeCategory.BUILDING_BLOCKS, this, Ingredient.of(baseBlock))
        .unlockedBy(RecipeProvider.getHasName(baseBlock), recipeGenerator.has(baseBlock));
  }

  @Override
  public void writeRecipes(RecipeProvider recipeGenerator, RecipeOutput exporter) {
    Road.super.writeRecipes(recipeGenerator, exporter);
    final RecipeBuilder paintingRecipe = getPaintingRecipe(RoadBlocks.ROAD_BLOCK.getRoadSlab(), this, recipeGenerator);
    if (paintingRecipe != null) {
      paintingRecipe.group(getRecipeGroup()).save(exporter, getPaintingRecipeKey());
    }
  }

  @Environment(EnvType.CLIENT)
  @Override
  public String getModelName(String suffix) {
    return "road_slab" + suffix;
  }

  @Environment(EnvType.CLIENT)
  @Override
  public final void registerModels(ModelProvider modelProvider, BlockModelGenerators blockStateModelGenerator) {
    ((AbstractRoadBlock) baseBlock).registerBaseOrSlabModels(this, blockStateModelGenerator);
    blockStateModelGenerator.registerSimpleItemModel(this, ModelLocationUtils.getModelLocation(this));
  }

  @Environment(EnvType.CLIENT)
  @Override
  public Identifier uploadModel(String suffix, TextureMapping textureMap, BlockModelGenerators blockStateModelGenerator, TextureSlot... textureKeys) {
    final ModelTemplate slabModel = MishangucModels.createBlock(getModelName(suffix), textureKeys);
    final ModelTemplate slabTopModel = MishangucModels.createBlock(getModelName(suffix + "_top"), "_top", textureKeys);
    final Identifier slabModelId = slabModel.create(this, textureMap, blockStateModelGenerator.modelOutput);
    slabTopModel.create(this, textureMap, blockStateModelGenerator.modelOutput);
    return slabModelId;
  }

  @Environment(EnvType.CLIENT)
  @Override
  public Identifier uploadModel(String suffix, String variant, TextureMapping textureMap, BlockModelGenerators blockStateModelGenerator, TextureSlot... textureKeys) {
    final ModelTemplate slabModel = MishangucModels.createBlock(getModelName(suffix), variant, textureKeys);
    final ModelTemplate slabTopModel = MishangucModels.createBlock(getModelName(suffix + "_top"), variant + "_top", textureKeys);
    final Identifier slabModelId = slabModel.create(this, textureMap, blockStateModelGenerator.modelOutput);
    slabTopModel.create(this, textureMap, blockStateModelGenerator.modelOutput);
    return slabModelId;
  }
}
