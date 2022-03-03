package pers.solid.mishang.uc.util;

import net.minecraft.util.StringIdentifiable;

/**
 * 道路标线类型，可以是：普通、粗线、双线等，暂无虚线。
 */
public enum LineType implements StringIdentifiable {
  NORMAL,
  DOUBLE,
  THICK;

  @Override
  public String asString() {
    switch (this) {
      case DOUBLE:
        return "double";
      case THICK:
        return "thick";
      default:
        return "normal";
    }
  }
}
