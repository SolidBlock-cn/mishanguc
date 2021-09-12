package pers.solid.mishang.uc.block;

public enum RoadConnectionState {
    NOT_CONNECTED_TO,
    MAY_CONNECT_TO,
    PROBABLY_CONNECT_TO,
    CONNECTED_TO;

    public static RoadConnectionState or(RoadConnectionState state1, RoadConnectionState state2) {
        return state1.compareTo(state2) > 0 ? state1 : state2;
    }

    public static RoadConnectionState of(boolean bool) {
        return bool ? CONNECTED_TO : NOT_CONNECTED_TO;
    }

    public RoadConnectionState or(RoadConnectionState state) {
        return or(this, state);
    }
}
