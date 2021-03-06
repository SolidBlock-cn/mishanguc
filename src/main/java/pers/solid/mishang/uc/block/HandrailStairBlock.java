package pers.solid.mishang.uc.block;

import com.google.common.collect.Iterators;
import com.google.common.collect.Maps;
import net.devtech.arrp.api.RuntimeResourcePack;
import net.devtech.arrp.generator.BlockResourceGenerator;
import net.devtech.arrp.json.blockstate.JBlockModel;
import net.devtech.arrp.json.blockstate.JBlockStates;
import net.devtech.arrp.json.blockstate.JVariants;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.*;
import net.minecraft.block.enums.BlockHalf;
import net.minecraft.fluid.FluidState;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.Item;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.state.property.EnumProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.text.MutableText;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.BlockMirror;
import net.minecraft.util.Identifier;
import net.minecraft.util.StringIdentifiable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.Unmodifiable;
import pers.solid.mishang.uc.MishangucProperties;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public abstract class HandrailStairBlock<T extends HandrailBlock> extends HorizontalFacingBlock implements Waterloggable, BlockResourceGenerator, Handrails {
  public static final BooleanProperty WATERLOGGED = Properties.WATERLOGGED;
  public static final EnumProperty<Position> POSITION = MishangucProperties.HANDRAIL_STAIR_POSITION;
  public static final EnumProperty<Shape> SHAPE = MishangucProperties.HANDRAIL_STAIR_SHAPE;
  public final @NotNull T baseRail;

  @Unmodifiable
  public static final Map<Direction, Map<Position, Map<Shape, VoxelShape>>> SHAPES = Maps.toMap(
      Direction.Type.HORIZONTAL.iterator(),
      facing -> Maps.toMap(
          Iterators.forArray(Position.values()),
          position ->
              Maps.toMap(
                  Iterators.forArray(Shape.values()),
                  shape -> composeShape(facing, position, shape)
              )
      )
  );

  protected HandrailStairBlock(@NotNull T baseRail, Settings settings) {
    super(settings);
    this.baseRail = baseRail;
    setDefaultState(stateManager.getDefaultState()
        .with(WATERLOGGED, false)
        .with(POSITION, Position.CENTER)
        .with(FACING, Direction.SOUTH)
        .with(SHAPE, Shape.MIDDLE));
  }

  public HandrailStairBlock(@NotNull T baseRail) {
    this(baseRail, FabricBlockSettings.copyOf(baseRail));
  }

  @Override
  protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
    super.appendProperties(builder);
    builder.add(WATERLOGGED, POSITION, FACING, SHAPE);
  }

  @Environment(EnvType.CLIENT)
  @Override
  public @NotNull JBlockStates getBlockStates() {
    final JVariants variant = new JVariants();
    final Identifier blockModelId = getBlockModelId();
    for (Direction facing : Direction.Type.HORIZONTAL) {
      for (Position position : Position.values()) {
        for (Shape shape : Shape.values()) {
          variant.put("facing=" + facing.asString() + ",position=" + position.asString() + ",shape=" + shape.asString(), new JBlockModel(blockModelId.brrp_append("_" + shape.asString() + "_" + position.asString())).y(((int) facing.getOpposite().asRotation())).uvlock());
        }
      }
    }
    return JBlockStates.ofVariants(variant);
  }

  @Environment(EnvType.CLIENT)
  @Override
  public abstract void writeBlockModel(RuntimeResourcePack pack);

  @Nullable
  @Override
  public BlockState getPlacementState(ItemPlacementContext ctx) {
    final BlockState placementState = super.getPlacementState(ctx);
    if (placementState == null) return null;

    final BlockPos blockPos = ctx.getBlockPos();
    final World world = ctx.getWorld();
    final Shape shape;
    final BlockState stateBelow = world.getBlockState(blockPos.down());
    final Direction facing;
    if (stateBelow.getBlock() instanceof StairsBlock && stateBelow.contains(StairsBlock.FACING) && stateBelow.get(StairsBlock.HALF) == BlockHalf.BOTTOM) {
      facing = stateBelow.get(StairsBlock.FACING);
      final BlockPos forwardPos = blockPos.offset(facing);
      final BlockState forwardState = world.getBlockState(forwardPos);
      if (forwardState.getBlock() instanceof StairsBlock) {
        shape = Shape.MIDDLE;
      } else {
        shape = Shape.TOP;
      }
    } else {
      facing = ctx.getPlayerFacing();
      shape = Shape.BOTTOM;
    }
    final Vec3d hitPos = ctx.getHitPos();
    final double diff = switch (facing) {
      case SOUTH -> hitPos.x - blockPos.getX();
      case NORTH -> blockPos.getX() + 1 - hitPos.x;
      case EAST -> blockPos.getZ() + 1 - hitPos.z;
      case WEST -> hitPos.z - blockPos.getZ();
      default -> 0.5;
    };
    return placementState.with(WATERLOGGED, world.getFluidState(blockPos).getFluid() == Fluids.WATER).with(FACING, facing).with(POSITION, diff < 0.3 ? Position.RIGHT : diff < 0.7 ? Position.CENTER : Position.LEFT).with(SHAPE, shape);
  }

  /**
   * ??????????????????????????????????????????
   *
   * @param facing   ???????????????????????????????????????????????????????????????
   * @param position ?????????
   * @return ???????????????????????????????????? position ??? CENTER?????????????????????
   */
  @Nullable
  protected static Direction equivalentFacing(Direction facing, Position position) {
    return switch (position) {
      case LEFT -> facing.rotateYClockwise();
      case RIGHT -> facing.rotateYCounterclockwise();
      case CENTER -> null;
    };
  }

  /**
   * ?????????????????????????????????
   *
   * @param state ????????????????????????????????????????????????????????????
   * @return ?????????????????????????????????
   */
  @Nullable
  public static Direction equivalentFacing(BlockState state) {
    return equivalentFacing(state.get(FACING), state.get(POSITION));
  }

  @SuppressWarnings("deprecation")
  @Override
  public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
    final Direction facing = state.get(FACING);
    final Position type = state.get(POSITION);
    final Shape shape = state.get(SHAPE);
    return SHAPES.get(facing).get(type).get(shape);
  }

  private static VoxelShape composeShape(Direction facing, Position position, Shape shape) {
    List<VoxelShape> shapes = new ArrayList<>();

    // ???????????????????????????????????????????????????
    final Vec3d vector = Vec3d.of(facing.getVector());
    // ??????????????????????????????????????????????????????
    final Vec3d vector2 = Vec3d.of(facing.rotateYClockwise().getVector());
    new Vec3d(0.5d, 0, 0.5d).add(vector.multiply(0.5));
    Vec3d basePoint = new Vec3d(0.5d, 0, 0.5d).add(vector.multiply(0.5));
    basePoint = switch (position) {
      case LEFT -> basePoint.add(vector2.multiply(-7d / 16));
      case CENTER -> basePoint.add(vector2.multiply(-1 / 32d));
      case RIGHT -> basePoint.add(vector2.multiply(6d / 16));
    };

    // ?????????????????????
    if (shape == Shape.TOP) {
      shapes.add(VoxelShapes.cuboid(new Box(basePoint, basePoint.add(0, 14d / 16, 0).add(vector.multiply(-0.5d)).add(vector2.multiply(1 / 16d)))));
      basePoint = basePoint.add(vector.multiply(-0.5d));
    } else for (int i = 0; i < 8; i++) {
      shapes.add(VoxelShapes.cuboid(new Box(basePoint, basePoint.add(0, (22 - i) / 16d, 0).add(vector.multiply(-1d / 16)).add(vector2.multiply(1d / 16)))));
      basePoint = basePoint.add(vector.multiply(-1d / 16));
    }
    basePoint = basePoint.add(0, -0.5d, 0);

    // ?????????????????????
    if (shape == Shape.BOTTOM) {
      basePoint = basePoint.add(0, 0.5d, 0);
      shapes.add(VoxelShapes.cuboid(new Box(basePoint, basePoint.add(0, 14d / 16, 0).add(vector.multiply(-0.5d)).add(vector2.multiply(1 / 16d)))));
    } else for (int i = 0; i < 8; i++) {
      shapes.add(VoxelShapes.cuboid(new Box(basePoint, basePoint.add(0, (22 - i) / 16d, 0).add(vector.multiply(-1d / 16)).add(vector2.multiply(1d / 16)))));
      basePoint = basePoint.add(vector.multiply(-1d / 16));
    }

    return VoxelShapes.union(VoxelShapes.empty(), shapes.toArray(new VoxelShape[0]));
  }

  @Override
  public Item asItem() {
    return baseRail.asItem();
  }

  @Override
  public Identifier getItemId() {
    return Registry.ITEM.getId(asItem());
  }

  @Override
  public @Nullable Block baseBlock() {
    return baseRail.baseBlock();
  }

  @Override
  public boolean connectsIn(@NotNull BlockState blockState, @NotNull Direction direction, @Nullable Direction offsetFacing) {
    return offsetFacing == equivalentFacing(blockState)
        && (blockState.get(FACING) == direction && blockState.get(SHAPE) == Shape.TOP
        || blockState.get(FACING) == direction.getOpposite() && blockState.get(SHAPE) == Shape.BOTTOM);
  }

  @SuppressWarnings("deprecation")
  @Override
  public boolean isSideInvisible(BlockState state, BlockState stateFrom, Direction direction) {
    if (stateFrom.getBlock() instanceof final Handrails block) {
      return block.baseBlock() == this.baseBlock()
          && block.connectsIn(stateFrom, direction.getOpposite(), equivalentFacing(state));
    }
    return super.isSideInvisible(state, stateFrom, direction);
  }

  @SuppressWarnings("deprecation")
  @Override
  public FluidState getFluidState(BlockState state) {
    return (state.get(WATERLOGGED)) ? Fluids.WATER.getStill(false) : Fluids.EMPTY.getDefaultState();
  }

  @SuppressWarnings("deprecation")
  @Override
  public BlockState getStateForNeighborUpdate(BlockState state, Direction direction, BlockState neighborState, WorldAccess world, BlockPos pos, BlockPos neighborPos) {
    if (state.get(WATERLOGGED)) {
      world.createAndScheduleFluidTick(pos, Fluids.WATER, Fluids.WATER.getTickRate(world));
    }
    return super.getStateForNeighborUpdate(state, direction, neighborState, world, pos, neighborPos);
  }

  @Override
  public BlockState mirror(BlockState state, BlockMirror mirror) {
    final Direction facing = state.get(FACING);
    final Direction mirrored = mirror.apply(facing);
    return super.mirror(state, mirror)
        .with(FACING, mirrored)
        .with(POSITION, state.get(POSITION).swap());
  }

  @Override
  public MutableText getName() {
    final Block block = baseBlock();
    return block == null ? super.getName() : new TranslatableText("block.mishanguc.handrail_stair", block.getName());
  }

  public enum Position implements StringIdentifiable {
    LEFT("left"), CENTER("center"), RIGHT("right");

    private final String name;

    Position(@NotNull String name) {
      this.name = name;
    }

    @Override
    public String asString() {
      return this.name;
    }

    public Position swap() {
      return switch (this) {
        case LEFT -> RIGHT;
        case RIGHT -> LEFT;
        default -> this;
      };
    }
  }

  public enum Shape implements StringIdentifiable {
    BOTTOM("bottom"), MIDDLE("middle"), TOP("top");

    private final String name;

    Shape(String name) {
      this.name = name;
    }

    @Override
    public String asString() {
      return name;
    }
  }
}
