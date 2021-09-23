package pers.solid.mishang.uc.block;

import com.mojang.datafixers.util.Either;
import net.minecraft.util.math.Direction;
import org.jetbrains.annotations.Nullable;
import pers.solid.mishang.uc.LineColor;

public class RoadConnectionState {
    private final Probability probability;
    private final LineColor lineColor;
    public final @Nullable Either<Direction, HorizontalCornerDirection> direction;

    public enum Probability {
        NOT_CONNECTED_TO,
        MAY_CONNECT_TO,
        PROBABLY_CONNECT_TO,
        CONNECTED_TO;
    }

    public RoadConnectionState(Probability probability, LineColor lineColor, @Nullable Either<Direction,HorizontalCornerDirection> direction) {
        this.probability = probability;
        this.lineColor = lineColor;
        this.direction = direction;
    }

    public boolean compareTo(Probability probability) {
        return this.probability.compareTo(probability) > 0;
    }

    public boolean compareTo(RoadConnectionState state) {
        return this.compareTo(state.probability);
    }

    public boolean mayConnect() {
        return this.probability.compareTo(Probability.MAY_CONNECT_TO)>=0;
    }

    public static RoadConnectionState empty() {
        return new RoadConnectionState(Probability.NOT_CONNECTED_TO,LineColor.NONE,null);
    }

    public static RoadConnectionState notConnectedTo(LineColor lineColor, Either<Direction, HorizontalCornerDirection> direction) {
        return new RoadConnectionState(Probability.NOT_CONNECTED_TO,lineColor,direction);
    }

    public static RoadConnectionState connectedTo(LineColor lineColor, Either<Direction, HorizontalCornerDirection> direction) {
        return new RoadConnectionState(Probability.CONNECTED_TO,lineColor,direction);
    }

    public static RoadConnectionState mayConnectTo(LineColor lineColor, Either<Direction, HorizontalCornerDirection> direction) {
        return new RoadConnectionState(Probability.MAY_CONNECT_TO,lineColor,direction);
    }

    public static RoadConnectionState of(boolean bool,LineColor lineColor, Either<Direction, HorizontalCornerDirection> direction) {
        return new RoadConnectionState(bool ? Probability.CONNECTED_TO : Probability.NOT_CONNECTED_TO,lineColor,direction);
    }

    public static RoadConnectionState or(RoadConnectionState state1, RoadConnectionState state2) {
        return state1.probability.compareTo(state2.probability) > 0 ? state1 : state2;
    }

    public RoadConnectionState or(RoadConnectionState state) {
        return or(this, state);
    }
}
