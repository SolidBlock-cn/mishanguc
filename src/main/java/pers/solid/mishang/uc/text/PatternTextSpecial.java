package pers.solid.mishang.uc.text;

import com.google.common.collect.ImmutableMap;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.font.GlyphRenderer;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.text.Style;
import net.minecraft.util.math.Matrix4f;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Unmodifiable;
import pers.solid.mishang.uc.MishangUtils;
import pers.solid.mishang.uc.mixin.TextRendererAccessor;

public record PatternTextSpecial(TextContext textContext, String shapeName, @Unmodifiable float[][] rectangles) implements TextSpecial {
  private static final float[][] EMPTY = {};
  private static final float[][] ARROW_LEFT =
      {
          // 箭头的中间部分：
          {0, 3, 7, 4},
          // 箭头的上边：
          {2, 1, 3, 3},
          {1, 2, 2, 3},
          // 箭头的下边：
          {1, 4, 2, 5},
          {2, 4, 3, 6}
      };

  private static final float[][] ARROW_RIGHT =
      {
          // 箭头的中间部分：
          {0, 3, 7, 4},
          // 箭头的上边：
          {4, 1, 5, 3},
          {5, 2, 6, 3},
          // 箭头的下边：
          {5, 4, 6, 5},
          {4, 4, 5, 6}
      };

  private static final float[][] ARROW_LEFT_TOP =
      {
          // 箭头的左尾：
          {1, 1, 2, 5},
          // 箭头的右尾：
          {2, 1, 5, 2},
          // 箭头的杆：
          {2, 2, 3, 3},
          {3, 3, 4, 4},
          {4, 4, 5, 5},
          {5, 5, 6, 6}
      };
  private static final float[][] ARROW_RIGHT_TOP =
      {
          // 箭头的左尾：
          {5, 1, 6, 5},
          // 箭头的右尾：
          {2, 1, 5, 2},
          // 箭头的杆：
          {4, 2, 5, 3},
          {3, 3, 4, 4},
          {2, 4, 3, 5},
          {1, 5, 2, 6}
      };
  private static final float[][] ARROW_LEFT_BOTTOM =
      {
          // 箭头的左尾：
          {1, 2, 2, 6},
          // 箭头的右尾：
          {2, 5, 5, 6},
          // 箭头的杆：
          {2, 4, 3, 5},
          {3, 3, 4, 4},
          {4, 2, 5, 3},
          {5, 1, 6, 2}
      };
  private static final float[][] ARROW_RIGHT_BOTTOM =
      {
          // 箭头的左尾：
          {5, 2, 6, 6},
          // 箭头的右尾：
          {2, 5, 5, 6},
          // 箭头的杆：
          {4, 4, 5, 5},
          {3, 3, 4, 4},
          {2, 2, 3, 3},
          {1, 1, 2, 2}
      };
  private static final float[][] CIRCLE = {
      {2, 1, 5, 2},
      {2, 5, 5, 6},
      {1, 2, 2, 5},
      {5, 2, 6, 5}
  };
  private static final float[][] BAN = {
      {2, 1, 5, 2},
      {2, 5, 5, 6},
      {1, 2, 2, 5},
      {5, 2, 6, 5},
      {2, 2, 3, 3},
      {3, 3, 4, 4},
      {4, 4, 5, 5}
  };

  private static final ImmutableMap<String, float[][]> NAME_TO_SHAPE = ImmutableMap.of(
      "al", ARROW_LEFT,
      "ar", ARROW_RIGHT,
      "alt", ARROW_LEFT_TOP,
      "art", ARROW_RIGHT_TOP,
      "alb", ARROW_LEFT_BOTTOM,
      "arb", ARROW_RIGHT_BOTTOM,
      "circle", CIRCLE,
      "ban", BAN
  );

  /**
   * 该方法会返回对象中的所有正方形列表，但是会对数组进行深度复制。
   *
   * @return 新的副本。
   */
  @Contract(value = "-> new", pure = true)
  @Override
  public float[][] rectangles() {
    float[][] newArray = new float[rectangles.length][];
    for (int i = 0; i < rectangles.length; i++) {
      newArray[i] = rectangles[i].clone();
    }
    return newArray;
  }

  @Contract(pure = true)
  public boolean isEmpty() {
    return rectangles == EMPTY;
  }

  @Environment(EnvType.CLIENT)
  @Override
  public void drawExtra(TextRenderer textRenderer, MatrixStack matrixStack, VertexConsumerProvider vertexConsumers, int light, float x, float y) {
    int color = textContext.color;
    final float red = (float) (color >> 16 & 0xFF) / 255.0f;
    final float green = (float) (color >> 8 & 0xFF) / 255.0f;
    final float blue = (float) (color & 0xFF) / 255.0f;
    final float alpha = ((color & 0xFC000000) == 0) ? 1 : (float) (color >> 24 & 0xFF) / 255.0f;
    GlyphRenderer glyphRenderer = ((TextRendererAccessor) textRenderer).invokeGetFontStorage(Style.DEFAULT_FONT_ID).getRectangleRenderer();
    final float size = 2;
    final RenderLayer layer = glyphRenderer.getLayer(textContext.outlineColor != -2 ? TextRenderer.TextLayerType.POLYGON_OFFSET : textContext.seeThrough ? TextRenderer.TextLayerType.SEE_THROUGH : TextRenderer.TextLayerType.NORMAL);
    final Matrix4f matrix4f = matrixStack.peek().getPositionMatrix();
    final VertexConsumer vertexConsumer = vertexConsumers.getBuffer(layer);

    // 文本是否存在阴影。
    final boolean shadow = textContext.shadow;
    // 用于文本渲染的矩阵。当存在阴影时，文本渲染需要适当调整。
    for (float[] rectangle : rectangles) {
      final float minX = (rectangle[0] + x) * size;
      final float minY = (rectangle[3] + y) * size;
      final float maxX = (rectangle[2] + x) * size;
      final float maxY = (rectangle[1] + y) * size;
      if (shadow) {
        float g = red * 0.25f;
        float h = green * 0.25f;
        float l = blue * 0.25f;
        glyphRenderer.drawRectangle(
            new GlyphRenderer.Rectangle(minX + 1, minY + 1, maxX + 1, maxY + 1, 0, g, h, l, alpha),
            matrix4f, vertexConsumer, light
        );
      }
      if (textContext.outlineColor != -2) {
        final VertexConsumer vertexConsumerOutline = vertexConsumers.getBuffer(glyphRenderer.getLayer(TextRenderer.TextLayerType.NORMAL));
        int outlineColor = textContext.outlineColor == -1 ? MishangUtils.toSignOutlineColor(color) : textContext.outlineColor;
        float outlineR = (outlineColor >> 16 & 255) / 255f;
        float outlineG = (outlineColor >> 8 & 255) / 255f;
        float outlineB = (outlineColor & 255) / 255f;
        glyphRenderer.drawRectangle(
            new GlyphRenderer.Rectangle(minX - 1, minY + 1, maxX + 1, maxY - 1, 0, outlineR, outlineG, outlineB, alpha),
            matrix4f, vertexConsumerOutline, light
        );

      }
      glyphRenderer.drawRectangle(
          new GlyphRenderer.Rectangle(minX, minY, maxX, maxY, shadow ? 0.24f : textContext.outlineColor != -2 ? 0.02f : 0, red, green, blue, alpha),
          matrix4f, vertexConsumer, light
      );
    }
  }

  @Environment(EnvType.CLIENT)
  @Override
  public float getHeight() {
    return 7;
  }

  @Environment(EnvType.CLIENT)
  @Override
  public float getWidth() {
    return 7;
  }

  @Override
  public String getId() {
    return "pattern";
  }

  @Override
  public TextSpecial cloneWithNewTextContext(@NotNull TextContext textContext) {
    return new PatternTextSpecial(textContext, shapeName, rectangles);
  }

  @Override
  public String describeArgs() {
    return shapeName;
  }

  @Contract(value = "_,_ -> new", pure = true)
  public static PatternTextSpecial fromNbt(TextContext textContext, NbtCompound nbt) {
    final String shapeName = nbt.getString("shapeName");
    return fromName(textContext, shapeName);
  }

  @Override
  public NbtCompound writeNbt(NbtCompound nbt) {
    nbt.putString("shapeName", shapeName);
    return TextSpecial.super.writeNbt(nbt);
  }

  public static PatternTextSpecial fromName(TextContext textContext, String shapeName) {
    return new PatternTextSpecial(textContext, shapeName, NAME_TO_SHAPE.getOrDefault(shapeName, EMPTY));
  }
}
