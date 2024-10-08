package pers.solid.mishang.uc.block;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.ShapeContext;
import net.minecraft.block.Waterloggable;
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
import net.minecraft.state.property.EnumProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.util.BlockRotation;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;
import org.jetbrains.annotations.NotNull;
import pers.solid.mishang.uc.MishangUtils;
import pers.solid.mishang.uc.data.MishangucModels;
import pers.solid.mishang.uc.data.MishangucTextureKeys;

/**
 * 柱形灯方块，且没有底座，因此没有朝向，而是直接根据的坐标轴。
 */
public class ColumnLightBlock extends Block implements Waterloggable, MishangucBlock {
  public static final MapCodec<ColumnLightBlock> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(Codec.STRING.fieldOf("light_color").forGetter(block -> block.lightColor), createSettingsCodec(), Codec.INT.fieldOf("size_type").forGetter(block -> block.sizeType)).apply(instance, ColumnLightBlock::new));
  public static final EnumProperty<Direction.Axis> AXIS = Properties.AXIS;
  public final String lightColor;
  private final int sizeType;

  public ColumnLightBlock(String lightColor, Settings settings, int sizeType) {
    super(settings);
    this.lightColor = lightColor;
    this.sizeType = sizeType;
    setDefaultState(getDefaultState().with(AXIS, Direction.Axis.X));
  }

  @Override
  protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
    super.appendProperties(builder);
    builder.add(AXIS, Properties.WATERLOGGED);
  }

  @SuppressWarnings("deprecation")
  @Override
  public BlockState getStateForNeighborUpdate(BlockState state, Direction direction, BlockState neighborState, WorldAccess world, BlockPos pos, BlockPos neighborPos) {
    if (state.get(Properties.WATERLOGGED)) {
      world.scheduleFluidTick(pos, Fluids.WATER, Fluids.WATER.getTickRate(world));
    }

    return super.getStateForNeighborUpdate(
        state, direction, neighborState, world, pos, neighborPos);
  }

  @SuppressWarnings("deprecation")
  @Override
  public BlockState rotate(BlockState state, BlockRotation rotation) {
    return super.rotate(state, rotation).with(AXIS, MishangUtils.rotateAxis(rotation, state.get(AXIS)));
  }

  @Override
  public BlockState getPlacementState(ItemPlacementContext ctx) {
    Direction direction = ctx.getSide();
    final World world = ctx.getWorld();
    final BlockPos blockPos = ctx.getBlockPos();
    BlockState blockState = world.getBlockState(blockPos.offset(direction.getOpposite()));
    if (blockState.getSidesShape(world, blockPos).getFace(direction).isEmpty() && blockState.getOutlineShape(world, blockPos).getFace(direction).isEmpty()) {
      return null;
    }
    return this.getDefaultState()
        .with(AXIS, direction.getAxis())
        .with(Properties.WATERLOGGED, world.getBlockState(blockPos).getFluidState().getFluid() == Fluids.WATER);
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
    return (sizeType >= 2 ? ColumnWallLightBlock.SHAPES4 : sizeType == 1 ? ColumnWallLightBlock.SHAPES5 : ColumnWallLightBlock.SHAPES6).get(state.get(AXIS));
  }

  @SuppressWarnings("deprecation")
  @Override
  public VoxelShape getCollisionShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
    return (sizeType >= 2 ? ColumnWallLightBlock.SHAPES5 : sizeType == 1 ? ColumnWallLightBlock.SHAPES6 : ColumnWallLightBlock.SHAPES7).get(state.get(AXIS));
  }

  @SuppressWarnings("deprecation")
  @Override
  public boolean isSideInvisible(BlockState state, BlockState stateFrom, Direction direction) {
    return stateFrom.isOf(this) && state.get(AXIS).test(direction) && stateFrom.get(AXIS).test(direction) || super.isSideInvisible(state, stateFrom, direction);
  }


  @Override
  public void registerModels(ModelProvider modelProvider, BlockStateModelGenerator blockStateModelGenerator) {
    final TextureMap textures = TextureMap.of(MishangucTextureKeys.LIGHT, MishangucModels.texture(lightColor + "_light"));
    final Identifier modelId = getModelType().upload(this, textures, blockStateModelGenerator.modelCollector);
    blockStateModelGenerator.blockStateCollector.accept(VariantsBlockStateSupplier.create(this, BlockStateVariant.create()
            .put(VariantSettings.MODEL, modelId))
        .coordinate(BlockStateVariantMap.create(AXIS)
            .register(Direction.Axis.Y, BlockStateVariant.create())
            .register(Direction.Axis.X, BlockStateVariant.create()
                .put(VariantSettings.X, VariantSettings.Rotation.R270)
                .put(VariantSettings.Y, VariantSettings.Rotation.R90))
            .register(Direction.Axis.Z, BlockStateVariant.create()
                .put(VariantSettings.X, VariantSettings.Rotation.R270))));
    blockStateModelGenerator.registerParentedItemModel(this, modelId);
  }

  public Model getModelType() {
    final Identifier identifier = Registries.BLOCK.getId(this);
    String path = identifier.getPath();
    final int i = lightColor.length();
    if (path.startsWith(lightColor) && path.charAt(i) == '_') {
      path = path.substring(i + 1);
    } else {
      throw new AssertionError();
    }
    return MishangucModels.createBlock(path, MishangucTextureKeys.LIGHT);
  }

  @Override
  public CraftingRecipeJsonBuilder getCraftingRecipe() {
    final Identifier itemId = Registries.ITEM.getId(asItem());
    final String itemPath = itemId.getPath();
    if (itemPath.endsWith("_tube")) {
      final @NotNull Item fullLight = WallLightBlock.getBaseLight(itemId.getNamespace(), lightColor, this);
      final int outputCount;
      if (itemPath.contains("_thin_")) {
        outputCount = 32;
      } else if (itemPath.contains("_medium_")) {
        outputCount = 16;
      } else if (itemPath.contains("thick")) {
        outputCount = 8;
      } else {
        throw new IllegalStateException(String.format("Can't generate recipes: Cannot determine the type of %s according to its id", this));
      }
      return SingleItemRecipeJsonBuilder.createStonecutting(Ingredient.ofItems(fullLight), RecipeCategory.DECORATIONS, this, outputCount)
          .criterion(RecipeProvider.hasItem(fullLight), RecipeProvider.conditionsFromItem(fullLight));
    } else {
      final Identifier tubeId = itemId.withSuffixedPath("_tube");
      final @NotNull Item tube = Registries.ITEM.getOrEmpty(tubeId).orElseThrow(() -> new IllegalArgumentException(String.format("Can't generate recipes: %s does not have a corresponding tube block (with id [%s])", this, tubeId)));
      return ShapelessRecipeJsonBuilder.create(RecipeCategory.DECORATIONS, this, 1)
          .input(tube)
          .input(Items.GRAY_CONCRETE)
          .criterion(RecipeProvider.hasItem(tube), RecipeProvider.conditionsFromItem(tube));
    }
  }

  @Override
  protected MapCodec<? extends ColumnLightBlock> getCodec() {
    return CODEC;
  }

  @Override
  public String customRecipeCategory() {
    return "light";
  }
}
