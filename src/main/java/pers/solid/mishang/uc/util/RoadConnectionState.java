package pers.solid.mishang.uc.util;

import com.mojang.datafixers.util.Either;
import net.minecraft.text.MutableText;
import net.minecraft.util.Formatting;
import net.minecraft.util.StringIdentifiable;
import net.minecraft.util.math.Direction;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * 表示一个道路在一个方向上的连接状态。
 *
 * @param whetherConnected 该道路在该方向上是否已连接。
 * @param lineColor        道路连线的颜色。
 * @param direction        道路连线的方向。通常来说是正对着的，但偶尔也有可能是斜的方向。
 * @param lineType         道路连接线的类型，一般是普通线，也有可能是粗线或双线。
 * @param lineOffset       该道路连接状态的偏移。
 * @since 0.2.0-mc1.17+ 此类更改为记录；1.16.5 由于仍为 Java 8，因此仍使用普通类的形式。
 */
public record RoadConnectionState(WhetherConnected whetherConnected, LineColor lineColor, @Nullable EightHorizontalDirection direction, LineType lineType, @Nullable LineOffset lineOffset) implements Comparable<RoadConnectionState> {

  public RoadConnectionState(WhetherConnected whetherConnected, LineColor lineColor, @Nullable EightHorizontalDirection direction, LineType lineType) {
    this(whetherConnected, lineColor, direction, lineType, null);
  }

  @Contract(" -> new")
  public static RoadConnectionState empty() {
    return new RoadConnectionState(WhetherConnected.NOT_CONNECTED, LineColor.NONE, null, LineType.NORMAL, null);
  }

  public static RoadConnectionState of(boolean whetherConnected, LineColor lineColor, EightHorizontalDirection direction, LineType lineType, LineOffset lineOffset) {
    return new RoadConnectionState(whetherConnected ? WhetherConnected.CONNECTED : WhetherConnected.NOT_CONNECTED, lineColor, direction, lineType, lineOffset);
  }

  public static RoadConnectionState or(RoadConnectionState state1, RoadConnectionState state2) {
    return state1.compareTo(state2) > 0 ? state1 : state2;
  }

  public static MutableText text(@Nullable Direction direction) {
    if (direction == null) {
      return TextBridge.translatable("direction.none");
    }
    return TextBridge.translatable("direction." + direction.asString());
  }

  public static MutableText text(@Nullable HorizontalCornerDirection direction) {
    if (direction == null) {
      return TextBridge.translatable("direction.none");
    } else {
      return TextBridge.translatable("direction." + direction.asString());
    }
  }

  public static MutableText text(@Nullable Either<Direction, HorizontalCornerDirection> direction) {
    if (direction == null) {
      return TextBridge.translatable("direction.none");
    } else {
      return direction.map(RoadConnectionState::text, RoadConnectionState::text);
    }
  }

  public static MutableText text(@Nullable EightHorizontalDirection direction) {
    return direction == null ? text((Direction) null) : text(direction.either);
  }

  public static MutableText text(@NotNull WhetherConnected whetherConnected) {
    return TextBridge.translatable("roadConnectionState.whether." + whetherConnected.asString()).formatted(switch (whetherConnected) {
      case NOT_CONNECTED -> Formatting.RED;
      case CONNECTED -> Formatting.GREEN;
      default -> Formatting.YELLOW;
    });
  }

  public static MutableText text(@NotNull LineColor lineColor) {
    return lineColor.getName().formatted(switch (lineColor) {
      case WHITE -> Formatting.WHITE;
      case YELLOW -> Formatting.YELLOW;
      default -> Formatting.GRAY;
    });
  }

  public static MutableText text(@NotNull LineType lineType) {
    return lineType.getName();
  }

  @Contract(pure = true)
  public boolean mayConnect() {
    return this.whetherConnected.compareTo(WhetherConnected.MAY_CONNECT) >= 0;
  }

  @ApiStatus.AvailableSince("0.2.4")
  @Contract(pure = true)
  public boolean sureConnect() {
    return whetherConnected == WhetherConnected.CONNECTED;
  }

  public RoadConnectionState or(RoadConnectionState state) {
    return or(this, state);
  }

  @Override
  public int compareTo(@NotNull RoadConnectionState o) {
    return whetherConnected.compareTo(o.whetherConnected);
  }

  @Contract("_ -> new")
  public RoadConnectionState createWithOffset(LineOffset lineOffset) {
    return new RoadConnectionState(whetherConnected, lineColor, direction, lineType, lineOffset);
  }

  @Contract(pure = true)
  @ApiStatus.AvailableSince("1.1.0")
  public Direction offsetDirection() {
    return lineOffset == null ? null : lineOffset.offsetDirection();
  }

  @Contract(pure = true)
  @ApiStatus.AvailableSince("1.1.0")
  public int offsetLevel() {
    return lineOffset == null ? 0 : lineOffset.level();
  }

  public enum WhetherConnected implements StringIdentifiable {
    NOT_CONNECTED("not_connected"),
    MAY_CONNECT("may_connect"),
    CONNECTED("connected");

    private final String id;

    WhetherConnected(String id) {
      this.id = id;
    }

    @Override
    public String asString() {
      return this.id;
    }
  }
}
