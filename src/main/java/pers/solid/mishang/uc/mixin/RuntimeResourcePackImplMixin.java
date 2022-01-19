package pers.solid.mishang.uc.mixin;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.devtech.arrp.impl.RuntimeResourcePackImpl;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import pers.solid.mishang.uc.arrp.FixedWhen;

@SuppressWarnings({"deprecation", "UnstableApiUsage"})
@Mixin(RuntimeResourcePackImpl.class)
public class RuntimeResourcePackImplMixin {
  @SuppressWarnings("SpellCheckingInspection")
  @Redirect(
      method = "<clinit>",
      at =
          @At(
              value = "INVOKE",
              target = "Lcom/google/gson/GsonBuilder;create()Lcom/google/gson/Gson;"))
  private static Gson addRegister(GsonBuilder instance) {
    return instance.registerTypeAdapter(FixedWhen.class, new FixedWhen.Serializer()).create();
  }
}
