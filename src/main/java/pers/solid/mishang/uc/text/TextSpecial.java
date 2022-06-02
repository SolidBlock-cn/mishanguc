package pers.solid.mishang.uc.text;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.nbt.NbtCompound;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * TextSpecial 是一个 TextContext 中可能存在的一部分，用于进行特殊的渲染。可以通过 {@link #fromNbt} 与 {@link #writeNbt} 实现 TextSpecial 与 TextContext 中的转换。当 TextContext 中的 TextSpecial 字段不为 {@code null} 时，渲染时就会使用 {@link TextSpecial#drawExtra}。<p>
 * 注意每一个 TextSpecial 的 {@link #drawExtra} 方法通常都应该照顾 x、y 以及 {@link TextContext#size}（若有）等参数。
 */
public interface TextSpecial extends Cloneable {
  /**
   * 表示一个无效的 TextSpecial 对象，通常用于解析无效的情况。
   */
  TextSpecial INVALID = new TextSpecial() {
    @Override
    public void drawExtra(TextRenderer textRenderer, MatrixStack matrixStack, VertexConsumerProvider vertexConsumers, int light, float x, float y) {
    }

    @Override
    public String getId() {
      return "invalid";
    }

    @Override
    public TextSpecial cloneWithNewTextContext(@NotNull TextContext textContext) {
      return INVALID;
    }
  };

  /**
   * 绘制该对象。在 {@link TextContext#draw} 中，如果 {@link TextContext#extra} 不为 {@code null}，则会使用这个方法。
   */
  @Environment(EnvType.CLIENT)
  void drawExtra(TextRenderer textRenderer, MatrixStack matrixStack, VertexConsumerProvider vertexConsumers, int light, float x, float y);

  /**
   * 这一类 TextSpecial 对象的 id，通常是一个字符串，并且应该要被 {@link #fromNbt} 和 {@link #fromDescription} 识别。同一类对象返回的 id 应该相同，因此覆盖此方法时，通常是返回一个常量。
   *
   * @return 这一类 TextSpecial 对象的 id。
   */
  @Contract(pure = true)
  String getId();

  @Contract("_ -> param1")
  default NbtCompound writeNbt(NbtCompound nbt) {
    nbt.putString("id", getId());
    return nbt;
  }

  /**
   * 根据已有的 TextContext 对象和一段 nbt，返回一个新的 TextSpecial 对象。通常来说，先识别该 nbt 中的 {@code id} 标签（参见 {@link #getId()}），然后再形成对应的对象。
   */
  @Contract("_, _ -> new")
  static @Nullable TextSpecial fromNbt(TextContext textContext, @NotNull NbtCompound nbt) {
    final String id = nbt.getString("id");
    if (id == null || id.isEmpty()) {
      return null;
    } else if (id.equals("rect")) {
      final RectTextSpecial rectTextSpecial = new RectTextSpecial(textContext);
      rectTextSpecial.readNbt(nbt);
      return rectTextSpecial;
    } else if (id.equals("pattern")) {
      return PatternTextSpecial.fromNbt(textContext, nbt);
    }
    return null;
  }

  /**
   * 将该对象转化为字符串形式的参数形式，用于告示牌编辑屏幕中。告示牌编辑屏幕会将已有 TextSpecial 的文本行在文本输入框中显示为“-id args”的形式。
   */
  @Contract(pure = true)
  default String describeArgs() {
    return "";
  }

  /**
   * 根据已有的 id 和参数返回对象，用于告示牌编辑屏幕中。如果在文本框中输入 {@code -rect 2 3}，则会调用 {@code fromDescription(textContext, "rect", "2 3")}。
   */
  static @Nullable TextSpecial fromDescription(TextContext textContext, String id, String args) {
    if (id == null || id.isEmpty()) return null;
    else if (id.equals("rect")) {
      final RectTextSpecial rect = new RectTextSpecial(textContext);
      final String[] split = args.split(" ");
      if (split.length < 2) return INVALID;
      try {
        rect.width = Float.parseFloat(split[0]);
        rect.height = Float.parseFloat(split[1]);
      } catch (NumberFormatException e) {
        return INVALID;
      }
      return rect;
    } else if (id.equals("pattern")) {
      final PatternTextSpecial pattern = PatternTextSpecial.fromName(textContext, args);
      if (pattern.isEmpty()) {
        return INVALID;
      } else {
        return pattern;
      }
    }
    return null;
  }

  /**
   * 该对象的宽度，用于在渲染 TextContext 时适当排版。
   */
  @Environment(EnvType.CLIENT)
  default float getWidth() {
    return 0;
  }

  /**
   * 该对象的高度，用于在渲染 TextContext 时适当排版。
   */
  @Environment(EnvType.CLIENT)
  default float getHeight() {
    return 0;
  }

  /**
   * 由于该对象需要确保其对应的 TextContext 对应，因此 {@link TextContext#clone()} 中需要复制此对象并将其 textContext 字段设为复制后的 textContext。
   *
   * @param textContext 新的 TextContext 对象。通常来说，应该确保返回的对象是（或者将会是）textContext 的 {@link TextContext#extra} 字段，同时返回的这个 TextSpecial 对象的 textContext 字段应该是该参数。
   * @return 新的 TextSpecial 字段。
   */
  @Contract(value = "_ -> new", pure = true)
  @ApiStatus.Internal
  TextSpecial cloneWithNewTextContext(@NotNull TextContext textContext);
}
