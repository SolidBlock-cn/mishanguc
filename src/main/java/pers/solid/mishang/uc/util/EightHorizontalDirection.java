package pers.solid.mishang.uc.util;

import com.google.common.collect.ImmutableList;
import com.mojang.datafixers.util.Either;
import net.minecraft.core.Direction;
import net.minecraft.util.Mth;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.Rotation;

import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public enum EightHorizontalDirection implements StringRepresentable {
  SOUTH(Direction.SOUTH),
  SOUTH_WEST(HorizontalCornerDirection.SOUTH_WEST, FourHorizontalAxis.NE_SW),
  WEST(Direction.WEST),
  NORTH_WEST(HorizontalCornerDirection.NORTH_WEST, FourHorizontalAxis.NW_SE),
  NORTH(Direction.NORTH),
  NORTH_EAST(HorizontalCornerDirection.NORTH_EAST, FourHorizontalAxis.NE_SW),
  EAST(Direction.EAST),
  SOUTH_EAST(HorizontalCornerDirection.SOUTH_EAST, FourHorizontalAxis.NW_SE);

  public final Either<Direction, HorizontalCornerDirection> either;
  private static final Map<Direction, EightHorizontalDirection> DIRECTIONS = new EnumMap<>(Direction.class);
  private static final Map<HorizontalCornerDirection, EightHorizontalDirection> CORNER_DIRECTIONS = new EnumMap<>(HorizontalCornerDirection.class);
  public static final List<EightHorizontalDirection> VALUES = ImmutableList.copyOf(values());

  static {
    for (EightHorizontalDirection value : VALUES) {
      value.either.map(left -> DIRECTIONS.put(left, value), right -> CORNER_DIRECTIONS.put(right, value));
    }
  }

  public final FourHorizontalAxis axis;

  EightHorizontalDirection(Direction vanillaDirection) {
    either = Either.left(vanillaDirection);
    this.axis = FourHorizontalAxis.of(vanillaDirection.getAxis());
  }

  EightHorizontalDirection(HorizontalCornerDirection cornerDirection, FourHorizontalAxis axis) {
    either = Either.right(cornerDirection);
    this.axis = axis;
  }

  public static EightHorizontalDirection of(Direction direction) {
    return DIRECTIONS.get(direction);
  }

  public static EightHorizontalDirection of(HorizontalCornerDirection cornerDirection) {
    return CORNER_DIRECTIONS.get(cornerDirection);
  }

  public EightHorizontalDirection rotate(Rotation rotation) {
    return either.map(direction -> of(rotation.rotate(direction)), cornerDirection -> of(cornerDirection.rotate(rotation)));
  }

  public EightHorizontalDirection mirror(Mirror mirror) {
    return either.map(direction -> of(mirror.mirror(direction)), cornerDirection -> of(cornerDirection.mirror(mirror)));
  }

  public float asRotation() {
    return either.map(Direction::toYRot, cornerDirection -> (float) cornerDirection.asRotation());
  }

  public static EightHorizontalDirection fromRotation(float rotation) {
    return VALUES.get(Mth.floor(rotation / 45 + 0.5) & 7);
  }

  public Optional<Direction> left() {
    return either.left();
  }

  public Optional<HorizontalCornerDirection> right() {
    return either.right();
  }

  @Override
  public String getSerializedName() {
    return either.map(Direction::getSerializedName, HorizontalCornerDirection::getSerializedName);
  }
}
