package pers.solid.mishang.uc.mixin;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.world.dimension.NetherPortal;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import pers.solid.mishang.uc.blocks.ColoredBlocks;

@Mixin(NetherPortal.class)
public abstract class AreaHelperMixin {
  @Inject(method = "validStateInsidePortal", at = @At("RETURN"), cancellable = true)
  private static void validColoredPortal(BlockState state, CallbackInfoReturnable<Boolean> cir) {
    cir.setReturnValue(cir.getReturnValueZ() || state.isOf(ColoredBlocks.COLORED_NETHER_PORTAL));
  }

  @Redirect(method = "getPotentialHeight", at = @At(value = "INVOKE", target = "Lnet/minecraft/block/BlockState;isOf(Lnet/minecraft/block/Block;)Z"))
  private boolean redirectedPotentialHeight(BlockState instance, Block block) {
    return instance.isOf(block) || instance.isOf(ColoredBlocks.COLORED_NETHER_PORTAL);
  }
}
