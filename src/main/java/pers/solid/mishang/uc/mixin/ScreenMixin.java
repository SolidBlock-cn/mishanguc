package pers.solid.mishang.uc.mixin;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.nbt.NbtElement;
import net.minecraft.text.ClickEvent;
import net.minecraft.text.Text;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import pers.solid.mishang.uc.util.NbtClickEvent;
import pers.solid.mishang.uc.util.NbtPrettyPrinter;
import pers.solid.mishang.uc.util.TextClickEvent;

@Environment(EnvType.CLIENT)
@Mixin(Screen.class)
public abstract class ScreenMixin {
  @Final
  @Shadow
  @Nullable
  protected MinecraftClient client;

  /**
   * This injection is used for an extended "clickEvent" of JSON string. It does not add to an enum
   * element, but instead, uses {@link TextClickEvent} that extends vanilla {@link ClickEvent}s.
   */
  @Inject(
      method = "handleClickEvent(Lnet/minecraft/text/ClickEvent;Lnet/minecraft/client/MinecraftClient;Lnet/minecraft/client/gui/screen/Screen;)V",
      at =
      @At(
          target = "Ljava/util/Objects;requireNonNull(Ljava/lang/Object;)Ljava/lang/Object;",
          value = "INVOKE",
          shift = At.Shift.AFTER),
      cancellable = true)
  private static void handleTextClickMixin(ClickEvent clickEvent, MinecraftClient client, Screen screenAfterRun, CallbackInfo ci) {
    if (clickEvent instanceof TextClickEvent(Text text) && client != null && client.player != null) {
      client.player.sendMessage(text, false);
      ci.cancel();
    } else if (clickEvent instanceof NbtClickEvent(NbtElement nbt) && client != null && client.player != null) {
      client.player.sendMessage(NbtPrettyPrinter.serialize(nbt), false);
      ci.cancel();
    }
  }
}
