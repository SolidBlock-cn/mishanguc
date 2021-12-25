package pers.solid.mishang.uc.mixin;

import net.devtech.arrp.json.blockstate.JState;
import net.devtech.arrp.json.blockstate.JVariant;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import java.util.List;

@Mixin(JState.class)
public interface JStateAccessor {
  @Accessor("variants")
  List<JVariant> getVariants();
}
