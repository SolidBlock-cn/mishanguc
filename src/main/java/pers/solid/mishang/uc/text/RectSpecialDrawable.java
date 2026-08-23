package pers.solid.mishang.uc.text;

import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.font.glyphs.EffectGlyph;
import net.minecraft.client.renderer.feature.TextFeatureRenderer;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import org.joml.Matrix4f;
import pers.solid.mishang.uc.MishangUtils;
import pers.solid.mishang.uc.mixin.TextRendererAccessor;

/**
 * 长方形，可以指定其宽度和高度。
 *
 * @param width  长方形宽度，若为 8 则与文本大小的高度（注意不是文本宽度）相同。
 * @param height 长方形的高度，若为 8 则与文本大小的高度相同。
 * @since 0.2.1 将此类改成了记录。
 */
public record RectSpecialDrawable(float width, float height, TextContext textContext) implements SpecialDrawable {

  @Environment(EnvType.CLIENT)
  @Override
  public void drawInternal(Matrix4f matricesEntry, TextFeatureRenderer textFeatureRenderer, int light, float x, float y) {
    final int color = textContext.color;
    final boolean shadow = this.textContext.outlineColorType == OutlineColorType.NONE && this.textContext.shadow;
    final Font textRenderer = Minecraft.getInstance().font;
    final Font.DisplayMode textLayerType = this.textContext.outlineColorType != OutlineColorType.NONE ? Font.DisplayMode.POLYGON_OFFSET : this.textContext.seeThrough ? Font.DisplayMode.SEE_THROUGH : Font.DisplayMode.NORMAL;
//    final Font.GlyphVisitor glyphDrawer = Font.GlyphVisitor.forMultiBufferSource(textFeatureRenderer, matricesEntry, textLayerType, light); todo 重新实现
    final EffectGlyph rectangleGlyph = ((TextRendererAccessor) textRenderer).getProvider().effect();
    if (this.textContext.outlineColorType != OutlineColorType.NONE) {
      // 绘制轮廓
      int outlineColor = this.textContext.outlineColorType == OutlineColorType.AUTO ? MishangUtils.toSignOutlineColor(color) : this.textContext.outlineColor;
      final int outlineAlpha = ((outlineColor & 0xfc000000) == 0) ? 255 : (outlineColor >> 24 & 0xFF);
//      glyphDrawer.acceptEffect(rectangleGlyph.createEffect(x - 1, y - 1, (width + x) + 1, y + height + 1, 0, ARGB.color(outlineAlpha, outlineColor), 0, 0));
    }
//    glyphDrawer.acceptEffect(rectangleGlyph.createEffect(x, y, (width + x), height + y, this.textContext.outlineColorType != OutlineColorType.NONE ? 0.02f : 0, color, shadow ? ARGB.scaleRGB(color, 0.25f) : 0, 1));
  }


  @Override
  public String getId() {
    return "rect";
  }

  @Override
  public SpecialDrawableType<RectSpecialDrawable> getType() {
    return SpecialDrawableTypes.RECT;
  }

  public static RectSpecialDrawable fromNbt(TextContext textContext, CompoundTag nbt) {
    return new RectSpecialDrawable(nbt.getFloatOr("width", 0f), nbt.getFloatOr("height", 0f), textContext);
  }

  public static RectSpecialDrawable fromStringArgs(TextContext textContext, String args) throws CommandSyntaxException {
    final String[] split = args.split(" ");
    if (split.length < 2) {
      throw new CommandSyntaxException(null, Component.translatable("special_drawable.rect.too_few", 2, split.length));
    } else if (split.length > 2) {
      throw new CommandSyntaxException(null, Component.translatable("special_drawable.rect.too_many", 2, split.length));
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
  public void writeNbt(CompoundTag nbt, HolderLookup.Provider registryLookup) {
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
  public SpecialDrawable cloneWithNewTextContext(TextContext textContext) {
    return new RectSpecialDrawable(width, height, textContext);
  }

  @Override
  public MutableComponent asStyledText() {
    return Component.empty()
        .append(Component.literal("■").withStyle(style -> style.withColor(textContext.color)))
        .append(" (" + width + "×" + height + ")");
  }
}
