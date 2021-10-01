package pers.solid.mishang.uc.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemStack;
import net.minecraft.state.StateManager;
import net.minecraft.text.Text;
import net.minecraft.util.BlockMirror;
import net.minecraft.util.BlockRotation;
import net.minecraft.util.math.Direction;
import net.minecraft.world.BlockView;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public interface RoadWithStraightAndAngleLine extends RoadWithAngleLine, RoadWithStraightLine {
    @Override
    default void appendRoadProperties(StateManager.Builder<Block, BlockState> builder) {
        RoadWithAngleLine.super.appendRoadProperties(builder);
        RoadWithStraightLine.super.appendRoadProperties(builder);
    }

    @Override
    default RoadConnectionState getConnectionStateOf(BlockState state, Direction direction) {
        return RoadConnectionState.or(RoadWithStraightLine.super.getConnectionStateOf(state, direction), RoadWithAngleLine.super.getConnectionStateOf(state, direction));
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

    @Override
    default void appendRoadTooltip(ItemStack stack, @Nullable BlockView world, List<Text> tooltip, TooltipContext options) {
        RoadWithAngleLine.super.appendRoadTooltip(stack, world, tooltip, options);
        RoadWithStraightLine.super.appendRoadTooltip(stack, world, tooltip, options);
    }
}
