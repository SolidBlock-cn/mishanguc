package pers.solid.mishang.uc.item;

import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.util.ActionResult;
import net.minecraft.util.BlockMirror;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;

public class MirroringToolItem extends Item {
    public MirroringToolItem(Settings settings) {
        super(settings);
    }

    @Override
    public ActionResult useOnBlock(ItemUsageContext context) {
        final BlockPos blockPos = context.getBlockPos();
        final World world = context.getWorld();
        final BlockState blockState = world.getBlockState(blockPos);
        final Direction side = context.getSide();
        final Direction.Axis axis = side.getAxis();
        final BlockMirror mirror;
        switch (axis) {
            case X:
                mirror = BlockMirror.FRONT_BACK;
                break;
            default:
                mirror = BlockMirror.NONE;
                break;
            case Z:
                mirror = BlockMirror.LEFT_RIGHT;
                break;
        }
        world.setBlockState(blockPos, blockState.mirror(mirror));
        return ActionResult.SUCCESS;
    }

    @Override
    public boolean canMine(BlockState state, World world, BlockPos pos, PlayerEntity miner) {
        final BlockHitResult raycast = (BlockHitResult) miner.raycast(20, 0, false);
        final Direction side = raycast.getSide();
        final Direction.Axis axis = side.getAxis();
        final BlockMirror mirror;
        switch (axis) {
            case X:
                mirror = BlockMirror.FRONT_BACK;
                break;
            default:
                mirror = BlockMirror.NONE;
                break;
            case Z:
                mirror = BlockMirror.LEFT_RIGHT;
                break;
        }
        world.setBlockState(pos, state.mirror(mirror));
        return false;
    }
}
