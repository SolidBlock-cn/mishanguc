package pers.solid.mishang.uc.screen;

import it.unimi.dsi.fastutil.floats.Float2ObjectFunction;
import it.unimi.dsi.fastutil.floats.FloatConsumer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.narration.NarrationMessageBuilder;
import net.minecraft.client.gui.screen.narration.NarrationPart;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.util.InputUtil;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.screen.ScreenTexts;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.Nullable;
import org.lwjgl.glfw.GLFW;
import pers.solid.mishang.uc.MishangUtils;
import pers.solid.mishang.uc.util.TextBridge;

import java.util.function.BiFunction;
import java.util.function.Function;

/**
 * 用于处理浮点数的按钮。按下鼠标时增大，但是按住 shift 则会减小。滚动鼠标滚轮也会减小。
 */
@Environment(EnvType.CLIENT)
public class FloatButtonWidget extends ButtonWidget implements TooltipUpdated {
  private final Function<FloatButtonWidget, @Nullable Float> valueGetter;
  private final FloatConsumer valueSetter;
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

  public static final Float2ObjectFunction<MutableText> DEFAULT_VALUE_NARRATOR = value -> TextBridge.literal(MishangUtils.numberToString(value));
  private Float2ObjectFunction<MutableText> valueToText = DEFAULT_VALUE_NARRATOR;

  public FloatButtonWidget(int x, int y, int width, int height, Text message, Function<FloatButtonWidget, Float> valueGetter, FloatConsumer valueSetter, PressAction onPress) {
    super(
        x,
        y,
        width,
        height,
        message,
        onPress);
    this.valueGetter = valueGetter;
    this.valueSetter = valueSetter;
  }

  @Override
  public void updateTooltip() {
    final Float value = getValue();
    if (value != null) {
      final MutableText valueText = valueToText.get(value.floatValue());
      renderedTooltip = ScreenTexts.composeGenericOptionText(getSummaryMessage(), valueText);
    } else {
      renderedTooltip = getSummaryMessage();
    }
  }

  private Text renderedTooltip;

  public Text getRenderedTooltip() {
    return renderedTooltip;
  }

  @Override
  public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
    super.render(matrices, mouseX, mouseY, delta);
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
  public boolean mouseClicked(double mouseX, double mouseY, int button) {
    final boolean b = super.mouseClicked(mouseX, mouseY, button);
    if (this.active && this.visible && clicked(mouseX, mouseY)) {
      onPress(button);
    }
    return b;
  }

  public void onPress(int button) {
    final Float value = getValue();
    if (value != null) {
      switch (button) { // 这种情况下直接采用了 onPress，所以直接略。
        case 0, 1 -> setValue(value
            + (Screen.hasShiftDown() || button == 1 ? -1 : 1)
            * step
            * (Screen.hasControlDown() ? 8 : 1)
            * (Screen.hasAltDown() ? 0.125f : 1));
        case 2 -> setValue(defaultValue);
        default -> {
        }
      }
    }
  }

  @Override
  public void onPress() {
    this.onPress(0);
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
      if (isFocused()) {
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
      return super.keyPressed(keyCode, scanCode, modifiers);
    }
  }

  @Override
  public Text getMessage() {
    final Float value = getValue();
    if (renderedNameSupplier != null) {
      final Text apply = renderedNameSupplier.apply(value, valueToText.apply(value));
      if (apply != null) return apply;
    }
    if (value == null || value == defaultValue) {
      return super.getMessage();
    } else {
      return TextBridge.empty().append(super.getMessage()).formatted(Formatting.ITALIC);
    }
  }

  public interface NameRenderer extends BiFunction<@Nullable Float, Text, @Nullable Text> {
    @Override
    @Nullable Text apply(@Nullable Float value, Text valueText);
  }

  public NameRenderer renderedNameSupplier = null;

  public FloatButtonWidget setRenderedNameSupplier(NameRenderer renderedNameSupplier) {
    this.renderedNameSupplier = renderedNameSupplier;
    return this;
  }


  public Text getSummaryMessage() {
    return super.getMessage();
  }

  @Override
  protected MutableText getNarrationMessage() {
    return getNarrationMessage(getSummaryMessage());
  }

  @Override
  protected void appendDefaultNarrations(NarrationMessageBuilder builder) {
    super.appendDefaultNarrations(builder);
    if (getValue() == null) {
      builder.put(NarrationPart.USAGE, TextBridge.translatable("narration.mishanguc.button.null"));
    } else {
      builder.put(NarrationPart.USAGE, TextBridge.translatable("narration.mishanguc.button.float_usage"));
    }
  }

  @Contract(value = "_ -> this", mutates = "this")
  protected FloatButtonWidget nameValueAs(Float2ObjectFunction<MutableText> valueToText) {
    this.valueToText = valueToText;
    return this;
  }
}
