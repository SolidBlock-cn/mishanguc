package pers.solid.mishang.uc.mixin;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import pers.solid.mishang.uc.item.BlockToolItem;

@Environment(EnvType.CLIENT)
@Mixin(LocalPlayer.class)
public abstract class ClientPlayerEntityMixin {
  @ModifyArg(
      method = "pick(Lnet/minecraft/world/entity/Entity;DDF)Lnet/minecraft/world/phys/HitResult;",
      at =
      @At(
          value = "INVOKE",
          target =
              "Lnet/minecraft/world/entity/Entity;pick(DFZ)Lnet/minecraft/world/phys/HitResult;"),
      index = 2)
  private static boolean modifyRaycastCall(boolean includeFluids) {
    final LocalPlayer player = Minecraft.getInstance().player;
    if (player == null) {
      return includeFluids;
    }
    final ItemStack itemStack = player.getItemInHand(InteractionHand.MAIN_HAND).isEmpty()
        ? player.getItemInHand(InteractionHand.OFF_HAND)
        : player.getItemInHand(InteractionHand.MAIN_HAND);
    final Item item = itemStack.getItem();
    if (item instanceof final BlockToolItem blockToolItem) {
      return blockToolItem.includesFluid(itemStack, player.isShiftKeyDown());
    } else {
      return includeFluids;
    }
  }
}
