package pers.solid.mishang.uc.render;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderContext;
import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderEvents;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import pers.solid.mishang.uc.MishangucClient;

/**
 * 物品实现此接口后，玩家拿着物品时就会调用 {@link #rendersBlockOutline}。{@link #RENDERER} 是个匿名的 {@link
 * WorldRenderEvents.BlockOutline} 实例，并且会在 {@link MishangucClient} 中注册。<br>
 * Item implements this interface, and when a player holds this item, {@link #rendersBlockOutline}
 * will be called. The {@link #RENDERER} is in an anonymous {@link WorldRenderEvents.BlockOutline}
 * instance, and was registered in {@link MishangucClient}.<br>
 * 物品实现此接口时，需要注解为：<br>
 * Items implementing this interface must be annotated as:<br>
 *
 * <pre>
 * {@code @EnvironmentInterface(value = EnvType.CLIENT, itf = RendersBlockOutline.class)}</pre>
 */
@Environment(EnvType.CLIENT)
public interface RendersBlockOutline {
  @Environment(EnvType.CLIENT)
  WorldRenderEvents.BlockOutline RENDERER =
      (worldRenderContext, blockOutlineContext) -> {
        if (!(blockOutlineContext.entity() instanceof final PlayerEntity player)) {
          return true;
        }
        final ItemStack mainHandStack = player.getMainHandStack();
        final Item item = mainHandStack.getItem();
        if (item instanceof final RendersBlockOutline rendersBlockOutline) {
          return rendersBlockOutline
              .rendersBlockOutline(player, mainHandStack, worldRenderContext, blockOutlineContext);
        } else {
          return true;
        }
      };

  /**
   * 玩家持有该物品的物品堆时，进行渲染。将会被 {@link #RENDERER} 中的 {@link
   * WorldRenderEvents.BlockOutline#onBlockOutline} 调用。<br>
   * Render when a player holds an item stack of this item. Called in {@link
   * WorldRenderEvents.BlockOutline#onBlockOutline} of {@link #RENDERER}.<br>
   * 子类覆盖此方法时，必须注解 <code>@{@link Environment}({@link EnvType#CLIENT})</code>。<br>
   * <code>@{@link Environment}({@link EnvType#CLIENT})</code> must be annotated when overrode by
   * subtype methods.
   */
  @Environment(EnvType.CLIENT)
  boolean rendersBlockOutline(
      PlayerEntity player,
      ItemStack itemStack,
      WorldRenderContext worldRenderContext,
      WorldRenderContext.BlockOutlineContext blockOutlineContext);
}
