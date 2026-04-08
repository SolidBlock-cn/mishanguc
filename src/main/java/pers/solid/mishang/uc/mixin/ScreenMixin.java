package pers.solid.mishang.uc.mixin;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.ClickEvent;
import net.minecraft.network.chat.Component;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import pers.solid.mishang.uc.util.NbtClickEvent;
import pers.solid.mishang.uc.util.NbtPrettyPrinter;
import pers.solid.mishang.uc.util.TextClickEvent;

@Environment(EnvType.CLIENT)
@Mixin(Screen.class)
public abstract class ScreenMixin {

  /**
   * This injection is used for an extended "clickEvent" of JSON string. It does not add to an enum
   * element, but instead, uses {@link TextClickEvent} that extends vanilla {@link ClickEvent}s.
   */
  @Inject(
      method = "defaultHandleGameClickEvent(Lnet/minecraft/network/chat/ClickEvent;Lnet/minecraft/client/Minecraft;Lnet/minecraft/client/gui/screens/Screen;)V",
      at =
      @At(
          target = "Ljava/util/Objects;requireNonNull(Ljava/lang/Object;)Ljava/lang/Object;",
          value = "INVOKE",
          shift = At.Shift.AFTER),
      cancellable = true)
  private static void handleTextClickMixin(ClickEvent clickEvent, Minecraft minecraft, Screen screen, CallbackInfo ci) {
    if (clickEvent instanceof TextClickEvent(Component text) && minecraft.player != null) {
      minecraft.player.displayClientMessage(text, false);
      ci.cancel();
    } else if (clickEvent instanceof NbtClickEvent(Tag nbt) && minecraft.player != null) {
      minecraft.player.displayClientMessage(NbtPrettyPrinter.serialize(nbt), false);
      ci.cancel();
    }
  }
}
