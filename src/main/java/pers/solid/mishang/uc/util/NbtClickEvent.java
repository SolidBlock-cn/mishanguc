package pers.solid.mishang.uc.util;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.ClickEvent;

/**
 * The click event that, when clicked, shows the player a prettified NBT.
 *
 * @see pers.solid.mishang.uc.mixin.ScreenMixin#handleTextClickMixin
 * @since 0.1.7 This class is designed for client-only, as it is related to client-side clicking actions, and it cannot be serialized as JSON.
 */
public record NbtClickEvent(Tag nbt) implements ClickEvent {
  public static final MapCodec<NbtClickEvent> CODEC = RecordCodecBuilder.mapCodec(i -> i.group(CompoundTag.CODEC.fieldOf("nbt").forGetter(o -> {
    final CompoundTag nbtCompound = new CompoundTag();
    nbtCompound.put("value", o.nbt);
    return nbtCompound;
  })).apply(i, nbtCompound -> new NbtClickEvent(nbtCompound.contains("value") ? nbtCompound.get("value") : new CompoundTag())));

  @Override
  public Action action() {
    return Action.RUN_COMMAND;
  }
}
