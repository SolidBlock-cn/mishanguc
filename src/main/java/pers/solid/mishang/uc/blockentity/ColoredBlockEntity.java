package pers.solid.mishang.uc.blockentity;

import net.fabricmc.fabric.api.util.NbtType;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Text;
import net.minecraft.text.TextColor;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Formatting;
import net.minecraft.util.math.BlockPos;
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
      tooltip.add(new TranslatableText("block.mishanguc.custom_color_tooltip.color",
          new LiteralText("")
              .append(new LiteralText("■ ").styled(style -> style.withColor(TextColor.fromRgb(color))))
              .append(Integer.toString(color))
      ).formatted(Formatting.GRAY));
      tooltip.add(new TranslatableText("block.mishanguc.custom_color_tooltip.components",
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

  /**
   * 根据方块坐标返回一个对应坐标的颜色。<br>
   * 放置一个没有指定颜色的可着色方块时，就会这样。
   */
  static int getDefaultColorFromPos(BlockPos pos) {
    final int x = pos.getX();
    final int y = pos.getY();
    final int z = pos.getZ();
    float hue = (x + y + z) / 64f;
    float saturation = 0.8f + 0.2f * (float) Math.sin(0.7 * x + 0.7 * y + 0.7 * z + 1);
    float brightness = (float) (0.6f + 0.2f * Math.sin(0.8 * x + 0.8 * y + 0.8 * z + 2));
    return Color.HSBtoRGB(hue, saturation, brightness);
  }

  int getColor();
}
