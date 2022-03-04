package pers.solid.mishang.uc;

import com.google.common.collect.BiMap;
import com.google.common.collect.EnumHashBiMap;
import com.google.common.collect.Streams;
import net.minecraft.block.Block;
import net.minecraft.client.texture.NativeImage;
import net.minecraft.util.BlockRotation;
import net.minecraft.util.DyeColor;
import net.minecraft.util.Util;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import pers.solid.mishang.uc.annotations.RegisterIdentifier;
import pers.solid.mishang.uc.blocks.*;
import pers.solid.mishang.uc.util.TextContext;
import pers.solid.mishang.uc.util.VerticalAlign;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.*;
import java.util.stream.Stream;

/**
 * 本类存放一些实用方法。
 */
public class MishangUtils {
  @SuppressWarnings("SuspiciousNameCombination")
  public static EnumMap<Direction, @NotNull VoxelShape> createDirectionToShape(
      double minX, double minY, double minZ, double maxX, double maxY, double maxZ) {
    final EnumMap<Direction, VoxelShape> map = new EnumMap<>(Direction.class);
    map.put(Direction.UP, Block.createCuboidShape(minX, minY, minZ, maxX, maxY, maxZ));
    map.put(
        Direction.DOWN,
        Block.createCuboidShape(16 - maxX, 16 - maxY, 16 - maxZ, 16 - minX, 16 - minY, 16 - minZ));
    map.put(Direction.EAST, Block.createCuboidShape(minY, minZ, minX, maxY, maxZ, maxX));
    map.put(
        Direction.WEST,
        Block.createCuboidShape(16 - maxY, 16 - maxZ, 16 - maxX, 16 - minY, 16 - minZ, 16 - minX));
    map.put(Direction.SOUTH, Block.createCuboidShape(minX, minZ, minY, maxX, maxZ, maxY));
    map.put(
        Direction.NORTH,
        Block.createCuboidShape(16 - maxX, 16 - maxZ, 16 - maxY, 16 - minX, 16 - minZ, 16 - minY));
    return map;
  }

  public static Map<Direction, @Nullable VoxelShape> createHorizontalDirectionToShape(
      double minX, double minY, double minZ, double maxX, double maxY, double maxZ) {
    final Map<Direction, VoxelShape> map = new EnumMap<>(Direction.class);
    map.put(Direction.SOUTH, Block.createCuboidShape(minX, minY, minZ, maxX, maxY, maxZ));
    map.put(Direction.WEST, Block.createCuboidShape(16 - maxZ, minY, minX, 16 - minZ, maxY, maxX));
    map.put(
        Direction.NORTH,
        Block.createCuboidShape(16 - maxX, minY, 16 - maxZ, 16 - minX, maxY, 16 - minZ));
    map.put(Direction.EAST, Block.createCuboidShape(minZ, minY, 16 - maxX, maxZ, maxY, 16 - minX));
    return map;
  }

  @SafeVarargs
  public static Map<Direction, VoxelShape> createDirectionToUnionShape(
      Map<Direction, VoxelShape> firstDirectionToShape,
      Map<Direction, VoxelShape>... directionToShapes) {
    final Map<Direction, VoxelShape> map = new EnumMap<>(Direction.class);
    for (Direction direction : Direction.values()) {
      final VoxelShape first = firstDirectionToShape.get(direction);
      if (first != null) {
        map.put(
            direction,
            VoxelShapes.union(
                first,
                Arrays.stream(directionToShapes)
                    .filter(Objects::nonNull)
                    .map(directionToShape -> directionToShape.get(direction))
                    .toArray(VoxelShape[]::new)));
      }
    }
    return map;
  }

  /**
   * 根据 signColor 返回颜色值。
   *
   * @param signColor 对应的告示牌颜色（整数）。
   * @return 颜色枚举对象，可能为 null。
   */
  public static @Nullable DyeColor colorBySignColor(int signColor) {
    for (DyeColor color : DyeColor.values()) {
      if (color.getSignColor() == signColor) {
        return color;
      }
    }
    return null;
  }

  /**
   * 将颜色转化为告示牌发光的颜色。
   *
   * @param color 颜色。
   * @return 发光后颜色的整数值。
   */
  private static int toSignOutlineColor(DyeColor color) {
    return toSignOutlineColor(color.getSignColor());
  }

  /**
   * 将颜色转化为告示牌发光的颜色。若为黑色，则返回白色。
   *
   * @param color 颜色的整数值。
   * @return 发光后颜色的整数值。
   */
  public static int toSignOutlineColor(int color) {
    int j = (int) ((double) NativeImage.getRed(color) * 0.4);
    int k = (int) ((double) NativeImage.getGreen(color) * 0.4);
    int l = (int) ((double) NativeImage.getBlue(color) * 0.4);
    if (color == 0) {
      return 0xfff0ebcc;
    }
    return NativeImage.packColor(0, l, k, j);
  }

  public static final BiMap<DyeColor, Integer> COLOR_TO_OUTLINE_COLOR = Util.make(EnumHashBiMap.create(DyeColor.class), map -> {
    for (DyeColor color : DyeColor.values()) {
      map.put(color, toSignOutlineColor(color));
    }
  });

  /**
   * @return 所有方块字段的流。使用反射。<br>
   * 该方法可以在 {@link MishangucBlocks} 被加载之前执行，并不会尝试访问字段内容。
   */
  public static Stream<Field> blockStream() {
    return Streams.concat(
            Arrays.stream(RoadBlocks.class.getFields()),
            Arrays.stream(RoadSlabBlocks.class.getFields()),
            Arrays.stream(LightBlocks.class.getFields()),
            Arrays.stream(HungSignBlocks.class.getFields()),
            Arrays.stream(WallSignBlocks.class.getFields()))
        .filter(
            field -> {
              int modifier = field.getModifiers();
              return Modifier.isPublic(modifier)
                  && Modifier.isStatic(modifier)
                  && Block.class.isAssignableFrom(field.getType())
                  && field.isAnnotationPresent(RegisterIdentifier.class);
            });
  }

  /**
   * 重新整理 textContexts。
   *
   * @param textContexts 由 textContexts 组成的集合。
   */
  public static void rearrange(Collection<TextContext> textContexts) {
    final EnumMap<VerticalAlign, List<TextContext>> directionToContexts = new EnumMap<>(VerticalAlign.class);
    for (TextContext textContext : textContexts) {
      if (!textContext.absolute) {
        directionToContexts.putIfAbsent(textContext.verticalAlign, new ArrayList<>());
        directionToContexts.get(textContext.verticalAlign).add(textContext);
      }
    }
    final float lineMargin = 1 / 8f;
    directionToContexts.forEach(
        ((verticalAlign, list) -> {
          float stackedHeight = 0;
          for (TextContext textContext : list) {
            stackedHeight += textContext.size * lineMargin / 2;
            textContext.offsetY = stackedHeight;
            stackedHeight += textContext.size * (1 + lineMargin) / 2;
          }
          for (TextContext textContext : list) {
            switch (verticalAlign) {
              case MIDDLE:
                textContext.offsetY -= (stackedHeight - textContext.size / 2f) / 2f;
                break;
              case BOTTOM:
                textContext.offsetY -= stackedHeight - textContext.size / 2f;
                break;
            }
          }
        }));
  }

  /**
   * 对一个坐标轴进行旋转。
   */
  public static Direction.Axis rotateAxis(BlockRotation rotation, Direction.Axis axis) {
    switch (rotation) {
      case COUNTERCLOCKWISE_90:
      case CLOCKWISE_90:
        switch (axis) {
          case X:
            return Direction.Axis.Z;
          case Z:
            return Direction.Axis.X;
          default:
            return axis;
        }
      default:
        return axis;
    }
  }
}
