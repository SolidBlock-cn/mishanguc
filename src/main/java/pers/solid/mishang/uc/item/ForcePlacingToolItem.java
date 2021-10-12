package pers.solid.mishang.uc.item;

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.fluid.FluidState;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Formatting;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import pers.solid.mishang.uc.render.ForcePlacingToolOutlineRenderer;
import pers.solid.mishang.uc.util.BlockPlacementContext;

import java.util.List;

/**
 * @see ForcePlacingToolOutlineRenderer
 */
public class ForcePlacingToolItem extends BlockToolItem {

    public ForcePlacingToolItem(Settings settings, @Nullable Boolean includesFluid) {
        super(settings, includesFluid);
    }

    @Override
    public ActionResult useOnBlock(PlayerEntity player, World world, BlockHitResult blockHitResult, Hand hand, boolean fluidIncluded) {
        BlockPlacementContext blockPlacementContext = new BlockPlacementContext(world,blockHitResult.getBlockPos(),player,player.getStackInHand(hand),blockHitResult,fluidIncluded);
        if (blockPlacementContext.world.isClient()) {
            blockPlacementContext.playSound();
        } else
            blockPlacementContext.setBlockState(24);
        return ActionResult.success(world.isClient);
    }

    @Override
    public ActionResult attackBlock(PlayerEntity player, World world, BlockPos pos, Direction direction, boolean fluidIncluded) {
        final BlockState blockState = world.getBlockState(pos);
        FluidState fluidState = blockState.getFluidState();
        world.setBlockState(pos, fluidIncluded ? Blocks.AIR.getDefaultState() : fluidState.getBlockState(), 24);
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
        tooltip.add(new TranslatableText("item.mishanguc.force_placing_tool.tooltip.1").formatted(Formatting.GRAY));
        tooltip.add(new TranslatableText("item.mishanguc.force_placing_tool.tooltip.2").formatted(Formatting.GRAY));
        if (Boolean.TRUE.equals(includesFluid(stack))) {
            tooltip.add(new TranslatableText("item.mishanguc.force_placing_tool.tooltip.fluids").formatted(Formatting.GRAY));
        }
        tooltip.add(new TranslatableText("item.mishanguc.force_placing_tool.tooltip.3").formatted(Formatting.GRAY));
    }
}
