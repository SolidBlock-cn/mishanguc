package pers.solid.mishang.uc.components;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;

public record TextCopyToolComponent(boolean fromVanillaSign) {
  public static final TextCopyToolComponent DEFAULT = new TextCopyToolComponent(false);
  public static final Codec<TextCopyToolComponent> CODEC = RecordCodecBuilder.create(i -> i.group(Codec.BOOL.optionalFieldOf("from_vanilla_sign", false).forGetter(TextCopyToolComponent::fromVanillaSign)).apply(i, TextCopyToolComponent::new));
  public static final StreamCodec<RegistryFriendlyByteBuf, TextCopyToolComponent> PACKET_CODEC = StreamCodec.composite(ByteBufCodecs.BOOL, TextCopyToolComponent::fromVanillaSign, TextCopyToolComponent::new);
}
