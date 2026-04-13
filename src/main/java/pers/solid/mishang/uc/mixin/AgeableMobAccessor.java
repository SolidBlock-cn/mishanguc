package pers.solid.mishang.uc.mixin;

import net.minecraft.world.entity.AgeableMob;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(AgeableMob.class)
public interface AgeableMobAccessor {
  @Invoker
  void callSetAgeLockedData();
}
