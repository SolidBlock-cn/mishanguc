package pers.solid.mishang.uc.components;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import pers.solid.mishang.uc.util.BlockMatchingRule;

public record FastBuildingToolData(int range, BlockMatchingRule matchingRule) {
  public static final FastBuildingToolData DEFAULT = new FastBuildingToolData(5, BlockMatchingRule.SAME_BLOCK);
  public static final Codec<FastBuildingToolData> CODEC = RecordCodecBuilder.create(i -> i.group(
      Codec.INT.optionalFieldOf("range", 5).forGetter(FastBuildingToolData::range),
      BlockMatchingRule.REGISTRY.getCodec().optionalFieldOf("matching_rule", BlockMatchingRule.SAME_BLOCK).forGetter(FastBuildingToolData::matchingRule)
  ).apply(i, FastBuildingToolData::new));
  public static final PacketCodec<RegistryByteBuf, FastBuildingToolData> PACKET_CODEC = PacketCodec.tuple(PacketCodecs.INTEGER, FastBuildingToolData::range, PacketCodecs.registryValue(BlockMatchingRule.REGISTRY_KEY), FastBuildingToolData::matchingRule, FastBuildingToolData::new);
}
