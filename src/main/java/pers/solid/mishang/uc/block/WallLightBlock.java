package pers.solid.mishang.uc.block;

import com.google.common.collect.BiMap;
import com.google.common.collect.EnumHashBiMap;
import net.devtech.arrp.generator.BlockResourceGenerator;
import net.devtech.arrp.json.blockstate.JBlockModel;
import net.devtech.arrp.json.blockstate.JBlockStates;
import net.devtech.arrp.json.blockstate.JVariants;
import net.devtech.arrp.json.models.JModel;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.*;
import net.minecraft.fluid.FluidState;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.util.BlockMirror;
import net.minecraft.util.BlockRotation;
import net.minecraft.util.Identifier;
import net.minecraft.util.Util;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;
import net.minecraft.world.WorldAccess;
import net.minecraft.world.WorldView;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Nullable;
import pers.solid.mishang.uc.MishangUtils;
import pers.solid.mishang.uc.arrp.FasterJTextures;

import java.util.Map;

public class WallLightBlock extends FacingBlock implements Waterloggable, BlockResourceGenerator {
  protected static final BooleanProperty WEST = Properties.WEST;
  protected static final BooleanProperty EAST = Properties.EAST;
  protected static final BooleanProperty SOUTH = Properties.SOUTH;
  protected static final BooleanProperty NORTH = Properties.NORTH;
  protected static final BooleanProperty UP = Properties.UP;
  protected static final BooleanProperty DOWN = Properties.DOWN;
  protected static final BiMap<Direction, BooleanProperty> DIRECTION_TO_PROPERTY =
      Util.make(
          EnumHashBiMap.create(Direction.class),
          map -> {
            map.put(Direction.WEST, WEST);
            map.put(Direction.EAST, EAST);
            map.put(Direction.SOUTH, SOUTH);
            map.put(Direction.NORTH, NORTH);
            map.put(Direction.UP, UP);
            map.put(Direction.DOWN, DOWN);
          });
  private static final Map<Direction, VoxelShape> SHAPE_PER_DIRECTION =
      MishangUtils.createDirectionToShape(4, 0, 4, 12, 2, 12);
  public final String lightColor;

  public WallLightBlock(String lightColor, Settings settings) {
    super(settings);
    this.lightColor = lightColor;
    this.setDefaultState(
        this.stateManager
            .getDefaultState()
            .with(Properties.WATERLOGGED, false)
            .with(FACING, Direction.UP));
  }

  @SuppressWarnings("deprecation")
  @Override
  public boolean canPlaceAt(BlockState state, WorldView world, BlockPos pos) {
    Direction direction = state.get(FACING).getOpposite();
    BlockPos blockPos = pos.offset(direction);
    return world
        .getBlockState(blockPos)
        .isSideSolid(world, blockPos, direction.getOpposite(), SideShapeType.CENTER);
  }

  @Override
  protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
    builder.add(FACING, Properties.WATERLOGGED);
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
      world.getFluidTickScheduler().schedule(pos, Fluids.WATER, Fluids.WATER.getTickRate(world));
    }

    return super.getStateForNeighborUpdate(
        state, direction, neighborState, world, pos, neighborPos);
  }

  @SuppressWarnings({"deprecation"})
  @Override
  public BlockState rotate(BlockState state, BlockRotation rotation) {
    return state.with(FACING, rotation.rotate(state.get(FACING)));
  }

  @Override
  @SuppressWarnings({"deprecation"})
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
        .with(
            Properties.WATERLOGGED,
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

  @Override
  @SuppressWarnings({"deprecation"})
  public VoxelShape getOutlineShape(
      BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
    return SHAPE_PER_DIRECTION.get(state.get(FACING));
  }

  @SuppressWarnings("deprecation")
  @Override
  public void prepare(
      BlockState state, WorldAccess world, BlockPos pos, int flags, int maxUpdateDepth) {
    super.prepare(state, world, pos, flags, maxUpdateDepth);
    final Direction facing = state.get(FACING);
    if (this instanceof final LightConnectable lightConnectable) {
      lightConnectable.prepareConnection(state, world, pos, flags, maxUpdateDepth, facing);
    }
  }

  @Environment(EnvType.CLIENT)
  @Override
  public @Nullable JBlockStates getBlockStates() {
    final JVariants variants = new JVariants();
    final Identifier id = getBlockModelId();
    variants.addVariant("facing", "up", new JBlockModel(id));
    variants.addVariant("facing", "down", new JBlockModel(id).x(180));
    for (Direction direction : Direction.Type.HORIZONTAL) {
      variants.addVariant("facing", direction.asString(), new JBlockModel(id).x(-90).y((int) direction.asRotation()));
    }
    return JBlockStates.ofVariants(variants);
  }

  @Environment(EnvType.CLIENT)
  @Override
  public @Nullable JModel getBlockModel() {
    return new JModel(getModelParent())
        .textures(new FasterJTextures().varP("light", lightColor + "_light"));
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
    return new Identifier(identifier.getNamespace(), path).brrp_prepend("block/");
  }
}
