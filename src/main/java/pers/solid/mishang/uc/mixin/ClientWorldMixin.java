package pers.solid.mishang.uc.mixin;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import pers.solid.mishang.uc.MishangucClient;

@Environment(EnvType.CLIENT)
@Mixin(World.class)
public abstract class ClientWorldMixin {

  @Shadow
  @Final
  public boolean isClient;

  @ModifyVariable(method = "setBlockState(Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/block/BlockState;II)Z", at = @At("HEAD"), index = 3, argsOnly = true)
  private int injectedSetBlockState(int value) {
    if (this.isClient) {
      if (MishangucClient.CLIENT_SUSPENDS_LIGHT_UPDATE != null && MishangucClient.CLIENT_SUSPENDS_LIGHT_UPDATE.get()) {
        return value | 128;
      }
    }
    return value;
  }
}
