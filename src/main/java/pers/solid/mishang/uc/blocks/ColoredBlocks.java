package pers.solid.mishang.uc.blocks;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.fabricmc.fabric.api.tool.attribute.v1.FabricToolTags;
import net.minecraft.block.Blocks;
import net.minecraft.block.Material;
import net.minecraft.data.client.model.BlockStateModelGenerator;
import net.minecraft.data.client.model.BlockStateSupplier;
import net.minecraft.data.client.model.Texture;
import net.minecraft.data.server.BlockLootTableGenerator;
import net.minecraft.loot.LootTable;
import net.minecraft.util.DyeColor;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import pers.solid.brrp.v1.api.RuntimeResourcePack;
import pers.solid.brrp.v1.model.ModelJsonBuilder;
import pers.solid.mishang.uc.Mishanguc;
import pers.solid.mishang.uc.annotations.Translucent;
import pers.solid.mishang.uc.block.*;

/**
 * 此类包含迷上城建模组的所有染色方块。注意：这些方块通常需要适当配置好 mineable 标签（1.16.5 及以下版本，只需要在 FabricBlockSetting 中设置）以配置可破坏的方块。此外，还需要配置好 {@link Mishanguc#registerColoredBlocks()} 以配置方块与方块之间的对应关系。
 *
 * @see ColoredBlock
 * @see pers.solid.mishang.uc.blockentity.ColoredBlockEntity
 */
@ApiStatus.AvailableSince("0.2.2")
public final class ColoredBlocks extends MishangucBlocks {
  public static final ColoredCubeBlock COLORED_WOOL = ColoredCubeBlock.cubeAll(FabricBlockSettings.copyOf(Blocks.WHITE_WOOL).breakByTool(FabricToolTags.SHEARS), "block/white_wool");
  public static final ColoredCubeBlock COLORED_TERRACOTTA = ColoredCubeBlock.cubeAll(FabricBlockSettings.copyOf(Blocks.WHITE_TERRACOTTA), "block/white_terracotta");
  @ApiStatus.AvailableSince("0.2.4")
  public static final ColoredStairsBlock COLORED_TERRACOTTA_STAIRS = new ColoredStairsBlock(COLORED_TERRACOTTA);
  @ApiStatus.AvailableSince("0.2.4")
  public static final ColoredSlabBlock COLORED_TERRACOTTA_SLAB = new ColoredSlabBlock(COLORED_TERRACOTTA);
  public static final ColoredCubeBlock COLORED_CONCRETE = ColoredCubeBlock.cubeAll(FabricBlockSettings.copyOf(Blocks.WHITE_CONCRETE), "block/white_concrete");
  @ApiStatus.AvailableSince("0.2.4")
  public static final ColoredStairsBlock COLORED_CONCRETE_STAIRS = new ColoredStairsBlock(COLORED_CONCRETE);
  @ApiStatus.AvailableSince("0.2.4")
  public static final ColoredSlabBlock COLORED_CONCRETE_SLAB = new ColoredSlabBlock(COLORED_CONCRETE);
  public static final ColoredCubeBlock COLORED_PLANKS = ColoredCubeBlock.cubeAll(FabricBlockSettings.copyOf(Blocks.BIRCH_PLANKS).breakByTool(FabricToolTags.AXES), "mishanguc:block/pale_planks");
  public static final ColoredStairsBlock COLORED_PLANK_STAIRS = new ColoredStairsBlock(COLORED_PLANKS, FabricBlockSettings.copyOf(Blocks.BIRCH_STAIRS).breakByTool(FabricToolTags.AXES));
  public static final ColoredSlabBlock COLORED_PLANK_SLAB = new ColoredSlabBlock(COLORED_PLANKS, FabricBlockSettings.copyOf(Blocks.BIRCH_SLAB).breakByTool(FabricToolTags.AXES));
  public static final ColoredCubeBlock COLORED_DIRT = ColoredCubeBlock.cubeAll(FabricBlockSettings.copyOf(Blocks.DIRT).breakByTool(FabricToolTags.SHOVELS), "mishanguc:block/pale_dirt");
  public static final ColoredCubeBlock COLORED_COBBLESTONE = ColoredCubeBlock.cubeAll(FabricBlockSettings.copyOf(Blocks.COBBLESTONE), "mishanguc:block/pale_cobblestone");
  public static final ColoredStairsBlock COLORED_COBBLESTONE_STAIRS = new ColoredStairsBlock(COLORED_COBBLESTONE, FabricBlockSettings.copyOf(Blocks.COBBLESTONE_STAIRS));
  public static final ColoredSlabBlock COLORED_COBBLESTONE_SLAB = new ColoredSlabBlock(COLORED_COBBLESTONE, FabricBlockSettings.copyOf(Blocks.COBBLESTONE_SLAB));
  @ApiStatus.AvailableSince("0.2.4")
  public static final ColoredCubeBlock COLORED_ANDESITE = ColoredCubeBlock.cubeAll(FabricBlockSettings.copyOf(Blocks.ANDESITE), "block/andesite");
  @ApiStatus.AvailableSince("0.2.4")
  public static final ColoredStairsBlock COLORED_ANDESITE_STAIRS = new ColoredStairsBlock(COLORED_ANDESITE, FabricBlockSettings.copyOf(Blocks.ANDESITE_STAIRS));
  @ApiStatus.AvailableSince("0.2.4")
  public static final ColoredSlabBlock COLORED_ANDESITE_SLAB = new ColoredSlabBlock(COLORED_ANDESITE, FabricBlockSettings.copyOf(Blocks.ANDESITE_SLAB));
  @ApiStatus.AvailableSince("0.2.4")
  public static final ColoredCubeBlock COLORED_DIORITE = ColoredCubeBlock.cubeAll(FabricBlockSettings.copyOf(Blocks.DIORITE), "block/diorite");
  @ApiStatus.AvailableSince("0.2.4")
  public static final ColoredStairsBlock COLORED_DIORITE_STAIRS = new ColoredStairsBlock(COLORED_DIORITE, FabricBlockSettings.copyOf(Blocks.DIORITE_STAIRS));
  @ApiStatus.AvailableSince("0.2.4")
  public static final ColoredSlabBlock COLORED_DIORITE_SLAB = new ColoredSlabBlock(COLORED_DIORITE, FabricBlockSettings.copyOf(Blocks.DIORITE_SLAB));

  public static final ColoredCubeBlock COLORED_STONE = new ColoredCubeBlock(FabricBlockSettings.copyOf(Blocks.STONE), new Identifier("mishanguc:block/colored_cube_all"), Texture.all(new Identifier("mishanguc:block/pale_stone"))) {
    @Environment(EnvType.CLIENT)
    @Override
    public void writeBlockModel(RuntimeResourcePack pack) {
      final ModelJsonBuilder model = getBlockModel();
      final Identifier blockModelId = getBlockModelId();
      pack.addModel(blockModelId, model);
      pack.addModel(blockModelId.brrp_suffixed("_mirrored"), model.withParent(new Identifier("mishanguc:block/colored_cube_mirrored_all")));
    }

    @Environment(EnvType.CLIENT)
    @Override
    public @NotNull BlockStateSupplier getBlockStates() {
      final Identifier blockModelId = getBlockModelId();
      return BlockStateModelGenerator.createBlockStateWithTwoModelAndRandomInversion(this, blockModelId, blockModelId.brrp_suffixed("_mirrored"));
    }

    @Override
    public LootTable.@NotNull Builder getLootTable() {
      return BlockLootTableGenerator.drops(COLORED_STONE, COLORED_COBBLESTONE).apply(ColoredBlock.COPY_COLOR_LOOT_FUNCTION);
    }
  };
  public static final ColoredStairsBlock COLORED_STONE_STAIRS = new ColoredStairsBlock(COLORED_STONE, FabricBlockSettings.copyOf(Blocks.STONE_STAIRS));
  public static final ColoredSlabBlock COLORED_STONE_SLAB = new ColoredSlabBlock(COLORED_STONE, FabricBlockSettings.copyOf(Blocks.STONE_SLAB));
  @ApiStatus.AvailableSince("0.2.4")
  public static final ColoredCubeBlock COLORED_STONE_BRICKS = ColoredCubeBlock.cubeAll(FabricBlockSettings.copyOf(Blocks.STONE_BRICKS), "mishanguc:block/pale_stone_bricks");
  @ApiStatus.AvailableSince("0.2.4")
  public static final ColoredStairsBlock COLORED_STONE_BRICK_STAIRS = new ColoredStairsBlock(COLORED_STONE_BRICKS);
  @ApiStatus.AvailableSince("0.2.4")
  public static final ColoredSlabBlock COLORED_STONE_BRICK_SLAB = new ColoredSlabBlock(COLORED_STONE_BRICKS);
  @ApiStatus.AvailableSince("0.2.4")
  public static final ColoredCubeBlock COLORED_BRICKS = ColoredCubeBlock.cubeAll(FabricBlockSettings.copyOf(Blocks.BRICKS), "mishanguc:block/pale_bricks");
  @ApiStatus.AvailableSince("0.2.4")
  public static final ColoredStairsBlock COLORED_BRICK_STAIRS = new ColoredStairsBlock(COLORED_BRICKS);
  @ApiStatus.AvailableSince("0.2.4")
  public static final ColoredSlabBlock COLORED_BRICK_SLAB = new ColoredSlabBlock(COLORED_BRICKS);

  public static final ColoredCubeBlock COLORED_QUARTZ_BLOCK = ColoredCubeBlock.cubeBottomTop(FabricBlockSettings.copyOf(Blocks.QUARTZ_BLOCK), "block/quartz_block_top", "block/quartz_block_side", "block/quartz_block_top");
  @ApiStatus.AvailableSince("0.2.4")
  public static final ColoredStairsBlock COLORED_QUARTZ_STAIRS = new ColoredStairsBlock(COLORED_QUARTZ_BLOCK);
  @ApiStatus.AvailableSince("0.2.4")
  public static final ColoredSlabBlock COLORED_QUARTZ_SLAB = new ColoredSlabBlock(COLORED_QUARTZ_BLOCK);

  public static final ColoredCubeBlock COLORED_CHISELED_QUARTZ_BLOCK = ColoredCubeBlock.cubeBottomTop(FabricBlockSettings.copyOf(Blocks.CHISELED_QUARTZ_BLOCK), "block/chiseled_quartz_block_top", "block/chiseled_quartz_block", "block/chiseled_quartz_block_top");

  public static final ColoredCubeBlock COLORED_QUARTZ_BRICKS = ColoredCubeBlock.cubeAll(FabricBlockSettings.copyOf(Blocks.QUARTZ_BRICKS), "block/quartz_bricks");
  public static final ColoredCubeBlock COLORED_SMOOTH_QUARTZ = ColoredCubeBlock.cubeAll(FabricBlockSettings.copyOf(Blocks.SMOOTH_QUARTZ), "block/quartz_block_bottom");
  public static final ColoredStairsBlock COLORED_SMOOTH_QUARTZ_STAIRS = new ColoredStairsBlock(COLORED_SMOOTH_QUARTZ);
  public static final ColoredSlabBlock COLORED_SMOOTH_QUARTZ_SLAB = new ColoredSlabBlock(COLORED_SMOOTH_QUARTZ);
  public static final ColoredPillarBlock COLORED_QUARTZ_PILLAR = new ColoredPillarBlock(FabricBlockSettings.copyOf(Blocks.QUARTZ_PILLAR), Texture.sideEnd(new Identifier("block/quartz_pillar"), new Identifier("block/quartz_pillar_top")));
  @ApiStatus.AvailableSince("0.2.4")
  public static final ColoredCubeBlock COLORED_IRON_BLOCK = ColoredCubeBlock.cubeAll(FabricBlockSettings.copyOf(Blocks.IRON_BLOCK), "block/iron_block");
  @ApiStatus.AvailableSince("0.2.4")
  public static final ColoredStairsBlock COLORED_IRON_STAIRS = new ColoredStairsBlock(COLORED_IRON_BLOCK);
  @ApiStatus.AvailableSince("0.2.4")
  public static final ColoredSlabBlock COLORED_IRON_SLAB = new ColoredSlabBlock(COLORED_IRON_BLOCK);
  public static final ColoredCubeBlock COLORED_PURPUR_BLOCK = ColoredCubeBlock.cubeAll(FabricBlockSettings.copyOf(Blocks.PURPUR_BLOCK), "mishanguc:block/pale_purpur_block");
  public static final ColoredPillarBlock COLORED_PURPUR_PILLAR = new ColoredPillarBlock(FabricBlockSettings.copyOf(Blocks.PURPUR_PILLAR), Texture.sideEnd(new Identifier("mishanguc:block/pale_purpur_pillar"), new Identifier("mishanguc:block/pale_purpur_pillar_top")));
  @ApiStatus.AvailableSince("1.0.2")
  public static final ColoredCubeBlock COLORED_END_STONE = ColoredCubeBlock.cubeAll(FabricBlockSettings.copyOf(Blocks.END_STONE), "mishanguc:block/pale_end_stone");
  @ApiStatus.AvailableSince("1.0.2")
  public static final ColoredStairsBlock COLORED_END_STONE_STAIRS = new ColoredStairsBlock(COLORED_END_STONE);
  @ApiStatus.AvailableSince("1.0.2")
  public static final ColoredSlabBlock COLORED_END_STONE_SLAB = new ColoredSlabBlock(COLORED_END_STONE);
  @ApiStatus.AvailableSince("1.0.2")
  public static final ColoredCubeBlock COLORED_END_STONE_BRICKS = ColoredCubeBlock.cubeAll(FabricBlockSettings.copyOf(Blocks.END_STONE_BRICKS), "mishanguc:block/pale_end_stone_bricks");
  @ApiStatus.AvailableSince("1.0.2")
  public static final ColoredStairsBlock COLORED_END_STONE_BRICK_STAIRS = new ColoredStairsBlock(COLORED_END_STONE_BRICKS);
  @ApiStatus.AvailableSince("1.0.2")
  public static final ColoredSlabBlock COLORED_END_STONE_BRICK_SLAB = new ColoredSlabBlock(COLORED_END_STONE_BRICKS);
  @ApiStatus.AvailableSince("1.0.2")
  @Translucent
  public static final ColoredNetherPortalBlock COLORED_NETHER_PORTAL = new ColoredNetherPortalBlock(FabricBlockSettings.copyOf(Blocks.NETHER_PORTAL));

  public static final ColoredCubeBlock COLORED_LIGHT = new ColoredCubeBlock(WHITE_LIGHT_SETTINGS, new Identifier("mishanguc:block/colored_cube_all_without_shade"), Texture.all(new Identifier("mishanguc:block/white_light")));
  @Translucent
  public static final ColoredGlassBlock COLORED_GLASS = new ColoredGlassBlock(FabricBlockSettings.copyOf(Blocks.WHITE_STAINED_GLASS), Texture.all(new Identifier("block/white_stained_glass")));
  @Translucent
  public static final ColoredIceBlock COLORED_ICE = new ColoredIceBlock(FabricBlockSettings.copyOf(Blocks.ICE).breakByTool(FabricToolTags.PICKAXES), Texture.all(new Identifier("mishanguc:block/pale_ice")));
  public static final ColoredCubeBlock COLORED_SNOW_BLOCK = ColoredCubeBlock.cubeAll(FabricBlockSettings.copyOf(Blocks.SNOW_BLOCK).breakByTool(FabricToolTags.SHOVELS), "block/snow");
  public static final ColoredCubeBlock COLORED_PACKED_ICE = ColoredCubeBlock.cubeAll(FabricBlockSettings.copyOf(Blocks.PACKED_ICE).breakByTool(FabricToolTags.SHOVELS), "mishanguc:block/pale_packed_ice");

  @ApiStatus.AvailableSince("0.2.4")
  public static final ColoredLeavesBlock COLORED_OAK_LEAVES = new ColoredLeavesBlock(FabricBlockSettings.copyOf(Blocks.OAK_LEAVES).breakByTool(FabricToolTags.HOES), block -> BlockLootTableGenerator.oakLeavesDrop(block, Blocks.OAK_SAPLING, 0.05F, 0.0625F, 0.083333336F, 0.1F), "block/oak_leaves");
  @ApiStatus.AvailableSince("0.2.4")
  public static final ColoredLeavesBlock COLORED_DARK_OAK_LEAVES = new ColoredLeavesBlock(FabricBlockSettings.copyOf(Blocks.DARK_OAK_LEAVES).breakByTool(FabricToolTags.HOES), block -> BlockLootTableGenerator.oakLeavesDrop(block, Blocks.DARK_OAK_SAPLING, 0.05F, 0.0625F, 0.083333336F, 0.1F), "block/dark_oak_leaves");
  @ApiStatus.AvailableSince("0.2.4")
  public static final ColoredLeavesBlock COLORED_BIRCH_LEAVES = new ColoredLeavesBlock(FabricBlockSettings.copyOf(Blocks.BIRCH_LEAVES).breakByTool(FabricToolTags.HOES), block -> BlockLootTableGenerator.leavesDrop(block, Blocks.BIRCH_SAPLING, 0.05F, 0.0625F, 0.083333336F, 0.1F), "block/birch_leaves");
  @ApiStatus.AvailableSince("0.2.4")
  public static final ColoredLeavesBlock COLORED_ACACIA_LEAVES = new ColoredLeavesBlock(FabricBlockSettings.copyOf(Blocks.ACACIA_LEAVES).breakByTool(FabricToolTags.HOES), block -> BlockLootTableGenerator.leavesDrop(block, Blocks.ACACIA_SAPLING, 0.05F, 0.0625F, 0.083333336F, 0.1F), "block/acacia_leaves");
  @ApiStatus.AvailableSince("0.2.4")
  public static final ColoredLeavesBlock COLORED_JUNGLE_LEAVES = new ColoredLeavesBlock(FabricBlockSettings.copyOf(Blocks.JUNGLE_LEAVES).breakByTool(FabricToolTags.HOES), block -> BlockLootTableGenerator.leavesDrop(block, Blocks.JUNGLE_SAPLING, 0.025F, 0.027777778F, 0.03125F, 0.041666668F, 0.1F), "block/jungle_leaves");
  @ApiStatus.AvailableSince("0.2.4")
  public static final ColoredLeavesBlock COLORED_SPRUCE_LEAVES = new ColoredLeavesBlock(FabricBlockSettings.copyOf(Blocks.SPRUCE_LEAVES).breakByTool(FabricToolTags.HOES), block -> BlockLootTableGenerator.leavesDrop(block, Blocks.SPRUCE_SAPLING, 0.05F, 0.0625F, 0.083333336F, 0.1F), "block/spruce_leaves");

  public static final ColoredCubeBlock COLORED_PURE_BLOCK = ColoredCubeBlock.cubeAll(FabricBlockSettings.of(Material.STONE, DyeColor.WHITE).strength(0.2f), "mishanguc:block/white_pure");
  public static final ColoredCubeBlock COLORED_PURE_LIGHT = ColoredCubeBlock.cubeAll(WHITE_LIGHT_SETTINGS, "mishanguc:block/white_pure");
  public static final ColoredCubeBlock COLORED_PURE_BLOCK_WITHOUT_SHADE = new ColoredCubeBlock(FabricBlockSettings.of(Material.STONE, DyeColor.WHITE).strength(0.2f), new Identifier("mishanguc:block/colored_cube_all_without_shade"), Texture.all(new Identifier("mishanguc:block/white_pure")));
  public static final ColoredCubeBlock COLORED_PURE_LIGHT_WITHOUT_SHADE = new ColoredCubeBlock(WHITE_LIGHT_SETTINGS, new Identifier("mishanguc:block/colored_cube_all_without_shade"), Texture.all(new Identifier("mishanguc:block/white_pure")));

  private ColoredBlocks() {
  }
}
