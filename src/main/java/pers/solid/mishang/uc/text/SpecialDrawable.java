package pers.solid.mishang.uc.text;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.text.MutableText;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import pers.solid.mishang.uc.util.TextBridge;

import java.util.Collection;

/**
 * <p>SpecialDrawable 是一个 TextContext 中可以特殊渲染的内容，它可以是图片、图形等。可以通过 {@link #fromNbt} 与 {@link #writeNbt} 实现 SpecialDrawable 与 NBT 中的转换。当 TextContext 中的 {@link TextContext#extra} 字段不为 {@code null} 时，渲染时就会使用 {@link SpecialDrawable#drawExtra}，渲染的大小取决于 {@link TextContext#size}，{@link SpecialDrawable} 对象自身一般不会存储渲染大小。</p>
 * <p>为了便于序列化和反序列化，每一类 SpecialDrawable 都有一个对应的类型对象，即 {@link SpecialDrawableType}，它指定了这一类的对象该如何进行反序列化。</p>
 *
 * @since 0.2.4 由 {@code TextSpecial} 更名为 {@code SpecialDrawable}
 */
public interface SpecialDrawable extends Cloneable {
  /**
   * 表示一个无效的 SpecialDrawable 对象，通常用于解析无效的情况。
   */
  SpecialDrawable INVALID = new SpecialDrawable() {
    @Environment(EnvType.CLIENT)
    @Override
    public void drawExtra(TextRenderer textRenderer, MatrixStack matrixStack, VertexConsumerProvider vertexConsumers, int light, float x, float y) {
    }

    @Override
    public @NotNull String getId() {
      return "invalid";
    }

    @Override
    public @NotNull SpecialDrawableType<SpecialDrawable> getType() {
      return SpecialDrawableTypes.INVALID;
    }

    @Override
    public SpecialDrawable cloneWithNewTextContext(@NotNull TextContext textContext) {
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
   * 这一类 SpecialDrawable 对象的 id，通常是一个字符串，并且应该要被 {@link #fromNbt} 和 {@link #fromStringArgs} 识别。同一类对象返回的 id 应该相同，因此覆盖此方法时，通常是返回一个常量。
   *
   * @return 这一类 SpecialDrawable 对象的 id。
   */
  @Contract(pure = true)
  default @NotNull String getId() {
    final Identifier id = getType().getId();
    if (id.getNamespace().equals("mishanguc")) {
      return id.getPath();
    } else {
      return id.toString();
    }
  }

  /**
   * 当前 SpecialDrawable 对象对应的 {@link SpecialDrawableType}。通常对于同一类对象来说，其返回值应该是一个常量。
   *
   * @return 这个 SpecialDrawable 对象（通常是这一类对象）的类型。
   * @implSpec 建议调整子类返回值的类型。例如 {@code XXXSpecialDrawable} 覆盖此类时，返回值应该调整为 {@code SpecialDrawableType<? extends XXXSpecialDrawable>} 或 {@code SpecialDrawableType<XXXSpecialDrawable>}。
   */
  @Contract(pure = true)
  @ApiStatus.AvailableSince("0.2.4")
  @NotNull
  SpecialDrawableType<? extends SpecialDrawable> getType();

  @Contract(mutates = "param1")
  default void writeNbt(NbtCompound nbt, RegistryWrapper.WrapperLookup registryLookup) {
    nbt.putString("id", getId());
  }

  default NbtCompound createNbt(RegistryWrapper.WrapperLookup registryLookup) {
    final NbtCompound nbt = new NbtCompound();
    writeNbt(nbt, registryLookup);
    return nbt;
  }

  /**
   * 根据已有的 TextContext 对象和一段 nbt，返回一个新的 SpecialDrawable 对象。通常来说，先识别该 nbt 中的 {@code id} 标签（通常与各个子类的 {@link #getId()} 方法相同），然后再形成对应的对象。
   */
  @Contract("_, _ -> new")
  static @Nullable SpecialDrawable fromNbt(TextContext textContext, @NotNull NbtCompound nbt) {
    final String id = nbt.getString("id");
    if (id == null) {
      return null;
    }
    final SpecialDrawableType<? extends SpecialDrawable> type = SpecialDrawableType.tryFromId(id);
    if (type == null) {
      return null;
    } else {
      return type.fromNbt(textContext, nbt);
    }
  }

  /**
   * 将该对象转化为字符串形式的参数形式，用于告示牌编辑界面中。告示牌编辑界面会将已有 SpecialDrawable 的文本行在文本输入框中显示为“-id args”的形式。
   */
  @Contract(pure = true)
  default String asStringArgs() {
    return "";
  }

  /**
   * 根据已有的 id 和参数返回对象，用于告示牌编辑界面中。如果在文本框中输入 {@code -rect 2 3}，则会调用 {@code fromStringArgs(textContext, "rect", "2 3")}。
   */
  static @Nullable SpecialDrawable fromStringArgs(TextContext textContext, String id, String args) {
    if (id == null) return null;
    final SpecialDrawableType<? extends SpecialDrawable> type = SpecialDrawableType.tryFromId(id);
    if (type == null) return null;
    final SpecialDrawable specialDrawable = type.fromStringArgs(textContext, args);
    return specialDrawable == null ? INVALID : specialDrawable;
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
   * @param textContext 新的 TextContext 对象。通常来说，应该确保返回的对象是（或者将会是）textContext 的 {@link TextContext#extra} 字段，同时返回的这个 SpecialDrawable 对象的 textContext 字段应该是该参数。
   * @return 新的 SpecialDrawable 字段。
   */
  @Contract(value = "_ -> new", pure = true)
  @ApiStatus.Internal
  SpecialDrawable cloneWithNewTextContext(@NotNull TextContext textContext);

  default @NotNull MutableText asStyledText() {
    return TextBridge.literal(getType().getId().getPath() + " " + asStringArgs());
  }
}
