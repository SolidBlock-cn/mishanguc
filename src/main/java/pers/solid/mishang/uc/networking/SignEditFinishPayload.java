package pers.solid.mishang.uc.networking;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import pers.solid.mishang.uc.Mishanguc;

public record SignEditFinishPayload(BlockPos blockPos, CompoundTag nbt) implements CustomPacketPayload {
  public static final Type<SignEditFinishPayload> ID = new CustomPacketPayload.Type<>(Mishanguc.id("sign_edit_finish"));
  public static final StreamCodec<FriendlyByteBuf, SignEditFinishPayload> CODEC = StreamCodec.ofMember((value, buf) -> buf.writeBlockPos(value.blockPos).writeNbt(value.nbt), buf -> new SignEditFinishPayload(buf.readBlockPos(), buf.readNbt()));

  @Override
  public Type<? extends CustomPacketPayload> type() {
    return ID;
  }
}
