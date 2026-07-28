package pers.solid.mishang.uc.networking;

import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.util.math.BlockPos;
import pers.solid.mishang.uc.Mishanguc;

public record SlabToolPayload(BlockPos blockPos, boolean isTop) implements CustomPayload {
  public static final Id<SlabToolPayload> ID = new CustomPayload.Id<>(Mishanguc.id("slab_tool"));
  public static final PacketCodec<PacketByteBuf, SlabToolPayload> CODEC = PacketCodec.of((value, buf) -> buf.writeBlockPos(value.blockPos).writeBoolean(value.isTop), buf -> new SlabToolPayload(buf.readBlockPos(), buf.readBoolean()));

  @Override
  public Id<? extends CustomPayload> getId() {
    return ID;
  }
}
