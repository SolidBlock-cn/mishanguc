package pers.solid.mishang.uc.render;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderContext;
import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderEvents;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.HitResult;

/**
 * 实现此方法的物品，在玩家持有时（无论是主手还是副手），均会调用 {@link #renderBeforeOutline(WorldRenderContext, HitResult, ClientPlayerEntity, Hand)} 方法。这是在 {@link pers.solid.mishang.uc.MishangucClient} 中注册的。<p>
 * <p>
 * 此方法为客户端专有，因此实现此方法时，务必注解为 {@link net.fabricmc.api.EnvironmentInterface}，并在重写的方法上注解 {@link net.fabricmc.api.Environment}。
 */
@Environment(EnvType.CLIENT)
public interface RendersBeforeOutline {
  /**
   * 玩家持有此物品时，客户端进行的渲染操作。仅限客户端执行。覆盖此方法时，请一并注解上 {@link net.fabricmc.api.Environment}。<p>
   * 注意该方法是在旁观者检查之前调用的，也就是说，即使是在旁观模式下也会调用此方法，因此你可能需要手动检查玩家是否为旁观模式。
   *
   * @param context   当前渲染场景中的参数，参见 {@link WorldRenderEvents#BEFORE_DEBUG_RENDER}。
   * @param hitResult 客户端的追星目标。
   * @param player    执行此渲染的客户端玩家。
   * @param hand      玩家持有此物品的手，可以用来指定只有玩家使用特定的手持有此物品时才会进行渲染。
   */
  void renderBeforeOutline(
      WorldRenderContext context,
      HitResult hitResult,
      ClientPlayerEntity player, Hand hand);

  WorldRenderEvents.BeforeBlockOutline RENDERER = (context, hitResult) -> {
    final MinecraftClient client = MinecraftClient.getInstance();
    final ClientPlayerEntity player = client.player;
    if (player == null) return true;
    for (final Hand hand : new Hand[]{Hand.MAIN_HAND, Hand.OFF_HAND}) {
      final ItemStack stackInHand = player.getStackInHand(hand);
      if (stackInHand.getItem() instanceof RendersBeforeOutline rendersBeforeOutline) {
        rendersBeforeOutline.renderBeforeOutline(context, hitResult, player, hand);
      }
    }
    return true;
  };
}
