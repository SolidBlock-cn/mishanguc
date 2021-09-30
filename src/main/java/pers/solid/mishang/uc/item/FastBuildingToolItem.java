package pers.solid.mishang.uc.item;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.sound.SoundCategory;
import net.minecraft.util.ActionResult;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;

public class FastBuildingToolItem extends Item {
    public static final int range = 3;

    public FastBuildingToolItem(Settings settings) {
        super(settings);
    }

    @Override
    public ActionResult useOnBlock(ItemUsageContext context) {
        final Direction side = context.getSide();
        final BlockPos centerBlockPos = context.getBlockPos();
        final Direction.Axis axis = side.getAxis();
        final PlayerEntity player = context.getPlayer();
        final World world = context.getWorld();
        if (!world.isClient() && player!=null) {
            final BlockState centerState = world.getBlockState(centerBlockPos);
            final NbtCompound tag = context.getStack().getOrCreateTag();
            final int distance = tag.contains("distance",99) ? tag.getInt("distance") : 3;
            Block block = centerState.getBlock();
            BlockSoundGroup blockSoundGroup = centerState.getSoundGroup();
            world.playSound(context.getPlayer(), centerBlockPos.offset(side), centerState.getSoundGroup().getPlaceSound(), SoundCategory.BLOCKS, (blockSoundGroup.getVolume() + 1.0F) / 2.0F, blockSoundGroup.getPitch() * 0.8F);
            for (BlockPos pos : BlockPos.iterateOutwards(centerBlockPos,
                    axis == Direction.Axis.X ? 0 : distance,
                    axis == Direction.Axis.Y ? 0 : distance,
                    axis == Direction.Axis.Z ? 0 : distance)) {
                BlockState state = world.getBlockState(pos);
                if (state.getBlock() == block) {
                    final BlockPos newPos = pos.offset(side);
                    if (world.getBlockState(newPos).canReplace(new ItemPlacementContext(context))) {
                        world.setBlockState(newPos, state);
                    }
                }
            }
        }
        return ActionResult.success(world.isClient);
    }

    @Override
    public boolean canMine(BlockState centerState, World world, BlockPos centerPos, PlayerEntity miner) {
        if (!world.isClient()) {
            Block block = centerState.getBlock();
            final BlockHitResult hit = ((BlockHitResult) miner.raycast(20, 0, false));
            final Direction side = hit.getSide();
            final BlockPos centerBlockPos = hit.getBlockPos();
//            if (!Objects.equals(centerBlockPos, centerPos)) {
//                miner.sendMessage(new LiteralText(String.format("视线追踪错误：\npostMine参数坐标：%s\nraycast坐标：%s", centerPos, centerBlockPos)), false);
//            }
            final Direction.Axis axis = side.getAxis();
            final NbtCompound tag = miner.getMainHandStack().getOrCreateTag();
            final int distance = tag.contains("distance", 99) ? tag.getInt("distance") : 3;
            for (BlockPos pos : BlockPos.iterateOutwards(centerBlockPos,
                    axis == Direction.Axis.X ? 0 : distance,
                    axis == Direction.Axis.Y ? 0 : distance,
                    axis == Direction.Axis.Z ? 0 : distance)) {
                BlockState state = world.getBlockState(pos);
                if (state.getBlock() == block) {
                    world.removeBlock(pos,false);
                }
            }
        }
        return true;
    }

    @Override
    public boolean postProcessNbt(NbtCompound nbt) {
        nbt.putInt("distance",3);
        return false;
    }
}
