package pers.solid.mishang.uc.mixin;

import net.minecraft.block.BlockState;
import net.minecraft.block.WallBlock;
import net.minecraft.util.math.Direction;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import pers.solid.mishang.uc.block.HandrailCentralBlock;

@Mixin(WallBlock.class)
public class WallBlockMixin {
  @Inject(method = "shouldConnectTo", at = @At("RETURN"), cancellable = true)
  private void modifiedShouldConnect(BlockState state, boolean faceFullSquare, Direction side, CallbackInfoReturnable<Boolean> cir) {
    if (HandrailCentralBlock.connectsHandrailTo(side, state)) cir.setReturnValue(true);
  }
}
