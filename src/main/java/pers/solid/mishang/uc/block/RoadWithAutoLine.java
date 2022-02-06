package pers.solid.mishang.uc.block;

import com.google.common.collect.Maps;
import com.mojang.datafixers.util.Either;
import net.minecraft.block.AirBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.Properties;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Formatting;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import pers.solid.mishang.uc.LineColor;
import pers.solid.mishang.uc.ModProperties;
import pers.solid.mishang.uc.RoadTexture;

import java.util.EnumMap;
import java.util.List;
import java.util.Map;

public interface RoadWithAutoLine extends Road {
  /**
   * 根据附近的连接状态自动产生一个新的方块状态。
   *
   * @param connectionStateMap 连接状态映射，各个方向的连接状态。
   * @param defaultState 默认方块状态。
   * @return 转换后的方块状态。
   */
  BlockState makeState(
      EnumMap<Direction, RoadConnectionState> connectionStateMap, BlockState defaultState);

  /**
   * 获取附近的连接状态映射。
   *
   * @param world 世界。
   * @param pos0 坐标。
   * @return 连接状态的映射。
   */
  default EnumMap<Direction, RoadConnectionState> getConnectionStateMap(
      WorldAccess world, BlockPos pos0) {
    EnumMap<Direction, RoadConnectionState> connectionStateMap = Maps.newEnumMap(Direction.class);
    for (Direction direction : Direction.Type.HORIZONTAL) {
      RoadConnectionState state = RoadConnectionState.empty();
      // 检查毗邻方块及其上下方。
      for (BlockPos pos : new BlockPos[] {pos0, pos0.up(), pos0.down()}) {
        BlockState nextState = world.getBlockState(pos.offset(direction, 1));
        Block nextBlock = nextState.getBlock();
        if (nextBlock instanceof Road) {
          RoadConnectionState connectionState =
              ((Road) nextBlock).getConnectionStateOf(nextState, direction.getOpposite());
          if (connectionState.mayConnect()) {
            state = connectionState;
            break;
          }
        }
      }
      connectionStateMap.put(direction, state);
    }
    return connectionStateMap;
  }

  @Override
  default RoadConnectionState getConnectionStateOf(BlockState state, Direction direction) {
    return Road.super
        .getConnectionStateOf(state, direction)
        .or(RoadConnectionState.mayConnectTo(getLineColor(), Either.left(direction)));
  }

  @Override
  default void appendRoadProperties(StateManager.Builder<Block, BlockState> builder) {
    Road.super.appendRoadProperties(builder);
  }

  @Override
  default ActionResult onUseRoad(
      BlockState state,
      World world,
      BlockPos pos,
      PlayerEntity player,
      Hand hand,
      BlockHitResult hit) {
    Road.super.onUseRoad(state, world, pos, player, hand, hit);
    final Item item = player.getStackInHand(hand).getItem();
    if (item instanceof BlockItem
        && ((BlockItem) item).getBlock() instanceof RoadWithAutoLine
        && !Direction.Type.VERTICAL.test(hit.getSide())) {
      return ActionResult.PASS;
    }
    world.setBlockState(pos, makeState(getConnectionStateMap(world, pos), state), 2);
    return ActionResult.SUCCESS;
  }

  @Override
  default void neighborRoadUpdate(
      BlockState state, World world, BlockPos pos, Block block, BlockPos fromPos, boolean notify) {
    // 屏蔽上下方的更新。
    if (!fromPos.equals(pos.up())
        && !fromPos.equals(pos.down())
        && !(world.getBlockState(fromPos).getBlock() instanceof AirBlock))
    // flags设为2从而使得 <code>flags&1 !=0</code> 不成立，从而不递归更新邻居，参考 {@link World#setBlockState}。
    {
      world.setBlockState(pos, makeState(getConnectionStateMap(world, pos), state), 2);
    }
    Road.super.neighborRoadUpdate(state, world, pos, block, fromPos, notify);
  }

  @Override
  default void appendRoadTooltip(
      ItemStack stack, @Nullable BlockView world, List<Text> tooltip, TooltipContext options) {
    Road.super.appendRoadTooltip(stack, world, tooltip, options);
    tooltip.add(
        new TranslatableText("block.mishanguc.tooltip.road_with_auto_line.1")
            .formatted(Formatting.GRAY));
    tooltip.add(
        new TranslatableText("block.mishanguc.tooltip.road_with_auto_line.2")
            .formatted(Formatting.GRAY));
  }

  /** 道路自动连接的类型，分为直角和斜线。 */
  enum RoadAutoLineType {
    /** 直角 */
    RIGHT_ANGLE,
    /** 45°的斜角 */
    BEVEL
  }

  class SlabImpl extends AbstractRoadSlabBlock implements RoadWithAutoLine {
    public final RoadAutoLineType type;
    private final RoadTexture texture;
    private final LineColor lineColor;

    public SlabImpl(
        Settings settings, RoadAutoLineType type, RoadTexture texture, LineColor lineColor) {
      super(settings, lineColor);
      this.type = type;
      this.texture = texture;
      this.lineColor = lineColor;
    }

    @SuppressWarnings("AlibabaMethodTooLong")
    @Override
    public @NotNull BlockState makeState(
        EnumMap<Direction, RoadConnectionState> connectionStateMap, BlockState defaultState) {
      int connected = 0;
      for (Map.Entry<Direction, RoadConnectionState> e : connectionStateMap.entrySet()) {
        RoadConnectionState value = e.getValue();
        if (value.mayConnect()) {
          connected++;
        }
      }
      if (texture == RoadTexture.ASPHALT) {
        switch (connected) {
          case 0:
            // 全都不连接的情况。
            return MishangucBlocks.ASPHALT_ROAD_SLAB
                .getDefaultState()
                .with(Properties.SLAB_TYPE, defaultState.get(Properties.SLAB_TYPE))
                .with(WATERLOGGED, defaultState.get(WATERLOGGED));
          case 4:
            // 全都连接的情况。
            if (lineColor == LineColor.WHITE) {
              return MishangucBlocks.ASPHALT_ROAD_SLAB_WITH_WHITE_CROSS_LINE
                  .getDefaultState()
                  .with(Properties.SLAB_TYPE, defaultState.get(Properties.SLAB_TYPE))
                  .with(WATERLOGGED, defaultState.get(WATERLOGGED));
            } else {
              return defaultState;
            }
          case 2:
            // 仅有两种方向的情况，又分为两种：直线和直角/斜线。
            for (Map.Entry<Direction, RoadConnectionState> entry : connectionStateMap.entrySet()) {
              if (!entry.getValue().mayConnect()) {
                continue;
              }
              Direction direction = entry.getKey();
              if (connectionStateMap.get(direction.getOpposite()).mayConnect()) {
                // 如果对面方向也有的情况。
                return MishangucBlocks.ASPHALT_ROAD_SLAB_WITH_WHITE_STRAIGHT_LINE
                    .getDefaultState()
                    .with(Properties.SLAB_TYPE, defaultState.get(Properties.SLAB_TYPE))
                    .with(WATERLOGGED, defaultState.get(WATERLOGGED))
                    .with(Properties.HORIZONTAL_AXIS, direction.getAxis());
              } else {
                // 直角或斜角的情况。
                Direction direction2 = direction.rotateYClockwise();
                Block block;
                if (lineColor == LineColor.WHITE) {
                  block = switch (type) {
                    case BEVEL -> MishangucBlocks.ASPHALT_ROAD_SLAB_WITH_WHITE_BEVEL_ANGLE_LINE;
                    case RIGHT_ANGLE -> MishangucBlocks.ASPHALT_ROAD_SLAB_WITH_WHITE_RIGHT_ANGLE_LINE;
                  };
                } else {
                  block = null;
                }
                if (block == null) {
                  return defaultState;
                }
                // 现在已经确定好了block的类型，现在考虑方向。
                BlockState state =
                    block
                        .getDefaultState()
                        .with(Properties.SLAB_TYPE, defaultState.get(Properties.SLAB_TYPE))
                        .with(WATERLOGGED, defaultState.get(WATERLOGGED));
                if (state.contains(RoadWithAngleLine.FACING)) {
                  return state.with(
                      RoadWithAngleLine.FACING,
                      HorizontalCornerDirection.fromDirections(
                          direction,
                          connectionStateMap.get(direction2).mayConnect()
                              ? direction2
                              : direction.rotateYCounterclockwise()));
                }
              }
            }
          case 1:
            // 只有一个方向连接的情况下，为直线。
            for (Map.Entry<Direction, RoadConnectionState> entry : connectionStateMap.entrySet()) {
              if (entry.getValue().mayConnect() && lineColor == LineColor.WHITE) {
                return MishangucBlocks.ASPHALT_ROAD_SLAB_WITH_WHITE_STRAIGHT_LINE
                    .getDefaultState()
                    .with(Properties.SLAB_TYPE, defaultState.get(Properties.SLAB_TYPE))
                    .with(WATERLOGGED, defaultState.get(WATERLOGGED))
                    .with(Properties.HORIZONTAL_AXIS, entry.getKey().getAxis());
              }
            }
            return defaultState;
          case 3:
            for (Map.Entry<Direction, RoadConnectionState> entry : connectionStateMap.entrySet()) {
              if (!entry.getValue().mayConnect() && lineColor == LineColor.WHITE)
              // 检测需要连接正向的方块对应的连接是否为斜线。
              {
                // 朝向的方块
                Direction direction = entry.getKey().getOpposite();
                RoadConnectionState state = connectionStateMap.get(direction);
                if (state.direction == null
                    || state.direction.left().isPresent()
                    || type != RoadAutoLineType.BEVEL)
                // 连接直线
                {
                  return MishangucBlocks.ASPHALT_ROAD_SLAB_WITH_WHITE_JOINT_LINE
                      .getDefaultState()
                      .with(Properties.SLAB_TYPE, defaultState.get(Properties.SLAB_TYPE))
                      .with(WATERLOGGED, defaultState.get(WATERLOGGED))
                      .with(Properties.HORIZONTAL_FACING, direction);
                } else if (state.direction.right().isPresent()) {
                  // 连接斜线
                  return MishangucBlocks.ASPHALT_ROAD_SLAB_WITH_WHITE_STRAIGHT_AND_BEVEL_ANGLE_LINE
                      .getDefaultState()
                      .with(Properties.SLAB_TYPE, defaultState.get(Properties.SLAB_TYPE))
                      .with(WATERLOGGED, defaultState.get(WATERLOGGED))
                      .with(Properties.HORIZONTAL_AXIS, direction.rotateYClockwise().getAxis())
                      .with(
                          ModProperties.HORIZONTAL_CORNER_FACING,
                          state.direction.right().get().getOpposite().mirror(direction));
                }
              }
            }
            return defaultState;
          default:
            throw new IllegalStateException("Illegal connected number: " + connected);
        }
      } else {
        throw new UnsupportedOperationException(
            "Road auto placement of non-asphalt road is not supported yet! qwq");
      }
    }
  }

  class Impl extends AbstractRoadBlock implements RoadWithAutoLine {
    public final RoadAutoLineType type;
    private final RoadTexture texture;
    private final LineColor lineColor;

    public Impl(
        Settings settings, RoadAutoLineType type, RoadTexture texture, LineColor lineColor) {
      super(settings, lineColor);
      this.type = type;
      this.texture = texture;
      this.lineColor = lineColor;
    }

    @SuppressWarnings("AlibabaMethodTooLong")
    @Override
    public @NotNull BlockState makeState(
        EnumMap<Direction, RoadConnectionState> connectionStateMap, BlockState defaultState) {
      int connected = 0;
      for (Map.Entry<Direction, RoadConnectionState> e : connectionStateMap.entrySet()) {
        RoadConnectionState value = e.getValue();
        if (value.mayConnect()) {
          connected++;
        }
      }
      if (texture == RoadTexture.ASPHALT) {
        switch (connected) {
          case 0:
            // 全都不连接的情况。
            return MishangucBlocks.ASPHALT_ROAD_BLOCK.getDefaultState();
          case 4:
            // 全都连接的情况。
            if (lineColor == LineColor.WHITE) {
              return MishangucBlocks.ASPHALT_ROAD_WITH_WHITE_CROSS_LINE.getDefaultState();
            } else {
              return defaultState;
            }
          case 2:
            // 仅有两种方向的情况，又分为两种：直线和直角/斜线。
            for (Map.Entry<Direction, RoadConnectionState> entry : connectionStateMap.entrySet()) {
              if (!entry.getValue().mayConnect()) {
                continue;
              }
              Direction direction = entry.getKey();
              if (connectionStateMap.get(direction.getOpposite()).mayConnect()) {
                // 如果对面方向也有的情况。
                return MishangucBlocks.ASPHALT_ROAD_WITH_WHITE_STRAIGHT_LINE
                    .getDefaultState()
                    .with(Properties.HORIZONTAL_AXIS, direction.getAxis());
              } else {
                // 直角或斜角的情况。
                Direction direction2 = direction.rotateYClockwise();
                Block block;
                if (lineColor == LineColor.WHITE) {
                  block = switch (type) {
                    case BEVEL -> MishangucBlocks.ASPHALT_ROAD_WITH_WHITE_BEVEL_ANGLE_LINE;
                    case RIGHT_ANGLE -> MishangucBlocks.ASPHALT_ROAD_WITH_WHITE_RIGHT_ANGLE_LINE;
                  };
                } else {
                  block = null;
                }
                if (block == null) {
                  return defaultState;
                }
                // 现在已经确定好了block的类型，现在考虑方向。
                BlockState state = block.getDefaultState();
                if (state.contains(RoadWithAngleLine.FACING)) {
                  return state.with(
                      RoadWithAngleLine.FACING,
                      HorizontalCornerDirection.fromDirections(
                          direction,
                          connectionStateMap.get(direction2).mayConnect()
                              ? direction2
                              : direction.rotateYCounterclockwise()));
                }
              }
            }
          case 1:
            // 只有一个方向连接的情况下，为直线。
            for (Map.Entry<Direction, RoadConnectionState> entry : connectionStateMap.entrySet()) {
              if (entry.getValue().mayConnect() && lineColor == LineColor.WHITE) {
                return MishangucBlocks.ASPHALT_ROAD_WITH_WHITE_STRAIGHT_LINE
                    .getDefaultState()
                    .with(Properties.HORIZONTAL_AXIS, entry.getKey().getAxis());
              }
            }
            return defaultState;
          case 3:
            for (Map.Entry<Direction, RoadConnectionState> entry : connectionStateMap.entrySet()) {
              if (!entry.getValue().mayConnect() && lineColor == LineColor.WHITE)
              // 检测需要连接正向的方块对应的连接是否为斜线。
              {
                // 朝向的方块
                Direction direction = entry.getKey().getOpposite();
                RoadConnectionState state = connectionStateMap.get(direction);
                if (state.direction == null
                    || state.direction.left().isPresent()
                    || type != RoadAutoLineType.BEVEL)
                // 连接直线
                {
                  return MishangucBlocks.ASPHALT_ROAD_WITH_WHITE_JOINT_LINE
                      .getDefaultState()
                      .with(Properties.HORIZONTAL_FACING, direction);
                } else if (state.direction.right().isPresent()) {
                  // 连接斜线
                  return MishangucBlocks.ASPHALT_ROAD_WITH_WHITE_STRAIGHT_AND_BEVEL_ANGLE_LINE
                      .getDefaultState()
                      .with(Properties.HORIZONTAL_AXIS, direction.rotateYClockwise().getAxis())
                      .with(
                          ModProperties.HORIZONTAL_CORNER_FACING,
                          state.direction.right().get().getOpposite().mirror(direction));
                }
              }
            }
            return defaultState;
          default:
            throw new IllegalStateException("Illegal connected number: " + connected);
        }
      } else {
        throw new UnsupportedOperationException(
            "Road auto placement of non-asphalt road is not supported yet! qwq");
      }
    }
  }
}
