package pers.solid.mishang.uc.text;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.texture.ResourceTexture;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.util.Collection;

/**
 * <p>TextSpecial 是一个 TextContext 中用于特殊渲染的一部分内容，它可以是图片、图形等。可以通过 {@link #fromNbt} 与 {@link #writeNbt} 实现 TextSpecial 与 NBT 中的转换。当 TextContext 中的 TextSpecial 字段不为 {@code null} 时，渲染时就会使用 {@link TextSpecial#drawExtra}，渲染的大小取决于 {@link TextContext#size}，{@link TextSpecial} 对象自身一般不会存储渲染大小。</p>
 */
public interface TextSpecial extends Cloneable {
  /**
   * 表示一个无效的 TextSpecial 对象，通常用于解析无效的情况。
   */
  TextSpecial INVALID = new TextSpecial() {
    @Environment(EnvType.CLIENT)
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
   * <p>渲染该对象。在 {@link TextContext#draw} 中，如果 {@link TextContext#extra} 不为 {@code null}，则会使用这个方法。</p>
   * <p>注意在 {@link TextContext#draw} 中，其渲染的大小会根据 {@link TextContext#size} 调整，并调整好渲染的位置，即根据 {@link #width()} 和 {@link #height()} 进行偏移。因此，这里的渲染通常从矩阵零点开始渲染，即将矩阵的零点位置视为左上角。</p>
   * <p>实现此方法时，必须注解 {@code @Environment(EnvType.CLIENT)}。</p>
   */
  @Environment(EnvType.CLIENT)
  void drawExtra(TextRenderer textRenderer, MatrixStack matrixStack, VertexConsumerProvider vertexConsumers, int light, float x, float y);

  /**
   * 这一类 TextSpecial 对象的 id，通常是一个字符串，并且应该要被 {@link #fromNbt} 和 {@link #fromStringArgs} 识别。同一类对象返回的 id 应该相同，因此覆盖此方法时，通常是返回一个常量。
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
   * 根据已有的 TextContext 对象和一段 nbt，返回一个新的 TextSpecial 对象。通常来说，先识别该 nbt 中的 {@code id} 标签（通常与各个子类的 {@link #getId()} 方法相同），然后再形成对应的对象。
   */
  @Contract("_, _ -> new")
  static @Nullable TextSpecial fromNbt(TextContext textContext, @NotNull NbtCompound nbt) {
    final String id = nbt.getString("id");
    if (id == null || id.isEmpty()) {
      return null;
    } else if (id.equals("rect")) {
      return RectTextSpecial.fromNbt(nbt, textContext);
    } else if (id.equals("pattern")) {
      return PatternTextSpecial.fromNbt(textContext, nbt);
    } else if (id.equals("texture") || id.equals("texture_beta")) {
      final Identifier texture = Identifier.tryParse(nbt.getString("texture"));
      return texture != null ? new TextureTextSpecial(texture, textContext) : null;
    }
    return null;
  }

  /**
   * 将该对象转化为字符串形式的参数形式，用于告示牌编辑界面中。告示牌编辑界面会将已有 TextSpecial 的文本行在文本输入框中显示为“-id args”的形式。
   */
  @Contract(pure = true)
  default String asStringArgs() {
    return "";
  }

  /**
   * 根据已有的 id 和参数返回对象，用于告示牌编辑界面中。如果在文本框中输入 {@code -rect 2 3}，则会调用 {@code fromStringArgs(textContext, "rect", "2 3")}。
   */
  static @Nullable TextSpecial fromStringArgs(TextContext textContext, String id, String args) {
    if (id == null || id.isEmpty()) return null;
    else if (id.equals("rect")) {
      final RectTextSpecial rect;
      final String[] split = args.split(" ");
      if (split.length < 2) return INVALID;
      try {
        final float width = Float.parseFloat(split[0]);
        final float height = Float.parseFloat(split[1]);
        rect = new RectTextSpecial(width, height, textContext);
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
    } else if (id.equals("texture") || id.equals("texture_beta")) {
      final Identifier identifier = Identifier.tryParse(args);
      if (identifier == null) {
        return INVALID;
      } else if (FabricLoader.getInstance().getEnvironmentType() == EnvType.CLIENT) {
        // 考虑到输入过程中，还未输入完成时会抛出纹理不存在的错误，故在这里先进行抑制。
        try (final ResourceTexture resourceTexture = new ResourceTexture(identifier)) {
          resourceTexture.load(MinecraftClient.getInstance().getResourceManager());
        } catch (IOException e) {
          return INVALID;
        }
        return new TextureTextSpecial(identifier, textContext);
      }
    }
    return null;
  }

  /**
   * 该对象的宽度，用于在渲染时适当排版。注意：这里的宽度是忽略了 {@link TextContext#size} 的，在实际渲染时，会再乘以 {@link TextContext#size}。因此，如果要与文本（无论文本大小）的高度（注意是高度）相等，则为 1。
   */
  default float width() {
    return 0;
  }

  /**
   * 该对象的高度，用于在渲染时适当排版，以及在 {@link pers.solid.mishang.uc.MishangUtils#rearrange(Collection)} 中的排版。这里的高度忽略了 {@link TextContext#size}，实际渲染和排版时会将其相乘。因此，如果要与文本（无论文本大小）的高度相等，则为 1。
   */
  default float height() {
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
