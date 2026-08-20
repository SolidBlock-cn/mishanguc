package pers.solid.mishang.uc.mixin;

import net.minecraft.client.gui.components.AbstractContainerWidget;
import net.minecraft.client.gui.components.events.GuiEventListener;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(AbstractContainerWidget.class)
public interface ContainerWidgetAccessor {
  @Accessor("focused")
    // 方法不命名为 setFocused，避免与原版方法冲突，导致通过原版的 setFocused 方法修改了 selected
  void setFocusedRaw(@Nullable GuiEventListener focusedElement);
}
