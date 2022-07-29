package pers.solid.mishang.uc.blocks;

import net.devtech.arrp.json.models.JTextures;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.Blocks;
import net.minecraft.block.Material;
import net.minecraft.util.DyeColor;
import org.jetbrains.annotations.ApiStatus;
import pers.solid.mishang.uc.annotations.RegisterIdentifier;
import pers.solid.mishang.uc.annotations.Translucent;
import pers.solid.mishang.uc.block.ColoredCubeBlock;
import pers.solid.mishang.uc.block.ColoredGlassBlock;
import pers.solid.mishang.uc.block.ColoredIceBlock;
import pers.solid.mishang.uc.block.ColoredPillarBlock;

@ApiStatus.AvailableSince("0.2.2")
public final class ColoredBlocks extends MishangucBlocks {
  @RegisterIdentifier
  public static final ColoredCubeBlock COLORED_WOOL = ColoredCubeBlock.cubeAll(FabricBlockSettings.copyOf(Blocks.WHITE_WOOL), "block/white_wool");
  @RegisterIdentifier
  public static final ColoredCubeBlock COLORED_TERRACOTTA = ColoredCubeBlock.cubeAll(FabricBlockSettings.copyOf(Blocks.WHITE_TERRACOTTA), "block/white_terracotta");
  @RegisterIdentifier
  public static final ColoredCubeBlock COLORED_CONCRETE = ColoredCubeBlock.cubeAll(FabricBlockSettings.copyOf(Blocks.WHITE_CONCRETE), "block/white_concrete");
  @RegisterIdentifier
  public static final ColoredCubeBlock COLORED_PLANKS = ColoredCubeBlock.cubeAll(FabricBlockSettings.copyOf(Blocks.BIRCH_PLANKS), "mishanguc:block/pale_planks");

  @RegisterIdentifier
  public static final ColoredCubeBlock COLORED_QUARTZ_BLOCK = ColoredCubeBlock.cubeBottomTop(FabricBlockSettings.copyOf(Blocks.QUARTZ_BLOCK), "block/quartz_block_top", "block/quartz_block_side", "block/quartz_block_top");

  @RegisterIdentifier
  public static final ColoredCubeBlock COLORED_CHISELED_QUARTZ_BLOCK = ColoredCubeBlock.cubeBottomTop(FabricBlockSettings.copyOf(Blocks.CHISELED_QUARTZ_BLOCK), "block/chiseled_quartz_block_top", "block/chiseled_quartz_block", "block/chiseled_quartz_block_top");

  @RegisterIdentifier
  public static final ColoredCubeBlock COLORED_QUARTZ_BRICKS = ColoredCubeBlock.cubeAll(FabricBlockSettings.copyOf(Blocks.QUARTZ_BRICKS), "block/quartz_bricks");
  @RegisterIdentifier
  public static final ColoredCubeBlock COLORED_SMOOTH_QUARTZ = ColoredCubeBlock.cubeAll(FabricBlockSettings.copyOf(Blocks.SMOOTH_QUARTZ), "block/quartz_block_bottom");
  @RegisterIdentifier
  public static final ColoredPillarBlock COLORED_QUARTZ_PILLAR = new ColoredPillarBlock(FabricBlockSettings.copyOf(Blocks.QUARTZ_PILLAR), JTextures.of("side", "block/quartz_pillar").var("end", "block/quartz_pillar_top"));
  @RegisterIdentifier
  public static final ColoredCubeBlock COLORED_PURPUR_BLOCK = ColoredCubeBlock.cubeAll(FabricBlockSettings.copyOf(Blocks.PURPUR_BLOCK), "mishanguc:block/pale_purpur_block");
  @RegisterIdentifier
  public static final ColoredPillarBlock COLORED_PURPUR_PILLAR = new ColoredPillarBlock(FabricBlockSettings.copyOf(Blocks.PURPUR_PILLAR), JTextures.of("side", "mishanguc:block/pale_purpur_pillar").var("end", "mishanguc:block/pale_purpur_pillar_top"));

  @RegisterIdentifier
  public static final ColoredCubeBlock COLORED_LIGHT = ColoredCubeBlock.cubeAll(WHITE_LIGHT_SETTINGS, "mishanguc:block/white_light");
  @RegisterIdentifier
  public static final ColoredCubeBlock COLORED_PURE_BLOCK = ColoredCubeBlock.cubeAll(FabricBlockSettings.of(Material.STONE, DyeColor.WHITE).strength(0.2f), "mishanguc:block/white_pure");
  @RegisterIdentifier
  public static final ColoredCubeBlock COLORED_PURE_LIGHT = ColoredCubeBlock.cubeAll(WHITE_LIGHT_SETTINGS, "mishanguc:block/white_pure");
  @RegisterIdentifier
  public static final ColoredCubeBlock COLORED_PURE_BLOCK_WITHOUT_SHADE = new ColoredCubeBlock(FabricBlockSettings.of(Material.STONE, DyeColor.WHITE).strength(0.2f), "mishanguc:block/colored_cube_all_without_shade", JTextures.ofAll("mishanguc:block/white_pure"));
  @RegisterIdentifier
  public static final ColoredCubeBlock COLORED_PURE_LIGHT_WITHOUT_SHADE = new ColoredCubeBlock(WHITE_LIGHT_SETTINGS, "mishanguc:block/colored_cube_all_without_shade", JTextures.ofAll("mishanguc:block/white_pure"));
  @RegisterIdentifier
  @Translucent
  public static final ColoredGlassBlock COLORED_GLASS = new ColoredGlassBlock(FabricBlockSettings.copyOf(Blocks.WHITE_STAINED_GLASS), JTextures.ofAll("block/white_stained_glass"));
  @RegisterIdentifier
  @Translucent
  public static final ColoredIceBlock COLORED_ICE = new ColoredIceBlock(FabricBlockSettings.copyOf(Blocks.ICE), JTextures.ofAll("mishanguc:block/pale_ice"));
}
