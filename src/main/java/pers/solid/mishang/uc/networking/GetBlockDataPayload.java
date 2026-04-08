package pers.solid.mishang.uc.networking;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.Identifier;
import pers.solid.mishang.uc.Mishanguc;

public record GetBlockDataPayload(Identifier blockId, BlockPos blockPos, boolean hasData, CompoundTag data) implements CustomPacketPayload {
  public static final Type<GetBlockDataPayload> ID = new CustomPacketPayload.Type<>(Mishanguc.id("get_block_data"));
  public static final StreamCodec<FriendlyByteBuf, GetBlockDataPayload> CODEC = StreamCodec.ofMember((value, buf) -> {
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
  public Type<? extends CustomPacketPayload> type() {
    return ID;
  }
}
