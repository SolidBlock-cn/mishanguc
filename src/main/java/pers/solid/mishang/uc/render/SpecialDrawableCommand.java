package pers.solid.mishang.uc.render;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.VertexConsumerProvider;
import org.joml.Matrix4f;
import pers.solid.mishang.uc.text.SpecialDrawable;

/**
 * 用于渲染此模组中的 {@link SpecialDrawable}。
 *
 * @since Minecraft 1.21.10
 */
@Environment(EnvType.CLIENT)
public record SpecialDrawableCommand(Matrix4f matricesEntry, SpecialDrawable specialDrawable, int light, float x, float y) {

  public void render(VertexConsumerProvider.Immediate vertexConsumers) {
    specialDrawable.drawInternal(matricesEntry, vertexConsumers, light, x, y);
  }
}
