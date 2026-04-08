package pers.solid.mishang.uc.util;

import com.google.common.collect.BiMap;
import com.google.common.collect.ImmutableBiMap;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.util.StringRepresentable;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.Nullable;

/**
 * 文本的水平对齐方式。
 */
public enum HorizontalAlign implements StringRepresentable {
  LEFT,
  CENTER,
  RIGHT;
  private static final BiMap<HorizontalAlign, String> M =
      ImmutableBiMap.of(LEFT, "left", CENTER, "center", RIGHT, "right");

  public static @Nullable HorizontalAlign byName(String name) {
    return M.inverse().get(name);
  }

  @Override
  public String getSerializedName() {
    return M.get(this);
  }

  public MutableComponent getName() {
    return TextBridge.translatable("horizontal_align.mishanguc." + getSerializedName());
  }

  /**
   * 左右交换，居中的不变。
   */
  @Contract(pure = true)
  public HorizontalAlign flip() {
    return switch (this) {
      case LEFT -> RIGHT;
      case RIGHT -> LEFT;
      default -> this;
    };
  }
}
