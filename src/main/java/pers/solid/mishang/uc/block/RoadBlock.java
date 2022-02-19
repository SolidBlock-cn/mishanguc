package pers.solid.mishang.uc.block;

import net.minecraft.block.BlockState;
import net.minecraft.util.math.Direction;
import pers.solid.mishang.uc.util.LineColor;
import pers.solid.mishang.uc.util.LineType;
import pers.solid.mishang.uc.util.RoadConnectionState;

public class RoadBlock extends AbstractRoadBlock {
  public RoadBlock(Settings settings) {
    super(settings, LineColor.NONE, LineType.NORMAL);
  }

  @Override
  public RoadConnectionState getConnectionStateOf(BlockState state, Direction direction) {
    return RoadConnectionState.empty();
  }
}
