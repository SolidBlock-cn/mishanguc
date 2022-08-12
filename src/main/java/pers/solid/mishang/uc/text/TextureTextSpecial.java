package pers.solid.mishang.uc.text;

import com.google.common.annotations.Beta;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.texture.ResourceTexture;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Matrix4f;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

/**
 * 表示一个纹理的特殊文本内容，用于渲染其纹理，一般来说这个纹理的宽度和高度是和文本的大小相同的。
 */
@Beta
public final class TextureTextSpecial implements TextSpecial {
  private final Identifier identifier;
  private final TextContext textContext;

  /**
   * @param identifier  纹理在游戏资源中的路径，如 {@code "textures/block/stone.png"}。
   * @param textContext 该对象对应的文本。在 {@link TextContext#draw} 中渲染时，会根据其大小来决定这个纹理渲染的大小，偏移等参数也是同理。
   */
  public TextureTextSpecial(@NotNull Identifier identifier, @NotNull TextContext textContext) {
    this.identifier = identifier;
    this.textContext = textContext;
  }

  public Identifier identifier() {
    return identifier;
  }

  public TextContext textContext() {
    return textContext;
  }

  @Override
  public boolean equals(Object obj) {
    if (obj == this) return true;
    if (obj == null || obj.getClass() != this.getClass()) return false;
    TextureTextSpecial that = (TextureTextSpecial) obj;
    return Objects.equals(this.identifier, that.identifier) &&
        Objects.equals(this.textContext, that.textContext);
  }

  @Override
  public int hashCode() {
    return Objects.hash(identifier, textContext);
  }

  @Override
  public String toString() {
    return "TextureTextSpecial[" +
        "identifier=" + identifier + ", " +
        "textContext=" + textContext + ']';
  }

  @Override
  @Environment(EnvType.CLIENT)
  public void drawExtra(TextRenderer textRenderer, MatrixStack matrixStack, VertexConsumerProvider vertexConsumers, int light, float x, float y) {
    try (final ResourceTexture resourceTexture = new ResourceTexture(identifier)) {
      resourceTexture.bindTexture();
      final VertexConsumer vertexConsumer = vertexConsumers.getBuffer(RenderLayer.getText(identifier));
      final Matrix4f matrix4f = matrixStack.peek().getModel();

      vertexConsumer.vertex(matrix4f, 0, 8, -0).color(255, 255, 255, 255).texture(0.0f, 1.0f).light(light).next();
      vertexConsumer.vertex(matrix4f, 8, 8, -0).color(255, 255, 255, 255).texture(1.0f, 1.0f).light(light).next();
      vertexConsumer.vertex(matrix4f, 8, 0, -0).color(255, 255, 255, 255).texture(1.0f, 0.0f).light(light).next();
      vertexConsumer.vertex(matrix4f, 0, 0, -0).color(255, 255, 255, 255).texture(0.0f, 0.0f).light(light).next();
    }
  }

  @Override
  public String getId() {
    return "texture";
  }

  @Override
  public float width() {
    return 1;
  }

  @Override
  public float height() {
    return 1;
  }

  @Override
  public TextureTextSpecial cloneWithNewTextContext(@NotNull TextContext textContext) {
    return new TextureTextSpecial(identifier, textContext);
  }

  @Override
  public NbtCompound writeNbt(NbtCompound nbt) {
    final NbtCompound nbtCompound = TextSpecial.super.writeNbt(nbt);
    nbtCompound.putString("texture", identifier.toString());
    return nbtCompound;
  }

  @Override
  public String asStringArgs() {
    return identifier.toString();
  }
}
