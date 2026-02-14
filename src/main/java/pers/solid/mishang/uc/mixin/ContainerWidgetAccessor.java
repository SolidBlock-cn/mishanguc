package pers.solid.mishang.uc.mixin;

import net.minecraft.client.gui.AbstractParentElement;
import net.minecraft.client.gui.Element;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(AbstractParentElement.class)
public interface ContainerWidgetAccessor {
  @Accessor("focused")
  void setFocusedElement(Element focusedElement);
}
