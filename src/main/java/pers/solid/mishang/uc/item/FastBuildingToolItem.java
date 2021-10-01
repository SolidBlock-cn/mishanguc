package pers.solid.mishang.uc.item;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.sound.SoundCategory;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Formatting;
import net.minecraft.util.Util;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import pers.solid.mishang.uc.util.BlockMatchingRule;

import java.util.List;

public class FastBuildingToolItem extends Item {
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
        if (!world.isClient() && player != null) {
            final BlockState centerState = world.getBlockState(centerBlockPos);
            BlockSoundGroup blockSoundGroup = centerState.getSoundGroup();
            world.playSound(context.getPlayer(), centerBlockPos.offset(side), centerState.getSoundGroup().getPlaceSound(), SoundCategory.BLOCKS, (blockSoundGroup.getVolume() + 1.0F) / 2.0F, blockSoundGroup.getPitch() * 0.8F);
            final ItemStack stack = context.getStack();
            final int range = this.getRange(stack);
            final BlockMatchingRule matchingRule = this.getMatchingRule(stack);
            for (BlockPos pos : BlockPos.iterateOutwards(centerBlockPos,
                    axis == Direction.Axis.X ? 0 : range,
                    axis == Direction.Axis.Y ? 0 : range,
                    axis == Direction.Axis.Z ? 0 : range)) {
                BlockState state = world.getBlockState(pos);
                if (matchingRule.match(centerState, state)) {
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
            final Direction.Axis axis = side.getAxis();
            final ItemStack stack = miner.getMainHandStack();
            final int range = this.getRange(stack);
            final BlockMatchingRule matchingRule = this.getMatchingRule(stack);
            for (BlockPos pos : BlockPos.iterateOutwards(centerBlockPos,
                    axis == Direction.Axis.X ? 0 : range,
                    axis == Direction.Axis.Y ? 0 : range,
                    axis == Direction.Axis.Z ? 0 : range)) {
                BlockState state = world.getBlockState(pos);
                if (matchingRule.match(centerState, state)) {
                    world.removeBlock(pos, false);
                }
            }
        }
        return true;
    }

    @Override
    public ItemStack getDefaultStack() {
        return Util.make(super.getDefaultStack(), stack -> {
            final NbtCompound tag = stack.getOrCreateTag();
            tag.putInt("Range", 5);
            tag.putString("MatchingRule", "mishanguc:same_block");
        });
    }

    public int getRange(ItemStack stack) {
        final NbtCompound tag = stack.getOrCreateTag();
        return tag.contains("Range", 99) ? tag.getInt("Range") : 3;
    }

    public @NotNull BlockMatchingRule getMatchingRule(ItemStack stack) {
        final NbtCompound tag = stack.getOrCreateTag();
        final BlockMatchingRule matchingRule = BlockMatchingRule.fromString(tag.getString("MatchingRule"));
        return matchingRule == null ? BlockMatchingRule.SAME_BLOCK : matchingRule;
    }

    @Override
    public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
        super.appendTooltip(stack, world, tooltip, context);
        tooltip.add(new TranslatableText("item.mishanguc.fast_building_tool.tooltip.range", this.getRange(stack)).setStyle(Style.EMPTY.withColor(Formatting.GRAY)));
        tooltip.add(new TranslatableText("item.mishanguc.fast_building_tool.tooltip.matchingRule", this.getMatchingRule(stack).getName()).setStyle(Style.EMPTY.withColor(Formatting.GRAY)));
    }
}
