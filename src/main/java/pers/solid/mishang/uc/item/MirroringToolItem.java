package pers.solid.mishang.uc.item;

import net.minecraft.block.BlockState;
import net.minecraft.client.item.TooltipContext;
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

  public ActionResult mirror(World world, BlockPos blockPos, Direction side) {
    final BlockState blockState = world.getBlockState(blockPos);
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
    return ActionResult.success(world.setBlockState(blockPos, blockState.mirror(mirror)));
  }

  @Override
  public ActionResult useOnBlock(
      PlayerEntity player,
      World world,
      BlockHitResult blockHitResult,
      Hand hand,
      boolean fluidIncluded) {
    return mirror(world, blockHitResult.getBlockPos(), blockHitResult.getSide());
  }

  @Override
  public ActionResult beginAttackBlock(
      PlayerEntity player, World world, BlockPos pos, Direction direction, boolean fluidIncluded) {
    return mirror(world, pos, direction);
  }

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
