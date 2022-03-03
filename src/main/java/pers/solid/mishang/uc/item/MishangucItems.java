package pers.solid.mishang.uc.item;

import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.minecraft.item.Item;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import pers.solid.mishang.uc.MishangUc;
import pers.solid.mishang.uc.MishangucItemGroups;
import pers.solid.mishang.uc.annotations.RegisterIdentifier;
import pers.solid.mishang.uc.annotations.SimpleModel;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

public final class MishangucItems {
  @RegisterIdentifier
  @SimpleModel(parent = "item/handheld")
  public static final RoadConnectionStateDebuggingToolItem ROAD_CONNECTION_STATE_DEBUGGING_TOOL =
      new RoadConnectionStateDebuggingToolItem(
          new FabricItemSettings().group(MishangucItemGroups.TOOLS), false);

  @RegisterIdentifier
  @SimpleModel(parent = "item/handheld")
  public static final IdCheckerToolItem ID_CHECKER_TOOL =
      new IdCheckerToolItem(new FabricItemSettings().group(MishangucItemGroups.TOOLS), null);

  @RegisterIdentifier
  @SimpleModel(parent = "item/handheld")
  public static final IdCheckerToolItem FLUID_ID_CHECKER_TOOL =
      new IdCheckerToolItem(new FabricItemSettings().group(MishangucItemGroups.TOOLS), true);

  @RegisterIdentifier
  @SimpleModel(parent = "item/handheld")
  public static final FastBuildingToolItem FAST_BUILDING_TOOL =
      new FastBuildingToolItem(new FabricItemSettings().group(MishangucItemGroups.TOOLS), null);

  @RegisterIdentifier
  @SimpleModel(parent = "item/handheld")
  public static final RotatingToolItem ROTATING_TOOL =
      new RotatingToolItem(new FabricItemSettings().group(MishangucItemGroups.TOOLS), null);

  @RegisterIdentifier
  @SimpleModel(parent = "item/handheld")
  public static final MirroringToolItem MIRRORING_TOOL =
      new MirroringToolItem(new FabricItemSettings().group(MishangucItemGroups.TOOLS), null);

  @RegisterIdentifier
  @SimpleModel(parent = "item/handheld")
  public static final SlabToolItem SLAB_TOOL =
      new SlabToolItem(new FabricItemSettings().group(MishangucItemGroups.TOOLS));

  @RegisterIdentifier
  @SimpleModel(parent = "item/handheld")
  public static final ForcePlacingToolItem FORCE_PLACING_TOOL =
      new ForcePlacingToolItem(new FabricItemSettings().group(MishangucItemGroups.TOOLS), null);

  @RegisterIdentifier
  @SimpleModel(parent = "item/handheld")
  public static final ForcePlacingToolItem FLUID_FORCE_PLACING_TOOL =
      new ForcePlacingToolItem(new FabricItemSettings().group(MishangucItemGroups.TOOLS), true);

  @RegisterIdentifier
  @SimpleModel(parent = "item/handheld")
  public static final BlockStateToolItem BLOCK_STATE_TOOL =
      new BlockStateToolItem(new FabricItemSettings().group(MishangucItemGroups.TOOLS), null);

  @RegisterIdentifier
  @SimpleModel(parent = "item/handheld")
  public static final BlockStateToolItem FLUID_STATE_TOOL =
      new BlockStateToolItem(new FabricItemSettings().group(MishangucItemGroups.TOOLS), true);

  @RegisterIdentifier
  @SimpleModel(parent = "item/handheld")
  public static final DataTagToolItem DATA_TAG_TOOL =
      new DataTagToolItem(new FabricItemSettings().group(MishangucItemGroups.TOOLS), null);

  private MishangucItems() {}

  private static void registerAll() {
    for (Field field : MishangucItems.class.getFields()) {
      int modifier = field.getModifiers();
      if (Modifier.isFinal(modifier)
          && Modifier.isStatic(modifier)
          && Item.class.isAssignableFrom(field.getType())) {
        try {
          // 注册物品。
          Item value = (Item) field.get(null);
          if (field.isAnnotationPresent(RegisterIdentifier.class)) {
            final RegisterIdentifier annotation = field.getAnnotation(RegisterIdentifier.class);
            String path = annotation.value();
            if (path.isEmpty()) {
              path = field.getName().toLowerCase();
            }
            Registry.register(Registry.ITEM, new Identifier("mishanguc", path), value);
          }
        } catch (IllegalAccessException e) {
          MishangUc.MISHANG_LOGGER.error("Error in registering items", e);
        }
      }
    }
  }

  public static void init() {
    registerAll();
  }
}
