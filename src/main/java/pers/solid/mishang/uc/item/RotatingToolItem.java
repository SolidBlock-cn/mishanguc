package pers.solid.mishang.uc.item;

import net.minecraft.block.BlockState;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.ActionResult;
import net.minecraft.util.BlockRotation;
import net.minecraft.util.Formatting;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class RotatingToolItem extends Item {
    public RotatingToolItem(Settings settings) {
        super(settings);
    }

    @Override
    public ActionResult useOnBlock(ItemUsageContext context) {
        final BlockPos blockPos = context.getBlockPos();
        final World world = context.getWorld();
        final BlockState blockState = world.getBlockState(blockPos);
        world.setBlockState(blockPos, blockState.rotate(context.getPlayer() != null && context.getPlayer().isSneaking() ? BlockRotation.COUNTERCLOCKWISE_90 : BlockRotation.CLOCKWISE_90));
        return ActionResult.SUCCESS;
    }

    @Override
    public boolean canMine(BlockState state, World world, BlockPos pos, PlayerEntity miner) {
        world.setBlockState(pos, state.rotate(miner.isSneaking() ? BlockRotation.COUNTERCLOCKWISE_90 : BlockRotation.CLOCKWISE_90));
        return false;
    }

    @Override
    public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
        super.appendTooltip(stack, world, tooltip, context);
        tooltip.add(new TranslatableText("item.mishanguc.rotating_tool.tooltip.1").setStyle(Style.EMPTY.withColor(Formatting.GRAY)));
        tooltip.add(new TranslatableText("item.mishanguc.rotating_tool.tooltip.2").setStyle(Style.EMPTY.withColor(Formatting.GRAY)));
    }
}
