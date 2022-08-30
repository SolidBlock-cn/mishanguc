package pers.solid.mishang.uc;

import net.minecraft.state.property.BooleanProperty;
import net.minecraft.state.property.EnumProperty;
import org.jetbrains.annotations.ApiStatus;
import pers.solid.mishang.uc.block.HandrailStairBlock;
import pers.solid.mishang.uc.util.HorizontalCornerDirection;

public final class MishangucProperties {
  public static final EnumProperty<HorizontalCornerDirection> HORIZONTAL_CORNER_FACING =
      EnumProperty.of("facing", HorizontalCornerDirection.class);
  public static final EnumProperty<HandrailStairBlock.Position> HANDRAIL_STAIR_POSITION = EnumProperty.of("position", HandrailStairBlock.Position.class);
  public static final EnumProperty<HandrailStairBlock.Shape> HANDRAIL_STAIR_SHAPE = EnumProperty.of("shape", HandrailStairBlock.Shape.class);

  /**
   * 对于直角与斜线混合的道路，其斜线部分是否在顶部。
   */
  @ApiStatus.AvailableSince("0.2.4")
  public static final BooleanProperty BEVEL_TOP = BooleanProperty.of("bevel_top");
}
