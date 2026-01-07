package pers.solid.mishang.uc.mixin;

import com.llamalad7.mixinextras.sugar.Local;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.command.BatchingRenderCommandQueue;
import net.minecraft.client.render.command.RenderDispatcher;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import pers.solid.mishang.uc.render.TextSpecialDrawableRenderer;

@Mixin(RenderDispatcher.class)
@Environment(EnvType.CLIENT)
public abstract class RenderDispatcherMixin {
  @Shadow
  @Final
  private VertexConsumerProvider.Immediate vertexConsumers;
  @Unique
  private final TextSpecialDrawableRenderer textSpecialDrawableRenderer = new TextSpecialDrawableRenderer();

  @Inject(method = "render", at = @At(value = "FIELD", target = "Lnet/minecraft/client/render/command/RenderDispatcher;leashCommandRenderer:Lnet/minecraft/client/render/command/LeashCommandRenderer;", opcode = Opcodes.GETFIELD))
  private void renderSpecialText(CallbackInfo ci, @Local BatchingRenderCommandQueue batchingRenderCommandQueue) {
    this.textSpecialDrawableRenderer.render(batchingRenderCommandQueue, vertexConsumers);
  }
}
