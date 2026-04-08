package pers.solid.mishang.uc.block;

import com.mojang.datafixers.util.Function4;
import it.unimi.dsi.fastutil.objects.Object2ObjectMap;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import pers.solid.mishang.uc.MishangUtils;
import pers.solid.mishang.uc.blockentity.ColoredBlockEntity;
import pers.solid.mishang.uc.components.MishangucComponents;
import pers.solid.mishang.uc.util.TextBridge;
import pers.solid.mishang.uc.util.WithMishangTooltip;

import java.awt.*;
import java.util.List;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.TooltipDisplay;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.loot.functions.CopyComponentsFunction;
import net.minecraft.world.level.storage.loot.functions.LootItemFunction;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;

/**
 * <p>所有带有颜色的方块应有的接口。其对应的方块实体应该实现 {@link pers.solid.mishang.uc.blockentity.ColoredBlockEntity}。
 * <p>在 {@link pers.solid.mishang.uc.MishangucClient} 中，本模组中所有实现该接口的方块都会为其自身以及方块物品注册颜色提供器。
 */
public interface ColoredBlock extends EntityBlock, MishangucBlock, WithMishangTooltip {

  LootItemFunction COPY_COLOR_LOOT_FUNCTION = CopyComponentsFunction.copyComponentsFromBlockEntity(LootContextParams.BLOCK_ENTITY).include(MishangucComponents.COLOR).build();

  /**
   * 给方块添加关于颜色的提示。
   *
   * @see WithMishangTooltip#getMishangTooltip(ItemStack, Item.TooltipContext, List, TooltipFlag)
   */
  static void appendColorTooltip(ItemStack stack, List<Component> tooltip) {
    if (!stack.getOrDefault(DataComponents.TOOLTIP_DISPLAY, TooltipDisplay.DEFAULT).shows(MishangucComponents.COLOR)) {
      return;
    }
    final Integer color = stack.get(MishangucComponents.COLOR);
    if (color != null) {
      // 此时该对象已经定义了颜色。
      Color colorObject = new Color(color);
      tooltip.add(TextBridge.translatable("block.mishanguc.colored_block.tooltip.color", MishangUtils.describeColor(color)).withStyle(ChatFormatting.GRAY));
      tooltip.add(TextBridge.translatable("block.mishanguc.colored_block.tooltip.color_components", colorObject.getRed(), colorObject.getGreen(), colorObject.getBlue(), colorObject.getAlpha()).withStyle(ChatFormatting.GRAY));
    } else {
      // 没有定义颜色的情况。
      tooltip.add(TextBridge.translatable("block.mishanguc.colored_block.tooltip.auto_color").withStyle(ChatFormatting.GRAY));
      tooltip.add(TextBridge.translatable("block.mishanguc.colored_block.tooltip.auto_color2").withStyle(ChatFormatting.GRAY));
    }
  }

  /**
   * 子类在覆盖 {@link net.minecraft.world.level.block.Block#getCloneItemStack(LevelReader, BlockPos, BlockState, boolean)} 时，可以这么写（下列代码使用yarn映射）：
   * <pre>{@code
   *     return getColoredPickStack(world, pos, state, super::getPickStack);}</pre>
   */
  default ItemStack getColoredPickStack(LevelReader world, BlockPos pos, BlockState state, boolean includeData, Function4<LevelReader, BlockPos, BlockState, Boolean, ItemStack> superGetPickStack) {
    final ItemStack stack = superGetPickStack.apply(world, pos, state, includeData);
    final BlockEntity blockEntity = world.getBlockEntity(pos);
    if (blockEntity instanceof ColoredBlockEntity coloredBlockEntity) {
      stack.set(MishangucComponents.COLOR, coloredBlockEntity.getColor());
    }
    return stack;
  }

  Object2ObjectMap<Block, Block> BASE_TO_COLORED = new Object2ObjectOpenHashMap<>();
  Object2ObjectMap<TagKey<Block>, Block> BASE_TAG_TO_COLORED = new Object2ObjectOpenHashMap<>();

  @Override
  default String customRecipeCategory() {
    return "colored_blocks";
  }
}
