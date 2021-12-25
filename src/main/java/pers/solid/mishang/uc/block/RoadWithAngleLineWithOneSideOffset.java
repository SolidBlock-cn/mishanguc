package pers.solid.mishang.uc.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.DirectionProperty;
import net.minecraft.util.BlockMirror;
import net.minecraft.util.BlockRotation;
import net.minecraft.util.math.Direction;
import pers.solid.mishang.uc.LineColor;

public interface RoadWithAngleLineWithOneSideOffset extends RoadWithAngleLine {
  /**
   * 该道路方块的偏移的方向。
   *
   * @see net.minecraft.state.property.Properties#HORIZONTAL_FACING
   */
  DirectionProperty OFFSET_DIRECTION =
      DirectionProperty.of("offset_direction", Direction.Type.HORIZONTAL);

  @Override
  default void appendRoadProperties(StateManager.Builder<Block, BlockState> builder) {
    RoadWithAngleLine.super.appendRoadProperties(builder);
    builder.add(OFFSET_DIRECTION);
  }

  @Override
  default BlockState mirrorRoad(BlockState state, BlockMirror mirror) {
    return RoadWithAngleLine.super
        .mirrorRoad(state, mirror)
        .with(OFFSET_DIRECTION, mirror.apply(state.get(OFFSET_DIRECTION)));
  }

  @Override
  default BlockState rotateRoad(BlockState state, BlockRotation rotation) {
    return RoadWithAngleLine.super
        .rotateRoad(state, rotation)
        .with(OFFSET_DIRECTION, rotation.rotate(state.get(OFFSET_DIRECTION)));
  }

  @Override
  default BlockState withPlacementState(BlockState state, ItemPlacementContext ctx) {
    return RoadWithAngleLine.super
        .withPlacementState(state, ctx)
        .with(OFFSET_DIRECTION, ctx.getPlayerFacing());
  }

  class Impl extends RoadWithAngleLine.Impl implements RoadWithAngleLineWithOneSideOffset {
    public Impl(Settings settings, LineColor lineColor, boolean isBevel) {
      super(settings, lineColor, isBevel);
    }
  }

  class SlabImpl extends RoadWithAngleLine.SlabImpl implements RoadWithAngleLineWithOneSideOffset {
    public SlabImpl(Settings settings, LineColor lineColor, boolean isBevel) {
      super(settings, lineColor, isBevel);
    }
  }
}
