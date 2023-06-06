package pers.solid.mishang.uc.screen;

import it.unimi.dsi.fastutil.booleans.BooleanConsumer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.screen.narration.NarrationMessageBuilder;
import net.minecraft.client.gui.screen.narration.NarrationPart;
import net.minecraft.client.gui.tooltip.Tooltip;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.screen.ScreenTexts;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import org.jetbrains.annotations.Nullable;
import pers.solid.mishang.uc.MishangUtils;
import pers.solid.mishang.uc.util.TextBridge;

import java.util.function.Function;

/**
 * 用于处理布尔值的按钮。按下鼠标时切换。
 */
@Environment(EnvType.CLIENT)
public class BooleanButtonWidget extends ButtonWidget implements TooltipUpdated {
  public final boolean defaultValue = false;

  /**
   * 通常在没有选中对象时返回 null。
   */
  private final Function<BooleanButtonWidget, @Nullable Boolean> valueGetter;

  private final BooleanConsumer valueSetter;

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
  public BooleanButtonWidget(int x, int y, int width, int height, Text message, Function<BooleanButtonWidget, @Nullable Boolean> valueGetter, BooleanConsumer valueSetter, PressAction onPress) {
    super(x, y, width, height, message, onPress, ButtonWidget.DEFAULT_NARRATION_SUPPLIER);
    this.valueGetter = valueGetter;
    this.valueSetter = valueSetter;
    updateTooltip();
  }

  public Function<@Nullable Boolean, Text> renderedNameSupplier = null;

  public BooleanButtonWidget setRenderedNameSupplier(Function<@Nullable Boolean, Text> renderedNameSupplier) {
    this.renderedNameSupplier = renderedNameSupplier;
    return this;
  }

  public BooleanButtonWidget setRenderedName(Text renderedName) {
    this.renderedNameSupplier = ignore -> renderedName;
    return this;
  }

  public @Nullable Function<@Nullable Boolean, @Nullable Text> tooltipSupplier = null;

  public BooleanButtonWidget setTooltipSupplier(Function<@Nullable Boolean, @Nullable Text> tooltipSupplier) {
    this.tooltipSupplier = tooltipSupplier;
    return this;
  }

  public BooleanButtonWidget setTooltip(Text tooltip) {
    this.tooltipSupplier = ignore -> tooltip;
    return this;
  }

  public @Nullable Text keyboardShortcut = null;

  public BooleanButtonWidget setKeyboardShortcut(Text text) {
    this.keyboardShortcut = text;
    return this;
  }

  public Text getSummaryMessage() {
    return super.getMessage(); // 忽略 renderMessage
  }

  @Override
  public void updateTooltip() {
    final Boolean value = getValue();
    final Text tooltip = tooltipSupplier == null ? null : tooltipSupplier.apply(value);
    final MutableText content = value == null ? TextBridge.empty().append(getSummaryMessage()) : ScreenTexts.composeToggleText(getSummaryMessage(), value);
    final MutableText narration = value == null ? TextBridge.empty() : TextBridge.translatable("narration.mishanguc.button.current_value", value ? ScreenTexts.ON : ScreenTexts.OFF);
    if (tooltip != null) {
      content.append(ScreenTexts.LINE_BREAK).append(tooltip);
      narration.append(ScreenTexts.LINE_BREAK).append(tooltip);
    }
    if (keyboardShortcut != null) {
      MutableText composed = MishangUtils.describeShortcut(keyboardShortcut);
      content.append(ScreenTexts.LINE_BREAK).append(composed);
      narration.append(ScreenTexts.LINE_BREAK).append(composed);
    }
    setTooltip(Tooltip.of(content, narration));
  }

  @Override
  public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
//    if (this.isHovered()) updateTooltip();
    super.render(matrices, mouseX, mouseY, delta);
  }

  public @Nullable Boolean getValue() {
    return valueGetter.apply(this);
  }

  public void setValue(boolean value) {
    valueSetter.accept(value);
    updateTooltip();
  }

  @Override
  public boolean mouseClicked(double mouseX, double mouseY, int button) {
    if (this.active && this.visible && clicked(mouseX, mouseY) && button == 2) {
        setValue(defaultValue);
        return true;
      } else {
      return super.mouseClicked(mouseX, mouseY, button);
    }
  }

  @Override
  public void onPress() {
        final Boolean value = getValue();
        if (value != null) {
          setValue(!value);
      }
    }

  @Override
  public boolean mouseScrolled(double mouseX, double mouseY, double amount) {
    final boolean b = super.mouseScrolled(mouseX, mouseY, amount);
    final Boolean value = getValue();
    if (value != null) {
      setValue(!value);
      return true;
    }
    return b;
  }

  @Override
  public Text getMessage() {
    final Text renderedName = renderedNameSupplier == null ? super.getMessage() : renderedNameSupplier.apply(getValue());
    final @Nullable Boolean value = getValue();
    return value == null
        ? renderedName
        : TextBridge.empty()
        .append(renderedName)
        .styled(style -> style.withColor(value ? 0xb2ff96 : 0xffac96));
  }

  @Override
  protected MutableText getNarrationMessage() {
    // 考虑到部分按钮，比如加粗按钮，显示时只显示“B”，但是事实上复述功能应该复述“加粗”。
    return getNarrationMessage(getSummaryMessage());
  }

  @Override
  protected void appendDefaultNarrations(NarrationMessageBuilder builder) {
    super.appendDefaultNarrations(builder);
    if (getValue() == null) {
      builder.put(NarrationPart.USAGE, TextBridge.translatable("narration.mishanguc.button.null"));
    } else {
      builder.put(NarrationPart.USAGE, TextBridge.translatable("narration.mishanguc.button.boolean_usage"));
    }
  }

  @Override
  public void setFocused(boolean focused) {
    super.setFocused(focused);
    updateTooltip();
  }
}
