package pers.solid.mishang.uc.block;

import pers.solid.mishang.uc.LineColor;

public class RoadBlockWithCentralAndOffsettedAngleLine extends AbstractRoadBlock implements RoadWithCentralAndOffsetAngleLine {
    private final boolean isBevel;

    public RoadBlockWithCentralAndOffsettedAngleLine(Settings settings, LineColor lineColor, boolean isBevel) {
        super(settings, lineColor);
        this.isBevel = isBevel;
    }

    @Override
    public boolean isBevel() {
        return isBevel;
    }
}
