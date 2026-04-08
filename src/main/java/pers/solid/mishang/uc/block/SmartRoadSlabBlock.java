package pers.solid.mishang.uc.block;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.data.models.blockstates.BlockModelDefinitionGenerator;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.recipes.RecipeBuilder;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.data.recipes.SingleItemRecipeBuilder;
import net.minecraft.network.chat.Component;
import net.minecraft.util.RandomSource;
import net.minecraft.util.Util;
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
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.redstone.Orientation;
import net.minecraft.world.phys.BlockHitResult;
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
  public static final MapCodec<SmartRoadSlabBlock<?>> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(BuiltInRegistries.BLOCK.byNameCodec().fieldOf("base_block").forGetter(o -> o.baseBlock), propertiesCodec()).apply(instance, (block, settings) -> new SmartRoadSlabBlock<>((AbstractRoadBlock) block, settings)));
  private static Block cachedBaseBlock;
  public final T baseBlock;

  public SmartRoadSlabBlock(T baseBlock, Properties settings) {
    super(baseBlock, Util.make(() -> {
      cachedBaseBlock = baseBlock;
      return settings;
    }));
    this.baseBlock = baseBlock;
  }

  @Override
  public void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
    super.createBlockStateDefinition(builder);
    // 由于该方法是在构造方法中执行的，所以可能存在 null 的情况。
    (baseBlock == null ? cachedBaseBlock : baseBlock)
        .getStateDefinition()
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
  public void appendDescriptionTooltip(List<Component> tooltip, Item.TooltipContext context) {
    baseBlock.appendDescriptionTooltip(tooltip, context);
  }

  @Nullable
  @Override
  public BlockState getStateForPlacement(BlockPlaceContext ctx) {
    BlockPos blockPos = ctx.getClickedPos();
    BlockState blockState = ctx.getLevel().getBlockState(blockPos);
    if (blockState.is(this)) {
      return super.getStateForPlacement(ctx);
    } else {
      return baseBlock.withPlacementState(super.getStateForPlacement(ctx), ctx);
    }
  }

  @Override
  public BlockState rotate(BlockState state, Rotation rotation) {
    return baseBlock.rotate(state, rotation);
  }

  @Override
  public BlockState mirror(BlockState state, Mirror mirror) {
    return baseBlock.mirror(state, mirror);
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
    if (result instanceof InteractionResult.Fail) {
      return result;
    }
    return onUseRoadWithItem(stack, state, world, pos, player, hand, hit);
  }

  @Override
  protected void neighborChanged(BlockState state, Level world, BlockPos pos, Block sourceBlock, @Nullable Orientation wireOrientation, boolean notify) {
    baseBlock.neighborChanged(state, world, pos, sourceBlock, wireOrientation, notify);
  }

  @Override
  protected BlockState updateShape(BlockState state, LevelReader world, ScheduledTickAccess tickView, BlockPos pos, Direction direction, BlockPos neighborPos, BlockState neighborState, RandomSource random) {
    return withPropertiesOf(baseBlock.withPropertiesOf(state).updateShape(world, tickView, pos, direction, neighborPos, neighborState, random))
        .setValue(TYPE, state.getValue(TYPE))
        .setValue(WATERLOGGED, state.getValue(WATERLOGGED));
  }

  @Override
  public void getMishangTooltip(ItemStack stack, Item.TooltipContext context, List<Component> tooltip, TooltipFlag options) {
    super.getMishangTooltip(stack, context, tooltip, options);
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
  public SingleItemRecipeBuilder getStonecuttingRecipe(RecipeProvider recipeGenerator) {
    return SingleItemRecipeBuilder.stonecutting(Ingredient.of(baseBlock), RecipeCategory.BUILDING_BLOCKS, this, 2)
        .unlockedBy(RecipeProvider.getHasName(baseBlock), recipeGenerator.has(baseBlock));
  }

  @Override
  public MapCodec<? extends SmartRoadSlabBlock<?>> codec() {
    return CODEC;
  }

  @Override
  public RecipeBuilder getPaintingRecipe(Block base, Block self, RecipeProvider recipeGenerator) {
    return baseBlock.getPaintingRecipe(base, this, recipeGenerator);
  }

  @Environment(EnvType.CLIENT)
  @Override
  public BlockModelDefinitionGenerator composeState(BlockModelDefinitionGenerator stateForFull) {
    return ModelHelper.composeStateForSlab(stateForFull);
  }
}
