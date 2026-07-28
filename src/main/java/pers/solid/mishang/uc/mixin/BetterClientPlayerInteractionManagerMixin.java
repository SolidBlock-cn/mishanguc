package pers.solid.mishang.uc.mixin;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.multiplayer.MultiPlayerGameMode;
import net.minecraft.client.multiplayer.prediction.PredictiveAction;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.protocol.game.ServerboundPlayerActionPacket;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.level.GameType;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import pers.solid.mishang.uc.Mishanguc;

/**
 * 本 mixin 参考了 {@code ClientPlayerInteractionManagerMixin}（仅限旧版本），不过这个 mixin
 * 有个很大的问题，一是在客户端结果不为 PASS 时会阻止产生 packet 导致服务器不执行该 callback，而且方块破坏过程中也会执行该 mixin。因此改进了此 mixin。<br>
 * 该 mixin 具有两个特点：一是在客户端结果不返回 PASS 时也会发送 packet，这样可以让服务器也执行，二是分类开始破坏和中途破坏两个情况。
 */
@Environment(EnvType.CLIENT)
@Mixin(MultiPlayerGameMode.class)
public abstract class BetterClientPlayerInteractionManagerMixin {

  @Shadow
  @Final
  private Minecraft minecraft;
  @Shadow
  private GameType localPlayerMode;

  @Shadow
  protected abstract void startPrediction(ClientLevel world, PredictiveAction packetCreator);

  @Inject(
      at = {@At(
          value = "INVOKE",
          target = "Lnet/minecraft/client/player/LocalPlayer;getAbilities()Lnet/minecraft/world/entity/player/Abilities;",
          ordinal = 0
      )},
      method = "startDestroyBlock",
      cancellable = true)
  public void attackBlock(BlockPos loc, Direction face, CallbackInfoReturnable<Boolean> info) {
    InteractionResult result =
        Mishanguc.BEGIN_ATTACK_BLOCK_EVENT
            .invoker()
            .interact(minecraft.player, minecraft.level, InteractionHand.MAIN_HAND, loc, face);

    if (result != InteractionResult.PASS) {
      startPrediction(this.minecraft.level, (sequence) -> new ServerboundPlayerActionPacket(ServerboundPlayerActionPacket.Action.START_DESTROY_BLOCK, loc, face, sequence));
      info.setReturnValue(result == InteractionResult.SUCCESS);
      info.cancel();
    }
  }

  @Inject(
      at = {@At(
          value = "INVOKE",
          target = "Lnet/minecraft/client/player/LocalPlayer;getAbilities()Lnet/minecraft/world/entity/player/Abilities;",
          ordinal = 0
      )},
      method = "continueDestroyBlock",
      cancellable = true)
  public void method_2902(BlockPos pos, Direction direction, CallbackInfoReturnable<Boolean> info) {
    if (!localPlayerMode.isCreative()) {
      return;
    }

    InteractionResult result =
        Mishanguc.PROGRESS_ATTACK_BLOCK_EVENT
            .invoker()
            .interact(minecraft.player, minecraft.level, InteractionHand.MAIN_HAND, pos, direction);

    if (result != InteractionResult.PASS) {
      info.setReturnValue(result == InteractionResult.SUCCESS);
      info.cancel();
    }
  }

  /**
   * 在原版MC中，处理破坏过程中，仍有可能执行 attackBlock。当玩家持有此类物品时，不再击打方块。这种情况通常在生存模式出现。
   */
  @Inject(at = @At(value = "INVOKE", target = "Lnet/minecraft/client/multiplayer/MultiPlayerGameMode;startDestroyBlock(Lnet/minecraft/core/BlockPos;Lnet/minecraft/core/Direction;)Z", ordinal = 0), method = "continueDestroyBlock", cancellable = true)
  public void doNotAttack(BlockPos pos, Direction direction, CallbackInfoReturnable<Boolean> cir) {
    InteractionResult result =
        Mishanguc.PROGRESS_ATTACK_BLOCK_EVENT
            .invoker()
            .interact(minecraft.player, minecraft.level, InteractionHand.MAIN_HAND, pos, direction);

    if (result != InteractionResult.PASS) {
      cir.setReturnValue(result == InteractionResult.SUCCESS);
      cir.cancel();
    }
  }
}
