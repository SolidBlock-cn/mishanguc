package pers.solid.mishang.uc;

import net.minecraft.state.property.EnumProperty;
import pers.solid.mishang.uc.block.HorizontalCornerDirection;

public class ModProperties {
  public static final EnumProperty<HorizontalCornerDirection> HORIZONTAL_CORNER_FACING = EnumProperty.of("facing",
          HorizontalCornerDirection.class);
}
