package pers.solid.mishang.uc.screen;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.ContainerObjectSelectionList;
import net.minecraft.client.gui.components.Tooltip;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.narration.NarratableEntry;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import pers.solid.mishang.uc.text.TextContext;

import java.util.ArrayList;
import java.util.List;

@Environment(EnvType.CLIENT)
public class SignPresetGridWidget extends ContainerObjectSelectionList<SignPresetGridWidget.Entry> {
  public SignPresetGridWidget(Minecraft minecraftClient, int width, int height, int y, int itemHeight) {
    super(minecraftClient, width, height, y, itemHeight);
  }

  public static SignPresetGridWidget createAllWidgets(AbstractSignBlockEditScreen<?> screen, Minecraft minecraftClient, int height, int y) {
    final SignPresetGridWidget gridWidget = new SignPresetGridWidget(minecraftClient, 480, height, y, 20);
    final List<Button> widgets = new ArrayList<>(3);
    SignPresets.streamValues().forEach(value -> {
      final Button widgetForPreset = createWidgetForPreset(screen, value);
      widgets.add(widgetForPreset);
      if (widgets.size() >= 3) {
        gridWidget.addEntry(new SignPresetGridWidget.Entry(List.copyOf(widgets)));
        widgets.clear();
      }
    });
    if (!widgets.isEmpty()) {
      gridWidget.addEntry(new SignPresetGridWidget.Entry(List.copyOf(widgets)));
    }
    return gridWidget;
  }

  public static Button createWidgetForPreset(AbstractSignBlockEditScreen<?> screen, SignPreset signPreset) {
    Component description = signPreset.description();
    final MutableComponent idText = Component.translatable("message.mishanguc.signPreset.list.id_info", signPreset.id()).withStyle(ChatFormatting.GRAY);
    if (description != null) {
      description = Component.empty().append(description).append(CommonComponents.NEW_LINE).append(idText);
    } else {
      description = idText;
    }
    return new Button.Builder(signPreset.name(), button -> {
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
    }).bounds(0, 0, 150, 20)
        .tooltip(Tooltip.create(description))
        .build();
  }

  @Override
  protected void renderListBackground(GuiGraphics context) {
  }

  @Override
  protected void renderListSeparators(GuiGraphics context) {

  }

  @Override
  public int getRowWidth() {
    return 450;
  }

  public static class Entry extends ContainerObjectSelectionList.Entry<SignPresetGridWidget.Entry> {
    public final List<Button> buttons;

    public Entry(List<Button> buttons) {
      this.buttons = buttons;
    }

    @Override
    public List<? extends NarratableEntry> narratables() {
      return buttons;
    }

    @Override
    public List<? extends GuiEventListener> children() {
      return buttons;
    }

    @Override
    public void setX(int x) {
      super.setX(x);
      for (int i = 0; i < buttons.size(); i++) {
        buttons.get(i).setX(x + i * 150);
      }
    }

    @Override
    public void setY(int y) {
      super.setY(y);
      for (Button button : buttons) {
        button.setY(y);
      }
    }

    @Override
    public void renderContent(GuiGraphics context, int mouseX, int mouseY, boolean hovered, float deltaTicks) {
      for (Button button : buttons) {
        button.render(context, mouseX, mouseY, deltaTicks);
      }
    }
  }
}
