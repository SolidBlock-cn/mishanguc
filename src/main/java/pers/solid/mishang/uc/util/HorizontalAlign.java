package pers.solid.mishang.uc.util;

import com.google.common.collect.BiMap;
import com.google.common.collect.ImmutableBiMap;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.StringIdentifiable;
import org.jetbrains.annotations.Nullable;

@SuppressWarnings("AlibabaEnumConstantsMustHaveComment")
public enum HorizontalAlign implements StringIdentifiable {
  LEFT,
  CENTER,
  RIGHT;
  private static final BiMap<HorizontalAlign, String> M =
      ImmutableBiMap.of(LEFT, "left", CENTER, "center", RIGHT, "right");

  public static @Nullable HorizontalAlign byName(String name) {
    return M.inverse().get(name);
  }

  @Override
  public String asString() {
    return M.get(this);
  }

  public TranslatableText getName() {
    return new TranslatableText("horizontal_align.mishanguc." + asString());
  }
}
