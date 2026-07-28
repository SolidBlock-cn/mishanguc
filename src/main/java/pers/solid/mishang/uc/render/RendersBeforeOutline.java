package pers.solid.mishang.uc.render;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.rendering.v1.world.WorldRenderContext;
import net.fabricmc.fabric.api.client.rendering.v1.world.WorldRenderEvents;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import pers.solid.mishang.uc.render.state.MishangRenderStateProvider;

/**
 * 实现此方法的物品，在玩家持有时（无论是主手还是副手），均会调用 {@link #renderBeforeOutline} 方法。这是在 {@link pers.solid.mishang.uc.MishangucClient} 中注册的。<p>
 * <p>
 * 此方法为客户端专有，因此实现此方法时，务必注解为 {@link net.fabricmc.api.EnvironmentInterface}，并在重写的方法上注解 {@link net.fabricmc.api.Environment}。
 */
@Environment(EnvType.CLIENT)
public interface RendersBeforeOutline extends MishangRenderStateProvider {
  /**
   * 玩家持有此物品时，客户端进行的渲染操作。仅限客户端执行。覆盖此方法时，请一并注解上 {@link net.fabricmc.api.Environment}。<p>
   * 注意该方法是在旁观者检查之前调用的，也就是说，即使是在旁观模式下也会调用此方法，因此你可能需要手动检查玩家是否为旁观模式。
   *
   * @param player  执行此渲染的客户端玩家。
   * @param stack   玩家持有此物品的手，可以用来指定只有玩家使用特定的手持有此物品时才会进行渲染。
   * @param context 当前渲染场景中的参数，参见 {@link WorldRenderEvents#BEFORE_DEBUG_RENDER}。
   */
  void renderBeforeOutline(ClientPlayerEntity player, ItemStack stack, WorldRenderContext context);

  WorldRenderEvents.DebugRender DEBUG_RENDER = context -> {
    final ClientPlayerEntity player = MinecraftClient.getInstance().player;
    final ItemStack stack = context.worldState().getData(MishangRenderStateProvider.HAND_STACK);
    if (stack == null) return;
    if (player == null) return;
    final Item item = stack.getItem();
    if (item instanceof final RendersBeforeOutline rendersBlockOutline) {
      rendersBlockOutline.renderBeforeOutline(player, stack, context);
    }
  };
}
