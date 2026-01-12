package pers.solid.mishang.uc.mixin;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.command.BatchingRenderCommandQueue;
import net.minecraft.client.render.command.OrderedRenderCommandQueueImpl;
import net.minecraft.client.util.math.MatrixStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import pers.solid.mishang.uc.render.RenderCommandQueueExtension;
import pers.solid.mishang.uc.text.SpecialDrawable;

/**
 * @since Minecraft 1.21.10
 */
@Environment(EnvType.CLIENT)
@Mixin(OrderedRenderCommandQueueImpl.class)
public abstract class OrderedRenderCommandQueueImplMixin implements RenderCommandQueueExtension {
  @Shadow
  public abstract BatchingRenderCommandQueue getBatchingQueue(int i);

  @Override
  public void submitSpecialDrawable$mishang(MatrixStack matrixStack, SpecialDrawable specialDrawable, int light, float x, float y) {
    ((RenderCommandQueueExtension) getBatchingQueue(0)).submitSpecialDrawable$mishang(matrixStack, specialDrawable, light, x, y);
  }
}
