package pers.solid.mishang.uc.util;

import net.minecraft.util.StringIdentifiable;

/**
 * 道路标线颜色，目前分为白色和黄色。
 */
public enum LineColor implements StringIdentifiable {
  WHITE,
  YELLOW,
  UNKNOWN,
  NONE;

  @Override
  public String asString() {
    return switch (this) {
      case WHITE -> "white";
      case YELLOW -> "yellow";
      case UNKNOWN -> "unknown";
      default -> "none";
    };
  }
}
