package pers.solid.mishang.uc.components;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.ChatFormatting;
import net.minecraft.core.component.DataComponentGetter;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.TooltipProvider;
import pers.solid.mishang.uc.util.BlockMatchingRule;

import java.util.function.Consumer;

public record FastBuildingToolData(int range, BlockMatchingRule matchingRule) implements TooltipProvider {
  public static final FastBuildingToolData DEFAULT = new FastBuildingToolData(5, BlockMatchingRule.SAME_BLOCK);
  public static final Codec<FastBuildingToolData> CODEC = RecordCodecBuilder.create(i -> i.group(
      Codec.INT.optionalFieldOf("range", 5).forGetter(FastBuildingToolData::range),
      BlockMatchingRule.REGISTRY.byNameCodec().optionalFieldOf("matching_rule", BlockMatchingRule.SAME_BLOCK).forGetter(FastBuildingToolData::matchingRule)
  ).apply(i, FastBuildingToolData::new));
  public static final StreamCodec<RegistryFriendlyByteBuf, FastBuildingToolData> PACKET_CODEC = StreamCodec.composite(ByteBufCodecs.INT, FastBuildingToolData::range, ByteBufCodecs.registry(BlockMatchingRule.REGISTRY_KEY), FastBuildingToolData::matchingRule, FastBuildingToolData::new);

  @Override
  public void addToTooltip(Item.TooltipContext context, Consumer<Component> textConsumer, TooltipFlag type, DataComponentGetter components) {
    textConsumer.accept(Component.translatable("item.mishanguc.fast_building_tool.tooltip.range", Component.literal(Integer.toString(this.range())).withStyle(ChatFormatting.YELLOW))
        .withStyle(ChatFormatting.GRAY));
    textConsumer.accept(Component.translatable("item.mishanguc.fast_building_tool.tooltip.matchingRule", this.matchingRule().getName().withStyle(ChatFormatting.YELLOW))
        .withStyle(ChatFormatting.GRAY));
  }
}
