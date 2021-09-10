package pers.solid.mishang.uc.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.util.math.Direction;

public class RoadBlock extends AbstractRoadBlock {
    public RoadBlock(Settings settings) {
        super(settings);
    }

    @Override
    public RoadConnectionState getConnectionStateOf(BlockState state, Direction direction) {
        return RoadConnectionState.NOT_CONNECTED_TO;
    }
}
