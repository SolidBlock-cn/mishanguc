package pers.solid.mishang.uc.util;

import net.minecraft.client.font.GlyphRenderer;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.text.Style;
import org.jetbrains.annotations.NotNull;
import pers.solid.mishang.uc.mixin.TextRendererAccessor;

public class RectTextExtra implements TextExtra {
  protected float minX;
  protected float minY;
  protected float maxX;
  protected float maxY;
  protected float zIndex;
  public final TextContext textContext;

  public RectTextExtra(TextContext textContext) {
    this.textContext = textContext;
  }

  @Override
  public void drawExtra(TextRenderer textRenderer, MatrixStack matrixStack, VertexConsumerProvider vertexConsumers, int light, float x, float y) {
    int color = textContext.color;
    final float red = (float) (color >> 16 & 0xFF) / 255.0f;
    final float green = (float) (color >> 8 & 0xFF) / 255.0f;
    final float blue = (float) (color & 0xFF) / 255.0f;
    final float alpha = ((color & 0xFC000000) == 0) ? 1 : (float) (color >> 24 & 0xFF) / 255.0f;
    GlyphRenderer glyphRenderer = ((TextRendererAccessor) textRenderer).invokeGetFontStorage(Style.DEFAULT_FONT_ID).getRectangleRenderer();
    GlyphRenderer.Rectangle rectangle = new GlyphRenderer.Rectangle(minX + x, minY - y, maxX + x, maxY - y, zIndex, red, green, blue, alpha);
    glyphRenderer.drawRectangle(rectangle, matrixStack.peek().getPositionMatrix(), vertexConsumers.getBuffer(glyphRenderer.getLayer(TextRenderer.TextLayerType.SEE_THROUGH)), light);
  }

  @Override
  public String getId() {
    return "rect";
  }

  public void readNbt(@NotNull NbtCompound nbt) {
    minX = nbt.getFloat("minX");
    minY = nbt.getFloat("minY");
    maxX = nbt.getFloat("maxX");
    maxY = nbt.getFloat("maxY");
    zIndex = nbt.getFloat("zIndex");
  }

  @Override
  public NbtCompound writeNbt(NbtCompound nbt) {
    nbt.putFloat("minX", minX);
    nbt.putFloat("minY", minY);
    nbt.putFloat("maxX", maxX);
    nbt.putFloat("maxY", maxY);
    nbt.putFloat("zIndex", zIndex);
    return nbt;
  }

  @Override
  public String describeArgs() {
    final String s0 = String.format("%s %s %s %s",
        minX % 1 == 0 ? Integer.toString((int) minX) : Float.toString(minX),
        minY % 1 == 0 ? Integer.toString((int) minY) : Float.toString(minY),
        maxX % 1 == 0 ? Integer.toString((int) maxX) : Float.toString(maxX),
        maxY % 1 == 0 ? Integer.toString((int) maxY) : Float.toString(maxY));
    return zIndex == 0 ? s0 : String.format("%s %s", s0, zIndex % 1 == 0 ? Integer.toString((int) zIndex) : Float.toString(zIndex));
  }

  @Override
  public float getWidth() {
    return Math.abs(minX - maxX);
  }
}
