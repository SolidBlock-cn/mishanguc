package pers.solid.mishang.uc.item;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.BlockState;
import net.minecraft.block.pattern.CachedBlockPosition;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.ActionResult;
import net.minecraft.util.BlockMirror;
import net.minecraft.util.Formatting;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class MirroringToolItem extends BlockToolItem {
  public MirroringToolItem(Settings settings, @Nullable Boolean includesFluid) {
    super(settings, includesFluid);
  }

  public ActionResult mirror(World world, BlockPos blockPos, Direction side, @Nullable Entity entity) {
    final BlockState blockState = world.getBlockState(blockPos);
    final Direction.Axis axis = side.getAxis();
    final BlockMirror mirror;
    switch (axis) {
      case X:
        mirror = BlockMirror.FRONT_BACK;
        break;
      default:
        if (entity == null) mirror = BlockMirror.NONE;
        else {
          switch (entity.getHorizontalFacing().getAxis()) {
            case X:
              mirror = BlockMirror.FRONT_BACK;
              break;
            case Z:
              mirror = BlockMirror.LEFT_RIGHT;
              break;
            default:
              mirror = BlockMirror.NONE;
              break;
          }
        }
        break;
      case Z:
        mirror = BlockMirror.LEFT_RIGHT;
        break;
    }
    final BlockState mirrored = blockState.mirror(mirror);
    final boolean setBlockState = world.setBlockState(blockPos, mirrored);
    return setBlockState && !blockState.equals(mirrored) ? ActionResult.SUCCESS : ActionResult.FAIL;
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
    final ActionResult result = mirror(world, blockPos, blockHitResult.getSide(), player);
    if (result == ActionResult.SUCCESS) stack.damage(1, player, player1 -> player1.sendToolBreakStatus(hand));
    return result;
  }

  @Override
  public ActionResult beginAttackBlock(
      PlayerEntity player, World world, Hand hand, BlockPos pos, Direction direction, boolean fluidIncluded) {
    final ItemStack mainHandStack = player.getStackInHand(hand);
    if (!player.abilities.allowModifyWorld && !mainHandStack.canDestroy(world.getTagManager(), new CachedBlockPosition(world, pos, false))) {
      return ActionResult.PASS;
    }
    final ActionResult result = mirror(world, pos, direction, player);
    if (result == ActionResult.SUCCESS) mainHandStack.damage(1, player, player1 -> player1.sendToolBreakStatus(hand));
    return result;
  }

  @Environment(EnvType.CLIENT)
  @Override
  public void appendTooltip(
      ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
    super.appendTooltip(stack, world, tooltip, context);
    tooltip.add(
        new TranslatableText("item.mishanguc.mirroring_tool.tooltip").formatted(Formatting.GRAY));
    final Boolean includesFluid = includesFluid(stack);
    if (includesFluid == null) {
      tooltip.add(
          new TranslatableText("item.mishanguc.block_tool.tooltip.includesFluidWhileSneaking")
              .formatted(Formatting.GRAY));
    } else if (includesFluid) {
      tooltip.add(
          new TranslatableText("item.mishanguc.block_tool.tooltip.includesFluid")
              .formatted(Formatting.GRAY));
    }
  }
}
