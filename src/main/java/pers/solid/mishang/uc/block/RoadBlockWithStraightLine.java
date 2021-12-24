package pers.solid.mishang.uc.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.state.StateManager;
import pers.solid.mishang.uc.LineColor;

public class RoadBlockWithStraightLine extends AbstractRoadBlock implements RoadWithStraightLine {
  public RoadBlockWithStraightLine(Settings settings, LineColor lineColor) {
    super(settings, lineColor);
  }

  @Override
  public void appendRoadProperties(StateManager.Builder<Block, BlockState> builder) {
    super.appendRoadProperties(builder);
    RoadWithStraightLine.super.appendRoadProperties(builder);
  }
}
