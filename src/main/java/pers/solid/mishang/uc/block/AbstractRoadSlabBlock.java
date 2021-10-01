package pers.solid.mishang.uc.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.SlabBlock;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemStack;
import net.minecraft.state.StateManager;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.BlockMirror;
import net.minecraft.util.BlockRotation;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import pers.solid.mishang.uc.LineColor;

import java.util.List;

public abstract class AbstractRoadSlabBlock extends SlabBlock implements Road {
    public final LineColor lineColor;

    public AbstractRoadSlabBlock(Settings settings, LineColor lineColor) {
        super(settings);
        this.lineColor = lineColor;
    }

    @Override
    public void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        super.appendProperties(builder);
        appendRoadProperties(builder);
    }

    @Nullable
    @Override
    public final BlockState getPlacementState(ItemPlacementContext ctx) {
        BlockPos blockPos = ctx.getBlockPos();
        BlockState blockState = ctx.getWorld().getBlockState(blockPos);
        if (blockState.isOf(this)) {
            return super.getPlacementState(ctx);
        } else {
            return withPlacementState(super.getPlacementState(ctx),ctx);
        }
    }

    @Override
    public BlockState rotate(BlockState state, BlockRotation rotation) {
        return rotateRoad(super.rotate(state, rotation),rotation);
    }

    @Override
    public BlockState mirror(BlockState state, BlockMirror mirror) {
        return mirrorRoad(super.mirror(state,mirror),mirror);
    }

    @Override
    public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
        ActionResult result = super.onUse(state, world, pos, player, hand, hit);
        if (result==ActionResult.FAIL) return result;
        else return onUseRoad(state, world, pos, player, hand, hit);
    }

    @Override
    public void neighborUpdate(BlockState state, World world, BlockPos pos, Block block, BlockPos fromPos, boolean notify) {
        super.neighborUpdate(state, world, pos, block, fromPos, notify);
        neighborRoadUpdate(state, world, pos, block, fromPos, notify);
    }

    @Override
    public LineColor getLineColor() {
        return this.lineColor;
    }

    @Override
    public void appendTooltip(ItemStack stack, @Nullable BlockView world, List<Text> tooltip, TooltipContext options) {
        super.appendTooltip(stack, world, tooltip, options);
        appendRoadTooltip(stack, world, tooltip, options);
    }
}
