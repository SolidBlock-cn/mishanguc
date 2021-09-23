package pers.solid.mishang.uc.block;

import pers.solid.mishang.uc.LineColor;

/**
 * @see RoadBlockWithStraightAndAngleLine
 */
public class RoadSlabBlockWithStraightAndAngleLine extends AbstractRoadSlabBlock implements RoadWithStraightAndAngleLine {
    public RoadSlabBlockWithStraightAndAngleLine(Settings settings, LineColor lineColor) {
        super(settings,lineColor);
    }

    @Override
    public boolean isBevel() {
        return true;
    }
}
