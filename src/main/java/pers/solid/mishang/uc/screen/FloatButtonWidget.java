package pers.solid.mishang.uc.screen;

import com.mojang.blaze3d.platform.InputConstants;
import it.unimi.dsi.fastutil.floats.Float2FloatFunction;
import it.unimi.dsi.fastutil.floats.Float2ObjectFunction;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ActiveTextCollector;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.Tooltip;
import net.minecraft.client.gui.narration.NarratedElementType;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.client.input.InputWithModifiers;
import net.minecraft.client.input.KeyEvent;
import net.minecraft.client.input.MouseButtonEvent;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.util.Util;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.Nullable;
import org.lwjgl.glfw.GLFW;
import pers.solid.mishang.uc.MishangUtils;

import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Function;

import static pers.solid.mishang.uc.screen.MishangScreenUtil.*;

/**
 * 用于处理浮点数的按钮。按下鼠标时增大，但是按住 shift 则会减小。滚动鼠标滚轮也会减小。
 */
@Environment(EnvType.CLIENT)
public class FloatButtonWidget extends Button.Plain implements TooltipUpdated {
  private final Function<FloatButtonWidget, @Nullable Float> valueGetter;
  private final ValueConsumer valueSetter;
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

  public static final Float2ObjectFunction<MutableComponent> DEFAULT_VALUE_NARRATOR = value -> {
    return Component.literal(MishangUtils.numberToString(value));
  };
  private Float2ObjectFunction<MutableComponent> valueToText = DEFAULT_VALUE_NARRATOR;

  public FloatButtonWidget(int x, int y, int width, int height, net.minecraft.network.chat.Component message, Function<FloatButtonWidget, Float> valueGetter, ValueConsumer valueSetter, OnPress onPress) {
    super(x, y, width, height, message, onPress, Button.DEFAULT_NARRATION);
    this.valueGetter = valueGetter;
    this.valueSetter = valueSetter;
    updateTooltip();
  }

  @Override
  public void updateTooltip() {
    final Float value = getValue();
    if (value != null) {
      final MutableComponent valueText = valueToText.get(value.floatValue());
      setTooltip(Tooltip.create(CommonComponents.optionNameValue(getSummaryMessage(), valueText), Component.translatable("narration.mishanguc.button.current_value", valueText)));
    } else {
      setTooltip(Tooltip.create(getSummaryMessage(), Component.empty()));
    }
  }

  public @Nullable Float getValue() {
    return valueGetter.apply(this);
  }

  public void setAllSameValue(float value) {
    setValue(n -> value);
  }

  /**
   * 设置该按钮的值。会受到最小值和最大值的限制。
   */
  public void setValue(Float2FloatFunction valueFunction) {
    final Float original = getValue();
    if (original == null) return;
    valueSetter.accept(valueFunction.andThenFloat(value -> {
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
      return value;
    }), original);
    updateTooltip();
  }

  @Override
  protected void extractDefaultLabel(ActiveTextCollector output) {
    // 对应 1.21.10 之前的 drawScrollableText
    if (!sliderFocused || Util.getMillis() % 1000 > 500) {
      // 在 sliderFocused 的情况下，文字应该闪烁
      super.extractDefaultLabel(output);
    }
  }

  @Override
  public void onPress(InputWithModifiers input) {
    if (input instanceof MouseButtonEvent click) {
      final int button = click.button();
      switch (button) { // 这种情况下直接采用了 onPress，所以直接略。
        case 0, 1 -> setValue(value -> value
            + (input.hasShiftDown() || button == 1 ? -1 : 1)
            * step
            * (input.hasControlDown() ? 8 : 1)
            * (input.hasAltDown() ? 0.125f : 1));
        case 2 -> setAllSameValue(defaultValue);
      }
    }
  }

  @Override
  public boolean mouseClicked(MouseButtonEvent click, boolean doubled) {
    if (this.active && this.visible && isMouseOver(click.x(), click.y())) {
      this.playDownSound(Minecraft.getInstance().getSoundManager());
      onPress(click);
      return true;
    }
    return false;
  }

  @Override
  public boolean mouseScrolled(double mouseX, double mouseY, double horizontalAmount, double verticalAmount) {
    setValue(value -> (float) (value
        + verticalAmount
        * (hasShiftDown() ? -1 : 1)
        * (hasControlDown() ? 8 : 1)
        * step * scrollMultiplier
        * (hasAltDown() ? 0.125f : 1)));
    super.mouseScrolled(mouseX, mouseY, horizontalAmount, verticalAmount);
    return true;
  }

  /**
   * @see net.minecraft.client.gui.components.AbstractSliderButton#keyPressed
   */

  public boolean keyPressed(KeyEvent input) {
    if (!input.isSelection()) {
      if (this.sliderFocused) {
        boolean decreases = input.isLeft() || input.isDown();
        final var window = Minecraft.getInstance().getWindow();
        if (input.isLeft() && InputConstants.isKeyDown(window, GLFW.GLFW_KEY_RIGHT)
            || input.isRight() && InputConstants.isKeyDown(window, InputConstants.KEY_LEFT)) {
          // 当同时按下左右时，设为默认值。
          setAllSameValue(defaultValue);
          return true;
        } else if ((decreases || input.isRight() || input.isUp()) && getValue() != null) {
          final float multiplier = input.isRight() || input.isLeft() ? rightArrowStepMultiplier : upArrowStepMultiplier;
          float sign = decreases ? -1.0F : 1.0F;
          setValue(value -> value + sign
              * (input.hasShiftDown() ? -1 : 1)
              * (input.hasControlDown() ? 8 : 1)
              * step * multiplier
              * (input.hasAltDown() ? 0.125f : 1));
          return true;
        }
      }
      return false;
    } else {
      this.sliderFocused = getValue() != null && !this.sliderFocused;
      this.playDownSound(Minecraft.getInstance().getSoundManager());
      return true;
    }
  }

  @Override
  public net.minecraft.network.chat.Component getMessage() {
    final Float value = getValue();
    if (renderedNameSupplier != null) {
      final net.minecraft.network.chat.Component apply = renderedNameSupplier.apply(value, valueToText.apply(value));
      if (apply != null) return apply;
    }
    if (value == null || value == defaultValue) {
      return super.getMessage();
    } else {
      return Component.empty().append(super.getMessage()).withStyle(ChatFormatting.ITALIC);
    }
  }

  @Environment(EnvType.CLIENT)
  public interface NameRenderer extends BiFunction<@Nullable Float, net.minecraft.network.chat.Component, net.minecraft.network.chat.@Nullable Component> {
    @Override
    @Nullable
    net.minecraft.network.chat.Component apply(@Nullable Float value, net.minecraft.network.chat.Component valueText);
  }

  public NameRenderer renderedNameSupplier = null;

  public FloatButtonWidget setRenderedNameSupplier(NameRenderer renderedNameSupplier) {
    this.renderedNameSupplier = renderedNameSupplier;
    return this;
  }


  public net.minecraft.network.chat.Component getSummaryMessage() {
    return super.getMessage();
  }

  /**
   * @see net.minecraft.client.gui.components.AbstractSliderButton#setFocused(boolean)
   */
  public void setFocused(boolean focused) {
    super.setFocused(focused);
    if (!focused) {
      this.sliderFocused = false;
    }
    updateTooltip();
  }

  @Override
  protected MutableComponent createNarrationMessage() {
    return wrapDefaultNarrationMessage(getSummaryMessage());
  }

  @Override
  protected void defaultButtonNarrationText(NarrationElementOutput builder) {
    super.defaultButtonNarrationText(builder);
    if (getValue() == null) {
      builder.add(NarratedElementType.USAGE, Component.translatable("narration.mishanguc.button.null"));
    } else if (sliderFocused) {
      builder.add(NarratedElementType.USAGE, Component.translatable("narration.mishanguc.button.float_usage.focused"));
    } else {
      builder.add(NarratedElementType.USAGE, Component.translatable("narration.mishanguc.button.float_usage"));
    }
  }

  @Contract(value = "_ -> this", mutates = "this")
  protected FloatButtonWidget nameValueAs(Float2ObjectFunction<MutableComponent> valueToText) {
    this.valueToText = valueToText;
    return this;
  }

  @FunctionalInterface
  public interface ValueConsumer extends BiConsumer<Float2FloatFunction, Float> {
    @Override
    void accept(Float2FloatFunction valueFunction, Float original);
  }
}
