package pers.solid.mishang.uc.text;

import com.mojang.brigadier.exceptions.CommandSyntaxException;
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

  @Environment(EnvType.CLIENT)
  @Override
  public void drawExtra(TextRenderer textRenderer, MatrixStack matrixStack, VertexConsumerProvider vertexConsumers, int light, float x, float y) {
    int color = textContext.color;
    BakedGlyph bakedGlyph = ((TextRendererAccessor) textRenderer).invokeGetFontStorage(Style.DEFAULT_FONT_ID).getRectangleBakedGlyph();
    final Matrix4f matrix4f = matrixStack.peek().getPositionMatrix();
    final RenderLayer layer = bakedGlyph.getLayer(textContext.outlineColorType != OutlineColorType.NONE ? TextRenderer.TextLayerType.POLYGON_OFFSET : textContext.seeThrough ? TextRenderer.TextLayerType.SEE_THROUGH : TextRenderer.TextLayerType.NORMAL);
    final VertexConsumer vertexConsumer = vertexConsumers.getBuffer(layer);
    final boolean shadow = textContext.outlineColorType == OutlineColorType.NONE && textContext.shadow;
    if (shadow) {
      // 绘制阴影
      BakedGlyph.Rectangle shadowRectangle = new BakedGlyph.Rectangle(x + 1, y + 1, (width + x) + 1, y + height + 1, 0, ColorHelper.scaleRgb(color, 0.25f));
      bakedGlyph.drawRectangle(shadowRectangle, matrix4f, vertexConsumer, light, false);
    }
    if (textContext.outlineColorType != OutlineColorType.NONE) {
      // 绘制轮廓
      int outlineColor = textContext.outlineColorType == OutlineColorType.AUTO ? MishangUtils.toSignOutlineColor(color) : textContext.outlineColor;
      final int outlineAlpha = ((outlineColor & 0xfc000000) == 0) ? 255 : (outlineColor >> 24 & 0xFF);
      BakedGlyph.Rectangle outlineRectangle = new BakedGlyph.Rectangle(x - 1, y - 1, (width + x) + 1, y + height + 1, 0, ColorHelper.withAlpha(outlineAlpha, outlineColor));
      bakedGlyph.drawRectangle(outlineRectangle, matrix4f, vertexConsumers.getBuffer(bakedGlyph.getLayer(TextRenderer.TextLayerType.NORMAL)), light, false);
    }

    final VertexConsumer vertexConsumer2 = vertexConsumers.getBuffer(layer);
    BakedGlyph.Rectangle rectangle = new BakedGlyph.Rectangle(x, y, (width + x), y + height, shadow ? 0.03f : textContext.outlineColorType != OutlineColorType.NONE ? 0.02f : 0, color);
    bakedGlyph.drawRectangle(rectangle, matrix4f, vertexConsumer2, light, false);
  }

  @Override
  public @NotNull String getId() {
    return "rect";
  }

  @Override
  public @NotNull SpecialDrawableType<RectSpecialDrawable> getType() {
    return SpecialDrawableTypes.RECT;
  }

  public static @NotNull RectSpecialDrawable fromNbt(@NotNull TextContext textContext, @NotNull NbtCompound nbt) {
    return new RectSpecialDrawable(nbt.getFloat("width", 0f), nbt.getFloat("height", 0f), textContext);
  }

  public static @NotNull RectSpecialDrawable fromStringArgs(@NotNull TextContext textContext, @NotNull String args) throws CommandSyntaxException {
    final String[] split = args.split(" ");
    if (split.length < 2) {
      throw new CommandSyntaxException(null, TextBridge.translatable("special_drawable.rect.too_few", 2, split.length));
    } else if (split.length > 2) {
      throw new CommandSyntaxException(null, TextBridge.translatable("special_drawable.rect.too_many", 2, split.length));
    }
    final float width;
    try {
      width = Float.parseFloat(split[0]);
    } catch (NumberFormatException e) {
      throw CommandSyntaxException.BUILT_IN_EXCEPTIONS.readerInvalidFloat().create(split[0]);
    }
    final float height;
    try {
      height = Float.parseFloat(split[1]);
    } catch (NumberFormatException e) {
      throw CommandSyntaxException.BUILT_IN_EXCEPTIONS.readerInvalidFloat().create(split[1]);
    }
    return new RectSpecialDrawable(width, height, textContext);
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
