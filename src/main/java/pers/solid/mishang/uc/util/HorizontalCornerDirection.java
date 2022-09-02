package pers.solid.mishang.uc.util;

import net.minecraft.util.BlockMirror;
import net.minecraft.util.BlockRotation;
import net.minecraft.util.StringIdentifiable;
import net.minecraft.util.math.Direction;
import org.jetbrains.annotations.NotNull;

/**
 * 水平的角落的方向。Minecraft 原版的 {@link Direction} 包含 6 个正的方向，其中 4 个水平正方向之间会有 4 个角落的方向，也就是偏 45° 的方向。
 */
public enum HorizontalCornerDirection implements StringIdentifiable {
  /**
   * 西南
   */
  SOUTH_WEST(0, 45, "south_west", Direction.SOUTH, Direction.WEST),
  /**
   * 西北
   */
  NORTH_WEST(1, 135, "north_west", Direction.NORTH, Direction.WEST),
  /**
   * 东北
   */
  NORTH_EAST(2, 225, "north_east", Direction.NORTH, Direction.EAST),
  /**
   * 东南
   */
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

  /**
   * 根据两个相邻的水平方向返回一个水平的角落方向，如果这两个方向之间不能形成水平的角落方向将会抛出错误。
   *
   * @param dir1 第一个方向。
   * @param dir2 第二个方向。
   * @return 对应的水平角落方向。
   * @throws IllegalArgumentException 如果两个方向不相邻，或者有些不是水平的。
   */
  public static @NotNull HorizontalCornerDirection fromDirections(@NotNull Direction dir1, @NotNull Direction dir2) {
    for (HorizontalCornerDirection direction : HorizontalCornerDirection.values()) {
      if ((direction.dir1 == dir1 && direction.dir2 == dir2) || (direction.dir1 == dir2 && direction.dir2 == dir1)) {
        return direction;
      }
    }
    throw new IllegalArgumentException("There is no horizontal corner direction composed of " + dir1.asString() + " " + dir2.asString() + ".");
  }

  /**
   * 根据 id 返回一个方向对象。
   *
   * @param id 这个方向的内部 id。
   */
  public static @NotNull HorizontalCornerDirection fromId(int id) {
    id = id & 3;
    for (HorizontalCornerDirection direction : HorizontalCornerDirection.values()) {
      if (direction.id == id) {
        return direction;
      }
    }
    throw new IllegalStateException();
  }

  /**
   * 根据水平的旋转角度返回方向，会返回最近的水平角落方向。
   *
   * @param rotation 角度。
   * @return 与这个角度最近的水平角落方向。
   */
  public static HorizontalCornerDirection fromRotation(float rotation) {
    return fromId(Math.floorDiv((int) rotation, 90));
  }

  @Override
  public String asString() {
    return this.name;
  }

  /**
   * 获取该水平角落方向在指定坐标轴上的原版方向。
   *
   * @param axis 坐标轴。
   * @return 指定坐标轴上的方向。
   * @throws IllegalStateException 不存在此轴上的方向时。
   */
  public Direction getDirectionInAxis(Direction.Axis axis) {
    if (dir1.getAxis() == axis) {
      return dir1;
    }
    if (dir2.getAxis() == axis) {
      return dir2;
    }
    throw new IllegalStateException("Direction " + this.asString() + " has no direction in axis " + axis.asString() + "!");
  }

  public int asRotation() {
    return rotation;
  }

  /**
   * 该水平角落方向所在的两个原版方向是否拥有指定的方向。
   */
  public boolean hasDirection(Direction direction) {
    return direction == dir1 || direction == dir2;
  }

  public @NotNull HorizontalCornerDirection rotateYClockwise() {
    return fromDirections(dir1.rotateYClockwise(), dir2.rotateYClockwise());
  }

  public @NotNull HorizontalCornerDirection rotateYCounterclockwise() {
    return fromDirections(dir1.rotateYCounterclockwise(), dir2.rotateYCounterclockwise());
  }

  public @NotNull HorizontalCornerDirection mirror(BlockMirror mirror) {
    return fromDirections(mirror.apply(dir1), mirror.apply(dir2));
  }

  public @NotNull HorizontalCornerDirection mirror(Direction direction) {
    BlockMirror mirror = switch (direction.getAxis()) {
      case X -> BlockMirror.LEFT_RIGHT;
      case Z -> BlockMirror.FRONT_BACK;
      default -> BlockMirror.NONE;
    };
    return mirror(mirror);
  }

  public @NotNull HorizontalCornerDirection rotate(BlockRotation rotation) {
    return switch (rotation) {
      case NONE -> this;
      case CLOCKWISE_90 -> this.rotateYClockwise();
      case COUNTERCLOCKWISE_90 -> this.rotateYCounterclockwise();
      case CLOCKWISE_180 -> this.getOpposite();
    };
  }

  public @NotNull HorizontalCornerDirection getOpposite() {
    return fromDirections(dir1.getOpposite(), dir2.getOpposite());
  }
}
