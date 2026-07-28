package pers.solid.mishang.uc.mixin;

import net.minecraft.core.Direction;
import net.minecraft.world.level.block.WallBlock;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import pers.solid.mishang.uc.block.HandrailCentralBlock;

@Mixin(WallBlock.class)
public class WallBlockMixin {
  @Inject(method = "connectsTo", at = @At("RETURN"), cancellable = true)
  private void modifiedShouldConnect(BlockState state, boolean sideSolid, Direction direction, CallbackInfoReturnable<Boolean> cir) {
    if (HandrailCentralBlock.connectsHandrailTo(direction, state)) cir.setReturnValue(true);
  }
}
