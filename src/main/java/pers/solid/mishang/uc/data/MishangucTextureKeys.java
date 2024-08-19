package pers.solid.mishang.uc.data;

import net.minecraft.data.client.TextureKey;

public final class MishangucTextureKeys {
  public static final TextureKey LIGHT = TextureKey.of("light", TextureKey.ALL);
  public static final TextureKey BAR = TextureKey.of("bar", TextureKey.TEXTURE);
  public static final TextureKey GLOW = TextureKey.of("glow");
  public static final TextureKey TEXTURE_TOP = TextureKey.of("texture_top", TextureKey.TEXTURE);
  public static final TextureKey LINE_SIDE = TextureKey.of("line_side", TextureKey.ALL);
  public static final TextureKey LINE_SIDE2 = TextureKey.of("line_side2", LINE_SIDE);
  public static final TextureKey LINE_SIDE3 = TextureKey.of("line_side3", LINE_SIDE);
  public static final TextureKey LINE_TOP = TextureKey.of("line_top");
  public static final TextureKey LINE_TOP2 = TextureKey.of("line_top2", LINE_TOP);
  public static final TextureKey BASE = TextureKey.of("base");
  public static final TextureKey LINE = TextureKey.of("line");

  private MishangucTextureKeys() {
  }
}
