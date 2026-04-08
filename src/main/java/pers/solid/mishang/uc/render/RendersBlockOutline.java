package pers.solid.mishang.uc.render;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.rendering.v1.world.WorldRenderContext;
import net.fabricmc.fabric.api.client.rendering.v1.world.WorldRenderEvents;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.state.BlockOutlineRenderState;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import pers.solid.mishang.uc.MishangucClient;
import pers.solid.mishang.uc.render.state.MishangRenderStateProvider;

/**
 * <p>物品实现此接口后，玩家拿着物品时就会调用 {@link #renderBlockOutline}。{@link #RENDERER} 是个匿名的 {@link
 * WorldRenderEvents.BeforeBlockOutline} 实例，并且会在 {@link MishangucClient} 中注册。
 * <p>Item implements this interface, and when a player holds this item, {@link #renderBlockOutline}
 * will be called. The {@link #RENDERER} is in an anonymous {@link WorldRenderEvents.BeforeBlockOutline}
 * instance, and was registered in {@link MishangucClient}.
 * <p>自 1.21.10 开始，渲染时需要使用通过 {@link MishangRenderStateProvider#getMishangRenderState} 返回的 render state。
 * <p>Since 1.21.10, the render state returned through {@link MishangRenderStateProvider#getMishangRenderState} has been needed when rendering.
 * <p>物品实现此接口时，需要注解为：
 * <p>Items implementing this interface must be annotated as:
 *
 * <pre>
 * {@code @EnvironmentInterface(value = EnvType.CLIENT, itf = RendersBlockOutline.class)}</pre>
 */
@Environment(EnvType.CLIENT)
public interface RendersBlockOutline extends MishangRenderStateProvider {

  @Environment(EnvType.CLIENT)
  WorldRenderEvents.BeforeBlockOutline RENDERER = (worldRenderContext, outlineRenderState) -> {
    final LocalPlayer player = Minecraft.getInstance().player;
    final ItemStack stack = worldRenderContext.worldState().getData(MishangRenderStateProvider.HAND_STACK);
    if (stack == null) return true;
    if (player == null) return true;
    final Item item = stack.getItem();
    if (item instanceof final RendersBlockOutline rendersBlockOutline) {
      return rendersBlockOutline.renderBlockOutline(player, stack, worldRenderContext, outlineRenderState);
    }

    return true;
  };


  /**
   * <p>玩家持有该物品的物品堆时，进行渲染。将会被 {@link #RENDERER} 中的 {@link WorldRenderEvents.BeforeBlockOutline#beforeBlockOutline} 调用。
   * <p>Render when a player holds an item stack of this item. Called in {@link WorldRenderEvents.BeforeBlockOutline#beforeBlockOutline} of {@link #RENDERER}.
   * <p>子类覆盖此方法时，必须注解 <code>@{@link Environment}({@link EnvType#CLIENT})</code>。
   * <p><code>@{@link Environment}({@link EnvType#CLIENT})</code> must be annotated when overridden by
   * subtype methods.
   *
   * @since 0.2.0 加入了参数 hand，表示持有此物品的手。这是考虑到主手和副手都有可能持有此物品，当副手持有此物品时，只能应用“使用”效果，但不能应用“攻击”效果。此参数可以用来进行区分。
   */
  @Environment(EnvType.CLIENT)
  boolean renderBlockOutline(
      Player player,
      ItemStack itemStack,
      WorldRenderContext context,
      BlockOutlineRenderState outlineRenderState);
}
