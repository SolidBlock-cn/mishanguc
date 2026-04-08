package pers.solid.mishang.uc.util;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.data.models.model.TextureMapping;
import net.minecraft.client.data.models.model.TextureSlot;
import net.minecraft.client.resources.model.sprite.Material;
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
        return TextureMapping.cube(new Material(texture));
      }
    };
  }

  static TextureMapReference topSideBottom(Identifier topTexture, Identifier sideTexture, Identifier bottomTexture) {
    return new TextureMapReference() {
      @Environment(EnvType.CLIENT)
      @Override
      public TextureMapping getTextureMap() {
        return TextureMapping.singleSlot(TextureSlot.TOP, new Material(topTexture)).put(TextureSlot.SIDE, new Material(sideTexture)).put(TextureSlot.BOTTOM, new Material(bottomTexture));
      }
    };
  }

  static TextureMapReference sideEnd(Identifier side, Identifier end) {
    return new TextureMapReference() {
      @Environment(EnvType.CLIENT)
      @Override
      public TextureMapping getTextureMap() {
        return TextureMapping.column(new Material(side), new Material(end));
      }
    };
  }
}
