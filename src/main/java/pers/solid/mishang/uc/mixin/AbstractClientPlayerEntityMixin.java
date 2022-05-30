package pers.solid.mishang.uc.mixin;

import com.mojang.authlib.GameProfile;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.entity.attribute.EntityAttributeInstance;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import pers.solid.mishang.uc.block.Road;

@Environment(EnvType.CLIENT)
@Mixin(AbstractClientPlayerEntity.class)
public abstract class AbstractClientPlayerEntityMixin extends PlayerEntity {
  private AbstractClientPlayerEntityMixin(World world, BlockPos pos, float yaw, GameProfile profile) {
    super(world, pos, yaw, profile);
  }

  /**
   * 在计算视场角之前，移除该属性，计算完成之后重新加入该属性。因此，此值只是一个临时值。
   */
  private boolean cachedHasAttribute = false;

  /**
   * 当玩家行走在道路上时，取消掉其视场角。
   */
  @Inject(method = "getSpeed", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/network/AbstractClientPlayerEntity;getAttributeValue(Lnet/minecraft/entity/attribute/EntityAttribute;)D", shift = At.Shift.BEFORE))
  private void reduceRoadFovMultiplier(CallbackInfoReturnable<Float> cir) {
    final EntityAttributeInstance attributeInstance = this.getAttributeInstance(EntityAttributes.GENERIC_MOVEMENT_SPEED);
    if (attributeInstance != null) {
      if (attributeInstance.hasModifier(Road.ROAD_SPEED_BOOST)) {
        cachedHasAttribute = true;
        attributeInstance.removeModifier(Road.ROAD_SPEED_BOOST);
      } else {
        cachedHasAttribute = false;
      }
    }
  }

  @Inject(method = "getSpeed", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/network/AbstractClientPlayerEntity;getAttributeValue(Lnet/minecraft/entity/attribute/EntityAttribute;)D", shift = At.Shift.AFTER))
  private void restoreAttributeAfterFOV(CallbackInfoReturnable<Float> cir) {
    final EntityAttributeInstance attributeInstance = this.getAttributeInstance(EntityAttributes.GENERIC_MOVEMENT_SPEED);
    if (attributeInstance != null)
      if (!attributeInstance.hasModifier(Road.ROAD_SPEED_BOOST) && cachedHasAttribute) {
        attributeInstance.addTemporaryModifier(Road.ROAD_SPEED_BOOST);
      } else if (!cachedHasAttribute) {
        attributeInstance.removeModifier(Road.ROAD_SPEED_BOOST);
      }
  }
}
