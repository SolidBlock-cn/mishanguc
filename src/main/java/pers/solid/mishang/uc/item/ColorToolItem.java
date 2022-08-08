package pers.solid.mishang.uc.item;

import net.devtech.arrp.generator.ItemResourceGenerator;
import net.fabricmc.fabric.api.util.NbtType;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Formatting;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import pers.solid.mishang.uc.MishangUtils;
import pers.solid.mishang.uc.blockentity.ColoredBlockEntity;

import java.awt.*;
import java.util.List;

public class ColorToolItem extends BlockToolItem implements ItemResourceGenerator {
  public ColorToolItem(Settings settings, @Nullable Boolean includesFluid) {
    super(settings, includesFluid);
  }

  @Override
  public Text getName(ItemStack stack) {
    final NbtCompound nbt = stack.getNbt();
    if (nbt != null && nbt.contains("color", NbtType.NUMBER)) {
      final int color = nbt.getInt("color");
      return Text.translatable("block.mishanguc.colored_block.color", super.getName(stack), Text.empty().append(Text.literal("■").styled(style -> style.withColor(color))).append(Integer.toHexString(color)));
    } else {
      return super.getName(stack);
    }
  }

  @Override
  public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
    final NbtCompound nbt = stack.getNbt();
    tooltip.add(Text.translatable("item.mishanguc.color_tool.tooltip.1", Text.keybind("key.attack").styled(style -> style.withColor(0xdddddd))).formatted(Formatting.GRAY));
    tooltip.add(Text.translatable("item.mishanguc.color_tool.tooltip.2", Text.keybind("key.use").styled(style -> style.withColor(0xdddddd))).formatted(Formatting.GRAY));
    if (nbt != null && nbt.contains("color", NbtType.NUMBER)) {
      // 此时该对象已经定义了颜色。
      final int color = nbt.getInt("color");
      Color colorObject = new Color(color);
      tooltip.add(Text.translatable("block.mishanguc.colored_block.tooltip.color",
          Text.empty()
              .append(Text.literal("■").styled(style -> style.withColor(color)))
              .append(Integer.toHexString(color))
      ).formatted(Formatting.GRAY));
      tooltip.add(Text.translatable("block.mishanguc.colored_block.tooltip.color_components",
          colorObject.getRed(),
          colorObject.getGreen(),
          colorObject.getBlue(),
          colorObject.getAlpha()
      ).formatted(Formatting.GRAY));
    }
  }

  @Override
  public ActionResult useOnBlock(PlayerEntity player, World world, BlockHitResult blockHitResult, Hand hand, boolean fluidIncluded) {
    final BlockPos blockPos = blockHitResult.getBlockPos();
    final BlockEntity blockEntity = world.getBlockEntity(blockPos);
    final ItemStack stack = player.getStackInHand(hand);
    final NbtCompound nbt = stack.getNbt();
    if (nbt == null || !nbt.contains("color", NbtType.NUMBER)) {
      if (!world.isClient) {
        player.sendMessage(Text.translatable("item.mishanguc.color_tool.message.no_data").formatted(Formatting.RED));
      }
      return ActionResult.FAIL;
    }
    if (blockEntity instanceof ColoredBlockEntity coloredBlockEntity) {
      final int color = nbt.getInt("color");
      coloredBlockEntity.setColor(color);
      world.updateListeners(blockPos, blockEntity.getCachedState(), blockEntity.getCachedState(), Block.NOTIFY_LISTENERS);
      if (!world.isClient) {
        player.sendMessage(Text.translatable("item.mishanguc.color_tool.message.success_set", MishangUtils.describeColor(color)));
      }
      return ActionResult.success(world.isClient);
    } else {
      if (!world.isClient) {
        player.sendMessage(Text.translatable("item.mishanguc.color_tool.message.not_colored"));
        return ActionResult.CONSUME;
      }
      return ActionResult.PASS;
    }
  }

  @Override
  public ActionResult beginAttackBlock(PlayerEntity player, World world, Hand hand, BlockPos pos, Direction direction, boolean fluidIncluded) {
    final BlockState blockState = world.getBlockState(pos);
    final BlockEntity blockEntity = world.getBlockEntity(pos);
    final ItemStack stack = player.getStackInHand(hand);
    final int color;
    if (blockEntity instanceof ColoredBlockEntity coloredBlockEntity) {
      stack.getOrCreateNbt().putInt("color", color = coloredBlockEntity.getColor());
    } else {
      stack.getOrCreateNbt().putInt("color", color = blockState.getMapColor(world, pos).color);
    }
    if (!world.isClient) {
      player.sendMessage(Text.translatable("item.mishanguc.color_tool.message.success_copied", MishangUtils.describeColor(color)));
    }
    return null;
  }
}
