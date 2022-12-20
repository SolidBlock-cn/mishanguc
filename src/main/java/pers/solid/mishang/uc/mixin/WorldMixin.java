package pers.solid.mishang.uc.mixin;

import net.minecraft.world.GameRules;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import pers.solid.mishang.uc.MishangucClient;
import pers.solid.mishang.uc.MishangucRules;

@Mixin(World.class)
public abstract class WorldMixin {
  @Shadow
  @Final
  public boolean isClient;

  @Shadow
  public abstract GameRules getGameRules();

  @ModifyVariable(method = "setBlockState(Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/block/BlockState;II)Z", at = @At("HEAD"), index = 3, argsOnly = true)
  private int injectedSetBlockState(int value) {
    if (isClient) {
      if (MishangucClient.CLIENT_SUSPENDS_LIGHT_UPDATE != null && MishangucClient.CLIENT_SUSPENDS_LIGHT_UPDATE.get()) {
        return value | 128;
      }
    } else {
      if (MishangucRules.SUSPENDS_BLOCK_LIGHT_UPDATE != null && getGameRules().getBoolean(MishangucRules.SUSPENDS_BLOCK_LIGHT_UPDATE)) {
        return value | 128;
      }
    }
    return value;
  }
}
