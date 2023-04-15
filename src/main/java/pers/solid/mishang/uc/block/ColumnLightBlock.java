package pers.solid.mishang.uc.block;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.ShapeContext;
import net.minecraft.block.Waterloggable;
import net.minecraft.data.client.model.*;
import net.minecraft.fluid.FluidState;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.ItemPlacementContext;
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
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import pers.solid.brrp.v1.generator.BlockResourceGenerator;
import pers.solid.brrp.v1.model.ModelJsonBuilder;
import pers.solid.mishang.uc.MishangUtils;
import pers.solid.mishang.uc.arrp.FasterJTextures;

/**
 * 柱形灯方块，且没有底座，因此没有朝向，而是直接根据的坐标轴。
 */
public class ColumnLightBlock extends Block implements Waterloggable, BlockResourceGenerator {
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
  public BlockState getStateForNeighborUpdate(
      BlockState state,
      Direction direction,
      BlockState neighborState,
      WorldAccess world,
      BlockPos pos,
      BlockPos neighborPos) {
    if (state.get(Properties.WATERLOGGED)) {
      world.createAndScheduleFluidTick(pos, Fluids.WATER, Fluids.WATER.getTickRate(world));
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
        .with(Properties.WATERLOGGED,
            world.getBlockState(blockPos).getFluidState().getFluid()
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

  @Environment(EnvType.CLIENT)
  @Override
  public @Nullable BlockStateSupplier getBlockStates() {
    final Identifier id = getBlockModelId();
    return VariantsBlockStateSupplier.create(this, BlockStateVariant.create().put(VariantSettings.MODEL, id)).coordinate(BlockStateVariantMap.create(AXIS).register(Direction.Axis.Y, BlockStateVariant.create()).register(Direction.Axis.X, BlockStateVariant.create().put(VariantSettings.X, VariantSettings.Rotation.R270).put(VariantSettings.Y, VariantSettings.Rotation.R90)).register(Direction.Axis.Z, BlockStateVariant.create().put(VariantSettings.X, VariantSettings.Rotation.R270)));
  }

  @Environment(EnvType.CLIENT)
  @Override
  public @NotNull ModelJsonBuilder getBlockModel() {
    return ModelJsonBuilder.create(getModelParent())
        .setTextures(new FasterJTextures().varP("light", lightColor + "_light"));
  }

  @Environment(EnvType.CLIENT)
  @ApiStatus.AvailableSince("0.1.7")
  public Identifier getModelParent() {
    final Identifier identifier = getBlockId();
    String path = identifier.getPath();
    final int i = lightColor.length();
    if (path.startsWith(lightColor) && path.charAt(i) == '_') {
      path = path.substring(i + 1);
    } else {
      throw new AssertionError();
    }
    return new Identifier(identifier.getNamespace(), path).brrp_prefixed("block/");
  }
}
