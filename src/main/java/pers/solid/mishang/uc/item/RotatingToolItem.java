package pers.solid.mishang.uc.item;

import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Style;
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
  public ActionResult useOnBlock(PlayerEntity player, World world, BlockHitResult blockHitResult, Hand hand, boolean fluidIncluded) {
    final BlockPos blockPos = blockHitResult.getBlockPos();
    return rotateBlock(player, world, blockPos);
  }

  @NotNull
  private ActionResult rotateBlock(PlayerEntity player, World world, BlockPos blockPos) {
    final BlockRotation rotation = player.isSneaking() ? BlockRotation.COUNTERCLOCKWISE_90 : BlockRotation.CLOCKWISE_90;
    return rotateBlock(world, blockPos, rotation);
  }

  @NotNull
  private ActionResult rotateBlock(World world, BlockPos blockPos, BlockRotation rotation) {
    world.setBlockState(blockPos, world.getBlockState(blockPos).rotate(rotation));
    final BlockEntity blockEntity = world.getBlockEntity(blockPos);
    if (blockEntity != null) {
      blockEntity.applyRotation(rotation);
    }
    return ActionResult.SUCCESS;
  }

  @Override
  public ActionResult attackBlock(PlayerEntity player, World world, BlockPos pos, Direction direction, boolean fluidIncluded) {
    return rotateBlock(player, world, pos);
  }

  @Override
  public boolean canMine(BlockState state, World world, BlockPos pos, PlayerEntity miner) {
    rotateBlock(miner, world, pos);
    return false;
  }

  @Override
  public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
    super.appendTooltip(stack, world, tooltip, context);
    tooltip.add(new TranslatableText("item.mishanguc.rotating_tool.tooltip.1").setStyle(Style.EMPTY.withColor(Formatting.GRAY)));
    tooltip.add(new TranslatableText("item.mishanguc.rotating_tool.tooltip.2").setStyle(Style.EMPTY.withColor(Formatting.GRAY)));
  }
}
