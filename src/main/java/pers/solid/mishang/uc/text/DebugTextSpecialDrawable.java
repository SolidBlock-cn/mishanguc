package pers.solid.mishang.uc.text;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.renderer.feature.TextFeatureRenderer;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import org.joml.Matrix4fc;

public record DebugTextSpecialDrawable(String text, TextContext textContext) implements SpecialDrawable {
  @Environment(EnvType.CLIENT)
  @Override
  public void drawInternal(Matrix4fc pose, TextFeatureRenderer textFeatureRenderer, Font.GlyphVisitor glyphVisitor, int light, float x, float y) {
    final Font textRenderer = Minecraft.getInstance().font;
    final Font.PreparedText preparedText = textRenderer.prepareText(text, x, y, textContext.color, textContext.shadow, 0);
    try {
      GlyphRendererReflections.poseField.set(glyphVisitor, pose);
      GlyphRendererReflections.lightCoordsField.set(glyphVisitor, light);
      GlyphRendererReflections.displayModeField.set(glyphVisitor, Font.DisplayMode.NORMAL);
    } catch (IllegalAccessException e) {
      throw new RuntimeException(e);
    }
    preparedText.visit(glyphVisitor);
  }

  @Override
  public SpecialDrawableType<DebugTextSpecialDrawable> getType() {
    return SpecialDrawableTypes.DEBUG_TEXT;
  }

  @Override
  public SpecialDrawable cloneWithNewTextContext(TextContext textContext) {
    return new DebugTextSpecialDrawable(text, textContext);
  }

  @Override
  public void writeNbt(CompoundTag nbt, HolderLookup.Provider registryLookup) {
    SpecialDrawable.super.writeNbt(nbt, registryLookup);
    nbt.putString("text", text);
  }

  @Override
  public String asStringArgs() {
    return text;
  }

  @Override
  public MutableComponent asStyledText() {
    return Component.literal("debug_text: ").append(text);
  }

}
