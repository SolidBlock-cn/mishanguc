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
  /**
   * 决定着此元素在告示牌编辑界面中是否在显示，如果为 false，则 {@link #isMouseOver(double, double)} 始终为 false，这是为了避免当告示牌预设列表未显示时仍视为被悬浮导致无法操作告示牌编辑界面的问题。此字段仅存在于 1.20.1 中。
   */
  public boolean active = true;

  public SignPresetGridWidget(MinecraftClient minecraftClient, int width, int height, int top, int bottom, int itemHeight) {
    super(minecraftClient, width, height, top, bottom, itemHeight);
  }

  public static SignPresetGridWidget createAllWidgets(AbstractSignBlockEditScreen<?> screen, MinecraftClient minecraftClient, int height, int top, int bottom) {
    final SignPresetGridWidget gridWidget = new SignPresetGridWidget(minecraftClient, screen.width, height, top, bottom, 20);
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
  public int getRowWidth() {
    return 450;
  }

  @Override
  protected int getScrollbarPositionX() {
    return width / 2 + 228;
  }

  @Override
  public boolean isMouseOver(double mouseX, double mouseY) {
    return this.active && super.isMouseOver(mouseX, mouseY);
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
