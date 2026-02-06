package pers.solid.mishang.uc.text;

import com.google.common.annotations.Beta;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.texture.MissingSprite;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.resource.Resource;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.NotNull;
import org.joml.Matrix4f;

import java.util.Optional;

/**
 * 表示一个纹理的特殊文本内容，用于渲染其纹理，一般来说这个纹理的宽度和高度是和文本的大小相同的。
 *
 * @param identifier  纹理在游戏资源中的路径，如 {@code "textures/block/stone.png"}。
 * @param textContext 该对象对应的文本。在 {@link TextContext#draw} 中渲染时，会根据其大小来决定这个纹理渲染的大小，偏移等参数也是同理。
 */
@Beta
public record TextureSpecialDrawable(@NotNull Identifier identifier, @NotNull TextContext textContext) implements SpecialDrawable {

  /**
   * 说明：原版的文本的实现见于 {@link net.minecraft.client.texture.SpriteAtlasGlyphs.Glyph#draw(Matrix4f, VertexConsumer, int, float, float, float, int)}，此处的逻辑略有不同。
   */
  @Environment(EnvType.CLIENT)
  @Override
  public void drawInternal(Matrix4f matricesEntry, VertexConsumerProvider.Immediate vertexConsumers, int light, float x, float y) {
    final Optional<Resource> resource = MinecraftClient.getInstance().getResourceManager().getResource(identifier);
    final RenderLayer layer;
    if (resource.isEmpty()) {
      layer = RenderLayer.getText(MissingSprite.getMissingSpriteId());
    } else {
      layer = RenderLayer.getText(identifier);
    }
    final VertexConsumer vertexConsumer = vertexConsumers.getBuffer(layer);

    vertexConsumer.vertex(matricesEntry, 0, 8, -0).color(255, 255, 255, 255).texture(0.0f, 1.0f).light(light);
    vertexConsumer.vertex(matricesEntry, 8, 8, -0).color(255, 255, 255, 255).texture(1.0f, 1.0f).light(light);
    vertexConsumer.vertex(matricesEntry, 8, 0, -0).color(255, 255, 255, 255).texture(1.0f, 0.0f).light(light);
    vertexConsumer.vertex(matricesEntry, 0, 0, -0).color(255, 255, 255, 255).texture(0.0f, 0.0f).light(light);
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
  public void writeNbt(NbtCompound nbt, RegistryWrapper.WrapperLookup registryLookup) {
    SpecialDrawable.super.writeNbt(nbt, registryLookup);
    nbt.putString("texture", identifier.toString());
  }

  @Override
  public String asStringArgs() {
    return identifier.toString();
  }
}
