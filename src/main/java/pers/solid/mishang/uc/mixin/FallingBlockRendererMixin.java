package pers.solid.mishang.uc.mixin;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.renderer.entity.FallingBlockRenderer;
import net.minecraft.client.renderer.entity.state.FallingBlockRenderState;
import net.minecraft.world.entity.item.FallingBlockEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import pers.solid.mishang.uc.MishangucEntityData;
import pers.solid.mishang.uc.block.ColoredBlock;
import pers.solid.mishang.uc.render.MovingBlockRenderStateExtension;

import java.util.OptionalInt;

@Environment(EnvType.CLIENT)
@Mixin(FallingBlockRenderer.class)
public abstract class FallingBlockRendererMixin {
  @Inject(method = "extractRenderState(Lnet/minecraft/world/entity/item/FallingBlockEntity;Lnet/minecraft/client/renderer/entity/state/FallingBlockRenderState;F)V", at = @At("TAIL"))
  private void addRenderStateForCustomColoredBlock(FallingBlockEntity entity, FallingBlockRenderState state, float partialTicks, CallbackInfo ci) {
    if (entity.getBlockState().getBlock() instanceof ColoredBlock && entity.getEntityData() != null) {
      final OptionalInt color = entity.getEntityData().get(MishangucEntityData.FALLING_BLOCK_COLOR);
      if (color.isPresent()) {
        ((MovingBlockRenderStateExtension) state.movingBlockRenderState).setCustomColor$mishang(color.getAsInt());
      }
    }
  }
}
