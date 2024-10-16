package pers.solid.mishang.uc.block;

import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.data.client.*;
import net.minecraft.data.server.recipe.CraftingRecipeJsonBuilder;
import net.minecraft.data.server.recipe.RecipeExporter;
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
      BlockState state, World world, BlockPos pos, Block sourceBlock, BlockPos sourcePos, boolean notify) {
    super.neighborUpdate(state, world, pos, sourceBlock, sourcePos, notify);
    neighborRoadUpdate(state, world, pos, sourceBlock, sourcePos, notify);
  }

  @Override
  public BlockState getStateForNeighborUpdate(BlockState state, Direction direction, BlockState neighborState, WorldAccess world, BlockPos pos, BlockPos neighborPos) {
    return withStateForNeighborUpdate(super.getStateForNeighborUpdate(state, direction, neighborState, world, pos, neighborPos), direction, neighborState, world, pos, neighborPos);
  }

  @Override
  public LineColor getLineColor(BlockState state, Direction direction) {
    return lineColor;
  }

  @Override
  public void appendTooltip(ItemStack stack, Item.TooltipContext context, List<Text> tooltip, TooltipType options) {
    super.appendTooltip(stack, context, tooltip, options);
    appendDescriptionTooltip(tooltip, context);
    appendRoadTooltip(stack, context, tooltip, options);
  }

  @ApiStatus.AvailableSince("1.1.0")
  @Contract(pure = true)
  public final AbstractRoadSlabBlock getRoadSlab() {
    return RoadSlabBlocks.BLOCK_TO_SLABS.get(this);
  }

  @Override
  public void writeRecipes(RecipeExporter exporter) {
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
