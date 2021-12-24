package pers.solid.mishang.uc.render;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderContext;
import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderEvents;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import pers.solid.mishang.uc.MishangUcClient;

/**
 * Item implements this interface, and when a player holds this item, {@link #rendersBlockOutline} will be called.
 * The {@link #RENDERER} is in an anonymous {@link WorldRenderEvents.BlockOutline} instance, and was registered in {@link MishangUcClient}.
 */
@Environment(EnvType.CLIENT)
public interface RendersBlockOutline {
  WorldRenderEvents.BlockOutline RENDERER = (worldRenderContext, blockOutlineContext) -> {
    final PlayerEntity player;
    if (blockOutlineContext.entity() instanceof PlayerEntity) {
      player = (PlayerEntity) blockOutlineContext.entity();
    } else {
      return true;
    }
    final ItemStack mainHandStack = player.getMainHandStack();
    final Item item = mainHandStack.getItem();
    if (item instanceof RendersBlockOutline) {
      return ((RendersBlockOutline) item).rendersBlockOutline(player, mainHandStack, worldRenderContext, blockOutlineContext);
    } else {
      return true;
    }
  };

  /**
   * 玩家持有该物品的物品堆时，进行渲染。将会被 {@link #RENDERER} 中的 {@link WorldRenderEvents.BlockOutline#onBlockOutline} 调用。<br>
   * Render when a player holds an item stack of this item. Called in {@link WorldRenderEvents.BlockOutline#onBlockOutline} of {@link #RENDERER}.
   */
  @Environment(EnvType.CLIENT)
  boolean rendersBlockOutline(PlayerEntity player, ItemStack itemStack, WorldRenderContext worldRenderContext, WorldRenderContext.BlockOutlineContext blockOutlineContext);
}
