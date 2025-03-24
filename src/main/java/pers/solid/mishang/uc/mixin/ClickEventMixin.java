package pers.solid.mishang.uc.mixin;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.mojang.datafixers.util.Either;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import net.minecraft.text.ClickEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import pers.solid.mishang.uc.util.NbtClickEvent;
import pers.solid.mishang.uc.util.TextClickEvent;

import java.util.function.Function;

@Mixin(ClickEvent.class)
public interface ClickEventMixin {
  @ModifyExpressionValue(method = "<clinit>", at = @At(value = "INVOKE", target = "Lcom/mojang/serialization/Codec;dispatch(Ljava/lang/String;Ljava/util/function/Function;Ljava/util/function/Function;)Lcom/mojang/serialization/Codec;", remap = false))
  private static Codec<ClickEvent> modifyCodec(Codec<ClickEvent> original) {
    return Codec.either(Codec.STRING.<ClickEvent>dispatch("action", clickEvent -> switch (clickEvent) {
      case NbtClickEvent nbtClickEvent -> "mishanguc:nbt";
      case TextClickEvent textClickEvent -> "mishanguc:text";
      default -> "original";
    }, s -> switch (s) {
      case "mishanguc:nbt" -> NbtClickEvent.CODEC;
      case "mishanguc:text" -> TextClickEvent.CODEC;
      default -> Codec.EMPTY.flatXmap(unit -> DataResult.error(() -> "skipped"), clickEvent -> DataResult.error(() -> "skipped"));
    }), original).xmap(either -> either.map(Function.identity(), Function.identity()), clickEvent -> clickEvent instanceof NbtClickEvent || clickEvent instanceof TextClickEvent ? Either.left(clickEvent) : Either.right(clickEvent));
  }
}
