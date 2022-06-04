package pers.solid.mishang.uc.util;

import com.mojang.datafixers.util.Either;
import net.minecraft.text.MutableText;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Formatting;
import net.minecraft.util.StringIdentifiable;
import net.minecraft.util.math.Direction;

import java.util.Objects;

/**
 * 表示一个道路在一个方向上的连接状态。
 *
 * @since 0.2.0-mc1.17+ 此类更改为记录；1.16.5 由于仍为 Java 8，因此仍使用普通类的形式。
 */
public final class RoadConnectionState {
  private final WhetherConnected whetherConnected;
  private final LineColor lineColor;
  private final Either<Direction, HorizontalCornerDirection> direction;
  private final LineType lineType;

  /**
   * @param whetherConnected 该道路在该方向上是否已连接。
   * @param lineColor        道路连线的颜色。
   * @param direction        道路连线的方向。通常来说是正对着的，但偶尔也有可能是斜的方向。
   * @param lineType         道路连接线的类型，一般是普通线，也有可能是粗线或双线。
   */
  RoadConnectionState(WhetherConnected whetherConnected, LineColor lineColor, Either<Direction, HorizontalCornerDirection> direction, LineType lineType) {
    this.whetherConnected = whetherConnected;
    this.lineColor = lineColor;
    this.direction = direction;
    this.lineType = lineType;
  }

  public WhetherConnected whetherConnected() {
    return whetherConnected;
  }

  public LineColor lineColor() {
    return lineColor;
  }

  public Either<Direction, HorizontalCornerDirection> direction() {
    return direction;
  }

  public LineType lineType() {
    return lineType;
  }

  @Override
  public int hashCode() {
    return Objects.hash(whetherConnected, lineColor);
  }

  @Override
  public String toString() {
    return "RoadConnectionState[" +
        "whetherConnected=" + whetherConnected + ", " +
        "lineColor=" + lineColor + ']';
  }

  public static RoadConnectionState empty() {
    return new RoadConnectionState(
        WhetherConnected.NOT_CONNECTED_TO, LineColor.NONE, null, LineType.NORMAL);
  }

  public static RoadConnectionState notConnectedTo(
      LineColor lineColor,
      Either<Direction, HorizontalCornerDirection> direction,
      LineType lineType) {
    return new RoadConnectionState(WhetherConnected.NOT_CONNECTED_TO, lineColor, direction, lineType);
  }

  public static RoadConnectionState connectedTo(
      LineColor lineColor,
      Either<Direction, HorizontalCornerDirection> direction,
      LineType lineType) {
    return new RoadConnectionState(WhetherConnected.CONNECTED_TO, lineColor, direction, lineType);
  }

  public static RoadConnectionState mayConnectTo(
      LineColor lineColor,
      Either<Direction, HorizontalCornerDirection> direction,
      LineType lineType) {
    return new RoadConnectionState(WhetherConnected.MAY_CONNECT_TO, lineColor, direction, lineType);
  }

  public static RoadConnectionState of(
      boolean bool,
      LineColor lineColor,
      Either<Direction, HorizontalCornerDirection> direction,
      LineType lineType) {
    return new RoadConnectionState(
        bool ? WhetherConnected.CONNECTED_TO : WhetherConnected.NOT_CONNECTED_TO,
        lineColor,
        direction,
        lineType);
  }

  public static RoadConnectionState or(RoadConnectionState state1, RoadConnectionState state2) {
    return state1.whetherConnected.compareTo(state2.whetherConnected) > 0 ? state1 : state2;
  }

  public static MutableText text(Direction direction) {
    return new TranslatableText("direction." + direction.asString());
  }

  public static MutableText text(HorizontalCornerDirection direction) {
    return new TranslatableText("direction." + direction.asString());
  }

  public static MutableText text(Either<Direction, HorizontalCornerDirection> direction) {
    if (direction == null) {
      return new TranslatableText("direction.none");
    } else {
      return direction.map(RoadConnectionState::text, RoadConnectionState::text);
    }
  }

  public static MutableText text(WhetherConnected whetherConnected) {
    Formatting formatting;
    switch (whetherConnected) {
      case NOT_CONNECTED_TO:
        formatting = Formatting.RED;
        break;
      case CONNECTED_TO:
        formatting = Formatting.GREEN;
        break;
      default:
        formatting = Formatting.YELLOW;
        break;
    }
    return new TranslatableText("roadConnectionState.whether." + whetherConnected.asString()).formatted(formatting);
  }

  public static MutableText text(LineColor lineColor) {
    Formatting formatting;
    switch (lineColor) {
      case WHITE:
        formatting = Formatting.WHITE;
        break;
      case YELLOW:
        formatting = Formatting.YELLOW;
        break;
      default:
        formatting = Formatting.GRAY;
        break;
    }
    return new TranslatableText("roadConnectionState.lineColor." + lineColor.asString()).formatted(formatting);
  }

  public static MutableText text(LineType lineType) {
    return new TranslatableText("roadConnectionState.lineType." + lineType.asString());
  }

  public boolean mayConnect() {
    return this.whetherConnected.compareTo(WhetherConnected.MAY_CONNECT_TO) >= 0;
  }

  public RoadConnectionState or(RoadConnectionState state) {
    return or(this, state);
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (!(o instanceof RoadConnectionState)) return false;

    RoadConnectionState that = (RoadConnectionState) o;

    if (!Objects.equals(direction, that.direction)) return false;
    if (whetherConnected != that.whetherConnected) return false;
    if (lineColor != that.lineColor) return false;
    return lineType == that.lineType;
  }

  public enum WhetherConnected implements StringIdentifiable {
    NOT_CONNECTED_TO("not_connected_to"),
    MAY_CONNECT_TO("may_connect_to"),
    PROBABLY_CONNECT_TO("probably_connected_to"),
    CONNECTED_TO("connected_to");

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
