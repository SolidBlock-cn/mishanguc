package pers.solid.mishang.uc.mixin;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.dimension.NetherPortal;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import pers.solid.mishang.uc.blocks.ColoredBlocks;

@Mixin(NetherPortal.class)
public abstract class AreaHelperMixin {
  @Inject(method = "validStateInsidePortal", at = @At("RETURN"), cancellable = true)
  private static void validColoredPortal(BlockState state, CallbackInfoReturnable<Boolean> cir) {
    if (state.isOf(ColoredBlocks.COLORED_NETHER_PORTAL)) {
      cir.setReturnValue(true);
    }
  }

  @ModifyExpressionValue(method = "getPotentialHeight", at = @At(value = "INVOKE", target = "Lnet/minecraft/block/BlockState;isOf(Lnet/minecraft/block/Block;)Z"))
  private boolean redirectedPotentialHeight(boolean original, BlockPos.Mutable mutable, @Local BlockState blockState) {
    return original || blockState.isOf(ColoredBlocks.COLORED_NETHER_PORTAL);
  }
}
