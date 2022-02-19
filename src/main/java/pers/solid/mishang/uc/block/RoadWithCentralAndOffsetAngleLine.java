package pers.solid.mishang.uc.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.EnumProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.util.BlockMirror;
import net.minecraft.util.BlockRotation;
import net.minecraft.util.math.Direction;
import pers.solid.mishang.uc.ModProperties;
import pers.solid.mishang.uc.util.HorizontalCornerDirection;
import pers.solid.mishang.uc.util.LineColor;
import pers.solid.mishang.uc.util.LineType;
import pers.solid.mishang.uc.util.RoadConnectionState;

/** 带有一条正中直线和偏移直线的道路。类似于 {@link RoadWithAngleLine}，不过其中一条半线是侧的。 */
public interface RoadWithCentralAndOffsetAngleLine extends RoadWithAngleLine {
  /** 正中线和侧线围成的角的朝向。 */
  EnumProperty<HorizontalCornerDirection> FACING = ModProperties.HORIZONTAL_CORNER_FACING;
  /** 正中线所在轴。 */
  EnumProperty<Direction.Axis> AXIS = Properties.HORIZONTAL_AXIS;

  /** @see RoadWithAngleLine#getConnectionStateOf */
  @Override
  default RoadConnectionState getConnectionStateOf(BlockState state, Direction direction) {
    return RoadWithAngleLine.super.getConnectionStateOf(state, direction);
  }

  @Override
  default void appendRoadProperties(StateManager.Builder<Block, BlockState> builder) {
    RoadWithAngleLine.super.appendRoadProperties(builder);
    builder.add(AXIS);
  }

  @Override
  default BlockState mirrorRoad(BlockState state, BlockMirror mirror) {
    return RoadWithAngleLine.super.mirrorRoad(state, mirror);
  }

  @Override
  default BlockState rotateRoad(BlockState state, BlockRotation rotation) {
    Direction.Axis axis = state.get(AXIS);
    return RoadWithAngleLine.super
        .rotateRoad(state, rotation)
        .with(
            AXIS,
            rotation == BlockRotation.CLOCKWISE_90 || rotation == BlockRotation.COUNTERCLOCKWISE_90
                ? axis == Direction.Axis.X
                    ? Direction.Axis.Z
                    : axis == Direction.Axis.Z ? Direction.Axis.X : axis
                : axis);
  }

  @Override
  default BlockState withPlacementState(BlockState state, ItemPlacementContext ctx) {
    return RoadWithAngleLine.super
        .withPlacementState(state, ctx)
        .with(AXIS, ctx.getPlayerFacing().getAxis());
  }

  class RoadBlockWithCentralAndOffsettedAngleLine extends AbstractRoadBlock
      implements RoadWithCentralAndOffsetAngleLine {
    private final boolean isBevel;

    public RoadBlockWithCentralAndOffsettedAngleLine(
        Settings settings, LineColor lineColor, boolean isBevel) {
      super(settings, lineColor, LineType.NORMAL);
      this.isBevel = isBevel;
    }

    @Override
    public boolean isBevel() {
      return isBevel;
    }
  }
}
