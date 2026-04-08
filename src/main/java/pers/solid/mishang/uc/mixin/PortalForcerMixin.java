package pers.solid.mishang.uc.mixin;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import net.minecraft.core.Holder;
import net.minecraft.world.entity.ai.village.poi.PoiType;
import net.minecraft.world.level.portal.PortalForcer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import pers.solid.mishang.uc.Mishanguc;

@Mixin(PortalForcer.class)
public abstract class PortalForcerMixin {
  @ModifyReturnValue(method = "lambda$findClosestPortalPosition$0", at = @At("RETURN"))
  private static boolean acceptColoredPortal(boolean original, Holder<PoiType> type) {
    return original || type.is(Mishanguc.id("nether_portal"));
  }
}
