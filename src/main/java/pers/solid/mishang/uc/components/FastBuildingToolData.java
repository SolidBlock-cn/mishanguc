package pers.solid.mishang.uc.components;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.component.ComponentsAccess;
import net.minecraft.item.Item;
import net.minecraft.item.tooltip.TooltipAppender;
import net.minecraft.item.tooltip.TooltipType;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import pers.solid.mishang.uc.util.BlockMatchingRule;
import pers.solid.mishang.uc.util.TextBridge;

import java.util.function.Consumer;

public record FastBuildingToolData(int range, BlockMatchingRule matchingRule) implements TooltipAppender {
  public static final FastBuildingToolData DEFAULT = new FastBuildingToolData(5, BlockMatchingRule.SAME_BLOCK);
  public static final Codec<FastBuildingToolData> CODEC = RecordCodecBuilder.create(i -> i.group(
      Codec.INT.optionalFieldOf("range", 5).forGetter(FastBuildingToolData::range),
      BlockMatchingRule.REGISTRY.getCodec().optionalFieldOf("matching_rule", BlockMatchingRule.SAME_BLOCK).forGetter(FastBuildingToolData::matchingRule)
  ).apply(i, FastBuildingToolData::new));
  public static final PacketCodec<RegistryByteBuf, FastBuildingToolData> PACKET_CODEC = PacketCodec.tuple(PacketCodecs.INTEGER, FastBuildingToolData::range, PacketCodecs.registryValue(BlockMatchingRule.REGISTRY_KEY), FastBuildingToolData::matchingRule, FastBuildingToolData::new);

  @Override
  public void appendTooltip(Item.TooltipContext context, Consumer<Text> textConsumer, TooltipType type, ComponentsAccess components) {
    textConsumer.accept(TextBridge.translatable("item.mishanguc.fast_building_tool.tooltip.range", TextBridge.literal(Integer.toString(range())).formatted(Formatting.YELLOW))
        .formatted(Formatting.GRAY));
    textConsumer.accept(TextBridge.translatable("item.mishanguc.fast_building_tool.tooltip.matchingRule", matchingRule().getName().formatted(Formatting.YELLOW))
        .formatted(Formatting.GRAY));
  }
}
