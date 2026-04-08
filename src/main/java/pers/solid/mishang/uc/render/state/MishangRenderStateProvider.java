package pers.solid.mishang.uc.render.state;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.rendering.v1.FabricRenderState;
import net.fabricmc.fabric.api.client.rendering.v1.RenderStateDataKey;
import net.fabricmc.fabric.api.client.rendering.v1.level.LevelExtractionContext;
import net.fabricmc.fabric.api.client.rendering.v1.level.LevelRenderEvents;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.state.level.LevelRenderState;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.HitResult;
import org.jetbrains.annotations.Nullable;
import pers.solid.mishang.uc.render.RendersBlockOutline;

/**
 * 物品实现此接口可在提取方块轮廓数据时设置额外的数据。使用 {@link #getMishangRenderState} 返回需要设置的额外数据，从而在渲染时可使用这些额外数据。这些额外数据的存储是通过 Fabric API 中的 {@link FabricRenderState} 实现。
 *
 * @see FabricRenderState#getData
 * @see FabricRenderState#setData
 * @since Minecraft 1.21.10
 */
@Environment(EnvType.CLIENT)
public interface MishangRenderStateProvider {
  /**
   * 存储主手物品堆数据的键。只有在手持物品是实现了 {@link MishangRenderStateProvider} 的物品时，才会存储此 render state。存储的 render state 将用作 {@link RendersBlockOutline#renderBlockOutline} 的 {@code itemStack} 参数。
   */
  RenderStateDataKey<ItemStack> HAND_STACK = RenderStateDataKey.create(() -> "mishanguc:hand_stack");
  /**
   * 存储本模组中的 render state 的键。
   */
  RenderStateDataKey<MishangRenderState> MISHANG_BLOCK_OUTLINE = RenderStateDataKey.create(() -> "mishanguc:block_outline");


  LevelRenderEvents.AfterBlockOutlineExtraction MISHANG_EXTRACTION = (context, result) -> {
    final LocalPlayer player = Minecraft.getInstance().player;
    if (player == null) return;
    final LevelRenderState worldRenderState = context.levelState();
    worldRenderState.setData(HAND_STACK, null);
    for (final InteractionHand hand : new InteractionHand[]{InteractionHand.MAIN_HAND, InteractionHand.OFF_HAND}) {
      final ItemStack stackInHand = player.getItemInHand(hand);
      final Item item = stackInHand.getItem();
      if (item instanceof final MishangRenderStateProvider mishangRenderStateProvider) {
        worldRenderState.setData(HAND_STACK, stackInHand);
        worldRenderState.setData(MISHANG_BLOCK_OUTLINE, mishangRenderStateProvider.getMishangRenderState(player, hand, stackInHand, context, result));
        break;
      }
    }
  };

  /**
   * 覆盖此方法以在渲染数据提取过程中存储 render state。可以返回 null。
   *
   * @param player 执行渲染的玩家，通常相当于 {@code MinecraftClient.getInstance().player}，用作参数通常是为了方便。
   * @param hand   玩家手持此物品时使用的手。
   * @param stack  玩家手中触发了此渲染逻辑的物品堆。
   */
  @Nullable
  default MishangRenderState getMishangRenderState(LocalPlayer player, InteractionHand hand, ItemStack stack, LevelExtractionContext context, @Nullable HitResult result) {
    return null;
  }
}
