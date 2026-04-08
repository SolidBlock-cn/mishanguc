package pers.solid.mishang.uc.text;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.font.glyphs.EffectGlyph;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.util.ARGB;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.Nullable;
import org.joml.Matrix4f;
import pers.solid.mishang.uc.MishangUtils;
import pers.solid.mishang.uc.mixin.TextRendererAccessor;

import java.util.ArrayList;
import java.util.List;

public record PatternSpecialDrawable(TextContext textContext, RectanglePattern rectanglePattern) implements SpecialDrawable {

  @Contract(pure = true)
  public boolean isEmpty() {
    return rectanglePattern == RectanglePatterns.EMPTY;
  }

  @Environment(EnvType.CLIENT)
  @Override
  public void drawInternal(Matrix4f matricesEntry, MultiBufferSource.BufferSource vertexConsumers, int light, float x, float y) {
    int color = this.textContext.color;
    final Font textRenderer = Minecraft.getInstance().font;
    final float sizeMultiplier = 1;
    final Font.DisplayMode textLayerType = this.textContext.outlineColorType != OutlineColorType.NONE ? Font.DisplayMode.POLYGON_OFFSET : this.textContext.seeThrough ? Font.DisplayMode.SEE_THROUGH : Font.DisplayMode.NORMAL;
    final Font.GlyphVisitor glyphDrawer = Font.GlyphVisitor.forMultiBufferSource(vertexConsumers, matricesEntry, textLayerType, light);

    // 文本是否存在阴影。
    final boolean shadow = this.textContext.outlineColorType == OutlineColorType.NONE && this.textContext.shadow;
    // 用于文本渲染的矩阵。当存在阴影时，文本渲染需要适当调整。
    final List<float[]> mainRectangles = new ArrayList<>();
    final List<float[]> outlineRectangles = this.textContext.outlineColorType == OutlineColorType.NONE ? null : new ArrayList<>();
    for (float[] rectangle : rectanglePattern.rectangles()) {
      final float minX = (rectangle[0] + x) * sizeMultiplier;
      final float minY = (rectangle[1] + y) * sizeMultiplier;
      final float maxX = (rectangle[2] + x) * sizeMultiplier;
      final float maxY = (rectangle[3] + y) * sizeMultiplier;
      if (outlineRectangles != null) {
        outlineRectangles.add(new float[]{minX - 1, minY - 1, maxX + 1, maxY + 1});

      }
      mainRectangles.add(new float[]{minX, minY, maxX, maxY});
    }

    final EffectGlyph rectangleGlyph = ((TextRendererAccessor) textRenderer).getProvider()
        .effect();
    if (outlineRectangles != null) {
      int outlineColor = this.textContext.outlineColorType == OutlineColorType.AUTO ? MishangUtils.toSignOutlineColor(color) : this.textContext.outlineColor;
      final int outlineAlpha = ((outlineColor & 0xFC000000) == 0) ? 255 : (outlineColor >> 24 & 0xFF);
      outlineColor = ARGB.color(outlineAlpha, outlineColor);
      for (float[] rectangle : outlineRectangles) {
        glyphDrawer.acceptEffect(rectangleGlyph.createEffect(rectangle[0], rectangle[1], rectangle[2], rectangle[3], 0, outlineColor, 0, 0));
      }
    }
    for (float[] rectangle : mainRectangles) {
      glyphDrawer.acceptEffect(rectangleGlyph.createEffect(rectangle[0], rectangle[1], rectangle[2], rectangle[3], this.textContext.outlineColorType != OutlineColorType.NONE ? 0.02f : 0, color, shadow ? ARGB.scaleRGB(color, 0.25f) : 0, 1));
    }
  }

  @Override
  public float height() {
    return 7 / 8f;
  }

  @Override
  public float width() {
    return 7 / 8f;
  }

  @Override
  public String getId() {
    return "pattern";
  }

  @Override
  public SpecialDrawableType<PatternSpecialDrawable> getType() {
    return SpecialDrawableTypes.PATTERN;
  }

  @Override
  public SpecialDrawable cloneWithNewTextContext(TextContext textContext) {
    return new PatternSpecialDrawable(textContext, rectanglePattern);
  }

  @Override
  public String asStringArgs() {
    return rectanglePattern.name();
  }

  @Contract(value = "_,_ -> new", pure = true)
  public static @Nullable PatternSpecialDrawable fromNbt(TextContext textContext, CompoundTag nbt) {
    final String shapeName = nbt.getStringOr("shapeName", null);
    return fromName(textContext, shapeName);
  }

  @Override
  public void writeNbt(CompoundTag nbt, HolderLookup.Provider registryLookup) {
    SpecialDrawable.super.writeNbt(nbt, registryLookup);
    nbt.putString("shapeName", rectanglePattern.name());
  }

  public static @Nullable PatternSpecialDrawable fromName(TextContext textContext, String shapeName) {
    final RectanglePattern pattern = RectanglePatterns.get(shapeName);
    if (pattern == null) return null;
    return new PatternSpecialDrawable(textContext, pattern);
  }

  @Override
  public MutableComponent asStyledText() {
    return SpecialDrawable.super.asStyledText().withStyle(style -> style.withColor(textContext.color));
  }
}
