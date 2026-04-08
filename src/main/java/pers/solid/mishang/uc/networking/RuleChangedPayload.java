package pers.solid.mishang.uc.networking;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import pers.solid.mishang.uc.Mishanguc;
import pers.solid.mishang.uc.MishangucRules;

public record RuleChangedPayload(short ruleType, MishangucRules.ToolAccess toolAccess) implements CustomPacketPayload {
  public static final Type<RuleChangedPayload> ID = new CustomPacketPayload.Type<>(Mishanguc.id("rule_changed"));

  @Override
  public Type<RuleChangedPayload> type() {
    return ID;
  }

  public static final StreamCodec<FriendlyByteBuf, RuleChangedPayload> CODEC = StreamCodec.ofMember((value, buf) -> buf.writeShort(value.ruleType).writeEnum(value.toolAccess), buf -> new RuleChangedPayload(buf.readShort(), buf.readEnum(MishangucRules.ToolAccess.class)));
}
