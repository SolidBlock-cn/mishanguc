package pers.solid.mishang.uc.blocks;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.data.client.BlockStateModelGenerator;
import net.minecraft.data.client.BlockStateSupplier;
import net.minecraft.data.client.TextureMap;
import net.minecraft.loot.LootTable;
import net.minecraft.util.DyeColor;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import pers.solid.brrp.v1.api.RuntimeResourcePack;
import pers.solid.brrp.v1.model.ModelJsonBuilder;
import pers.solid.mishang.uc.Mishanguc;
import pers.solid.mishang.uc.annotations.Translucent;
import pers.solid.mishang.uc.arrp.ARRPMain;
import pers.solid.mishang.uc.block.*;

/**
 * 此类包含迷上城建模组的所有染色方块。注意：这些方块通常需要适当配置好 mineable 标签（1.16.5 及以下版本，只需要在 FabricBlockSetting 中设置）以配置可破坏的方块。此外，还需要配置好 {@link Mishanguc#registerColoredBlocks()} 以配置方块与方块之间的对应关系。
 *
 * @see ColoredBlock
 * @see pers.solid.mishang.uc.blockentity.ColoredBlockEntity
 */
@ApiStatus.AvailableSince("0.2.2")
public final class ColoredBlocks extends MishangucBlocks {
  public static final ColoredCubeBlock COLORED_WOOL = ColoredCubeBlock.cubeAll(Block.Settings.copy(Blocks.WHITE_WOOL), "block/white_wool");
  public static final ColoredCubeBlock COLORED_TERRACOTTA = ColoredCubeBlock.cubeAll(Block.Settings.copy(Blocks.WHITE_TERRACOTTA), "block/white_terracotta");
  @ApiStatus.AvailableSince("0.2.4")
  public static final ColoredStairsBlock COLORED_TERRACOTTA_STAIRS = new ColoredStairsBlock(COLORED_TERRACOTTA);
  @ApiStatus.AvailableSince("0.2.4")
  public static final ColoredSlabBlock COLORED_TERRACOTTA_SLAB = new ColoredSlabBlock(COLORED_TERRACOTTA);
  public static final ColoredCubeBlock COLORED_CONCRETE = ColoredCubeBlock.cubeAll(Block.Settings.copy(Blocks.WHITE_CONCRETE), "block/white_concrete");
  @ApiStatus.AvailableSince("0.2.4")
  public static final ColoredStairsBlock COLORED_CONCRETE_STAIRS = new ColoredStairsBlock(COLORED_CONCRETE);
  @ApiStatus.AvailableSince("0.2.4")
  public static final ColoredSlabBlock COLORED_CONCRETE_SLAB = new ColoredSlabBlock(COLORED_CONCRETE);
  public static final ColoredCubeBlock COLORED_PLANKS = ColoredCubeBlock.cubeAll(Block.Settings.copy(Blocks.BIRCH_PLANKS), "mishanguc:block/pale_planks");
  public static final ColoredStairsBlock COLORED_PLANK_STAIRS = new ColoredStairsBlock(COLORED_PLANKS, Block.Settings.copy(Blocks.BIRCH_STAIRS));
  public static final ColoredSlabBlock COLORED_PLANK_SLAB = new ColoredSlabBlock(COLORED_PLANKS, Block.Settings.copy(Blocks.BIRCH_SLAB));
  public static final ColoredCubeBlock COLORED_DIRT = ColoredCubeBlock.cubeAll(Block.Settings.copy(Blocks.DIRT), "mishanguc:block/pale_dirt");
  public static final ColoredCubeBlock COLORED_COBBLESTONE = ColoredCubeBlock.cubeAll(Block.Settings.copy(Blocks.COBBLESTONE), "mishanguc:block/pale_cobblestone");
  public static final ColoredStairsBlock COLORED_COBBLESTONE_STAIRS = new ColoredStairsBlock(COLORED_COBBLESTONE, Block.Settings.copy(Blocks.COBBLESTONE_STAIRS));
  public static final ColoredSlabBlock COLORED_COBBLESTONE_SLAB = new ColoredSlabBlock(COLORED_COBBLESTONE, Block.Settings.copy(Blocks.COBBLESTONE_SLAB));
  @ApiStatus.AvailableSince("0.2.4")
  public static final ColoredCubeBlock COLORED_ANDESITE = ColoredCubeBlock.cubeAll(Block.Settings.copy(Blocks.ANDESITE), "block/andesite");
  @ApiStatus.AvailableSince("0.2.4")
  public static final ColoredStairsBlock COLORED_ANDESITE_STAIRS = new ColoredStairsBlock(COLORED_ANDESITE, Block.Settings.copy(Blocks.ANDESITE_STAIRS));
  @ApiStatus.AvailableSince("0.2.4")
  public static final ColoredSlabBlock COLORED_ANDESITE_SLAB = new ColoredSlabBlock(COLORED_ANDESITE, Block.Settings.copy(Blocks.ANDESITE_SLAB));
  @ApiStatus.AvailableSince("0.2.4")
  public static final ColoredCubeBlock COLORED_DIORITE = ColoredCubeBlock.cubeAll(Block.Settings.copy(Blocks.DIORITE), "block/diorite");
  @ApiStatus.AvailableSince("0.2.4")
  public static final ColoredStairsBlock COLORED_DIORITE_STAIRS = new ColoredStairsBlock(COLORED_DIORITE, Block.Settings.copy(Blocks.DIORITE_STAIRS));
  @ApiStatus.AvailableSince("0.2.4")
  public static final ColoredSlabBlock COLORED_DIORITE_SLAB = new ColoredSlabBlock(COLORED_DIORITE, Block.Settings.copy(Blocks.DIORITE_SLAB));
  @ApiStatus.AvailableSince("0.2.4, mc1.17")
  public static final ColoredCubeBlock COLORED_CALCITE = ColoredCubeBlock.cubeAll(Block.Settings.copy(Blocks.CALCITE), "block/calcite");
  @ApiStatus.AvailableSince("0.2.4, mc1.17")
  public static final ColoredStairsBlock COLORED_CALCITE_STAIRS = new ColoredStairsBlock(COLORED_CALCITE);
  @ApiStatus.AvailableSince("0.2.4, mc1.17")
  public static final ColoredSlabBlock COLORED_CALCITE_SLAB = new ColoredSlabBlock(COLORED_CALCITE);
  @ApiStatus.AvailableSince("0.2.4, mc1.17")
  public static final ColoredCubeBlock COLORED_TUFF = ColoredCubeBlock.cubeAll(Block.Settings.copy(Blocks.TUFF), "block/tuff");
  @ApiStatus.AvailableSince("0.2.4, mc1.17")
  public static final ColoredStairsBlock COLORED_TUFF_STAIRS = new ColoredStairsBlock(COLORED_TUFF);
  @ApiStatus.AvailableSince("0.2.4, mc1.17")
  public static final ColoredSlabBlock COLORED_TUFF_SLAB = new ColoredSlabBlock(COLORED_TUFF);

  public static final ColoredCubeBlock COLORED_STONE = new ColoredCubeBlock(Block.Settings.copy(Blocks.STONE), Identifier.of("mishanguc:block/colored_cube_all"), TextureMap.all(Identifier.of("mishanguc:block/pale_stone"))) {
    @Environment(EnvType.CLIENT)
    @Override
    public void writeBlockModel(RuntimeResourcePack pack) {
      final ModelJsonBuilder model = getBlockModel();
      final Identifier blockModelId = getBlockModelId();
      pack.addModel(blockModelId, model);
      pack.addModel(blockModelId.brrp_suffixed("_mirrored"), model.withParent(Identifier.of("mishanguc:block/colored_cube_mirrored_all")));
    }

    @Environment(EnvType.CLIENT)
    @Override
    public @NotNull BlockStateSupplier getBlockStates() {
      final Identifier blockModelId = getBlockModelId();
      return BlockStateModelGenerator.createBlockStateWithTwoModelAndRandomInversion(this, blockModelId, blockModelId.brrp_suffixed("_mirrored"));
    }

    @Override
    public LootTable.@NotNull Builder getLootTable() {
      return ARRPMain.LOOT_TABLE_GENERATOR.drops(COLORED_STONE, COLORED_COBBLESTONE).apply(ColoredBlock.COPY_COLOR_LOOT_FUNCTION);
    }
  };
  public static final ColoredStairsBlock COLORED_STONE_STAIRS = new ColoredStairsBlock(COLORED_STONE, Block.Settings.copy(Blocks.STONE_STAIRS));
  public static final ColoredSlabBlock COLORED_STONE_SLAB = new ColoredSlabBlock(COLORED_STONE, Block.Settings.copy(Blocks.STONE_SLAB));
  @ApiStatus.AvailableSince("0.2.4")
  public static final ColoredCubeBlock COLORED_STONE_BRICKS = ColoredCubeBlock.cubeAll(Block.Settings.copy(Blocks.STONE_BRICKS), "mishanguc:block/pale_stone_bricks");
  @ApiStatus.AvailableSince("0.2.4")
  public static final ColoredStairsBlock COLORED_STONE_BRICK_STAIRS = new ColoredStairsBlock(COLORED_STONE_BRICKS);
  @ApiStatus.AvailableSince("0.2.4")
  public static final ColoredSlabBlock COLORED_STONE_BRICK_SLAB = new ColoredSlabBlock(COLORED_STONE_BRICKS);
  @ApiStatus.AvailableSince("0.2.4")
  public static final ColoredCubeBlock COLORED_BRICKS = ColoredCubeBlock.cubeAll(Block.Settings.copy(Blocks.BRICKS), "mishanguc:block/pale_bricks");
  @ApiStatus.AvailableSince("0.2.4")
  public static final ColoredStairsBlock COLORED_BRICK_STAIRS = new ColoredStairsBlock(COLORED_BRICKS);
  @ApiStatus.AvailableSince("0.2.4")
  public static final ColoredSlabBlock COLORED_BRICK_SLAB = new ColoredSlabBlock(COLORED_BRICKS);

  public static final ColoredCubeBlock COLORED_QUARTZ_BLOCK = ColoredCubeBlock.cubeBottomTop(Block.Settings.copy(Blocks.QUARTZ_BLOCK), "block/quartz_block_top", "block/quartz_block_side", "block/quartz_block_top");
  @ApiStatus.AvailableSince("0.2.4")
  public static final ColoredStairsBlock COLORED_QUARTZ_STAIRS = new ColoredStairsBlock(COLORED_QUARTZ_BLOCK);
  @ApiStatus.AvailableSince("0.2.4")
  public static final ColoredSlabBlock COLORED_QUARTZ_SLAB = new ColoredSlabBlock(COLORED_QUARTZ_BLOCK);

  public static final ColoredCubeBlock COLORED_CHISELED_QUARTZ_BLOCK = ColoredCubeBlock.cubeBottomTop(Block.Settings.copy(Blocks.CHISELED_QUARTZ_BLOCK), "block/chiseled_quartz_block_top", "block/chiseled_quartz_block", "block/chiseled_quartz_block_top");

  public static final ColoredCubeBlock COLORED_QUARTZ_BRICKS = ColoredCubeBlock.cubeAll(Block.Settings.copy(Blocks.QUARTZ_BRICKS), "block/quartz_bricks");
  public static final ColoredCubeBlock COLORED_SMOOTH_QUARTZ = ColoredCubeBlock.cubeAll(Block.Settings.copy(Blocks.SMOOTH_QUARTZ), "block/quartz_block_bottom");
  public static final ColoredStairsBlock COLORED_SMOOTH_QUARTZ_STAIRS = new ColoredStairsBlock(COLORED_SMOOTH_QUARTZ);
  public static final ColoredSlabBlock COLORED_SMOOTH_QUARTZ_SLAB = new ColoredSlabBlock(COLORED_SMOOTH_QUARTZ);
  public static final ColoredPillarBlock COLORED_QUARTZ_PILLAR = new ColoredPillarBlock(Block.Settings.copy(Blocks.QUARTZ_PILLAR), TextureMap.sideEnd(Identifier.ofVanilla("block/quartz_pillar"), Identifier.ofVanilla("block/quartz_pillar_top")));
  @ApiStatus.AvailableSince("0.2.4")
  public static final ColoredCubeBlock COLORED_IRON_BLOCK = ColoredCubeBlock.cubeAll(Block.Settings.copy(Blocks.IRON_BLOCK), "block/iron_block");
  @ApiStatus.AvailableSince("0.2.4")
  public static final ColoredStairsBlock COLORED_IRON_STAIRS = new ColoredStairsBlock(COLORED_IRON_BLOCK);
  @ApiStatus.AvailableSince("0.2.4")
  public static final ColoredSlabBlock COLORED_IRON_SLAB = new ColoredSlabBlock(COLORED_IRON_BLOCK);
  public static final ColoredCubeBlock COLORED_PURPUR_BLOCK = ColoredCubeBlock.cubeAll(Block.Settings.copy(Blocks.PURPUR_BLOCK), "mishanguc:block/pale_purpur_block");
  public static final ColoredPillarBlock COLORED_PURPUR_PILLAR = new ColoredPillarBlock(Block.Settings.copy(Blocks.PURPUR_PILLAR), TextureMap.sideEnd(Identifier.of("mishanguc:block/pale_purpur_pillar"), Identifier.of("mishanguc:block/pale_purpur_pillar_top")));
  @ApiStatus.AvailableSince("1.0.2")
  public static final ColoredCubeBlock COLORED_END_STONE = ColoredCubeBlock.cubeAll(Block.Settings.copy(Blocks.END_STONE), "mishanguc:block/pale_end_stone");
  @ApiStatus.AvailableSince("1.0.2")
  public static final ColoredStairsBlock COLORED_END_STONE_STAIRS = new ColoredStairsBlock(COLORED_END_STONE);
  @ApiStatus.AvailableSince("1.0.2")
  public static final ColoredSlabBlock COLORED_END_STONE_SLAB = new ColoredSlabBlock(COLORED_END_STONE);
  @ApiStatus.AvailableSince("1.0.2")
  public static final ColoredCubeBlock COLORED_END_STONE_BRICKS = ColoredCubeBlock.cubeAll(Block.Settings.copy(Blocks.END_STONE_BRICKS), "mishanguc:block/pale_end_stone_bricks");
  @ApiStatus.AvailableSince("1.0.2")
  public static final ColoredStairsBlock COLORED_END_STONE_BRICK_STAIRS = new ColoredStairsBlock(COLORED_END_STONE_BRICKS);
  @ApiStatus.AvailableSince("1.0.2")
  public static final ColoredSlabBlock COLORED_END_STONE_BRICK_SLAB = new ColoredSlabBlock(COLORED_END_STONE_BRICKS);
  @ApiStatus.AvailableSince("1.0.2")
  @Translucent
  public static final ColoredNetherPortalBlock COLORED_NETHER_PORTAL = new ColoredNetherPortalBlock(Block.Settings.copy(Blocks.NETHER_PORTAL));

  public static final ColoredCubeBlock COLORED_LIGHT = new ColoredCubeBlock(WHITE_LIGHT_SETTINGS, Identifier.of("mishanguc:block/colored_cube_all_without_shade"), TextureMap.all(Identifier.of("mishanguc:block/white_light")));
  @Translucent
  public static final ColoredGlassBlock COLORED_GLASS = new ColoredGlassBlock(Block.Settings.copy(Blocks.WHITE_STAINED_GLASS), TextureMap.all(Identifier.ofVanilla("block/white_stained_glass")));
  @Translucent
  @ApiStatus.AvailableSince("1.2.3")
  public static final ColoredGlassPaneBlock COLORED_GLASS_PANE = new ColoredGlassPaneBlock(Identifier.ofVanilla("block/white_stained_glass"), Identifier.ofVanilla("block/white_stained_glass_pane_top"), Block.Settings.copy(Blocks.WHITE_STAINED_GLASS_PANE));
  @Translucent
  public static final ColoredIceBlock COLORED_ICE = new ColoredIceBlock(Block.Settings.copy(Blocks.ICE), TextureMap.all(Identifier.of("mishanguc:block/pale_ice")));
  public static final ColoredCubeBlock COLORED_SNOW_BLOCK = ColoredCubeBlock.cubeAll(Block.Settings.copy(Blocks.SNOW_BLOCK), "block/snow");
  public static final ColoredCubeBlock COLORED_PACKED_ICE = ColoredCubeBlock.cubeAll(Block.Settings.copy(Blocks.PACKED_ICE), "mishanguc:block/pale_packed_ice");

  @ApiStatus.AvailableSince("0.2.4")
  public static final ColoredLeavesBlock COLORED_OAK_LEAVES = new ColoredLeavesBlock(Block.Settings.copy(Blocks.OAK_LEAVES), block -> ARRPMain.LOOT_TABLE_GENERATOR.oakLeavesDrops(block, Blocks.OAK_SAPLING, 0.05F, 0.0625F, 0.083333336F, 0.1F), "block/oak_leaves");
  @ApiStatus.AvailableSince("0.2.4")
  public static final ColoredLeavesBlock COLORED_DARK_OAK_LEAVES = new ColoredLeavesBlock(Block.Settings.copy(Blocks.DARK_OAK_LEAVES), block -> ARRPMain.LOOT_TABLE_GENERATOR.oakLeavesDrops(block, Blocks.DARK_OAK_SAPLING, 0.05F, 0.0625F, 0.083333336F, 0.1F), "block/dark_oak_leaves");
  @ApiStatus.AvailableSince("0.2.4")
  public static final ColoredLeavesBlock COLORED_BIRCH_LEAVES = new ColoredLeavesBlock(Block.Settings.copy(Blocks.BIRCH_LEAVES), block -> ARRPMain.LOOT_TABLE_GENERATOR.leavesDrops(block, Blocks.BIRCH_SAPLING, 0.05F, 0.0625F, 0.083333336F, 0.1F), "block/birch_leaves");
  @ApiStatus.AvailableSince("0.2.4")
  public static final ColoredLeavesBlock COLORED_ACACIA_LEAVES = new ColoredLeavesBlock(Block.Settings.copy(Blocks.ACACIA_LEAVES), block -> ARRPMain.LOOT_TABLE_GENERATOR.leavesDrops(block, Blocks.ACACIA_SAPLING, 0.05F, 0.0625F, 0.083333336F, 0.1F), "block/acacia_leaves");
  @ApiStatus.AvailableSince("1.1.1-mc1.19.4")
  public static final ColoredLeavesBlock COLORED_CHERRY_LEAVES = new ColoredLeavesBlock(Block.Settings.copy(Blocks.CHERRY_LEAVES), block -> ARRPMain.LOOT_TABLE_GENERATOR.leavesDrops(block, Blocks.CHERRY_SAPLING, 0.05F, 0.0625F, 0.083333336F, 0.1F), "mishanguc:block/pale_cherry_leaves");
  @ApiStatus.AvailableSince("0.2.4")
  public static final ColoredLeavesBlock COLORED_JUNGLE_LEAVES = new ColoredLeavesBlock(Block.Settings.copy(Blocks.JUNGLE_LEAVES), block -> ARRPMain.LOOT_TABLE_GENERATOR.leavesDrops(block, Blocks.JUNGLE_SAPLING, 0.025F, 0.027777778F, 0.03125F, 0.041666668F, 0.1F), "block/jungle_leaves");
  @ApiStatus.AvailableSince("0.2.4")
  public static final ColoredLeavesBlock COLORED_SPRUCE_LEAVES = new ColoredLeavesBlock(Block.Settings.copy(Blocks.SPRUCE_LEAVES), block -> ARRPMain.LOOT_TABLE_GENERATOR.leavesDrops(block, Blocks.SPRUCE_SAPLING, 0.05F, 0.0625F, 0.083333336F, 0.1F), "block/spruce_leaves");
  @ApiStatus.AvailableSince("0.2.4")
  public static final ColoredLeavesBlock COLORED_MANGROVE_LEAVES = new ColoredLeavesBlock(Block.Settings.copy(Blocks.MANGROVE_LEAVES), leaves -> /* do not replace it with method references, as we should not initialize the class too early! */ ARRPMain.LOOT_TABLE_GENERATOR.mangroveLeavesDrops(leaves), "block/mangrove_leaves");

  public static final ColoredCubeBlock COLORED_PURE_BLOCK = ColoredCubeBlock.cubeAll(AbstractBlock.Settings.create().mapColor(DyeColor.WHITE).strength(0.2f), "mishanguc:block/white_pure");
  public static final ColoredCubeBlock COLORED_PURE_LIGHT = ColoredCubeBlock.cubeAll(WHITE_LIGHT_SETTINGS, "mishanguc:block/white_pure");
  public static final ColoredCubeBlock COLORED_PURE_BLOCK_WITHOUT_SHADE = new ColoredCubeBlock(AbstractBlock.Settings.create().mapColor(DyeColor.WHITE).strength(0.2f), Identifier.of("mishanguc:block/colored_cube_all_without_shade"), TextureMap.all(Identifier.of("mishanguc:block/white_pure")));
  public static final ColoredCubeBlock COLORED_PURE_LIGHT_WITHOUT_SHADE = new ColoredCubeBlock(WHITE_LIGHT_SETTINGS, Identifier.of("mishanguc:block/colored_cube_all_without_shade"), TextureMap.all(Identifier.of("mishanguc:block/white_pure")));

  private ColoredBlocks() {
  }
}
