package pers.solid.mishang.uc.render;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.command.BatchingRenderCommandQueue;

/**
 * 渲染队列中的 {@link SpecialDrawableCommand}。
 *
 * @since Minecraft 1.21.10
 */
@Environment(EnvType.CLIENT)
public class TextSpecialDrawableRenderer {
  public void render(BatchingRenderCommandQueue queue, VertexConsumerProvider.Immediate vertexConsumers) {
    for (SpecialDrawableCommand specialDrawableCommand : ((BatchingRenderCommandQueueExtension) queue).getSpecialDrawableCommands$mishang()) {
      specialDrawableCommand.render(vertexConsumers);
    }
  }
}
