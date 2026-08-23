package pers.solid.mishang.uc.util;

import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;

import java.util.List;

/**
 * @since MC 1.21.5 自此版本开始，物品的提示被取代，因此使用此接口的方法代替。
 */
public interface WithMishangTooltip {
  void getMishangTooltip(ItemStack stack, Item.TooltipContext context, List<Component> tooltip, TooltipFlag options);
}
