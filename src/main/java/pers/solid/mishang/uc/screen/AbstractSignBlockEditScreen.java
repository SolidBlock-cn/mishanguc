package pers.solid.mishang.uc.screen;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import com.google.gson.JsonParseException;
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
import org.lwjgl.glfw.GLFW;
import pers.solid.mishang.uc.MishangUtils;
import pers.solid.mishang.uc.blockentity.BlockEntityWithText;
import pers.solid.mishang.uc.text.PatternTextSpecial;
import pers.solid.mishang.uc.text.TextContext;
import pers.solid.mishang.uc.text.TextSpecial;
import pers.solid.mishang.uc.util.HorizontalAlign;
import pers.solid.mishang.uc.util.VerticalAlign;

import java.util.Arrays;
import java.util.List;
import java.util.ListIterator;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.BooleanSupplier;
import java.util.function.Supplier;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * ??????????????????????????????<br>
 * ???????????????????????????????????????
 *
 * <pre>{@code
 * this.client.setScreen(new TextPadEditScreen(entity))
 * }</pre>
 *
 * @param <T> ????????????????????????
 * @see net.minecraft.client.gui.screen.ingame.SignEditScreen
 * @see net.minecraft.client.network.ClientPlayerEntity#openEditSignScreen
 * @see net.minecraft.server.network.ServerPlayerEntity#openEditSignScreen
 */
@Environment(EnvType.CLIENT)
public abstract class AbstractSignBlockEditScreen<T extends BlockEntityWithText> extends Screen {
  // ???????????????????????????????????????????????????
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
   * ????????????????????????????????????????????????
   */
  @ApiStatus.AvailableSince("0.1.6")
  public final AtomicReference<@Unmodifiable Text> descriptionAtom =
      new AtomicReference<>(LiteralText.EMPTY);

  public final T entity;

  protected final BiMap<@NotNull TextContext, @NotNull TextFieldWidget> contextToWidgetBiMap =
      HashBiMap.create();
  /**
   * ?????????????????????????????????????????????????????????????????????????????????????????? NBT ?????????????????????
   */
  public boolean changed = false;

  public TextFieldListScreen textFieldListScreen;

  /**
   * ?????????????????? TextWidget????????? {@link #setFocused(Element)} ????????????????????? null???
   */
  public @Nullable TextFieldWidget focusedTextField = null;

  /**
   * ?????????????????? TextContent????????? {@link #setFocused(Element)} ????????????????????? null??????????????????
   */
  public @Nullable TextContext focusedTextContext = null;


  /*
  ===== ??????????????? =====
   */

  /**
   * ????????????????????????????????????
   */
  public final ButtonWidget addTextButton = new ButtonWidget(width / 2 - 120 - 100, 10, 200, 20, new TranslatableText("message.mishanguc.add_text"), button1 -> {
    int index = textFieldListScreen.children().indexOf(textFieldListScreen.new Entry(focusedTextField));
    addTextField(index == -1 ? textFieldListScreen.children().size() : index + 1);
  });

  /**
   * ????????????????????????????????????
   */
  public final ButtonWidget removeTextButton = new ButtonWidget(width / 2 + 120 - 100, 10, 200, 20, new TranslatableText("message.mishanguc.remove_text"), button -> {
    int index = textFieldListScreen.children().indexOf(textFieldListScreen.new Entry(focusedTextField));
    if (index != -1) {
      removeTextField(index);
    }
  });



  /*
   ===== ???????????????????????? =====
   */

  /**
   * ????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????
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
    textContext0.extra = PatternTextSpecial.fromName(textContext0, "al");
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
    textContext0.extra = PatternTextSpecial.fromName(textContext0, "ar");
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
  ===== ??????????????? =====
   */

  /**
   * ?????????????????????????????????
   */
  public final BooleanButtonWidget boldButton = new BooleanButtonWidget(this.width / 2 - 200, this.height - 50, 20, 20, new LiteralText("B").formatted(Formatting.BOLD), button -> focusedTextContext == null ? null : focusedTextContext.bold, b -> {
    changed = true;
    if (focusedTextContext != null) focusedTextContext.bold = b;
  }, b -> b == null ? new TranslatableText("message.mishanguc.bold") : new TranslatableText("message.mishanguc.bold.description", new TranslatableText(b ? "options.on" : "options.off")), button -> {
  }, descriptionAtom);

  /**
   * ?????????????????????????????????
   */
  public final BooleanButtonWidget italicButton = new BooleanButtonWidget(this.width / 2 - 180, this.height - 50, 20, 20, new LiteralText("I").formatted(Formatting.ITALIC), button -> focusedTextContext == null ? null : focusedTextContext.italic, b -> {
    changed = true;
    if (focusedTextContext != null) focusedTextContext.italic = b;
  }, b -> b == null ? new TranslatableText("message.mishanguc.italic") : new TranslatableText("message.mishanguc.italic.description", new TranslatableText(b ? "options.on" : "options.off")), button -> {
  }, descriptionAtom);

  /**
   * ????????????????????????????????????
   */
  public final BooleanButtonWidget underlineButton = new BooleanButtonWidget(this.width / 2 - 160, this.height - 50, 20, 20, new LiteralText("U").formatted(Formatting.UNDERLINE), button -> focusedTextContext == null ? null : focusedTextContext.underline, b -> {
    changed = true;
    if (focusedTextContext != null) focusedTextContext.underline = b;
  }, b -> b == null ? new TranslatableText("message.mishanguc.underline") : new TranslatableText("message.mishanguc.underline.description", new TranslatableText(b ? "options.on" : "options.off")), button -> {
  }, descriptionAtom);

  /**
   * ????????????????????????????????????
   */
  public final BooleanButtonWidget strikethroughButton = new BooleanButtonWidget(this.width / 2 - 140, this.height - 50, 20, 20, new LiteralText("S").formatted(Formatting.STRIKETHROUGH), button -> focusedTextContext == null ? null : focusedTextContext.strikethrough, b -> {
    changed = true;
    if (focusedTextContext != null) focusedTextContext.strikethrough = b;
  }, b -> b == null ? new TranslatableText("message.mishanguc.strikethrough") : new TranslatableText("message.mishanguc.strikethrough.description", new TranslatableText(b ? "options.on" : "options.off")), button -> {
  }, descriptionAtom);

  /**
   * ?????????????????????????????????obfuscated????????????
   */
  public final BooleanButtonWidget obfuscatedButton = new BooleanButtonWidget(this.width / 2 - 120, this.height - 50, 20, 20, new LiteralText("O").formatted(Formatting.OBFUSCATED), button -> focusedTextContext == null ? null : focusedTextContext.obfuscated, b -> {
    changed = true;
    if (focusedTextContext != null) focusedTextContext.obfuscated = b;
  }, b -> b == null ? new TranslatableText("message.mishanguc.obfuscated") : new TranslatableText("message.mishanguc.obfuscated.description", new TranslatableText(b ? "options.on" : "options.off")), button -> {
  }, descriptionAtom);


  /**
   * ?????????????????????????????????
   */
  public final BooleanButtonWidget shadeButton = new BooleanButtonWidget(this.width / 2 - 100, this.height - 50, 40, 20, new TranslatableText("message.mishanguc.shade"), button -> focusedTextContext == null ? null : focusedTextContext.shadow, b -> {
    changed = true;
    if (focusedTextContext != null) focusedTextContext.shadow = b;
  }, b -> b == null ? new TranslatableText("message.mishanguc.shade.description") : new TranslatableText("message.mishanguc.shade.param", new TranslatableText(b ? "options.on" : "options.off")), button -> {
  }, descriptionAtom);

  /**
   * ???????????????????????????????????????
   */
  public final FloatButtonWidget sizeButton = new FloatButtonWidget(this.width / 2 - 60, this.height - 50, 40, 20, new TranslatableText("message.mishanguc.size"), x -> new TranslatableText("message.mishanguc.size.description", x), buttons -> focusedTextContext != null ? focusedTextContext.size : 0, value -> {
    changed = true;
    if (focusedTextContext != null) {
      focusedTextContext.size = value;
    }
  }, button -> {
  }, descriptionAtom);

  /**
   * ??????????????????X?????????
   */
  public final FloatButtonWidget offsetXButton = new FloatButtonWidget(this.width / 2 - 10, this.height - 50, 40, 20, new TranslatableText("message.mishanguc.offsetX"), x -> new TranslatableText("message.mishanguc.offsetX.description", x), button -> focusedTextContext != null ? focusedTextContext.offsetX : 0, value -> {
    changed = true;
    if (focusedTextContext != null) {
      focusedTextContext.offsetX = value;
    }
  }, button -> {
  }, descriptionAtom);

  /**
   * ??????????????????Y?????????
   */
  public final FloatButtonWidget offsetYButton = new FloatButtonWidget(this.width / 2 + 40, this.height - 50, 40, 20, new TranslatableText("message.mishanguc.offsetY"), x -> new TranslatableText("message.mishanguc.offsetY.description", x), button -> focusedTextContext != null ? focusedTextContext.offsetY : 0, value -> {
    changed = true;
    if (focusedTextContext != null) {
      focusedTextContext.offsetY = value;
    }
  }, button -> {
  }, descriptionAtom);

  /**
   * ??????????????????Z?????????
   */
  public final FloatButtonWidget offsetZButton = new FloatButtonWidget(this.width / 2 + 40, this.height - 50, 40, 20, new TranslatableText("message.mishanguc.offsetZ"), x -> new TranslatableText("message.mishanguc.offsetZ.description", x), button -> focusedTextContext != null ? focusedTextContext.offsetZ : 0, value -> {
    changed = true;
    if (focusedTextContext != null) {
      focusedTextContext.offsetZ = value;
    }
  }, button -> {
  }, descriptionAtom);

  /**
   * ???????????????????????????
   *
   * @see #customColorTextField
   */
  public final FloatButtonWidget colorButton = new FloatButtonWidget(0, 0, 40, 20, new TranslatableText("message.mishanguc.color"), colorId -> {
    if (colorId == -1) {
      return new TranslatableText("message.mishanguc.color");
    } else if (colorId == -2 && focusedTextContext != null) {
      return new TranslatableText("message.mishanguc.color.param", new LiteralText(String.format("#%06x", focusedTextContext.color)).styled(style -> style.withColor(focusedTextContext.color)))
          ;
    }
    final DyeColor dyeColor = DyeColor.byId((int) colorId);
    return new TranslatableText(
        "message.mishanguc.color.param",
        new TranslatableText("color.minecraft." + dyeColor.asString())
            .styled(style -> style.withColor(dyeColor.getSignColor())));
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
  }, button -> {
  }, descriptionAtom);

  /**
   * ??????????????????????????????????????????<br>
   * ?????? {@link #textRenderer} ???????????? {@code null} ?????? {@link #init(MinecraftClient, int, int)}
   * ???????????????????????????????????? {@code MinecraftClient.getInstance().textRenderer}??? <br>
   * ??????????????????????????? {@code init} ???????????? mixin ?????????????????? textRender?????????????????????????????????qwq
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
   * ?????????????????????????????????
   */
  @ApiStatus.AvailableSince("0.1.6-mc1.17")
  public final FloatButtonWidget outlineColorButton = new FloatButtonWidget(0, 0, 60, 20, new TranslatableText("message.mishanguc.outline_color"), colorId -> {
    // colorId=-1??????????????????????????????????????????????????????
    // colorId=-2?????????????????????????????????????????????
    // colorId=-3???????????????????????????
    // colorId=-4????????????????????????????????????
    // colorId=0-15??????????????????
    // colorId=16-31???????????????
    if (colorId == -1) {
      return new TranslatableText("message.mishanguc.outline_color.auto");
    } else if (colorId == -2) {
      return new TranslatableText("message.mishanguc.outline_color.none");
    } else if (colorId == -3 && focusedTextContext != null) {
      return new TranslatableText("message.mishanguc.outline_color.param", String.format("#%06x", focusedTextContext.outlineColor)).styled(style -> style.withColor(focusedTextContext.color));
    } else if (colorId == -4) {
      return new TranslatableText("message.mishanguc.outline_color");
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
  ==== ??????????????? ====
   */

  /**
   * ??????????????????X?????????
   */
  @ApiStatus.AvailableSince("0.1.6")
  public final FloatButtonWidget rotationXButton = new FloatButtonWidget(this.width / 2 + 40, this.height - 50, 40, 20, new TranslatableText("message.mishanguc.rotationX"), x -> new TranslatableText("message.mishanguc.rotationX.description", x), button -> focusedTextContext != null ? focusedTextContext.rotationX : 0, value -> {
    changed = true;
    if (focusedTextContext != null) focusedTextContext.rotationX = value;
  }, button -> {
  }, descriptionAtom);

  /**
   * ??????????????????Y?????????
   */
  @ApiStatus.AvailableSince("0.1.6")
  public final FloatButtonWidget rotationYButton = new FloatButtonWidget(this.width / 2 + 40, this.height - 50, 40, 20, new TranslatableText("message.mishanguc.rotationY"), x -> new TranslatableText("message.mishanguc.rotationY.description", x), button -> focusedTextContext != null ? focusedTextContext.rotationY : 0, value -> {
    changed = true;
    if (focusedTextContext != null) focusedTextContext.rotationY = value;
  }, button -> {
  }, descriptionAtom);

  /**
   * ??????????????????Z?????????
   */
  @ApiStatus.AvailableSince("0.1.6")
  public final FloatButtonWidget rotationZButton = new FloatButtonWidget(this.width / 2 + 40, this.height - 50, 40, 20, new TranslatableText("message.mishanguc.rotationZ"), x -> new TranslatableText("message.mishanguc.rotationZ.description", x), button -> focusedTextContext != null ? focusedTextContext.rotationZ : 0, value -> {
    changed = true;
    if (focusedTextContext != null) focusedTextContext.rotationZ = value;
  }, button -> {
  }, descriptionAtom);

  /**
   * ??????????????????X?????????
   */
  public final FloatButtonWidget scaleXButton = new FloatButtonWidget(this.width / 2 + 90, this.height - 50, 40, 20, new TranslatableText("message.mishanguc.scaleX"), x -> new TranslatableText("message.mishanguc.scaleX.description", x), button -> focusedTextContext != null ? focusedTextContext.scaleX : 1, value -> {
    changed = true;
    if (focusedTextContext != null) {
      focusedTextContext.scaleX = value;
    }
  }, button -> {
  }, descriptionAtom);

  /**
   * ??????????????????Y?????????
   */
  public final FloatButtonWidget scaleYButton = new FloatButtonWidget(this.width / 2 + 140, this.height - 50, 40, 20, new TranslatableText("message.mishanguc.scaleY"), x -> new TranslatableText("message.mishanguc.scaleY.description", x), button -> focusedTextContext != null ? focusedTextContext.scaleY : 1, value -> {
    changed = true;
    if (focusedTextContext != null) {
      focusedTextContext.scaleY = value;
    }
  }, button -> {
  }, descriptionAtom);

  /**
   * ???????????????????????????????????????
   */
  public final FloatButtonWidget horizontalAlignButton = new FloatButtonWidget(0, 0, 50, 20, new TranslatableText("message.mishanguc.horizontal_align"), f -> f != -1 ? new TranslatableText("message.mishanguc.horizontal_align_param", HorizontalAlign.values()[(int) f].getName()) : new TranslatableText("message.mishanguc.horizontal_align"), b -> focusedTextContext != null ? focusedTextContext.horizontalAlign.ordinal() : -1, f -> {
    if (focusedTextContext != null) {
      focusedTextContext.horizontalAlign = HorizontalAlign.values()[(int) f];
    }
  }, b -> {
  }, descriptionAtom);

  /**
   * ???????????????????????????????????????
   */
  public final FloatButtonWidget verticalAlignButton = new FloatButtonWidget(0, 0, 50, 20, new TranslatableText("message.mishanguc.vertical_align"), f -> f != -1 ? new TranslatableText("message.mishanguc.vertical_align_param", VerticalAlign.values()[(int) f].getName()) : new TranslatableText("message.mishanguc.vertical_align"), b -> focusedTextContext != null ? focusedTextContext.verticalAlign.ordinal() : -1, f -> {
    if (focusedTextContext != null) {
      focusedTextContext.verticalAlign = VerticalAlign.values()[(int) f];
    }
  }, b -> {
  }, descriptionAtom);

  /**
   * ???????????????????????????????????????????????????
   */
  public final BooleanButtonWidget seeThroughButton = new BooleanButtonWidget(0, 0, 60, 20, new TranslatableText("message.mishanguc.see_through"), button -> focusedTextContext == null ? null : focusedTextContext.seeThrough, b -> {
    changed = true;
    if (focusedTextContext != null) focusedTextContext.seeThrough = b;
  }, b -> b == null ? new TranslatableText("message.mishanguc.see_through") : new TranslatableText("message.mishanguc.see_through.param", new TranslatableText(b ? "options.on" : "options.off")), button -> {
  }, descriptionAtom);

  /**
   * ?????????????????????????????????
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
  ===== ??????????????? =====
   */

  /**
   * ?????????????????????????????????
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
   * ?????????????????????????????????
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
   * ?????????????????????????????????
   */
  public final ButtonWidget rearrangeButton = new ButtonWidget(this.width / 2 + 190, this.height - 50, 70, 20, new TranslatableText("message.mishanguc.rearrange"), button -> {
    rearrange();
    descriptionAtom.set(REARRANGE_SUCCESS_NOTICE);
  }, (a, b, c, d) -> {
    if (descriptionAtom.get() != REARRANGE_SUCCESS_NOTICE)
      descriptionAtom.set(new TranslatableText("message.mishanguc.rearrange.tooltip"));
  });

  /**
   * ???????????????????????????????????????
   */
  public final ButtonWidget finishButton = new ButtonWidget(this.width / 2 - 100, this.height - 30, 120, 20, ScreenTexts.DONE, buttonWidget -> this.finishEditing(), (button, matrices, mouseX, mouseY) ->
      descriptionAtom.set(new TranslatableText("message.mishanguc.finish.description")));

  /**
   * ???????????????????????????????????????
   */
  public final ButtonWidget cancelButton = new ButtonWidget(this.width / 2, height - 30, 40, 20, ScreenTexts.CANCEL, button -> this.cancelEditing(), (button, matrices, mouseX, mouseY) ->
      descriptionAtom.set(new TranslatableText("message.mishanguc.cancel.description")));

  /**
   * ????????????????????????????????????????????????
   */
  public final ButtonWidget clearButton = new ButtonWidget(this.width / 2, this.height - 50, 50, 20, BUTTON_CLEAR_MESSAGE, button -> {
    if (button.getMessage() == BUTTON_CLEAR_CONFIRM_MESSAGE) {
      for (int i = AbstractSignBlockEditScreen.this.textFieldListScreen.children().size() - 1; i >= 0; i--) {
        removeTextField(i);
      }
      button.setMessage(BUTTON_CLEAR_MESSAGE);
      descriptionAtom.set(BUTTON_CLEAR_DESCRIPTION_MESSAGE);
    } else {
      // ???????????????????????????????????????????????????
      button.setMessage(BUTTON_CLEAR_CONFIRM_MESSAGE);
      descriptionAtom.set(BUTTON_CLEAR_CONFIRM_DESCRIPTION_MESSAGE);
    }
  }, (button, matrices, mouseX, mouseY) -> {
    if (button.getMessage() == BUTTON_CLEAR_CONFIRM_MESSAGE)
      descriptionAtom.set(BUTTON_CLEAR_CONFIRM_DESCRIPTION_MESSAGE);
    else descriptionAtom.set(BUTTON_CLEAR_DESCRIPTION_MESSAGE);
  });

  /**
   * ???????????????????????????????????????????????????
   */
  @ApiStatus.AvailableSince("0.1.7")
  public final ButtonWidget flipButton = new ButtonWidget(this.width / 2, this.height - 50, 50, 20, new TranslatableText("message.mishanguc.flip"), button -> {
    if (hasControlDown()) {
      for (TextContext textContext : AbstractSignBlockEditScreen.this.textContextsEditing) {
        textContext.flip();
      }
    } else {
      if (focusedTextContext != null) {
        focusedTextContext.flip();
      }
    }
  }, (button, matrices, mouseX, mouseY) -> descriptionAtom.set(new TranslatableText("message.mishanguc.flip.description")));


  public AbstractSignBlockEditScreen(T entity, BlockPos blockPos, List<TextContext> textContextsEditing) {
    super(new TranslatableText("message.mishanguc.sign_edit"));
    this.entity = entity;
    this.blockPos = blockPos;
    this.textContextsEditing = textContextsEditing;
    entity.setEditor(this.client != null ? this.client.player : null);
    sizeButton.defaultValue = entity.getDefaultTextContext().size;
  }

  /**
   * ???????????????????????????
   *
   * @see #rearrangeButton
   */
  public void rearrange() {
    final List<TextContext> textContextsEditing = AbstractSignBlockEditScreen.this.textContextsEditing;
    MishangUtils.rearrange(textContextsEditing);
  }

  /**
   * ???????????????????????????????????? {@link #removed()}??????????????? NBT ?????????????????????
   */
  public void cancelEditing() {
    changed = false;
    if (this.client != null) {
      this.client.setScreen(null);
    }
  }

  /**
   * ????????????????????????????????????
   */
  @Override
  protected void init() {
    super.init();
    textFieldListScreen = new TextFieldListScreen(client, width, height, 30, height - 80, 18);
    setFocused(textFieldListScreen);
    // ????????????
    /// ?????????????????????????????????
    this.addDrawableChild(placeHolder);
    this.addDrawableChild(applyDoubleLineTemplateButton);
    this.addDrawableChild(applyLeftArrowTemplateButton);
    this.addDrawableChild(applyRightArrowTemplateButton);
    this.addDrawableChild(textFieldListScreen);

    /// ???????????????
    this.addDrawableChild(addTextButton);
    this.addDrawableChild(removeTextButton);

    /// ???????????????????????????
    Arrays.stream(toolbox1).forEach(this::addDrawableChild);
    Arrays.stream(toolbox2).forEach(this::addDrawableChild);

    /// ???????????????
    this.addDrawableChild(moveUpButton);
    this.addDrawableChild(moveDownButton);
    this.addDrawableChild(rearrangeButton);
    this.addDrawableChild(finishButton);
    this.addDrawableChild(cancelButton);
    this.addDrawableChild(clearButton);
    this.addDrawableChild(flipButton);

    // ???????????????
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
    flipButton.x = width / 2 + 150;
    flipButton.y = height - 25;
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
   * ???????????????????????????????????????????????? {@link #contextToWidgetBiMap} ??????
   *
   * @param index ????????????????????????????????????????????????????????????
   */
  public void addTextField(int index) {
    addTextField(index, entity.getDefaultTextContext(), false);
  }

  /**
   * ?????????????????????????????????????????? {@link #contextToWidgetBiMap} ??????
   *
   * @param index       ????????????????????????????????????????????????????????????
   * @param textContext ??????????????? {@link TextContext}???
   * @param isExisting  ??????????????????????????????????????????????????? textContext ????????? {@link #textContextsEditing} ?????????????????? {@link
   *                    #changed} ?????? <code>true</code>???
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
    if (textContext.extra != null) {
      textFieldWidget.setText(String.format("-%s %s", textContext.extra.getId(), textContext.extra.describeArgs()));
    } else if (textContext.text != null) {
      if (textContext.text instanceof LiteralText && textContext.text.getSiblings().isEmpty() && textContext.text.getStyle().isEmpty()) {
        final String text = textContext.text.asString();
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
                  textContext1.text = new LiteralText(value);
                  break;
                case "json":
                  try {
                    textContext1.text = Text.Serializer.fromLenientJson(value);
                  } catch (JsonParseException e) {
                    // ?????????????????????????????????????????????
                  }
                  break;
                default:
                  final TextSpecial textSpecial = TextSpecial.fromDescription(textContext1, name, value);
                  if (textSpecial == null) {
                    textContext1.extra = null;
                    textContext1.text = new LiteralText(s);
                  } else if (textSpecial != TextSpecial.INVALID) {
                    textContext1.extra = textSpecial;
                    textContext1.text = new LiteralText("");
                  } // ????????? INVALID ?????????????????????
              }
            } else {
              textContext1.extra = null;
              textContext1.text = new LiteralText(s);
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
   * ???????????????????????????????????????????????????????????????????????????????????????
   */
  private void arrangeToolboxButtons() {
    // ??????????????????
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
   * ?????????????????????????????? {@link #contextToWidgetBiMap} ??? {@link #textContextsEditing} ???????????????????????????
   *
   * @param index ??????????????????????????????
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
  public boolean mouseClicked(double mouseX, double mouseY, int button) {
    for (Element element : this.children()) {
      if (!element.mouseClicked(mouseX, mouseY, button)) continue;
      this.setFocused(element);
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
   * ?????????????????????????????? {@link #setFocused(Element)} ??????????????? focused ??????????????? {@link #textFieldListScreen}???
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
      // ????????? super ???????????????????????????????????? super.setFocused ?????? this.setFocused
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
    if (entity.isRemoved()) {
      finishEditing();
    }
  }

  @Override
  public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
    return super.keyPressed(keyCode, scanCode, modifiers);
  }

  @Override
  public void setFocused(@Nullable Element focused) {
    if (focused instanceof ButtonWidget) {
      super.setFocused(textFieldListScreen);
    } else {
      super.setFocused(focused);
      if (focused != textFieldListScreen) {
        textFieldListScreen.setFocused(null);
      }
    }
  }

  /**
   * ???????????????????????????????????????????????????????????????????????????????????? {@link TextFieldWidget} ???????????? {@link Entry}???
   */
  @Environment(EnvType.CLIENT)
  public class TextFieldListScreen extends EntryListWidget<TextFieldListScreen.Entry> {

    public TextFieldListScreen(
        MinecraftClient client, int width, int height, int top, int bottom, int itemHeight) {
      super(client, width, height, top, bottom, itemHeight);
      this.setRenderBackground(false);
      this.setRenderHeader(false, 0);
    }

    /**
     * ???????????????????????????????????????????????????????????????????????????
     */
    @Nullable
    @Override
    public Entry getFocused() {
      return AbstractSignBlockEditScreen.this.getFocused() == this ? super.getFocused() : null;
    }

    /**
     * @param focused ??????????????? {@link Entry}???
     */
    @Override
    public void setFocused(@Nullable Element focused) {
      super.setFocused(focused);
      for (Entry child : children()) {
        child.textFieldWidget.setTextFieldFocused(child == focused);
      }
      if (focused instanceof AbstractSignBlockEditScreen.TextFieldListScreen.Entry entry) {
        focusedTextField = entry.textFieldWidget;
        focusedTextContext = contextToWidgetBiMap.inverse().get(entry.textFieldWidget);

        // ?????????????????????????????? customColorTextField ?????????
        if (focusedTextContext != null)
          customColorTextField.setText(String.format("#%06x", focusedTextContext.color));
      } else if (focused == null) {
        focusedTextField = null;
        focusedTextContext = null;
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
      Entry entry = this.getEntryAtPosition(mouseX, mouseY);
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

    }

    /**
     * {@link TextFieldListScreen} ?????????????????? {@link TextFieldWidget} ?????? {@link EntryListWidget.Entry}
     * ?????????????????????????????????????????????<br>
     * ???????????????????????????????????????????????????????????????????????????
     */
    @Environment(EnvType.CLIENT)
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
        if (!(o instanceof final AbstractSignBlockEditScreen.TextFieldListScreen.Entry entry)) {
          return false;
        }

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
          case GLFW.GLFW_KEY_ENTER -> {
            final List<Entry> children = textFieldListScreen.children();
            final int index = children.indexOf(getFocused());
            if (index + 1 < children.size())
              textFieldListScreen.setFocused(children.get(index + 1));
            else if (children.size() > 0) addTextField(index + 1);
          }
          case GLFW.GLFW_KEY_DOWN -> {
            final List<Entry> children = textFieldListScreen.children();
            final int index = children.indexOf(getFocused());
            if (index + 1 < children.size())
              textFieldListScreen.setFocused(children.get(index + 1));
            else if (children.size() > 0) textFieldListScreen.setFocused(children.get(0));
          }
          case GLFW.GLFW_KEY_UP -> {
            final List<Entry> children = textFieldListScreen.children();
            final int index = children.indexOf(getFocused());
            if (index - 1 >= 0) textFieldListScreen.setFocused(children.get(index - 1));
            else if (children.size() > 0 && index == 0)
              textFieldListScreen.setFocused(children.get(children.size() - 1));
          }
          case GLFW.GLFW_KEY_BACKSPACE -> {
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
