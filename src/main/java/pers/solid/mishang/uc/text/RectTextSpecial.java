package pers.solid.mishang.uc.text;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.font.GlyphRenderer;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.text.Style;
import net.minecraft.util.math.Matrix4f;
import org.jetbrains.annotations.NotNull;
import pers.solid.mishang.uc.MishangUtils;
import pers.solid.mishang.uc.mixin.TextRendererAccessor;

/**
 * 长方形，可以指定其宽度和高度。
 *
 * @param width  长方形宽度，若为 8 则与文本大小的高度（注意不是文本宽度）相同。
 * @param height 长方形的高度，若为 8 则与文本大小的高度相同。
 * @since 0.2.1 将此类改成了记录。
 */
public record RectTextSpecial(float width, float height, @NotNull TextContext textContext) implements TextSpecial {

  @Environment(EnvType.CLIENT)
  @Override
  public void drawExtra(TextRenderer textRenderer, MatrixStack matrixStack, VertexConsumerProvider vertexConsumers, int light, float x, float y) {
    int color = textContext.color;
    final float red = (float) (color >> 16 & 0xFF) / 255.0f;
    final float green = (float) (color >> 8 & 0xFF) / 255.0f;
    final float blue = (float) (color & 0xFF) / 255.0f;
    final float alpha = ((color & 0xFC000000) == 0) ? 1 : (float) (color >> 24 & 0xFF) / 255.0f;
    GlyphRenderer glyphRenderer = ((TextRendererAccessor) textRenderer).invokeGetFontStorage(Style.DEFAULT_FONT_ID).getRectangleRenderer();
    final Matrix4f matrix4f = matrixStack.peek().getPositionMatrix();
    final VertexConsumer vertexConsumer = vertexConsumers.getBuffer(glyphRenderer.getLayer(textContext.outlineColor != -2 ? TextRenderer.TextLayerType.POLYGON_OFFSET : textContext.seeThrough ? TextRenderer.TextLayerType.SEE_THROUGH : TextRenderer.TextLayerType.NORMAL));
    if (textContext.shadow) {
      // 绘制阴影
      GlyphRenderer.Rectangle shadowRectangle = new GlyphRenderer.Rectangle(x + 1, (height + y) + 1, (width + x) + 1, y + 1, 0, red * 0.25f, green * 0.25f, blue * 0.25f, alpha);
      glyphRenderer.drawRectangle(shadowRectangle, matrix4f, vertexConsumer, light);
    }
    if (textContext.outlineColor != -2) {
      // 绘制轮廓
      int outlineColor = textContext.outlineColor == -1 ? MishangUtils.toSignOutlineColor(color) : textContext.outlineColor;
      GlyphRenderer.Rectangle rectangle = new GlyphRenderer.Rectangle(x - 1, (height + y) + 1, (width + x) + 1, y - 1, 0, (outlineColor >> 16 & 255) / 255f, (outlineColor >> 8 & 255) / 255f, (outlineColor & 255) / 255f, alpha);
      glyphRenderer.drawRectangle(rectangle, matrix4f, vertexConsumers.getBuffer(glyphRenderer.getLayer(TextRenderer.TextLayerType.NORMAL)), light);
    }
    GlyphRenderer.Rectangle rectangle = new GlyphRenderer.Rectangle(x, (height + y), (width + x), y, textContext.shadow ? 0.24f : textContext.outlineColor != -2 ? 0.02f : 0, red, green, blue, alpha);
    glyphRenderer.drawRectangle(rectangle, matrix4f, vertexConsumer, light);
  }

  @Override
  public String getId() {
    return "rect";
  }

  public static RectTextSpecial fromNbt(@NotNull NbtCompound nbt, @NotNull TextContext textContext) {
    return new RectTextSpecial(nbt.getFloat("width"), nbt.getFloat("height"), textContext);
  }

  @Override
  public NbtCompound writeNbt(NbtCompound nbt) {
    TextSpecial.super.writeNbt(nbt);
    nbt.putFloat("width", width);
    nbt.putFloat("height", height);
    return nbt;
  }

  @Override
  public String asStringArgs() {
    return String.format("%s %s",
        width % 1 == 0 ? Integer.toString((int) width) : Float.toString(width),
        height % 1 == 0 ? Integer.toString((int) height) : Float.toString(height));
  }

  @Override
  public float width() {
    return width / 8;
  }

  @Override
  public float height() {
    return height / 8;
  }

  @Override
  public RectTextSpecial clone() {
    try {
      return (RectTextSpecial) super.clone();
    } catch (CloneNotSupportedException e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  public TextSpecial cloneWithNewTextContext(@NotNull TextContext textContext) {
    return new RectTextSpecial(width, height, textContext);
  }
}
