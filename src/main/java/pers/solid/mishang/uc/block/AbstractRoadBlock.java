package pers.solid.mishang.uc.block;

import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.data.models.BlockModelGenerators;
import net.minecraft.client.data.models.ModelProvider;
import net.minecraft.client.data.models.blockstates.BlockModelDefinitionGenerator;
import net.minecraft.client.data.models.model.ModelLocationUtils;
import net.minecraft.client.data.models.model.TextureMapping;
import net.minecraft.client.data.models.model.TextureSlot;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.data.recipes.RecipeBuilder;
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
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.ScheduledTickAccess;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.redstone.Orientation;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.Nullable;
import pers.solid.mishang.uc.blocks.RoadBlocks;
import pers.solid.mishang.uc.blocks.RoadSlabBlocks;
import pers.solid.mishang.uc.data.MishangucModels;
import pers.solid.mishang.uc.util.LineColor;
import pers.solid.mishang.uc.util.LineType;

import java.util.List;

public abstract class AbstractRoadBlock extends Block implements Road {
  protected final LineColor lineColor;
  protected final LineType lineType;
  protected static final RecordCodecBuilder<AbstractRoadBlock, LineColor> LINE_COLOR_FIELD_CODEC = LineColor.CODEC.fieldOf("line_color").forGetter(b -> b.lineColor);
  protected static final RecordCodecBuilder<AbstractRoadBlock, LineType> LINE_TYPE_FIELD_CODEC = LineType.CODEC.fieldOf("line_type").forGetter(b -> b.lineType);

  @SuppressWarnings("unchecked")
  protected static <B extends AbstractRoadBlock> RecordCodecBuilder<B, LineColor> lineColorFieldCodec() {
    return (RecordCodecBuilder<B, LineColor>) LINE_COLOR_FIELD_CODEC;
  }

  @SuppressWarnings("unchecked")
  protected static <B extends AbstractRoadBlock> RecordCodecBuilder<B, LineType> lineTypeFieldCodec() {
    return (RecordCodecBuilder<B, LineType>) LINE_TYPE_FIELD_CODEC;
  }

  public AbstractRoadBlock(Properties settings, LineColor lineColor, LineType lineType) {
    super(settings);
    this.lineColor = lineColor;
    this.lineType = lineType;
  }

  @Override
  public LineType getLineType(BlockState state, Direction direction) {
    return lineType;
  }

  @Override
  public void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
    super.createBlockStateDefinition(builder);
    appendRoadProperties(builder);
  }

  @Nullable
  @Override
  public BlockState getStateForPlacement(BlockPlaceContext ctx) {
    return withPlacementState(super.getStateForPlacement(ctx), ctx);
  }

  @Override
  public BlockState mirror(BlockState state, Mirror mirror) {
    return mirrorRoad(super.mirror(state, mirror), mirror);
  }

  @Override
  public BlockState rotate(BlockState state, Rotation rotation) {
    return rotateRoad(super.rotate(state, rotation), rotation);
  }

  @Override
  public InteractionResult useWithoutItem(BlockState state, Level world, BlockPos pos, Player player, BlockHitResult hit) {
    InteractionResult result = super.useWithoutItem(state, world, pos, player, hit);
    if (result instanceof InteractionResult.Fail) {
      return result;
    }
    return onUseRoad(state, world, pos, player, hit);
  }

  @Override
  protected InteractionResult useItemOn(ItemStack stack, BlockState state, Level world, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
    final InteractionResult result = super.useItemOn(stack, state, world, pos, player, hand, hit);
    if (result instanceof InteractionResult.Fail) {
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
  public LineColor getLineColor(BlockState state, Direction direction) {
    return lineColor;
  }

  @Override
  public void getMishangTooltip(ItemStack stack, Item.TooltipContext context, List<Component> tooltip, TooltipFlag options) {
    appendDescriptionTooltip(tooltip, context);
    appendRoadTooltip(stack, context, tooltip, options);
  }

  @ApiStatus.AvailableSince("1.1.0")
  @Contract(pure = true)
  public final AbstractRoadSlabBlock getRoadSlab() {
    return RoadSlabBlocks.BLOCK_TO_SLABS.get(this);
  }

  @Override
  public void writeRecipes(RecipeProvider recipeGenerator, RecipeOutput exporter) {
    Road.super.writeRecipes(recipeGenerator, exporter);
    final RecipeBuilder paintingRecipe = getPaintingRecipe(RoadBlocks.ROAD_BLOCK, this, recipeGenerator);
    if (paintingRecipe != null) {
      paintingRecipe.group(getRecipeGroup()).save(exporter, getPaintingRecipeKey());
    }
  }

  @Environment(EnvType.CLIENT)
  @Override
  public final void registerModels(ModelProvider modelProvider, BlockModelGenerators blockStateModelGenerator) {
    registerBaseOrSlabModels(this, blockStateModelGenerator);
    blockStateModelGenerator.registerSimpleItemModel(this, ModelLocationUtils.getModelLocation(this));
  }

  /**
   * 注册基础方块或台阶方块的模型。此方块应该由基础方块调用，即 {@code this} 应该是基础方块，然而 {@code road} 可能是基础方块，也可能是台阶。
   */
  @Environment(EnvType.CLIENT)
  protected abstract <B extends Block & Road> void registerBaseOrSlabModels(B road, BlockModelGenerators blockStateModelGenerator);

  @Environment(EnvType.CLIENT)
  @Override
  public String getModelName(String suffix) {
    return "road" + suffix;
  }

  @Environment(EnvType.CLIENT)
  @Override
  public Identifier uploadModel(String suffix, TextureMapping textureMap, BlockModelGenerators blockStateModelGenerator, TextureSlot... textureKeys) {
    return MishangucModels.createBlock(getModelName(suffix), textureKeys).create(this, textureMap, blockStateModelGenerator.modelOutput);
  }

  @Environment(EnvType.CLIENT)
  @Override
  public Identifier uploadModel(String suffix, String variant, TextureMapping textureMap, BlockModelGenerators blockStateModelGenerator, TextureSlot... textureKeys) {
    return MishangucModels.createBlock(getModelName(suffix), variant, textureKeys).create(this, textureMap, blockStateModelGenerator.modelOutput);
  }

  @Environment(EnvType.CLIENT)
  @Override
  public BlockModelDefinitionGenerator composeState(BlockModelDefinitionGenerator stateForFull) {
    return stateForFull;
  }
}
