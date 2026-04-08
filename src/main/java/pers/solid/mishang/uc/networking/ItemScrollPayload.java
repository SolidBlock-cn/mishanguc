package pers.solid.mishang.uc.networking;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import pers.solid.mishang.uc.Mishanguc;

public record ItemScrollPayload(int selectedSlot, double scrollAmount) implements CustomPacketPayload {
  public static final Type<ItemScrollPayload> ID = new CustomPacketPayload.Type<>(Mishanguc.id("item_scroll"));
  public static final StreamCodec<FriendlyByteBuf, ItemScrollPayload> CODEC = StreamCodec.ofMember((value, buf) -> buf.writeInt(value.selectedSlot).writeDouble(value.scrollAmount), buf -> new ItemScrollPayload(buf.readInt(), buf.readDouble()));

  @Override
  public Type<? extends CustomPacketPayload> type() {
    return ID;
  }
}
