package pers.solid.mishang.uc.item;

import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.TooltipDisplay;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.Nullable;
import pers.solid.mishang.uc.components.MishangucComponents;
import pers.solid.mishang.uc.util.TextBridge;
import pers.solid.mishang.uc.util.WithMishangTooltip;

import java.util.Collection;
import java.util.List;

public class BlockStateToolItem extends BlockToolItem implements MishangucItem, WithMishangTooltip {

  public BlockStateToolItem(Properties settings, @Nullable Boolean includesFluid) {
    super(settings, includesFluid);
  }

  public static void broadcastProperties(
      BlockPos blockPos, Player player, BlockState blockState) {
    // 吐槽：为什么 Block#getName 要注解为 @Environment(EnvType.CLIENT)，导致这些东西都只能在客户端使用。
    final Collection<Property<?>> properties = blockState.getProperties();
    if (properties.isEmpty()) {
      player.displayClientMessage(
          TextBridge.translatable("debug.mishanguc.blockStates.none", String.format("%s %s %s", blockPos.getX(), blockPos.getY(), blockPos.getZ()), blockState.getBlock().getName().withStyle(ChatFormatting.BOLD))
              .withStyle(ChatFormatting.RED),
          false);
    } else {
      player.displayClientMessage(
          TextBridge.translatable("debug.mishanguc.blockStates", String.format("%s %s %s", blockPos.getX(), blockPos.getY(), blockPos.getZ()), blockState.getBlock().getName().withStyle(ChatFormatting.BOLD))
              .withStyle(ChatFormatting.YELLOW),
          false);
    }
    for (Property<?> property : properties) {
      final MutableComponent value = getFormattedValue(blockState, property);
      player.displayClientMessage(
          TextBridge.literal("  ")
              .append(
                  TextBridge.literal(property.getName())
                      .withStyle(style -> style.withColor(0xcccccc)))
              .append(" = ")
              .append(value),
          false);
    }
  }

  /**
   * 本方法的目的是考虑到泛型。如果内联，则 {@link Property#getName(Comparable)} 会因为泛型而存在问题。
   */
  private static <T extends Comparable<T>> MutableComponent getFormattedValue(BlockState blockState, Property<T> property) {
    final T propertyValue = blockState.getValue(property);
    final MutableComponent value = TextBridge.literal(property.getName(propertyValue));
    if (property instanceof BooleanProperty) {
      value.withStyle(propertyValue == Boolean.TRUE ? ChatFormatting.GREEN : ChatFormatting.RED);
    } else if (property instanceof IntegerProperty) {
      value.withStyle(style -> style.withColor(0x00eedd));
    }
    return value;
  }

  @Override
  public InteractionResult useOnBlock(
      ItemStack stack, Player player,
      Level world,
      BlockHitResult blockHitResult,
      InteractionHand hand,
      boolean fluidIncluded) {
    if (world.isClientSide()) {
      return getBlockStateOf(player, world, blockHitResult.getBlockPos(), fluidIncluded);
    } else {
      return InteractionResult.SUCCESS;
    }
  }

  @Override
  public InteractionResult beginAttackBlock(
      ItemStack stack, Player player, Level world, InteractionHand hand, BlockPos pos, Direction direction, boolean fluidIncluded) {
    if (!world.isClientSide()) return InteractionResult.SUCCESS;
    return getBlockStateOf(player, world, pos, fluidIncluded);
  }

  public InteractionResult getBlockStateOf(
      Player player, Level world, BlockPos blockPos, boolean fluidIncluded) {
    BlockState blockState = world.getBlockState(blockPos);
    broadcastProperties(blockPos, player, blockState);
    if (fluidIncluded) {
      final FluidState fluidState = world.getFluidState(blockPos);
      final int fluidLevel = fluidState.getAmount();
      if (fluidLevel != 0) {
        player.displayClientMessage(
            TextBridge.literal("  ")
                .append(
                    TextBridge.translatable("debug.mishanguc.blockStates.fluidLevel")
                        .withStyle(style -> style.withColor(0xcccccc)))
                .append(" = ")
                .append(String.valueOf(fluidLevel)),
            false);
      }
    }
    return InteractionResult.SUCCESS;
  }

  @Override
  public void getMishangTooltip(ItemStack stack, TooltipContext context, List<Component> tooltip, TooltipFlag options) {
    tooltip.add(
        TextBridge.translatable("item.mishanguc.block_state_tool.tooltip").withStyle(ChatFormatting.GRAY));
    final Boolean includesFluid = includesFluid(stack);
    if (stack.getOrDefault(DataComponents.TOOLTIP_DISPLAY, TooltipDisplay.DEFAULT).shows(MishangucComponents.INCLUDES_FLUID)) {
      if (includesFluid == null) {
        tooltip.add(
            TextBridge.translatable("item.mishanguc.block_state_tool.tooltip.includesFluidWhileSneaking")
                .withStyle(ChatFormatting.GRAY));
      } else if (includesFluid) {
        tooltip.add(
            TextBridge.translatable("item.mishanguc.block_state_tool.tooltip.includesFluid")
                .withStyle(ChatFormatting.GRAY));
      }
    }
  }
}
