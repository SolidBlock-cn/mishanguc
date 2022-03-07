package pers.solid.mishang.uc.screen;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.Element;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.ScreenTexts;
import net.minecraft.client.gui.screen.narration.NarrationMessageBuilder;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.ClickableWidget;
import net.minecraft.client.gui.widget.EntryListWidget;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtList;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Text;
import net.minecraft.text.TextColor;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.DyeColor;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;
import net.minecraft.util.Util;
import net.minecraft.util.math.BlockPos;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.Unmodifiable;
import pers.solid.mishang.uc.MishangUtils;
import pers.solid.mishang.uc.blockentity.BlockEntityWithText;
import pers.solid.mishang.uc.util.HorizontalAlign;
import pers.solid.mishang.uc.util.TextContext;
import pers.solid.mishang.uc.util.VerticalAlign;

import java.util.Arrays;
import java.util.List;
import java.util.ListIterator;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.BooleanSupplier;
import java.util.function.Supplier;

/**
 * 编辑告示牌时的屏幕。<br>
 * 放置后如需打开此屏幕，使用
 *
 * <pre>{@code
 * this.client.setScreen();(new TextPadEditScreen(entity)
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
  private static final TranslatableText REARRANGE_SUCCESS_NOTICE =
      new TranslatableText("message.mishanguc.rearrange.success");
  private static final TranslatableText BUTTON_CLEAR_MESSAGE =
      new TranslatableText("message.mishanguc.clear");
  private static final TranslatableText BUTTON_CLEAR_CONFIRM_MESSAGE =
      new TranslatableText("message.mishanguc.clear.confirm");
  private static final TranslatableText BUTTON_CLEAR_CONFIRM_DESCRIPTION_MESSAGE =
      new TranslatableText("message.mishanguc.clear.confirm.description");
  private static final TranslatableText BUTTON_CLEAR_DESCRIPTION_MESSAGE =
      new TranslatableText("message.mishanguc.clear.description");
  public final BlockPos blockPos;
  public final List<TextContext> textContextsEditing;
  /**
   * 描述文本。悬浮在按钮时就会显示。
   */
  @ApiStatus.AvailableSince("0.1.6")
  public final AtomicReference<@Unmodifiable Text> descriptionAtom =
      new AtomicReference<>(LiteralText.EMPTY);

  public final T entity;

  protected final BiMap<@NotNull TextContext, @NotNull TextFieldWidget> contextToWidgetBiMap =
      HashBiMap.create();
  /**
   * 是否发生了改变。如果改变了，则提交时发送完整内容，否则发送空 NBT 表示未做更改。
   */
  public boolean changed = false;

  public TextFieldListScreen textFieldListScreen;

  /**
   * 正在被选中的 TextWidget。会在 {@link #setFocused(Element)} 时更改。可能为 null。
   */
  public @Nullable TextFieldWidget focusedTextField = null;

  /**
   * 正在被选中的 TextContent。会在 {@link #setFocused(Element)} 时更改。可能为 null。不是副本。
   */
  public @Nullable TextContext focusedTextContext = null;


  /*
  ===== 上方第一行 =====
   */

  /**
   * 上方第一行：添加文本按钮
   */
  public final ButtonWidget addTextButton = new ButtonWidget(width / 2 - 120 - 100, 10, 200, 20, new TranslatableText("message.mishanguc.add_text"), button1 -> {
    int index = textFieldListScreen.children().indexOf(textFieldListScreen.new Entry(focusedTextField));
    addTextField(index == -1 ? textFieldListScreen.children().size() : index + 1);
  });

  /**
   * 上方第一行：移除文本按钮
   */
  public final ButtonWidget removeTextButton = new ButtonWidget(width / 2 + 120 - 100, 10, 200, 20, new TranslatableText("message.mishanguc.remove_text"), button -> {
    int index = textFieldListScreen.children().indexOf(textFieldListScreen.new Entry(focusedTextField));
    if (index != -1) {
      removeTextField(index);
    }
  });



  /*
   ===== 文本区域列表部分 =====
   */

  /**
   * 没有添加文本时，显示的一条“点击此处添加文本”的消息。文本添加后，该按钮将消失。
   */
  public final ButtonWidget placeHolder = new ButtonWidget(0, 30, 200, 20, new TranslatableText("message.mishanguc.add_first_text"), button -> {
    addTextField(0);
    setFocused(textFieldListScreen);
    textFieldListScreen.setFocused(textFieldListScreen.children().get(0));
  });

  @ApiStatus.AvailableSince("0.1.6")
  public final ButtonWidget applyDoubleLineTemplateButton = new ButtonWidget(width / 2 - 50, 70, 120, 20, new TranslatableText("message.mishanguc.apply_double_line_template"), button -> {
    addTextField(0, AbstractSignBlockEditScreen.this.entity.getDefaultTextContext(), false);
    addTextField(1, Util.make(AbstractSignBlockEditScreen.this.entity.getDefaultTextContext(), textContext -> textContext.size /= 2), false);
    textFieldListScreen.setFocused(textFieldListScreen.children().get(0));
    rearrange();
  });

  @ApiStatus.AvailableSince("0.1.6")
  public final ButtonWidget applyLeftArrowTemplateButton = new ButtonWidget(width / 2 - 150, 70, 120, 20, new TranslatableText("message.mishanguc.apply_left_arrow_template"), (ButtonWidget button) -> {
    BlockEntityWithText entity = AbstractSignBlockEditScreen.this.entity;
    final TextContext textContext0 = entity.getDefaultTextContext();
    textContext0.text = new LiteralText("←");
    textContext0.size = 8;
    textContext0.offsetX = -4;
    textContext0.absolute = true;
    AbstractSignBlockEditScreen.this.addTextField(0, textContext0, false);
    final TextContext textContext1 = entity.getDefaultTextContext();
    textContext1.offsetX = 8;
    textContext1.horizontalAlign = HorizontalAlign.LEFT;
    AbstractSignBlockEditScreen.this.addTextField(1, textContext1, false);
    final TextContext textContext2 = entity.getDefaultTextContext();
    textContext2.offsetX = 8;
    textContext2.horizontalAlign = HorizontalAlign.LEFT;
    textContext2.size /= 2;
    AbstractSignBlockEditScreen.this.addTextField(2, textContext2, false);
    textFieldListScreen.setFocused(textFieldListScreen.children().get(1));
    rearrange();
  });

  @ApiStatus.AvailableSince("0.1.6")
  public final ButtonWidget applyRightArrowTemplateButton = new ButtonWidget(width / 2 - 50, 70, 120, 20, new TranslatableText("message.mishanguc.apply_right_arrow_template"), (ButtonWidget button) -> {
    BlockEntityWithText entity = AbstractSignBlockEditScreen.this.entity;
    final TextContext textContext0 = entity.getDefaultTextContext();
    textContext0.text = new LiteralText("→");
    textContext0.size = 8;
    textContext0.offsetX = 4;
    textContext0.absolute = true;
    AbstractSignBlockEditScreen.this.addTextField(0, textContext0, false);
    final TextContext textContext1 = entity.getDefaultTextContext();
    textContext1.offsetX = -8;
    textContext1.horizontalAlign = HorizontalAlign.RIGHT;
    AbstractSignBlockEditScreen.this.addTextField(1, textContext1, false);
    final TextContext textContext2 = entity.getDefaultTextContext();
    textContext2.offsetX = -8;
    textContext2.horizontalAlign = HorizontalAlign.RIGHT;
    textContext2.size /= 2;
    AbstractSignBlockEditScreen.this.addTextField(2, textContext2, false);
    textFieldListScreen.setFocused(textFieldListScreen.children().get(1));
    rearrange();
  });


  /*
  ===== 下方第一行 =====
   */

  /**
   * 下方第一行：加粗按钮。
   */
  public final BooleanButtonWidget boldButton = new BooleanButtonWidget(this.width / 2 - 200, this.height - 50, 20, 20, new LiteralText("B").formatted(Formatting.BOLD), button -> focusedTextContext == null ? null : focusedTextContext.bold, b -> {
    changed = true;
    if (focusedTextContext != null) focusedTextContext.bold = b;
  }, b -> b == null ? new TranslatableText("message.mishanguc.bold") : new TranslatableText("message.mishanguc.bold.description", new TranslatableText(b ? "options.on" : "options.off")), button -> {
  }, descriptionAtom);

  /**
   * 下方第一行：斜体按钮。
   */
  public final BooleanButtonWidget italicButton = new BooleanButtonWidget(this.width / 2 - 180, this.height - 50, 20, 20, new LiteralText("I").formatted(Formatting.ITALIC), button -> focusedTextContext == null ? null : focusedTextContext.italic, b -> {
    changed = true;
    if (focusedTextContext != null) focusedTextContext.italic = b;
  }, b -> b == null ? new TranslatableText("message.mishanguc.italic") : new TranslatableText("message.mishanguc.italic.description", new TranslatableText(b ? "options.on" : "options.off")), button -> {
  }, descriptionAtom);

  /**
   * 下方第一行：下划线按钮。
   */
  public final BooleanButtonWidget underlineButton = new BooleanButtonWidget(this.width / 2 - 160, this.height - 50, 20, 20, new LiteralText("U").formatted(Formatting.UNDERLINE), button -> focusedTextContext == null ? null : focusedTextContext.underline, b -> {
    changed = true;
    if (focusedTextContext != null) focusedTextContext.underline = b;
  }, b -> b == null ? new TranslatableText("message.mishanguc.underline") : new TranslatableText("message.mishanguc.underline.description", new TranslatableText(b ? "options.on" : "options.off")), button -> {
  }, descriptionAtom);

  /**
   * 下方第一行：删除线按钮。
   */
  public final BooleanButtonWidget strikethroughButton = new BooleanButtonWidget(this.width / 2 - 140, this.height - 50, 20, 20, new LiteralText("S").formatted(Formatting.STRIKETHROUGH), button -> focusedTextContext == null ? null : focusedTextContext.strikethrough, b -> {
    changed = true;
    if (focusedTextContext != null) focusedTextContext.strikethrough = b;
  }, b -> b == null ? new TranslatableText("message.mishanguc.strikethrough") : new TranslatableText("message.mishanguc.strikethrough.description", new TranslatableText(b ? "options.on" : "options.off")), button -> {
  }, descriptionAtom);

  /**
   * 下方第一行：随机文字（obfuscated）按钮。
   */
  public final BooleanButtonWidget obfuscatedButton = new BooleanButtonWidget(this.width / 2 - 120, this.height - 50, 20, 20, new LiteralText("O").formatted(Formatting.OBFUSCATED), button -> focusedTextContext == null ? null : focusedTextContext.obfuscated, b -> {
    changed = true;
    if (focusedTextContext != null) focusedTextContext.obfuscated = b;
  }, b -> b == null ? new TranslatableText("message.mishanguc.obfuscated") : new TranslatableText("message.mishanguc.obfuscated.description", new TranslatableText(b ? "options.on" : "options.off")), button -> {
  }, descriptionAtom);


  /**
   * 下方第一行：阴影按钮。
   */
  public final BooleanButtonWidget shadeButton = new BooleanButtonWidget(this.width / 2 - 100, this.height - 50, 40, 20, new TranslatableText("message.mishanguc.shade"), button -> focusedTextContext == null ? null : focusedTextContext.shadow, b -> {
    changed = true;
    if (focusedTextContext != null) focusedTextContext.shadow = b;
  }, b -> b == null ? new TranslatableText("message.mishanguc.shade.description") : new TranslatableText("message.mishanguc.shade.param", new TranslatableText(b ? "options.on" : "options.off")), button -> {
  }, descriptionAtom);

  /**
   * 下方第一行：文本大小按钮。
   */
  public final FloatButtonWidget sizeButton = new FloatButtonWidget(this.width / 2 - 60, this.height - 50, 40, 20, new TranslatableText("message.mishanguc.size"), x -> new TranslatableText("message.mishanguc.size.description", x), (buttons) -> focusedTextContext != null ? focusedTextContext.size : 0, value -> {
    changed = true;
    if (focusedTextContext != null) {
      focusedTextContext.size = value;
    }
  }, (button) -> {
  }, descriptionAtom);

  /**
   * 下方第一行：X偏移。
   */
  public final FloatButtonWidget offsetXButton = new FloatButtonWidget(this.width / 2 - 10, this.height - 50, 40, 20, new TranslatableText("message.mishanguc.offsetX"), x -> new TranslatableText("message.mishanguc.offsetX.description", x), button -> focusedTextContext != null ? focusedTextContext.offsetX : 0, value -> {
    changed = true;
    if (focusedTextContext != null) {
      focusedTextContext.offsetX = value;
    }
  }, (button) -> {
  }, descriptionAtom);

  /**
   * 下方第一行：Y偏移。
   */
  public final FloatButtonWidget offsetYButton = new FloatButtonWidget(this.width / 2 + 40, this.height - 50, 40, 20, new TranslatableText("message.mishanguc.offsetY"), x -> new TranslatableText("message.mishanguc.offsetY.description", x), button -> focusedTextContext != null ? focusedTextContext.offsetY : 0, value -> {
    changed = true;
    if (focusedTextContext != null) {
      focusedTextContext.offsetY = value;
    }
  }, (button) -> {
  }, descriptionAtom);

  /**
   * 下方第一行：Z偏移。
   */
  public final FloatButtonWidget offsetZButton = new FloatButtonWidget(this.width / 2 + 40, this.height - 50, 40, 20, new TranslatableText("message.mishanguc.offsetZ"), x -> new TranslatableText("message.mishanguc.offsetZ.description", x), button -> focusedTextContext != null ? focusedTextContext.offsetZ : 0, value -> {
    changed = true;
    if (focusedTextContext != null) {
      focusedTextContext.offsetZ = value;
    }
  }, (button) -> {
  }, descriptionAtom);

  /**
   * 下方第一行：颜色。
   *
   * @see #customColorTextField
   */
  public final FloatButtonWidget colorButton = new FloatButtonWidget(0, 0, 40, 20, new TranslatableText("message.mishanguc.color"), colorId -> {
    if (colorId == -1) {
      return new TranslatableText("message.mishanguc.color");
    } else if (colorId == -2 && focusedTextContext != null) {
      return new TranslatableText("message.mishanguc.color.param", new LiteralText(String.format("#%06x", focusedTextContext.color)).styled(style -> style.withColor(TextColor.fromRgb(focusedTextContext.color))))
          ;
    }
    final DyeColor dyeColor = DyeColor.byId((int) colorId);
    return new TranslatableText(
        "message.mishanguc.color.param",
        new TranslatableText("color.minecraft." + dyeColor.asString())
            .styled(style -> style.withColor(TextColor.fromRgb(dyeColor.getSignColor()))));
  }, button -> {
    changed = true;
    if (focusedTextContext == null) {
      ((FloatButtonWidget) button).active = false;
      return -1;
    }
    final DyeColor dyeColor = MishangUtils.colorBySignColor(focusedTextContext.color);
    if (dyeColor == null) {
      ((FloatButtonWidget) button).active = false;
      return -2;
    } else {
      ((FloatButtonWidget) button).active = true;
      return dyeColor.getId();
    }
  }, colorId -> {
    if (focusedTextContext != null) {
      focusedTextContext.color = DyeColor.byId((int) colorId).getSignColor();
      this.customColorTextField.setText(String.format("#%06x", focusedTextContext.color));
    }
  }, (button) -> {
  }, descriptionAtom);

  /**
   * 下方第一行：设置自定义颜色。<br>
   * 因为 {@link #textRenderer} 暂时还是 {@code null} 得在 {@link #init(MinecraftClient, int, int)}
   * 中赋值，所以这里直接使用 {@code MinecraftClient.getInstance().textRenderer}。 <br>
   * （其实我觉得应该在 {@code init} 里面通过 mixin 修改该字段的 textRender，不过意义其实不大吧。qwq
   *
   * @see #colorButton
   */
  public final TextFieldWidget customColorTextField = Util.make(new TextFieldWidget(MinecraftClient.getInstance().textRenderer, 0, 0, 50, 20, new TranslatableText("message.mishanguc.custom_color")), widget ->
      widget.setChangedListener(
          s -> {
            changed = true;
            if (focusedTextContext != null) {
              final TextColor parsedColor = TextColor.parse(widget.getText());
              if (parsedColor != null) focusedTextContext.color = parsedColor.getRgb();
            }
          }));

  /**
   * 下方第一行：描边颜色。
   */
  @ApiStatus.AvailableSince("0.1.6-mc1.17")
  public final FloatButtonWidget outlineColorButton = new FloatButtonWidget(0, 0, 60, 20, new TranslatableText("message.mishanguc.outline_color"), colorId -> {
    // colorId=-1：表示当前自动根据文本内容绘制描边。
    // colorId=-2：表示当前不绘制描边（默认）。
    // colorId=-3：表示是自定义的。
    // colorId=-4：表示当前没有选中文本。
    // colorId=0-15：标准颜色。
    // colorId=16-31：描边颜色
    if (colorId == -1) {
      return (new TranslatableText("message.mishanguc.outline_color.auto"));
    } else if (colorId == -2) {
      return (new TranslatableText("message.mishanguc.outline_color.none"));
    } else if (colorId == -3 && focusedTextContext != null) {
      return (new TranslatableText("message.mishanguc.outline_color.param", String.format("#%06x", focusedTextContext.outlineColor)).styled(style -> style.withColor(TextColor.fromRgb(focusedTextContext.color))));
    } else if (colorId == -4) {
      return (new TranslatableText("message.mishanguc.outline_color"));
    } else if (colorId > 15) {
      final DyeColor color = DyeColor.byId((int) colorId - 16);
      return new TranslatableText("message.mishanguc.outline_color.param", new TranslatableText("message.mishanguc.outline_color.relate", new TranslatableText("message.mishanguc.outline_color.relate.$1")
          .styled(style -> style.withColor(MishangUtils.COLOR_TO_OUTLINE_COLOR.get(color))), new TranslatableText("color.minecraft." + color.asString()).styled(style -> style.withColor(color.getSignColor()))));
    } else {
      final DyeColor color = DyeColor.byId((int) colorId);
      if (color == null) return new TranslatableText("message.mishanguc.outline_color.none");
      return new TranslatableText("message.mishanguc.outline_color.param", new TranslatableText("color.minecraft." + color.asString()).styled(style -> style.withColor(color.getSignColor())));
    }
  }, button -> {
    if (focusedTextContext == null) {
      ((ClickableWidget) button).active = false;
      return -4;
    }
    if (focusedTextContext.outlineColor == -1) {
      ((ClickableWidget) button).active = true;
      return -1;
    } else if (focusedTextContext.outlineColor == -2) {
      ((ClickableWidget) button).active = true;
      return -2;
    }
    final DyeColor colorOutline = MishangUtils.COLOR_TO_OUTLINE_COLOR.inverse().get(focusedTextContext.outlineColor);
    if (colorOutline != null) {
      ((ClickableWidget) button).active = true;
      return colorOutline.getId() + 16;
    }
    final DyeColor color = MishangUtils.colorBySignColor(focusedTextContext.outlineColor);
    if (color != null) {
      ((ClickableWidget) button).active = true;
      return color.getId();
    } else {
      ((ClickableWidget) button).active = false;
      return -3;
    }
  }, colorId -> {
    changed = true;
    if (focusedTextContext != null) {
      if (colorId == -1 || colorId == -2) {
        focusedTextContext.outlineColor = (int) colorId;
      } else if (colorId > 15) {
        focusedTextContext.outlineColor = MishangUtils.COLOR_TO_OUTLINE_COLOR.get(DyeColor.byId((int) colorId - 16));
      } else {
        focusedTextContext.outlineColor = DyeColor.byId((int) colorId).getSignColor();
      }
    }
  }, button -> {
  }, descriptionAtom);


  /*
  ==== 下方第二行 ====
   */

  /**
   * 下方第二行：X旋转。
   */
  @ApiStatus.AvailableSince("0.1.6")
  public final FloatButtonWidget rotationXButton = new FloatButtonWidget(this.width / 2 + 40, this.height - 50, 40, 20, new TranslatableText("message.mishanguc.rotationX"), x -> new TranslatableText("message.mishanguc.rotationX.description", x), button -> focusedTextContext != null ? focusedTextContext.rotationX : 0, value -> {
    changed = true;
    if (focusedTextContext != null) focusedTextContext.rotationX = value;
  }, button -> {
  }, descriptionAtom);

  /**
   * 下方第二行：Y旋转。
   */
  @ApiStatus.AvailableSince("0.1.6")
  public final FloatButtonWidget rotationYButton = new FloatButtonWidget(this.width / 2 + 40, this.height - 50, 40, 20, new TranslatableText("message.mishanguc.rotationY"), x -> new TranslatableText("message.mishanguc.rotationY.description", x), button -> focusedTextContext != null ? focusedTextContext.rotationY : 0, value -> {
    changed = true;
    if (focusedTextContext != null) focusedTextContext.rotationY = value;
  }, button -> {
  }, descriptionAtom);

  /**
   * 下方第二行：Z旋转。
   */
  @ApiStatus.AvailableSince("0.1.6")
  public final FloatButtonWidget rotationZButton = new FloatButtonWidget(this.width / 2 + 40, this.height - 50, 40, 20, new TranslatableText("message.mishanguc.rotationZ"), x -> new TranslatableText("message.mishanguc.rotationZ.description", x), button -> focusedTextContext != null ? focusedTextContext.rotationZ : 0, value -> {
    changed = true;
    if (focusedTextContext != null) focusedTextContext.rotationZ = value;
  }, button -> {
  }, descriptionAtom);

  /**
   * 下方第二行：X缩放。
   */
  public final FloatButtonWidget scaleXButton = new FloatButtonWidget(this.width / 2 + 90, this.height - 50, 40, 20, new TranslatableText("message.mishanguc.scaleX"), x -> new TranslatableText("message.mishanguc.scaleX.description", x), button -> focusedTextContext != null ? focusedTextContext.scaleX : 1, value -> {
    changed = true;
    if (focusedTextContext != null) {
      focusedTextContext.scaleX = value;
    }
  }, (button) -> {
  }, descriptionAtom);

  /**
   * 下方第二行：Y缩放。
   */
  public final FloatButtonWidget scaleYButton = new FloatButtonWidget(this.width / 2 + 140, this.height - 50, 40, 20, new TranslatableText("message.mishanguc.scaleY"), x -> new TranslatableText("message.mishanguc.scaleY.description", x), button -> focusedTextContext != null ? focusedTextContext.scaleY : 1, value -> {
    changed = true;
    if (focusedTextContext != null) {
      focusedTextContext.scaleY = value;
    }
  }, (button) -> {
  }, descriptionAtom);

  /**
   * 下方第二行：水平对齐方式。
   */
  public final FloatButtonWidget horizontalAlignButton = new FloatButtonWidget(0, 0, 50, 20, new TranslatableText("message.mishanguc.horizontal_align"), f -> f != -1 ? new TranslatableText("message.mishanguc.horizontal_align_param", HorizontalAlign.values()[(int) f].getName()) : new TranslatableText("message.mishanguc.horizontal_align"), b -> focusedTextContext != null ? focusedTextContext.horizontalAlign.ordinal() : -1, f -> {
    if (focusedTextContext != null) {
      focusedTextContext.horizontalAlign = HorizontalAlign.values()[(int) f];
    }
  }, (b) -> {
  }, descriptionAtom);

  /**
   * 下方第二行：垂直对齐方式。
   */
  public final FloatButtonWidget verticalAlignButton = new FloatButtonWidget(0, 0, 50, 20, new TranslatableText("message.mishanguc.vertical_align"), f -> f != -1 ? new TranslatableText("message.mishanguc.vertical_align_param", VerticalAlign.values()[(int) f].getName()) : new TranslatableText("message.mishanguc.vertical_align"), b -> focusedTextContext != null ? focusedTextContext.verticalAlign.ordinal() : -1, f -> {
    if (focusedTextContext != null) {
      focusedTextContext.verticalAlign = VerticalAlign.values()[(int) f];
    }
  }, (b) -> {
  }, descriptionAtom);

  /**
   * 下方第二行：切换文字是否可以看穿。
   */
  public final BooleanButtonWidget seeThroughButton = new BooleanButtonWidget(0, 0, 60, 20, new TranslatableText("message.mishanguc.see_through"), button -> focusedTextContext == null ? null : focusedTextContext.seeThrough, b -> {
    changed = true;
    if (focusedTextContext != null) focusedTextContext.seeThrough = b;
  }, b -> b == null ? new TranslatableText("message.mishanguc.see_through") : new TranslatableText("message.mishanguc.see_through.param", new TranslatableText(b ? "options.on" : "options.off")), button -> {
  }, descriptionAtom);

  /**
   * 下方第二行：绝对模式。
   */
  public final BooleanButtonWidget absoluteButton = new BooleanButtonWidget(0, 0, 50, 20, new TranslatableText("message.mishanguc.absolute"), button -> focusedTextContext != null ? focusedTextContext.absolute : null, b -> {
    changed = true;
    if (focusedTextContext != null) focusedTextContext.absolute = b;
  }, b -> b == null ? new TranslatableText("message.mishanguc.absolute.description") : new TranslatableText("message.mishanguc.absolute.param", new TranslatableText(b ? "options.on" : "options.off")), button -> {
  }, descriptionAtom);


  public final ClickableWidget[] toolbox1 = new ClickableWidget[]{boldButton, italicButton, underlineButton, strikethroughButton, obfuscatedButton, shadeButton, sizeButton, offsetXButton, offsetYButton, offsetZButton, colorButton, customColorTextField, outlineColorButton};
  public final ClickableWidget[] toolbox2 = new ClickableWidget[]{rotationXButton, rotationYButton, rotationZButton, scaleXButton, scaleYButton, horizontalAlignButton, verticalAlignButton, seeThroughButton, absoluteButton};


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
    offsetYButton.step = 0.5f;
    scaleXButton.step = -0.125f;
    scaleXButton.defaultValue = 1;
    scaleYButton.step = -0.125f;
    scaleYButton.defaultValue = 1;
    sizeButton.min = 0;
    sizeButton.step = -0.5f;
    horizontalAlignButton.defaultValue = 1;
    verticalAlignButton.defaultValue = 1;
  }


  /*
  ===== 下方第三行 =====
   */

  /**
   * 下方第三行：上移按钮。
   */
  public final ButtonWidget moveUpButton = new ButtonWidget(this.width - 20, this.height - 50, 30, 20, new TranslatableText("message.mishanguc.moveUp"), button -> {
    if (focusedTextField == null) return;
    int i = textFieldListScreen.children().indexOf(textFieldListScreen.getFocused());
    final TextContext textContext = AbstractSignBlockEditScreen.this.textContextsEditing.get(i);
    removeTextField(i);
    if (i > 0) i--;
    addTextField(i, textContext, false);
  }, (button, matrices, mouseX, mouseY) -> descriptionAtom.set(new TranslatableText("message.mishanguc.moveUp.description")));

  /**
   * 下方第三行：下移按钮。
   */
  public final ButtonWidget moveDownButton = new ButtonWidget(this.width - 20, this.height - 50, 30, 20, new TranslatableText("message.mishanguc.moveDown"), button -> {
    if (focusedTextField == null) return;
    int i = textFieldListScreen.children().indexOf(textFieldListScreen.getFocused());
    final TextContext textContext = AbstractSignBlockEditScreen.this.textContextsEditing.get(i);
    removeTextField(i);
    if (i < textFieldListScreen.children().size()) i++;
    addTextField(i, textContext, false);
  }, (button, matrices, mouseX, mouseY) -> descriptionAtom.set(new TranslatableText("message.mishanguc.moveDown.description")));

  /**
   * 下方第三行：重排按钮。
   */
  public final ButtonWidget rearrangeButton = new ButtonWidget(this.width / 2 + 190, this.height - 50, 70, 20, new TranslatableText("message.mishanguc.rearrange"), button -> {
    rearrange();
    descriptionAtom.set(REARRANGE_SUCCESS_NOTICE);
  }, (a, b, c, d) -> {
    if (descriptionAtom.get() != REARRANGE_SUCCESS_NOTICE)
      descriptionAtom.set(new TranslatableText("message.mishanguc.rearrange.tooltip"));
  });

  /**
   * 下方第三行：完成编辑按钮。
   */
  public final ButtonWidget finishButton = new ButtonWidget(this.width / 2 - 100, this.height - 30, 120, 20, ScreenTexts.DONE, buttonWidget -> this.finishEditing(), (button, matrices, mouseX, mouseY) ->
      descriptionAtom.set(new TranslatableText("message.mishanguc.finish.description")));

  /**
   * 下方第三行：取消编辑按钮。
   */
  public final ButtonWidget cancelButton = new ButtonWidget(this.width / 2, height - 30, 40, 20, ScreenTexts.CANCEL, button -> this.cancelEditing(), (button, matrices, mouseX, mouseY) ->
      descriptionAtom.set(new TranslatableText("message.mishanguc.cancel.description")));

  /**
   * 下方第三行：清除所有文本的按钮。
   */
  public final ButtonWidget clearButton = new ButtonWidget(this.width / 2, this.height - 50, 50, 20, BUTTON_CLEAR_MESSAGE, button -> {
    if (button.getMessage() == BUTTON_CLEAR_CONFIRM_MESSAGE) {
      for (int i = AbstractSignBlockEditScreen.this.textFieldListScreen.children().size() - 1; i >= 0; i--) {
        removeTextField(i);
      }
      button.setMessage(BUTTON_CLEAR_MESSAGE);
      descriptionAtom.set(BUTTON_CLEAR_DESCRIPTION_MESSAGE);
    } else {
      // 要求用户再次点击一次按钮才能删除。
      button.setMessage(BUTTON_CLEAR_CONFIRM_MESSAGE);
      descriptionAtom.set(BUTTON_CLEAR_CONFIRM_DESCRIPTION_MESSAGE);
    }
  }, (button, matrices, mouseX, mouseY) -> {
    if (button.getMessage() == BUTTON_CLEAR_CONFIRM_MESSAGE)
      descriptionAtom.set(BUTTON_CLEAR_CONFIRM_DESCRIPTION_MESSAGE);
    else descriptionAtom.set(BUTTON_CLEAR_DESCRIPTION_MESSAGE);
  });


  public AbstractSignBlockEditScreen(T entity, BlockPos blockPos, List<TextContext> textContextsEditing) {
    super(new TranslatableText("message.mishanguc.sign_edit"));
    this.entity = entity;
    this.blockPos = blockPos;
    this.textContextsEditing = textContextsEditing;
    entity.setEditor(this.client != null ? this.client.player : null);
    sizeButton.defaultValue = entity.getDefaultTextContext().size;
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
    textFieldListScreen = new TextFieldListScreen(client, width, height, 30, height - 80, 18);
    setFocused(textFieldListScreen);
    // 添加按钮
    /// 文本列表屏幕以及占位符
    this.addDrawableChild(placeHolder);
    this.addDrawableChild(applyDoubleLineTemplateButton);
    this.addDrawableChild(applyLeftArrowTemplateButton);
    this.addDrawableChild(applyRightArrowTemplateButton);
    this.addDrawableChild(textFieldListScreen);

    /// 上方第一行
    this.addDrawableChild(addTextButton);
    this.addDrawableChild(removeTextButton);

    /// 下方第一行和第二行
    Arrays.stream(toolbox1).forEach(this::addDrawableChild);
    Arrays.stream(toolbox2).forEach(this::addDrawableChild);

    /// 下方第三行
    this.addDrawableChild(moveUpButton);
    this.addDrawableChild(moveDownButton);
    this.addDrawableChild(rearrangeButton);
    this.addDrawableChild(finishButton);
    this.addDrawableChild(cancelButton);
    this.addDrawableChild(clearButton);

    // 添加文本框
    for (int i = 0, textContextsEditingSize = textContextsEditing.size();
         i < textContextsEditingSize;
         i++) {
      TextContext textContext = textContextsEditing.get(i);
      addTextField(i, textContext, true);
    }

    arrangeToolboxButtons();

    addTextButton.x = width / 2 - 220;
    removeTextButton.x = width / 2 + 20;
    moveUpButton.x = width / 2 - 190;
    moveUpButton.y = height - 25;
    rearrangeButton.x = width / 2 - 160;
    rearrangeButton.y = height - 25;
    moveDownButton.x = width / 2 - 90;
    moveDownButton.y = height - 25;
    finishButton.x = width / 2 - 60;
    finishButton.y = height - 25;
    cancelButton.x = width / 2 + 60;
    cancelButton.y = height - 25;
    clearButton.x = width / 2 + 100;
    clearButton.y = height - 25;
    placeHolder.x = width / 2 - 100;
    applyDoubleLineTemplateButton.x = width / 2 - 60;
    applyLeftArrowTemplateButton.x = width / 2 - 180;
    applyRightArrowTemplateButton.x = width / 2 + 60;
  }

  @Override
  public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
    descriptionAtom.set(LiteralText.EMPTY);
    super.render(matrices, mouseX, mouseY, delta);
    final Text description = descriptionAtom.get();
    textRenderer.draw(
        matrices,
        description,
        width / 2f - textRenderer.getWidth(description) / 2f,
        height - 75,
        0xeeeeee);
    if (placeHolder.visible) {
      final TranslatableText text0 = new TranslatableText("message.mishanguc.or");
      textRenderer.draw(
          matrices,
          text0,
          width / 2f - textRenderer.getWidth(text0) / 2f,
          60,
          0xdddddd);
    }
  }

  /**
   * 添加一个新的文本框，并将其添加到 {@link #contextToWidgetBiMap} 中。
   *
   * @param index 添加到的位置，对应在数组或列表中的次序。
   */
  public void addTextField(int index) {
    addTextField(index, entity.getDefaultTextContext(), false);
  }

  /**
   * 添加一个文本框，并将其添加到 {@link #contextToWidgetBiMap} 中。
   *
   * @param index       添加到的位置，对应在数组或列表中的次序。
   * @param textContext 需要添加的 {@link TextContext}。
   * @param isExisting  是否为现有的，如果是，则不会将这个 textContext 添加到 {@link #textContextsEditing} 中，也不会将 {@link
   *                    #changed} 设为 <code>true</code>。
   */
  public void addTextField(int index, @NotNull TextContext textContext, boolean isExisting) {
    if (!isExisting) {
      textContextsEditing.add(index, textContext);
      changed = true;
    }
    final TextFieldWidget textFieldWidget =
        new TextFieldWidget(
            textRenderer,
            2,
            height / 4,
            width - 4,
            15,
            new TranslatableText("message.mishanguc.text_field"));
    textFieldWidget.setMaxLength(Integer.MAX_VALUE);
    if (textContext.text != null) {
      textFieldWidget.setText(textContext.text.asString());
    }
    final TextFieldListScreen.Entry newEntry = textFieldListScreen.new Entry(textFieldWidget);
    textFieldListScreen.children().add(index, newEntry);
    contextToWidgetBiMap.put(textContext, textFieldWidget);
    textFieldListScreen.setFocused(newEntry);
    textFieldWidget.setChangedListener(
        s -> {
          final TextContext textContext1 = contextToWidgetBiMap.inverse().get(textFieldWidget);
          if (textContext1 != null) {
            textContext1.text = new LiteralText(s);
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
    int belowToolboxWidth = 0;
    for (ClickableWidget widget : toolbox1) {
      final int width = widget.getWidth();
      widget.x = belowToolboxWidth;
      belowToolboxWidth += width;
    }
    for (ClickableWidget widget : toolbox1) {
      widget.visible = true;
      widget.x += width / 2 - belowToolboxWidth / 2;
      widget.y = height - 65;
    }
    belowToolboxWidth = 0;
    for (ClickableWidget widget : toolbox2) {
      final int width = widget.getWidth();
      widget.x = belowToolboxWidth;
      belowToolboxWidth += width;
    }
    for (ClickableWidget widget : toolbox2) {
      widget.visible = true;
      widget.x += width / 2 - belowToolboxWidth / 2;
      widget.y = height - 45;
    }
  }

  /**
   * 移除一个文本框。将从 {@link #contextToWidgetBiMap} 和 {@link #textContextsEditing} 中移除对应的元素。
   *
   * @param index 移除的文本框的位置。
   * @see #addTextField(int, TextContext, boolean)
   */
  public void removeTextField(int index) {
    final TextFieldWidget removedWidget =
        textFieldListScreen.children().remove(index).textFieldWidget;
    final TextContext removedTextContext = contextToWidgetBiMap.inverse().get(removedWidget);
    if (textFieldListScreen.getFocused() != null
        && removedWidget == textFieldListScreen.getFocused().textFieldWidget) {
      textFieldListScreen.setFocused(null);
    }
    if (textFieldListScreen.children().size() > index) {
      textFieldListScreen.setFocused(textFieldListScreen.children().get(index));
    } else if (textFieldListScreen.children().size() > index - 1 && index - 1 >= 0) {
      textFieldListScreen.setFocused(textFieldListScreen.children().get(index - 1));
    }
    textContextsEditing.remove(removedTextContext);
    contextToWidgetBiMap.remove(removedTextContext);
    placeHolder.visible = textFieldListScreen.children().size() == 0;
    applyDoubleLineTemplateButton.visible = placeHolder.visible;
    applyLeftArrowTemplateButton.visible = placeHolder.visible;
    applyRightArrowTemplateButton.visible = placeHolder.visible;
    changed = true;
  }

  @Override
  public void removed() {
    super.removed();
    entity.setEditor(null);
    final NbtList list = new NbtList();
    for (TextContext textContext : textContextsEditing) {
      list.add(textContext.writeNbt(new NbtCompound()));
    }
    ClientPlayNetworking.send(
        new Identifier("mishanguc", "edit_sign_finish"),
        PacketByteBufs.create()
            .writeBlockPos(blockPos)
            .writeNbt(
                changed ? Util.make(new NbtCompound(), nbt -> nbt.put("texts", list)) : null));
  }

  @Override
  public boolean charTyped(char chr, int modifiers) {
    if (getFocused() instanceof TextFieldWidget
        || getFocused() instanceof AbstractSignBlockEditScreen.TextFieldListScreen) {
      return getFocused().charTyped(chr, modifiers);
    } else {
      return textFieldListScreen.charTyped(chr, modifiers);
    }
  }

  /**
   * 这样是为了避免在调用 {@link #setFocused(Element)} 时，强制将 focused 设为了那个 {@link #textFieldListScreen}。
   */
  @Override
  public boolean changeFocus(boolean lookForwards) {
    Element focused = this.getFocused();
    boolean bl = focused != null;
    if (!bl || !focused.changeFocus(lookForwards)) {
      List<? extends Element> list = this.children();
      int i = list.indexOf(focused);
      int l;
      if (bl && i >= 0) {
        l = i + (lookForwards ? 1 : 0);
      } else if (lookForwards) {
        l = 0;
      } else {
        l = list.size();
      }

      ListIterator<? extends Element> listIterator = list.listIterator(l);
      BooleanSupplier booleanSupplier =
          lookForwards ? listIterator::hasNext : listIterator::hasPrevious;
      Supplier<Element> supplier = lookForwards ? listIterator::next : listIterator::previous;

      Element element2;
      do {
        if (!booleanSupplier.getAsBoolean()) {
          this.setFocused(null);
          return false;
        }

        element2 = supplier.get();
      } while (!element2.changeFocus(lookForwards));
      // 这里和 super 方法不同，这里直接调用的 super.setFocused 而非 this.setFocused
      super.setFocused(element2);
    }
    return true;
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
    if (!entity.getType().supports(entity.getCachedState())) {
      finishEditing();
    }
  }

  @Override
  public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
    return super.keyPressed(keyCode, scanCode, modifiers);
  }

  @Override
  public void setFocused(@Nullable Element focused) {
    if (focused instanceof ButtonWidget) setFocused(textFieldListScreen);
    else super.setFocused(focused);
  }

  /**
   * 文本框列表的屏幕。每个列表项都是一个文本框（实际上就是把 {@link TextFieldWidget} 包装成了 {@link Entry}。
   */
  public class TextFieldListScreen extends EntryListWidget<TextFieldListScreen.Entry> {

    public TextFieldListScreen(
        MinecraftClient client, int width, int height, int top, int bottom, int itemHeight) {
      super(client, width, height, top, bottom, itemHeight);
      this.setRenderBackground(false);
      this.setRenderHeader(false, 0);
    }

    /**
     * 如果该对象没有被选中，则里面一定没有文本框被选中。
     */
    @Nullable
    @Override
    public Entry getFocused() {
      return AbstractSignBlockEditScreen.this.getFocused() == this ? super.getFocused() : null;
    }

    /**
     * @param focused 需要选中的 {@link Entry}。
     */
    @Override
    public void setFocused(@Nullable Element focused) {
      super.setFocused(focused);
      for (Entry child : children()) {
        child.textFieldWidget.setTextFieldFocused(child == focused);
      }
      if (focused instanceof AbstractSignBlockEditScreen.TextFieldListScreen.Entry) {
        //noinspection rawtypes
        focusedTextField =
            ((AbstractSignBlockEditScreen.TextFieldListScreen.Entry) focused).textFieldWidget;
        //noinspection rawtypes
        focusedTextContext =
            contextToWidgetBiMap
                .inverse()
                .get(
                    ((AbstractSignBlockEditScreen.TextFieldListScreen.Entry) focused)
                        .textFieldWidget);

        // 设置焦点后，重新设置 customColorTextField 的内容
        if (focusedTextContext != null)
          customColorTextField.setText(String.format("#%06x", focusedTextContext.color));
      } else if (focused == null) {
        focusedTextField = null;
        focusedTextContext = null;
      }
    }

    @Override
    protected int getScrollbarPositionX() {
      return width - 6;
    }

    @ApiStatus.AvailableSince("mc1.17")
    @Override
    public void appendNarrations(NarrationMessageBuilder builder) {

    }

    /**
     * {@link TextFieldListScreen} 中的项。由于 {@link TextFieldWidget} 不是 {@link EntryListWidget.Entry}
     * 的子类，所以对该类进行了包装。<br>
     * 请看清楚，本类是内部类的内部类，且不是静态内部类。
     */
    public class Entry extends EntryListWidget.Entry<Entry> {
      public final TextFieldWidget textFieldWidget;


      public Entry(TextFieldWidget textFieldWidget) {
        this.textFieldWidget = textFieldWidget;
      }

      @Override
      public boolean equals(Object o) {
        if (this == o) {
          return true;
        }
        if (!(o instanceof AbstractSignBlockEditScreen.TextFieldListScreen.Entry)) {
          return false;
        }
        @SuppressWarnings({"unchecked", "rawtypes"}) Entry entry = (AbstractSignBlockEditScreen.TextFieldListScreen.Entry) o;

        return new EqualsBuilder().append(textFieldWidget, entry.textFieldWidget).isEquals();
      }

      @Override
      public int hashCode() {
        return new HashCodeBuilder(17, 37).append(textFieldWidget).toHashCode();
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
        textFieldWidget.y = y;
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

      @Override
      public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        switch (keyCode) {
          case 257 -> {
            final List<Entry> children = textFieldListScreen.children();
            final int index = children.indexOf(getFocused());
            if (index + 1 < children.size())
              textFieldListScreen.setFocused(children.get(index + 1));
            else if (children.size() > 0) addTextField(index + 1);
          }
          case 264 -> {
            final List<Entry> children = textFieldListScreen.children();
            final int index = children.indexOf(getFocused());
            if (index + 1 < children.size())
              textFieldListScreen.setFocused(children.get(index + 1));
            else if (children.size() > 0) textFieldListScreen.setFocused(children.get(0));
          }
          case 265 -> {
            final List<Entry> children = textFieldListScreen.children();
            final int index = children.indexOf(getFocused());
            if (index - 1 >= 0) textFieldListScreen.setFocused(children.get(index - 1));
            else if (children.size() > 0 && index == 0)
              textFieldListScreen.setFocused(children.get(children.size() - 1));
          }
          case 259 -> {
            final Entry focused = textFieldListScreen.getFocused();
            if (focused != null && textFieldWidget.getText().isEmpty()) {
              final int index = textFieldListScreen.children().indexOf(focused);
              if (index >= 0) {
                removeTextField(index);
                if (index - 1 >= 0 && index - 1 < textFieldListScreen.children().size()) {
                  textFieldListScreen.setFocused(textFieldListScreen.children().get(index - 1));
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
      public boolean changeFocus(boolean lookForwards) {
        return super.changeFocus(lookForwards) || textFieldWidget.changeFocus(lookForwards);
      }
    }
  }
}
