package pers.solid.mishang.uc.render;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderContext;
import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderEvents;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import pers.solid.mishang.uc.MishangucClient;

/**
 * 物品实现此接口后，玩家拿着物品时就会调用 {@link #renderBlockOutline}。{@link #RENDERER} 是个匿名的 {@link
 * WorldRenderEvents.BlockOutline} 实例，并且会在 {@link MishangucClient} 中注册。<br>
 * Item implements this interface, and when a player holds this item, {@link #renderBlockOutline}
 * will be called. The {@link #RENDERER} is in an anonymous {@link WorldRenderEvents.BlockOutline}
 * instance, and was registered in {@link MishangucClient}.<p>
 * 物品实现此接口时，需要注解为：<br>
 * Items implementing this interface must be annotated as:
 *
 * <pre>
 * {@code @EnvironmentInterface(value = EnvType.CLIENT, itf = RendersBlockOutline.class)}</pre>
 */
@Environment(EnvType.CLIENT)
public interface RendersBlockOutline {
  @Environment(EnvType.CLIENT)
  WorldRenderEvents.BlockOutline RENDERER =
      (worldRenderContext, blockOutlineContext) -> {
        final PlayerEntity player;
        if (blockOutlineContext.entity() instanceof PlayerEntity) {
          player = (PlayerEntity) blockOutlineContext.entity();
        } else {
          return true;
        }
        for (final Hand hand : new Hand[]{Hand.MAIN_HAND, Hand.OFF_HAND}) {
          final ItemStack stackInHand = player.getStackInHand(hand);
          final Item item = stackInHand.getItem();
          if (item instanceof RendersBlockOutline) {
            if (!((RendersBlockOutline) item).renderBlockOutline(player, stackInHand, worldRenderContext, blockOutlineContext, hand)) {
              return false;
            }
          }
        }
        return true;
      };

  /**
   * 玩家持有该物品的物品堆时，进行渲染。将会被 {@link #RENDERER} 中的 {@link WorldRenderEvents.BlockOutline#onBlockOutline} 调用。<br>
   * Render when a player holds an item stack of this item. Called in {@link WorldRenderEvents.BlockOutline#onBlockOutline} of {@link #RENDERER}.<p>
   * 子类覆盖此方法时，必须注解 <code>@{@link Environment}({@link EnvType#CLIENT})</code>。<br>
   * <code>@{@link Environment}({@link EnvType#CLIENT})</code> must be annotated when overrode by
   * subtype methods.
   *
   * @since 0.2.0 加入了参数 hand，表示持有此物品的手。这是考虑到主手和副手都有可能持有此物品，当副手持有此物品时，只能应用“使用”效果，但不能应用“攻击”效果。此参数可以用来进行区分。
   */
  @Environment(EnvType.CLIENT)
  boolean renderBlockOutline(
      PlayerEntity player,
      ItemStack itemStack,
      WorldRenderContext worldRenderContext,
      WorldRenderContext.BlockOutlineContext blockOutlineContext, Hand hand);
}
