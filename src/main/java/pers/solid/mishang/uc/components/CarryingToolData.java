package pers.solid.mishang.uc.components;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.ChatFormatting;
import net.minecraft.core.component.DataComponentGetter;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.ComponentSerialization;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.TooltipProvider;
import net.minecraft.world.level.block.state.BlockState;

import java.util.Optional;
import java.util.function.Consumer;

public sealed interface CarryingToolData extends TooltipProvider {
  short type();

  Codec<CarryingToolData> CODEC = Codec.SHORT.dispatch(CarryingToolData::type, s -> switch (s) {
    case 0 -> HoldingBlockState.CODEC;
    case 1 -> HoldingEntity.CODEC;
    default -> MapCodec.unit(() -> {throw new IllegalArgumentException("invalid type: " + s);});
  });
  StreamCodec<RegistryFriendlyByteBuf, CarryingToolData> PACKET_CODEC = ByteBufCodecs.SHORT.<RegistryFriendlyByteBuf>cast().dispatch(CarryingToolData::type, s -> switch (s) {
    case 0 -> HoldingBlockState.PACKET_CODEC;
    case 1 -> HoldingEntity.PACKET_CODEC;
    default -> throw new IllegalArgumentException("invalid type: " + s);
  });

  record HoldingBlockState(BlockState state, Optional<CompoundTag> blockEntityTag) implements CarryingToolData {
    public static final MapCodec<HoldingBlockState> CODEC = RecordCodecBuilder.mapCodec(i -> i.group(
        BlockState.CODEC.fieldOf("state").forGetter(HoldingBlockState::state),
        CompoundTag.CODEC.optionalFieldOf("block_entity_tag").forGetter(HoldingBlockState::blockEntityTag)
    ).apply(i, HoldingBlockState::new));
    public static final StreamCodec<RegistryFriendlyByteBuf, HoldingBlockState> PACKET_CODEC = StreamCodec.composite(ByteBufCodecs.fromCodec(BlockState.CODEC), HoldingBlockState::state, ByteBufCodecs.OPTIONAL_COMPOUND_TAG, HoldingBlockState::blockEntityTag, HoldingBlockState::new);

    @Override
    public short type() {
      return 0;
    }

    @Override
    public void addToTooltip(Item.TooltipContext context, Consumer<Component> textConsumer, TooltipFlag type, DataComponentGetter components) {
      textConsumer.accept(Component.translatable("item.mishanguc.carrying_tool.tooltip.currently", this.state().getBlock().getName().withStyle(ChatFormatting.YELLOW)).withStyle(ChatFormatting.GREEN));
    }
  }

  record HoldingEntity(EntityType<?> entityType, Optional<CompoundTag> entityTag, Component name, float width, float height) implements CarryingToolData {
    public static final MapCodec<HoldingEntity> CODEC = RecordCodecBuilder.mapCodec(i -> i.group(
        BuiltInRegistries.ENTITY_TYPE.byNameCodec().fieldOf("entity_type").forGetter(HoldingEntity::entityType),
        CompoundTag.CODEC.optionalFieldOf("entity_tag").forGetter(HoldingEntity::entityTag),
        ComponentSerialization.CODEC.fieldOf("name").forGetter(HoldingEntity::name),
        Codec.FLOAT.fieldOf("width").forGetter(HoldingEntity::width),
        Codec.FLOAT.fieldOf("height").forGetter(HoldingEntity::height)
    ).apply(i, HoldingEntity::new));
    public static final StreamCodec<RegistryFriendlyByteBuf, HoldingEntity> PACKET_CODEC = StreamCodec.composite(ByteBufCodecs.registry(Registries.ENTITY_TYPE), HoldingEntity::entityType, ByteBufCodecs.OPTIONAL_COMPOUND_TAG, HoldingEntity::entityTag, ComponentSerialization.TRUSTED_CONTEXT_FREE_STREAM_CODEC, HoldingEntity::name, ByteBufCodecs.FLOAT, HoldingEntity::width, ByteBufCodecs.FLOAT, HoldingEntity::height, HoldingEntity::new);

    @Override
    public short type() {
      return 1;
    }

    @Override
    public void addToTooltip(Item.TooltipContext context, Consumer<Component> textConsumer, TooltipFlag type, DataComponentGetter components) {
      textConsumer.accept(Component.translatable("item.mishanguc.carrying_tool.tooltip.currently", this.name().copy().withStyle(ChatFormatting.YELLOW)).withStyle(ChatFormatting.GREEN));
    }
  }
}
