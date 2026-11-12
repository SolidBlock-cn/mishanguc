package pers.solid.mishang.uc.blocks;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.references.BlockItemId;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.ColorCollection;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.storage.loot.providers.number.ints.ContextIntProvider;
import net.minecraft.world.level.storage.loot.providers.number.ints.ContextIntProviders;
import org.jetbrains.annotations.ApiStatus;
import pers.solid.mishang.uc.Mishanguc;
import pers.solid.mishang.uc.annotations.CustomId;
import pers.solid.mishang.uc.block.HandrailBlock;
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
import java.util.*;
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
          && (Block.class.isAssignableFrom(fieldType) || ColorCollection.class.isAssignableFrom(fieldType))) {
        try {

          // 注册方块。
          final List<Block> blocks;
          switch (field.get(null)) {
            case Block block -> blocks = Collections.singletonList(block);
            case ColorCollection<?> colorCollection -> blocks = colorCollection.asList().stream().map(o -> o instanceof Block block ? block : null).filter(Objects::nonNull).toList();
            default -> {
              continue;
            }
          }

          for (Block block : blocks) {
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
            if (BlockCollection.woodenSignBlocks.contains(block) || BlockCollection.woodenHandrails.contains(block)) {
              settings.cookingFuel(ContextIntProviders.COOKING_TIME_WOOD_ITEMS_EXTRA_SMALL);
            }
            if (BlockCollection.blockFuels.containsKey(block)) {
              settings.cookingFuel(BlockCollection.blockFuels.get(block));
            }
            final BiFunction<Block, Item.Properties, Item> biFunction =
                block instanceof HungSignBlock
                    ? HungSignBlockItem::new
                    : block instanceof WallSignBlock
                    ? WallSignBlockItem::new
                    : block instanceof StandingSignBlock
                    ? StandingSignBlockItem::new
                    : NamedBlockItem::new;
            ItemsAccessor.callRegisterBlock(blockItemId(block), block, biFunction, settings);
          }
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

  static BlockItemId blockItemId(Block block) {
    final Identifier id = BuiltInRegistries.BLOCK.getKey(block);
    return BlockItemId.create(id, id);
  }

  public static final class BlockCollection {
    public static final Set<Block> woodenSignBlocks = ImmutableSet.of(
        HungSignBlocks.OAK_HUNG_SIGN,
        HungSignBlocks.SPRUCE_HUNG_SIGN,
        HungSignBlocks.BIRCH_HUNG_SIGN,
        HungSignBlocks.JUNGLE_HUNG_SIGN,
        HungSignBlocks.ACACIA_HUNG_SIGN,
        HungSignBlocks.CHERRY_HUNG_SIGN,
        HungSignBlocks.DARK_OAK_HUNG_SIGN,
        HungSignBlocks.PALE_OAK_HUNG_SIGN,
        HungSignBlocks.MANGROVE_HUNG_SIGN,
        HungSignBlocks.POPLAR_HUNG_SIGN,
        HungSignBlocks.OAK_WOOD_HUNG_SIGN,
        HungSignBlocks.SPRUCE_WOOD_HUNG_SIGN,
        HungSignBlocks.BIRCH_WOOD_HUNG_SIGN,
        HungSignBlocks.JUNGLE_WOOD_HUNG_SIGN,
        HungSignBlocks.ACACIA_WOOD_HUNG_SIGN,
        HungSignBlocks.CHERRY_WOOD_HUNG_SIGN,
        HungSignBlocks.DARK_OAK_WOOD_HUNG_SIGN,
        HungSignBlocks.PALE_OAK_WOOD_HUNG_SIGN,
        HungSignBlocks.MANGROVE_WOOD_HUNG_SIGN,
        HungSignBlocks.POPLAR_WOOD_HUNG_SIGN,
        HungSignBlocks.STRIPPED_OAK_WOOD_HUNG_SIGN,
        HungSignBlocks.STRIPPED_SPRUCE_WOOD_HUNG_SIGN,
        HungSignBlocks.STRIPPED_BIRCH_WOOD_HUNG_SIGN,
        HungSignBlocks.STRIPPED_JUNGLE_WOOD_HUNG_SIGN,
        HungSignBlocks.STRIPPED_ACACIA_WOOD_HUNG_SIGN,
        HungSignBlocks.STRIPPED_CHERRY_WOOD_HUNG_SIGN,
        HungSignBlocks.STRIPPED_DARK_OAK_WOOD_HUNG_SIGN,
        HungSignBlocks.STRIPPED_PALE_OAK_WOOD_HUNG_SIGN,
        HungSignBlocks.STRIPPED_MANGROVE_WOOD_HUNG_SIGN,
        HungSignBlocks.STRIPPED_POPLAR_WOOD_HUNG_SIGN,
        HungSignBlocks.BAMBOO_HUNG_SIGN,
        HungSignBlocks.BAMBOO_PLANK_HUNG_SIGN,
        HungSignBlocks.BAMBOO_MOSAIC_HUNG_SIGN,
        HungSignBlocks.OAK_HUNG_SIGN_BAR,
        HungSignBlocks.SPRUCE_HUNG_SIGN_BAR,
        HungSignBlocks.BIRCH_HUNG_SIGN_BAR,
        HungSignBlocks.JUNGLE_HUNG_SIGN_BAR,
        HungSignBlocks.ACACIA_HUNG_SIGN_BAR,
        HungSignBlocks.CHERRY_HUNG_SIGN_BAR,
        HungSignBlocks.DARK_OAK_HUNG_SIGN_BAR,
        HungSignBlocks.PALE_OAK_HUNG_SIGN_BAR,
        HungSignBlocks.MANGROVE_HUNG_SIGN_BAR,
        HungSignBlocks.POPLAR_HUNG_SIGN_BAR,
        HungSignBlocks.BAMBOO_HUNG_SIGN_BAR,
        HungSignBlocks.STRIPPED_OAK_HUNG_SIGN_BAR,
        HungSignBlocks.STRIPPED_SPRUCE_HUNG_SIGN_BAR,
        HungSignBlocks.STRIPPED_BIRCH_HUNG_SIGN_BAR,
        HungSignBlocks.STRIPPED_JUNGLE_HUNG_SIGN_BAR,
        HungSignBlocks.STRIPPED_ACACIA_HUNG_SIGN_BAR,
        HungSignBlocks.STRIPPED_CHERRY_HUNG_SIGN_BAR,
        HungSignBlocks.STRIPPED_DARK_OAK_HUNG_SIGN_BAR,
        HungSignBlocks.STRIPPED_PALE_OAK_HUNG_SIGN_BAR,
        HungSignBlocks.STRIPPED_MANGROVE_HUNG_SIGN_BAR,
        HungSignBlocks.STRIPPED_POPLAR_HUNG_SIGN_BAR,
        WallSignBlocks.OAK_WOOD_WALL_SIGN,
        WallSignBlocks.SPRUCE_WOOD_WALL_SIGN,
        WallSignBlocks.BIRCH_WOOD_WALL_SIGN,
        WallSignBlocks.JUNGLE_WOOD_WALL_SIGN,
        WallSignBlocks.ACACIA_WOOD_WALL_SIGN,
        WallSignBlocks.CHERRY_WOOD_WALL_SIGN,
        WallSignBlocks.DARK_OAK_WOOD_WALL_SIGN,
        WallSignBlocks.PALE_OAK_WOOD_WALL_SIGN,
        WallSignBlocks.MANGROVE_WOOD_WALL_SIGN,
        WallSignBlocks.POPLAR_WOOD_WALL_SIGN,
        WallSignBlocks.STRIPPED_OAK_WOOD_WALL_SIGN,
        WallSignBlocks.STRIPPED_SPRUCE_WOOD_WALL_SIGN,
        WallSignBlocks.STRIPPED_BIRCH_WOOD_WALL_SIGN,
        WallSignBlocks.STRIPPED_JUNGLE_WOOD_WALL_SIGN,
        WallSignBlocks.STRIPPED_ACACIA_WOOD_WALL_SIGN,
        WallSignBlocks.STRIPPED_CHERRY_WOOD_WALL_SIGN,
        WallSignBlocks.STRIPPED_DARK_OAK_WOOD_WALL_SIGN,
        WallSignBlocks.STRIPPED_PALE_OAK_WOOD_WALL_SIGN,
        WallSignBlocks.STRIPPED_MANGROVE_WOOD_WALL_SIGN,
        WallSignBlocks.STRIPPED_POPLAR_WOOD_WALL_SIGN,
        WallSignBlocks.OAK_WALL_SIGN,
        WallSignBlocks.SPRUCE_WALL_SIGN,
        WallSignBlocks.BIRCH_WALL_SIGN,
        WallSignBlocks.JUNGLE_WALL_SIGN,
        WallSignBlocks.ACACIA_WALL_SIGN,
        WallSignBlocks.CHERRY_WALL_SIGN,
        WallSignBlocks.DARK_OAK_WALL_SIGN,
        WallSignBlocks.PALE_OAK_WALL_SIGN,
        WallSignBlocks.MANGROVE_WALL_SIGN,
        WallSignBlocks.POPLAR_WALL_SIGN,
        WallSignBlocks.BAMBOO_WALL_SIGN,
        WallSignBlocks.BAMBOO_PLANK_WALL_SIGN,
        WallSignBlocks.BAMBOO_MOSAIC_WALL_SIGN,
        WallSignBlocks.COLORED_WOODEN_WALL_SIGN,
        StandingSignBlocks.OAK_STANDING_SIGN,
        StandingSignBlocks.SPRUCE_STANDING_SIGN,
        StandingSignBlocks.BIRCH_STANDING_SIGN,
        StandingSignBlocks.ACACIA_STANDING_SIGN,
        StandingSignBlocks.CHERRY_STANDING_SIGN,
        StandingSignBlocks.JUNGLE_STANDING_SIGN,
        StandingSignBlocks.DARK_OAK_STANDING_SIGN,
        StandingSignBlocks.PALE_OAK_STANDING_SIGN,
        StandingSignBlocks.MANGROVE_STANDING_SIGN,
        StandingSignBlocks.POPLAR_STANDING_SIGN,
        StandingSignBlocks.OAK_WOOD_STANDING_SIGN,
        StandingSignBlocks.SPRUCE_WOOD_STANDING_SIGN,
        StandingSignBlocks.BIRCH_WOOD_STANDING_SIGN,
        StandingSignBlocks.ACACIA_WOOD_STANDING_SIGN,
        StandingSignBlocks.CHERRY_WOOD_STANDING_SIGN,
        StandingSignBlocks.JUNGLE_WOOD_STANDING_SIGN,
        StandingSignBlocks.DARK_OAK_WOOD_STANDING_SIGN,
        StandingSignBlocks.PALE_OAK_WOOD_STANDING_SIGN,
        StandingSignBlocks.MANGROVE_WOOD_STANDING_SIGN,
        StandingSignBlocks.POPLAR_WOOD_STANDING_SIGN,
        StandingSignBlocks.STRIPPED_OAK_WOOD_STANDING_SIGN,
        StandingSignBlocks.STRIPPED_SPRUCE_WOOD_STANDING_SIGN,
        StandingSignBlocks.STRIPPED_BIRCH_WOOD_STANDING_SIGN,
        StandingSignBlocks.STRIPPED_ACACIA_WOOD_STANDING_SIGN,
        StandingSignBlocks.STRIPPED_CHERRY_WOOD_STANDING_SIGN,
        StandingSignBlocks.STRIPPED_JUNGLE_WOOD_STANDING_SIGN,
        StandingSignBlocks.STRIPPED_DARK_OAK_WOOD_STANDING_SIGN,
        StandingSignBlocks.STRIPPED_PALE_OAK_WOOD_STANDING_SIGN,
        StandingSignBlocks.STRIPPED_MANGROVE_WOOD_STANDING_SIGN,
        StandingSignBlocks.STRIPPED_POPLAR_WOOD_STANDING_SIGN,
        StandingSignBlocks.BAMBOO_STANDING_SIGN,
        StandingSignBlocks.BAMBOO_PLANK_STANDING_SIGN,
        StandingSignBlocks.BAMBOO_MOSAIC_STANDING_SIGN
    );

    public static final Collection<HandrailBlock> woodenHandrailSets = ImmutableSet.of(
        HandrailBlocks.SIMPLE_OAK_HANDRAIL,
        HandrailBlocks.SIMPLE_SPRUCE_HANDRAIL,
        HandrailBlocks.SIMPLE_BIRCH_HANDRAIL,
        HandrailBlocks.SIMPLE_JUNGLE_HANDRAIL,
        HandrailBlocks.SIMPLE_ACACIA_HANDRAIL,
        HandrailBlocks.SIMPLE_CHERRY_HANDRAIL,
        HandrailBlocks.SIMPLE_DARK_OAK_HANDRAIL,
        HandrailBlocks.SIMPLE_PALE_OAK_HANDRAIL,
        HandrailBlocks.SIMPLE_MANGROVE_HANDRAIL,
        HandrailBlocks.SIMPLE_POPLAR_HANDRAIL,
        HandrailBlocks.SIMPLE_OAK_PLANK_HANDRAIL,
        HandrailBlocks.SIMPLE_SPRUCE_PLANK_HANDRAIL,
        HandrailBlocks.SIMPLE_BIRCH_PLANK_HANDRAIL,
        HandrailBlocks.SIMPLE_JUNGLE_PLANK_HANDRAIL,
        HandrailBlocks.SIMPLE_ACACIA_PLANK_HANDRAIL,
        HandrailBlocks.SIMPLE_CHERRY_PLANK_HANDRAIL,
        HandrailBlocks.SIMPLE_DARK_OAK_PLANK_HANDRAIL,
        HandrailBlocks.SIMPLE_PALE_OAK_PLANK_HANDRAIL,
        HandrailBlocks.SIMPLE_MANGROVE_PLANK_HANDRAIL,
        HandrailBlocks.SIMPLE_POPLAR_PLANK_HANDRAIL,
        HandrailBlocks.SIMPLE_BAMBOO_HANDRAIL,
        HandrailBlocks.SIMPLE_BAMBOO_PLANK_HANDRAIL,
        HandrailBlocks.SIMPLE_BAMBOO_MOSAIC_HANDRAIL,
        HandrailBlocks.GLASS_OAK_HANDRAIL,
        HandrailBlocks.GLASS_SPRUCE_HANDRAIL,
        HandrailBlocks.GLASS_BIRCH_HANDRAIL,
        HandrailBlocks.GLASS_JUNGLE_HANDRAIL,
        HandrailBlocks.GLASS_ACACIA_HANDRAIL,
        HandrailBlocks.GLASS_CHERRY_HANDRAIL,
        HandrailBlocks.GLASS_DARK_OAK_HANDRAIL,
        HandrailBlocks.GLASS_PALE_OAK_HANDRAIL,
        HandrailBlocks.GLASS_MANGROVE_HANDRAIL,
        HandrailBlocks.GLASS_POPLAR_HANDRAIL,
        HandrailBlocks.GLASS_BAMBOO_HANDRAIL,
        HandrailBlocks.COLORED_DECORATED_OAK_HANDRAIL,
        HandrailBlocks.COLORED_DECORATED_SPRUCE_HANDRAIL,
        HandrailBlocks.COLORED_DECORATED_BIRCH_HANDRAIL,
        HandrailBlocks.COLORED_DECORATED_JUNGLE_HANDRAIL,
        HandrailBlocks.COLORED_DECORATED_ACACIA_HANDRAIL,
        HandrailBlocks.COLORED_DECORATED_CHERRY_HANDRAIL,
        HandrailBlocks.COLORED_DECORATED_DARK_OAK_HANDRAIL,
        HandrailBlocks.COLORED_DECORATED_PALE_OAK_HANDRAIL,
        HandrailBlocks.COLORED_DECORATED_MANGROVE_HANDRAIL,
        HandrailBlocks.COLORED_DECORATED_POPLAR_HANDRAIL,
        HandrailBlocks.COLORED_DECORATED_BAMBOO_HANDRAIL,
        HandrailBlocks.COLORED_DECORATED_STRIPPED_OAK_HANDRAIL,
        HandrailBlocks.COLORED_DECORATED_STRIPPED_SPRUCE_HANDRAIL,
        HandrailBlocks.COLORED_DECORATED_STRIPPED_BIRCH_HANDRAIL,
        HandrailBlocks.COLORED_DECORATED_STRIPPED_JUNGLE_HANDRAIL,
        HandrailBlocks.COLORED_DECORATED_STRIPPED_ACACIA_HANDRAIL,
        HandrailBlocks.COLORED_DECORATED_STRIPPED_CHERRY_HANDRAIL,
        HandrailBlocks.COLORED_DECORATED_STRIPPED_DARK_OAK_HANDRAIL,
        HandrailBlocks.COLORED_DECORATED_STRIPPED_PALE_OAK_HANDRAIL,
        HandrailBlocks.COLORED_DECORATED_STRIPPED_MANGROVE_HANDRAIL,
        HandrailBlocks.COLORED_DECORATED_STRIPPED_POPLAR_HANDRAIL,
        HandrailBlocks.COLORED_DECORATED_STRIPPED_BAMBOO_HANDRAIL
    );

    public static final Collection<Block> woodenHandrails = woodenHandrailSets.stream().flatMap(handrailBlock -> Arrays.stream(handrailBlock.selfAndVariants())).collect(ImmutableSet.toImmutableSet());

    public static final Map<Block, ResourceKey<ContextIntProvider>> blockFuels = ImmutableMap.of(
        ColoredBlocks.COLORED_PLANKS, ContextIntProviders.COOKING_TIME_WOOD_BLOCKS,
        ColoredBlocks.COLORED_PLANK_STAIRS, ContextIntProviders.COOKING_TIME_WOOD_BLOCKS,
        ColoredBlocks.COLORED_PLANK_SLAB, ContextIntProviders.COOKING_TIME_WOOD_SLABS,
        ColoredBlocks.COLORED_WOOL, ContextIntProviders.COOKING_TIME_WOOL,
        ColoredBlocks.COLORED_WOOL_STAIRS, ContextIntProviders.COOKING_TIME_WOOL,
        ColoredBlocks.COLORED_WOOL_SLAB, ContextIntProviders.COOKING_TIME_WOOL_SLABS
    );
  }
}
