package pers.solid.mishang.uc.util;

import net.minecraft.text.ClickEvent;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/**
 * The click event that, when clicked, shows the player another {@link Text}.
 *
 * @see pers.solid.mishang.uc.mixin.ScreenMixin#handleTextClickMixin(Style, CallbackInfoReturnable)
 */
public class ExtendedClickEvent extends ClickEvent {
  public Text text;

  public ExtendedClickEvent(Text text) {
    super(Action.RUN_COMMAND, "/tell @s INVALID_COMMAND_ACCESS");
    this.text = text;
  }
}
