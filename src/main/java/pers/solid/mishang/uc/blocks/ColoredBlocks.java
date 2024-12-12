package pers.solid.mishang.uc.blocks;

import net.minecraft.block.Blocks;
import net.minecraft.client.data.TextureMap;
import net.minecraft.util.DyeColor;
import net.minecraft.util.Identifier;
import pers.solid.mishang.uc.Mishanguc;
import pers.solid.mishang.uc.annotations.MiningLevel;
import pers.solid.mishang.uc.annotations.Translucent;
import pers.solid.mishang.uc.block.*;

/**
 * 此类包含迷上城建模组的所有染色方块。注意：这些方块通常需要适当配置好 mineable 标签（1.16.5 及以下版本，只需要在 FabricBlockSetting 中设置）以配置可破坏的方块。此外，还需要配置好 {@link Mishanguc#registerColoredBlocks()} 以配置方块与方块之间的对应关系。
 *
 * @see ColoredBlock
 * @see pers.solid.mishang.uc.blockentity.ColoredBlockEntity
 */

public final class ColoredBlocks extends MishangucBlocks {
  @MiningLevel(MiningLevel.Tool.NONE)
  public static final ColoredCubeBlock COLORED_WOOL = register("colored_wool", settings -> ColoredCubeBlock.cubeAll(settings, "block/white_wool"), Blocks.WHITE_WOOL);
  public static final ColoredCubeBlock COLORED_TERRACOTTA = register("colored_terracotta", settings -> ColoredCubeBlock.cubeAll(settings, "block/white_terracotta"), Blocks.WHITE_TERRACOTTA);

  public static final ColoredStairsBlock COLORED_TERRACOTTA_STAIRS = register("colored_terracotta_stairs", settings -> new ColoredStairsBlock(COLORED_TERRACOTTA, settings), COLORED_TERRACOTTA);

  public static final ColoredSlabBlock COLORED_TERRACOTTA_SLAB = register("colored_terracotta_slab", settings -> new ColoredSlabBlock(COLORED_TERRACOTTA, settings), COLORED_TERRACOTTA);

  public static final ColoredCubeBlock COLORED_CONCRETE = register("colored_concrete", settings -> ColoredCubeBlock.cubeAll(settings, "block/white_concrete"), Blocks.WHITE_CONCRETE);

  public static final ColoredStairsBlock COLORED_CONCRETE_STAIRS = register("colored_concrete_stairs", settings -> new ColoredStairsBlock(COLORED_CONCRETE, settings), COLORED_CONCRETE);

  public static final ColoredSlabBlock COLORED_CONCRETE_SLAB = register("colored_concrete_slab", settings -> new ColoredSlabBlock(COLORED_CONCRETE, settings), COLORED_CONCRETE);

  @MiningLevel(MiningLevel.Tool.AXE)
  public static final ColoredCubeBlock COLORED_PLANKS = register("colored_planks", settings -> ColoredCubeBlock.cubeAll(settings, "mishanguc:block/pale_planks"), Blocks.BIRCH_PLANKS);
  @MiningLevel(MiningLevel.Tool.AXE)
  public static final ColoredStairsBlock COLORED_PLANK_STAIRS = register("colored_plank_stairs", settings -> new ColoredStairsBlock(COLORED_PLANKS, settings), Blocks.BIRCH_STAIRS);
  @MiningLevel(MiningLevel.Tool.AXE)
  public static final ColoredSlabBlock COLORED_PLANK_SLAB = register("colored_plank_slab", settings -> new ColoredSlabBlock(COLORED_PLANKS, settings), Blocks.BIRCH_SLAB);
  @MiningLevel(MiningLevel.Tool.SHOVEL)
  public static final ColoredCubeBlock COLORED_DIRT = register("colored_dirt", settings -> ColoredCubeBlock.cubeAll(settings, "mishanguc:block/pale_dirt"), Blocks.DIRT);
  public static final ColoredCubeBlock COLORED_COBBLESTONE = register("colored_cobblestone", settings -> ColoredCubeBlock.cubeAll(settings, "mishanguc:block/pale_cobblestone"), Blocks.COBBLESTONE);
  public static final ColoredStairsBlock COLORED_COBBLESTONE_STAIRS = register("colored_cobblestone_stairs", settings -> new ColoredStairsBlock(COLORED_COBBLESTONE, settings), Blocks.COBBLESTONE_STAIRS);
  public static final ColoredSlabBlock COLORED_COBBLESTONE_SLAB = register("colored_cobblestone_slab", settings -> new ColoredSlabBlock(COLORED_COBBLESTONE, settings), Blocks.COBBLESTONE_SLAB);

  public static final ColoredCubeBlock COLORED_ANDESITE = register("colored_andesite", settings -> ColoredCubeBlock.cubeAll(settings, "block/andesite"), Blocks.ANDESITE);

  public static final ColoredStairsBlock COLORED_ANDESITE_STAIRS = register("colored_andesite_stairs", settings -> new ColoredStairsBlock(COLORED_ANDESITE, settings), Blocks.ANDESITE_STAIRS);

  public static final ColoredSlabBlock COLORED_ANDESITE_SLAB = register("colored_andesite_slab", settings -> new ColoredSlabBlock(COLORED_ANDESITE, settings), Blocks.ANDESITE_SLAB);

  public static final ColoredCubeBlock COLORED_DIORITE = register("colored_diorite", settings -> ColoredCubeBlock.cubeAll(settings, "block/diorite"), Blocks.DIORITE);

  public static final ColoredStairsBlock COLORED_DIORITE_STAIRS = register("colored_diorite_stairs", settings -> new ColoredStairsBlock(COLORED_DIORITE, settings), Blocks.DIORITE_STAIRS);

  public static final ColoredSlabBlock COLORED_DIORITE_SLAB = register("colored_diorite_slab", settings -> new ColoredSlabBlock(COLORED_DIORITE, settings), Blocks.DIORITE_SLAB);

  public static final ColoredCubeBlock COLORED_CALCITE = register("colored_calcite", settings -> ColoredCubeBlock.cubeAll(settings, "block/calcite"), Blocks.CALCITE);

  public static final ColoredStairsBlock COLORED_CALCITE_STAIRS = register("colored_calcite_stairs", settings -> new ColoredStairsBlock(COLORED_CALCITE, settings), COLORED_CALCITE);

  public static final ColoredSlabBlock COLORED_CALCITE_SLAB = register("colored_calcite_slab", settings -> new ColoredSlabBlock(COLORED_CALCITE, settings), COLORED_CALCITE);

  public static final ColoredCubeBlock COLORED_TUFF = register("colored_tuff", settings -> ColoredCubeBlock.cubeAll(settings, "block/tuff"), Blocks.TUFF);

  public static final ColoredStairsBlock COLORED_TUFF_STAIRS = register("colored_tuff_stairs", settings -> new ColoredStairsBlock(COLORED_TUFF, settings), Blocks.TUFF_STAIRS);

  public static final ColoredSlabBlock COLORED_TUFF_SLAB = register("colored_tuff_slab", settings -> new ColoredSlabBlock(COLORED_TUFF, settings), Blocks.TUFF_SLAB);

  public static final ColoredCubeBlock COLORED_STONE = register("colored_stone", settings -> ColoredCubeBlock.cubeAll(settings, "mishanguc:block/pale_stone"), Blocks.STONE);
  public static final ColoredStairsBlock COLORED_STONE_STAIRS = register("colored_stone_stairs", settings -> new ColoredStairsBlock(COLORED_STONE, settings), Blocks.STONE_STAIRS);
  public static final ColoredSlabBlock COLORED_STONE_SLAB = register("colored_stone_slab", settings -> new ColoredSlabBlock(COLORED_STONE, settings), Blocks.STONE_SLAB);

  public static final ColoredCubeBlock COLORED_STONE_BRICKS = register("colored_stone_bricks", settings -> ColoredCubeBlock.cubeAll(settings, "mishanguc:block/pale_stone_bricks"), Blocks.STONE_BRICKS);

  public static final ColoredStairsBlock COLORED_STONE_BRICK_STAIRS = register("colored_stone_brick_stairs", settings -> new ColoredStairsBlock(COLORED_STONE_BRICKS, settings), Blocks.STONE_BRICK_STAIRS);

  public static final ColoredSlabBlock COLORED_STONE_BRICK_SLAB = register("colored_stone_brick_slab", settings -> new ColoredSlabBlock(COLORED_STONE_BRICKS, settings), Blocks.STONE_BRICK_SLAB);

  public static final ColoredCubeBlock COLORED_BRICKS = register("colored_bricks", settings -> ColoredCubeBlock.cubeAll(settings, "mishanguc:block/pale_bricks"), Blocks.BRICKS);

  public static final ColoredStairsBlock COLORED_BRICK_STAIRS = register("colored_brick_stairs", settings -> new ColoredStairsBlock(COLORED_BRICKS, settings), Blocks.BRICK_STAIRS);

  public static final ColoredSlabBlock COLORED_BRICK_SLAB = register("colored_brick_slab", settings -> new ColoredSlabBlock(COLORED_BRICKS, settings), Blocks.BRICK_SLAB);

  public static final ColoredCubeBlock COLORED_QUARTZ_BLOCK = register("colored_quartz_block", settings -> ColoredCubeBlock.cubeBottomTop(settings, "block/quartz_block_top", "block/quartz_block_side", "block/quartz_block_top"), Blocks.QUARTZ_BLOCK);

  public static final ColoredStairsBlock COLORED_QUARTZ_STAIRS = register("colored_quartz_stairs", settings -> new ColoredStairsBlock(COLORED_QUARTZ_BLOCK, settings), Blocks.QUARTZ_STAIRS);

  public static final ColoredSlabBlock COLORED_QUARTZ_SLAB = register("colored_quartz_slab", settings -> new ColoredSlabBlock(COLORED_QUARTZ_BLOCK, settings), Blocks.QUARTZ_SLAB);

  public static final ColoredCubeBlock COLORED_CHISELED_QUARTZ_BLOCK = register("colored_chiseled_quartz_block", settings -> ColoredCubeBlock.cubeBottomTop(settings, "block/chiseled_quartz_block_top", "block/chiseled_quartz_block", "block/chiseled_quartz_block_top"), Blocks.CHISELED_QUARTZ_BLOCK);

  public static final ColoredCubeBlock COLORED_QUARTZ_BRICKS = register("colored_quartz_bricks", settings -> ColoredCubeBlock.cubeAll(settings, "block/quartz_bricks"), Blocks.QUARTZ_BRICKS);
  public static final ColoredCubeBlock COLORED_SMOOTH_QUARTZ = register("colored_smooth_quartz", settings -> ColoredCubeBlock.cubeAll(settings, "block/quartz_block_bottom"), Blocks.SMOOTH_QUARTZ);
  public static final ColoredStairsBlock COLORED_SMOOTH_QUARTZ_STAIRS = register("colored_smooth_quartz_stairs", settings -> new ColoredStairsBlock(COLORED_SMOOTH_QUARTZ, settings), Blocks.SMOOTH_QUARTZ_STAIRS);
  public static final ColoredSlabBlock COLORED_SMOOTH_QUARTZ_SLAB = register("colored_smooth_quartz_slab", settings -> new ColoredSlabBlock(COLORED_SMOOTH_QUARTZ, settings), Blocks.SMOOTH_QUARTZ_SLAB);

  public static final ColoredPillarBlock COLORED_QUARTZ_PILLAR = register("colored_quartz_pillar", settings -> new ColoredPillarBlock(settings, TextureMap.sideEnd(Identifier.ofVanilla("block/quartz_pillar"), Identifier.ofVanilla("block/quartz_pillar_top"))), Blocks.QUARTZ_PILLAR);

  @MiningLevel(level = MiningLevel.Level.STONE)
  public static final ColoredCubeBlock COLORED_IRON_BLOCK = register("colored_iron_block", settings -> ColoredCubeBlock.cubeAll(settings, "block/iron_block"), Blocks.IRON_BLOCK);

  @MiningLevel(level = MiningLevel.Level.STONE)
  public static final ColoredStairsBlock COLORED_IRON_STAIRS = register("colored_iron_stairs", settings -> new ColoredStairsBlock(COLORED_IRON_BLOCK, settings), COLORED_IRON_BLOCK);

  @MiningLevel(level = MiningLevel.Level.STONE)
  public static final ColoredSlabBlock COLORED_IRON_SLAB = register("colored_iron_slab", settings -> new ColoredSlabBlock(COLORED_IRON_BLOCK, settings), COLORED_IRON_BLOCK);

  public static final ColoredCubeBlock COLORED_PURPUR_BLOCK = register("colored_purpur_block", settings -> ColoredCubeBlock.cubeAll(settings, "mishanguc:block/pale_purpur_block"), Blocks.PURPUR_BLOCK);
  public static final ColoredPillarBlock COLORED_PURPUR_PILLAR = register("colored_purpur_pillar", settings -> new ColoredPillarBlock(settings, TextureMap.sideEnd(Identifier.of("mishanguc:block/pale_purpur_pillar"), Identifier.of("mishanguc:block/pale_purpur_pillar_top"))), Blocks.PURPUR_PILLAR);

  public static final ColoredCubeBlock COLORED_END_STONE = register("colored_end_stone", settings -> ColoredCubeBlock.cubeAll(settings, "mishanguc:block/pale_end_stone"), Blocks.END_STONE);

  public static final ColoredStairsBlock COLORED_END_STONE_STAIRS = register("colored_end_stone_stairs", settings -> new ColoredStairsBlock(COLORED_END_STONE, settings), COLORED_END_STONE);

  public static final ColoredSlabBlock COLORED_END_STONE_SLAB = register("colored_end_stone_slab", settings -> new ColoredSlabBlock(COLORED_END_STONE, settings), COLORED_END_STONE);

  public static final ColoredCubeBlock COLORED_END_STONE_BRICKS = register("colored_end_stone_bricks", settings -> ColoredCubeBlock.cubeAll(settings, "mishanguc:block/pale_end_stone_bricks"), Blocks.END_STONE_BRICKS);

  public static final ColoredStairsBlock COLORED_END_STONE_BRICK_STAIRS = register("colored_end_stone_brick_stairs", settings -> new ColoredStairsBlock(COLORED_END_STONE_BRICKS, settings), Blocks.END_STONE_BRICK_STAIRS);

  public static final ColoredSlabBlock COLORED_END_STONE_BRICK_SLAB = register("colored_end_stone_brick_slab", settings -> new ColoredSlabBlock(COLORED_END_STONE_BRICKS, settings), Blocks.END_STONE_BRICK_SLAB);

  @Translucent
  @MiningLevel(MiningLevel.Tool.NONE)
  public static final ColoredNetherPortalBlock COLORED_NETHER_PORTAL = register("colored_nether_portal", ColoredNetherPortalBlock::new, Blocks.NETHER_PORTAL);

  @MiningLevel(MiningLevel.Tool.NONE)
  public static final ColoredCubeBlock COLORED_LIGHT = register("colored_light", settings -> new ColoredCubeBlock(settings, ColoredCubeBlock.COLORED_CUBE_ALL_WITHOUT_SHADE, TextureMap.all(Identifier.of("mishanguc:block/white_light"))), WHITE_LIGHT_SETTINGS);
  @Translucent
  @MiningLevel(MiningLevel.Tool.NONE)
  public static final ColoredGlassBlock COLORED_GLASS = register("colored_glass", settings -> new ColoredGlassBlock(settings, TextureMap.all(Identifier.ofVanilla("block/white_stained_glass"))), Blocks.WHITE_STAINED_GLASS);
  @Translucent

  @MiningLevel(MiningLevel.Tool.NONE)
  public static final ColoredGlassPaneBlock COLORED_GLASS_PANE = register("colored_glass_pane", settings -> new ColoredGlassPaneBlock(Identifier.ofVanilla("block/white_stained_glass"), Identifier.ofVanilla("block/white_stained_glass_pane_top"), settings), Blocks.WHITE_STAINED_GLASS_PANE);
  @Translucent
  public static final ColoredIceBlock COLORED_ICE = register("colored_ice", settings -> new ColoredIceBlock(settings, TextureMap.all(Identifier.of("mishanguc:block/pale_ice"))), Blocks.ICE);
  @MiningLevel(MiningLevel.Tool.SHOVEL)
  public static final ColoredCubeBlock COLORED_SNOW_BLOCK = register("colored_snow_block", settings -> ColoredCubeBlock.cubeAll(settings, "block/snow"), Blocks.SNOW_BLOCK);
  public static final ColoredCubeBlock COLORED_PACKED_ICE = register("colored_packed_ice", settings -> ColoredCubeBlock.cubeAll(settings, "mishanguc:block/pale_packed_ice"), Blocks.PACKED_ICE);


  @MiningLevel(MiningLevel.Tool.HOE)
  public static final ColoredLeavesBlock COLORED_OAK_LEAVES = register("colored_oak_leaves", settings -> new ColoredLeavesBlock(settings, (block, blockLootTableGenerator) -> blockLootTableGenerator.oakLeavesDrops(block, Blocks.OAK_SAPLING, 0.05F, 0.0625F, 0.083333336F, 0.1F), "block/oak_leaves"), Blocks.OAK_LEAVES);

  @MiningLevel(MiningLevel.Tool.HOE)
  public static final ColoredLeavesBlock COLORED_DARK_OAK_LEAVES = register("colored_dark_oak_leaves", settings -> new ColoredLeavesBlock(settings, (block, blockLootTableGenerator) -> blockLootTableGenerator.oakLeavesDrops(block, Blocks.DARK_OAK_SAPLING, 0.05F, 0.0625F, 0.083333336F, 0.1F), "block/dark_oak_leaves"), Blocks.DARK_OAK_LEAVES);

  @MiningLevel(MiningLevel.Tool.HOE)
  public static final ColoredLeavesBlock COLORED_PALE_OAK_LEAVES = register("colored_pale_oak_leaves", settings -> new ColoredLeavesBlock(settings, (block, blockLootTableGenerator) -> blockLootTableGenerator.oakLeavesDrops(block, Blocks.PALE_OAK_SAPLING, 0.05F, 0.0625F, 0.083333336F, 0.1F), "block/pale_oak_leaves"), Blocks.PALE_OAK_LEAVES);

  @MiningLevel(MiningLevel.Tool.HOE)
  public static final ColoredLeavesBlock COLORED_BIRCH_LEAVES = register("colored_birch_leaves", settings -> new ColoredLeavesBlock(settings, (block, blockLootTableGenerator) -> blockLootTableGenerator.leavesDrops(block, Blocks.BIRCH_SAPLING, 0.05F, 0.0625F, 0.083333336F, 0.1F), "block/birch_leaves"), Blocks.BIRCH_LEAVES);

  @MiningLevel(MiningLevel.Tool.HOE)
  public static final ColoredLeavesBlock COLORED_ACACIA_LEAVES = register("colored_acacia_leaves", settings -> new ColoredLeavesBlock(settings, (block, blockLootTableGenerator) -> blockLootTableGenerator.leavesDrops(block, Blocks.ACACIA_SAPLING, 0.05F, 0.0625F, 0.083333336F, 0.1F), "block/acacia_leaves"), Blocks.ACACIA_LEAVES);

  @MiningLevel(MiningLevel.Tool.HOE)
  public static final ColoredLeavesBlock COLORED_CHERRY_LEAVES = register("colored_cherry_leaves", settings -> new ColoredLeavesBlock(settings, (block, blockLootTableGenerator) -> blockLootTableGenerator.leavesDrops(block, Blocks.CHERRY_SAPLING, 0.05F, 0.0625F, 0.083333336F, 0.1F), "mishanguc:block/pale_cherry_leaves"), Blocks.CHERRY_LEAVES);

  @MiningLevel(MiningLevel.Tool.HOE)
  public static final ColoredLeavesBlock COLORED_JUNGLE_LEAVES = register("colored_jungle_leaves", settings -> new ColoredLeavesBlock(settings, (block, blockLootTableGenerator) -> blockLootTableGenerator.leavesDrops(block, Blocks.JUNGLE_SAPLING, 0.025F, 0.027777778F, 0.03125F, 0.041666668F, 0.1F), "block/jungle_leaves"), Blocks.JUNGLE_LEAVES);

  @MiningLevel(MiningLevel.Tool.HOE)
  public static final ColoredLeavesBlock COLORED_SPRUCE_LEAVES = register("colored_spruce_leaves", settings -> new ColoredLeavesBlock(settings, (block, blockLootTableGenerator) -> blockLootTableGenerator.leavesDrops(block, Blocks.SPRUCE_SAPLING, 0.05F, 0.0625F, 0.083333336F, 0.1F), "block/spruce_leaves"), Blocks.SPRUCE_LEAVES);

  @MiningLevel(MiningLevel.Tool.HOE)
  public static final ColoredLeavesBlock COLORED_MANGROVE_LEAVES = register("colored_mangrove_leaves", settings -> new ColoredLeavesBlock(settings, (leaves, blockLootTableGenerator) -> blockLootTableGenerator.mangroveLeavesDrops(leaves), "block/mangrove_leaves"), Blocks.MANGROVE_LEAVES);

  @MiningLevel(MiningLevel.Tool.NONE)
  public static final ColoredCubeBlock COLORED_PURE_BLOCK = register("colored_pure_block", settings -> ColoredCubeBlock.cubeAll(settings.mapColor(DyeColor.WHITE).strength(0.2f), "mishanguc:block/white_pure"));
  @MiningLevel(MiningLevel.Tool.NONE)
  public static final ColoredCubeBlock COLORED_PURE_LIGHT = register("colored_pure_light", settings -> ColoredCubeBlock.cubeAll(settings, "mishanguc:block/white_pure"), WHITE_LIGHT_SETTINGS);
  @MiningLevel(MiningLevel.Tool.NONE)
  public static final ColoredCubeBlock COLORED_PURE_BLOCK_WITHOUT_SHADE = register("colored_pure_block_without_shade", settings -> new ColoredCubeBlock(settings.mapColor(DyeColor.WHITE).strength(0.2f), ColoredCubeBlock.COLORED_CUBE_ALL_WITHOUT_SHADE, TextureMap.all(Identifier.of("mishanguc:block/white_pure"))), COLORED_PURE_BLOCK);
  @MiningLevel(MiningLevel.Tool.NONE)
  public static final ColoredCubeBlock COLORED_PURE_LIGHT_WITHOUT_SHADE = register("colored_pure_light_without_shade", settings -> new ColoredCubeBlock(settings, ColoredCubeBlock.COLORED_CUBE_ALL_WITHOUT_SHADE, TextureMap.all(Identifier.of("mishanguc:block/white_pure"))), WHITE_LIGHT_SETTINGS);

  private ColoredBlocks() {
  }
}
