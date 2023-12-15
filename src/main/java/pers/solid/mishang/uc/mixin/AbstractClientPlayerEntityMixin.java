package pers.solid.mishang.uc.mixin;

import com.llamalad7.mixinextras.sugar.Share;
import com.llamalad7.mixinextras.sugar.ref.LocalBooleanRef;
import com.llamalad7.mixinextras.sugar.ref.LocalRef;
import com.mojang.authlib.GameProfile;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.entity.attribute.EntityAttributeInstance;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import pers.solid.mishang.uc.MishangucRules;

@Environment(EnvType.CLIENT)
@Mixin(AbstractClientPlayerEntity.class)
public abstract class AbstractClientPlayerEntityMixin extends PlayerEntity {

  private AbstractClientPlayerEntityMixin(World world, BlockPos pos, float yaw, GameProfile gameProfile) {
    super(world, pos, yaw, gameProfile);
  }

  /**
   * 当玩家行走在道路上时，取消掉其视场角。
   */
  @Inject(method = "getFovMultiplier", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/network/AbstractClientPlayerEntity;getAttributeValue(Lnet/minecraft/entity/attribute/EntityAttribute;)D", shift = At.Shift.BEFORE))
  private void reduceRoadFovMultiplier(CallbackInfoReturnable<Float> cir, @Share("has_attribute") LocalBooleanRef hasAttribute, @Share("attribute_instance") LocalRef<EntityAttributeInstance> attributeInstanceRef) {
    MishangucRules.filterRoadBoost = true;
  }

  @Inject(method = "getFovMultiplier", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/network/AbstractClientPlayerEntity;getAttributeValue(Lnet/minecraft/entity/attribute/EntityAttribute;)D", shift = At.Shift.AFTER))
  private void restoreAttributeAfterFOV(CallbackInfoReturnable<Float> cir, @Share("has_attribute") LocalBooleanRef hasAttribute, @Share("attribute_instance") LocalRef<EntityAttributeInstance> attributeInstanceRef) {
    MishangucRules.filterRoadBoost = false;
  }
}
