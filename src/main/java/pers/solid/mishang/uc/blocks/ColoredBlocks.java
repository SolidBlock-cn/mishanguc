package pers.solid.mishang.uc.blocks;

import net.devtech.arrp.api.RuntimeResourcePack;
import net.devtech.arrp.json.blockstate.JBlockStates;
import net.devtech.arrp.json.loot.JLootTable;
import net.devtech.arrp.json.models.JModel;
import net.devtech.arrp.json.models.JTextures;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.fabricmc.fabric.api.tool.attribute.v1.FabricToolTags;
import net.minecraft.block.Blocks;
import net.minecraft.block.Material;
import net.minecraft.data.client.model.BlockStateModelGenerator;
import net.minecraft.data.server.BlockLootTableGenerator;
import net.minecraft.util.DyeColor;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import pers.solid.mishang.uc.annotations.RegisterIdentifier;
import pers.solid.mishang.uc.annotations.Translucent;
import pers.solid.mishang.uc.block.*;

@ApiStatus.AvailableSince("0.2.2")
public final class ColoredBlocks extends MishangucBlocks {
  @RegisterIdentifier
  public static final ColoredCubeBlock COLORED_WOOL = ColoredCubeBlock.cubeAll(FabricBlockSettings.copyOf(Blocks.WHITE_WOOL).breakByTool(FabricToolTags.SHEARS), "block/white_wool");
  @RegisterIdentifier
  public static final ColoredCubeBlock COLORED_TERRACOTTA = ColoredCubeBlock.cubeAll(FabricBlockSettings.copyOf(Blocks.WHITE_TERRACOTTA), "block/white_terracotta");
  @RegisterIdentifier
  public static final ColoredCubeBlock COLORED_CONCRETE = ColoredCubeBlock.cubeAll(FabricBlockSettings.copyOf(Blocks.WHITE_CONCRETE), "block/white_concrete");
  @RegisterIdentifier
  public static final ColoredCubeBlock COLORED_PLANKS = ColoredCubeBlock.cubeAll(FabricBlockSettings.copyOf(Blocks.BIRCH_PLANKS).breakByTool(FabricToolTags.AXES), "mishanguc:block/pale_planks");
  @RegisterIdentifier
  public static final ColoredStairsBlock COLORED_PLANK_STAIRS = new ColoredStairsBlock(COLORED_PLANKS, FabricBlockSettings.copyOf(Blocks.BIRCH_STAIRS).breakByTool(FabricToolTags.AXES));
  @RegisterIdentifier
  public static final ColoredSlabBlock COLORED_PLANK_SLAB = new ColoredSlabBlock(COLORED_PLANKS, FabricBlockSettings.copyOf(Blocks.BIRCH_SLAB).breakByTool(FabricToolTags.AXES));
  @RegisterIdentifier
  public static final ColoredCubeBlock COLORED_DIRT = ColoredCubeBlock.cubeAll(FabricBlockSettings.copyOf(Blocks.DIRT).breakByTool(FabricToolTags.SHOVELS), "mishanguc:block/pale_dirt");
  @RegisterIdentifier
  public static final ColoredCubeBlock COLORED_COBBLESTONE = ColoredCubeBlock.cubeAll(FabricBlockSettings.copyOf(Blocks.COBBLESTONE), "mishanguc:block/pale_cobblestone");
  @RegisterIdentifier
  public static final ColoredStairsBlock COLORED_COBBLESTONE_STAIRS = new ColoredStairsBlock(COLORED_COBBLESTONE, FabricBlockSettings.copyOf(Blocks.COBBLESTONE_STAIRS));
  @RegisterIdentifier
  public static final ColoredSlabBlock COLORED_COBBLESTONE_SLAB = new ColoredSlabBlock(COLORED_COBBLESTONE, FabricBlockSettings.copyOf(Blocks.COBBLESTONE_SLAB));

  @RegisterIdentifier
  public static final ColoredCubeBlock COLORED_STONE = new ColoredCubeBlock(FabricBlockSettings.copyOf(Blocks.STONE), "mishanguc:block/colored_cube_all", JTextures.ofAll("mishanguc:block/pale_stone")) {
    @Environment(EnvType.CLIENT)
    @Override
    public void writeBlockModel(RuntimeResourcePack pack) {
      final JModel model = getBlockModel();
      final Identifier blockModelId = getBlockModelId();
      pack.addModel(model, blockModelId);
      pack.addModel(model.clone().parent("mishanguc:block/colored_cube_mirrored_all"), blockModelId.brrp_append("_mirrored"));
    }

    @Environment(EnvType.CLIENT)
    @Override
    public @NotNull JBlockStates getBlockStates() {
      final Identifier blockModelId = getBlockModelId();
      return JBlockStates.delegate(BlockStateModelGenerator.createBlockStateWithTwoModelAndRandomInversion(this, blockModelId, blockModelId.brrp_append("_mirrored")));
    }

    @Override
    public JLootTable getLootTable() {
      return JLootTable.delegate(BlockLootTableGenerator.drops(COLORED_STONE, COLORED_COBBLESTONE).apply(ColoredBlock.COPY_COLOR_LOOT_FUNCTION).build());
    }
  };
  @RegisterIdentifier
  public static final ColoredStairsBlock COLORED_STONE_STAIRS = new ColoredStairsBlock(COLORED_STONE, FabricBlockSettings.copyOf(Blocks.STONE_STAIRS));
  @RegisterIdentifier
  public static final ColoredSlabBlock COLORED_STONE_SLAB = new ColoredSlabBlock(COLORED_STONE, FabricBlockSettings.copyOf(Blocks.STONE_SLAB));
  @ApiStatus.AvailableSince("0.2.4")
  @RegisterIdentifier
  public static final ColoredCubeBlock COLORED_STONE_BRICKS = ColoredCubeBlock.cubeAll(FabricBlockSettings.copyOf(Blocks.STONE_BRICKS), "mishanguc:block/pale_stone_bricks");
  @ApiStatus.AvailableSince("0.2.4")
  @RegisterIdentifier
  public static final ColoredStairsBlock COLORED_STONE_BRICK_STAIRS = new ColoredStairsBlock(COLORED_STONE_BRICKS);
  @ApiStatus.AvailableSince("0.2.4")
  @RegisterIdentifier
  public static final ColoredSlabBlock COLORED_STONE_BRICK_SLAB = new ColoredSlabBlock(COLORED_STONE_BRICKS);
  @ApiStatus.AvailableSince("0.2.4")
  @RegisterIdentifier
  public static final ColoredCubeBlock COLORED_BRICKS = ColoredCubeBlock.cubeAll(FabricBlockSettings.copyOf(Blocks.BRICKS), "mishanguc:block/pale_bricks");
  @ApiStatus.AvailableSince("0.2.4")
  @RegisterIdentifier
  public static final ColoredStairsBlock COLORED_BRICK_STAIRS = new ColoredStairsBlock(COLORED_BRICKS);
  @ApiStatus.AvailableSince("0.2.4")
  @RegisterIdentifier
  public static final ColoredSlabBlock COLORED_BRICK_SLAB = new ColoredSlabBlock(COLORED_BRICKS);

  @RegisterIdentifier
  public static final ColoredCubeBlock COLORED_QUARTZ_BLOCK = ColoredCubeBlock.cubeBottomTop(FabricBlockSettings.copyOf(Blocks.QUARTZ_BLOCK), "block/quartz_block_top", "block/quartz_block_side", "block/quartz_block_top");
  @ApiStatus.AvailableSince("0.2.4")
  @RegisterIdentifier
  public static final ColoredStairsBlock COLORED_QUARTZ_STAIRS = new ColoredStairsBlock(COLORED_QUARTZ_BLOCK);
  @ApiStatus.AvailableSince("0.2.4")
  @RegisterIdentifier
  public static final ColoredSlabBlock COLORED_QUARTZ_SLAB = new ColoredSlabBlock(COLORED_QUARTZ_BLOCK);

  @RegisterIdentifier
  public static final ColoredCubeBlock COLORED_CHISELED_QUARTZ_BLOCK = ColoredCubeBlock.cubeBottomTop(FabricBlockSettings.copyOf(Blocks.CHISELED_QUARTZ_BLOCK), "block/chiseled_quartz_block_top", "block/chiseled_quartz_block", "block/chiseled_quartz_block_top");

  @RegisterIdentifier
  public static final ColoredCubeBlock COLORED_QUARTZ_BRICKS = ColoredCubeBlock.cubeAll(FabricBlockSettings.copyOf(Blocks.QUARTZ_BRICKS), "block/quartz_bricks");
  @RegisterIdentifier
  public static final ColoredCubeBlock COLORED_SMOOTH_QUARTZ = ColoredCubeBlock.cubeAll(FabricBlockSettings.copyOf(Blocks.SMOOTH_QUARTZ), "block/quartz_block_bottom");
  @RegisterIdentifier
  public static final ColoredStairsBlock COLORED_SMOOTH_QUARTZ_STAIRS = new ColoredStairsBlock(COLORED_SMOOTH_QUARTZ);
  @RegisterIdentifier
  public static final ColoredSlabBlock COLORED_SMOOTH_QUARTZ_SLAB = new ColoredSlabBlock(COLORED_SMOOTH_QUARTZ);
  @RegisterIdentifier
  public static final ColoredPillarBlock COLORED_QUARTZ_PILLAR = new ColoredPillarBlock(FabricBlockSettings.copyOf(Blocks.QUARTZ_PILLAR), JTextures.of("side", "block/quartz_pillar").var("end", "block/quartz_pillar_top"));
  @RegisterIdentifier
  public static final ColoredCubeBlock COLORED_PURPUR_BLOCK = ColoredCubeBlock.cubeAll(FabricBlockSettings.copyOf(Blocks.PURPUR_BLOCK), "mishanguc:block/pale_purpur_block");
  @RegisterIdentifier
  public static final ColoredPillarBlock COLORED_PURPUR_PILLAR = new ColoredPillarBlock(FabricBlockSettings.copyOf(Blocks.PURPUR_PILLAR), JTextures.of("side", "mishanguc:block/pale_purpur_pillar").var("end", "mishanguc:block/pale_purpur_pillar_top"));

  @RegisterIdentifier
  public static final ColoredCubeBlock COLORED_LIGHT = new ColoredCubeBlock(WHITE_LIGHT_SETTINGS, "mishanguc:block/colored_cube_all_without_shade", JTextures.ofAll("mishanguc:block/white_light"));
  @RegisterIdentifier
  @Translucent
  public static final ColoredGlassBlock COLORED_GLASS = new ColoredGlassBlock(FabricBlockSettings.copyOf(Blocks.WHITE_STAINED_GLASS), JTextures.ofAll("block/white_stained_glass"));
  @RegisterIdentifier
  @Translucent
  public static final ColoredIceBlock COLORED_ICE = new ColoredIceBlock(FabricBlockSettings.copyOf(Blocks.ICE).breakByTool(FabricToolTags.PICKAXES), JTextures.ofAll("mishanguc:block/pale_ice"));
  @RegisterIdentifier
  public static final ColoredCubeBlock COLORED_SNOW_BLOCK = ColoredCubeBlock.cubeAll(FabricBlockSettings.copyOf(Blocks.SNOW_BLOCK).breakByTool(FabricToolTags.SHOVELS), "block/snow");
  @RegisterIdentifier
  public static final ColoredCubeBlock COLORED_PACKED_ICE = ColoredCubeBlock.cubeAll(FabricBlockSettings.copyOf(Blocks.PACKED_ICE).breakByTool(FabricToolTags.SHOVELS), "mishanguc:block/pale_packed_ice");

  @ApiStatus.AvailableSince("0.2.4")
  @RegisterIdentifier
  public static final ColoredLeavesBlock COLORED_OAK_LEAVES = new ColoredLeavesBlock(FabricBlockSettings.copyOf(Blocks.OAK_LEAVES).breakByTool(FabricToolTags.HOES), block -> BlockLootTableGenerator.oakLeavesDrop(block, Blocks.OAK_SAPLING, 0.05F, 0.0625F, 0.083333336F, 0.1F), "block/oak_leaves");
  @ApiStatus.AvailableSince("0.2.4")
  @RegisterIdentifier
  public static final ColoredLeavesBlock COLORED_DARK_OAK_LEAVES = new ColoredLeavesBlock(FabricBlockSettings.copyOf(Blocks.DARK_OAK_LEAVES).breakByTool(FabricToolTags.HOES), block -> BlockLootTableGenerator.oakLeavesDrop(block, Blocks.DARK_OAK_SAPLING, 0.05F, 0.0625F, 0.083333336F, 0.1F), "block/dark_oak_leaves");
  @ApiStatus.AvailableSince("0.2.4")
  @RegisterIdentifier
  public static final ColoredLeavesBlock COLORED_BIRCH_LEAVES = new ColoredLeavesBlock(FabricBlockSettings.copyOf(Blocks.BIRCH_LEAVES).breakByTool(FabricToolTags.HOES), block -> BlockLootTableGenerator.leavesDrop(block, Blocks.BIRCH_SAPLING, 0.05F, 0.0625F, 0.083333336F, 0.1F), "block/birch_leaves");
  @ApiStatus.AvailableSince("0.2.4")
  @RegisterIdentifier
  public static final ColoredLeavesBlock COLORED_ACACIA_LEAVES = new ColoredLeavesBlock(FabricBlockSettings.copyOf(Blocks.ACACIA_LEAVES).breakByTool(FabricToolTags.HOES), block -> BlockLootTableGenerator.leavesDrop(block, Blocks.ACACIA_SAPLING, 0.05F, 0.0625F, 0.083333336F, 0.1F), "block/acacia_leaves");
  @ApiStatus.AvailableSince("0.2.4")
  @RegisterIdentifier
  public static final ColoredLeavesBlock COLORED_JUNGLE_LEAVES = new ColoredLeavesBlock(FabricBlockSettings.copyOf(Blocks.JUNGLE_LEAVES).breakByTool(FabricToolTags.HOES), block -> BlockLootTableGenerator.leavesDrop(block, Blocks.JUNGLE_SAPLING, 0.025F, 0.027777778F, 0.03125F, 0.041666668F, 0.1F), "block/jungle_leaves");
  @ApiStatus.AvailableSince("0.2.4")
  @RegisterIdentifier
  public static final ColoredLeavesBlock COLORED_SPRUCE_LEAVES = new ColoredLeavesBlock(FabricBlockSettings.copyOf(Blocks.SPRUCE_LEAVES).breakByTool(FabricToolTags.HOES), block -> BlockLootTableGenerator.leavesDrop(block, Blocks.SPRUCE_SAPLING, 0.05F, 0.0625F, 0.083333336F, 0.1F), "block/spruce_leaves");

  @RegisterIdentifier
  public static final ColoredCubeBlock COLORED_PURE_BLOCK = ColoredCubeBlock.cubeAll(FabricBlockSettings.of(Material.STONE, DyeColor.WHITE).strength(0.2f), "mishanguc:block/white_pure");
  @RegisterIdentifier
  public static final ColoredCubeBlock COLORED_PURE_LIGHT = ColoredCubeBlock.cubeAll(WHITE_LIGHT_SETTINGS, "mishanguc:block/white_pure");
  @RegisterIdentifier
  public static final ColoredCubeBlock COLORED_PURE_BLOCK_WITHOUT_SHADE = new ColoredCubeBlock(FabricBlockSettings.of(Material.STONE, DyeColor.WHITE).strength(0.2f), "mishanguc:block/colored_cube_all_without_shade", JTextures.ofAll("mishanguc:block/white_pure"));
  @RegisterIdentifier
  public static final ColoredCubeBlock COLORED_PURE_LIGHT_WITHOUT_SHADE = new ColoredCubeBlock(WHITE_LIGHT_SETTINGS, "mishanguc:block/colored_cube_all_without_shade", JTextures.ofAll("mishanguc:block/white_pure"));

  private ColoredBlocks() {
  }
}
