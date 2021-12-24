package pers.solid.mishang.uc.block;

import pers.solid.mishang.uc.LineColor;

/**
 * @see RoadBlockWithAngleLine
 */
public class RoadSlabBlockWithAngleLine extends AbstractRoadSlabBlock implements RoadWithAngleLine {
  private final boolean isBevel;

  public RoadSlabBlockWithAngleLine(Settings settings, LineColor lineColor, boolean isBevel) {
    super(settings, lineColor);
    this.isBevel = isBevel;
  }

  @Override
  public boolean isBevel() {
    return isBevel;
  }
}
