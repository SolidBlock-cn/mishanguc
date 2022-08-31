package pers.solid.mishang.uc.util;

import net.minecraft.text.MutableText;
import net.minecraft.util.StringIdentifiable;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

/**
 * 道路标线类型，可以是：普通、粗线、双线等，暂无虚线。
 */
public enum LineType implements StringIdentifiable {
  NORMAL,
  DOUBLE,
  THICK;

  private final String name;

  LineType() {
    name = name().toLowerCase();
  }

  @Override
  public String asString() {
    return name;
  }

  @Contract(" -> new")
  public @NotNull MutableText getName() {
    return TextBridge.translatable("lineType." + name);
  }
}
