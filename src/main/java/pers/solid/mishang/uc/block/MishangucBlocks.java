package pers.solid.mishang.uc.block;

import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.*;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemGroup;
import net.minecraft.util.DyeColor;
import net.minecraft.util.Identifier;
import net.minecraft.util.Util;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;
import org.jetbrains.annotations.Nullable;
import pers.solid.mishang.uc.*;
import pers.solid.mishang.uc.annotations.Cutout;
import pers.solid.mishang.uc.annotations.InGroup;
import pers.solid.mishang.uc.annotations.RegisterIdentifier;
import pers.solid.mishang.uc.item.NamedBlockItem;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.EnumMap;
import java.util.Map;

/** 迷上城建模组的所有方块。 */
public final class MishangucBlocks {
  /**
   *
   *
   * <h1>告示牌类方块</h1>
   *
   * 具有多种不同颜色。
   */
  public static final Map<DyeColor, HungSignBlock> HUNG_CONCRETE_GLOWING_SIGNS =
      Util.make(
          new EnumMap<>(DyeColor.class),
          map -> {
            for (DyeColor color : DyeColor.values()) {
              final Block block =
                  Registry.BLOCK.get(new Identifier("minecraft", color.asString() + "_concrete"));
              map.put(
                  color, new HungSignBlock(block, FabricBlockSettings.copyOf(block).luminance(15)));
            }
          });

  public static final Map<DyeColor, HungSignBlock> HUNG_TERRACOTTA_GLOWING_SIGNS =
      Util.make(
          new EnumMap<>(DyeColor.class),
          map -> {
            for (DyeColor color : DyeColor.values()) {
              final Block block =
                  Registry.BLOCK.get(new Identifier("minecraft", color.asString() + "_terracotta"));
              map.put(
                  color, new HungSignBlock(block, FabricBlockSettings.copyOf(block).luminance(15)));
            }
          });

  @InGroup("signs")
  @RegisterIdentifier
  public static final HungSignBlock HUNG_WHITE_CONCRETE_GLOWING_SIGN =
      HUNG_CONCRETE_GLOWING_SIGNS.get(DyeColor.WHITE);

  @RegisterIdentifier
  public static final HungSignBlock HUNG_ORANGE_CONCRETE_GLOWING_SIGN =
      HUNG_CONCRETE_GLOWING_SIGNS.get(DyeColor.ORANGE);

  @RegisterIdentifier
  public static final HungSignBlock HUNG_MAGENTA_CONCRETE_GLOWING_SIGN =
      HUNG_CONCRETE_GLOWING_SIGNS.get(DyeColor.MAGENTA);

  @RegisterIdentifier
  public static final HungSignBlock HUNG_LIGHT_BLUE_CONCRETE_GLOWING_SIGN =
      HUNG_CONCRETE_GLOWING_SIGNS.get(DyeColor.LIGHT_BLUE);

  @RegisterIdentifier
  public static final HungSignBlock HUNG_YELLOW_CONCRETE_GLOWING_SIGN =
      HUNG_CONCRETE_GLOWING_SIGNS.get(DyeColor.YELLOW);

  @RegisterIdentifier
  public static final HungSignBlock HUNG_LIME_CONCRETE_GLOWING_SIGN =
      HUNG_CONCRETE_GLOWING_SIGNS.get(DyeColor.LIME);

  @RegisterIdentifier
  public static final HungSignBlock HUNG_PINK_CONCRETE_GLOWING_SIGN =
      HUNG_CONCRETE_GLOWING_SIGNS.get(DyeColor.PINK);

  @RegisterIdentifier
  public static final HungSignBlock HUNG_GRAY_CONCRETE_GLOWING_SIGN =
      HUNG_CONCRETE_GLOWING_SIGNS.get(DyeColor.GRAY);

  @RegisterIdentifier
  public static final HungSignBlock HUNG_LIGHT_GRAY_CONCRETE_GLOWING_SIGN =
      HUNG_CONCRETE_GLOWING_SIGNS.get(DyeColor.LIGHT_GRAY);

  @RegisterIdentifier
  public static final HungSignBlock HUNG_CYAN_CONCRETE_GLOWING_SIGN =
      HUNG_CONCRETE_GLOWING_SIGNS.get(DyeColor.CYAN);

  @RegisterIdentifier
  public static final HungSignBlock HUNG_PURPLE_CONCRETE_GLOWING_SIGN =
      HUNG_CONCRETE_GLOWING_SIGNS.get(DyeColor.PURPLE);

  @RegisterIdentifier
  public static final HungSignBlock HUNG_BLUE_CONCRETE_GLOWING_SIGN =
      HUNG_CONCRETE_GLOWING_SIGNS.get(DyeColor.BLUE);

  @RegisterIdentifier
  public static final HungSignBlock HUNG_BROWN_CONCRETE_GLOWING_SIGN =
      HUNG_CONCRETE_GLOWING_SIGNS.get(DyeColor.BROWN);

  @RegisterIdentifier
  public static final HungSignBlock HUNG_GREEN_CONCRETE_GLOWING_SIGN =
      HUNG_CONCRETE_GLOWING_SIGNS.get(DyeColor.GREEN);

  @RegisterIdentifier
  public static final HungSignBlock HUNG_RED_CONCRETE_GLOWING_SIGN =
      HUNG_CONCRETE_GLOWING_SIGNS.get(DyeColor.RED);

  @RegisterIdentifier
  public static final HungSignBlock HUNG_BLACK_CONCRETE_GLOWING_SIGN =
      HUNG_CONCRETE_GLOWING_SIGNS.get(DyeColor.BLACK);

  @RegisterIdentifier
  public static final HungSignBlock HUNG_WHITE_TERRACOTTA_GLOWING_SIGN =
      HUNG_TERRACOTTA_GLOWING_SIGNS.get(DyeColor.WHITE);

  @RegisterIdentifier
  public static final HungSignBlock HUNG_ORANGE_TERRACOTTA_GLOWING_SIGN =
      HUNG_TERRACOTTA_GLOWING_SIGNS.get(DyeColor.ORANGE);

  @RegisterIdentifier
  public static final HungSignBlock HUNG_MAGENTA_TERRACOTTA_GLOWING_SIGN =
      HUNG_TERRACOTTA_GLOWING_SIGNS.get(DyeColor.MAGENTA);

  @RegisterIdentifier
  public static final HungSignBlock HUNG_LIGHT_BLUE_TERRACOTTA_GLOWING_SIGN =
      HUNG_TERRACOTTA_GLOWING_SIGNS.get(DyeColor.LIGHT_BLUE);

  @RegisterIdentifier
  public static final HungSignBlock HUNG_YELLOW_TERRACOTTA_GLOWING_SIGN =
      HUNG_TERRACOTTA_GLOWING_SIGNS.get(DyeColor.YELLOW);

  @RegisterIdentifier
  public static final HungSignBlock HUNG_LIME_TERRACOTTA_GLOWING_SIGN =
      HUNG_TERRACOTTA_GLOWING_SIGNS.get(DyeColor.LIME);

  @RegisterIdentifier
  public static final HungSignBlock HUNG_PINK_TERRACOTTA_GLOWING_SIGN =
      HUNG_TERRACOTTA_GLOWING_SIGNS.get(DyeColor.PINK);

  @RegisterIdentifier
  public static final HungSignBlock HUNG_GRAY_TERRACOTTA_GLOWING_SIGN =
      HUNG_TERRACOTTA_GLOWING_SIGNS.get(DyeColor.GRAY);

  @RegisterIdentifier
  public static final HungSignBlock HUNG_LIGHT_GRAY_TERRACOTTA_GLOWING_SIGN =
      HUNG_TERRACOTTA_GLOWING_SIGNS.get(DyeColor.LIGHT_GRAY);

  @RegisterIdentifier
  public static final HungSignBlock HUNG_CYAN_TERRACOTTA_GLOWING_SIGN =
      HUNG_TERRACOTTA_GLOWING_SIGNS.get(DyeColor.CYAN);

  @RegisterIdentifier
  public static final HungSignBlock HUNG_PURPLE_TERRACOTTA_GLOWING_SIGN =
      HUNG_TERRACOTTA_GLOWING_SIGNS.get(DyeColor.PURPLE);

  @RegisterIdentifier
  public static final HungSignBlock HUNG_BLUE_TERRACOTTA_GLOWING_SIGN =
      HUNG_TERRACOTTA_GLOWING_SIGNS.get(DyeColor.BLUE);

  @RegisterIdentifier
  public static final HungSignBlock HUNG_BROWN_TERRACOTTA_GLOWING_SIGN =
      HUNG_TERRACOTTA_GLOWING_SIGNS.get(DyeColor.BROWN);

  @RegisterIdentifier
  public static final HungSignBlock HUNG_GREEN_TERRACOTTA_GLOWING_SIGN =
      HUNG_TERRACOTTA_GLOWING_SIGNS.get(DyeColor.GREEN);

  @RegisterIdentifier
  public static final HungSignBlock HUNG_RED_TERRACOTTA_GLOWING_SIGN =
      HUNG_TERRACOTTA_GLOWING_SIGNS.get(DyeColor.RED);

  @RegisterIdentifier
  public static final HungSignBlock HUNG_BLACK_TERRACOTTA_GLOWING_SIGN =
      HUNG_TERRACOTTA_GLOWING_SIGNS.get(DyeColor.BLACK);

  @RegisterIdentifier
  public static final HungSignBlock HUNG_NETHERRACK_GLOWING_SIGN =
      new HungSignBlock(
          Blocks.NETHERRACK, FabricBlockSettings.copyOf(Blocks.NETHERRACK).luminance(15));

  @RegisterIdentifier
  public static final HungSignBlock HUNG_NETHER_BRICK_GLOWING_SIGN =
      new HungSignBlock(
          Blocks.NETHER_BRICKS, FabricBlockSettings.copyOf(Blocks.NETHER_BRICKS).luminance(15));

  @RegisterIdentifier
  public static final HungSignBlock HUNG_BLACKSTONE_GLOWING_SIGN =
      new HungSignBlock(
          Blocks.BLACKSTONE, FabricBlockSettings.copyOf(Blocks.BLACKSTONE).luminance(15));

  @RegisterIdentifier
  public static final HungSignBlock HUNG_POLISHED_BLACKSTONE_GLOWING_SIGN =
      new HungSignBlock(
          Blocks.POLISHED_BLACKSTONE,
          FabricBlockSettings.copyOf(Blocks.POLISHED_BLACKSTONE).luminance(15));

  @RegisterIdentifier
  public static final WallSignBlock OAK_WALL_SIGN =
      new WallSignBlock(Blocks.OAK_PLANKS, FabricBlockSettings.copyOf(Blocks.OAK_PLANKS));

  @RegisterIdentifier
  public static final WallSignBlock SPRUCE_WALL_SIGN =
      new WallSignBlock(Blocks.SPRUCE_PLANKS, FabricBlockSettings.copyOf(Blocks.SPRUCE_PLANKS));

  @RegisterIdentifier
  public static final WallSignBlock BIRCH_WALL_SIGN =
      new WallSignBlock(Blocks.BIRCH_PLANKS, FabricBlockSettings.copyOf(Blocks.BIRCH_PLANKS));

  @RegisterIdentifier
  public static final WallSignBlock JUNGLE_WALL_SIGN =
      new WallSignBlock(Blocks.JUNGLE_PLANKS, FabricBlockSettings.copyOf(Blocks.JUNGLE_PLANKS));

  @RegisterIdentifier
  public static final WallSignBlock ACACIA_WALL_SIGN =
      new WallSignBlock(Blocks.ACACIA_PLANKS, FabricBlockSettings.copyOf(Blocks.ACACIA_PLANKS));

  @RegisterIdentifier
  public static final WallSignBlock DARK_OAK_WALL_SIGN =
      new WallSignBlock(Blocks.DARK_OAK_PLANKS, FabricBlockSettings.copyOf(Blocks.DARK_OAK_PLANKS));

  @RegisterIdentifier
  public static final WallSignBlock CRIMSON_WALL_SIGN =
      new WallSignBlock(Blocks.CRIMSON_PLANKS, FabricBlockSettings.copyOf(Blocks.CRIMSON_PLANKS));

  @RegisterIdentifier
  public static final WallSignBlock WARPED_WALL_SIGN =
      new WallSignBlock(Blocks.WARPED_PLANKS, FabricBlockSettings.copyOf(Blocks.WARPED_PLANKS));

  @RegisterIdentifier
  public static final WallSignBlock WHITE_CONCRETE_WALL_SIGN =
      new WallSignBlock(Blocks.WHITE_CONCRETE, FabricBlockSettings.copyOf(Blocks.WHITE_CONCRETE));

  @RegisterIdentifier
  public static final WallSignBlock ORANGE_CONCRETE_WALL_SIGN =
      new WallSignBlock(Blocks.ORANGE_CONCRETE, FabricBlockSettings.copyOf(Blocks.ORANGE_CONCRETE));

  @RegisterIdentifier
  public static final WallSignBlock MAGENTA_CONCRETE_WALL_SIGN =
      new WallSignBlock(
          Blocks.MAGENTA_CONCRETE, FabricBlockSettings.copyOf(Blocks.MAGENTA_CONCRETE));

  @RegisterIdentifier
  public static final WallSignBlock LIGHT_BLUE_CONCRETE_WALL_SIGN =
      new WallSignBlock(
          Blocks.LIGHT_BLUE_CONCRETE, FabricBlockSettings.copyOf(Blocks.LIGHT_BLUE_CONCRETE));

  @RegisterIdentifier
  public static final WallSignBlock YELLOW_CONCRETE_WALL_SIGN =
      new WallSignBlock(Blocks.YELLOW_CONCRETE, FabricBlockSettings.copyOf(Blocks.YELLOW_CONCRETE));

  @RegisterIdentifier
  public static final WallSignBlock LIME_CONCRETE_WALL_SIGN =
      new WallSignBlock(Blocks.LIME_CONCRETE, FabricBlockSettings.copyOf(Blocks.LIME_CONCRETE));

  @RegisterIdentifier
  public static final WallSignBlock PINK_CONCRETE_WALL_SIGN =
      new WallSignBlock(Blocks.PINK_CONCRETE, FabricBlockSettings.copyOf(Blocks.PINK_CONCRETE));

  @RegisterIdentifier
  public static final WallSignBlock GRAY_CONCRETE_WALL_SIGN =
      new WallSignBlock(Blocks.GRAY_CONCRETE, FabricBlockSettings.copyOf(Blocks.GRAY_CONCRETE));

  @RegisterIdentifier
  public static final WallSignBlock LIGHT_GRAY_CONCRETE_WALL_SIGN =
      new WallSignBlock(
          Blocks.LIGHT_GRAY_CONCRETE, FabricBlockSettings.copyOf(Blocks.LIGHT_GRAY_CONCRETE));

  @RegisterIdentifier
  public static final WallSignBlock CYAN_CONCRETE_WALL_SIGN =
      new WallSignBlock(Blocks.CYAN_CONCRETE, FabricBlockSettings.copyOf(Blocks.CYAN_CONCRETE));

  @RegisterIdentifier
  public static final WallSignBlock PURPLE_CONCRETE_WALL_SIGN =
      new WallSignBlock(Blocks.PURPLE_CONCRETE, FabricBlockSettings.copyOf(Blocks.PURPLE_CONCRETE));

  @RegisterIdentifier
  public static final WallSignBlock BLUE_CONCRETE_WALL_SIGN =
      new WallSignBlock(Blocks.BLUE_CONCRETE, FabricBlockSettings.copyOf(Blocks.BLUE_CONCRETE));

  @RegisterIdentifier
  public static final WallSignBlock BROWN_CONCRETE_WALL_SIGN =
      new WallSignBlock(Blocks.BROWN_CONCRETE, FabricBlockSettings.copyOf(Blocks.BROWN_CONCRETE));

  @RegisterIdentifier
  public static final WallSignBlock GREEN_CONCRETE_WALL_SIGN =
      new WallSignBlock(Blocks.GREEN_CONCRETE, FabricBlockSettings.copyOf(Blocks.GREEN_CONCRETE));

  @RegisterIdentifier
  public static final WallSignBlock RED_CONCRETE_WALL_SIGN =
      new WallSignBlock(Blocks.RED_CONCRETE, FabricBlockSettings.copyOf(Blocks.RED_CONCRETE));

  @RegisterIdentifier
  public static final WallSignBlock BLACK_CONCRETE_WALL_SIGN =
      new WallSignBlock(Blocks.BLACK_CONCRETE, FabricBlockSettings.copyOf(Blocks.BLACK_CONCRETE));

  public static final Map<DyeColor, WallSignBlock> CONCRETE_WALL_SIGNS =
      Util.make(
          new EnumMap<>(DyeColor.class),
          map -> {
            map.put(DyeColor.WHITE, WHITE_CONCRETE_WALL_SIGN);
            map.put(DyeColor.ORANGE, ORANGE_CONCRETE_WALL_SIGN);
            map.put(DyeColor.MAGENTA, MAGENTA_CONCRETE_WALL_SIGN);
            map.put(DyeColor.LIGHT_BLUE, LIGHT_BLUE_CONCRETE_WALL_SIGN);
            map.put(DyeColor.YELLOW, YELLOW_CONCRETE_WALL_SIGN);
            map.put(DyeColor.LIME, LIME_CONCRETE_WALL_SIGN);
            map.put(DyeColor.PINK, PINK_CONCRETE_WALL_SIGN);
            map.put(DyeColor.GRAY, GRAY_CONCRETE_WALL_SIGN);
            map.put(DyeColor.LIGHT_GRAY, LIGHT_GRAY_CONCRETE_WALL_SIGN);
            map.put(DyeColor.CYAN, CYAN_CONCRETE_WALL_SIGN);
            map.put(DyeColor.PURPLE, PURPLE_CONCRETE_WALL_SIGN);
            map.put(DyeColor.BLUE, BLUE_CONCRETE_WALL_SIGN);
            map.put(DyeColor.BROWN, BROWN_CONCRETE_WALL_SIGN);
            map.put(DyeColor.GREEN, GREEN_CONCRETE_WALL_SIGN);
            map.put(DyeColor.RED, RED_CONCRETE_WALL_SIGN);
            map.put(DyeColor.BLACK, BLACK_CONCRETE_WALL_SIGN);
          });

  @RegisterIdentifier
  public static final WallSignBlock WHITE_TERRACOTTA_WALL_SIGN =
      new WallSignBlock(
          Blocks.WHITE_TERRACOTTA, FabricBlockSettings.copyOf(Blocks.WHITE_TERRACOTTA));

  @RegisterIdentifier
  public static final WallSignBlock ORANGE_TERRACOTTA_WALL_SIGN =
      new WallSignBlock(
          Blocks.ORANGE_TERRACOTTA, FabricBlockSettings.copyOf(Blocks.ORANGE_TERRACOTTA));

  @RegisterIdentifier
  public static final WallSignBlock MAGENTA_TERRACOTTA_WALL_SIGN =
      new WallSignBlock(
          Blocks.MAGENTA_TERRACOTTA, FabricBlockSettings.copyOf(Blocks.MAGENTA_TERRACOTTA));

  @RegisterIdentifier
  public static final WallSignBlock LIGHT_BLUE_TERRACOTTA_WALL_SIGN =
      new WallSignBlock(
          Blocks.LIGHT_BLUE_TERRACOTTA, FabricBlockSettings.copyOf(Blocks.LIGHT_BLUE_TERRACOTTA));

  @RegisterIdentifier
  public static final WallSignBlock YELLOW_TERRACOTTA_WALL_SIGN =
      new WallSignBlock(
          Blocks.YELLOW_TERRACOTTA, FabricBlockSettings.copyOf(Blocks.YELLOW_TERRACOTTA));

  @RegisterIdentifier
  public static final WallSignBlock LIME_TERRACOTTA_WALL_SIGN =
      new WallSignBlock(Blocks.LIME_TERRACOTTA, FabricBlockSettings.copyOf(Blocks.LIME_TERRACOTTA));

  @RegisterIdentifier
  public static final WallSignBlock PINK_TERRACOTTA_WALL_SIGN =
      new WallSignBlock(Blocks.PINK_TERRACOTTA, FabricBlockSettings.copyOf(Blocks.PINK_TERRACOTTA));

  @RegisterIdentifier
  public static final WallSignBlock GRAY_TERRACOTTA_WALL_SIGN =
      new WallSignBlock(Blocks.GRAY_TERRACOTTA, FabricBlockSettings.copyOf(Blocks.GRAY_TERRACOTTA));

  @RegisterIdentifier
  public static final WallSignBlock LIGHT_GRAY_TERRACOTTA_WALL_SIGN =
      new WallSignBlock(
          Blocks.LIGHT_GRAY_TERRACOTTA, FabricBlockSettings.copyOf(Blocks.LIGHT_GRAY_TERRACOTTA));

  @RegisterIdentifier
  public static final WallSignBlock CYAN_TERRACOTTA_WALL_SIGN =
      new WallSignBlock(Blocks.CYAN_TERRACOTTA, FabricBlockSettings.copyOf(Blocks.CYAN_TERRACOTTA));

  @RegisterIdentifier
  public static final WallSignBlock PURPLE_TERRACOTTA_WALL_SIGN =
      new WallSignBlock(
          Blocks.PURPLE_TERRACOTTA, FabricBlockSettings.copyOf(Blocks.PURPLE_TERRACOTTA));

  @RegisterIdentifier
  public static final WallSignBlock BLUE_TERRACOTTA_WALL_SIGN =
      new WallSignBlock(Blocks.BLUE_TERRACOTTA, FabricBlockSettings.copyOf(Blocks.BLUE_TERRACOTTA));

  @RegisterIdentifier
  public static final WallSignBlock BROWN_TERRACOTTA_WALL_SIGN =
      new WallSignBlock(
          Blocks.BROWN_TERRACOTTA, FabricBlockSettings.copyOf(Blocks.BROWN_TERRACOTTA));

  @RegisterIdentifier
  public static final WallSignBlock GREEN_TERRACOTTA_WALL_SIGN =
      new WallSignBlock(
          Blocks.GREEN_TERRACOTTA, FabricBlockSettings.copyOf(Blocks.GREEN_TERRACOTTA));

  @RegisterIdentifier
  public static final WallSignBlock RED_TERRACOTTA_WALL_SIGN =
      new WallSignBlock(Blocks.RED_TERRACOTTA, FabricBlockSettings.copyOf(Blocks.RED_TERRACOTTA));

  @RegisterIdentifier
  public static final WallSignBlock BLACK_TERRACOTTA_WALL_SIGN =
      new WallSignBlock(
          Blocks.BLACK_TERRACOTTA, FabricBlockSettings.copyOf(Blocks.BLACK_TERRACOTTA));

  public static final Map<DyeColor, WallSignBlock> TERRACOTTA_WALL_SIGNS =
      Util.make(
          new EnumMap<>(DyeColor.class),
          map -> {
            map.put(DyeColor.WHITE, WHITE_TERRACOTTA_WALL_SIGN);
            map.put(DyeColor.ORANGE, ORANGE_TERRACOTTA_WALL_SIGN);
            map.put(DyeColor.MAGENTA, MAGENTA_TERRACOTTA_WALL_SIGN);
            map.put(DyeColor.LIGHT_BLUE, LIGHT_BLUE_TERRACOTTA_WALL_SIGN);
            map.put(DyeColor.YELLOW, YELLOW_TERRACOTTA_WALL_SIGN);
            map.put(DyeColor.LIME, LIME_TERRACOTTA_WALL_SIGN);
            map.put(DyeColor.PINK, PINK_TERRACOTTA_WALL_SIGN);
            map.put(DyeColor.GRAY, GRAY_TERRACOTTA_WALL_SIGN);
            map.put(DyeColor.LIGHT_GRAY, LIGHT_GRAY_TERRACOTTA_WALL_SIGN);
            map.put(DyeColor.CYAN, CYAN_TERRACOTTA_WALL_SIGN);
            map.put(DyeColor.PURPLE, PURPLE_TERRACOTTA_WALL_SIGN);
            map.put(DyeColor.BLUE, BLUE_TERRACOTTA_WALL_SIGN);
            map.put(DyeColor.BROWN, BROWN_TERRACOTTA_WALL_SIGN);
            map.put(DyeColor.GREEN, GREEN_TERRACOTTA_WALL_SIGN);
            map.put(DyeColor.RED, RED_TERRACOTTA_WALL_SIGN);
            map.put(DyeColor.BLACK, BLACK_TERRACOTTA_WALL_SIGN);
          });

  @RegisterIdentifier
  public static final GlowingWallSignBlock GLOWING_WHITE_CONCRETE_WALL_SIGN =
      new GlowingWallSignBlock(
          Blocks.WHITE_CONCRETE, FabricBlockSettings.copyOf(Blocks.WHITE_CONCRETE).luminance(15));

  @RegisterIdentifier
  public static final GlowingWallSignBlock GLOWING_ORANGE_CONCRETE_WALL_SIGN =
      new GlowingWallSignBlock(
          Blocks.ORANGE_CONCRETE, FabricBlockSettings.copyOf(Blocks.ORANGE_CONCRETE).luminance(15));

  @RegisterIdentifier
  public static final GlowingWallSignBlock GLOWING_MAGENTA_CONCRETE_WALL_SIGN =
      new GlowingWallSignBlock(
          Blocks.MAGENTA_CONCRETE,
          FabricBlockSettings.copyOf(Blocks.MAGENTA_CONCRETE).luminance(15));

  @RegisterIdentifier
  public static final GlowingWallSignBlock GLOWING_LIGHT_BLUE_CONCRETE_WALL_SIGN =
      new GlowingWallSignBlock(
          Blocks.LIGHT_BLUE_CONCRETE,
          FabricBlockSettings.copyOf(Blocks.LIGHT_BLUE_CONCRETE).luminance(15));

  @RegisterIdentifier
  public static final GlowingWallSignBlock GLOWING_YELLOW_CONCRETE_WALL_SIGN =
      new GlowingWallSignBlock(
          Blocks.YELLOW_CONCRETE, FabricBlockSettings.copyOf(Blocks.YELLOW_CONCRETE).luminance(15));

  @RegisterIdentifier
  public static final GlowingWallSignBlock GLOWING_LIME_CONCRETE_WALL_SIGN =
      new GlowingWallSignBlock(
          Blocks.LIME_CONCRETE, FabricBlockSettings.copyOf(Blocks.LIME_CONCRETE).luminance(15));

  @RegisterIdentifier
  public static final GlowingWallSignBlock GLOWING_PINK_CONCRETE_WALL_SIGN =
      new GlowingWallSignBlock(
          Blocks.PINK_CONCRETE, FabricBlockSettings.copyOf(Blocks.PINK_CONCRETE).luminance(15));

  @RegisterIdentifier
  public static final GlowingWallSignBlock GLOWING_GRAY_CONCRETE_WALL_SIGN =
      new GlowingWallSignBlock(
          Blocks.GRAY_CONCRETE, FabricBlockSettings.copyOf(Blocks.GRAY_CONCRETE).luminance(15));

  @RegisterIdentifier
  public static final GlowingWallSignBlock GLOWING_LIGHT_GRAY_CONCRETE_WALL_SIGN =
      new GlowingWallSignBlock(
          Blocks.LIGHT_GRAY_CONCRETE,
          FabricBlockSettings.copyOf(Blocks.LIGHT_GRAY_CONCRETE).luminance(15));

  @RegisterIdentifier
  public static final GlowingWallSignBlock GLOWING_CYAN_CONCRETE_WALL_SIGN =
      new GlowingWallSignBlock(
          Blocks.CYAN_CONCRETE, FabricBlockSettings.copyOf(Blocks.CYAN_CONCRETE).luminance(15));

  @RegisterIdentifier
  public static final GlowingWallSignBlock GLOWING_PURPLE_CONCRETE_WALL_SIGN =
      new GlowingWallSignBlock(
          Blocks.PURPLE_CONCRETE, FabricBlockSettings.copyOf(Blocks.PURPLE_CONCRETE).luminance(15));

  @RegisterIdentifier
  public static final GlowingWallSignBlock GLOWING_BLUE_CONCRETE_WALL_SIGN =
      new GlowingWallSignBlock(
          Blocks.BLUE_CONCRETE, FabricBlockSettings.copyOf(Blocks.BLUE_CONCRETE).luminance(15));

  @RegisterIdentifier
  public static final GlowingWallSignBlock GLOWING_BROWN_CONCRETE_WALL_SIGN =
      new GlowingWallSignBlock(
          Blocks.BROWN_CONCRETE, FabricBlockSettings.copyOf(Blocks.BROWN_CONCRETE).luminance(15));

  @RegisterIdentifier
  public static final GlowingWallSignBlock GLOWING_GREEN_CONCRETE_WALL_SIGN =
      new GlowingWallSignBlock(
          Blocks.GREEN_CONCRETE, FabricBlockSettings.copyOf(Blocks.GREEN_CONCRETE).luminance(15));

  @RegisterIdentifier
  public static final GlowingWallSignBlock GLOWING_RED_CONCRETE_WALL_SIGN =
      new GlowingWallSignBlock(
          Blocks.RED_CONCRETE, FabricBlockSettings.copyOf(Blocks.RED_CONCRETE).luminance(15));

  @RegisterIdentifier
  public static final GlowingWallSignBlock GLOWING_BLACK_CONCRETE_WALL_SIGN =
      new GlowingWallSignBlock(
          Blocks.BLACK_CONCRETE, FabricBlockSettings.copyOf(Blocks.BLACK_CONCRETE).luminance(15));

  public static final Map<DyeColor, GlowingWallSignBlock> GLOWING_CONCRETE_WALL_SIGNS =
      Util.make(
          new EnumMap<>(DyeColor.class),
          map -> {
            map.put(DyeColor.WHITE, GLOWING_WHITE_CONCRETE_WALL_SIGN);
            map.put(DyeColor.ORANGE, GLOWING_ORANGE_CONCRETE_WALL_SIGN);
            map.put(DyeColor.MAGENTA, GLOWING_MAGENTA_CONCRETE_WALL_SIGN);
            map.put(DyeColor.LIGHT_BLUE, GLOWING_LIGHT_BLUE_CONCRETE_WALL_SIGN);
            map.put(DyeColor.YELLOW, GLOWING_YELLOW_CONCRETE_WALL_SIGN);
            map.put(DyeColor.LIME, GLOWING_LIME_CONCRETE_WALL_SIGN);
            map.put(DyeColor.PINK, GLOWING_PINK_CONCRETE_WALL_SIGN);
            map.put(DyeColor.GRAY, GLOWING_GRAY_CONCRETE_WALL_SIGN);
            map.put(DyeColor.LIGHT_GRAY, GLOWING_LIGHT_GRAY_CONCRETE_WALL_SIGN);
            map.put(DyeColor.CYAN, GLOWING_CYAN_CONCRETE_WALL_SIGN);
            map.put(DyeColor.PURPLE, GLOWING_PURPLE_CONCRETE_WALL_SIGN);
            map.put(DyeColor.BLUE, GLOWING_BLUE_CONCRETE_WALL_SIGN);
            map.put(DyeColor.BROWN, GLOWING_BROWN_CONCRETE_WALL_SIGN);
            map.put(DyeColor.GREEN, GLOWING_GREEN_CONCRETE_WALL_SIGN);
            map.put(DyeColor.RED, GLOWING_RED_CONCRETE_WALL_SIGN);
            map.put(DyeColor.BLACK, GLOWING_BLACK_CONCRETE_WALL_SIGN);
          });

  @RegisterIdentifier
  public static final GlowingWallSignBlock GLOWING_WHITE_TERRACOTTA_WALL_SIGN =
      new GlowingWallSignBlock(
          Blocks.WHITE_TERRACOTTA,
          FabricBlockSettings.copyOf(Blocks.WHITE_TERRACOTTA).luminance(15));

  @RegisterIdentifier
  public static final GlowingWallSignBlock GLOWING_ORANGE_TERRACOTTA_WALL_SIGN =
      new GlowingWallSignBlock(
          Blocks.ORANGE_TERRACOTTA,
          FabricBlockSettings.copyOf(Blocks.ORANGE_TERRACOTTA).luminance(15));

  @RegisterIdentifier
  public static final GlowingWallSignBlock GLOWING_MAGENTA_TERRACOTTA_WALL_SIGN =
      new GlowingWallSignBlock(
          Blocks.MAGENTA_TERRACOTTA,
          FabricBlockSettings.copyOf(Blocks.MAGENTA_TERRACOTTA).luminance(15));

  @RegisterIdentifier
  public static final GlowingWallSignBlock GLOWING_LIGHT_BLUE_TERRACOTTA_WALL_SIGN =
      new GlowingWallSignBlock(
          Blocks.LIGHT_BLUE_TERRACOTTA,
          FabricBlockSettings.copyOf(Blocks.LIGHT_BLUE_TERRACOTTA).luminance(15));

  @RegisterIdentifier
  public static final GlowingWallSignBlock GLOWING_YELLOW_TERRACOTTA_WALL_SIGN =
      new GlowingWallSignBlock(
          Blocks.YELLOW_TERRACOTTA,
          FabricBlockSettings.copyOf(Blocks.YELLOW_TERRACOTTA).luminance(15));

  @RegisterIdentifier
  public static final GlowingWallSignBlock GLOWING_LIME_TERRACOTTA_WALL_SIGN =
      new GlowingWallSignBlock(
          Blocks.LIME_TERRACOTTA, FabricBlockSettings.copyOf(Blocks.LIME_TERRACOTTA).luminance(15));

  @RegisterIdentifier
  public static final GlowingWallSignBlock GLOWING_PINK_TERRACOTTA_WALL_SIGN =
      new GlowingWallSignBlock(
          Blocks.PINK_TERRACOTTA, FabricBlockSettings.copyOf(Blocks.PINK_TERRACOTTA).luminance(15));

  @RegisterIdentifier
  public static final GlowingWallSignBlock GLOWING_GRAY_TERRACOTTA_WALL_SIGN =
      new GlowingWallSignBlock(
          Blocks.GRAY_TERRACOTTA, FabricBlockSettings.copyOf(Blocks.GRAY_TERRACOTTA).luminance(15));

  @RegisterIdentifier
  public static final GlowingWallSignBlock GLOWING_LIGHT_GRAY_TERRACOTTA_WALL_SIGN =
      new GlowingWallSignBlock(
          Blocks.LIGHT_GRAY_TERRACOTTA,
          FabricBlockSettings.copyOf(Blocks.LIGHT_GRAY_TERRACOTTA).luminance(15));

  @RegisterIdentifier
  public static final GlowingWallSignBlock GLOWING_CYAN_TERRACOTTA_WALL_SIGN =
      new GlowingWallSignBlock(
          Blocks.CYAN_TERRACOTTA, FabricBlockSettings.copyOf(Blocks.CYAN_TERRACOTTA).luminance(15));

  @RegisterIdentifier
  public static final GlowingWallSignBlock GLOWING_PURPLE_TERRACOTTA_WALL_SIGN =
      new GlowingWallSignBlock(
          Blocks.PURPLE_TERRACOTTA,
          FabricBlockSettings.copyOf(Blocks.PURPLE_TERRACOTTA).luminance(15));

  @RegisterIdentifier
  public static final GlowingWallSignBlock GLOWING_BLUE_TERRACOTTA_WALL_SIGN =
      new GlowingWallSignBlock(
          Blocks.BLUE_TERRACOTTA, FabricBlockSettings.copyOf(Blocks.BLUE_TERRACOTTA).luminance(15));

  @RegisterIdentifier
  public static final GlowingWallSignBlock GLOWING_BROWN_TERRACOTTA_WALL_SIGN =
      new GlowingWallSignBlock(
          Blocks.BROWN_TERRACOTTA,
          FabricBlockSettings.copyOf(Blocks.BROWN_TERRACOTTA).luminance(15));

  @RegisterIdentifier
  public static final GlowingWallSignBlock GLOWING_GREEN_TERRACOTTA_WALL_SIGN =
      new GlowingWallSignBlock(
          Blocks.GREEN_TERRACOTTA,
          FabricBlockSettings.copyOf(Blocks.GREEN_TERRACOTTA).luminance(15));

  @RegisterIdentifier
  public static final GlowingWallSignBlock GLOWING_RED_TERRACOTTA_WALL_SIGN =
      new GlowingWallSignBlock(
          Blocks.RED_TERRACOTTA, FabricBlockSettings.copyOf(Blocks.RED_TERRACOTTA).luminance(15));

  @RegisterIdentifier
  public static final GlowingWallSignBlock GLOWING_BLACK_TERRACOTTA_WALL_SIGN =
      new GlowingWallSignBlock(
          Blocks.BLACK_TERRACOTTA,
          FabricBlockSettings.copyOf(Blocks.BLACK_TERRACOTTA).luminance(15));

  public static final Map<DyeColor, GlowingWallSignBlock> GLOWING_TERRACOTTA_WALL_SIGNS =
      Util.make(
          new EnumMap<>(DyeColor.class),
          map -> {
            map.put(DyeColor.WHITE, GLOWING_WHITE_TERRACOTTA_WALL_SIGN);
            map.put(DyeColor.ORANGE, GLOWING_ORANGE_TERRACOTTA_WALL_SIGN);
            map.put(DyeColor.MAGENTA, GLOWING_MAGENTA_TERRACOTTA_WALL_SIGN);
            map.put(DyeColor.LIGHT_BLUE, GLOWING_LIGHT_BLUE_TERRACOTTA_WALL_SIGN);
            map.put(DyeColor.YELLOW, GLOWING_YELLOW_TERRACOTTA_WALL_SIGN);
            map.put(DyeColor.LIME, GLOWING_LIME_TERRACOTTA_WALL_SIGN);
            map.put(DyeColor.PINK, GLOWING_PINK_TERRACOTTA_WALL_SIGN);
            map.put(DyeColor.GRAY, GLOWING_GRAY_TERRACOTTA_WALL_SIGN);
            map.put(DyeColor.LIGHT_GRAY, GLOWING_LIGHT_GRAY_TERRACOTTA_WALL_SIGN);
            map.put(DyeColor.CYAN, GLOWING_CYAN_TERRACOTTA_WALL_SIGN);
            map.put(DyeColor.PURPLE, GLOWING_PURPLE_TERRACOTTA_WALL_SIGN);
            map.put(DyeColor.BLUE, GLOWING_BLUE_TERRACOTTA_WALL_SIGN);
            map.put(DyeColor.BROWN, GLOWING_BROWN_TERRACOTTA_WALL_SIGN);
            map.put(DyeColor.GREEN, GLOWING_GREEN_TERRACOTTA_WALL_SIGN);
            map.put(DyeColor.RED, GLOWING_RED_TERRACOTTA_WALL_SIGN);
            map.put(DyeColor.BLACK, GLOWING_BLACK_TERRACOTTA_WALL_SIGN);
          });

  /** 绝大多数柏油路方块共用的方块设置。 */
  private static final FabricBlockSettings ASPHALT_ROAD_SETTINGS =
      FabricBlockSettings.of(Material.STONE, MapColor.GRAY).strength(0.5F);
  /**
   *
   *
   * <h1>道路方块部分</h1>
   *
   * 最基本的普通路块。
   */
  @InGroup("roads")
  @RegisterIdentifier
  public static final RoadBlock ASPHALT_ROAD_BLOCK = new RoadBlock(ASPHALT_ROAD_SETTINGS);
  /**
   *
   *
   * <h2>单直线道路</h2>
   *
   * 白色直线。
   */
  @Cutout @RegisterIdentifier
  public static final RoadWithStraightLine.Impl ASPHALT_ROAD_WITH_WHITE_STRAIGHT_LINE =
      new RoadWithStraightLine.Impl(ASPHALT_ROAD_SETTINGS, LineColor.WHITE);
  /** 白色直角。 */
  @Cutout @RegisterIdentifier
  public static final RoadWithAngleLine.Impl ASPHALT_ROAD_WITH_WHITE_RIGHT_ANGLE_LINE =
      new RoadWithAngleLine.Impl(ASPHALT_ROAD_SETTINGS, LineColor.WHITE, false);
  /** 白色斜角。 */
  @Cutout @RegisterIdentifier
  public static final RoadWithAngleLine.Impl ASPHALT_ROAD_WITH_WHITE_BEVEL_ANGLE_LINE =
      new RoadWithAngleLine.Impl(ASPHALT_ROAD_SETTINGS, LineColor.WHITE, true);
  /** 上面三种白色对应的黄色 */
  @Cutout @RegisterIdentifier
  public static final RoadWithStraightLine.Impl ASPHALT_ROAD_WITH_YELLOW_STRAIGHT_LINE =
      new RoadWithStraightLine.Impl(ASPHALT_ROAD_SETTINGS, LineColor.YELLOW);

  @Cutout @RegisterIdentifier
  public static final RoadWithAngleLine.Impl ASPHALT_ROAD_WITH_YELLOW_RIGHT_ANGLE_LINE =
      new RoadWithAngleLine.Impl(ASPHALT_ROAD_SETTINGS, LineColor.YELLOW, false);

  @Cutout @RegisterIdentifier
  public static final RoadWithAngleLine.Impl ASPHALT_ROAD_WITH_YELLOW_BEVEL_ANGLE_LINE =
      new RoadWithAngleLine.Impl(ASPHALT_ROAD_SETTINGS, LineColor.YELLOW, true);
  /**
   *
   *
   * <h2>由两条线组成的道路</h2>
   *
   * 白色直角+斜角。
   */
  @Cutout @RegisterIdentifier
  public static final RoadWithStraightAndAngleLine.Impl
      ASPHALT_ROAD_WITH_WHITE_STRAIGHT_AND_BEVEL_ANGLE_LINE =
          new RoadWithStraightAndAngleLine.Impl(ASPHALT_ROAD_SETTINGS, LineColor.WHITE);
  /** 白色T字形线。 */
  @Cutout @RegisterIdentifier
  public static final RoadWithJointLine.Impl ASPHALT_ROAD_WITH_WHITE_JOINT_LINE =
      new RoadWithJointLine.Impl(ASPHALT_ROAD_SETTINGS, LineColor.WHITE);
  /** 白色十字交叉线。 */
  @Cutout @RegisterIdentifier
  public static final RoadWithCrossLine.Impl ASPHALT_ROAD_WITH_WHITE_CROSS_LINE =
      new RoadWithCrossLine.Impl(ASPHALT_ROAD_SETTINGS, LineColor.WHITE);
  /**
   *
   *
   * <h2>带有特殊线的单线道路</h2>
   *
   * 偏移的直线。
   */
  @Cutout @RegisterIdentifier
  public static final RoadWithOffsetStraightLine.Impl ASPHALT_ROAD_WITH_WHITE_OFFSET_STRAIGHT_LINE =
      new RoadWithOffsetStraightLine.Impl(ASPHALT_ROAD_SETTINGS, LineColor.WHITE);
  /** 双线。 */
  @Cutout @RegisterIdentifier
  public static final RoadWithStraightLine.Impl ASPHALT_ROAD_WITH_WHITE_STRAIGHT_DOUBLE_LINE =
      new RoadWithStraightLine.Impl(ASPHALT_ROAD_SETTINGS, LineColor.WHITE);
  /** 粗线。 */
  @Cutout @RegisterIdentifier
  public static final RoadWithStraightLine.Impl ASPHALT_ROAD_WITH_WHITE_STRAIGHT_THICK_LINE =
      new RoadWithStraightLine.Impl(ASPHALT_ROAD_SETTINGS, LineColor.WHITE);
  /** 一侧偏移的直角。 */
  @Cutout @RegisterIdentifier
  public static final RoadWithAngleLineWithOnePartOffset.Impl
      ASPHALT_ROAD_WITH_WHITE_RIGHT_ANGLE_LINE_WITH_ONE_PART_OFFSET_OUT =
          new RoadWithAngleLineWithOnePartOffset.Impl(
              ASPHALT_ROAD_SETTINGS, LineColor.WHITE, false);

  @Cutout @RegisterIdentifier
  public static final RoadWithAngleLineWithOnePartOffset.Impl
      ASPHALT_ROAD_WITH_WHITE_RIGHT_ANGLE_LINE_WITH_ONE_PART_OFFSET_IN =
          new RoadWithAngleLineWithOnePartOffset.Impl(
              ASPHALT_ROAD_SETTINGS, LineColor.WHITE, false);
  /**
   *
   *
   * <h2>带有特殊线的双线道路</h2>
   *
   * T字形，其中单侧部分为双线。
   */
  @Cutout @RegisterIdentifier
  public static final RoadWithJointLine.Impl ASPHALT_ROAD_WITH_WHITE_JOINT_LINE_WITH_DOUBLE_SIDE =
      new RoadWithJointLine.Impl(ASPHALT_ROAD_SETTINGS, LineColor.WHITE);
  /** T字形，其中单侧部分为粗线。 */
  @Cutout @RegisterIdentifier
  public static final RoadWithJointLine.Impl ASPHALT_ROAD_WITH_WHITE_JOINT_LINE_WITH_THICK_SIDE =
      new RoadWithJointLine.Impl(ASPHALT_ROAD_SETTINGS, LineColor.WHITE);
  /** T字形，其中单侧部分有偏移。 */
  @Cutout @RegisterIdentifier
  public static final RoadWithJointLineWithOffsetSide.Impl
      ASPHALT_ROAD_WITH_WHITE_JOINT_LINE_WITH_OFFSET_SIDE =
          new RoadWithJointLineWithOffsetSide.Impl(ASPHALT_ROAD_SETTINGS, LineColor.WHITE);
  /**
   *
   *
   * <h2>自动路块</h2>
   *
   * 斜角自动路块。放置后遇到方块更新会自动确定线路走向。
   */
  @RegisterIdentifier @Cutout
  public static final RoadWithAutoLine.Impl ASPHALT_ROAD_WITH_WHITE_AUTO_BEVEL_ANGLE_LINE =
      new RoadWithAutoLine.Impl(
          ASPHALT_ROAD_SETTINGS,
          RoadWithAutoLine.RoadAutoLineType.BEVEL,
          RoadTexture.ASPHALT,
          LineColor.WHITE);
  /** 直角自动路块。 */
  @RegisterIdentifier @Cutout
  public static final RoadWithAutoLine.Impl ASPHALT_ROAD_WITH_WHITE_AUTO_RIGHT_ANGLE_LINE =
      new RoadWithAutoLine.Impl(
          ASPHALT_ROAD_SETTINGS,
          RoadWithAutoLine.RoadAutoLineType.RIGHT_ANGLE,
          RoadTexture.ASPHALT,
          LineColor.WHITE);
  /**
   *
   *
   * <h2>其他</h2>
   *
   * 填满的路块。
   */
  @RegisterIdentifier
  public static final RoadBlock ASPHALT_ROAD_FILLED_WITH_WHITE =
      new RoadBlock(
          FabricBlockSettings.copyOf(ASPHALT_ROAD_SETTINGS).materialColor(MapColor.WHITE));
  /**
   *
   *
   * <h2>其他</h2>
   *
   * @see #ASPHALT_ROAD_FILLED_WITH_WHITE
   */
  @RegisterIdentifier
  public static final RoadSlabBlock ASPHALT_ROAD_SLAB_FILLED_WITH_WHITE =
      new RoadSlabBlock(
          FabricBlockSettings.copyOf(MishangucBlocks.ASPHALT_ROAD_FILLED_WITH_WHITE),
          LineColor.WHITE);
  /**
   *
   *
   * <h1>道路台阶部分</h1>
   *
   * 道路方块对应的台阶。
   *
   * @see #ASPHALT_ROAD_BLOCK
   */
  @RegisterIdentifier
  public static final RoadSlabBlock ASPHALT_ROAD_SLAB =
      new RoadSlabBlock(MishangucBlocks.ASPHALT_ROAD_SETTINGS, LineColor.NONE);
  /**
   *
   *
   * <h2>单直线道路台阶</h2>
   *
   * @see #ASPHALT_ROAD_WITH_WHITE_STRAIGHT_LINE
   */
  @InGroup("roads")
  @Cutout
  @RegisterIdentifier
  public static final RoadWithStraightLine.SlabImpl ASPHALT_ROAD_SLAB_WITH_WHITE_STRAIGHT_LINE =
      new RoadWithStraightLine.SlabImpl(MishangucBlocks.ASPHALT_ROAD_SETTINGS, LineColor.WHITE);
  /** @see #ASPHALT_ROAD_WITH_WHITE_RIGHT_ANGLE_LINE */
  @Cutout @RegisterIdentifier
  public static final RoadWithAngleLine.SlabImpl ASPHALT_ROAD_SLAB_WITH_WHITE_RIGHT_ANGLE_LINE =
      new RoadWithAngleLine.SlabImpl(MishangucBlocks.ASPHALT_ROAD_SETTINGS, LineColor.WHITE, false);
  /** @see #ASPHALT_ROAD_WITH_WHITE_BEVEL_ANGLE_LINE */
  @Cutout @RegisterIdentifier
  public static final RoadWithAngleLine.SlabImpl ASPHALT_ROAD_SLAB_WITH_WHITE_BEVEL_ANGLE_LINE =
      new RoadWithAngleLine.SlabImpl(MishangucBlocks.ASPHALT_ROAD_SETTINGS, LineColor.WHITE, true);
  /**
   *
   *
   * <h2>由两条线组成的道路台阶</h2>
   *
   * @see #ASPHALT_ROAD_WITH_WHITE_STRAIGHT_AND_BEVEL_ANGLE_LINE
   */
  @Cutout @RegisterIdentifier
  public static final RoadWithStraightAndAngleLine.SlabImpl
      ASPHALT_ROAD_SLAB_WITH_WHITE_STRAIGHT_AND_BEVEL_ANGLE_LINE =
          new RoadWithStraightAndAngleLine.SlabImpl(
              MishangucBlocks.ASPHALT_ROAD_SETTINGS, LineColor.WHITE);
  /** @see #ASPHALT_ROAD_WITH_WHITE_JOINT_LINE */
  @Cutout @RegisterIdentifier
  public static final RoadWithJointLine.SlabImpl ASPHALT_ROAD_SLAB_WITH_WHITE_JOINT_LINE =
      new RoadWithJointLine.SlabImpl(MishangucBlocks.ASPHALT_ROAD_SETTINGS, LineColor.WHITE);
  /** @see #ASPHALT_ROAD_WITH_WHITE_CROSS_LINE */
  @Cutout @RegisterIdentifier
  public static final RoadWithCrossLine.SlabImpl ASPHALT_ROAD_SLAB_WITH_WHITE_CROSS_LINE =
      new RoadWithCrossLine.SlabImpl(MishangucBlocks.ASPHALT_ROAD_SETTINGS, LineColor.WHITE);
  /**
   *
   *
   * <h2>带有特殊线的单线道路台阶。</h2>
   *
   * @see #ASPHALT_ROAD_WITH_WHITE_OFFSET_STRAIGHT_LINE
   */
  @Cutout @RegisterIdentifier
  public static final RoadWithOffsetStraightLine.SlabImpl
      ASPHALT_ROAD_SLAB_WITH_WHITE_OFFSET_STRAIGHT_LINE =
          new RoadWithOffsetStraightLine.SlabImpl(
              MishangucBlocks.ASPHALT_ROAD_SETTINGS, LineColor.WHITE);
  /** @see #ASPHALT_ROAD_WITH_WHITE_STRAIGHT_DOUBLE_LINE */
  @Cutout @RegisterIdentifier
  public static final RoadWithStraightLine.SlabImpl
      ASPHALT_ROAD_SLAB_WITH_WHITE_STRAIGHT_DOUBLE_LINE =
          new RoadWithStraightLine.SlabImpl(MishangucBlocks.ASPHALT_ROAD_SETTINGS, LineColor.WHITE);
  /** @see #ASPHALT_ROAD_WITH_WHITE_STRAIGHT_THICK_LINE */
  @Cutout @RegisterIdentifier
  public static final RoadWithStraightLine.SlabImpl
      ASPHALT_ROAD_SLAB_WITH_WHITE_STRAIGHT_THICK_LINE =
          new RoadWithStraightLine.SlabImpl(MishangucBlocks.ASPHALT_ROAD_SETTINGS, LineColor.WHITE);
  /**
   * 一侧偏移的直角。
   *
   * @see #ASPHALT_ROAD_WITH_WHITE_RIGHT_ANGLE_LINE_WITH_ONE_PART_OFFSET_OUT
   */
  @Cutout @RegisterIdentifier
  public static final RoadWithAngleLineWithOnePartOffset.SlabImpl
      ASPHALT_ROAD_SLAB_WITH_WHITE_RIGHT_ANGLE_LINE_WITH_ONE_PART_OFFSET_OUT =
          new RoadWithAngleLineWithOnePartOffset.SlabImpl(
              ASPHALT_ROAD_SETTINGS, LineColor.WHITE, false);
  /** @see #ASPHALT_ROAD_WITH_WHITE_RIGHT_ANGLE_LINE_WITH_ONE_PART_OFFSET_IN */
  @Cutout @RegisterIdentifier
  public static final RoadWithAngleLineWithOnePartOffset.SlabImpl
      ASPHALT_ROAD_SLAB_WITH_WHITE_RIGHT_ANGLE_LINE_WITH_ONE_PART_OFFSET_IN =
          new RoadWithAngleLineWithOnePartOffset.SlabImpl(
              ASPHALT_ROAD_SETTINGS, LineColor.WHITE, false);
  /**
   *
   *
   * <h2>带有特殊线的双线道路台阶</h2>
   *
   * @see #ASPHALT_ROAD_WITH_WHITE_JOINT_LINE_WITH_DOUBLE_SIDE
   */
  @Cutout @RegisterIdentifier
  public static final RoadWithJointLine.SlabImpl
      ASPHALT_ROAD_SLAB_WITH_WHITE_JOINT_LINE_WITH_DOUBLE_SIDE =
          new RoadWithJointLine.SlabImpl(MishangucBlocks.ASPHALT_ROAD_SETTINGS, LineColor.WHITE);
  /** @see #ASPHALT_ROAD_WITH_WHITE_JOINT_LINE_WITH_THICK_SIDE */
  @Cutout @RegisterIdentifier
  public static final RoadWithJointLine.SlabImpl
      ASPHALT_ROAD_SLAB_WITH_WHITE_JOINT_LINE_WITH_THICK_SIDE =
          new RoadWithJointLine.SlabImpl(MishangucBlocks.ASPHALT_ROAD_SETTINGS, LineColor.WHITE);
  /** @see #ASPHALT_ROAD_WITH_WHITE_JOINT_LINE_WITH_OFFSET_SIDE */
  @Cutout @RegisterIdentifier
  public static final RoadWithJointLineWithOffsetSide.SlabImpl
      ASPHALT_ROAD_SLAB_WITH_WHITE_JOINT_LINE_WITH_OFFSET_SIDE =
          new RoadWithJointLineWithOffsetSide.SlabImpl(ASPHALT_ROAD_SETTINGS, LineColor.WHITE);
  /**
   *
   *
   * <h2>自动路台阶</h2>
   *
   * @see #ASPHALT_ROAD_WITH_WHITE_AUTO_BEVEL_ANGLE_LINE
   */
  @RegisterIdentifier @Cutout
  public static final RoadWithAutoLine.SlabImpl ASPHALT_ROAD_SLAB_WITH_WHITE_AUTO_BEVEL_ANGLE_LINE =
      new RoadWithAutoLine.SlabImpl(
          MishangucBlocks.ASPHALT_ROAD_SETTINGS,
          RoadWithAutoLine.RoadAutoLineType.BEVEL,
          RoadTexture.ASPHALT,
          LineColor.WHITE);
  /** @see #ASPHALT_ROAD_WITH_WHITE_AUTO_RIGHT_ANGLE_LINE */
  @RegisterIdentifier @Cutout
  public static final RoadWithAutoLine.SlabImpl ASPHALT_ROAD_SLAB_WITH_WHITE_AUTO_RIGHT_ANGLE_LINE =
      new RoadWithAutoLine.SlabImpl(
          ASPHALT_ROAD_SETTINGS,
          RoadWithAutoLine.RoadAutoLineType.RIGHT_ANGLE,
          RoadTexture.ASPHALT,
          LineColor.WHITE);
  /**
   *
   *
   * <h1>光源部分</h1>
   *
   * 所有灯方块都会用到的方块设置。
   */
  private static final FabricBlockSettings WHITE_LIGHT_SETTINGS =
      FabricBlockSettings.of(Material.REDSTONE_LAMP).luminance(15);

  @RegisterIdentifier
  @InGroup("lights")
  public static final Block WHITE_LIGHT = new Block(WHITE_LIGHT_SETTINGS);
  /** 墙上的灯等方块等用到的方块设置。与{@link #WHITE_LIGHT_SETTINGS}相比，该方块设置具有{@code noCollision}属性。 */
  private static final FabricBlockSettings WHITE_WALL_LIGHT_SETTINGS =
      FabricBlockSettings.copyOf(WHITE_LIGHT_SETTINGS).noCollision();

  @RegisterIdentifier
  public static final WallLightBlock WHITE_SMALL_WALL_LIGHT_TUBE =
      new WallLightBlock(WHITE_WALL_LIGHT_SETTINGS);

  @RegisterIdentifier
  public static final WallLightBlock WHITE_LARGE_WALL_LIGHT_TUBE =
      new WallLightBlock(WHITE_WALL_LIGHT_SETTINGS);

  @RegisterIdentifier
  public static final StripWallLightBlock WHITE_THIN_STRIP_WALL_LIGHT_TUBE =
      new StripWallLightBlock(WHITE_WALL_LIGHT_SETTINGS);

  @RegisterIdentifier
  public static final StripWallLightBlock WHITE_THICK_STRIP_WALL_LIGHT_TUBE =
      new StripWallLightBlock(WHITE_WALL_LIGHT_SETTINGS);

  @RegisterIdentifier
  public static final StripWallLightBlock WHITE_DOUBLE_STRIP_WALL_LIGHT_TUBE =
      new StripWallLightBlock(WHITE_WALL_LIGHT_SETTINGS);

  @RegisterIdentifier
  public static final CornerLightBlock WHITE_THIN_STRIP_CORNER_LIGHT_TUBE =
      new CornerLightBlock(WHITE_WALL_LIGHT_SETTINGS);

  @RegisterIdentifier
  public static final CornerLightBlock WHITE_THICK_STRIP_CORNER_LIGHT_TUBE =
      new CornerLightBlock(WHITE_WALL_LIGHT_SETTINGS);

  @RegisterIdentifier
  public static final CornerLightBlock WHITE_DOUBLE_STRIP_CORNER_LIGHT_TUBE =
      new CornerLightBlock(WHITE_WALL_LIGHT_SETTINGS);

  @RegisterIdentifier
  public static final WallLightBlock WHITE_SMALL_WALL_LIGHT =
      new WallLightBlock(WHITE_WALL_LIGHT_SETTINGS);

  @RegisterIdentifier
  public static final WallLightBlock WHITE_LARGE_WALL_LIGHT =
      new WallLightBlock(WHITE_WALL_LIGHT_SETTINGS);

  @RegisterIdentifier
  public static final AutoConnectWallLightBlock WHITE_WALL_LIGHT_SIMPLE_DECORATION =
      new AutoConnectWallLightBlock(WHITE_WALL_LIGHT_SETTINGS);

  @RegisterIdentifier
  public static final AutoConnectWallLightBlock WHITE_WALL_LIGHT_POINT_DECORATION =
      new AutoConnectWallLightBlock(WHITE_WALL_LIGHT_SETTINGS);

  @RegisterIdentifier
  public static final AutoConnectWallLightBlock WHITE_WALL_LIGHT_RHOMBUS_DECORATION =
      new AutoConnectWallLightBlock(WHITE_WALL_LIGHT_SETTINGS);

  @RegisterIdentifier
  public static final AutoConnectWallLightBlock WHITE_WALL_LIGHT_HASH_DECORATION =
      new AutoConnectWallLightBlock(WHITE_WALL_LIGHT_SETTINGS);

  @RegisterIdentifier
  public static final AutoConnectWallLightBlock WHITE_WALL_LIGHT_ROUND_DECORATION =
      new AutoConnectWallLightBlock(WHITE_WALL_LIGHT_SETTINGS) {
        final Map<Direction, VoxelShape> SHAPE_PER_DIRECTION =
            MishangUtils.createDirectionToShape(0, 0, 0, 16, 1, 16);

        @Override
        public VoxelShape getOutlineShape(
            BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
          return SHAPE_PER_DIRECTION.get(state.get(FACING));
        }
      };

  static {
    registerAll();
  }

  private MishangucBlocks() {}

  /**
   * 自动注册一个类中的所有静态常量字段的方块，同时创建并注册对应的物品。
   *
   * @see RegisterIdentifier
   */
  private static void registerAll() {
    // 需要将方块物品放入对应的组。
    @Nullable ItemGroup group = null;

    for (Field field : MishangucBlocks.class.getFields()) {
      int modifier = field.getModifiers();
      if (Modifier.isFinal(modifier)
          && Modifier.isStatic(modifier)
          && Block.class.isAssignableFrom(field.getType())) {
        try {

          // 注册方块。
          Block value = (Block) field.get(null);
          if (field.isAnnotationPresent(RegisterIdentifier.class)) {
            final RegisterIdentifier annotation = field.getAnnotation(RegisterIdentifier.class);
            String path = annotation.value();
            if (path.isEmpty()) {
              path = field.getName().toLowerCase();
            }
            Registry.register(Registry.BLOCK, new Identifier("mishanguc", path), value);
            // 如果找到InGroup注解，则将其放入该组，后面的所有方块也一并放到该组，直到再次发现该注解为止。
            if (field.isAnnotationPresent(InGroup.class)) {
              switch (field.getAnnotation(InGroup.class).value()) {
                case "roads":
                  group = ModItemGroups.ROADS;
                  break;
                case "lights":
                  group = ModItemGroups.LIGHTS;
                  break;
                case "signs":
                  group = ModItemGroups.SIGNS;
                  break;
                default:
                  group = null;
              }
            }
            BlockItem item = new NamedBlockItem(value, new FabricItemSettings().group(group));
            Registry.register(Registry.ITEM, new Identifier("mishanguc", path), item);
          }
        } catch (IllegalAccessException e) {
          MishangUc.MISHANG_LOGGER.error(e);
        }
      }
    }
  }

  public static void init() {}
}
