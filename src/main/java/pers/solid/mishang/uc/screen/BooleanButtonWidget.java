package pers.solid.mishang.uc.screen;

import it.unimi.dsi.fastutil.booleans.BooleanConsumer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.Tooltip;
import net.minecraft.client.gui.narration.NarratedElementType;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.client.input.InputWithModifiers;
import net.minecraft.client.input.MouseButtonEvent;
import net.minecraft.client.input.MouseButtonInfo;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.MutableComponent;
import org.jetbrains.annotations.Nullable;
import pers.solid.mishang.uc.MishangUtils;
import pers.solid.mishang.uc.util.TextBridge;

import java.util.function.Function;
import java.util.function.Supplier;

/**
 * 用于处理布尔值的按钮。按下鼠标时切换。
 */
@Environment(EnvType.CLIENT)
public class BooleanButtonWidget extends Button.Plain implements TooltipUpdated {
  public final boolean defaultValue = false;

  /**
   * 通常在没有选中对象时返回 null。
   */
  private final Function<BooleanButtonWidget, @Nullable Boolean> valueGetter;

  private final BooleanConsumer valueSetter;
  public Function<@Nullable Boolean, net.minecraft.network.chat.Component> renderedNameSupplier = null;
  public @Nullable Function<@Nullable Boolean, net.minecraft.network.chat.@Nullable Component> tooltipSupplier = null;
  public @Nullable net.minecraft.network.chat.Component keyboardShortcut = null;
  private Supplier<net.minecraft.network.chat.Component> summaryTextSupplier = null;

  /**
   * 用于布尔值的按钮。
   *
   * @param x           坐标的X值。
   * @param y           坐标的Y值。
   * @param width       按钮的宽度。
   * @param height      按钮的高度。
   * @param message     按钮上显示的文本。是固定的。
   * @param valueGetter 如何获取布尔值？
   * @param valueSetter 如何设置布尔值？
   * @param onPress     按钮按下去的反应。通常为空。
   */
  public BooleanButtonWidget(int x, int y, int width, int height, net.minecraft.network.chat.Component message, Function<BooleanButtonWidget, @Nullable Boolean> valueGetter, BooleanConsumer valueSetter, OnPress onPress) {
    super(x, y, width, height, message, onPress, Button.DEFAULT_NARRATION);
    this.valueGetter = valueGetter;
    this.valueSetter = valueSetter;
    updateTooltip();
  }

  public BooleanButtonWidget setSummaryTextSupplier(Supplier<net.minecraft.network.chat.Component> summaryTextSupplier) {
    this.summaryTextSupplier = summaryTextSupplier;
    return this;
  }

  public BooleanButtonWidget setRenderedNameSupplier(Function<@Nullable Boolean, net.minecraft.network.chat.Component> renderedNameSupplier) {
    this.renderedNameSupplier = renderedNameSupplier;
    return this;
  }

  public BooleanButtonWidget setRenderedName(net.minecraft.network.chat.Component renderedName) {
    this.renderedNameSupplier = ignore -> renderedName;
    return this;
  }

  public BooleanButtonWidget setTooltipSupplier(Function<@Nullable Boolean, net.minecraft.network.chat.@Nullable Component> tooltipSupplier) {
    this.tooltipSupplier = tooltipSupplier;
    return this;
  }

  public BooleanButtonWidget setTooltip(net.minecraft.network.chat.Component tooltip) {
    this.tooltipSupplier = ignore -> tooltip;
    return this;
  }

  public BooleanButtonWidget setKeyboardShortcut(net.minecraft.network.chat.Component text) {
    this.keyboardShortcut = text;
    return this;
  }

  public net.minecraft.network.chat.Component getSummaryMessage() {
    return summaryTextSupplier == null ? super.getMessage() : summaryTextSupplier.get(); // 忽略 renderMessage
  }

  @Override
  public void updateTooltip() {
    final Boolean value = getValue();
    final net.minecraft.network.chat.Component tooltip = tooltipSupplier == null ? null : tooltipSupplier.apply(value);
    final MutableComponent content = value == null ? TextBridge.empty().append(getSummaryMessage()) : CommonComponents.optionStatus(getSummaryMessage(), value);
    final MutableComponent narration = value == null ? TextBridge.empty() : TextBridge.translatable("narration.mishanguc.button.current_value", value ? CommonComponents.OPTION_ON : CommonComponents.OPTION_OFF);
    if (tooltip != null) {
      content.append(CommonComponents.NEW_LINE).append(tooltip);
      narration.append(CommonComponents.NEW_LINE).append(tooltip);
    }
    if (keyboardShortcut != null) {
      MutableComponent composed = MishangUtils.describeShortcut(keyboardShortcut);
      content.append(CommonComponents.NEW_LINE).append(composed);
      narration.append(CommonComponents.NEW_LINE).append(composed);
    }
    setTooltip(Tooltip.create(content, narration));
  }

  public @Nullable Boolean getValue() {
    return valueGetter.apply(this);
  }

  public void setValue(boolean value) {
    valueSetter.accept(value);
    updateTooltip();
  }

  @Override
  public boolean mouseClicked(MouseButtonEvent click, boolean doubled) {
    if (this.active && this.visible && isMouseOver(click.x(), click.y()) && click.button() == 2) {
      this.playDownSound(Minecraft.getInstance().getSoundManager());
      setValue(defaultValue);
      return true;
    } else {
      return super.mouseClicked(click, doubled);
    }
  }

  @Override
  protected boolean isValidClickButton(MouseButtonInfo input) {
    final int button = input.button();
    return button == 0 || button == 1;
  }

  @Override
  public void onPress(InputWithModifiers input) {
    final Boolean value = getValue();
    if (value != null) {
      setValue(!value);
    }
  }

  @Override
  public boolean mouseScrolled(double mouseX, double mouseY, double horizontalAmount, double verticalAmount) {
    final boolean b = super.mouseScrolled(mouseX, mouseY, horizontalAmount, verticalAmount);
    final Boolean value = getValue();
    if (value != null) {
      setValue(!value);
      return true;
    }
    return b;
  }

  @Override
  public net.minecraft.network.chat.Component getMessage() {
    final net.minecraft.network.chat.Component renderedName = renderedNameSupplier == null ? super.getMessage() : renderedNameSupplier.apply(getValue());
    final @Nullable Boolean value = getValue();
    return value == null
        ? renderedName
        : TextBridge.empty()
        .append(renderedName)
        .withStyle(style -> style.withColor(value ? 0xb2ff96 : 0xffac96));
  }

  @Override
  protected MutableComponent createNarrationMessage() {
    // 考虑到部分按钮，比如加粗按钮，显示时只显示“B”，但是事实上复述功能应该复述“加粗”。
    return wrapDefaultNarrationMessage(getSummaryMessage());
  }

  @Override
  protected void defaultButtonNarrationText(NarrationElementOutput builder) {
    super.defaultButtonNarrationText(builder);
    if (getValue() == null) {
      builder.add(NarratedElementType.USAGE, TextBridge.translatable("narration.mishanguc.button.null"));
    } else {
      builder.add(NarratedElementType.USAGE, TextBridge.translatable("narration.mishanguc.button.boolean_usage"));
    }
  }

  @Override
  public void setFocused(boolean focused) {
    super.setFocused(focused);
    updateTooltip();
  }
}
