package pers.solid.mishang.uc.block;

import com.mojang.datafixers.util.Either;
import net.minecraft.block.BlockState;
import net.minecraft.util.math.Direction;
import pers.solid.mishang.uc.LineColor;

public interface RoadWithCrossLine extends Road {
  @Override
  default RoadConnectionState getConnectionStateOf(BlockState state, Direction direction) {
    return Road.super
        .getConnectionStateOf(state, direction)
        .or(RoadConnectionState.connectedTo(getLineColor(), Either.left(direction)));
  }

  class SlabImpl extends RoadSlabBlock implements RoadWithCrossLine {
    public SlabImpl(Settings settings, LineColor lineColor) {
      super(settings, lineColor);
    }
  }

  class Impl extends AbstractRoadBlock implements RoadWithCrossLine {
    public Impl(Settings settings, LineColor lineColor) {
      super(settings, lineColor);
    }
  }
}
