package pers.solid.mishang.uc.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.EnumProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.util.BlockRotation;
import net.minecraft.util.math.Direction;
import pers.solid.mishang.uc.MishangUtils;
import pers.solid.mishang.uc.util.LineColor;
import pers.solid.mishang.uc.util.LineType;

/** 类似于 {@link RoadWithAngleLine}，但是直角两边可能不同。 */
public interface RoadWithDiffAngleLine extends RoadWithAngleLine {
  /** 直角上该坐标轴上的边视为第二个边，另一方向的边则视为第一个边。 */
  EnumProperty<Direction.Axis> AXIS = Properties.HORIZONTAL_AXIS;

  @Override
  default void appendRoadProperties(StateManager.Builder<Block, BlockState> builder) {
    RoadWithAngleLine.super.appendRoadProperties(builder);
    builder.add(AXIS);
  }

  @Override
  default BlockState rotateRoad(BlockState state, BlockRotation rotation) {
    return RoadWithAngleLine.super
        .rotateRoad(state, rotation)
        .with(AXIS, MishangUtils.rotateAxis(rotation, state.get(AXIS)));
  }

  @Override
  default BlockState withPlacementState(BlockState state, ItemPlacementContext ctx) {
    return RoadWithAngleLine.super
        .withPlacementState(state, ctx)
        .with(AXIS, ctx.getPlayerFacing().getAxis());
  }

  class Impl extends RoadWithAngleLine.Impl implements RoadWithDiffAngleLine {
    public final LineColor lineColor2;
    public final LineType lineType2;

    public Impl(
        Settings settings,
        LineColor lineColor,
        LineColor lineColor2,
        LineType lineType,
        LineType lineType2,
        boolean isBevel) {
      super(settings, lineColor, lineType, isBevel);
      this.lineColor2 = lineColor2;
      this.lineType2 = lineType2;
    }

    @Override
    public LineColor getLineColor(BlockState state, Direction direction) {
      return state.get(AXIS) == direction.getAxis() ? lineColor2 : lineColor;
    }

    @Override
    public LineType getLineType(BlockState state, Direction direction) {
      return state.get(AXIS) == direction.getAxis() ? lineType2 : lineType;
    }
  }

  class SlabImpl extends RoadWithAngleLine.SlabImpl implements RoadWithDiffAngleLine {
    public final LineColor lineColor2;
    public final LineType lineType2;

    public SlabImpl(
        Settings settings,
        LineColor lineColor,
        LineColor lineColor2,
        LineType lineType,
        LineType lineType2,
        boolean isBevel) {
      super(lineColor, settings, lineType, isBevel);
      this.lineColor2 = lineColor2;
      this.lineType2 = lineType2;
    }

    @Override
    public LineColor getLineColor(BlockState state, Direction direction) {
      return state.get(AXIS) == direction.getAxis() ? lineColor2 : lineColor;
    }

    @Override
    public LineType getLineType(BlockState state, Direction direction) {
      return state.get(AXIS) == direction.getAxis() ? lineType2 : lineType;
    }
  }
}
