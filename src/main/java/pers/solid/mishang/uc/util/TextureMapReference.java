package pers.solid.mishang.uc.util;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.data.TextureKey;
import net.minecraft.client.data.TextureMap;
import net.minecraft.util.Identifier;

public interface TextureMapReference {
  @Environment(EnvType.CLIENT)
  TextureMap getTextureMap();

  TextureMapReference EMPTY = new TextureMapReference() {
    @Environment(EnvType.CLIENT)
    @Override
    public TextureMap getTextureMap() {
      return new TextureMap();
    }
  };

  static TextureMapReference all(Identifier texture) {
    return new TextureMapReference() {
      @Environment(EnvType.CLIENT)
      @Override
      public TextureMap getTextureMap() {
        return TextureMap.all(texture);
      }
    };
  }

  static TextureMapReference topSideBottom(Identifier topTexture, Identifier sideTexture, Identifier bottomTexture) {
    return new TextureMapReference() {
      @Environment(EnvType.CLIENT)
      @Override
      public TextureMap getTextureMap() {
        return TextureMap.of(TextureKey.TOP, (topTexture)).put(TextureKey.SIDE, (sideTexture)).put(TextureKey.BOTTOM, bottomTexture);
      }
    };
  }

  static TextureMapReference sideEnd(Identifier side, Identifier end) {
    return new TextureMapReference() {
      @Environment(EnvType.CLIENT)
      @Override
      public TextureMap getTextureMap() {
        return TextureMap.sideEnd(side, end);
      }
    };
  }
}
