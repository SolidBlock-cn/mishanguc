package pers.solid.mishang.uc.util;

import com.google.common.collect.ImmutableList;
import net.minecraft.core.Direction;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.level.block.Rotation;

import java.util.List;
import java.util.function.Predicate;

/**
 * @see Direction.Axis
 */
public enum FourHorizontalAxis implements StringRepresentable, Predicate<EightHorizontalDirection> {
  X,
  NW_SE,
  Z,
  NE_SW;

  private final String name = name().toLowerCase();
  public static final List<FourHorizontalAxis> VALUES = ImmutableList.copyOf(values());

  @Override
  public String getSerializedName() {
    return name;
  }

  public static FourHorizontalAxis of(Direction.Axis axis) {
    return switch (axis) {
      case X -> X;
      case Z -> Z;
      default -> throw new IllegalArgumentException();
    };
  }

  /**
   * @see Direction.Axis#test
   */
  @Override
  public boolean test(EightHorizontalDirection eightHorizontalDirection) {
    return eightHorizontalDirection != null && eightHorizontalDirection.axis == this;
  }

  public FourHorizontalAxis rotate(Rotation rotation) {
    return switch (rotation) {
      case CLOCKWISE_90 -> VALUES.get((ordinal() + 2) & 3);
      case COUNTERCLOCKWISE_90 -> VALUES.get((ordinal() - 2) & 3);
      case CLOCKWISE_180 -> VALUES.get((ordinal() + 4) & 3);
      default -> this;
    };
  }

  public FourHorizontalAxis mirror() {
    return switch (this) {
      case NW_SE -> NE_SW;
      case NE_SW -> NW_SE;
      default -> this;
    };
  }
}
