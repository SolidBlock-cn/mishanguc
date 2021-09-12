package pers.solid.mishang.uc.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.EnumProperty;
import net.minecraft.util.BlockMirror;
import net.minecraft.util.BlockRotation;
import net.minecraft.util.math.Direction;
import pers.solid.mishang.uc.ModProperties;

public interface RoadWithAngleLine extends Road {
    EnumProperty<HorizontalCornerDirection> FACING = ModProperties.HORIZONTAL_CORNER_FACING;

    default void appendRoadProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(FACING);
    }

    @Override
    default RoadConnectionState getConnectionStateOf(BlockState state, Direction direction) {
        return state.get(FACING).hasDirection(direction) ? RoadConnectionState.CONNECTED_TO : RoadConnectionState.NOT_CONNECTED_TO;
    }

    default BlockState mirrorRoad(BlockState state, BlockMirror mirror) {
        return state.with(FACING,state.get(FACING).mirror(mirror));
    }

    default BlockState rotateRoad(BlockState state, BlockRotation rotation) {
        HorizontalCornerDirection facing = state.get(FACING);
        return state.with(FACING, facing.rotate(rotation));
    }

    default BlockState withPlacementState(BlockState state, ItemPlacementContext ctx) {
        if (state==null) return null;
        return state.with(FACING, HorizontalCornerDirection.fromRotation(ctx.getPlayerYaw()));
    }
}
