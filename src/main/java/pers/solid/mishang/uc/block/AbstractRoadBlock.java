package pers.solid.mishang.uc.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.state.StateManager;
import net.minecraft.util.BlockMirror;
import net.minecraft.util.BlockRotation;
import org.jetbrains.annotations.Nullable;

public abstract class AbstractRoadBlock extends Block implements Road {
    public AbstractRoadBlock(Settings settings) {
        super(settings);
    }

    @Override
    public void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        super.appendProperties(builder);
        appendRoadProperties(builder);
    }

    @Nullable
    @Override
    public BlockState getPlacementState(ItemPlacementContext ctx) {
        return withPlacementState(super.getPlacementState(ctx),ctx);
    }

    @Override
    public BlockState mirror(BlockState state, BlockMirror mirror) {
        return mirrorRoad(super.mirror(state, mirror),mirror);
    }

    @Override
    public BlockState rotate(BlockState state, BlockRotation rotation) {
        return rotateRoad(super.rotate(state, rotation),rotation);
    }
}
