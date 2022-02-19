package pers.solid.mishang.uc.screen;

import it.unimi.dsi.fastutil.floats.Float2ObjectFunction;
import it.unimi.dsi.fastutil.floats.FloatConsumer;
import it.unimi.dsi.fastutil.objects.Object2FloatFunction;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import pers.solid.mishang.uc.MishangUc;

/** 用于处理浮点数的按钮。按下鼠标时增大，但是按住 shift 则会减小。滚动鼠标滚轮也会减小。 */
@Environment(EnvType.CLIENT)
public class FloatButtonWidget extends ButtonWidget {
  public final Float2ObjectFunction<Text> messageSupplier;
  /** 按钮的默认值。可以按鼠标中键或者按住 Alt + Shift 点击以恢复。 */
  public final float defaultValue = 0;

  private final Object2FloatFunction<FloatButtonWidget> valueGetter;
  private final FloatConsumer valueSetter;
  /** 按钮的步长，默认为1。 */
  public float step = 1;
  /** 按钮当前的最小值。若低于最小值，则从最大值开始循环，但是如果没有最大值时除外。 */
  public float min = Float.NEGATIVE_INFINITY;
  /** 按钮当前的最大值。若高于最大值，则从最小值开始循环，但是如果没有最小值时除外。 */
  public float max = Float.POSITIVE_INFINITY;

  public FloatButtonWidget(
      int x,
      int y,
      int width,
      int height,
      Float2ObjectFunction<Text> messageSupplier,
      Object2FloatFunction<FloatButtonWidget> valueGetter,
      FloatConsumer valueSetter,
      PressAction onPress) {
    super(
        x,
        y,
        width,
        height,
        messageSupplier.get(0),
        onPress,
        (a, b, c, d) -> new TranslatableText("message.mishanguc.text_field.tooltip"));
    this.messageSupplier = messageSupplier;
    this.valueGetter = valueGetter;
    this.valueSetter = valueSetter;
  }

  public float getValue() {
    return valueGetter.getFloat(this);
  }

  /** 设置该按钮的值。会受到最小值和最大值的限制。 */
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
    if (value<min||value>max) {
      MishangUc.MISHANG_LOGGER.error("The value adjusted does not meet requirements: value={}, min={}, max={}", value,min,max);
    }
    valueSetter.accept(value);
    setMessage(messageSupplier.get(value));
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
            setValue(
                getValue()
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
    setValue(
        (float)
            (getValue()
                + amount
                    * (Screen.hasShiftDown() ? 1 : -1)
                    * (Screen.hasControlDown() ? 8 : 1)
                    * step
                    * (Screen.hasAltDown() ? 0.125f : 1)));
    super.mouseScrolled(mouseX, mouseY, amount);
    return true;
  }

  @Override
  public Text getMessage() {
    return messageSupplier.get(getValue());
  }
}
