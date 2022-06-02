package pers.solid.mishang.uc.util;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.text.ClickEvent;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/**
 * <p>The click event that, when clicked, shows the player another {@link Text}.</p>
 *
 * @see pers.solid.mishang.uc.mixin.ScreenMixin#handleTextClickMixin(Style, CallbackInfoReturnable)
 * @since 0.1.7 This class is designed for client-only, as it is related to client-side clicking actions, and it cannot be serialized as JSON.
 */
@Environment(EnvType.CLIENT)
public class TextClickEvent extends ClickEvent {
  public final Text text;

  public TextClickEvent(Text text) {
    super(Action.RUN_COMMAND, "/tell @s INVALID_COMMAND_ACCESS");
    this.text = text;
  }
}
