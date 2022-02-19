package pers.solid.mishang.uc.util;

import com.google.common.collect.ImmutableSet;
import net.minecraft.util.BlockMirror;
import net.minecraft.util.BlockRotation;
import net.minecraft.util.StringIdentifiable;
import net.minecraft.util.math.Direction;
import org.jetbrains.annotations.Nullable;

public enum HorizontalCornerDirection implements StringIdentifiable {
  /** 西南 */
  SOUTH_WEST(0, 45, "south_west", Direction.SOUTH, Direction.WEST),
  NORTH_WEST(1, 135, "north_west", Direction.NORTH, Direction.WEST),
  NORTH_EAST(2, 225, "north_east", Direction.NORTH, Direction.EAST),
  SOUTH_EAST(3, -45, "south_east", Direction.SOUTH, Direction.EAST);

  private final String name;
  private final Direction dir1, dir2;
  private final int id;
  private final int rotation;

  HorizontalCornerDirection(int id, int rotation, String name, Direction dir1, Direction dir2) {
    this.rotation = rotation;
    if (dir1 == dir2 || dir1.getOpposite() == dir2) {
      throw new IllegalArgumentException(
          "The two directions of a " + "corner direction " + "cannot be " + "same or opposite!");
    }
    this.id = id;
    this.name = name;
    this.dir1 = dir1;
    this.dir2 = dir2;
  }

  public static @Nullable HorizontalCornerDirection fromDirections(Direction dir1, Direction dir2) {
    ImmutableSet<Direction> directions = ImmutableSet.of(dir1, dir2);
    for (HorizontalCornerDirection direction : HorizontalCornerDirection.values()) {
      if (directions.equals(ImmutableSet.of(direction.dir1, direction.dir2))) {
        return direction;
      }
    }
    return null;
  }

  public static HorizontalCornerDirection fromId(int id) {
    id = id & 3;
    for (HorizontalCornerDirection direction : HorizontalCornerDirection.values()) {
      if (direction.id == id) {
        return direction;
      }
    }
    throw new IllegalStateException();
  }

  public static HorizontalCornerDirection fromRotation(float rotation) {
    return fromId(Math.floorDiv((int) rotation, 90));
  }

  @Override
  public String asString() {
    return this.name;
  }

  /**
   * @param axis 坐标轴。
   * @return 指定坐标轴上的方向。
   */
  public Direction getDirectionInAxis(Direction.Axis axis) {
    if (dir1.getAxis() == axis) {
      return dir1;
    }
    if (dir2.getAxis() == axis) {
      return dir2;
    }
    throw new IllegalStateException(
        String.format(
            "Direction %s has no direction in axis %s!", this.asString(), axis.asString()));
  }

  public int asRotation() {
    return rotation;
  }

  public boolean hasDirection(Direction direction) {
    return direction == dir1 || direction == dir2;
  }

  public HorizontalCornerDirection rotateYClockwise() {
    return fromDirections(dir1.rotateYClockwise(), dir2.rotateYClockwise());
  }

  public HorizontalCornerDirection rotateYCounterclockwise() {
    return fromDirections(dir1.rotateYCounterclockwise(), dir2.rotateYCounterclockwise());
  }

  public HorizontalCornerDirection mirror(BlockMirror mirror) {
    return fromDirections(mirror.apply(dir1), mirror.apply(dir2));
  }

  public HorizontalCornerDirection mirror(Direction direction) {
    BlockMirror mirror = switch (direction.getAxis()) {
      case X -> BlockMirror.LEFT_RIGHT;
      case Z -> BlockMirror.FRONT_BACK;
      default -> BlockMirror.NONE;
    };
    return mirror(mirror);
  }

  public HorizontalCornerDirection rotate(BlockRotation rotation) {
    return switch (rotation) {
      case NONE -> this;
      case CLOCKWISE_90 -> this.rotateYClockwise();
      case COUNTERCLOCKWISE_90 -> this.rotateYCounterclockwise();
      case CLOCKWISE_180 -> this.getOpposite();
    };
  }

  public HorizontalCornerDirection getOpposite() {
    return fromDirections(dir1.getOpposite(), dir2.getOpposite());
  }
}
