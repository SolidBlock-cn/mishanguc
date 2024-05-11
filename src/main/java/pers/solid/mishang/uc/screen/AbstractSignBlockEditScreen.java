package pers.solid.mishang.uc.screen;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import com.google.common.collect.Streams;
import com.google.gson.JsonParseException;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.MultilineText;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.Element;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.tooltip.Tooltip;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.ClickableWidget;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.client.input.KeyCodes;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtList;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.screen.ScreenTexts;
import net.minecraft.text.MutableText;
import net.minecraft.text.PlainTextContent;
import net.minecraft.text.Text;
import net.minecraft.text.TextColor;
import net.minecraft.util.DyeColor;
import net.minecraft.util.Formatting;
import net.minecraft.util.Util;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.lwjgl.glfw.GLFW;
import pers.solid.mishang.uc.MishangUtils;
import pers.solid.mishang.uc.blockentity.BlockEntityWithText;
import pers.solid.mishang.uc.networking.SignEditFinishPayload;
import pers.solid.mishang.uc.text.PatternSpecialDrawable;
import pers.solid.mishang.uc.text.SpecialDrawable;
import pers.solid.mishang.uc.text.TextContext;
import pers.solid.mishang.uc.util.HorizontalAlign;
import pers.solid.mishang.uc.util.TextBridge;
import pers.solid.mishang.uc.util.VerticalAlign;

import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
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
 * @see net.minecraft.client.gui.screen.ingame.SignEditScreen
 * @see net.minecraft.client.network.ClientPlayerEntity#openEditSignScreen
 * @see net.minecraft.server.network.ServerPlayerEntity#openEditSignScreen
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
  public final List<TextContext> textContextsEditing;
  public boolean hidden = false;
  private static final ButtonWidget.PressAction EMPTY_PRESS_ACTION = button -> {
  };

  private final RegistryWrapper.WrapperLookup registryLookup;
  public final T entity;

  protected final BiMap<@NotNull TextContext, @NotNull TextFieldWidget> contextToWidgetBiMap =
      HashBiMap.create();
  /**
   * 是否发生了改变。如果改变了，则提交时发送完整内容，否则发送空 NBT 表示未做更改。
   */
  public boolean changed = false;

  public TextFieldListWidget textFieldListWidget;

  /**
   * 正在被选中的 TextWidget。会在 {@link #setFocused(Element)} 时更改。可能为 null。
   */
  public @Nullable TextFieldWidget selectedTextField = null;

  /**
   * 正在被选中的 TextContent。会在 {@link TextFieldListWidget#setFocused(Element)} 时更改。可能为 null。不是副本。
   */
  public @Nullable TextContext selectedTextContext = null;


  /*
  ===== 上方第一行 =====
   */

  /**
   * 上方第一行：添加文本按钮
   */
  public final ButtonWidget addTextButton = new ButtonWidget.Builder(TextBridge.translatable("message.mishanguc.add_text"), button1 -> {
    int index = textFieldListWidget.children().indexOf(textFieldListWidget.children().stream().filter(entry -> entry.textFieldWidget == selectedTextField).findFirst().orElse(null));
    addTextField(index == -1 ? textFieldListWidget.children().size() : index + 1);
  }).position(width / 2 - 120 - 100, 5).size(80, 20).tooltip(Tooltip.of(TextBridge.translatable("message.mishanguc.add_text.description").append(ScreenTexts.LINE_BREAK).append(MishangUtils.describeShortcut(TextBridge.literal("Ctrl + Shift + ").append(TextBridge.translatable("message.mishanguc.keyboard_shortcut.equal")))))).build();

  /**
   * 上方第一行：移除文本按钮
   */
  public final ButtonWidget removeTextButton = new ButtonWidget.Builder(TextBridge.translatable("message.mishanguc.remove_text"), button -> {
    if (selectedTextField == null)
      return;
    int index = textFieldListWidget.children().indexOf(textFieldListWidget.children().stream().filter(entry -> entry.textFieldWidget == selectedTextField).findFirst().orElse(null));
    if (index != -1) {
      removeTextField(index);
    }
  }).dimensions(width / 2 + 120 - 100, 5, 80, 20).tooltip(Tooltip.of(TextBridge.translatable("message.mishanguc.remove_text.description").append(ScreenTexts.LINE_BREAK).append(MishangUtils.describeShortcut(TextBridge.literal("Ctrl + Shift + ").append(TextBridge.translatable("message.mishanguc.keyboard_shortcut.minus")))))).build();


  /**
   * 上方第一行：上移按钮。
   */
  public final ButtonWidget moveUpButton = new ButtonWidget.Builder(TextBridge.translatable("message.mishanguc.moveUp"), button -> {
    if (selectedTextField == null)
      return;
    int i = textFieldListWidget.children().indexOf(textFieldListWidget.getSelectedOrNull());
    final TextContext textContext = AbstractSignBlockEditScreen.this.textContextsEditing.get(i);
    removeTextField(i);
    if (i > 0)
      i--;
    addTextField(i, textContext, false);
  }).dimensions(this.width - 20, 5, 80, 20).tooltip(Tooltip.of(TextBridge.translatable("message.mishanguc.moveUp.description").append(ScreenTexts.LINE_BREAK).append(MishangUtils.describeShortcut(TextBridge.literal("Ctrl + Shift + ").append(TextBridge.translatable("key.keyboard.up")))))).build();

  /**
   * 上方第一行：下移按钮。
   */
  public final ButtonWidget moveDownButton = new ButtonWidget.Builder(TextBridge.translatable("message.mishanguc.moveDown"), button -> {
    if (selectedTextField == null)
      return;
    int i = textFieldListWidget.children().indexOf(textFieldListWidget.getSelectedOrNull());
    final TextContext textContext = AbstractSignBlockEditScreen.this.textContextsEditing.get(i);
    removeTextField(i);
    if (i < textFieldListWidget.children().size())
      i++;
    addTextField(i, textContext, false);
  }).dimensions(this.width - 20, 5, 80, 20).tooltip(Tooltip.of(TextBridge.translatable("message.mishanguc.moveDown.description").append(ScreenTexts.LINE_BREAK).append(MishangUtils.describeShortcut(TextBridge.literal("Ctrl + Shift + ").append(TextBridge.translatable("key.keyboard.down")))))).build();

  /**
   * 上方第一行：重排按钮。
   */
  public final ButtonWidget clearButton = new ButtonWidget.Builder(BUTTON_CLEAR_MESSAGE, button -> {
    if (button.getMessage() == BUTTON_CLEAR_CONFIRM_MESSAGE) {
      for (int i = AbstractSignBlockEditScreen.this.textFieldListWidget.children().size() - 1; i >= 0; i--) {
        removeTextField(i);
      }
      button.setMessage(BUTTON_CLEAR_MESSAGE);
      button.setTooltip(Tooltip.of(BUTTON_CLEAR_DESCRIPTION_MESSAGE));
    } else {
      // 要求用户再次点击一次按钮才能删除。
      button.setMessage(BUTTON_CLEAR_CONFIRM_MESSAGE);
      button.setTooltip(Tooltip.of(BUTTON_CLEAR_CONFIRM_DESCRIPTION_MESSAGE));
    }
  }).dimensions(this.width / 2 + 190, this.height - 50, 80, 20).tooltip(Tooltip.of(TextBridge.translatable("message.mishanguc.rearrange.tooltip"))).build();



  /*
   ===== 文本区域列表部分 =====
   */

  /**
   * 没有添加文本时，显示的一条“点击此处添加文本”的消息。文本添加后，该按钮将消失。
   */
  public final ButtonWidget placeHolder = new ButtonWidget.Builder(TextBridge.translatable("message.mishanguc.add_first_text"), button -> {
    addTextField(0);
    setFocused(textFieldListWidget);
    textFieldListWidget.setFocused(textFieldListWidget.children().get(0));
  }).dimensions(0, 35, 200, 20).build();

  @ApiStatus.AvailableSince("0.1.6")
  public final ButtonWidget applyDoubleLineTemplateButton = new ButtonWidget.Builder(TextBridge.translatable("message.mishanguc.apply_double_line_template"), button -> {
    addTextField(0, AbstractSignBlockEditScreen.this.entity.createDefaultTextContext(), false);
    addTextField(1, Util.make(AbstractSignBlockEditScreen.this.entity.createDefaultTextContext(), textContext -> textContext.size /= 2), false);
    textFieldListWidget.setFocused(textFieldListWidget.children().get(0));
    rearrange();
  }).dimensions(width / 2 - 50, 70, 120, 20).tooltip(Tooltip.of(TextBridge.translatable("message.mishanguc.apply_double_line_template.description"))).build();

  @ApiStatus.AvailableSince("0.1.6")
  public final ButtonWidget applyLeftArrowTemplateButton = new ButtonWidget.Builder(TextBridge.translatable("message.mishanguc.apply_left_arrow_template"), (ButtonWidget button) -> {
    BlockEntityWithText entity = AbstractSignBlockEditScreen.this.entity;
    final TextContext textContext0 = entity.createDefaultTextContext();
    textContext0.extra = PatternSpecialDrawable.fromName(textContext0, "al");
    textContext0.size = 8;
    textContext0.offsetX = -4;
    textContext0.absolute = true;
    AbstractSignBlockEditScreen.this.addTextField(0, textContext0, false);
    final TextContext textContext1 = entity.createDefaultTextContext();
    textContext1.offsetX = 8;
    textContext1.horizontalAlign = HorizontalAlign.LEFT;
    AbstractSignBlockEditScreen.this.addTextField(1, textContext1, false);
    final TextContext textContext2 = entity.createDefaultTextContext();
    textContext2.offsetX = 8;
    textContext2.horizontalAlign = HorizontalAlign.LEFT;
    textContext2.size /= 2;
    AbstractSignBlockEditScreen.this.addTextField(2, textContext2, false);
    textFieldListWidget.setFocused(textFieldListWidget.children().get(1));
    rearrange();
  }).dimensions(width / 2 - 150, 70, 120, 20).tooltip(Tooltip.of(TextBridge.translatable("message.mishanguc.apply_left_arrow_template.description"))).build();

  @ApiStatus.AvailableSince("0.1.6")
  public final ButtonWidget applyRightArrowTemplateButton = new ButtonWidget.Builder(TextBridge.translatable("message.mishanguc.apply_right_arrow_template"), (ButtonWidget button) -> {
    BlockEntityWithText entity = AbstractSignBlockEditScreen.this.entity;
    final TextContext textContext0 = entity.createDefaultTextContext();
    textContext0.extra = PatternSpecialDrawable.fromName(textContext0, "ar");
    textContext0.size = 8;
    textContext0.offsetX = 4;
    textContext0.absolute = true;
    AbstractSignBlockEditScreen.this.addTextField(0, textContext0, false);
    final TextContext textContext1 = entity.createDefaultTextContext();
    textContext1.offsetX = -8;
    textContext1.horizontalAlign = HorizontalAlign.RIGHT;
    AbstractSignBlockEditScreen.this.addTextField(1, textContext1, false);
    final TextContext textContext2 = entity.createDefaultTextContext();
    textContext2.offsetX = -8;
    textContext2.horizontalAlign = HorizontalAlign.RIGHT;
    textContext2.size /= 2;
    AbstractSignBlockEditScreen.this.addTextField(2, textContext2, false);
    textFieldListWidget.setFocused(textFieldListWidget.children().get(1));
    rearrange();
  }).dimensions(width / 2 - 50, 70, 120, 20).tooltip(Tooltip.of(TextBridge.translatable("message.mishanguc.apply_right_arrow_template.description"))).build();


  /*
  ===== 下方第一行 =====
   */

  /**
   * 下方第一行：加粗按钮。
   */
  public final BooleanButtonWidget boldButton = new BooleanButtonWidget(this.width / 2 - 200, this.height - 50, 20, 20, TextBridge.translatable("message.mishanguc.bold"), button -> selectedTextContext == null ? null : selectedTextContext.bold, b -> {
    changed = true;
    if (selectedTextContext != null)
      selectedTextContext.bold = b;
  }, EMPTY_PRESS_ACTION)
      .setRenderedName(TextBridge.literal("B").formatted(Formatting.BOLD));

  /**
   * 下方第一行：斜体按钮。
   */
  public final BooleanButtonWidget italicButton = new BooleanButtonWidget(this.width / 2 - 180, this.height - 50, 20, 20, TextBridge.translatable("message.mishanguc.italic"), button -> selectedTextContext == null ? null : selectedTextContext.italic, b -> {
    changed = true;
    if (selectedTextContext != null)
      selectedTextContext.italic = b;
  }, EMPTY_PRESS_ACTION)
      .setRenderedName(TextBridge.literal("I").formatted(Formatting.ITALIC))
      .setKeyboardShortcut(TextBridge.literal("Ctrl + I"));

  /**
   * 下方第一行：下划线按钮。
   */
  public final BooleanButtonWidget underlineButton = new BooleanButtonWidget(this.width / 2 - 160, this.height - 50, 20, 20, TextBridge.translatable("message.mishanguc.underline"), button -> selectedTextContext == null ? null : selectedTextContext.underline, b -> {
    changed = true;
    if (selectedTextContext != null)
      selectedTextContext.underline = b;
  }, EMPTY_PRESS_ACTION)
      .setRenderedName(TextBridge.literal("U").formatted(Formatting.UNDERLINE))
      .setKeyboardShortcut(TextBridge.literal("Ctrl + U"));

  /**
   * 下方第一行：删除线按钮。
   */
  public final BooleanButtonWidget strikethroughButton = new BooleanButtonWidget(this.width / 2 - 140, this.height - 50, 20, 20, TextBridge.translatable("message.mishanguc.strikethrough"), button -> selectedTextContext == null ? null : selectedTextContext.strikethrough, b -> {
    changed = true;
    if (selectedTextContext != null)
      selectedTextContext.strikethrough = b;
  }, EMPTY_PRESS_ACTION)
      .setRenderedName(TextBridge.literal("S").formatted(Formatting.STRIKETHROUGH))
      .setKeyboardShortcut(TextBridge.literal("Ctrl + S"));

  /**
   * 下方第一行：随机文字（obfuscated）按钮。
   */
  public final BooleanButtonWidget obfuscatedButton = new BooleanButtonWidget(this.width / 2 - 120, this.height - 50, 20, 20, TextBridge.translatable("message.mishanguc.obfuscated"), button -> selectedTextContext == null ? null : selectedTextContext.obfuscated, b -> {
    changed = true;
    if (selectedTextContext != null)
      selectedTextContext.obfuscated = b;
  }, EMPTY_PRESS_ACTION)
      .setRenderedName(TextBridge.literal("O").formatted(Formatting.OBFUSCATED))
      .setKeyboardShortcut(TextBridge.literal("Ctrl + O"));


  /**
   * 下方第一行：阴影按钮。
   */
  public final BooleanButtonWidget shadeButton = new BooleanButtonWidget(this.width / 2 - 100, this.height - 50, 35, 20, TextBridge.translatable("message.mishanguc.shade"), button -> selectedTextContext == null ? null : selectedTextContext.shadow, b -> {
    changed = true;
    if (selectedTextContext != null)
      selectedTextContext.shadow = b;
  }, EMPTY_PRESS_ACTION)
      .setTooltip(TextBridge.translatable("message.mishanguc.shade.description"));

  /**
   * 下方第一行：文本大小按钮。
   */
  public final FloatButtonWidget sizeButton = new FloatButtonWidget(this.width / 2 - 60, this.height - 50, 35, 20, TextBridge.translatable("message.mishanguc.size"), buttons -> selectedTextContext != null ? selectedTextContext.size : null, value -> {
    changed = true;
    if (selectedTextContext != null) {
      selectedTextContext.size = value;
    }
  }, EMPTY_PRESS_ACTION);

  /**
   * 下方第一行：X偏移。
   */
  public final FloatButtonWidget offsetXButton = new FloatButtonWidget(this.width / 2 - 10, this.height - 50, 40, 20, TextBridge.translatable("message.mishanguc.offsetX"), button -> selectedTextContext != null ? selectedTextContext.offsetX : null, value -> {
    changed = true;
    if (selectedTextContext != null) {
      selectedTextContext.offsetX = value;
    }
  }, EMPTY_PRESS_ACTION);

  /**
   * 下方第一行：Y偏移。
   */
  public final FloatButtonWidget offsetYButton = new FloatButtonWidget(this.width / 2 + 40, this.height - 50, 40, 20, TextBridge.translatable("message.mishanguc.offsetY"), button -> selectedTextContext != null ? selectedTextContext.offsetY : null, value -> {
    changed = true;
    if (selectedTextContext != null) {
      selectedTextContext.offsetY = value;
    }
  }, EMPTY_PRESS_ACTION);

  /**
   * 下方第一行：Z偏移。
   */
  public final FloatButtonWidget offsetZButton = new FloatButtonWidget(this.width / 2 + 40, this.height - 50, 40, 20, TextBridge.translatable("message.mishanguc.offsetZ"), button -> selectedTextContext != null ? selectedTextContext.offsetZ : null, value -> {
    changed = true;
    if (selectedTextContext != null) {
      selectedTextContext.offsetZ = value;
    }
  }, EMPTY_PRESS_ACTION);

  /**
   * 下方第一行：颜色。
   */
  public final FloatButtonWidget colorButton = new FloatButtonWidget(0, 0, 50, 20, TextBridge.translatable("message.mishanguc.color"), button -> {
    changed = true;
    if (selectedTextContext == null) {
      return null;
    }
    final DyeColor dyeColor = MishangUtils.colorBySignColor(selectedTextContext.color);
    if (dyeColor == null) {
      return -2f;
    } else {
      return (float) dyeColor.getId();
    }
  }, colorId -> {
    if (selectedTextContext != null) {
      selectedTextContext.color = DyeColor.byId((int) colorId).getSignColor();
    }
  }, EMPTY_PRESS_ACTION).nameValueAs(colorId -> {
    if (colorId == -2 && selectedTextContext != null) {
      return MishangUtils.describeColor(selectedTextContext.color);
    } else {
      final DyeColor dyeColor = DyeColor.byId((int) colorId);
      return MishangUtils.describeColor(dyeColor.getSignColor(), TextBridge.translatable("color.minecraft." + dyeColor.asString()));
    }
  }).setRenderedNameSupplier((value, valueText) -> valueText);

  /**
   * 下方第一行：描边颜色。
   */
  @ApiStatus.AvailableSince("0.1.6-mc1.17")
  public final FloatButtonWidget outlineColorButton = new FloatButtonWidget(0, 0, 70, 20, TextBridge.translatable("message.mishanguc.outline_color"), button -> {
    if (selectedTextContext == null) {
      return null;
    }
    if (selectedTextContext.outlineColor == -1) {
      return -1f;
    } else if (selectedTextContext.outlineColor == -2) {
      return -2f;
    }
    final DyeColor colorOutline = MishangUtils.COLOR_TO_OUTLINE_COLOR.inverse().get(selectedTextContext.outlineColor);
    if (colorOutline != null) {
      return colorOutline.getId() + 16f;
    }
    final DyeColor color = MishangUtils.colorBySignColor(selectedTextContext.outlineColor);
    if (color != null) {
      return (float) color.getId();
    } else {
      return -3f;
    }
  }, colorId -> {
    changed = true;
    if (selectedTextContext != null) {
      if (colorId == -1 || colorId == -2) {
        selectedTextContext.outlineColor = (int) colorId;
      } else if (colorId > 15) {
        selectedTextContext.outlineColor = MishangUtils.COLOR_TO_OUTLINE_COLOR.get(DyeColor.byId((int) colorId - 16));
      } else {
        selectedTextContext.outlineColor = DyeColor.byId((int) colorId).getSignColor();
      }
    }
  }, EMPTY_PRESS_ACTION).nameValueAs(colorId -> {
    // colorId=-1：表示当前自动根据文本内容绘制描边。
    // colorId=-2：表示当前不绘制描边（默认）。
    // colorId=-3：表示是自定义的。
    // colorId=null：表示当前没有选中文本。
    // colorId=0-15：标准颜色。
    // colorId=16-31：描边颜色
    if (colorId == -1) {
      return TextBridge.translatable("message.mishanguc.outline_color.auto");
    } else if (colorId == -2) {
      return TextBridge.translatable("message.mishanguc.outline_color.none");
    } else if (colorId == -3 && selectedTextContext != null) {
      return MishangUtils.describeColor(selectedTextContext.outlineColor);
    } else if (colorId > 15) {
      final DyeColor color = DyeColor.byId((int) colorId - 16);
      return TextBridge.translatable("message.mishanguc.outline_color.relate", MishangUtils.describeColor(selectedTextContext.outlineColor, TextBridge.translatable("message.mishanguc.outline_color.relate.$1")), MishangUtils.describeColor(color.getSignColor(), TextBridge.translatable("color.minecraft." + color.asString())));
    } else {
      final DyeColor color = DyeColor.byId((int) colorId);
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
    } else if (selectedTextContext != null) {
      return TextBridge.translatable("message.mishanguc.outline_color.composed", MishangUtils.describeColor(selectedTextContext.outlineColor));
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
  public final FloatButtonWidget rotationXButton = new FloatButtonWidget(this.width / 2 + 40, this.height - 50, 40, 20, TextBridge.translatable("message.mishanguc.rotationX"), button -> selectedTextContext != null ? selectedTextContext.rotationX : null, value -> {
    changed = true;
    if (selectedTextContext != null)
      selectedTextContext.rotationX = value;
  }, EMPTY_PRESS_ACTION);

  /**
   * 下方第二行：Y旋转。
   */
  @ApiStatus.AvailableSince("0.1.6")
  public final FloatButtonWidget rotationYButton = new FloatButtonWidget(this.width / 2 + 40, this.height - 50, 40, 20, TextBridge.translatable("message.mishanguc.rotationY"), button -> selectedTextContext != null ? selectedTextContext.rotationY : null, value -> {
    changed = true;
    if (selectedTextContext != null)
      selectedTextContext.rotationY = value;
  }, EMPTY_PRESS_ACTION);

  /**
   * 下方第二行：Z旋转。
   */
  @ApiStatus.AvailableSince("0.1.6")
  public final FloatButtonWidget rotationZButton = new FloatButtonWidget(this.width / 2 + 40, this.height - 50, 40, 20, TextBridge.translatable("message.mishanguc.rotationZ"), button -> selectedTextContext != null ? selectedTextContext.rotationZ : null, value -> {
    changed = true;
    if (selectedTextContext != null)
      selectedTextContext.rotationZ = value;
  }, EMPTY_PRESS_ACTION);

  /**
   * 下方第二行：X缩放。
   */
  public final FloatButtonWidget scaleXButton = new FloatButtonWidget(this.width / 2 + 90, this.height - 50, 40, 20, TextBridge.translatable("message.mishanguc.scaleX"), button -> selectedTextContext != null ? selectedTextContext.scaleX : null, value -> {
    changed = true;
    if (selectedTextContext != null) {
      selectedTextContext.scaleX = value;
    }
  }, EMPTY_PRESS_ACTION);

  /**
   * 下方第二行：Y缩放。
   */
  public final FloatButtonWidget scaleYButton = new FloatButtonWidget(this.width / 2 + 140, this.height - 50, 40, 20, TextBridge.translatable("message.mishanguc.scaleY"), button -> selectedTextContext != null ? selectedTextContext.scaleY : null, value -> {
    changed = true;
    if (selectedTextContext != null) {
      selectedTextContext.scaleY = value;
    }
  }, EMPTY_PRESS_ACTION);

  /**
   * 下方第二行：水平对齐方式。
   */
  public final FloatButtonWidget horizontalAlignButton = new FloatButtonWidget(0, 0, 50, 20, TextBridge.translatable("message.mishanguc.horizontal_align"), b -> selectedTextContext != null ? (float) selectedTextContext.horizontalAlign.ordinal() : null, f -> {
    if (selectedTextContext != null) {
      selectedTextContext.horizontalAlign = HorizontalAlign.values()[(int) f];
    }
  }, b -> {
  }).nameValueAs(f -> HorizontalAlign.values()[(int) f].getName()).setRenderedNameSupplier((value, valueText) -> valueText);

  /**
   * 下方第二行：垂直对齐方式。
   */
  public final FloatButtonWidget verticalAlignButton = new FloatButtonWidget(0, 0, 50, 20, TextBridge.translatable("message.mishanguc.vertical_align"), b -> selectedTextContext != null ? (float) selectedTextContext.verticalAlign.ordinal() : null, f -> {
    if (selectedTextContext != null) {
      selectedTextContext.verticalAlign = VerticalAlign.values()[(int) f];
    }
  }, b -> {
  }).nameValueAs(f -> VerticalAlign.values()[(int) f].getName()).setRenderedNameSupplier((value, valueText) -> valueText);

  /**
   * 下方第二行：切换文字是否可以看穿。
   */
  public final BooleanButtonWidget seeThroughButton = new BooleanButtonWidget(0, 0, 60, 20, TextBridge.translatable("message.mishanguc.see_through"), button -> selectedTextContext == null ? null : selectedTextContext.seeThrough, b -> {
    changed = true;
    if (selectedTextContext != null)
      selectedTextContext.seeThrough = b;
  }, EMPTY_PRESS_ACTION);

  /**
   * 下方第二行：绝对模式。
   */
  public final BooleanButtonWidget absoluteButton = new BooleanButtonWidget(0, 0, 50, 20, TextBridge.translatable("message.mishanguc.absolute"), button -> selectedTextContext != null ? selectedTextContext.absolute : null, b -> {
    changed = true;
    if (selectedTextContext != null)
      selectedTextContext.absolute = b;
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
  private Float customValueBeforeChange;
  private FloatButtonWidget customValueFor;
  public final ButtonWidget customValueConfirmButton = ButtonWidget.builder(ScreenTexts.OK, button -> {
    customValueStopAccepting();
    changed = true;
  }).dimensions(width - 105, height - 40, 50, 20).build();
  public final ButtonWidget customValueCancelButton = ButtonWidget.builder(ScreenTexts.CANCEL, button -> {
    if (customValueFor != null) {
      if (customValueFor == colorButton) {
        if (selectedTextContext != null)
          selectedTextContext.color = customValueBeforeChange.intValue();
      } else if (customValueFor == outlineColorButton) {
        if (selectedTextContext != null)
          selectedTextContext.outlineColor = customValueBeforeChange.intValue();
      } else {
        customValueFor.setValue(customValueBeforeChange == null ? customValueFor.defaultValue : customValueBeforeChange);
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
      for (TextContext textContext : AbstractSignBlockEditScreen.this.textContextsEditing) {
        textContext.flip();
      }
    } else {
      if (selectedTextContext != null) {
        selectedTextContext.flip();
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
   * 下方第三行：清除所有文本的按钮。
   */
  public final ButtonWidget rearrangeButton = new ButtonWidget.Builder(TextBridge.translatable("message.mishanguc.rearrange"), button -> rearrange()).dimensions(this.width / 2, this.height - 50, 40, 20).tooltip(Tooltip.of(BUTTON_CLEAR_CONFIRM_DESCRIPTION_MESSAGE)).build();

  /**
   * 下方第三行：隐藏界面
   */
  public final ButtonWidget hideButton = new BooleanButtonWidget(0, height - 25, 40, 20, TextBridge.translatable("message.mishanguc.hide_gui"), booleanButtonWidget -> hidden, value -> hidden = value, EMPTY_PRESS_ACTION)
      .setRenderedNameSupplier(value -> Boolean.TRUE.equals(value) ? TextBridge.translatable("message.mishanguc.hide_gui.show") : TextBridge.translatable("message.mishanguc.hide_gui.hide"))
      .setTooltipSupplier(value -> Boolean.TRUE.equals(value) ? null : TextBridge.translatable("message.mishanguc.hide_gui.tooltip"));

  public final ClickableWidget[] toolboxTop = new ClickableWidget[]{addTextButton, removeTextButton, moveUpButton, moveDownButton, clearButton};
  public final ClickableWidget[] toolbox1 = new ClickableWidget[]{boldButton, italicButton, underlineButton, strikethroughButton, obfuscatedButton, shadeButton, sizeButton, offsetXButton, offsetYButton, offsetZButton, colorButton, outlineColorButton};
  public final ClickableWidget[] toolbox2 = new ClickableWidget[]{rotationXButton, rotationYButton, rotationZButton, scaleXButton, scaleYButton, horizontalAlignButton, verticalAlignButton, seeThroughButton, absoluteButton};
  public final ClickableWidget[] toolbox3 = new ClickableWidget[]{setCustomValueButton, flipButton, finishButton, cancelButton, rearrangeButton, hideButton};


  public AbstractSignBlockEditScreen(RegistryWrapper.WrapperLookup registryLookup, T entity, BlockPos blockPos, List<TextContext> textContextsEditing) {
    super(TextBridge.translatable("message.mishanguc.sign_edit"));
    this.registryLookup = registryLookup;
    this.entity = entity;
    this.blockPos = blockPos;
    this.textContextsEditing = textContextsEditing;
    entity.setEditor(this.client != null ? this.client.player : null);
    sizeButton.defaultValue = entity.createDefaultTextContext().size;
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
    final List<TextContext> textContextsEditing = AbstractSignBlockEditScreen.this.textContextsEditing;
    MishangUtils.rearrange(textContextsEditing);
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
    textFieldListWidget = new TextFieldListWidget(this, client, width, height - 90, 25, 16);
    // 添加按钮

    if (!isAcceptingCustomValue && !isSelectingButtonToSetCustom) {
      Arrays.stream(toolboxTop).forEach(this::addDrawableChild);
    }

    /// 文本列表屏幕以及占位符
    if (!isAcceptingCustomValue && !isSelectingButtonToSetCustom) {
      this.addDrawableChild(textFieldListWidget);
    } else {
      this.addDrawable(textFieldListWidget);
    }
    initTextHolders();

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
    for (int i = 0, textContextsEditingSize = textContextsEditing.size();
         i < textContextsEditingSize;
         i++) {
      TextContext textContext = textContextsEditing.get(i);
      addTextField(i, textContext, true);
    }

    arrangeToolboxButtons();

    placeHolder.setX(width / 2 - 100);
    applyDoubleLineTemplateButton.setX(width / 2 - 60);
    applyLeftArrowTemplateButton.setX(width / 2 - 180);
    applyRightArrowTemplateButton.setX(width / 2 + 60);

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

  protected void initTextHolders() {
    this.addDrawableChild(placeHolder);
    this.addDrawableChild(applyLeftArrowTemplateButton);
    this.addDrawableChild(applyDoubleLineTemplateButton);
    this.addDrawableChild(applyRightArrowTemplateButton);
  }

  public static final Text HIDDEN_TEXT_NOTE = TextBridge.translatable("message.mishanguc.hide_gui.note");

  @Override
  public void render(DrawContext context, int mouseX, int mouseY, float delta) {
    if (hidden) {
      hideButton.render(context, mouseX, mouseY, delta);
      MultilineText.create(textRenderer, HIDDEN_TEXT_NOTE, width - 20).drawWithShadow(context, 10, 10, 20, 0xffffff);
      return;
    }
    super.render(context, mouseX, mouseY, delta);
    if (placeHolder.visible) {
      final MutableText text0 = TextBridge.translatable("message.mishanguc.or");
      context.drawTextWithShadow(
          textRenderer,
          text0,
          (int) (width / 2f - textRenderer.getWidth(text0) / 2f),
          60,
          0xdddddd);
    }
    if (isSelectingButtonToSetCustom) {
      final MutableText text = TextBridge.translatable("message.mishanguc.select_button_to_set_custom");
      MultilineText.create(textRenderer, text, width - 20).drawWithShadow(context, 10, 10, 20, 0xdddddd);
    } else if (isAcceptingCustomValue) {
      final MutableText text = TextBridge.translatable("message.mishanguc.accept_custom_value", customValueFor.getSummaryMessage().copy().formatted(Formatting.YELLOW));
      MultilineText.create(textRenderer, text, width - 20).drawWithShadow(context, 10, 10, 20, 0xdddddd);
      context.drawTextWithShadow(textRenderer, ScreenTexts.composeGenericOptionText(customValueFor.getSummaryMessage(), ScreenTexts.EMPTY), 5, height - 55, 0xdddddd);
    }
  }

  /**
   * 添加一个新的文本框，并将其添加到 {@link #contextToWidgetBiMap} 中。
   *
   * @param index 添加到的位置，对应在数组或列表中的次序。
   */
  public void addTextField(int index) {
    // 添加时，默认相当于上一行的。
    final TextContext emptyTextContext = index > 0 ? textContextsEditing.get(index - 1).clone() : entity.createDefaultTextContext();
    emptyTextContext.text = null;
    emptyTextContext.extra = null;
    addTextField(index, emptyTextContext, false);
  }

  /**
   * 添加一个文本框，并将其添加到 {@link #contextToWidgetBiMap} 中。
   *
   * @param index       添加到的位置，对应在数组或列表中的次序。
   * @param textContext 需要添加的 {@link TextContext}。
   * @param isExisting  是否为现有的，如果是，则不会将这个 textContext 添加到 {@link #textContextsEditing} 中，也不会将 {@link #changed} 设为 <code>true</code>。
   */
  public void addTextField(int index, @NotNull TextContext textContext, boolean isExisting) {
    if (!isExisting) {
      textContextsEditing.add(index, textContext);
      changed = true;
    }
    final TextFieldWidget textFieldWidget = new TextFieldWidget(textRenderer, 2, 0, width - 4, 15, TextBridge.empty());
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
        textFieldWidget.setText("-json" + Text.Serialization.toJsonString(textContext.text, registryLookup));
      }
    }
    final TextFieldListWidget.Entry newEntry = textFieldListWidget.new Entry(textFieldWidget);
    textFieldListWidget.children().add(index, newEntry);
    contextToWidgetBiMap.put(textContext, textFieldWidget);
    textFieldListWidget.setFocused(newEntry);
    textFieldListWidget.setScrollAmount(textFieldListWidget.getScrollAmount());
    if (!textFieldListWidget.children().isEmpty()) {
      setFocused(textFieldListWidget);
    }
    textFieldWidget.setChangedListener(s -> {
      final TextContext textContext1 = contextToWidgetBiMap.inverse().get(textFieldWidget);
      if (textContext1 != null) {
        final Matcher matcher = Pattern.compile("^-(\\w+?) (.+)$").matcher(s);
        if (matcher.matches()) {
          final String name = matcher.group(1);
          final String value = matcher.group(2);
          switch (name) {
            case "literal":
              textContext1.text = TextBridge.literal(value);
              break;
            case "json":
              try {
                textContext1.text = Text.Serialization.fromLenientJson(value, registryLookup);
              } catch (
                  JsonParseException e) {
                // 如果文本有问题，则不执行操作。
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
              } // 如果为 INVALID 则不执行操作。
          }
        } else {
          textContext1.extra = null;
          textContext1.text = TextBridge.literal(s);
        }
      }
      changed = true;
    });
    placeHolder.visible = false;
    applyDoubleLineTemplateButton.visible = false;
    applyLeftArrowTemplateButton.visible = false;
    applyRightArrowTemplateButton.visible = false;
  }

  /**
   * 切换底部按钮的显示。显示高级按钮，或者取消高级按钮的显示。
   */
  private void arrangeToolboxButtons() {
    // 调整按钮位置
    arrangeToolboxButtons(toolboxTop, 5);
    arrangeToolboxButtons(toolbox1, height - 65);
    arrangeToolboxButtons(toolbox2, height - 45);
    arrangeToolboxButtons(toolbox3, height - 25);
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

  /**
   * 移除一个文本框。将从 {@link #contextToWidgetBiMap} 和 {@link #textContextsEditing} 中移除对应的元素。
   *
   * @param index 移除的文本框的位置。
   * @see #addTextField(int, TextContext, boolean)
   */
  public void removeTextField(int index) {
    final List<TextFieldListWidget.Entry> children = textFieldListWidget.children();
    final TextFieldWidget removedWidget = children.remove(index).textFieldWidget;
    final TextContext removedTextContext = contextToWidgetBiMap.inverse().get(removedWidget);
    if (textFieldListWidget.getSelectedOrNull() != null
        && removedWidget == textFieldListWidget.getSelectedOrNull().textFieldWidget) {
      textFieldListWidget.setFocused(null);
    }
    if (!children.isEmpty()) {
      textFieldListWidget.setFocused(children.get(MathHelper.clamp(index - 1, 0, children.size() - 1)));
    }
    // 删除一行元素后，对滚动数量进行一次 clamp，以避免出现过度滚动的情况。
    textFieldListWidget.setScrollAmount(textFieldListWidget.getScrollAmount());
    textContextsEditing.remove(removedTextContext);
    contextToWidgetBiMap.remove(removedTextContext);
    placeHolder.visible = children.isEmpty();
    applyDoubleLineTemplateButton.visible = placeHolder.visible;
    applyLeftArrowTemplateButton.visible = placeHolder.visible;
    applyRightArrowTemplateButton.visible = placeHolder.visible;
    changed = true;
  }

  @Override
  public boolean mouseClicked(double mouseX, double mouseY, int button) {
    for (Element element : this.children()) {
      if (isSelectingButtonToSetCustom && element instanceof FloatButtonWidget floatButtonWidget) {
        if (element.isMouseOver(mouseX, mouseY)) {
          floatButtonWidget.playDownSound(MinecraftClient.getInstance().getSoundManager());
          customValueStartAccepting(floatButtonWidget);
          return true;
        } else {
          continue;
        }
      }
      if (!element.mouseClicked(mouseX, mouseY, button))
        continue;
      if (element == textFieldListWidget || element instanceof TextFieldWidget) {
        this.setFocused(element);
      } else {
        setFocused(textFieldListWidget);
      }
      if (button == 0) {
        this.setDragging(true);
      }
      return true;
    }
    return false;
  }

  private void customValueStartAccepting(FloatButtonWidget floatButtonWidget) {
    isSelectingButtonToSetCustom = false;
    for (Element child : children()) {
      if (child instanceof ClickableWidget clickableWidget) {
        clickableWidget.visible = clickableWidget == floatButtonWidget;
      }
    }
    isAcceptingCustomValue = true;
    customValueTextField.setEditableColor(16777215);
    customValueTextField.setSuggestion(null);
    clearAndInit();
    setFocused(customValueTextField);
    customValueFor = floatButtonWidget;
    if (floatButtonWidget == colorButton) {
      if (selectedTextContext != null) {
        customValueTextField.setText(MishangUtils.formatColorHex(selectedTextContext.color));
        customValueBeforeChange = (float) selectedTextContext.color;
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
        final TextColor parse = TextColor.parse(text).result().orElse(null);
        if (parse == null) {
          customValueTextField.setEditableColor(16733525);
        } else {
          customValueTextField.setEditableColor(16777215);
          if (selectedTextContext != null)
            selectedTextContext.color = parse.getRgb();
        }
      });
    } else if (floatButtonWidget == outlineColorButton) {
      if (selectedTextContext != null) {
        if (selectedTextContext.outlineColor == -1) {
          customValueTextField.setText("auto");
        } else if (selectedTextContext.outlineColor == -2) {
          customValueTextField.setText("none");
        } else {
          customValueTextField.setText(MishangUtils.formatColorHex(selectedTextContext.outlineColor));
        }
        customValueBeforeChange = (float) selectedTextContext.outlineColor;
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
          selectedTextContext.outlineColor = -1;
          customValueTextField.setEditableColor(16777215);
          return;
        } else if (text.equals("none")) {
          selectedTextContext.outlineColor = -2;
          customValueTextField.setEditableColor(16777215);
          return;
        }
        final TextColor parse = TextColor.parse(text).result().orElse(null);
        if (parse == null) {
          customValueTextField.setEditableColor(16733525);
        } else {
          customValueTextField.setEditableColor(16777215);
          if (selectedTextContext != null)
            selectedTextContext.outlineColor = parse.getRgb();
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
          customValueTextField.setEditableColor(16777215);
          floatButtonWidget.setValue(value);
        } catch (
            NumberFormatException e) {
          customValueTextField.setEditableColor(16733525);
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
    clearAndInit();
  }

  @Override
  public void removed() {
    super.removed();
    entity.setEditor(null);
    final NbtList list = new NbtList();
    for (TextContext textContext : textContextsEditing) {
      list.add(textContext.createNbt(registryLookup));
    }
    ClientPlayNetworking.send(new SignEditFinishPayload(blockPos, changed ? Util.make(new NbtCompound(), nbt -> nbt.put("texts", list)) : null));
  }

  @Override
  protected void clearAndInit() {
    final int previouslyFocusedTextIndex = textFieldListWidget == null ? -1 : textFieldListWidget.children().indexOf(textFieldListWidget.getFocused());
    final double scrollAmountBeforeClear = textFieldListWidget == null ? -1 : textFieldListWidget.getScrollAmount();
    super.clearAndInit();
    if (textFieldListWidget != null && previouslyFocusedTextIndex >= 0 && textFieldListWidget.children().size() > previouslyFocusedTextIndex) {
      textFieldListWidget.setFocused(textFieldListWidget.children().get(previouslyFocusedTextIndex));
      textFieldListWidget.setScrollAmount(scrollAmountBeforeClear);
    }
  }

  @Override
  public boolean charTyped(char chr, int modifiers) {
    if (getFocused() instanceof TextFieldWidget || getFocused() instanceof TextFieldListWidget) {
      return getFocused().charTyped(chr, modifiers);
    } else {
      return textFieldListWidget.charTyped(chr, modifiers);
    }
  }

  @Override
  public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
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
      } else if (keyCode == GLFW.GLFW_KEY_E && !isAcceptingCustomValue) {
        final Element focused = getFocused();
        if (focused instanceof FloatButtonWidget floatButtonWidget && focused != horizontalAlignButton && focused != verticalAlignButton) {
          customValueStartAccepting(floatButtonWidget);
        } else {
          setCustomValueButton.onPress();
        }
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
      addTextField(textFieldListWidget.children().size());
      return true;
    }
    return super.keyPressed(keyCode, scanCode, modifiers);
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
