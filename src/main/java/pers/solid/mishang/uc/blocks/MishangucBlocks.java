package pers.solid.mishang.uc.blocks;

import com.google.common.annotations.Beta;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.Block;
import net.minecraft.block.MapColor;
import net.minecraft.block.Material;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemGroup;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import pers.solid.mishang.uc.Mishanguc;
import pers.solid.mishang.uc.MishangucItemGroups;
import pers.solid.mishang.uc.annotations.RegisterIdentifier;
import pers.solid.mishang.uc.block.HandrailBlock;
import pers.solid.mishang.uc.block.HungSignBlock;
import pers.solid.mishang.uc.block.WallSignBlock;
import pers.solid.mishang.uc.item.HungSignBlockItem;
import pers.solid.mishang.uc.item.NamedBlockItem;
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
      FabricBlockSettings.of(Material.STONE, MapColor.GRAY).strength(0.5F);
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
      FabricBlockSettings.of(Material.REDSTONE_LAMP, MapColor.WHITE).luminance(15).strength(0.2f);
  /**
   * 墙上的白色灯等方块等用到的方块设置。与{@link #WHITE_LIGHT_SETTINGS}相比，该方块设置具有{@code noCollision}属性。
   */
  protected static final FabricBlockSettings WHITE_WALL_LIGHT_SETTINGS =
      FabricBlockSettings.copyOf(WHITE_LIGHT_SETTINGS).noCollision();
  /**
   * 绝大多数黄色光方块共用的方块设置。
   */
  protected static final FabricBlockSettings YELLOW_LIGHT_SETTINGS = FabricBlockSettings.of(Material.REDSTONE_LAMP, MapColor.YELLOW).luminance(14).strength(0.2f);
  /**
   * 墙上的黄色灯等方块等用到的方块设置。与{@link #YELLOW_LIGHT_SETTINGS}相比，该方块设置具有{@code noCollision}属性。
   */
  protected static final FabricBlockSettings YELLOW_WALL_LIGHT_SETTINGS =
      FabricBlockSettings.copyOf(YELLOW_LIGHT_SETTINGS).noCollision();
  /**
   * 绝大多数青色光方块共用的方块设置。
   */
  protected static final FabricBlockSettings CYAN_LIGHT_SETTINGS = FabricBlockSettings.of(Material.REDSTONE_LAMP, MapColor.CYAN).luminance(13).strength(0.2f);
  /**
   * 墙上的青色灯等方块等用到的方块设置。与{@link #YELLOW_LIGHT_SETTINGS}相比，该方块设置具有{@code noCollision}属性。
   */
  protected static final FabricBlockSettings CYAN_WALL_LIGHT_SETTINGS =
      FabricBlockSettings.copyOf(CYAN_LIGHT_SETTINGS).noCollision();

  /**
   * 自动注册一个类中的所有静态常量字段的方块，同时创建并注册对应的物品。
   *
   * @see RegisterIdentifier
   */
  private static <T> void registerAll(Class<T> cls, final ItemGroup group) {
    for (Field field : cls.getFields()) {
      int modifier = field.getModifiers();
      final Class<?> fieldType = field.getType();
      if (Modifier.isFinal(modifier)
          && Modifier.isStatic(modifier)
          && Block.class.isAssignableFrom(fieldType)) {
        try {

          // 注册方块。
          Block value = (Block) field.get(null);
          if (field.isAnnotationPresent(RegisterIdentifier.class)) {
            final RegisterIdentifier annotation = field.getAnnotation(RegisterIdentifier.class);
            String path = annotation.value();
            if (path.isEmpty()) {
              path = field.getName().toLowerCase();
            }
            Registry.register(Registry.BLOCK, new Identifier("mishanguc", path), value);
            if (value instanceof HandrailBlock) {
              // 如果该方块为 HandrailBlock，则一并注册其 central 方块，应为该方块并没有作为字段存在。
              // 此类方块也没有对应的方块物品，其物品为对应的基础方块的物品。
              Registry.register(Registry.BLOCK, new Identifier("mishanguc", path + "_central"), ((HandrailBlock) value).central());
              Registry.register(Registry.BLOCK, new Identifier("mishanguc", path + "_corner"), ((HandrailBlock) value).corner());
              Registry.register(Registry.BLOCK, new Identifier("mishanguc", path + "_stair"), ((HandrailBlock) value).stair());
              Registry.register(Registry.BLOCK, new Identifier("mishanguc", path + "_outer"), ((HandrailBlock) value).outer());
            }
            final FabricItemSettings settings = new FabricItemSettings().group(field.isAnnotationPresent(Beta.class) ? null : group);
            final BlockItem item =
                HungSignBlock.class.isAssignableFrom(fieldType)
                    ? new HungSignBlockItem(value, settings)
                    : WallSignBlock.class.isAssignableFrom(fieldType)
                    ? new WallSignBlockItem(value, settings)
                    : new NamedBlockItem(value, settings);
            Registry.register(Registry.ITEM, new Identifier("mishanguc", path), item);
          }
        } catch (IllegalAccessException e) {
          Mishanguc.MISHANG_LOGGER.error("Error when registering blocks:", e);
        }
      }
    }
  }

  public static void init() {
    registerAll(RoadBlocks.class, MishangucItemGroups.ROADS);
    registerAll(RoadSlabBlocks.class, MishangucItemGroups.ROADS);
    registerAll(LightBlocks.class, MishangucItemGroups.LIGHTS);
    registerAll(HungSignBlocks.class, MishangucItemGroups.SIGNS);
    registerAll(WallSignBlocks.class, MishangucItemGroups.SIGNS);
    registerAll(HandrailBlocks.class, MishangucItemGroups.DECORATIONS);
  }
}
