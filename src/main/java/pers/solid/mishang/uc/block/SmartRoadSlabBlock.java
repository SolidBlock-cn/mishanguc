package pers.solid.mishang.uc.block;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.client.data.BlockStateSupplier;
import net.minecraft.data.recipe.CraftingRecipeJsonBuilder;
import net.minecraft.data.recipe.RecipeGenerator;
import net.minecraft.data.recipe.StonecuttingRecipeJsonBuilder;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemStack;
import net.minecraft.item.tooltip.TooltipType;
import net.minecraft.recipe.Ingredient;
import net.minecraft.recipe.book.RecipeCategory;
import net.minecraft.registry.Registries;
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
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import pers.solid.mishang.uc.data.ModelHelper;
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
  public static final MapCodec<SmartRoadSlabBlock<?>> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(Registries.BLOCK.getCodec().fieldOf("base_block").forGetter(o -> o.baseBlock), createSettingsCodec()).apply(instance, (block, settings) -> new SmartRoadSlabBlock<>((AbstractRoadBlock) block, settings)));
  private static Block cachedBaseBlock;
  public final T baseBlock;

  public SmartRoadSlabBlock(T baseBlock, Settings settings) {
    super(baseBlock, Util.make(() -> {
      cachedBaseBlock = baseBlock;
      return settings;
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
  public void appendDescriptionTooltip(List<Text> tooltip, Item.TooltipContext context) {
    baseBlock.appendDescriptionTooltip(tooltip, context);
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
  public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, BlockHitResult hit) {
    ActionResult result = super.onUse(state, world, pos, player, hit);
    if (result == ActionResult.FAIL) {
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
    baseBlock.neighborUpdate(state, world, pos, sourceBlock, wireOrientation, notify);
  }

  @Override
  protected BlockState getStateForNeighborUpdate(BlockState state, WorldView world, ScheduledTickView tickView, BlockPos pos, Direction direction, BlockPos neighborPos, BlockState neighborState, Random random) {
    return getStateWithProperties(baseBlock.getStateWithProperties(state).getStateForNeighborUpdate(world, tickView, pos, direction, neighborPos, neighborState, random))
        .with(TYPE, state.get(TYPE))
        .with(WATERLOGGED, state.get(WATERLOGGED));
  }

  @Override
  public void appendTooltip(ItemStack stack, Item.TooltipContext context, List<Text> tooltip, TooltipType options) {
    super.appendTooltip(stack, context, tooltip, options);
    appendDescriptionTooltip(tooltip, context);
    appendRoadTooltip(stack, context, tooltip, options);
  }

  @Override
  public RoadConnectionState getConnectionStateOf(BlockState state, Direction direction) {
    return baseBlock.getConnectionStateOf(state, direction);
  }

  @Override
  public boolean shouldWriteStonecuttingRecipe() {
    return true;
  }

  @Override
  public StonecuttingRecipeJsonBuilder getStonecuttingRecipe(RecipeGenerator recipeGenerator) {
    return StonecuttingRecipeJsonBuilder.createStonecutting(Ingredient.ofItems(baseBlock), RecipeCategory.BUILDING_BLOCKS, this, 2)
        .criterion(RecipeGenerator.hasItem(baseBlock), recipeGenerator.conditionsFromItem(baseBlock));
  }

  @Override
  public MapCodec<? extends SmartRoadSlabBlock<?>> getCodec() {
    return CODEC;
  }

  @Override
  public CraftingRecipeJsonBuilder getPaintingRecipe(Block base, Block self, RecipeGenerator recipeGenerator) {
    return baseBlock.getPaintingRecipe(base, this, recipeGenerator);
  }

  @Override
  public BlockStateSupplier composeState(@NotNull BlockStateSupplier stateForFull) {
    return ModelHelper.composeStateForSlab(stateForFull);
  }
}
