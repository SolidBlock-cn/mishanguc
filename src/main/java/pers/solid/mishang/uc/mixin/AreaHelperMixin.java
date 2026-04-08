package pers.solid.mishang.uc.mixin;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.portal.PortalShape;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import pers.solid.mishang.uc.blocks.ColoredBlocks;

@Mixin(PortalShape.class)
public abstract class AreaHelperMixin {
  @ModifyReturnValue(method = "isEmpty", at = @At("RETURN"))
  private static boolean validColoredPortal(boolean original, @Local(argsOnly = true, name = "state", ordinal = 0) BlockState state) {
    return original || state.is(ColoredBlocks.COLORED_NETHER_PORTAL);
  }

  @ModifyExpressionValue(method = "getDistanceUntilTop", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/block/state/BlockState;is(Lnet/minecraft/world/level/block/Block;)Z"))
  private static boolean redirectedPotentialHeight(boolean original, @Local(name = "blockState") BlockState blockState) {
    return original || blockState.is(ColoredBlocks.COLORED_NETHER_PORTAL);
  }
}
