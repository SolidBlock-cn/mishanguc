package pers.solid.mishang.uc;

import com.google.common.collect.BiMap;
import com.google.common.collect.EnumHashBiMap;
import com.google.common.collect.Streams;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.state.property.Property;
import net.minecraft.util.BlockRotation;
import net.minecraft.util.DyeColor;
import net.minecraft.util.Identifier;
import net.minecraft.util.Util;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.ApiStatus;
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
  private static final Logger LOGGER = LogManager.getLogger(MishangUtils.class);

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
    int j = (int) ((double) (color & 0xFF) * 0.4);
    int k = (int) ((double) (color >> 8 & 0xFF) * 0.4);
    int l = (int) ((double) (color >> 16 & 0xFF) * 0.4);
    if (color == 0) {
      return 0xfff0ebcc;
    }
    return (0) << 24 | (l & 0xFF) << 16 | (k & 0xFF) << 8 | (j & 0xFF);
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
            Arrays.stream(WallSignBlocks.class.getFields()),
            Arrays.stream(HandrailBlocks.class.getFields()))
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
   * @return 当前方块字段的流。使用反射。
   */
  @ApiStatus.AvailableSince("0.1.7")
  public static <T> Stream<Field> blockStream(Class<T> cls) {
    return Arrays.stream(cls.getFields())
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
   * 返回类中所有的 public static final 字段的值。例如：
   * <pre>{@code
   *  Collection<Block> hungSignBarBlocks =
   *    <Block>getInstances(HungSignBlocks.class).collect(ImmutableSet.toImmutableSet())
   * }</pre>
   *
   * @param cls 类。
   * @param <T> 字段类型。不在此类型的会被忽略，但是应当留意。
   * @return 类的所有 public static final 字段的值。
   */
  @ApiStatus.AvailableSince("0.1.7")
  public static <T> Stream<T> blockInstanceStream(Class<?> cls) {
    return blockStream(cls).map(field -> {
      try {
        //noinspection unchecked
        return (T) field.get(null);
      } catch (ClassCastException | IllegalAccessException e) {
        LOGGER.error("Error: ", e);
        return null;
      }
    }).filter(Objects::nonNull);
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
              case MIDDLE -> textContext.offsetY -= (stackedHeight - textContext.size / 2f) / 2f;
              case BOTTOM -> textContext.offsetY -= stackedHeight - textContext.size / 2f;
            }
          }
        }));
  }

  /**
   * 对一个坐标轴进行旋转。
   */
  public static Direction.Axis rotateAxis(BlockRotation rotation, Direction.Axis axis) {
    return switch (rotation) {
      case COUNTERCLOCKWISE_90, CLOCKWISE_90 -> switch (axis) {
        case X -> Direction.Axis.Z;
        case Z -> Direction.Axis.X;
        default -> axis;
      };
      default -> axis;
    };
  }

  /**
   * 给一个标识符加上后缀。例如：
   * <pre>
   *   identifierSuffix(minecraft:oak_slab, "_top") -> minecraft:oak_slab_top;
   * </pre>
   */
  @ApiStatus.AvailableSince("0.1.7")
  public static Identifier identifierSuffix(Identifier identifier, String suffix) {
    return new Identifier(identifier.getNamespace(), identifier.getPath() + suffix);
  }

  /**
   * 给一个标识符的路径部分加上前缀。例如：
   * <pre>
   *   identifierPrefix(minecraft:stone, "block/") -> minecraft:block/stone;
   *   identifierPrefix(mishanguc:white_light, "block/") -> mishanguc:block/white_light;
   * </pre>
   */
  @ApiStatus.AvailableSince("0.1.7")
  public static Identifier identifierPrefix(Identifier identifier, String prefix) {
    return new Identifier(identifier.getNamespace(), prefix + identifier.getPath());
  }

  public static <T extends Comparable<T>> BlockState with(BlockState state, Property<T> property, String name) {
    return property.parse(name).map((value) -> state.with(property, value)).orElse(state);
  }

}
