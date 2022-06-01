package pers.solid.mishang.uc.item;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;

/**
 * 实现此接口的物体，在滚动鼠标滚轮时，会暂时锁定快捷栏，并进行相应的特殊处理。
 */
public interface HotbarScrollInteraction {
  /**
   * 玩家持有此物品并在客户端滚动鼠标滚轮时调用此方法。<p>
   * 此方法仅限在客户端滚动鼠标滚轮时执行，<b>在服务器无效</b>。注意该方法不是纯方法，调用此方法可能会执行一些操作。<p>
   * 此方法的默认值为，向服务器发送封包，服务器收到封包后调用 {@link #onScroll}。
   *
   * @param scrollAmount 滚动鼠标滚轮的数量。可能是正的，也有可能是负的。
   * @return 是否应该锁定快捷栏的选择。如果为 true，则当期选择的快捷栏槽位不会改变。
   */
  @Environment(EnvType.CLIENT)
  default boolean shouldLockScroll(int selectedSlot, double scrollAmount) {
    if (Screen.hasShiftDown() || Screen.hasAltDown()) {
      final PacketByteBuf buf = PacketByteBufs.create();
      buf.writeInt(selectedSlot);
      buf.writeDouble(scrollAmount);
      ClientPlayNetworking.send(new Identifier("mishanguc", "item_scroll"), buf);
      return true;
    } else {
      return false;
    }
  }

  /**
   * 服务器收到关于滚动快捷栏时调用方法的封包后执行的方法。
   *
   * @param selectedSlot 当前玩家选中的物品槽位，由封包决定。
   * @param scrollAmount 滚动的数量，由封包决定。
   * @param player       滚动了该物品的玩家。
   */
  void onScroll(int selectedSlot, double scrollAmount, ServerPlayerEntity player, ItemStack stack);
}
