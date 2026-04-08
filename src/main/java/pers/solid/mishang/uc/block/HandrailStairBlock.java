package pers.solid.mishang.uc.block;

import com.google.common.collect.Iterators;
import com.google.common.collect.Maps;
import com.mojang.serialization.MapCodec;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.data.models.BlockModelGenerators;
import net.minecraft.client.data.models.blockstates.BlockModelDefinitionGenerator;
import net.minecraft.client.data.models.blockstates.MultiVariantGenerator;
import net.minecraft.client.data.models.blockstates.PropertyDispatch;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.Identifier;
import net.minecraft.util.RandomSource;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.ScheduledTickAccess;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.block.state.properties.Half;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.Unmodifiable;
import pers.solid.mishang.uc.MishangucProperties;
import pers.solid.mishang.uc.mixin.BlockStateModelGeneratorAccessor;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public abstract class HandrailStairBlock<T extends HandrailBlock> extends HorizontalDirectionalBlock implements SimpleWaterloggedBlock, MishangucBlock, Handrails {
  public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;
  public static final EnumProperty<Position> POSITION = MishangucProperties.HANDRAIL_STAIR_POSITION;
  public static final EnumProperty<Shape> SHAPE = MishangucProperties.HANDRAIL_STAIR_SHAPE;
  public final T baseHandrail;

  @Unmodifiable
  public static final Map<Direction, Map<Position, Map<Shape, VoxelShape>>> SHAPES = Maps.toMap(
      Direction.Plane.HORIZONTAL.iterator(),
      facing -> Maps.toMap(
          Iterators.forArray(Position.values()),
          position ->
              Maps.toMap(
                  Iterators.forArray(Shape.values()),
                  shape -> composeShape(facing, position, shape)
              )
      )
  );

  protected HandrailStairBlock(T baseHandrail, Properties settings) {
    super(settings);
    this.baseHandrail = baseHandrail;
    registerDefaultState(defaultBlockState()
        .setValue(WATERLOGGED, false)
        .setValue(POSITION, Position.CENTER)
        .setValue(FACING, Direction.SOUTH)
        .setValue(SHAPE, Shape.MIDDLE));
  }

  public HandrailStairBlock(T baseHandrail) {
    this(baseHandrail, Block.Properties.ofFullCopy(baseHandrail));
  }

  @Override
  protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
    super.createBlockStateDefinition(builder);
    builder.add(WATERLOGGED, POSITION, FACING, SHAPE);
  }

  /**
   * @param modelId 不含形状和位置信息的模型 ID
   */
  @Environment(EnvType.CLIENT)
  public BlockModelDefinitionGenerator createBlockStates(Identifier modelId) {
    return MultiVariantGenerator.dispatch(this)
        .with(PropertyDispatch.initial(POSITION, SHAPE)
            .generate((position, shape) -> BlockModelGenerators.variant(BlockModelGenerators.plainModel(modelId.withSuffix("_" + shape.getSerializedName() + "_" + position.getSerializedName())))))
        .with(BlockModelGenerators.UV_LOCK)
        .with(BlockStateModelGeneratorAccessor.getROTATION_HORIZONTAL_FACING());

  }

  @Nullable
  @Override
  public BlockState getStateForPlacement(BlockPlaceContext ctx) {
    final BlockState placementState = super.getStateForPlacement(ctx);
    if (placementState == null) return null;

    final BlockPos blockPos = ctx.getClickedPos();
    final Level world = ctx.getLevel();
    final Shape shape;
    final BlockState stateBelow = world.getBlockState(blockPos.below());
    final Direction facing;
    if (stateBelow.getBlock() instanceof StairBlock && stateBelow.hasProperty(StairBlock.FACING) && stateBelow.hasProperty(StairBlock.HALF) && stateBelow.getValue(StairBlock.HALF) == Half.BOTTOM) {
      facing = stateBelow.getValue(StairBlock.FACING);
      final BlockPos forwardPos = blockPos.relative(facing);
      final BlockState forwardState = world.getBlockState(forwardPos);
      if (forwardState.getBlock() instanceof StairBlock && forwardState.hasProperty(StairBlock.HALF) && forwardState.getValue(StairBlock.HALF) == Half.BOTTOM && forwardState.hasProperty(StairBlock.FACING) && forwardState.getValue(StairBlock.FACING) == facing) {
        shape = Shape.MIDDLE;
      } else {
        shape = Shape.TOP;
      }
    } else {
      facing = ctx.getHorizontalDirection();
      shape = Shape.BOTTOM;
    }
    final Vec3 hitPos = ctx.getClickLocation();
    final double diff = switch (facing) {
      case SOUTH -> hitPos.x - blockPos.getX();
      case NORTH -> blockPos.getX() + 1 - hitPos.x;
      case EAST -> blockPos.getZ() + 1 - hitPos.z;
      case WEST -> hitPos.z - blockPos.getZ();
      default -> 0.5;
    };
    return placementState.setValue(WATERLOGGED, world.getFluidState(blockPos).getType() == Fluids.WATER).setValue(FACING, facing).setValue(POSITION, diff < 0.3 ? Position.RIGHT : diff < 0.7 ? Position.CENTER : Position.LEFT).setValue(SHAPE, shape);
  }

  /**
   * 该方向和位置对应的等价朝向。
   *
   * @param facing   该楼梯扶手方块的朝向，即楼梯向上走的方向。
   * @param position 位置。
   * @return 该楼梯等价的侧边朝向。若 position 为 CENTER，则没有朝向。
   */
  @Nullable
  protected static Direction equivalentFacing(Direction facing, Position position) {
    return switch (position) {
      case LEFT -> facing.getClockWise();
      case RIGHT -> facing.getCounterClockWise();
      case CENTER -> null;
    };
  }

  /**
   * 该方块状态的等价朝向。
   *
   * @param state 方块状态，必须是楼梯扶手方块的方块状态。
   * @return 该楼梯等价的侧边朝向。
   */
  @Nullable
  public static Direction equivalentFacing(BlockState state) {
    return equivalentFacing(state.getValue(FACING), state.getValue(POSITION));
  }

  @Override
  public VoxelShape getShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext context) {
    final Direction facing = state.getValue(FACING);
    final Position type = state.getValue(POSITION);
    final Shape shape = state.getValue(SHAPE);
    return SHAPES.get(facing).get(type).get(shape);
  }

  private static VoxelShape composeShape(Direction facing, Position position, Shape shape) {
    List<VoxelShape> shapes = new ArrayList<>();

    // 沿着楼梯前进上升的方向的单位向量。
    final Vec3 forwardUnit = Vec3.atLowerCornerOf(facing.getUnitVec3i());
    // 沿着楼梯前进方向看，向右的单位向量。
    final Vec3 rightUnit = Vec3.atLowerCornerOf(facing.getClockWise().getUnitVec3i());
    new Vec3(0.5d, 0, 0.5d).add(forwardUnit.scale(0.5));
    Vec3 basePoint = new Vec3(0.5d, 0, 0.5d).add(forwardUnit.scale(0.5));
    basePoint = switch (position) {
      case LEFT -> basePoint.add(rightUnit.scale(-7.5d / 16));
      case CENTER -> basePoint.add(rightUnit.scale(-1d / 16));
      case RIGHT -> basePoint.add(rightUnit.scale(5.5d / 16));
    };

    // 上半部分的栏杆
    if (shape == Shape.TOP) {
      shapes.add(Shapes.create(new AABB(basePoint, basePoint.add(0, 16d / 16, 0).add(forwardUnit.scale(-0.5d)).add(rightUnit.scale(2 / 16d)))));
      basePoint = basePoint.add(forwardUnit.scale(-0.5d));
    } else for (int i = 0; i < 8; i++) {
      shapes.add(Shapes.create(new AABB(basePoint, basePoint.add(0, (24 - i) / 16d, 0).add(forwardUnit.scale(-1d / 16)).add(rightUnit.scale(2d / 16)))));
      basePoint = basePoint.add(forwardUnit.scale(-1d / 16));
    }
    basePoint = basePoint.add(0, -0.5d, 0);

    // 下半部分的栏杆
    if (shape == Shape.BOTTOM) {
      basePoint = basePoint.add(0, 0.5d, 0);
      shapes.add(Shapes.create(new AABB(basePoint, basePoint.add(0, 16d / 16, 0).add(forwardUnit.scale(-0.5d)).add(rightUnit.scale(2 / 16d)))));
    } else for (int i = 0; i < 8; i++) {
      shapes.add(Shapes.create(new AABB(basePoint, basePoint.add(0, (24 - i) / 16d, 0).add(forwardUnit.scale(-1d / 16)).add(rightUnit.scale(2d / 16)))));
      basePoint = basePoint.add(forwardUnit.scale(-1d / 16));
    }

    return Shapes.or(Shapes.empty(), shapes.toArray(new VoxelShape[0]));
  }

  @Override
  public Item asItem() {
    return baseHandrail.asItem();
  }

  @Override
  public @Nullable Block baseBlock() {
    return baseHandrail.baseBlock();
  }

  @Override
  public boolean connectsIn(BlockState blockState, Direction direction, @Nullable Direction offsetFacing) {
    return offsetFacing == equivalentFacing(blockState)
        && (blockState.getValue(FACING) == direction && blockState.getValue(SHAPE) == Shape.TOP
        || blockState.getValue(FACING) == direction.getOpposite() && blockState.getValue(SHAPE) == Shape.BOTTOM);
  }

  @Override
  public boolean skipRendering(BlockState state, BlockState stateFrom, Direction direction) {
    final Block block = stateFrom.getBlock();
    if (block instanceof final Handrails handrails) {
      return handrails.connectsIn(stateFrom, direction.getOpposite(), equivalentFacing(state))
          && block.asItem() == this.asItem();  // 仅限同一栏杆物品对应的方块
    }
    return super.skipRendering(state, stateFrom, direction);
  }

  @Override
  public FluidState getFluidState(BlockState state) {
    return (state.getValue(WATERLOGGED)) ? Fluids.WATER.getSource(false) : Fluids.EMPTY.defaultFluidState();
  }

  @Override
  protected BlockState updateShape(BlockState state, LevelReader world, ScheduledTickAccess tickView, BlockPos pos, Direction direction, BlockPos neighborPos, BlockState neighborState, RandomSource random) {
    if (state.getValue(WATERLOGGED)) {
      tickView.scheduleTick(pos, Fluids.WATER, Fluids.WATER.getTickDelay(world));
    }
    return super.updateShape(state, world, tickView, pos, direction, neighborPos, neighborState, random);
  }

  @Override
  public BlockState mirror(BlockState state, Mirror mirror) {
    final Direction facing = state.getValue(FACING);
    final Direction mirrored = mirror.mirror(facing);
    return super.mirror(state, mirror)
        .setValue(FACING, mirrored)
        .setValue(POSITION, state.getValue(POSITION).swap());
  }

  @Override
  public MutableComponent getName() {
    final Block block = baseBlock();
    return block == null ? super.getName() : Component.translatable("block.mishanguc.handrail_stair", block.getName());
  }

  @Override
  protected abstract MapCodec<? extends HandrailStairBlock<?>> codec();

  @Override
  protected boolean isPathfindable(BlockState state, PathComputationType type) {
    return false;
  }

  public enum Position implements StringRepresentable {
    LEFT("left"), CENTER("center"), RIGHT("right");
    public static final EnumCodec<Position> CODEC = StringRepresentable.fromEnum(Position::values);
    private final String name;

    Position(String name) {
      this.name = name;
    }

    @Override
    public String getSerializedName() {
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

  public enum Shape implements StringRepresentable {
    BOTTOM("bottom"), MIDDLE("middle"), TOP("top");
    public static final EnumCodec<Shape> CODEC = StringRepresentable.fromEnum(Shape::values);
    private final String name;

    Shape(String name) {
      this.name = name;
    }

    @Override
    public String getSerializedName() {
      return name;
    }
  }
}
