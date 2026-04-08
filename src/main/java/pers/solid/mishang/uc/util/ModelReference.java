package pers.solid.mishang.uc.util;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.data.models.model.ModelTemplate;
import pers.solid.mishang.uc.data.MishangucModels;

public enum ModelReference {
  COLORED_CUBE_ALL,
  COLORED_CUBE_BOTTOM_TOP,
  COLORED_CUBE_MIRRORED_ALL,
  COLORED_CUBE_ALL_WITHOUT_SHADE;

  @Environment(EnvType.CLIENT)
  public final ModelTemplate getModel() {
    return switch (this) {
      case COLORED_CUBE_ALL -> MishangucModels.COLORED_CUBE_ALL;
      case COLORED_CUBE_BOTTOM_TOP -> MishangucModels.COLORED_CUBE_BOTTOM_TOP;
      case COLORED_CUBE_MIRRORED_ALL -> MishangucModels.COLORED_CUBE_MIRRORED_ALL;
      case COLORED_CUBE_ALL_WITHOUT_SHADE -> MishangucModels.COLORED_CUBE_ALL_WITHOUT_SHADE;
    };
  }
}
