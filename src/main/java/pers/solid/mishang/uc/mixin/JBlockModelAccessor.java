package pers.solid.mishang.uc.mixin;

import net.devtech.arrp.json.blockstate.JBlockModel;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@SuppressWarnings("AlibabaClassNamingShouldBeCamel")
@Mixin(JBlockModel.class)
public interface JBlockModelAccessor {
  @Accessor
  Identifier getModel();

  @Accessor("model")
  void setModel(Identifier model);

  @Accessor("x")
  Integer getX();

  @Accessor("x")
  void setX(Integer x);

  @Accessor("y")
  Integer getY();

  @Accessor("y")
  void setY(Integer y);

  @Accessor
  Boolean getUvlock();

  @Accessor
  void setUvlock(Boolean uvlock);
}
