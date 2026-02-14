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
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gl.RenderPipelines;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.Element;
import net.minecraft.client.gui.Narratable;
import net.minecraft.client.gui.ParentElement;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.narration.NarrationMessageBuilder;
import net.minecraft.client.gui.screen.narration.NarrationPart;
import net.minecraft.client.gui.tooltip.Tooltip;
import net.minecraft.client.gui.widget.AlwaysSelectedEntryListWidget;
import net.minecraft.client.gui.widget.EntryListWidget;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtOps;
import net.minecraft.nbt.SnbtParsing;
import net.minecraft.text.*;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.packrat.PackratParser;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.UnmodifiableView;
import org.lwjgl.glfw.GLFW;
import pers.solid.mishang.uc.Mishanguc;
import pers.solid.mishang.uc.mixin.ContainerWidgetAccessor;
import pers.solid.mishang.uc.text.SpecialDrawable;
import pers.solid.mishang.uc.text.TextContext;
import pers.solid.mishang.uc.util.TextBridge;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 文本框列表的屏幕。每个列表项都是一个文本框（实际上就是把 {@link TextFieldWidget} 包装成了 {@link Entry}。<p>
 * 此类原本是 {@link AbstractSignBlockEditScreen} 的内部类，后面独立出来了。
 */
@Environment(EnvType.CLIENT)
public class TextFieldListWidget extends AlwaysSelectedEntryListWidget<TextFieldListWidget.Entry> {

  private static final Identifier background = Identifier.ofVanilla("textures/gui/inworld_menu_background.png");
  /**
   * 被选中的多个项的列表，通常包含 {@link #selected} 的对象但不一定。一般通过 {@link Entry#setSelected(boolean)} 来修改。
   */
  protected final @NotNull Set<@NotNull Entry> selectedEntries = new HashSet<>();
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
  private @Nullable Entry startContEntry;

  public TextFieldListWidget(AbstractSignBlockEditScreen<?> signBlockEditScreen,
                             MinecraftClient client, int width, int height, int y, int itemHeight) {
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
   * 类似于 {@link #setFocused(Element)}，但是支持在调用 {@link #setSelected(Entry, boolean, boolean)} 时指定参数。
   */
  public void setFocused(@Nullable Entry focused, boolean multiSel, boolean contSel) {
    Entry entry = this.getFocused();
    if (entry != focused && entry instanceof ParentElement parentElement) {
      parentElement.setFocused(null);
    }

    ((ContainerWidgetAccessor) this).setFocusedElement(focused);
    final List<Entry> children = children();
    int i = children.indexOf(focused);
    if (i >= 0) {
      Entry entry2 = children.get(i);
      this.setSelected(entry2, multiSel, contSel);
    }
  }

  /**
   * 设置当前 TextFieldListScreen 的已选中的文本框。
   *
   * @param entry 需要选中的 {@link Entry}。
   * @implNote 此对象的 {@link #selected} 一般不是 null，而 {@link #focused} 会在此对象（{@link TextFieldListWidget}）失焦时变成 {@code null}。
   * @see AbstractSignBlockEditScreen#setFocused(Element)
   */
  @Override
  public void setSelected(@Nullable TextFieldListWidget.Entry entry) {
    setSelected(entry, Screen.hasControlDown(), Screen.hasShiftDown());
  }

  /**
   * 设置当前 TextFieldListScreen 的已选中的文本框。
   *
   * @param entry    需要选中的 {@link Entry}。
   * @param multiSel 是否多选。如果为 {@code false}，则之前已经选中的其他元素将会未选中。
   * @param contSel  是否连续选。如果为 {@code true}，则将之前选中的和当前选中的均选中。
   * @see AbstractSignBlockEditScreen#setFocused(Element)
   */
  public void setSelected(@Nullable TextFieldListWidget.Entry entry, boolean multiSel, boolean contSel) {
    final Entry prevSelected = getSelectedOrNull();
    super.setSelected(entry);

    if (entry != null) {
      if (!this.client.getNavigationType().isKeyboard()) {
        // 在 isKeyboard() 的情况下， super.setFocused 就已经调用了 ensureVisible，故不再看重复调用。
        this.ensureVisible(getSelectedOrNull());
      }
    }

    if (entry == prevSelected && MinecraftClient.getInstance().getNavigationType().isKeyboard()) {
      // 通常是从其他地方通过键盘焦点返回此处的情形，不执行操作。
      Runnables.doNothing().run();
    } else if (entry instanceof Entry) {
      if (contSel) {
        if (startContEntry == null) {
          startContEntry = prevSelected;
        }
      } else {
        startContEntry = null;
      }
      final int contFrom = contSel ? children().indexOf(startContEntry) : -1;
      if (!multiSel) {
        for (Entry selectedEntry : Set.copyOf(selectedEntries)) {
          selectedEntry.setSelected(false);
        }
      }

      final int contUntil = contSel ? children().indexOf(entry) : -1;
      if (contFrom != -1 && contUntil != -1 && contFrom != contUntil) {
        final int min = Math.min(contFrom, contUntil);
        final int max = Math.max(contFrom, contUntil);

        for (int i = min; i <= max; i++) {
          final Entry entry1 = children().get(i);
          entry1.setSelected(true);
        }
      } else if (multiSel && selectedEntries.contains(entry)) {
        // 在多选模式下，如果再次选中同一个，则失掉这个选择。
        entry.setSelected(false);
        if (getSelectedOrNull() == entry) {
          super.setSelected(null);
        }
      } else {
        entry.setSelected(true);
      }
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
        setFocused(children().get(MathHelper.floorMod(children().indexOf(getSelectedOrNull()) - 1, children().size())));
        return true;
      } else if (keyCode == GLFW.GLFW_KEY_DOWN) {
        setFocused(children().get(MathHelper.floorMod(children().indexOf(getSelectedOrNull()) + 1, children().size())));
        return true;
      }
    } else if (keyCode == GLFW.GLFW_KEY_ENTER || keyCode == GLFW.GLFW_KEY_KP_ENTER) {
      // 此时，children().isEmpty() 为 true
      final Entry newEntry = addEmptyTextField(0);
      TextFieldListWidget.this.setFocused(newEntry, false, false);
      signBlockEditScreen.setFocused(TextFieldListWidget.this);
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
  public int getRowTop(int index) {
    return super.getRowTop(index) - 2;
  }

  @Override
  protected int getScrollbarX() {
    return width - 6;
  }

  @Override
  public void appendClickableNarrations(NarrationMessageBuilder builder) {
    builder.put(NarrationPart.TITLE, TextBridge.translatable("narration.mishanguc.text_field_list"));
    builder.put(NarrationPart.USAGE, TextBridge.translatable("narration.mishanguc.text_field_list.usage"));
    super.appendClickableNarrations(builder);
  }

  @Override
  protected void drawSelectionHighlight(DrawContext context, int y, int entryWidth, int entryHeight, int borderColor, int fillColor) {
    context.fill(1, y - 1, width - 1, y + entryHeight + 4, 0xe0ffffff);
  }

  @Contract(pure = true)
  protected boolean isSimplified() {
    return simplified;
  }

  protected void setSimplified(boolean simplified) {
    this.simplified = simplified;
    this.setHeight(heightForBackground);
    this.setScrollY(getScrollY());
    final Entry selectedOrNull = getSelectedOrNull();
    if (selectedOrNull != null) {
      ensureVisible(selectedOrNull);
    }
  }

  protected void increaseHeight(int amount) {
    cuttingHeight = (Math.clamp(cuttingHeight + amount, 0, heightForBackground));
    this.setHeight(heightForBackground);
    setScrollY(getScrollY()); // 更新滚动以避免滚动溢出
    final Entry selectedOrNull = getSelectedOrNull();
    if (selectedOrNull != null) {
      ensureVisible(selectedOrNull);
    }
  }

  protected void drawHeaderAndFooterSeparators(DrawContext context) {
    super.drawHeaderAndFooterSeparators(context);
    if (simplified) {
      // 简化模式下，多显示一个。
      Identifier identifier2 = this.client.world == null ? Screen.FOOTER_SEPARATOR_TEXTURE : Screen.INWORLD_FOOTER_SEPARATOR_TEXTURE;
      context.drawTexture(RenderPipelines.GUI_TEXTURED, identifier2, this.getX(), this.getY() + heightForBackground, 0.0F, 0.0F, this.getWidth(), 2, 32, 2);
    }
  }


  @Override
  protected void drawMenuListBackground(DrawContext context) {
    Identifier identifier = background;
    context.drawTexture(RenderPipelines.GUI_TEXTURED, identifier, this.getX(), 0, 0, 0, this.getWidth(), this.getY(), 32, 32);
    context.drawTexture(RenderPipelines.GUI_TEXTURED, identifier, this.getX(), getY() + heightForBackground, 0, 0, this.getWidth(), heightForBackground, 32, 32);
  }

  /**
   * 添加一个文本框。此方法执行时，不会设置任何的选择或聚焦。
   *
   * @param index       添加的文本的位置，可以设置为 -1，表示添加到最后一个。
   * @param textContext 需要添加的 {@link TextContext}。
   * @param isExisting  是否为现有的，如果是，则不会将 {@link AbstractSignBlockEditScreen#changed} 设为 <code>true</code>。
   */
  public Entry addTextField(int index, @NotNull TextContext textContext, boolean isExisting) {
    if (!isExisting) {
      signBlockEditScreen.changed = true;
    }
    final Entry newEntry = createEntry(textContext);
    final int newIndex = addEntry(newEntry);
    if (index != -1) {
      final List<Entry> rawChildren = children();
      rawChildren.remove(newIndex);
      rawChildren.add(index, newEntry);
    }
    setScrollY(getScrollY()); // 此处会调用私有方法 recalculateAllChildrenPositions

    signBlockEditScreen.updateContentVisibility();

    return newEntry;
  }

  private @NotNull Entry createEntry(@NotNull TextContext textContext) {
    final TextFieldWidget textFieldWidget = new TextFieldWidget(Objects.requireNonNull(signBlockEditScreen.getTextRenderer(), "textRenderer"), 2, 0, signBlockEditScreen.width - 4, 15, TextBridge.empty());
    textFieldWidget.setMaxLength(Integer.MAX_VALUE);
    if (textContext.extra != null) {
      textFieldWidget.setText(String.format("-%s %s", textContext.extra.getId(), textContext.extra.asStringArgs()));
    } else if (textContext.text != null) {
      if (textContext.text.getContent() instanceof PlainTextContent plainTextContent && textContext.text.getSiblings().isEmpty() && textContext.text.getStyle().isEmpty()) {
        final String text = plainTextContent.string();
        if (Pattern.compile("^-(\\w+?) (.+)$").matcher(text).matches()) {
          textFieldWidget.setText("-literal " + text);
        } else {
          textFieldWidget.setText(text);
        }
      } else {
        textFieldWidget.setText("-nbt " + TextCodecs.CODEC.encodeStart(signBlockEditScreen.registryLookup.getOps(NbtOps.INSTANCE), textContext.text).getOrThrow().toString());
      }
    }
    final Entry newEntry = new Entry(textFieldWidget, textContext);
    textFieldWidget.setChangedListener(s -> {
      final TextContext textContext1 = newEntry.textContext;
      final Matcher matcher = Pattern.compile("^-(\\w+?) (.+)$").matcher(s);
      textFieldWidget.setTooltip(null);
      textFieldWidget.setEditableColor(0xffe0e0e0);
      if (matcher.matches()) {
        final String name = matcher.group(1);
        final String value = matcher.group(2);
        switch (name) {
          case "literal":
            textContext1.text = TextBridge.literal(value);
            break;
          case "json":
            try {
              final JsonElement jsonElement = TextContext.GSON.fromJson(value, JsonElement.class);
              textContext1.text = (MutableText) TextCodecs.CODEC.parse(signBlockEditScreen.registryLookup.getOps(JsonOps.INSTANCE), jsonElement).getOrThrow();
            } catch (JsonParseException | IllegalStateException e) {
              textFieldWidget.setEditableColor(0xffff5555);
              textFieldWidget.setTooltip(Tooltip.of(Text.literal(e.getMessage())));
            }
            break;
          case "nbt":
            try {
              final PackratParser<NbtElement> parser = SnbtParsing.createParser(NbtOps.INSTANCE);
              final StringReader reader = new StringReader(value);
              final NbtElement nbtElement = parser.parse(reader);
              if (reader.canRead()) {
                throw CommandSyntaxException.BUILT_IN_EXCEPTIONS.dispatcherUnknownArgument().createWithContext(reader);
              }
              textContext1.text = (MutableText) TextCodecs.CODEC.parse(signBlockEditScreen.registryLookup.getOps(NbtOps.INSTANCE), nbtElement).getOrThrow();
            } catch (CommandSyntaxException e) {
              textFieldWidget.setEditableColor(0xffff5555);
              textFieldWidget.setTooltip(Tooltip.of(Texts.toText(e.getRawMessage())));
            } catch (IllegalStateException e) {
              textFieldWidget.setEditableColor(0xffff5555);
              textFieldWidget.setTooltip(Tooltip.of(Text.literal(e.getMessage())));
            }
            break;
          default:
            final SpecialDrawable specialDrawable = SpecialDrawable.fromStringArgs(textContext1, name, value);
            if (specialDrawable == null) {
              textContext1.extra = null;
              textContext1.text = TextBridge.literal(s);
            } else if (specialDrawable != SpecialDrawable.INVALID) {
              textContext1.extra = specialDrawable;
              textContext1.text = TextBridge.literal("");
            } else { // 如果为 INVALID 则文本为红色。
              textFieldWidget.setEditableColor(0xffff5555);
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

  public Entry addEmptyTextField(int index) {
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
    final List<Entry> children = children();
    final Entry removedEntry = children.get(index);
    removeEntry(removedEntry);
    // 删除一行元素后，对滚动数量进行一次 clamp，以避免出现过度滚动的情况。
    setScrollY(getScrollY()); // 此处会调用私有方法 recalculateAllChildrenPositions

    signBlockEditScreen.updateContentVisibility();
    signBlockEditScreen.changed = true;
  }

  public void moveUpEntries(Collection<Entry> entries) {
    if (entries.isEmpty()) {
      return;
    }

    // 确保按顺序排序
    final List<Entry> children = children();
    final List<Entry> orderedCopy = children.stream().filter(entries::contains).toList();

    for (Entry entry : orderedCopy) {
      final int i = children.indexOf(entry);
      if (i < 0) {
        Mishanguc.MISHANG_LOGGER.warn("Unexpected entry which is not in children when moving up: {}", entry);
        continue;
      } else if (i == 0) {
        // 顶到了第一元素，不能再移动。
        break;
      }
      final Entry entryAtI = children.get(i);
      children.set(i, children.get(i - 1));
      children.set(i - 1, entryAtI);
    }
  }

  public void moveDownEntries(Collection<Entry> entries) {
    if (entries.isEmpty()) {
      return;
    }

    // 确保按倒序排序
    final List<Entry> children = children();
    final List<Entry> reversedEntries = Lists.reverse(children).stream().filter(entries::contains).toList();

    for (Entry entry : reversedEntries) {
      final int i = children.indexOf(entry);
      if (i < 0) {
        Mishanguc.MISHANG_LOGGER.warn("Unexpected entry which is not in children when moving down: {}", entry);
        continue;
      } else if (i == children.size() - 1) {
        // 顶到了最后元素，不能再移动。
        break;
      }
      final Entry entryAtI = children.get(i);
      children.set(i, children.get(i + 1));
      children.set(i + 1, entryAtI);
    }
  }


  public @UnmodifiableView List<TextContext> getTextContexts() {
    return Lists.transform(children(), input -> input.textContext);
  }

  /**
   * {@link TextFieldListWidget} 中的项。由于 {@link TextFieldWidget} 不是 {@link EntryListWidget.Entry}
   * 的子类，所以对该类进行了包装。
   */
  @Environment(EnvType.CLIENT)
  public class Entry extends AlwaysSelectedEntryListWidget.Entry<Entry> implements Narratable {
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
      if (!(o instanceof Entry entry))
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

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
      switch (keyCode) {
        case GLFW.GLFW_KEY_ENTER -> {
          final List<Entry> children = TextFieldListWidget.this.children();
          final int index = children.indexOf(this);
          if (index + 1 < children.size()) {
            TextFieldListWidget.this.setFocused(children.get(index + 1));
          } else if (!children.isEmpty()) {
            final Entry entry = addEmptyTextField(index + 1);
            TextFieldListWidget.this.setFocused(entry, false, false);
          }
        }
        case GLFW.GLFW_KEY_BACKSPACE -> {
          if (textFieldWidget.getText().isEmpty()) {
            final int index = TextFieldListWidget.this.children().indexOf(this);
            if (index >= 0) {
              TextFieldListWidget.this.removeTextField(index);
              if (!children().isEmpty()) {
                final Entry nearbyEntry = TextFieldListWidget.this.children().get(MathHelper.clamp(index - 1, 0, children().size() - 1));
                TextFieldListWidget.this.setFocused(nearbyEntry, false, false);
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
      if (button == 0 && mouseX >= getScrollbarX() && mouseX < getScrollbarX() + 6) {
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
