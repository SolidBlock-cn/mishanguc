package pers.solid.mishang.uc.data;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.data.models.model.TextureMapping;
import net.minecraft.client.data.models.model.TextureSlot;

@Environment(EnvType.CLIENT)
public class FasterTextureMap extends TextureMapping implements Cloneable {
  public FasterTextureMap varP(TextureSlot textureKey, String val) {
    put(textureKey, MishangucModels.texture(val));
    return this;
  }

  public FasterTextureMap base(String val) {
    return varP(MishangucTextureKeys.BASE, val);
  }

  public FasterTextureMap line(String val) {
    return varP(MishangucTextureKeys.LINE, val);
  }

  public FasterTextureMap particle(String val) {
    return varP(TextureSlot.PARTICLE, val);
  }

  public FasterTextureMap lineSide(String val) {
    return varP(MishangucTextureKeys.LINE_SIDE, val);
  }

  public FasterTextureMap lineSide2(String val) {
    return varP(MishangucTextureKeys.LINE_SIDE2, val);
  }

  public FasterTextureMap lineTop(String val) {
    return varP(MishangucTextureKeys.LINE_TOP, val);
  }

  public FasterTextureMap lineTop2(String val) {
    return varP(MishangucTextureKeys.LINE_TOP2, val);
  }

  @Override
  public FasterTextureMap clone() {
    try {
      return (FasterTextureMap) super.clone();
    } catch (CloneNotSupportedException e) {
      throw new RuntimeException(e);
    }
  }
}
