package pers.solid.mishang.uc.util;

import com.mojang.serialization.Codec;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.util.StringRepresentable;
import org.jetbrains.annotations.Contract;

/**
 * 道路标线类型，可以是：普通、粗线、双线等，暂无虚线。道路标线的偏移不在此范围内。
 */
public enum LineType implements StringRepresentable {
  /**
   * 普通标线。
   */
  NORMAL,
  /**
   * 双线，即相近平行的两条线。
   */
  DOUBLE,
  /**
   * 粗线。
   */
  THICK;

  private final String name;
  public static final Codec<LineType> CODEC = StringRepresentable.fromEnum(LineType::values);

  LineType() {
    name = name().toLowerCase();
  }

  @Override
  public String getSerializedName() {
    return name;
  }

  @Contract(" -> new")
  public MutableComponent getName() {
    return Component.translatable("lineType." + name);
  }
}
