package pers.solid.mishang.uc.block;

import com.mojang.datafixers.util.Either;
import net.minecraft.text.MutableText;
import net.minecraft.text.Style;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Formatting;
import net.minecraft.util.StringIdentifiable;
import net.minecraft.util.Util;
import net.minecraft.util.math.Direction;
import org.jetbrains.annotations.Nullable;
import pers.solid.mishang.uc.LineColor;

public class RoadConnectionState {
  public final @Nullable Either<Direction, HorizontalCornerDirection> direction;
  public final Probability probability;
  public final LineColor lineColor;

  public RoadConnectionState(Probability probability, LineColor lineColor, @Nullable Either<Direction, HorizontalCornerDirection> direction) {
    this.probability = probability;
    this.lineColor = lineColor;
    this.direction = direction;
  }

  public static RoadConnectionState empty() {
    return new RoadConnectionState(Probability.NOT_CONNECTED_TO, LineColor.NONE, null);
  }

  public static RoadConnectionState notConnectedTo(LineColor lineColor, Either<Direction, HorizontalCornerDirection> direction) {
    return new RoadConnectionState(Probability.NOT_CONNECTED_TO, lineColor, direction);
  }

  public static RoadConnectionState connectedTo(LineColor lineColor, Either<Direction, HorizontalCornerDirection> direction) {
    return new RoadConnectionState(Probability.CONNECTED_TO, lineColor, direction);
  }

  public static RoadConnectionState mayConnectTo(LineColor lineColor, Either<Direction, HorizontalCornerDirection> direction) {
    return new RoadConnectionState(Probability.MAY_CONNECT_TO, lineColor, direction);
  }

  public static RoadConnectionState of(boolean bool, LineColor lineColor, Either<Direction, HorizontalCornerDirection> direction) {
    return new RoadConnectionState(bool ? Probability.CONNECTED_TO : Probability.NOT_CONNECTED_TO, lineColor, direction);
  }

  public static RoadConnectionState or(RoadConnectionState state1, RoadConnectionState state2) {
    return state1.probability.compareTo(state2.probability) > 0 ? state1 : state2;
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

  public static MutableText text(Probability probability) {
    return new TranslatableText("roadConnectionState.probability." + probability.asString()).setStyle(Style.EMPTY.withColor(Util.make(() -> {
      switch (probability) {
        case NOT_CONNECTED_TO:
          return Formatting.RED;
        case CONNECTED_TO:
          return Formatting.GREEN;
        default:
          return null;
      }
    })));
  }

  public static MutableText text(LineColor lineColor) {
    return new TranslatableText("roadConnectionState.lineColor." + lineColor.asString()).setStyle(Style.EMPTY.withColor(Util.make(() -> {
      switch (lineColor) {
        case WHITE:
          return Formatting.WHITE;
        case YELLOW:
          return Formatting.YELLOW;
        default:
          return Formatting.GRAY;
      }
    })));
  }

  public boolean compareTo(Probability probability) {
    return this.probability.compareTo(probability) > 0;
  }

  public boolean compareTo(RoadConnectionState state) {
    return this.compareTo(state.probability);
  }

  public boolean mayConnect() {
    return this.probability.compareTo(Probability.MAY_CONNECT_TO) >= 0;
  }

  public RoadConnectionState or(RoadConnectionState state) {
    return or(this, state);
  }

  public enum Probability implements StringIdentifiable {
    NOT_CONNECTED_TO("not_connected_to"),
    MAY_CONNECT_TO("may_connect_to"),
    PROBABLY_CONNECT_TO("probably_connected_to"),
    CONNECTED_TO("connected_to");

    private final String id;

    Probability(String id) {
      this.id = id;
    }

    @Override
    public String asString() {
      return this.id;
    }
  }
}
