package pers.solid.mishang.uc.util;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.data.models.model.TextureMapping;
import net.minecraft.client.data.models.model.TextureSlot;
import net.minecraft.resources.Identifier;

public interface TextureMapReference {
  @Environment(EnvType.CLIENT)
  TextureMapping getTextureMap();

  TextureMapReference EMPTY = new TextureMapReference() {
    @Environment(EnvType.CLIENT)
    @Override
    public TextureMapping getTextureMap() {
      return new TextureMapping();
    }
  };

  static TextureMapReference all(Identifier texture) {
    return new TextureMapReference() {
      @Environment(EnvType.CLIENT)
      @Override
      public TextureMapping getTextureMap() {
        return TextureMapping.cube(texture);
      }
    };
  }

  static TextureMapReference topSideBottom(Identifier topTexture, Identifier sideTexture, Identifier bottomTexture) {
    return new TextureMapReference() {
      @Environment(EnvType.CLIENT)
      @Override
      public TextureMapping getTextureMap() {
        return TextureMapping.singleSlot(TextureSlot.TOP, (topTexture)).put(TextureSlot.SIDE, (sideTexture)).put(TextureSlot.BOTTOM, bottomTexture);
      }
    };
  }

  static TextureMapReference sideEnd(Identifier side, Identifier end) {
    return new TextureMapReference() {
      @Environment(EnvType.CLIENT)
      @Override
      public TextureMapping getTextureMap() {
        return TextureMapping.column(side, end);
      }
    };
  }
}
