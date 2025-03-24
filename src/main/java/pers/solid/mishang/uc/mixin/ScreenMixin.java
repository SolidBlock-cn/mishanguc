package pers.solid.mishang.uc.mixin;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.nbt.NbtElement;
import net.minecraft.text.ClickEvent;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import pers.solid.mishang.uc.util.NbtClickEvent;
import pers.solid.mishang.uc.util.NbtPrettyPrinter;
import pers.solid.mishang.uc.util.TextClickEvent;

@Environment(EnvType.CLIENT)
@Mixin(Screen.class)
public abstract class ScreenMixin {
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
          target = "Lorg/slf4j/Logger;error(Ljava/lang/String;Ljava/lang/Object;)V",
          value = "INVOKE",
          ordinal = 1,
          remap = false),
      cancellable = true)
  public void handleTextClickMixin(Style style, CallbackInfoReturnable<Boolean> cir) {
    final ClickEvent clickEvent = style.getClickEvent();
    if (clickEvent instanceof TextClickEvent(Text text) && client != null && client.player != null) {
      this.client.player.sendMessage(text, false);
      cir.setReturnValue(true);
      cir.cancel();
    } else if (clickEvent instanceof NbtClickEvent(NbtElement nbt) && client != null && client.player != null) {
      this.client.player.sendMessage(NbtPrettyPrinter.serialize(nbt), false);
      cir.setReturnValue(true);
      cir.cancel();
    }
  }
}
