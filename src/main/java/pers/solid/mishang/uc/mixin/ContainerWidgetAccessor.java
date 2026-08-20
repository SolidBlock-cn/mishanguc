package pers.solid.mishang.uc.mixin;

import net.minecraft.client.gui.Element;
import net.minecraft.client.gui.widget.ContainerWidget;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(ContainerWidget.class)
public interface ContainerWidgetAccessor {
  @Accessor("focusedElement")
    // 方法不命名为 setFocused，避免与原版方法冲突，导致通过原版的 setFocused 方法修改了 selected
  void setFocusedRaw(@Nullable Element focusedElement);
}
