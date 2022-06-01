package pers.solid.mishang.uc.item;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.pattern.CachedBlockPosition;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.ActionResult;
import net.minecraft.util.BlockRotation;
import net.minecraft.util.Formatting;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class RotatingToolItem extends BlockToolItem {

  public RotatingToolItem(Settings settings, @Nullable Boolean includesFluid) {
    super(settings, includesFluid);
  }

  @Override
  public ActionResult useOnBlock(
      PlayerEntity player,
      World world,
      BlockHitResult blockHitResult,
      Hand hand,
      boolean fluidIncluded) {
    final BlockPos blockPos = blockHitResult.getBlockPos();
    final ItemStack stack = player.getStackInHand(hand);
    if (!player.abilities.allowModifyWorld && !stack.canPlaceOn(world.getTagManager(), new CachedBlockPosition(world, blockPos, false))) {
      return ActionResult.PASS;
    }
    final ActionResult result = rotateBlock(player, world, blockPos);
    if (result == ActionResult.SUCCESS) {
      stack.damage(1, player, player1 -> player1.sendToolBreakStatus(hand));
    }
    return result;
  }

  @NotNull
  private ActionResult rotateBlock(PlayerEntity player, World world, BlockPos blockPos) {
    final BlockRotation rotation =
        player.isSneaking() ? BlockRotation.COUNTERCLOCKWISE_90 : BlockRotation.CLOCKWISE_90;
    return rotateBlock(world, blockPos, rotation);
  }

  @NotNull
  private ActionResult rotateBlock(World world, BlockPos blockPos, BlockRotation rotation) {
    final boolean b = world.setBlockState(blockPos, world.getBlockState(blockPos).rotate(rotation));
    final BlockEntity blockEntity = world.getBlockEntity(blockPos);
    if (blockEntity != null) {
      blockEntity.applyRotation(rotation);
    }
    return ActionResult.success(b);
  }

  @Override
  public ActionResult beginAttackBlock(
      PlayerEntity player, World world, Hand hand, BlockPos pos, Direction direction, boolean fluidIncluded) {
    if (!player.abilities.allowModifyWorld && !player.getMainHandStack().canDestroy(world.getTagManager(), new CachedBlockPosition(world, pos, false))) {
      return ActionResult.PASS;
    }
    final ActionResult result = rotateBlock(player, world, pos);
    if (result == ActionResult.SUCCESS) {
      stack.damage(1, player, p -> p.sendToolBreakStatus(hand));
    }
    return result;
  }

  @Environment(EnvType.CLIENT)
  @Override
  public void appendTooltip(
      ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
    super.appendTooltip(stack, world, tooltip, context);
    tooltip.add(
        new TranslatableText("item.mishanguc.rotating_tool.tooltip.1")
            .formatted(Formatting.GRAY));
    tooltip.add(
        new TranslatableText("item.mishanguc.rotating_tool.tooltip.2")
            .formatted(Formatting.GRAY));
  }
}
