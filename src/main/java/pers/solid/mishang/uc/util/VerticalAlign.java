package pers.solid.mishang.uc.util;

import com.google.common.collect.BiMap;
import com.google.common.collect.ImmutableBiMap;
import net.minecraft.text.MutableText;
import net.minecraft.util.StringIdentifiable;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.Nullable;

public enum VerticalAlign implements StringIdentifiable {
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
  public String asString() {
    return M.get(this);
  }

  public MutableText getName() {
    return TextBridge.translatable("vertical_align.mishanguc." + asString());
  }
}
