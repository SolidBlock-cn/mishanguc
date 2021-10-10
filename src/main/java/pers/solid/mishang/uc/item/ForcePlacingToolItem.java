package pers.solid.mishang.uc.item;

import net.minecraft.block.BlockState;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.fluid.FluidState;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Formatting;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import pers.solid.mishang.uc.render.ForcePlacingToolOutlineRenderer;
import pers.solid.mishang.uc.util.BlockPlacementContext;

import java.util.List;

/**
 * @see ForcePlacingToolOutlineRenderer
 */
public class ForcePlacingToolItem extends Item {
    public ForcePlacingToolItem(Settings settings) {
        super(settings);
    }

    @Override
    public ActionResult useOnBlock(ItemUsageContext context) {
        final World world = context.getWorld();
//        final BlockPos pos = context.getBlockPos();
//        final PlayerEntity player = context.getPlayer();
//        @Nullable Hand hand = null;
//        @Nullable Block handBlock = null;
//        for (@NotNull Hand hand1 : Hand.values()) {
//            if (player==null) break;
//            final ItemStack stackInHand = player.getStackInHand(hand1);
//            if (stackInHand.getItem() instanceof BlockItem) {
//                handBlock = ((BlockItem) stackInHand.getItem()).getBlock();
//                hand = hand1;
//                break;
//            }
//        }
        BlockPlacementContext blockPlacementContext = BlockPlacementContext.ofContext(context);
        if (blockPlacementContext == null) return ActionResult.PASS;
        blockPlacementContext.setBlockState(24);
        if (blockPlacementContext.world.isClient()) {
            blockPlacementContext.playSound();
        }
//        final BlockState state = world.getBlockState(pos);
//        final ItemPlacementContext placementContext = new ItemPlacementContext(player,hand,hand==null ? new ItemStack(state.getBlock().asItem()) : player.getStackInHand(hand), ((ItemUsageContextInvoker) context).invokeGetHitResult());
//        final BlockPos offsetPos = placementContext.getBlockPos();
//        final BlockState offsetPosState = world.getBlockState(offsetPos);
//        BlockState newState = handBlock == null ? null : handBlock.getPlacementState(placementContext);
//        if (newState == null)
//            newState = placementContext.canReplaceExisting() ? state.getBlock().getPlacementState(placementContext) : null;
//        if (newState == null) newState = state;
//        if (newState.getProperties().contains(Properties.WATERLOGGED)) {
//            newState = newState.with(Properties.WATERLOGGED, offsetPosState.getFluidState().isStill());
//        }
//        world.setBlockState(offsetPos,newState,24);
        return ActionResult.success(world.isClient);
    }

    @Override
    public boolean canMine(BlockState state, World world, BlockPos pos, PlayerEntity miner) {
        FluidState fluidState = world.getFluidState(pos);
        world.setBlockState(pos, fluidState.getBlockState(), 24);
        return true;
    }

    @Override
    public boolean postHit(ItemStack stack, LivingEntity target, LivingEntity attacker) {
        target.setHealth(0f);
        return super.postHit(stack, target, attacker);
    }

    @Override
    public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
        super.appendTooltip(stack, world, tooltip, context);
        tooltip.add(new TranslatableText("item.mishanguc.force_placing_tool.tooltip.1").setStyle(Style.EMPTY.withColor(Formatting.GRAY)));
        tooltip.add(new TranslatableText("item.mishanguc.force_placing_tool.tooltip.2").setStyle(Style.EMPTY.withColor(Formatting.GRAY)));
        tooltip.add(new TranslatableText("item.mishanguc.force_placing_tool.tooltip.3").setStyle(Style.EMPTY.withColor(Formatting.GRAY)));
    }
}
