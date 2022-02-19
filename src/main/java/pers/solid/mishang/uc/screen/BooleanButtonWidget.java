package pers.solid.mishang.uc.screen;

import it.unimi.dsi.fastutil.booleans.BooleanConsumer;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.text.Text;
import org.jetbrains.annotations.Nullable;

import java.util.function.Function;

/** 用于处理布尔值的按钮。按下鼠标时切换。 */
public class BooleanButtonWidget extends ButtonWidget {
  public final Function<@Nullable Boolean, Text> messageSupplier;
  /** 通常在没有选中对象时返回 null。 */
  private final Function<BooleanButtonWidget, @Nullable Boolean> valueGetter;

  private final BooleanConsumer valueSetter;
  public final boolean defaultValue = false;

  public BooleanButtonWidget(
      int x,
      int y,
      int width,
      int height,
      Function<BooleanButtonWidget, @Nullable Boolean> valueGetter,
      BooleanConsumer valueSetter,
      Function<@Nullable Boolean, Text> messageSupplier,
      PressAction onPress) {
    super(x, y, width, height, messageSupplier.apply(null), onPress);
    this.valueGetter = valueGetter;
    this.valueSetter = valueSetter;
    this.messageSupplier = messageSupplier;
  }

  public @Nullable Boolean getValue() {
    return valueGetter.apply(this);
  }

  public void setValue(boolean value) {
    valueSetter.accept(value);
  }

  @Override
  public boolean mouseClicked(double mouseX, double mouseY, int button) {
    final boolean b = super.mouseClicked(mouseX, mouseY, button);
    if (this.active && this.visible && clicked(mouseX, mouseY)) {
      if (button == 2) {
        setValue(defaultValue);
        return true;
      } else {
        final Boolean value = getValue();
        if (value != null) {
          setValue(!value);
          return true;
        }
      }
    }
    return b;
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
    return messageSupplier.apply(getValue());
  }
}
