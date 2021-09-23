package pers.solid.mishang.uc.block;

import pers.solid.mishang.uc.LineColor;

public class RoadBlockWithStraightAndAngleLine extends AbstractRoadBlock implements RoadWithStraightAndAngleLine{
    public RoadBlockWithStraightAndAngleLine(Settings settings, LineColor lineColor) {
        super(settings,lineColor);
    }

    @Override
    public boolean isBevel() {
        return true;
    }
}
