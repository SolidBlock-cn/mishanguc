package pers.solid.mishang.uc.text;

import com.google.common.annotations.Beta;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.texture.MissingSprite;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.resource.Resource;
import net.minecraft.util.Identifier;
import net.minecraft.util.PathUtil;
import org.jetbrains.annotations.NotNull;
import org.joml.Matrix4f;

import java.nio.file.Path;
import java.util.Optional;

/**
 * 表示一个纹理的特殊文本内容，用于渲染其纹理，一般来说这个纹理的宽度和高度是和文本的大小相同的。
 *
 * @param identifier  纹理在游戏资源中的路径，如 {@code "textures/block/stone.png"}。
 * @param textContext 该对象对应的文本。在 {@link TextContext#draw} 中渲染时，会根据其大小来决定这个纹理渲染的大小，偏移等参数也是同理。
 */
@Beta
public record TextureSpecialDrawable(@NotNull Identifier identifier, @NotNull TextContext textContext) implements SpecialDrawable {
  public TextureSpecialDrawable {
    validateIdentifier(identifier);
  }

  /**
   * 检验路径是否有效，如果无效，抛出异常。注意：不检查资源是否存在。可以存储指定不存在资源的对象，但路径不能是无效的。
   */
  public static void validateIdentifier(Identifier identifier) throws IllegalArgumentException {
    PathUtil.split(identifier.getPath()).error().ifPresent(error -> {throw new IllegalArgumentException(error.message());});
    var ignore = Path.of(identifier.getNamespace());
  }

  public static boolean isValidIdentifier(Identifier identifier) {
    try {
      validateIdentifier(identifier);
      return true;
    } catch (IllegalArgumentException e) {
      return false;
    }
  }

  @Override
  @Environment(EnvType.CLIENT)
  public void drawExtra(TextRenderer textRenderer, MatrixStack matrixStack, VertexConsumerProvider vertexConsumers, int light, float x, float y) {
    final Optional<Resource> resource = MinecraftClient.getInstance().getResourceManager().getResource(identifier);
    final RenderLayer layer;
    if (resource.isEmpty()) {
      layer = RenderLayer.getText(MissingSprite.getMissingSpriteId());
    } else {
      layer = RenderLayer.getText(identifier);
    }
    final VertexConsumer vertexConsumer = vertexConsumers.getBuffer(layer);
    final Matrix4f matrix4f = matrixStack.peek().getPositionMatrix();

      vertexConsumer.vertex(matrix4f, 0, 8, -0).color(255, 255, 255, 255).texture(0.0f, 1.0f).light(light).next();
      vertexConsumer.vertex(matrix4f, 8, 8, -0).color(255, 255, 255, 255).texture(1.0f, 1.0f).light(light).next();
      vertexConsumer.vertex(matrix4f, 8, 0, -0).color(255, 255, 255, 255).texture(1.0f, 0.0f).light(light).next();
      vertexConsumer.vertex(matrix4f, 0, 0, -0).color(255, 255, 255, 255).texture(0.0f, 0.0f).light(light).next();
  }

  @Override
  public @NotNull String getId() {
    return "texture";
  }

  @Override
  public @NotNull SpecialDrawableType<TextureSpecialDrawable> getType() {
    return SpecialDrawableTypes.TEXTURE;
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
  public TextureSpecialDrawable cloneWithNewTextContext(@NotNull TextContext textContext) {
    return new TextureSpecialDrawable(identifier, textContext);
  }

  @Override
  public void writeNbt(NbtCompound nbt) {
    SpecialDrawable.super.writeNbt(nbt);
    nbt.putString("texture", identifier.toString());
  }

  @Override
  public String asStringArgs() {
    return identifier.toString();
  }
}
