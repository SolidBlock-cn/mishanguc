package pers.solid.mishang.uc.block;

import net.devtech.arrp.generator.BlockResourceGenerator;
import net.devtech.arrp.json.blockstate.JBlockModel;
import net.devtech.arrp.json.blockstate.JBlockStates;
import net.devtech.arrp.json.blockstate.JVariants;
import net.devtech.arrp.json.models.JModel;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.*;
import net.minecraft.block.enums.BlockHalf;
import net.minecraft.fluid.FluidState;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.recipe.book.RecipeCategory;
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
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import pers.solid.mishang.uc.MishangUtils;
import pers.solid.mishang.uc.arrp.FasterJTextures;

import java.util.Map;

import static net.minecraft.fluid.Fluids.WATER;

public class CornerLightBlock extends HorizontalFacingBlock
    implements Waterloggable, LightConnectable, BlockResourceGenerator {
  private static final EnumProperty<BlockHalf> BLOCK_HALF = Properties.BLOCK_HALF;
  private static final BooleanProperty WATERLOGGED = Properties.WATERLOGGED;
  private static final Map<Direction, VoxelShape> SHAPE_PER_DIRECTION_WHEN_BOTTOM =
      MishangUtils.createDirectionToUnionShape(
          MishangUtils.createHorizontalDirectionToShape(4, 0, 0, 12, 1, 16),
          MishangUtils.createHorizontalDirectionToShape(4, 0, 0, 12, 16, 1));
  private static final Map<Direction, VoxelShape> SHAPE_PER_DIRECTION_WHEN_TOP =
      MishangUtils.createDirectionToUnionShape(
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
        .with(
            BLOCK_HALF,
            side == Direction.DOWN || ctx.getHitPos().y - ctx.getBlockPos().getY() > 0.5
                ? BlockHalf.TOP
                : BlockHalf.BOTTOM)
        .with(
            FACING,
            Direction.Type.HORIZONTAL.test(side) ? side : ctx.getPlayerFacing().getOpposite());
  }

  @Override
  protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
    super.appendProperties(builder);
    builder.add(FACING, BLOCK_HALF, WATERLOGGED);
  }

  @SuppressWarnings("deprecation")
  @Override
  public boolean canPlaceAt(BlockState state, WorldView world, BlockPos pos) {
    Direction direction = state.get(FACING).getOpposite();
    BlockPos blockPos = pos.offset(direction);
    return world
        .getBlockState(blockPos)
        .isSideSolidFullSquare(world, blockPos, direction.getOpposite());
  }

  @SuppressWarnings("deprecation")
  @Override
  public FluidState getFluidState(BlockState state) {
    return state.get(WATERLOGGED) ? WATER.getStill(false) : super.getFluidState(state);
  }

  @SuppressWarnings("deprecation")
  @Override
  public BlockState getStateForNeighborUpdate(
      BlockState state,
      Direction direction,
      BlockState neighborState,
      WorldAccess world,
      BlockPos pos,
      BlockPos neighborPos) {
    if (state.get(WATERLOGGED)) {
      world.scheduleFluidTick(pos, WATER, WATER.getTickRate(world));
    }

    return super.getStateForNeighborUpdate(
        state, direction, neighborState, world, pos, neighborPos);
  }

  @SuppressWarnings("deprecation")
  @Override
  public VoxelShape getOutlineShape(
      BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
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

  @SuppressWarnings("deprecation")
  @Override
  public void prepare(
      BlockState state, WorldAccess world, BlockPos pos, int flags, int maxUpdateDepth) {
    super.prepare(state, world, pos, flags, maxUpdateDepth);
    final Direction facing = state.get(FACING);
    final Direction facingVertical =
        state.get(BLOCK_HALF) == BlockHalf.TOP ? Direction.DOWN : Direction.UP;
    prepareConnection(state, world, pos, flags, maxUpdateDepth, facing);
    prepareConnection(state, world, pos, flags, maxUpdateDepth, facingVertical);
  }

  @Environment(EnvType.CLIENT)
  @Override
  public @NotNull JBlockStates getBlockStates() {
    final JVariants variants = new JVariants();
    final Identifier identifier = getBlockModelId();
    for (Direction direction : Direction.Type.HORIZONTAL) {
      variants.addVariant("half=bottom,facing", direction.asString(), new JBlockModel(identifier).y((int) direction.asRotation()));
      variants.addVariant("half=top,facing", direction.asString(), new JBlockModel(identifier).y((int) direction.asRotation() - 180).x(180));
    }
    return JBlockStates.ofVariants(variants);
  }

  @Environment(EnvType.CLIENT)
  @Override
  public @NotNull JModel getBlockModel() {
    return new JModel(getModelParent())
        .textures(new FasterJTextures().varP("light", lightColor + "_light"));
  }


  @ApiStatus.AvailableSince("0.1.7")
  public Identifier getModelParent() {
    final Identifier identifier = getBlockId();
    String path = identifier.getPath();
    final int i = lightColor.length();
    try {
      if (path.startsWith(lightColor) && path.charAt(i) == '_') {
        path = path.substring(i + 1);
      }
    } catch (IndexOutOfBoundsException ignored) {
    }
    return new Identifier(identifier.getNamespace(), path).brrp_prepend("block/");
  }

  @Override
  public Identifier getAdvancementIdForRecipe(Identifier recipeId, @Nullable RecipeCategory recipeCategory) {
    return recipeId.brrp_prepend("recipes/lights/");
  }

  @Override
  public @Nullable RecipeCategory getRecipeCategory() {
    return RecipeCategory.DECORATIONS;
  }
}
