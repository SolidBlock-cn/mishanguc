package pers.solid.mishang.uc.mixin;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import pers.solid.mishang.uc.item.HotbarScrollInteraction;

@Mixin(PlayerInventory.class)
public abstract class PlayerInventoryMixin {
  @Shadow
  public abstract ItemStack getMainHandStack();

  @Shadow
  public int selectedSlot;

  /**
   * 当玩家手持快速建造工具并潜行时，不进行滑动，同时修改快速建造工具的类型。
   */
  @Environment(EnvType.CLIENT)
  @Inject(method = "scrollInHotbar", at = @At("HEAD"), cancellable = true)
  public void lockSelection(double scrollAmount, CallbackInfo ci) {
    final ItemStack mainHandStack = this.getMainHandStack();
    if (mainHandStack.getItem() instanceof HotbarScrollInteraction && ((HotbarScrollInteraction) mainHandStack.getItem()).shouldLockScroll(selectedSlot, scrollAmount)) {
      ci.cancel();
    }
  }
}
