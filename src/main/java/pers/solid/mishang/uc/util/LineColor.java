package pers.solid.mishang.uc.util;

import net.minecraft.text.MutableText;
import net.minecraft.util.StringIdentifiable;

/**
 * 道路标线颜色，目前分为白色和黄色。
 */
public enum LineColor implements StringIdentifiable {
  WHITE,
  YELLOW,
  UNKNOWN,
  NONE;

  private final String name;

  LineColor() {
    name = name().toLowerCase();
  }

  @Override
  public String asString() {
    return name;
  }

  public MutableText getName() {
    return TextBridge.translatable("lineColor.mishanguc." + name);
  }
}
