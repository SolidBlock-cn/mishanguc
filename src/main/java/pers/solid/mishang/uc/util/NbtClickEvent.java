package pers.solid.mishang.uc.util;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.text.ClickEvent;

/**
 * The click event that, when clicked, shows the player a prettified NBT.
 *
 * @see pers.solid.mishang.uc.mixin.ScreenMixin#handleTextClickMixin
 * @since 0.1.7 This class is designed for client-only, as it is related to client-side clicking actions, and it cannot be serialized as JSON.
 */
public record NbtClickEvent(NbtElement nbt) implements ClickEvent {
  public static final MapCodec<NbtClickEvent> CODEC = RecordCodecBuilder.mapCodec(i -> i.group(NbtCompound.CODEC.fieldOf("nbt").forGetter(o -> {
    final NbtCompound nbtCompound = new NbtCompound();
    nbtCompound.put("value", o.nbt);
    return nbtCompound;
  })).apply(i, nbtCompound -> new NbtClickEvent(nbtCompound.contains("value") ? nbtCompound.get("value") : new NbtCompound())));

  @Override
  public Action getAction() {
    return Action.RUN_COMMAND;
  }
}
