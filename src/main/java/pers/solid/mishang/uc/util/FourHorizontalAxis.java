package pers.solid.mishang.uc.util;

import com.google.common.collect.ImmutableList;
import net.minecraft.util.BlockRotation;
import net.minecraft.util.StringIdentifiable;
import net.minecraft.util.math.Direction;

import java.util.List;
import java.util.function.Predicate;

/**
 * @see Direction.Axis
 */
public enum FourHorizontalAxis implements StringIdentifiable, Predicate<EightHorizontalDirection> {
  X,
  NW_SE,
  Z,
  NE_SW;

  private final String name = name().toLowerCase();
  public static final List<FourHorizontalAxis> VALUES = ImmutableList.copyOf(values());

  @Override
  public String asString() {
    return name;
  }

  public static FourHorizontalAxis of(Direction.Axis axis) {
    switch (axis) {
      case X:
        return X;
      case Z:
        return Z;
      default:
        throw new IllegalArgumentException();
    }
  }

  /**
   * @see Direction.Axis#test
   */
  @Override
  public boolean test(EightHorizontalDirection eightHorizontalDirection) {
    return eightHorizontalDirection != null && eightHorizontalDirection.axis == this;
  }

  public FourHorizontalAxis rotate(BlockRotation rotation) {
    switch (rotation) {
      case CLOCKWISE_90:
        return VALUES.get((ordinal() + 2) & 3);
      case COUNTERCLOCKWISE_90:
        return VALUES.get((ordinal() - 2) & 3);
      case CLOCKWISE_180:
        return VALUES.get((ordinal() + 4) & 3);
      default:
        return this;
    }
  }

  public FourHorizontalAxis mirror() {
    switch (this) {
      case NW_SE:
        return NE_SW;
      case NE_SW:
        return NW_SE;
      default:
        return this;
    }
  }
}
