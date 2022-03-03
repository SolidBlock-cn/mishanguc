package pers.solid.mishang.uc.blocks;

import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.Block;
import net.minecraft.block.MapColor;
import net.minecraft.block.Material;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemGroup;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import pers.solid.mishang.uc.MishangUc;
import pers.solid.mishang.uc.MishangucItemGroups;
import pers.solid.mishang.uc.annotations.RegisterIdentifier;
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
  protected static final FabricBlockSettings ASPHALT_ROAD_SETTINGS =
      FabricBlockSettings.of(Material.STONE, MapColor.GRAY).strength(0.5F);

  /**
   * 绝大多数光源方块共用的方块设置。
   */
  protected static final FabricBlockSettings WHITE_LIGHT_SETTINGS =
      FabricBlockSettings.of(Material.REDSTONE_LAMP).luminance(15);

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
            final FabricItemSettings settings = new FabricItemSettings().group(group);
            final BlockItem item =
                HungSignBlock.class.isAssignableFrom(fieldType)
                    ? new HungSignBlockItem(value, settings)
                    : WallSignBlock.class.isAssignableFrom(fieldType)
                    ? new WallSignBlockItem(value, settings)
                    : new NamedBlockItem(value, settings);
            Registry.register(Registry.ITEM, new Identifier("mishanguc", path), item);
          }
        } catch (IllegalAccessException e) {
          MishangUc.MISHANG_LOGGER.error("Error in registering blocks:", e);
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
  }
}
