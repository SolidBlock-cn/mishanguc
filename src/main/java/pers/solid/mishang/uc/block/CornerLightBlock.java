package pers.solid.mishang.uc.block;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.block.*;
import net.minecraft.block.enums.BlockHalf;
import net.minecraft.data.client.*;
import net.minecraft.data.server.recipe.CraftingRecipeJsonBuilder;
import net.minecraft.data.server.recipe.RecipeProvider;
import net.minecraft.data.server.recipe.ShapelessRecipeJsonBuilder;
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
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;
import net.minecraft.world.WorldAccess;
import net.minecraft.world.WorldView;
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
    Direction direction = state.get(FACING).getOpposite();
    BlockPos blockPos = pos.offset(direction);
    return world
        .getBlockState(blockPos)
        .isSideSolidFullSquare(world, blockPos, direction.getOpposite());
  }

  @Override
  public FluidState getFluidState(BlockState state) {
    return state.get(WATERLOGGED) ? WATER.getStill(false) : super.getFluidState(state);
  }

  @Override
  public BlockState getStateForNeighborUpdate(BlockState state, Direction direction, BlockState neighborState, WorldAccess world, BlockPos pos, BlockPos neighborPos) {
    if (state.get(WATERLOGGED)) {
      world.scheduleFluidTick(pos, WATER, WATER.getTickRate(world));
    }

    return super.getStateForNeighborUpdate(state, direction, neighborState, world, pos, neighborPos);
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

  @Override
  public void registerModels(ModelProvider modelProvider, BlockStateModelGenerator blockStateModelGenerator) {
    final TextureMap textures = TextureMap.of(MishangucTextureKeys.LIGHT, MishangucModels.texture(lightColor + "_light"));
    final Identifier modelId = getModelType().upload(this, textures, blockStateModelGenerator.modelCollector);
    blockStateModelGenerator.blockStateCollector.accept(VariantsBlockStateSupplier.create(this, BlockStateVariant.create().put(VariantSettings.MODEL, modelId)).coordinate(BlockStateVariantMap.create(BLOCK_HALF, FACING).register((blockHalf, direction) -> {
      if (blockHalf == BlockHalf.BOTTOM) {
        return BlockStateVariant.create().put(MishangUtils.DIRECTION_Y_VARIANT, direction);
      } else {
        return BlockStateVariant.create().put(MishangUtils.DIRECTION_Y_VARIANT, direction.getOpposite()).put(VariantSettings.X, VariantSettings.Rotation.R180);
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
  public CraftingRecipeJsonBuilder getCraftingRecipe() {
    final Identifier itemId = Registries.ITEM.getId(asItem());
    final Identifier wallId = Identifier.of(itemId.getNamespace(), itemId.getPath().replace("_corner_", "_wall_"));
    if (wallId.equals(itemId)) {
      throw new IllegalStateException("Can't generate recipes: can't find the id of corresponding wall light block for " + this);
    }
    final @NotNull Item wall = Registries.ITEM.getOrEmpty(wallId).orElseThrow(() -> new IllegalArgumentException(String.format("Can't generate recipes: can't find the corresponding wall light block with id [%s] for [%s]", wallId, itemId)));
    return ShapelessRecipeJsonBuilder.create(RecipeCategory.DECORATIONS, this, 1)
        .input(wall)
        .input(wall)
        .criterion(RecipeProvider.hasItem(wall), RecipeProvider.conditionsFromItem(wall));
  }

  @Override
  protected MapCodec<? extends CornerLightBlock> getCodec() {
    return CODEC;
  }
}
