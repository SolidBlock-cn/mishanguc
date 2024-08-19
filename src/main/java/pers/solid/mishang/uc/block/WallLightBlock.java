package pers.solid.mishang.uc.block;

import com.google.common.collect.BiMap;
import com.google.common.collect.ImmutableBiMap;
import net.minecraft.block.*;
import net.minecraft.data.client.*;
import net.minecraft.data.server.recipe.CraftingRecipeJsonBuilder;
import net.minecraft.data.server.recipe.RecipeProvider;
import net.minecraft.data.server.recipe.ShapelessRecipeJsonBuilder;
import net.minecraft.data.server.recipe.SingleItemRecipeJsonBuilder;
import net.minecraft.fluid.FluidState;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.Item;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.Items;
import net.minecraft.recipe.Ingredient;
import net.minecraft.recipe.book.RecipeCategory;
import net.minecraft.registry.Registries;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.util.BlockMirror;
import net.minecraft.util.BlockRotation;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;
import net.minecraft.world.WorldAccess;
import net.minecraft.world.WorldView;
import org.jetbrains.annotations.NotNull;
import pers.solid.mishang.uc.MishangUtils;
import pers.solid.mishang.uc.data.MishangucModels;
import pers.solid.mishang.uc.data.MishangucTextureKeys;

import java.util.Map;

public class WallLightBlock extends FacingBlock implements Waterloggable, MishangucBlock {
  protected static final BooleanProperty WEST = Properties.WEST;
  protected static final BooleanProperty EAST = Properties.EAST;
  protected static final BooleanProperty SOUTH = Properties.SOUTH;
  protected static final BooleanProperty NORTH = Properties.NORTH;
  protected static final BooleanProperty UP = Properties.UP;
  protected static final BooleanProperty DOWN = Properties.DOWN;
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

  public WallLightBlock(String lightColor, Settings settings, boolean largeShape) {
    super(settings);
    this.lightColor = lightColor;
    this.largeShape = largeShape;
    this.setDefaultState(getDefaultState()
        .with(Properties.WATERLOGGED, false)
        .with(FACING, Direction.UP));
  }

  @SuppressWarnings("deprecation")
  @Override
  public boolean canPlaceAt(BlockState state, WorldView world, BlockPos pos) {
    Direction direction = state.get(FACING).getOpposite();
    BlockPos blockPos = pos.offset(direction);
    final BlockState blockState = world.getBlockState(blockPos);
    return !blockState.getSidesShape(world, pos).getFace(direction.getOpposite()).isEmpty() || !blockState.getOutlineShape(world, pos).getFace(direction.getOpposite()).isEmpty();
  }

  @Override
  protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
    builder.add(FACING, Properties.WATERLOGGED);
  }

  @SuppressWarnings("deprecation")
  @Override
  public BlockState getStateForNeighborUpdate(BlockState state, Direction direction, BlockState neighborState, WorldAccess world, BlockPos pos, BlockPos neighborPos) {
    if (state.get(Properties.WATERLOGGED)) {
      world.scheduleFluidTick(pos, Fluids.WATER, Fluids.WATER.getTickRate(world));
    }

    return super.getStateForNeighborUpdate(state, direction, neighborState, world, pos, neighborPos);
  }

  @SuppressWarnings("deprecation")
  @Override
  public BlockState rotate(BlockState state, BlockRotation rotation) {
    return state.with(FACING, rotation.rotate(state.get(FACING)));
  }

  @SuppressWarnings("deprecation")
  @Override
  public BlockState mirror(BlockState state, BlockMirror mirror) {
    return state.with(FACING, mirror.apply(state.get(FACING)));
  }

  @Override
  public BlockState getPlacementState(ItemPlacementContext ctx) {
    Direction direction = ctx.getSide();
    BlockState blockState =
        ctx.getWorld().getBlockState(ctx.getBlockPos().offset(direction.getOpposite()));
    if (blockState.isOf(this)) {
      blockState.get(FACING);
    }
    return this.getDefaultState()
        .with(FACING, direction)
        .with(Properties.WATERLOGGED,
            ctx.getWorld().getBlockState(ctx.getBlockPos()).getFluidState().getFluid()
                == Fluids.WATER);
  }

  @SuppressWarnings("deprecation")
  @Override
  public FluidState getFluidState(BlockState state) {
    return state.get(Properties.WATERLOGGED)
        ? Fluids.WATER.getStill(false)
        : super.getFluidState(state);
  }

  @SuppressWarnings("deprecation")
  @Override
  public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
    return (largeShape ? LARGE_SHAPE_PER_DIRECTION : SHAPE_PER_DIRECTION).get(state.get(FACING));
  }

  @SuppressWarnings("deprecation")
  @Override
  public void prepare(BlockState state, WorldAccess world, BlockPos pos, int flags, int maxUpdateDepth) {
    super.prepare(state, world, pos, flags, maxUpdateDepth);
    final Direction facing = state.get(FACING);
    if (this instanceof final LightConnectable lightConnectable) {
      lightConnectable.prepareConnection(state, world, pos, flags, maxUpdateDepth, facing);
    }
  }

  @Override
  public void registerModels(ModelProvider modelProvider, BlockStateModelGenerator blockStateModelGenerator) {
    final Identifier id = getModelType().upload(this, getTextureMap(), blockStateModelGenerator.modelCollector);
    final BlockStateVariantMap.SingleProperty<Direction> map = BlockStateVariantMap.create(FACING);
    map.register(Direction.UP, BlockStateVariant.create().put(VariantSettings.MODEL, id));
    map.register(Direction.DOWN, BlockStateVariant.create().put(VariantSettings.MODEL, id).put(VariantSettings.X, VariantSettings.Rotation.R180));
    for (Direction direction : Direction.Type.HORIZONTAL) {
      map.register(direction, BlockStateVariant.create().put(VariantSettings.MODEL, id).put(VariantSettings.X, VariantSettings.Rotation.R270).put(MishangUtils.DIRECTION_Y_VARIANT, direction));
    }
    blockStateModelGenerator.blockStateCollector.accept(VariantsBlockStateSupplier.create(this, BlockStateVariant.create().put(VariantSettings.UVLOCK, true)).coordinate(map));
    blockStateModelGenerator.registerParentedItemModel(this, id);
  }

  protected TextureMap getTextureMap() {
    return TextureMap.of(MishangucTextureKeys.LIGHT, MishangucModels.texture(lightColor + "_light"));
  }

  public Model getModelType() {
    return getModelType("");
  }

  public Model getModelType(String suffix) {
    final Identifier identifier = Registries.BLOCK.getId(this);
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
  public CraftingRecipeJsonBuilder getCraftingRecipe() {
    final Identifier itemId = Registries.ITEM.getId(asItem());
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
      return SingleItemRecipeJsonBuilder.createStonecutting(Ingredient.ofItems(fullLight), RecipeCategory.DECORATIONS, this, outputCount)
          .criterion(RecipeProvider.hasItem(fullLight), RecipeProvider.conditionsFromItem(fullLight));
    } else {
      // 非灯管方块，采用与混凝土的合成。
      final Identifier tubeId = itemId.withSuffixedPath("_tube");
      final @NotNull Item tube = Registries.ITEM.getOrEmpty(tubeId).orElseThrow(() -> new IllegalArgumentException(String.format("Can't generate recipes: %s does not have a corresponding tube block (with id [%s])", this, tubeId)));
      return ShapelessRecipeJsonBuilder.create(RecipeCategory.DECORATIONS, this, 1)
          .input(tube)
          .input(Items.GRAY_CONCRETE)
          .criterion(RecipeProvider.hasItem(tube), RecipeProvider.conditionsFromItem(tube));
    }
  }

  public static @NotNull Item getBaseLight(String namespace, String lightColor, Block self) {
    final Identifier fullLightId = new Identifier(namespace, lightColor + "_light");
    return Registries.ITEM.getOrEmpty(fullLightId).orElseThrow(() -> new IllegalArgumentException(String.format("Can't generate recipes: %s does not have a corresponding base light block (with id [%s])", self, fullLightId)));
  }

  @Override
  public String customRecipeCategory() {
    return "light";
  }
}
