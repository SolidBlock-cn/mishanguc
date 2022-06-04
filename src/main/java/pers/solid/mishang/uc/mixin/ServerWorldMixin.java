package pers.solid.mishang.uc.mixin;

import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.TranslatableText;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ServerWorld.class)
public class ServerWorldMixin {
  @Inject(method = "onPlayerConnected", at = @At("RETURN"))
  private void sendModMessage(ServerPlayerEntity player, CallbackInfo ci) {
    player.sendMessage(new TranslatableText("notice.mishanguc.load")
        .styled(style -> style.withColor(0xd0e8a5)), false);
  }
}
