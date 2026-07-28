package pers.solid.mishang.uc;

import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import org.jetbrains.annotations.ApiStatus;
import pers.solid.mishang.uc.block.HandrailStairBlock;
import pers.solid.mishang.uc.util.HorizontalCornerDirection;

public final class MishangucProperties {
  public static final EnumProperty<HorizontalCornerDirection> HORIZONTAL_CORNER_FACING =
      EnumProperty.create("facing", HorizontalCornerDirection.class);
  public static final EnumProperty<HandrailStairBlock.Position> HANDRAIL_STAIR_POSITION = EnumProperty.create("position", HandrailStairBlock.Position.class);
  public static final EnumProperty<HandrailStairBlock.Shape> HANDRAIL_STAIR_SHAPE = EnumProperty.create("shape", HandrailStairBlock.Shape.class);

  /**
   * 对于直角与斜线混合的道路，其斜线部分是否在顶部。
   */
  @ApiStatus.AvailableSince("0.2.4")
  public static final BooleanProperty BEVEL_TOP = BooleanProperty.create("bevel_top");
}
