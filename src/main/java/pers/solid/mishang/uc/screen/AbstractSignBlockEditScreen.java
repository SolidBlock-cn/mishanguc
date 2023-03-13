package pers.solid.mishang.uc.screen;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import com.google.gson.JsonParseException;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.MultilineText;
import net.minecraft.client.gui.Element;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.tooltip.Tooltip;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.ClickableWidget;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtList;
import net.minecraft.screen.ScreenTexts;
import net.minecraft.text.LiteralTextContent;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.text.TextColor;
import net.minecraft.util.DyeColor;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;
import net.minecraft.util.Util;
import net.minecraft.util.math.BlockPos;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.Unmodifiable;
import org.lwjgl.glfw.GLFW;
import pers.solid.mishang.uc.MishangUtils;
import pers.solid.mishang.uc.blockentity.BlockEntityWithText;
import pers.solid.mishang.uc.text.PatternSpecialDrawable;
import pers.solid.mishang.uc.text.SpecialDrawable;
import pers.solid.mishang.uc.text.TextContext;
import pers.solid.mishang.uc.util.HorizontalAlign;
import pers.solid.mishang.uc.util.TextBridge;
import pers.solid.mishang.uc.util.VerticalAlign;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
  private static final MutableText REARRANGE_SUCCESS_NOTICE =
      TextBridge.translatable("message.mishanguc.rearrange.success");
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
  /**
   * 描述文本。悬浮在按钮时就会显示。
   */
  @ApiStatus.AvailableSince("0.1.6")
  public final AtomicReference<@Unmodifiable Text> descriptionAtom =
      new AtomicReference<>(TextBridge.empty());

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
  public @Nullable TextFieldWidget selectedTextField = null;

  /**
   * 正在被选中的 TextContent。会在 {@link #setFocused(Element)} 时更改。可能为 null。不是副本。
   */
  public @Nullable TextContext selectedTextContext = null;


  /*
  ===== 上方第一行 =====
   */

  /**
   * 上方第一行：添加文本按钮
   */
  public final ButtonWidget addTextButton = new ButtonWidget.Builder(TextBridge.translatable("message.mishanguc.add_text"), button1 -> {
    int index = textFieldListScreen.children().indexOf(textFieldListScreen.children().stream().filter(entry -> entry.textFieldWidget == selectedTextField).findFirst().orElse(null));
    addTextField(index == -1 ? textFieldListScreen.children().size() : index + 1);
  }).position(width / 2 - 120 - 100, 10).size(200, 20).tooltip(Tooltip.of(TextBridge.translatable("message.mishanguc.add_text.description"))).build();

  /**
   * 上方第一行：移除文本按钮
   */
  public final ButtonWidget removeTextButton = new ButtonWidget.Builder(TextBridge.translatable("message.mishanguc.remove_text"), button -> {
    if (selectedTextField == null) return;
    int index = textFieldListScreen.children().indexOf(textFieldListScreen.children().stream().filter(entry -> entry.textFieldWidget == selectedTextField).findFirst().orElse(null));
    if (index != -1) {
      removeTextField(index);
    }
  }).dimensions(width / 2 + 120 - 100, 10, 200, 20).tooltip(Tooltip.of(TextBridge.translatable("message.mishanguc.remove_text.description"))).build();



  /*
   ===== 文本区域列表部分 =====
   */

  /**
   * 没有添加文本时，显示的一条“点击此处添加文本”的消息。文本添加后，该按钮将消失。
   */
  public final ButtonWidget placeHolder = new ButtonWidget.Builder(TextBridge.translatable("message.mishanguc.add_first_text"), button -> {
    addTextField(0);
    setFocused(textFieldListScreen);
    textFieldListScreen.setFocused(textFieldListScreen.children().get(0));
  }).dimensions(0, 35, 200, 20).build();

  @ApiStatus.AvailableSince("0.1.6")
  public final ButtonWidget applyDoubleLineTemplateButton = new ButtonWidget.Builder(TextBridge.translatable("message.mishanguc.apply_double_line_template"), button -> {
    addTextField(0, AbstractSignBlockEditScreen.this.entity.createDefaultTextContext(), false);
    addTextField(1, Util.make(AbstractSignBlockEditScreen.this.entity.createDefaultTextContext(), textContext -> textContext.size /= 2), false);
    textFieldListScreen.setFocused(textFieldListScreen.children().get(0));
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
    textFieldListScreen.setFocused(textFieldListScreen.children().get(1));
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
    textFieldListScreen.setFocused(textFieldListScreen.children().get(1));
    rearrange();
  }).dimensions(width / 2 - 50, 70, 120, 20).tooltip(Tooltip.of(TextBridge.translatable("message.mishanguc.apply_right_arrow_template.description"))).build();


  /*
  ===== 下方第一行 =====
   */

  /**
   * 下方第一行：加粗按钮。
   */
  public final BooleanButtonWidget boldButton = new BooleanButtonWidget(this.width / 2 - 200, this.height - 50, 20, 20, TextBridge.literal("B").formatted(Formatting.BOLD), button -> selectedTextContext == null ? null : selectedTextContext.bold, b -> {
    changed = true;
    if (selectedTextContext != null) selectedTextContext.bold = b;
  }, b -> b == null ? TextBridge.translatable("message.mishanguc.bold") : TextBridge.translatable("message.mishanguc.bold.composed", TextBridge.translatable(b ? "options.on" : "options.off")), button -> {
  });

  /**
   * 下方第一行：斜体按钮。
   */
  public final BooleanButtonWidget italicButton = new BooleanButtonWidget(this.width / 2 - 180, this.height - 50, 20, 20, TextBridge.literal("I").formatted(Formatting.ITALIC), button -> selectedTextContext == null ? null : selectedTextContext.italic, b -> {
    changed = true;
    if (selectedTextContext != null) selectedTextContext.italic = b;
  }, b -> b == null ? TextBridge.translatable("message.mishanguc.italic") : TextBridge.translatable("message.mishanguc.italic.composed", TextBridge.translatable(b ? "options.on" : "options.off")), button -> {
  });

  /**
   * 下方第一行：下划线按钮。
   */
  public final BooleanButtonWidget underlineButton = new BooleanButtonWidget(this.width / 2 - 160, this.height - 50, 20, 20, TextBridge.literal("U").formatted(Formatting.UNDERLINE), button -> selectedTextContext == null ? null : selectedTextContext.underline, b -> {
    changed = true;
    if (selectedTextContext != null) selectedTextContext.underline = b;
  }, b -> b == null ? TextBridge.translatable("message.mishanguc.underline") : TextBridge.translatable("message.mishanguc.underline.composed", TextBridge.translatable(b ? "options.on" : "options.off")), button -> {
  });

  /**
   * 下方第一行：删除线按钮。
   */
  public final BooleanButtonWidget strikethroughButton = new BooleanButtonWidget(this.width / 2 - 140, this.height - 50, 20, 20, TextBridge.literal("S").formatted(Formatting.STRIKETHROUGH), button -> selectedTextContext == null ? null : selectedTextContext.strikethrough, b -> {
    changed = true;
    if (selectedTextContext != null) selectedTextContext.strikethrough = b;
  }, b -> b == null ? TextBridge.translatable("message.mishanguc.strikethrough") : TextBridge.translatable("message.mishanguc.strikethrough.composed", TextBridge.translatable(b ? "options.on" : "options.off")), button -> {
  });

  /**
   * 下方第一行：随机文字（obfuscated）按钮。
   */
  public final BooleanButtonWidget obfuscatedButton = new BooleanButtonWidget(this.width / 2 - 120, this.height - 50, 20, 20, TextBridge.literal("O").formatted(Formatting.OBFUSCATED), button -> selectedTextContext == null ? null : selectedTextContext.obfuscated, b -> {
    changed = true;
    if (selectedTextContext != null) selectedTextContext.obfuscated = b;
  }, b -> b == null ? TextBridge.translatable("message.mishanguc.obfuscated") : TextBridge.translatable("message.mishanguc.obfuscated.composed", TextBridge.translatable(b ? "options.on" : "options.off")), button -> {
  });


  /**
   * 下方第一行：阴影按钮。
   */
  public final BooleanButtonWidget shadeButton = new BooleanButtonWidget(this.width / 2 - 100, this.height - 50, 40, 20, TextBridge.translatable("message.mishanguc.shade"), button -> selectedTextContext == null ? null : selectedTextContext.shadow, b -> {
    changed = true;
    if (selectedTextContext != null) selectedTextContext.shadow = b;
  }, b -> b == null ? TextBridge.translatable("message.mishanguc.shade.description") : TextBridge.translatable("message.mishanguc.shade.composed", TextBridge.translatable(b ? "options.on" : "options.off")), button -> {
  });

  /**
   * 下方第一行：文本大小按钮。
   */
  public final FloatButtonWidget sizeButton = new FloatButtonWidget(this.width / 2 - 60, this.height - 50, 40, 20, TextBridge.translatable("message.mishanguc.size"), x -> TextBridge.translatable("message.mishanguc.size.description", x), buttons -> selectedTextContext != null ? selectedTextContext.size : 0, value -> {
    changed = true;
    if (selectedTextContext != null) {
      selectedTextContext.size = value;
    }
  }, button -> {
  });

  /**
   * 下方第一行：X偏移。
   */
  public final FloatButtonWidget offsetXButton = new FloatButtonWidget(this.width / 2 - 10, this.height - 50, 40, 20, TextBridge.translatable("message.mishanguc.offsetX"), x -> TextBridge.translatable("message.mishanguc.offsetX.composed", x), button -> selectedTextContext != null ? selectedTextContext.offsetX : 0, value -> {
    changed = true;
    if (selectedTextContext != null) {
      selectedTextContext.offsetX = value;
    }
  }, button -> {
  });

  /**
   * 下方第一行：Y偏移。
   */
  public final FloatButtonWidget offsetYButton = new FloatButtonWidget(this.width / 2 + 40, this.height - 50, 40, 20, TextBridge.translatable("message.mishanguc.offsetY"), x -> TextBridge.translatable("message.mishanguc.offsetY.composed", x), button -> selectedTextContext != null ? selectedTextContext.offsetY : 0, value -> {
    changed = true;
    if (selectedTextContext != null) {
      selectedTextContext.offsetY = value;
    }
  }, button -> {
  });

  /**
   * 下方第一行：Z偏移。
   */
  public final FloatButtonWidget offsetZButton = new FloatButtonWidget(this.width / 2 + 40, this.height - 50, 40, 20, TextBridge.translatable("message.mishanguc.offsetZ"), x -> TextBridge.translatable("message.mishanguc.offsetZ.composed", x), button -> selectedTextContext != null ? selectedTextContext.offsetZ : 0, value -> {
    changed = true;
    if (selectedTextContext != null) {
      selectedTextContext.offsetZ = value;
    }
  }, button -> {
  });

  /**
   * 下方第一行：颜色。
   *
   * @see #customColorTextField
   */
  public final FloatButtonWidget colorButton = new FloatButtonWidget(0, 0, 40, 20, TextBridge.translatable("message.mishanguc.color"), colorId -> {
    if (colorId == -1) {
      return TextBridge.translatable("message.mishanguc.color");
    } else if (colorId == -2 && selectedTextContext != null) {
      return TextBridge.translatable("message.mishanguc.color.composed",
          TextBridge.empty()
              .append(TextBridge.literal("■").styled(style -> style.withColor(selectedTextContext.color)))
              .append(TextBridge.literal(String.format("#%06x", selectedTextContext.color))));
    }
    final DyeColor dyeColor = DyeColor.byId((int) colorId);
    return TextBridge.translatable("message.mishanguc.color.composed",
        TextBridge.empty()
            .append(TextBridge.literal("■")
                .styled(style -> style.withColor(dyeColor.getSignColor())))
            .append(TextBridge.translatable("color.minecraft." + dyeColor.asString())));
  }, button -> {
    changed = true;
    if (selectedTextContext == null) {
      ((FloatButtonWidget) button).active = false;
      return -1;
    }
    final DyeColor dyeColor = MishangUtils.colorBySignColor(selectedTextContext.color);
    if (dyeColor == null) {
      ((FloatButtonWidget) button).active = false;
      return -2;
    } else {
      ((FloatButtonWidget) button).active = true;
      return dyeColor.getId();
    }
  }, colorId -> {
    if (selectedTextContext != null) {
      selectedTextContext.color = DyeColor.byId((int) colorId).getSignColor();
      this.customColorTextField.setText(String.format("#%06x", selectedTextContext.color));
    }
  }, button -> {
  });

  /**
   * 下方第一行：设置自定义颜色。<p>
   * 因为 {@link #textRenderer} 暂时还是 {@code null} 得在 {@link #init(MinecraftClient, int, int)}
   * 中赋值，所以这里直接使用 {@code MinecraftClient.getInstance().textRenderer}。 <p>
   * （其实我觉得应该在 {@code init} 里面通过 mixin 修改该字段的 textRender，不过意义其实不大吧。qwq）
   *
   * @see #colorButton
   */
  public final TextFieldWidget customColorTextField = Util.make(new TextFieldWidget(MinecraftClient.getInstance().textRenderer, 0, 0, 50, 20, TextBridge.translatable("message.mishanguc.custom_color")) {
    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
      // 避免无法用方向左右键来离开此编辑框
      if (keyCode == GLFW.GLFW_KEY_LEFT && getCursor() <= 0) {
        return false;
      } else if (keyCode == GLFW.GLFW_KEY_RIGHT && getCursor() >= getText().length()) {
        return false;
      }
      return super.keyPressed(keyCode, scanCode, modifiers);
    }
  }, widget ->
      widget.setChangedListener(
          s -> {
            changed = true;
            if (selectedTextContext != null) {
              final TextColor parsedColor = TextColor.parse(widget.getText());
              if (parsedColor != null) selectedTextContext.color = parsedColor.getRgb();
            }
          }));

  /**
   * 下方第一行：描边颜色。
   */
  @ApiStatus.AvailableSince("0.1.6-mc1.17")
  public final FloatButtonWidget outlineColorButton = new FloatButtonWidget(0, 0, 60, 20, TextBridge.translatable("message.mishanguc.outline_color"), colorId -> {
    // colorId=-1：表示当前自动根据文本内容绘制描边。
    // colorId=-2：表示当前不绘制描边（默认）。
    // colorId=-3：表示是自定义的。
    // colorId=-4：表示当前没有选中文本。
    // colorId=0-15：标准颜色。
    // colorId=16-31：描边颜色
    if (colorId == -1) {
      return TextBridge.translatable("message.mishanguc.outline_color.auto");
    } else if (colorId == -2) {
      return TextBridge.translatable("message.mishanguc.outline_color.none");
    } else if (colorId == -3 && selectedTextContext != null) {
      return TextBridge.translatable("message.mishanguc.outline_color.composed", String.format("#%06x", selectedTextContext.outlineColor)).styled(style -> style.withColor(selectedTextContext.color));
    } else if (colorId == -4) {
      return TextBridge.translatable("message.mishanguc.outline_color");
    } else if (colorId > 15) {
      final DyeColor color = DyeColor.byId((int) colorId - 16);
      return TextBridge.translatable("message.mishanguc.outline_color.composed", TextBridge.translatable("message.mishanguc.outline_color.relate", TextBridge.translatable("message.mishanguc.outline_color.relate.$1")
          .styled(style -> style.withColor(MishangUtils.COLOR_TO_OUTLINE_COLOR.get(color))), TextBridge.translatable("color.minecraft." + color.asString()).styled(style -> style.withColor(color.getSignColor()))));
    } else {
      final DyeColor color = DyeColor.byId((int) colorId);
      if (color == null) return TextBridge.translatable("message.mishanguc.outline_color.none");
      return TextBridge.translatable("message.mishanguc.outline_color.composed", TextBridge.translatable("color.minecraft." + color.asString()).styled(style -> style.withColor(color.getSignColor())));
    }
  }, button -> {
    if (selectedTextContext == null) {
      ((ClickableWidget) button).active = false;
      return -4;
    }
    if (selectedTextContext.outlineColor == -1) {
      ((ClickableWidget) button).active = true;
      return -1;
    } else if (selectedTextContext.outlineColor == -2) {
      ((ClickableWidget) button).active = true;
      return -2;
    }
    final DyeColor colorOutline = MishangUtils.COLOR_TO_OUTLINE_COLOR.inverse().get(selectedTextContext.outlineColor);
    if (colorOutline != null) {
      ((ClickableWidget) button).active = true;
      return colorOutline.getId() + 16;
    }
    final DyeColor color = MishangUtils.colorBySignColor(selectedTextContext.outlineColor);
    if (color != null) {
      ((ClickableWidget) button).active = true;
      return color.getId();
    } else {
      ((ClickableWidget) button).active = false;
      return -3;
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
  }, button -> {
  });


  /*
  ==== 下方第二行 ====
   */

  /**
   * 下方第二行：X旋转。
   */
  @ApiStatus.AvailableSince("0.1.6")
  public final FloatButtonWidget rotationXButton = new FloatButtonWidget(this.width / 2 + 40, this.height - 50, 40, 20, TextBridge.translatable("message.mishanguc.rotationX"), x -> TextBridge.translatable("message.mishanguc.rotationX.composed", x), button -> selectedTextContext != null ? selectedTextContext.rotationX : 0, value -> {
    changed = true;
    if (selectedTextContext != null) selectedTextContext.rotationX = value;
  }, button -> {
  });

  /**
   * 下方第二行：Y旋转。
   */
  @ApiStatus.AvailableSince("0.1.6")
  public final FloatButtonWidget rotationYButton = new FloatButtonWidget(this.width / 2 + 40, this.height - 50, 40, 20, TextBridge.translatable("message.mishanguc.rotationY"), x -> TextBridge.translatable("message.mishanguc.rotationY.composed", x), button -> selectedTextContext != null ? selectedTextContext.rotationY : 0, value -> {
    changed = true;
    if (selectedTextContext != null) selectedTextContext.rotationY = value;
  }, button -> {
  });

  /**
   * 下方第二行：Z旋转。
   */
  @ApiStatus.AvailableSince("0.1.6")
  public final FloatButtonWidget rotationZButton = new FloatButtonWidget(this.width / 2 + 40, this.height - 50, 40, 20, TextBridge.translatable("message.mishanguc.rotationZ"), x -> TextBridge.translatable("message.mishanguc.rotationZ.composed", x), button -> selectedTextContext != null ? selectedTextContext.rotationZ : 0, value -> {
    changed = true;
    if (selectedTextContext != null) selectedTextContext.rotationZ = value;
  }, button -> {
  });

  /**
   * 下方第二行：X缩放。
   */
  public final FloatButtonWidget scaleXButton = new FloatButtonWidget(this.width / 2 + 90, this.height - 50, 40, 20, TextBridge.translatable("message.mishanguc.scaleX"), x -> TextBridge.translatable("message.mishanguc.scaleX.composed", x), button -> selectedTextContext != null ? selectedTextContext.scaleX : 1, value -> {
    changed = true;
    if (selectedTextContext != null) {
      selectedTextContext.scaleX = value;
    }
  }, button -> {
  });

  /**
   * 下方第二行：Y缩放。
   */
  public final FloatButtonWidget scaleYButton = new FloatButtonWidget(this.width / 2 + 140, this.height - 50, 40, 20, TextBridge.translatable("message.mishanguc.scaleY"), x -> TextBridge.translatable("message.mishanguc.scaleY.composed", x), button -> selectedTextContext != null ? selectedTextContext.scaleY : 1, value -> {
    changed = true;
    if (selectedTextContext != null) {
      selectedTextContext.scaleY = value;
    }
  }, button -> {
  });

  /**
   * 下方第二行：水平对齐方式。
   */
  public final FloatButtonWidget horizontalAlignButton = new FloatButtonWidget(0, 0, 50, 20, TextBridge.translatable("message.mishanguc.horizontal_align"), f -> f != -1 ? TextBridge.translatable("message.mishanguc.horizontal_align.composed", HorizontalAlign.values()[(int) f].getName()) : TextBridge.translatable("message.mishanguc.horizontal_align"), b -> selectedTextContext != null ? selectedTextContext.horizontalAlign.ordinal() : -1, f -> {
    if (selectedTextContext != null) {
      selectedTextContext.horizontalAlign = HorizontalAlign.values()[(int) f];
    }
  }, b -> {
  });

  /**
   * 下方第二行：垂直对齐方式。
   */
  public final FloatButtonWidget verticalAlignButton = new FloatButtonWidget(0, 0, 50, 20, TextBridge.translatable("message.mishanguc.vertical_align"), f -> f != -1 ? TextBridge.translatable("message.mishanguc.vertical_align.composed", VerticalAlign.values()[(int) f].getName()) : TextBridge.translatable("message.mishanguc.vertical_align"), b -> selectedTextContext != null ? selectedTextContext.verticalAlign.ordinal() : -1, f -> {
    if (selectedTextContext != null) {
      selectedTextContext.verticalAlign = VerticalAlign.values()[(int) f];
    }
  }, b -> {
  });

  /**
   * 下方第二行：切换文字是否可以看穿。
   */
  public final BooleanButtonWidget seeThroughButton = new BooleanButtonWidget(0, 0, 60, 20, TextBridge.translatable("message.mishanguc.see_through"), button -> selectedTextContext == null ? null : selectedTextContext.seeThrough, b -> {
    changed = true;
    if (selectedTextContext != null) selectedTextContext.seeThrough = b;
  }, b -> b == null ? TextBridge.translatable("message.mishanguc.see_through") : TextBridge.translatable("message.mishanguc.see_through.composed", TextBridge.translatable(b ? "options.on" : "options.off")), button -> {
  });

  /**
   * 下方第二行：绝对模式。
   */
  public final BooleanButtonWidget absoluteButton = new BooleanButtonWidget(0, 0, 50, 20, TextBridge.translatable("message.mishanguc.absolute"), button -> selectedTextContext != null ? selectedTextContext.absolute : null, b -> {
    changed = true;
    if (selectedTextContext != null) selectedTextContext.absolute = b;
  }, b -> b == null ? TextBridge.translatable("message.mishanguc.absolute.description") : TextBridge.translatable("message.mishanguc.absolute.composed", TextBridge.translatable(b ? "options.on" : "options.off")), button -> {
  });


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
  public final ButtonWidget moveUpButton = new ButtonWidget.Builder(TextBridge.translatable("message.mishanguc.moveUp"), button -> {
    if (selectedTextField == null) return;
    int i = textFieldListScreen.children().indexOf(textFieldListScreen.getSelectedOrNull());
    final TextContext textContext = AbstractSignBlockEditScreen.this.textContextsEditing.get(i);
    removeTextField(i);
    if (i > 0) i--;
    addTextField(i, textContext, false);
  }).dimensions(this.width - 20, this.height - 50, 30, 20).tooltip(Tooltip.of(TextBridge.translatable("message.mishanguc.moveUp.description"))).build();

  /**
   * 下方第三行：下移按钮。
   */
  public final ButtonWidget moveDownButton = new ButtonWidget.Builder(TextBridge.translatable("message.mishanguc.moveDown"), button -> {
    if (selectedTextField == null) return;
    int i = textFieldListScreen.children().indexOf(textFieldListScreen.getSelectedOrNull());
    final TextContext textContext = AbstractSignBlockEditScreen.this.textContextsEditing.get(i);
    removeTextField(i);
    if (i < textFieldListScreen.children().size()) i++;
    addTextField(i, textContext, false);
  }).dimensions(this.width - 20, this.height - 50, 30, 20).tooltip(Tooltip.of(TextBridge.translatable("message.mishanguc.moveDown.description"))).build();

  /**
   * 下方第三行：重排按钮。
   */
  public final ButtonWidget rearrangeButton = new ButtonWidget.Builder(TextBridge.translatable("message.mishanguc.rearrange"), button -> {
    rearrange();
    descriptionAtom.set(REARRANGE_SUCCESS_NOTICE);
  }).dimensions(this.width / 2 + 190, this.height - 50, 70, 20).tooltip(Tooltip.of(TextBridge.translatable("message.mishanguc.rearrange.tooltip"))).build();

  /**
   * 下方第三行：完成编辑按钮。
   */
  public final ButtonWidget finishButton = new ButtonWidget.Builder(ScreenTexts.DONE, buttonWidget -> this.finishEditing()).dimensions(this.width / 2 - 100, this.height - 30, 120, 20).tooltip(Tooltip.of(TextBridge.translatable("message.mishanguc.finish.description"))).build();

  /**
   * 下方第三行：取消编辑按钮。
   */
  public final ButtonWidget cancelButton = new ButtonWidget.Builder(ScreenTexts.CANCEL, button -> this.cancelEditing()).dimensions(this.width / 2, height - 30, 40, 20).tooltip(Tooltip.of(TextBridge.translatable("message.mishanguc.cancel.description"))).build();

  /**
   * 下方第三行：清除所有文本的按钮。
   */
  public final ButtonWidget clearButton = new ButtonWidget.Builder(BUTTON_CLEAR_MESSAGE, button -> {
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
  }).dimensions(this.width / 2, this.height - 50, 50, 20).tooltip(Tooltip.of(BUTTON_CLEAR_CONFIRM_DESCRIPTION_MESSAGE)).build();

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
  }).dimensions(this.width / 2, this.height - 50, 50, 20).tooltip(Tooltip.of(TextBridge.translatable("message.mishanguc.flip.description"))).build();


  public AbstractSignBlockEditScreen(T entity, BlockPos blockPos, List<TextContext> textContextsEditing) {
    super(TextBridge.translatable("message.mishanguc.sign_edit"));
    this.entity = entity;
    this.blockPos = blockPos;
    this.textContextsEditing = textContextsEditing;
    entity.setEditor(this.client != null ? this.client.player : null);
    sizeButton.defaultValue = entity.createDefaultTextContext().size;
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
    textFieldListScreen = new TextFieldListScreen(this, client, width, height, 30, height - 65, 18);
    setFocused(textFieldListScreen);
    // 添加按钮

    /// 上方第一行，先 addChild 再 addDrawable 以确保 tab 顺序正确，同时不被 textFieldListScreen 覆盖。
    this.addSelectableChild(addTextButton);
    this.addSelectableChild(removeTextButton);

    /// 文本列表屏幕以及占位符
    initTextHolders();
    this.addDrawableChild(textFieldListScreen);

    this.addDrawable(addTextButton);
    this.addDrawable(removeTextButton);

    /// 下方第一行和第二行
    Arrays.stream(toolbox1).forEach(this::addDrawableChild);
    Arrays.stream(toolbox2).forEach(this::addDrawableChild);

    /// 下方第三行
    this.addDrawableChild(moveUpButton);
    this.addDrawableChild(rearrangeButton);
    this.addDrawableChild(moveDownButton);
    this.addDrawableChild(finishButton);
    this.addDrawableChild(cancelButton);
    this.addDrawableChild(clearButton);
    this.addDrawableChild(flipButton);

    // 添加文本框
    for (int i = 0, textContextsEditingSize = textContextsEditing.size();
         i < textContextsEditingSize;
         i++) {
      TextContext textContext = textContextsEditing.get(i);
      addTextField(i, textContext, true);
    }

    arrangeToolboxButtons();

    addTextButton.setX(width / 2 - 220);
    removeTextButton.setX(width / 2 + 20);
    moveUpButton.setX(width / 2 - 190);
    moveUpButton.setY(height - 25);
    rearrangeButton.setX(width / 2 - 160);
    rearrangeButton.setY(height - 25);
    moveDownButton.setX(width / 2 - 90);
    moveDownButton.setY(height - 25);
    finishButton.setX(width / 2 - 60);
    finishButton.setY(height - 25);
    cancelButton.setX(width / 2 + 60);
    cancelButton.setY(height - 25);
    clearButton.setX(width / 2 + 100);
    clearButton.setY(height - 25);
    flipButton.setX(width / 2 + 150);
    flipButton.setY(height - 25);
    placeHolder.setX(width / 2 - 100);
    applyDoubleLineTemplateButton.setX(width / 2 - 60);
    applyLeftArrowTemplateButton.setX(width / 2 - 180);
    applyRightArrowTemplateButton.setX(width / 2 + 60);
  }

  protected void initTextHolders() {
    this.addDrawableChild(placeHolder);
    this.addDrawableChild(applyLeftArrowTemplateButton);
    this.addDrawableChild(applyDoubleLineTemplateButton);
    this.addDrawableChild(applyRightArrowTemplateButton);
  }

  @Override
  public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
    descriptionAtom.set(TextBridge.empty());
    super.render(matrices, mouseX, mouseY, delta);
    final Text description = descriptionAtom.get();
    final MultilineText multilineText = MultilineText.create(textRenderer, description, width);
    multilineText.drawCenterWithShadow(
        matrices,
        width / 2,
        height - 75, 9,
        0xcccccc);
    if (placeHolder.visible) {
      final MutableText text0 = TextBridge.translatable("message.mishanguc.or");
      textRenderer.drawWithShadow(
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
    final TextFieldWidget textFieldWidget =
        new TextFieldWidget(
            textRenderer,
            2,
            height / 4,
            width - 4,
            15,
            TextBridge.translatable("message.mishanguc.text_field"));
    textFieldWidget.setMaxLength(Integer.MAX_VALUE);
    if (textContext.extra != null) {
      textFieldWidget.setText(String.format("-%s %s", textContext.extra.getId(), textContext.extra.asStringArgs()));
    } else if (textContext.text != null) {
      if (textContext.text.getContent() instanceof LiteralTextContent literalTextContent && textContext.text.getSiblings().isEmpty() && textContext.text.getStyle().isEmpty()) {
        final String text = literalTextContent.string();
        if (Pattern.compile("^-(\\w+?) (.+)$").matcher(text).matches()) {
          textFieldWidget.setText("-literal " + text);
        } else {
          textFieldWidget.setText(text);
        }
      } else {
        textFieldWidget.setText("-json " + Text.Serializer.toJson(textContext.text));
      }
    }
    final TextFieldListScreen.Entry newEntry = textFieldListScreen.new Entry(textFieldWidget);
    textFieldListScreen.children().add(index, newEntry);
    contextToWidgetBiMap.put(textContext, textFieldWidget);
    textFieldListScreen.setFocused(newEntry);
    textFieldWidget.setChangedListener(
        s -> {
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
                    textContext1.text = Text.Serializer.fromLenientJson(value);
                  } catch (JsonParseException e) {
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
    int belowToolboxWidth = 0;
    for (ClickableWidget widget : toolbox1) {
      final int width = widget.getWidth();
      widget.setX(belowToolboxWidth);
      belowToolboxWidth += width;
    }
    for (ClickableWidget widget : toolbox1) {
      widget.visible = true;
      widget.setPosition(widget.getX() + width / 2 - belowToolboxWidth / 2, height - 65);
    }
    belowToolboxWidth = 0;
    for (ClickableWidget widget : toolbox2) {
      final int width = widget.getWidth();
      widget.setX(belowToolboxWidth);
      belowToolboxWidth += width;
    }
    for (ClickableWidget widget : toolbox2) {
      widget.visible = true;
      widget.setPosition(widget.getX() + (width / 2 - belowToolboxWidth / 2), height - 45);
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
    if (textFieldListScreen.getSelectedOrNull() != null
        && removedWidget == textFieldListScreen.getSelectedOrNull().textFieldWidget) {
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
  public boolean mouseClicked(double mouseX, double mouseY, int button) {
    for (Element element : this.children()) {
      if (!element.mouseClicked(mouseX, mouseY, button)) continue;
      if (element == textFieldListScreen || element instanceof TextFieldWidget) {
        this.setFocused(element);
      } else {
        setFocused(textFieldListScreen);
      }
      if (button == 0) {
        this.setDragging(true);
      }
      return true;
    }
    return false;
  }

  @Override
  public void removed() {
    super.removed();
    entity.setEditor(null);
    final NbtList list = new NbtList();
    for (TextContext textContext : textContextsEditing) {
      list.add(textContext.createNbt());
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
        || getFocused() instanceof TextFieldListScreen) {
      return getFocused().charTyped(chr, modifiers);
    } else {
      return textFieldListScreen.charTyped(chr, modifiers);
    }
  }

  protected Element weakFocus = textFieldListScreen;

  @Override
  public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
    if (keyCode == GLFW.GLFW_KEY_TAB) {
      if (getFocused() == textFieldListScreen) {
        // 当前焦点为文本时，按 tab 切换到按钮。

        if (weakFocus == null || weakFocus == textFieldListScreen) {
          // 还没有 weakFocus 的情况
          setFocused(boldButton);
          weakFocus = getFocused();
        } else {
          // 已经设置过 weakFocus 的情况
          setFocused(weakFocus);
          weakFocus = textFieldListScreen;
        }
      } else if (textFieldListScreen.children().size() != 0) {
        // 当前焦点为按钮时，按 tab 切换到文本。
        weakFocus = getFocused();
        setFocused(textFieldListScreen);
        for (TextFieldListScreen.Entry child : textFieldListScreen.children()) {
          child.textFieldWidget.setFocused(textFieldListScreen.isFocused() && child == textFieldListScreen.getFocused());
        }
      }

      return true;
    } else {
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
      return super.keyPressed(keyCode, scanCode, modifiers);
    }
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

  /**
   * 设置整个 AbstractSignBlockEditScreen 的已选中的元素。这个元素可以是一个按钮、整个 {@link #textFieldListScreen} 或 {@link #customColorTextField}。
   *
   * @param focused 已选中的元素。
   * @see TextFieldListScreen#setFocused(Element)
   * @see TextFieldListScreen.Entry#setFocused(Element)
   */
  @Override
  public void setFocused(@Nullable Element focused) {
    if (getFocused() instanceof TextFieldListScreen != focused instanceof TextFieldListScreen) {
      weakFocus = getFocused();
    }
    super.setFocused(focused);
  }
}
