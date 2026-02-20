package pers.solid.mishang.uc.screen;

import com.google.common.collect.Lists;
import com.google.common.collect.Streams;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.MultilineText;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.Element;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.ingame.SignEditScreen;
import net.minecraft.client.gui.tooltip.Tooltip;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.ClickableWidget;
import net.minecraft.client.gui.widget.EntryListWidget;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.client.input.KeyCodes;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtList;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.screen.ScreenTexts;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.DyeColor;
import net.minecraft.util.Formatting;
import net.minecraft.util.Util;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.UnmodifiableView;
import org.lwjgl.glfw.GLFW;
import pers.solid.mishang.uc.MishangUtils;
import pers.solid.mishang.uc.Mishanguc;
import pers.solid.mishang.uc.blockentity.BlockEntityWithText;
import pers.solid.mishang.uc.networking.SignEditFinishPayload;
import pers.solid.mishang.uc.text.OutlineColorType;
import pers.solid.mishang.uc.text.TextContext;
import pers.solid.mishang.uc.util.HorizontalAlign;
import pers.solid.mishang.uc.util.TextBridge;
import pers.solid.mishang.uc.util.VerticalAlign;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

/**
 * 编辑告示牌时的屏幕。<br>
 * 放置后如需打开此屏幕，使用
 *
 * <pre>{@code
 * this.client.setScreen(new TextPadEditScreen(entity))
 * }</pre>
 *
 * @param <T> 方块实体的类型。
 * @see SignEditScreen
 * @see ClientPlayerEntity#openEditSignScreen
 * @see ServerPlayerEntity#openEditSignScreen
 */
@Environment(EnvType.CLIENT)
public abstract class AbstractSignBlockEditScreen<T extends BlockEntityWithText> extends Screen {
  // 由于需要多次使用，故作为字段存储。
  private static final MutableText BUTTON_CLEAR_MESSAGE =
      TextBridge.translatable("message.mishanguc.clear");
  private static final MutableText BUTTON_CLEAR_CONFIRM_MESSAGE =
      TextBridge.translatable("message.mishanguc.clear.confirm");
  private static final MutableText BUTTON_CLEAR_CONFIRM_DESCRIPTION_MESSAGE =
      TextBridge.translatable("message.mishanguc.clear.confirm.description");
  private static final MutableText BUTTON_CLEAR_DESCRIPTION_MESSAGE =
      TextBridge.translatable("message.mishanguc.clear.description");
  public final BlockPos blockPos;
  public boolean hidden = false;
  private static final ButtonWidget.PressAction EMPTY_PRESS_ACTION = button -> {
  };

  final RegistryWrapper.WrapperLookup registryLookup;
  public final T entity;

  /**
   * 是否发生了改变。如果改变了，则提交时发送完整内容，否则发送空 NBT 表示未做更改。
   */
  public boolean changed = false;

  public @NotNull TextFieldListWidget textFieldListWidget = new TextFieldListWidget(this, MinecraftClient.getInstance(), width, height - 90, 25, 16);

  /**
   * 最初创建屏幕时的 textContext 的列表，可以不改变，将在调用 init 时转化为实际的屏幕元素，同时将此字段设置为 null。在使用时会被复制一次。
   */
  public List<TextContext> initialTexts;
  /*
  ===== 上方第一行 =====
   */

  /**
   * 上方第一行：添加文本按钮
   */
  public final ButtonWidget addTextButton = new ButtonWidget.Builder(TextBridge.translatable("message.mishanguc.add_text"), button1 -> {
    if (textFieldListWidget.selectedEntries.isEmpty()) {
      final TextFieldListWidget.Entry newEntry = textFieldListWidget.addEmptyTextField(-1);
      textFieldListWidget.setFocused(newEntry, false, false);
    } else {
      final List<TextFieldListWidget.Entry> selectedCopy = Lists.reverse(textFieldListWidget.children()).stream().filter(textFieldListWidget.selectedEntries::contains).toList();
      final TextFieldListWidget.Entry previouslySelected = textFieldListWidget.getSelectedOrNull();
      for (TextFieldListWidget.Entry selectedEntry : selectedCopy) {
        selectedEntry.setSelected(false);
      }
      textFieldListWidget.setFocused(null, false, false);
      for (TextFieldListWidget.Entry entry : selectedCopy) {
        final int i = textFieldListWidget.children().indexOf(entry);
        if (i < 0) {
          Mishanguc.MISHANG_LOGGER.warn("Unexpected entry which is not in children when adding text: {}", entry);
          continue;
        }
        final TextFieldListWidget.Entry newEntry = textFieldListWidget.addEmptyTextField(i + 1);
        if (entry == previouslySelected) {
          textFieldListWidget.setFocused(newEntry, true, false);
        } else {
          newEntry.setSelected(true);
        }
      }
    }
    setFocused(textFieldListWidget);
  }).position(width / 2 - 120 - 100, 5).size(80, 20).tooltip(Tooltip.of(TextBridge.translatable("message.mishanguc.add_text.description").append(ScreenTexts.LINE_BREAK).append(MishangUtils.describeShortcut(TextBridge.literal("Ctrl + Shift + ").append(TextBridge.translatable("message.mishanguc.keyboard_shortcut.equal")))))).build();

  /**
   * 上方第一行：移除文本按钮
   */
  public final ButtonWidget removeTextButton = new ButtonWidget.Builder(TextBridge.translatable("message.mishanguc.remove_text"), button -> {
    if (textFieldListWidget.selectedEntries.isEmpty()) {
      return;
    }

    final List<TextFieldListWidget.Entry> selectedCopy = Lists.reverse(textFieldListWidget.children()).stream().filter(textFieldListWidget.selectedEntries::contains).toList();
    final TextFieldListWidget.Entry previouslySelected = textFieldListWidget.getSelectedOrNull();
    for (TextFieldListWidget.Entry selectedEntry : textFieldListWidget.selectedEntries) {
      selectedEntry.setFocused(false);
    }
    textFieldListWidget.selectedEntries.clear();

    for (TextFieldListWidget.Entry entry : selectedCopy) {
      final int index = textFieldListWidget.children().indexOf(entry);
      if (index >= 0) {
        textFieldListWidget.removeTextField(index);
        if (!textFieldListWidget.children().isEmpty()) {
          final TextFieldListWidget.Entry nearbyEntry = textFieldListWidget.children().get(MathHelper.clamp(index - 1, 0, children().size() - 1));
          if (entry == previouslySelected) {
            textFieldListWidget.setFocused(nearbyEntry, true, false);
          }
          nearbyEntry.setSelected(true);
        }
      }
    }
  }).dimensions(width / 2 + 120 - 100, 5, 80, 20).tooltip(Tooltip.of(TextBridge.translatable("message.mishanguc.remove_text.description").append(ScreenTexts.LINE_BREAK).append(MishangUtils.describeShortcut(TextBridge.literal("Ctrl + Shift + ").append(TextBridge.translatable("message.mishanguc.keyboard_shortcut.minus")))))).build();


  /**
   * 上方第一行：上移按钮。
   */
  public final ButtonWidget moveUpButton = new ButtonWidget.Builder(TextBridge.translatable("message.mishanguc.moveUp"), button -> textFieldListWidget.moveUpEntries(textFieldListWidget.selectedEntries)).dimensions(this.width - 20, 5, 80, 20).tooltip(Tooltip.of(TextBridge.translatable("message.mishanguc.moveUp.description").append(ScreenTexts.LINE_BREAK).append(MishangUtils.describeShortcut(TextBridge.literal("Ctrl + Shift + ").append(TextBridge.translatable("key.keyboard.up")))))).build();

  /**
   * 上方第一行：下移按钮。
   */
  public final ButtonWidget moveDownButton = new ButtonWidget.Builder(TextBridge.translatable("message.mishanguc.moveDown"), button -> textFieldListWidget.moveDownEntries(textFieldListWidget.selectedEntries)).dimensions(this.width - 20, 5, 80, 20).tooltip(Tooltip.of(TextBridge.translatable("message.mishanguc.moveDown.description").append(ScreenTexts.LINE_BREAK).append(MishangUtils.describeShortcut(TextBridge.literal("Ctrl + Shift + ").append(TextBridge.translatable("key.keyboard.down")))))).build();

  /**
   * 上方第一行：清除按钮。
   */
  public final ButtonWidget clearButton = new ButtonWidget.Builder(BUTTON_CLEAR_MESSAGE, button -> {
    if (button.getMessage() == BUTTON_CLEAR_CONFIRM_MESSAGE) {
      textFieldListWidget.clearTextFields();
      button.setMessage(BUTTON_CLEAR_MESSAGE);
      button.setTooltip(Tooltip.of(BUTTON_CLEAR_DESCRIPTION_MESSAGE));
    } else {
      // 要求用户再次点击一次按钮才能删除。
      button.setMessage(BUTTON_CLEAR_CONFIRM_MESSAGE);
      button.setTooltip(Tooltip.of(BUTTON_CLEAR_CONFIRM_DESCRIPTION_MESSAGE));
    }
  }).dimensions(this.width / 2 + 190, this.height - 50, 80, 20).tooltip(Tooltip.of(BUTTON_CLEAR_CONFIRM_DESCRIPTION_MESSAGE)).build();



  /*
   ===== 文本区域列表部分 =====
   */

  /**
   * 没有添加文本时，显示的一条“点击此处添加文本”的消息。文本添加后，该按钮将消失。
   */
  public final ButtonWidget placeHolder = new ButtonWidget.Builder(TextBridge.translatable("message.mishanguc.add_first_text"), button -> {
    final TextFieldListWidget.Entry newEntry = textFieldListWidget.addEmptyTextField(0);
    textFieldListWidget.setFocused(newEntry, false, false);
    setFocused(textFieldListWidget);
  }).dimensions(0, 35, 200, 20).build();

  public final SignPresetGridWidget signPresets = SignPresetGridWidget.createAllWidgets(this, MinecraftClient.getInstance(), height - 140, 72);


  /*
  ===== 下方第一行 =====
   */

  /**
   * 下方第一行：加粗按钮。
   */
  public final BooleanButtonWidget boldButton = new BooleanButtonWidget(this.width / 2 - 200, this.height - 50, 20, 20, TextBridge.translatable("message.mishanguc.bold"), button -> textFieldListWidget.getSelectedOrNull() == null ? null : textFieldListWidget.getSelectedOrNull().textContext.bold, b -> {
    changed = true;
    for (TextFieldListWidget.@NotNull Entry entry : textFieldListWidget.selectedEntries) {
      entry.textContext.bold = b;
    }
  }, EMPTY_PRESS_ACTION)
      .setRenderedName(TextBridge.literal("B").formatted(Formatting.BOLD));

  /**
   * 下方第一行：斜体按钮。
   */
  public final BooleanButtonWidget italicButton = new BooleanButtonWidget(this.width / 2 - 180, this.height - 50, 20, 20, TextBridge.translatable("message.mishanguc.italic"), button -> textFieldListWidget.getSelectedOrNull() == null ? null : textFieldListWidget.getSelectedOrNull().textContext.italic, b -> {
    changed = true;
    for (TextFieldListWidget.@NotNull Entry entry : textFieldListWidget.selectedEntries) {
      entry.textContext.italic = b;
    }
  }, EMPTY_PRESS_ACTION)
      .setRenderedName(TextBridge.literal("I").formatted(Formatting.ITALIC))
      .setKeyboardShortcut(TextBridge.literal("Ctrl + I"));

  /**
   * 下方第一行：下划线按钮。
   */
  public final BooleanButtonWidget underlineButton = new BooleanButtonWidget(this.width / 2 - 160, this.height - 50, 20, 20, TextBridge.translatable("message.mishanguc.underline"), button -> textFieldListWidget.getSelectedOrNull() == null ? null : textFieldListWidget.getSelectedOrNull().textContext.underline, b -> {
    changed = true;
    for (TextFieldListWidget.@NotNull Entry entry : textFieldListWidget.selectedEntries) {
      entry.textContext.underline = b;
    }
  }, EMPTY_PRESS_ACTION)
      .setRenderedName(TextBridge.literal("U").formatted(Formatting.UNDERLINE))
      .setKeyboardShortcut(TextBridge.literal("Ctrl + U"));

  /**
   * 下方第一行：删除线按钮。
   */
  public final BooleanButtonWidget strikethroughButton = new BooleanButtonWidget(this.width / 2 - 140, this.height - 50, 20, 20, TextBridge.translatable("message.mishanguc.strikethrough"), button -> textFieldListWidget.getSelectedOrNull() == null ? null : textFieldListWidget.getSelectedOrNull().textContext.strikethrough, b -> {
    changed = true;
    for (TextFieldListWidget.@NotNull Entry entry : textFieldListWidget.selectedEntries) {
      entry.textContext.strikethrough = b;
    }
  }, EMPTY_PRESS_ACTION)
      .setRenderedName(TextBridge.literal("S").formatted(Formatting.STRIKETHROUGH))
      .setKeyboardShortcut(TextBridge.literal("Ctrl + S"));

  /**
   * 下方第一行：随机文字（obfuscated）按钮。
   */
  public final BooleanButtonWidget obfuscatedButton = new BooleanButtonWidget(this.width / 2 - 120, this.height - 50, 20, 20, TextBridge.translatable("message.mishanguc.obfuscated"), button -> textFieldListWidget.getSelectedOrNull() == null ? null : textFieldListWidget.getSelectedOrNull().textContext.obfuscated, b -> {
    changed = true;
    for (TextFieldListWidget.@NotNull Entry entry : textFieldListWidget.selectedEntries) {
      entry.textContext.obfuscated = b;
    }
  }, EMPTY_PRESS_ACTION)
      .setRenderedName(TextBridge.literal("O").formatted(Formatting.OBFUSCATED))
      .setKeyboardShortcut(TextBridge.literal("Ctrl + O"));


  /**
   * 下方第一行：阴影按钮。
   */
  public final BooleanButtonWidget shadeButton = new BooleanButtonWidget(this.width / 2 - 100, this.height - 50, 35, 20, TextBridge.translatable("message.mishanguc.shade"), button -> textFieldListWidget.getSelectedOrNull() == null ? null : textFieldListWidget.getSelectedOrNull().textContext.shadow, b -> {
    changed = true;
    for (TextFieldListWidget.@NotNull Entry entry : textFieldListWidget.selectedEntries) {
      entry.textContext.shadow = b;
    }
  }, EMPTY_PRESS_ACTION);

  /**
   * 下方第一行：文本大小按钮。
   */
  public final FloatButtonWidget sizeButton = new FloatButtonWidget(this.width / 2 - 60, this.height - 50, 35, 20, TextBridge.translatable("message.mishanguc.size"), buttons -> textFieldListWidget.getSelectedOrNull() == null ? null : textFieldListWidget.getSelectedOrNull().textContext.size, (valueFunction, original) -> {
    changed = true;
    for (TextFieldListWidget.@NotNull Entry entry : textFieldListWidget.selectedEntries) {
      entry.textContext.size = valueFunction.get(entry.textContext.size);
    }
  }, EMPTY_PRESS_ACTION);

  /**
   * 下方第一行：X偏移。
   */
  public final FloatButtonWidget offsetXButton = new FloatButtonWidget(this.width / 2 - 10, this.height - 50, 40, 20, TextBridge.translatable("message.mishanguc.offsetX"), button -> textFieldListWidget.getSelectedOrNull() == null ? null : textFieldListWidget.getSelectedOrNull().textContext.offsetX, (valueFunction, original) -> {
    changed = true;
    for (TextFieldListWidget.@NotNull Entry entry : textFieldListWidget.selectedEntries) {
      entry.textContext.offsetX = valueFunction.get(entry.textContext.offsetX);
    }
  }, EMPTY_PRESS_ACTION);

  /**
   * 下方第一行：Y偏移。
   */
  public final FloatButtonWidget offsetYButton = new FloatButtonWidget(this.width / 2 + 40, this.height - 50, 40, 20, TextBridge.translatable("message.mishanguc.offsetY"), button -> textFieldListWidget.getSelectedOrNull() == null ? null : textFieldListWidget.getSelectedOrNull().textContext.offsetY, (valueFunction, original) -> {
    changed = true;
    for (TextFieldListWidget.@NotNull Entry entry : textFieldListWidget.selectedEntries) {
      entry.textContext.offsetY = valueFunction.get((entry.textContext.offsetY));
    }
  }, EMPTY_PRESS_ACTION);

  /**
   * 下方第一行：Z偏移。
   */
  public final FloatButtonWidget offsetZButton = new FloatButtonWidget(this.width / 2 + 40, this.height - 50, 40, 20, TextBridge.translatable("message.mishanguc.offsetZ"), button -> textFieldListWidget.getSelectedOrNull() == null ? null : textFieldListWidget.getSelectedOrNull().textContext.offsetZ, (valueFunction, original) -> {
    changed = true;
    for (TextFieldListWidget.@NotNull Entry entry : textFieldListWidget.selectedEntries) {
      entry.textContext.offsetZ = valueFunction.get(entry.textContext.offsetZ);
    }
  }, EMPTY_PRESS_ACTION);

  /**
   * 下方第一行：颜色。
   */
  public final FloatButtonWidget colorButton = new FloatButtonWidget(0, 0, 50, 20, TextBridge.translatable("message.mishanguc.color"), button -> {
    changed = true;
    if (textFieldListWidget.getSelectedOrNull() == null) {
      return null;
    }
    final DyeColor dyeColor = MishangUtils.colorBySignColor(textFieldListWidget.getSelectedOrNull().textContext.color);
    if (dyeColor == null) {
      return -2f;
    } else {
      return (float) dyeColor.getIndex();
    }
  }, (valueFunction, original) -> {
    final int color = DyeColor.byIndex((int) valueFunction.get(original.floatValue())).getSignColor();
    for (TextFieldListWidget.@NotNull Entry entry : textFieldListWidget.selectedEntries) {
      entry.textContext.color = color;
    }
  }, EMPTY_PRESS_ACTION).nameValueAs(colorId -> {
    if (colorId == -2 && textFieldListWidget.getSelectedOrNull() != null) {
      return MishangUtils.describeColor(textFieldListWidget.getSelectedOrNull().textContext.color);
    } else {
      final DyeColor dyeColor = DyeColor.byIndex((int) colorId);
      return MishangUtils.describeColor(dyeColor.getSignColor(), TextBridge.translatable("color.minecraft." + dyeColor.asString()));
    }
  }).setRenderedNameSupplier((value, valueText) -> valueText);

  /**
   * 下方第一行：描边颜色。
   */
  @ApiStatus.AvailableSince("0.1.6-mc1.17")
  public final FloatButtonWidget outlineColorButton = new FloatButtonWidget(0, 0, 70, 20, TextBridge.translatable("message.mishanguc.outline_color"), button -> {
    if (textFieldListWidget.getSelectedOrNull() == null) {
      return null;
    }
    if (textFieldListWidget.getSelectedOrNull().textContext.outlineColorType == OutlineColorType.AUTO) {
      return -1f;
    } else if (textFieldListWidget.getSelectedOrNull().textContext.outlineColorType == OutlineColorType.NONE) {
      return -2f;
    }
    final DyeColor colorOutline = MishangUtils.COLOR_TO_OUTLINE_COLOR.inverse().get(textFieldListWidget.getSelectedOrNull().textContext.outlineColor);
    if (colorOutline != null) {
      return colorOutline.getIndex() + 16f;
    }
    final DyeColor color = MishangUtils.colorBySignColor(textFieldListWidget.getSelectedOrNull().textContext.outlineColor);
    if (color != null) {
      return (float) color.getIndex();
    } else {
      return -3f;
    }
  }, (valueFunction, original) -> {
    changed = true;
    final int colorIndex = (int) valueFunction.get(original.floatValue());
    final int outlineColor;
    final OutlineColorType outlineColorType;
    if (colorIndex == -1) {
      outlineColor = colorIndex;
      outlineColorType = OutlineColorType.AUTO;
    } else if (colorIndex == -2) {
      outlineColor = colorIndex;
      outlineColorType = OutlineColorType.NONE;
    } else if (colorIndex > 15) {
      outlineColor = MishangUtils.COLOR_TO_OUTLINE_COLOR.get(DyeColor.byIndex(colorIndex - 16));
      outlineColorType = OutlineColorType.CUSTOM;
    } else {
      outlineColor = DyeColor.byIndex(colorIndex).getSignColor();
      outlineColorType = OutlineColorType.CUSTOM;
    }
    for (TextFieldListWidget.@NotNull Entry entry : textFieldListWidget.selectedEntries) {
      entry.textContext.outlineColor = outlineColor;
      entry.textContext.outlineColorType = outlineColorType;
    }
  }, EMPTY_PRESS_ACTION).nameValueAs(colorIndex -> {
    // colorIndex=-1：表示当前自动根据文本内容绘制描边。
    // colorIndex=-2：表示当前不绘制描边（默认）。
    // colorIndex=-3：表示是自定义的。
    // colorIndex=null：表示当前没有选中文本。
    // colorIndex=0-15：标准颜色。
    // colorIndex=16-31：描边颜色
    if (colorIndex == -1) {
      return TextBridge.translatable("message.mishanguc.outline_color.auto");
    } else if (colorIndex == -2) {
      return TextBridge.translatable("message.mishanguc.outline_color.none");
    } else if (colorIndex == -3 && textFieldListWidget.getSelectedOrNull() != null) {
      return MishangUtils.describeColor(textFieldListWidget.getSelectedOrNull().textContext.outlineColor);
    } else if (colorIndex > 15 && textFieldListWidget.getSelectedOrNull() != null) {
      final DyeColor color = DyeColor.byIndex((int) colorIndex - 16);
      return TextBridge.translatable("message.mishanguc.outline_color.relate", MishangUtils.describeColor(textFieldListWidget.getSelectedOrNull().textContext.outlineColor, TextBridge.translatable("message.mishanguc.outline_color.relate.$1")), MishangUtils.describeColor(color.getSignColor(), TextBridge.translatable("color.minecraft." + color.asString())));
    } else {
      final DyeColor color = DyeColor.byIndex((int) colorIndex);
      if (color == null)
        return TextBridge.translatable("message.mishanguc.outline_color.none");
      return MishangUtils.describeColor(color.getSignColor(), TextBridge.translatable("color.minecraft." + color.asString()));
    }
  }).setRenderedNameSupplier((value, valueText) -> {
    if (value == null) {
      return null;
    } else if (value == -1) {
      return TextBridge.translatable("message.mishanguc.outline_color.composed.auto");
    } else if (value == -2) {
      return TextBridge.translatable("message.mishanguc.outline_color.composed.none");
    } else if (textFieldListWidget.getSelectedOrNull() != null) {
      return TextBridge.translatable("message.mishanguc.outline_color.composed", MishangUtils.describeColor(textFieldListWidget.getSelectedOrNull().textContext.outlineColor));
    } else {
      return null;
    }
  });


  /*
  ==== 下方第二行 ====
   */

  /**
   * 下方第二行：X旋转。
   */
  @ApiStatus.AvailableSince("0.1.6")
  public final FloatButtonWidget rotationXButton = new FloatButtonWidget(this.width / 2 + 40, this.height - 50, 40, 20, TextBridge.translatable("message.mishanguc.rotationX"), button -> textFieldListWidget.getSelectedOrNull() == null ? null : textFieldListWidget.getSelectedOrNull().textContext.rotationX, (valueFunction, original) -> {
    changed = true;
    for (TextFieldListWidget.@NotNull Entry entry : textFieldListWidget.selectedEntries) {
      entry.textContext.rotationX = valueFunction.apply(entry.textContext.rotationX);
    }
  }, EMPTY_PRESS_ACTION);

  /**
   * 下方第二行：Y旋转。
   */
  @ApiStatus.AvailableSince("0.1.6")
  public final FloatButtonWidget rotationYButton = new FloatButtonWidget(this.width / 2 + 40, this.height - 50, 40, 20, TextBridge.translatable("message.mishanguc.rotationY"), button -> textFieldListWidget.getSelectedOrNull() == null ? null : textFieldListWidget.getSelectedOrNull().textContext.rotationY, (valueFunction, original) -> {
    changed = true;
    for (TextFieldListWidget.@NotNull Entry entry : textFieldListWidget.selectedEntries) {
      entry.textContext.rotationY = valueFunction.apply(entry.textContext.rotationY);
    }
  }, EMPTY_PRESS_ACTION);

  /**
   * 下方第二行：Z旋转。
   */
  @ApiStatus.AvailableSince("0.1.6")
  public final FloatButtonWidget rotationZButton = new FloatButtonWidget(this.width / 2 + 40, this.height - 50, 40, 20, TextBridge.translatable("message.mishanguc.rotationZ"), button -> textFieldListWidget.getSelectedOrNull() == null ? null : textFieldListWidget.getSelectedOrNull().textContext.rotationZ, (valueFunction, original) -> {
    changed = true;
    for (TextFieldListWidget.Entry entry : textFieldListWidget.selectedEntries) {
      entry.textContext.rotationZ = valueFunction.apply(entry.textContext.rotationZ);
    }
  }, EMPTY_PRESS_ACTION);

  /**
   * 下方第二行：X缩放。
   */
  public final FloatButtonWidget scaleXButton = new FloatButtonWidget(this.width / 2 + 90, this.height - 50, 40, 20, TextBridge.translatable("message.mishanguc.scaleX"), button -> textFieldListWidget.getSelectedOrNull() == null ? null : textFieldListWidget.getSelectedOrNull().textContext.scaleX, (valueFunction, original) -> {
    changed = true;
    for (TextFieldListWidget.Entry entry : textFieldListWidget.selectedEntries) {
      entry.textContext.scaleX = valueFunction.apply(entry.textContext.scaleX);
    }
  }, EMPTY_PRESS_ACTION);

  /**
   * 下方第二行：Y缩放。
   */
  public final FloatButtonWidget scaleYButton = new FloatButtonWidget(this.width / 2 + 140, this.height - 50, 40, 20, TextBridge.translatable("message.mishanguc.scaleY"), button -> textFieldListWidget.getSelectedOrNull() == null ? null : textFieldListWidget.getSelectedOrNull().textContext.scaleY, (valueFunction, original) -> {
    changed = true;
    for (TextFieldListWidget.Entry entry : textFieldListWidget.selectedEntries) {
      entry.textContext.scaleY = valueFunction.apply(entry.textContext.scaleY);
    }
  }, EMPTY_PRESS_ACTION);

  /**
   * 下方第二行：水平对齐方式。
   */
  public final FloatButtonWidget horizontalAlignButton = new FloatButtonWidget(0, 0, 50, 20, TextBridge.translatable("message.mishanguc.horizontal_align"), b -> textFieldListWidget.getSelectedOrNull() == null ? null : (float) textFieldListWidget.getSelectedOrNull().textContext.horizontalAlign.ordinal(), (valueFunction, original) -> {
    for (TextFieldListWidget.Entry entry : textFieldListWidget.selectedEntries) {
      entry.textContext.horizontalAlign = HorizontalAlign.values()[(int) valueFunction.get(original.floatValue())];
    }
  }, b -> {
  }).nameValueAs(f -> HorizontalAlign.values()[(int) f].getName()).setRenderedNameSupplier((value, valueText) -> valueText);

  /**
   * 下方第二行：垂直对齐方式。
   */
  public final FloatButtonWidget verticalAlignButton = new FloatButtonWidget(0, 0, 50, 20, TextBridge.translatable("message.mishanguc.vertical_align"), b -> textFieldListWidget.getSelectedOrNull() == null ? null : (float) textFieldListWidget.getSelectedOrNull().textContext.verticalAlign.ordinal(), (valueFunction, original) -> {
    for (TextFieldListWidget.Entry entry : textFieldListWidget.selectedEntries) {
      entry.textContext.verticalAlign = VerticalAlign.values()[(int) valueFunction.get(original.floatValue())];
    }
  }, b -> {
  }).nameValueAs(f -> VerticalAlign.values()[(int) f].getName()).setRenderedNameSupplier((value, valueText) -> valueText);

  /**
   * 下方第二行：切换文字是否可以看穿。
   */
  public final BooleanButtonWidget seeThroughButton = new BooleanButtonWidget(0, 0, 60, 20, TextBridge.translatable("message.mishanguc.see_through"), button -> textFieldListWidget.getSelectedOrNull() == null ? null : textFieldListWidget.getSelectedOrNull().textContext.seeThrough, b -> {
    changed = true;
    for (TextFieldListWidget.Entry entry : textFieldListWidget.selectedEntries) {
      entry.textContext.seeThrough = b;
    }
  }, EMPTY_PRESS_ACTION);

  /**
   * 下方第二行：绝对模式。
   */
  public final BooleanButtonWidget absoluteButton = new BooleanButtonWidget(0, 0, 50, 20, TextBridge.translatable("message.mishanguc.absolute"), button -> textFieldListWidget.getSelectedOrNull() == null ? null : textFieldListWidget.getSelectedOrNull().textContext.absolute, b -> {
    changed = true;
    for (TextFieldListWidget.Entry entry : textFieldListWidget.selectedEntries) {
      entry.textContext.absolute = b;
    }
  }, EMPTY_PRESS_ACTION)
      .setTooltip(TextBridge.translatable("message.mishanguc.absolute.description"));

  {
    colorButton.min = 0;
    colorButton.max = DyeColor.values().length - 1;
    outlineColorButton.defaultValue = -2;
    outlineColorButton.min = -2;
    outlineColorButton.max = 2 * DyeColor.values().length - 1;
    horizontalAlignButton.min = 0;
    horizontalAlignButton.max = 2;
    verticalAlignButton.min = 0;
    verticalAlignButton.max = 2;
    rotationXButton.step = 15;
    rotationYButton.step = 15;
    rotationZButton.step = 15;
    offsetXButton.step = 0.5f;
    offsetYButton.step = -0.5f;
    offsetYButton.rightArrowStepMultiplier = -1f;
    offsetYButton.scrollMultiplier = 1f;
    scaleXButton.step = 0.125f;
    scaleXButton.defaultValue = 1;
    scaleYButton.step = 0.125f;
    scaleYButton.defaultValue = 1;
    sizeButton.min = 0;
    sizeButton.step = 0.5f;
    sizeButton.scrollMultiplier = 1;
    scaleXButton.scrollMultiplier = 1;
    scaleYButton.scrollMultiplier = 1;
    horizontalAlignButton.defaultValue = 1;
    verticalAlignButton.defaultValue = 1;
    verticalAlignButton.scrollMultiplier = -1;
    verticalAlignButton.upArrowStepMultiplier = -1;
  }


  /*
  ===== 下方第三行 =====
   */

  private boolean isSelectingButtonToSetCustom = false;
  private boolean isAcceptingCustomValue = false;
  /**
   * 下方第三行：设置自定义值。
   */
  @ApiStatus.AvailableSince("1.2.3")
  public final ButtonWidget setCustomValueButton = new ButtonWidget.Builder(TextBridge.translatable("message.mishanguc.set_custom_value"), button -> {
    isSelectingButtonToSetCustom = !isSelectingButtonToSetCustom;
    clearAndInit();
  }).dimensions(this.width / 2, this.height - 50, 80, 20).tooltip(Tooltip.of(TextBridge.translatable("message.mishanguc.set_custom_value.description").append(ScreenTexts.LINE_BREAK).append(TextBridge.translatable("message.mishanguc.set_custom_value.description.keyboard")).append(ScreenTexts.LINE_BREAK).append(MishangUtils.describeShortcut(TextBridge.literal("Ctrl + E"))))).build();

  /**
   * 自定义文本编辑框，仅在编辑自定义值时显示。
   */
  public final TextFieldWidget customValueTextField = new TextFieldWidget(MinecraftClient.getInstance().textRenderer, 5, height - 40, width - 112, 20, TextBridge.translatable("message.mishanguc.custom_value"));

  // 对于 outlineColor，此时可能为 -0.125f 和 -0.25f。
  private Float customValueBeforeChange;
  private FloatButtonWidget customValueFor;
  public final ButtonWidget customValueConfirmButton = ButtonWidget.builder(ScreenTexts.OK, button -> {
    customValueStopAccepting();
    changed = true;
  }).dimensions(width - 105, height - 40, 50, 20).build();
  public final ButtonWidget customValueCancelButton = ButtonWidget.builder(ScreenTexts.CANCEL, button -> {
    if (customValueFor != null) {
      if (customValueFor == colorButton) {
        for (TextFieldListWidget.Entry entry : textFieldListWidget.selectedEntries) {
          entry.textContext.color = customValueBeforeChange.intValue();
        }
      } else if (customValueFor == outlineColorButton) {
        for (TextFieldListWidget.Entry entry : textFieldListWidget.selectedEntries) {
          if (customValueBeforeChange == -0.25f) {
            entry.textContext.outlineColorType = OutlineColorType.NONE;
            entry.textContext.outlineColor = -2;
          } else if (customValueBeforeChange == -0.125f) {
            entry.textContext.outlineColorType = OutlineColorType.AUTO;
            entry.textContext.outlineColor = -1;
          } else {
            entry.textContext.outlineColorType = OutlineColorType.CUSTOM;
            entry.textContext.outlineColor = customValueBeforeChange.intValue();
          }
        }
      } else {
        customValueFor.setAllSameValue(customValueBeforeChange == null ? customValueFor.defaultValue : customValueBeforeChange);
      }
    }
    customValueStopAccepting();
  }).dimensions(width - 55, height - 40, 50, 20).build();

  /**
   * 下方第三行：翻转排版当前文本按钮。
   */
  @ApiStatus.AvailableSince("0.1.7")
  public final ButtonWidget flipButton = new ButtonWidget.Builder(TextBridge.translatable("message.mishanguc.flip"), button -> {
    if (hasControlDown()) {
      for (TextFieldListWidget.Entry entry : textFieldListWidget.children()) {
        entry.textContext.flip();
      }
    } else {
      for (TextFieldListWidget.Entry entry : textFieldListWidget.selectedEntries) {
        entry.textContext.flip();
      }
    }
  }).dimensions(this.width / 2, this.height - 50, 40, 20).tooltip(Tooltip.of(TextBridge.translatable("message.mishanguc.flip.description"))).build();

  /**
   * 下方第三行：完成编辑按钮。
   */
  public final ButtonWidget finishButton = new ButtonWidget.Builder(ScreenTexts.DONE, buttonWidget -> this.finishEditing()).dimensions(this.width / 2 - 100, this.height - 30, 170, 20).tooltip(Tooltip.of(TextBridge.translatable("message.mishanguc.finish.description"))).build();

  /**
   * 下方第三行：取消编辑按钮。
   */
  public final ButtonWidget cancelButton = new ButtonWidget.Builder(ScreenTexts.CANCEL, button -> this.cancelEditing()).dimensions(this.width / 2, height - 30, 40, 20).tooltip(Tooltip.of(TextBridge.translatable("message.mishanguc.cancel.description"))).build();

  /**
   * 下方第三行：重排按钮。
   */
  public final ButtonWidget rearrangeButton = new ButtonWidget.Builder(TextBridge.translatable("message.mishanguc.rearrange"), button -> rearrange()).dimensions(this.width / 2, this.height - 50, 40, 20).tooltip(Tooltip.of(TextBridge.translatable("message.mishanguc.rearrange.tooltip"))).build();

  /**
   * 下方第三行：隐藏界面
   */
  public final BooleanButtonWidget hideButton = new BooleanButtonWidget(0, height - 25, 40, 20, TextBridge.translatable("message.mishanguc.hide_gui"), booleanButtonWidget -> hidden || textFieldListWidget.isSimplified(), value -> {
    if (textFieldListWidget.isSimplified()) {
      textFieldListWidget.setSimplified(false);
    } else if (!hidden && hasShiftDown()) {
      textFieldListWidget.setSimplified(true);
    } else {
      hidden = value;
    }
  }, EMPTY_PRESS_ACTION)
      .setSummaryTextSupplier(() -> (hasShiftDown() && !hidden || textFieldListWidget.isSimplified()) ? Text.translatable("message.mishanguc.simplify") : TextBridge.translatable("message.mishanguc.hide_gui"))
      .setRenderedNameSupplier(value -> {
        if (hidden) {
          // 在隐藏模式下，显示“显示”按钮
          return TextBridge.translatable("message.mishanguc.hide_gui.show");
        } else if (hasShiftDown() || textFieldListWidget.isSimplified()) {
          return textFieldListWidget.isSimplified() ? TextBridge.translatable("message.mishanguc.simplify.disable") : TextBridge.translatable("message.mishanguc.simplify.enable");
        } else {
          return TextBridge.translatable("message.mishanguc.hide_gui.hide");
        }
      })
      .setTooltipSupplier(value -> {
        if (!hidden && hasShiftDown() || textFieldListWidget.isSimplified()) {
          return Text.empty()
              .append(Text.translatable("message.mishanguc.simplify.height", textFieldListWidget.cuttingHeight).withColor(0xffd0d0d0))
              .append(ScreenTexts.LINE_BREAK)
              .append(TextBridge.translatable("message.mishanguc.simplify.tooltip").formatted(Formatting.GRAY));
        } else {
          return TextBridge.translatable("message.mishanguc.hide_gui.tooltip").formatted(Formatting.GRAY);
        }
      });

  public final ClickableWidget[] toolboxTop = new ClickableWidget[]{addTextButton, removeTextButton, moveUpButton, moveDownButton, clearButton};
  public final ClickableWidget[] toolbox1 = new ClickableWidget[]{boldButton, italicButton, underlineButton, strikethroughButton, obfuscatedButton, shadeButton, sizeButton, offsetXButton, offsetYButton, offsetZButton, colorButton, outlineColorButton};
  public final ClickableWidget[] toolbox2 = new ClickableWidget[]{rotationXButton, rotationYButton, rotationZButton, scaleXButton, scaleYButton, horizontalAlignButton, verticalAlignButton, seeThroughButton, absoluteButton};
  public final ClickableWidget[] toolbox3 = new ClickableWidget[]{setCustomValueButton, flipButton, finishButton, cancelButton, rearrangeButton, hideButton};


  public AbstractSignBlockEditScreen(RegistryWrapper.WrapperLookup registryLookup, T entity, BlockPos blockPos, @UnmodifiableView List<TextContext> initialTexts) {
    super(TextBridge.translatable("message.mishanguc.sign_edit"));
    this.registryLookup = registryLookup;
    this.entity = entity;
    this.blockPos = blockPos;
    entity.setEditor(this.client != null ? this.client.player : null);
    sizeButton.defaultValue = entity.createDefaultTextContext().size;
    this.initialTexts = initialTexts;
  }

  @Override
  public List<? extends Element> children() {
    if (hidden) {
      return List.of(hideButton);
    } else {
      return super.children();
    }
  }

  /**
   * 重新整理所有文本。
   *
   * @see #rearrangeButton
   */
  public void rearrange() {
    MishangUtils.rearrange(textFieldListWidget.getTextContexts());
  }

  /**
   * 退出编辑，不作修改。根据 {@link #removed()}，会发送空 NBT 以表示未修改。
   */
  public void cancelEditing() {
    changed = false;
    if (this.client != null) {
      this.client.setScreen(null);
    }
  }

  /**
   * 初始化，对屏幕进行配置。
   */
  @Override
  protected void init() {
    super.init();
    textFieldListWidget.setHeight(height - 90);
    textFieldListWidget.setWidth(width);

    // 添加按钮

    this.addDrawable(textFieldListWidget);

    if (!isAcceptingCustomValue && !isSelectingButtonToSetCustom) {
      Arrays.stream(toolboxTop).forEach(this::addDrawableChild);
    }

    /// 文本列表屏幕以及占位符
    if (!isAcceptingCustomValue && !isSelectingButtonToSetCustom) {
      this.addSelectableChild(textFieldListWidget);
    }

    initTextHolders();
    initSignPresets();

    /// 下方第三行
    final Stream<ClickableWidget> stream = Streams.concat(Arrays.stream(toolbox1), Arrays.stream(toolbox2), Arrays.stream(toolbox3));
    if (isAcceptingCustomValue) {
      stream.forEach(clickableWidget -> clickableWidget.active = false);
    } else if (isSelectingButtonToSetCustom) {
      stream.peek(clickableWidget -> clickableWidget.active = clickableWidget instanceof FloatButtonWidget && clickableWidget != horizontalAlignButton && clickableWidget != verticalAlignButton || clickableWidget == setCustomValueButton).forEach(this::addDrawableChild);
    } else {
      stream.peek(clickableWidget -> clickableWidget.active = true).forEach(this::addDrawableChild);
    }

    // 添加文本框
    if (initialTexts != null) {
      for (TextContext initialText : initialTexts) {
        textFieldListWidget.addTextField(-1, initialText.clone(), true);
      }
      if (!textFieldListWidget.children().isEmpty()) {
        textFieldListWidget.setFocused(textFieldListWidget.children().getLast());
      }
      initialTexts = null;
    }
    updateContentVisibility();

    arrangeToolboxButtons();
    hideButton.updateTooltip();

    if (isAcceptingCustomValue) {
      customValueTextField.setY(height - 40);
      customValueTextField.setWidth(width - 112);
      customValueConfirmButton.setY(height - 40);
      customValueConfirmButton.setX(width - 105);
      customValueCancelButton.setY(height - 40);
      customValueCancelButton.setX(width - 55);
      addDrawableChild(customValueTextField);
      addDrawableChild(customValueConfirmButton);
      addDrawableChild(customValueCancelButton);
    }
    if (isAcceptingCustomValue || isSelectingButtonToSetCustom) {
      setFocused(null);
    } else if (!textFieldListWidget.children().isEmpty()) {
      setFocused(textFieldListWidget);
    }
  }

  protected List<ButtonWidget> getTextHolders() {
    return List.of(placeHolder);
  }

  protected void initTextHolders() {
    final List<ButtonWidget> textHolders = getTextHolders();
    for (ButtonWidget textHolder : textHolders) {
      this.addDrawableChild(textHolder);
    }
    if (textHolders.size() == 1) {
      textHolders.get(0).setDimensionsAndPosition(200, 20, width / 2 - 100, 35);
    } else if (textHolders.size() >= 2) {
      textHolders.get(0).setDimensionsAndPosition(160, 20, width / 2 - 160, 35);
      textHolders.get(1).setDimensionsAndPosition(160, 20, width / 2, 35);
    }
  }

  protected void initSignPresets() {
    addDrawableChild(signPresets);
    signPresets.setHeight(height - 140);
    signPresets.setX(width / 2 - signPresets.getWidth() / 2);
    signPresets.setY(72);
    signPresets.setScrollY(signPresets.getScrollY());
  }

  /**
   * 更新初始屏幕（未添加文本时的按钮，包括告示牌预设界面）与文本编辑框的可见性。初始化界面以及增删文本时，均调用此方法。
   */
  protected void updateContentVisibility() {
    final boolean visible = textFieldListWidget.children().isEmpty();
    for (ButtonWidget textHolder : getTextHolders()) {
      textHolder.visible = visible;
    }
    signPresets.visible = visible;
    if (!visible) {
      // 在 1.21.3 以下版本，此语句是为了避免预设列表元素隐藏后如果重新显示，仍会聚集到原来的元素，导致 Tab 键导航操作不一致。
      if (signPresets.getSelectedOrNull() != null) {
        signPresets.getSelectedOrNull().setFocused(null);
      }
      signPresets.setSelected(null);
      signPresets.setFocused(null);
    }
    for (SignPresetGridWidget.Entry textHolder : signPresets.children()) {
      for (ButtonWidget button : textHolder.buttons) {
        button.visible = visible;
        if (!visible) {
          // 在 1.21.3 以下版本，选中一个按钮并应用预设后，按钮会持续保持聚集，即使其不显示。
          button.setFocused(false);
        }
      }
    }

    // 同时也需要更新 textFieldListWidget 的可见性
    textFieldListWidget.active = !visible;
  }

  public static final Text HIDDEN_TEXT_NOTE = TextBridge.translatable("message.mishanguc.hide_gui.note");

  @Override
  public void render(DrawContext context, int mouseX, int mouseY, float delta) {
    if (hidden) {
      hideButton.render(context, mouseX, mouseY, delta);
      MultilineText.create(textRenderer, HIDDEN_TEXT_NOTE, width - 20).drawWithShadow(context, 10, 10, 20, 0xffffffff);
      return;
    }
    super.render(context, mouseX, mouseY, delta);
    if (placeHolder.visible) {
      final MutableText text0 = TextBridge.translatable("message.mishanguc.or_use_preset");
      context.drawTextWithShadow(
          textRenderer,
          text0,
          (int) (width / 2f - textRenderer.getWidth(text0) / 2f),
          60,
          0xffdddddd);
    }
    if (isSelectingButtonToSetCustom) {
      final MutableText text = TextBridge.translatable("message.mishanguc.select_button_to_set_custom");
      MultilineText.create(textRenderer, text, width - 20).drawWithShadow(context, 10, 10, 20, 0xffdddddd);
    } else if (isAcceptingCustomValue) {
      final MutableText text = TextBridge.translatable("message.mishanguc.accept_custom_value", customValueFor.getSummaryMessage().copy().formatted(Formatting.YELLOW));
      MultilineText.create(textRenderer, text, width - 20).drawWithShadow(context, 10, 10, 20, 0xffdddddd);
      context.drawTextWithShadow(textRenderer, ScreenTexts.composeGenericOptionText(customValueFor.getSummaryMessage(), ScreenTexts.EMPTY), 5, height - 55, 0xffdddddd);
    }
  }

  /**
   * 切换底部按钮的显示。显示高级按钮，或者取消高级按钮的显示。
   */
  private void arrangeToolboxButtons() {
    // 调整按钮位置
    arrangeToolboxButtons(toolboxTop, 3);
    arrangeToolboxButtons(toolbox1, height - 63);
    arrangeToolboxButtons(toolbox2, height - 43);
    arrangeToolboxButtons(toolbox3, height - 23);
  }

  /**
   * 调整一组按钮的位置，使其依次相邻，并总共居中显示。
   */
  private void arrangeToolboxButtons(ClickableWidget[] widgets, int y) {
    int accumulatedWidth = 0;
    for (ClickableWidget widget : widgets) {
      final int width = widget.getWidth();
      widget.setX(accumulatedWidth);
      accumulatedWidth += width;
    }
    for (ClickableWidget widget : widgets) {
      widget.visible = true;
      widget.setPosition(widget.getX() + width / 2 - accumulatedWidth / 2, y);
    }
  }

  @Override
  public boolean mouseClicked(double mouseX, double mouseY, int button) {
    // 此处采用的是新版的写法，即使用 hoveredElement 找到正在悬浮的对象，而非在此方法中迭代。
    // 旧版本可能采用不同的方法，具体参见 super 方法的源代码。
    Optional<Element> optional = this.hoveredElement(mouseX, mouseY);
    if (optional.isEmpty()) {
      return false;
    } else {
      Element element = optional.get();
      if (isSelectingButtonToSetCustom && element instanceof FloatButtonWidget floatButtonWidget) {
        floatButtonWidget.playDownSound(MinecraftClient.getInstance().getSoundManager());
        customValueStartAccepting(floatButtonWidget);
        return true;
      }
      if (element.mouseClicked(mouseX, mouseY, button)) {
        if (element instanceof EntryListWidget<?> entryListWidget && entryListWidget.visible || element instanceof TextFieldWidget) {
          this.setFocused(element);
        } else {
          setFocused(textFieldListWidget);
        }
        if (button == 0) {
          this.setDragging(true);
        }
      }

      return true;
    }
  }

  private void customValueStartAccepting(FloatButtonWidget floatButtonWidget) {
    isSelectingButtonToSetCustom = false;
    for (Element child : children()) {
      if (child instanceof ClickableWidget clickableWidget) {
        clickableWidget.visible = clickableWidget == floatButtonWidget;
      }
    }
    textFieldListWidget.visible = true;
    isAcceptingCustomValue = true;
    customValueTextField.setEditableColor(0xffe0e0e0);
    customValueTextField.setSuggestion(null);
    clearAndInit();
    setFocused(customValueTextField);
    customValueFor = floatButtonWidget;
    if (floatButtonWidget == colorButton) {
      if (textFieldListWidget.getSelectedOrNull() != null) {
        customValueTextField.setText(MishangUtils.formatColorHex(textFieldListWidget.getSelectedOrNull().textContext.color));
        customValueBeforeChange = (float) textFieldListWidget.getSelectedOrNull().textContext.color;
      } else {
        customValueTextField.setText(StringUtils.EMPTY);
        customValueBeforeChange = null;
      }
      customValueTextField.setChangedListener(s -> {
        final String text = customValueTextField.getText();
        if (text.isEmpty()) {
          customValueTextField.setSuggestion(null);
        } else {
          Arrays.stream(Formatting.values()).filter(Formatting::isColor).map(Formatting::asString).filter(name -> name.startsWith(text)).findAny().ifPresentOrElse(name -> customValueTextField.setSuggestion(name.substring(text.length())), () -> customValueTextField.setSuggestion(null));
        }
        final Integer parse = MishangUtils.parseColor(text).result().orElse(null);
        if (parse == null) {
          customValueTextField.setEditableColor(0xffff5555);
        } else {
          customValueTextField.setEditableColor(0xffe0e0e0);
          for (TextFieldListWidget.Entry entry : textFieldListWidget.selectedEntries) {
            entry.textContext.color = parse;
          }
        }
      });
    } else if (floatButtonWidget == outlineColorButton) {
      if (textFieldListWidget.getSelectedOrNull() != null) {
        if (textFieldListWidget.getSelectedOrNull().textContext.outlineColorType == OutlineColorType.AUTO) {
          customValueTextField.setText("auto");
          customValueBeforeChange = -0.125f;
        } else if (textFieldListWidget.getSelectedOrNull().textContext.outlineColorType == OutlineColorType.NONE) {
          customValueTextField.setText("none");
          customValueBeforeChange = -0.25f;
        } else {
          customValueTextField.setText(MishangUtils.formatColorHex(textFieldListWidget.getSelectedOrNull().textContext.outlineColor));
          customValueBeforeChange = (float) textFieldListWidget.getSelectedOrNull().textContext.outlineColor;
        }
      } else {
        customValueTextField.setText(StringUtils.EMPTY);
        customValueBeforeChange = null;
      }
      customValueTextField.setChangedListener(s -> {
        final String text = customValueTextField.getText();
        if (text.isEmpty()) {
          customValueTextField.setSuggestion(null);
        } else {
          Streams.concat(Stream.of("auto", "none"), Arrays.stream(Formatting.values()).filter(Formatting::isColor).map(Formatting::asString)).filter(name -> name.startsWith(text)).findAny().ifPresentOrElse(name -> customValueTextField.setSuggestion(name.substring(text.length())), () -> customValueTextField.setSuggestion(null));
        }
        if (text.equals("auto")) {
          for (TextFieldListWidget.Entry entry : textFieldListWidget.selectedEntries) {
            entry.textContext.outlineColor = -1;
            entry.textContext.outlineColorType = OutlineColorType.AUTO;
          }
          customValueTextField.setEditableColor(0xffe0e0e0);
          return;
        } else if (text.equals("none")) {
          for (TextFieldListWidget.Entry entry : textFieldListWidget.selectedEntries) {
            entry.textContext.outlineColor = -2;
            entry.textContext.outlineColorType = OutlineColorType.NONE;
          }
          customValueTextField.setEditableColor(0xffe0e0e0);
          return;
        }
        final Integer parse = MishangUtils.parseColor(text).result().orElse(null);
        if (parse == null) {
          customValueTextField.setEditableColor(0xffff5555);
        } else {
          customValueTextField.setEditableColor(0xffe0e0e0);
          for (TextFieldListWidget.Entry entry : textFieldListWidget.selectedEntries) {
            entry.textContext.outlineColor = parse;
            entry.textContext.outlineColorType = OutlineColorType.CUSTOM;
          }
        }
      });
    } else {
      customValueBeforeChange = floatButtonWidget.getValue();
      if (customValueBeforeChange != null) {
        customValueTextField.setText(MishangUtils.numberToString(customValueBeforeChange));
      } else {
        customValueTextField.setText(StringUtils.EMPTY);
      }
      customValueTextField.setChangedListener(s -> {
        try {
          final float value = Float.parseFloat(s);
          customValueTextField.setEditableColor(0xffe0e0e0);
          floatButtonWidget.setAllSameValue(value);
        } catch (NumberFormatException e) {
          customValueTextField.setEditableColor(0xffff5555);
        }
      });
    }
    customValueTextField.setTooltip(Tooltip.of(TextBridge.translatable("message.mishanguc.custom_value.tooltip", floatButtonWidget.getSummaryMessage().copy().formatted(Formatting.YELLOW))));
  }

  private void customValueStopAccepting() {
    isSelectingButtonToSetCustom = false;
    customValueFor = null;
    customValueBeforeChange = null;
    customValueTextField.setChangedListener(s -> {
    });
    isAcceptingCustomValue = false;
    for (Element child : children()) {
      if (child instanceof ClickableWidget clickableWidget) {
        clickableWidget.visible = true;
      }
    }
    setFocused(setCustomValueButton);
    clearAndInit();
  }

  @Override
  public void removed() {
    super.removed();
    entity.setEditor(null);
    final NbtList list = new NbtList();
    for (TextFieldListWidget.Entry entry : textFieldListWidget.children()) {
      list.add(entry.textContext.createNbt(registryLookup));
    }
    ClientPlayNetworking.send(new SignEditFinishPayload(blockPos, changed ? Util.make(new NbtCompound(), nbt -> nbt.put("texts", list)) : null));
  }

  @Override
  protected void clearAndInit() {
    final double scrollAmountBeforeClear = textFieldListWidget.getScrollY();
    final Element previousFocused = getFocused();
    final TextFieldListWidget.Entry previousTextFieldsSelected = textFieldListWidget.getSelectedOrNull();
    final TextFieldListWidget.Entry previousTextFieldsFocused = textFieldListWidget.getFocused();
    final boolean previousSimplified = textFieldListWidget.isSimplified();
    final int previousCuttingHeight = textFieldListWidget.cuttingHeight;
    final List<TextFieldListWidget.Entry> selectedEntriesCopy = List.copyOf(textFieldListWidget.selectedEntries);
    final SignPresetGridWidget.Entry previousSignPresetsFocused = signPresets.getFocused();
    final Element previousSignPresetsFocusedElement = previousSignPresetsFocused == null ? null : previousSignPresetsFocused.getFocused();
    super.clearAndInit();
    setFocused(previousFocused);
    textFieldListWidget.setScrollY(scrollAmountBeforeClear);
    textFieldListWidget.setSelected(previousTextFieldsSelected, false, false);
    textFieldListWidget.setFocused(previousTextFieldsFocused, false, false);
    textFieldListWidget.selectedEntries.clear();
    textFieldListWidget.selectedEntries.addAll(selectedEntriesCopy);
    textFieldListWidget.cuttingHeight = previousCuttingHeight;
    textFieldListWidget.setSimplified(previousSimplified);
    textFieldListWidget.increaseHeight(0);
    for (TextFieldListWidget.Entry selectedEntry : textFieldListWidget.selectedEntries) {
      selectedEntry.setSelected(true);
    }
    signPresets.setFocused(previousSignPresetsFocused);
    if (previousSignPresetsFocused != null) {
      previousSignPresetsFocused.setFocused(previousSignPresetsFocusedElement);
    }
  }

  @Override
  public boolean charTyped(char chr, int modifiers) {
    return super.charTyped(chr, modifiers);
  }

  @Override
  public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
    if (keyCode == GLFW.GLFW_KEY_LEFT_SHIFT || keyCode == GLFW.GLFW_KEY_RIGHT_SHIFT) {
      // 因技术限制，在修改高度以及按下/松开 shift 后需要更新。
      hideButton.updateTooltip();
    }
    if (hasControlDown() && !hasShiftDown() && !hasAltDown()) {
        /*if (keyCode == GLFW.GLFW_KEY_B) {
          boldButton.onPress();
          return true;
          Ctrl + B 与复述功能冲突。
        } else */
      if (keyCode == GLFW.GLFW_KEY_I) {
        italicButton.onPress();
        return true;
      } else if (keyCode == GLFW.GLFW_KEY_U) {
        underlineButton.onPress();
        return true;
      } else if (keyCode == GLFW.GLFW_KEY_S) {
        strikethroughButton.onPress();
        return true;
      } else if (keyCode == GLFW.GLFW_KEY_O) {
        obfuscatedButton.onPress();
        return true;
      } else if (keyCode == GLFW.GLFW_KEY_MINUS || keyCode == GLFW.GLFW_KEY_KP_SUBTRACT) {
        removeTextButton.onPress();
        return true;
      } else if (keyCode == GLFW.GLFW_KEY_KP_ADD) {
        addTextButton.onPress();
        return true;
      } else if (keyCode == GLFW.GLFW_KEY_E && !isAcceptingCustomValue && !hidden) {
        final Element focused = getFocused();
        if (focused instanceof FloatButtonWidget floatButtonWidget && focused != horizontalAlignButton && focused != verticalAlignButton) {
          customValueStartAccepting(floatButtonWidget);
        } else {
          setCustomValueButton.onPress();
        }
        return true;
      }
    } else if (hasControlDown() && hasShiftDown() && !hasAltDown()) {
      if (keyCode == GLFW.GLFW_KEY_EQUAL) {
        addTextButton.onPress();
        return true;
      } else if (keyCode == GLFW.GLFW_KEY_UP) {
        moveUpButton.onPress();
        return true;
      } else if (keyCode == GLFW.GLFW_KEY_DOWN) {
        moveDownButton.onPress();
        return true;
      }
    }
    if (isAcceptingCustomValue) {
      if (keyCode == GLFW.GLFW_KEY_ESCAPE || (keyCode == GLFW.GLFW_KEY_ENTER || keyCode == GLFW.GLFW_KEY_KP_ENTER) && getFocused() == customValueTextField) {
        customValueStopAccepting();
        changed = true;
        return true;
      }
    } else if (isSelectingButtonToSetCustom) {
      if (keyCode == GLFW.GLFW_KEY_ESCAPE) {
        isSelectingButtonToSetCustom = false;
        clearAndInit();
        return true;
      } else if (getFocused() instanceof FloatButtonWidget floatButtonWidget && floatButtonWidget.active && floatButtonWidget.visible) {
        if (KeyCodes.isToggle(keyCode)) {
          floatButtonWidget.playDownSound(MinecraftClient.getInstance().getSoundManager());
          customValueStartAccepting(floatButtonWidget);
          return true;
        }
      }
    } else if (getFocused() == null && (keyCode == GLFW.GLFW_KEY_ENTER || keyCode == GLFW.GLFW_KEY_KP_ENTER)) {
      final TextFieldListWidget.Entry newEntry = textFieldListWidget.addEmptyTextField(-1);
      textFieldListWidget.setFocused(newEntry, false, false);
      setFocused(textFieldListWidget);
      return true;
    } else if (textFieldListWidget.isSimplified() && getFocused() == hideButton) {
      if (keyCode == GLFW.GLFW_KEY_UP) {
        textFieldListWidget.increaseHeight(-8);
        hideButton.updateTooltip();
        return true;
      } else if (keyCode == GLFW.GLFW_KEY_DOWN || keyCode == GLFW.GLFW_HAT_DOWN || keyCode == GLFW.GLFW_GAMEPAD_BUTTON_DPAD_DOWN) {
        textFieldListWidget.increaseHeight(8);
        hideButton.updateTooltip();
        return true;
      }
    }
    return super.keyPressed(keyCode, scanCode, modifiers);
  }

  @Override
  public boolean keyReleased(int keyCode, int scanCode, int modifiers) {
    if (keyCode == GLFW.GLFW_KEY_LEFT_SHIFT || keyCode == GLFW.GLFW_KEY_RIGHT_SHIFT) {
      // 因技术限制，在修改高度以及按下/松开 shift 后需要更新。
      hideButton.updateTooltip();
    }
    return super.keyReleased(keyCode, scanCode, modifiers);
  }

  @Override
  public boolean mouseScrolled(double mouseX, double mouseY, double horizontalAmount, double verticalAmount) {
    // 在启用了简化模式时，对着隐藏按钮并按下 Shift 键滚动时，可调节高度。
    if (textFieldListWidget.isSimplified() && hideButton.isHovered()) {
      textFieldListWidget.increaseHeight((int) verticalAmount * -4);
      hideButton.updateTooltip();
      return true;
    }
    return super.mouseScrolled(mouseX, mouseY, horizontalAmount, verticalAmount);
  }

  private void finishEditing() {
    this.entity.markDirty();
    if (this.client != null) {
      this.client.setScreen(null);
    }
  }

  @Override
  public void tick() {
    super.tick();
    if (entity.isRemoved()) {
      finishEditing();
    }
  }

  @Override
  public void renderBackground(DrawContext context, int mouseX, int mouseY, float delta) {
  }
}
