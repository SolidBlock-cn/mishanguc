package pers.solid.mishang.uc.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MouseHandler;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import pers.solid.mishang.uc.item.HotbarScrollInteraction;

@Environment(EnvType.CLIENT)
@Mixin(MouseHandler.class)
public abstract class MouseMixin {
  /**
   * 当玩家手持快速建造工具并潜行时，不进行滑动，同时修改快速建造工具的类型。
   */
  @WrapOperation(method = "onScroll", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/ScrollWheelHandler;getNextScrollWheelSelection(DII)I"))
  private int lockSelection(double yOffset, int selected, int selectionSize, Operation<Integer> original, @Local Inventory inventory) {
    final ItemStack mainHandStack = inventory.getSelectedItem();
    if (mainHandStack.getItem() instanceof HotbarScrollInteraction interaction && interaction.shouldLockScroll(selected, yOffset)) {
      return selected;
    } else {
      return original.call(yOffset, selected, selectionSize);
    }
  }
}
