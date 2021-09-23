package pers.solid.mishang.uc.block;

import com.mojang.datafixers.util.Either;
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
        return RoadConnectionState.of(state.get(FACING).hasDirection(direction),getLineColor(),isBevel() ? Either.right(state.get(FACING).mirror(direction)) : Either.left(direction));
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

    boolean isBevel();
}
