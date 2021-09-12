package pers.solid.mishang.uc.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.SlabBlock;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.state.StateManager;
import net.minecraft.util.BlockMirror;
import net.minecraft.util.BlockRotation;
import net.minecraft.util.math.BlockPos;
import org.jetbrains.annotations.Nullable;

public class AbstractRoadSlabBlock extends SlabBlock implements Road {
    public AbstractRoadSlabBlock(Settings settings) {
        super(settings);
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
}
