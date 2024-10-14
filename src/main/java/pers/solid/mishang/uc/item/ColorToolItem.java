package pers.solid.mishang.uc.item;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.client.item.TooltipType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.tooltip.TooltipType;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
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
import pers.solid.mishang.uc.block.ColoredBlock;
import pers.solid.mishang.uc.blockentity.ColoredBlockEntity;
import pers.solid.mishang.uc.components.MishangucComponents;
import pers.solid.mishang.uc.util.TextBridge;

import java.awt.*;
import java.util.List;
import java.util.Map;

public class ColorToolItem extends BlockToolItem implements MishangucItem {
  public ColorToolItem(Settings settings, @Nullable Boolean includesFluid) {
    super(settings, includesFluid);
  }

  @Override
  public Text getName(ItemStack stack) {
    final Integer color = stack.get(MishangucComponents.COLOR);
    if (color != null) {
      return TextBridge.translatable("block.mishanguc.colored_block.color", super.getName(stack), MishangUtils.describeColor(color));
    } else {
      return super.getName(stack);
    }
  }

  @Override
  public void appendTooltip(ItemStack stack, TooltipContext context, List<Text> tooltip, TooltipType type) {
    final Integer color = stack.get(MishangucComponents.COLOR);
    tooltip.add(TextBridge.translatable("item.mishanguc.color_tool.tooltip.1", TextBridge.keybind("key.attack").styled(style -> style.withColor(0xdddddd))).formatted(Formatting.GRAY));
    tooltip.add(TextBridge.translatable("item.mishanguc.color_tool.tooltip.2", TextBridge.keybind("key.use").styled(style -> style.withColor(0xdddddd))).formatted(Formatting.GRAY));
    if (color != null) {
      // 此时该对象已经定义了颜色。
      Color colorObject = new Color(color);
      tooltip.add(TextBridge.translatable("block.mishanguc.colored_block.tooltip.color",
          MishangUtils.describeColor(color)
      ).formatted(Formatting.GRAY));
      tooltip.add(TextBridge.translatable("block.mishanguc.colored_block.tooltip.color_components", colorObject.getRed(), colorObject.getGreen(), colorObject.getBlue(), colorObject.getAlpha()).formatted(Formatting.GRAY));
    }
  }

  @Override
  public ActionResult useOnBlock(ItemStack stack, PlayerEntity player, World world, BlockHitResult blockHitResult, Hand hand, boolean fluidIncluded) {
    final BlockPos blockPos = blockHitResult.getBlockPos();
    BlockEntity blockEntity = world.getBlockEntity(blockPos);
    final Integer color = stack.get(MishangucComponents.COLOR);
    if (color == null) {
      if (!world.isClient) {
        player.sendMessage(TextBridge.translatable("item.mishanguc.color_tool.message.no_data").formatted(Formatting.RED), true);
        return ActionResult.FAIL;
      }
      return ActionResult.PASS;
    }
    if (!(blockEntity instanceof ColoredBlockEntity)) {
      final BlockState blockState = world.getBlockState(blockPos);
      final Block block = blockState.getBlock();
      final Block coloredBlock;
      if (ColoredBlock.BASE_TO_COLORED.containsKey(block)) {
        coloredBlock = ColoredBlock.BASE_TO_COLORED.get(block);
      } else {
        coloredBlock = ColoredBlock.BASE_TAG_TO_COLORED.entrySet().stream()
            .filter(entry -> blockState.isIn(entry.getKey()))
            .findAny()
            .map(Map.Entry::getValue)
            .orElse(null);
      }

      if (coloredBlock != null) {
        final BlockState coloredState = coloredBlock.getStateWithProperties(blockState);
        world.setBlockState(blockPos, coloredState);
        final BlockEntity oldBlockEntity = blockEntity;
        blockEntity = world.getBlockEntity(blockPos);
        if (oldBlockEntity != null && blockEntity != null) {
          blockEntity.read(oldBlockEntity.createNbt(world.getRegistryManager()), world.getRegistryManager());
        }
      }
    }
    if (blockEntity instanceof ColoredBlockEntity coloredBlockEntity) {
      coloredBlockEntity.setColor(color);
      blockEntity.markDirty();
      world.updateListeners(blockPos, blockEntity.getCachedState(), blockEntity.getCachedState(), Block.NOTIFY_LISTENERS);
      world.playSound(null, blockPos, SoundEvents.ITEM_DYE_USE, SoundCategory.BLOCKS, 1.0F, 1.0F);
      if (!world.isClient) {
        player.sendMessage(TextBridge.translatable("item.mishanguc.color_tool.message.success_set", MishangUtils.describeColor(color)), true);
      }
      stack.damage(1, player, LivingEntity.getSlotForHand(hand));
      return ActionResult.success(world.isClient);
    } else {
      if (!world.isClient) {
        player.sendMessage(TextBridge.translatable("item.mishanguc.color_tool.message.not_colored").formatted(Formatting.RED), true);
        return ActionResult.FAIL;
      }
      return ActionResult.PASS;
    }
  }

  @Override
  public ActionResult beginAttackBlock(ItemStack stack, PlayerEntity player, World world, Hand hand, BlockPos pos, Direction direction, boolean fluidIncluded) {
    final BlockState blockState = world.getBlockState(pos);
    final BlockEntity blockEntity = world.getBlockEntity(pos);
    final int color;
    if (blockEntity instanceof ColoredBlockEntity coloredBlockEntity) {
      color = coloredBlockEntity.getColor();
    } else {
      color = blockState.getMapColor(world, pos).color;
    }
    stack.set(MishangucComponents.COLOR, color);
    if (!world.isClient) {
      player.sendMessage(TextBridge.translatable("item.mishanguc.color_tool.message.success_copied", MishangUtils.describeColor(color)), true);
    }
    return null;
  }
}
