package pers.solid.mishang.uc.block;

import com.google.common.collect.BiMap;
import com.google.common.collect.ImmutableBiMap;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.data.models.BlockModelGenerators;
import net.minecraft.client.data.models.ModelProvider;
import net.minecraft.client.data.models.blockstates.MultiVariantGenerator;
import net.minecraft.client.data.models.blockstates.PropertyDispatch;
import net.minecraft.client.data.models.model.ModelTemplate;
import net.minecraft.client.data.models.model.TextureMapping;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.recipes.RecipeBuilder;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.data.recipes.SingleItemRecipeBuilder;
import net.minecraft.resources.Identifier;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.ScheduledTickAccess;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.shapes.BooleanOp;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;
import pers.solid.mishang.uc.MishangUtils;
import pers.solid.mishang.uc.data.MishangucModels;
import pers.solid.mishang.uc.data.MishangucTextureKeys;

import java.util.Map;

public class WallLightBlock extends DirectionalBlock implements SimpleWaterloggedBlock, MishangucBlock {
  public static final MapCodec<WallLightBlock> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(Codec.STRING.fieldOf("light_color").forGetter(b -> b.lightColor), propertiesCodec(), Codec.BOOL.fieldOf("large_shape").forGetter(b -> b.largeShape)).apply(instance, WallLightBlock::new));
  protected static final BooleanProperty WEST = BlockStateProperties.WEST;
  protected static final BooleanProperty EAST = BlockStateProperties.EAST;
  protected static final BooleanProperty SOUTH = BlockStateProperties.SOUTH;
  protected static final BooleanProperty NORTH = BlockStateProperties.NORTH;
  protected static final BooleanProperty UP = BlockStateProperties.UP;
  protected static final BooleanProperty DOWN = BlockStateProperties.DOWN;
  protected static final BiMap<Direction, BooleanProperty> DIRECTION_TO_PROPERTY = new ImmutableBiMap.Builder<Direction, BooleanProperty>()
      .put(Direction.WEST, WEST)
      .put(Direction.EAST, EAST)
      .put(Direction.SOUTH, SOUTH)
      .put(Direction.NORTH, NORTH)
      .put(Direction.UP, UP)
      .put(Direction.DOWN, DOWN)
      .build();
  private static final Map<Direction, VoxelShape> SHAPE_PER_DIRECTION = MishangUtils.createDirectionToShape(4, 0, 4, 12, 2, 12);
  private static final Map<Direction, VoxelShape> LARGE_SHAPE_PER_DIRECTION = MishangUtils.createDirectionToShape(2, 0, 2, 14, 2, 14);
  public final String lightColor;
  protected final boolean largeShape;

  public WallLightBlock(String lightColor, Properties settings, boolean largeShape) {
    super(settings);
    this.lightColor = lightColor;
    this.largeShape = largeShape;
    this.registerDefaultState(defaultBlockState()
        .setValue(BlockStateProperties.WATERLOGGED, false)
        .setValue(FACING, Direction.UP));
  }

  @Override
  public boolean canSurvive(BlockState state, LevelReader world, BlockPos pos) {
    final Direction facing = state.getValue(FACING);
    final Direction backDirection = facing.getOpposite();
    final BlockPos backPos = pos.relative(backDirection);
    final VoxelShape centerShape = Block.box(7, 7, 7, 9, 9, 9);
    final BlockState backState = world.getBlockState(backPos);
    return !Shapes.joinIsNotEmpty(backState.getBlockSupportShape(world, backPos).getFaceShape(facing), centerShape, BooleanOp.ONLY_SECOND) || !Shapes.joinIsNotEmpty(backState.getCollisionShape(world, backPos).getFaceShape(facing), centerShape, BooleanOp.ONLY_SECOND);
  }

  @Override
  protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
    builder.add(FACING, BlockStateProperties.WATERLOGGED);
  }

  @Override
  protected BlockState updateShape(BlockState state, LevelReader world, ScheduledTickAccess tickView, BlockPos pos, Direction direction, BlockPos neighborPos, BlockState neighborState, RandomSource random) {
    if (state.getValue(BlockStateProperties.WATERLOGGED)) {
      tickView.scheduleTick(pos, Fluids.WATER, Fluids.WATER.getTickDelay(world));
    }
    return super.updateShape(state, world, tickView, pos, direction, neighborPos, neighborState, random);
  }

  @Override
  public BlockState rotate(BlockState state, Rotation rotation) {
    return state.setValue(FACING, rotation.rotate(state.getValue(FACING)));
  }

  @Override
  public BlockState mirror(BlockState state, Mirror mirror) {
    return state.setValue(FACING, mirror.mirror(state.getValue(FACING)));
  }

  @Override
  public BlockState getStateForPlacement(BlockPlaceContext ctx) {
    Direction direction = ctx.getClickedFace();
    BlockState blockState =
        ctx.getLevel().getBlockState(ctx.getClickedPos().relative(direction.getOpposite()));
    if (blockState.is(this)) {
      blockState.getValue(FACING);
    }
    return this.defaultBlockState()
        .setValue(FACING, direction)
        .setValue(BlockStateProperties.WATERLOGGED,
            ctx.getLevel().getBlockState(ctx.getClickedPos()).getFluidState().getType()
                == Fluids.WATER);
  }

  @Override
  public FluidState getFluidState(BlockState state) {
    return state.getValue(BlockStateProperties.WATERLOGGED)
        ? Fluids.WATER.getSource(false)
        : super.getFluidState(state);
  }

  @Override
  public VoxelShape getShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext context) {
    return (largeShape ? LARGE_SHAPE_PER_DIRECTION : SHAPE_PER_DIRECTION).get(state.getValue(FACING));
  }

  @Override
  public void updateIndirectNeighbourShapes(BlockState state, LevelAccessor world, BlockPos pos, int flags, int maxUpdateDepth) {
    super.updateIndirectNeighbourShapes(state, world, pos, flags, maxUpdateDepth);
    final Direction facing = state.getValue(FACING);
    if (this instanceof final LightConnectable lightConnectable) {
      lightConnectable.prepareConnection(state, world, pos, flags, maxUpdateDepth, facing);
    }
  }

  @Environment(EnvType.CLIENT)
  @Override
  public void registerModels(ModelProvider modelProvider, BlockModelGenerators blockStateModelGenerator) {
    final Identifier id = getModelType().create(this, getTextureMap(), blockStateModelGenerator.modelOutput);
    final var map = PropertyDispatch.modify(FACING);
    map.select(Direction.UP, BlockModelGenerators.NOP);
    map.select(Direction.DOWN, BlockModelGenerators.X_ROT_180);
    map.select(Direction.SOUTH, BlockModelGenerators.X_ROT_270.then(BlockModelGenerators.NOP));
    map.select(Direction.WEST, BlockModelGenerators.X_ROT_270.then(BlockModelGenerators.Y_ROT_90));
    map.select(Direction.NORTH, BlockModelGenerators.X_ROT_270.then(BlockModelGenerators.Y_ROT_180));
    map.select(Direction.EAST, BlockModelGenerators.X_ROT_270.then(BlockModelGenerators.Y_ROT_270));
    blockStateModelGenerator.blockStateOutput.accept(MultiVariantGenerator.dispatch(this, BlockModelGenerators.plainVariant(id)).with(map).with(BlockModelGenerators.UV_LOCK));
    blockStateModelGenerator.registerSimpleItemModel(this, id);
  }

  protected TextureMapping getTextureMap() {
    return TextureMapping.singleSlot(MishangucTextureKeys.LIGHT, MishangucModels.material(lightColor + "_light"));
  }

  public ModelTemplate getModelType() {
    return getModelType("");
  }

  public ModelTemplate getModelType(String suffix) {
    final Identifier identifier = BuiltInRegistries.BLOCK.getKey(this);
    String path = identifier.getPath() + suffix;
    final int i = lightColor.length();
    if (path.startsWith(lightColor) && path.charAt(i) == '_') {
      path = path.substring(i + 1);
    } else {
      throw new AssertionError();
    }
    return MishangucModels.createBlock(path, suffix, MishangucTextureKeys.LIGHT);
  }

  @Override
  public RecipeBuilder getCraftingRecipe(RecipeProvider recipeGenerator) {
    final Identifier itemId = BuiltInRegistries.ITEM.getKey(asItem());
    final String itemPath = itemId.getPath();
    if (itemPath.endsWith("_tube")) {
      // 灯管方式采用切石的方式合成，这里直接作为主要的合成方式。
      final @NotNull Item fullLight = getBaseLight(itemId.getNamespace(), lightColor, this);
      final int outputCount;
      if (itemPath.contains("_small_")) {
        outputCount = 64;
      } else if (itemPath.contains("_medium_")) {
        outputCount = 32;
      } else if (itemPath.contains("_large_")) {
        outputCount = 16;
      } else if (itemPath.contains("_thin_strip_")) {
        outputCount = 36;
      } else if (itemPath.contains("_double_strip_")) {
        outputCount = 18;
      } else if (itemPath.contains("_thick_strip_")) {
        outputCount = 12;
      } else {
        throw new IllegalStateException(String.format("Can't generate recipes: Cannot determine the type of %s according to its id", this));
      }
      return SingleItemRecipeBuilder.stonecutting(Ingredient.of(fullLight), RecipeCategory.DECORATIONS, this, outputCount)
          .unlockedBy(RecipeProvider.getHasName(fullLight), recipeGenerator.has(fullLight));
    } else {
      // 非灯管方块，采用与混凝土的合成。
      final Identifier tubeId = itemId.withSuffix("_tube");
      final @NotNull Item tube = BuiltInRegistries.ITEM.getOptional(tubeId).orElseThrow(() -> new IllegalArgumentException(String.format("Can't generate recipes: %s does not have a corresponding tube block (with id [%s])", this, tubeId)));
      return recipeGenerator.shapeless(RecipeCategory.DECORATIONS, this, 1)
          .requires(tube)
          .requires(Items.CONCRETE.gray())
          .unlockedBy(RecipeProvider.getHasName(tube), recipeGenerator.has(tube));
    }
  }

  @Override
  protected MapCodec<? extends WallLightBlock> codec() {
    return CODEC;
  }

  public static Item getBaseLight(String namespace, String lightColor, Block self) {
    final Identifier fullLightId = Identifier.fromNamespaceAndPath(namespace, lightColor + "_light");
    return BuiltInRegistries.ITEM.getOptional(fullLightId).orElseThrow(() -> new IllegalArgumentException(String.format("Can't generate recipes: %s does not have a corresponding base light block (with id [%s])", self, fullLightId)));
  }

  @Override
  public String customRecipeCategory() {
    return "light";
  }
}
