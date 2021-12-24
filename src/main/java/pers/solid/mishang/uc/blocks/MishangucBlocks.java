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
import org.jetbrains.annotations.Nullable;
import pers.solid.mishang.uc.MishangUc;
import pers.solid.mishang.uc.ModItemGroups;
import pers.solid.mishang.uc.annotations.InGroup;
import pers.solid.mishang.uc.annotations.RegisterIdentifier;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

/** 迷上城建模组的所有方块。 */
public class MishangucBlocks {
  @RegisterIdentifier
  @InGroup("lights")
  public static final Block WHITE_LAMP =
      new Block(FabricBlockSettings.of(Material.REDSTONE_LAMP).luminance(15));
  /** 绝大多数柏油路方块共用的方块设置。 */
  protected static final FabricBlockSettings ASPHALT_ROAD_SETTINGS =
      FabricBlockSettings.of(Material.STONE, MapColor.GRAY).strength(0.5F).breakByHand(true);

  /**
   * 自动注册一个类中的所有静态常量字段的方块，同时创建并注册对应的物品。
   *
   * @param cls 需要注册字段值的类。
   * @param <T> 类对象所属的类。
   * @see RegisterIdentifier
   */
  protected static <T> void registerAll(Class<T> cls) {
    // 需要将方块物品放入对应的组。
    @Nullable ItemGroup group = null;

    for (Field field : cls.getFields()) {
      int modifier = field.getModifiers();
      if (Modifier.isFinal(modifier)
          && Modifier.isStatic(modifier)
          && Block.class.isAssignableFrom(field.getType())) {
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
            // 如果找到InGroup注解，则将其放入该组，后面的所有方块也一并放到该组，直到再次发现该注解为止。
            if (field.isAnnotationPresent(InGroup.class)) {
              switch (field.getAnnotation(InGroup.class).value()) {
                case "roads":
                  group = ModItemGroups.ROADS;
                  break;
                case "lights":
                  group = ModItemGroups.LIGHTS;
                  break;
                default:
                  group = null;
              }
            }
            BlockItem item = new BlockItem(value, new FabricItemSettings().group(group));
            Registry.register(Registry.ITEM, new Identifier("mishanguc", path), item);
          }
        } catch (IllegalAccessException e) {
          MishangUc.MISHANG_LOGGER.error(e);
        }
      }
    }
  }

  protected MishangucBlocks() {}

  public static void init() {}
}
