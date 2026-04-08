package pers.solid.mishang.uc.networking;

import pers.solid.mishang.uc.Mishanguc;

import java.util.Optional;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.world.phys.BlockHitResult;

public record EditSignPayload(BlockPos blockPos, Optional<Direction> direction, Optional<BlockHitResult> blockHitResult) implements CustomPacketPayload {
  public static final Type<EditSignPayload> ID = new CustomPacketPayload.Type<>(Mishanguc.id("edit_sign"));

  public static final StreamCodec<FriendlyByteBuf, EditSignPayload> CODEC = StreamCodec.ofMember((value, buf) -> {
    buf.writeBlockPos(value.blockPos);
    if (value.direction.isPresent()) {
      buf.writeBoolean(true);
      buf.writeEnum(value.direction.get());
    } else {
      buf.writeBoolean(false);
      if (value.blockHitResult.isPresent()) {
        buf.writeBoolean(true);
        buf.writeBlockHitResult(value.blockHitResult.get());
      } else {
        buf.writeBoolean(false);
      }
    }
  }, buf -> {
    final BlockPos blockPos = buf.readBlockPos();
    final boolean directionPresent = buf.readBoolean();
    if (directionPresent) {
      return new EditSignPayload(blockPos, Optional.of(buf.readEnum(Direction.class)), Optional.empty());
    } else {
      final boolean hitPresent = buf.readBoolean();
      if (hitPresent) {
        return new EditSignPayload(blockPos, Optional.empty(), Optional.of(buf.readBlockHitResult()));
      } else {
        return new EditSignPayload(blockPos, Optional.empty(), Optional.empty());
      }
    }
  });

  @Override
  public Type<? extends CustomPacketPayload> type() {
    return ID;
  }
}
