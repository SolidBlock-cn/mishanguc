package pers.solid.mishang.uc;

import net.minecraft.state.property.EnumProperty;
import pers.solid.mishang.uc.block.HandrailStairBlock;
import pers.solid.mishang.uc.util.HorizontalCornerDirection;

public final class MishangucProperties {
  public static final EnumProperty<HorizontalCornerDirection> HORIZONTAL_CORNER_FACING =
      EnumProperty.of("facing", HorizontalCornerDirection.class);
  public static final EnumProperty<HandrailStairBlock.Position> HANDRAIL_STAIR_POSITION = EnumProperty.of("position", HandrailStairBlock.Position.class);
  public static final EnumProperty<HandrailStairBlock.Shape> HANDRAIL_STAIR_SHAPE = EnumProperty.of("shape", HandrailStairBlock.Shape.class);
}
