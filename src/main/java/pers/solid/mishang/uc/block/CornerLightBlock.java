package pers.solid.mishang.uc.block;

import com.mojang.math.Quadrant;
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
import net.minecraft.client.renderer.block.dispatch.VariantMutator;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.recipes.RecipeBuilder;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.resources.Identifier;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.ScheduledTickAccess;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.SimpleWaterloggedBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.block.state.properties.Half;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.phys.shapes.BooleanOp;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import pers.solid.mishang.uc.MishangUtils;
import pers.solid.mishang.uc.data.MishangucModels;
import pers.solid.mishang.uc.data.MishangucTextureKeys;

import java.util.Map;

import static net.minecraft.world.level.material.Fluids.WATER;

public class CornerLightBlock extends HorizontalDirectionalBlock
    implements SimpleWaterloggedBlock, LightConnectable, MishangucBlock {
  public static final MapCodec<CornerLightBlock> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(Codec.STRING.fieldOf("light_color").forGetter(b -> b.lightColor), propertiesCodec()).apply(instance, CornerLightBlock::new));
  private static final EnumProperty<Half> BLOCK_HALF = BlockStateProperties.HALF;
  private static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;
  private static final Map<Direction, VoxelShape> SHAPE_PER_DIRECTION_WHEN_BOTTOM = MishangUtils.createDirectionToUnionShape(
      MishangUtils.createHorizontalDirectionToShape(4, 0, 0, 12, 1, 16),
      MishangUtils.createHorizontalDirectionToShape(4, 0, 0, 12, 16, 1));
  private static final Map<Direction, VoxelShape> SHAPE_PER_DIRECTION_WHEN_TOP = MishangUtils.createDirectionToUnionShape(
      MishangUtils.createHorizontalDirectionToShape(4, 15, 0, 12, 16, 16),
      MishangUtils.createHorizontalDirectionToShape(4, 0, 0, 12, 16, 1));
  public final String lightColor;

  public CornerLightBlock(String lightColor, Properties settings) {
    super(settings);
    this.lightColor = lightColor;
    this.registerDefaultState(defaultBlockState()
        .setValue(WATERLOGGED, false)
        .setValue(BLOCK_HALF, Half.BOTTOM));
  }

  @Nullable
  @Override
  public BlockState getStateForPlacement(BlockPlaceContext ctx) {
    final BlockState placementState = super.getStateForPlacement(ctx);
    if (placementState == null) {
      return null;
    }
    final Direction side = ctx.getClickedFace();
    return placementState
        .setValue(WATERLOGGED, ctx.getLevel().getFluidState(ctx.getClickedPos()).getType() == WATER)
        .setValue(BLOCK_HALF,
            side == Direction.DOWN || ctx.getClickLocation().y - ctx.getClickedPos().getY() > 0.5
                ? Half.TOP
                : Half.BOTTOM)
        .setValue(FACING,
            Direction.Plane.HORIZONTAL.test(side) ? side : ctx.getHorizontalDirection().getOpposite());
  }

  @Override
  protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
    super.createBlockStateDefinition(builder);
    builder.add(FACING, BLOCK_HALF, WATERLOGGED);
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
  public FluidState getFluidState(BlockState state) {
    return state.getValue(WATERLOGGED) ? WATER.getSource(false) : super.getFluidState(state);
  }


  @Override
  protected BlockState updateShape(BlockState state, LevelReader world, ScheduledTickAccess tickView, BlockPos pos, Direction direction, BlockPos neighborPos, BlockState neighborState, RandomSource random) {
    if (state.getValue(WATERLOGGED)) {
      tickView.scheduleTick(pos, WATER, WATER.getTickDelay(world));
    }
    return super.updateShape(state, world, tickView, pos, direction, neighborPos, neighborState, random);
  }

  @Override
  public VoxelShape getShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext context) {
    return switch (state.getValue(BLOCK_HALF)) {
      case BOTTOM -> SHAPE_PER_DIRECTION_WHEN_BOTTOM.get(state.getValue(FACING));
      case TOP -> SHAPE_PER_DIRECTION_WHEN_TOP.get(state.getValue(FACING));
    };
  }

  @Override
  public boolean isConnectedIn(BlockState blockState, Direction facing, Direction direction) {
    final Direction facingProperty = blockState.getValue(FACING);
    final Half blockHalf = blockState.getValue(BLOCK_HALF);

    return switch (facing) {
      case UP -> blockHalf == Half.BOTTOM && direction.getAxis() == facingProperty.getAxis();
      case DOWN -> blockHalf == Half.TOP && direction.getAxis() == facingProperty.getAxis();
      default -> facing == facingProperty && direction.getAxis() == Direction.Axis.Y;
    };
  }

  @Override
  public void updateIndirectNeighbourShapes(BlockState state, LevelAccessor world, BlockPos pos, int flags, int maxUpdateDepth) {
    super.updateIndirectNeighbourShapes(state, world, pos, flags, maxUpdateDepth);
    final Direction facing = state.getValue(FACING);
    final Direction facingVertical =
        state.getValue(BLOCK_HALF) == Half.TOP ? Direction.DOWN : Direction.UP;
    prepareConnection(state, world, pos, flags, maxUpdateDepth, facing);
    prepareConnection(state, world, pos, flags, maxUpdateDepth, facingVertical);
  }

  @Environment(EnvType.CLIENT)
  @Override
  public void registerModels(ModelProvider modelProvider, BlockModelGenerators blockStateModelGenerator) {
    final TextureMapping textures = TextureMapping.singleSlot(MishangucTextureKeys.LIGHT, MishangucModels.material(lightColor + "_light"));
    final Identifier modelId = getModelType().create(this, textures, blockStateModelGenerator.modelOutput);
    blockStateModelGenerator.blockStateOutput.accept(MultiVariantGenerator.dispatch(this, BlockModelGenerators.plainVariant(modelId)).with(PropertyDispatch.modify(BLOCK_HALF, FACING).generate((blockHalf, direction) -> {
      if (blockHalf == Half.BOTTOM) {
        return VariantMutator.Y_ROT.withValue(switch (direction) {
          case WEST -> Quadrant.R90;
          case NORTH -> Quadrant.R180;
          case EAST -> Quadrant.R270;
          default -> Quadrant.R0;
        });
      } else {
        return VariantMutator.Y_ROT.withValue(switch (direction) {
          case EAST -> Quadrant.R90;
          case SOUTH -> Quadrant.R180;
          case WEST -> Quadrant.R270;
          default -> Quadrant.R0;
        }).then(BlockModelGenerators.X_ROT_180);
      }
    })));
    blockStateModelGenerator.registerSimpleItemModel(this, modelId);
  }

  public ModelTemplate getModelType() {
    final Identifier identifier = BuiltInRegistries.BLOCK.getKey(this);
    String path = identifier.getPath();
    final int i = lightColor.length();
    try {
      if (path.startsWith(lightColor) && path.charAt(i) == '_') {
        path = path.substring(i + 1);
      }
    } catch (IndexOutOfBoundsException ignored) {
    }
    return MishangucModels.createBlock(path, MishangucTextureKeys.LIGHT);
  }

  @Override
  public RecipeBuilder getCraftingRecipe(RecipeProvider recipeGenerator) {
    final Identifier itemId = BuiltInRegistries.ITEM.getKey(asItem());
    final Identifier wallId = Identifier.fromNamespaceAndPath(itemId.getNamespace(), itemId.getPath().replace("_corner_", "_wall_"));
    if (wallId.equals(itemId)) {
      throw new IllegalStateException("Can't generate recipes: can't find the id of corresponding wall light block for " + this);
    }
    final @NotNull Item wall = BuiltInRegistries.ITEM.getOptional(wallId).orElseThrow(() -> new IllegalArgumentException(String.format("Can't generate recipes: can't find the corresponding wall light block with id [%s] for [%s]", wallId, itemId)));
    return recipeGenerator.shapeless(RecipeCategory.DECORATIONS, this, 1)
        .requires(wall)
        .requires(wall)
        .unlockedBy(RecipeProvider.getHasName(wall), recipeGenerator.has(wall));
  }

  @Override
  protected MapCodec<? extends CornerLightBlock> codec() {
    return CODEC;
  }
}
