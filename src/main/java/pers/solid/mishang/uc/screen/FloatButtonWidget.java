package pers.solid.mishang.uc.screen;

import it.unimi.dsi.fastutil.floats.Float2ObjectFunction;
import it.unimi.dsi.fastutil.floats.FloatConsumer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.narration.NarrationMessageBuilder;
import net.minecraft.client.gui.screen.narration.NarrationPart;
import net.minecraft.client.gui.tooltip.Tooltip;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.util.InputUtil;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Util;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.Nullable;
import org.lwjgl.glfw.GLFW;
import pers.solid.mishang.uc.util.TextBridge;

import java.util.function.Function;

/**
 * 用于处理浮点数的按钮。按下鼠标时增大，但是按住 shift 则会减小。滚动鼠标滚轮也会减小。
 */
@Environment(EnvType.CLIENT)
public class FloatButtonWidget extends ButtonWidget {
  public final Function<@Nullable Float, @Nullable Text> tooltipSupplier;
  private final Function<FloatButtonWidget, @Nullable Float> valueGetter;
  private final FloatConsumer valueSetter;
  private boolean sliderFocused;

  /**
   * 按钮的默认值。可以按鼠标中键或者按住 Alt + Shift 点击以恢复。
   */
  public float defaultValue = 0;
  /**
   * 按钮的步长，默认为1。
   */
  public float step = 1;

  /**
   * 按下“右”方向键时，步长再乘以此值。
   */
  public float rightArrowStepMultiplier = 1;
  /**
   * 按下“上”或“下”方向键时，步长再乘以此值。
   */
  public float upArrowStepMultiplier = 1;

  /**
   * 滚动鼠标滚轮时，步长再乘以此值。
   */
  public float scrollMultiplier = -1;

  /**
   * 按钮当前的最小值。若低于最小值，则从最大值开始循环，但是如果没有最大值时除外。
   */
  public float min = Float.NEGATIVE_INFINITY;
  /**
   * 按钮当前的最大值。若高于最大值，则从最小值开始循环，但是如果没有最小值时除外。
   */
  public float max = Float.POSITIVE_INFINITY;

  public static final Float2ObjectFunction<MutableText> DEFAULT_VALUE_NARRATOR = value -> TextBridge.literal(Float.toString(value));
  private Float2ObjectFunction<MutableText> valueNarrator = DEFAULT_VALUE_NARRATOR;

  public FloatButtonWidget(
      int x,
      int y,
      int width,
      int height,
      Text message,
      Function<Float, Text> tooltipSupplier,
      Function<FloatButtonWidget, Float> valueGetter,
      FloatConsumer valueSetter,
      PressAction onPress) {
    super(
        x,
        y,
        width,
        height,
        message,
        onPress,
        ButtonWidget.DEFAULT_NARRATION_SUPPLIER);
    this.tooltipSupplier = tooltipSupplier;
    this.valueGetter = valueGetter;
    this.valueSetter = valueSetter;
    updateTooltip();
  }

  public void updateTooltip() {
    final Float value = getValue();
    final Text tooltip = this.tooltipSupplier.apply(value);
    if (value != null) {
      setTooltip(Tooltip.of(tooltip == null ? getMessage() : tooltip, TextBridge.translatable("narration.mishanguc.button.current_value", valueNarrator.get(value.floatValue()))));
    } else {
      setTooltip(Tooltip.of(tooltip == null ? getMessage() : tooltip, TextBridge.empty()));
    }
  }

  @Override
  public void render(DrawContext context, int mouseX, int mouseY, float delta) {
    if (this.isHovered()) updateTooltip();
    super.render(context, mouseX, mouseY, delta);
  }

  public @Nullable Float getValue() {
    return valueGetter.apply(this);
  }

  /**
   * 设置该按钮的值。会受到最小值和最大值的限制。
   */
  public void setValue(float value) {
    if (value < min) {
      if (Float.isFinite(max)) {
        // 从最大值开始向下循环。
        value = max;
      } else {
        // 封底为最小值。
        value = min;
      }
    } else if (value > max) {
      if (Float.isFinite(min)) {
        // 从最小值开始向上循环。
        value = min;
      } else {
        // 封顶为最大值。
        value = max;
      }
    }
    valueSetter.accept(value);
    updateTooltip();
  }

  @Override
  protected void drawScrollableText(DrawContext context, TextRenderer textRenderer, int xMargin, int color) {
    if (!sliderFocused || Util.getMeasuringTimeMs() % 1000 > 500) {
      // 在 sliderFocused 的情况下，文字应该闪烁
      super.drawScrollableText(context, textRenderer, xMargin, color);
    }
  }

  @Override
  public boolean mouseClicked(double mouseX, double mouseY, int button) {
    final Float value = getValue();
    final boolean b = super.mouseClicked(mouseX, mouseY, button);
    if (this.active && this.visible && clicked(mouseX, mouseY) && value != null) {
      switch (button) { // 这种情况下直接采用了 onPress，所以直接略。
        case 0, 1 -> {
          setValue(value
              + (Screen.hasShiftDown() || button == 1 ? -1 : 1)
              * step
              * (Screen.hasControlDown() ? 8 : 1)
              * (Screen.hasAltDown() ? 0.125f : 1));
          return true;
        }
        case 2 -> {
          setValue(defaultValue);
          return true;
        }
        default -> {
        }
      }
    }
    return b;
  }

  @Override
  public boolean mouseScrolled(double mouseX, double mouseY, double amount) {
    final Float value = getValue();
    if (value != null) {
      setValue((float) (value
          + amount
          * (Screen.hasShiftDown() ? -1 : 1)
          * (Screen.hasControlDown() ? 8 : 1)
          * step * scrollMultiplier
          * (Screen.hasAltDown() ? 0.125f : 1)));
    }
    super.mouseScrolled(mouseX, mouseY, amount);
    return true;
  }


  /**
   * @see net.minecraft.client.gui.widget.SliderWidget#keyPressed(int, int, int)
   */
  public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
    final Float value = getValue();
    if (keyCode != GLFW.GLFW_KEY_SPACE && keyCode != GLFW.GLFW_KEY_ENTER && keyCode != GLFW.GLFW_KEY_KP_ENTER) {
      if (this.sliderFocused) {
        boolean decreases = keyCode == GLFW.GLFW_KEY_LEFT || keyCode == GLFW.GLFW_KEY_DOWN;
        final var handle = MinecraftClient.getInstance().getWindow().getHandle();
        if (keyCode == GLFW.GLFW_KEY_LEFT && InputUtil.isKeyPressed(handle, GLFW.GLFW_KEY_RIGHT)
            || keyCode == GLFW.GLFW_KEY_RIGHT && InputUtil.isKeyPressed(handle, InputUtil.GLFW_KEY_LEFT)) {
          // 当同时按下左右时，设为默认值。
          setValue(defaultValue);
          return true;
        } else if ((decreases || keyCode == GLFW.GLFW_KEY_RIGHT || keyCode == GLFW.GLFW_KEY_UP) && value != null) {
          final float multiplier = keyCode == GLFW.GLFW_KEY_RIGHT || keyCode == GLFW.GLFW_KEY_LEFT ? rightArrowStepMultiplier : upArrowStepMultiplier;
          float sign = decreases ? -1.0F : 1.0F;
          this.setValue(value + sign
              * (Screen.hasShiftDown() ? -1 : 1)
              * (Screen.hasControlDown() ? 8 : 1)
              * step * multiplier
              * (Screen.hasAltDown() ? 0.125f : 1));
          return true;
        }
      }
      return false;
    } else {
      this.sliderFocused = value != null && !this.sliderFocused;
      return super.keyPressed(keyCode, scanCode, modifiers);
    }
  }

  @Override
  public Text getMessage() {
    final Float value = getValue();
    if (value == null || value == defaultValue) return super.getMessage();
    else return TextBridge.empty().append(super.getMessage()).formatted(Formatting.ITALIC);
  }

  /**
   * @see net.minecraft.client.gui.widget.SliderWidget#setFocused(boolean)
   */
  public void setFocused(boolean focused) {
    super.setFocused(focused);
    if (!focused) {
      this.sliderFocused = false;
    }
    updateTooltip();
  }

  @Override
  protected MutableText getNarrationMessage() {
    return getNarrationMessage(super.getMessage());
  }

  @Override
  protected void appendDefaultNarrations(NarrationMessageBuilder builder) {
    super.appendDefaultNarrations(builder);
    if (getValue() == null) {
      builder.put(NarrationPart.USAGE, TextBridge.translatable("narration.mishanguc.button.null"));
    } else if (sliderFocused) {
      builder.put(NarrationPart.USAGE, TextBridge.translatable("narration.mishanguc.button.float_usage.focused"));
    } else {
      builder.put(NarrationPart.USAGE, TextBridge.translatable("narration.mishanguc.button.float_usage"));
    }
  }

  @Contract(value = "_ -> this", mutates = "this")
  protected FloatButtonWidget narratesValueAs(Float2ObjectFunction<MutableText> valueNarrator) {
    this.valueNarrator = valueNarrator;
    return this;
  }
}
