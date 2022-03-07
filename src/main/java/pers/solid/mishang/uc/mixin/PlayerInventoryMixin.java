package pers.solid.mishang.uc.mixin;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import pers.solid.mishang.uc.item.FastBuildingToolItem;

@Environment(EnvType.CLIENT)
@Mixin(PlayerInventory.class)
public abstract class PlayerInventoryMixin {
  @Shadow
  public abstract ItemStack getMainHandStack();

  @Shadow
  @Final
  public PlayerEntity player;

  @Shadow
  public abstract void setStack(int slot, ItemStack stack);

  @Shadow
  public int selectedSlot;

  /**
   * 当玩家手持快速建造工具并潜行时，不进行滑动，同时修改快速建造工具的类型。
   */
  @Inject(method = "scrollInHotbar", at = @At("HEAD"), cancellable = true)
  public void lockSelection(double scrollAmount, CallbackInfo ci) {
    final ItemStack mainHandStack = this.getMainHandStack();
    if ((Screen.hasShiftDown() || Screen.hasAltDown()) && mainHandStack.getItem() instanceof FastBuildingToolItem) {
      final PacketByteBuf buf = PacketByteBufs.create();
      buf.writeInt(selectedSlot);
      buf.writeInt(((int) scrollAmount));
      ClientPlayNetworking.send(new Identifier("mishanguc", "update_matching_rule"), buf);
      ci.cancel();
    }
  }
}
