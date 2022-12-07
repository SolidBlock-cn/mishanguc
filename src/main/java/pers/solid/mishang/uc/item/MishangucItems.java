package pers.solid.mishang.uc.item;

import net.devtech.arrp.generator.ItemResourceGenerator;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.minecraft.item.Item;
import net.minecraft.recipe.book.RecipeCategory;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;
import net.minecraft.util.Rarity;
import org.jetbrains.annotations.ApiStatus;
import pers.solid.mishang.uc.Mishanguc;
import pers.solid.mishang.uc.annotations.RegisterIdentifier;
import pers.solid.mishang.uc.annotations.SimpleModel;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

public final class MishangucItems {
  @RegisterIdentifier
  @SimpleModel(parent = "item/handheld")
  public static final RoadConnectionStateDebuggingToolItem ROAD_CONNECTION_STATE_DEBUGGING_TOOL =
      new RoadConnectionStateDebuggingToolItem(
          new FabricItemSettings(), false);

  @RegisterIdentifier
  @SimpleModel(parent = "item/handheld")
  public static final IdCheckerToolItem ID_CHECKER_TOOL =
      new IdCheckerToolItem(new FabricItemSettings().maxCount(1), null);

  @RegisterIdentifier
  @SimpleModel(parent = "item/handheld")
  public static final IdCheckerToolItem FLUID_ID_CHECKER_TOOL =
      new IdCheckerToolItem(new FabricItemSettings().maxCount(1), true);

  @RegisterIdentifier
  public static final FastBuildingToolItem FAST_BUILDING_TOOL =
      new FastBuildingToolItem(new FabricItemSettings().maxCount(1), null);

  @RegisterIdentifier
  @SimpleModel(parent = "item/handheld")
  public static final RotatingToolItem ROTATING_TOOL =
      new RotatingToolItem(new FabricItemSettings().maxDamage(512), null);

  @RegisterIdentifier
  @SimpleModel(parent = "item/handheld")
  public static final MirroringToolItem MIRRORING_TOOL =
      new MirroringToolItem(new FabricItemSettings().maxDamage(512), null);

  @RegisterIdentifier
  @SimpleModel(parent = "item/handheld")
  public static final SlabToolItem SLAB_TOOL =
      new SlabToolItem(new FabricItemSettings().maxDamage(1024));

  @RegisterIdentifier
  @SimpleModel(parent = "item/handheld")
  public static final ForcePlacingToolItem FORCE_PLACING_TOOL =
      new ForcePlacingToolItem(new FabricItemSettings().maxCount(1), null);

  @RegisterIdentifier
  @SimpleModel(parent = "item/handheld")
  public static final ForcePlacingToolItem FLUID_FORCE_PLACING_TOOL =
      new ForcePlacingToolItem(new FabricItemSettings().maxCount(1), true);

  @RegisterIdentifier
  @SimpleModel(parent = "item/handheld")
  public static final BlockStateToolItem BLOCK_STATE_TOOL =
      new BlockStateToolItem(new FabricItemSettings().maxCount(1), null);

  @RegisterIdentifier
  @SimpleModel(parent = "item/handheld")
  public static final BlockStateToolItem FLUID_STATE_TOOL =
      new BlockStateToolItem(new FabricItemSettings().maxCount(1), true);

  @RegisterIdentifier
  @SimpleModel(parent = "item/handheld")
  public static final DataTagToolItem DATA_TAG_TOOL =
      new DataTagToolItem(new FabricItemSettings().maxCount(1), null);

  @RegisterIdentifier
  @SimpleModel(parent = "item/handheld")
  public static final TextCopyToolItem TEXT_COPY_TOOL = new TextCopyToolItem(new FabricItemSettings().maxDamage(1024), null);

  @RegisterIdentifier
  @SimpleModel(parent = "item/handheld")
  public static final OmnipotentToolItem OMNIPOTENT_TOOL = new OmnipotentToolItem(new FabricItemSettings().fireproof().rarity(Rarity.EPIC).maxCount(1));

  @RegisterIdentifier
  public static final ExplosionToolItem EXPLOSION_TOOL = new ExplosionToolItem(new FabricItemSettings().maxDamage(1024));

  @RegisterIdentifier
  @ApiStatus.AvailableSince("0.2.1")
  public static final ColorToolItem COLOR_TOOL = new ColorToolItem(new FabricItemSettings().maxDamage(1024), null);

  @RegisterIdentifier
  @ApiStatus.AvailableSince("0.2.4")
  public static final RoadToolItem ROAD_TOOL = new RoadToolItem(new FabricItemSettings().maxDamage(512));
  @RegisterIdentifier
  @ApiStatus.AvailableSince("0.2.4")
  @SimpleModel(parent = "item/handheld")
  public static final TpToolItem TP_TOOL = new TpToolItem(new FabricItemSettings().maxDamage(2048));
  @RegisterIdentifier
  @ApiStatus.AvailableSince("0.2.4")
  @SimpleModel(parent = "item/handheld")
  public static final GrowthToolItem GROWTH_TOOL = new GrowthToolItem(new FabricItemSettings().maxDamage(1024));
  @RegisterIdentifier
  @ApiStatus.AvailableSince("0.2.4")
  @SimpleModel(parent = "item/generated", texture = "item/diamond")
  public static final CarryingToolItem CARRYING_TOOL = new CarryingToolItem(new FabricItemSettings().maxCount(1), null);

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
          ItemResourceGenerator.ITEM_TO_RECIPE_CATEGORY.put(value, RecipeCategory.TOOLS);
          if (field.isAnnotationPresent(RegisterIdentifier.class)) {
            final RegisterIdentifier annotation = field.getAnnotation(RegisterIdentifier.class);
            String path = annotation.value();
            if (path.isEmpty()) {
              path = field.getName().toLowerCase();
            }
            Registry.register(Registries.ITEM, new Identifier("mishanguc", path), value);
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
