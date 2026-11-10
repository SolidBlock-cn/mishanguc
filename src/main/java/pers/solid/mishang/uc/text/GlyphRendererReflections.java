package pers.solid.mishang.uc.text;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.renderer.feature.TextFeatureRenderer;

import java.lang.reflect.Field;

/**
 * 此类用于为原版的私有类 {@link TextFeatureRenderer.GlyphRenderer} 提供反射修改字段的方法。
 */
@Environment(EnvType.CLIENT)
final class GlyphRendererReflections {
  static final Field poseField;
  static final Field lightCoordsField;
  static final Field displayModeField;

  static {
    try {
      final Class<?> t = Class.forName("net.minecraft.client.renderer.feature.TextFeatureRenderer$GlyphRenderer");
      poseField = t.getDeclaredField("pose");
      poseField.setAccessible(true);
      lightCoordsField = t.getDeclaredField("lightCoords");
      lightCoordsField.setAccessible(true);
      displayModeField = t.getDeclaredField("displayMode");
      displayModeField.setAccessible(true);
    } catch (ClassNotFoundException | NoSuchFieldException e) {
      throw new RuntimeException("Cannot apply reflection on render-related classes", e);
    }
  }
}
