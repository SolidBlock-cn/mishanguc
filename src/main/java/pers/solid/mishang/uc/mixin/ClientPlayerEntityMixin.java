package pers.solid.mishang.uc.mixin;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import pers.solid.mishang.uc.item.BlockToolItem;

@Environment(EnvType.CLIENT)
@Mixin(ClientPlayerEntity.class)
public abstract class ClientPlayerEntityMixin {
  @ModifyArg(
      method = "getCrosshairTarget(Lnet/minecraft/entity/Entity;DDF)Lnet/minecraft/util/hit/HitResult;",
      at =
      @At(
          value = "INVOKE",
          target =
              "Lnet/minecraft/entity/Entity;raycast(DFZ)Lnet/minecraft/util/hit/HitResult;"),
      index = 2)
  private static boolean modifyRaycastCall(boolean includeFluids) {
    final ClientPlayerEntity player = MinecraftClient.getInstance().player;
    if (player == null) {
      return includeFluids;
    }
    final ItemStack itemStack = player.getStackInHand(Hand.MAIN_HAND).isEmpty()
        ? player.getStackInHand(Hand.OFF_HAND)
        : player.getStackInHand(Hand.MAIN_HAND);
    final Item item = itemStack.getItem();
    if (item instanceof final BlockToolItem blockToolItem) {
      return blockToolItem.includesFluid(itemStack, player.isSneaking());
    } else {
      return includeFluids;
    }
  }
}
