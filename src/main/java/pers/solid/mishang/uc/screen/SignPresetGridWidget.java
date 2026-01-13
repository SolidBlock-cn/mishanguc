package pers.solid.mishang.uc.screen;

import net.minecraft.client.gui.tooltip.Tooltip;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.GridWidget;
import net.minecraft.text.Text;
import pers.solid.mishang.uc.text.TextContext;

import java.util.List;

public class SignPresetGridWidget extends GridWidget {
  public static SignPresetGridWidget createAllWidgets(AbstractSignBlockEditScreen<?> screen) {
    final SignPresetGridWidget gridWidget = new SignPresetGridWidget();
    final Adder adder = gridWidget.createAdder(3);
    SignPresets.streamValues().forEach(signPreset -> adder.add(createWidgetForPreset(screen, signPreset)));
    return gridWidget;
  }

  public static ButtonWidget createWidgetForPreset(AbstractSignBlockEditScreen<?> screen, SignPreset signPreset) {
    final Text description = signPreset.description();
    return new ButtonWidget.Builder(signPreset.name(), button -> {
      for (TextContext textContext : signPreset.textContexts()) {
        final TextContext newTextContext = textContext.clone();
        screen.textFieldListWidget.addTextField(-1, newTextContext, false, false);
      }
      final List<TextFieldListWidget.Entry> children = screen.textFieldListWidget.children();
      final int initialFocus = signPreset.initialFocus();
      if (initialFocus >= 0 && initialFocus < children.size()) {
        screen.textFieldListWidget.setFocused(children.get(initialFocus));
      }
      screen.rearrange();
    }).dimensions(0, 0, 150, 20)
        .tooltip(description == null ? null : Tooltip.of(description))
        .build();
  }
}
