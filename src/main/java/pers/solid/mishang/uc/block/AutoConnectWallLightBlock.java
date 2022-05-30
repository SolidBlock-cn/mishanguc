package pers.solid.mishang.uc.block;

import com.google.common.collect.ImmutableList;
import net.devtech.arrp.api.RuntimeResourcePack;
import net.devtech.arrp.json.blockstate.JBlockModel;
import net.devtech.arrp.json.blockstate.JBlockStates;
import net.devtech.arrp.json.blockstate.JMultipart;
import net.devtech.arrp.json.blockstate.JWhenProperties;
import net.devtech.arrp.json.models.JModel;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.ShapeContext;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.Properties;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;
import org.jetbrains.annotations.NotNull;
import pers.solid.mishang.uc.MishangUtils;
import pers.solid.mishang.uc.Mishanguc;
import pers.solid.mishang.uc.arrp.FasterJTextures;

import java.util.*;

public class AutoConnectWallLightBlock extends WallLightBlock implements LightConnectable {

  /**
   * 每个朝向中，中心基础碰撞箱。任何自动连接的灯都会有此碰撞箱，且不进行任何连接的灯仅使用此碰撞箱。键为灯的 {@link #FACING} 属性。
   */
  private static final Map<Direction, VoxelShape> BASE_SHAPE_PER_FACING =
      MishangUtils.createDirectionToShape(4, 0, 4, 12, 1, 12);
  /**
   * 当 {@link #FACING} 为 {@link Direction#UP} 时，方块的各个连接物的碰撞箱。键为水平方向。
   */
  private static final Map<Direction, VoxelShape> SHAPE_PER_DIRECTION_WHEN_FACING_UP =
      MishangUtils.createHorizontalDirectionToShape(4, 0, 12, 12, 1, 16);
  /**
   * 当 {@link #FACING} 为 {@link Direction#DOWN} 时，方块的各个连接物的碰撞箱。键为水平方向。
   */
  private static final Map<Direction, VoxelShape> SHAPE_PER_DIRECTION_WHEN_FACING_DOWN =
      MishangUtils.createHorizontalDirectionToShape(4, 15, 4, 12, 16, 12);

  /**
   * 当 {@link #FACING} 为水平方向时，方块各个 direction 和 facing 对应的碰撞箱。第一个键为 direction 与 facing
   * 对应的关系（用数字表示），第二个键为 facing。<br>
   * 这样安排是为了方便。
   */
  private static final List<Map<Direction, VoxelShape>>
      SHAPE_PER_DIRECTION_PER_FACING_WHEN_FACING_HORIZONTALLY =
      ImmutableList.of(
          // 第一个元素为“上”。
          MishangUtils.createHorizontalDirectionToShape(4, 12, 0, 12, 16, 1),
          // 第二个元素为“下”。
          MishangUtils.createHorizontalDirectionToShape(4, 0, 0, 12, 4, 1),
          // 第三个元素为“右”，即 facing 逆时针旋转90度。
          MishangUtils.createHorizontalDirectionToShape(12, 4, 0, 16, 12, 1),
          // 第四个元素为“左”，即 facing 顺时针旋转90度。
          MishangUtils.createHorizontalDirectionToShape(0, 4, 0, 4, 12, 1));
  /**
   * 该灯光装饰方块对应的形状。
   */
  public final String shape;

  public AutoConnectWallLightBlock(String lightColor, String shape, Settings settings) {
    super(lightColor, settings);
    this.shape = shape;
    this.setDefaultState(
        this.stateManager
            .getDefaultState()
            .with(WEST, false)
            .with(EAST, false)
            .with(SOUTH, false)
            .with(NORTH, false)
            .with(UP, false)
            .with(DOWN, false));
  }

  @Override
  protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
    super.appendProperties(builder);
    builder.add(WEST, EAST, SOUTH, NORTH, UP, DOWN);
  }

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
    final BlockState newState =
        super.getStateForNeighborUpdate(state, direction, neighborState, world, pos, neighborPos);
    final Direction facing = state.get(FACING);
    final Block neighborBlock = neighborState.getBlock();
    boolean connect = false;

    // 检查该方向上与之直接毗邻的 LightConnectable 方块，如果符合，则修改自身。
    if (neighborBlock instanceof final LightConnectable lightConnectable) {
      connect =
          lightConnectable
              .isConnectedIn(neighborState, facing, direction.getOpposite());
    }

    // 检查该方向上不与之毗邻（与毗邻位置往 facing.getOpposite() 方向偏移一格）的方块。
    final BlockPos neighborPos2 = pos.offset(direction).offset(facing.getOpposite());
    final BlockState neighborState2 = world.getBlockState(neighborPos2);
    final Block neighborBlock2 = neighborState2.getBlock();
    if (neighborBlock2 instanceof final LightConnectable lightConnectable) {
      connect =
          connect
              || lightConnectable
              .isConnectedIn(neighborState2, direction, facing);
    }
    return newState.with(DIRECTION_TO_PROPERTY.get(direction), connect);
  }

  @Override
  public BlockState getPlacementState(ItemPlacementContext ctx) {
    BlockState placementState = super.getPlacementState(ctx);
    if (placementState == null) {
      return null;
    }
    final Direction facing = placementState.get(FACING);
    for (Direction direction : Direction.values()) {
      if (direction.getAxis() == facing.getAxis()) {
        continue;
      }
      final BlockPos blockPos = ctx.getBlockPos();
      final World world = ctx.getWorld();
      final BlockPos offsetBlockPos = blockPos.offset(direction);
      placementState =
          getStateForNeighborUpdate(
              placementState,
              direction,
              world.getBlockState(offsetBlockPos),
              world,
              blockPos,
              offsetBlockPos);
    }
    return placementState;
  }

  @Override
  public boolean isConnectedIn(BlockState blockState, Direction facing, Direction direction) {
    return blockState.get(FACING) == facing;
  }

  @Override
  public VoxelShape getOutlineShape(
      BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
    final Direction facing = state.get(FACING);
    final VoxelShape baseShape = BASE_SHAPE_PER_FACING.get(facing);
    final VoxelShape[] extraShapes;
    switch (facing) {
      case UP -> extraShapes =
          Arrays.stream(Direction.values())
              .filter(direction -> state.get(DIRECTION_TO_PROPERTY.get(direction)))
              .map(SHAPE_PER_DIRECTION_WHEN_FACING_UP::get)
              .filter(Objects::nonNull)
              .toArray(VoxelShape[]::new);
      case DOWN -> extraShapes =
          Arrays.stream(Direction.values())
              .filter(direction -> state.get(DIRECTION_TO_PROPERTY.get(direction)))
              .map(SHAPE_PER_DIRECTION_WHEN_FACING_DOWN::get)
              .filter(Objects::nonNull)
              .toArray(VoxelShape[]::new);
      default -> {
        final List<VoxelShape> voxelShapeList = new ArrayList<>();
        if (state.get(UP)) {
          voxelShapeList.add(
              SHAPE_PER_DIRECTION_PER_FACING_WHEN_FACING_HORIZONTALLY.get(0).get(facing));
        }
        if (state.get(DOWN)) {
          voxelShapeList.add(
              SHAPE_PER_DIRECTION_PER_FACING_WHEN_FACING_HORIZONTALLY.get(1).get(facing));
        }
        if (state.get(DIRECTION_TO_PROPERTY.get(facing.rotateYCounterclockwise()))) {
          voxelShapeList.add(
              SHAPE_PER_DIRECTION_PER_FACING_WHEN_FACING_HORIZONTALLY.get(2).get(facing));
        }
        if (state.get(DIRECTION_TO_PROPERTY.get(facing.rotateYClockwise()))) {
          voxelShapeList.add(
              SHAPE_PER_DIRECTION_PER_FACING_WHEN_FACING_HORIZONTALLY.get(3).get(facing));
        }
        extraShapes = voxelShapeList.toArray(new VoxelShape[]{});
      }
    }
    return VoxelShapes.union(baseShape, extraShapes);
  }

  @Environment(EnvType.CLIENT)
  @Override
  public @NotNull JBlockStates getBlockStates() {
    List<JMultipart> parts = new ArrayList<>();
    final Identifier id = getBlockModelId();
    for (Direction facing : Direction.values()) {
      // 中心装饰物
      JBlockModel jBlockModel = new JBlockModel(id.brrp_append("_center"))
          .y(facing.getAxis() == Direction.Axis.Y ? 0 : (int) (facing.asRotation() + 180))
          .x(facing == Direction.DOWN ? 180 : facing == Direction.UP ? 0 : 90);
      parts.add(new JMultipart(new JWhenProperties().add(FACING, facing), jBlockModel));

      // 连接物
      // 共有两种连接物模型：一种是位于底部或顶部的朝南连接，可以通过x和y的旋转得到位于底部朝向任意方向的连接，以及位于侧面朝向垂直方向的连接。
      // 第二种是位于侧面的朝东连接，可以通过x和y的旋转得到任意水平方向上的，以及底部或顶部任意连接。
      for (Direction direction : Direction.values()) {
        final Direction.Axis axis = direction.getAxis();
        final int x, y;
        final Identifier modelName;
        if (axis == facing.getAxis()) {
          continue;
        }
        if (facing == Direction.UP) {
          modelName = id.brrp_append("_connection");
          x = 0;
          y = (int) direction.asRotation();
        } else if (facing == Direction.DOWN) {
          modelName = id.brrp_append("_connection");
          x = 180;
          y = (int) direction.asRotation() + 180;
        } else if (direction == Direction.UP) {
          modelName = id.brrp_append("_connection");
          x = 90;
          y = (int) facing.asRotation() + 180;
        } else if (direction == Direction.DOWN) {
          modelName = id.brrp_append("_connection");
          x = -90;
          y = (int) facing.asRotation();
        } else if (direction == facing.rotateYCounterclockwise()) {
          modelName = id.brrp_append("_connection2");
          x = 0;
          y = (int) facing.asRotation();
        } else if (direction == facing.rotateYClockwise()) {
          modelName = id.brrp_append("_connection2");
          x = 180;
          y = (int) facing.asRotation() + 180;
        } else {
          Mishanguc.MISHANG_LOGGER.error(
              String.format(
                  "Unknown state to generate models: facing=%s,direction=%s",
                  facing.asString(), direction.asString()));
          continue;
        }
        parts.add(
            new JMultipart(new JWhenProperties()
                .add(FACING, facing)
                .add(direction.asString(), "true"), new JBlockModel(modelName).x(x).y(y).uvlock()));
      }
    }
    return JBlockStates.ofMultiparts(parts.toArray(JMultipart[]::new));
  }

  @Environment(EnvType.CLIENT)
  @Override
  public void writeBlockModel(RuntimeResourcePack pack) {
    final Identifier id = getBlockModelId();
    pack.addModel(
        new JModel(String.format("mishanguc:block/wall_light_%s_decoration", shape))
            .textures(new FasterJTextures().varP("light", lightColor + "_light")),
        id);
    pack.addModel(
        new JModel(String.format("mishanguc:block/wall_light_%s_decoration_center", shape))
            .textures(new FasterJTextures().varP("light", lightColor + "_light")),
        id.brrp_append("_center"));
    pack.addModel(
        new JModel(String.format("mishanguc:block/wall_light_%s_decoration_connection", shape))
            .textures(new FasterJTextures().varP("light", lightColor + "_light")),
        id.brrp_append("_connection"));
    pack.addModel(
        new JModel(String.format("mishanguc:block/wall_light_%s_decoration_connection2", shape))
            .textures(new FasterJTextures().varP("light", lightColor + "_light")),
        id.brrp_append("_connection2"));
  }
}
