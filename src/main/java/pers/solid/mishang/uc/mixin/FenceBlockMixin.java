package pers.solid.mishang.uc.mixin;

import net.minecraft.block.BlockState;
import net.minecraft.block.FenceBlock;
import net.minecraft.util.math.Direction;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import pers.solid.mishang.uc.block.HandrailCentralBlock;

@Mixin(FenceBlock.class)
public class FenceBlockMixin {
  @Inject(method = "canConnect", at = @At("RETURN"), cancellable = true)
  private void modifiedCanConnect(BlockState state, boolean neighborIsFullSquare, Direction dir, CallbackInfoReturnable<Boolean> cir) {
    if (HandrailCentralBlock.connectsHandrailTo(dir, state)) cir.setReturnValue(true);
  }
}
