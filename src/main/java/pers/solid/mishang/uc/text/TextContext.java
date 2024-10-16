package pers.solid.mishang.uc.text;

import com.google.common.collect.ImmutableBiMap;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonParser;
import com.google.gson.stream.JsonReader;
import com.mojang.serialization.Codec;
import com.mojang.serialization.JsonOps;
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
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.text.*;
import net.minecraft.util.Formatting;
import net.minecraft.util.Util;
import net.minecraft.util.math.MathHelper;
import org.jetbrains.annotations.*;
import org.joml.Quaternionf;
import pers.solid.mishang.uc.MishangUtils;
import pers.solid.mishang.uc.util.HorizontalAlign;
import pers.solid.mishang.uc.util.TextBridge;
import pers.solid.mishang.uc.util.VerticalAlign;

import java.io.StringReader;
import java.util.Arrays;
import java.util.Collection;

/**
 * 对 {@link net.minecraft.text.Text} 的简单包装与扩展，允许设置对齐属性、尺寸等参数，以便渲染时使用。同时还提供对象与 NBT、JSON 之间的转换。
 */
public class TextContext implements Cloneable {
  public static final Codec<TextContext> CODEC = NbtCompound.CODEC.xmap(nbtCompound -> TextContext.fromNbt(nbtCompound, null), textContext -> textContext.createNbt(null));
  public static final PacketCodec<RegistryByteBuf, TextContext> PACKET_CODEC = PacketCodec.of((value, buf) -> buf.writeNbt(value.createNbt(buf.getRegistryManager())), buf -> TextContext.fromNbt(buf.readNbt(), buf.getRegistryManager()));

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
   * 用于 {@link #flip()} 方法中，替换 {@link PatternSpecialDrawable} 中的样式。
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
   *   <li>否则，直接使用 NBT 字段 “text”，并直接将其作为原始文本（{@link net.minecraft.text.PlainTextContent}）使用。</li>
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
   * 是否为绝对定位。
   *
   * @see MishangUtils#rearrange(Collection)
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
   * 该对象的特殊渲染内容，如果存在，渲染时则会渲染它。此项通常用于特殊的渲染功能。
   */
  @ApiStatus.AvailableSince("0.2.0")
  public @Nullable SpecialDrawable extra = null;

  /**
   * <p>该字段用来检测 {@link #bold}、{@link #italic}、{@link #underline}、{@link #strikethrough}、{@link #obfuscated} 是否发生改变。如果发生改变了，则该字段将与 {@code {bold, italic, underline, strikethrough, obfuscated}} 不相等，此时会通过 {@link #reformatText()} 重新更新 {@link #formattedText} 对象，同时更新此字段的值。。
   * <p>请注意，渲染时文本的上述样式是需要考虑的，但不会直接写入 text 字段的值中，因此需要本地创建一个 {@code formattedText}。但如果每一帧都实例化一次 {@link #formattedText} 将消耗大量内存，影响性能，因此只会在样式或者文本被更改时，更新 {@code formattedText}。
   */
  @ApiStatus.AvailableSince("0.2.1")
  private transient boolean[] cachedStyles = null;
  /**
   * 该字段用来检测 {@link #text} 字段是否发生改变。如果发生改变了，则该字段与 {@link #text} 将会不相等，此时将会调用 {@link #reformatText()} 重新生成 {@link #formattedText}，同时将此字段更新为 {@link #text} 的值。
   */
  @ApiStatus.AvailableSince("0.2.1")
  private transient Text cachedText = null;
  /**
   * <p>将 {@link #text} 应用 {@link #bold} 等格式后的文本对象。注意：上述格式并不会直接写入 {@link #text} 对象中。
   * <p>渲染时，会直接使用此对象，而不直接使用 {@link #text} 对象。在每帧渲染时，如果 {@link #text} 或 {@link #bold} 等字段发生改变了，则会调用 {@link #reformatText()} 重新生成此字段的值。请注意：并不是每一帧都这么做，否则将会消耗大量内存。
   */
  @ApiStatus.AvailableSince("0.2.1")
  private transient MutableText formattedText = null;

  /**
   * 从一个 NBT 元素创建一个新的 TextContext 对象，并使用默认值。
   *
   * @param nbt NBT 复合标签或者字符串。
   * @return 新的 TextContext 对象。
   */
  public static @NotNull TextContext fromNbt(NbtElement nbt, RegistryWrapper.WrapperLookup registryLookup) {
    return fromNbt(nbt, new TextContext(), registryLookup);
  }

  /**
   * 从一个 NBT 元素创建一个新的 TextContext 对象，并使用指定的默认值。该默认值用于，在没有标签时如何处理。
   *
   * @param nbt      NBT 复合标签或者字符串。
   * @param defaults 一个默认的 TextContext 对象。该对象的值会直接修改。
   * @return 新的 TextContext 对象。
   */
  @Contract(value = "_, _, _ -> param2", mutates = "param2")
  public static @NotNull TextContext fromNbt(NbtElement nbt, TextContext defaults, RegistryWrapper.WrapperLookup registryLookup) {
    if (nbt instanceof NbtString || nbt instanceof AbstractNbtNumber) {
      defaults.text = TextBridge.literal(nbt.asString());
    } else if (nbt instanceof NbtCompound nbtCompound) {
      defaults.readNbt(nbtCompound, registryLookup);
    }
    return defaults;
  }

  private static void putBooleanParam(NbtCompound nbt, String name, boolean value) {
    if (value) {
      nbt.putBoolean(name, true);
    } else {
      nbt.remove(name);
    }
  }

  /**
   * 从一个 NBT 复合标签中读取数据，写入当前的 textContext 中。
   *
   * @param nbt NBT 复合标签。
   */
  @Contract(mutates = "this")
  public void readNbt(@NotNull NbtCompound nbt, RegistryWrapper.@Nullable WrapperLookup registryLookup) {
    final @Nullable NbtElement nbtText = nbt.get("text");
    final String textJson = nbt.getString("textJson");
    if (!textJson.isEmpty()) {
      try {
        if (registryLookup == null) {
          JsonReader jsonReader = new JsonReader(new StringReader(textJson));
          jsonReader.setLenient(true);
          JsonElement jsonElement = JsonParser.parseReader(jsonReader);
          text = (MutableText) TextCodecs.CODEC.parse(JsonOps.INSTANCE, jsonElement).getOrThrow();
        } else {
          text = Text.Serialization.fromLenientJson(textJson, registryLookup);
        }
      } catch (JsonParseException e) {
        text = TextBridge.translatable("message.mishanguc.invalid_json", e.getMessage());
      }
    } else if (nbtText instanceof NbtString) {
      text = TextBridge.literal(nbtText.asString());
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
    if (nbt.contains("color")) {
      color = MishangUtils.readColorFromNbtElement(nbt.get("color"));
    }
    if (nbt.contains("outlineColor")) {
      outlineColor = MishangUtils.readColorFromNbtElement(nbt.get("outlineColor"));
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

    extra = nbt.contains("extra", NbtElement.COMPOUND_TYPE) ? SpecialDrawable.fromNbt(this, nbt.getCompound("extra")) : null;
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

    // 处理文本的偏移
    matrixStack.translate(offsetX, offsetY, offsetZ);
    // 处理文本的旋转
    if (rotationX != 0 || rotationY != 0 || rotationZ != 0) {
      matrixStack.multiply(new Quaternionf().rotateXYZ(MathHelper.RADIANS_PER_DEGREE * rotationX, MathHelper.RADIANS_PER_DEGREE * rotationY, MathHelper.RADIANS_PER_DEGREE * rotationZ));
    }

    // 处理文本在 x 和 y 方向的对齐
    float x = 0;
    switch (horizontalAlign == null ? HorizontalAlign.CENTER : horizontalAlign) {
      case LEFT -> matrixStack.translate(-width / 2, 0, 0);
      case CENTER -> matrixStack.translate(-getWidth(textRenderer, orderedText) / 4, 0, 0);
      case RIGHT -> matrixStack.translate(width / 2 - getWidth(textRenderer, orderedText) / 2, 0, 0);
      default -> throw new IllegalStateException("Unexpected value: " + horizontalAlign);
    }
    float y = 0;
    switch (verticalAlign == null ? VerticalAlign.MIDDLE : verticalAlign) {
      case TOP -> matrixStack.translate(0, -height / 2, 0);
      case MIDDLE -> matrixStack.translate(0, -getHeight() / 4, 0);
      case BOTTOM -> matrixStack.translate(0, height / 2 - getHeight() / 2, 0);
      default -> throw new IllegalStateException("Unexpected value: " + verticalAlign);
    }

    // 处理文本的大小，这个大小对文本自身以及 {@link #extra} 都是有效的。
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
    final float width = text == null ? 0 : textRenderer.getWidth(text) * size / 8 * scaleX;
    return extra != null ? Math.max(width, extra.width() * size * scaleX) : width;
  }

  public float getHeight() {
    return (extra != null ? extra.height() * size : size) * scaleY;
  }

  public float getMarginTop() {
    return extra != null ? 0 : size / 8f;
  }

  @Environment(EnvType.CLIENT)
  @Contract(pure = true)
  protected void drawText(TextRenderer textRenderer, MatrixStack matrixStack, VertexConsumerProvider vertexConsumers, int light, OrderedText text, float x, float y) {
    if (outlineColor == -2) {
      textRenderer.draw(text, x, y, color, shadow, matrixStack.peek().getPositionMatrix(), vertexConsumers, seeThrough ? TextRenderer.TextLayerType.SEE_THROUGH : TextRenderer.TextLayerType.NORMAL, 0, light);
    } else {
      textRenderer.drawWithOutline(text, x, y, color, outlineColor == -1 ? MishangUtils.toSignOutlineColor(color) : outlineColor, matrixStack.peek().getPositionMatrix(), vertexConsumers, light);
    }
  }

  /**
   * 将文本的数据写入 NBT 中。
   *
   * @param nbt 一个待写入的 NBT 复合标签，可以是空的 NBT 复合标签：
   *            <pre>{@code  new NbtCompound()}</pre>
   */
  @Contract(mutates = "param1")
  public void writeNbt(@NotNull NbtCompound nbt, RegistryWrapper.WrapperLookup registryLookup) {
    if (text != null) {
      if (text.getContent() instanceof PlainTextContent plainTextContent && text.getSiblings().isEmpty() && text.getStyle().isEmpty()) {
        nbt.putString("text", plainTextContent.string());
      } else {
        nbt.putString("textJson", TextCodecs.CODEC.encodeStart(JsonOps.INSTANCE, text).getOrThrow(JsonParseException::new).toString());
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
      nbt.put("extra", extra.createNbt(registryLookup));
    }
  }

  @Contract("_ -> new")
  public final NbtCompound createNbt(RegistryWrapper.WrapperLookup registryLookup) {
    final NbtCompound nbtCompound = new NbtCompound();
    writeNbt(nbtCompound, registryLookup);
    return nbtCompound;
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

  public @NotNull MutableText asStyledText() {
    final MutableText text = this.text == null ? TextBridge.empty() : this.text.copy();
    if (bold) text.formatted(Formatting.BOLD);
    if (italic) text.formatted(Formatting.ITALIC);
    if (underline) text.formatted(Formatting.UNDERLINE);
    if (strikethrough) text.formatted(Formatting.STRIKETHROUGH);
    if (obfuscated) text.formatted(Formatting.OBFUSCATED);
    if (text.getStyle().getColor() == null) {
      text.styled(style -> style.withColor(color));
    }
    if (extra != null) {
      return extra.asStyledText();
    }
    return text;
  }

  @Contract(mutates = "this")
  public TextContext flip() {
    offsetX = -offsetX;
    horizontalAlign = horizontalAlign.flip();
    if (text != null && text.getContent() instanceof final PlainTextContent plainTextContent) {
      final String rawString = plainTextContent.string();
      final StringBuilder stringBuilder = new StringBuilder(rawString);
      for (int i = 0; i < stringBuilder.length(); i++) {
        final char c = stringBuilder.charAt(i);
        if (flipStringReplacement.containsKey(c)) {
          stringBuilder.setCharAt(i, flipStringReplacement.get(c));
        }
      }
      text = TextBridge.literal(stringBuilder.toString());
    }
    if (extra instanceof final PatternSpecialDrawable patternTextSpecial) {
      final String shapeName = patternTextSpecial.shapeName();
      if (flipPatternNameReplacement.containsKey(shapeName)) {
        extra = PatternSpecialDrawable.fromName(this, flipPatternNameReplacement.get(shapeName));
      } else if (flipPatternNameReplacement.inverse().containsKey(shapeName)) {
        extra = PatternSpecialDrawable.fromName(this, flipPatternNameReplacement.inverse().get(shapeName));
      }
    }
    return this;
  }
}
