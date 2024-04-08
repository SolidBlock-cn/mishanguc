package pers.solid.mishang.uc.networking;

import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.packet.CustomPayload;

public record ItemScrollPayload(int selectedSlot, double scrollAmount) implements CustomPayload {
  public static final Id<ItemScrollPayload> ID = CustomPayload.id("mishanguc:item_scroll");
  public static final PacketCodec<PacketByteBuf, ItemScrollPayload> CODEC = PacketCodec.of((value, buf) -> buf.writeInt(value.selectedSlot).writeDouble(value.scrollAmount), buf -> new ItemScrollPayload(buf.readInt(), buf.readDouble()));

  @Override
  public Id<? extends CustomPayload> getId() {
    return ID;
  }
}
