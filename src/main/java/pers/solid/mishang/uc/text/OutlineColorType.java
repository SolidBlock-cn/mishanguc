package pers.solid.mishang.uc.text;

import net.minecraft.util.StringIdentifiable;

/**
 * 控制告示牌内文本的描边渲染类型。此数值原先是使用的和描边颜色共用整数值表示，使用 -2 表示没有描边，-1 表示自动描边，但这样出现了问题，会与 #fffffeff 和 #ffffffff（这里将 alpha 写最后）。因此加入了专门的枚举与原来的表示方式作区分。
 */
public enum OutlineColorType implements StringIdentifiable {
  NONE("none"),
  AUTO("auto"),
  CUSTOM("custom");

  @SuppressWarnings("deprecation")
  public static final StringIdentifiable.EnumCodec<OutlineColorType> CODEC = StringIdentifiable.createCodec(OutlineColorType::values);

  private final String name;

  OutlineColorType(String name) {
    this.name = name;
  }

  @Override
  public String asString() {
    return name;
  }

  public static OutlineColorType fromCompatibilityValue(int outlineColor) {
    return switch (outlineColor) {
      case -2 -> NONE;
      case -1 -> AUTO;
      default -> CUSTOM;
    };
  }

  public int toCompatibilityValue(int outlineColor) {
    return switch (this) {
      case NONE -> -2;
      case AUTO -> -1;
      case CUSTOM -> outlineColor;
    };
  }
}
