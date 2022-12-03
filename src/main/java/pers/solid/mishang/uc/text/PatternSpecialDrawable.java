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
import net.minecraft.text.MutableText;
import net.minecraft.text.Style;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Unmodifiable;
import org.joml.Matrix4f;
import pers.solid.mishang.uc.MishangUtils;
import pers.solid.mishang.uc.mixin.TextRendererAccessor;

import java.util.Map;

public record PatternSpecialDrawable(TextContext textContext, String shapeName, @Unmodifiable float[][] rectangles) implements SpecialDrawable {
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

  private static final float[][] ARROW_TOP =
      {
          // 箭头的中间部分：
          {3, 0, 4, 7},
          // 箭头的左边：
          {2, 1, 3, 2},
          {1, 2, 3, 3},
          // 箭头的右边：
          {4, 1, 5, 2},
          {4, 2, 6, 3},
      };
  private static final float[][] ARROW_BOTTOM =
      {
          // 箭头的中间部分：
          {3, 0, 4, 7},
          // 箭头的左边：
          {2, 5, 3, 6},
          {1, 4, 3, 5},
          // 箭头的右边：
          {4, 5, 5, 6},
          {4, 4, 6, 5},
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
          {5, 5, 6, 6},
          {6, 6, 7, 7}
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
          {1, 5, 2, 6},
          {0, 6, 1, 7}
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
          {5, 1, 6, 2},
          {6, 0, 7, 1}
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
          {1, 1, 2, 2},
          {0, 0, 1, 1}
      };
  private static final float[][] CIRCLE = {
      // 自 0.2.1，略微调大了尺寸
      // slightly enlarged since 0.2.1
      {2, 0, 5, 1},
      {2, 6, 5, 7},
      {0, 2, 1, 5},
      {6, 2, 7, 5},
      {1, 1, 2, 2},
      {5, 1, 6, 2},
      {1, 5, 2, 6},
      {5, 5, 6, 6}
  };
  private static final float[][] BAN = {
      // 自 0.2.1，略微调大了尺寸
      // slightly enlarged since 0.2.1
      {2, 0, 5, 1},
      {2, 6, 5, 7},
      {0, 2, 1, 5},
      {6, 2, 7, 5},
      {1, 1, 2, 2},
      {5, 1, 6, 2},
      {1, 5, 2, 6},
      {5, 5, 6, 6},
      {2, 2, 3, 3},
      {3, 3, 4, 4},
      {4, 4, 5, 5}
  };

  @ApiStatus.AvailableSince("0.2.2")
  private static final float[][] U_TURN_LEFT_BOTTOM = {
      {3, 0, 6, 1},
      {6, 1, 7, 6},
      {2, 1, 3, 7},
      {0, 4, 1, 5},
      {1, 5, 2, 6},
      {4, 4, 5, 5},
      {3, 5, 4, 6}
  };
  @ApiStatus.AvailableSince("0.2.2")
  private static final float[][] U_TURN_RIGHT_BOTTOM = flipLeftRight(U_TURN_LEFT_BOTTOM);
  @ApiStatus.AvailableSince("0.2.2")
  private static final float[][] U_TURN_LEFT_TOP = flipUpDown(U_TURN_LEFT_BOTTOM);
  @ApiStatus.AvailableSince("0.2.2")
  private static final float[][] U_TURN_RIGHT_TOP = flipUpDown(U_TURN_RIGHT_BOTTOM);
  @ApiStatus.AvailableSince("0.2.2")
  private static final float[][] CROSS = {
      {1, 1, 2, 2},
      {2, 2, 3, 3},
      {3, 3, 4, 4},
      {4, 4, 5, 5},
      {5, 5, 6, 6},
      {4, 2, 5, 3},
      {5, 1, 6, 2},
      {2, 4, 3, 5},
      {1, 5, 2, 6}
  };

  private static final @Unmodifiable Map<String, float[][]> NAME_TO_SHAPE = new ImmutableMap.Builder<String, float[][]>()
      .put("al", ARROW_LEFT)
      .put("ar", ARROW_RIGHT)
      .put("at", ARROW_TOP)
      .put("ab", ARROW_BOTTOM)
      .put("alt", ARROW_LEFT_TOP)
      .put("art", ARROW_RIGHT_TOP)
      .put("alb", ARROW_LEFT_BOTTOM)
      .put("arb", ARROW_RIGHT_BOTTOM)
      .put("circle", CIRCLE)
      .put("ban", BAN)
      .put("ulb", U_TURN_LEFT_BOTTOM)
      .put("urb", U_TURN_RIGHT_BOTTOM)
      .put("ult", U_TURN_LEFT_TOP)
      .put("urt", U_TURN_RIGHT_TOP)
      .put("cross", CROSS)
      .build();

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
    //noinspection resource
    GlyphRenderer glyphRenderer = ((TextRendererAccessor) textRenderer).invokeGetFontStorage(Style.DEFAULT_FONT_ID).getRectangleRenderer();
    final float sizeMultiplier = 1;
    final RenderLayer layer = glyphRenderer.getLayer(textContext.outlineColor != -2 ? TextRenderer.TextLayerType.POLYGON_OFFSET : textContext.seeThrough ? TextRenderer.TextLayerType.SEE_THROUGH : TextRenderer.TextLayerType.NORMAL);
    final Matrix4f matrix4f = matrixStack.peek().getPositionMatrix();
    final VertexConsumer vertexConsumer = vertexConsumers.getBuffer(layer);

    // 文本是否存在阴影。
    final boolean shadow = textContext.shadow;
    // 用于文本渲染的矩阵。当存在阴影时，文本渲染需要适当调整。
    for (float[] rectangle : rectangles) {
      final float minX = (rectangle[0] + x) * sizeMultiplier;
      final float minY = (rectangle[3] + y) * sizeMultiplier;
      final float maxX = (rectangle[2] + x) * sizeMultiplier;
      final float maxY = (rectangle[1] + y) * sizeMultiplier;
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
    return new PatternSpecialDrawable(textContext, shapeName, rectangles);
  }

  @Override
  public String asStringArgs() {
    return shapeName;
  }

  @Contract(value = "_,_ -> new", pure = true)
  public static PatternSpecialDrawable fromNbt(TextContext textContext, NbtCompound nbt) {
    final String shapeName = nbt.getString("shapeName");
    return fromName(textContext, shapeName);
  }

  @Override
  public void writeNbt(NbtCompound nbt) {
    SpecialDrawable.super.writeNbt(nbt);
    nbt.putString("shapeName", shapeName);
  }

  public static PatternSpecialDrawable fromName(TextContext textContext, String shapeName) {
    return new PatternSpecialDrawable(textContext, shapeName, NAME_TO_SHAPE.getOrDefault(shapeName, EMPTY));
  }

  @ApiStatus.AvailableSince("0.2.1")
  @Contract(value = "_ -> new", pure = true)
  private static float[][] flipLeftRight(float[][] original) {
    final float[][] flipped = new float[original.length][];
    for (int i = 0; i < original.length; i++) {
      final float[] originalPiece = original[i];
      flipped[i] = new float[]{7 - originalPiece[2], originalPiece[1], 7 - originalPiece[0], originalPiece[3]};
    }
    return flipped;
  }

  @ApiStatus.AvailableSince("0.2.1")
  @Contract(value = "_ -> new", pure = true)
  private static float[][] flipUpDown(float[][] original) {
    final float[][] flipped = new float[original.length][];
    for (int i = 0; i < original.length; i++) {
      final float[] originalPiece = original[i];
      flipped[i] = new float[]{originalPiece[0], 7 - originalPiece[3], originalPiece[2], 7 - originalPiece[1]};
    }
    return flipped;
  }

  @ApiStatus.AvailableSince("0.2.1")
  @Contract(value = "_ -> new", pure = true)
  private static float[][] flipAll(float[][] original) {
    final float[][] flipped = new float[original.length][];
    for (int i = 0; i < original.length; i++) {
      final float[] originalPiece = original[i];
      flipped[i] = new float[]{7 - originalPiece[2], 7 - originalPiece[3], 7 - originalPiece[0], 7 - originalPiece[1]};
    }
    return flipped;
  }

  @Override
  public @NotNull MutableText asStyledText() {
    return SpecialDrawable.super.asStyledText().styled(style -> style.withColor(textContext.color));
  }
}
