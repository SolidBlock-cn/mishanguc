package pers.solid.mishang.uc.screen;

import it.unimi.dsi.fastutil.floats.Float2ObjectFunction;
import it.unimi.dsi.fastutil.floats.FloatConsumer;
import it.unimi.dsi.fastutil.objects.Object2FloatFunction;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.navigation.GuiNavigationType;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.tooltip.Tooltip;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.util.InputUtil;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Util;
import org.lwjgl.glfw.GLFW;
import pers.solid.mishang.uc.util.TextBridge;

/**
 * 用于处理浮点数的按钮。按下鼠标时增大，但是按住 shift 则会减小。滚动鼠标滚轮也会减小。
 */
@Environment(EnvType.CLIENT)
public class FloatButtonWidget extends ButtonWidget {
  public final Float2ObjectFunction<Text> tooltipSupplier;
  private final Object2FloatFunction<FloatButtonWidget> valueGetter;
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
   * 按钮当前的最小值。若低于最小值，则从最大值开始循环，但是如果没有最大值时除外。
   */
  public float min = Float.NEGATIVE_INFINITY;
  /**
   * 按钮当前的最大值。若高于最大值，则从最小值开始循环，但是如果没有最小值时除外。
   */
  public float max = Float.POSITIVE_INFINITY;

  public FloatButtonWidget(
      int x,
      int y,
      int width,
      int height,
      Text message,
      Float2ObjectFunction<Text> tooltipSupplier,
      Object2FloatFunction<FloatButtonWidget> valueGetter,
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
    setTooltip(Tooltip.of(this.tooltipSupplier.get(getValue())));
  }

  @Override
  public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
    if (this.isHovered()) updateTooltip();
    super.render(matrices, mouseX, mouseY, delta);
  }

  public float getValue() {
    return valueGetter.getFloat(this);
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
    setTooltip(Tooltip.of(tooltipSupplier.get(getValue())));
  }

  @Override
  protected void drawScrollableText(MatrixStack matrices, TextRenderer textRenderer, int xMargin, int color) {
    if (!sliderFocused || Util.getMeasuringTimeMs() % 1000 > 500) {
      // 在 sliderFocused 的情况下，文字应该闪烁
      super.drawScrollableText(matrices, textRenderer, xMargin, color);
    }
  }

  @Override
  public boolean mouseClicked(double mouseX, double mouseY, int button) {
    final boolean b = super.mouseClicked(mouseX, mouseY, button);
    if (this.active && this.visible && clicked(mouseX, mouseY)) {
      switch (button) { // 这种情况下直接采用了 onPress，所以直接略。
        case 0, 1 -> {
          if (Screen.hasAltDown() && Screen.hasShiftDown()) {
            setValue(defaultValue);
          } else {
            setValue(getValue()
                + (Screen.hasShiftDown() || button == 1 ? -1 : 1)
                * step
                * (Screen.hasControlDown() ? 8 : 1)
                * (Screen.hasAltDown() ? 0.125f : 1));
          }
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
    setValue((float) (getValue()
        + amount
        * (Screen.hasShiftDown() ? 1 : -1)
        * (Screen.hasControlDown() ? 8 : 1)
        * step
        * (Screen.hasAltDown() ? 0.125f : 1)));
    super.mouseScrolled(mouseX, mouseY, amount);
    return true;
  }


  /**
   * @see net.minecraft.client.gui.widget.SliderWidget#keyPressed(int, int, int)
   */
  public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
    if (keyCode != GLFW.GLFW_KEY_SPACE && keyCode != GLFW.GLFW_KEY_ENTER && keyCode != GLFW.GLFW_KEY_KP_ENTER) {
      if (this.sliderFocused) {
        boolean decreases = keyCode == GLFW.GLFW_KEY_LEFT || keyCode == GLFW.GLFW_KEY_DOWN;
        final var handle = MinecraftClient.getInstance().getWindow().getHandle();
        if (keyCode == GLFW.GLFW_KEY_LEFT && InputUtil.isKeyPressed(handle, GLFW.GLFW_KEY_RIGHT)
            || keyCode == GLFW.GLFW_KEY_RIGHT && InputUtil.isKeyPressed(handle, InputUtil.GLFW_KEY_LEFT)) {
          // 当同时按下左右时，设为默认值。
          setValue(defaultValue);
          return true;
        } else if (decreases || keyCode == GLFW.GLFW_KEY_RIGHT || keyCode == GLFW.GLFW_KEY_UP) {
          float sign = decreases ? -1.0F : 1.0F;
          this.setValue(getValue() + sign
              * (Screen.hasShiftDown() ? 1 : -1)
              * (Screen.hasControlDown() ? 8 : 1)
              * step
              * (Screen.hasAltDown() ? 0.125f : 1));
          return true;
        }
      }
      return false;
    } else {
      this.sliderFocused = !this.sliderFocused;
      return true;
    }
  }

  @Override
  public Text getMessage() {
    if (getValue() == defaultValue) return super.getMessage();
    else return TextBridge.literal("").append(super.getMessage()).formatted(Formatting.ITALIC);
  }

  /**
   * @see net.minecraft.client.gui.widget.SliderWidget#setFocused(boolean)
   */
  public void setFocused(boolean focused) {
    super.setFocused(focused);
    if (!focused) {
      this.sliderFocused = false;
    } else {
      GuiNavigationType guiNavigationType = MinecraftClient.getInstance().getNavigationType();
      if (guiNavigationType == GuiNavigationType.MOUSE || guiNavigationType == GuiNavigationType.KEYBOARD_TAB) {
        this.sliderFocused = true;
      }
    }
  }
}
