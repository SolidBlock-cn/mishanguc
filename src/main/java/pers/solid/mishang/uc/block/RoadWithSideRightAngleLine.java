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

public interface RoadWithSideRightAngleLine extends Road {
    EnumProperty<HorizontalCornerDirection> FACING = ModProperties.HORIZONTAL_CORNER_FACING;
    EnumProperty<Direction.Axis> AXIS = Properties.HORIZONTAL_AXIS;

    @Override
    default RoadConnectionState getConnectionStateOf(BlockState state, Direction direction) {
        return Road.super.getConnectionStateOf(state, direction);
    }

    @Override
    default void appendRoadProperties(StateManager.Builder<Block, BlockState> builder) {
        Road.super.appendRoadProperties(builder);
        builder.add(FACING, AXIS);
    }

    @Override
    default BlockState mirrorRoad(BlockState state, BlockMirror mirror) {
        return Road.super.mirrorRoad(state, mirror).with(FACING, state.get(FACING).mirror(mirror));
    }

    @Override
    default BlockState rotateRoad(BlockState state, BlockRotation rotation) {
        Direction.Axis axis = state.get(AXIS);
        return Road.super.rotateRoad(state, rotation)
                .with(FACING, state.get(FACING).rotate(rotation))
                .with(AXIS, rotation == BlockRotation.CLOCKWISE_90 || rotation == BlockRotation.COUNTERCLOCKWISE_90 ? axis == Direction.Axis.X ? Direction.Axis.Z : axis == Direction.Axis.Z ? Direction.Axis.X : axis : axis);
    }

    @Override
    default BlockState withPlacementState(BlockState state, ItemPlacementContext ctx) {
        return Road.super.withPlacementState(state, ctx)
                .with(FACING,HorizontalCornerDirection.fromRotation(ctx.getPlayerYaw()))
                .with(AXIS,ctx.getPlayerFacing().getAxis());
    }
}
