package pers.solid.mishang.uc.screen;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import it.unimi.dsi.fastutil.objects.Object2FloatMap;
import it.unimi.dsi.fastutil.objects.Object2FloatOpenHashMap;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.Element;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.ScreenTexts;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.EntryListWidget;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtList;
import net.minecraft.text.LiteralText;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.DyeColor;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import pers.solid.mishang.uc.MishangUtils;
import pers.solid.mishang.uc.blockentity.HungSignBlockEntity;
import pers.solid.mishang.uc.util.HorizontalAlign;
import pers.solid.mishang.uc.util.TextContext;
import pers.solid.mishang.uc.util.VerticalAlign;

import java.util.ArrayList;
import java.util.List;

/**
 * 编辑告示牌时的屏幕。<br>
 * 放置后如需打开此屏幕，使用
 *
 * <pre>{@code
 * this.client.openScreen(new TextPadEditScreen(entity)
 * }</pre>
 *
 * @see net.minecraft.client.gui.screen.ingame.SignEditScreen
 * @see net.minecraft.client.network.ClientPlayerEntity#openEditSignScreen
 * @see net.minecraft.server.network.ServerPlayerEntity#openEditSignScreen
 */
@Environment(EnvType.CLIENT)
public class SignBlockEditScreen extends Screen {
  public final Direction direction;
  public List<TextContext> textContextsEditing;
  @Deprecated public final List<TextFieldWidget> textFields = new ArrayList<>();
  public final BlockPos blockPos;
  public TextFieldListScreen textFieldListScreen;

  protected final BiMap<@NotNull TextContext, @NotNull TextFieldWidget> contextToWidgetBiMap =
      HashBiMap.create();
  private final HungSignBlockEntity entity;
  /** 完成编辑按钮 */
  final ButtonWidget finishButton =
      new ButtonWidget(
          this.width / 2 - 100,
          this.height - 30,
          200,
          20,
          ScreenTexts.DONE,
          buttonWidget -> SignBlockEditScreen.this.finishEditing());
  /** 正在被选中的 TextWidget。会在 {@link #setFocused(Element)} 时更改。可能为 null。 */
  public @Nullable TextFieldWidget focusedTextField = null;
  /** 正在被选中的 TextContent。会在 {@link #setFocused(Element)} 时更改。可能为 null。不是副本。 */
  public @Nullable TextContext focusedTextContext = null;
  /** 加粗按钮 */
  public final ButtonWidget boldButton =
      new ButtonWidget(
          this.width / 2 - 200,
          this.height - 50,
          20,
          20,
          new LiteralText("B").formatted(Formatting.BOLD),
          button -> {
            if (focusedTextContext != null) {
              focusedTextContext.bold = !focusedTextContext.bold;
            }
          });
  /** 斜体按钮 */
  public final ButtonWidget italicButton =
      new ButtonWidget(
          this.width / 2 - 180,
          this.height - 50,
          20,
          20,
          new TranslatableText("I").formatted(Formatting.ITALIC),
          button -> {
            if (focusedTextContext != null) {
              focusedTextContext.italic = !focusedTextContext.italic;
            }
          });
  /** 下划线按钮 */
  public final ButtonWidget underlineButton =
      new ButtonWidget(
          this.width / 2 - 160,
          this.height - 50,
          20,
          20,
          new TranslatableText("U").formatted(Formatting.UNDERLINE),
          button -> {
            if (focusedTextContext != null) {
              focusedTextContext.underline = !focusedTextContext.underline;
            }
          });
  /** 删除线按钮 */
  public final ButtonWidget strikethroughButton =
      new ButtonWidget(
          this.width / 2 - 140,
          this.height - 50,
          20,
          20,
          new TranslatableText("S").formatted(Formatting.STRIKETHROUGH),
          button -> {
            if (focusedTextContext != null) {
              focusedTextContext.strikethrough = !focusedTextContext.strikethrough;
            }
          });
  /** 随机文字按钮 */
  public final ButtonWidget obfuscatedButton =
      new ButtonWidget(
          this.width / 2 - 120,
          this.height - 50,
          20,
          20,
          new TranslatableText("O").formatted(Formatting.OBFUSCATED),
          button -> {
            if (focusedTextContext != null) {
              focusedTextContext.obfuscated = !focusedTextContext.obfuscated;
            }
          });
  /** 添加文本按钮 */
  public final ButtonWidget addTextButton =
      new ButtonWidget(
          width / 2 - 120 - 100,
          10,
          200,
          20,
          new TranslatableText("message.mishanguc.add_text"),
          button1 -> {
            int index =
                textFieldListScreen
                    .children()
                    .indexOf(textFieldListScreen.new Entry(focusedTextField));
            addTextField(index == -1 ? textFieldListScreen.children().size() : index + 1, null);
          });
  /** 阴影按钮 */
  public final ButtonWidget shadeButton =
      new ButtonWidget(
          this.width / 2 - 100,
          this.height - 50,
          40,
          20,
          new TranslatableText("message.mishanguc.shade"),
          button -> {
            if (focusedTextContext != null) {
              focusedTextContext.shadow = !focusedTextContext.shadow;
            }
          });
  /** 大小 */
  public final FloatButtonWidget sizeButton =
      new FloatButtonWidget(
          this.width / 2 - 60,
          this.height - 50,
          50,
          20,
          x -> new TranslatableText("message.mishanguc.size", x),
          (buttons) -> focusedTextContext != null ? focusedTextContext.size : 0,
          value -> {
            if (focusedTextContext != null) {
              focusedTextContext.size = value;
            }
          },
          (button) -> {});
  /** X偏移 */
  public final FloatButtonWidget offsetXButton =
      new FloatButtonWidget(
          this.width / 2 - 10,
          this.height - 50,
          60,
          20,
          x -> new TranslatableText("message.mishanguc.offsetX", x),
          button -> focusedTextContext != null ? focusedTextContext.offsetX : 0,
          value -> {
            if (focusedTextContext != null) {
              focusedTextContext.offsetX = value;
            }
          },
          (button) -> {});
  /** Y偏移 */
  public final FloatButtonWidget offsetYButton =
      new FloatButtonWidget(
          this.width / 2 + 40,
          this.height - 50,
          60,
          20,
          x -> new TranslatableText("message.mishanguc.offsetY", x),
          button -> focusedTextContext != null ? focusedTextContext.offsetY : 0,
          value -> {
            if (focusedTextContext != null) {
              focusedTextContext.offsetY = value;
            }
          },
          (button) -> {});
  /** Z偏移 */
  public final FloatButtonWidget offsetZButton =
      new FloatButtonWidget(
          this.width / 2 + 40,
          this.height - 50,
          60,
          20,
          x -> new TranslatableText("message.mishanguc.offsetZ", x),
          button -> focusedTextContext != null ? focusedTextContext.offsetZ : 0,
          value -> {
            if (focusedTextContext != null) {
              focusedTextContext.offsetZ = value;
            }
          },
          (button) -> {});
  /** 移除文本按钮 */
  public final ButtonWidget removeTextButton =
      new ButtonWidget(
          width / 2 + 120 - 100,
          10,
          200,
          20,
          new TranslatableText("message.mishanguc.remove_text"),
          button -> {
            int index =
                textFieldListScreen
                    .children()
                    .indexOf(textFieldListScreen.new Entry(focusedTextField));
            if (index != -1) {
              removeTextField(index);
            }
          });
  /** X缩放 */
  public final FloatButtonWidget scaleXButton =
      new FloatButtonWidget(
          this.width / 2 + 90,
          this.height - 50,
          60,
          20,
          x -> new TranslatableText("message.mishanguc.scaleX", x),
          button -> focusedTextContext != null ? focusedTextContext.scaleX : 1,
          value -> {
            if (focusedTextContext != null) {
              focusedTextContext.scaleX = value;
            }
          },
          (button) -> {});
  /** Y缩放 */
  public final FloatButtonWidget scaleYButton =
      new FloatButtonWidget(
          this.width / 2 + 140,
          this.height - 50,
          60,
          20,
          x -> new TranslatableText("message.mishanguc.scaleY", x),
          button -> focusedTextContext != null ? focusedTextContext.scaleY : 1,
          value -> {
            if (focusedTextContext != null) {
              focusedTextContext.scaleY = value;
            }
          },
          (button) -> {});

  /** 颜色按钮 */
  public final FloatButtonWidget colorButton =
      new FloatButtonWidget(
          0,
          0,
          60,
          20,
          colorId -> {
            if (colorId == -1) {
              return new TranslatableText("message.mishanguc.color");
            } else if (colorId == -2 && focusedTextContext != null) {
              return new TranslatableText(
                  "message.mishanguc.color.param", String.format("#%h", focusedTextContext.color));
            }
            final DyeColor dyeColor = DyeColor.byId((int) colorId);
            return new TranslatableText(
                "message.mishanguc.color.param",
                new TranslatableText("color.minecraft." + dyeColor.asString()));
          },
          button -> {
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
          },
          colorId -> {
            if (focusedTextContext != null) {
              focusedTextContext.color = DyeColor.byId((int) colorId).getSignColor();
            }
          },
          (button) -> {});

  {
    colorButton.min = -0.5f;
    colorButton.max = DyeColor.values().length - 0.5f;
  }

  /** 重排按钮 */
  public final ButtonWidget rearrangeButton =
      new ButtonWidget(
          this.width / 2 + 190,
          this.height - 50,
          50,
          20,
          new TranslatableText("message.mishanguc.rearrange"),
          button -> {
            Object2FloatMap<VerticalAlign> stackHeights =
                new Object2FloatOpenHashMap<>(VerticalAlign.values().length);
            for (TextContext textContext : textContextsEditing) {
              if (!textContext.absolute) {
                final float currentStackSize =
                    stackHeights.getOrDefault(textContext.verticalAlign, 0f);
                textContext.offsetY = currentStackSize + textContext.size * 9 / 8 / 4;
                stackHeights.put(
                    textContext.verticalAlign, currentStackSize + textContext.size * 9 / 8 / 2);
              }
            }
            for (TextContext textContext : textContextsEditing) {
              if (!textContext.absolute) {
                switch (textContext.verticalAlign) {
                  case MIDDLE:
                    textContext.offsetY -=
                        stackHeights.getOrDefault(textContext.verticalAlign, 0) / 2;
                    break;
                  case BOTTOM:
                    textContext.offsetY -=
                        textContextsEditing.get(textContextsEditing.size() - 1).size / 4 * 9 / 8;
                    break;
                  default:
                    textContext.offsetY -= textContextsEditing.get(0).size / 4 * 9 / 8;
                }
              }
            }
          },
          (a, b, c, d) -> new TranslatableText("message.mishanguc.rearrange.tooltip"));

  /** 水平对齐方式 */
  public final FloatButtonWidget horizontalAlignButton =
      new FloatButtonWidget(
          0,
          0,
          60,
          20,
          f ->
              f != -1
                  ? new TranslatableText(
                      "message.mishanguc.horizontal_align_param",
                      HorizontalAlign.values()[(int) f].getName())
                  : new TranslatableText("message.mishanguc.horizontal_align"),
          b -> focusedTextContext != null ? focusedTextContext.horizontalAlign.ordinal() : -1,
          f -> {
            if (focusedTextContext != null) {
              focusedTextContext.horizontalAlign = HorizontalAlign.values()[(int) f];
            }
          },
          (b) -> {});

  {
    horizontalAlignButton.min = -0.5f;
    horizontalAlignButton.max = 2.5f;
  }

  /** 垂直对齐方式 */
  public final FloatButtonWidget verticalAlignButton =
      new FloatButtonWidget(
          0,
          0,
          60,
          20,
          f ->
              f != -1
                  ? new TranslatableText(
                      "message.mishanguc.vertical_align_param",
                      VerticalAlign.values()[(int) f].getName())
                  : new TranslatableText("message.mishanguc.vertical_align"),
          b -> focusedTextContext != null ? focusedTextContext.verticalAlign.ordinal() : -1,
          f -> {
            if (focusedTextContext != null) {
              focusedTextContext.verticalAlign = VerticalAlign.values()[(int) f];
            }
          },
          (b) -> {});

  {
    verticalAlignButton.min = -0.5f;
    verticalAlignButton.max = 2.5f;
  }

  /**
   * 是否为高级模式。若为高级模式，则显示高级模式的按钮，不显示非高级模式的按钮。
   *
   * @see #switchAdvanceButton
   */
  private boolean advance = false;
  /** 切换高级模式为开或关。 */
  public final ButtonWidget switchAdvanceButton =
      new ButtonWidget(
          0,
          0,
          30,
          20,
          new TranslatableText("message.mishanguc.advanced"),
          b -> {
            advance = !advance;
            switchToolboxButtons();
          });

  private final ButtonWidget[] toolbox1 =
      new ButtonWidget[] {
        boldButton,
        italicButton,
        underlineButton,
        strikethroughButton,
        obfuscatedButton,
        shadeButton,
        sizeButton,
        offsetXButton,
        offsetYButton,
        colorButton,
        rearrangeButton,
        switchAdvanceButton
      };

  private final ButtonWidget[] toolbox2 =
      new ButtonWidget[] {
        offsetZButton,
        scaleXButton,
        scaleYButton,
        horizontalAlignButton,
        verticalAlignButton,
        rearrangeButton,
        switchAdvanceButton
      };

  /** 是否发生了改变。如果改变了，则提交时发送完整内容，否则发送空字符串表示未做更改。 */
  public boolean changed = false;

  public SignBlockEditScreen(HungSignBlockEntity entity, Direction direction, BlockPos blockPos) {
    super(new TranslatableText("message.mishanguc.sign_edit"));
    this.entity = entity;
    this.blockPos = blockPos;
    this.direction = direction;
    final List<@NotNull TextContext> get = entity.texts.get(direction);
    textContextsEditing = get == null ? Lists.newArrayList() : Lists.newArrayList(get);
    entity.texts = Maps.newHashMap(entity.texts);
    entity.texts.put(direction, textContextsEditing);

    // 调整按钮配置
    sizeButton.min = 0;
    sizeButton.step = -0.5f;
    offsetXButton.step = 0.5f;
    offsetYButton.step = 0.5f;
    scaleXButton.step = -0.125f;
    scaleYButton.step = -0.125f;
  }

  /** 初始化，对屏幕进行配置。 */
  @SuppressWarnings({"AlibabaLowerCamelCaseVariableNaming", "AlibabaMethodTooLong"})
  @Override
  protected void init() {
    super.init();

    textFieldListScreen = new TextFieldListScreen(client, width, height, 30, height - 50, 18);
    entity.editedSide = direction;
    placeHolder.setWidth(width);
    // 添加按钮
    this.addButton(placeHolder);
    this.addChild(textFieldListScreen);
    this.addButton(addTextButton);
    this.addButton(removeTextButton);
    this.addButton(boldButton);
    this.addButton(italicButton);
    this.addButton(underlineButton);

    this.addButton(strikethroughButton);
    this.addButton(obfuscatedButton);
    this.addButton(shadeButton);
    this.addButton(sizeButton);
    this.addButton(offsetXButton);
    this.addButton(offsetYButton);

    this.addButton(scaleXButton);
    this.addButton(scaleYButton);
    this.addButton(offsetZButton);
    this.addButton(horizontalAlignButton);
    this.addButton(verticalAlignButton);
    this.addButton(switchAdvanceButton);
    this.addButton(colorButton);
    this.addButton(rearrangeButton);
    this.addButton(finishButton);
    // 添加文本框
    for (int i = 0, textContextsEditingSize = textContextsEditing.size();
        i < textContextsEditingSize;
        i++) {
      TextContext textContext = textContextsEditing.get(i);
      addTextField(i, textContext);
    }

    switchToolboxButtons();
    addTextButton.x = width / 2 - 220;
    removeTextButton.x = width / 2 + 20;
    finishButton.x = width / 2 - 100;
    finishButton.y = height - 30;
  }

  /** 切换底部按钮的显示。显示高级按钮，或者取消高级按钮的显示。 */
  private void switchToolboxButtons() {
    for (ButtonWidget widget : advance ? toolbox1 : toolbox2) {
      widget.visible = false;
    }
    // 调整按钮位置
    int belowToolboxWidth = 0;
    for (ButtonWidget widget : advance ? toolbox2 : toolbox1) {
      final int width = widget.getWidth();
      widget.x = belowToolboxWidth;
      belowToolboxWidth += width;
    }
    for (ButtonWidget widget : advance ? toolbox2 : toolbox1) {
      widget.visible = true;
      widget.x += width / 2 - belowToolboxWidth / 2;
      widget.y = height - 50;
    }
  }

  /**
   * 添加一个文本框，并将其添加到 {@link #textFields} 和 {@link #contextToWidgetBiMap} 中。
   *
   * @param index 添加到的位置，对应在数组或列表中的次序。
   * @param textContext 需要添加的 {@link TextContext}。若为 {@code null}，则会复制一遍默认的。该
   *     textContext（或者复制的默认的）将会添加到 {@link #textContextsEditing} 中，且 {@code text} 字段将会与对应的 {@link
   *     TextFieldWidget} 中的同步。
   */
  public void addTextField(int index, @Nullable TextContext textContext) {
    if (textContext == null) {
      textContext = HungSignBlockEntity.DEFAULT_TEXT_CONTEXT.clone();
      textContext.text = new LiteralText("");
      textContextsEditing.add(index, textContext);
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
    textFieldListScreen.children().add(newEntry);
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
    changed = true;
  }

  /** 点击按钮后，还是应该聚焦在文本区域，而不是聚焦在按钮处，故做此修复。 */
  @Override
  public boolean mouseClicked(double mouseX, double mouseY, int button) {
    final boolean b = super.mouseClicked(mouseX, mouseY, button);
    setFocused(textFieldListScreen);
    return b;
  }

  /**
   * 移除一个文本框。将从 {@link #textFields}、{@link #contextToWidgetBiMap} 和 {@link #textContextsEditing}
   * 中移除对应的元素。
   *
   * @param index 移除的文本框的位置。
   * @see #addTextField(int, TextContext)
   */
  public void removeTextField(int index) {
    final TextFieldWidget removedWidget =
        textFieldListScreen.children().remove(index).textFieldWidget;
    final TextContext removedTextContext = contextToWidgetBiMap.inverse().get(removedWidget);
    if (textFieldListScreen.getSelected() != null
        && removedWidget == textFieldListScreen.getSelected().textFieldWidget) {
      textFieldListScreen.setSelected(null);
    }
    if (textFieldListScreen.children().size() > index) {
      textFieldListScreen.setFocused(textFieldListScreen.children().get(index));
    }
    textContextsEditing.remove(removedTextContext);
    contextToWidgetBiMap.remove(removedTextContext);
    placeHolder.visible = textFieldListScreen.children().size() == 0;
    changed = true;
  }

  @Override
  public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
    textFieldListScreen.render(matrices, mouseX, mouseY, delta);
    super.render(matrices, mouseX, mouseY, delta);
  }

  @Override
  public void removed() {
    super.removed();
    final NbtList list = new NbtList();
    for (TextContext textContext : textContextsEditing) {
      list.add(textContext.writeNbt(new NbtCompound()));
    }
    ClientPlayNetworking.send(
        new Identifier("mishanguc", "edit_sign_finish"),
        PacketByteBufs.create()
            .writeBlockPos(blockPos)
            .writeEnumConstant(direction)
            .writeString(changed ? list.asString() : ""));
    entity.editedSide = null;
  }

  private void finishEditing() {
    this.entity.markDirty();
    if (this.client != null) {
      this.client.openScreen(null);
    }
  }

  @Override
  public void tick() {
    super.tick();
    if (!entity.getType().supports(entity.getCachedState().getBlock())) {
      finishEditing();
    }
  }

  @Override
  public void setFocused(@Nullable Element focused) {
    super.setFocused(focused);
  }

  /** 文本框列表的屏幕。每个列表项都是一个文本框（实际上就是把 {@link TextFieldWidget} 包装成了 {@link Entry}。 */
  public class TextFieldListScreen extends EntryListWidget<TextFieldListScreen.Entry> {
    public TextFieldListScreen(
        MinecraftClient client, int width, int height, int top, int bottom, int itemHeight) {
      super(client, width, height, top, bottom, itemHeight);
      this.method_31322(false);
      this.setRenderHeader(false, 0);
    }

    /** 如果该对象没有被选中，则里面一定没有文本框被选中。 */
    @Nullable
    @Override
    public Entry getFocused() {
      return SignBlockEditScreen.this.getFocused() == this ? super.getFocused() : null;
    }

    @Override
    public void setFocused(@Nullable Element focused) {
      super.setFocused(focused);
      for (Entry child : children()) {
        child.textFieldWidget.setTextFieldFocused(child == focused);
      }
      if (focused instanceof Entry) {
        SignBlockEditScreen.this.focusedTextField = ((Entry) focused).textFieldWidget;
        focusedTextContext = contextToWidgetBiMap.inverse().get(((Entry) focused).textFieldWidget);
      } else if (focused == null) {
        focusedTextField = null;
        focusedTextContext = null;
      }
    }

    @Override
    protected int getScrollbarPositionX() {
      return width - 6;
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
        if (!(o instanceof Entry)) {
          return false;
        }
        Entry entry = (Entry) o;

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

  /** 没有添加文本时，显示的一条“点击此处添加文本”的消息。文本添加后，该按钮将消失。 */
  public final ButtonWidget placeHolder =
      new ButtonWidget(
          0,
          30,
          width,
          15,
          new TranslatableText("message.mishanguc.add_first_text"),
          button -> {
            addTextField(0, null);
            setFocused(textFieldListScreen);
            textFieldListScreen.setFocused(textFieldListScreen.children().get(0));
          }) {
        @Override
        public void drawTexture(
            MatrixStack matrices, int x, int y, int u, int v, int width, int height) {}
      };
}
