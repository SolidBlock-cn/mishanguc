package pers.solid.mishang.uc.mixin;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerInteractionManager;
import net.minecraft.network.packet.c2s.play.PlayerActionC2SPacket;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.GameMode;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import pers.solid.mishang.uc.MishangUc;

/**
 * 本 mixin 参考了 {@link
 * net.fabricmc.fabric.mixin.event.interaction.MixinClientPlayerInteractionManager}，不过这个 mixin
 * 有个很大的问题，一是在客户端结果不为 PASS 时会阻止产生 packet 导致服务器不执行该 callback，而且方块破坏过程中也会执行该 mixin。因此改进了此 mixin。<br>
 * 该 mixin 具有两个特点：一是在客户端结果不返回 PASS 时也会发送 packet，这样可以让服务器也执行，二是分类开始破坏和中途破坏两个情况。
 */
@Mixin(ClientPlayerInteractionManager.class)
public abstract class BetterClientPlayerInteractionManagerMixin {

  @Shadow @Final private MinecraftClient client;
  @Shadow private GameMode gameMode;

  @Shadow
  protected abstract void sendPlayerAction(
      PlayerActionC2SPacket.Action action, BlockPos pos, Direction direction);

  @Inject(
      at =
          @At(
              value = "INVOKE",
              target = "Lnet/minecraft/world/GameMode;isCreative()Z",
              ordinal = 0),
      method = "attackBlock",
      cancellable = true)
  public void attackBlock(BlockPos pos, Direction direction, CallbackInfoReturnable<Boolean> info) {
    ActionResult result =
        MishangUc.BEGIN_ATTACK_BLOCK_EVENT
            .invoker()
            .interact(client.player, client.world, Hand.MAIN_HAND, pos, direction);

    if (result != ActionResult.PASS) {
      sendPlayerAction(PlayerActionC2SPacket.Action.START_DESTROY_BLOCK, pos, direction);
      info.setReturnValue(result == ActionResult.SUCCESS);
      info.cancel();
    }
  }

  @Inject(
      at =
          @At(
              value = "INVOKE",
              target = "Lnet/minecraft/world/GameMode;isCreative()Z",
              ordinal = 0),
      method = "updateBlockBreakingProgress",
      cancellable = true)
  public void method_2902(BlockPos pos, Direction direction, CallbackInfoReturnable<Boolean> info) {
    if (!gameMode.isCreative()) {
      return;
    }

    ActionResult result =
        MishangUc.PROGRESS_ATTACK_BLOCK_EVENT
            .invoker()
            .interact(client.player, client.world, Hand.MAIN_HAND, pos, direction);

    if (result != ActionResult.PASS) {
      info.setReturnValue(result == ActionResult.SUCCESS);
      info.cancel();
    }
  }
}
