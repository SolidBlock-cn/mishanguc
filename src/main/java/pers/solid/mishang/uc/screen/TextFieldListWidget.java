package pers.solid.mishang.uc.screen;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.Element;
import net.minecraft.client.gui.Narratable;
import net.minecraft.client.gui.screen.narration.NarrationMessageBuilder;
import net.minecraft.client.gui.screen.narration.NarrationPart;
import net.minecraft.client.gui.widget.AlwaysSelectedEntryListWidget;
import net.minecraft.client.gui.widget.EntryListWidget;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;
import net.minecraft.util.math.MathHelper;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.lwjgl.glfw.GLFW;
import pers.solid.mishang.uc.util.TextBridge;

import java.util.List;
import java.util.function.Predicate;

/**
 * 文本框列表的屏幕。每个列表项都是一个文本框（实际上就是把 {@link TextFieldWidget} 包装成了 {@link TextFieldListWidget.Entry}。<p>
 * 此类原本是 {@link AbstractSignBlockEditScreen} 的内部类，后面独立出来了。
 */
@Environment(EnvType.CLIENT)
public class TextFieldListWidget extends AlwaysSelectedEntryListWidget<TextFieldListWidget.Entry> {

  private final AbstractSignBlockEditScreen<?> signBlockEditScreen;

  public TextFieldListWidget(AbstractSignBlockEditScreen<?> signBlockEditScreen,
                             MinecraftClient client, int width, int height, int top, int bottom, int itemHeight) {
    super(client, width, height, top, bottom, itemHeight);
    this.signBlockEditScreen = signBlockEditScreen;
    this.setRenderBackground(false);
    this.setRenderHeader(false, 0);
    this.setRenderSelection(false);
  }

  private boolean isFocused;

  @Override
  public boolean changeFocus(boolean lookForwards) {
    return (this.isFocused = !isFocused) && !children().isEmpty();
  }

  /**
   * 与 {@link EntryListWidget#moveSelectionIf(MoveDirection, Predicate)} 相比，把 {@link MathHelper#clamp} 改成了 {@link MathHelper#floorMod}。
   */
  @Override
  protected boolean moveSelectionIf(MoveDirection direction, Predicate<TextFieldListWidget.Entry> predicate) {
    int i = direction == EntryListWidget.MoveDirection.UP ? -1 : 1;
    if (!this.children().isEmpty()) {
      int j = this.children().indexOf(this.getSelectedOrNull());
      while (true) {
        int k = MathHelper.floorMod(j + i, this.getEntryCount());
        if (j == k) {
          break;
        }
        TextFieldListWidget.Entry entry = this.children().get(k);
        if (predicate.test(entry)) {
          this.setSelected(entry);
          this.ensureVisible(entry);
          return true;
        }
        j = k;
      }
    }
    return false;
  }

  @Override
  public boolean isFocused() {
    // 防止此元素总被视为已经 focused
    return isFocused;
  }

  public void setFocused(boolean isFocused) {
    this.isFocused = isFocused;
  }

  /**
   * 设置当前 TextFieldListScreen 的已选中的文本框。
   *
   * @param entry 需要选中的 {@link TextFieldListWidget.Entry}。
   * @see AbstractSignBlockEditScreen#setFocused(Element)
   */
  @Override
  public void setSelected(@Nullable TextFieldListWidget.Entry entry) {
    super.setSelected(entry);
    setFocused(entry);
    for (TextFieldListWidget.Entry child : children()) {
      child.textFieldWidget.setTextFieldFocused(child == entry);
    }
    if (entry != null) {
      signBlockEditScreen.selectedTextField = entry.textFieldWidget;
      signBlockEditScreen.selectedTextContext = signBlockEditScreen.contextToWidgetBiMap.inverse().get(entry.textFieldWidget);
      ensureVisible(entry);
    } else if (children().isEmpty()) {
      signBlockEditScreen.selectedTextField = null;
      signBlockEditScreen.selectedTextContext = null;
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
        setSelected(children().get(MathHelper.floorMod(children().indexOf(getFocused()) - 1, children().size())));
        return true;
      } else if (keyCode == GLFW.GLFW_KEY_DOWN) {
        setSelected(children().get(MathHelper.floorMod(children().indexOf(getFocused()) + 1, children().size())));
        return true;
      }
    } else if (keyCode == GLFW.GLFW_KEY_ENTER || keyCode == GLFW.GLFW_KEY_KP_ENTER) {
      // 此时，children().isEmpty() 为 true
      signBlockEditScreen.addTextField(0);
      return true;
    }
    return super.keyPressed(keyCode, scanCode, modifiers);
  }

  @Override
  public int getRowWidth() {
    return width;
  }

  @Override
  protected int getScrollbarPositionX() {
    return width - 6;
  }

  @ApiStatus.AvailableSince("mc1.17")
  @Override
  public void appendNarrations(NarrationMessageBuilder builder) {
    builder.put(NarrationPart.TITLE, TextBridge.translatable("narration.mishanguc.text_field_list"));
    builder.put(NarrationPart.USAGE, TextBridge.translatable("narration.mishanguc.text_field_list.usage"));
    super.appendNarrations(builder);
  }

  @Override
  public void setScrollAmount(double amount) {
    super.setScrollAmount(amount);
    int width = getMaxScroll() > 0 ? this.width - 10 : this.width - 4;
    for (Entry child : children()) {
      child.textFieldWidget.setWidth(width);
    }
  }

  /**
   * {@link TextFieldListWidget} 中的项。由于 {@link TextFieldWidget} 不是 {@link EntryListWidget.Entry}
   * 的子类，所以对该类进行了包装。
   */
  @Environment(EnvType.CLIENT)
  public class Entry extends AlwaysSelectedEntryListWidget.Entry<TextFieldListWidget.Entry> implements Narratable {
    public final @NotNull TextFieldWidget textFieldWidget;


    public Entry(@NotNull TextFieldWidget textFieldWidget) {
      this.textFieldWidget = textFieldWidget;
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
    public void render(MatrixStack matrices, int index, int y, int x, int entryWidth, int entryHeight, int mouseX, int mouseY, boolean hovered, float tickDelta) {
      if (getSelectedOrNull() == this && textFieldWidget.isVisible() && isFocused()) {
        fill(matrices, textFieldWidget.getX() - 2, y - 2, textFieldWidget.getX() + textFieldWidget.getWidth() + 2, y + textFieldWidget.getHeight() + 2, 0xfff0f0f0);
      }
      textFieldWidget.setY(y);
      textFieldWidget.render(matrices, mouseX, mouseY, tickDelta);
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
      if (button == 0) {
        setSelected(this);
      }
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
          final int index = children.indexOf(getSelectedOrNull());
          if (index + 1 < children.size())
            TextFieldListWidget.this.setSelected(children.get(index + 1));
          else if (children.size() > 0)
            signBlockEditScreen.addTextField(index + 1);
        }
        case GLFW.GLFW_KEY_BACKSPACE -> {
          final TextFieldListWidget.Entry focused = TextFieldListWidget.this.getSelectedOrNull();
          if (focused != null && textFieldWidget.getText().isEmpty()) {
            final int index = TextFieldListWidget.this.children().indexOf(focused);
            if (index >= 0) {
              signBlockEditScreen.removeTextField(index);
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
    public boolean mouseScrolled(double mouseX, double mouseY, double amount) {
      return super.mouseScrolled(mouseX, mouseY, amount)
          || textFieldWidget.mouseScrolled(mouseX, mouseY, amount);
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
    public boolean changeFocus(boolean lookForwards) {
      return super.changeFocus(lookForwards) || textFieldWidget.changeFocus(lookForwards);
    }

    @Override
    public Text getNarration() {
      return textFieldWidget.getMessage();
    }

    @Override
    public void appendNarrations(NarrationMessageBuilder builder) {
      textFieldWidget.appendNarrations(builder);
    }
  }
}
