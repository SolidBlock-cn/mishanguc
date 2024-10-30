package pers.solid.mishang.uc.item;

import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.util.Rarity;
import org.apache.commons.lang3.Validate;
import org.jetbrains.annotations.ApiStatus;
import pers.solid.mishang.uc.Mishanguc;
import pers.solid.mishang.uc.components.MishangucComponents;
import pers.solid.mishang.uc.util.ColorMixtureType;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Stream;

public final class MishangucItems {

  private static final List<Item> members = new ArrayList<>();
  private static final List<Item> members_unmodifiable = Collections.unmodifiableList(members);

  public static final RoadConnectionStateDebuggingToolItem ROAD_CONNECTION_STATE_DEBUGGING_TOOL = register("road_connection_state_debugging_tool", settings -> new RoadConnectionStateDebuggingToolItem(settings, false), new Item.Settings().maxCount(1));

  public static final IdCheckerToolItem ID_CHECKER_TOOL = register("id_checker_tool", settings -> new IdCheckerToolItem(settings, null), new Item.Settings().maxCount(1));

  public static final IdCheckerToolItem FLUID_ID_CHECKER_TOOL = register("fluid_id_checker_tool", settings -> new IdCheckerToolItem(settings, true), new Item.Settings().maxCount(1));

  public static final FastBuildingToolItem FAST_BUILDING_TOOL = register("fast_building_tool", settings -> new FastBuildingToolItem(settings.maxCount(1), null));

  public static final ColumnBuildingTool COLUMN_BUILDING_TOOL = register("column_building_tool", settings -> new ColumnBuildingTool(settings.maxCount(1), null));

  public static final RotatingToolItem ROTATING_TOOL = register("rotating_tool", settings -> new RotatingToolItem(settings.maxDamage(512), null));

  public static final MirroringToolItem MIRRORING_TOOL = register("mirroring_tool", settings -> new MirroringToolItem(settings.maxDamage(512), null));

  public static final SlabToolItem SLAB_TOOL = register("slab_tool", settings -> new SlabToolItem(settings.maxDamage(1024)));

  public static final ForcePlacingToolItem FORCE_PLACING_TOOL = register("force_placing_tool", settings -> new ForcePlacingToolItem(settings.maxCount(1), null));

  public static final ForcePlacingToolItem FLUID_FORCE_PLACING_TOOL = register("fluid_force_placing_tool", settings -> new ForcePlacingToolItem(settings.maxCount(1), true));

  public static final BlockStateToolItem BLOCK_STATE_TOOL = register("block_state_tool", settings -> new BlockStateToolItem(settings.maxCount(1), null));

  public static final BlockStateToolItem FLUID_STATE_TOOL = register("fluid_state_tool", settings -> new BlockStateToolItem(settings.maxCount(1), true));

  public static final DataTagToolItem DATA_TAG_TOOL = register("data_tag_tool", settings -> new DataTagToolItem(settings.maxCount(1), null));

  public static final TextCopyToolItem TEXT_COPY_TOOL = register("text_copy_tool", settings -> new TextCopyToolItem(settings.maxDamage(1024), null));

  public static final OmnipotentToolItem OMNIPOTENT_TOOL = register("omnipotent_tool", settings -> new OmnipotentToolItem(settings.fireproof().rarity(Rarity.EPIC).maxCount(1)));

  public static final ExplosionToolItem EXPLOSION_TOOL = register("explosion_tool", settings -> new ExplosionToolItem(settings.maxDamage(1024)));

  @ApiStatus.AvailableSince("0.2.1")
  public static final ColorToolItem COLOR_TOOL = register("color_tool", settings -> new ColorToolItem(settings.maxDamage(1024).component(MishangucComponents.OPACITY, 1f).component(MishangucComponents.COLOR_MIXTURE_TYPE, ColorMixtureType.NORMAL), null));

  @ApiStatus.AvailableSince("0.2.4")
  public static final RoadToolItem ROAD_TOOL = register("road_tool", settings -> new RoadToolItem(settings.maxDamage(512)));
  @ApiStatus.AvailableSince("0.2.4")
  public static final TpToolItem TP_TOOL = register("tp_tool", settings -> new TpToolItem(settings.maxDamage(2048)));
  @ApiStatus.AvailableSince("0.2.4")
  public static final GrowthToolItem GROWTH_TOOL = register("growth_tool", settings -> new GrowthToolItem(settings.maxDamage(1024)));
  @ApiStatus.AvailableSince("0.2.4")
  public static final CarryingToolItem CARRYING_TOOL = register("carrying_tool", settings -> new CarryingToolItem(settings.maxCount(1), null));
  public static final IceSnowTool ICE_SNOW_TOOL = register("ice_snow_tool", settings -> new IceSnowTool(settings.maxDamage(128)));

  private MishangucItems() {
  }

  /**
   * @see Items#register(RegistryKey, Function)
   */
  private static <T extends Item> T register(String name, Function<Item.Settings, T> factory) {
    return register(name, factory, new Item.Settings());
  }

  /**
   * @see Items#register(RegistryKey, Function, Item.Settings)
   */
  private static <T extends Item> T register(String name, Function<Item.Settings, T> factory, Item.Settings settings) {
    final RegistryKey<Item> registryKey = RegistryKey.of(RegistryKeys.ITEM, Mishanguc.id(name));
    T item = factory.apply(settings.registryKey(registryKey));
    if (item instanceof BlockItem blockItem) {
      blockItem.appendBlocks(Item.BLOCK_ITEMS, item);
    }
    members.add(item);

    return Registry.register(Registries.ITEM, registryKey, item);
  }

  public static Stream<Item> stream() {
    return members.stream();
  }

  public static List<Item> items() {
    return members_unmodifiable;
  }

  public static void init() {
    Validate.notEmpty(members);
  }
}
