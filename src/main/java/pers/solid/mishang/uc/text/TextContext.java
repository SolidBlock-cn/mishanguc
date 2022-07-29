package pers.solid.mishang.uc.text;

import com.google.common.collect.ImmutableBiMap;
import com.google.gson.JsonParseException;
import it.unimi.dsi.fastutil.chars.Char2CharArrayMap;
import it.unimi.dsi.fastutil.chars.Char2CharMap;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.nbt.AbstractNbtNumber;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtString;
import net.minecraft.text.LiteralTextContent;
import net.minecraft.text.MutableText;
import net.minecraft.text.OrderedText;
import net.minecraft.text.Text;
import net.minecraft.util.DyeColor;
import net.minecraft.util.Formatting;
import net.minecraft.util.Util;
import net.minecraft.util.math.Quaternion;
import org.jetbrains.annotations.*;
import pers.solid.mishang.uc.MishangUtils;
import pers.solid.mishang.uc.util.HorizontalAlign;
import pers.solid.mishang.uc.util.VerticalAlign;

import java.util.Arrays;

/**
 * 对 {@link net.minecraft.text.Text} 的简单包装与扩展，允许设置对齐属性、尺寸等参数，以便渲染时使用。同时还提供对象与 NBT、JSON 之间的转换。
 */
public class TextContext implements Cloneable {
  /**
   * 用于 {@link #flip()} 方法中，左右替换字符串。
   */
  private static final Char2CharMap flipStringReplacement =
      Util.make(
          new Char2CharArrayMap(),
          map -> {
            map.put('←', '→');
            map.put('→', '←');
            map.put('↖', '↗');
            map.put('↗', '↖');
            map.put('↘', '↙');
            map.put('↙', '↘');
          });

  /**
   * 用于 {@link #flip()} 方法中，替换 {@link PatternTextSpecial} 中的样式。
   */
  @ApiStatus.AvailableSince("0.2.0")
  @Unmodifiable
  private static final ImmutableBiMap<String, String> flipPatternNameReplacement = new ImmutableBiMap.Builder<String, String>()
      .put("al", "ar")
      .put("alt", "art")
      .put("alb", "arb")
      .put("ulb", "urb")
      .put("ult", "urt")
      .build();

  /**
   * 文本内容。该字段对应 NBT 中的两种情况：<br>
   * <ul>
   *   <li>若 NBT 中存在字段 “textJson”，则将其作为原始 JSON 文本进行解析。</li>
   *   <li>否则，直接使用 NBT 字段 “text”，并直接将其作为原始文本（{@link net.minecraft.text.LiteralTextContent}）使用。</li>
   * </ul>
   */
  public @Nullable MutableText text;
  /**
   * 水平对齐方式。若为 null 则取默认值。
   */
  public HorizontalAlign horizontalAlign = HorizontalAlign.CENTER;
  /**
   * 垂直对齐方式。若为 null 则取默认值。
   */
  public VerticalAlign verticalAlign = VerticalAlign.MIDDLE;
  /**
   * 文本颜色。
   */
  public int color = 0xffffff;
  /**
   * 是否渲染阴影。
   */
  public boolean shadow = false;
  /**
   * 是否穿透性渲染。
   */
  public boolean seeThrough = false;
  /**
   * X方向的偏移。
   */
  public float offsetX = 0;
  /**
   * Y方向的偏移。
   */
  public float offsetY = 0;
  /**
   * Z方向的偏移。
   */
  public float offsetZ = 0;
  /**
   * X方向的旋转。
   */
  public float rotationX = 0;
  /**
   * Y方向的旋转。
   */
  public float rotationY = 0;
  /**
   * Z方向的旋转。
   */
  public float rotationZ = 0;

  public float scaleX = 1;
  public float scaleY = 1;
  /**
   * @see net.minecraft.util.Formatting#BOLD
   */
  public boolean bold = false;
  /**
   * @see net.minecraft.util.Formatting#ITALIC
   */
  public boolean italic = false;
  /**
   * @see net.minecraft.util.Formatting#UNDERLINE
   */
  public boolean underline = false;
  /**
   * @see net.minecraft.util.Formatting#STRIKETHROUGH
   */
  public boolean strikethrough = false;
  /**
   * @see net.minecraft.util.Formatting#OBFUSCATED
   */
  public boolean obfuscated = false;
  /**
   * 是否为绝对定位。如果为 <code>false</code>，会按照从上到下的顺序渲染。
   */
  public boolean absolute = false;

  /**
   * 文本大小
   */
  public float size = 8;
  /**
   * 文本的外边框的颜色。<br>
   * 若为 -1，则表示自动渲染。<br>
   * 若为 -2，则表示不渲染。
   */
  @ApiStatus.AvailableSince("0.1.6-mc1.17")
  public int outlineColor = -2;

  /**
   * 该对象的额外渲染对象，如果存在，渲染时则会渲染它。此项通常用于特殊的渲染功能。
   */
  @ApiStatus.AvailableSince("0.2.0")
  public @Nullable TextSpecial extra = null;

  /**
   * <p>该字段用来检测 {@link #bold}、{@link #italic}、{@link #underline}、{@link #strikethrough}、{@link #obfuscated} 是否发生改变。如果发生改变了，则该字段将与 {@code {bold, italic, underline, strikethrough, obfuscated}} 不相等，此时会通过 {@link #reformatText()} 重新更新 {@link #formattedText} 对象，同时更新此字段的值。。
   * <p>请注意，渲染时文本的上述样式是需要考虑的，但不会直接写入 text 字段的值中，因此需要本地创建一个 {@code formattedText}。但如果每一帧都实例化一次 {@link #formattedText} 将消耗大量内存，影响性能，因此只会在样式或者文本被更改时，更新 {@code formattedText}。
   */
  @Environment(EnvType.CLIENT)
  @ApiStatus.AvailableSince("0.2.1")
  private transient boolean[] cachedStyles = null;
  /**
   * 该字段用来检测 {@link #text} 字段是否发生改变。如果发生改变了，则该字段与 {@link #text} 将会不相等，此时将会调用 {@link #reformatText()} 重新生成 {@link #formattedText}，同时将此字段更新为 {@link #text} 的值。
   */
  @Environment(EnvType.CLIENT)
  @ApiStatus.AvailableSince("0.2.1")
  private transient Text cachedText = null;
  /**
   * <p>将 {@link #text} 应用 {@link #bold} 等格式后的文本对象。注意：上述格式并不会直接写入 {@link #text} 对象中。
   * <p>渲染时，会直接使用此对象，而不直接使用 {@link #text} 对象。在每帧渲染时，如果 {@link #text} 或 {@link #bold} 等字段发生改变了，则会调用 {@link #reformatText()} 重新生成此字段的值。请注意：并不是每一帧都这么做，否则将会消耗大量内存。
   */
  @Environment(EnvType.CLIENT)
  @ApiStatus.AvailableSince("0.2.1")
  private transient MutableText formattedText = null;

  /**
   * 从一个 NBT 元素创建一个新的 TextContext 对象，并使用默认值。
   *
   * @param nbt NBT 复合标签或者字符串。
   * @return 新的 TextContext 对象。
   */
  public static @NotNull TextContext fromNbt(NbtElement nbt) {
    return fromNbt(nbt, new TextContext());
  }

  /**
   * 从一个 NBT 元素创建一个新的 TextContext 对象，并使用指定的默认值。该默认值用于，在没有标签时如何处理。
   *
   * @param nbt      NBT 复合标签或者字符串。
   * @param defaults 一个默认的 TextContext 对象。在内部会被复制一次。该对象内的属性会被使用。
   * @return 新的 TextContext 对象。
   */
  public static @NotNull TextContext fromNbt(NbtElement nbt, TextContext defaults) {
    final TextContext textContext = defaults.clone();
    if (nbt instanceof NbtString) {
      textContext.text = Text.literal(nbt.asString());
    } else if (nbt instanceof NbtCompound nbtCompound) {
      textContext.readNbt(nbtCompound);
    }
    return textContext;
  }

  private static void putBooleanParam(NbtCompound nbt, String name, boolean value) {
    if (value) {
      nbt.putBoolean(name, true);
    } else {
      nbt.remove(name);
    }
  }

  @Override
  public boolean equals(Object o) {
    return super.equals(o);
  }

  /**
   * 从一个 NBT 复合标签中读取数据，写入当前的 textContext 中。
   *
   * @param nbt NBT 复合标签。
   */
  public void readNbt(@NotNull NbtCompound nbt) {
    final @Nullable NbtElement nbtText = nbt.get("text");
    final String textJson = nbt.getString("textJson");
    if (!textJson.isEmpty()) {
      try {
        text = Text.Serializer.fromLenientJson(textJson);
      } catch (JsonParseException e) {
        text = Text.translatable("message.mishanguc.invalid_json", e.getMessage());
      }
    } else if (nbtText instanceof NbtString) {
      text = Text.literal(nbtText.asString());
    } else {
      text = null;
    }
    horizontalAlign = HorizontalAlign.byName(nbt.getString("horizontalAlign"));
    if (horizontalAlign == null) {
      horizontalAlign = HorizontalAlign.CENTER;
    }
    verticalAlign = VerticalAlign.byName(nbt.getString("verticalAlign"));
    if (verticalAlign == null) {
      verticalAlign = VerticalAlign.MIDDLE;
    }
    final NbtElement nbtColor = nbt.get("color");
    if (nbtColor instanceof final AbstractNbtNumber abstractNbtNumber) {
      color = abstractNbtNumber.intValue();
    } else if (nbtColor instanceof NbtString) {
      final @Nullable DyeColor dyeColor = DyeColor.byName(nbtColor.asString(), null);
      if (dyeColor != null) {
        color = dyeColor.getSignColor();
      }
    }
    final NbtElement nbtOutlineColor = nbt.get("outlineColor");
    if (nbtOutlineColor instanceof final AbstractNbtNumber abstractNbtNumber) {
      outlineColor = abstractNbtNumber.intValue();
    } else if (nbtOutlineColor instanceof NbtString) {
      final @Nullable DyeColor dyeColor = DyeColor.byName(nbtOutlineColor.asString(), null);
      if (dyeColor != null) {
        outlineColor = MishangUtils.COLOR_TO_OUTLINE_COLOR.get(dyeColor);
      }
    }
    shadow = nbt.getBoolean("shadow");
    seeThrough = nbt.getBoolean("seeThrough");
    offsetX = nbt.getFloat("offsetX");
    offsetY = nbt.getFloat("offsetY");
    offsetZ = nbt.getFloat("offsetZ");
    rotationX = nbt.getFloat("rotationX");
    rotationY = nbt.getFloat("rotationY");
    rotationZ = nbt.getFloat("rotationZ");
    scaleX = nbt.getFloat("scaleX");
    if (scaleX == 0) {
      scaleX = 1;
    }
    scaleY = nbt.getFloat("scaleY");
    if (scaleY == 0) {
      scaleY = 1;
    }
    size = nbt.contains("size", 99) ? nbt.getFloat("size") : size;
    bold = nbt.getBoolean("bold");
    italic = nbt.getBoolean("italic");
    underline = nbt.getBoolean("underline");
    strikethrough = nbt.getBoolean("strikethrough");
    obfuscated = nbt.getBoolean("obfuscated");
    absolute = nbt.getBoolean("absolute");

    extra = nbt.contains("extra", NbtElement.COMPOUND_TYPE) ? TextSpecial.fromNbt(this, nbt.getCompound("extra")) : null;
  }

  @Environment(EnvType.CLIENT)
  @Contract(pure = true)
  public void draw(TextRenderer textRenderer, MatrixStack matrixStack, VertexConsumerProvider vertexConsumers, int light, float width, float height) {
    if (text == null && extra == null) {
      return;
    }
    if (!Arrays.equals(cachedStyles, new boolean[]{bold, italic, underline, strikethrough, obfuscated}) || text != cachedText) {
      reformatText();
    }
    final OrderedText orderedText = formattedText == null ? null : formattedText.asOrderedText();

    matrixStack.push();

    // 处理文本的 offset
    matrixStack.translate(offsetX, offsetY, offsetZ);
    // 处理文本的旋转
    if (rotationX != 0 || rotationY != 0 || rotationZ != 0)
      matrixStack.multiply(new Quaternion(rotationX, rotationY, rotationZ, true));
    float x = 0;
    switch (horizontalAlign == null ? HorizontalAlign.CENTER : horizontalAlign) {
      case LEFT -> matrixStack.translate(-width / 2, 0, 0);
      case CENTER -> x = -getWidth(textRenderer, orderedText) / 2f;
      case RIGHT -> {
        matrixStack.translate(width / 2, 0, 0);
        x = -getWidth(textRenderer, orderedText);
      }
      default -> throw new IllegalStateException("Unexpected value: " + horizontalAlign);
    }
    float y = 0;
    switch (verticalAlign == null ? VerticalAlign.MIDDLE : verticalAlign) {
      case TOP -> matrixStack.translate(0, -height / 2, 0);
      case MIDDLE -> y = extra == null ? -4 : -extra.getHeight() / 2;
      case BOTTOM -> {
        matrixStack.translate(0, height / 2, 0);
        y = extra == null ? -8 : -extra.getHeight();
      }
      default -> throw new IllegalStateException("Unexpected value: " + verticalAlign);
    }

    // 处理文本的 scale
    matrixStack.scale(size / 16f, size / 16f, size / 16f);
    matrixStack.scale(scaleX, scaleY, 1);

    // 执行渲染
    if (orderedText != null) {
      drawText(textRenderer, matrixStack, vertexConsumers, light, orderedText, x, y);
    }
    if (extra != null) {
      extra.drawExtra(textRenderer, matrixStack, vertexConsumers, light, x, y);
    }
    matrixStack.pop();
  }

  /**
   * 当检测到文本对象的 bold、italic、underline、strikethrough 和 obfuscated 有更新，或者 text 字段被改变之后，重新设置该文本的 formattedText 对象。<p>
   * 此前的版本的做法是，每渲染一次都产生一次 formattedText，这种做法有非常大的问题，因为每一帧都要产生对象，并将文本 order 一次。事实上，如果文本或者样式没有改变，那么 formattedText 就不需要更换，其 orderedText 也可以直接使用。<p>
   * 此方法只会在渲染（{@link #draw}）时调用，并且不会每一帧都调用。
   */
  @Environment(EnvType.CLIENT)
  @ApiStatus.AvailableSince("0.2.1")
  private void reformatText() {
    if (text == null) {
      cachedText = null;
      formattedText = null;
      return;
    }
    cachedText = text;
    formattedText = text.copy();

    if (bold) {
      formattedText.formatted(Formatting.BOLD);
    }
    if (italic) {
      formattedText.formatted(Formatting.ITALIC);
    }
    if (underline) {
      formattedText.formatted(Formatting.UNDERLINE);
    }
    if (strikethrough) {
      formattedText.formatted(Formatting.STRIKETHROUGH);
    }
    if (obfuscated) {
      formattedText.formatted(Formatting.OBFUSCATED);
    }
    cachedStyles = new boolean[]{bold, italic, underline, strikethrough, obfuscated};
  }

  /**
   * 获取文本宽度，如果存在 extra 字段，则还需要考虑该对象的宽度。
   */
  private float getWidth(TextRenderer textRenderer, @Nullable OrderedText text) {
    final int width = text == null ? 0 : textRenderer.getWidth(text);
    return extra != null ? Math.max(width, extra.getWidth()) : width;
  }

  @Environment(EnvType.CLIENT)
  @Contract(pure = true)
  protected void drawText(TextRenderer textRenderer, MatrixStack matrixStack, VertexConsumerProvider vertexConsumers, int light, OrderedText text, float x, float y) {
    if (outlineColor == -2) {
      textRenderer.draw(text, x, y, color, shadow, matrixStack.peek().getPositionMatrix(), vertexConsumers, seeThrough, 0, light);
    } else {
      textRenderer.drawWithOutline(text, x, y, color, outlineColor == -1 ? MishangUtils.toSignOutlineColor(color) : outlineColor, matrixStack.peek().getPositionMatrix(), vertexConsumers, light);
    }
  }

  /**
   * 将文本的数据写入 NBT 中。
   *
   * @param nbt 一个待写入的 NBT 复合标签，可以是空的 NBT 复合标签：
   *            <pre>{@code  new NbtCompound()}</pre>
   * @return 修改后的 <tt>nbt</tt>。
   */
  public NbtCompound writeNbt(NbtCompound nbt) {
    if (text != null) {
      if (text.getContent() instanceof LiteralTextContent literalTextContent && text.getSiblings().isEmpty() && text.getStyle().isEmpty()) {
        nbt.putString("text", literalTextContent.string());
      } else {
        nbt.putString("textJson", Text.Serializer.toJson(text));
      }
    } else {
      nbt.remove("text");
    }
    if (horizontalAlign != HorizontalAlign.CENTER) {
      nbt.putString("horizontalAlign", horizontalAlign.asString());
    } else {
      nbt.remove("horizontalAlign");
    }
    if (verticalAlign != VerticalAlign.MIDDLE) {
      nbt.putString("verticalAlign", verticalAlign.asString());
    } else {
      nbt.remove("verticalAlign");
    }
    nbt.putInt("color", color);
    if (outlineColor != -2) {
      nbt.putInt("outlineColor", outlineColor);
    } else {
      nbt.remove("outlineColor");
    }
    putBooleanParam(nbt, "shadow", shadow);
    putBooleanParam(nbt, "seeThrough", seeThrough);
    nbt.putFloat("size", size);
    if (offsetX != 0) {
      nbt.putFloat("offsetX", offsetX);
    } else {
      nbt.remove("offsetX");
    }
    if (offsetY != 0) {
      nbt.putFloat("offsetY", offsetY);
    } else {
      nbt.remove("offsetY");
    }
    if (offsetZ != 0) {
      nbt.putFloat("offsetZ", offsetZ);
    } else {
      nbt.remove("offsetZ");
    }
    if (rotationX != 0) nbt.putFloat("rotationX", rotationX);
    else nbt.remove("rotationX");
    if (rotationY != 0) nbt.putFloat("rotationY", rotationY);
    else nbt.remove("rotationX");
    if (rotationZ != 0) nbt.putFloat("rotationZ", rotationZ);
    else nbt.remove("rotationX");
    if (scaleX != 1) {
      nbt.putFloat("scaleX", scaleX);
    } else {
      nbt.remove("scaleX");
    }
    if (scaleY != 1) {
      nbt.putFloat("scaleY", scaleY);
    } else {
      nbt.remove("scaleY");
    }
    putBooleanParam(nbt, "bold", bold);
    putBooleanParam(nbt, "italic", italic);
    putBooleanParam(nbt, "underline", underline);
    putBooleanParam(nbt, "strikethrough", strikethrough);
    putBooleanParam(nbt, "obfuscated", obfuscated);
    putBooleanParam(nbt, "absolute", absolute);

    if (extra == null) {
      nbt.remove("extra");
    } else {
      nbt.put("extra", extra.writeNbt(new NbtCompound()));
    }
    return nbt;
  }

  @Override
  public TextContext clone() {
    try {
      final TextContext clone = (TextContext) super.clone();
      clone.extra = clone.extra != null ? clone.extra.cloneWithNewTextContext(clone) : null;
      return clone;
    } catch (CloneNotSupportedException e) {
      throw new AssertionError();
    }
  }

  public @Nullable MutableText asStyledText() {
    if (text == null) return null;
    final MutableText text = this.text.copy();
    if (bold) text.formatted(Formatting.BOLD);
    if (italic) text.formatted(Formatting.ITALIC);
    if (underline) text.formatted(Formatting.UNDERLINE);
    if (strikethrough) text.formatted(Formatting.STRIKETHROUGH);
    if (obfuscated) text.formatted(Formatting.OBFUSCATED);
    text.styled(style -> style.withColor(color));
    return text;
  }

  @Contract(mutates = "this")
  public TextContext flip() {
    offsetX = -offsetX;
    switch (horizontalAlign) {
      case LEFT -> horizontalAlign = HorizontalAlign.RIGHT;
      case RIGHT -> horizontalAlign = HorizontalAlign.LEFT;
    }
    if (text.getContent() instanceof final LiteralTextContent literalTextContent) {
      final String rawString = literalTextContent.string();
      final StringBuilder stringBuilder = new StringBuilder(rawString);
      for (int i = 0; i < stringBuilder.length(); i++) {
        final char c = stringBuilder.charAt(i);
        if (flipStringReplacement.containsKey(c)) {
          stringBuilder.setCharAt(i, flipStringReplacement.get(c));
        }
      }
      text = Text.literal(stringBuilder.toString());
    }
    if (extra instanceof final PatternTextSpecial patternTextSpecial) {
      final String shapeName = patternTextSpecial.shapeName();
      if (flipPatternNameReplacement.containsKey(shapeName)) {
        extra = PatternTextSpecial.fromName(this, flipPatternNameReplacement.get(shapeName));
      } else if (flipPatternNameReplacement.inverse().containsKey(shapeName)) {
        extra = PatternTextSpecial.fromName(this, flipPatternNameReplacement.inverse().get(shapeName));
      }
    }
    return this;
  }
}
