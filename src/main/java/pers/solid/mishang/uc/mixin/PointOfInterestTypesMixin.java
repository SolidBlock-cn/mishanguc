package pers.solid.mishang.uc.mixin;

import net.minecraft.world.poi.PointOfInterestTypes;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(PointOfInterestTypes.class)
public abstract class PointOfInterestTypesMixin {
  // 重新实现，但是不能在此时就引用 colored blocks
}
