package pers.solid.mishang.uc.text;

import com.google.common.annotations.Beta;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.feature.TextFeatureRenderer;
import net.minecraft.client.renderer.rendertype.RenderType;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.client.renderer.texture.MissingTextureAtlasSprite;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.Identifier;
import net.minecraft.server.packs.resources.Resource;
import net.minecraft.util.FileUtil;
import org.joml.Matrix4f;
import org.joml.Matrix4fc;

import java.nio.file.Path;
import java.util.Optional;

/**
 * 表示一个纹理的特殊文本内容，用于渲染其纹理，一般来说这个纹理的宽度和高度是和文本的大小相同的。
 *
 * @param identifier  纹理在游戏资源中的路径，如 {@code "textures/block/stone.png"}。
 * @param textContext 该对象对应的文本。在 {@link TextContext#draw} 中渲染时，会根据其大小来决定这个纹理渲染的大小，偏移等参数也是同理。
 */
@Beta
public record TextureSpecialDrawable(Identifier identifier, TextContext textContext) implements SpecialDrawable {
  public TextureSpecialDrawable {
    validateIdentifier(identifier);
  }

  /**
   * 检验路径是否有效，如果无效，抛出异常。注意：不检查资源是否存在。可以存储指定不存在资源的对象，但路径不能是无效的。
   */
  public static void validateIdentifier(Identifier identifier) throws IllegalArgumentException {
    FileUtil.decomposePath(identifier.getPath()).ifError(error -> {throw new IllegalArgumentException(error.message());});
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

  /**
   * 说明：原版的文本的实现见于 {@link net.minecraft.client.gui.font.AtlasGlyphProvider.Instance#renderSprite(Matrix4fc, VertexConsumer, int, float, float, float, int)}，此处的逻辑略有不同。
   */
  @Environment(EnvType.CLIENT)
  @Override
  public void drawInternal(Matrix4f matricesEntry, TextFeatureRenderer textFeatureRenderer, int light, float x, float y) {
    final Optional<Resource> resource = Minecraft.getInstance().getResourceManager().getResource(identifier);
    final RenderType layer;
    if (resource.isEmpty()) {
      layer = RenderTypes.text(MissingTextureAtlasSprite.getLocation());
    } else {
      layer = RenderTypes.text(identifier);
    }

    // todo 需要一个 buffer 对象，其类型为 VertexConsumer

//    buffer.addVertex(matricesEntry, 0, 8, -0).setColor(255, 255, 255, 255).setUv(0.0f, 1.0f).setLight(light);
//    buffer.addVertex(matricesEntry, 8, 8, -0).setColor(255, 255, 255, 255).setUv(1.0f, 1.0f).setLight(light);
//    buffer.addVertex(matricesEntry, 8, 0, -0).setColor(255, 255, 255, 255).setUv(1.0f, 0.0f).setLight(light);
//    buffer.addVertex(matricesEntry, 0, 0, -0).setColor(255, 255, 255, 255).setUv(0.0f, 0.0f).setLight(light);
  }

  @Override
  public String getId() {
    return "texture";
  }

  @Override
  public SpecialDrawableType<TextureSpecialDrawable> getType() {
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
  public TextureSpecialDrawable cloneWithNewTextContext(TextContext textContext) {
    return new TextureSpecialDrawable(identifier, textContext);
  }

  @Override
  public void writeNbt(CompoundTag nbt, HolderLookup.Provider registryLookup) {
    SpecialDrawable.super.writeNbt(nbt, registryLookup);
    nbt.putString("texture", identifier.toString());
  }

  @Override
  public String asStringArgs() {
    return identifier.toString();
  }
}
