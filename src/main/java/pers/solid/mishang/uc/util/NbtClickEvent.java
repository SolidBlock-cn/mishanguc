package pers.solid.mishang.uc.util;

import net.minecraft.nbt.NbtElement;
import net.minecraft.text.ClickEvent;

/**
 * The click event that, when clicked, shows the player a prettified NBT.
 *
 * @see pers.solid.mishang.uc.mixin.ScreenMixin#handleTextClickMixin
 */
public class NbtClickEvent extends ClickEvent {
  public final NbtElement nbt;

  public NbtClickEvent(NbtElement nbt) {
    super(Action.RUN_COMMAND, "/tell @s INVALID_COMMAND_ACCESS");
    this.nbt = nbt;
  }
}
