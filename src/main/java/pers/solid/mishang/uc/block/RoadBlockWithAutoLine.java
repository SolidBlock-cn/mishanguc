package pers.solid.mishang.uc.block;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.data.client.BlockStateModelGenerator;
import net.minecraft.data.client.BlockStateSupplier;
import net.minecraft.item.Item.TooltipContext;
import net.minecraft.state.property.Properties;
import net.minecraft.text.Text;
import net.minecraft.util.math.Direction;
import org.apache.commons.lang3.ObjectUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import pers.solid.brrp.v1.api.RuntimeResourcePack;
import pers.solid.brrp.v1.model.ModelJsonBuilder;
import pers.solid.mishang.uc.arrp.BRRPHelper;
import pers.solid.mishang.uc.arrp.FasterJTextures;
import pers.solid.mishang.uc.util.*;

import java.util.EnumMap;
import java.util.List;
import java.util.Map;

import static pers.solid.mishang.uc.blocks.RoadBlocks.*;

/**
 * 具有自动连接功能的道路方块。关于本模组的所有道路方块，请参见 {@link pers.solid.mishang.uc.blocks.RoadBlocks}。
 */
public class RoadBlockWithAutoLine extends AbstractRoadBlock implements RoadWithAutoLine {
  public static final MapCodec<RoadBlockWithAutoLine> CODEC = RecordCodecBuilder.mapCodec(i -> i.group(createSettingsCodec(), RoadAutoLineType.CODEC.fieldOf("type").forGetter(b -> b.type), Codec.STRING.fieldOf("texture").forGetter(b -> b.texture)).apply(i, RoadBlockWithAutoLine::new));
  /**
   * 该自动连接方块在一些特定情况下，应该产生直角还是斜线。
   */
  public final RoadAutoLineType type;
  private final String texture;

  public RoadBlockWithAutoLine(Settings settings, RoadAutoLineType type, String texture) {
    super(settings, LineColor.UNKNOWN, LineType.NORMAL);
    this.type = type;
    this.texture = texture;
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
        // 考虑使用双斜线搭配直线的情况。
        final boolean yellow = sumYellow >= 2;
        for (Direction direction : Direction.Type.HORIZONTAL) {
          final EightHorizontalDirection direction1 = connectionStateMap.get(direction.rotateYClockwise()).direction();
          final EightHorizontalDirection direction2 = connectionStateMap.get(direction.rotateYCounterclockwise()).direction();
          if (direction1.right().map(cornerDirection -> cornerDirection.hasDirection(direction)).orElse(false)
              && direction2.right().map(cornerDirection -> cornerDirection.hasDirection(direction)).orElse(false)) {
            // 使用双斜线搭配直线的情况
            return (yellow ? ROAD_WITH_YS_AND_BI_BA_LINE : ROAD_WITH_WS_AND_BI_BA_LINE).getDefaultState()
                .with(ROAD_WITH_WS_AND_BI_BA_LINE.FACING, direction);
          }
        }
        return (yellow ? ROAD_WITH_YELLOW_CROSS_LINE : ROAD_WITH_WHITE_CROSS_LINE).getDefaultState();
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
            } else if (adjacentState.lineColor() == LineColor.UNKNOWN) {
              color = connectionState.lineColor();
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

            if (connectionState.offsetLevel() != 0 && (adjacentState.offsetLevel() != 0 || !adjacentState.sureConnect())) {
              if ((connectionState.offsetDirection() == adjacentState.offsetDirection() && connectionState.offsetLevel() == adjacentState.offsetLevel() || !adjacentState.sureConnect())) {
                return composeOffsetStraightLine(connectionState.offsetDirection(), connectionState.offsetLevel(), color);
              }
            } else if (!connectionState.sureConnect() && adjacentState.offsetLevel() != 0) {
              // 考虑到有不确定的情况，跳过这种情况，在后续循环中完成。
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
              // 考虑带有偏移的直角的情况，注意这种情况下，双方的颜色应当一致。
              if (connectionState.lineColor() == adjacentState.lineColor() || adjacentState.lineColor() == LineColor.UNKNOWN) {
                // 双方均有偏移，且偏移方向均为向外或者向内。
                if (connectionState.offsetLevel() == 2
                    && (connectionState.offsetLevel() == adjacentState.offsetLevel() && (connectionState.offsetDirection() == adjacentDirection) == (adjacentState.offsetDirection() == direction) || !adjacentState.sureConnect())) {
                  return composeAngleLineWithTwoPartsOffset(connectionState.lineColor(), HorizontalCornerDirection.fromDirections(direction, adjacentDirection), connectionState.offsetDirection() == adjacentDirection, type);
                } else if (connectionState.offsetLevel() == 2 && adjacentState.offsetLevel() != 2) {
                  return composeAngleLineWithOnePartOffset(connectionState.lineColor(), HorizontalCornerDirection.fromDirections(direction, adjacentDirection), adjacentDirection.getAxis(), connectionState.offsetDirection() == adjacentDirection);
                } else if (connectionState.offsetLevel() != 2 && adjacentState.offsetLevel() == 2) {
                  // 跳到后续循环中，使得 connectionState.offsetLevel() == 2 成立，并运行上面的这一段语句。
                  continue;
                }
              }

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
              final LineType lineType = connectionState.sureConnect() && adjacentState.sureConnect() ? ObjectUtils.min(connectionState.lineType(), adjacentState.lineType()) : connectionState.sureConnect() ? connectionState.lineType() : adjacentState.lineType();

              // 先考虑有偏移的情况。
              // 偏移斜线要求双方是同为向外或者同为向内偏移。以下情况下，会使用偏移：
              // - 双方均为向外或者向内偏移。
              // - 一侧有偏移，另一个的线路是不确定的。
              if (connectionState.offsetLevel() == 2) {
                boolean isInwards = connectionState.offsetDirection() == adjacentDirection;
                if ((adjacentState.offsetLevel() == 2 &&
                    (adjacentState.offsetDirection() == direction) == isInwards
                    || !adjacentState.sureConnect())) {
                  return composeAngleLineWithTwoPartsOffset(connectionState.lineColor(), HorizontalCornerDirection.fromDirections(direction, adjacentDirection), isInwards, type);
                }
              }

              return (switch (connectionState.lineColor()) {
                case YELLOW -> switch (type) {
                  case BEVEL -> switch (lineType) {
                    case DOUBLE -> ROAD_WITH_YELLOW_BA_DOUBLE_LINE;
                    case THICK -> ROAD_WITH_YELLOW_BA_THICK_LINE;
                    default -> ROAD_WITH_YELLOW_BA_LINE;
                  };
                  case RIGHT_ANGLE -> ROAD_WITH_YELLOW_RA_LINE;
                };
                case WHITE, NONE, UNKNOWN -> switch (type) {
                  case BEVEL -> switch (lineType) {
                    case DOUBLE -> ROAD_WITH_WHITE_BA_DOUBLE_LINE;
                    case THICK -> ROAD_WITH_WHITE_BA_THICK_LINE;
                    default -> ROAD_WITH_WHITE_BA_LINE;
                  };
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
            if (connectionState.offsetLevel() != 0) {
              return composeOffsetStraightLine(connectionState.offsetDirection(), connectionState.offsetLevel(), connectionState.lineColor());
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

            // 考虑双斜线的情况。
            if (type == RoadAutoLineType.BEVEL
                && stateLeft.direction().right().map(cornerDirection -> cornerDirection.hasDirection(facingDirection)).orElse(false)
                && stateRight.direction().right().map(cornerDirection -> cornerDirection.hasDirection(facingDirection)).orElse(false)) {
              if (facingState.lineColor() == LineColor.YELLOW && (stateLeft.lineColor() == LineColor.YELLOW || stateRight.lineColor() == LineColor.YELLOW)) {
                return ROAD_WITH_YELLOW_BI_BA_LINE.getDefaultState().with(RoadWithTwoBevelAngleLines.FACING, facingDirection);
              } else {
                return ROAD_WITH_WHITE_BI_BA_LINE.getDefaultState().with(RoadWithTwoBevelAngleLines.FACING, facingDirection);
              }
            }

            // 若旁边两侧道路连接的颜色和类型相同，则考虑比较复杂的情况：
            final LineOffset facingOffset = facingState.lineOffset();
            if (stateLeft.lineColor() == stateRight.lineColor() && stateLeft.lineType() == stateRight.lineType()) {
              if (stateLeft.lineColor() != facingState.lineColor() && facingState.lineColor() != LineColor.UNKNOWN) {
                // 优先考虑混色部分，产生的均为T形线。
                // - 两端白色粗线，前方黄色双线 → 白色粗+黄色双
                // - 两端白线，前方黄色双线 → 白色+黄色双
                // - 两端白色，前方黄色 → 白色+黄色
                // - 两端黄色，前方白色 → 黄色+白色
                if (stateLeft.lineColor() == LineColor.WHITE && facingState.lineColor() == LineColor.YELLOW) {
                  final RoadWithJointLine.Impl block = switch (stateLeft.lineType()) {
                    case THICK -> (facingState.lineType() == LineType.DOUBLE ? ROAD_WITH_WT_TS_YD_LINE : ROAD_WITH_WT_TS_Y_LINE);
                    default -> (facingState.lineType() == LineType.DOUBLE ? ROAD_WITH_W_TS_YD_LINE : ROAD_WITH_W_TS_Y_LINE);
                  };
                  return composeJointLine(block, facingDirection, facingOffset);
                }
                if (stateLeft.lineColor() == LineColor.YELLOW && facingState.lineColor() == LineColor.WHITE) {
                  return composeJointLine(ROAD_WITH_Y_TS_W_LINE, facingDirection, facingOffset);
                } else {
                  return composeJointLine(ROAD_WITH_WHITE_TS_LINE, facingDirection, facingOffset);
                }
              } else {
                // 然后考虑同色
                // - 均为黄色 → 黄色
                // - 两端白色，前方白色粗线 → 白色+白色粗
                // - 两端白色，前方白色双线 → 白色+白色双
                // - 两端白色双线 → 白色双+白色
                // - 两端白色粗线 → 白色粗+白色
                // - 其他情况 → 白色普通

                if (facingState.lineColor() == LineColor.YELLOW || (facingState.lineColor() == LineColor.UNKNOWN && stateLeft.lineColor() == LineColor.YELLOW)) {
                  return composeJointLine(ROAD_WITH_YELLOW_TS_LINE, facingDirection, facingOffset);
                } else {
                  final RoadWithJointLine.Impl block = switch (facingState.lineType()) {
                    case DOUBLE -> ROAD_WITH_WHITE_TS_DOUBLE_LINE;
                    case THICK -> ROAD_WITH_WHITE_TS_THICK_LINE;
                    default -> switch (stateLeft.lineType()) {
                      case DOUBLE -> ROAD_WITH_WHITE_DOUBLE_TS_LINE;
                      case THICK -> ROAD_WITH_WHITE_THICK_TS_LINE;
                      default -> ROAD_WITH_WHITE_TS_LINE;
                    };
                  };
                  return composeJointLine(block, facingDirection, facingOffset);
                }
              }
            } else {
              // 存在左右两侧标线不等的情况。
              final RoadWithJointLine.Impl block = switch (facingState.lineColor()) {
                case YELLOW -> ROAD_WITH_YELLOW_TS_LINE;
                case WHITE, UNKNOWN, NONE -> ROAD_WITH_WHITE_TS_LINE;
              };
              return composeJointLine(block, facingDirection, facingOffset);
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

  /**
   * 返回一个 T 形线的方块状态，并且如果存在对应的偏移，则转化为相应的带有偏移的线路。
   *
   * @param block           不带偏移的 T 字形线的道路方块。
   * @param facingDirection T 字形道路朝向的方向。
   * @param facingOffset    朝向方向的道路连接状态中存在的偏移情况。
   * @return 带有偏移或者不带偏移的 T 字形线的方块状态。
   */
  private BlockState composeJointLine(RoadWithJointLine.Impl block, Direction facingDirection, @Nullable LineOffset facingOffset) {
    if (facingOffset != null && facingOffset.level() == 2 && RoadWithJointLineWithOffsetSide.Impl.OFFSET_ROADS.containsKey(block)) {
      final RoadWithJointLineWithOffsetSide.Impl offsetSide = RoadWithJointLineWithOffsetSide.Impl.OFFSET_ROADS.get(block);
      return offsetSide.getDefaultState()
          .with(RoadWithJointLineWithOffsetSide.FACING, HorizontalCornerDirection.fromDirections(facingDirection, facingOffset.offsetDirection()))
          .with(RoadWithJointLineWithOffsetSide.AXIS, facingDirection.rotateYClockwise().getAxis());
    } else {
      return block.getDefaultState().with(RoadWithJointLine.FACING, facingDirection);
    }
  }

  private BlockState composeAngleLineWithOnePartOffset(LineColor lineColor, HorizontalCornerDirection facing, Direction.Axis axis, boolean isInwards) {
    final RoadWithAngleLineWithOnePartOffset.Impl block;
    block = switch (lineColor) {
      case YELLOW -> (isInwards ? ROAD_WITH_YELLOW_RA_LINE_OFFSET_IN : ROAD_WITH_YELLOW_RA_LINE_OFFSET_OUT);
      default -> (isInwards ? ROAD_WITH_WHITE_RA_LINE_OFFSET_IN : ROAD_WITH_WHITE_RA_LINE_OFFSET_OUT);
    };
    return block.getDefaultState().with(RoadWithAngleLineWithOnePartOffset.AXIS, axis).with(RoadWithAngleLine.FACING, facing);
  }

  private BlockState composeAngleLineWithTwoPartsOffset(LineColor lineColor, HorizontalCornerDirection facing, boolean isInwards, RoadAutoLineType type) {
    final RoadWithAngleLineWithTwoPartsOffset.Impl block;
    if (type == RoadAutoLineType.RIGHT_ANGLE) {
      block = switch (lineColor) {
        case YELLOW -> (isInwards ? ROAD_WITH_YELLOW_OFFSET_IN_RA_LINE : ROAD_WITH_YELLOW_OFFSET_OUT_RA_LINE);
        default -> (isInwards ? ROAD_WITH_WHITE_OFFSET_IN_RA_LINE : ROAD_WITH_WHITE_OFFSET_OUT_RA_LINE);
      };
    } else {
      block = switch (lineColor) {
        case YELLOW -> (isInwards ? ROAD_WITH_YELLOW_OFFSET_IN_BA_LINE : ROAD_WITH_YELLOW_OFFSET_OUT_BA_LINE);
        default -> (isInwards ? ROAD_WITH_WHITE_OFFSET_IN_BA_LINE : ROAD_WITH_WHITE_OFFSET_OUT_BA_LINE);
      };
    }
    return block.getDefaultState().with(RoadWithAngleLine.FACING, facing);
  }

  private static BlockState composeOffsetStraightLine(Direction offsetDirection, int offsetLevel, LineColor color) {
    return switch (offsetLevel) {
      case 114514 -> ROAD_WITH_WHITE_YELLOW_DOUBLE_LINE.getDefaultState().with(Properties.HORIZONTAL_FACING, offsetDirection.getOpposite());
      case 2 -> {
        final Block block = switch (color) {
          case YELLOW -> ROAD_WITH_YELLOW_OFFSET_LINE;
          default -> ROAD_WITH_WHITE_OFFSET_LINE;
        };
        yield block.getDefaultState().with(Properties.HORIZONTAL_FACING, offsetDirection.getOpposite());
      }
      case 1 -> {
        final Block block = switch (color) {
          case YELLOW -> ROAD_WITH_YELLOW_HALF_DOUBLE_LINE;
          default -> ROAD_WITH_WHITE_HALF_DOUBLE_LINE;
        };
        yield block.getDefaultState().with(Properties.HORIZONTAL_FACING, offsetDirection.getOpposite());
      }
      default -> {
        final Block block = switch (color) {
          case YELLOW -> ROAD_WITH_YELLOW_LINE;
          default -> ROAD_WITH_WHITE_LINE;
        };
        yield block.getDefaultState().with(Properties.AXIS, offsetDirection.rotateYClockwise().getAxis());
      }
    };
  }

  @Environment(EnvType.CLIENT)
  @Override
  public @NotNull BlockStateSupplier getBlockStates() {
    return BlockStateModelGenerator.createSingletonBlockState(this, getBlockModelId());
  }

  @Environment(EnvType.CLIENT)
  @Override
  public @NotNull ModelJsonBuilder getBlockModel() {
    return ModelJsonBuilder.create("mishanguc", "block/road_with_auto_line")
        .setTextures(new FasterJTextures()
            .base("asphalt")
            .line(texture)
            .particle("asphalt"));
  }

  @Environment(EnvType.CLIENT)
  @Override
  public void writeBlockModel(RuntimeResourcePack pack) {
    BRRPHelper.addModelWithSlab(pack, this);
  }

  @Override
  public void appendDescriptionTooltip(List<Text> tooltip, TooltipContext options) {
  }

  @Override
  protected MapCodec<? extends RoadBlockWithAutoLine> getCodec() {
    return CODEC;
  }
}
