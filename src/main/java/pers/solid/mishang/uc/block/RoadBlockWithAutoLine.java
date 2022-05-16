package pers.solid.mishang.uc.block;

import net.devtech.arrp.json.blockstate.JBlockStates;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.BlockState;
import net.minecraft.state.property.Properties;
import net.minecraft.util.math.Direction;
import org.apache.commons.lang3.ObjectUtils;
import org.jetbrains.annotations.NotNull;
import pers.solid.mishang.uc.MishangucProperties;
import pers.solid.mishang.uc.util.HorizontalCornerDirection;
import pers.solid.mishang.uc.util.LineColor;
import pers.solid.mishang.uc.util.LineType;
import pers.solid.mishang.uc.util.RoadConnectionState;

import java.util.EnumMap;
import java.util.Map;

import static pers.solid.mishang.uc.blocks.RoadBlocks.*;

/**
 * 具有自动连接功能的道路方块。关于本模组的所有道路方块，请参见 {@link pers.solid.mishang.uc.blocks.RoadBlocks}。
 */
public class RoadBlockWithAutoLine extends AbstractRoadBlock implements RoadWithAutoLine {
  /**
   * 该自动连接方块是直角的还是斜角的。
   */
  public final RoadAutoLineType type;

  public RoadBlockWithAutoLine(Settings settings, RoadAutoLineType type) {
    super(settings, LineColor.UNKNOWN, LineType.NORMAL);
    this.type = type;
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
    switch (connected) {
      case 0:
        // 全都不连接的情况。
        return ROAD_BLOCK.getDefaultState();
      case 4:
        // 全都连接的情况。这种情况下，不论道路颜色，一律返回白色的十字形道路。
        return ROAD_WITH_WHITE_CROSS_LINE.getDefaultState();
      case 2:
        // 仅有两种方向的情况，又分为两种：直线和直角/斜线。
        for (Map.Entry<Direction, RoadConnectionState> entry : connectionStateMap.entrySet()) {
          // 当前这个方向的连接状态
          final RoadConnectionState connectionState = entry.getValue();
          if (!connectionState.mayConnect()) {
            continue;
          }
          Direction direction = entry.getKey();

          // 相邻的一个有连接的道路的方向
          final Direction adjacentDirection;
          // 相邻的一个有连接的道路的连接状态
          final RoadConnectionState adjacentState;
          if (connectionStateMap.get(direction.getOpposite()).mayConnect()) {
            adjacentDirection = direction.getOpposite();
          } else if (connectionStateMap.get(direction.rotateYClockwise()).mayConnect()) {
            adjacentDirection = direction.rotateYClockwise();
          } else {
            adjacentDirection = direction.rotateYCounterclockwise();
          }
          adjacentState = connectionStateMap.get(adjacentDirection);

          if (adjacentDirection == direction.getOpposite()) {
            // 道路的两个相对方向都连接了道路。
            final LineColor color = ObjectUtils.max(connectionState.lineColor, adjacentState.lineColor);
            final LineType type = ObjectUtils.min(connectionState.lineType, adjacentState.lineType);
            return (
                color == LineColor.YELLOW ?
                    switch (type) {
                      case DOUBLE -> ROAD_WITH_YELLOW_DOUBLE_LINE;
                      case THICK -> ROAD_WITH_YELLOW_THICK_LINE;
                      default -> ROAD_WITH_YELLOW_LINE;
                    } :
                    switch (type) {
                      case DOUBLE -> ROAD_WITH_WHITE_DOUBLE_LINE;
                      case THICK -> ROAD_WITH_WHITE_THICK_LINE;
                      default -> ROAD_WITH_WHITE_LINE;
                    }
            )
                .getDefaultState()
                .with(Properties.HORIZONTAL_AXIS, direction.getAxis());
          } else {
            // 直角或斜角的情况。

            // 若不是斜角，则还需要考虑更复杂的情况：
            // - 一侧白色粗线，一侧黄色双线 → 白色粗线和黄色双线
            // - 一侧白线，一侧黄色双线 → 白色和黄色双线
            // - 一侧白色粗线，一侧黄线 → 白色粗线和黄线
            // - 一侧白线，一侧黄线 → 白色和黄线
            // - 其他情况：仅有白线
            if (type == RoadAutoLineType.RIGHT_ANGLE) {
              if (connectionState.lineColor == LineColor.YELLOW && adjacentState.lineColor == LineColor.WHITE) {
                // 若当前侧为黄，另一侧为白，则直接跳到后续循环。
                continue;
              } else if (connectionState.lineColor == LineColor.WHITE && adjacentState.lineColor == LineColor.YELLOW) {
                // 若当前侧为白，另一侧为黄，则执行如下操作：
                return (connectionState.lineType == LineType.THICK && adjacentState.lineType == LineType.DOUBLE
                    ? ROAD_WITH_WT_YD_RA_LINE
                    : adjacentState.lineType == LineType.DOUBLE
                    ? ROAD_WITH_W_YD_RA_LINE
                    : connectionState.lineType == LineType.THICK
                    ? ROAD_WITH_WT_Y_RA_LINE
                    : ROAD_WITH_W_Y_RA_LINE
                ).getDefaultState()
                    .with(RoadWithDiffAngleLine.AXIS, adjacentDirection.getAxis())
                    .with(RoadWithAngleLine.FACING, HorizontalCornerDirection.fromDirections(direction, adjacentDirection));
              } else if (connectionState.lineColor == LineColor.WHITE && adjacentState.lineColor == LineColor.WHITE) {
                // 两侧均为白色，则需要考虑普通线与白色线混合的情况。
                if (connectionState.lineType == LineType.THICK && adjacentState.lineType == LineType.NORMAL) {
                  return ROAD_WITH_WT_N_RA_LINE
                      .getDefaultState()
                      .with(RoadWithAngleLine.FACING, HorizontalCornerDirection.fromDirections(direction, adjacentDirection))
                      .with(RoadWithDiffAngleLine.AXIS, adjacentDirection.getAxis());
                } else if (connectionState.lineType == LineType.NORMAL && adjacentState.lineType == LineType.THICK) {
                  return ROAD_WITH_WT_N_RA_LINE
                      .getDefaultState()
                      .with(RoadWithAngleLine.FACING, HorizontalCornerDirection.fromDirections(direction, adjacentDirection))
                      .with(RoadWithDiffAngleLine.AXIS, direction.getAxis());
                } else if (connectionState.lineType == LineType.NORMAL && adjacentState.lineType == LineType.NORMAL) {
                  return ROAD_WITH_WHITE_RA_LINE.getDefaultState()
                      .with(RoadWithAngleLine.FACING, HorizontalCornerDirection.fromDirections(direction, adjacentDirection));
                }
              }
            }

            if (connectionState.lineColor == adjacentState.lineColor) {
              return (switch (connectionState.lineColor) {
                case YELLOW -> switch (type) {
                  case BEVEL -> ROAD_WITH_YELLOW_BA_LINE;
                  case RIGHT_ANGLE -> ROAD_WITH_YELLOW_RA_LINE;
                };
                case WHITE, NONE, UNKNOWN -> switch (type) {
                  case BEVEL -> ROAD_WITH_WHITE_BA_LINE;
                  case RIGHT_ANGLE -> ROAD_WITH_WHITE_RA_LINE;
                };
              }).getDefaultState().with(RoadWithAngleLine.FACING, HorizontalCornerDirection.fromDirections(direction, adjacentDirection));
            }

            // 如果还是不能决定，则直接返回白色标线。
            return (switch (type) {
              case BEVEL -> ROAD_WITH_WHITE_BA_LINE;
              case RIGHT_ANGLE -> ROAD_WITH_WHITE_RA_LINE;
            }).getDefaultState().with(RoadWithAngleLine.FACING, HorizontalCornerDirection.fromDirections(direction, adjacentDirection));
          }
        }
      case 1:
        // 只有一个方向连接的情况下，为直线。
        for (Map.Entry<Direction, RoadConnectionState> entry : connectionStateMap.entrySet()) {
          final RoadConnectionState connectionState = entry.getValue();
          if (connectionState.mayConnect()) {
            return (
                connectionState.lineColor == LineColor.YELLOW
                    ? (
                    switch (connectionState.lineType) {
                      case THICK -> ROAD_WITH_YELLOW_THICK_LINE;
                      case DOUBLE -> ROAD_WITH_YELLOW_DOUBLE_LINE;
                      default -> ROAD_WITH_YELLOW_LINE;
                    }
                ) : (
                    switch (connectionState.lineType) {
                      case THICK -> ROAD_WITH_WHITE_THICK_LINE;
                      case DOUBLE -> ROAD_WITH_WHITE_DOUBLE_LINE;
                      default -> ROAD_WITH_WHITE_LINE;
                    }
                )
            )
                .getDefaultState()
                .with(Properties.HORIZONTAL_AXIS, entry.getKey().getAxis());
          }
        }
        return defaultState;
      case 3:
        for (Map.Entry<Direction, RoadConnectionState> entry : connectionStateMap.entrySet()) {
          if (!entry.getValue().mayConnect())
          // 检测需要连接正向的方块对应的连接是否为斜线。
          {
            // 朝向的方向，即唯一没有被连接的方向的反方向。
            Direction direction = entry.getKey().getOpposite();
            // 朝向的那个方向的道路连接状态。
            RoadConnectionState state = connectionStateMap.get(direction);
            if (state.direction == null
                || state.direction.left().isPresent()
                || type != RoadAutoLineType.BEVEL)
            // 连接直线
            {
              RoadConnectionState stateLeft = connectionStateMap.get(direction.rotateYCounterclockwise());
              RoadConnectionState stateRight = connectionStateMap.get(direction.rotateYClockwise());

              // 若旁边两侧道路连接的颜色和类型相同，则考虑比较复杂的情况：
              if (stateLeft.lineColor == stateRight.lineColor && stateLeft.lineType == stateRight.lineType) {
                // 优先考虑混色部分
                // - 两端白色粗线，前方黄色双线 → 白色粗+黄色双
                // - 两端白线，前方黄色双线 → 白色+黄色双
                // - 两端白色，前方黄色 → 白色+黄色
                // - 两端黄色，前方白色 → 黄色+白色

                if (stateLeft.lineColor != state.lineColor) {
                  return (
                      stateLeft.lineColor == LineColor.WHITE && state.lineColor == LineColor.YELLOW
                          ? (
                          stateLeft.lineType == LineType.THICK && state.lineType == LineType.DOUBLE
                              ? ROAD_WITH_WT_TS_YD_LINE
                              : state.lineType == LineType.DOUBLE
                              ? ROAD_WITH_W_TS_YD_LINE : ROAD_WITH_W_TS_Y_LINE
                      )
                          : stateLeft.lineColor == LineColor.YELLOW && state.lineColor == LineColor.WHITE
                          ? ROAD_WITH_Y_TS_W_LINE : ROAD_WITH_WHITE_TS_LINE
                  ).getDefaultState().with(RoadWithJointLine.FACING, direction);
                }

                // 然后考虑同色
                // - 均为黄色 → 黄色
                // - 两端白色，前方白色粗线 → 白色+白色粗
                // - 两端白色，前方白色双线 → 白色+白色双
                // - 两端白色双线 → 白色双+白色
                // - 两端白色粗线 → 白色粗+白色
                // - 其他情况 → 白色普通

                else {
                  return (state.lineColor == LineColor.YELLOW
                      ? ROAD_WITH_YELLOW_TS_LINE
                      : switch (state.lineType) {
                    case DOUBLE -> ROAD_WITH_WHITE_TS_DOUBLE_LINE;
                    case THICK -> ROAD_WITH_WHITE_TS_THICK_LINE;
                    default -> switch (stateLeft.lineType) {
                      case DOUBLE -> ROAD_WITH_WHITE_DOUBLE_TS_LINE;
                      case THICK -> ROAD_WITH_WHITE_THICK_TS_LINE;
                      default -> ROAD_WITH_WHITE_TS_LINE;
                    };
                  }
                  ).getDefaultState().with(RoadWithJointLine.FACING, direction);
                }
              } else {
                // 存在左右两侧标线不等的情况。
                return (switch (state.lineColor) {
                  case YELLOW -> ROAD_WITH_YELLOW_TS_LINE;
                  case WHITE, UNKNOWN, NONE -> ROAD_WITH_WHITE_TS_LINE;
                })
                    .getDefaultState()
                    .with(Properties.HORIZONTAL_FACING, direction);
              }
            } else if (state.direction.right().isPresent()) {
              // 连接斜线
              return ROAD_WITH_WHITE_S_BA_LINE
                  .getDefaultState()
                  .with(Properties.HORIZONTAL_AXIS, direction.rotateYClockwise().getAxis())
                  .with(
                      MishangucProperties.HORIZONTAL_CORNER_FACING,
                      state.direction.right().get().getOpposite().mirror(direction));
            }
          }
        }
        return defaultState;
      default:
        throw new IllegalStateException("Illegal connected number: " + connected);
    }
  }

  @Environment(EnvType.CLIENT)
  @Override
  public @NotNull JBlockStates getBlockStates() {
    return JBlockStates.simple(getBlockModelId());
  }
}
