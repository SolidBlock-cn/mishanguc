package pers.solid.mishang.uc.mixin;

import com.mojang.blaze3d.vertex.PoseStack;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.renderer.SubmitNodeCollection;
import net.minecraft.client.renderer.SubmitNodeStorage;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import pers.solid.mishang.uc.render.RenderCommandQueueExtension;
import pers.solid.mishang.uc.text.SpecialDrawable;

/**
 * @since Minecraft 1.21.10
 */
@Environment(EnvType.CLIENT)
@Mixin(SubmitNodeStorage.class)
public abstract class OrderedRenderCommandQueueImplMixin implements RenderCommandQueueExtension {
  @Shadow
  public abstract SubmitNodeCollection order(int order);

  @Override
  public void submitSpecialDrawable$mishang(PoseStack matrixStack, SpecialDrawable specialDrawable, int light, float x, float y) {
    ((RenderCommandQueueExtension) order(0)).submitSpecialDrawable$mishang(matrixStack, specialDrawable, light, x, y);
  }
}
