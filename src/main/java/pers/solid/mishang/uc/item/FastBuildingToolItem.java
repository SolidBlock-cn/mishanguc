package pers.solid.mishang.uc.item;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.*;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Formatting;
import net.minecraft.util.Hand;
import net.minecraft.util.Util;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import pers.solid.mishang.uc.render.FastBuildingToolOutlineRenderer;
import pers.solid.mishang.uc.util.BlockMatchingRule;
import pers.solid.mishang.uc.util.BlockPlacementContext;

import java.util.List;

/**
 * 该物品可以快速建造或者删除一个平面上的多个方块。
 *
 * @see BlockMatchingRule
 * @see FastBuildingToolOutlineRenderer
 */
public class FastBuildingToolItem extends Item {
    public FastBuildingToolItem(Settings settings) {
        super(settings);
    }

    @Override
    public ActionResult useOnBlock(ItemUsageContext context) {
        final Direction side = context.getSide();
        final BlockPos centerBlockPos = context.getBlockPos();
        final @NotNull PlayerEntity player = context.getPlayer();
        final World world = context.getWorld();
        final BlockState centerState = world.getBlockState(centerBlockPos);
        final BlockPlacementContext blockPlacementContext = BlockPlacementContext.ofContext(context);
        if (blockPlacementContext == null) return ActionResult.PASS;
        final ItemStack stack = context.getStack();
        final int range = this.getRange(stack);
        final BlockMatchingRule matchingRule = this.getMatchingRule(stack);
        @Nullable Block handBlock = null;
        @Nullable Hand hand = null;
        for (Hand hand1 : Hand.values()) {
            final ItemStack stackInHand = player.getStackInHand(hand1);
            if (stackInHand.getItem() instanceof BlockItem) {
                handBlock = ((BlockItem) stackInHand.getItem()).getBlock();
                hand = hand1;
            }
        }
        for (BlockPos pos : matchingRule.getPlainValidBlockPoss(world, centerBlockPos, side, range)) {
            BlockState state = world.getBlockState(pos);
            if (matchingRule.match(centerState, state)) {
                final BlockPlacementContext offsetBlockPlacementContext = new BlockPlacementContext(blockPlacementContext, pos);
                if (offsetBlockPlacementContext.canPlace() && offsetBlockPlacementContext.canReplace()) {
                    if (world.isClient()) {
                        // 播放声音。
                        offsetBlockPlacementContext.playSound();
                        break; // 只播放一次声音就结束循环。。
                    } else {
                        offsetBlockPlacementContext.setBlockState(11);
                    }
                }
            }
        } // end for
        return ActionResult.success(world.isClient);
    }

    @Override
    public boolean canMine(BlockState centerState, World world, BlockPos centerPos, PlayerEntity miner) {
        if (!world.isClient()) {
            final BlockHitResult hit = ((BlockHitResult) miner.raycast(20, 0, false));
            final Direction side = hit.getSide();
            final BlockPos centerBlockPos = hit.getBlockPos();
            final ItemStack stack = miner.getMainHandStack();
            final int range = this.getRange(stack);
            final BlockMatchingRule matchingRule = this.getMatchingRule(stack);
            for (BlockPos pos : matchingRule.getPlainValidBlockPoss(world, centerBlockPos, side, range)) {
                world.removeBlock(pos, false);
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
        return tag.contains("Range", 99) ? Integer.min(tag.getInt("Range"), 128) : 8;
    }

    public @NotNull BlockMatchingRule getMatchingRule(ItemStack stack) {
        final NbtCompound tag = stack.getOrCreateTag();
        final BlockMatchingRule matchingRule = BlockMatchingRule.fromString(tag.getString("MatchingRule"));
        return matchingRule == null ? BlockMatchingRule.SAME_BLOCK : matchingRule;
    }

    @Override
    public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
        super.appendTooltip(stack, world, tooltip, context);
        tooltip.add(new TranslatableText("item.mishanguc.fast_building_tool.tooltip").setStyle(Style.EMPTY.withColor(Formatting.GRAY)));
        tooltip.add(new TranslatableText("item.mishanguc.fast_building_tool.tooltip.range", this.getRange(stack)).setStyle(Style.EMPTY.withColor(Formatting.GRAY)));
        tooltip.add(new TranslatableText("item.mishanguc.fast_building_tool.tooltip.matchingRule", this.getMatchingRule(stack).getName()).setStyle(Style.EMPTY.withColor(Formatting.GRAY)));
    }

    @Override
    public void appendStacks(ItemGroup group, DefaultedList<ItemStack> stacks) {
        if (this.isIn(group)) {
            stacks.add(Util.make(new ItemStack(this), stack -> {
                final NbtCompound tag = stack.getOrCreateTag();
                tag.putInt("Range", 8);
                tag.putString("MatchingRule", BlockMatchingRule.SAME_STATE.asString());
            }));
            stacks.add(Util.make(new ItemStack(this), stack -> {
                final NbtCompound tag = stack.getOrCreateTag();
                tag.putInt("Range", 32);
                tag.putString("MatchingRule", BlockMatchingRule.SAME_STATE.asString());
            }));
            stacks.add(Util.make(new ItemStack(this), stack -> {
                final NbtCompound tag = stack.getOrCreateTag();
                tag.putInt("Range", 8);
                tag.putString("MatchingRule", BlockMatchingRule.SAME_BLOCK.asString());
            }));
            stacks.add(Util.make(new ItemStack(this), stack -> {
                final NbtCompound tag = stack.getOrCreateTag();
                tag.putInt("Range", 32);
                tag.putString("MatchingRule", BlockMatchingRule.SAME_BLOCK.asString());
            }));
            stacks.add(Util.make(new ItemStack(this), stack -> {
                final NbtCompound tag = stack.getOrCreateTag();
                tag.putInt("Range", 8);
                tag.putString("MatchingRule", BlockMatchingRule.SAME_MATERIAL.asString());
            }));
            stacks.add(Util.make(new ItemStack(this), stack -> {
                final NbtCompound tag = stack.getOrCreateTag();
                tag.putInt("Range", 32);
                tag.putString("MatchingRule", BlockMatchingRule.SAME_MATERIAL.asString());
            }));
            stacks.add(Util.make(new ItemStack(this), stack -> {
                final NbtCompound tag = stack.getOrCreateTag();
                tag.putInt("Range", 8);
                tag.putString("MatchingRule", BlockMatchingRule.ANY.asString());
            }));
            stacks.add(Util.make(new ItemStack(this), stack -> {
                final NbtCompound tag = stack.getOrCreateTag();
                tag.putInt("Range", 32);
                tag.putString("MatchingRule", BlockMatchingRule.ANY.asString());
            }));
        }
    }
}
