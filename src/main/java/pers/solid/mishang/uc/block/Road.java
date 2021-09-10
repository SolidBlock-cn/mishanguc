package pers.solid.mishang.uc.block;

import net.minecraft.block.BlockState;
import net.minecraft.util.math.Direction;

public interface Road {
    RoadConnectionState getConnectionStateOf(BlockState state,Direction direction);
}
