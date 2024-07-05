package pers.solid.mishang.uc.networking;

import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.util.Identifier;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;

import java.util.Optional;

public record EditSignPayload(BlockPos blockPos, Optional<Direction> direction, Optional<BlockHitResult> blockHitResult) implements CustomPayload {
  public static final Id<EditSignPayload> ID = new CustomPayload.Id<>(Identifier.of("mishanguc", "edit_sign"));

  public static final PacketCodec<PacketByteBuf, EditSignPayload> CODEC = PacketCodec.of((value, buf) -> {
    buf.writeBlockPos(value.blockPos);
    if (value.direction.isPresent()) {
      buf.writeBoolean(true);
      buf.writeEnumConstant(value.direction.get());
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
      return new EditSignPayload(blockPos, Optional.of(buf.readEnumConstant(Direction.class)), Optional.empty());
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
  public Id<? extends CustomPayload> getId() {
    return ID;
  }
}
