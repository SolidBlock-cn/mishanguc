package pers.solid.mishang.uc.mixin;

import net.devtech.arrp.json.blockstate.JBlockModel;
import net.devtech.arrp.json.blockstate.JVariant;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import java.util.Map;

@Mixin(JVariant.class)
public interface JVariantAccessor {
  @Accessor("models")
  Map<String, JBlockModel> getModels();
}
