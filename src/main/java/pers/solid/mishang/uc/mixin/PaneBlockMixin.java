package pers.solid.mishang.uc.mixin;

import net.minecraft.world.level.block.IronBarsBlock;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import pers.solid.mishang.uc.block.HandrailCentralBlock;

@Mixin(IronBarsBlock.class)
public class PaneBlockMixin {
  @Inject(method = "attachsTo", at = @At("RETURN"), cancellable = true)
  private void modifiedConnectsTo(BlockState state, boolean solidSide, CallbackInfoReturnable<Boolean> cir) {
    if (state.getBlock() instanceof HandrailCentralBlock) cir.setReturnValue(true);
  }
}
