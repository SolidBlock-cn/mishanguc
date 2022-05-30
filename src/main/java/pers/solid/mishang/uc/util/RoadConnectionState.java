package pers.solid.mishang.uc.util;

import com.mojang.datafixers.util.Either;
import net.minecraft.text.MutableText;
import net.minecraft.text.Style;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Formatting;
import net.minecraft.util.StringIdentifiable;
import net.minecraft.util.math.Direction;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

public class RoadConnectionState {
  public final @Nullable Either<Direction, HorizontalCornerDirection> direction;
  public final WhetherConnected whetherConnected;
  public final LineColor lineColor;
  public final LineType lineType;

  public RoadConnectionState(
      WhetherConnected whetherConnected,
      LineColor lineColor,
      @Nullable Either<Direction, HorizontalCornerDirection> direction,
      LineType lineType) {
    this.whetherConnected = whetherConnected;
    this.lineColor = lineColor;
    this.direction = direction;
    this.lineType = lineType;
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
    } else if (direction.left().isPresent()) {
      return text(direction.left().get());
    } else if (direction.right().isPresent()) {
      return text(direction.right().get());
    } else {
      throw new IllegalStateException();
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
    return new TranslatableText("roadConnectionState.lineColor." + lineColor.asString()).setStyle(Style.EMPTY.withColor(formatting));
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

  @Override
  public int hashCode() {
    int result = direction != null ? direction.hashCode() : 0;
    result = 31 * result + (whetherConnected != null ? whetherConnected.hashCode() : 0);
    result = 31 * result + (lineColor != null ? lineColor.hashCode() : 0);
    result = 31 * result + (lineType != null ? lineType.hashCode() : 0);
    return result;
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
