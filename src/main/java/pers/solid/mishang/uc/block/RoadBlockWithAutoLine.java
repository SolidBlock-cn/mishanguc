package pers.solid.mishang.uc.block;

import net.devtech.arrp.json.blockstate.JBlockStates;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.state.property.Properties;
import net.minecraft.text.Text;
import net.minecraft.util.math.Direction;
import org.apache.commons.lang3.ObjectUtils;
import org.jetbrains.annotations.NotNull;
import pers.solid.mishang.uc.util.HorizontalCornerDirection;
import pers.solid.mishang.uc.util.LineColor;
import pers.solid.mishang.uc.util.LineType;
import pers.solid.mishang.uc.util.RoadConnectionState;

import java.util.EnumMap;
import java.util.List;
import java.util.Map;

import static pers.solid.mishang.uc.blocks.RoadBlocks.*;

/**
 * 具有自动连接功能的道路方块。关于本模组的所有道路方块，请参见 {@link pers.solid.mishang.uc.blocks.RoadBlocks}。
 */
public class RoadBlockWithAutoLine extends AbstractRoadBlock implements RoadWithAutoLine {
  /**
   * 该自动连接方块在一些特定情况下，应该产生直角还是斜线。
   */
  public final RoadAutoLineType type;

  public RoadBlockWithAutoLine(Settings settings, RoadAutoLineType type) {
    super(settings, LineColor.UNKNOWN, LineType.NORMAL);
    this.type = type;
  }

  @SuppressWarnings("SwitchStatementWithTooFewBranches" /* 借助 switch 表达式，代码可以更清晰 */)
  @Override
  public @NotNull BlockState makeState(
      EnumMap<Direction, @NotNull RoadConnectionState> connectionStateMap, BlockState defaultState) {
    int connected = 0;
    for (Map.Entry<Direction, @NotNull RoadConnectionState> e : connectionStateMap.entrySet()) {
      @NotNull RoadConnectionState value = e.getValue();
      if (value.mayConnect()) {
        connected++;
      }
    }
    switch (connected) {
      case 0:
        // 全都不连接的情况。
        return ROAD_BLOCK.getDefaultState();
      case 4: {
        // 全都连接的情况。这种情况下，如果至少两侧的道路为黄色，则返回黄色十字形道路，否则返回白色十字形道路。
        final int sumYellow = connectionStateMap.values().stream().mapToInt(state -> state.lineColor() == LineColor.YELLOW ? 1 : 0).sum();
        return (sumYellow >= 2 ? ROAD_WITH_YELLOW_CROSS_LINE : ROAD_WITH_WHITE_CROSS_LINE).getDefaultState();
      }
      case 2:
        // 仅有两种方向的情况，这两个方向可能是相对的，也有可能是相邻的。
        // 这两个方向分别命名为 connectionState 和 adjacentState。
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
            // 道路的两个相对方向都连接了标线，因此应该连接直线。

            // 直线只能有一种颜色，因此取相邻两个道路中排位较靠后的颜色。
            final LineColor color;
            if (connectionState.lineColor() == adjacentState.lineColor()) {
              color = connectionState.lineColor();
            } else if (connectionState.lineColor() == LineColor.UNKNOWN) {
              color = adjacentState.lineColor();
            } else {
              color = ObjectUtils.max(connectionState.lineColor(), adjacentState.lineColor());
            }
            final LineType type;
            if (connectionState.lineType() == adjacentState.lineType()) {
              type = connectionState.lineType();
            } else if (connectionState.sureConnect() == adjacentState.sureConnect()) {
              type = ObjectUtils.max(connectionState.lineType(), adjacentState.lineType());
            } else {
              type = connectionState.sureConnect() ? connectionState.lineType() : adjacentState.lineType();
            }

            // 考虑两边道路都是偏移直线的情况，以及一边是偏移直线但另一边不确定的情况。
            if (connectionState.block() instanceof RoadWithOffsetStraightLine) {
              if (adjacentState.block() instanceof RoadWithOffsetStraightLine && connectionState.block() == adjacentState.block()) {
                // 两侧都是相同类型且相同方向的偏移直线，此时中间也补上这个偏移直线。
                if (connectionState.blockState().get(RoadWithOffsetStraightLine.FACING) == adjacentState.blockState().get(RoadWithOffsetStraightLine.FACING)) {
                  return connectionState.block().getDefaultState().with(RoadWithOffsetStraightLine.FACING, connectionState.blockState().get(RoadWithOffsetStraightLine.FACING));
                }
              } else if (!adjacentState.sureConnect()) {
                // 一侧是偏移直线，另一侧是不确定的，那么也直接使用这个偏移直线。
                return connectionState.block().getDefaultState().with(RoadWithOffsetStraightLine.FACING, connectionState.blockState().get(RoadWithOffsetStraightLine.FACING));
              }
              // 一侧偏移直线，另一侧不是相同偏移直线也不是不确定的线，那么直接忽略。
            } else if (!connectionState.sureConnect() && adjacentState.block() instanceof RoadWithOffsetStraightLine) {
              // 一侧是不确定的线，另一侧是偏移的直线，那么跳过这种情况，使得下一次循环中，connectionState 与 adjacentState 交换。
              continue;
            }

            return (switch (color) {
              case YELLOW -> switch (type) {
                case DOUBLE -> ROAD_WITH_YELLOW_DOUBLE_LINE;
                case THICK -> ROAD_WITH_YELLOW_THICK_LINE;
                default -> ROAD_WITH_YELLOW_LINE;
              };
              default -> switch (type) {
                case DOUBLE -> ROAD_WITH_WHITE_DOUBLE_LINE;
                case THICK -> ROAD_WITH_WHITE_THICK_LINE;
                default -> ROAD_WITH_WHITE_LINE;
              };
            })
                .getDefaultState()
                .with(Properties.HORIZONTAL_AXIS, direction.getAxis());
          } else {
            // 道路两个相邻的方向都连接了标线，所以连接直角或斜线。

            // 若不是斜角，则还需要考虑更复杂的情况：
            // - 一侧白色粗线，一侧黄色双线 → 白色粗线和黄色双线
            // - 一侧白线，一侧黄色双线 → 白色和黄色双线
            // - 一侧白色粗线，一侧黄线 → 白色粗线和黄线
            // - 一侧白线，一侧黄线 → 白色和黄线
            // - 其他情况：仅有白线
            if (type == RoadAutoLineType.RIGHT_ANGLE) {
              if (connectionState.lineColor() == LineColor.YELLOW && adjacentState.lineColor() == LineColor.WHITE) {
                // 若当前侧为黄，另一侧为白，则直接跳到后续循环。
                continue;
              } else if (connectionState.lineColor() == LineColor.WHITE && adjacentState.lineColor() == LineColor.YELLOW) {
                // 若当前侧为白，另一侧为黄，则执行如下操作：
                final Block block;
                if (connectionState.lineType() == LineType.THICK && adjacentState.lineType() == LineType.DOUBLE) {
                  block = ROAD_WITH_WT_YD_RA_LINE;
                } else if (adjacentState.lineType() == LineType.DOUBLE) {
                  block = ROAD_WITH_W_YD_RA_LINE;
                } else if (connectionState.lineType() == LineType.THICK) {
                  block = ROAD_WITH_WT_Y_RA_LINE;
                } else {
                  block = ROAD_WITH_W_Y_RA_LINE;
                }
                return block.getDefaultState()
                    .with(RoadWithDiffAngleLine.AXIS, adjacentDirection.getAxis())
                    .with(RoadWithAngleLine.FACING, HorizontalCornerDirection.fromDirections(direction, adjacentDirection));
              } else if (connectionState.lineColor() == LineColor.WHITE && adjacentState.lineColor() == LineColor.WHITE) {
                // 两侧均为白色，则需要考虑普通线与白色线混合的情况。
                if (connectionState.lineType() == LineType.THICK && adjacentState.lineType() == LineType.NORMAL) {
                  return ROAD_WITH_WT_N_RA_LINE
                      .getDefaultState()
                      .with(RoadWithAngleLine.FACING, HorizontalCornerDirection.fromDirections(direction, adjacentDirection))
                      .with(RoadWithDiffAngleLine.AXIS, adjacentDirection.getAxis());
                } else if (connectionState.lineType() == LineType.NORMAL && adjacentState.lineType() == LineType.THICK) {
                  return ROAD_WITH_WT_N_RA_LINE
                      .getDefaultState()
                      .with(RoadWithAngleLine.FACING, HorizontalCornerDirection.fromDirections(direction, adjacentDirection))
                      .with(RoadWithDiffAngleLine.AXIS, direction.getAxis());
                } else if (connectionState.lineType() == LineType.NORMAL && adjacentState.lineType() == LineType.NORMAL) {
                  return ROAD_WITH_WHITE_RA_LINE.getDefaultState()
                      .with(RoadWithAngleLine.FACING, HorizontalCornerDirection.fromDirections(direction, adjacentDirection));
                }
                // 两侧均为白线的情况结束。
              }
              // 直角的部分情况结束，但有些直角的情况仍会下降至后面的代码中。
            }

            if (connectionState.lineColor() == adjacentState.lineColor() || adjacentState.lineColor() == LineColor.UNKNOWN) {
              return (switch (connectionState.lineColor()) {
                case YELLOW -> switch (type) {
                  case BEVEL -> ROAD_WITH_YELLOW_BA_LINE;
                  case RIGHT_ANGLE -> ROAD_WITH_YELLOW_RA_LINE;
                };
                case WHITE, NONE, UNKNOWN -> switch (type) {
                  case BEVEL -> ROAD_WITH_WHITE_BA_LINE;
                  case RIGHT_ANGLE -> ROAD_WITH_WHITE_RA_LINE;
                };
              }).getDefaultState().with(RoadWithAngleLine.FACING, HorizontalCornerDirection.fromDirections(direction, adjacentDirection));
            } else if (connectionState.lineColor() == LineColor.UNKNOWN) {
              continue;
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
            // 如果这一边是偏移的直线，则产生这个偏移的直线。
            if (connectionState.block() instanceof RoadWithOffsetStraightLine) {
              return connectionState.block().getDefaultState().with(RoadWithOffsetStraightLine.FACING, connectionState.blockState().get(RoadWithOffsetStraightLine.FACING));
            }
            return (switch (connectionState.lineColor()) {
              case YELLOW -> switch (connectionState.lineType()) {
                case THICK -> ROAD_WITH_YELLOW_THICK_LINE;
                case DOUBLE -> ROAD_WITH_YELLOW_DOUBLE_LINE;
                default -> ROAD_WITH_YELLOW_LINE;
              };
              default -> switch (connectionState.lineType()) {
                case THICK -> ROAD_WITH_WHITE_THICK_LINE;
                case DOUBLE -> ROAD_WITH_WHITE_DOUBLE_LINE;
                default -> ROAD_WITH_WHITE_LINE;
              };
            })
                .getDefaultState()
                .with(Properties.HORIZONTAL_AXIS, entry.getKey().getAxis());
          }
        }
        return defaultState;
      case 3:
        for (Map.Entry<Direction, RoadConnectionState> entry : connectionStateMap.entrySet()) {
          final RoadConnectionState unconnectedState = entry.getValue();
          // 检测需要连接正向的方块对应的连接是否为斜线。
          if (unconnectedState.mayConnect()) continue;

          // 朝向的方向，即唯一没有被连接的方向的反方向。
          final Direction unconnectedDirection = entry.getKey();
          Direction facingDirection = unconnectedDirection.getOpposite();
          final RoadConnectionState facingState = connectionStateMap.get(facingDirection);
          // 朝向的那个方向的道路连接状态。
          if (facingState.direction() == null
              || facingState.direction().left().isPresent()
              || type != RoadAutoLineType.BEVEL) {
            // 朝向的那个方向是正对方向而非角落方向的情况，这种情况下通常应该连接T形线。
            RoadConnectionState stateLeft = connectionStateMap.get(facingDirection.rotateYCounterclockwise());
            RoadConnectionState stateRight = connectionStateMap.get(facingDirection.rotateYClockwise());

            // 若旁边两侧道路连接的颜色和类型相同，则考虑比较复杂的情况：
            if (stateLeft.lineColor() == stateRight.lineColor() && stateLeft.lineType() == stateRight.lineType()) {
              // 优先考虑混色部分，产生的均为T形线。
              // - 两端白色粗线，前方黄色双线 → 白色粗+黄色双
              // - 两端白线，前方黄色双线 → 白色+黄色双
              // - 两端白色，前方黄色 → 白色+黄色
              // - 两端黄色，前方白色 → 黄色+白色

              if (stateLeft.lineColor() != facingState.lineColor()) {
                if (stateLeft.lineColor() == LineColor.WHITE && facingState.lineColor() == LineColor.YELLOW) {
                  return (switch (stateLeft.lineType()) {
                    case THICK -> (facingState.lineType() == LineType.DOUBLE ? ROAD_WITH_WT_TS_YD_LINE : ROAD_WITH_WT_TS_Y_LINE);
                    default -> (facingState.lineType() == LineType.DOUBLE ? ROAD_WITH_W_TS_YD_LINE : ROAD_WITH_W_TS_Y_LINE);
                  }).getDefaultState().with(RoadWithJointLine.FACING, facingDirection);
                }
                if (stateLeft.lineColor() == LineColor.YELLOW && facingState.lineColor() == LineColor.WHITE) {
                  return ROAD_WITH_Y_TS_W_LINE.getDefaultState().with(RoadWithJointLine.FACING, facingDirection);
                } else {
                  return ROAD_WITH_WHITE_TS_LINE.getDefaultState().with(RoadWithJointLine.FACING, facingDirection);
                }
              } else {
                // 然后考虑同色
                // - 均为黄色 → 黄色
                // - 两端白色，前方白色粗线 → 白色+白色粗
                // - 两端白色，前方白色双线 → 白色+白色双
                // - 两端白色双线 → 白色双+白色
                // - 两端白色粗线 → 白色粗+白色
                // - 其他情况 → 白色普通

                if (unconnectedState.lineColor() == LineColor.YELLOW) {
                  return ROAD_WITH_YELLOW_TS_LINE.getDefaultState()
                      .with(RoadWithJointLine.FACING, facingDirection);
                } else {
                  return (switch (unconnectedState.lineType()) {
                    case DOUBLE -> ROAD_WITH_WHITE_TS_DOUBLE_LINE;
                    case THICK -> ROAD_WITH_WHITE_TS_THICK_LINE;
                    default -> switch (stateLeft.lineType()) {
                      case DOUBLE -> ROAD_WITH_WHITE_DOUBLE_TS_LINE;
                      case THICK -> ROAD_WITH_WHITE_THICK_TS_LINE;
                      default -> ROAD_WITH_WHITE_TS_LINE;
                    };
                  }).getDefaultState()
                      .with(RoadWithJointLine.FACING, facingDirection);
                }
              }
            } else {
              // 存在左右两侧标线不等的情况。
              return (switch (unconnectedState.lineColor()) {
                case YELLOW -> ROAD_WITH_YELLOW_TS_LINE;
                case WHITE, UNKNOWN, NONE -> ROAD_WITH_WHITE_TS_LINE;
              })
                  .getDefaultState()
                  .with(Properties.HORIZONTAL_FACING, facingDirection);
            }
            // T字形线部分结束
          } else if (facingState.direction().right().isPresent()) {
            // 考虑连接直斜混线

            // 前方黄色斜线，左右一侧白色粗，另一侧白色粗或黄色 → 白粗黄斜
            // 前方黄色斜线，左右均黄且一侧粗 → 黄粗+斜
            // 前方黄色斜线，左右均白或一白一黄 → 白直黄斜
            // 前方黄色斜线，左右均黄 → 黄直+斜
            // 前方白色斜线，左右一侧黄色粗，另一侧黄色粗或白色 → 黄粗白斜
            // 前方白色斜线，左右均白且一侧粗 → 白粗+斜
            // 前方白色斜线，左右均黄或一黄一白 → 黄直白斜
            // 其他情况 → 白直+斜

            final HorizontalCornerDirection facingStateDirection = facingState.direction().right().get();

            // 与 facingDirection 将有可能通过斜线连接的那个方向。
            // 例如，如果 facingDirection = north，facingState.direction = south_west，
            // 那么 bevelConnectingDirection = west
            final Direction bevelConDirection = facingStateDirection.getDirectionInAxis(facingDirection.rotateYClockwise().getAxis());
            final RoadConnectionState bevelConState = connectionStateMap.get(bevelConDirection);
            final Direction bevelNonDirection = bevelConDirection.getOpposite();
            final RoadConnectionState bevelNonState = connectionStateMap.get(bevelNonDirection);
            final Block block;
            if (facingState.lineColor() == LineColor.YELLOW) {
              // 前方是黄色斜线的情况。
              if (bevelNonState.lineColor() == LineColor.WHITE && bevelNonState.lineType() == LineType.THICK
                  && (bevelConState.lineColor() == LineColor.WHITE && bevelConState.lineType() == LineType.THICK
                  || bevelConState.lineColor() == LineColor.YELLOW)) {
                // 直线白粗，斜线黄色
                block = ROAD_WITH_WT_S_YN_BA_LINE;
              } else if (bevelNonState.lineColor() == LineColor.YELLOW && bevelNonState.lineType() == LineType.THICK && bevelConState.lineColor() == LineColor.YELLOW) {
                // 直线黄粗，斜线黄色
                block = ROAD_WITH_YT_S_WN_BA_LINE;
              } else if (bevelNonState.lineColor() == LineColor.WHITE) {
                // 直线白色，斜线黄色
                block = ROAD_WITH_W_S_Y_BA_LINE;
              } else {
                // 直线斜线均为黄色
                block = ROAD_WITH_YELLOW_S_BA_LINE;
              }
            } else {
              // 前方是其他颜色斜线的情况。
              if (bevelNonState.lineColor() == LineColor.YELLOW && bevelNonState.lineType() == LineType.THICK
                  && (bevelConState.lineColor() != LineColor.YELLOW || bevelConState.lineType() == LineType.THICK)) {
                // 直线黄粗，斜线白色
                block = ROAD_WITH_YT_S_WN_BA_LINE;
              } else if (bevelNonState.lineType() == LineType.THICK && bevelConState.lineColor() != LineColor.YELLOW) {
                // 直线白粗，斜线白色
                block = ROAD_WITH_WT_S_N_BA_LINE;
              } else if (bevelNonState.lineColor() == LineColor.YELLOW) {
                // 直线黄色，斜线白色
                block = ROAD_WITH_Y_S_W_BA_LINE;
              } else {
                // 其他情况均为白色。
                block = ROAD_WITH_WHITE_S_BA_LINE;
              }
            }
            return block
                .getDefaultState()
                .with(RoadWithStraightAndAngleLine.AXIS, facingDirection.rotateYClockwise().getAxis())
                .with(RoadWithStraightAndAngleLine.FACING, HorizontalCornerDirection.fromDirections(facingDirection, bevelConDirection));
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

  @Override
  public void appendDescriptionTooltip(List<Text> tooltip, TooltipContext options) {
  }
}
