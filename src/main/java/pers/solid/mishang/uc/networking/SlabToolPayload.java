package pers.solid.mishang.uc.networking;

import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import pers.solid.mishang.uc.Mishanguc;

public record SlabToolPayload(BlockPos blockPos, boolean isTop) implements CustomPacketPayload {
  public static final Type<SlabToolPayload> ID = new CustomPacketPayload.Type<>(Mishanguc.id("slab_tool"));
  public static final StreamCodec<FriendlyByteBuf, SlabToolPayload> CODEC = StreamCodec.ofMember((value, buf) -> buf.writeBlockPos(value.blockPos).writeBoolean(value.isTop), buf -> new SlabToolPayload(buf.readBlockPos(), buf.readBoolean()));

  @Override
  public Type<? extends CustomPacketPayload> type() {
    return ID;
  }
}
