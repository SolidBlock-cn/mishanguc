package pers.solid.mishang.uc.screen;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.Element;
import net.minecraft.client.gui.Selectable;
import net.minecraft.client.gui.tooltip.Tooltip;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.ElementListWidget;
import net.minecraft.screen.ScreenTexts;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import pers.solid.mishang.uc.text.TextContext;

import java.util.ArrayList;
import java.util.List;

@Environment(EnvType.CLIENT)
public class SignPresetGridWidget extends ElementListWidget<SignPresetGridWidget.Entry> {
  public SignPresetGridWidget(MinecraftClient minecraftClient, int width, int height, int y, int itemHeight) {
    super(minecraftClient, width, height, y, itemHeight);
  }

  public static SignPresetGridWidget createAllWidgets(AbstractSignBlockEditScreen<?> screen, MinecraftClient minecraftClient, int height, int y) {
    final SignPresetGridWidget gridWidget = new SignPresetGridWidget(minecraftClient, 480, height, y, 20);
    final List<ButtonWidget> widgets = new ArrayList<>(3);
    SignPresets.streamValues().forEach(value -> {
      final ButtonWidget widgetForPreset = createWidgetForPreset(screen, value);
      widgets.add(widgetForPreset);
      if (widgets.size() >= 3) {
        gridWidget.addEntry(new Entry(List.copyOf(widgets)));
        widgets.clear();
      }
    });
    if (!widgets.isEmpty()) {
      gridWidget.addEntry(new Entry(List.copyOf(widgets)));
    }
    return gridWidget;
  }

  public static ButtonWidget createWidgetForPreset(AbstractSignBlockEditScreen<?> screen, SignPreset signPreset) {
    Text description = signPreset.description();
    final MutableText idText = Text.translatable("message.mishanguc.signPreset.list.id_info", signPreset.id()).formatted(Formatting.GRAY);
    if (description != null) {
      description = Text.empty().append(description).append(ScreenTexts.LINE_BREAK).append(idText);
    } else {
      description = idText;
    }
    return new ButtonWidget.Builder(signPreset.name(), button -> {
      for (TextContext textContext : signPreset.textContexts()) {
        final TextContext newTextContext = textContext.clone();
        newTextContext.size = (screen.entity.createDefaultTextContext().size * newTextContext.size / 8);
        screen.textFieldListWidget.addTextField(-1, newTextContext, false);
      }
      final List<TextFieldListWidget.Entry> children = screen.textFieldListWidget.children();
      final int initialFocus = signPreset.initialFocus();
      if (initialFocus >= 0 && initialFocus < children.size()) {
        screen.setFocused(screen.textFieldListWidget);
        screen.textFieldListWidget.setFocused(children.get(initialFocus), false, false);
      }
      screen.rearrange();
    }).dimensions(0, 0, 150, 20)
        .tooltip(Tooltip.of(description))
        .build();
  }

  @Override
  protected void drawMenuListBackground(DrawContext context) {
  }

  @Override
  protected void drawHeaderAndFooterSeparators(DrawContext context) {

  }

  @Override
  public int getRowWidth() {
    return 450;
  }

  public static class Entry extends ElementListWidget.Entry<Entry> {
    public final List<ButtonWidget> buttons;

    public Entry(List<ButtonWidget> buttons) {
      this.buttons = buttons;
    }

    @Override
    public List<? extends Selectable> selectableChildren() {
      return buttons;
    }

    @Override
    public List<? extends Element> children() {
      return buttons;
    }

    @Override
    public void render(DrawContext context, int index, int y, int x, int entryWidth, int entryHeight, int mouseX, int mouseY, boolean hovered, float tickProgress) {
      for (int i = 0, buttonsSize = buttons.size(); i < buttonsSize; i++) {
        ButtonWidget button = buttons.get(i);
        button.setX(x + i * 150);
        button.setY(y);
        button.render(context, mouseX, mouseY, tickProgress);
      }
    }
  }
}
