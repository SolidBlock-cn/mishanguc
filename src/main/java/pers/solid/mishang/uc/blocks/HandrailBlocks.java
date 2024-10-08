package pers.solid.mishang.uc.blocks;

import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.fabricmc.fabric.mixin.object.builder.AbstractBlockAccessor;
import net.fabricmc.fabric.mixin.object.builder.AbstractBlockSettingsAccessor;
import net.minecraft.block.Blocks;
import net.minecraft.block.MapColor;
import net.minecraft.state.property.Properties;
import net.minecraft.util.DyeColor;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Direction;
import org.jetbrains.annotations.ApiStatus;
import pers.solid.mishang.uc.annotations.MiningLevel;
import pers.solid.mishang.uc.annotations.Translucent;
import pers.solid.mishang.uc.block.ColoredGlassHandrailBlock;
import pers.solid.mishang.uc.block.GlassHandrailBlock;
import pers.solid.mishang.uc.block.SimpleHandrailBlock;

import java.util.EnumMap;
import java.util.Map;

/**
 * 本模组中的所有栏杆方块。
 *
 * @see pers.solid.mishang.uc.block.HandrailBlock
 */
@ApiStatus.AvailableSince("0.1.7")
public final class HandrailBlocks extends MishangucBlocks {

  // 简单的混凝土栏杆

  public static final SimpleHandrailBlock SIMPLE_WHITE_CONCRETE_HANDRAIL = new SimpleHandrailBlock(Blocks.WHITE_CONCRETE);
  public static final SimpleHandrailBlock SIMPLE_ORANGE_CONCRETE_HANDRAIL = new SimpleHandrailBlock(Blocks.ORANGE_CONCRETE);
  public static final SimpleHandrailBlock SIMPLE_MAGENTA_CONCRETE_HANDRAIL = new SimpleHandrailBlock(Blocks.MAGENTA_CONCRETE);
  public static final SimpleHandrailBlock SIMPLE_LIGHT_BLUE_CONCRETE_HANDRAIL = new SimpleHandrailBlock(Blocks.LIGHT_BLUE_CONCRETE);
  public static final SimpleHandrailBlock SIMPLE_YELLOW_CONCRETE_HANDRAIL = new SimpleHandrailBlock(Blocks.YELLOW_CONCRETE);
  public static final SimpleHandrailBlock SIMPLE_LIME_CONCRETE_HANDRAIL = new SimpleHandrailBlock(Blocks.LIME_CONCRETE);
  public static final SimpleHandrailBlock SIMPLE_PINK_CONCRETE_HANDRAIL = new SimpleHandrailBlock(Blocks.PINK_CONCRETE);
  public static final SimpleHandrailBlock SIMPLE_GRAY_CONCRETE_HANDRAIL = new SimpleHandrailBlock(Blocks.GRAY_CONCRETE);
  public static final SimpleHandrailBlock SIMPLE_LIGHT_GRAY_CONCRETE_HANDRAIL = new SimpleHandrailBlock(Blocks.LIGHT_GRAY_CONCRETE);
  public static final SimpleHandrailBlock SIMPLE_CYAN_CONCRETE_HANDRAIL = new SimpleHandrailBlock(Blocks.CYAN_CONCRETE);
  public static final SimpleHandrailBlock SIMPLE_PURPLE_CONCRETE_HANDRAIL = new SimpleHandrailBlock(Blocks.PURPLE_CONCRETE);
  public static final SimpleHandrailBlock SIMPLE_BLUE_CONCRETE_HANDRAIL = new SimpleHandrailBlock(Blocks.BLUE_CONCRETE);
  public static final SimpleHandrailBlock SIMPLE_BROWN_CONCRETE_HANDRAIL = new SimpleHandrailBlock(Blocks.BROWN_CONCRETE);
  public static final SimpleHandrailBlock SIMPLE_GREEN_CONCRETE_HANDRAIL = new SimpleHandrailBlock(Blocks.GREEN_CONCRETE);
  public static final SimpleHandrailBlock SIMPLE_RED_CONCRETE_HANDRAIL = new SimpleHandrailBlock(Blocks.RED_CONCRETE);
  public static final SimpleHandrailBlock SIMPLE_BLACK_CONCRETE_HANDRAIL = new SimpleHandrailBlock(Blocks.BLACK_CONCRETE);

  /**
   * 颜色及其对应的简单混凝土栏杆组成的映射。
   */
  public static final Map<DyeColor, SimpleHandrailBlock> SIMPLE_CONCRETE_HANDRAILS = new EnumMap<>(DyeColor.class);

  static {
    SIMPLE_CONCRETE_HANDRAILS.put(DyeColor.WHITE, SIMPLE_WHITE_CONCRETE_HANDRAIL);
    SIMPLE_CONCRETE_HANDRAILS.put(DyeColor.ORANGE, SIMPLE_ORANGE_CONCRETE_HANDRAIL);
    SIMPLE_CONCRETE_HANDRAILS.put(DyeColor.MAGENTA, SIMPLE_MAGENTA_CONCRETE_HANDRAIL);
    SIMPLE_CONCRETE_HANDRAILS.put(DyeColor.LIGHT_BLUE, SIMPLE_LIGHT_BLUE_CONCRETE_HANDRAIL);
    SIMPLE_CONCRETE_HANDRAILS.put(DyeColor.YELLOW, SIMPLE_YELLOW_CONCRETE_HANDRAIL);
    SIMPLE_CONCRETE_HANDRAILS.put(DyeColor.LIME, SIMPLE_LIME_CONCRETE_HANDRAIL);
    SIMPLE_CONCRETE_HANDRAILS.put(DyeColor.PINK, SIMPLE_PINK_CONCRETE_HANDRAIL);
    SIMPLE_CONCRETE_HANDRAILS.put(DyeColor.GRAY, SIMPLE_GRAY_CONCRETE_HANDRAIL);
    SIMPLE_CONCRETE_HANDRAILS.put(DyeColor.LIGHT_GRAY, SIMPLE_LIGHT_GRAY_CONCRETE_HANDRAIL);
    SIMPLE_CONCRETE_HANDRAILS.put(DyeColor.CYAN, SIMPLE_CYAN_CONCRETE_HANDRAIL);
    SIMPLE_CONCRETE_HANDRAILS.put(DyeColor.PURPLE, SIMPLE_PURPLE_CONCRETE_HANDRAIL);
    SIMPLE_CONCRETE_HANDRAILS.put(DyeColor.BLUE, SIMPLE_BLUE_CONCRETE_HANDRAIL);
    SIMPLE_CONCRETE_HANDRAILS.put(DyeColor.BROWN, SIMPLE_BROWN_CONCRETE_HANDRAIL);
    SIMPLE_CONCRETE_HANDRAILS.put(DyeColor.GREEN, SIMPLE_GREEN_CONCRETE_HANDRAIL);
    SIMPLE_CONCRETE_HANDRAILS.put(DyeColor.RED, SIMPLE_RED_CONCRETE_HANDRAIL);
    SIMPLE_CONCRETE_HANDRAILS.put(DyeColor.BLACK, SIMPLE_BLACK_CONCRETE_HANDRAIL);
  }


  // 简单的陶瓦栏杆

  public static final SimpleHandrailBlock SIMPLE_WHITE_TERRACOTTA_HANDRAIL = new SimpleHandrailBlock(Blocks.WHITE_TERRACOTTA);
  public static final SimpleHandrailBlock SIMPLE_ORANGE_TERRACOTTA_HANDRAIL = new SimpleHandrailBlock(Blocks.ORANGE_TERRACOTTA);
  public static final SimpleHandrailBlock SIMPLE_MAGENTA_TERRACOTTA_HANDRAIL = new SimpleHandrailBlock(Blocks.MAGENTA_TERRACOTTA);
  public static final SimpleHandrailBlock SIMPLE_LIGHT_BLUE_TERRACOTTA_HANDRAIL = new SimpleHandrailBlock(Blocks.LIGHT_BLUE_TERRACOTTA);
  public static final SimpleHandrailBlock SIMPLE_YELLOW_TERRACOTTA_HANDRAIL = new SimpleHandrailBlock(Blocks.YELLOW_TERRACOTTA);
  public static final SimpleHandrailBlock SIMPLE_LIME_TERRACOTTA_HANDRAIL = new SimpleHandrailBlock(Blocks.LIME_TERRACOTTA);
  public static final SimpleHandrailBlock SIMPLE_PINK_TERRACOTTA_HANDRAIL = new SimpleHandrailBlock(Blocks.PINK_TERRACOTTA);
  public static final SimpleHandrailBlock SIMPLE_GRAY_TERRACOTTA_HANDRAIL = new SimpleHandrailBlock(Blocks.GRAY_TERRACOTTA);
  public static final SimpleHandrailBlock SIMPLE_LIGHT_GRAY_TERRACOTTA_HANDRAIL = new SimpleHandrailBlock(Blocks.LIGHT_GRAY_TERRACOTTA);
  public static final SimpleHandrailBlock SIMPLE_CYAN_TERRACOTTA_HANDRAIL = new SimpleHandrailBlock(Blocks.CYAN_TERRACOTTA);
  public static final SimpleHandrailBlock SIMPLE_PURPLE_TERRACOTTA_HANDRAIL = new SimpleHandrailBlock(Blocks.PURPLE_TERRACOTTA);
  public static final SimpleHandrailBlock SIMPLE_BLUE_TERRACOTTA_HANDRAIL = new SimpleHandrailBlock(Blocks.BLUE_TERRACOTTA);
  public static final SimpleHandrailBlock SIMPLE_BROWN_TERRACOTTA_HANDRAIL = new SimpleHandrailBlock(Blocks.BROWN_TERRACOTTA);
  public static final SimpleHandrailBlock SIMPLE_GREEN_TERRACOTTA_HANDRAIL = new SimpleHandrailBlock(Blocks.GREEN_TERRACOTTA);
  public static final SimpleHandrailBlock SIMPLE_RED_TERRACOTTA_HANDRAIL = new SimpleHandrailBlock(Blocks.RED_TERRACOTTA);
  public static final SimpleHandrailBlock SIMPLE_BLACK_TERRACOTTA_HANDRAIL = new SimpleHandrailBlock(Blocks.BLACK_TERRACOTTA);

  /**
   * 颜色及其对应的简单陶瓦栏杆组成的映射。
   */
  public static final Map<DyeColor, SimpleHandrailBlock> SIMPLE_TERRACOTTA_HANDRAILS = new EnumMap<>(DyeColor.class);

  static {
    SIMPLE_TERRACOTTA_HANDRAILS.put(DyeColor.WHITE, SIMPLE_WHITE_TERRACOTTA_HANDRAIL);
    SIMPLE_TERRACOTTA_HANDRAILS.put(DyeColor.ORANGE, SIMPLE_ORANGE_TERRACOTTA_HANDRAIL);
    SIMPLE_TERRACOTTA_HANDRAILS.put(DyeColor.MAGENTA, SIMPLE_MAGENTA_TERRACOTTA_HANDRAIL);
    SIMPLE_TERRACOTTA_HANDRAILS.put(DyeColor.LIGHT_BLUE, SIMPLE_LIGHT_BLUE_TERRACOTTA_HANDRAIL);
    SIMPLE_TERRACOTTA_HANDRAILS.put(DyeColor.YELLOW, SIMPLE_YELLOW_TERRACOTTA_HANDRAIL);
    SIMPLE_TERRACOTTA_HANDRAILS.put(DyeColor.LIME, SIMPLE_LIME_TERRACOTTA_HANDRAIL);
    SIMPLE_TERRACOTTA_HANDRAILS.put(DyeColor.PINK, SIMPLE_PINK_TERRACOTTA_HANDRAIL);
    SIMPLE_TERRACOTTA_HANDRAILS.put(DyeColor.GRAY, SIMPLE_GRAY_TERRACOTTA_HANDRAIL);
    SIMPLE_TERRACOTTA_HANDRAILS.put(DyeColor.LIGHT_GRAY, SIMPLE_LIGHT_GRAY_TERRACOTTA_HANDRAIL);
    SIMPLE_TERRACOTTA_HANDRAILS.put(DyeColor.CYAN, SIMPLE_CYAN_TERRACOTTA_HANDRAIL);
    SIMPLE_TERRACOTTA_HANDRAILS.put(DyeColor.PURPLE, SIMPLE_PURPLE_TERRACOTTA_HANDRAIL);
    SIMPLE_TERRACOTTA_HANDRAILS.put(DyeColor.BLUE, SIMPLE_BLUE_TERRACOTTA_HANDRAIL);
    SIMPLE_TERRACOTTA_HANDRAILS.put(DyeColor.BROWN, SIMPLE_BROWN_TERRACOTTA_HANDRAIL);
    SIMPLE_TERRACOTTA_HANDRAILS.put(DyeColor.GREEN, SIMPLE_GREEN_TERRACOTTA_HANDRAIL);
    SIMPLE_TERRACOTTA_HANDRAILS.put(DyeColor.RED, SIMPLE_RED_TERRACOTTA_HANDRAIL);
    SIMPLE_TERRACOTTA_HANDRAILS.put(DyeColor.BLACK, SIMPLE_BLACK_TERRACOTTA_HANDRAIL);
  }

  // 冰雪。
  @Translucent
  public static final SimpleHandrailBlock SIMPLE_ICE_HANDRAIL = new SimpleHandrailBlock(Blocks.ICE);
  public static final SimpleHandrailBlock SIMPLE_PACKED_ICE_HANDRAIL = new SimpleHandrailBlock(Blocks.PACKED_ICE);
  public static final SimpleHandrailBlock SIMPLE_BLUE_ICE_HANDRAIL = new SimpleHandrailBlock(Blocks.BLUE_ICE);
  @MiningLevel(MiningLevel.Tool.SHOVEL)
  public static final SimpleHandrailBlock SIMPLE_SNOW_HANDRAIL = new SimpleHandrailBlock(Blocks.SNOW_BLOCK);

  static {
    SIMPLE_SNOW_HANDRAIL.texture = new Identifier("block/snow");
  }

  // 木头
  public static final SimpleHandrailBlock SIMPLE_OAK_HANDRAIL = new SimpleHandrailBlock(Blocks.OAK_WOOD);
  public static final SimpleHandrailBlock SIMPLE_SPRUCE_HANDRAIL = new SimpleHandrailBlock(Blocks.SPRUCE_WOOD);
  public static final SimpleHandrailBlock SIMPLE_BIRCH_HANDRAIL = new SimpleHandrailBlock(Blocks.BIRCH_WOOD);
  public static final SimpleHandrailBlock SIMPLE_JUNGLE_HANDRAIL = new SimpleHandrailBlock(Blocks.JUNGLE_WOOD);
  public static final SimpleHandrailBlock SIMPLE_ACACIA_HANDRAIL = new SimpleHandrailBlock(Blocks.ACACIA_WOOD);
  @ApiStatus.AvailableSince("1.1.1-mc1.19.4")
  public static final SimpleHandrailBlock SIMPLE_CHERRY_HANDRAIL = new SimpleHandrailBlock(Blocks.CHERRY_WOOD);
  public static final SimpleHandrailBlock SIMPLE_DARK_OAK_HANDRAIL = new SimpleHandrailBlock(Blocks.DARK_OAK_WOOD);
  @ApiStatus.AvailableSince("0.2.0-mc1.19")
  public static final SimpleHandrailBlock SIMPLE_MANGROVE_HANDRAIL = new SimpleHandrailBlock(Blocks.MANGROVE_WOOD);
  public static final SimpleHandrailBlock SIMPLE_CRIMSON_HANDRAIL = new SimpleHandrailBlock(Blocks.CRIMSON_HYPHAE);
  public static final SimpleHandrailBlock SIMPLE_WARPED_HANDRAIL = new SimpleHandrailBlock(Blocks.WARPED_HYPHAE);
  public static final SimpleHandrailBlock SIMPLE_OAK_PLANK_HANDRAIL = new SimpleHandrailBlock(Blocks.OAK_PLANKS);
  public static final SimpleHandrailBlock SIMPLE_SPRUCE_PLANK_HANDRAIL = new SimpleHandrailBlock(Blocks.SPRUCE_PLANKS);
  public static final SimpleHandrailBlock SIMPLE_BIRCH_PLANK_HANDRAIL = new SimpleHandrailBlock(Blocks.BIRCH_PLANKS);
  public static final SimpleHandrailBlock SIMPLE_JUNGLE_PLANK_HANDRAIL = new SimpleHandrailBlock(Blocks.JUNGLE_PLANKS);
  public static final SimpleHandrailBlock SIMPLE_ACACIA_PLANK_HANDRAIL = new SimpleHandrailBlock(Blocks.ACACIA_PLANKS);
  @ApiStatus.AvailableSince("1.1.1-mc1.19.4")
  public static final SimpleHandrailBlock SIMPLE_CHERRY_PLANK_HANDRAIL = new SimpleHandrailBlock(Blocks.CHERRY_PLANKS);
  public static final SimpleHandrailBlock SIMPLE_DARK_OAK_PLANK_HANDRAIL = new SimpleHandrailBlock(Blocks.DARK_OAK_PLANKS);
  @ApiStatus.AvailableSince("0.2.0-mc1.19")
  public static final SimpleHandrailBlock SIMPLE_MANGROVE_PLANK_HANDRAIL = new SimpleHandrailBlock(Blocks.MANGROVE_PLANKS);
  public static final SimpleHandrailBlock SIMPLE_CRIMSON_PLANK_HANDRAIL = new SimpleHandrailBlock(Blocks.CRIMSON_PLANKS);
  public static final SimpleHandrailBlock SIMPLE_WARPED_PLANK_HANDRAIL = new SimpleHandrailBlock(Blocks.WARPED_PLANKS);

  @ApiStatus.AvailableSince("1.0.4-mc1.19.3")
  @MiningLevel(MiningLevel.Tool.AXE)
  public static final SimpleHandrailBlock SIMPLE_BAMBOO_HANDRAIL = new SimpleHandrailBlock(Blocks.BAMBOO_BLOCK, FabricBlockSettings.copyOf(Blocks.BAMBOO_BLOCK).mapColor(MapColor.DARK_GREEN));

  @ApiStatus.AvailableSince("1.0.4-mc1.19.3")
  @MiningLevel(MiningLevel.Tool.AXE)
  public static final SimpleHandrailBlock SIMPLE_BAMBOO_PLANK_HANDRAIL = new SimpleHandrailBlock(Blocks.BAMBOO_PLANKS, FabricBlockSettings.copyOf(Blocks.BAMBOO_PLANKS));
  @ApiStatus.AvailableSince("1.0.4-mc1.19.3")
  @MiningLevel(MiningLevel.Tool.AXE)
  public static final SimpleHandrailBlock SIMPLE_BAMBOO_MOSAIC_HANDRAIL = new SimpleHandrailBlock(Blocks.BAMBOO_MOSAIC, FabricBlockSettings.copyOf(Blocks.BAMBOO_MOSAIC));

  static {
    SIMPLE_OAK_HANDRAIL.texture = new Identifier("block/oak_log");
    SIMPLE_SPRUCE_HANDRAIL.texture = new Identifier("block/spruce_log");
    SIMPLE_BIRCH_HANDRAIL.texture = new Identifier("block/birch_log");
    SIMPLE_JUNGLE_HANDRAIL.texture = new Identifier("block/jungle_log");
    SIMPLE_ACACIA_HANDRAIL.texture = new Identifier("block/acacia_log");
    SIMPLE_CHERRY_HANDRAIL.texture = new Identifier("block/cherry_log");
    SIMPLE_DARK_OAK_HANDRAIL.texture = new Identifier("block/dark_oak_log");
    SIMPLE_MANGROVE_HANDRAIL.texture = new Identifier("block/mangrove_log");
    SIMPLE_CRIMSON_HANDRAIL.texture = new Identifier("block/crimson_stem");
    SIMPLE_WARPED_HANDRAIL.texture = new Identifier("block/warped_stem");
    SIMPLE_BAMBOO_HANDRAIL.texture = new Identifier("block/bamboo_block");
  }

  // 染色玻璃。

  @MiningLevel(MiningLevel.Tool.SHOVEL)
  public static final SimpleHandrailBlock SIMPLE_DIRT_HANDRAIL = new SimpleHandrailBlock(Blocks.DIRT);
  public static final SimpleHandrailBlock SIMPLE_STONE_HANDRAIL = new SimpleHandrailBlock(Blocks.STONE);
  public static final SimpleHandrailBlock SIMPLE_COBBLESTONE_HANDRAIL = new SimpleHandrailBlock(Blocks.COBBLESTONE);
  public static final SimpleHandrailBlock SIMPLE_MOSSY_COBBLESTONE_HANDRAIL = new SimpleHandrailBlock(Blocks.MOSSY_COBBLESTONE);

  @Translucent
  public static final SimpleHandrailBlock SIMPLE_WHITE_STAINED_GLASS_HANDRAIL = new SimpleHandrailBlock(Blocks.WHITE_STAINED_GLASS);
  @Translucent
  public static final SimpleHandrailBlock SIMPLE_ORANGE_STAINED_GLASS_HANDRAIL = new SimpleHandrailBlock(Blocks.ORANGE_STAINED_GLASS);
  @Translucent
  public static final SimpleHandrailBlock SIMPLE_MAGENTA_STAINED_GLASS_HANDRAIL = new SimpleHandrailBlock(Blocks.MAGENTA_STAINED_GLASS);
  @Translucent
  public static final SimpleHandrailBlock SIMPLE_LIGHT_BLUE_STAINED_GLASS_HANDRAIL = new SimpleHandrailBlock(Blocks.LIGHT_BLUE_STAINED_GLASS);
  @Translucent
  public static final SimpleHandrailBlock SIMPLE_YELLOW_STAINED_GLASS_HANDRAIL = new SimpleHandrailBlock(Blocks.YELLOW_STAINED_GLASS);
  @Translucent
  public static final SimpleHandrailBlock SIMPLE_LIME_STAINED_GLASS_HANDRAIL = new SimpleHandrailBlock(Blocks.LIME_STAINED_GLASS);
  @Translucent
  public static final SimpleHandrailBlock SIMPLE_PINK_STAINED_GLASS_HANDRAIL = new SimpleHandrailBlock(Blocks.PINK_STAINED_GLASS);
  @Translucent
  public static final SimpleHandrailBlock SIMPLE_GRAY_STAINED_GLASS_HANDRAIL = new SimpleHandrailBlock(Blocks.GRAY_STAINED_GLASS);
  @Translucent
  public static final SimpleHandrailBlock SIMPLE_LIGHT_GRAY_STAINED_GLASS_HANDRAIL = new SimpleHandrailBlock(Blocks.LIGHT_GRAY_STAINED_GLASS);
  @Translucent
  public static final SimpleHandrailBlock SIMPLE_CYAN_STAINED_GLASS_HANDRAIL = new SimpleHandrailBlock(Blocks.CYAN_STAINED_GLASS);
  @Translucent
  public static final SimpleHandrailBlock SIMPLE_PURPLE_STAINED_GLASS_HANDRAIL = new SimpleHandrailBlock(Blocks.PURPLE_STAINED_GLASS);
  @Translucent
  public static final SimpleHandrailBlock SIMPLE_BLUE_STAINED_GLASS_HANDRAIL = new SimpleHandrailBlock(Blocks.BLUE_STAINED_GLASS);
  @Translucent
  public static final SimpleHandrailBlock SIMPLE_BROWN_STAINED_GLASS_HANDRAIL = new SimpleHandrailBlock(Blocks.BROWN_STAINED_GLASS);
  @Translucent
  public static final SimpleHandrailBlock SIMPLE_GREEN_STAINED_GLASS_HANDRAIL = new SimpleHandrailBlock(Blocks.GREEN_STAINED_GLASS);
  @Translucent
  public static final SimpleHandrailBlock SIMPLE_RED_STAINED_GLASS_HANDRAIL = new SimpleHandrailBlock(Blocks.RED_STAINED_GLASS);
  @Translucent
  public static final SimpleHandrailBlock SIMPLE_BLACK_STAINED_GLASS_HANDRAIL = new SimpleHandrailBlock(Blocks.BLACK_STAINED_GLASS);

  /**
   * 颜色及其对应的染色玻璃栏杆组成的映射。
   */
  public static final Map<DyeColor, SimpleHandrailBlock> SIMPLE_STAINED_GLASS_HANDRAILS = new EnumMap<>(DyeColor.class);

  static {
    SIMPLE_STAINED_GLASS_HANDRAILS.put(DyeColor.WHITE, SIMPLE_WHITE_STAINED_GLASS_HANDRAIL);
    SIMPLE_STAINED_GLASS_HANDRAILS.put(DyeColor.ORANGE, SIMPLE_ORANGE_STAINED_GLASS_HANDRAIL);
    SIMPLE_STAINED_GLASS_HANDRAILS.put(DyeColor.MAGENTA, SIMPLE_MAGENTA_STAINED_GLASS_HANDRAIL);
    SIMPLE_STAINED_GLASS_HANDRAILS.put(DyeColor.LIGHT_BLUE, SIMPLE_LIGHT_BLUE_STAINED_GLASS_HANDRAIL);
    SIMPLE_STAINED_GLASS_HANDRAILS.put(DyeColor.YELLOW, SIMPLE_YELLOW_STAINED_GLASS_HANDRAIL);
    SIMPLE_STAINED_GLASS_HANDRAILS.put(DyeColor.LIME, SIMPLE_LIME_STAINED_GLASS_HANDRAIL);
    SIMPLE_STAINED_GLASS_HANDRAILS.put(DyeColor.PINK, SIMPLE_PINK_STAINED_GLASS_HANDRAIL);
    SIMPLE_STAINED_GLASS_HANDRAILS.put(DyeColor.GRAY, SIMPLE_GRAY_STAINED_GLASS_HANDRAIL);
    SIMPLE_STAINED_GLASS_HANDRAILS.put(DyeColor.LIGHT_GRAY, SIMPLE_LIGHT_GRAY_STAINED_GLASS_HANDRAIL);
    SIMPLE_STAINED_GLASS_HANDRAILS.put(DyeColor.CYAN, SIMPLE_CYAN_STAINED_GLASS_HANDRAIL);
    SIMPLE_STAINED_GLASS_HANDRAILS.put(DyeColor.PURPLE, SIMPLE_PURPLE_STAINED_GLASS_HANDRAIL);
    SIMPLE_STAINED_GLASS_HANDRAILS.put(DyeColor.BLUE, SIMPLE_BLUE_STAINED_GLASS_HANDRAIL);
    SIMPLE_STAINED_GLASS_HANDRAILS.put(DyeColor.BROWN, SIMPLE_BROWN_STAINED_GLASS_HANDRAIL);
    SIMPLE_STAINED_GLASS_HANDRAILS.put(DyeColor.GREEN, SIMPLE_GREEN_STAINED_GLASS_HANDRAIL);
    SIMPLE_STAINED_GLASS_HANDRAILS.put(DyeColor.RED, SIMPLE_RED_STAINED_GLASS_HANDRAIL);
    SIMPLE_STAINED_GLASS_HANDRAILS.put(DyeColor.BLACK, SIMPLE_BLACK_STAINED_GLASS_HANDRAIL);
  }

  @ApiStatus.AvailableSince("1.2.4")
  @Translucent
  public static final ColoredGlassHandrailBlock COLORED_DECORATED_STONE_HANDRAIL = new ColoredGlassHandrailBlock(Blocks.STONE, FabricBlockSettings.copyOf(Blocks.STONE).strength(2.5f, 6f), "block/stone", "block/white_concrete");
  @ApiStatus.AvailableSince("1.2.4")
  @Translucent
  public static final ColoredGlassHandrailBlock COLORED_DECORATED_COBBLESTONE_HANDRAIL = new ColoredGlassHandrailBlock(Blocks.COBBLESTONE, FabricBlockSettings.copyOf(Blocks.COBBLESTONE).strength(2.5f, 6f), "block/cobblestone", "block/white_concrete");
  @ApiStatus.AvailableSince("1.2.4")
  @Translucent
  public static final ColoredGlassHandrailBlock COLORED_DECORATED_MOSSY_COBBLESTONE_HANDRAIL = new ColoredGlassHandrailBlock(Blocks.MOSSY_COBBLESTONE, FabricBlockSettings.copyOf(Blocks.COBBLESTONE).strength(2.5f, 6f), "block/mossy_cobblestone", "block/white_concrete");

  @ApiStatus.AvailableSince("0.2.4")
  @Translucent
  @MiningLevel(level = MiningLevel.Level.STONE)
  public static final GlassHandrailBlock WHITE_DECORATED_IRON_HANDRAIL = new GlassHandrailBlock(Blocks.IRON_BLOCK, FabricBlockSettings.copyOf(Blocks.IRON_BLOCK).strength(2.5f, 6f).mapColor(DyeColor.WHITE), "block/iron_block", "block/white_concrete");
  @ApiStatus.AvailableSince("0.2.4")
  @Translucent
  @MiningLevel(level = MiningLevel.Level.STONE)
  public static final GlassHandrailBlock ORANGE_DECORATED_IRON_HANDRAIL = new GlassHandrailBlock(Blocks.IRON_BLOCK, FabricBlockSettings.copyOf(WHITE_DECORATED_IRON_HANDRAIL).mapColor(DyeColor.ORANGE), "block/iron_block", "block/orange_concrete");
  @ApiStatus.AvailableSince("0.2.4")
  @Translucent
  @MiningLevel(level = MiningLevel.Level.STONE)
  public static final GlassHandrailBlock MAGENTA_DECORATED_IRON_HANDRAIL = new GlassHandrailBlock(Blocks.IRON_BLOCK, FabricBlockSettings.copyOf(WHITE_DECORATED_IRON_HANDRAIL).mapColor(DyeColor.MAGENTA), "block/iron_block", "block/magenta_concrete");
  @ApiStatus.AvailableSince("0.2.4")
  @Translucent
  @MiningLevel(level = MiningLevel.Level.STONE)
  public static final GlassHandrailBlock LIGHT_BLUE_DECORATED_IRON_HANDRAIL = new GlassHandrailBlock(Blocks.IRON_BLOCK, FabricBlockSettings.copyOf(WHITE_DECORATED_IRON_HANDRAIL).mapColor(DyeColor.LIGHT_BLUE), "block/iron_block", "block/light_blue_concrete");
  @ApiStatus.AvailableSince("0.2.4")
  @Translucent
  @MiningLevel(level = MiningLevel.Level.STONE)
  public static final GlassHandrailBlock YELLOW_DECORATED_IRON_HANDRAIL = new GlassHandrailBlock(Blocks.IRON_BLOCK, FabricBlockSettings.copyOf(WHITE_DECORATED_IRON_HANDRAIL).mapColor(DyeColor.YELLOW), "block/iron_block", "block/yellow_concrete");
  @ApiStatus.AvailableSince("0.2.4")
  @Translucent
  @MiningLevel(level = MiningLevel.Level.STONE)
  public static final GlassHandrailBlock LIME_DECORATED_IRON_HANDRAIL = new GlassHandrailBlock(Blocks.IRON_BLOCK, FabricBlockSettings.copyOf(WHITE_DECORATED_IRON_HANDRAIL).mapColor(DyeColor.LIME), "block/iron_block", "block/lime_concrete");
  @ApiStatus.AvailableSince("0.2.4")
  @Translucent
  @MiningLevel(level = MiningLevel.Level.STONE)
  public static final GlassHandrailBlock PINK_DECORATED_IRON_HANDRAIL = new GlassHandrailBlock(Blocks.IRON_BLOCK, FabricBlockSettings.copyOf(WHITE_DECORATED_IRON_HANDRAIL).mapColor(DyeColor.PINK), "block/iron_block", "block/pink_concrete");
  @ApiStatus.AvailableSince("0.2.4")
  @Translucent
  @MiningLevel(level = MiningLevel.Level.STONE)
  public static final GlassHandrailBlock GRAY_DECORATED_IRON_HANDRAIL = new GlassHandrailBlock(Blocks.IRON_BLOCK, FabricBlockSettings.copyOf(WHITE_DECORATED_IRON_HANDRAIL).mapColor(DyeColor.GRAY), "block/iron_block", "block/gray_concrete");
  @ApiStatus.AvailableSince("0.2.4")
  @Translucent
  @MiningLevel(level = MiningLevel.Level.STONE)
  public static final GlassHandrailBlock LIGHT_GRAY_DECORATED_IRON_HANDRAIL = new GlassHandrailBlock(Blocks.IRON_BLOCK, FabricBlockSettings.copyOf(WHITE_DECORATED_IRON_HANDRAIL).mapColor(DyeColor.LIGHT_GRAY), "block/iron_block", "block/light_gray_concrete");
  @ApiStatus.AvailableSince("0.2.4")
  @Translucent
  @MiningLevel(level = MiningLevel.Level.STONE)
  public static final GlassHandrailBlock CYAN_DECORATED_IRON_HANDRAIL = new GlassHandrailBlock(Blocks.IRON_BLOCK, FabricBlockSettings.copyOf(WHITE_DECORATED_IRON_HANDRAIL).mapColor(DyeColor.CYAN), "block/iron_block", "block/cyan_concrete");
  @ApiStatus.AvailableSince("0.2.4")
  @Translucent
  @MiningLevel(level = MiningLevel.Level.STONE)
  public static final GlassHandrailBlock PURPLE_DECORATED_IRON_HANDRAIL = new GlassHandrailBlock(Blocks.IRON_BLOCK, FabricBlockSettings.copyOf(WHITE_DECORATED_IRON_HANDRAIL).mapColor(DyeColor.PURPLE), "block/iron_block", "block/purple_concrete");
  @ApiStatus.AvailableSince("0.2.4")
  @Translucent
  @MiningLevel(level = MiningLevel.Level.STONE)
  public static final GlassHandrailBlock BLUE_DECORATED_IRON_HANDRAIL = new GlassHandrailBlock(Blocks.IRON_BLOCK, FabricBlockSettings.copyOf(WHITE_DECORATED_IRON_HANDRAIL).mapColor(DyeColor.BLUE), "block/iron_block", "block/blue_concrete");
  @ApiStatus.AvailableSince("0.2.4")
  @Translucent
  @MiningLevel(level = MiningLevel.Level.STONE)
  public static final GlassHandrailBlock BROWN_DECORATED_IRON_HANDRAIL = new GlassHandrailBlock(Blocks.IRON_BLOCK, FabricBlockSettings.copyOf(WHITE_DECORATED_IRON_HANDRAIL).mapColor(DyeColor.BROWN), "block/iron_block", "block/brown_concrete");
  @ApiStatus.AvailableSince("0.2.4")
  @Translucent
  @MiningLevel(level = MiningLevel.Level.STONE)
  public static final GlassHandrailBlock GREEN_DECORATED_IRON_HANDRAIL = new GlassHandrailBlock(Blocks.IRON_BLOCK, FabricBlockSettings.copyOf(WHITE_DECORATED_IRON_HANDRAIL).mapColor(DyeColor.GREEN), "block/iron_block", "block/green_concrete");
  @ApiStatus.AvailableSince("0.2.4")
  @Translucent
  @MiningLevel(level = MiningLevel.Level.STONE)
  public static final GlassHandrailBlock RED_DECORATED_IRON_HANDRAIL = new GlassHandrailBlock(Blocks.IRON_BLOCK, FabricBlockSettings.copyOf(WHITE_DECORATED_IRON_HANDRAIL).mapColor(DyeColor.RED), "block/iron_block", "block/red_concrete");
  @ApiStatus.AvailableSince("0.2.4")
  @Translucent
  @MiningLevel(level = MiningLevel.Level.STONE)
  public static final GlassHandrailBlock BLACK_DECORATED_IRON_HANDRAIL = new GlassHandrailBlock(Blocks.IRON_BLOCK, FabricBlockSettings.copyOf(WHITE_DECORATED_IRON_HANDRAIL).mapColor(DyeColor.BLACK), "block/iron_block", "block/black_concrete");

  public static final EnumMap<DyeColor, GlassHandrailBlock> DECORATED_IRON_HANDRAILS = new EnumMap<>(DyeColor.class);

  static {
    DECORATED_IRON_HANDRAILS.put(DyeColor.WHITE, WHITE_DECORATED_IRON_HANDRAIL);
    DECORATED_IRON_HANDRAILS.put(DyeColor.ORANGE, ORANGE_DECORATED_IRON_HANDRAIL);
    DECORATED_IRON_HANDRAILS.put(DyeColor.MAGENTA, MAGENTA_DECORATED_IRON_HANDRAIL);
    DECORATED_IRON_HANDRAILS.put(DyeColor.LIGHT_BLUE, LIGHT_BLUE_DECORATED_IRON_HANDRAIL);
    DECORATED_IRON_HANDRAILS.put(DyeColor.YELLOW, YELLOW_DECORATED_IRON_HANDRAIL);
    DECORATED_IRON_HANDRAILS.put(DyeColor.LIME, LIME_DECORATED_IRON_HANDRAIL);
    DECORATED_IRON_HANDRAILS.put(DyeColor.PINK, PINK_DECORATED_IRON_HANDRAIL);
    DECORATED_IRON_HANDRAILS.put(DyeColor.GRAY, GRAY_DECORATED_IRON_HANDRAIL);
    DECORATED_IRON_HANDRAILS.put(DyeColor.LIGHT_GRAY, LIGHT_GRAY_DECORATED_IRON_HANDRAIL);
    DECORATED_IRON_HANDRAILS.put(DyeColor.CYAN, CYAN_DECORATED_IRON_HANDRAIL);
    DECORATED_IRON_HANDRAILS.put(DyeColor.PURPLE, PURPLE_DECORATED_IRON_HANDRAIL);
    DECORATED_IRON_HANDRAILS.put(DyeColor.BLUE, BLUE_DECORATED_IRON_HANDRAIL);
    DECORATED_IRON_HANDRAILS.put(DyeColor.BROWN, BROWN_DECORATED_IRON_HANDRAIL);
    DECORATED_IRON_HANDRAILS.put(DyeColor.GREEN, GREEN_DECORATED_IRON_HANDRAIL);
    DECORATED_IRON_HANDRAILS.put(DyeColor.RED, RED_DECORATED_IRON_HANDRAIL);
    DECORATED_IRON_HANDRAILS.put(DyeColor.BLACK, BLACK_DECORATED_IRON_HANDRAIL);
  }

  /**
   * 可自定义染色的栏杆方块。
   */
  @ApiStatus.AvailableSince("0.2.4")
  @Translucent
  @MiningLevel(level = MiningLevel.Level.STONE)
  public static final ColoredGlassHandrailBlock COLORED_DECORATED_IRON_HANDRAIL = new ColoredGlassHandrailBlock(Blocks.IRON_BLOCK, FabricBlockSettings.copyOf(WHITE_DECORATED_IRON_HANDRAIL), "block/iron_block", "block/white_concrete");

  /**
   * 可自定义染色的金栏杆方块。
   */
  @ApiStatus.AvailableSince("0.2.4")
  @Translucent
  @MiningLevel(level = MiningLevel.Level.IRON)
  public static final ColoredGlassHandrailBlock COLORED_DECORATED_GOLD_HANDRAIL = new ColoredGlassHandrailBlock(Blocks.GOLD_BLOCK, FabricBlockSettings.copyOf(Blocks.GOLD_BLOCK).strength(1.5f, 6f), "block/gold_block", "block/white_concrete");

  @ApiStatus.AvailableSince("0.2.4")
  @Translucent
  @MiningLevel(level = MiningLevel.Level.IRON)
  public static final ColoredGlassHandrailBlock COLORED_DECORATED_EMERALD_HANDRAIL = new ColoredGlassHandrailBlock(Blocks.EMERALD_BLOCK, FabricBlockSettings.copyOf(Blocks.EMERALD_BLOCK).strength(2.5f, 6f), "block/emerald_block", "block/white_concrete");

  @ApiStatus.AvailableSince("0.2.4")
  @Translucent
  @MiningLevel(level = MiningLevel.Level.IRON)
  public static final ColoredGlassHandrailBlock COLORED_DECORATED_DIAMOND_HANDRAIL = new ColoredGlassHandrailBlock(Blocks.DIAMOND_BLOCK, FabricBlockSettings.copyOf(Blocks.DIAMOND_BLOCK).strength(2.5f, 6f), "block/diamond_block", "block/white_concrete");

  @ApiStatus.AvailableSince("0.2.4")
  @Translucent
  @MiningLevel(level = MiningLevel.Level.DIAMOND)
  public static final ColoredGlassHandrailBlock COLORED_DECORATED_NETHERITE_HANDRAIL = new ColoredGlassHandrailBlock(Blocks.NETHERITE_BLOCK, FabricBlockSettings.copyOf(Blocks.NETHERITE_BLOCK).strength(25f, 1200f), "block/netherite_block", "block/white_concrete");
  @ApiStatus.AvailableSince("1.2.4")
  @Translucent
  @MiningLevel(level = MiningLevel.Level.IRON)
  public static final ColoredGlassHandrailBlock COLORED_DECORATED_LAPIS_HANDRAIL = new ColoredGlassHandrailBlock(Blocks.LAPIS_BLOCK, FabricBlockSettings.copyOf(Blocks.LAPIS_BLOCK).strength(2.5f, 6f), "block/lapis_block", "block/white_concrete");
  @ApiStatus.AvailableSince("1.2.4")
  @Translucent
  public static final GlassHandrailBlock SNOW_DECORATED_PACKED_ICE_HANDRAIL = new GlassHandrailBlock(Blocks.PACKED_ICE, FabricBlockSettings.copyOf(Blocks.PACKED_ICE).strength(2.5f, 6f), "block/packed_ice", "block/snow");
  @ApiStatus.AvailableSince("1.2.4")
  @Translucent
  public static final GlassHandrailBlock SNOW_DECORATED_BLUE_ICE_HANDRAIL = new GlassHandrailBlock(Blocks.BLUE_ICE, FabricBlockSettings.copyOf(Blocks.BLUE_ICE).strength(2.5f, 6f), "block/blue_ice", "block/snow");

  @ApiStatus.AvailableSince("0.2.4")
  @Translucent
  @MiningLevel(MiningLevel.Tool.AXE)
  public static final GlassHandrailBlock GLASS_OAK_HANDRAIL = new GlassHandrailBlock(Blocks.OAK_WOOD, FabricBlockSettings.copyOf(Blocks.OAK_WOOD).strength(1.0f), "block/oak_log", "block/oak_planks");
  @ApiStatus.AvailableSince("0.2.4")
  @Translucent
  @MiningLevel(MiningLevel.Tool.AXE)
  public static final ColoredGlassHandrailBlock COLORED_DECORATED_OAK_HANDRAIL = new ColoredGlassHandrailBlock(Blocks.OAK_WOOD, FabricBlockSettings.copyOf(Blocks.OAK_WOOD).strength(1.0f), "block/oak_log", "mishanguc:block/pale_planks");
  @ApiStatus.AvailableSince("0.2.4")
  @Translucent
  @MiningLevel(MiningLevel.Tool.AXE)
  public static final GlassHandrailBlock GLASS_SPRUCE_HANDRAIL = new GlassHandrailBlock(Blocks.SPRUCE_WOOD, FabricBlockSettings.copyOf(Blocks.SPRUCE_WOOD).strength(1.0f), "block/spruce_log", "block/spruce_planks");
  @ApiStatus.AvailableSince("0.2.4")
  @Translucent
  @MiningLevel(MiningLevel.Tool.AXE)
  public static final ColoredGlassHandrailBlock COLORED_DECORATED_SPRUCE_HANDRAIL = new ColoredGlassHandrailBlock(Blocks.SPRUCE_WOOD, FabricBlockSettings.copyOf(Blocks.SPRUCE_WOOD).strength(1.0f), "block/spruce_log", "mishanguc:block/pale_planks");
  @ApiStatus.AvailableSince("0.2.4")
  @Translucent
  @MiningLevel(MiningLevel.Tool.AXE)
  public static final GlassHandrailBlock GLASS_BIRCH_HANDRAIL = new GlassHandrailBlock(Blocks.BIRCH_WOOD, FabricBlockSettings.copyOf(Blocks.BIRCH_WOOD).strength(1.0f), "block/birch_log", "block/birch_planks");
  @ApiStatus.AvailableSince("0.2.4")
  @Translucent
  @MiningLevel(MiningLevel.Tool.AXE)
  public static final ColoredGlassHandrailBlock COLORED_DECORATED_BIRCH_HANDRAIL = new ColoredGlassHandrailBlock(Blocks.BIRCH_WOOD, FabricBlockSettings.copyOf(Blocks.BIRCH_WOOD).strength(1.0f), "block/birch_log", "mishanguc:block/pale_planks");
  @ApiStatus.AvailableSince("0.2.4")
  @Translucent
  @MiningLevel(MiningLevel.Tool.AXE)
  public static final GlassHandrailBlock GLASS_JUNGLE_HANDRAIL = new GlassHandrailBlock(Blocks.JUNGLE_WOOD, FabricBlockSettings.copyOf(Blocks.JUNGLE_WOOD).strength(1.0f), "block/jungle_log", "block/jungle_planks");
  @ApiStatus.AvailableSince("0.2.4")
  @Translucent
  @MiningLevel(MiningLevel.Tool.AXE)
  public static final ColoredGlassHandrailBlock COLORED_DECORATED_JUNGLE_HANDRAIL = new ColoredGlassHandrailBlock(Blocks.JUNGLE_WOOD, FabricBlockSettings.copyOf(Blocks.JUNGLE_WOOD).strength(1.0f), "block/jungle_log", "mishanguc:block/pale_planks");
  @ApiStatus.AvailableSince("0.2.4")
  @Translucent
  @MiningLevel(MiningLevel.Tool.AXE)
  public static final GlassHandrailBlock GLASS_ACACIA_HANDRAIL = new GlassHandrailBlock(Blocks.ACACIA_WOOD, FabricBlockSettings.copyOf(Blocks.ACACIA_WOOD).strength(1.0f), "block/acacia_log", "block/acacia_planks");
  @ApiStatus.AvailableSince("0.2.4")
  @Translucent
  @MiningLevel(MiningLevel.Tool.AXE)
  public static final ColoredGlassHandrailBlock COLORED_DECORATED_ACACIA_HANDRAIL = new ColoredGlassHandrailBlock(Blocks.ACACIA_WOOD, FabricBlockSettings.copyOf(Blocks.ACACIA_WOOD).strength(1.0f), "block/acacia_log", "mishanguc:block/pale_planks");
  @ApiStatus.AvailableSince("1.1.1-mc1.19.4")
  @Translucent
  @MiningLevel(MiningLevel.Tool.AXE)
  public static final GlassHandrailBlock GLASS_CHERRY_HANDRAIL = new GlassHandrailBlock(Blocks.CHERRY_WOOD, FabricBlockSettings.copyOf(Blocks.CHERRY_WOOD).strength(1.0f), "block/cherry_log", "block/cherry_planks");
  @ApiStatus.AvailableSince("1.1.1-mc1.19.4")
  @Translucent
  @MiningLevel(MiningLevel.Tool.AXE)
  public static final ColoredGlassHandrailBlock COLORED_DECORATED_CHERRY_HANDRAIL = new ColoredGlassHandrailBlock(Blocks.CHERRY_WOOD, FabricBlockSettings.copyOf(Blocks.CHERRY_WOOD).strength(1.0f), "block/cherry_log", "mishanguc:block/pale_planks");
  @ApiStatus.AvailableSince("0.2.4")
  @Translucent
  @MiningLevel(MiningLevel.Tool.AXE)
  public static final GlassHandrailBlock GLASS_DARK_OAK_HANDRAIL = new GlassHandrailBlock(Blocks.DARK_OAK_WOOD, FabricBlockSettings.copyOf(Blocks.DARK_OAK_WOOD).strength(1.0f), "block/dark_oak_log", "block/dark_oak_planks");
  @ApiStatus.AvailableSince("0.2.4")
  @Translucent
  @MiningLevel(MiningLevel.Tool.AXE)
  public static final ColoredGlassHandrailBlock COLORED_DECORATED_DARK_OAK_HANDRAIL = new ColoredGlassHandrailBlock(Blocks.DARK_OAK_WOOD, FabricBlockSettings.copyOf(Blocks.DARK_OAK_WOOD).strength(1.0f), "block/dark_oak_log", "mishanguc:block/pale_planks");
  @ApiStatus.AvailableSince("0.2.4")
  @Translucent
  @MiningLevel(MiningLevel.Tool.AXE)
  public static final GlassHandrailBlock GLASS_MANGROVE_HANDRAIL = new GlassHandrailBlock(Blocks.MANGROVE_WOOD, FabricBlockSettings.copyOf(Blocks.MANGROVE_WOOD).strength(1.0f), "block/mangrove_log", "block/mangrove_planks");
  @ApiStatus.AvailableSince("0.2.4")
  @Translucent
  @MiningLevel(MiningLevel.Tool.AXE)
  public static final ColoredGlassHandrailBlock COLORED_DECORATED_MANGROVE_HANDRAIL = new ColoredGlassHandrailBlock(Blocks.MANGROVE_WOOD, FabricBlockSettings.copyOf(Blocks.MANGROVE_WOOD).strength(1.0f), "block/mangrove_log", "mishanguc:block/pale_planks");
  @ApiStatus.AvailableSince("1.2.4")
  @Translucent
  @MiningLevel(MiningLevel.Tool.AXE)
  public static final GlassHandrailBlock GLASS_CRIMSON_HANDRAIL = new GlassHandrailBlock(Blocks.CRIMSON_HYPHAE, FabricBlockSettings.copyOf(Blocks.CRIMSON_HYPHAE).strength(1.0f), "block/crimson_stem", "block/crimson_planks");
  @ApiStatus.AvailableSince("1.2.4")
  @Translucent
  @MiningLevel(MiningLevel.Tool.AXE)
  public static final ColoredGlassHandrailBlock COLORED_DECORATED_CRIMSON_HANDRAIL = new ColoredGlassHandrailBlock(Blocks.CRIMSON_HYPHAE, FabricBlockSettings.copyOf(Blocks.CRIMSON_HYPHAE).strength(1.0f), "block/crimson_stem", "mishanguc:block/pale_planks");
  @ApiStatus.AvailableSince("1.2.4")
  @Translucent
  @MiningLevel(MiningLevel.Tool.AXE)
  public static final GlassHandrailBlock GLASS_WARPED_HANDRAIL = new GlassHandrailBlock(Blocks.WARPED_HYPHAE, FabricBlockSettings.copyOf(Blocks.WARPED_HYPHAE).strength(1.0f), "block/warped_stem", "block/warped_planks");
  @ApiStatus.AvailableSince("1.2.4")
  @Translucent
  @MiningLevel(MiningLevel.Tool.AXE)
  public static final ColoredGlassHandrailBlock COLORED_DECORATED_WARPED_HANDRAIL = new ColoredGlassHandrailBlock(Blocks.WARPED_HYPHAE, FabricBlockSettings.copyOf(Blocks.WARPED_HYPHAE).strength(1.0f), "block/warped_stem", "mishanguc:block/pale_planks");
  @ApiStatus.AvailableSince("1.0.4-mc1.19.3")
  @Translucent
  @MiningLevel(MiningLevel.Tool.AXE)
  public static final GlassHandrailBlock GLASS_BAMBOO_HANDRAIL = new GlassHandrailBlock(Blocks.BAMBOO_BLOCK, FabricBlockSettings.copyOf(Blocks.BAMBOO_BLOCK).mapColor(((AbstractBlockSettingsAccessor) ((AbstractBlockAccessor) Blocks.BAMBOO_BLOCK).getSettings()).getMapColorProvider().apply(Blocks.BAMBOO_BLOCK.getDefaultState().with(Properties.AXIS, Direction.Axis.X))).strength(1.0f), "block/bamboo_block", "block/bamboo_mosaic");

  @ApiStatus.AvailableSince("1.2.4")
  @Translucent
  @MiningLevel(level = MiningLevel.Level.DIAMOND)
  public static final GlassHandrailBlock NETHERRACK_DECORATED_OBSIDIAN_HANDRAIL = new GlassHandrailBlock(Blocks.OBSIDIAN, FabricBlockSettings.copyOf(Blocks.OBSIDIAN).strength(10, 1200), "block/obsidian", "block/netherrack");
  @ApiStatus.AvailableSince("1.2.4")
  @Translucent
  @MiningLevel(level = MiningLevel.Level.DIAMOND)
  public static final GlassHandrailBlock SOUL_SOIL_DECORATED_OBSIDIAN_HANDRAIL = new GlassHandrailBlock(Blocks.OBSIDIAN, FabricBlockSettings.copyOf(Blocks.OBSIDIAN).strength(10, 1200), "block/obsidian", "block/soul_soil");
  @ApiStatus.AvailableSince("1.2.4")
  @Translucent
  @MiningLevel(level = MiningLevel.Level.DIAMOND)
  public static final GlassHandrailBlock MAGMA_DECORATED_OBSIDIAN_HANDRAIL = new GlassHandrailBlock(Blocks.OBSIDIAN, FabricBlockSettings.copyOf(Blocks.OBSIDIAN).strength(10, 1200).luminance(3), "block/obsidian", "block/magma");
  @ApiStatus.AvailableSince("0.2.4")
  @Translucent
  @MiningLevel(level = MiningLevel.Level.DIAMOND)
  public static final ColoredGlassHandrailBlock COLORED_DECORATED_OBSIDIAN_HANDRAIL = new ColoredGlassHandrailBlock(Blocks.OBSIDIAN, FabricBlockSettings.copyOf(Blocks.OBSIDIAN).strength(10, 1200), "block/obsidian", "block/white_concrete");

  @ApiStatus.AvailableSince("1.2.4")
  @Translucent
  @MiningLevel(level = MiningLevel.Level.DIAMOND)
  public static final GlassHandrailBlock NETHERRACK_DECORATED_CRYING_OBSIDIAN_HANDRAIL = new GlassHandrailBlock(Blocks.CRYING_OBSIDIAN, FabricBlockSettings.copyOf(Blocks.CRYING_OBSIDIAN).strength(10, 1200), "block/crying_obsidian", "block/netherrack");
  @ApiStatus.AvailableSince("1.2.4")
  @Translucent
  @MiningLevel(level = MiningLevel.Level.DIAMOND)
  public static final GlassHandrailBlock SOUL_SOIL_DECORATED_CRYING_OBSIDIAN_HANDRAIL = new GlassHandrailBlock(Blocks.CRYING_OBSIDIAN, FabricBlockSettings.copyOf(Blocks.CRYING_OBSIDIAN).strength(10, 1200), "block/crying_obsidian", "block/soul_soil");
  @ApiStatus.AvailableSince("1.2.4")
  @Translucent
  @MiningLevel(level = MiningLevel.Level.DIAMOND)
  public static final GlassHandrailBlock MAGMA_DECORATED_CRYING_OBSIDIAN_HANDRAIL = new GlassHandrailBlock(Blocks.CRYING_OBSIDIAN, FabricBlockSettings.copyOf(Blocks.CRYING_OBSIDIAN).strength(10, 1200).luminance(3), "block/crying_obsidian", "block/magma");
  @ApiStatus.AvailableSince("0.2.4")
  @Translucent
  @MiningLevel(level = MiningLevel.Level.DIAMOND)
  public static final ColoredGlassHandrailBlock COLORED_DECORATED_CRYING_OBSIDIAN_HANDRAIL = new ColoredGlassHandrailBlock(Blocks.CRYING_OBSIDIAN, FabricBlockSettings.copyOf(Blocks.CRYING_OBSIDIAN).strength(10, 1200), "block/crying_obsidian", "block/white_concrete");
}
