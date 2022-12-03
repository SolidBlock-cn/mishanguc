package pers.solid.mishang.uc.mixin;

import net.minecraft.util.DyeColor;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(DyeColor.class)
public interface DyeColorAccessorFor1_16 {
  @Accessor
  int getSignColor();
}
