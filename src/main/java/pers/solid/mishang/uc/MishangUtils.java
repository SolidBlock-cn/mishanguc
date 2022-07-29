package pers.solid.mishang.uc;

import com.google.common.base.Functions;
import com.google.common.base.Suppliers;
import com.google.common.collect.*;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.item.Item;
import net.minecraft.state.property.Property;
import net.minecraft.text.LiteralTextContent;
import net.minecraft.util.BlockRotation;
import net.minecraft.util.DyeColor;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.Unmodifiable;
import pers.solid.mishang.uc.blocks.*;
import pers.solid.mishang.uc.item.MishangucItems;
import pers.solid.mishang.uc.text.PatternTextSpecial;
import pers.solid.mishang.uc.text.TextContext;
import pers.solid.mishang.uc.util.VerticalAlign;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.*;
import java.util.function.Supplier;
import java.util.stream.Stream;

/**
 * 本类存放一些实用方法。
 */
public class MishangUtils {
  /**
   * @since 0.2.0 该字段为一个不可变的映射。
   */
  public static final @Unmodifiable BiMap<DyeColor, Integer> COLOR_TO_OUTLINE_COLOR = Arrays.stream(DyeColor.values()).collect(ImmutableBiMap.toImmutableBiMap(Functions.identity(), MishangUtils::toSignOutlineColor));
  private static final Logger LOGGER = LogManager.getLogger(MishangUtils.class);
  private static final Supplier<ImmutableMap<Field, Block>> memoizedBlocks = Suppliers.memoize(MishangUtils::blocksInternal);
  private static final Supplier<ImmutableMap<Field, Item>> memoizedItems = Suppliers.memoize(MishangUtils::itemsInternal);
  private static final @Unmodifiable Map<String, String> ARROW_TO_NAMES = new ImmutableMap.Builder<String, String>()
      .put("←", "al")
      .put("→", "ar")
      .put("↑", "at")
      .put("↓", "ab")
      .put("↖", "alt")
      .put("↗", "art")
      .put("↙", "alb")
      .put("↘", "arb")
      .build();

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
        map.put(direction, VoxelShapes.union(
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

  /**
   * @return 所有方块字段的流。使用反射。<br>
   * 该方法可以在 {@link MishangucBlocks} 被加载之前执行，并不会尝试访问字段内容。
   * @since 0.2.0 该方法由 blockStream 更名为 blockFieldStream。
   */
  public static Stream<Field> blockFieldStream() {
    return Streams.concat(
        fieldStream(RoadBlocks.class),
        fieldStream(RoadSlabBlocks.class),
        fieldStream(LightBlocks.class),
        fieldStream(HungSignBlocks.class),
        fieldStream(WallSignBlocks.class),
        fieldStream(HandrailBlocks.class),
        fieldStream(ColoredBlocks.class)
    );
  }

  public static Stream<Field> itemFieldStream() {
    return fieldStream(MishangucItems.class);
  }

  @ApiStatus.AvailableSince("0.2.0")
  @ApiStatus.Internal
  private static @Unmodifiable ImmutableMap<Field, Block> blocksInternal() {
    ImmutableMap.Builder<Field, Block> builder = new ImmutableMap.Builder<>();
    instanceEntryStream(blockFieldStream(), Block.class).forEach(builder::put);
    final ImmutableMap<Field, Block> build = builder.build();
    if (build.isEmpty()) {
      throw new AssertionError("The collection returned is empty, which is not expected.. You may have to report to the author of Mishang Urban Construction mod.");
    }
    return build;
  }

  @ApiStatus.AvailableSince("0.2.0")
  @ApiStatus.Internal
  private static @Unmodifiable ImmutableMap<Field, Item> itemsInternal() {
    ImmutableMap.Builder<Field, Item> builder = new ImmutableMap.Builder<>();
    instanceEntryStream(itemFieldStream(), Item.class).forEach(builder::put);
    final ImmutableMap<Field, Item> build = builder.build();
    if (build.isEmpty()) {
      throw new AssertionError("The collection returned is empty, which is not expected.. You may have to report to the author of Mishang Urban Construction mod.");
    }
    return build;
  }

  /**
   * 该模组的所有方块字段及其值的集合。会通过反射来访问字段，并记住这个值，下次直接返回该值。
   *
   * @return 由方块字段和值组成的不可变映射。第一次调用时会生成，此后的所有调用都会直接使用这个值。
   */
  @ApiStatus.AvailableSince("0.2.0")
  public static @Unmodifiable ImmutableMap<Field, Block> blocks() {
    return memoizedBlocks.get();
  }

  /**
   * 该模组的所有物品字段及其值的集合。会通过反射来访问字段，并记住这个值，下次直接返回该值。
   *
   * @return 由物品字段和值组成的不可变映射。第一次调用时会生成，此后的所有调用都会直接使用这个值。
   */
  @ApiStatus.AvailableSince("0.2.0")
  public static @Unmodifiable ImmutableMap<Field, Item> items() {
    return memoizedItems.get();
  }

  /**
   * 迭代某个类中的所有字段，并返回由 public static final 字段组成的流。
   *
   * @param containerClass 包含该字段的类，该字段不一定要属于该类。
   * @return 由类中的 public static final 字段组成的流。
   */
  public static Stream<Field> fieldStream(Class<?> containerClass) {
    return Arrays.stream(containerClass.getFields())
        .filter(
            field -> {
              int modifier = field.getModifiers();
              return Modifier.isPublic(modifier)
                  && Modifier.isStatic(modifier)
                  && Modifier.isFinal(modifier);
            });
  }

  public static <T> Stream<Map.Entry<Field, T>> instanceEntryStream(Stream<Field> fieldStream, Class<T> castToClass) {
    return fieldStream.map(field -> {
      final Object o;
      try {
        o = field.get(null);
      } catch (IllegalAccessException e) {
        throw new InternalError("Cannot access value of the field in Mishang Urban Construction mod.", e);
      }
      if (castToClass.isInstance(o)) {
        return Maps.immutableEntry(field, castToClass.cast(o));
      } else {
        return null;
      }
    }).filter(Objects::nonNull);
  }

  public static <T> Stream<Map.Entry<Field, T>> instanceEntryStream(Class<?> containerClass, Class<T> castToClass) {
    return instanceEntryStream(fieldStream(containerClass), castToClass);
  }

  public static <T> Stream<T> instanceStream(Class<?> containerClass, Class<T> castToClass) {
    return instanceEntryStream(containerClass, castToClass).map(Map.Entry::getValue);
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

  public static <T extends Comparable<T>> BlockState with(BlockState state, Property<T> property, String name) {
    return property.parse(name).map((value) -> state.with(property, value)).orElse(state);
  }

  /**
   * 将一个告示牌中的 TextContext 中手动完成的箭头文字转化为 0.2.0 新加入的 PatternTextSpecial 格式。
   */
  @ApiStatus.AvailableSince("0.2.0")
  public static void replaceArrows(Collection<TextContext> textContexts) {
    for (TextContext textContext : textContexts) {
      if (textContext.text.getContent() instanceof final LiteralTextContent literalTextContent) {
        final String rawString = literalTextContent.string();
        if (ARROW_TO_NAMES.containsKey(rawString)) {
          textContext.text = null;
          textContext.extra = PatternTextSpecial.fromName(textContext, ARROW_TO_NAMES.get(rawString));
          if ("←".equals(rawString) || "→".equals(rawString) || "↑".equals(rawString) || "↓".equals(rawString)) {
            textContext.size /= 1;
          } else {
            textContext.size /= 2;
          }
        }
      }
    }
  }
}
