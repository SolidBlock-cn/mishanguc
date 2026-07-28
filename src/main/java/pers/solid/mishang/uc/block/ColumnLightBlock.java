package pers.solid.mishang.uc.block;

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
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.ScheduledTickAccess;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.SimpleWaterloggedBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;
import pers.solid.mishang.uc.MishangUtils;
import pers.solid.mishang.uc.data.MishangucModels;
import pers.solid.mishang.uc.data.MishangucTextureKeys;

/**
 * 柱形灯方块，且没有底座，因此没有朝向，而是直接根据的坐标轴。
 */
public class ColumnLightBlock extends Block implements SimpleWaterloggedBlock, MishangucBlock {
  public static final MapCodec<ColumnLightBlock> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(Codec.STRING.fieldOf("light_color").forGetter(block -> block.lightColor), propertiesCodec(), Codec.INT.fieldOf("size_type").forGetter(block -> block.sizeType)).apply(instance, ColumnLightBlock::new));
  public static final EnumProperty<Direction.Axis> AXIS = BlockStateProperties.AXIS;
  public final String lightColor;
  private final int sizeType;

  public ColumnLightBlock(String lightColor, Properties settings, int sizeType) {
    super(settings);
    this.lightColor = lightColor;
    this.sizeType = sizeType;
    registerDefaultState(defaultBlockState().setValue(AXIS, Direction.Axis.X));
  }

  @Override
  protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
    super.createBlockStateDefinition(builder);
    builder.add(AXIS, BlockStateProperties.WATERLOGGED);
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
    return super.rotate(state, rotation).setValue(AXIS, MishangUtils.rotateAxis(rotation, state.getValue(AXIS)));
  }

  @Override
  public BlockState getStateForPlacement(BlockPlaceContext ctx) {
    Direction direction = ctx.getClickedFace();
    final Level world = ctx.getLevel();
    final BlockPos blockPos = ctx.getClickedPos();
    BlockState blockState = world.getBlockState(blockPos.relative(direction.getOpposite()));
    if (blockState.getBlockSupportShape(world, blockPos).getFaceShape(direction).isEmpty() && blockState.getShape(world, blockPos).getFaceShape(direction).isEmpty()) {
      return null;
    }
    return this.defaultBlockState()
        .setValue(AXIS, direction.getAxis())
        .setValue(BlockStateProperties.WATERLOGGED, world.getBlockState(blockPos).getFluidState().getType() == Fluids.WATER);
  }

  @Override
  public FluidState getFluidState(BlockState state) {
    return state.getValue(BlockStateProperties.WATERLOGGED)
        ? Fluids.WATER.getSource(false)
        : super.getFluidState(state);
  }

  @Override
  public VoxelShape getShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext context) {
    return (sizeType >= 2 ? ColumnWallLightBlock.SHAPES4 : sizeType == 1 ? ColumnWallLightBlock.SHAPES5 : ColumnWallLightBlock.SHAPES6).get(state.getValue(AXIS));
  }

  @Override
  public VoxelShape getCollisionShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext context) {
    return (sizeType >= 2 ? ColumnWallLightBlock.SHAPES5 : sizeType == 1 ? ColumnWallLightBlock.SHAPES6 : ColumnWallLightBlock.SHAPES7).get(state.getValue(AXIS));
  }

  @Override
  public boolean skipRendering(BlockState state, BlockState stateFrom, Direction direction) {
    return stateFrom.is(this) && state.getValue(AXIS).test(direction) && stateFrom.getValue(AXIS).test(direction) || super.skipRendering(state, stateFrom, direction);
  }


  @Environment(EnvType.CLIENT)
  @Override
  public void registerModels(ModelProvider modelProvider, BlockModelGenerators blockStateModelGenerator) {
    final TextureMapping textures = TextureMapping.singleSlot(MishangucTextureKeys.LIGHT, MishangucModels.texture(lightColor + "_light"));
    final Identifier modelId = getModelType().create(this, textures, blockStateModelGenerator.modelOutput);
    blockStateModelGenerator.blockStateOutput.accept(MultiVariantGenerator.dispatch(this, BlockModelGenerators.plainVariant(modelId))
        .with(PropertyDispatch.modify(AXIS)
            .select(Direction.Axis.Y, BlockModelGenerators.NOP)
            .select(Direction.Axis.X, BlockModelGenerators.X_ROT_270.then(BlockModelGenerators.Y_ROT_90))
            .select(Direction.Axis.Z, BlockModelGenerators.X_ROT_270)));
    blockStateModelGenerator.registerSimpleItemModel(this, modelId);
  }

  public ModelTemplate getModelType() {
    final Identifier identifier = BuiltInRegistries.BLOCK.getKey(this);
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
  public RecipeBuilder getCraftingRecipe(RecipeProvider recipeGenerator) {
    final Identifier itemId = BuiltInRegistries.ITEM.getKey(asItem());
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
      return SingleItemRecipeBuilder.stonecutting(Ingredient.of(fullLight), RecipeCategory.DECORATIONS, this, outputCount)
          .unlockedBy(RecipeProvider.getHasName(fullLight), recipeGenerator.has(fullLight));
    } else {
      final Identifier tubeId = itemId.withSuffix("_tube");
      final @NotNull Item tube = BuiltInRegistries.ITEM.getOptional(tubeId).orElseThrow(() -> new IllegalArgumentException(String.format("Can't generate recipes: %s does not have a corresponding tube block (with id [%s])", this, tubeId)));
      return recipeGenerator.shapeless(RecipeCategory.DECORATIONS, this, 1)
          .requires(tube)
          .requires(Items.GRAY_CONCRETE)
          .unlockedBy(RecipeProvider.getHasName(tube), recipeGenerator.has(tube));
    }
  }

  @Override
  protected MapCodec<? extends ColumnLightBlock> codec() {
    return CODEC;
  }

  @Override
  public String customRecipeCategory() {
    return "light";
  }

  @Override
  protected boolean isPathfindable(BlockState state, PathComputationType type) {
    return false;
  }
}
