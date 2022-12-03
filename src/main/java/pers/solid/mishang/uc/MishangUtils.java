package pers.solid.mishang.uc;

import com.google.common.base.Suppliers;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Maps;
import com.google.common.collect.Streams;
import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.MappingResolver;
import net.minecraft.block.*;
import net.minecraft.item.Item;
import net.minecraft.state.property.Property;
import net.minecraft.text.LiteralText;
import net.minecraft.text.MutableText;
import net.minecraft.text.TextColor;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.BlockRotation;
import net.minecraft.util.DyeColor;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import org.jetbrains.annotations.*;
import pers.solid.mishang.uc.blocks.*;
import pers.solid.mishang.uc.item.MishangucItems;
import pers.solid.mishang.uc.mixin.DyeColorAccessorFor1_16;
import pers.solid.mishang.uc.text.PatternSpecialDrawable;
import pers.solid.mishang.uc.text.TextContext;
import pers.solid.mishang.uc.util.LineColor;
import pers.solid.mishang.uc.util.LineType;
import pers.solid.mishang.uc.util.TextBridge;
import pers.solid.mishang.uc.util.VerticalAlign;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.*;
import java.util.function.Supplier;
import java.util.stream.Stream;

/**
 * 本类存放一些实用方法。
 */
public class MishangUtils {
  private static final Supplier<ImmutableMap<Field, Block>> memoizedBlocks = Suppliers.memoize(MishangUtils::blocksInternal);
  private static final Supplier<ImmutableMap<Field, Item>> memoizedItems = Suppliers.memoize(MishangUtils::itemsInternal);

  private static final ImmutableSet<Block> WOODS = ImmutableSet.of(Blocks.OAK_WOOD, Blocks.SPRUCE_WOOD, Blocks.BIRCH_WOOD, Blocks.JUNGLE_WOOD, Blocks.ACACIA_WOOD, Blocks.DARK_OAK_WOOD, Blocks.CRIMSON_HYPHAE, Blocks.WARPED_HYPHAE);

  public static boolean isWood(Block block) {
    return WOODS.contains(block);
  }

  private static final ImmutableSet<Block> PLANKS = ImmutableSet.of(Blocks.OAK_PLANKS, Blocks.SPRUCE_PLANKS, Blocks.BIRCH_PLANKS, Blocks.JUNGLE_PLANKS, Blocks.ACACIA_PLANKS, Blocks.DARK_OAK_PLANKS, Blocks.WARPED_PLANKS, Blocks.CRIMSON_PLANKS);

  public static boolean isPlanks(Block block) {
    return PLANKS.contains(block) || block == ColoredBlocks.COLORED_PLANKS;
  }

  private static final ImmutableSet<Block> CONCRETES = ImmutableSet.of(Blocks.WHITE_CONCRETE, Blocks.ORANGE_CONCRETE, Blocks.MAGENTA_CONCRETE, Blocks.LIGHT_BLUE_CONCRETE, Blocks.YELLOW_CONCRETE, Blocks.LIME_CONCRETE, Blocks.PINK_CONCRETE, Blocks.GRAY_CONCRETE, Blocks.LIGHT_GRAY_CONCRETE, Blocks.CYAN_CONCRETE, Blocks.PURPLE_CONCRETE, Blocks.BLUE_CONCRETE, Blocks.BROWN_CONCRETE, Blocks.GREEN_CONCRETE, Blocks.RED_CONCRETE, Blocks.BLACK_CONCRETE);

  public static boolean isConcrete(Block block) {
    return CONCRETES.contains(block) || block == ColoredBlocks.COLORED_CONCRETE;
  }

  private static final ImmutableSet<Block> TERRACOTTAS = ImmutableSet.of(Blocks.WHITE_TERRACOTTA, Blocks.ORANGE_TERRACOTTA, Blocks.MAGENTA_TERRACOTTA, Blocks.LIGHT_BLUE_TERRACOTTA, Blocks.YELLOW_TERRACOTTA, Blocks.LIME_TERRACOTTA, Blocks.PINK_TERRACOTTA, Blocks.GRAY_TERRACOTTA, Blocks.LIGHT_GRAY_TERRACOTTA, Blocks.CYAN_TERRACOTTA, Blocks.PURPLE_TERRACOTTA, Blocks.BLUE_TERRACOTTA, Blocks.BROWN_TERRACOTTA, Blocks.GREEN_TERRACOTTA, Blocks.RED_TERRACOTTA, Blocks.BLACK_TERRACOTTA);

  public static boolean isTerracotta(Block block) {
    return TERRACOTTAS.contains(block) || block == ColoredBlocks.COLORED_TERRACOTTA;
  }

  private static final ImmutableSet<Block> WOOLS = ImmutableSet.of(Blocks.WHITE_WOOL, Blocks.ORANGE_WOOL, Blocks.MAGENTA_WOOL, Blocks.LIGHT_BLUE_WOOL, Blocks.YELLOW_WOOL, Blocks.LIME_WOOL, Blocks.PINK_WOOL, Blocks.GRAY_WOOL, Blocks.LIGHT_GRAY_WOOL, Blocks.CYAN_WOOL, Blocks.PURPLE_WOOL, Blocks.BLUE_WOOL, Blocks.BROWN_WOOL, Blocks.GREEN_WOOL, Blocks.RED_WOOL, Blocks.BLACK_WOOL);

  public static boolean isWool(Block block) {
    return WOOLS.contains(block) || block == ColoredBlocks.COLORED_WOOL;
  }

  private static final ImmutableSet<Block> STAINED_GLASSES = ImmutableSet.of(Blocks.WHITE_STAINED_GLASS, Blocks.ORANGE_STAINED_GLASS, Blocks.MAGENTA_STAINED_GLASS, Blocks.LIGHT_BLUE_STAINED_GLASS, Blocks.YELLOW_STAINED_GLASS, Blocks.LIME_STAINED_GLASS, Blocks.PINK_STAINED_GLASS, Blocks.GRAY_STAINED_GLASS, Blocks.LIGHT_GRAY_STAINED_GLASS, Blocks.CYAN_STAINED_GLASS, Blocks.PURPLE_STAINED_GLASS, Blocks.BLUE_STAINED_GLASS, Blocks.BROWN_STAINED_GLASS, Blocks.GREEN_STAINED_GLASS, Blocks.RED_STAINED_GLASS, Blocks.BLACK_STAINED_GLASS);

  public static boolean isStained_glass(Block block) {
    return STAINED_GLASSES.contains(block) || block == ColoredBlocks.COLORED_GLASS;
  }

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
      if (((DyeColorAccessorFor1_16) (Enum<DyeColor>) color).getSignColor() == signColor) {
        return color;
      }
    }
    return null;
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
        fieldStream(StandingSignBlocks.class),
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
      throw new AssertionError("The collection returned is empty, which is not expected. You may have to report to the author of Mishang Urban Construction mod.");
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
      throw new AssertionError("The collection returned is empty, which is not expected. You may have to report to the author of Mishang Urban Construction mod.");
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

  public static <T> Stream<@NotNull T> instanceStream(Class<?> containerClass, Class<T> castToClass) {
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
    directionToContexts.forEach((verticalAlign, list) -> {
      float stackedHeight = 0;
      for (TextContext textContext : list) {
        textContext.offsetY = (stackedHeight += textContext.getMarginTop() / 2f);
        stackedHeight += textContext.getHeight() / 2f;
        stackedHeight += textContext.getMarginTop() / 2f;
      }
      for (TextContext textContext : list) {
        switch (verticalAlign) {
          case MIDDLE:
            textContext.offsetY -= (stackedHeight - textContext.getHeight() / 2f) / 2f;
            break;
          case BOTTOM:
            textContext.offsetY -= stackedHeight - textContext.getHeight() / 2f;
            break;
        }
      }
    });
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

  public static <T extends Comparable<T>> BlockState with(BlockState state, Property<T> property, String name) {
    return property.parse(name).map((value) -> state.with(property, value)).orElse(state);
  }

  /**
   * 将一个告示牌中的 TextContext 中手动完成的箭头文字转化为 0.2.0 新加入的 PatternTextSpecial 格式。
   */
  @ApiStatus.AvailableSince("0.2.0")
  public static void replaceArrows(Collection<TextContext> textContexts) {
    for (TextContext textContext : textContexts) {
      if (textContext.text instanceof LiteralText) {
        LiteralText literalText = (LiteralText) textContext.text;
        final String rawString = literalText.getRawString();
        if (ARROW_TO_NAMES.containsKey(rawString)) {
          textContext.text = null;
          textContext.extra = PatternSpecialDrawable.fromName(textContext, ARROW_TO_NAMES.get(rawString));
          if ("←".equals(rawString) || "→".equals(rawString) || "↑".equals(rawString) || "↓".equals(rawString)) {
            textContext.size /= 1;
          } else {
            textContext.size /= 2;
          }
        }
      }
    }
  }

  @ApiStatus.AvailableSince("0.2.1")
  public static MutableText describeColor(int color) {
    return TextBridge.empty().append(TextBridge.literal("■").styled(style -> style.withColor(TextColor.fromRgb(color)))).append(String.format("#%06x", color));
  }

  @ApiStatus.AvailableSince("0.2.4")
  public static String composeStraightLineTexture(LineColor lineColor, LineType lineType) {
    if (lineType == LineType.NORMAL) {
      return lineColor.asString() + "_straight_line";
    } else {
      return lineColor.asString() + "_straight_" + lineType.asString() + "_line";
    }
  }

  public static String composeAngleLineTexture(LineColor lineColor, boolean bevel) {
    return lineColor.asString() + "_" + (bevel ? "bevel" : "right") + "_angle_line";
  }

  @SuppressWarnings({"unchecked", "rawtypes"})
  @ApiStatus.AvailableSince("0.2.4, 1.16.5")
  @Contract(pure = true)
  public static BlockState copyPropertiesFrom(final BlockState to, final BlockState from) {
    BlockState blockState = to;
    for (Property<?> property : from.getProperties()) {
      if (blockState.contains(property)) {
        blockState = blockState.with((Property) property, from.get(property));
      }
    }
    return blockState;
  }

  /**
   * 1.17 之前还没有 OperatorBlock 接口，故使用此类代替。
   */
  @ApiStatus.AvailableSince("1.0.0, 1.16.5")
  public static boolean isOperatorBlock(final Block block) {
    return (block instanceof CommandBlock || block instanceof StructureBlock || block instanceof JigsawBlock);
  }

  /**
   * 1.17 之前，{@link Block#getName()} 仅限客户端使用。
   */
  @ApiStatus.AvailableSince("1.0.0, 1.16.5")
  public static MutableText getBlockName(final Block block) {
    final MappingResolver resolver = FabricLoader.getInstance().getMappingResolver();
    try {
      final String methodName = resolver.mapMethodName(
          "intermediary",
          resolver.unmapClassName("intermediary", Block.class.getName()),
          "method_9518",
          "()Lnet/minecraft/class_5250;");
      MethodHandles.Lookup lookup = MethodHandles.publicLookup();
      MethodHandle namespaceGetter = lookup.findVirtual(
          block.getClass(),
          methodName,
          MethodType.methodType(MutableText.class)
      );
      return (MutableText) namespaceGetter.invoke(block);
    } catch (Throwable ignore) {
      return new TranslatableText(block.getTranslationKey());
    }
  }
}
