package pers.solid.mishang.uc.data;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.data.models.model.TextureSlot;

@Environment(EnvType.CLIENT)
public final class MishangucTextureKeys {
  public static final TextureSlot LIGHT = TextureSlot.create("light", TextureSlot.ALL);
  public static final TextureSlot BAR = TextureSlot.create("bar", TextureSlot.TEXTURE);
  public static final TextureSlot GLOW = TextureSlot.create("glow");
  public static final TextureSlot TEXTURE_TOP = TextureSlot.create("texture_top", TextureSlot.TEXTURE);
  public static final TextureSlot LINE_SIDE = TextureSlot.create("line_side", TextureSlot.ALL);
  public static final TextureSlot LINE_SIDE2 = TextureSlot.create("line_side2", LINE_SIDE);
  public static final TextureSlot LINE_SIDE3 = TextureSlot.create("line_side3", LINE_SIDE);
  public static final TextureSlot LINE_TOP = TextureSlot.create("line_top");
  public static final TextureSlot LINE_TOP2 = TextureSlot.create("line_top2", LINE_TOP);
  public static final TextureSlot BASE = TextureSlot.create("base");
  public static final TextureSlot LINE = TextureSlot.create("line");

  private MishangucTextureKeys() {
  }
}
