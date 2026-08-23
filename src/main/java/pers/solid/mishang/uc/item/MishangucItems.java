package pers.solid.mishang.uc.item;

import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.Rarity;
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

  public static final RoadConnectionStateDebuggingToolItem ROAD_CONNECTION_STATE_DEBUGGING_TOOL = register("road_connection_state_debugging_tool", settings -> new RoadConnectionStateDebuggingToolItem(settings, false), new Item.Properties().stacksTo(1));

  public static final IdCheckerToolItem ID_CHECKER_TOOL = register("id_checker_tool", settings -> new IdCheckerToolItem(settings, null), new Item.Properties().stacksTo(1));

  public static final IdCheckerToolItem FLUID_ID_CHECKER_TOOL = register("fluid_id_checker_tool", settings -> new IdCheckerToolItem(settings, true), new Item.Properties().stacksTo(1));

  public static final FastBuildingToolItem FAST_BUILDING_TOOL = register("fast_building_tool", settings -> new FastBuildingToolItem(settings.stacksTo(1), null));

  public static final ColumnBuildingTool COLUMN_BUILDING_TOOL = register("column_building_tool", settings -> new ColumnBuildingTool(settings.stacksTo(1), null));

  public static final RotatingToolItem ROTATING_TOOL = register("rotating_tool", settings -> new RotatingToolItem(settings.durability(512), null));

  public static final MirroringToolItem MIRRORING_TOOL = register("mirroring_tool", settings -> new MirroringToolItem(settings.durability(512), null));

  public static final SlabToolItem SLAB_TOOL = register("slab_tool", settings -> new SlabToolItem(settings.durability(1024)));

  public static final ForcePlacingToolItem FORCE_PLACING_TOOL = register("force_placing_tool", settings -> new ForcePlacingToolItem(settings.stacksTo(1), null));

  public static final ForcePlacingToolItem FLUID_FORCE_PLACING_TOOL = register("fluid_force_placing_tool", settings -> new ForcePlacingToolItem(settings.stacksTo(1), true));

  public static final BlockStateToolItem BLOCK_STATE_TOOL = register("block_state_tool", settings -> new BlockStateToolItem(settings.stacksTo(1), null));

  public static final BlockStateToolItem FLUID_STATE_TOOL = register("fluid_state_tool", settings -> new BlockStateToolItem(settings.stacksTo(1), true));

  public static final DataTagToolItem DATA_TAG_TOOL = register("data_tag_tool", settings -> new DataTagToolItem(settings.stacksTo(1), null));

  public static final TextCopyToolItem TEXT_COPY_TOOL = register("text_copy_tool", settings -> new TextCopyToolItem(settings.durability(1024), null));

  public static final OmnipotentToolItem OMNIPOTENT_TOOL = register("omnipotent_tool", settings -> new OmnipotentToolItem(settings.fireResistant().rarity(Rarity.EPIC).stacksTo(1)));

  public static final ExplosionToolItem EXPLOSION_TOOL = register("explosion_tool", settings -> new ExplosionToolItem(settings.durability(1024)));

  @ApiStatus.AvailableSince("0.2.1")
  public static final ColorToolItem COLOR_TOOL = register("color_tool", settings -> new ColorToolItem(settings.durability(1024).component(MishangucComponents.OPACITY, 1f).component(MishangucComponents.COLOR_MIXTURE_TYPE, ColorMixtureType.NORMAL), null));

  @ApiStatus.AvailableSince("0.2.4")
  public static final RoadToolItem ROAD_TOOL = register("road_tool", settings -> new RoadToolItem(settings.durability(512)));
  @ApiStatus.AvailableSince("0.2.4")
  public static final TpToolItem TP_TOOL = register("tp_tool", settings -> new TpToolItem(settings.durability(2048)));
  @ApiStatus.AvailableSince("0.2.4")
  public static final GrowthToolItem GROWTH_TOOL = register("growth_tool", settings -> new GrowthToolItem(settings.durability(1024)));
  @ApiStatus.AvailableSince("0.2.4")
  public static final CarryingToolItem CARRYING_TOOL = register("carrying_tool", settings -> new CarryingToolItem(settings.stacksTo(1), null));
  public static final IceSnowTool ICE_SNOW_TOOL = register("ice_snow_tool", settings -> new IceSnowTool(settings.durability(128)));

  private MishangucItems() {
  }

  /**
   * @see Items#registerItem(ResourceKey, Function)
   */
  private static <T extends Item> T register(String name, Function<Item.Properties, T> factory) {
    return register(name, factory, new Item.Properties());
  }

  /**
   * @see Items#registerItem(ResourceKey, Function, Item.Properties)
   */
  private static <T extends Item> T register(String name, Function<Item.Properties, T> factory, Item.Properties settings) {
    final ResourceKey<Item> registryKey = ResourceKey.create(Registries.ITEM, Mishanguc.id(name));
    T item = factory.apply(settings.setId(registryKey));
    if (item instanceof BlockItem blockItem) {
      blockItem.registerBlocks(Item.BY_BLOCK, item);
    }
    members.add(item);

    return Registry.register(BuiltInRegistries.ITEM, registryKey, item);
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
