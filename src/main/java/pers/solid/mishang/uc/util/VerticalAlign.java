package pers.solid.mishang.uc.util;

import com.google.common.collect.BiMap;
import com.google.common.collect.ImmutableBiMap;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.StringIdentifiable;
import org.jetbrains.annotations.Nullable;

@SuppressWarnings("AlibabaEnumConstantsMustHaveComment")
public enum VerticalAlign implements StringIdentifiable {
  TOP,
  MIDDLE,
  BOTTOM;
  private static final BiMap<VerticalAlign, String> M =
      ImmutableBiMap.of(TOP, "top", MIDDLE, "middle", BOTTOM, "bottom");

  public static @Nullable VerticalAlign byName(String name) {
    return M.inverse().get(name);
  }

  @Override
  public String asString() {
    return M.get(this);
  }

  public TranslatableText getName() {
    return new TranslatableText("vertical_align.mishanguc." + asString());
  }
}
