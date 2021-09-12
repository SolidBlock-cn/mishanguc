package pers.solid.mishang.uc.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.DirectionProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.util.BlockMirror;
import net.minecraft.util.BlockRotation;
import net.minecraft.util.math.Direction;

public interface RoadWithJointLine extends Road {
    DirectionProperty FACING = Properties.HORIZONTAL_FACING;

    @Override
    default void appendRoadProperties(StateManager.Builder<Block, BlockState> builder) {
        Road.super.appendRoadProperties(builder);
        builder.add(FACING);
    }

    @Override
    default RoadConnectionState getConnectionStateOf(BlockState state, Direction direction) {
        return RoadConnectionState.or(Road.super.getConnectionStateOf(state, direction), (state.get(FACING) == direction.getOpposite()) ? RoadConnectionState.NOT_CONNECTED_TO : RoadConnectionState.CONNECTED_TO);
    }

    @Override
    default BlockState mirrorRoad(BlockState state, BlockMirror mirror) {
        return Road.super.mirrorRoad(state, mirror).with(FACING, mirror.apply(state.get(FACING)));
    }

    @Override
    default BlockState rotateRoad(BlockState state, BlockRotation rotation) {
        return Road.super.rotateRoad(state, rotation).with(FACING, rotation.rotate(state.get(FACING)));
    }

    @Override
    default BlockState withPlacementState(BlockState state, ItemPlacementContext ctx) {
        return Road.super.withPlacementState(state, ctx).with(FACING, Direction.fromRotation(ctx.getPlayerYaw()));
    }
}
