package pers.solid.mishang.uc.screen;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

/**
 * 这个接口主要用于 {@link BooleanButtonWidget} 和 {@link FloatButtonWidget}。当更改选择的对象时，更新其 tooltip。
 */
@Environment(EnvType.CLIENT)
public interface TooltipUpdated {
  void updateTooltip();
}
