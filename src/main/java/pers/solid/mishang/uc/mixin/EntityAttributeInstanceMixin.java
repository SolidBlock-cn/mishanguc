package pers.solid.mishang.uc.mixin;

import com.google.common.collect.Iterators;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.entity.attribute.EntityAttributeInstance;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Slice;
import pers.solid.mishang.uc.MishangucRules;
import pers.solid.mishang.uc.block.Road;

import java.util.Iterator;

@Environment(EnvType.CLIENT)
@Mixin(EntityAttributeInstance.class)
public abstract class EntityAttributeInstanceMixin {


  @ModifyExpressionValue(method = "computeValue", at = @At(value = "INVOKE", target = "Ljava/util/Collection;iterator()Ljava/util/Iterator;"), slice = @Slice(from = @At(value = "FIELD", target = "Lnet/minecraft/entity/attribute/EntityAttributeModifier$Operation;MULTIPLY_TOTAL:Lnet/minecraft/entity/attribute/EntityAttributeModifier$Operation;")))
  private static Iterator<EntityAttributeModifier> filterRoadBoostModifier(Iterator<EntityAttributeModifier> original) {
    if (MishangucRules.currentRoadBoostSpeed == 0 || !MishangucRules.filterRoadBoost) {
      return original;
    }
    return Iterators.filter(original, x -> x != Road.ROAD_SPEED_BOOST);
  }
}
