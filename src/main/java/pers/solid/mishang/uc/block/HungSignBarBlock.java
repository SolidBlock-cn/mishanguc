package pers.solid.mishang.uc.block;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.data.models.BlockModelGenerators;
import net.minecraft.client.data.models.ModelProvider;
import net.minecraft.client.data.models.blockstates.BlockModelDefinitionGenerator;
import net.minecraft.client.data.models.blockstates.MultiPartGenerator;
import net.minecraft.client.data.models.model.ItemModelUtils;
import net.minecraft.client.data.models.model.TextureMapping;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.recipes.RecipeBuilder;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.data.recipes.SingleItemRecipeBuilder;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.Identifier;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.*;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;
import pers.solid.mishang.uc.MishangUtils;
import pers.solid.mishang.uc.data.MishangucModels;
import pers.solid.mishang.uc.data.ModelHelper;
import pers.solid.mishang.uc.item.ColoredTintSource;
import pers.solid.mishang.uc.util.TextBridge;

import java.util.Map;

/**
 * 悬挂的告示牌上面的专用的悬挂物方块。其方块状态会与其下方的悬挂告示牌方块同步。
 */
public class HungSignBarBlock extends Block implements SimpleWaterloggedBlock, MishangucBlock {
  public static final MapCodec<ColoredHungSignBarBlock> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(createBaseBlockCodec(), propertiesCodec()).apply(instance, ColoredHungSignBarBlock::new));

  protected static <B extends HungSignBarBlock> RecordCodecBuilder<B, Block> createBaseBlockCodec() {
    return BuiltInRegistries.BLOCK.byNameCodec().fieldOf("base_block").forGetter(b -> b.baseBlock);
  }

  public static final EnumProperty<Direction.Axis> AXIS = BlockStateProperties.HORIZONTAL_AXIS;
  public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;
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
  private static final VoxelShape BAR_SHAPE_CENTRAL = box(7.5, 0, 7.5, 8.5, 16, 8.5);

  private static final VoxelShape BAR_SHAPE_CENTRAL_WIDE = box(6.5, 0, 6.5, 9.5, 16, 9.5);
  public final @Nullable Block baseBlock;
  /**
   * 告示牌杆的纹理。若为 {@code null}，则根据其 {@link #baseBlock} 的 id 来推断。
   */
  public Identifier texture;

  public HungSignBarBlock(@Nullable Block baseBlock, Properties settings) {
    super(settings);
    this.baseBlock = baseBlock;
    this.registerDefaultState(defaultBlockState()
        .setValue(WATERLOGGED, false)
        .setValue(AXIS, Direction.Axis.X)
        .setValue(LEFT, true)
        .setValue(RIGHT, true));
  }

  @Override
  protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
    super.createBlockStateDefinition(builder);
    builder.add(AXIS, WATERLOGGED, LEFT, RIGHT);
  }

  @Nullable
  @Override
  public BlockState getStateForPlacement(BlockPlaceContext ctx) {
    final BlockState placementState = super.getStateForPlacement(ctx);
    if (placementState == null) {
      return null;
    }
    final Level world = ctx.getLevel();
    final BlockPos blockPos = ctx.getClickedPos();
    final BlockPos downPos = blockPos.below();
    final BlockState downState = world.getBlockState(downPos);

    // 考虑放置之初，底部若为悬挂的告示牌方块，则该方块没有连接，因此在
    // getStateForNeighborUpdate 的时候，将 neighborState 设为假定连接后的 state。
    // 注意，悬挂告示牌方块的 getStateForNeighborUpdate 并不会检查其上方的告示牌杆的属性是否匹配，只要存在就行。
    return placementState.updateShape(
            world,
            world,
            blockPos,
            Direction.DOWN,
            downPos,
            downState
                .updateShape(world, world, downPos, Direction.UP, blockPos, placementState, world.getRandom()),
            world.getRandom())
        .setValue(WATERLOGGED, world.getFluidState(blockPos).getType() == Fluids.WATER);
  }

  @Override
  public FluidState getFluidState(BlockState state) {
    return state.getValue(WATERLOGGED) ? Fluids.WATER.getSource(false) : super.getFluidState(state);
  }

  @Override
  public VoxelShape getShape(
      BlockState state, BlockGetter world, BlockPos pos, CollisionContext context) {
    final Direction.Axis axis = state.getValue(AXIS);
    final Boolean left = state.getValue(LEFT);
    final Boolean right = state.getValue(RIGHT);

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
          return Shapes.or(
              barShapesEdge.get(Direction.SOUTH), barShapesEdge.get(Direction.NORTH));
        else
          return Shapes.or(
              !left ? barShapes.get(Direction.SOUTH) : Shapes.empty(),
              !right ? barShapes.get(Direction.NORTH) : Shapes.empty());
      case Z:
        if (!(left || right))
          return Shapes.or(
              barShapesEdge.get(Direction.WEST), barShapesEdge.get(Direction.EAST));
        else
          return Shapes.or(
              !left ? barShapes.get(Direction.WEST) : Shapes.empty(),
              !right ? barShapes.get(Direction.EAST) : Shapes.empty());
      default:
        return Shapes.empty();
    }
  }

  @Override
  public VoxelShape getBlockSupportShape(BlockState state, BlockGetter world, BlockPos pos) {
    return getShape(state, world, pos, CollisionContext.empty());
  }

  @Override
  public VoxelShape getCollisionShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext context) {
    final Direction.Axis axis = state.getValue(AXIS);
    final Boolean left = state.getValue(LEFT);
    final Boolean right = state.getValue(RIGHT);
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
          return Shapes.or(
              barShapesEdge.get(Direction.SOUTH), barShapesEdge.get(Direction.NORTH));
        else
          return Shapes.or(
              !left ? barShapes.get(Direction.SOUTH) : Shapes.empty(),
              !right ? barShapes.get(Direction.NORTH) : Shapes.empty());
      case Z:
        if (!(left || right))
          return Shapes.or(
              barShapesEdge.get(Direction.WEST), barShapesEdge.get(Direction.EAST));
        else
          return Shapes.or(
              !left ? barShapes.get(Direction.WEST) : Shapes.empty(),
              !right ? barShapes.get(Direction.EAST) : Shapes.empty());
      default:
        return Shapes.empty();
    }
  }

  @Override
  protected VoxelShape getOcclusionShape(BlockState state) {
    return getCollisionShape(state, EmptyBlockGetter.INSTANCE, BlockPos.ZERO, CollisionContext.empty());
  }

  @Override
  protected BlockState updateShape(BlockState state, LevelReader world, ScheduledTickAccess tickView, BlockPos pos, Direction direction, BlockPos neighborPos, BlockState neighborState, RandomSource random) {
    state = super.updateShape(state, world, tickView, pos, direction, neighborPos, neighborState, random);
    if (state.getValue(WATERLOGGED)) {
      tickView.scheduleTick(pos, Fluids.WATER, Fluids.WATER.getTickDelay(world));
    }
    if (direction == Direction.DOWN) {
      final Block neighborBlock = neighborState.getBlock();
      if (neighborBlock instanceof HungSignBlock || neighborBlock instanceof HungSignBarBlock) {
        state = state
            .setValue(AXIS, neighborState.getValue(AXIS))
            .setValue(LEFT, neighborState.getValue(LEFT))
            .setValue(RIGHT, neighborState.getValue(RIGHT));
      } else state = state.setValue(LEFT, true).setValue(RIGHT, true);
    }
    return state;
  }

  /**
   * 和 {@link HungSignBlock#rotate} 一致。
   */
  @Override
  public BlockState rotate(BlockState state, Rotation rotation) {
    final Direction.Axis oldAxis = state.getValue(AXIS);
    state = super.rotate(state, rotation)
        .setValue(
            AXIS,
            rotation == Rotation.CLOCKWISE_90
                || rotation == Rotation.COUNTERCLOCKWISE_90
                ? (oldAxis == Direction.Axis.X ? Direction.Axis.Z : Direction.Axis.X)
                : oldAxis);
    if (rotation == Rotation.CLOCKWISE_180
        || (oldAxis == Direction.Axis.X && rotation == Rotation.COUNTERCLOCKWISE_90)
        || (oldAxis == Direction.Axis.Z && rotation == Rotation.CLOCKWISE_90)) {
      state = state.setValue(LEFT, state.getValue(RIGHT)).setValue(RIGHT, state.getValue(LEFT));
    }
    return state;
  }

  @Override
  public BlockState mirror(BlockState state, Mirror mirror) {
    state = super.mirror(state, mirror);
    final Direction.Axis axis = state.getValue(AXIS);
    if ((axis == Direction.Axis.Z && mirror == Mirror.FRONT_BACK) || (axis == Direction.Axis.X && mirror == Mirror.LEFT_RIGHT)) {
      state = state.setValue(LEFT, state.getValue(RIGHT)).setValue(RIGHT, state.getValue(LEFT));
    }
    return state;
  }

  @Override
  public MutableComponent getName() {
    if (baseBlock != null) {
      return TextBridge.translatable("block.mishanguc.hung_sign_bar", baseBlock.getName());
    }
    return super.getName();
  }

  @Environment(EnvType.CLIENT)
  @Override
  public void registerModels(ModelProvider modelProvider, BlockModelGenerators blockStateModelGenerator) {
    final TextureMapping textures = TextureMapping.defaultTexture(getBaseTexture());
    final Identifier modelId = MishangucModels.HUNG_SIGN_BAR.create(this, textures, blockStateModelGenerator.modelOutput);
    final Identifier centralModelId = MishangucModels.HUNG_SIGN_BAR_CENTRAL.create(this, textures, blockStateModelGenerator.modelOutput);
    final Identifier edgeModelId = MishangucModels.HUNG_SIGN_BAR_EDGE.create(this, textures, blockStateModelGenerator.modelOutput);

    blockStateModelGenerator.blockStateOutput.accept(createBlockStates(modelId, centralModelId, edgeModelId));
    if (this instanceof ColoredBlock) {
      blockStateModelGenerator.itemModelOutput.accept(asItem(), ItemModelUtils.tintedModel(modelId, ColoredTintSource.INSTANCE, ColoredTintSource.INSTANCE));
    } else {
      blockStateModelGenerator.registerSimpleItemModel(this, modelId);
    }
  }

  @Environment(EnvType.CLIENT)
  public @Nullable BlockModelDefinitionGenerator createBlockStates(Identifier modelId, Identifier centralModelId, Identifier edgeModelId) {
    return MultiPartGenerator.multiPart(this)
        .with(BlockModelGenerators.condition().term(LEFT, true).term(RIGHT, true), BlockModelGenerators.plainVariant(centralModelId).with(BlockModelGenerators.UV_LOCK))
        .with(BlockModelGenerators.condition().term(AXIS, Direction.Axis.Z).term(LEFT, false).term(RIGHT, true), BlockModelGenerators.plainVariant(modelId).with(BlockModelGenerators.UV_LOCK))
        .with(BlockModelGenerators.condition().term(AXIS, Direction.Axis.Z).term(LEFT, true).term(RIGHT, false), BlockModelGenerators.plainVariant(modelId).with(BlockModelGenerators.UV_LOCK).with(BlockModelGenerators.Y_ROT_180))
        .with(BlockModelGenerators.condition().term(AXIS, Direction.Axis.X).term(LEFT, false).term(RIGHT, true), BlockModelGenerators.plainVariant(modelId).with(BlockModelGenerators.UV_LOCK).with(BlockModelGenerators.Y_ROT_270))
        .with(BlockModelGenerators.condition().term(AXIS, Direction.Axis.X).term(LEFT, true).term(RIGHT, false), BlockModelGenerators.plainVariant(modelId).with(BlockModelGenerators.UV_LOCK).with(BlockModelGenerators.Y_ROT_90))
        .with(BlockModelGenerators.condition().term(AXIS, Direction.Axis.Z).term(LEFT, false).term(RIGHT, false), BlockModelGenerators.plainVariant(edgeModelId).with(BlockModelGenerators.UV_LOCK))
        .with(BlockModelGenerators.condition().term(AXIS, Direction.Axis.Z).term(LEFT, false).term(RIGHT, false), BlockModelGenerators.plainVariant(edgeModelId).with(BlockModelGenerators.UV_LOCK).with(BlockModelGenerators.Y_ROT_180))
        .with(BlockModelGenerators.condition().term(AXIS, Direction.Axis.X).term(LEFT, false).term(RIGHT, false), BlockModelGenerators.plainVariant(edgeModelId).with(BlockModelGenerators.UV_LOCK).with(BlockModelGenerators.Y_ROT_90))
        .with(BlockModelGenerators.condition().term(AXIS, Direction.Axis.X).term(LEFT, false).term(RIGHT, false), BlockModelGenerators.plainVariant(edgeModelId).with(BlockModelGenerators.UV_LOCK).with(BlockModelGenerators.Y_ROT_270));
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
  public RecipeBuilder getCraftingRecipe(RecipeProvider recipeGenerator) {
    return SingleItemRecipeBuilder.stonecutting(
            Ingredient.of(baseBlock),
            RecipeCategory.DECORATIONS,
            this,
            20)
        .unlockedBy("has_base_block", recipeGenerator.has(baseBlock))
        .group(getRecipeGroup());
  }

  @Override
  protected MapCodec<? extends HungSignBarBlock> codec() {
    return CODEC;
  }

  @Override
  public String customRecipeCategory() {
    return "signs";
  }

  @Override
  protected boolean isPathfindable(BlockState state, PathComputationType type) {
    return false;
  }
}
