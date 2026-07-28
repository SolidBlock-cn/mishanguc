package pers.solid.mishang.uc.networking;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.ComponentSerialization;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import pers.solid.mishang.uc.Mishanguc;

public record GetEntityDataPayload(Component entityName, BlockPos blockPos, CompoundTag entityNbt) implements CustomPacketPayload {
  public static final Type<GetEntityDataPayload> ID = new CustomPacketPayload.Type<>(Mishanguc.id("get_entity"));
  public static final StreamCodec<FriendlyByteBuf, GetEntityDataPayload> CODEC = StreamCodec.ofMember((value, buf) -> buf.writeNbt(ComponentSerialization.CODEC.encodeStart(NbtOps.INSTANCE, value.entityName).getOrThrow()).writeBlockPos(value.blockPos).writeNbt(value.entityNbt), buf -> new GetEntityDataPayload(ComponentSerialization.CODEC.parse(NbtOps.INSTANCE, buf.readNbt()).getOrThrow(), buf.readBlockPos(), buf.readNbt()));

  @Override
  public Type<? extends CustomPacketPayload> type() {
    return ID;
  }
}
