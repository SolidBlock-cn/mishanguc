package pers.solid.mishang.uc.networking;

import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.util.math.BlockPos;

public record SignEditFinishPayload(BlockPos blockPos, NbtCompound nbt) implements CustomPayload {
  public static final Id<SignEditFinishPayload> ID = CustomPayload.id("mishanguc:edit_sign_finish");
  public static final PacketCodec<PacketByteBuf, SignEditFinishPayload> CODEC = PacketCodec.of((value, buf) -> buf.writeBlockPos(value.blockPos).writeNbt(value.nbt), buf -> new SignEditFinishPayload(buf.readBlockPos(), buf.readNbt()));

  @Override
  public Id<? extends CustomPayload> getId() {
    return ID;
  }
}
