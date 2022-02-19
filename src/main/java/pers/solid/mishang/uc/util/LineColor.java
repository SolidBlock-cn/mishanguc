package pers.solid.mishang.uc.util;

import net.minecraft.util.StringIdentifiable;

/** 道路标线颜色，目前分为白色和黄色。 */
public enum LineColor implements StringIdentifiable {
  WHITE,
  YELLOW,
  UNKNOWN,
  NONE;

  @Override
  public String asString() {
    switch (this) {
      case WHITE:
        return "white";
      case YELLOW:
        return "yellow";
      case UNKNOWN:
        return "unknown";
      default:
        return "none";
    }
  }
}
