package pers.solid.mishang.uc.text;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.text.MutableText;
import net.minecraft.util.math.ColorHelper;
import org.jetbrains.annotations.NotNull;
import org.joml.Matrix4f;
import pers.solid.mishang.uc.MishangUtils;
import pers.solid.mishang.uc.mixin.TextRendererAccessor;
import pers.solid.mishang.uc.util.TextBridge;

/**
 * 长方形，可以指定其宽度和高度。
 *
 * @param width  长方形宽度，若为 8 则与文本大小的高度（注意不是文本宽度）相同。
 * @param height 长方形的高度，若为 8 则与文本大小的高度相同。
 * @since 0.2.1 将此类改成了记录。
 */
public record RectSpecialDrawable(float width, float height, @NotNull TextContext textContext) implements SpecialDrawable {

  @Override
  public void drawInternal(Matrix4f matricesEntry, VertexConsumerProvider.Immediate vertexConsumers, int light, float x, float y) {
    final int color = textContext.color;
    final boolean shadow = this.textContext.outlineColorType == OutlineColorType.NONE && this.textContext.shadow;
    final TextRenderer textRenderer = MinecraftClient.getInstance().textRenderer;
    final TextRenderer.TextLayerType textLayerType = this.textContext.outlineColorType != OutlineColorType.NONE ? TextRenderer.TextLayerType.POLYGON_OFFSET : this.textContext.seeThrough ? TextRenderer.TextLayerType.SEE_THROUGH : TextRenderer.TextLayerType.NORMAL;
    final TextRenderer.GlyphDrawer glyphDrawer = TextRenderer.GlyphDrawer.drawing(vertexConsumers, matricesEntry, textLayerType, light);
    if (this.textContext.outlineColorType != OutlineColorType.NONE) {
      // 绘制轮廓
      int outlineColor = this.textContext.outlineColorType == OutlineColorType.AUTO ? MishangUtils.toSignOutlineColor(color) : this.textContext.outlineColor;
      final int outlineAlpha = ((outlineColor & 0xfc000000) == 0) ? 255 : (outlineColor >> 24 & 0xFF);
      glyphDrawer.drawRectangle(
          ((TextRendererAccessor) textRenderer).getFonts()
              .getRectangleGlyph()
              .create(x - 1, y - 1, (width + x) + 1, y + height + 1, 0, ColorHelper.withAlpha(outlineAlpha, outlineColor), 0, 0));
    }
    glyphDrawer.drawRectangle(
        ((TextRendererAccessor) textRenderer).getFonts()
            .getRectangleGlyph()
            .create(x, y, (width + x), height + y, shadow ? 0.03f : this.textContext.outlineColorType != OutlineColorType.NONE ? 0.02f : 0, color, shadow ? ColorHelper.scaleRgb(color, 0.25f) : 0, 1));
  }


  @Override
  public @NotNull String getId() {
    return "rect";
  }

  @Override
  public @NotNull SpecialDrawableType<RectSpecialDrawable> getType() {
    return SpecialDrawableTypes.RECT;
  }

  public static RectSpecialDrawable fromNbt(@NotNull TextContext textContext, @NotNull NbtCompound nbt) {
    return new RectSpecialDrawable(nbt.getFloat("width", 0f), nbt.getFloat("height", 0f), textContext);
  }

  public static RectSpecialDrawable fromStringArgs(TextContext textContext, String args) {
    final RectSpecialDrawable rect;
    final String[] split = args.split(" ");
    if (split.length < 2) return null;
    try {
      final float width = Float.parseFloat(split[0]);
      final float height = Float.parseFloat(split[1]);
      rect = new RectSpecialDrawable(width, height, textContext);
    } catch (NumberFormatException e) {
      return null;
    }
    return rect;
  }

  @Override
  public void writeNbt(NbtCompound nbt, RegistryWrapper.WrapperLookup registryLookup) {
    SpecialDrawable.super.writeNbt(nbt, registryLookup);
    nbt.putFloat("width", width);
    nbt.putFloat("height", height);
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
  public RectSpecialDrawable clone() {
    try {
      return (RectSpecialDrawable) super.clone();
    } catch (CloneNotSupportedException e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  public SpecialDrawable cloneWithNewTextContext(@NotNull TextContext textContext) {
    return new RectSpecialDrawable(width, height, textContext);
  }

  @Override
  public @NotNull MutableText asStyledText() {
    return TextBridge.empty()
        .append(TextBridge.literal("■").styled(style -> style.withColor(textContext.color)))
        .append(" (" + width + "×" + height + ")");
  }
}
