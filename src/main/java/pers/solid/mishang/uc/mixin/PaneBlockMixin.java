package pers.solid.mishang.uc.mixin;

import net.minecraft.block.BlockState;
import net.minecraft.block.PaneBlock;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import pers.solid.mishang.uc.block.HandrailCentralBlock;

@Mixin(PaneBlock.class)
public class PaneBlockMixin {
  @Inject(method = "connectsTo", at = @At("RETURN"), cancellable = true)
  private void modifiedConnectsTo(BlockState state, boolean sideSolidFullSquare, CallbackInfoReturnable<Boolean> cir) {
    if (state.getBlock() instanceof HandrailCentralBlock) cir.setReturnValue(true);
  }
}
