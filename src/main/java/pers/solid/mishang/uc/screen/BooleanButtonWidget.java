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
import pers.solid.mishang.uc.util.TextBridge;

import java.util.function.Function;

/**
 * 用于处理布尔值的按钮。按下鼠标时切换。
 */
@Environment(EnvType.CLIENT)
public class BooleanButtonWidget extends ButtonWidget {
  public final Function<@Nullable Boolean, Text> tooltipSupplier;
  public final boolean defaultValue = false;

  /**
   * 通常在没有选中对象时返回 null。
   */
  private final Function<BooleanButtonWidget, @Nullable Boolean> valueGetter;

  private final BooleanConsumer valueSetter;

  /**
   * 用于布尔值的按钮。
   *
   * @param x               坐标的X值。
   * @param y               坐标的Y值。
   * @param width           按钮的宽度。
   * @param height          按钮的高度。
   * @param message         按钮上显示的文本。是固定的。
   * @param valueGetter     如何获取布尔值？
   * @param valueSetter     如何设置布尔值？
   * @param tooltipSupplier 根据布尔值返回其文本内容。该文本将会用于设置 {@code atom}。
   * @param onPress         按钮按下去的反应。通常为空。
   */
  public BooleanButtonWidget(
      int x,
      int y,
      int width,
      int height,
      Text message,
      Function<BooleanButtonWidget, @Nullable Boolean> valueGetter,
      BooleanConsumer valueSetter,
      Function<@Nullable Boolean, Text> tooltipSupplier,
      PressAction onPress) {
    super(
        x,
        y,
        width,
        height,
        message,
        onPress,
        ButtonWidget.DEFAULT_NARRATION_SUPPLIER);
    this.valueGetter = valueGetter;
    this.valueSetter = valueSetter;
    this.tooltipSupplier = tooltipSupplier;
    updateTooltip();
  }

  public void updateTooltip() {
    final Boolean value = getValue();
    final Text tooltip = tooltipSupplier.apply(value);
    setTooltip(Tooltip.of(tooltip, value == null ? TextBridge.empty() : TextBridge.translatable("narration.mishanguc.button.current_value", value ? ScreenTexts.ON : ScreenTexts.OFF)));
  }

  @Override
  public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
    if (this.isHovered()) updateTooltip();
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
    final Text message = super.getMessage();
    final @Nullable Boolean value = getValue();
    return value == null
        ? message
        : TextBridge.empty()
        .append(message)
        .styled(style -> style.withColor(value ? 0xb2ff96 : 0xffac96));
  }

  private boolean narrateTooltipAsMessage = false;

  /**
   * 像“B”、“I”之类的按钮，其名称不宜被直接复述，这种情况下，直接复述其提示。
   */
  public BooleanButtonWidget narrateTooltipAsMessage(boolean value) {
    this.narrateTooltipAsMessage = value;
    return this;
  }

  @Override
  protected MutableText getNarrationMessage() {
    return narrateTooltipAsMessage ? getNarrationMessage(tooltipSupplier.apply(null)) : super.getNarrationMessage();
  }

  @Override
  protected void appendDefaultNarrations(NarrationMessageBuilder builder) {
    super.appendDefaultNarrations(builder);
    final Boolean value = valueGetter.apply(this);
    if (value != null) {
      builder.put(NarrationPart.HINT, TextBridge.translatable("narration.mishanguc.button.current_value", value ? ScreenTexts.ON : ScreenTexts.OFF));
    } else {
      builder.put(NarrationPart.HINT, TextBridge.empty());
    }
    builder.put(NarrationPart.USAGE, TextBridge.translatable("narration.mishanguc.button.boolean_usage"));
  }
}
