package pers.solid.mishang.uc.item;

import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.ComponentUtils;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.util.ProblemReporter;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.TooltipDisplay;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.TagValueInput;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.Nullable;
import pers.solid.mishang.uc.MishangUtils;
import pers.solid.mishang.uc.block.ColoredBlock;
import pers.solid.mishang.uc.blockentity.ColoredBlockEntity;
import pers.solid.mishang.uc.components.MishangucComponents;
import pers.solid.mishang.uc.util.ColorMixtureType;
import pers.solid.mishang.uc.util.TextBridge;
import pers.solid.mishang.uc.util.WithMishangTooltip;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ColorToolItem extends BlockToolItem implements MishangucItem, WithMishangTooltip {
  public ColorToolItem(Properties settings, @Nullable Boolean includesFluid) {
    super(settings, includesFluid);
  }

  @Override
  public Component getName(ItemStack stack) {
    final Integer color = stack.get(MishangucComponents.COLOR);
    final Float opacity = stack.getOrDefault(MishangucComponents.OPACITY, 1f);
    final ColorMixtureType mixtureType = stack.getOrDefault(MishangucComponents.COLOR_MIXTURE_TYPE, ColorMixtureType.NORMAL);
    final List<Component> propertyTexts = new ArrayList<>();
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
      return TextBridge.translatable("item.mishanguc.color_tool.properties", super.getName(stack), ComponentUtils.formatList(propertyTexts, ComponentUtils.DEFAULT_NO_STYLE_SEPARATOR));
    }
  }

  @Override
  public void getMishangTooltip(ItemStack stack, TooltipContext context, List<Component> tooltip, TooltipFlag options) {
    final TooltipDisplay displayComponent = stack.getOrDefault(DataComponents.TOOLTIP_DISPLAY, TooltipDisplay.DEFAULT);
    final Integer color = stack.get(MishangucComponents.COLOR);
    final ColorMixtureType mixtureType = stack.getOrDefault(MishangucComponents.COLOR_MIXTURE_TYPE, ColorMixtureType.NORMAL);
    if (mixtureType.requiresTargetColor()) {
      tooltip.add(TextBridge.translatable("item.mishanguc.color_tool.tooltip.1", TextBridge.keybind("key.attack").withStyle(style -> style.withColor(0xdddddd))).withStyle(ChatFormatting.GRAY));
    }
    switch (mixtureType) {
      case HUE_ROTATE -> tooltip.add(TextBridge.translatable("item.mishanguc.color_tool.tooltip.hue_rotate", TextBridge.keybind("key.use").withStyle(style -> style.withColor(0xdddddd))).withStyle(ChatFormatting.GRAY));
      case SATURATION_CHANGE -> tooltip.add(TextBridge.translatable("item.mishanguc.color_tool.tooltip.saturation", TextBridge.keybind("key.use").withStyle(style -> style.withColor(0xdddddd))).withStyle(ChatFormatting.GRAY));
      case BRIGHTNESS_CHANGE -> tooltip.add(TextBridge.translatable("item.mishanguc.color_tool.tooltip.brightness", TextBridge.keybind("key.use").withStyle(style -> style.withColor(0xdddddd))).withStyle(ChatFormatting.GRAY));
      default -> tooltip.add(TextBridge.translatable("item.mishanguc.color_tool.tooltip.2", TextBridge.keybind("key.use").withStyle(style -> style.withColor(0xdddddd))).withStyle(ChatFormatting.GRAY));
    }
    if (mixtureType.hasInvertEffect()) {
      tooltip.add(TextBridge.translatable("item.mishanguc.color_tool.tooltip.invert_when_sneaking").withStyle(ChatFormatting.GRAY));
    }
    if (color != null && displayComponent.shows(MishangucComponents.COLOR)) {
      // 此时该对象已经定义了颜色。
      Color colorObject = new Color(color);
      tooltip.add(TextBridge.translatable("block.mishanguc.colored_block.tooltip.color",
          MishangUtils.describeColor(color)
      ).withStyle(ChatFormatting.GRAY));
      tooltip.add(TextBridge.translatable("block.mishanguc.colored_block.tooltip.color_components", colorObject.getRed(), colorObject.getGreen(), colorObject.getBlue(), colorObject.getAlpha()).withStyle(ChatFormatting.GRAY));
    }

    final Float opacity = stack.getOrDefault(MishangucComponents.OPACITY, 1f);
    if (!opacity.equals(1f) && displayComponent.shows(MishangucComponents.OPACITY)) {
      tooltip.add(TextBridge.translatable("item.mishanguc.color_tool.tooltip.opacity", opacity).withStyle(ChatFormatting.GRAY));
    }
    if (displayComponent.shows(MishangucComponents.COLOR_MIXTURE_TYPE)) {
      tooltip.add(TextBridge.translatable("item.mishanguc.color_tool.tooltip.mixture_type", mixtureType.getName()).withStyle(ChatFormatting.GRAY));
    }
  }

  @Override
  public InteractionResult useOnBlock(ItemStack stack, Player player, Level world, BlockHitResult blockHitResult, InteractionHand hand, boolean fluidIncluded) {
    final BlockPos blockPos = blockHitResult.getBlockPos();
    BlockEntity blockEntity = world.getBlockEntity(blockPos);
    final Integer color = stack.get(MishangucComponents.COLOR);
    final ColorMixtureType mixtureType = stack.getOrDefault(MishangucComponents.COLOR_MIXTURE_TYPE, ColorMixtureType.NORMAL);
    if (color == null && mixtureType.requiresTargetColor()) {
      if (!world.isClientSide()) {
        player.displayClientMessage(TextBridge.translatable("item.mishanguc.color_tool.message.no_data").withStyle(ChatFormatting.RED), true);
        return InteractionResult.FAIL;
      }
      return InteractionResult.CONSUME;
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
            .filter(entry -> blockState.is(entry.getKey()))
            .findAny()
            .map(Map.Entry::getValue)
            .orElse(null);
      }

      if (coloredBlock != null && (mixtureType != ColorMixtureType.RANDOM || !world.isClientSide())) {
        prevColorRgb = blockState.getMapColor(world, blockPos).col;
        final BlockState coloredState = coloredBlock.withPropertiesOf(blockState);
        world.setBlockAndUpdate(blockPos, coloredState);
        final BlockEntity oldBlockEntity = blockEntity;
        blockEntity = world.getBlockEntity(blockPos);
        if (oldBlockEntity != null && blockEntity != null) {
          blockEntity.loadWithComponents(TagValueInput.create(ProblemReporter.DISCARDING, world.registryAccess(), oldBlockEntity.saveWithoutMetadata(world.registryAccess())));
        }
      }
    } else {
      prevColorRgb = coloredBlockEntity.getColor();
    }
    if (blockEntity instanceof ColoredBlockEntity coloredBlockEntity) {
      final Float opacity = stack.getOrDefault(MishangucComponents.OPACITY, 1f);
      final int mixed;

      // 根据颜色混合类型，计算 target
      final float amount = stack.getOrDefault(MishangucComponents.COLOR_CHANGE_AMOUNT, 0.05f) * (player.isShiftKeyDown() ? -1 : 1);
      final int target = mixtureType.handle(prevColorRgb, color == null ? 0 : color, amount, world.getRandom());

      if (mixtureType != ColorMixtureType.RANDOM || !world.isClientSide()) {
        // 处于客户端时，且类型为随机时，不执行。
        if (!world.isClientSide()) {
          if (opacity.equals(1f)) {
            coloredBlockEntity.setColor(mixed = target);
          } else {
            final Color prevColor = new Color(prevColorRgb);
            final Color targetColor = new Color(target);
            final Color mixedColor = new Color(
                Mth.lerpInt(opacity, prevColor.getRed(), targetColor.getRed()),
                Mth.lerpInt(opacity, prevColor.getGreen(), targetColor.getGreen()),
                Mth.lerpInt(opacity, prevColor.getBlue(), targetColor.getBlue()),
                0
            );
            mixed = mixedColor.getRGB();
            coloredBlockEntity.setColor(mixed);
          }
          world.sendBlockUpdated(blockPos, blockEntity.getBlockState(), blockEntity.getBlockState(), Block.UPDATE_CLIENTS);
          world.playSound(null, blockPos, SoundEvents.DYE_USE, SoundSource.BLOCKS, 1.0F, 1.0F);
          player.displayClientMessage(TextBridge.translatable("item.mishanguc.color_tool.message.success_set", MishangUtils.describeColor(mixed)), true);
        }
        blockEntity.setChanged();
      }
      stack.hurtAndBreak(1, player, hand.asEquipmentSlot());
      return InteractionResult.SUCCESS;
    } else {
      if (!world.isClientSide()) {
        player.displayClientMessage(TextBridge.translatable("item.mishanguc.color_tool.message.not_colored").withStyle(ChatFormatting.RED), true);
        return InteractionResult.FAIL;
      }
      return InteractionResult.CONSUME;
    }
  }

  @Override
  public InteractionResult beginAttackBlock(ItemStack stack, Player player, Level world, InteractionHand hand, BlockPos pos, Direction direction, boolean fluidIncluded) {
    final ColorMixtureType mixtureType = stack.getOrDefault(MishangucComponents.COLOR_MIXTURE_TYPE, ColorMixtureType.NORMAL);
    if (!mixtureType.requiresTargetColor()) {
      return InteractionResult.SUCCESS;
    }
    final BlockState blockState = world.getBlockState(pos);
    final BlockEntity blockEntity = world.getBlockEntity(pos);
    final int color;
    if (blockEntity instanceof ColoredBlockEntity coloredBlockEntity) {
      color = coloredBlockEntity.getColor();
    } else {
      color = blockState.getMapColor(world, pos).col;
    }
    stack.set(MishangucComponents.COLOR, color);
    if (!world.isClientSide()) {
      player.displayClientMessage(TextBridge.translatable("item.mishanguc.color_tool.message.success_copied", MishangUtils.describeColor(color)), true);
    }
    return InteractionResult.SUCCESS;
  }

  public void appendToEntries(CreativeModeTab.Output entries) {
    entries.accept(createStack(1f, ColorMixtureType.NORMAL, null));
    entries.accept(createStack(0.5f, ColorMixtureType.NORMAL, null));
    entries.accept(createStack(0.25f, ColorMixtureType.NORMAL, null));
    entries.accept(createStack(0.1f, ColorMixtureType.NORMAL, null));
    entries.accept(createStack(1f, ColorMixtureType.RANDOM, null));
    entries.accept(createStack(1f, ColorMixtureType.INVERT, null));
    entries.accept(createStack(1f, ColorMixtureType.HUE, null));
    entries.accept(createStack(1f, ColorMixtureType.HUE_AND_SATURATION, null));
    entries.accept(createStack(1f, ColorMixtureType.HUE_ROTATE, 1f / 24));
    entries.accept(createStack(1f, ColorMixtureType.SATURATION_CHANGE, 0.1f));
    entries.accept(createStack(1f, ColorMixtureType.BRIGHTNESS_CHANGE, 0.1f));
  }

  private ItemStack createStack(float opacity, ColorMixtureType mixtureType, @Nullable Float amount) {
    final ItemStack defaultStack = getDefaultInstance();
    defaultStack.set(MishangucComponents.OPACITY, opacity);
    defaultStack.set(MishangucComponents.COLOR_MIXTURE_TYPE, mixtureType);
    defaultStack.set(MishangucComponents.COLOR_CHANGE_AMOUNT, amount);
    return defaultStack;
  }
}
