package pers.solid.mishang.uc.block;

import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.client.data.*;
import net.minecraft.data.recipe.CraftingRecipeJsonBuilder;
import net.minecraft.data.recipe.RecipeExporter;
import net.minecraft.data.recipe.RecipeGenerator;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemStack;
import net.minecraft.item.tooltip.TooltipType;
import net.minecraft.state.StateManager;
import net.minecraft.text.Text;
import net.minecraft.util.*;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.World;
import net.minecraft.world.WorldView;
import net.minecraft.world.block.WireOrientation;
import net.minecraft.world.tick.ScheduledTickView;
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

  @Override
  public BlockState mirror(BlockState state, BlockMirror mirror) {
    return mirrorRoad(super.mirror(state, mirror), mirror);
  }

  @Override
  public BlockState rotate(BlockState state, BlockRotation rotation) {
    return rotateRoad(super.rotate(state, rotation), rotation);
  }

  @Override
  public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, BlockHitResult hit) {
    ActionResult result = super.onUse(state, world, pos, player, hit);
    if (result instanceof ActionResult.Fail) {
      return result;
    }
    return onUseRoad(state, world, pos, player, hit);
  }

  @Override
  protected ActionResult onUseWithItem(ItemStack stack, BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
    final ActionResult result = super.onUseWithItem(stack, state, world, pos, player, hand, hit);
    if (result instanceof ActionResult.Fail) {
      return result;
    }
    return onUseRoadWithItem(stack, state, world, pos, player, hand, hit);
  }

  @Override
  protected void neighborUpdate(BlockState state, World world, BlockPos pos, Block sourceBlock, @Nullable WireOrientation wireOrientation, boolean notify) {
    super.neighborUpdate(state, world, pos, sourceBlock, wireOrientation, notify);
    neighborRoadUpdate(state, world, pos, sourceBlock, wireOrientation, notify);
  }

  @Override
  protected BlockState getStateForNeighborUpdate(BlockState state, WorldView world, ScheduledTickView tickView, BlockPos pos, Direction direction, BlockPos neighborPos, BlockState neighborState, Random random) {
    return withStateForNeighborUpdate(super.getStateForNeighborUpdate(state, world, tickView, pos, direction, neighborPos, neighborState, random), tickView, world, pos, direction, neighborPos, neighborState, random);
  }

  @Override
  public LineColor getLineColor(BlockState state, Direction direction) {
    return lineColor;
  }

  @Override
  public void getMishangTooltip(ItemStack stack, Item.TooltipContext context, List<Text> tooltip, TooltipType options) {
    appendDescriptionTooltip(tooltip, context);
    appendRoadTooltip(stack, context, tooltip, options);
  }

  @ApiStatus.AvailableSince("1.1.0")
  @Contract(pure = true)
  public final AbstractRoadSlabBlock getRoadSlab() {
    return RoadSlabBlocks.BLOCK_TO_SLABS.get(this);
  }

  @Override
  public void writeRecipes(RecipeGenerator recipeGenerator, RecipeExporter exporter) {
    Road.super.writeRecipes(recipeGenerator, exporter);
    final CraftingRecipeJsonBuilder paintingRecipe = getPaintingRecipe(RoadBlocks.ROAD_BLOCK, this, recipeGenerator);
    if (paintingRecipe != null) {
      paintingRecipe.group(getRecipeGroup()).offerTo(exporter, getPaintingRecipeKey());
    }
  }

  @Environment(EnvType.CLIENT)
  @Override
  public final void registerModels(ModelProvider modelProvider, BlockStateModelGenerator blockStateModelGenerator) {
    registerBaseOrSlabModels(this, blockStateModelGenerator);
    blockStateModelGenerator.registerParentedItemModel(this, ModelIds.getBlockModelId(this));
  }

  /**
   * 注册基础方块或台阶方块的模型。此方块应该由基础方块调用，即 {@code this} 应该是基础方块，然而 {@code road} 可能是基础方块，也可能是台阶。
   */
  @Environment(EnvType.CLIENT)
  protected abstract <B extends Block & Road> void registerBaseOrSlabModels(B road, BlockStateModelGenerator blockStateModelGenerator);

  @Environment(EnvType.CLIENT)
  @Override
  public String getModelName(String suffix) {
    return "road" + suffix;
  }

  @Environment(EnvType.CLIENT)
  @Override
  public Identifier uploadModel(String suffix, TextureMap textureMap, BlockStateModelGenerator blockStateModelGenerator, TextureKey... textureKeys) {
    return MishangucModels.createBlock(getModelName(suffix), textureKeys).upload(this, textureMap, blockStateModelGenerator.modelCollector);
  }

  @Environment(EnvType.CLIENT)
  @Override
  public Identifier uploadModel(String suffix, String variant, TextureMap textureMap, BlockStateModelGenerator blockStateModelGenerator, TextureKey... textureKeys) {
    return MishangucModels.createBlock(getModelName(suffix), variant, textureKeys).upload(this, textureMap, blockStateModelGenerator.modelCollector);
  }

  @Environment(EnvType.CLIENT)
  @Override
  public BlockModelDefinitionCreator composeState(@NotNull BlockModelDefinitionCreator stateForFull) {
    return stateForFull;
  }
}
