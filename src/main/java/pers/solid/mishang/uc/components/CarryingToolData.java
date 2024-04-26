package pers.solid.mishang.uc.components;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.block.BlockState;
import net.minecraft.entity.EntityType;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.registry.Registries;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.text.Text;
import net.minecraft.text.TextCodecs;

import java.util.Optional;

public sealed interface CarryingToolData {
  short type();

  Codec<CarryingToolData> CODEC = Codec.SHORT.dispatch(CarryingToolData::type, s -> switch (s) {
    case 0 -> HoldingBlockState.CODEC;
    case 1 -> HoldingEntity.CODEC;
    default -> MapCodec.unit(() -> {throw new IllegalArgumentException("invalid type: " + s);});
  });
  PacketCodec<RegistryByteBuf, CarryingToolData> PACKET_CODEC = PacketCodecs.SHORT.<RegistryByteBuf>cast().dispatch(CarryingToolData::type, s -> switch (s) {
    case 0 -> HoldingBlockState.PACKET_CODEC;
    case 1 -> HoldingEntity.PACKET_CODEC;
    default -> throw new IllegalArgumentException("invalid type: " + s);
  });

  record HoldingBlockState(BlockState state, Optional<NbtCompound> blockEntityTag) implements CarryingToolData {
    public static final MapCodec<HoldingBlockState> CODEC = RecordCodecBuilder.mapCodec(i -> i.group(
        BlockState.CODEC.fieldOf("state").forGetter(HoldingBlockState::state),
        NbtCompound.CODEC.optionalFieldOf("block_entity_tag").forGetter(HoldingBlockState::blockEntityTag)
    ).apply(i, HoldingBlockState::new));
    public static final PacketCodec<RegistryByteBuf, HoldingBlockState> PACKET_CODEC = PacketCodec.tuple(PacketCodecs.codec(BlockState.CODEC), HoldingBlockState::state, PacketCodecs.OPTIONAL_NBT, HoldingBlockState::blockEntityTag, HoldingBlockState::new);

    @Override
    public short type() {
      return 0;
    }
  }

  record HoldingEntity(EntityType<?> entityType, Optional<NbtCompound> entityTag, Text name, float width, float height) implements CarryingToolData {
    public static final MapCodec<HoldingEntity> CODEC = RecordCodecBuilder.mapCodec(i -> i.group(
        Registries.ENTITY_TYPE.getCodec().fieldOf("entity_type").forGetter(HoldingEntity::entityType),
        NbtCompound.CODEC.optionalFieldOf("entity_tag").forGetter(HoldingEntity::entityTag),
        TextCodecs.CODEC.fieldOf("name").forGetter(HoldingEntity::name),
        Codec.FLOAT.fieldOf("width").forGetter(HoldingEntity::width),
        Codec.FLOAT.fieldOf("height").forGetter(HoldingEntity::height)
    ).apply(i, HoldingEntity::new));
    public static final PacketCodec<RegistryByteBuf, HoldingEntity> PACKET_CODEC = PacketCodec.tuple(PacketCodecs.registryValue(RegistryKeys.ENTITY_TYPE), HoldingEntity::entityType, PacketCodecs.OPTIONAL_NBT, HoldingEntity::entityTag, TextCodecs.PACKET_CODEC, HoldingEntity::name, PacketCodecs.FLOAT, HoldingEntity::width, PacketCodecs.FLOAT, HoldingEntity::height, HoldingEntity::new);

    @Override
    public short type() {
      return 1;
    }
  }
}
