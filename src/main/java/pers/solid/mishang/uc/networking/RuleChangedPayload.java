package pers.solid.mishang.uc.networking;

import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.util.Identifier;
import pers.solid.mishang.uc.MishangucRules;

public record RuleChangedPayload(short type, MishangucRules.ToolAccess toolAccess) implements CustomPayload {
  public static final Id<RuleChangedPayload> ID = new CustomPayload.Id<>(Identifier.of("mishanguc", "rule_changed"));

  @Override
  public Id<RuleChangedPayload> getId() {
    return ID;
  }

  public static final PacketCodec<PacketByteBuf, RuleChangedPayload> CODEC = PacketCodec.of((value, buf) -> buf.writeShort(value.type).writeEnumConstant(value.toolAccess), buf -> new RuleChangedPayload(buf.readShort(), buf.readEnumConstant(MishangucRules.ToolAccess.class)));
}
