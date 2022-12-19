package pers.solid.mishang.uc.item;

import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.minecraft.item.Item;
import net.minecraft.util.Identifier;
import net.minecraft.util.Rarity;
import net.minecraft.util.registry.Registry;
import org.jetbrains.annotations.ApiStatus;
import pers.solid.mishang.uc.Mishanguc;
import pers.solid.mishang.uc.MishangucItemGroups;
import pers.solid.mishang.uc.annotations.CustomId;
import pers.solid.mishang.uc.annotations.SimpleModel;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

public final class MishangucItems {
  @SimpleModel(parent = "item/handheld")
  public static final RoadConnectionStateDebuggingToolItem ROAD_CONNECTION_STATE_DEBUGGING_TOOL =
      new RoadConnectionStateDebuggingToolItem(
          new FabricItemSettings().group(MishangucItemGroups.TOOLS).maxCount(1), false);

  @SimpleModel(parent = "item/handheld")
  public static final IdCheckerToolItem ID_CHECKER_TOOL =
      new IdCheckerToolItem(new FabricItemSettings().group(MishangucItemGroups.TOOLS).maxCount(1), null);

  @SimpleModel(parent = "item/handheld")
  public static final IdCheckerToolItem FLUID_ID_CHECKER_TOOL =
      new IdCheckerToolItem(new FabricItemSettings().group(MishangucItemGroups.TOOLS).maxCount(1), true);

  public static final FastBuildingToolItem FAST_BUILDING_TOOL =
      new FastBuildingToolItem(new FabricItemSettings().group(MishangucItemGroups.TOOLS).maxCount(1), null);

  @SimpleModel(parent = "item/handheld")
  public static final RotatingToolItem ROTATING_TOOL =
      new RotatingToolItem(new FabricItemSettings().group(MishangucItemGroups.TOOLS).maxDamage(512), null);

  @SimpleModel(parent = "item/handheld")
  public static final MirroringToolItem MIRRORING_TOOL =
      new MirroringToolItem(new FabricItemSettings().group(MishangucItemGroups.TOOLS).maxDamage(512), null);

  @SimpleModel(parent = "item/handheld")
  public static final SlabToolItem SLAB_TOOL =
      new SlabToolItem(new FabricItemSettings().group(MishangucItemGroups.TOOLS).maxDamage(1024));

  @SimpleModel(parent = "item/handheld")
  public static final ForcePlacingToolItem FORCE_PLACING_TOOL =
      new ForcePlacingToolItem(new FabricItemSettings().group(MishangucItemGroups.TOOLS).maxCount(1), null);

  @SimpleModel(parent = "item/handheld")
  public static final ForcePlacingToolItem FLUID_FORCE_PLACING_TOOL =
      new ForcePlacingToolItem(new FabricItemSettings().group(MishangucItemGroups.TOOLS).maxCount(1), true);

  @SimpleModel(parent = "item/handheld")
  public static final BlockStateToolItem BLOCK_STATE_TOOL =
      new BlockStateToolItem(new FabricItemSettings().group(MishangucItemGroups.TOOLS).maxCount(1), null);

  @SimpleModel(parent = "item/handheld")
  public static final BlockStateToolItem FLUID_STATE_TOOL =
      new BlockStateToolItem(new FabricItemSettings().group(MishangucItemGroups.TOOLS).maxCount(1), true);

  @SimpleModel(parent = "item/handheld")
  public static final DataTagToolItem DATA_TAG_TOOL =
      new DataTagToolItem(new FabricItemSettings().group(MishangucItemGroups.TOOLS).maxCount(1), null);

  @SimpleModel(parent = "item/handheld")
  public static final TextCopyToolItem TEXT_COPY_TOOL = new TextCopyToolItem(new FabricItemSettings().group(MishangucItemGroups.TOOLS).maxDamage(1024), null);

  @SimpleModel(parent = "item/handheld")
  public static final OmnipotentToolItem OMNIPOTENT_TOOL = new OmnipotentToolItem(new FabricItemSettings().group(MishangucItemGroups.TOOLS).fireproof().rarity(Rarity.EPIC).maxCount(1));

  public static final ExplosionToolItem EXPLOSION_TOOL = new ExplosionToolItem(new FabricItemSettings().group(MishangucItemGroups.TOOLS).maxDamage(1024));

  @ApiStatus.AvailableSince("0.2.1")
  public static final ColorToolItem COLOR_TOOL = new ColorToolItem(new FabricItemSettings().group(MishangucItemGroups.TOOLS).maxDamage(1024), null);

  @ApiStatus.AvailableSince("0.2.4")
  public static final RoadToolItem ROAD_TOOL = new RoadToolItem(new FabricItemSettings().group(MishangucItemGroups.TOOLS).maxDamage(512));
  @ApiStatus.AvailableSince("0.2.4")
  @SimpleModel(parent = "item/handheld")
  public static final TpToolItem TP_TOOL = new TpToolItem(new FabricItemSettings().group(MishangucItemGroups.TOOLS).maxDamage(2048));
  @ApiStatus.AvailableSince("0.2.4")
  @SimpleModel(parent = "item/handheld")
  public static final GrowthToolItem GROWTH_TOOL = new GrowthToolItem(new FabricItemSettings().group(MishangucItemGroups.TOOLS).maxDamage(1024));
  @ApiStatus.AvailableSince("0.2.4")
  @SimpleModel(parent = "item/generated", texture = "item/diamond")
  public static final CarryingToolItem CARRYING_TOOL = new CarryingToolItem(new FabricItemSettings().group(MishangucItemGroups.TOOLS).maxCount(1), null);

  private MishangucItems() {
  }

  private static void registerAll() {
    for (Field field : MishangucItems.class.getFields()) {
      int modifier = field.getModifiers();
      if (Modifier.isFinal(modifier)
          && Modifier.isStatic(modifier)
          && Item.class.isAssignableFrom(field.getType())) {
        try {
          // 注册物品。
          Item value = (Item) field.get(null);
          {
            final CustomId annotation = field.getAnnotation(CustomId.class);
            String namespace, path;
            if (field.isAnnotationPresent(CustomId.class)) {
              namespace = annotation.nameSpace();
              path = annotation.path();
            } else {
              namespace = "mishanguc";
              path = field.getName().toLowerCase();
            }
            Registry.register(Registry.ITEM, new Identifier(namespace, path), value);
          }
        } catch (IllegalAccessException e) {
          Mishanguc.MISHANG_LOGGER.error("Error when registering items:", e);
        }
      }
    }
  }

  public static void init() {
    registerAll();
  }
}
