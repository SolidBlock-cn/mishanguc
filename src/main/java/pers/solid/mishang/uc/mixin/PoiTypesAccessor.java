package pers.solid.mishang.uc.mixin;

import net.minecraft.block.BlockState;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.world.poi.PointOfInterestType;
import net.minecraft.world.poi.PointOfInterestTypes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

import java.util.Set;

@Mixin(PointOfInterestTypes.class)
public interface PoiTypesAccessor {
  @Invoker
  static PointOfInterestType callRegister(Registry<PointOfInterestType> key, RegistryKey<PointOfInterestType> value, Set<BlockState> matchingStates, int maxTickets, int validRange) {
    throw new UnsupportedOperationException();
  }
}
