package pers.solid.mishang.uc.render;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

@Environment(EnvType.CLIENT)
public interface MovingBlockRenderStateExtension {
  default boolean hasCustomColor$mishang() {
    return false;
  }

  default int customColor$mishang() {
    return 0;
  }

  default void setCustomColor$mishang(int color) {
    throw new AssertionError("Implemented via mixin.");
  }
}
