package pers.solid.mishang.uc.screen;

import com.google.common.collect.Lists;
import com.google.common.util.concurrent.Runnables;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.serialization.JsonOps;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractSelectionList;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.components.ObjectSelectionList;
import net.minecraft.client.gui.components.Tooltip;
import net.minecraft.client.gui.components.events.ContainerEventHandler;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.narration.NarratedElementType;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.client.gui.narration.NarrationSupplier;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.input.CharacterEvent;
import net.minecraft.client.input.KeyEvent;
import net.minecraft.client.input.MouseButtonEvent;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.nbt.NbtOps;
import net.minecraft.nbt.SnbtGrammar;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.ComponentSerialization;
import net.minecraft.network.chat.ComponentUtils;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.contents.PlainTextContents;
import net.minecraft.resources.Identifier;
import net.minecraft.util.Mth;
import net.minecraft.util.parsing.packrat.commands.Grammar;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.UnmodifiableView;
import org.lwjgl.glfw.GLFW;
import pers.solid.mishang.uc.Mishanguc;
import pers.solid.mishang.uc.mixin.ContainerWidgetAccessor;
import pers.solid.mishang.uc.mixin.EntryListWidgetAccessor;
import pers.solid.mishang.uc.text.SpecialDrawable;
import pers.solid.mishang.uc.text.TextContext;
import pers.solid.mishang.uc.util.TextBridge;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static pers.solid.mishang.uc.screen.MishangScreenUtil.hasControlDown;
import static pers.solid.mishang.uc.screen.MishangScreenUtil.hasShiftDown;

/**
 * 文本框列表的屏幕。每个列表项都是一个文本框（实际上就是把 {@link EditBox} 包装成了 {@link pers.solid.mishang.uc.screen.TextFieldListWidget.Entry}。<p>
 * 此类原本是 {@link AbstractSignBlockEditScreen} 的内部类，后面独立出来了。
 */
@Environment(EnvType.CLIENT)
public class TextFieldListWidget extends ObjectSelectionList<TextFieldListWidget.Entry> {

  private static final Identifier background = Identifier.withDefaultNamespace("textures/gui/inworld_menu_background.png");
  /**
   * 被选中的多个项的列表，通常包含 {@link #selected} 的对象但不一定。一般通过 {@link pers.solid.mishang.uc.screen.TextFieldListWidget.Entry#setSelected(boolean)} 来修改。
   */
  protected final Set<TextFieldListWidget.Entry> selectedEntries = new HashSet<>();
  private final AbstractSignBlockEditScreen<?> signBlockEditScreen;
  private boolean simplified;
  /**
   * 用于显示时渲染背景时的高度。通常情况下与 {@link #height} 保持一致，但简化模式下会使用不一致的值。{@link #setHeight(int)} 方法会同步更新此字段的值。
   */
  protected int heightForBackground;
  /**
   * 用于在简化模式下渲染内容的高度。在简化模式下，此值与实际的 {@link #height} 保持一致。
   */
  protected int cuttingHeight = 48;
  /**
   * 在按住 Shift 进行多选时，多选起始的元素。在非 Shift 模式下进行任意选择后，此字段清空。
   */
  private @Nullable pers.solid.mishang.uc.screen.TextFieldListWidget.Entry startContEntry;

  public TextFieldListWidget(AbstractSignBlockEditScreen<?> signBlockEditScreen,
                             Minecraft client, int width, int height, int y, int itemHeight) {
    super(client, width, height, y, itemHeight);
    this.signBlockEditScreen = signBlockEditScreen;
    this.heightForBackground = height;
  }

  /**
   * 在设置高度的同时，会同时更新自身的高度。注意即使是在 simplified 模式下，参数 {@code height} 的值仍应是完整的高度，如 {@link #heightForBackground}，而非 {@link #cuttingHeight} 的值，通常也不应该传入 {@link #height}。
   */
  @Override
  public void setHeight(int height) {
    if (simplified) {
      super.setHeight(cuttingHeight);
    } else {
      super.setHeight(height);
    }
    this.heightForBackground = height;
  }

  /**
   * 类似于 {@link #setFocused(GuiEventListener)}，但是支持在调用 {@link #setSelected(pers.solid.mishang.uc.screen.TextFieldListWidget.Entry, boolean, boolean)} 时指定参数。
   */
  public void setFocusedAndSelected(@Nullable pers.solid.mishang.uc.screen.TextFieldListWidget.Entry focused, boolean multiSel, boolean contSel) {
    pers.solid.mishang.uc.screen.TextFieldListWidget.Entry entry = this.getFocused();
    if (entry != focused && entry instanceof ContainerEventHandler parentElement) {
      parentElement.setFocused(null);
    }

    ((ContainerWidgetAccessor) this).setFocusedRaw(focused);
    this.setSelected(focused, multiSel, contSel);
    if (focused != null) {
      this.scrollToEntry(focused);
    }
  }

  /**
   * 设置当前 TextFieldListScreen 的已选中的文本框。
   *
   * @param entry 需要选中的 {@link pers.solid.mishang.uc.screen.TextFieldListWidget.Entry}。
   * @implNote 此对象的 {@link #selected} 一般不是 null，而 {@link #focused} 会在此对象（{@link TextFieldListWidget}）失焦时变成 {@code null}。
   * @see AbstractSignBlockEditScreen#setFocused(GuiEventListener)
   */
  @Override
  public void setSelected(@Nullable TextFieldListWidget.Entry entry) {
    setSelected(entry, hasControlDown(), hasShiftDown());
  }

  /**
   * 设置当前 TextFieldListScreen 的已选中的文本框。
   *
   * @param entry    需要选中的 {@link pers.solid.mishang.uc.screen.TextFieldListWidget.Entry}。
   * @param multiSel 是否多选。如果为 {@code false}，则之前已经选中的其他元素将会未选中。
   * @param contSel  是否连续选。如果为 {@code true}，则将之前选中的和当前选中的均选中。
   * @see AbstractSignBlockEditScreen#setFocused(GuiEventListener)
   */
  public void setSelected(@Nullable TextFieldListWidget.Entry entry, boolean multiSel, boolean contSel) {
    final pers.solid.mishang.uc.screen.TextFieldListWidget.Entry prevSelected = getSelected();
    super.setSelected(entry);

    if (entry == prevSelected && Minecraft.getInstance().getLastInputType().isKeyboard()) {
      // 通常是从其他地方通过键盘焦点返回此处的情形，不执行操作。
      Runnables.doNothing().run();
    } else if (entry instanceof pers.solid.mishang.uc.screen.TextFieldListWidget.Entry) {
      if (contSel) {
        if (startContEntry == null) {
          startContEntry = prevSelected;
        }
      } else {
        startContEntry = null;
      }
      final int contFrom = contSel ? children().indexOf(startContEntry) : -1;
      if (!multiSel) {
        for (pers.solid.mishang.uc.screen.TextFieldListWidget.Entry selectedEntry : Set.copyOf(selectedEntries)) {
          selectedEntry.setSelected(false);
        }
      }

      final int contUntil = contSel ? children().indexOf(entry) : -1;
      if (contFrom != -1 && contUntil != -1 && contFrom != contUntil) {
        final int min = Math.min(contFrom, contUntil);
        final int max = Math.max(contFrom, contUntil);

        for (int i = min; i <= max; i++) {
          final pers.solid.mishang.uc.screen.TextFieldListWidget.Entry entry1 = children().get(i);
          entry1.setSelected(true);
        }
      } else if (multiSel && selectedEntries.contains(entry)) {
        // 在多选模式下，如果再次选中同一个，则失掉这个选择。
        entry.setSelected(false);
        if (getSelected() == entry) {
          super.setSelected(null);
        }
      } else {
        entry.setSelected(true);
      }
    }

    // 更新屏幕按钮中的一些 tooltip
    for (GuiEventListener child : signBlockEditScreen.children()) {
      if (child instanceof TooltipUpdated tooltipUpdated) {
        tooltipUpdated.updateTooltip();
      }
    }
  }

  @Override
  public boolean keyPressed(KeyEvent input) {
    if (!children().isEmpty()) {
      if (input.isUp()) {
        setFocused(children().get(Mth.positiveModulo(children().indexOf(getSelected()) - 1, children().size())));
        return true;
      } else if (input.isDown()) {
        setFocused(children().get(Mth.positiveModulo(children().indexOf(getSelected()) + 1, children().size())));
        return true;
      }
    } else if (input.isConfirmation()) {
      // 此时，children().isEmpty() 为 true
      final pers.solid.mishang.uc.screen.TextFieldListWidget.Entry newEntry = addEmptyTextField(0);
      TextFieldListWidget.this.setFocusedAndSelected(newEntry, false, false);
      signBlockEditScreen.setFocused(TextFieldListWidget.this);
      return true;
    }
    if (selectedEntries.size() > 1) {
      boolean success = false;
      for (pers.solid.mishang.uc.screen.TextFieldListWidget.Entry selectedEntry : List.copyOf(selectedEntries)) {
        success = selectedEntry.keyPressed(input) || success;
      }
      return success;
    }
    return super.keyPressed(input);
  }

  @Override
  public boolean charTyped(CharacterEvent input) {
    if (selectedEntries.size() > 1) {
      boolean success = false;
      for (pers.solid.mishang.uc.screen.TextFieldListWidget.Entry selectedEntry : selectedEntries) {
        success = selectedEntry.charTyped(input) || success;
      }
      return success;
    }
    return super.charTyped(input);
  }

  @Override
  public int getRowWidth() {
    return width;
  }

  @Override
  public int getRowTop(int index) {
    return super.getRowTop(index) - 2;
  }

  @Override
  protected int scrollBarX() {
    return width - 6;
  }

  @Override
  public void updateWidgetNarration(NarrationElementOutput builder) {
    builder.add(NarratedElementType.TITLE, TextBridge.translatable("narration.mishanguc.text_field_list"));
    builder.add(NarratedElementType.USAGE, TextBridge.translatable("narration.mishanguc.text_field_list.usage"));
    super.updateWidgetNarration(builder);
  }

  @Override
  public void renderWidget(GuiGraphics guiGraphics, int i, int j, float f) {
    super.renderWidget(guiGraphics, i, j, f);
  }

  @Override
  protected void renderSelection(GuiGraphics context, Entry entry, int color) {
    int i = entry.getX();
    int j = entry.getY();
    int k = i + entry.getWidth();
    int l = j + entry.getHeight();
    context.fill(i + 1, j - 1, k - 1, l, 0xe0ffffff);
  }

  @Contract(pure = true)
  protected boolean isSimplified() {
    return simplified;
  }

  protected void setSimplified(boolean simplified) {
    this.simplified = simplified;
    this.setHeight(heightForBackground);
    this.setScrollAmount(scrollAmount());
    final pers.solid.mishang.uc.screen.TextFieldListWidget.Entry selectedOrNull = getSelected();
    if (selectedOrNull != null) {
      scrollToEntry(selectedOrNull);
    }
  }

  protected void increaseHeight(int amount) {
    cuttingHeight = (Math.clamp(cuttingHeight + amount, 0, heightForBackground));
    this.setHeight(heightForBackground);
    setScrollAmount(scrollAmount()); // 更新滚动以避免滚动溢出
    final pers.solid.mishang.uc.screen.TextFieldListWidget.Entry selectedOrNull = getSelected();
    if (selectedOrNull != null) {
      scrollToEntry(selectedOrNull);
    }
  }

  protected void renderListSeparators(GuiGraphics context) {
    super.renderListSeparators(context);
    if (simplified) {
      // 简化模式下，多显示一个。
      Identifier identifier2 = this.minecraft.level == null ? Screen.FOOTER_SEPARATOR : Screen.INWORLD_FOOTER_SEPARATOR;
      context.blit(RenderPipelines.GUI_TEXTURED, identifier2, this.getX(), this.getY() + heightForBackground, 0.0F, 0.0F, this.getWidth(), 2, 32, 2);
    }
  }


  @Override
  protected void renderListBackground(GuiGraphics context) {
    Identifier identifier = background;
    context.blit(RenderPipelines.GUI_TEXTURED, identifier, this.getX(), 0, 0, 0, this.getWidth(), this.getY(), 32, 32);
    context.blit(RenderPipelines.GUI_TEXTURED, identifier, this.getX(), getY() + heightForBackground, 0, 0, this.getWidth(), heightForBackground, 32, 32);
  }

  /**
   * 添加一个文本框。此方法执行时，不会设置任何的选择或聚焦。
   *
   * @param index       添加的文本的位置，可以设置为 -1，表示添加到最后一个。
   * @param textContext 需要添加的 {@link TextContext}。
   * @param isExisting  是否为现有的，如果是，则不会将 {@link AbstractSignBlockEditScreen#changed} 设为 <code>true</code>。
   */
  public pers.solid.mishang.uc.screen.TextFieldListWidget.Entry addTextField(int index, TextContext textContext, boolean isExisting) {
    if (!isExisting) {
      signBlockEditScreen.changed = true;
    }
    final pers.solid.mishang.uc.screen.TextFieldListWidget.Entry newEntry = createEntry(textContext);
    final int newIndex = addEntry(newEntry);
    if (index != -1) {
      @SuppressWarnings("unchecked") final List<pers.solid.mishang.uc.screen.TextFieldListWidget.Entry> rawChildren = ((EntryListWidgetAccessor<pers.solid.mishang.uc.screen.TextFieldListWidget.Entry>) this).getChildren();
      rawChildren.remove(newIndex);
      rawChildren.add(index, newEntry);
    }
    setScrollAmount(scrollAmount()); // 此处会调用私有方法 recalculateAllChildrenPositions，重新设置其宽度

    signBlockEditScreen.updateContentVisibility();

    return newEntry;
  }

  private pers.solid.mishang.uc.screen.TextFieldListWidget.Entry createEntry(TextContext textContext) {
    final EditBox textFieldWidget = new EditBox(Objects.requireNonNull(signBlockEditScreen.getFont(), "textRenderer"), 2, 0, signBlockEditScreen.width - 4, 15, TextBridge.empty());
    textFieldWidget.setMaxLength(Integer.MAX_VALUE);
    if (textContext.extra != null) {
      textFieldWidget.setValue(String.format("-%s %s", textContext.extra.getId(), textContext.extra.asStringArgs()));
    } else if (textContext.text != null) {
      if (textContext.text.getContents() instanceof PlainTextContents plainTextContent && textContext.text.getSiblings().isEmpty() && textContext.text.getStyle().isEmpty()) {
        final String text = plainTextContent.text();
        if (Pattern.compile("^-(\\w+?) (.+)$").matcher(text).matches()) {
          textFieldWidget.setValue("-literal " + text);
        } else {
          textFieldWidget.setValue(text);
        }
      } else {
        textFieldWidget.setValue("-nbt " + ComponentSerialization.CODEC.encodeStart(signBlockEditScreen.registryLookup.createSerializationContext(NbtOps.INSTANCE), textContext.text).getOrThrow().toString());
      }
    }
    final pers.solid.mishang.uc.screen.TextFieldListWidget.Entry newEntry = new pers.solid.mishang.uc.screen.TextFieldListWidget.Entry(textFieldWidget, textContext);
    textFieldWidget.setResponder(s -> {
      final TextContext textContext1 = newEntry.textContext;
      final Matcher matcher = Pattern.compile("^-(\\w+?) (.+)$").matcher(s);
      textFieldWidget.setTooltip(null);
      textFieldWidget.setTextColor(0xffe0e0e0);
      if (matcher.matches()) {
        final String name = matcher.group(1);
        final String value = matcher.group(2);
        switch (name) {
          case "literal" -> textContext1.text = TextBridge.literal(value);
          case "json" -> {
            try {
              final JsonElement jsonElement = TextContext.GSON.fromJson(value, JsonElement.class);
              textContext1.text = (MutableComponent) ComponentSerialization.CODEC.parse(signBlockEditScreen.registryLookup.createSerializationContext(JsonOps.INSTANCE), jsonElement).getOrThrow();
            } catch (JsonParseException | IllegalStateException e) {
              textFieldWidget.setTextColor(0xffff5555);
              textFieldWidget.setTooltip(Tooltip.create(Component.literal(e.getMessage())));
            }
          }
          case "nbt" -> {
            try {
              final Grammar<Tag> parser = SnbtGrammar.createParser(NbtOps.INSTANCE);
              final StringReader reader = new StringReader(value);
              final Tag nbtElement = parser.parseForCommands(reader);
              if (reader.canRead()) {
                throw CommandSyntaxException.BUILT_IN_EXCEPTIONS.dispatcherUnknownArgument().createWithContext(reader);
              }
              textContext1.text = (MutableComponent) ComponentSerialization.CODEC.parse(signBlockEditScreen.registryLookup.createSerializationContext(NbtOps.INSTANCE), nbtElement).getOrThrow();
            } catch (CommandSyntaxException e) {
              textFieldWidget.setTextColor(0xffff5555);
              textFieldWidget.setTooltip(Tooltip.create(ComponentUtils.fromMessage(e.getRawMessage())));
            } catch (IllegalStateException e) {
              textFieldWidget.setTextColor(0xffff5555);
              textFieldWidget.setTooltip(Tooltip.create(Component.literal(e.getMessage())));
            }
          }
          default -> {
            final SpecialDrawable specialDrawable;
            try {
              specialDrawable = SpecialDrawable.fromStringArgs(textContext1, name, value);
              if (specialDrawable == SpecialDrawable.INVALID) { // 如果为 INVALID 则文本为红色。
                textFieldWidget.setTextColor(0xffff5555);
              } else if (specialDrawable != null) {
                textContext1.extra = specialDrawable;
                textContext1.text = TextBridge.empty();
              } else {
                textContext1.extra = null;
                textContext1.text = TextBridge.literal(s);
              }
            } catch (CommandSyntaxException e) {
              textFieldWidget.setTextColor(0xffff5555);
              textFieldWidget.setTooltip(Tooltip.create(ComponentUtils.fromMessage(e.getRawMessage())));
            }
          }
        }
      } else {
        textContext1.extra = null;
        textContext1.text = TextBridge.literal(s);
      }
      signBlockEditScreen.changed = true;
    });
    return newEntry;
  }

  /**
   * 添加一个新的文本框。
   *
   * @param index 添加到的位置，对应在数组或列表中的次序。
   */

  public pers.solid.mishang.uc.screen.TextFieldListWidget.Entry addEmptyTextField(int index) {
    // 添加时，默认相当于上一行的。
    final TextContext emptyTextContext = index > 0 ? children().get(index - 1).textContext.clone() : signBlockEditScreen.entity.createDefaultTextContext();
    emptyTextContext.text = null;
    emptyTextContext.extra = null;
    return addTextField(index, emptyTextContext, false);
  }

  /**
   * 移除一行文本。此方法执行时，不会自动选中相邻文本。
   */
  public void removeTextField(int index) {
    final List<pers.solid.mishang.uc.screen.TextFieldListWidget.Entry> children = children();
    final pers.solid.mishang.uc.screen.TextFieldListWidget.Entry removedEntry = children.get(index);
    removeEntry(removedEntry);
    removedEntry.setSelected(false);
    // 删除一行元素后，对滚动数量进行一次 clamp，以避免出现过度滚动的情况。
    setScrollAmount(scrollAmount()); // 此处会调用私有方法 recalculateAllChildrenPositions，重新设置其宽度

    signBlockEditScreen.updateContentVisibility();
    signBlockEditScreen.changed = true;
  }

  /**
   * 清除所有文本。
   */
  public void clearTextFields() {
    clearEntries();
    for (pers.solid.mishang.uc.screen.TextFieldListWidget.Entry selectedEntry : selectedEntries) {
      selectedEntry.textFieldWidget.setFocused(false);
    }
    selectedEntries.clear();

    signBlockEditScreen.updateContentVisibility();
    signBlockEditScreen.changed = true;
  }

  public void moveUpEntries(Collection<pers.solid.mishang.uc.screen.TextFieldListWidget.Entry> entries) {
    if (entries.isEmpty()) {
      return;
    }

    // 确保按顺序排序
    final List<pers.solid.mishang.uc.screen.TextFieldListWidget.Entry> childrenCopy = children();
    final List<pers.solid.mishang.uc.screen.TextFieldListWidget.Entry> orderedCopy = childrenCopy.stream().filter(entries::contains).toList();

    for (pers.solid.mishang.uc.screen.TextFieldListWidget.Entry entry : orderedCopy) {
      final int i = childrenCopy.indexOf(entry);
      if (i < 0) {
        Mishanguc.MISHANG_LOGGER.warn("Unexpected entry which is not in children when moving up: {}", entry);
        continue;
      } else if (i == 0) {
        // 顶到了第一元素，不能再移动。
        break;
      }
      swap(i, i - 1);
    }
  }

  public void moveDownEntries(Collection<pers.solid.mishang.uc.screen.TextFieldListWidget.Entry> entries) {
    if (entries.isEmpty()) {
      return;
    }

    // 确保按倒序排序
    final List<pers.solid.mishang.uc.screen.TextFieldListWidget.Entry> childrenCopy = children();
    final List<pers.solid.mishang.uc.screen.TextFieldListWidget.Entry> reversedEntries = Lists.reverse(childrenCopy).stream().filter(entries::contains).toList();

    for (pers.solid.mishang.uc.screen.TextFieldListWidget.Entry entry : reversedEntries) {
      final int i = childrenCopy.indexOf(entry);
      if (i < 0) {
        Mishanguc.MISHANG_LOGGER.warn("Unexpected entry which is not in children when moving down: {}", entry);
        continue;
      } else if (i == childrenCopy.size() - 1) {
        // 顶到了最后元素，不能再移动。
        break;
      }
      swap(i, i + 1);
    }
  }


  public @UnmodifiableView List<TextContext> getTextContexts() {
    return Lists.transform(children(), input -> input.textContext);
  }

  /**
   * {@link TextFieldListWidget} 中的项。由于 {@link EditBox} 不是 {@link AbstractSelectionList.Entry}
   * 的子类，所以对该类进行了包装。
   */
  @Environment(EnvType.CLIENT)
  public class Entry extends ObjectSelectionList.Entry<pers.solid.mishang.uc.screen.TextFieldListWidget.Entry> implements NarrationSupplier {
    public final EditBox textFieldWidget;
    public final TextContext textContext;

    public Entry(EditBox textFieldWidget, TextContext textContext) {
      this.textFieldWidget = textFieldWidget;
      this.textContext = textContext;
    }

    @Override
    public boolean equals(Object o) {
      if (this == o)
        return true;
      if (!(o instanceof pers.solid.mishang.uc.screen.TextFieldListWidget.Entry entry))
        return false;

      return textFieldWidget.equals(entry.textFieldWidget);
    }

    @Override
    public int hashCode() {
      return textFieldWidget.hashCode();
    }

    @Override
    public void setX(int x) {
      super.setX(x);
      textFieldWidget.setX(getContentX());
    }

    @Override
    public void setY(int y) {
      super.setY(y);
      textFieldWidget.setY(getContentY());
    }

    @Override
    public void setWidth(int width) {
      super.setWidth(width);
      final boolean scrollbarVisible = scrollbarVisible();
      final int elementWidth = getContentWidth() - (scrollbarVisible ? 6 : 0);
      textFieldWidget.setWidth(elementWidth);
    }

    @Override
    public void setHeight(int height) {
      super.setHeight(height);
    }

    @Override
    public int getContentY() {
      return super.getContentY() - 2;
    }

    @Override
    public int getContentHeight() {
      return super.getContentHeight() + 4;
    }

    @Override
    public void renderContent(GuiGraphics context, int mouseX, int mouseY, boolean hovered, float deltaTicks) {
      textFieldWidget.render(context, mouseX, mouseY, deltaTicks);
    }

    @Override
    public boolean mouseClicked(MouseButtonEvent click, boolean doubled) {
      return textFieldWidget.mouseClicked(click, doubled);
    }

    @Override
    public boolean mouseReleased(MouseButtonEvent click) {
      return textFieldWidget.mouseReleased(click);
    }

    /**
     * @see TextFieldListWidget#keyPressed(KeyEvent)
     */
    @Override
    public boolean keyPressed(KeyEvent input) {
      switch (input.input()) {
        case GLFW.GLFW_KEY_ENTER -> {
          final List<pers.solid.mishang.uc.screen.TextFieldListWidget.Entry> children = TextFieldListWidget.this.children();
          final int index = children.indexOf(this);
          if (index + 1 < children.size()) {
            TextFieldListWidget.this.setFocused(children.get(index + 1));
          } else if (!children.isEmpty()) {
            final pers.solid.mishang.uc.screen.TextFieldListWidget.Entry entry = addEmptyTextField(index + 1);
            TextFieldListWidget.this.setFocusedAndSelected(entry, false, false);
          }
        }
        case GLFW.GLFW_KEY_BACKSPACE -> {
          if (textFieldWidget.getValue().isEmpty()) {
            final int index = TextFieldListWidget.this.children().indexOf(this);
            if (index >= 0) {
              TextFieldListWidget.this.removeTextField(index);
              if (!children().isEmpty()) {
                final pers.solid.mishang.uc.screen.TextFieldListWidget.Entry nearbyEntry = TextFieldListWidget.this.children().get(Mth.clamp(index - 1, 0, children().size() - 1));
                TextFieldListWidget.this.setFocusedAndSelected(nearbyEntry, false, false);
              }
            }
          }
        }
      }
      return super.keyPressed(input) || textFieldWidget.keyPressed(input);
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
    public boolean mouseDragged(MouseButtonEvent click, double offsetX, double offsetY) {
      if (click.button() == 0 && click.x() >= scrollBarX() && click.x() < scrollBarX() + 6) {
        return false;
      }
      return super.mouseDragged(click, offsetX, offsetY)
          || textFieldWidget.mouseDragged(click, offsetX, offsetY);
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double horizontalAmount, double verticalAmount) {
      return super.mouseScrolled(mouseX, mouseY, horizontalAmount, verticalAmount)
          || textFieldWidget.mouseScrolled(mouseX, mouseY, horizontalAmount, verticalAmount);
    }

    @Override
    public boolean keyReleased(KeyEvent input) {
      return super.keyReleased(input)
          || textFieldWidget.keyReleased(input);
    }

    @Override
    public boolean charTyped(CharacterEvent input) {
      return super.charTyped(input) || textFieldWidget.charTyped(input);
    }

    @Override
    public Component getNarration() {
      return textFieldWidget.getMessage();
    }

    @Override
    public void updateNarration(NarrationElementOutput builder) {
      textFieldWidget.updateNarration(builder);
    }

    @Override
    public void setFocused(boolean focused) {
      super.setFocused(focused);
    }

    /**
     * <p>标记此元素是否被选中，与 {@link #focused} 有区别，即使 {@link TextFieldListWidget} 对象失焦时，此字段可能仍为 {@code true}，从而确保文本框的边缘能够正常用白色显示。
     * <p>修改此对象时，也会一并修改 {@link #selectedEntries}。
     */
    public void setSelected(boolean selected) {
      // 即使是因为焦点转移到其他元素，导致此元素的 focused 为 false，其文本框仍为 focused
      this.setFocused(selected);
      if (selected) {
        selectedEntries.add(this);
      } else {
        selectedEntries.remove(this);
      }
      textFieldWidget.setFocused(selected);
    }
  }
}
