package pers.solid.mishang.uc.util;

import net.minecraft.util.math.MathHelper;
import pers.solid.mishang.uc.Mishanguc;

/**
 * 用于处理颜色相关计算的实用类
 */
public final class ColorUtils {
  public static final float MaxRGB = 255.0f;

  public static float[] rgbToHsl(int red, int green, int blue) {
    float b, delta, g, max, min, r;

    float hue, saturation, luminosity;
    /*
     * Convert RGB to HSL colorspace.
     */
    r = red / MaxRGB;
    g = green / MaxRGB;
    b = blue / MaxRGB;
    max = Math.max(r, Math.max(g, b));
    min = Math.min(r, Math.min(g, b));

    hue = 0f;
    saturation = 0f;
    luminosity = (min + max) / 2f;
    delta = max - min;
    if (delta == 0.0) {
      return new float[]{hue, saturation, luminosity};
    }
    saturation = delta / ((luminosity <= 0.5f) ? (min + max) : (2.0f - max - min));
    if (r == max)
      hue = (g == min ? 5.0f + (max - b) / delta : 1.0f - (max - g) / delta);
    else if (g == max)
      hue = (b == min ? 1.0f + (max - r) / delta : 3.0f - (max - b) / delta);
    else
      hue = (r == min ? 3.0f + (max - g) / delta : 5.0f - (max - r) / delta);
    hue /= 6.0f;

    return new float[]{hue, saturation, luminosity};
  }

  public static int hslToRgb(float hue, float saturation, float luminosity) {
    float b, g, r, v, x, y, z;

    /*
     * Convert HSL to RGB colorspace.
     */
    v = (luminosity <= 0.5f) ? (luminosity * (1.0f + saturation))
        : (luminosity + saturation - luminosity * saturation);
    int red, green, blue;
    if (saturation == 0.0) {
      red = (int) (MaxRGB * luminosity + 0.5f);
      green = (int) (MaxRGB * luminosity + 0.5f);
      blue = (int) (MaxRGB * luminosity + 0.5f);
    } else {
      y = 2f * luminosity - v;
      hue = MathHelper.floorMod(hue, 1);
      x = y + (v - y) * (6.0f * hue - MathHelper.floor(6.0f * hue));
      z = v - (v - y) * (6.0f * hue - MathHelper.floor(6.0f * hue));
      switch ((int) (6.0f * hue)) {
        case 1:
          r = z;
          g = v;
          b = y;
          break;
        case 2:
          r = y;
          g = v;
          b = x;
          break;
        case 3:
          r = y;
          g = z;
          b = v;
          break;
        case 4:
          r = x;
          g = y;
          b = v;
          break;
        case 5:
          r = v;
          g = y;
          b = z;
          break;
        case 0:
        default:
          r = v;
          g = x;
          b = y;
          break;
      }
      red = (int) (MaxRGB * r + 0.5);
      green = (int) (MaxRGB * g + 0.5);
      blue = (int) (MaxRGB * b + 0.5);
    }

    if (red > 255 || green > 255 || blue > 255) {
      Mishanguc.MISHANG_LOGGER.warn("rgb = {} {} {}", red, green, blue);
    }

    return (red << 16) | (green << 8) | (blue);
  }
}