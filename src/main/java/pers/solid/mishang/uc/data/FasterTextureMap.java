package pers.solid.mishang.uc.data;

import net.minecraft.data.client.TextureKey;
import net.minecraft.data.client.TextureMap;

public class FasterTextureMap extends TextureMap implements Cloneable {
  public FasterTextureMap varP(TextureKey textureKey, String val) {
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
    return varP(TextureKey.PARTICLE, val);
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
