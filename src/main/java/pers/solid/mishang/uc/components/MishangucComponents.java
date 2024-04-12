package pers.solid.mishang.uc.components;

import com.mojang.serialization.Codec;
import net.minecraft.component.DataComponentType;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

/**
 * @see DataComponentTypes
 */
public final class MishangucComponents {
  public static final DataComponentType<ExplosionToolComponent> EXPLOSION_TOOL = register("explosion_tool", ExplosionToolComponent.CODEC, ExplosionToolComponent.PACKET_CODEC);

  private MishangucComponents() {
  }

  private static <T> DataComponentType<T> register(String id, Codec<T> codec, PacketCodec<? super RegistryByteBuf, T> packetCodec) {
    return Registry.register(Registries.DATA_COMPONENT_TYPE, new Identifier("mishanguc", id), DataComponentType.<T>builder().codec(codec).packetCodec(packetCodec).build());
  }
}
