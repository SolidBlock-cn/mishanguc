package pers.solid.mishang.uc.screen;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.Element;
import net.minecraft.client.gui.Narratable;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.narration.NarrationMessageBuilder;
import net.minecraft.client.gui.screen.narration.NarrationPart;
import net.minecraft.client.gui.widget.AlwaysSelectedEntryListWidget;
import net.minecraft.client.gui.widget.EntryListWidget;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.text.Text;
import net.minecraft.util.math.MathHelper;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.lwjgl.glfw.GLFW;
import pers.solid.mishang.uc.text.TextContext;
import pers.solid.mishang.uc.util.TextBridge;

import java.util.ArrayList;
import java.util.List;

/**
 * 文本框列表的屏幕。每个列表项都是一个文本框（实际上就是把 {@link TextFieldWidget} 包装成了 {@link TextFieldListWidget.Entry}。<p>
 * 此类原本是 {@link AbstractSignBlockEditScreen} 的内部类，后面独立出来了。
 */
@Environment(EnvType.CLIENT)
public class TextFieldListWidget extends AlwaysSelectedEntryListWidget<TextFieldListWidget.Entry> {

  private final AbstractSignBlockEditScreen<?> signBlockEditScreen;

  public TextFieldListWidget(AbstractSignBlockEditScreen<?> signBlockEditScreen,
                             MinecraftClient client, int width, int height, int y, int itemHeight) {
    super(client, width, height, y, itemHeight);
    this.signBlockEditScreen = signBlockEditScreen;
    setRenderBackground(false);
  }

  private boolean isFocused;

  /**
   * 在按住 Shift 进行多选时，多选起始的元素。在非 Shift 模式下进行任意选择后，此字段清空。
   */
  private @Nullable Entry startContEntry;

  /**
   * 被选中的多个项。
   */
  protected final @NotNull List<TextFieldListWidget.@NotNull Entry> selectedEntries = new ArrayList<>();

  @Override
  public void setFocused(boolean focused) {
    super.setFocused(focused);
    isFocused = focused;
    if (getFocused() != null) {
      getFocused().textFieldWidget.setFocused(focused);
    }
  }

  @Override
  public boolean isFocused() {
    // 防止此元素总被视为已经 focused
    return isFocused;
  }

  /**
   * 设置当前 TextFieldListScreen 的已选中的文本框。
   *
   * @param entry 需要选中的 {@link Entry}。
   * @see AbstractSignBlockEditScreen#setFocused(Element)
   */
  @Override
  public void setFocused(@Nullable Element entry) {
    setFocused(entry, Screen.hasControlDown(), Screen.hasShiftDown());
  }

  /**
   * 设置当前 TextFieldListScreen 的已选中的文本框。
   *
   * @param entry    需要选中的 {@link Entry}。
   * @param multiSel 是否多选。如果为 {@code false}，则之前已经选中的其他元素将会未选中。
   * @param contSel  是否连续选。如果为 {@code true}，则将之前选中的和当前选中的均选中。
   * @see AbstractSignBlockEditScreen#setFocused(Element)
   */
  public void setFocused(@Nullable Element entry, boolean multiSel, boolean contSel) {
    final Entry prevFocused = getFocused();
    if (entry != null) {
      super.setFocused(entry);
      if (!this.client.getNavigationType().isKeyboard()) {
        // 在 isKeyboard() 的情况下， super.setFocused 就已经调用了 ensureVisible，故不再看重复调用。
        this.ensureVisible(getSelectedOrNull());
      }
    }
    if (entry instanceof TextFieldListWidget.Entry) {
      if (contSel) {
        if (startContEntry == null) {
          startContEntry = prevFocused;
        }
      } else {
        startContEntry = null;
      }
      final int contFrom = contSel ? children().indexOf(startContEntry) : -1;
      if (!multiSel) {
        for (Entry selectedEntry : selectedEntries) {
          selectedEntry.setFocused(false);
        }
        selectedEntries.clear();
      }

      final int contUntil = contSel ? children().indexOf(entry) : -1;
      if (contFrom != -1 && contUntil != -1 && contFrom != contUntil) {
        final int min = Math.min(contFrom, contUntil);
        final int max = Math.max(contFrom, contUntil);

        for (int i = min; i <= max; i++) {
          final Entry entry1 = children().get(i);
          selectedEntries.add(entry1);
          entry1.setFocused(true);
        }
      } else if (multiSel && selectedEntries.contains(entry)) {
        // 在多选模式下，如果再次选中同一个，则失掉这个选择。
        final int index = selectedEntries.indexOf(entry);
        selectedEntries.remove(entry);
        if (getFocused() == entry) {
          super.setFocused(null);
          if (!selectedEntries.isEmpty()) {
            // 失掉一个多选后，如果还有一个多选，则选中其他选项。
            if (selectedEntries.size() > index) {
              super.setFocused(selectedEntries.get(index));
            } else {
              super.setFocused(selectedEntries.get(index - 1));
            }
          }
        }
        if (getSelectedOrNull() == entry) {
          super.setSelected(null);
        }
      } else {
        selectedEntries.add((Entry) entry);
      }
      for (Entry selectedEntry : selectedEntries) {
        selectedEntry.setFocused(true);
      }
    } else if (children().isEmpty() || !MinecraftClient.getInstance().getNavigationType().isKeyboard()) {
      for (Entry selectedEntry : selectedEntries) {
        selectedEntry.setFocused(false);
      }
      // 使用键盘导航至其他按钮的时候，不设为 null。
      selectedEntries.clear();
    }

    // 更新屏幕按钮中的一些 tooltip
    for (Element child : signBlockEditScreen.children()) {
      if (child instanceof TooltipUpdated tooltipUpdated) {
        tooltipUpdated.updateTooltip();
      }
    }
  }

  @Override
  public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
    if (!children().isEmpty()) {
      if (keyCode == GLFW.GLFW_KEY_UP) {
        setFocused(children().get(MathHelper.floorMod(children().indexOf(getFocused()) - 1, children().size())), Screen.hasControlDown(), Screen.hasShiftDown());
        return true;
      } else if (keyCode == GLFW.GLFW_KEY_DOWN) {
        setFocused(children().get(MathHelper.floorMod(children().indexOf(getFocused()) + 1, children().size())), Screen.hasControlDown(), Screen.hasShiftDown());
        return true;
      }
    } else if (keyCode == GLFW.GLFW_KEY_ENTER || keyCode == GLFW.GLFW_KEY_KP_ENTER) {
      // 此时，children().isEmpty() 为 true
      signBlockEditScreen.addTextField(0, false);
      return true;
    }
    if (selectedEntries.size() > 1) {
      boolean success = false;
      for (Entry selectedEntry : List.copyOf(selectedEntries)) {
        success = selectedEntry.keyPressed(keyCode, scanCode, modifiers) || success;
      }
      return success;
    }
    return super.keyPressed(keyCode, scanCode, modifiers);
  }

  @Override
  public boolean charTyped(char chr, int modifiers) {
    if (selectedEntries.size() > 1) {
      boolean success = false;
      for (Entry selectedEntry : selectedEntries) {
        success = selectedEntry.charTyped(chr, modifiers) || success;
      }
      return success;
    }
    return super.charTyped(chr, modifiers);
  }

  @Override
  public int getRowWidth() {
    return width;
  }

  @Override
  protected int getScrollbarPositionX() {
    return width - 6;
  }

  @Override
  public void appendClickableNarrations(NarrationMessageBuilder builder) {
    builder.put(NarrationPart.TITLE, TextBridge.translatable("narration.mishanguc.text_field_list"));
    builder.put(NarrationPart.USAGE, TextBridge.translatable("narration.mishanguc.text_field_list.usage"));
    super.appendClickableNarrations(builder);
  }

  @Override
  public void setScrollAmount(double amount) {
    super.setScrollAmount(amount);
    int width = getMaxScroll() > 0 ? this.width - 10 : this.width - 4;
    for (Entry child : children()) {
      child.textFieldWidget.setWidth(width);
    }
  }

  @Override
  protected boolean isSelectedEntry(int index) {
    return super.isSelectedEntry(index) || selectedEntries.contains(children().get(index));
  }

  @Override
  protected void drawSelectionHighlight(DrawContext context, int y, int entryWidth, int entryHeight, int borderColor, int fillColor) {
    context.fill(1, y - 1, width - 1, y + entryHeight + 4, 0xfff0f0f0);
  }

  /**
   * {@link TextFieldListWidget} 中的项。由于 {@link TextFieldWidget} 不是 {@link EntryListWidget.Entry}
   * 的子类，所以对该类进行了包装。
   */
  @Environment(EnvType.CLIENT)
  public class Entry extends AlwaysSelectedEntryListWidget.Entry<TextFieldListWidget.Entry> implements Narratable {
    public final @NotNull TextFieldWidget textFieldWidget;
    public final @NotNull TextContext textContext;

    public Entry(@NotNull TextFieldWidget textFieldWidget, @NotNull TextContext textContext) {
      this.textFieldWidget = textFieldWidget;
      this.textContext = textContext;
    }

    @Override
    public boolean equals(Object o) {
      if (this == o)
        return true;
      if (!(o instanceof TextFieldListWidget.Entry entry))
        return false;

      return textFieldWidget.equals(entry.textFieldWidget);
    }

    @Override
    public int hashCode() {
      return textFieldWidget.hashCode();
    }

    @Override
    public void render(DrawContext context, int index, int y, int x, int entryWidth, int entryHeight, int mouseX, int mouseY, boolean hovered, float tickDelta) {
      textFieldWidget.setY(y);
      textFieldWidget.render(context, mouseX, mouseY, tickDelta);
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
      return textFieldWidget.mouseClicked(mouseX, mouseY, button);
    }

    @Override
    public boolean mouseReleased(double mouseX, double mouseY, int button) {
      return textFieldWidget.mouseReleased(mouseX, mouseY, button);
    }

    /**
     * @see TextFieldListWidget#keyPressed(int, int, int)
     */
    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
      switch (keyCode) {
        case GLFW.GLFW_KEY_ENTER -> {
          final List<TextFieldListWidget.Entry> children = TextFieldListWidget.this.children();
          final int index = children.indexOf(this);
          if (index + 1 < children.size()) {
            TextFieldListWidget.this.setFocused(children.get(index + 1));
          } else if (!children.isEmpty()) {
            signBlockEditScreen.addTextField(index + 1, false);
          }
        }
        case GLFW.GLFW_KEY_BACKSPACE -> {
          if (textFieldWidget.getText().isEmpty()) {
            final int index = TextFieldListWidget.this.children().indexOf(this);
            if (index >= 0) {
              signBlockEditScreen.removeTextField(index, true);
            }
          }
        }
      }
      return super.keyPressed(keyCode, scanCode, modifiers)
          || textFieldWidget.keyPressed(keyCode, scanCode, modifiers);
    }

    @Override
    public boolean isMouseOver(double mouseX, double mouseY) {
      return super.isMouseOver(mouseX, mouseY) || textFieldWidget.isMouseOver(mouseX, mouseY);
    }

    @Override
    public void mouseMoved(double mouseX, double mouseY) {
      super.mouseMoved(mouseX, mouseY);
      textFieldWidget.mouseMoved(mouseX, mouseY);
    }

    @Override
    public boolean mouseDragged(
        double mouseX, double mouseY, int button, double deltaX, double deltaY) {
      if (button == 0 && mouseX >= getScrollbarPositionX() && mouseX < getScrollbarPositionX() + 6) {
        return false;
      }
      return super.mouseDragged(mouseX, mouseY, button, deltaX, deltaY)
          || textFieldWidget.mouseDragged(mouseX, mouseY, button, deltaX, deltaY);
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double horizontalAmount, double verticalAmount) {
      return super.mouseScrolled(mouseX, mouseY, horizontalAmount, verticalAmount)
          || textFieldWidget.mouseScrolled(mouseX, mouseY, horizontalAmount, verticalAmount);
    }

    @Override
    public boolean keyReleased(int keyCode, int scanCode, int modifiers) {
      return super.keyReleased(keyCode, scanCode, modifiers)
          || textFieldWidget.keyReleased(keyCode, scanCode, modifiers);
    }

    @Override
    public boolean charTyped(char chr, int modifiers) {
      return super.charTyped(chr, modifiers) || textFieldWidget.charTyped(chr, modifiers);
    }

    @Override
    public Text getNarration() {
      return textFieldWidget.getMessage();
    }

    @Override
    public void appendNarrations(NarrationMessageBuilder builder) {
      textFieldWidget.appendNarrations(builder);
    }

    @Override
    public void setFocused(boolean focused) {
      super.setFocused(focused);
      textFieldWidget.setFocused(focused);
    }
  }
}
