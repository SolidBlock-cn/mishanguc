package pers.solid.mishang.uc.util;

import net.minecraft.util.math.Direction;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Range;

@ApiStatus.AvailableSince("1.1.0")
public record LineOffset(Direction offsetDirection, @Range(from = 0, to = Integer.MAX_VALUE) int level) {
  public LineOffset {
    if (level < 0) {
      throw new IllegalArgumentException();
    }
  }

  public static LineOffset of(Direction direction, @Range(from = Integer.MIN_VALUE, to = Integer.MAX_VALUE) int level) {
    if (level == 0) {
      return null;
    } else if (level > 0) {
      return new LineOffset(direction, level);
    } else {
      return new LineOffset(direction.getOpposite(), -level);
    }
  }
}
