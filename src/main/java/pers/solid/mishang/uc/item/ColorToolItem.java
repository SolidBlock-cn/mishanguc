package pers.solid.mishang.uc.item;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.item.tooltip.TooltipType;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.text.Texts;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Formatting;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import pers.solid.mishang.uc.MishangUtils;
import pers.solid.mishang.uc.block.ColoredBlock;
import pers.solid.mishang.uc.blockentity.ColoredBlockEntity;
import pers.solid.mishang.uc.components.MishangucComponents;
import pers.solid.mishang.uc.util.ColorMixtureType;
import pers.solid.mishang.uc.util.TextBridge;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ColorToolItem extends BlockToolItem implements MishangucItem {
  public ColorToolItem(Settings settings, @Nullable Boolean includesFluid) {
    super(settings, includesFluid);
  }

  @Override
  public Text getName(ItemStack stack) {
    final Integer color = stack.get(MishangucComponents.COLOR);
    final Float opacity = stack.getOrDefault(MishangucComponents.OPACITY, 1f);
    final ColorMixtureType mixtureType = stack.getOrDefault(MishangucComponents.COLOR_MIXTURE_TYPE, ColorMixtureType.NORMAL);
    final List<Text> propertyTexts = new ArrayList<>();
    if (!opacity.equals(1f)) {
      propertyTexts.add(TextBridge.translatable("item.mishanguc.color_tool.properties.opacity", String.format("%.2f", opacity)));
    }
    if (color != null) {
      propertyTexts.add(TextBridge.translatable("item.mishanguc.color_tool.properties.color", MishangUtils.describeColor(color)));
    }
    if (mixtureType != ColorMixtureType.NORMAL) {
      propertyTexts.add(TextBridge.translatable("item.mishanguc.color_tool.properties.mixture_type", mixtureType.getName()));
    }
    if (propertyTexts.isEmpty()) {
      return super.getName(stack);
    } else {
      return TextBridge.translatable("item.mishanguc.color_tool.properties", super.getName(stack), Texts.join(propertyTexts, Texts.DEFAULT_SEPARATOR_TEXT));
    }
  }

  @Override
  public void appendTooltip(ItemStack stack, TooltipContext context, List<Text> tooltip, TooltipType type) {
    final Integer color = stack.get(MishangucComponents.COLOR);
    final ColorMixtureType mixtureType = stack.getOrDefault(MishangucComponents.COLOR_MIXTURE_TYPE, ColorMixtureType.NORMAL);
    if (mixtureType.requiresTargetColor()) {
      tooltip.add(TextBridge.translatable("item.mishanguc.color_tool.tooltip.1", TextBridge.keybind("key.attack").styled(style -> style.withColor(0xdddddd))).formatted(Formatting.GRAY));
    }
    switch (mixtureType) {
      case HUE_ROTATE -> tooltip.add(TextBridge.translatable("item.mishanguc.color_tool.tooltip.hue_rotate", TextBridge.keybind("key.use").styled(style -> style.withColor(0xdddddd))).formatted(Formatting.GRAY));
      case SATURATION_CHANGE -> tooltip.add(TextBridge.translatable("item.mishanguc.color_tool.tooltip.saturation", TextBridge.keybind("key.use").styled(style -> style.withColor(0xdddddd))).formatted(Formatting.GRAY));
      case BRIGHTNESS_CHANGE -> tooltip.add(TextBridge.translatable("item.mishanguc.color_tool.tooltip.brightness", TextBridge.keybind("key.use").styled(style -> style.withColor(0xdddddd))).formatted(Formatting.GRAY));
      default -> tooltip.add(TextBridge.translatable("item.mishanguc.color_tool.tooltip.2", TextBridge.keybind("key.use").styled(style -> style.withColor(0xdddddd))).formatted(Formatting.GRAY));
    }
    if (mixtureType.hasInvertEffect()) {
      tooltip.add(TextBridge.translatable("item.mishanguc.color_tool.tooltip.invert_when_sneaking").formatted(Formatting.GRAY));
    }
    if (color != null) {
      // 此时该对象已经定义了颜色。
      Color colorObject = new Color(color);
      tooltip.add(TextBridge.translatable("block.mishanguc.colored_block.tooltip.color",
          MishangUtils.describeColor(color)
      ).formatted(Formatting.GRAY));
      tooltip.add(TextBridge.translatable("block.mishanguc.colored_block.tooltip.color_components", colorObject.getRed(), colorObject.getGreen(), colorObject.getBlue(), colorObject.getAlpha()).formatted(Formatting.GRAY));
    }

    final Float opacity = stack.getOrDefault(MishangucComponents.OPACITY, 1f);
    if (!opacity.equals(1f)) {
      tooltip.add(TextBridge.translatable("item.mishanguc.color_tool.tooltip.opacity", opacity).formatted(Formatting.GRAY));
    }
    tooltip.add(TextBridge.translatable("item.mishanguc.color_tool.tooltip.mixture_type", mixtureType.getName()).formatted(Formatting.GRAY));
  }

  @Override
  public ActionResult useOnBlock(ItemStack stack, PlayerEntity player, World world, BlockHitResult blockHitResult, Hand hand, boolean fluidIncluded) {
    final BlockPos blockPos = blockHitResult.getBlockPos();
    BlockEntity blockEntity = world.getBlockEntity(blockPos);
    final Integer color = stack.get(MishangucComponents.COLOR);
    final ColorMixtureType mixtureType = stack.getOrDefault(MishangucComponents.COLOR_MIXTURE_TYPE, ColorMixtureType.NORMAL);
    if (color == null && mixtureType.requiresTargetColor()) {
      if (!world.isClient) {
        player.sendMessage(TextBridge.translatable("item.mishanguc.color_tool.message.no_data").formatted(Formatting.RED), true);
        return ActionResult.FAIL;
      }
      return ActionResult.PASS;
    }

    int prevColorRgb = 0; // the initial value should not usually be used.
    if (!(blockEntity instanceof ColoredBlockEntity coloredBlockEntity)) {
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

      if (coloredBlock != null && (mixtureType != ColorMixtureType.RANDOM || !world.isClient)) {
        prevColorRgb = blockState.getMapColor(world, blockPos).color;
        final BlockState coloredState = coloredBlock.getStateWithProperties(blockState);
        world.setBlockState(blockPos, coloredState);
        final BlockEntity oldBlockEntity = blockEntity;
        blockEntity = world.getBlockEntity(blockPos);
        if (oldBlockEntity != null && blockEntity != null) {
          blockEntity.read(oldBlockEntity.createNbt(world.getRegistryManager()), world.getRegistryManager());
        }
      }
    } else {
      prevColorRgb = coloredBlockEntity.getColor();
    }
    if (blockEntity instanceof ColoredBlockEntity coloredBlockEntity) {
      final Float opacity = stack.getOrDefault(MishangucComponents.OPACITY, 1f);
      final int mixed;

      // 根据颜色混合类型，计算 target
      final float amount = stack.getOrDefault(MishangucComponents.COLOR_CHANGE_AMOUNT, 0.05f) * (player.isSneaking() ? -1 : 1);
      final int target = mixtureType.handle(prevColorRgb, color == null ? 0 : color, amount, world.getRandom());

      if (mixtureType != ColorMixtureType.RANDOM || !world.isClient) {
        // 处于客户端时，且类型为随机时，不执行。
        if (opacity.equals(1f)) {
          coloredBlockEntity.setColor(mixed = target);
        } else {
          final Color prevColor = new Color(prevColorRgb);
          final Color targetColor = new Color(target);
          final Color mixedColor = new Color(
              MathHelper.lerp(opacity, prevColor.getRed(), targetColor.getRed()),
              MathHelper.lerp(opacity, prevColor.getGreen(), targetColor.getGreen()),
              MathHelper.lerp(opacity, prevColor.getBlue(), targetColor.getBlue()),
              0
          );
          mixed = mixedColor.getRGB();
          coloredBlockEntity.setColor(mixed);
        }
        blockEntity.markDirty();
        if (!world.isClient) {
          world.updateListeners(blockPos, blockEntity.getCachedState(), blockEntity.getCachedState(), Block.NOTIFY_LISTENERS);
          world.playSound(null, blockPos, SoundEvents.ITEM_DYE_USE, SoundCategory.BLOCKS, 1.0F, 1.0F);
          player.sendMessage(TextBridge.translatable("item.mishanguc.color_tool.message.success_set", MishangUtils.describeColor(mixed)), true);
        }
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
    final ColorMixtureType mixtureType = stack.getOrDefault(MishangucComponents.COLOR_MIXTURE_TYPE, ColorMixtureType.NORMAL);
    if (!mixtureType.requiresTargetColor()) {
      return ActionResult.SUCCESS;
    }
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

  public void appendToEntries(ItemGroup.Entries entries) {
    entries.add(createStack(1f, ColorMixtureType.NORMAL, null));
    entries.add(createStack(0.5f, ColorMixtureType.NORMAL, null));
    entries.add(createStack(0.25f, ColorMixtureType.NORMAL, null));
    entries.add(createStack(0.1f, ColorMixtureType.NORMAL, null));
    entries.add(createStack(1f, ColorMixtureType.RANDOM, null));
    entries.add(createStack(1f, ColorMixtureType.INVERT, null));
    entries.add(createStack(1f, ColorMixtureType.HUE, null));
    entries.add(createStack(1f, ColorMixtureType.HUE_AND_SATURATION, null));
    entries.add(createStack(1f, ColorMixtureType.HUE_ROTATE, 1f / 24));
    entries.add(createStack(1f, ColorMixtureType.SATURATION_CHANGE, 0.1f));
    entries.add(createStack(1f, ColorMixtureType.BRIGHTNESS_CHANGE, 0.1f));
  }

  private ItemStack createStack(float opacity, ColorMixtureType mixtureType, @Nullable Float amount) {
    final ItemStack defaultStack = getDefaultStack();
    defaultStack.set(MishangucComponents.OPACITY, opacity);
    defaultStack.set(MishangucComponents.COLOR_MIXTURE_TYPE, mixtureType);
    defaultStack.set(MishangucComponents.COLOR_CHANGE_AMOUNT, amount);
    return defaultStack;
  }
}
