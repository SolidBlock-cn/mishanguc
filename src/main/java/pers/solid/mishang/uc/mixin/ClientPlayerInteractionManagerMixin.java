package pers.solid.mishang.uc.mixin;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerInteractionManager;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import pers.solid.mishang.uc.item.BlockToolItem;

/**
 * 本 mixin 主要用于修复 {@link net.fabricmc.fabric.api.event.player.AttackBlockCallback}
 * 会对“破坏进度更新”过程生效的问题。根据 {@link
 * net.fabricmc.fabric.mixin.event.interaction.MixinClientPlayerInteractionManager}，在 {@code
 * attackBlock} 和 {@code updateBlockBreakingProgress} 两个方法中都会运行 {@link
 * net.fabricmc.fabric.api.event.player.AttackBlockCallback#EVENT}，但 {@link
 * pers.solid.mishang.uc.item.BlockToolItem} 不需要，因此对这种情况予以取消。
 */
@Mixin(ClientPlayerInteractionManager.class)
public class ClientPlayerInteractionManagerMixin {
  @Shadow @Final private MinecraftClient client;

  @Inject(method = "updateBlockBreakingProgress", at = @At("HEAD"), cancellable = true)
  void updateBlockBreakingProgress(
      BlockPos pos, Direction direction, CallbackInfoReturnable<Boolean> cir) {
    if ((client.player != null ? client.player.getMainHandStack().getItem() : null)
        instanceof BlockToolItem) {
      cir.setReturnValue(false);
      cir.cancel();
    }
  }
}
