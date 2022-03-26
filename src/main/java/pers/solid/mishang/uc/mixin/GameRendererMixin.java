package pers.solid.mishang.uc.mixin;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import pers.solid.mishang.uc.item.BlockToolItem;

@Mixin(GameRenderer.class)
public class GameRendererMixin {
  @Shadow
  @Final
  private MinecraftClient client;

  @ModifyArg(
      method = "updateTargetedEntity(F)V",
      at =
      @At(
          value = "INVOKE",
          target =
              "Lnet/minecraft/entity/Entity;raycast(DFZ)Lnet/minecraft/util/hit/HitResult;"),
      index = 2)
  private boolean modifyRaycastCall(boolean includeFluids) {
    //        return true;
    final ClientPlayerEntity player = this.client.player;
    if (player == null) {
      return includeFluids;
    }
    final ItemStack itemStack =
        player.getStackInHand(Hand.MAIN_HAND).isEmpty()
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
