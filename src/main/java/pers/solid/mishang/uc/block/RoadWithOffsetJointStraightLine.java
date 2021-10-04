package pers.solid.mishang.uc.block;

import com.mojang.datafixers.util.Either;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.EnumProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.state.property.Property;
import net.minecraft.util.BlockMirror;
import net.minecraft.util.BlockRotation;
import net.minecraft.util.math.Direction;
import pers.solid.mishang.uc.LineColor;
import pers.solid.mishang.uc.ModProperties;

/**
 * 类似于 {@link RoadWithJointLine}，不过较短的那一条线是被偏移的。
 */
public interface RoadWithOffsetJointStraightLine extends RoadWithJointLine, RoadWithOffsetStraightLine {
    /**
     * 道路方块中，偏移半线与正中直线围成的面积范围较小的那个直角。
     */
    EnumProperty<HorizontalCornerDirection> FACING = ModProperties.HORIZONTAL_CORNER_FACING;
    /**
     * 道路方块中，正中直线所在的轴。
     */
    Property<Direction.Axis> AXIS = Properties.HORIZONTAL_AXIS;

    @Override
    LineColor getLineColor();

    @Override
    default void appendRoadProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(FACING,AXIS);
    }

    @Override
    default RoadConnectionState getConnectionStateOf(BlockState state, Direction direction) {
        return RoadConnectionState.of(state.get(FACING).hasDirection(direction) || state.get(AXIS).test(direction),getLineColor(), Either.left(direction.getOpposite()));
    }

    @Override
    default BlockState mirrorRoad(BlockState state, BlockMirror mirror) {
        return RoadWithOffsetStraightLine.super.mirrorRoad(RoadWithJointLine.super.mirrorRoad(state, mirror),mirror);
    }

    @Override
    default BlockState rotateRoad(BlockState state, BlockRotation rotation) {
        return RoadWithJointLine.super.rotateRoad(state, rotation);
    }

    @Override
    default BlockState withPlacementState(BlockState state, ItemPlacementContext ctx) {
        return RoadWithJointLine.super.withPlacementState(state, ctx);
    }
}
