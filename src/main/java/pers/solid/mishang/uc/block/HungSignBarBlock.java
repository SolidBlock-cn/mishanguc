package pers.solid.mishang.uc.block;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.block.*;
import net.minecraft.client.data.*;
import net.minecraft.data.recipe.CraftingRecipeJsonBuilder;
import net.minecraft.data.recipe.RecipeGenerator;
import net.minecraft.data.recipe.StonecuttingRecipeJsonBuilder;
import net.minecraft.fluid.FluidState;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.recipe.Ingredient;
import net.minecraft.recipe.book.RecipeCategory;
import net.minecraft.registry.Registries;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.state.property.EnumProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.text.MutableText;
import net.minecraft.util.BlockMirror;
import net.minecraft.util.BlockRotation;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.random.Random;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.minecraft.world.EmptyBlockView;
import net.minecraft.world.World;
import net.minecraft.world.WorldView;
import net.minecraft.world.tick.ScheduledTickView;
import org.jetbrains.annotations.Nullable;
import pers.solid.mishang.uc.MishangUtils;
import pers.solid.mishang.uc.data.MishangucModels;
import pers.solid.mishang.uc.data.ModelHelper;
import pers.solid.mishang.uc.util.TextBridge;

import java.util.Map;

/**
 * 悬挂的告示牌上面的专用的悬挂物方块。其方块状态会与其下方的悬挂告示牌方块同步。
 */
public class HungSignBarBlock extends Block implements Waterloggable, MishangucBlock {
  public static final MapCodec<ColoredHungSignBarBlock> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(createBaseBlockCodec(), createSettingsCodec()).apply(instance, ColoredHungSignBarBlock::new));

  protected static <B extends HungSignBarBlock> RecordCodecBuilder<B, Block> createBaseBlockCodec() {
    return Registries.BLOCK.getCodec().fieldOf("base_block").forGetter(b -> b.baseBlock);
  }

  public static final EnumProperty<Direction.Axis> AXIS = Properties.HORIZONTAL_AXIS;
  public static final BooleanProperty WATERLOGGED = Properties.WATERLOGGED;
  public static final BooleanProperty LEFT = HungSignBlock.LEFT;
  public static final BooleanProperty RIGHT = HungSignBlock.RIGHT;
  private static final Map<Direction, @Nullable VoxelShape> BAR_SHAPES =
      MishangUtils.createHorizontalDirectionToShape(7.5, 0, 11, 8.5, 16, 12);
  private static final Map<Direction, @Nullable VoxelShape> BAR_SHAPES_EDGE =
      MishangUtils.createHorizontalDirectionToShape(7.5, 0, 13, 8.5, 16, 14);
  private static final Map<Direction, @Nullable VoxelShape> BAR_SHAPES_WIDE =
      MishangUtils.createHorizontalDirectionToShape(6.5, 0, 10, 9.5, 16, 13);
  private static final Map<Direction, @Nullable VoxelShape> BAR_SHAPES_EDGE_WIDE =
      MishangUtils.createHorizontalDirectionToShape(6.5, 0, 12, 9.5, 16, 15);
  /**
   * 当 left 和 right 均为 false 时，显示在正中央，采用此轮廓。
   */
  private static final VoxelShape BAR_SHAPE_CENTRAL = createCuboidShape(7.5, 0, 7.5, 8.5, 16, 8.5);

  private static final VoxelShape BAR_SHAPE_CENTRAL_WIDE = createCuboidShape(6.5, 0, 6.5, 9.5, 16, 9.5);
  public final @Nullable Block baseBlock;
  /**
   * 告示牌杆的纹理。若为 {@code null}，则根据其 {@link #baseBlock} 的 id 来推断。
   */
  public Identifier texture;

  public HungSignBarBlock(@Nullable Block baseBlock, Settings settings) {
    super(settings);
    this.baseBlock = baseBlock;
    this.setDefaultState(getDefaultState()
        .with(WATERLOGGED, false)
        .with(AXIS, Direction.Axis.X)
        .with(LEFT, true)
        .with(RIGHT, true));
  }

  @Override
  protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
    super.appendProperties(builder);
    builder.add(AXIS, WATERLOGGED, LEFT, RIGHT);
  }

  @Nullable
  @Override
  public BlockState getPlacementState(ItemPlacementContext ctx) {
    final BlockState placementState = super.getPlacementState(ctx);
    if (placementState == null) {
      return null;
    }
    final World world = ctx.getWorld();
    final BlockPos blockPos = ctx.getBlockPos();
    final BlockPos downPos = blockPos.down();
    final BlockState downState = world.getBlockState(downPos);

    // 考虑放置之初，底部若为悬挂的告示牌方块，则该方块没有连接，因此在
    // getStateForNeighborUpdate 的时候，将 neighborState 设为假定连接后的 state。
    // 注意，悬挂告示牌方块的 getStateForNeighborUpdate 并不会检查其上方的告示牌杆的属性是否匹配，只要存在就行。
    return placementState.getStateForNeighborUpdate(
            world,
            world,
            blockPos,
            Direction.DOWN,
            downPos,
            downState
                .getStateForNeighborUpdate(world, world, blockPos, Direction.UP, downPos, placementState, world.getRandom()),
            world.getRandom())
        .with(WATERLOGGED, world.getFluidState(blockPos).getFluid() == Fluids.WATER);
  }

  @Override
  public FluidState getFluidState(BlockState state) {
    return state.get(WATERLOGGED) ? Fluids.WATER.getStill(false) : super.getFluidState(state);
  }

  @Override
  public VoxelShape getOutlineShape(
      BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
    final Direction.Axis axis = state.get(AXIS);
    final Boolean left = state.get(LEFT);
    final Boolean right = state.get(RIGHT);

    if (left && right) {
      return BAR_SHAPE_CENTRAL_WIDE;
    }
    final Map<Direction, @Nullable VoxelShape> barShapes =
        BAR_SHAPES_WIDE;
    final Map<Direction, @Nullable VoxelShape> barShapesEdge =
        BAR_SHAPES_EDGE_WIDE;
    switch (axis) {
      case X:
        if (!(left || right))
          return VoxelShapes.union(
              barShapesEdge.get(Direction.SOUTH), barShapesEdge.get(Direction.NORTH));
        else
          return VoxelShapes.union(
              !left ? barShapes.get(Direction.SOUTH) : VoxelShapes.empty(),
              !right ? barShapes.get(Direction.NORTH) : VoxelShapes.empty());
      case Z:
        if (!(left || right))
          return VoxelShapes.union(
              barShapesEdge.get(Direction.WEST), barShapesEdge.get(Direction.EAST));
        else
          return VoxelShapes.union(
              !left ? barShapes.get(Direction.WEST) : VoxelShapes.empty(),
              !right ? barShapes.get(Direction.EAST) : VoxelShapes.empty());
      default:
        return VoxelShapes.empty();
    }
  }

  @Override
  public VoxelShape getSidesShape(BlockState state, BlockView world, BlockPos pos) {
    return getOutlineShape(state, world, pos, ShapeContext.absent());
  }

  @Override
  public VoxelShape getCollisionShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
    final Direction.Axis axis = state.get(AXIS);
    final Boolean left = state.get(LEFT);
    final Boolean right = state.get(RIGHT);
    if (left && right) {
      return BAR_SHAPE_CENTRAL;
    }
    final Map<Direction, @Nullable VoxelShape> barShapes =
        BAR_SHAPES;
    final Map<Direction, @Nullable VoxelShape> barShapesEdge =
        BAR_SHAPES_EDGE;
    switch (axis) {
      case X:
        if (!(left || right))
          return VoxelShapes.union(
              barShapesEdge.get(Direction.SOUTH), barShapesEdge.get(Direction.NORTH));
        else
          return VoxelShapes.union(
              !left ? barShapes.get(Direction.SOUTH) : VoxelShapes.empty(),
              !right ? barShapes.get(Direction.NORTH) : VoxelShapes.empty());
      case Z:
        if (!(left || right))
          return VoxelShapes.union(
              barShapesEdge.get(Direction.WEST), barShapesEdge.get(Direction.EAST));
        else
          return VoxelShapes.union(
              !left ? barShapes.get(Direction.WEST) : VoxelShapes.empty(),
              !right ? barShapes.get(Direction.EAST) : VoxelShapes.empty());
      default:
        return VoxelShapes.empty();
    }
  }

  @Override
  protected VoxelShape getCullingShape(BlockState state) {
    return getCollisionShape(state, EmptyBlockView.INSTANCE, BlockPos.ORIGIN, ShapeContext.absent());
  }

  @Override
  protected BlockState getStateForNeighborUpdate(BlockState state, WorldView world, ScheduledTickView tickView, BlockPos pos, Direction direction, BlockPos neighborPos, BlockState neighborState, Random random) {
    state = super.getStateForNeighborUpdate(state, world, tickView, pos, direction, neighborPos, neighborState, random);
    if (state.get(WATERLOGGED)) {
      tickView.scheduleFluidTick(pos, Fluids.WATER, Fluids.WATER.getTickRate(world));
    }
    if (direction == Direction.DOWN) {
      final Block neighborBlock = neighborState.getBlock();
      if (neighborBlock instanceof HungSignBlock || neighborBlock instanceof HungSignBarBlock) {
        state = state
            .with(AXIS, neighborState.get(AXIS))
            .with(LEFT, neighborState.get(LEFT))
            .with(RIGHT, neighborState.get(RIGHT));
      } else state = state.with(LEFT, true).with(RIGHT, true);
    }
    return state;
  }

  /**
   * 和 {@link HungSignBlock#rotate} 一致。
   */
  @Override
  public BlockState rotate(BlockState state, BlockRotation rotation) {
    final Direction.Axis oldAxis = state.get(AXIS);
    state = super.rotate(state, rotation)
        .with(
            AXIS,
            rotation == BlockRotation.CLOCKWISE_90
                || rotation == BlockRotation.COUNTERCLOCKWISE_90
                ? (oldAxis == Direction.Axis.X ? Direction.Axis.Z : Direction.Axis.X)
                : oldAxis);
    if (rotation == BlockRotation.CLOCKWISE_180
        || (oldAxis == Direction.Axis.X && rotation == BlockRotation.COUNTERCLOCKWISE_90)
        || (oldAxis == Direction.Axis.Z && rotation == BlockRotation.CLOCKWISE_90)) {
      state = state.with(LEFT, state.get(RIGHT)).with(RIGHT, state.get(LEFT));
    }
    return state;
  }

  @Override
  public BlockState mirror(BlockState state, BlockMirror mirror) {
    state = super.mirror(state, mirror);
    final Direction.Axis axis = state.get(AXIS);
    if ((axis == Direction.Axis.Z && mirror == BlockMirror.FRONT_BACK) || (axis == Direction.Axis.X && mirror == BlockMirror.LEFT_RIGHT)) {
      state = state.with(LEFT, state.get(RIGHT)).with(RIGHT, state.get(LEFT));
    }
    return state;
  }

  @Override
  public MutableText getName() {
    if (baseBlock != null) {
      return TextBridge.translatable("block.mishanguc.hung_sign_bar", baseBlock.getName());
    }
    return super.getName();
  }

  @Override
  public void registerModels(ModelProvider modelProvider, BlockStateModelGenerator blockStateModelGenerator) {
    final TextureMap textures = TextureMap.texture(getBaseTexture());
    final Identifier modelId = MishangucModels.HUNG_SIGN_BAR.upload(this, textures, blockStateModelGenerator.modelCollector);
    final Identifier centralModelId = MishangucModels.HUNG_SIGN_BAR_CENTRAL.upload(this, textures, blockStateModelGenerator.modelCollector);
    final Identifier edgeModelId = MishangucModels.HUNG_SIGN_BAR_EDGE.upload(this, textures, blockStateModelGenerator.modelCollector);

    blockStateModelGenerator.blockStateCollector.accept(createBlockStates(modelId, centralModelId, edgeModelId));
    blockStateModelGenerator.registerParentedItemModel(this, modelId);
  }

  public @Nullable BlockStateSupplier createBlockStates(Identifier modelId, Identifier centralModelId, Identifier edgeModelId) {
    return MultipartBlockStateSupplier.create(this)
        .with(When.create().set(LEFT, true).set(RIGHT, true), BlockStateVariant.create().put(VariantSettings.MODEL, centralModelId).put(VariantSettings.UVLOCK, true))
        .with(When.create().set(AXIS, Direction.Axis.Z).set(LEFT, false).set(RIGHT, true), BlockStateVariant.create().put(VariantSettings.MODEL, modelId).put(VariantSettings.UVLOCK, true))
        .with(When.create().set(AXIS, Direction.Axis.Z).set(LEFT, true).set(RIGHT, false), BlockStateVariant.create().put(VariantSettings.MODEL, modelId).put(VariantSettings.UVLOCK, true).put(MishangUtils.INT_Y_VARIANT, 180))
        .with(When.create().set(AXIS, Direction.Axis.X).set(LEFT, false).set(RIGHT, true), BlockStateVariant.create().put(VariantSettings.MODEL, modelId).put(VariantSettings.UVLOCK, true).put(MishangUtils.INT_Y_VARIANT, -90))
        .with(When.create().set(AXIS, Direction.Axis.X).set(LEFT, true).set(RIGHT, false), BlockStateVariant.create().put(VariantSettings.MODEL, modelId).put(VariantSettings.UVLOCK, true).put(MishangUtils.INT_Y_VARIANT, 90))
        .with(When.create().set(AXIS, Direction.Axis.Z).set(LEFT, false).set(RIGHT, false), BlockStateVariant.create().put(VariantSettings.MODEL, edgeModelId).put(VariantSettings.UVLOCK, true))
        .with(When.create().set(AXIS, Direction.Axis.Z).set(LEFT, false).set(RIGHT, false), BlockStateVariant.create().put(VariantSettings.MODEL, edgeModelId).put(VariantSettings.UVLOCK, true).put(MishangUtils.INT_Y_VARIANT, 180))
        .with(When.create().set(AXIS, Direction.Axis.X).set(LEFT, false).set(RIGHT, false), BlockStateVariant.create().put(VariantSettings.MODEL, edgeModelId).put(VariantSettings.UVLOCK, true).put(MishangUtils.INT_Y_VARIANT, 90))
        .with(When.create().set(AXIS, Direction.Axis.X).set(LEFT, false).set(RIGHT, false), BlockStateVariant.create().put(VariantSettings.MODEL, edgeModelId).put(VariantSettings.UVLOCK, true).put(MishangUtils.INT_Y_VARIANT, 270));
  }

  public Identifier getBaseTexture() {
    if (texture != null) return texture;
    return ModelHelper.getTextureOf(baseBlock == null ? this : baseBlock);
  }

  private @Nullable String getRecipeGroup() {
    if (baseBlock instanceof ColoredBlock) return null;
    if (MishangUtils.isWood(baseBlock)) return "mishanguc:wood_hung_sign_bar";
    if (MishangUtils.isStrippedWood(baseBlock)) return "mishanguc:stripped_wood_hung_sign_bar";
    if (MishangUtils.isConcrete(baseBlock)) return "mishanguc:concrete_hung_sign_bar";
    if (MishangUtils.isTerracotta(baseBlock)) return "mishanguc:terracotta_hung_sign_bar";
    if (baseBlock == Blocks.ICE || baseBlock == Blocks.PACKED_ICE || baseBlock == Blocks.BLUE_ICE) {
      return "mishanguc:ice_hung_sign_bar";
    }
    return null;
  }

  @Override
  public CraftingRecipeJsonBuilder getCraftingRecipe(RecipeGenerator recipeGenerator) {
    return StonecuttingRecipeJsonBuilder.createStonecutting(
            Ingredient.ofItems(baseBlock),
            RecipeCategory.DECORATIONS,
            this,
            20)
        .criterion("has_base_block", recipeGenerator.conditionsFromItem(baseBlock))
        .group(getRecipeGroup());
  }

  @Override
  protected MapCodec<? extends HungSignBarBlock> getCodec() {
    return CODEC;
  }

  @Override
  public String customRecipeCategory() {
    return "signs";
  }
}
