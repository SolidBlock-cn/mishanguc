package pers.solid.mishang.uc.text;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.font.BakedGlyph;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.text.MutableText;
import net.minecraft.text.Style;
import net.minecraft.util.math.ColorHelper;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
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
  public void drawExtra(TextRenderer textRenderer, MatrixStack matrixStack, VertexConsumerProvider vertexConsumers, int light, float x, float y) {
    int color = textContext.color;
    final int alpha = ((color & 0xFC000000) == 0) ? 255 : (color >> 24 & 0xFF);
    //noinspection resource
    BakedGlyph bakedGlyph = ((TextRendererAccessor) textRenderer).invokeGetFontStorage(Style.DEFAULT_FONT_ID).getRectangleBakedGlyph();
    final float sizeMultiplier = 1;
    final RenderLayer layer = bakedGlyph.getLayer(textContext.outlineColorType != OutlineColorType.NONE ? TextRenderer.TextLayerType.POLYGON_OFFSET : textContext.seeThrough ? TextRenderer.TextLayerType.SEE_THROUGH : TextRenderer.TextLayerType.NORMAL);

    // 文本是否存在阴影。
    final boolean shadow = textContext.outlineColorType == OutlineColorType.NONE && textContext.shadow;
    // 用于文本渲染的矩阵。当存在阴影时，文本渲染需要适当调整。
    final List<BakedGlyph.Rectangle> rectanglesToDraw = new ArrayList<>();
    final List<BakedGlyph.Rectangle> outlineRectangles = textContext.outlineColorType == OutlineColorType.NONE ? null : new ArrayList<>();
    for (float[] rectangle : rectanglePattern.rectangles()) {
      final float minX = (rectangle[0] + x) * sizeMultiplier;
      final float minY = (rectangle[3] + y) * sizeMultiplier;
      final float maxX = (rectangle[2] + x) * sizeMultiplier;
      final float maxY = (rectangle[1] + y) * sizeMultiplier;
      if (shadow) {
        rectanglesToDraw.add(
            new BakedGlyph.Rectangle(minX + 1, minY + 1, maxX + 1, maxY + 1, 0, ColorHelper.withAlpha(alpha, ColorHelper.scaleRgb(color, 0.25f)))
        );
      }
      if (outlineRectangles != null) {
        int outlineColor = textContext.outlineColorType == OutlineColorType.AUTO ? MishangUtils.toSignOutlineColor(color) : textContext.outlineColor;
        final int outlineAlpha = ((outlineColor & 0xFC000000) == 0) ? 255 : (outlineColor >> 24 & 0xFF);
        outlineRectangles.add(
            new BakedGlyph.Rectangle(minX - 1, minY + 1, maxX + 1, maxY - 1, 0, ColorHelper.withAlpha(outlineAlpha, outlineColor))
        );

      }
      rectanglesToDraw.add(
          new BakedGlyph.Rectangle(minX, minY, maxX, maxY, shadow ? 0.03f : textContext.outlineColorType != OutlineColorType.NONE ? 0.02f : 0, ColorHelper.withAlpha(alpha, color))
      );
    }

    final Matrix4f matrix4f = matrixStack.peek().getPositionMatrix();
    final VertexConsumer vertexConsumer = vertexConsumers.getBuffer(layer);
    for (BakedGlyph.Rectangle rectangle : rectanglesToDraw) {
      bakedGlyph.drawRectangle(rectangle, matrix4f, vertexConsumer, light);
    }
    if (outlineRectangles != null) {
      final VertexConsumer vertexConsumerOutline = vertexConsumers.getBuffer(bakedGlyph.getLayer(TextRenderer.TextLayerType.NORMAL));
      for (BakedGlyph.Rectangle outlineRectangle : outlineRectangles) {
        bakedGlyph.drawRectangle(outlineRectangle, matrix4f, vertexConsumerOutline, light);
      }
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
  public @NotNull String getId() {
    return "pattern";
  }

  @Override
  public @NotNull SpecialDrawableType<PatternSpecialDrawable> getType() {
    return SpecialDrawableTypes.PATTERN;
  }

  @Override
  public SpecialDrawable cloneWithNewTextContext(@NotNull TextContext textContext) {
    return new PatternSpecialDrawable(textContext, rectanglePattern);
  }

  @Override
  public String asStringArgs() {
    return rectanglePattern.name();
  }

  @Contract(value = "_,_ -> new", pure = true)
  public static @Nullable PatternSpecialDrawable fromNbt(TextContext textContext, NbtCompound nbt) {
    final String shapeName = nbt.getString("shapeName", null);
    return fromName(textContext, shapeName);
  }

  @Override
  public void writeNbt(NbtCompound nbt, RegistryWrapper.WrapperLookup registryLookup) {
    SpecialDrawable.super.writeNbt(nbt, registryLookup);
    nbt.putString("shapeName", rectanglePattern.name());
  }

  public static @Nullable PatternSpecialDrawable fromName(TextContext textContext, String shapeName) {
    final RectanglePattern pattern = RectanglePatterns.get(shapeName);
    if (pattern == null) return null;
    return new PatternSpecialDrawable(textContext, pattern);
  }

  @Override
  public @NotNull MutableText asStyledText() {
    return SpecialDrawable.super.asStyledText().styled(style -> style.withColor(textContext.color));
  }
}
