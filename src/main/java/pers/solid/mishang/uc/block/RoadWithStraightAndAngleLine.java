package pers.solid.mishang.uc.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.state.StateManager;
import net.minecraft.util.BlockMirror;
import net.minecraft.util.BlockRotation;
import net.minecraft.util.math.Direction;

public interface RoadWithStraightAndAngleLine extends RoadWithAngleLine, RoadWithStraightLine {
    @Override
    default void appendRoadProperties(StateManager.Builder<Block, BlockState> builder) {
        RoadWithAngleLine.super.appendRoadProperties(builder);
        RoadWithStraightLine.super.appendRoadProperties(builder);
    }

    @Override
    default RoadConnectionState getConnectionStateOf(BlockState state, Direction direction) {
        return RoadConnectionState.or(RoadWithAngleLine.super.getConnectionStateOf(state, direction), RoadWithStraightLine.super.getConnectionStateOf(state, direction));
    }

    @Override
    default BlockState mirrorRoad(BlockState state, BlockMirror mirror) {
        return RoadWithStraightLine.super.mirrorRoad(RoadWithAngleLine.super.mirrorRoad(state, mirror), mirror);
    }


    @Override
    default BlockState rotateRoad(BlockState state, BlockRotation rotation) {
        return RoadWithStraightLine.super.rotateRoad(RoadWithAngleLine.super.rotateRoad(state, rotation), rotation);
    }

    @Override
    default BlockState withPlacementState(BlockState state, ItemPlacementContext ctx) {
        return RoadWithStraightLine.super.withPlacementState(RoadWithAngleLine.super.withPlacementState(state, ctx),ctx);
    }
}
