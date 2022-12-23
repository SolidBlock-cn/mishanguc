package pers.solid.mishang.uc.util;

import com.google.common.collect.ImmutableList;
import com.mojang.datafixers.util.Either;
import net.minecraft.util.BlockMirror;
import net.minecraft.util.BlockRotation;
import net.minecraft.util.StringIdentifiable;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.MathHelper;
import org.jetbrains.annotations.NotNull;

import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public enum EightHorizontalDirection implements StringIdentifiable {
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

  EightHorizontalDirection(@NotNull Direction vanillaDirection) {
    either = Either.left(vanillaDirection);
    this.axis = FourHorizontalAxis.of(vanillaDirection.getAxis());
  }

  EightHorizontalDirection(@NotNull HorizontalCornerDirection cornerDirection, FourHorizontalAxis axis) {
    either = Either.right(cornerDirection);
    this.axis = axis;
  }

  public static EightHorizontalDirection of(Direction direction) {
    return DIRECTIONS.get(direction);
  }

  public static EightHorizontalDirection of(HorizontalCornerDirection cornerDirection) {
    return CORNER_DIRECTIONS.get(cornerDirection);
  }

  public EightHorizontalDirection rotate(BlockRotation rotation) {
    return either.map(direction -> of(rotation.rotate(direction)), cornerDirection -> of(cornerDirection.rotate(rotation)));
  }

  public EightHorizontalDirection mirror(BlockMirror mirror) {
    return either.map(direction -> of(mirror.apply(direction)), cornerDirection -> of(cornerDirection.mirror(mirror)));
  }

  public float asRotation() {
    return either.map(Direction::asRotation, cornerDirection -> (float) cornerDirection.asRotation());
  }

  public static EightHorizontalDirection fromRotation(float rotation) {
    return VALUES.get(MathHelper.floor(rotation / 45 + 0.5) & 7);
  }

  public Optional<Direction> left() {
    return either.left();
  }

  public Optional<HorizontalCornerDirection> right() {
    return either.right();
  }

  @Override
  public String asString() {
    return either.map(Direction::asString, HorizontalCornerDirection::asString);
  }
}
