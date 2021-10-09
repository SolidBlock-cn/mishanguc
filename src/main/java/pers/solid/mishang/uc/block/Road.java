package pers.solid.mishang.uc.block;

import com.mojang.datafixers.util.Either;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemStack;
import net.minecraft.state.StateManager;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.util.*;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import pers.solid.mishang.uc.LineColor;

import java.util.List;

/**
 * 所有道路方块的接口。接口可以多重继承，并直接实现与已有类上，因此使用接口。
 */
public interface Road {
    Style GRAY_STYLE = Style.EMPTY.withColor(Formatting.GRAY);

    default RoadConnectionState getConnectionStateOf(BlockState state, Direction direction) {
        return RoadConnectionState.notConnectedTo(getLineColor(), Either.left(direction));
    }

    /**
     * 实现此接口的类，应当覆盖 <code>appendProperties</code> 并使用此方法。
     *
     * @param builder <code>appendProperties</code> 方法中的 builder。
     */
    default void appendRoadProperties(StateManager.Builder<Block, BlockState> builder) {
    }

    /**
     * 实现此接口的类，应当覆盖 <code>mirror</code> 并使用此方法。
     *
     * @param state  <code>mirror</code> 中的 state。
     * @param mirror <code>mirror</code> 中的 mirror。
     * @return 镜像后的方块状态。
     */
    default BlockState mirrorRoad(BlockState state, BlockMirror mirror) {
        return state;
    }

    /**
     * 实现此接口的类，应当覆盖 <code>rotate</code> 并使用此方法。
     *
     * @param state    <code>rotate</code> 中的 state。
     * @param rotation <code>rotate</code> 中的 rotation。
     * @return 旋转后的方块状态。
     */
    default BlockState rotateRoad(BlockState state, BlockRotation rotation) {
        return state;
    }

    /**
     * 追加放置状态。
     * 实现此接口的类，应当覆盖 <code>getPlacementState</code> 并使用此方法。
     *
     * @param state 需要被修改的方块状态，一般是 <code>super.getPlacementState(ctx)</code> 或者 <code>this.getDefaultState</code>（其中 this 是方块）。
     * @param ctx   <code>getPlacementState</code> 中的 ctx。
     * @return 追加后的方块状态。
     */
    default BlockState withPlacementState(BlockState state, ItemPlacementContext ctx) {
        return state;
    }

    default ActionResult onUseRoad(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
        return ActionResult.PASS;
    }

    default void neighborRoadUpdate(BlockState state, World world, BlockPos pos, Block block, BlockPos fromPos, boolean notify) {
    }

    default void appendRoadTooltip(ItemStack stack, @Nullable BlockView world, List<Text> tooltip, TooltipContext options) {
    }

    LineColor getLineColor();
}
