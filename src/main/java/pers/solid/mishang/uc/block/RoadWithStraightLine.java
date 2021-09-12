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

public interface RoadWithStraightLine extends Road {
    EnumProperty<Direction.Axis> AXIS = Properties.HORIZONTAL_AXIS;

    default void appendRoadProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(AXIS);
    }

    @Override
    default RoadConnectionState getConnectionStateOf(BlockState state, Direction direction) {
        Direction.Axis axis = state.get(AXIS);
        return direction.getAxis() == axis ? RoadConnectionState.CONNECTED_TO : RoadConnectionState.NOT_CONNECTED_TO;
    }

    default BlockState rotateRoad(BlockState state, BlockRotation rotation) {
        Direction.Axis axis = state.get(AXIS);
        Direction.Axis rotatedAxis;
        switch (rotation) {
            case CLOCKWISE_90:
            case COUNTERCLOCKWISE_90:
                rotatedAxis = axis == Direction.Axis.X ? Direction.Axis.Z : axis == Direction.Axis.Z ? Direction.Axis.X : axis;
                break;
            default:
                rotatedAxis = axis;
        }
        return state.with(AXIS, rotatedAxis);
    }

    default BlockState mirrorRoad(BlockState state, BlockMirror mirror) {
        return state;
    }

    default BlockState withPlacementState(BlockState state, ItemPlacementContext ctx) {
        return state.with(AXIS, ctx.getPlayerFacing().getAxis());
    }
}
