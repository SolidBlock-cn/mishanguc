package pers.solid.mishang.uc.mixin;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.world.dimension.PortalForcer;
import net.minecraft.world.poi.PointOfInterestType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import pers.solid.mishang.uc.Mishanguc;

@Mixin(PortalForcer.class)
public abstract class PortalForcerMixin {
  @ModifyReturnValue(method = "method_22389", at = @At("RETURN"))
  private static boolean acceptColoredPortal(boolean original, RegistryEntry<PointOfInterestType> holder) {
    return original || holder.matchesId(Mishanguc.id("nether_portal"));
  }
}
