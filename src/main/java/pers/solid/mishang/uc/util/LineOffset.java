package pers.solid.mishang.uc.util;

import net.minecraft.util.math.Direction;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Range;

import java.util.Objects;

@ApiStatus.AvailableSince("1.1.0")
public final class LineOffset {
  private final Direction offsetDirection;
  private final @Range(from = 0, to = Integer.MAX_VALUE) int level;

  public LineOffset(Direction offsetDirection, int level) {
    this.offsetDirection = offsetDirection;
    this.level = level;
    if (level < 0) {
      throw new IllegalArgumentException();
    }
  }

  public Direction offsetDirection() {
    return offsetDirection;
  }

  public int level() {
    return level;
  }

  @Override
  public boolean equals(Object obj) {
    if (obj == this) return true;
    if (obj == null || obj.getClass() != this.getClass()) return false;
    LineOffset that = (LineOffset) obj;
    return Objects.equals(this.offsetDirection, that.offsetDirection) &&
        this.level == that.level;
  }

  @Override
  public int hashCode() {
    return Objects.hash(offsetDirection, level);
  }

  @Override
  public String toString() {
    return "LineOffset[" +
        "offsetDirection=" + offsetDirection + ", " +
        "level=" + level + ']';
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
