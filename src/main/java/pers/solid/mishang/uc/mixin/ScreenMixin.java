package pers.solid.mishang.uc.mixin;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.ClickEvent;
import net.minecraft.text.Style;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import pers.solid.mishang.uc.util.NbtClickEvent;
import pers.solid.mishang.uc.util.NbtPrettyPrinter;
import pers.solid.mishang.uc.util.TextClickEvent;

@Mixin(Screen.class)
public class ScreenMixin {
  @Shadow
  @Nullable
  protected MinecraftClient client;

  /**
   * This injection is used for an extended "clickEvent" of JSON string. It does not add to an enum
   * element, but instead, uses {@link TextClickEvent} that extends vanilla {@link ClickEvent}s.
   */
  @Inject(
      method = "handleTextClick",
      at =
      @At(
          target = "Lnet/minecraft/client/gui/screen/Screen;sendMessage(Ljava/lang/String;Z)V",
          shift = At.Shift.BEFORE,
          value = "INVOKE"),
      cancellable = true)
  public void handleTextClickMixin(Style style, CallbackInfoReturnable<Boolean> cir) {
    final ClickEvent clickEvent = style.getClickEvent();
    if (clickEvent instanceof TextClickEvent && client != null && client.player != null) {
      this.client.player.sendSystemMessage(
          ((TextClickEvent) clickEvent).text, this.client.player.getUuid());
      cir.setReturnValue(true);
      cir.cancel();
    } else if (clickEvent instanceof NbtClickEvent && client != null && client.player != null) {
      this.client.player.sendSystemMessage(
          NbtPrettyPrinter.serialize(((NbtClickEvent) clickEvent).nbt),
          this.client.player.getUuid());
      cir.setReturnValue(true);
      cir.cancel();
    }
  }
}
