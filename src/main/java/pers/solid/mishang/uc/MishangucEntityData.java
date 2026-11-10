package pers.solid.mishang.uc;

import net.fabricmc.fabric.api.object.builder.v1.entity.FabricEntityDataRegistry;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializer;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.item.FallingBlockEntity;

import java.util.OptionalInt;

public final class MishangucEntityData {
  public static final EntityDataSerializer<OptionalInt> OPTIONAL_INT = EntityDataSerializer.forValueType(StreamCodec.of((output, value) -> value.ifPresentOrElse(p -> output.writeBoolean(true).writeInt(p), () -> output.writeBoolean(false)), input -> input.readBoolean() ? OptionalInt.of(input.readInt()) : OptionalInt.empty()));
  public static final EntityDataAccessor<OptionalInt> FALLING_BLOCK_COLOR = SynchedEntityData.defineId(FallingBlockEntity.class, EntityDataSerializers.OPTIONAL_UNSIGNED_INT);

  public static void init() {
    FabricEntityDataRegistry.register(Mishanguc.id("optional_int"), OPTIONAL_INT);
  }
}
