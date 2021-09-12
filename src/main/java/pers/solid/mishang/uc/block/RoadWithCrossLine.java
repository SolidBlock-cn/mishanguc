package pers.solid.mishang.uc.block;

import net.minecraft.block.BlockState;
import net.minecraft.util.math.Direction;

public interface RoadWithCrossLine extends Road{
    @Override
    default RoadConnectionState getConnectionStateOf(BlockState state, Direction direction) {
        return Road.super.getConnectionStateOf(state, direction).or(RoadConnectionState.CONNECTED_TO);
    }
}
