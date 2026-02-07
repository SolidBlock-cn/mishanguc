package pers.solid.mishang.uc.mixin;

import net.minecraft.client.gui.Element;
import org.spongepowered.asm.mixin.gen.Accessor;

@org.spongepowered.asm.mixin.Mixin(net.minecraft.client.gui.widget.ContainerWidget.class)
public interface ContainerWidgetAccessor {
  @Accessor
  void setFocusedElement(Element focusedElement);
}
