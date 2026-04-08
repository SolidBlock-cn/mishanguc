package pers.solid.mishang.uc.mixin;

import net.minecraft.client.gui.components.events.GuiEventListener;
import org.spongepowered.asm.mixin.gen.Accessor;

@org.spongepowered.asm.mixin.Mixin(net.minecraft.client.gui.components.AbstractContainerWidget.class)
public interface ContainerWidgetAccessor {
  @Accessor
  void setFocused(GuiEventListener focusedElement);
}
