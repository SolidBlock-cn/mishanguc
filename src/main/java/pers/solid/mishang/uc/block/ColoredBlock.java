package pers.solid.mishang.uc.block;

import com.mojang.datafixers.util.Function3;
import it.unimi.dsi.fastutil.objects.Object2ObjectMap;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import net.minecraft.block.Block;
import net.minecraft.block.BlockEntityProvider;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.tooltip.TooltipType;
import net.minecraft.loot.function.CopyComponentsLootFunction;
import net.minecraft.loot.function.LootFunction;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.WorldView;
import pers.solid.mishang.uc.MishangUtils;
import pers.solid.mishang.uc.blockentity.ColoredBlockEntity;
import pers.solid.mishang.uc.components.MishangucComponents;
import pers.solid.mishang.uc.util.TextBridge;

import java.awt.*;
import java.util.List;

/**
 * <p>所有带有颜色的方块应有的接口。其对应的方块实体应该实现 {@link pers.solid.mishang.uc.blockentity.ColoredBlockEntity}。
 * <p>在 {@link pers.solid.mishang.uc.MishangucClient} 中，本模组中所有实现该接口的方块都会为其自身以及方块物品注册颜色提供器。
 */
public interface ColoredBlock extends BlockEntityProvider, MishangucBlock {

  LootFunction COPY_COLOR_LOOT_FUNCTION = CopyComponentsLootFunction.builder(CopyComponentsLootFunction.Source.BLOCK_ENTITY).include(MishangucComponents.COLOR).build();

  /**
   * 给方块添加关于颜色的提示。
   *
   * @see Block#appendTooltip(ItemStack, Item.TooltipContext, List, TooltipType)
   */
  static void appendColorTooltip(ItemStack stack, List<Text> tooltip) {
    final Integer color = stack.get(MishangucComponents.COLOR);
    if (color != null) {
      // 此时该对象已经定义了颜色。
      Color colorObject = new Color(color);
      tooltip.add(TextBridge.translatable("block.mishanguc.colored_block.tooltip.color", MishangUtils.describeColor(color)).formatted(Formatting.GRAY));
      tooltip.add(TextBridge.translatable("block.mishanguc.colored_block.tooltip.color_components", colorObject.getRed(), colorObject.getGreen(), colorObject.getBlue(), colorObject.getAlpha()).formatted(Formatting.GRAY));
    } else {
      // 没有定义颜色的情况。
      tooltip.add(TextBridge.translatable("block.mishanguc.colored_block.tooltip.auto_color").formatted(Formatting.GRAY));
      tooltip.add(TextBridge.translatable("block.mishanguc.colored_block.tooltip.auto_color2").formatted(Formatting.GRAY));
    }
  }

  /**
   * 子类在覆盖 {@link net.minecraft.block.Block#getPickStack(WorldView, BlockPos, BlockState)} 时，可以这么写（下列代码使用yarn映射）：
   * <pre>{@code
   *     return getColoredPickStack(world, pos, state, super::getPickStack);}</pre>
   */
  default ItemStack getColoredPickStack(WorldView world, BlockPos pos, BlockState state, Function3<WorldView, BlockPos, BlockState, ItemStack> superGetPickStack) {
    final ItemStack stack = superGetPickStack.apply(world, pos, state);
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
