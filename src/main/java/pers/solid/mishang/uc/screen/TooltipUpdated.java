package pers.solid.mishang.uc.screen;

/**
 * 这个接口主要用于 {@link BooleanButtonWidget} 和 {@link FloatButtonWidget}。当更改选择的对象时，更新其 tooltip。
 */
public interface TooltipUpdated {
  void updateTooltip();
}
