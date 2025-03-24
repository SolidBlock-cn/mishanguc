package pers.solid.mishang.uc.util;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.text.ClickEvent;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.text.TextCodecs;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/**
 * <p>The click event that, when clicked, shows the player another {@link Text}.</p>
 *
 * @see pers.solid.mishang.uc.mixin.ScreenMixin#handleTextClickMixin(Style, CallbackInfoReturnable)
 * @since 0.1.7 This class is designed for client-only, as it is related to client-side clicking actions, and it cannot be serialized as JSON.
 */
public record TextClickEvent(Text text) implements ClickEvent {
  public static final MapCodec<TextClickEvent> CODEC = RecordCodecBuilder.mapCodec(i -> i.group(TextCodecs.CODEC.fieldOf("text").forGetter(TextClickEvent::text)).apply(i, TextClickEvent::new));

  @Override
  public Action getAction() {
    return Action.RUN_COMMAND;
  }
}
