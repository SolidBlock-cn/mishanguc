package pers.solid.mishang.uc.blocks;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.MapColor;
import org.jetbrains.annotations.ApiStatus;
import pers.solid.mishang.uc.Mishanguc;
import pers.solid.mishang.uc.annotations.CustomId;
import pers.solid.mishang.uc.block.HungSignBlock;
import pers.solid.mishang.uc.block.StandingSignBlock;
import pers.solid.mishang.uc.block.WallSignBlock;
import pers.solid.mishang.uc.item.HungSignBlockItem;
import pers.solid.mishang.uc.item.NamedBlockItem;
import pers.solid.mishang.uc.item.StandingSignBlockItem;
import pers.solid.mishang.uc.item.WallSignBlockItem;
import pers.solid.mishang.uc.mixin.ItemsAccessor;

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
  protected static final BlockBehaviour.Properties ROAD_SETTINGS =
      BlockBehaviour.Properties.of().mapColor(MapColor.COLOR_GRAY).strength(0.5F);
  /**
   * 具有白色标线的道路方块使用的方块设置。
   */
  protected static final BlockBehaviour.Properties WHITE_ROAD_SETTINGS = BlockBehaviour.Properties.of().mapColor(MapColor.COLOR_GRAY).strength(0.5F).mapColor(MapColor.SNOW);
  /**
   * 具有黄色标线的道路方块使用的方块设置。
   */
  protected static final BlockBehaviour.Properties YELLOW_ROAD_SETTINGS = BlockBehaviour.Properties.of().mapColor(MapColor.COLOR_GRAY).strength(0.5F).mapColor(MapColor.COLOR_YELLOW);

  /**
   * 绝大多数白色光方块共用的方块设置。
   */
  protected static final BlockBehaviour.Properties WHITE_LIGHT_SETTINGS =
      BlockBehaviour.Properties.of().mapColor(MapColor.SNOW).lightLevel(CONSTANT_15).strength(0.2f);
  /**
   * 墙上的白色灯等方块等用到的方块设置。与{@link #WHITE_LIGHT_SETTINGS}相比，该方块设置具有{@code noCollision}属性。
   */
  protected static final BlockBehaviour.Properties WHITE_WALL_LIGHT_SETTINGS =
      BlockBehaviour.Properties.of().mapColor(MapColor.SNOW).lightLevel(CONSTANT_15).strength(0.2f).noCollision();
  /**
   * 绝大多数黄色光方块共用的方块设置。
   */
  protected static final BlockBehaviour.Properties YELLOW_LIGHT_SETTINGS = BlockBehaviour.Properties.of().mapColor(MapColor.COLOR_YELLOW).lightLevel(CONSTANT_15).strength(0.2f);
  /**
   * 墙上的黄色灯等方块等用到的方块设置。与{@link #YELLOW_LIGHT_SETTINGS}相比，该方块设置具有{@code noCollision}属性。
   */
  protected static final BlockBehaviour.Properties YELLOW_WALL_LIGHT_SETTINGS =
      BlockBehaviour.Properties.of().mapColor(MapColor.COLOR_YELLOW).lightLevel(CONSTANT_15).strength(0.2f).noCollision();
  /**
   * 绝大多数青色光方块共用的方块设置。
   */
  protected static final BlockBehaviour.Properties CYAN_LIGHT_SETTINGS = BlockBehaviour.Properties.of().mapColor(MapColor.COLOR_CYAN).lightLevel(CONSTANT_15).strength(0.2f);
  /**
   * 墙上的青色灯等方块等用到的方块设置。与{@link #YELLOW_LIGHT_SETTINGS}相比，该方块设置具有{@code noCollision}属性。
   */
  protected static final BlockBehaviour.Properties CYAN_WALL_LIGHT_SETTINGS =
      BlockBehaviour.Properties.of().mapColor(MapColor.COLOR_CYAN).lightLevel(CONSTANT_15).strength(0.2f).noCollision();
  @ApiStatus.AvailableSince("1.1.0")
  protected static final BlockBehaviour.Properties ORANGE_LIGHT_SETTINGS = BlockBehaviour.Properties.of().mapColor(DyeColor.ORANGE).lightLevel(CONSTANT_15).strength(0.2f);
  @ApiStatus.AvailableSince("1.1.0")
  protected static final BlockBehaviour.Properties ORANGE_WALL_LIGHT_SETTINGS = BlockBehaviour.Properties.of().mapColor(DyeColor.ORANGE).lightLevel(CONSTANT_15).strength(0.2f).noCollision();
  @ApiStatus.AvailableSince("1.1.0")
  protected static final BlockBehaviour.Properties GREEN_LIGHT_SETTINGS = BlockBehaviour.Properties.of().mapColor(DyeColor.GREEN).lightLevel(CONSTANT_15).strength(0.2f);
  @ApiStatus.AvailableSince("1.1.0")
  protected static final BlockBehaviour.Properties GREEN_WALL_LIGHT_SETTINGS = BlockBehaviour.Properties.of().mapColor(DyeColor.GREEN).lightLevel(CONSTANT_15).noCollision();
  @ApiStatus.AvailableSince("1.1.0")
  protected static final BlockBehaviour.Properties PINK_LIGHT_SETTINGS = BlockBehaviour.Properties.of().mapColor(DyeColor.PINK).lightLevel(CONSTANT_15).strength(0.2f);
  @ApiStatus.AvailableSince("1.1.0")
  protected static final BlockBehaviour.Properties PINK_WALL_LIGHT_SETTINGS = BlockBehaviour.Properties.of().mapColor(DyeColor.PINK).lightLevel(CONSTANT_15).noCollision();

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
          final Item.Properties settings = new Item.Properties();
          if (path.contains("netherite")) {
            settings.fireResistant();
          }
          final BiFunction<Block, Item.Properties, Item> biFunction =
              value instanceof HungSignBlock
                  ? HungSignBlockItem::new
                  : value instanceof WallSignBlock
                  ? WallSignBlockItem::new
                  : value instanceof StandingSignBlock
                  ? StandingSignBlockItem::new
                  : NamedBlockItem::new;
          ItemsAccessor.callRegisterBlock(value, biFunction, settings);
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

  public static <T extends Block> T register(String name, Function<BlockBehaviour.Properties, T> factory) {
    return register(name, factory, BlockBehaviour.Properties.of());
  }

  @SuppressWarnings("unchecked")
  public static <T extends Block> T register(String name, Function<BlockBehaviour.Properties, T> factory, BlockBehaviour.Properties settings) {
    final Block block = Blocks.register(ResourceKey.create(Registries.BLOCK, Mishanguc.id(name)), factory::apply, settings);
    return (T) block;
  }

  public static <T extends Block> T register(String name, Function<BlockBehaviour.Properties, T> factory, Block copySettingsFrom) {
    return register(name, factory, BlockBehaviour.Properties.ofFullCopy(copySettingsFrom));
  }
}
