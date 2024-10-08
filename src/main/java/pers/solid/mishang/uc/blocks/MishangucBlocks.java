package pers.solid.mishang.uc.blocks;

import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.Block;
import net.minecraft.block.MapColor;
import net.minecraft.item.BlockItem;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.DyeColor;
import net.minecraft.util.Identifier;
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

/**
 * 迷上城建模组的所有方块。
 */
public class MishangucBlocks {

  /**
   * 绝大多数柏油路方块共用的方块设置。
   */
  protected static final FabricBlockSettings ROAD_SETTINGS =
      FabricBlockSettings.create().mapColor(MapColor.GRAY).strength(0.5F);
  /**
   * 具有白色标线的道路方块使用的方块设置。
   */
  protected static final FabricBlockSettings WHITE_ROAD_SETTINGS = FabricBlockSettings.copyOf(ROAD_SETTINGS).mapColor(MapColor.WHITE);
  /**
   * 具有黄色标线的道路方块使用的方块设置。
   */
  protected static final FabricBlockSettings YELLOW_ROAD_SETTINGS = FabricBlockSettings.copyOf(ROAD_SETTINGS).mapColor(MapColor.YELLOW);

  /**
   * 绝大多数白色光方块共用的方块设置。
   */
  protected static final FabricBlockSettings WHITE_LIGHT_SETTINGS =
      FabricBlockSettings.create().mapColor(MapColor.WHITE).luminance(15).strength(0.2f);
  /**
   * 墙上的白色灯等方块等用到的方块设置。与{@link #WHITE_LIGHT_SETTINGS}相比，该方块设置具有{@code noCollision}属性。
   */
  protected static final FabricBlockSettings WHITE_WALL_LIGHT_SETTINGS =
      FabricBlockSettings.copyOf(WHITE_LIGHT_SETTINGS).noCollision();
  /**
   * 绝大多数黄色光方块共用的方块设置。
   */
  protected static final FabricBlockSettings YELLOW_LIGHT_SETTINGS = FabricBlockSettings.create().mapColor(MapColor.YELLOW).luminance(15).strength(0.2f);
  /**
   * 墙上的黄色灯等方块等用到的方块设置。与{@link #YELLOW_LIGHT_SETTINGS}相比，该方块设置具有{@code noCollision}属性。
   */
  protected static final FabricBlockSettings YELLOW_WALL_LIGHT_SETTINGS =
      FabricBlockSettings.copyOf(YELLOW_LIGHT_SETTINGS).noCollision();
  /**
   * 绝大多数青色光方块共用的方块设置。
   */
  protected static final FabricBlockSettings CYAN_LIGHT_SETTINGS = FabricBlockSettings.create().mapColor(MapColor.CYAN).luminance(15).strength(0.2f);
  /**
   * 墙上的青色灯等方块等用到的方块设置。与{@link #YELLOW_LIGHT_SETTINGS}相比，该方块设置具有{@code noCollision}属性。
   */
  protected static final FabricBlockSettings CYAN_WALL_LIGHT_SETTINGS =
      FabricBlockSettings.copyOf(CYAN_LIGHT_SETTINGS).noCollision();
  @ApiStatus.AvailableSince("1.1.0")
  protected static final FabricBlockSettings ORANGE_LIGHT_SETTINGS = FabricBlockSettings.create().mapColor(DyeColor.ORANGE).luminance(15).strength(0.2f);
  @ApiStatus.AvailableSince("1.1.0")
  protected static final FabricBlockSettings ORANGE_WALL_LIGHT_SETTINGS = FabricBlockSettings.copyOf(ORANGE_LIGHT_SETTINGS).noCollision();
  @ApiStatus.AvailableSince("1.1.0")
  protected static final FabricBlockSettings GREEN_LIGHT_SETTINGS = FabricBlockSettings.create().mapColor(DyeColor.GREEN).luminance(15).strength(0.2f);
  @ApiStatus.AvailableSince("1.1.0")
  protected static final FabricBlockSettings GREEN_WALL_LIGHT_SETTINGS = FabricBlockSettings.copyOf(GREEN_LIGHT_SETTINGS).noCollision();
  @ApiStatus.AvailableSince("1.1.0")
  protected static final FabricBlockSettings PINK_LIGHT_SETTINGS = FabricBlockSettings.create().mapColor(DyeColor.PINK).luminance(15).strength(0.2f);
  @ApiStatus.AvailableSince("1.1.0")
  protected static final FabricBlockSettings PINK_WALL_LIGHT_SETTINGS = FabricBlockSettings.copyOf(PINK_LIGHT_SETTINGS).noCollision();
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
          String namespace, path;
          if (field.isAnnotationPresent(CustomId.class)) {
            final CustomId annotation = field.getAnnotation(CustomId.class);
            namespace = annotation.nameSpace();
            path = annotation.path();
          } else {
            namespace = "mishanguc";
            path = field.getName().toLowerCase();
          }
          Registry.register(Registries.BLOCK, new Identifier(namespace, path), value);
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
          if (value instanceof HandrailBlock handrailBlock) {
            // 如果该方块为 HandrailBlock，则一并注册其 central 方块，应为该方块并没有作为字段存在。
            // 此类方块也没有对应的方块物品，其物品为对应的基础方块的物品。
            Registry.register(Registries.BLOCK, Mishanguc.id(path + "_central"), handrailBlock.central());
            Registry.register(Registries.BLOCK, Mishanguc.id(path + "_corner"), handrailBlock.corner());
            Registry.register(Registries.BLOCK, Mishanguc.id(path + "_stair"), handrailBlock.stair());
            Registry.register(Registries.BLOCK, Mishanguc.id(path + "_outer"), handrailBlock.outer());
          }
          final FabricItemSettings settings = new FabricItemSettings();
          if (path.contains("netherite")) {
            settings.fireproof();
          }
          final BlockItem item =
              value instanceof HungSignBlock
                  ? new HungSignBlockItem(value, settings)
                  : value instanceof WallSignBlock
                  ? new WallSignBlockItem(value, settings)
                  : value instanceof StandingSignBlock
                  ? new StandingSignBlockItem(value, settings)
                  : new NamedBlockItem(value, settings);
          Registry.register(Registries.ITEM, Mishanguc.id(path), item);
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
}
