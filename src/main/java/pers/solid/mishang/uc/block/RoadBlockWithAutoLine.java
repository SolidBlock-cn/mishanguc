package pers.solid.mishang.uc.block;

import net.devtech.arrp.json.blockstate.JBlockStates;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.state.property.Properties;
import net.minecraft.util.math.Direction;
import org.jetbrains.annotations.NotNull;
import pers.solid.mishang.uc.MishangucProperties;
import pers.solid.mishang.uc.util.HorizontalCornerDirection;
import pers.solid.mishang.uc.util.LineColor;
import pers.solid.mishang.uc.util.LineType;
import pers.solid.mishang.uc.util.RoadConnectionState;

import java.util.EnumMap;
import java.util.Map;

import static pers.solid.mishang.uc.blocks.RoadBlocks.*;

public class RoadBlockWithAutoLine extends AbstractRoadBlock implements RoadWithAutoLine {
  public final RoadAutoLineType type;
  private final LineColor lineColor;

  public RoadBlockWithAutoLine(
      Settings settings, RoadAutoLineType type, LineColor lineColor) {
    super(settings, lineColor, LineType.NORMAL);
    this.type = type;
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
    switch (connected) {
      case 0:
        // 全都不连接的情况。
        return ROAD_BLOCK.getDefaultState();
      case 4:
        // 全都连接的情况。
        if (lineColor == LineColor.WHITE) {
          return ROAD_WITH_WHITE_CROSS_LINE.getDefaultState();
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
            return ROAD_WITH_WHITE_LINE
                .getDefaultState()
                .with(Properties.HORIZONTAL_AXIS, direction.getAxis());
          } else {
            // 直角或斜角的情况。
            Direction direction2 = direction.rotateYClockwise();
            Block block;
            if (lineColor == LineColor.WHITE) {
              switch (type) {
                case BEVEL:
                  block = ROAD_WITH_WHITE_BA_LINE;
                  break;
                case RIGHT_ANGLE:
                  block = ROAD_WITH_WHITE_RA_LINE;
                  break;
                default:
                  throw new IllegalStateException("Unknown angle type: " + type);
              }
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
            return ROAD_WITH_WHITE_LINE
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
              return ROAD_WITH_WHITE_TS_LINE
                  .getDefaultState()
                  .with(Properties.HORIZONTAL_FACING, direction);
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
