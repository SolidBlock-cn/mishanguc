package pers.solid.mishang.uc.block;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.*;
import net.minecraft.block.enums.BlockHalf;
import net.minecraft.client.data.*;
import net.minecraft.client.render.model.json.ModelVariantOperator;
import net.minecraft.data.recipe.CraftingRecipeJsonBuilder;
import net.minecraft.data.recipe.RecipeGenerator;
import net.minecraft.fluid.FluidState;
import net.minecraft.item.Item;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.recipe.book.RecipeCategory;
import net.minecraft.registry.Registries;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.state.property.EnumProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.util.Identifier;
import net.minecraft.util.function.BooleanBiFunction;
import net.minecraft.util.math.AxisRotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.random.Random;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.minecraft.world.WorldAccess;
import net.minecraft.world.WorldView;
import net.minecraft.world.tick.ScheduledTickView;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import pers.solid.mishang.uc.MishangUtils;
import pers.solid.mishang.uc.data.MishangucModels;
import pers.solid.mishang.uc.data.MishangucTextureKeys;

import java.util.Map;

import static net.minecraft.fluid.Fluids.WATER;

public class CornerLightBlock extends HorizontalFacingBlock
    implements Waterloggable, LightConnectable, MishangucBlock {
  public static final MapCodec<CornerLightBlock> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(Codec.STRING.fieldOf("light_color").forGetter(b -> b.lightColor), createSettingsCodec()).apply(instance, CornerLightBlock::new));
  private static final EnumProperty<BlockHalf> BLOCK_HALF = Properties.BLOCK_HALF;
  private static final BooleanProperty WATERLOGGED = Properties.WATERLOGGED;
  private static final Map<Direction, VoxelShape> SHAPE_PER_DIRECTION_WHEN_BOTTOM = MishangUtils.createDirectionToUnionShape(
      MishangUtils.createHorizontalDirectionToShape(4, 0, 0, 12, 1, 16),
      MishangUtils.createHorizontalDirectionToShape(4, 0, 0, 12, 16, 1));
  private static final Map<Direction, VoxelShape> SHAPE_PER_DIRECTION_WHEN_TOP = MishangUtils.createDirectionToUnionShape(
      MishangUtils.createHorizontalDirectionToShape(4, 15, 0, 12, 16, 16),
      MishangUtils.createHorizontalDirectionToShape(4, 0, 0, 12, 16, 1));
  public final String lightColor;

  public CornerLightBlock(String lightColor, Settings settings) {
    super(settings);
    this.lightColor = lightColor;
    this.setDefaultState(getDefaultState()
        .with(WATERLOGGED, false)
        .with(BLOCK_HALF, BlockHalf.BOTTOM));
  }

  @Nullable
  @Override
  public BlockState getPlacementState(ItemPlacementContext ctx) {
    final BlockState placementState = super.getPlacementState(ctx);
    if (placementState == null) {
      return null;
    }
    final Direction side = ctx.getSide();
    return placementState
        .with(WATERLOGGED, ctx.getWorld().getFluidState(ctx.getBlockPos()).getFluid() == WATER)
        .with(BLOCK_HALF,
            side == Direction.DOWN || ctx.getHitPos().y - ctx.getBlockPos().getY() > 0.5
                ? BlockHalf.TOP
                : BlockHalf.BOTTOM)
        .with(FACING,
            Direction.Type.HORIZONTAL.test(side) ? side : ctx.getHorizontalPlayerFacing().getOpposite());
  }

  @Override
  protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
    super.appendProperties(builder);
    builder.add(FACING, BLOCK_HALF, WATERLOGGED);
  }

  @Override
  public boolean canPlaceAt(BlockState state, WorldView world, BlockPos pos) {
    final Direction facing = state.get(FACING);
    final Direction backDirection = facing.getOpposite();
    final BlockPos backPos = pos.offset(backDirection);
    final VoxelShape centerShape = Block.createCuboidShape(7, 7, 7, 9, 9, 9);
    final BlockState backState = world.getBlockState(backPos);
    return !VoxelShapes.matchesAnywhere(backState.getSidesShape(world, backPos).getFace(facing), centerShape, BooleanBiFunction.ONLY_SECOND) || !VoxelShapes.matchesAnywhere(backState.getCollisionShape(world, backPos).getFace(facing), centerShape, BooleanBiFunction.ONLY_SECOND);
  }

  @Override
  public FluidState getFluidState(BlockState state) {
    return state.get(WATERLOGGED) ? WATER.getStill(false) : super.getFluidState(state);
  }


  @Override
  protected BlockState getStateForNeighborUpdate(BlockState state, WorldView world, ScheduledTickView tickView, BlockPos pos, Direction direction, BlockPos neighborPos, BlockState neighborState, Random random) {
    if (state.get(WATERLOGGED)) {
      tickView.scheduleFluidTick(pos, WATER, WATER.getTickRate(world));
    }
    return super.getStateForNeighborUpdate(state, world, tickView, pos, direction, neighborPos, neighborState, random);
  }

  @Override
  public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
    return switch (state.get(BLOCK_HALF)) {
      case BOTTOM -> SHAPE_PER_DIRECTION_WHEN_BOTTOM.get(state.get(FACING));
      case TOP -> SHAPE_PER_DIRECTION_WHEN_TOP.get(state.get(FACING));
    };
  }

  @Override
  public boolean isConnectedIn(BlockState blockState, Direction facing, Direction direction) {
    final Direction facingProperty = blockState.get(FACING);
    final BlockHalf blockHalf = blockState.get(BLOCK_HALF);

    return switch (facing) {
      case UP -> blockHalf == BlockHalf.BOTTOM && direction.getAxis() == facingProperty.getAxis();
      case DOWN -> blockHalf == BlockHalf.TOP && direction.getAxis() == facingProperty.getAxis();
      default -> facing == facingProperty && direction.getAxis() == Direction.Axis.Y;
    };
  }

  @Override
  public void prepare(BlockState state, WorldAccess world, BlockPos pos, int flags, int maxUpdateDepth) {
    super.prepare(state, world, pos, flags, maxUpdateDepth);
    final Direction facing = state.get(FACING);
    final Direction facingVertical =
        state.get(BLOCK_HALF) == BlockHalf.TOP ? Direction.DOWN : Direction.UP;
    prepareConnection(state, world, pos, flags, maxUpdateDepth, facing);
    prepareConnection(state, world, pos, flags, maxUpdateDepth, facingVertical);
  }

  @Environment(EnvType.CLIENT)
  @Override
  public void registerModels(ModelProvider modelProvider, BlockStateModelGenerator blockStateModelGenerator) {
    final TextureMap textures = TextureMap.of(MishangucTextureKeys.LIGHT, MishangucModels.texture(lightColor + "_light"));
    final Identifier modelId = getModelType().upload(this, textures, blockStateModelGenerator.modelCollector);
    blockStateModelGenerator.blockStateCollector.accept(VariantsBlockModelDefinitionCreator.of(this, BlockStateModelGenerator.createWeightedVariant(modelId)).apply(BlockStateVariantMap.operations(BLOCK_HALF, FACING).generate((blockHalf, direction) -> {
      if (blockHalf == BlockHalf.BOTTOM) {
        return ModelVariantOperator.ROTATION_Y.withValue(switch (direction) {
          case WEST -> AxisRotation.R90;
          case NORTH -> AxisRotation.R180;
          case EAST -> AxisRotation.R270;
          default -> AxisRotation.R0;
        });
      } else {
        return ModelVariantOperator.ROTATION_Y.withValue(switch (direction) {
          case EAST -> AxisRotation.R90;
          case SOUTH -> AxisRotation.R180;
          case WEST -> AxisRotation.R270;
          default -> AxisRotation.R0;
        }).then(BlockStateModelGenerator.ROTATE_X_180);
      }
    })));
    blockStateModelGenerator.registerParentedItemModel(this, modelId);
  }

  public Model getModelType() {
    final Identifier identifier = Registries.BLOCK.getId(this);
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
  public CraftingRecipeJsonBuilder getCraftingRecipe(RecipeGenerator recipeGenerator) {
    final Identifier itemId = Registries.ITEM.getId(asItem());
    final Identifier wallId = Identifier.of(itemId.getNamespace(), itemId.getPath().replace("_corner_", "_wall_"));
    if (wallId.equals(itemId)) {
      throw new IllegalStateException("Can't generate recipes: can't find the id of corresponding wall light block for " + this);
    }
    final @NotNull Item wall = Registries.ITEM.getOptionalValue(wallId).orElseThrow(() -> new IllegalArgumentException(String.format("Can't generate recipes: can't find the corresponding wall light block with id [%s] for [%s]", wallId, itemId)));
    return recipeGenerator.createShapeless(RecipeCategory.DECORATIONS, this, 1)
        .input(wall)
        .input(wall)
        .criterion(RecipeGenerator.hasItem(wall), recipeGenerator.conditionsFromItem(wall));
  }

  @Override
  protected MapCodec<? extends CornerLightBlock> getCodec() {
    return CODEC;
  }
}
