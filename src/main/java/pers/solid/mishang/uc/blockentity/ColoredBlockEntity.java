package pers.solid.mishang.uc.blockentity;

import net.fabricmc.fabric.api.util.NbtType;
import net.minecraft.block.MapColor;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Formatting;
import net.minecraft.world.BlockView;

import java.awt.*;
import java.util.List;

public interface ColoredBlockEntity {
  /**
   * 给方块添加关于颜色的提示。
   *
   * @see net.minecraft.block.Block#appendTooltip(ItemStack, BlockView, List, TooltipContext)
   */
  static void appendColorTooltip(ItemStack stack, List<Text> tooltip) {
    final NbtCompound blockEntityTag = stack.getSubTag("BlockEntityTag");
    if (blockEntityTag != null && blockEntityTag.contains("color", NbtType.NUMBER)) {
      // 此时该对象已经定义了颜色。
      final int color = blockEntityTag.getInt("color");
      Color colorObject = new Color(color);
      tooltip.add(new TranslatableText("block.mishanguc.colored_block.tooltip.color",
          new LiteralText("")
              .append(new LiteralText("■").styled(style -> style.withColor(color)))
              .append(Integer.toHexString(color))
      ).formatted(Formatting.GRAY));
      tooltip.add(new TranslatableText("block.mishanguc.colored_block.tooltip.color_components",
          colorObject.getRed(),
          colorObject.getGreen(),
          colorObject.getBlue(),
          colorObject.getAlpha()
      ).formatted(Formatting.GRAY));
    } else {
      // 没有定义颜色的情况。
      tooltip.add(new TranslatableText("block.mishanguc.custom_color_tooltip.auto_color").formatted(Formatting.GRAY));
    }
  }

  default MapColor getNearestMapColor() {
    final int color = getColor();
    int lastDistance = Integer.MAX_VALUE;
    MapColor nearestMapColor = MapColor.CLEAR;
    for (int i = 0; i < 64; i++) {
      final MapColor mapColor1 = MapColor.get(i);
      final int mapColor = mapColor1.color;
      final int distance = Math.abs((mapColor << 4) % 255 - (color << 4) % 255) + Math.abs((mapColor << 2) % 255 - (color << 2) % 255) + Math.abs(mapColor % 255 - color % 255);
      if (distance < lastDistance) {
        lastDistance = distance;
        nearestMapColor = mapColor1;
      }
    }
    return nearestMapColor;
  }

  int getColor();
}
