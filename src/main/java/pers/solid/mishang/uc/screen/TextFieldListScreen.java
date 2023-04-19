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

/**
 * 文本框列表的屏幕。每个列表项都是一个文本框（实际上就是把 {@link TextFieldWidget} 包装成了 {@link TextFieldListScreen.Entry}。<p>
 * 此类原本是 {@link AbstractSignBlockEditScreen} 的内部类，后面独立出来了。
 */
@Environment(EnvType.CLIENT)
public class TextFieldListScreen extends AlwaysSelectedEntryListWidget<TextFieldListScreen.Entry> {

  private final AbstractSignBlockEditScreen<?> signBlockEditScreen;

  public TextFieldListScreen(AbstractSignBlockEditScreen<?> signBlockEditScreen,
                             MinecraftClient client, int width, int height, int top, int bottom, int itemHeight) {
    super(client, width, height, top, bottom, itemHeight);
    this.signBlockEditScreen = signBlockEditScreen;
    this.setRenderBackground(false);
    this.setRenderHeader(false, 0);
    this.setRenderSelection(false);
  }

  private boolean isFocused;

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
   * @param entry 需要选中的 {@link TextFieldListScreen.Entry}。
   * @see AbstractSignBlockEditScreen#setFocused(Element)
   */
  @Override
  public void setFocused(@Nullable Element entry) {
    if (entry != null) {
      super.setFocused(entry);
    }
    for (TextFieldListScreen.Entry child : children()) {
      child.textFieldWidget.setFocused(this.isFocused() && child == entry);
    }
    if (entry instanceof TextFieldListScreen.Entry) {
      signBlockEditScreen.selectedTextField = ((Entry) entry).textFieldWidget;
      signBlockEditScreen.selectedTextContext = signBlockEditScreen.contextToWidgetBiMap.inverse().get(((Entry) entry).textFieldWidget);

      // 设置焦点后，重新设置 customColorTextField 的内容
      if (signBlockEditScreen.selectedTextContext != null) {
        signBlockEditScreen.customColorTextField.setText(String.format("#%06x", signBlockEditScreen.selectedTextContext.color));
      }
    } else if (children().isEmpty() || !MinecraftClient.getInstance().getNavigationType().isKeyboard()) {
      // 使用键盘导航至其他按钮的时候，不设为 null。
      signBlockEditScreen.selectedTextField = null;
      signBlockEditScreen.selectedTextContext = null;
    }
  }

  @Override
  public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
    if (children().size() != 0 && keyCode == GLFW.GLFW_KEY_UP) {
      setFocused(children().get(MathHelper.floorMod(children().indexOf(getFocused()) - 1, children().size())));
      return true;
    } else if (children().size() != 0 && keyCode == GLFW.GLFW_KEY_DOWN) {
      setFocused(children().get(MathHelper.floorMod(children().indexOf(getFocused()) + 1, children().size())));
      return true;
    } else {
      return super.keyPressed(keyCode, scanCode, modifiers);
    }
  }

  @Override
  public int getRowWidth() {
    return width;
  }

  @Override
  public boolean mouseClicked(double mouseX, double mouseY, int button) {
    this.updateScrollingState(mouseX, mouseY, button);
    if (!this.isMouseOver(mouseX, mouseY)) {
      return false;
    }
    TextFieldListScreen.Entry entry = this.getEntryAtPosition(mouseX, mouseY);
    if (entry != null) {
      if (entry.mouseClicked(mouseX, mouseY, button)) {
        this.setFocused(entry);
        this.setDragging(true);
        return true;
      }
    } else if (button == 0) {
      this.clickedHeader((int) (mouseX - (double) (this.left + this.width / 2 - this.getRowWidth() / 2)), (int) (mouseY - (double) this.top) + (int) this.getScrollAmount() - 4);
      return true;
    }
    return super.mouseClicked(-1, -1, button);
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

  /**
   * {@link TextFieldListScreen} 中的项。由于 {@link TextFieldWidget} 不是 {@link EntryListWidget.Entry}
   * 的子类，所以对该类进行了包装。
   */
  @Environment(EnvType.CLIENT)
  public class Entry extends AlwaysSelectedEntryListWidget.Entry<TextFieldListScreen.Entry> implements Narratable {
    public final @NotNull TextFieldWidget textFieldWidget;


    public Entry(@NotNull TextFieldWidget textFieldWidget) {
      this.textFieldWidget = textFieldWidget;
    }

    @Override
    public boolean equals(Object o) {
      if (this == o) return true;
      if (!(o instanceof TextFieldListScreen.Entry entry)) return false;

      return textFieldWidget.equals(entry.textFieldWidget);
    }

    @Override
    public int hashCode() {
      return textFieldWidget.hashCode();
    }

    @Override
    public void render(
        MatrixStack matrices,
        int index,
        int y,
        int x,
        int entryWidth,
        int entryHeight,
        int mouseX,
        int mouseY,
        boolean hovered,
        float tickDelta) {
      if (isFocused() && textFieldWidget.isVisible()) {
        fill(matrices, textFieldWidget.getX() - 2, y - 2, textFieldWidget.getX() + textFieldWidget.getWidth() + 2, y + textFieldWidget.getHeight() + 2, 0xfff0f0f0);
      }
      textFieldWidget.setY(y);
      textFieldWidget.render(matrices, mouseX, mouseY, tickDelta);
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
      return super.mouseClicked(mouseX, mouseY, button)
          || textFieldWidget.mouseClicked(mouseX, mouseY, button);
    }

    @Override
    public boolean mouseReleased(double mouseX, double mouseY, int button) {
      return super.mouseReleased(mouseX, mouseY, button)
          || textFieldWidget.mouseReleased(mouseX, mouseY, button);
    }

    /**
     * @see TextFieldListScreen#keyPressed(int, int, int)
     */
    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
      switch (keyCode) {
        case GLFW.GLFW_KEY_ENTER -> {
          final List<TextFieldListScreen.Entry> children = TextFieldListScreen.this.children();
          final int index = children.indexOf(getSelectedOrNull());
          if (index + 1 < children.size())
            TextFieldListScreen.this.setFocused(children.get(index + 1));
          else if (children.size() > 0) signBlockEditScreen.addTextField(index + 1);
        }
        case GLFW.GLFW_KEY_BACKSPACE -> {
          final TextFieldListScreen.Entry focused = TextFieldListScreen.this.getSelectedOrNull();
          if (focused != null && textFieldWidget.getText().isEmpty()) {
            final int index = TextFieldListScreen.this.children().indexOf(focused);
            if (index >= 0) {
              signBlockEditScreen.removeTextField(index);
              if (index - 1 >= 0 && index - 1 < TextFieldListScreen.this.children().size()) {
                TextFieldListScreen.this.setFocused(TextFieldListScreen.this.children().get(index - 1));
              }
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
    public Text getNarration() {
      return textFieldWidget.getMessage();
    }

    @Override
    public void appendNarrations(NarrationMessageBuilder builder) {
      textFieldWidget.appendNarrations(builder);
    }
  }
}
