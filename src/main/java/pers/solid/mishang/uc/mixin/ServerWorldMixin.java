package pers.solid.mishang.uc.mixin;

import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import pers.solid.mishang.uc.util.TextBridge;

@Mixin(ServerWorld.class)
public class ServerWorldMixin {
  @Inject(method = "onPlayerConnected", at = @At("RETURN"))
  private void sendModMessage(ServerPlayerEntity player, CallbackInfo ci) {
    player.sendMessage(TextBridge.translatable("notice.mishanguc.load")
        .styled(style -> style.withColor(0x9dc7ee)), false);
  }
}
