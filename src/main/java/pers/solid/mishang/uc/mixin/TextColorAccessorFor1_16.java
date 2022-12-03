package pers.solid.mishang.uc.mixin;

import net.minecraft.text.TextColor;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(TextColor.class)
public interface TextColorAccessorFor1_16 {
  @Accessor
  int getRgb();
}
