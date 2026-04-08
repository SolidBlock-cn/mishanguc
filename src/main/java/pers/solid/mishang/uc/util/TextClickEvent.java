package pers.solid.mishang.uc.util;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.ClickEvent;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.ComponentSerialization;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * <p>The click event that, when clicked, shows the player another {@link Component}.</p>
 *
 * @see pers.solid.mishang.uc.mixin.ScreenMixin#handleTextClickMixin(ClickEvent, Minecraft, Screen, CallbackInfo)
 * @since 0.1.7 This class is designed for client-only, as it is related to client-side clicking actions, and it cannot be serialized as JSON.
 */
public record TextClickEvent(Component text) implements ClickEvent {
  public static final MapCodec<TextClickEvent> CODEC = RecordCodecBuilder.mapCodec(i -> i.group(ComponentSerialization.CODEC.fieldOf("text").forGetter(TextClickEvent::text)).apply(i, TextClickEvent::new));

  @Override
  public Action action() {
    return Action.RUN_COMMAND;
  }
}
