package pers.solid.mishang.uc.item;

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.fluid.FluidState;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Formatting;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import pers.solid.mishang.uc.render.ForcePlacingToolOutlineRenderer;
import pers.solid.mishang.uc.util.BlockPlacementContext;

import java.util.List;
import java.util.Objects;

/**
 * @see ForcePlacingToolOutlineRenderer
 */
public class ForcePlacingToolItem extends BlockToolItem {

    public ForcePlacingToolItem(Settings settings, @Nullable Boolean includesFluid) {
        super(settings, includesFluid);
    }

    @Override
    public ActionResult useOnBlock(PlayerEntity player, World world, BlockHitResult blockHitResult, @Nullable ItemUsageContext itemUsageContext, boolean fluidIncluded) {
        if (!world.isClient) {
            Objects.requireNonNull(itemUsageContext);
            BlockPlacementContext blockPlacementContext = BlockPlacementContext.ofContext(itemUsageContext, fluidIncluded);
            if (blockPlacementContext == null) return ActionResult.PASS;
            blockPlacementContext.setBlockState(24);
            if (blockPlacementContext.world.isClient()) {
                blockPlacementContext.playSound();
            }
        }
        return ActionResult.success(world.isClient);
    }

    @Override
    public ActionResult mineBlock(PlayerEntity player, World world, BlockPos blockPos, BlockState blockState, boolean fluidIncluded) {
        if (!world.isClient) {
            FluidState fluidState = blockState.getFluidState();
            world.setBlockState(blockPos, fluidIncluded ? Blocks.AIR.getDefaultState() : fluidState.getBlockState(), 24);
        }
        return ActionResult.SUCCESS;
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
