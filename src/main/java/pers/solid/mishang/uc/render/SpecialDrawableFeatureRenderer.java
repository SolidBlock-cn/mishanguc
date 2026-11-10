package pers.solid.mishang.uc.render;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.Font;
import net.minecraft.client.renderer.feature.FeatureFrameContext;
import net.minecraft.client.renderer.feature.FeatureRendererType;
import net.minecraft.client.renderer.feature.RenderTypeFeatureRenderer;
import net.minecraft.client.renderer.feature.TextFeatureRenderer;
import net.minecraft.client.renderer.feature.submit.SubmitNode;
import org.joml.Matrix4f;
import pers.solid.mishang.uc.text.SpecialDrawable;

import java.util.List;

@Environment(EnvType.CLIENT)
public class SpecialDrawableFeatureRenderer extends RenderTypeFeatureRenderer<SpecialDrawableFeatureRenderer.SpecialDrawableSubmit> {

  public static List<SpecialDrawableSubmit> currentSpecialDrawableSubmits = List.of();

  @Override
  protected void buildGroup(FeatureFrameContext context, List<SpecialDrawableSubmit> specialDrawableSubmits) {
  }

  /**
   * 用于渲染此模组中的 {@link SpecialDrawable}。
   *
   * @since Minecraft 1.21.10
   */
  @Environment(EnvType.CLIENT)
  public record SpecialDrawableSubmit(Matrix4f matricesEntry, SpecialDrawable specialDrawable, int light, float x, float y) implements SubmitNode {
    public static final FeatureRendererType<SpecialDrawableSubmit> TYPE = FeatureRendererType.create("MishangSpecialDrawable");

    public void render(TextFeatureRenderer textFeatureRenderer, Font.GlyphVisitor glyphVisitor) {
      specialDrawable.drawInternal(matricesEntry, textFeatureRenderer, glyphVisitor, light, x, y);
    }

    @Override
    public FeatureRendererType<SpecialDrawableSubmit> featureType() {
      return TYPE;
    }
  }

}
