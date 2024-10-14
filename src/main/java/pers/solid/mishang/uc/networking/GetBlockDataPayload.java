package pers.solid.mishang.uc.networking;

import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import pers.solid.mishang.uc.Mishanguc;

public record GetBlockDataPayload(Identifier blockId, BlockPos blockPos, boolean hasData, NbtCompound data) implements CustomPayload {
  public static final Id<GetBlockDataPayload> ID = new CustomPayload.Id<>(Mishanguc.id("get_block_data"));
  public static final PacketCodec<PacketByteBuf, GetBlockDataPayload> CODEC = PacketCodec.of((value, buf) -> {
    buf.writeIdentifier(value.blockId).writeBlockPos(value.blockPos).writeBoolean(value.hasData);
    if (value.hasData) {
      buf.writeNbt(value.data);
    }
  }, buf -> {
    final Identifier blockId = buf.readIdentifier();
    final BlockPos blockPos = buf.readBlockPos();
    final boolean hasData = buf.readBoolean();
    if (hasData) {
      return new GetBlockDataPayload(blockId, blockPos, true, buf.readNbt());
    } else {
      return new GetBlockDataPayload(blockId, blockPos, false, null);
    }
  });

  @Override
  public Id<? extends CustomPayload> getId() {
    return ID;
  }
}
