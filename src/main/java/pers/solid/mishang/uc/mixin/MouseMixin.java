package pers.solid.mishang.uc.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Mouse;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import pers.solid.mishang.uc.item.HotbarScrollInteraction;

@Environment(EnvType.CLIENT)
@Mixin(Mouse.class)
public abstract class MouseMixin {
  /**
   * 当玩家手持快速建造工具并潜行时，不进行滑动，同时修改快速建造工具的类型。
   */
  @WrapOperation(method = "onMouseScroll", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/input/Scroller;scrollCycling(DII)I"))
  private int lockSelection(double amount, int selectedIndex, int total, Operation<Integer> original, @Local PlayerInventory playerInventory) {
    final ItemStack mainHandStack = playerInventory.getSelectedStack();
    if (mainHandStack.getItem() instanceof HotbarScrollInteraction interaction && interaction.shouldLockScroll(selectedIndex, amount)) {
      return selectedIndex;
    } else {
      return original.call(amount, selectedIndex, total);
    }
  }
}
