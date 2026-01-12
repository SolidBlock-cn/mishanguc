package pers.solid.mishang.uc.render.state;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.rendering.v1.RenderStateDataKey;
import net.fabricmc.fabric.api.client.rendering.v1.world.WorldExtractionContext;
import net.fabricmc.fabric.api.client.rendering.v1.world.WorldRenderEvents;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.render.state.WorldRenderState;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.HitResult;
import org.jetbrains.annotations.Nullable;

@Environment(EnvType.CLIENT)
public interface MishangRenderStateProvider {
  RenderStateDataKey<ItemStack> HAND_STACK = RenderStateDataKey.create(() -> "mishanguc:hand_stack");
  RenderStateDataKey<MishangRenderState> MISHANG_BLOCK_OUTLINE = RenderStateDataKey.create(() -> "mishanguc:block_outline");

  @Environment(EnvType.CLIENT)
  WorldRenderEvents.AfterBlockOutlineExtraction MISHANG_EXTRACTION = (context, result) -> {
    final ClientPlayerEntity player = MinecraftClient.getInstance().player;
    if (player == null) return;
    final WorldRenderState worldRenderState = context.worldState();
    worldRenderState.setData(HAND_STACK, null);
    for (final Hand hand : new Hand[]{Hand.MAIN_HAND, Hand.OFF_HAND}) {
      final ItemStack stackInHand = player.getStackInHand(hand);
      final Item item = stackInHand.getItem();
      if (item instanceof final MishangRenderStateProvider mishangRenderStateProvider) {
        worldRenderState.setData(HAND_STACK, stackInHand);
        worldRenderState.setData(MISHANG_BLOCK_OUTLINE, mishangRenderStateProvider.getMishangRenderState(player, hand, stackInHand, context, result));
        break;
      }
    }
  };

  @Nullable
  default MishangRenderState getMishangRenderState(ClientPlayerEntity player, Hand hand, ItemStack stack, WorldExtractionContext context, @Nullable HitResult result) {
    return null;
  }
}
