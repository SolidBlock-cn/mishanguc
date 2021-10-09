package pers.solid.mishang.uc.block;

import com.mojang.datafixers.util.Either;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemStack;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.EnumProperty;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.BlockMirror;
import net.minecraft.util.BlockRotation;
import net.minecraft.util.math.Direction;
import net.minecraft.world.BlockView;
import org.jetbrains.annotations.Nullable;
import pers.solid.mishang.uc.ModProperties;

import java.util.List;

public interface RoadWithAngleLine extends Road {
    EnumProperty<HorizontalCornerDirection> FACING = ModProperties.HORIZONTAL_CORNER_FACING;

    default void appendRoadProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(FACING);
    }

    @Override
    default RoadConnectionState getConnectionStateOf(BlockState state, Direction direction) {
        return RoadConnectionState.of(state.get(FACING).hasDirection(direction), getLineColor(), isBevel() ? Either.right(state.get(FACING).mirror(direction)) : Either.left(direction));
    }

    default BlockState mirrorRoad(BlockState state, BlockMirror mirror) {
        return state.with(FACING, state.get(FACING).mirror(mirror));
    }

    default BlockState rotateRoad(BlockState state, BlockRotation rotation) {
        HorizontalCornerDirection facing = state.get(FACING);
        return state.with(FACING, facing.rotate(rotation));
    }

    default BlockState withPlacementState(BlockState state, ItemPlacementContext ctx) {
        if (state == null) return null;
        final HorizontalCornerDirection rotation = HorizontalCornerDirection.fromRotation(ctx.getPlayerYaw());
        return state.with(FACING, ctx.getPlayer() != null && ctx.getPlayer().isSneaking() ? rotation.getOpposite() : rotation);
    }

    @Override
    default void appendRoadTooltip(ItemStack stack, @Nullable BlockView world, List<Text> tooltip, TooltipContext options) {
        Road.super.appendRoadTooltip(stack, world, tooltip, options);
        tooltip.add(new TranslatableText("block.mishanguc.tooltip.road_with_angle_line.1").setStyle(GRAY_STYLE));
        tooltip.add(new TranslatableText("block.mishanguc.tooltip.road_with_angle_line.2").setStyle(GRAY_STYLE));
    }

    boolean isBevel();
}
