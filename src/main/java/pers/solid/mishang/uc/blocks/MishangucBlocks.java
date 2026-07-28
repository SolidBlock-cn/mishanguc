package pers.solid.mishang.uc.blocks;

import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.block.*;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.util.DyeColor;
import org.jetbrains.annotations.ApiStatus;
import pers.solid.mishang.uc.Mishanguc;
import pers.solid.mishang.uc.annotations.CustomId;
import pers.solid.mishang.uc.annotations.Cutout;
import pers.solid.mishang.uc.annotations.Translucent;
import pers.solid.mishang.uc.block.HandrailBlock;
import pers.solid.mishang.uc.block.HungSignBlock;
import pers.solid.mishang.uc.block.StandingSignBlock;
import pers.solid.mishang.uc.block.WallSignBlock;
import pers.solid.mishang.uc.item.HungSignBlockItem;
import pers.solid.mishang.uc.item.NamedBlockItem;
import pers.solid.mishang.uc.item.StandingSignBlockItem;
import pers.solid.mishang.uc.item.WallSignBlockItem;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.ToIntFunction;

/**
 * 迷上城建模组的所有方块。
 */
public class MishangucBlocks {
  public static final ToIntFunction<BlockState> CONSTANT_15 = state -> 15;

  /**
   * 绝大多数柏油路方块共用的方块设置。
   */
  protected static final AbstractBlock.Settings ROAD_SETTINGS =
      AbstractBlock.Settings.create().mapColor(MapColor.GRAY).strength(0.5F);
  /**
   * 具有白色标线的道路方块使用的方块设置。
   */
  protected static final AbstractBlock.Settings WHITE_ROAD_SETTINGS = AbstractBlock.Settings.create().mapColor(MapColor.GRAY).strength(0.5F).mapColor(MapColor.WHITE);
  /**
   * 具有黄色标线的道路方块使用的方块设置。
   */
  protected static final AbstractBlock.Settings YELLOW_ROAD_SETTINGS = AbstractBlock.Settings.create().mapColor(MapColor.GRAY).strength(0.5F).mapColor(MapColor.YELLOW);

  /**
   * 绝大多数白色光方块共用的方块设置。
   */
  protected static final AbstractBlock.Settings WHITE_LIGHT_SETTINGS =
      AbstractBlock.Settings.create().mapColor(MapColor.WHITE).luminance(CONSTANT_15).strength(0.2f);
  /**
   * 墙上的白色灯等方块等用到的方块设置。与{@link #WHITE_LIGHT_SETTINGS}相比，该方块设置具有{@code noCollision}属性。
   */
  protected static final AbstractBlock.Settings WHITE_WALL_LIGHT_SETTINGS =
      AbstractBlock.Settings.create().mapColor(MapColor.WHITE).luminance(CONSTANT_15).strength(0.2f).noCollision();
  /**
   * 绝大多数黄色光方块共用的方块设置。
   */
  protected static final AbstractBlock.Settings YELLOW_LIGHT_SETTINGS = AbstractBlock.Settings.create().mapColor(MapColor.YELLOW).luminance(CONSTANT_15).strength(0.2f);
  /**
   * 墙上的黄色灯等方块等用到的方块设置。与{@link #YELLOW_LIGHT_SETTINGS}相比，该方块设置具有{@code noCollision}属性。
   */
  protected static final AbstractBlock.Settings YELLOW_WALL_LIGHT_SETTINGS =
      AbstractBlock.Settings.create().mapColor(MapColor.YELLOW).luminance(CONSTANT_15).strength(0.2f).noCollision();
  /**
   * 绝大多数青色光方块共用的方块设置。
   */
  protected static final AbstractBlock.Settings CYAN_LIGHT_SETTINGS = AbstractBlock.Settings.create().mapColor(MapColor.CYAN).luminance(CONSTANT_15).strength(0.2f);
  /**
   * 墙上的青色灯等方块等用到的方块设置。与{@link #YELLOW_LIGHT_SETTINGS}相比，该方块设置具有{@code noCollision}属性。
   */
  protected static final AbstractBlock.Settings CYAN_WALL_LIGHT_SETTINGS =
      AbstractBlock.Settings.create().mapColor(MapColor.CYAN).luminance(CONSTANT_15).strength(0.2f).noCollision();
  @ApiStatus.AvailableSince("1.1.0")
  protected static final AbstractBlock.Settings ORANGE_LIGHT_SETTINGS = AbstractBlock.Settings.create().mapColor(DyeColor.ORANGE).luminance(CONSTANT_15).strength(0.2f);
  @ApiStatus.AvailableSince("1.1.0")
  protected static final AbstractBlock.Settings ORANGE_WALL_LIGHT_SETTINGS = AbstractBlock.Settings.create().mapColor(DyeColor.ORANGE).luminance(CONSTANT_15).strength(0.2f).noCollision();
  @ApiStatus.AvailableSince("1.1.0")
  protected static final AbstractBlock.Settings GREEN_LIGHT_SETTINGS = AbstractBlock.Settings.create().mapColor(DyeColor.GREEN).luminance(CONSTANT_15).strength(0.2f);
  @ApiStatus.AvailableSince("1.1.0")
  protected static final AbstractBlock.Settings GREEN_WALL_LIGHT_SETTINGS = AbstractBlock.Settings.create().mapColor(DyeColor.GREEN).luminance(CONSTANT_15).noCollision();
  @ApiStatus.AvailableSince("1.1.0")
  protected static final AbstractBlock.Settings PINK_LIGHT_SETTINGS = AbstractBlock.Settings.create().mapColor(DyeColor.PINK).luminance(CONSTANT_15).strength(0.2f);
  @ApiStatus.AvailableSince("1.1.0")
  protected static final AbstractBlock.Settings PINK_WALL_LIGHT_SETTINGS = AbstractBlock.Settings.create().mapColor(DyeColor.PINK).luminance(CONSTANT_15).noCollision();
  @ApiStatus.Internal
  public static ObjectArrayList<Block> translucentBlocks = new ObjectArrayList<>();
  @ApiStatus.Internal
  public static ObjectArrayList<Block> cutoutBlocks = new ObjectArrayList<>();

  /**
   * 自动注册一个类中的所有静态常量字段的方块，同时创建并注册对应的物品。
   *
   * @see CustomId
   */
  private static <T> void registerAll(Class<T> cls) {
    for (Field field : cls.getFields()) {
      int modifier = field.getModifiers();
      final Class<?> fieldType = field.getType();
      if (Modifier.isFinal(modifier)
          && Modifier.isStatic(modifier)
          && Block.class.isAssignableFrom(fieldType)) {
        try {

          // 注册方块。
          Block value = (Block) field.get(null);
          String path;
          if (field.isAnnotationPresent(CustomId.class)) {
            final CustomId annotation = field.getAnnotation(CustomId.class);
            path = annotation.path();
          } else {
            path = field.getName().toLowerCase();
          }
          if (field.isAnnotationPresent(Cutout.class)) {
            cutoutBlocks.add(value);
          } else if (field.isAnnotationPresent(Translucent.class)) {
            translucentBlocks.add(value);
            if (value instanceof HandrailBlock) {
              translucentBlocks.add(((HandrailBlock) value).central());
              translucentBlocks.add(((HandrailBlock) value).corner());
              translucentBlocks.add(((HandrailBlock) value).outer());
              translucentBlocks.add(((HandrailBlock) value).stair());
            }
          }
          final Item.Settings settings = new Item.Settings();
          if (path.contains("netherite")) {
            settings.fireproof();
          }
          final BiFunction<Block, Item.Settings, Item> biFunction =
              value instanceof HungSignBlock
                  ? HungSignBlockItem::new
                  : value instanceof WallSignBlock
                  ? WallSignBlockItem::new
                  : value instanceof StandingSignBlock
                  ? StandingSignBlockItem::new
                  : NamedBlockItem::new;
          Items.register(value, biFunction, settings);
        } catch (IllegalAccessException e) {
          Mishanguc.MISHANG_LOGGER.error("Error when registering blocks:", e);
        }
      }
    }
  }

  public static void init() {
    registerAll(RoadBlocks.class);
    RoadSlabBlocks.registerAll();
    registerAll(RoadMarkBlocks.class);
    registerAll(LightBlocks.class);
    registerAll(WallSignBlocks.class);
    registerAll(HungSignBlocks.class);
    registerAll(StandingSignBlocks.class);
    registerAll(HandrailBlocks.class);
    registerAll(ColoredBlocks.class);
  }

  public static <T extends Block> T register(String name, Function<AbstractBlock.Settings, T> factory) {
    return register(name, factory, AbstractBlock.Settings.create());
  }

  @SuppressWarnings("unchecked")
  public static <T extends Block> T register(String name, Function<AbstractBlock.Settings, T> factory, AbstractBlock.Settings settings) {
    final Block block = Blocks.register(RegistryKey.of(RegistryKeys.BLOCK, Mishanguc.id(name)), factory::apply, settings);
    return (T) block;
  }

  public static <T extends Block> T register(String name, Function<AbstractBlock.Settings, T> factory, Block copySettingsFrom) {
    return register(name, factory, AbstractBlock.Settings.copy(copySettingsFrom));
  }
}
