package pers.solid.mishang.uc.blocks;

import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.Blocks;
import net.minecraft.util.DyeColor;
import net.minecraft.util.Util;
import pers.solid.mishang.uc.annotations.RegisterIdentifier;
import pers.solid.mishang.uc.block.GlowingHungSignBlock;
import pers.solid.mishang.uc.block.HungSignBarBlock;
import pers.solid.mishang.uc.block.HungSignBlock;

import java.util.EnumMap;
import java.util.Map;

public final class HungSignBlocks extends MishangucBlocks {

  /**
   * <h1>告示牌类方块</h1>
   * <p>
   * 具有多种不同颜色。
   */
  @RegisterIdentifier
  public static final HungSignBlock WHITE_CONCRETE_HUNG_SIGN =
      new HungSignBlock(Blocks.WHITE_CONCRETE, FabricBlockSettings.copyOf(Blocks.WHITE_CONCRETE));

  @RegisterIdentifier
  public static final HungSignBlock ORANGE_CONCRETE_HUNG_SIGN =
      new HungSignBlock(Blocks.ORANGE_CONCRETE, FabricBlockSettings.copyOf(Blocks.ORANGE_CONCRETE));

  @RegisterIdentifier
  public static final HungSignBlock MAGENTA_CONCRETE_HUNG_SIGN =
      new HungSignBlock(
          Blocks.MAGENTA_CONCRETE, FabricBlockSettings.copyOf(Blocks.MAGENTA_CONCRETE));

  @RegisterIdentifier
  public static final HungSignBlock LIGHT_BLUE_CONCRETE_HUNG_SIGN =
      new HungSignBlock(
          Blocks.LIGHT_BLUE_CONCRETE, FabricBlockSettings.copyOf(Blocks.LIGHT_BLUE_CONCRETE));

  @RegisterIdentifier
  public static final HungSignBlock YELLOW_CONCRETE_HUNG_SIGN =
      new HungSignBlock(Blocks.YELLOW_CONCRETE, FabricBlockSettings.copyOf(Blocks.YELLOW_CONCRETE));

  @RegisterIdentifier
  public static final HungSignBlock LIME_CONCRETE_HUNG_SIGN =
      new HungSignBlock(Blocks.LIME_CONCRETE, FabricBlockSettings.copyOf(Blocks.LIME_CONCRETE));

  @RegisterIdentifier
  public static final HungSignBlock PINK_CONCRETE_HUNG_SIGN =
      new HungSignBlock(Blocks.PINK_CONCRETE, FabricBlockSettings.copyOf(Blocks.PINK_CONCRETE));

  @RegisterIdentifier
  public static final HungSignBlock GRAY_CONCRETE_HUNG_SIGN =
      new HungSignBlock(Blocks.GRAY_CONCRETE, FabricBlockSettings.copyOf(Blocks.GRAY_CONCRETE));

  @RegisterIdentifier
  public static final HungSignBlock LIGHT_GRAY_CONCRETE_HUNG_SIGN =
      new HungSignBlock(
          Blocks.LIGHT_GRAY_CONCRETE, FabricBlockSettings.copyOf(Blocks.LIGHT_GRAY_CONCRETE));

  @RegisterIdentifier
  public static final HungSignBlock CYAN_CONCRETE_HUNG_SIGN =
      new HungSignBlock(Blocks.CYAN_CONCRETE, FabricBlockSettings.copyOf(Blocks.CYAN_CONCRETE));

  @RegisterIdentifier
  public static final HungSignBlock PURPLE_CONCRETE_HUNG_SIGN =
      new HungSignBlock(Blocks.PURPLE_CONCRETE, FabricBlockSettings.copyOf(Blocks.PURPLE_CONCRETE));

  @RegisterIdentifier
  public static final HungSignBlock BLUE_CONCRETE_HUNG_SIGN =
      new HungSignBlock(Blocks.BLUE_CONCRETE, FabricBlockSettings.copyOf(Blocks.BLUE_CONCRETE));

  @RegisterIdentifier
  public static final HungSignBlock BROWN_CONCRETE_HUNG_SIGN =
      new HungSignBlock(Blocks.BROWN_CONCRETE, FabricBlockSettings.copyOf(Blocks.BROWN_CONCRETE));

  @RegisterIdentifier
  public static final HungSignBlock GREEN_CONCRETE_HUNG_SIGN =
      new HungSignBlock(Blocks.GREEN_CONCRETE, FabricBlockSettings.copyOf(Blocks.GREEN_CONCRETE));

  @RegisterIdentifier
  public static final HungSignBlock RED_CONCRETE_HUNG_SIGN =
      new HungSignBlock(Blocks.RED_CONCRETE, FabricBlockSettings.copyOf(Blocks.RED_CONCRETE));

  @RegisterIdentifier
  public static final HungSignBlock BLACK_CONCRETE_HUNG_SIGN =
      new HungSignBlock(Blocks.BLACK_CONCRETE, FabricBlockSettings.copyOf(Blocks.BLACK_CONCRETE));

  public static final Map<DyeColor, HungSignBlock> CONCRETE_HUNG_SIGNS =
      Util.make(
          new EnumMap<>(DyeColor.class),
          map -> {
            map.put(DyeColor.WHITE, WHITE_CONCRETE_HUNG_SIGN);
            map.put(DyeColor.ORANGE, ORANGE_CONCRETE_HUNG_SIGN);
            map.put(DyeColor.MAGENTA, MAGENTA_CONCRETE_HUNG_SIGN);
            map.put(DyeColor.LIGHT_BLUE, LIGHT_BLUE_CONCRETE_HUNG_SIGN);
            map.put(DyeColor.YELLOW, YELLOW_CONCRETE_HUNG_SIGN);
            map.put(DyeColor.LIME, LIME_CONCRETE_HUNG_SIGN);
            map.put(DyeColor.PINK, PINK_CONCRETE_HUNG_SIGN);
            map.put(DyeColor.GRAY, GRAY_CONCRETE_HUNG_SIGN);
            map.put(DyeColor.LIGHT_GRAY, LIGHT_GRAY_CONCRETE_HUNG_SIGN);
            map.put(DyeColor.CYAN, CYAN_CONCRETE_HUNG_SIGN);
            map.put(DyeColor.PURPLE, PURPLE_CONCRETE_HUNG_SIGN);
            map.put(DyeColor.BLUE, BLUE_CONCRETE_HUNG_SIGN);
            map.put(DyeColor.BROWN, BROWN_CONCRETE_HUNG_SIGN);
            map.put(DyeColor.GREEN, GREEN_CONCRETE_HUNG_SIGN);
            map.put(DyeColor.RED, RED_CONCRETE_HUNG_SIGN);
            map.put(DyeColor.BLACK, BLACK_CONCRETE_HUNG_SIGN);
          });

  @RegisterIdentifier
  public static final HungSignBarBlock WHITE_CONCRETE_HUNG_SIGN_BAR =
      new HungSignBarBlock(WHITE_CONCRETE_HUNG_SIGN);

  @RegisterIdentifier
  public static final HungSignBarBlock ORANGE_CONCRETE_HUNG_SIGN_BAR =
      new HungSignBarBlock(ORANGE_CONCRETE_HUNG_SIGN);

  @RegisterIdentifier
  public static final HungSignBarBlock MAGENTA_CONCRETE_HUNG_SIGN_BAR =
      new HungSignBarBlock(MAGENTA_CONCRETE_HUNG_SIGN);

  @RegisterIdentifier
  public static final HungSignBarBlock LIGHT_BLUE_CONCRETE_HUNG_SIGN_BAR =
      new HungSignBarBlock(LIGHT_BLUE_CONCRETE_HUNG_SIGN);

  @RegisterIdentifier
  public static final HungSignBarBlock YELLOW_CONCRETE_HUNG_SIGN_BAR =
      new HungSignBarBlock(YELLOW_CONCRETE_HUNG_SIGN);

  @RegisterIdentifier
  public static final HungSignBarBlock LIME_CONCRETE_HUNG_SIGN_BAR =
      new HungSignBarBlock(LIME_CONCRETE_HUNG_SIGN);

  @RegisterIdentifier
  public static final HungSignBarBlock PINK_CONCRETE_HUNG_SIGN_BAR =
      new HungSignBarBlock(PINK_CONCRETE_HUNG_SIGN);

  @RegisterIdentifier
  public static final HungSignBarBlock GRAY_CONCRETE_HUNG_SIGN_BAR =
      new HungSignBarBlock(GRAY_CONCRETE_HUNG_SIGN);

  @RegisterIdentifier
  public static final HungSignBarBlock LIGHT_GRAY_CONCRETE_HUNG_SIGN_BAR =
      new HungSignBarBlock(LIGHT_GRAY_CONCRETE_HUNG_SIGN);

  @RegisterIdentifier
  public static final HungSignBarBlock CYAN_CONCRETE_HUNG_SIGN_BAR =
      new HungSignBarBlock(CYAN_CONCRETE_HUNG_SIGN);

  @RegisterIdentifier
  public static final HungSignBarBlock PURPLE_CONCRETE_HUNG_SIGN_BAR =
      new HungSignBarBlock(PURPLE_CONCRETE_HUNG_SIGN);

  @RegisterIdentifier
  public static final HungSignBarBlock BLUE_CONCRETE_HUNG_SIGN_BAR =
      new HungSignBarBlock(BLUE_CONCRETE_HUNG_SIGN);

  @RegisterIdentifier
  public static final HungSignBarBlock BROWN_CONCRETE_HUNG_SIGN_BAR =
      new HungSignBarBlock(BROWN_CONCRETE_HUNG_SIGN);

  @RegisterIdentifier
  public static final HungSignBarBlock GREEN_CONCRETE_HUNG_SIGN_BAR =
      new HungSignBarBlock(GREEN_CONCRETE_HUNG_SIGN);

  @RegisterIdentifier
  public static final HungSignBarBlock RED_CONCRETE_HUNG_SIGN_BAR =
      new HungSignBarBlock(RED_CONCRETE_HUNG_SIGN);

  @RegisterIdentifier
  public static final HungSignBarBlock BLACK_CONCRETE_HUNG_SIGN_BAR =
      new HungSignBarBlock(BLACK_CONCRETE_HUNG_SIGN);

  @RegisterIdentifier
  public static final HungSignBlock WHITE_TERRACOTTA_HUNG_SIGN =
      new HungSignBlock(
          Blocks.WHITE_TERRACOTTA, FabricBlockSettings.copyOf(Blocks.WHITE_TERRACOTTA));

  @RegisterIdentifier
  public static final HungSignBlock ORANGE_TERRACOTTA_HUNG_SIGN =
      new HungSignBlock(
          Blocks.ORANGE_TERRACOTTA, FabricBlockSettings.copyOf(Blocks.ORANGE_TERRACOTTA));

  @RegisterIdentifier
  public static final HungSignBlock MAGENTA_TERRACOTTA_HUNG_SIGN =
      new HungSignBlock(
          Blocks.MAGENTA_TERRACOTTA, FabricBlockSettings.copyOf(Blocks.MAGENTA_TERRACOTTA));

  @RegisterIdentifier
  public static final HungSignBlock LIGHT_BLUE_TERRACOTTA_HUNG_SIGN =
      new HungSignBlock(
          Blocks.LIGHT_BLUE_TERRACOTTA, FabricBlockSettings.copyOf(Blocks.LIGHT_BLUE_TERRACOTTA));

  @RegisterIdentifier
  public static final HungSignBlock YELLOW_TERRACOTTA_HUNG_SIGN =
      new HungSignBlock(
          Blocks.YELLOW_TERRACOTTA, FabricBlockSettings.copyOf(Blocks.YELLOW_TERRACOTTA));

  @RegisterIdentifier
  public static final HungSignBlock LIME_TERRACOTTA_HUNG_SIGN =
      new HungSignBlock(Blocks.LIME_TERRACOTTA, FabricBlockSettings.copyOf(Blocks.LIME_TERRACOTTA));

  @RegisterIdentifier
  public static final HungSignBlock PINK_TERRACOTTA_HUNG_SIGN =
      new HungSignBlock(Blocks.PINK_TERRACOTTA, FabricBlockSettings.copyOf(Blocks.PINK_TERRACOTTA));

  @RegisterIdentifier
  public static final HungSignBlock GRAY_TERRACOTTA_HUNG_SIGN =
      new HungSignBlock(Blocks.GRAY_TERRACOTTA, FabricBlockSettings.copyOf(Blocks.GRAY_TERRACOTTA));

  @RegisterIdentifier
  public static final HungSignBlock LIGHT_GRAY_TERRACOTTA_HUNG_SIGN =
      new HungSignBlock(
          Blocks.LIGHT_GRAY_TERRACOTTA, FabricBlockSettings.copyOf(Blocks.LIGHT_GRAY_TERRACOTTA));

  @RegisterIdentifier
  public static final HungSignBlock CYAN_TERRACOTTA_HUNG_SIGN =
      new HungSignBlock(Blocks.CYAN_TERRACOTTA, FabricBlockSettings.copyOf(Blocks.CYAN_TERRACOTTA));

  @RegisterIdentifier
  public static final HungSignBlock PURPLE_TERRACOTTA_HUNG_SIGN =
      new HungSignBlock(
          Blocks.PURPLE_TERRACOTTA, FabricBlockSettings.copyOf(Blocks.PURPLE_TERRACOTTA));

  @RegisterIdentifier
  public static final HungSignBlock BLUE_TERRACOTTA_HUNG_SIGN =
      new HungSignBlock(Blocks.BLUE_TERRACOTTA, FabricBlockSettings.copyOf(Blocks.BLUE_TERRACOTTA));

  @RegisterIdentifier
  public static final HungSignBlock BROWN_TERRACOTTA_HUNG_SIGN =
      new HungSignBlock(
          Blocks.BROWN_TERRACOTTA, FabricBlockSettings.copyOf(Blocks.BROWN_TERRACOTTA));

  @RegisterIdentifier
  public static final HungSignBlock GREEN_TERRACOTTA_HUNG_SIGN =
      new HungSignBlock(
          Blocks.GREEN_TERRACOTTA, FabricBlockSettings.copyOf(Blocks.GREEN_TERRACOTTA));

  @RegisterIdentifier
  public static final HungSignBlock RED_TERRACOTTA_HUNG_SIGN =
      new HungSignBlock(Blocks.RED_TERRACOTTA, FabricBlockSettings.copyOf(Blocks.RED_TERRACOTTA));

  @RegisterIdentifier
  public static final HungSignBlock BLACK_TERRACOTTA_HUNG_SIGN =
      new HungSignBlock(
          Blocks.BLACK_TERRACOTTA, FabricBlockSettings.copyOf(Blocks.BLACK_TERRACOTTA));

  public static final Map<DyeColor, HungSignBlock> TERRACOTTA_HUNG_SIGNS =
      Util.make(
          new EnumMap<>(DyeColor.class),
          map -> {
            map.put(DyeColor.WHITE, WHITE_TERRACOTTA_HUNG_SIGN);
            map.put(DyeColor.ORANGE, ORANGE_TERRACOTTA_HUNG_SIGN);
            map.put(DyeColor.MAGENTA, MAGENTA_TERRACOTTA_HUNG_SIGN);
            map.put(DyeColor.LIGHT_BLUE, LIGHT_BLUE_TERRACOTTA_HUNG_SIGN);
            map.put(DyeColor.YELLOW, YELLOW_TERRACOTTA_HUNG_SIGN);
            map.put(DyeColor.LIME, LIME_TERRACOTTA_HUNG_SIGN);
            map.put(DyeColor.PINK, PINK_TERRACOTTA_HUNG_SIGN);
            map.put(DyeColor.GRAY, GRAY_TERRACOTTA_HUNG_SIGN);
            map.put(DyeColor.LIGHT_GRAY, LIGHT_GRAY_TERRACOTTA_HUNG_SIGN);
            map.put(DyeColor.CYAN, CYAN_TERRACOTTA_HUNG_SIGN);
            map.put(DyeColor.PURPLE, PURPLE_TERRACOTTA_HUNG_SIGN);
            map.put(DyeColor.BLUE, BLUE_TERRACOTTA_HUNG_SIGN);
            map.put(DyeColor.BROWN, BROWN_TERRACOTTA_HUNG_SIGN);
            map.put(DyeColor.GREEN, GREEN_TERRACOTTA_HUNG_SIGN);
            map.put(DyeColor.RED, RED_TERRACOTTA_HUNG_SIGN);
            map.put(DyeColor.BLACK, BLACK_TERRACOTTA_HUNG_SIGN);
          });

  @RegisterIdentifier
  public static final HungSignBarBlock WHITE_TERRACOTTA_HUNG_SIGN_BAR =
      new HungSignBarBlock(WHITE_TERRACOTTA_HUNG_SIGN);

  @RegisterIdentifier
  public static final HungSignBarBlock ORANGE_TERRACOTTA_HUNG_SIGN_BAR =
      new HungSignBarBlock(ORANGE_TERRACOTTA_HUNG_SIGN);

  @RegisterIdentifier
  public static final HungSignBarBlock MAGENTA_TERRACOTTA_HUNG_SIGN_BAR =
      new HungSignBarBlock(MAGENTA_TERRACOTTA_HUNG_SIGN);

  @RegisterIdentifier
  public static final HungSignBarBlock LIGHT_BLUE_TERRACOTTA_HUNG_SIGN_BAR =
      new HungSignBarBlock(LIGHT_BLUE_TERRACOTTA_HUNG_SIGN);

  @RegisterIdentifier
  public static final HungSignBarBlock YELLOW_TERRACOTTA_HUNG_SIGN_BAR =
      new HungSignBarBlock(YELLOW_TERRACOTTA_HUNG_SIGN);

  @RegisterIdentifier
  public static final HungSignBarBlock LIME_TERRACOTTA_HUNG_SIGN_BAR =
      new HungSignBarBlock(LIME_TERRACOTTA_HUNG_SIGN);

  @RegisterIdentifier
  public static final HungSignBarBlock PINK_TERRACOTTA_HUNG_SIGN_BAR =
      new HungSignBarBlock(PINK_TERRACOTTA_HUNG_SIGN);

  @RegisterIdentifier
  public static final HungSignBarBlock GRAY_TERRACOTTA_HUNG_SIGN_BAR =
      new HungSignBarBlock(GRAY_TERRACOTTA_HUNG_SIGN);

  @RegisterIdentifier
  public static final HungSignBarBlock LIGHT_GRAY_TERRACOTTA_HUNG_SIGN_BAR =
      new HungSignBarBlock(LIGHT_GRAY_TERRACOTTA_HUNG_SIGN);

  @RegisterIdentifier
  public static final HungSignBarBlock CYAN_TERRACOTTA_HUNG_SIGN_BAR =
      new HungSignBarBlock(CYAN_TERRACOTTA_HUNG_SIGN);

  @RegisterIdentifier
  public static final HungSignBarBlock PURPLE_TERRACOTTA_HUNG_SIGN_BAR =
      new HungSignBarBlock(PURPLE_TERRACOTTA_HUNG_SIGN);

  @RegisterIdentifier
  public static final HungSignBarBlock BLUE_TERRACOTTA_HUNG_SIGN_BAR =
      new HungSignBarBlock(BLUE_TERRACOTTA_HUNG_SIGN);

  @RegisterIdentifier
  public static final HungSignBarBlock BROWN_TERRACOTTA_HUNG_SIGN_BAR =
      new HungSignBarBlock(BROWN_TERRACOTTA_HUNG_SIGN);

  @RegisterIdentifier
  public static final HungSignBarBlock GREEN_TERRACOTTA_HUNG_SIGN_BAR =
      new HungSignBarBlock(GREEN_TERRACOTTA_HUNG_SIGN);

  @RegisterIdentifier
  public static final HungSignBarBlock RED_TERRACOTTA_HUNG_SIGN_BAR =
      new HungSignBarBlock(RED_TERRACOTTA_HUNG_SIGN);

  @RegisterIdentifier
  public static final HungSignBarBlock BLACK_TERRACOTTA_HUNG_SIGN_BAR =
      new HungSignBarBlock(BLACK_TERRACOTTA_HUNG_SIGN);

  @RegisterIdentifier
  public static final GlowingHungSignBlock GLOWING_WHITE_CONCRETE_HUNG_SIGN =
      new GlowingHungSignBlock(
          Blocks.WHITE_CONCRETE, FabricBlockSettings.copyOf(Blocks.WHITE_CONCRETE));

  @RegisterIdentifier
  public static final GlowingHungSignBlock GLOWING_ORANGE_CONCRETE_HUNG_SIGN =
      new GlowingHungSignBlock(
          Blocks.ORANGE_CONCRETE, FabricBlockSettings.copyOf(Blocks.ORANGE_CONCRETE));

  @RegisterIdentifier
  public static final GlowingHungSignBlock GLOWING_MAGENTA_CONCRETE_HUNG_SIGN =
      new GlowingHungSignBlock(
          Blocks.MAGENTA_CONCRETE, FabricBlockSettings.copyOf(Blocks.MAGENTA_CONCRETE));

  @RegisterIdentifier
  public static final GlowingHungSignBlock GLOWING_LIGHT_BLUE_CONCRETE_HUNG_SIGN =
      new GlowingHungSignBlock(
          Blocks.LIGHT_BLUE_CONCRETE, FabricBlockSettings.copyOf(Blocks.LIGHT_BLUE_CONCRETE));

  @RegisterIdentifier
  public static final GlowingHungSignBlock GLOWING_YELLOW_CONCRETE_HUNG_SIGN =
      new GlowingHungSignBlock(
          Blocks.YELLOW_CONCRETE, FabricBlockSettings.copyOf(Blocks.YELLOW_CONCRETE));

  @RegisterIdentifier
  public static final GlowingHungSignBlock GLOWING_LIME_CONCRETE_HUNG_SIGN =
      new GlowingHungSignBlock(
          Blocks.LIME_CONCRETE, FabricBlockSettings.copyOf(Blocks.LIME_CONCRETE));

  @RegisterIdentifier
  public static final GlowingHungSignBlock GLOWING_PINK_CONCRETE_HUNG_SIGN =
      new GlowingHungSignBlock(
          Blocks.PINK_CONCRETE, FabricBlockSettings.copyOf(Blocks.PINK_CONCRETE));

  @RegisterIdentifier
  public static final GlowingHungSignBlock GLOWING_GRAY_CONCRETE_HUNG_SIGN =
      new GlowingHungSignBlock(
          Blocks.GRAY_CONCRETE, FabricBlockSettings.copyOf(Blocks.GRAY_CONCRETE));

  @RegisterIdentifier
  public static final GlowingHungSignBlock GLOWING_LIGHT_GRAY_CONCRETE_HUNG_SIGN =
      new GlowingHungSignBlock(
          Blocks.LIGHT_GRAY_CONCRETE, FabricBlockSettings.copyOf(Blocks.LIGHT_GRAY_CONCRETE));

  @RegisterIdentifier
  public static final GlowingHungSignBlock GLOWING_CYAN_CONCRETE_HUNG_SIGN =
      new GlowingHungSignBlock(
          Blocks.CYAN_CONCRETE, FabricBlockSettings.copyOf(Blocks.CYAN_CONCRETE));

  @RegisterIdentifier
  public static final GlowingHungSignBlock GLOWING_PURPLE_CONCRETE_HUNG_SIGN =
      new GlowingHungSignBlock(
          Blocks.PURPLE_CONCRETE, FabricBlockSettings.copyOf(Blocks.PURPLE_CONCRETE));

  @RegisterIdentifier
  public static final GlowingHungSignBlock GLOWING_BLUE_CONCRETE_HUNG_SIGN =
      new GlowingHungSignBlock(
          Blocks.BLUE_CONCRETE, FabricBlockSettings.copyOf(Blocks.BLUE_CONCRETE));

  @RegisterIdentifier
  public static final GlowingHungSignBlock GLOWING_BROWN_CONCRETE_HUNG_SIGN =
      new GlowingHungSignBlock(
          Blocks.BROWN_CONCRETE, FabricBlockSettings.copyOf(Blocks.BROWN_CONCRETE));

  @RegisterIdentifier
  public static final GlowingHungSignBlock GLOWING_GREEN_CONCRETE_HUNG_SIGN =
      new GlowingHungSignBlock(
          Blocks.GREEN_CONCRETE, FabricBlockSettings.copyOf(Blocks.GREEN_CONCRETE));

  @RegisterIdentifier
  public static final GlowingHungSignBlock GLOWING_RED_CONCRETE_HUNG_SIGN =
      new GlowingHungSignBlock(
          Blocks.RED_CONCRETE, FabricBlockSettings.copyOf(Blocks.RED_CONCRETE));

  @RegisterIdentifier
  public static final GlowingHungSignBlock GLOWING_BLACK_CONCRETE_HUNG_SIGN =
      new GlowingHungSignBlock(
          Blocks.BLACK_CONCRETE, FabricBlockSettings.copyOf(Blocks.BLACK_CONCRETE));

  public static final Map<DyeColor, GlowingHungSignBlock> GLOWING_CONCRETE_HUNG_SIGNS =
      Util.make(
          new EnumMap<>(DyeColor.class),
          map -> {
            map.put(DyeColor.WHITE, GLOWING_WHITE_CONCRETE_HUNG_SIGN);
            map.put(DyeColor.ORANGE, GLOWING_ORANGE_CONCRETE_HUNG_SIGN);
            map.put(DyeColor.MAGENTA, GLOWING_MAGENTA_CONCRETE_HUNG_SIGN);
            map.put(DyeColor.LIGHT_BLUE, GLOWING_LIGHT_BLUE_CONCRETE_HUNG_SIGN);
            map.put(DyeColor.YELLOW, GLOWING_YELLOW_CONCRETE_HUNG_SIGN);
            map.put(DyeColor.LIME, GLOWING_LIME_CONCRETE_HUNG_SIGN);
            map.put(DyeColor.PINK, GLOWING_PINK_CONCRETE_HUNG_SIGN);
            map.put(DyeColor.GRAY, GLOWING_GRAY_CONCRETE_HUNG_SIGN);
            map.put(DyeColor.LIGHT_GRAY, GLOWING_LIGHT_GRAY_CONCRETE_HUNG_SIGN);
            map.put(DyeColor.CYAN, GLOWING_CYAN_CONCRETE_HUNG_SIGN);
            map.put(DyeColor.PURPLE, GLOWING_PURPLE_CONCRETE_HUNG_SIGN);
            map.put(DyeColor.BLUE, GLOWING_BLUE_CONCRETE_HUNG_SIGN);
            map.put(DyeColor.BROWN, GLOWING_BROWN_CONCRETE_HUNG_SIGN);
            map.put(DyeColor.GREEN, GLOWING_GREEN_CONCRETE_HUNG_SIGN);
            map.put(DyeColor.RED, GLOWING_RED_CONCRETE_HUNG_SIGN);
            map.put(DyeColor.BLACK, GLOWING_BLACK_CONCRETE_HUNG_SIGN);
          });

  @RegisterIdentifier
  public static final GlowingHungSignBlock GLOWING_WHITE_TERRACOTTA_HUNG_SIGN =
      new GlowingHungSignBlock(
          Blocks.WHITE_TERRACOTTA, FabricBlockSettings.copyOf(Blocks.WHITE_TERRACOTTA));

  @RegisterIdentifier
  public static final GlowingHungSignBlock GLOWING_ORANGE_TERRACOTTA_HUNG_SIGN =
      new GlowingHungSignBlock(
          Blocks.ORANGE_TERRACOTTA, FabricBlockSettings.copyOf(Blocks.ORANGE_TERRACOTTA));

  @RegisterIdentifier
  public static final GlowingHungSignBlock GLOWING_MAGENTA_TERRACOTTA_HUNG_SIGN =
      new GlowingHungSignBlock(
          Blocks.MAGENTA_TERRACOTTA, FabricBlockSettings.copyOf(Blocks.MAGENTA_TERRACOTTA));

  @RegisterIdentifier
  public static final GlowingHungSignBlock GLOWING_LIGHT_BLUE_TERRACOTTA_HUNG_SIGN =
      new GlowingHungSignBlock(
          Blocks.LIGHT_BLUE_TERRACOTTA, FabricBlockSettings.copyOf(Blocks.LIGHT_BLUE_TERRACOTTA));

  @RegisterIdentifier
  public static final GlowingHungSignBlock GLOWING_YELLOW_TERRACOTTA_HUNG_SIGN =
      new GlowingHungSignBlock(
          Blocks.YELLOW_TERRACOTTA, FabricBlockSettings.copyOf(Blocks.YELLOW_TERRACOTTA));

  @RegisterIdentifier
  public static final GlowingHungSignBlock GLOWING_LIME_TERRACOTTA_HUNG_SIGN =
      new GlowingHungSignBlock(
          Blocks.LIME_TERRACOTTA, FabricBlockSettings.copyOf(Blocks.LIME_TERRACOTTA));

  @RegisterIdentifier
  public static final GlowingHungSignBlock GLOWING_PINK_TERRACOTTA_HUNG_SIGN =
      new GlowingHungSignBlock(
          Blocks.PINK_TERRACOTTA, FabricBlockSettings.copyOf(Blocks.PINK_TERRACOTTA));

  @RegisterIdentifier
  public static final GlowingHungSignBlock GLOWING_GRAY_TERRACOTTA_HUNG_SIGN =
      new GlowingHungSignBlock(
          Blocks.GRAY_TERRACOTTA, FabricBlockSettings.copyOf(Blocks.GRAY_TERRACOTTA));

  @RegisterIdentifier
  public static final GlowingHungSignBlock GLOWING_LIGHT_GRAY_TERRACOTTA_HUNG_SIGN =
      new GlowingHungSignBlock(
          Blocks.LIGHT_GRAY_TERRACOTTA, FabricBlockSettings.copyOf(Blocks.LIGHT_GRAY_TERRACOTTA));

  @RegisterIdentifier
  public static final GlowingHungSignBlock GLOWING_CYAN_TERRACOTTA_HUNG_SIGN =
      new GlowingHungSignBlock(
          Blocks.CYAN_TERRACOTTA, FabricBlockSettings.copyOf(Blocks.CYAN_TERRACOTTA));

  @RegisterIdentifier
  public static final GlowingHungSignBlock GLOWING_PURPLE_TERRACOTTA_HUNG_SIGN =
      new GlowingHungSignBlock(
          Blocks.PURPLE_TERRACOTTA, FabricBlockSettings.copyOf(Blocks.PURPLE_TERRACOTTA));

  @RegisterIdentifier
  public static final GlowingHungSignBlock GLOWING_BLUE_TERRACOTTA_HUNG_SIGN =
      new GlowingHungSignBlock(
          Blocks.BLUE_TERRACOTTA, FabricBlockSettings.copyOf(Blocks.BLUE_TERRACOTTA));

  @RegisterIdentifier
  public static final GlowingHungSignBlock GLOWING_BROWN_TERRACOTTA_HUNG_SIGN =
      new GlowingHungSignBlock(
          Blocks.BROWN_TERRACOTTA, FabricBlockSettings.copyOf(Blocks.BROWN_TERRACOTTA));

  @RegisterIdentifier
  public static final GlowingHungSignBlock GLOWING_GREEN_TERRACOTTA_HUNG_SIGN =
      new GlowingHungSignBlock(
          Blocks.GREEN_TERRACOTTA, FabricBlockSettings.copyOf(Blocks.GREEN_TERRACOTTA));

  @RegisterIdentifier
  public static final GlowingHungSignBlock GLOWING_RED_TERRACOTTA_HUNG_SIGN =
      new GlowingHungSignBlock(
          Blocks.RED_TERRACOTTA, FabricBlockSettings.copyOf(Blocks.RED_TERRACOTTA));

  @RegisterIdentifier
  public static final GlowingHungSignBlock GLOWING_BLACK_TERRACOTTA_HUNG_SIGN =
      new GlowingHungSignBlock(
          Blocks.BLACK_TERRACOTTA, FabricBlockSettings.copyOf(Blocks.BLACK_TERRACOTTA));

  public static final Map<DyeColor, GlowingHungSignBlock> GLOWING_TERRACOTTA_HUNG_SIGNS =
      Util.make(
          new EnumMap<>(DyeColor.class),
          map -> {
            map.put(DyeColor.WHITE, GLOWING_WHITE_TERRACOTTA_HUNG_SIGN);
            map.put(DyeColor.ORANGE, GLOWING_ORANGE_TERRACOTTA_HUNG_SIGN);
            map.put(DyeColor.MAGENTA, GLOWING_MAGENTA_TERRACOTTA_HUNG_SIGN);
            map.put(DyeColor.LIGHT_BLUE, GLOWING_LIGHT_BLUE_TERRACOTTA_HUNG_SIGN);
            map.put(DyeColor.YELLOW, GLOWING_YELLOW_TERRACOTTA_HUNG_SIGN);
            map.put(DyeColor.LIME, GLOWING_LIME_TERRACOTTA_HUNG_SIGN);
            map.put(DyeColor.PINK, GLOWING_PINK_TERRACOTTA_HUNG_SIGN);
            map.put(DyeColor.GRAY, GLOWING_GRAY_TERRACOTTA_HUNG_SIGN);
            map.put(DyeColor.LIGHT_GRAY, GLOWING_LIGHT_GRAY_TERRACOTTA_HUNG_SIGN);
            map.put(DyeColor.CYAN, GLOWING_CYAN_TERRACOTTA_HUNG_SIGN);
            map.put(DyeColor.PURPLE, GLOWING_PURPLE_TERRACOTTA_HUNG_SIGN);
            map.put(DyeColor.BLUE, GLOWING_BLUE_TERRACOTTA_HUNG_SIGN);
            map.put(DyeColor.BROWN, GLOWING_BROWN_TERRACOTTA_HUNG_SIGN);
            map.put(DyeColor.GREEN, GLOWING_GREEN_TERRACOTTA_HUNG_SIGN);
            map.put(DyeColor.RED, GLOWING_RED_TERRACOTTA_HUNG_SIGN);
            map.put(DyeColor.BLACK, GLOWING_BLACK_TERRACOTTA_HUNG_SIGN);
          });

  // 以下是比较杂项的一些发光悬挂告示牌方块。
  @RegisterIdentifier
  public static final GlowingHungSignBlock GLOWING_NETHERRACK_HUNG_SIGN =
      new GlowingHungSignBlock(
          Blocks.NETHERRACK, FabricBlockSettings.copyOf(Blocks.NETHERRACK).luminance(15));

  @RegisterIdentifier
  public static final GlowingHungSignBlock GLOWING_NETHER_BRICK_HUNG_SIGN =
      new GlowingHungSignBlock(
          Blocks.NETHER_BRICKS, FabricBlockSettings.copyOf(Blocks.NETHER_BRICKS).luminance(15));

  @RegisterIdentifier
  public static final GlowingHungSignBlock GLOWING_BLACKSTONE_HUNG_SIGN =
      new GlowingHungSignBlock(
          Blocks.BLACKSTONE, FabricBlockSettings.copyOf(Blocks.BLACKSTONE).luminance(15));

  @RegisterIdentifier
  public static final GlowingHungSignBlock GLOWING_POLISHED_BLACKSTONE_HUNG_SIGN =
      new GlowingHungSignBlock(
          Blocks.POLISHED_BLACKSTONE,
          FabricBlockSettings.copyOf(Blocks.POLISHED_BLACKSTONE).luminance(15));

  @RegisterIdentifier
  public static final HungSignBarBlock NETHERRACK_HUNG_SIGN_BAR =
      new HungSignBarBlock(GLOWING_NETHERRACK_HUNG_SIGN);

  @RegisterIdentifier
  public static final HungSignBarBlock NETHER_BRICK_HUNG_SIGN_BAR =
      new HungSignBarBlock(GLOWING_NETHER_BRICK_HUNG_SIGN);

  @RegisterIdentifier
  public static final HungSignBarBlock BLACKSTONE_HUNG_SIGN_BAR =
      new HungSignBarBlock(GLOWING_BLACKSTONE_HUNG_SIGN);

  @RegisterIdentifier
  public static final HungSignBarBlock POLISHED_BLACKSTONE_HUNG_SIGN_BAR =
      new HungSignBarBlock(GLOWING_POLISHED_BLACKSTONE_HUNG_SIGN);
}
