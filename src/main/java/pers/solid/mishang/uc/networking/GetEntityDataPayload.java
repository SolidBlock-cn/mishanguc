package pers.solid.mishang.uc.networking;

import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtOps;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.text.Text;
import net.minecraft.text.TextCodecs;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;

public record GetEntityDataPayload(Text entityName, BlockPos blockPos, NbtCompound entityNbt) implements CustomPayload {
  public static final Id<GetEntityDataPayload> ID = new CustomPayload.Id<>(Identifier.of("mishanguc", "get_entity"));
  public static final PacketCodec<PacketByteBuf, GetEntityDataPayload> CODEC = PacketCodec.of((value, buf) -> buf.writeNbt(TextCodecs.CODEC.encodeStart(NbtOps.INSTANCE, value.entityName).getOrThrow()).writeBlockPos(value.blockPos).writeNbt(value.entityNbt), buf -> new GetEntityDataPayload(TextCodecs.CODEC.parse(NbtOps.INSTANCE, buf.readNbt()).getOrThrow(), buf.readBlockPos(), buf.readNbt()));

  @Override
  public Id<? extends CustomPayload> getId() {
    return ID;
  }
}
