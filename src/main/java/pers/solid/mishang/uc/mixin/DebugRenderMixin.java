package pers.solid.mishang.uc.mixin;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.debug.DebugRenderer;
import net.minecraft.client.util.math.MatrixStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import pers.solid.mishang.uc.render.BuildingTooltipRenderer;

@Mixin(DebugRenderer.class)
public class DebugRenderMixin {
    public BuildingTooltipRenderer buildingTooltipRender;

    @Inject(method = "<init>(Lnet/minecraft/client/MinecraftClient;)V", at = @At("RETURN"))
    void constructorMixin(MinecraftClient client, CallbackInfo ci) {
        this.buildingTooltipRender = new BuildingTooltipRenderer(client);
    }

    @Inject(method = "reset", at = @At("RETURN"))
    void resetMixin(CallbackInfo ci) {
        this.buildingTooltipRender.clear();
    }

    @Inject(method = "render", at = @At("RETURN"))
    void renderMixin(MatrixStack matrices, VertexConsumerProvider.Immediate vertexConsumers, double cameraX, double cameraY, double cameraZ, CallbackInfo ci) {
        this.buildingTooltipRender.render(matrices,vertexConsumers,cameraX,cameraY,cameraZ);
    }
}
