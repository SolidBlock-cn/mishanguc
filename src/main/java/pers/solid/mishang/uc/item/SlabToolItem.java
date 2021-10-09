package pers.solid.mishang.uc.item;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.enums.SlabType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.state.property.Properties;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

/**
 * 用于处理台阶的工具。
 */
public class SlabToolItem extends Item {
    public SlabToolItem(Settings settings) {
        super(settings);
    }

    /**
     * 破坏台阶的一部分。
     *
     * @see Item#canMine
     */
    @Override
    public boolean canMine(BlockState state, World world, BlockPos pos, PlayerEntity miner) {
        Block block = state.getBlock();
        try {
            if (state.contains(Properties.SLAB_TYPE) && state.get(Properties.SLAB_TYPE) == SlabType.DOUBLE) {
                final BlockHitResult raycast = ((BlockHitResult) miner.raycast(20, 0, false));
                boolean bl = raycast.getPos().y - (double) raycast.getBlockPos().getY() > 0.5D;
                if (bl) {
                    // 破坏上半砖的情况。
                    world.setBlockState(pos, state.with(Properties.SLAB_TYPE, SlabType.BOTTOM));
                    block.onBreak(world, pos, state.with(Properties.SLAB_TYPE, SlabType.TOP), miner);
                    block.onBreak(world, pos, state.with(Properties.SLAB_TYPE, SlabType.TOP), miner);
                } else {
                    // 破坏下半砖的情况
                    world.setBlockState(pos, state.with(Properties.SLAB_TYPE, SlabType.TOP));
                    block.onBreak(world, pos, state.with(Properties.SLAB_TYPE, SlabType.BOTTOM), miner);
                    block.onBreak(world, pos, state.with(Properties.SLAB_TYPE, SlabType.BOTTOM), miner);
                }
                // 此处还需要模拟 ClientPlayerInteractionManager 和 ServerPlayerInteractionManager 中的情形。
                return false;
            }
        } catch (IllegalArgumentException | ClassCastException ignored) {
        }
        return super.canMine(state, world, pos, miner);
    }
}
