package pers.solid.mishang.uc.mixin;

import net.devtech.arrp.json.blockstate.JBlockModel;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(JBlockModel.class)
public interface JBlockModelAccessor {
  @Accessor
  Identifier getModel();

  @Accessor
  void setModel(Identifier model);
}
