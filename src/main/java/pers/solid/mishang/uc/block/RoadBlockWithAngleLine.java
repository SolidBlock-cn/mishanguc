package pers.solid.mishang.uc.block;

import pers.solid.mishang.uc.LineColor;

public class RoadBlockWithAngleLine extends AbstractRoadBlock implements RoadWithAngleLine{
    private final boolean isBevel;

    public RoadBlockWithAngleLine(Settings settings, LineColor lineColor, boolean isBevel) {
        super(settings,lineColor);
        this.isBevel = isBevel;
    }

    @Override
    public boolean isBevel() {
        return isBevel;
    }
}
