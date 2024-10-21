package pers.solid.mishang.uc.util;

import com.mojang.serialization.Codec;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.text.Text;
import net.minecraft.util.StringIdentifiable;
import net.minecraft.util.function.ValueLists;
import net.minecraft.util.math.random.Random;
import org.jetbrains.annotations.Contract;

import java.awt.*;

/**
 * 颜色工具的混合类型，仅用于颜色工具。可以设置方块的颜色，或者设置随机颜色，也可以修改颜色的特定部分。
 */
public enum ColorMixtureType implements StringIdentifiable {
  NORMAL("normal") {
    @Override
    public int handle(int original, int target, float amount, Random random) {
      return target;
    }
  },
  RANDOM("random") {
    @Override
    public int handle(int original, int target, float amount, Random random) {
      return random.nextInt(0x1000000);
    }
  },
  INVERT("invert") {
    @Override
    public int handle(int original, int target, float amount, Random random) {
      return 0xffffff - (0xffffff & original);
    }
  },
  HUE("hue") {
    @Override
    public int handle(int original, int target, float amount, Random random) {
      final Color originalColor = new Color(original);
      final Color targetColor = new Color(target);
      final float[] originalHsl = ColorUtils.rgbToHsl(originalColor.getRed(), originalColor.getGreen(), originalColor.getBlue());
      final float[] targetHsl = ColorUtils.rgbToHsl(targetColor.getRed(), targetColor.getGreen(), targetColor.getBlue());
      return ColorUtils.hslToRgb(targetHsl[0], originalHsl[1], originalHsl[2]) & 0xffffff;
    }
  },
  HUE_AND_SATURATION("hue_and_saturation") {
    @Override
    public int handle(int original, int target, float amount, Random random) {
      final Color originalColor = new Color(original);
      final Color targetColor = new Color(target);
      final float[] originalHsl = ColorUtils.rgbToHsl(originalColor.getRed(), originalColor.getGreen(), originalColor.getBlue());
      final float[] targetHsl = ColorUtils.rgbToHsl(targetColor.getRed(), targetColor.getGreen(), targetColor.getBlue());
      return ColorUtils.hslToRgb(targetHsl[0], targetHsl[1], originalHsl[2]) & 0xffffff;
    }
  },
  HUE_ROTATE("hue_rotate") {
    @Override
    public int handle(int original, int target, float amount, Random random) {
      final Color originalColor = new Color(original);
      final float[] originalHsl = ColorUtils.rgbToHsl(originalColor.getRed(), originalColor.getGreen(), originalColor.getBlue());
      return ColorUtils.hslToRgb(originalHsl[0] + amount, originalHsl[1], originalHsl[2]) & 0xffffff;
    }
  },
  SATURATION_CHANGE("saturation_change") {
    @Override
    public int handle(int original, int target, float amount, Random random) {
      final Color originalColor = new Color(original);
      final float[] originalHsl = ColorUtils.rgbToHsl(originalColor.getRed(), originalColor.getGreen(), originalColor.getBlue());
      return ColorUtils.hslToRgb(originalHsl[0], Math.clamp(originalHsl[1] + amount, 0f, 1f), originalHsl[2]) & 0xffffff;
    }
  },
  BRIGHTNESS_CHANGE("brightness_change") {
    @Override
    public int handle(int original, int target, float amount, Random random) {
      final Color originalColor = new Color(original);
      final float[] originalHsl = ColorUtils.rgbToHsl(originalColor.getRed(), originalColor.getGreen(), originalColor.getBlue());
      return ColorUtils.hslToRgb(originalHsl[0], originalHsl[1], Math.clamp(originalHsl[2] + amount, 0f, 1f)) & 0xffffff;
    }
  };

  public static final Codec<ColorMixtureType> CODEC = StringIdentifiable.createCodec(ColorMixtureType::values);
  public static final PacketCodec<ByteBuf, ColorMixtureType> PACKET_CODEC = PacketCodecs.indexed(ValueLists.createIdToValueFunction(ColorMixtureType::ordinal, values(), ValueLists.OutOfBoundsHandling.WRAP), Enum::ordinal);

  private final String name;
  private final Text translatableName;

  ColorMixtureType(String name) {
    this.name = name;
    this.translatableName = Text.translatable("item.mishanguc.color_tool.mixture_type." + name);
  }

  @Override
  public String asString() {
    return name;
  }

  /**
   * 处理颜色。此方法是不带不透明度的，如需带有不透明度，需要在调用此方法后再与原颜色进行混合。
   *
   * @param original 原颜色，以 rgb 的形式。
   * @param target   目标颜色，仅限部分值。
   * @param amount   需要操作的量，仅限于部分值，表示对色相、饱和度、亮度的修改量。
   * @param random   仅限于 {{@link #RANDOM}}。
   * @return 修改后的颜色，以 rgb 的形式。
   */
  public abstract int handle(int original, int target, float amount, Random random);

  public Text getName() {
    return translatableName;
  }

  /**
   * 此混合类型是否需要目标颜色。对于颜色工具而言，如果混合类型需要目标颜色的混合，则必须先从方块中选取一个颜色，才能进行操作。
   */
  @Contract(pure = true)
  public boolean requiresTargetColor() {
    return switch (this) {
      case NORMAL, HUE, HUE_AND_SATURATION -> true;
      default -> false;
    };
  }

  /**
   * 此混合类型是否能够具有相反效果，通常用于特定混合类型的颜色工具，当潜行时往相反的方向操作，即在操作时，{@link #handle} 的 {@code amount} 参数会取相反数。
   */
  @Contract(pure = true)
  public boolean hasInvertEffect() {
    return switch (this) {
      case HUE_ROTATE, SATURATION_CHANGE, BRIGHTNESS_CHANGE -> true;
      default -> false;
    };
  }
}
