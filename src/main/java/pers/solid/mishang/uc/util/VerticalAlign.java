package pers.solid.mishang.uc.util;

import com.google.common.collect.BiMap;
import com.google.common.collect.ImmutableBiMap;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.util.StringRepresentable;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.Nullable;

public enum VerticalAlign implements StringRepresentable {
  TOP,
  MIDDLE,
  BOTTOM;
  private static final BiMap<VerticalAlign, String> M =
      ImmutableBiMap.of(TOP, "top", MIDDLE, "middle", BOTTOM, "bottom");

  public static @Nullable VerticalAlign byName(String name) {
    return M.inverse().get(name);
  }

  @Contract(value = "_, !null -> !null", pure = true)
  public static @Nullable VerticalAlign byName(String name, @Nullable VerticalAlign defaultValue) {
    final VerticalAlign value = byName(name);
    return value == null ? defaultValue : value;
  }

  @Override
  public String getSerializedName() {
    return M.get(this);
  }

  public MutableComponent getName() {
    return TextBridge.translatable("vertical_align.mishanguc." + getSerializedName());
  }
}
