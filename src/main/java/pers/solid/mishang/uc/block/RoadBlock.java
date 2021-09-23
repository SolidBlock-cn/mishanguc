package pers.solid.mishang.uc.block;

import net.minecraft.block.BlockState;
import net.minecraft.util.math.Direction;
import pers.solid.mishang.uc.LineColor;

public class RoadBlock extends AbstractRoadBlock {
    public RoadBlock(Settings settings) {
        super(settings, LineColor.NONE);
    }

    @Override
    public RoadConnectionState getConnectionStateOf(BlockState state, Direction direction) {
        return RoadConnectionState.empty();
    }

    @Override
    public LineColor getLineColor() {
        return LineColor.NONE;
    }
}
