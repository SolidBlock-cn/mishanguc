package pers.solid.mishang.uc.util;

import net.minecraft.nbt.NbtElement;
import net.minecraft.text.ClickEvent;

/**
 * The click event that, when clicked, shows the player a prettified NBT.
 *
 * @see pers.solid.mishang.uc.mixin.ScreenMixin#handleTextClickMixin
 * @since 0.1.7 This class is designed for client-only, as it is related to client-side clicking actions, and it cannot be serialized as JSON.
 */
public class NbtClickEvent extends ClickEvent {
  public final NbtElement nbt;

  public NbtClickEvent(NbtElement nbt) {
    super(Action.RUN_COMMAND, "/");
    this.nbt = nbt;
  }
}
