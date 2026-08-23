package pers.solid.mishang.uc.blocks;

import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.ColorCollection;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MapColor;
import org.jetbrains.annotations.ApiStatus;
import pers.solid.mishang.uc.Mishanguc;
import pers.solid.mishang.uc.annotations.MiningLevel;
import pers.solid.mishang.uc.block.ColoredGlassHandrailBlock;
import pers.solid.mishang.uc.block.GlassHandrailBlock;
import pers.solid.mishang.uc.block.HandrailBlock;
import pers.solid.mishang.uc.block.SimpleHandrailBlock;


/**
 * 本模组中的所有栏杆方块。
 *
 * @see pers.solid.mishang.uc.block.HandrailBlock
 */
@ApiStatus.AvailableSince("0.1.7")
public final class HandrailBlocks extends MishangucBlocks {

  // 简单的混凝土栏杆

  public static final ColorCollection<SimpleHandrailBlock> SIMPLE_CONCRETE_HANDRAIL = ColorCollection.zipMap(ColorCollection.NAMES, ColorCollection.VALUES, (s, dyeColor) -> registerSimpleHandrail("simple_" + s + "_concrete_handrail", Blocks.CONCRETE.pick(dyeColor)));


  // 简单的陶瓦栏杆

  public static final ColorCollection<SimpleHandrailBlock> SIMPLE_DYED_TERRACOTTA_HANDRAIL = ColorCollection.zipMap(ColorCollection.NAMES, ColorCollection.VALUES, (s, dyeColor) -> registerSimpleHandrail("simple_" + s + "_terracotta_handrail", Blocks.DYED_TERRACOTTA.pick(dyeColor)));

  // 冰雪。
  public static final SimpleHandrailBlock SIMPLE_ICE_HANDRAIL = registerSimpleHandrail("simple_ice_handrail", Blocks.ICE);
  public static final SimpleHandrailBlock SIMPLE_PACKED_ICE_HANDRAIL = registerSimpleHandrail("simple_packed_ice_handrail", Blocks.PACKED_ICE);
  public static final SimpleHandrailBlock SIMPLE_BLUE_ICE_HANDRAIL = registerSimpleHandrail("simple_blue_ice_handrail", Blocks.BLUE_ICE);
  @MiningLevel(MiningLevel.Tool.SHOVEL)
  public static final SimpleHandrailBlock SIMPLE_SNOW_HANDRAIL = registerSimpleHandrail("simple_snow_handrail", Blocks.SNOW_BLOCK);

  static {
    SIMPLE_SNOW_HANDRAIL.texture = Identifier.withDefaultNamespace("block/snow");
  }

  // 木头
  public static final SimpleHandrailBlock SIMPLE_OAK_HANDRAIL = registerSimpleHandrail("simple_oak_handrail", Blocks.OAK_WOOD);
  public static final SimpleHandrailBlock SIMPLE_SPRUCE_HANDRAIL = registerSimpleHandrail("simple_spruce_handrail", Blocks.SPRUCE_WOOD);
  public static final SimpleHandrailBlock SIMPLE_BIRCH_HANDRAIL = registerSimpleHandrail("simple_birch_handrail", Blocks.BIRCH_WOOD);
  public static final SimpleHandrailBlock SIMPLE_JUNGLE_HANDRAIL = registerSimpleHandrail("simple_jungle_handrail", Blocks.JUNGLE_WOOD);
  public static final SimpleHandrailBlock SIMPLE_ACACIA_HANDRAIL = registerSimpleHandrail("simple_acacia_handrail", Blocks.ACACIA_WOOD);
  @ApiStatus.AvailableSince("1.1.1-mc1.19.4")
  public static final SimpleHandrailBlock SIMPLE_CHERRY_HANDRAIL = registerSimpleHandrail("simple_cherry_handrail", Blocks.CHERRY_WOOD);
  public static final SimpleHandrailBlock SIMPLE_DARK_OAK_HANDRAIL = registerSimpleHandrail("simple_dark_oak_handrail", Blocks.DARK_OAK_WOOD);
  public static final SimpleHandrailBlock SIMPLE_PALE_OAK_HANDRAIL = registerSimpleHandrail("simple_pale_oak_handrail", Blocks.PALE_OAK_WOOD);
  @ApiStatus.AvailableSince("0.2.0-mc1.19")
  public static final SimpleHandrailBlock SIMPLE_MANGROVE_HANDRAIL = registerSimpleHandrail("simple_mangrove_handrail", Blocks.MANGROVE_WOOD);
  public static final SimpleHandrailBlock SIMPLE_CRIMSON_HANDRAIL = registerSimpleHandrail("simple_crimson_handrail", Blocks.CRIMSON_HYPHAE);
  public static final SimpleHandrailBlock SIMPLE_WARPED_HANDRAIL = registerSimpleHandrail("simple_warped_handrail", Blocks.WARPED_HYPHAE);
  public static final SimpleHandrailBlock SIMPLE_OAK_PLANK_HANDRAIL = registerSimpleHandrail("simple_oak_plank_handrail", Blocks.OAK_PLANKS);
  public static final SimpleHandrailBlock SIMPLE_SPRUCE_PLANK_HANDRAIL = registerSimpleHandrail("simple_spruce_plank_handrail", Blocks.SPRUCE_PLANKS);
  public static final SimpleHandrailBlock SIMPLE_BIRCH_PLANK_HANDRAIL = registerSimpleHandrail("simple_birch_plank_handrail", Blocks.BIRCH_PLANKS);
  public static final SimpleHandrailBlock SIMPLE_JUNGLE_PLANK_HANDRAIL = registerSimpleHandrail("simple_jungle_plank_handrail", Blocks.JUNGLE_PLANKS);
  public static final SimpleHandrailBlock SIMPLE_ACACIA_PLANK_HANDRAIL = registerSimpleHandrail("simple_acacia_plank_handrail", Blocks.ACACIA_PLANKS);
  @ApiStatus.AvailableSince("1.1.1-mc1.19.4")
  public static final SimpleHandrailBlock SIMPLE_CHERRY_PLANK_HANDRAIL = registerSimpleHandrail("simple_cherry_plank_handrail", Blocks.CHERRY_PLANKS);
  public static final SimpleHandrailBlock SIMPLE_DARK_OAK_PLANK_HANDRAIL = registerSimpleHandrail("simple_dark_oak_plank_handrail", Blocks.DARK_OAK_PLANKS);
  public static final SimpleHandrailBlock SIMPLE_PALE_OAK_PLANK_HANDRAIL = registerSimpleHandrail("simple_pale_oak_plank_handrail", Blocks.PALE_OAK_PLANKS);
  @ApiStatus.AvailableSince("0.2.0-mc1.19")
  public static final SimpleHandrailBlock SIMPLE_MANGROVE_PLANK_HANDRAIL = registerSimpleHandrail("simple_mangrove_plank_handrail", Blocks.MANGROVE_PLANKS);
  public static final SimpleHandrailBlock SIMPLE_CRIMSON_PLANK_HANDRAIL = registerSimpleHandrail("simple_crimson_plank_handrail", Blocks.CRIMSON_PLANKS);
  public static final SimpleHandrailBlock SIMPLE_WARPED_PLANK_HANDRAIL = registerSimpleHandrail("simple_warped_plank_handrail", Blocks.WARPED_PLANKS);

  @ApiStatus.AvailableSince("1.0.4-mc1.19.3")
  @MiningLevel(MiningLevel.Tool.AXE)
  public static final SimpleHandrailBlock SIMPLE_BAMBOO_HANDRAIL = registerSimpleHandrail("simple_bamboo_handrail", Blocks.BAMBOO_BLOCK, Block.Properties.ofFullCopy(Blocks.BAMBOO_BLOCK).mapColor(MapColor.PLANT));

  @ApiStatus.AvailableSince("1.0.4-mc1.19.3")
  @MiningLevel(MiningLevel.Tool.AXE)
  public static final SimpleHandrailBlock SIMPLE_BAMBOO_PLANK_HANDRAIL = registerSimpleHandrail("simple_bamboo_plank_handrail", Blocks.BAMBOO_PLANKS, Block.Properties.ofFullCopy(Blocks.BAMBOO_PLANKS));
  @ApiStatus.AvailableSince("1.0.4-mc1.19.3")
  @MiningLevel(MiningLevel.Tool.AXE)
  public static final SimpleHandrailBlock SIMPLE_BAMBOO_MOSAIC_HANDRAIL = registerSimpleHandrail("simple_bamboo_mosaic_handrail", Blocks.BAMBOO_MOSAIC, Block.Properties.ofFullCopy(Blocks.BAMBOO_MOSAIC));

  static {
    SIMPLE_OAK_HANDRAIL.texture = Identifier.withDefaultNamespace("block/oak_log");
    SIMPLE_SPRUCE_HANDRAIL.texture = Identifier.withDefaultNamespace("block/spruce_log");
    SIMPLE_BIRCH_HANDRAIL.texture = Identifier.withDefaultNamespace("block/birch_log");
    SIMPLE_JUNGLE_HANDRAIL.texture = Identifier.withDefaultNamespace("block/jungle_log");
    SIMPLE_ACACIA_HANDRAIL.texture = Identifier.withDefaultNamespace("block/acacia_log");
    SIMPLE_CHERRY_HANDRAIL.texture = Identifier.withDefaultNamespace("block/cherry_log");
    SIMPLE_DARK_OAK_HANDRAIL.texture = Identifier.withDefaultNamespace("block/dark_oak_log");
    SIMPLE_PALE_OAK_HANDRAIL.texture = Identifier.withDefaultNamespace("block/pale_oak_log");
    SIMPLE_MANGROVE_HANDRAIL.texture = Identifier.withDefaultNamespace("block/mangrove_log");
    SIMPLE_CRIMSON_HANDRAIL.texture = Identifier.withDefaultNamespace("block/crimson_stem");
    SIMPLE_WARPED_HANDRAIL.texture = Identifier.withDefaultNamespace("block/warped_stem");
    SIMPLE_BAMBOO_HANDRAIL.texture = Identifier.withDefaultNamespace("block/bamboo_block");
  }

  @MiningLevel(MiningLevel.Tool.SHOVEL)
  public static final SimpleHandrailBlock SIMPLE_DIRT_HANDRAIL = registerSimpleHandrail("simple_dirt_handrail", Blocks.DIRT);
  public static final SimpleHandrailBlock SIMPLE_STONE_HANDRAIL = registerSimpleHandrail("simple_stone_handrail", Blocks.STONE);
  public static final SimpleHandrailBlock SIMPLE_COBBLESTONE_HANDRAIL = registerSimpleHandrail("simple_cobblestone_handrail", Blocks.COBBLESTONE);
  public static final SimpleHandrailBlock SIMPLE_MOSSY_COBBLESTONE_HANDRAIL = registerSimpleHandrail("simple_mossy_cobblestone_handrail", Blocks.MOSSY_COBBLESTONE);

  // 染色玻璃。

  public static final ColorCollection<SimpleHandrailBlock> SIMPLE_STAINED_GLASS_HANDRAIL = ColorCollection.zipMap(ColorCollection.NAMES, ColorCollection.VALUES, (s, dyeColor) -> registerSimpleHandrail("simple_" + s + "_stained_glass_handrail", Blocks.STAINED_GLASS.pick(dyeColor)));

  @ApiStatus.AvailableSince("1.2.4")
  public static final ColoredGlassHandrailBlock COLORED_DECORATED_STONE_HANDRAIL = registerColoredGlassHandrail("colored_decorated_stone_handrail", Blocks.STONE, Block.Properties.ofFullCopy(Blocks.STONE).strength(2.5f, 6f), "block/stone", "block/white_concrete");
  @ApiStatus.AvailableSince("1.2.4")
  public static final ColoredGlassHandrailBlock COLORED_DECORATED_COBBLESTONE_HANDRAIL = registerColoredGlassHandrail("colored_decorated_cobblestone_handrail", Blocks.COBBLESTONE, Block.Properties.ofFullCopy(Blocks.COBBLESTONE).strength(2.5f, 6f), "block/cobblestone", "block/white_concrete");
  @ApiStatus.AvailableSince("1.2.4")
  public static final ColoredGlassHandrailBlock COLORED_DECORATED_MOSSY_COBBLESTONE_HANDRAIL = registerColoredGlassHandrail("colored_decorated_mossy_cobblestone_handrail", Blocks.MOSSY_COBBLESTONE, Block.Properties.ofFullCopy(Blocks.COBBLESTONE).strength(2.5f, 6f), "block/mossy_cobblestone", "block/white_concrete");

  @MiningLevel(level = MiningLevel.Level.STONE)
  public static final ColorCollection<GlassHandrailBlock> DECORATED_IRON_HANDRAIL = ColorCollection.zipMap(ColorCollection.NAMES, ColorCollection.VALUES, (s, dyeColor) -> registerGlassHandrail(s + "_decorated_iron_handrail", Blocks.IRON_BLOCK, Block.Properties.ofFullCopy(Blocks.IRON_BLOCK).strength(2.5f, 6f).mapColor(dyeColor), "block/iron_block", "block/" + s + "_concrete"));
  /**
   * 可自定义染色的栏杆方块。
   */
  @ApiStatus.AvailableSince("0.2.4")
  @MiningLevel(level = MiningLevel.Level.STONE)
  public static final ColoredGlassHandrailBlock COLORED_DECORATED_IRON_HANDRAIL = registerColoredGlassHandrail("colored_decorated_iron_handrail", Blocks.IRON_BLOCK, Block.Properties.ofFullCopy(Blocks.IRON_BLOCK).strength(2.5f, 6f), "block/iron_block", "block/white_concrete");

  /**
   * 可自定义染色的金栏杆方块。
   */
  @ApiStatus.AvailableSince("0.2.4")
  @MiningLevel(level = MiningLevel.Level.IRON)
  public static final ColoredGlassHandrailBlock COLORED_DECORATED_GOLD_HANDRAIL = registerColoredGlassHandrail("colored_decorated_gold_handrail", Blocks.GOLD_BLOCK, Block.Properties.ofFullCopy(Blocks.GOLD_BLOCK).strength(1.5f, 6f), "block/gold_block", "block/white_concrete");

  @ApiStatus.AvailableSince("0.2.4")
  @MiningLevel(level = MiningLevel.Level.IRON)
  public static final ColoredGlassHandrailBlock COLORED_DECORATED_EMERALD_HANDRAIL = registerColoredGlassHandrail("colored_decorated_emerald_handrail", Blocks.EMERALD_BLOCK, Block.Properties.ofFullCopy(Blocks.EMERALD_BLOCK).strength(2.5f, 6f), "block/emerald_block", "block/white_concrete");

  @ApiStatus.AvailableSince("0.2.4")
  @MiningLevel(level = MiningLevel.Level.IRON)
  public static final ColoredGlassHandrailBlock COLORED_DECORATED_DIAMOND_HANDRAIL = registerColoredGlassHandrail("colored_decorated_diamond_handrail", Blocks.DIAMOND_BLOCK, Block.Properties.ofFullCopy(Blocks.DIAMOND_BLOCK).strength(2.5f, 6f), "block/diamond_block", "block/white_concrete");

  @ApiStatus.AvailableSince("0.2.4")
  @MiningLevel(level = MiningLevel.Level.DIAMOND)
  public static final ColoredGlassHandrailBlock COLORED_DECORATED_NETHERITE_HANDRAIL = registerColoredGlassHandrail("colored_decorated_netherite_handrail", Blocks.NETHERITE_BLOCK, Block.Properties.ofFullCopy(Blocks.NETHERITE_BLOCK).strength(25f, 1200f), "block/netherite_block", "block/white_concrete");
  @ApiStatus.AvailableSince("1.2.4")
  @MiningLevel(level = MiningLevel.Level.IRON)
  public static final ColoredGlassHandrailBlock COLORED_DECORATED_LAPIS_HANDRAIL = registerColoredGlassHandrail("colored_decorated_lapis_handrail", Blocks.LAPIS_BLOCK, Block.Properties.ofFullCopy(Blocks.LAPIS_BLOCK).strength(2.5f, 6f), "block/lapis_block", "block/white_concrete");
  @ApiStatus.AvailableSince("1.4.0")
  @MiningLevel(level = MiningLevel.Level.STONE)
  public static final ColoredGlassHandrailBlock GLOWING_COLORED_DECORATED_IRON_HANDRAIL = registerColoredGlassHandrail("glowing_colored_decorated_iron_handrail", Blocks.IRON_BLOCK, Block.Properties.ofFullCopy(Blocks.IRON_BLOCK).strength(2.5f, 6f).lightLevel(x -> 15), "block/iron_block", "mishanguc:block/white_light");
  @ApiStatus.AvailableSince("1.4.0")
  @MiningLevel(level = MiningLevel.Level.IRON)
  public static final ColoredGlassHandrailBlock GLOWING_COLORED_DECORATED_GOLD_HANDRAIL = registerColoredGlassHandrail("glowing_colored_decorated_gold_handrail", Blocks.GOLD_BLOCK, Block.Properties.ofFullCopy(Blocks.GOLD_BLOCK).strength(1.5f, 6f).lightLevel(x -> 15), "block/gold_block", "mishanguc:block/white_light");
  @ApiStatus.AvailableSince("1.4.0")
  @MiningLevel(level = MiningLevel.Level.IRON)
  public static final ColoredGlassHandrailBlock GLOWING_COLORED_DECORATED_EMERALD_HANDRAIL = registerColoredGlassHandrail("glowing_colored_decorated_emerald_handrail", Blocks.EMERALD_BLOCK, Block.Properties.ofFullCopy(Blocks.EMERALD_BLOCK).strength(2.5f, 6f).lightLevel(x -> 15), "block/emerald_block", "mishanguc:block/white_light");

  @ApiStatus.AvailableSince("1.4.0")
  @MiningLevel(level = MiningLevel.Level.IRON)
  public static final ColoredGlassHandrailBlock GLOWING_COLORED_DECORATED_DIAMOND_HANDRAIL = registerColoredGlassHandrail("glowing_colored_decorated_diamond_handrail", Blocks.DIAMOND_BLOCK, Block.Properties.ofFullCopy(Blocks.DIAMOND_BLOCK).strength(2.5f, 6f).lightLevel(x -> 15), "block/diamond_block", "mishanguc:block/white_light");

  @ApiStatus.AvailableSince("1.4.0")
  @MiningLevel(level = MiningLevel.Level.DIAMOND)
  public static final ColoredGlassHandrailBlock GLOWING_COLORED_DECORATED_NETHERITE_HANDRAIL = registerColoredGlassHandrail("glowing_colored_decorated_netherite_handrail", Blocks.NETHERITE_BLOCK, Block.Properties.ofFullCopy(Blocks.NETHERITE_BLOCK).strength(25f, 1200f).lightLevel(x -> 15), "block/netherite_block", "mishanguc:block/white_light");
  @ApiStatus.AvailableSince("1.4.0")
  @MiningLevel(level = MiningLevel.Level.IRON)
  public static final ColoredGlassHandrailBlock GLOWING_COLORED_DECORATED_LAPIS_HANDRAIL = registerColoredGlassHandrail("glowing_colored_decorated_lapis_handrail", Blocks.LAPIS_BLOCK, Block.Properties.ofFullCopy(Blocks.LAPIS_BLOCK).strength(2.5f, 6f).lightLevel(x -> 15), "block/lapis_block", "mishanguc:block/white_light");


  @ApiStatus.AvailableSince("1.2.4")
  public static final GlassHandrailBlock SNOW_DECORATED_PACKED_ICE_HANDRAIL = registerGlassHandrail("snow_decorated_packed_ice_handrail", Blocks.PACKED_ICE, Block.Properties.ofFullCopy(Blocks.PACKED_ICE).strength(2.5f, 6f), "block/packed_ice", "block/snow");
  @ApiStatus.AvailableSince("1.2.4")
  public static final GlassHandrailBlock SNOW_DECORATED_BLUE_ICE_HANDRAIL = registerGlassHandrail("snow_decorated_blue_ice_handrail", Blocks.BLUE_ICE, Block.Properties.ofFullCopy(Blocks.BLUE_ICE).strength(2.5f, 6f), "block/blue_ice", "block/snow");
  @ApiStatus.AvailableSince("1.4.0")
  public static final ColoredGlassHandrailBlock COLORED_DECORATED_PACKED_ICE_HANDRAIL = registerColoredGlassHandrail("colored_decorated_packed_ice_handrail", Blocks.PACKED_ICE, Block.Properties.ofFullCopy(Blocks.PACKED_ICE).strength(2.5f, 6f), "block/packed_ice", "block/snow");
  @ApiStatus.AvailableSince("1.4.0")
  public static final ColoredGlassHandrailBlock COLORED_DECORATED_BLUE_ICE_HANDRAIL = registerColoredGlassHandrail("colored_decorated_blue_ice_handrail", Blocks.BLUE_ICE, Block.Properties.ofFullCopy(Blocks.BLUE_ICE).strength(2.5f, 6f), "block/blue_ice", "block/snow");

  @ApiStatus.AvailableSince("0.2.4")
  @MiningLevel(MiningLevel.Tool.AXE)
  public static final GlassHandrailBlock GLASS_OAK_HANDRAIL = registerGlassHandrail("glass_oak_handrail", Blocks.OAK_WOOD, Block.Properties.ofFullCopy(Blocks.OAK_WOOD).strength(1.0f), "block/oak_log", "block/oak_planks");
  @ApiStatus.AvailableSince("0.2.4")
  @MiningLevel(MiningLevel.Tool.AXE)
  public static final GlassHandrailBlock GLASS_SPRUCE_HANDRAIL = registerGlassHandrail("glass_spruce_handrail", Blocks.SPRUCE_WOOD, Block.Properties.ofFullCopy(Blocks.SPRUCE_WOOD).strength(1.0f), "block/spruce_log", "block/spruce_planks");
  @ApiStatus.AvailableSince("0.2.4")
  @MiningLevel(MiningLevel.Tool.AXE)
  public static final GlassHandrailBlock GLASS_BIRCH_HANDRAIL = registerGlassHandrail("glass_birch_handrail", Blocks.BIRCH_WOOD, Block.Properties.ofFullCopy(Blocks.BIRCH_WOOD).strength(1.0f), "block/birch_log", "block/birch_planks");
  @ApiStatus.AvailableSince("0.2.4")
  @MiningLevel(MiningLevel.Tool.AXE)
  public static final GlassHandrailBlock GLASS_JUNGLE_HANDRAIL = registerGlassHandrail("glass_jungle_handrail", Blocks.JUNGLE_WOOD, Block.Properties.ofFullCopy(Blocks.JUNGLE_WOOD).strength(1.0f), "block/jungle_log", "block/jungle_planks");
  @ApiStatus.AvailableSince("0.2.4")
  @MiningLevel(MiningLevel.Tool.AXE)
  public static final GlassHandrailBlock GLASS_ACACIA_HANDRAIL = registerGlassHandrail("glass_acacia_handrail", Blocks.ACACIA_WOOD, Block.Properties.ofFullCopy(Blocks.ACACIA_WOOD).strength(1.0f), "block/acacia_log", "block/acacia_planks");
  @ApiStatus.AvailableSince("1.1.1-mc1.19.4")
  @MiningLevel(MiningLevel.Tool.AXE)
  public static final GlassHandrailBlock GLASS_CHERRY_HANDRAIL = registerGlassHandrail("glass_cherry_handrail", Blocks.CHERRY_WOOD, Block.Properties.ofFullCopy(Blocks.CHERRY_WOOD).strength(1.0f), "block/cherry_log", "block/cherry_planks");
  @ApiStatus.AvailableSince("0.2.4")
  @MiningLevel(MiningLevel.Tool.AXE)
  public static final GlassHandrailBlock GLASS_DARK_OAK_HANDRAIL = registerGlassHandrail("glass_dark_oak_handrail", Blocks.DARK_OAK_WOOD, Block.Properties.ofFullCopy(Blocks.DARK_OAK_WOOD).strength(1.0f), "block/dark_oak_log", "block/dark_oak_planks");
  @MiningLevel(MiningLevel.Tool.AXE)
  public static final GlassHandrailBlock GLASS_PALE_OAK_HANDRAIL = registerGlassHandrail("glass_pale_oak_handrail", Blocks.PALE_OAK_WOOD, Block.Properties.ofFullCopy(Blocks.PALE_OAK_WOOD).strength(1.0f), "block/pale_oak_log", "block/pale_oak_planks");
  @ApiStatus.AvailableSince("0.2.4")
  @MiningLevel(MiningLevel.Tool.AXE)
  public static final GlassHandrailBlock GLASS_MANGROVE_HANDRAIL = registerGlassHandrail("glass_mangrove_handrail", Blocks.MANGROVE_WOOD, Block.Properties.ofFullCopy(Blocks.MANGROVE_WOOD).strength(1.0f), "block/mangrove_log", "block/mangrove_planks");
  @ApiStatus.AvailableSince("1.2.4")
  @MiningLevel(MiningLevel.Tool.AXE)
  public static final GlassHandrailBlock GLASS_CRIMSON_HANDRAIL = registerGlassHandrail("glass_crimson_handrail", Blocks.CRIMSON_HYPHAE, Block.Properties.ofFullCopy(Blocks.CRIMSON_HYPHAE).strength(1.0f), "block/crimson_stem", "block/crimson_planks");
  @ApiStatus.AvailableSince("1.2.4")
  @MiningLevel(MiningLevel.Tool.AXE)
  public static final GlassHandrailBlock GLASS_WARPED_HANDRAIL = registerGlassHandrail("glass_warped_handrail", Blocks.WARPED_HYPHAE, Block.Properties.ofFullCopy(Blocks.WARPED_HYPHAE).strength(1.0f), "block/warped_stem", "block/warped_planks");
  @ApiStatus.AvailableSince("1.0.4-mc1.19.3")
  @MiningLevel(MiningLevel.Tool.AXE)
  public static final GlassHandrailBlock GLASS_BAMBOO_HANDRAIL = registerGlassHandrail("glass_bamboo_handrail", Blocks.BAMBOO_BLOCK, Block.Properties.ofFullCopy(Blocks.BAMBOO_BLOCK).mapColor(MapColor.PLANT).strength(1.0f), "block/bamboo_block", "block/bamboo_mosaic");
  @ApiStatus.AvailableSince("0.2.4")
  @MiningLevel(MiningLevel.Tool.AXE)
  public static final ColoredGlassHandrailBlock COLORED_DECORATED_OAK_HANDRAIL = registerColoredGlassHandrail("colored_decorated_oak_handrail", Blocks.OAK_WOOD, Block.Properties.ofFullCopy(Blocks.OAK_WOOD).strength(1.0f), "block/oak_log", "mishanguc:block/pale_planks");
  @ApiStatus.AvailableSince("0.2.4")
  @MiningLevel(MiningLevel.Tool.AXE)
  public static final ColoredGlassHandrailBlock COLORED_DECORATED_SPRUCE_HANDRAIL = registerColoredGlassHandrail("colored_decorated_spruce_handrail", Blocks.SPRUCE_WOOD, Block.Properties.ofFullCopy(Blocks.SPRUCE_WOOD).strength(1.0f), "block/spruce_log", "mishanguc:block/pale_planks");
  @ApiStatus.AvailableSince("0.2.4")
  @MiningLevel(MiningLevel.Tool.AXE)
  public static final ColoredGlassHandrailBlock COLORED_DECORATED_BIRCH_HANDRAIL = registerColoredGlassHandrail("colored_decorated_birch_handrail", Blocks.BIRCH_WOOD, Block.Properties.ofFullCopy(Blocks.BIRCH_WOOD).strength(1.0f), "block/birch_log", "mishanguc:block/pale_planks");
  @ApiStatus.AvailableSince("0.2.4")
  @MiningLevel(MiningLevel.Tool.AXE)
  public static final ColoredGlassHandrailBlock COLORED_DECORATED_JUNGLE_HANDRAIL = registerColoredGlassHandrail("colored_decorated_jungle_handrail", Blocks.JUNGLE_WOOD, Block.Properties.ofFullCopy(Blocks.JUNGLE_WOOD).strength(1.0f), "block/jungle_log", "mishanguc:block/pale_planks");
  @ApiStatus.AvailableSince("0.2.4")
  @MiningLevel(MiningLevel.Tool.AXE)
  public static final ColoredGlassHandrailBlock COLORED_DECORATED_ACACIA_HANDRAIL = registerColoredGlassHandrail("colored_decorated_acacia_handrail", Blocks.ACACIA_WOOD, Block.Properties.ofFullCopy(Blocks.ACACIA_WOOD).strength(1.0f), "block/acacia_log", "mishanguc:block/pale_planks");
  @ApiStatus.AvailableSince("1.1.1-mc1.19.4")
  @MiningLevel(MiningLevel.Tool.AXE)
  public static final ColoredGlassHandrailBlock COLORED_DECORATED_CHERRY_HANDRAIL = registerColoredGlassHandrail("colored_decorated_cherry_handrail", Blocks.CHERRY_WOOD, Block.Properties.ofFullCopy(Blocks.CHERRY_WOOD).strength(1.0f), "block/cherry_log", "mishanguc:block/pale_planks");
  @ApiStatus.AvailableSince("0.2.4")
  @MiningLevel(MiningLevel.Tool.AXE)
  public static final ColoredGlassHandrailBlock COLORED_DECORATED_DARK_OAK_HANDRAIL = registerColoredGlassHandrail("colored_decorated_dark_oak_handrail", Blocks.DARK_OAK_WOOD, Block.Properties.ofFullCopy(Blocks.DARK_OAK_WOOD).strength(1.0f), "block/dark_oak_log", "mishanguc:block/pale_planks");
  @MiningLevel(MiningLevel.Tool.AXE)
  public static final ColoredGlassHandrailBlock COLORED_DECORATED_PALE_OAK_HANDRAIL = registerColoredGlassHandrail("colored_decorated_pale_oak_handrail", Blocks.PALE_OAK_WOOD, Block.Properties.ofFullCopy(Blocks.PALE_OAK_WOOD).strength(1.0f), "block/pale_oak_log", "mishanguc:block/pale_planks");
  @ApiStatus.AvailableSince("0.2.4")
  @MiningLevel(MiningLevel.Tool.AXE)
  public static final ColoredGlassHandrailBlock COLORED_DECORATED_MANGROVE_HANDRAIL = registerColoredGlassHandrail("colored_decorated_mangrove_handrail", Blocks.MANGROVE_WOOD, Block.Properties.ofFullCopy(Blocks.MANGROVE_WOOD).strength(1.0f), "block/mangrove_log", "mishanguc:block/pale_planks");
  @ApiStatus.AvailableSince("1.2.4")
  @MiningLevel(MiningLevel.Tool.AXE)
  public static final ColoredGlassHandrailBlock COLORED_DECORATED_CRIMSON_HANDRAIL = registerColoredGlassHandrail("colored_decorated_crimson_handrail", Blocks.CRIMSON_HYPHAE, Block.Properties.ofFullCopy(Blocks.CRIMSON_HYPHAE).strength(1.0f), "block/crimson_stem", "mishanguc:block/pale_planks");
  @ApiStatus.AvailableSince("1.2.4")
  @MiningLevel(MiningLevel.Tool.AXE)
  public static final ColoredGlassHandrailBlock COLORED_DECORATED_WARPED_HANDRAIL = registerColoredGlassHandrail("colored_decorated_warped_handrail", Blocks.WARPED_HYPHAE, Block.Properties.ofFullCopy(Blocks.WARPED_HYPHAE).strength(1.0f), "block/warped_stem", "mishanguc:block/pale_planks");
  @ApiStatus.AvailableSince("1.4.0")
  @MiningLevel(MiningLevel.Tool.AXE)
  public static final ColoredGlassHandrailBlock COLORED_DECORATED_BAMBOO_HANDRAIL = registerColoredGlassHandrail("colored_decorated_bamboo_handrail", Blocks.BAMBOO_BLOCK, Block.Properties.ofFullCopy(Blocks.BAMBOO_BLOCK).mapColor(MapColor.PLANT).strength(1.0f), "block/bamboo_block", "mishanguc:block/pale_planks");
  @ApiStatus.AvailableSince("1.4.0")
  @MiningLevel(MiningLevel.Tool.AXE)
  public static final ColoredGlassHandrailBlock COLORED_DECORATED_STRIPPED_OAK_HANDRAIL = registerColoredGlassHandrail("colored_decorated_stripped_oak_handrail", Blocks.STRIPPED_OAK_WOOD, Block.Properties.ofFullCopy(Blocks.STRIPPED_OAK_WOOD).strength(1.0f), "block/stripped_oak_log", "mishanguc:block/pale_planks");
  @ApiStatus.AvailableSince("1.4.0")
  @MiningLevel(MiningLevel.Tool.AXE)
  public static final ColoredGlassHandrailBlock COLORED_DECORATED_STRIPPED_SPRUCE_HANDRAIL = registerColoredGlassHandrail("colored_decorated_stripped_spruce_handrail", Blocks.STRIPPED_SPRUCE_WOOD, Block.Properties.ofFullCopy(Blocks.STRIPPED_SPRUCE_WOOD).strength(1.0f), "block/stripped_spruce_log", "mishanguc:block/pale_planks");
  @ApiStatus.AvailableSince("1.4.0")
  @MiningLevel(MiningLevel.Tool.AXE)
  public static final ColoredGlassHandrailBlock COLORED_DECORATED_STRIPPED_BIRCH_HANDRAIL = registerColoredGlassHandrail("colored_decorated_stripped_birch_handrail", Blocks.STRIPPED_BIRCH_WOOD, Block.Properties.ofFullCopy(Blocks.STRIPPED_BIRCH_WOOD).strength(1.0f), "block/stripped_birch_log", "mishanguc:block/pale_planks");
  @ApiStatus.AvailableSince("1.4.0")
  @MiningLevel(MiningLevel.Tool.AXE)
  public static final ColoredGlassHandrailBlock COLORED_DECORATED_STRIPPED_JUNGLE_HANDRAIL = registerColoredGlassHandrail("colored_decorated_stripped_jungle_handrail", Blocks.STRIPPED_JUNGLE_WOOD, Block.Properties.ofFullCopy(Blocks.STRIPPED_JUNGLE_WOOD).strength(1.0f), "block/stripped_jungle_log", "mishanguc:block/pale_planks");
  @ApiStatus.AvailableSince("1.4.0")
  @MiningLevel(MiningLevel.Tool.AXE)
  public static final ColoredGlassHandrailBlock COLORED_DECORATED_STRIPPED_ACACIA_HANDRAIL = registerColoredGlassHandrail("colored_decorated_stripped_acacia_handrail", Blocks.STRIPPED_ACACIA_WOOD, Block.Properties.ofFullCopy(Blocks.STRIPPED_ACACIA_WOOD).strength(1.0f), "block/stripped_acacia_log", "mishanguc:block/pale_planks");
  @ApiStatus.AvailableSince("1.4.0")
  @MiningLevel(MiningLevel.Tool.AXE)
  public static final ColoredGlassHandrailBlock COLORED_DECORATED_STRIPPED_CHERRY_HANDRAIL = registerColoredGlassHandrail("colored_decorated_stripped_cherry_handrail", Blocks.STRIPPED_CHERRY_WOOD, Block.Properties.ofFullCopy(Blocks.STRIPPED_CHERRY_WOOD).strength(1.0f), "block/stripped_cherry_log", "mishanguc:block/pale_planks");
  @ApiStatus.AvailableSince("1.4.0")
  @MiningLevel(MiningLevel.Tool.AXE)
  public static final ColoredGlassHandrailBlock COLORED_DECORATED_STRIPPED_DARK_OAK_HANDRAIL = registerColoredGlassHandrail("colored_decorated_stripped_dark_oak_handrail", Blocks.STRIPPED_DARK_OAK_WOOD, Block.Properties.ofFullCopy(Blocks.STRIPPED_DARK_OAK_WOOD).strength(1.0f), "block/stripped_dark_oak_log", "mishanguc:block/pale_planks");
  @MiningLevel(MiningLevel.Tool.AXE)
  public static final ColoredGlassHandrailBlock COLORED_DECORATED_STRIPPED_PALE_OAK_HANDRAIL = registerColoredGlassHandrail("colored_decorated_stripped_pale_oak_handrail", Blocks.STRIPPED_PALE_OAK_WOOD, Block.Properties.ofFullCopy(Blocks.STRIPPED_PALE_OAK_WOOD).strength(1.0f), "block/stripped_pale_oak_log", "mishanguc:block/pale_planks");
  @ApiStatus.AvailableSince("1.4.0")
  @MiningLevel(MiningLevel.Tool.AXE)
  public static final ColoredGlassHandrailBlock COLORED_DECORATED_STRIPPED_MANGROVE_HANDRAIL = registerColoredGlassHandrail("colored_decorated_stripped_mangrove_handrail", Blocks.STRIPPED_MANGROVE_WOOD, Block.Properties.ofFullCopy(Blocks.STRIPPED_MANGROVE_WOOD).mapColor(MapColor.COLOR_RED).strength(1.0f), "block/stripped_mangrove_log", "mishanguc:block/pale_planks");
  @ApiStatus.AvailableSince("1.4.0")
  @MiningLevel(MiningLevel.Tool.AXE)
  public static final ColoredGlassHandrailBlock COLORED_DECORATED_STRIPPED_CRIMSON_HANDRAIL = registerColoredGlassHandrail("colored_decorated_stripped_crimson_handrail", Blocks.STRIPPED_CRIMSON_HYPHAE, Block.Properties.ofFullCopy(Blocks.STRIPPED_CRIMSON_HYPHAE).strength(1.0f), "block/stripped_crimson_stem", "mishanguc:block/pale_planks");
  @ApiStatus.AvailableSince("1.4.0")
  @MiningLevel(MiningLevel.Tool.AXE)
  public static final ColoredGlassHandrailBlock COLORED_DECORATED_STRIPPED_WARPED_HANDRAIL = registerColoredGlassHandrail("colored_decorated_stripped_warped_handrail", Blocks.STRIPPED_WARPED_HYPHAE, Block.Properties.ofFullCopy(Blocks.STRIPPED_WARPED_HYPHAE).strength(1.0f), "block/stripped_warped_stem", "mishanguc:block/pale_planks");
  @ApiStatus.AvailableSince("1.4.0")
  @MiningLevel(MiningLevel.Tool.AXE)
  public static final ColoredGlassHandrailBlock COLORED_DECORATED_STRIPPED_BAMBOO_HANDRAIL = registerColoredGlassHandrail("colored_decorated_stripped_bamboo_handrail", Blocks.STRIPPED_BAMBOO_BLOCK, Block.Properties.ofFullCopy(Blocks.STRIPPED_BAMBOO_BLOCK).mapColor(MapColor.COLOR_YELLOW).strength(1.0f), "block/bamboo_block", "mishanguc:block/pale_planks");

  @ApiStatus.AvailableSince("1.2.4")
  @MiningLevel(level = MiningLevel.Level.DIAMOND)
  public static final GlassHandrailBlock NETHERRACK_DECORATED_OBSIDIAN_HANDRAIL = registerGlassHandrail("netherrack_decorated_obsidian_handrail", Blocks.OBSIDIAN, Block.Properties.ofFullCopy(Blocks.OBSIDIAN).strength(10, 1200), "block/obsidian", "block/netherrack");
  @ApiStatus.AvailableSince("1.2.4")
  @MiningLevel(level = MiningLevel.Level.DIAMOND)
  public static final GlassHandrailBlock NETHERRACK_DECORATED_CRYING_OBSIDIAN_HANDRAIL = registerGlassHandrail("netherrack_decorated_crying_obsidian_handrail", Blocks.CRYING_OBSIDIAN, Block.Properties.ofFullCopy(Blocks.CRYING_OBSIDIAN).strength(10, 1200), "block/crying_obsidian", "block/netherrack");
  @ApiStatus.AvailableSince("1.2.4")
  @MiningLevel(level = MiningLevel.Level.DIAMOND)
  public static final GlassHandrailBlock SOUL_SOIL_DECORATED_OBSIDIAN_HANDRAIL = registerGlassHandrail("soul_soil_decorated_obsidian_handrail", Blocks.OBSIDIAN, Block.Properties.ofFullCopy(Blocks.OBSIDIAN).strength(10, 1200), "block/obsidian", "block/soul_soil");
  @ApiStatus.AvailableSince("1.2.4")
  @MiningLevel(level = MiningLevel.Level.DIAMOND)
  public static final GlassHandrailBlock SOUL_SOIL_DECORATED_CRYING_OBSIDIAN_HANDRAIL = registerGlassHandrail("soul_soil_decorated_crying_obsidian_handrail", Blocks.CRYING_OBSIDIAN, Block.Properties.ofFullCopy(Blocks.CRYING_OBSIDIAN).strength(10, 1200), "block/crying_obsidian", "block/soul_soil");
  @ApiStatus.AvailableSince("1.2.4")
  @MiningLevel(level = MiningLevel.Level.DIAMOND)
  public static final GlassHandrailBlock MAGMA_DECORATED_OBSIDIAN_HANDRAIL = registerGlassHandrail("magma_decorated_obsidian_handrail", Blocks.OBSIDIAN, Block.Properties.ofFullCopy(Blocks.OBSIDIAN).strength(10, 1200).lightLevel(x -> 3), "block/obsidian", "block/magma");

  @ApiStatus.AvailableSince("1.2.4")
  @MiningLevel(level = MiningLevel.Level.DIAMOND)
  public static final GlassHandrailBlock MAGMA_DECORATED_CRYING_OBSIDIAN_HANDRAIL = registerGlassHandrail("magma_decorated_crying_obsidian_handrail", Blocks.CRYING_OBSIDIAN, Block.Properties.ofFullCopy(Blocks.CRYING_OBSIDIAN).strength(10, 1200).lightLevel(x -> 3), "block/crying_obsidian", "block/magma");
  @ApiStatus.AvailableSince("0.2.4")
  @MiningLevel(level = MiningLevel.Level.DIAMOND)
  public static final ColoredGlassHandrailBlock COLORED_DECORATED_OBSIDIAN_HANDRAIL = registerColoredGlassHandrail("colored_decorated_obsidian_handrail", Blocks.OBSIDIAN, Block.Properties.ofFullCopy(Blocks.OBSIDIAN).strength(10, 1200), "block/obsidian", "block/white_concrete");
  @ApiStatus.AvailableSince("0.2.4")
  @MiningLevel(level = MiningLevel.Level.DIAMOND)
  public static final ColoredGlassHandrailBlock COLORED_DECORATED_CRYING_OBSIDIAN_HANDRAIL = registerColoredGlassHandrail("colored_decorated_crying_obsidian_handrail", Blocks.CRYING_OBSIDIAN, Block.Properties.ofFullCopy(Blocks.CRYING_OBSIDIAN).strength(10, 1200), "block/crying_obsidian", "block/white_concrete");

  private static <T extends HandrailBlock> T registerMultiple(String name, T block) {
    Registry.register(BuiltInRegistries.BLOCK, Mishanguc.id(name), block);
    Registry.register(BuiltInRegistries.BLOCK, Mishanguc.id(name + "_central"), block.central());
    Registry.register(BuiltInRegistries.BLOCK, Mishanguc.id(name + "_stair"), block.stair());
    Registry.register(BuiltInRegistries.BLOCK, Mishanguc.id(name + "_corner"), block.corner());
    Registry.register(BuiltInRegistries.BLOCK, Mishanguc.id(name + "_outer"), block.outer());
    return block;
  }

  private static SimpleHandrailBlock registerSimpleHandrail(String name, Block baseBlock, BlockBehaviour.Properties settings) {
    return registerMultiple(name, new SimpleHandrailBlock(baseBlock, settings, Mishanguc.id(name)));
  }

  private static SimpleHandrailBlock registerSimpleHandrail(String name, Block baseBlock) {
    return registerSimpleHandrail(name, baseBlock, BlockBehaviour.Properties.ofFullCopy(baseBlock));
  }

  private static GlassHandrailBlock registerGlassHandrail(String name, Block baseBlock, BlockBehaviour.Properties settings, String frameTexture, String decorationTexture) {
    return registerMultiple(name, new GlassHandrailBlock(baseBlock, settings, frameTexture, decorationTexture, Mishanguc.id(name)));
  }

  private static ColoredGlassHandrailBlock registerColoredGlassHandrail(String name, Block baseBlock, BlockBehaviour.Properties settings, String frameTexture, String decorationTexture) {
    return registerMultiple(name, new ColoredGlassHandrailBlock(baseBlock, settings, frameTexture, decorationTexture, Mishanguc.id(name)));
  }
}
