package pers.solid.mishang.uc;

import com.google.common.base.Functions;
import com.google.common.base.Suppliers;
import com.google.common.collect.*;
import com.google.gson.JsonPrimitive;
import net.fabricmc.fabric.api.tag.convention.v1.ConventionalItemTags;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.data.client.VariantSetting;
import net.minecraft.item.Item;
import net.minecraft.nbt.*;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.state.property.Property;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.text.TextColor;
import net.minecraft.util.BlockRotation;
import net.minecraft.util.DyeColor;
import net.minecraft.util.Formatting;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.Unmodifiable;
import pers.solid.mishang.uc.blocks.*;
import pers.solid.mishang.uc.item.MishangucItems;
import pers.solid.mishang.uc.text.TextContext;
import pers.solid.mishang.uc.util.LineColor;
import pers.solid.mishang.uc.util.LineType;
import pers.solid.mishang.uc.util.TextBridge;
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
  public static final VariantSetting<Integer> INT_X_VARIANT = new VariantSetting<>("x", JsonPrimitive::new);
  public static final VariantSetting<Integer> INT_Y_VARIANT = new VariantSetting<>("y", JsonPrimitive::new);
  public static final VariantSetting<Direction> DIRECTION_Y_VARIANT = new VariantSetting<>("y", direction -> new JsonPrimitive((int) direction.asRotation()));
  private static final Supplier<ImmutableList<Block>> memoizedBlocks = Suppliers.memoize(MishangUtils::blocksInternal);
  private static final Supplier<ImmutableList<Item>> memoizedItems = Suppliers.memoize(MishangUtils::itemsInternal);
  private static final ImmutableSet<Block> WOODS = ImmutableSet.of(Blocks.OAK_WOOD, Blocks.SPRUCE_WOOD, Blocks.BIRCH_WOOD, Blocks.JUNGLE_WOOD, Blocks.ACACIA_WOOD, Blocks.DARK_OAK_WOOD, Blocks.MANGROVE_WOOD, Blocks.CRIMSON_HYPHAE, Blocks.WARPED_HYPHAE, Blocks.CHERRY_WOOD);
  private static final ImmutableSet<Block> STRIPPED_WOODS = ImmutableSet.of(Blocks.STRIPPED_OAK_WOOD, Blocks.STRIPPED_SPRUCE_WOOD, Blocks.STRIPPED_BIRCH_WOOD, Blocks.STRIPPED_JUNGLE_WOOD, Blocks.STRIPPED_ACACIA_WOOD, Blocks.STRIPPED_DARK_OAK_WOOD, Blocks.STRIPPED_MANGROVE_WOOD, Blocks.STRIPPED_CRIMSON_HYPHAE, Blocks.STRIPPED_WARPED_HYPHAE, Blocks.STRIPPED_CHERRY_WOOD);
  private static final ImmutableSet<Block> PLANKS = ImmutableSet.of(Blocks.OAK_PLANKS, Blocks.SPRUCE_PLANKS, Blocks.BIRCH_PLANKS, Blocks.JUNGLE_PLANKS, Blocks.ACACIA_PLANKS, Blocks.DARK_OAK_PLANKS, Blocks.MANGROVE_PLANKS, Blocks.WARPED_PLANKS, Blocks.CRIMSON_PLANKS, Blocks.CHERRY_PLANKS, Blocks.BAMBOO_PLANKS);
  private static final ImmutableSet<Block> CONCRETES = ImmutableSet.of(Blocks.WHITE_CONCRETE, Blocks.ORANGE_CONCRETE, Blocks.MAGENTA_CONCRETE, Blocks.LIGHT_BLUE_CONCRETE, Blocks.YELLOW_CONCRETE, Blocks.LIME_CONCRETE, Blocks.PINK_CONCRETE, Blocks.GRAY_CONCRETE, Blocks.LIGHT_GRAY_CONCRETE, Blocks.CYAN_CONCRETE, Blocks.PURPLE_CONCRETE, Blocks.BLUE_CONCRETE, Blocks.BROWN_CONCRETE, Blocks.GREEN_CONCRETE, Blocks.RED_CONCRETE, Blocks.BLACK_CONCRETE);
  private static final ImmutableSet<Block> TERRACOTTAS = ImmutableSet.of(Blocks.WHITE_TERRACOTTA, Blocks.ORANGE_TERRACOTTA, Blocks.MAGENTA_TERRACOTTA, Blocks.LIGHT_BLUE_TERRACOTTA, Blocks.YELLOW_TERRACOTTA, Blocks.LIME_TERRACOTTA, Blocks.PINK_TERRACOTTA, Blocks.GRAY_TERRACOTTA, Blocks.LIGHT_GRAY_TERRACOTTA, Blocks.CYAN_TERRACOTTA, Blocks.PURPLE_TERRACOTTA, Blocks.BLUE_TERRACOTTA, Blocks.BROWN_TERRACOTTA, Blocks.GREEN_TERRACOTTA, Blocks.RED_TERRACOTTA, Blocks.BLACK_TERRACOTTA);
  private static final ImmutableSet<Block> WOOLS = ImmutableSet.of(Blocks.WHITE_WOOL, Blocks.ORANGE_WOOL, Blocks.MAGENTA_WOOL, Blocks.LIGHT_BLUE_WOOL, Blocks.YELLOW_WOOL, Blocks.LIME_WOOL, Blocks.PINK_WOOL, Blocks.GRAY_WOOL, Blocks.LIGHT_GRAY_WOOL, Blocks.CYAN_WOOL, Blocks.PURPLE_WOOL, Blocks.BLUE_WOOL, Blocks.BROWN_WOOL, Blocks.GREEN_WOOL, Blocks.RED_WOOL, Blocks.BLACK_WOOL);
  private static final ImmutableSet<Block> STAINED_GLASSES = ImmutableSet.of(Blocks.WHITE_STAINED_GLASS, Blocks.ORANGE_STAINED_GLASS, Blocks.MAGENTA_STAINED_GLASS, Blocks.LIGHT_BLUE_STAINED_GLASS, Blocks.YELLOW_STAINED_GLASS, Blocks.LIME_STAINED_GLASS, Blocks.PINK_STAINED_GLASS, Blocks.GRAY_STAINED_GLASS, Blocks.LIGHT_GRAY_STAINED_GLASS, Blocks.CYAN_STAINED_GLASS, Blocks.PURPLE_STAINED_GLASS, Blocks.BLUE_STAINED_GLASS, Blocks.BROWN_STAINED_GLASS, Blocks.GREEN_STAINED_GLASS, Blocks.RED_STAINED_GLASS, Blocks.BLACK_STAINED_GLASS);

  public static boolean isWooden(Block block) {
    return isWood(block) || isStrippedWood(block) || isPlanks(block);
  }

  public static boolean isWood(Block block) {
    return WOODS.contains(block);
  }

  public static boolean isStrippedWood(Block block) {
    return STRIPPED_WOODS.contains(block);
  }

  public static boolean isPlanks(Block block) {
    return PLANKS.contains(block) || block == ColoredBlocks.COLORED_PLANKS;
  }

  public static boolean isConcrete(Block block) {
    return CONCRETES.contains(block) || block == ColoredBlocks.COLORED_CONCRETE;
  }

  public static boolean isTerracotta(Block block) {
    return TERRACOTTAS.contains(block) || block == ColoredBlocks.COLORED_TERRACOTTA;
  }

  public static boolean isWool(Block block) {
    return WOOLS.contains(block) || block == ColoredBlocks.COLORED_WOOL;
  }

  public static boolean isStained_glass(Block block) {
    return STAINED_GLASSES.contains(block) || block == ColoredBlocks.COLORED_GLASS;
  }

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
      return 0xf0ebcc;
    }
    return (0) << 24 | (l & 0xFF) << 16 | (k & 0xFF) << 8 | (j & 0xFF);
  }

  @ApiStatus.AvailableSince("0.2.0")
  @ApiStatus.Internal
  private static @Unmodifiable ImmutableList<@NotNull Block> blocksInternal() {
    final ImmutableList<@NotNull Block> build = Streams.concat(
        instanceStream(RoadBlocks.class, Block.class),
        RoadSlabBlocks.SLABS.stream(),
        instanceStream(RoadMarkBlocks.class, Block.class),
        instanceStream(LightBlocks.class, Block.class),
        instanceStream(HungSignBlocks.class, Block.class),
        instanceStream(WallSignBlocks.class, Block.class),
        instanceStream(StandingSignBlocks.class, Block.class),
        instanceStream(HandrailBlocks.class, Block.class),
        instanceStream(ColoredBlocks.class, Block.class)
    ).collect(ImmutableList.toImmutableList());
    if (build.isEmpty()) {
      throw new AssertionError("The collection returned is empty, which is not expected. You may have to report to the author of Mishang Urban Construction mod.");
    }
    return build;
  }

  @ApiStatus.AvailableSince("0.2.0")
  @ApiStatus.Internal
  private static @Unmodifiable ImmutableList<@NotNull Item> itemsInternal() {
    final ImmutableList<@NotNull Item> build = instanceStream(MishangucItems.class, Item.class).collect(ImmutableList.toImmutableList());
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
  public static @Unmodifiable ImmutableList<Block> blocks() {
    return memoizedBlocks.get();
  }

  /**
   * 该模组的所有物品字段及其值的集合。会通过反射来访问字段，并记住这个值，下次直接返回该值。
   *
   * @return 由物品字段和值组成的不可变映射。第一次调用时会生成，此后的所有调用都会直接使用这个值。
   */
  @ApiStatus.AvailableSince("0.2.0")
  public static @Unmodifiable ImmutableList<Item> items() {
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
        field.setAccessible(true);
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
          case MIDDLE -> textContext.offsetY -= (stackedHeight - textContext.getHeight() / 2f) / 2f;
          case BOTTOM -> textContext.offsetY -= stackedHeight - textContext.getHeight() / 2f;
        }
      }
    });
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

  @ApiStatus.AvailableSince("0.2.1")
  public static MutableText describeColor(int color) {
    return describeColor(color, TextBridge.literal(formatColorHex(color)));
  }

  public static MutableText describeColor(int color, Text text) {
    return TextBridge.empty().append(TextBridge.literal("■").styled(style -> style.withColor(color))).append(text);
  }

  /**
   * 接收一个整数形式的颜色，考虑到 Minecraft 可能存在带有 alpha 通道的颜色，因此当检测到有 alpha 通道时，格式化为 #aarrggbb 的格式，否则格式化为 #rrggbb 的格式。
   */
  public static String formatColorHex(int color) {
    return (color & 0xff000000) != 0 ? String.format("#%08x", color) : String.format("#%06x", color);
  }

  public static MutableText describeShortcut(Text shortcut) {
    return TextBridge.translatable("message.mishanguc.keyboard_shortcut.composed", shortcut).formatted(Formatting.GRAY);
  }

  @ApiStatus.AvailableSince("0.2.4")
  public static String composeStraightLineTexture(LineColor lineColor, LineType lineType) {
    if (lineType == LineType.NORMAL) {
      return lineColor.asString() + "_straight_line";
    } else {
      return lineColor.asString() + "_straight_" + lineType.asString() + "_line";
    }
  }

  public static String composeAngleLineTexture(LineColor lineColor, LineType lineType, boolean bevel) {
    return lineColor.asString() + "_" + (lineType == LineType.NORMAL ? "" : lineColor.asString() + "_") + (bevel ? "bevel" : "right") + "_angle_line";
  }

  public static int readColorFromNbtElement(NbtElement nbtColor) {
    if (nbtColor instanceof final AbstractNbtNumber abstractNbtNumber) {
      return abstractNbtNumber.intValue();
    } else if (nbtColor instanceof NbtString) {
      final TextColor parse = TextColor.parse(nbtColor.asString());
      return parse == null ? 0 : parse.getRgb();
    } else if (nbtColor instanceof AbstractNbtList<?> list) {
      final int size = list.size();
      NbtElement _red = size > 0 ? list.get(0) : null;
      NbtElement _green = size > 1 ? list.get(1) : null;
      NbtElement _blue = size > 2 ? list.get(2) : null;
      NbtElement _alpha = size > 3 ? list.get(3) : null;
      int red = _red instanceof AbstractNbtNumber ? ((AbstractNbtNumber) _red).intValue() & 0xff : 0;
      int green = _green instanceof AbstractNbtNumber ? ((AbstractNbtNumber) _green).intValue() & 0xff : 0;
      int blue = _blue instanceof AbstractNbtNumber ? ((AbstractNbtNumber) _blue).intValue() & 0xff : 0;
      int alpha = _alpha instanceof AbstractNbtNumber ? ((AbstractNbtNumber) _alpha).intValue() & 0xff : 0;
      return (red << 16) | (green << 8) | blue | (alpha << 24);
    } else if (nbtColor instanceof final NbtCompound nbtCompound) {
      if (nbtCompound.contains("signColor", NbtElement.STRING_TYPE)) {
        return DyeColor.byName(nbtCompound.getString("signColor"), DyeColor.BLACK).getSignColor();
      } else if (nbtCompound.contains("fireworkColor", NbtElement.STRING_TYPE)) {
        return DyeColor.byName(nbtCompound.getString("fireworkColor"), DyeColor.BLACK).getFireworkColor();
      } else if (nbtCompound.contains("mapColor", NbtElement.STRING_TYPE)) {
        return DyeColor.byName(nbtCompound.getString("mapColor"), DyeColor.BLACK).getMapColor().color;
      } else {
        return 0;
      }
    } else {
      return 0;
    }
  }

  /**
   * 将数字转换为字符串，如果这个符点数的值正好等于整数，那么转换为字符串时不显示小数部分。
   */
  public static String numberToString(float value) {
    final int intValue = (int) value;
    return value == intValue ? Integer.toString(intValue) : Float.toString(value);
  }

  public static final Supplier<ImmutableMap<DyeColor, TagKey<Item>>> DYE_ITEM_TAGS = Suppliers.memoize(() -> ImmutableMap.<DyeColor, TagKey<Item>>builder()
      .put(DyeColor.BLACK, ConventionalItemTags.BLACK_DYES)
      .put(DyeColor.BLUE, ConventionalItemTags.BLUE_DYES)
      .put(DyeColor.BROWN, ConventionalItemTags.BROWN_DYES)
      .put(DyeColor.CYAN, ConventionalItemTags.CYAN_DYES)
      .put(DyeColor.GRAY, ConventionalItemTags.GRAY_DYES)
      .put(DyeColor.GREEN, ConventionalItemTags.GREEN_DYES)
      .put(DyeColor.LIGHT_BLUE, ConventionalItemTags.LIGHT_BLUE_DYES)
      .put(DyeColor.LIGHT_GRAY, ConventionalItemTags.LIGHT_GRAY_DYES)
      .put(DyeColor.LIME, ConventionalItemTags.LIME_DYES)
      .put(DyeColor.MAGENTA, ConventionalItemTags.MAGENTA_DYES)
      .put(DyeColor.ORANGE, ConventionalItemTags.ORANGE_DYES)
      .put(DyeColor.PINK, ConventionalItemTags.PINK_DYES)
      .put(DyeColor.PURPLE, ConventionalItemTags.PURPLE_DYES)
      .put(DyeColor.RED, ConventionalItemTags.RED_DYES)
      .put(DyeColor.WHITE, ConventionalItemTags.WHITE_DYES)
      .put(DyeColor.YELLOW, ConventionalItemTags.YELLOW_DYES)
      .build());
}
