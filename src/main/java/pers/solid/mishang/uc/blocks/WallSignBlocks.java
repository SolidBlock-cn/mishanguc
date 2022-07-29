package pers.solid.mishang.uc.blocks;

import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.Blocks;
import net.minecraft.util.DyeColor;
import net.minecraft.util.Util;
import org.jetbrains.annotations.ApiStatus;
import pers.solid.mishang.uc.annotations.RegisterIdentifier;
import pers.solid.mishang.uc.annotations.Translucent;
import pers.solid.mishang.uc.block.ColoredWallSignBlock;
import pers.solid.mishang.uc.block.FullWallSignBlock;
import pers.solid.mishang.uc.block.GlowingWallSignBlock;
import pers.solid.mishang.uc.block.WallSignBlock;

import java.util.EnumMap;
import java.util.Map;

/**
 * <h1>墙上的告示牌方块</h1>
 * 包括一般的墙上告示牌和完整的墙上告示牌。二者对应不同的方块实体类型。
 */
public final class WallSignBlocks extends MishangucBlocks {

  // 木质

  @RegisterIdentifier
  public static final WallSignBlock OAK_WALL_SIGN =
      new WallSignBlock(Blocks.OAK_PLANKS);

  @RegisterIdentifier
  public static final WallSignBlock SPRUCE_WALL_SIGN =
      new WallSignBlock(Blocks.SPRUCE_PLANKS);

  @RegisterIdentifier
  public static final WallSignBlock BIRCH_WALL_SIGN =
      new WallSignBlock(Blocks.BIRCH_PLANKS);

  @RegisterIdentifier
  public static final WallSignBlock JUNGLE_WALL_SIGN =
      new WallSignBlock(Blocks.JUNGLE_PLANKS);

  @RegisterIdentifier
  public static final WallSignBlock ACACIA_WALL_SIGN =
      new WallSignBlock(Blocks.ACACIA_PLANKS);

  @RegisterIdentifier
  public static final WallSignBlock DARK_OAK_WALL_SIGN =
      new WallSignBlock(Blocks.DARK_OAK_PLANKS);

  @RegisterIdentifier
  @ApiStatus.AvailableSince("0.2.0-mc1.19")
  public static final WallSignBlock MANGROVE_WALL_SIGN = new WallSignBlock(Blocks.MANGROVE_PLANKS);

  @RegisterIdentifier
  public static final WallSignBlock CRIMSON_WALL_SIGN =
      new WallSignBlock(Blocks.CRIMSON_PLANKS);

  @RegisterIdentifier
  public static final WallSignBlock WARPED_WALL_SIGN =
      new WallSignBlock(Blocks.WARPED_PLANKS);

  @RegisterIdentifier
  @ApiStatus.AvailableSince("0.2.2")
  public static final ColoredWallSignBlock COLORED_WOODEN_WALL_SIGN = new ColoredWallSignBlock(ColoredBlocks.COLORED_PLANKS);

  // 混凝土

  @RegisterIdentifier
  public static final WallSignBlock WHITE_CONCRETE_WALL_SIGN =
      new WallSignBlock(Blocks.WHITE_CONCRETE);

  @RegisterIdentifier
  public static final WallSignBlock ORANGE_CONCRETE_WALL_SIGN =
      new WallSignBlock(Blocks.ORANGE_CONCRETE);

  @RegisterIdentifier
  public static final WallSignBlock MAGENTA_CONCRETE_WALL_SIGN =
      new WallSignBlock(Blocks.MAGENTA_CONCRETE);

  @RegisterIdentifier
  public static final WallSignBlock LIGHT_BLUE_CONCRETE_WALL_SIGN =
      new WallSignBlock(Blocks.LIGHT_BLUE_CONCRETE);

  @RegisterIdentifier
  public static final WallSignBlock YELLOW_CONCRETE_WALL_SIGN =
      new WallSignBlock(Blocks.YELLOW_CONCRETE);

  @RegisterIdentifier
  public static final WallSignBlock LIME_CONCRETE_WALL_SIGN =
      new WallSignBlock(Blocks.LIME_CONCRETE);

  @RegisterIdentifier
  public static final WallSignBlock PINK_CONCRETE_WALL_SIGN =
      new WallSignBlock(Blocks.PINK_CONCRETE);

  @RegisterIdentifier
  public static final WallSignBlock GRAY_CONCRETE_WALL_SIGN =
      new WallSignBlock(Blocks.GRAY_CONCRETE);

  @RegisterIdentifier
  public static final WallSignBlock LIGHT_GRAY_CONCRETE_WALL_SIGN =
      new WallSignBlock(Blocks.LIGHT_GRAY_CONCRETE);

  @RegisterIdentifier
  public static final WallSignBlock CYAN_CONCRETE_WALL_SIGN =
      new WallSignBlock(Blocks.CYAN_CONCRETE);

  @RegisterIdentifier
  public static final WallSignBlock PURPLE_CONCRETE_WALL_SIGN =
      new WallSignBlock(Blocks.PURPLE_CONCRETE);

  @RegisterIdentifier
  public static final WallSignBlock BLUE_CONCRETE_WALL_SIGN =
      new WallSignBlock(Blocks.BLUE_CONCRETE);

  @RegisterIdentifier
  public static final WallSignBlock BROWN_CONCRETE_WALL_SIGN =
      new WallSignBlock(Blocks.BROWN_CONCRETE);

  @RegisterIdentifier
  public static final WallSignBlock GREEN_CONCRETE_WALL_SIGN =
      new WallSignBlock(Blocks.GREEN_CONCRETE);

  @RegisterIdentifier
  public static final WallSignBlock RED_CONCRETE_WALL_SIGN =
      new WallSignBlock(Blocks.RED_CONCRETE);

  @RegisterIdentifier
  public static final WallSignBlock BLACK_CONCRETE_WALL_SIGN =
      new WallSignBlock(Blocks.BLACK_CONCRETE);

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

  @ApiStatus.AvailableSince("0.2.2")
  @RegisterIdentifier
  public static final ColoredWallSignBlock COLORED_CONCRETE_WALL_SIGN = new ColoredWallSignBlock(ColoredBlocks.COLORED_CONCRETE);

  // 陶瓦

  @RegisterIdentifier
  public static final WallSignBlock WHITE_TERRACOTTA_WALL_SIGN =
      new WallSignBlock(Blocks.WHITE_TERRACOTTA);

  @RegisterIdentifier
  public static final WallSignBlock ORANGE_TERRACOTTA_WALL_SIGN =
      new WallSignBlock(Blocks.ORANGE_TERRACOTTA);

  @RegisterIdentifier
  public static final WallSignBlock MAGENTA_TERRACOTTA_WALL_SIGN =
      new WallSignBlock(Blocks.MAGENTA_TERRACOTTA);

  @RegisterIdentifier
  public static final WallSignBlock LIGHT_BLUE_TERRACOTTA_WALL_SIGN =
      new WallSignBlock(Blocks.LIGHT_BLUE_TERRACOTTA);

  @RegisterIdentifier
  public static final WallSignBlock YELLOW_TERRACOTTA_WALL_SIGN =
      new WallSignBlock(Blocks.YELLOW_TERRACOTTA);

  @RegisterIdentifier
  public static final WallSignBlock LIME_TERRACOTTA_WALL_SIGN =
      new WallSignBlock(Blocks.LIME_TERRACOTTA);

  @RegisterIdentifier
  public static final WallSignBlock PINK_TERRACOTTA_WALL_SIGN =
      new WallSignBlock(Blocks.PINK_TERRACOTTA);

  @RegisterIdentifier
  public static final WallSignBlock GRAY_TERRACOTTA_WALL_SIGN =
      new WallSignBlock(Blocks.GRAY_TERRACOTTA);

  @RegisterIdentifier
  public static final WallSignBlock LIGHT_GRAY_TERRACOTTA_WALL_SIGN =
      new WallSignBlock(Blocks.LIGHT_GRAY_TERRACOTTA);

  @RegisterIdentifier
  public static final WallSignBlock CYAN_TERRACOTTA_WALL_SIGN =
      new WallSignBlock(Blocks.CYAN_TERRACOTTA);

  @RegisterIdentifier
  public static final WallSignBlock PURPLE_TERRACOTTA_WALL_SIGN =
      new WallSignBlock(Blocks.PURPLE_TERRACOTTA);

  @RegisterIdentifier
  public static final WallSignBlock BLUE_TERRACOTTA_WALL_SIGN =
      new WallSignBlock(Blocks.BLUE_TERRACOTTA);

  @RegisterIdentifier
  public static final WallSignBlock BROWN_TERRACOTTA_WALL_SIGN =
      new WallSignBlock(Blocks.BROWN_TERRACOTTA);

  @RegisterIdentifier
  public static final WallSignBlock GREEN_TERRACOTTA_WALL_SIGN =
      new WallSignBlock(Blocks.GREEN_TERRACOTTA);

  @RegisterIdentifier
  public static final WallSignBlock RED_TERRACOTTA_WALL_SIGN =
      new WallSignBlock(Blocks.RED_TERRACOTTA);

  @RegisterIdentifier
  public static final WallSignBlock BLACK_TERRACOTTA_WALL_SIGN =
      new WallSignBlock(Blocks.BLACK_TERRACOTTA);

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
  @ApiStatus.AvailableSince("0.2.2")
  public static final ColoredWallSignBlock COLORED_TERRACOTTA_WALL_SIGN = new ColoredWallSignBlock(ColoredBlocks.COLORED_TERRACOTTA);

  // 发光的混凝土

  @RegisterIdentifier
  public static final GlowingWallSignBlock GLOWING_WHITE_CONCRETE_WALL_SIGN =
      new GlowingWallSignBlock(Blocks.WHITE_CONCRETE);

  @RegisterIdentifier
  public static final GlowingWallSignBlock GLOWING_ORANGE_CONCRETE_WALL_SIGN =
      new GlowingWallSignBlock(Blocks.ORANGE_CONCRETE);

  @RegisterIdentifier
  public static final GlowingWallSignBlock GLOWING_MAGENTA_CONCRETE_WALL_SIGN =
      new GlowingWallSignBlock(Blocks.MAGENTA_CONCRETE);

  @RegisterIdentifier
  public static final GlowingWallSignBlock GLOWING_LIGHT_BLUE_CONCRETE_WALL_SIGN =
      new GlowingWallSignBlock(Blocks.LIGHT_BLUE_CONCRETE);

  @RegisterIdentifier
  public static final GlowingWallSignBlock GLOWING_YELLOW_CONCRETE_WALL_SIGN =
      new GlowingWallSignBlock(Blocks.YELLOW_CONCRETE);

  @RegisterIdentifier
  public static final GlowingWallSignBlock GLOWING_LIME_CONCRETE_WALL_SIGN =
      new GlowingWallSignBlock(Blocks.LIME_CONCRETE);

  @RegisterIdentifier
  public static final GlowingWallSignBlock GLOWING_PINK_CONCRETE_WALL_SIGN =
      new GlowingWallSignBlock(Blocks.PINK_CONCRETE);

  @RegisterIdentifier
  public static final GlowingWallSignBlock GLOWING_GRAY_CONCRETE_WALL_SIGN =
      new GlowingWallSignBlock(Blocks.GRAY_CONCRETE);

  @RegisterIdentifier
  public static final GlowingWallSignBlock GLOWING_LIGHT_GRAY_CONCRETE_WALL_SIGN =
      new GlowingWallSignBlock(Blocks.LIGHT_GRAY_CONCRETE);

  @RegisterIdentifier
  public static final GlowingWallSignBlock GLOWING_CYAN_CONCRETE_WALL_SIGN =
      new GlowingWallSignBlock(Blocks.CYAN_CONCRETE);

  @RegisterIdentifier
  public static final GlowingWallSignBlock GLOWING_PURPLE_CONCRETE_WALL_SIGN =
      new GlowingWallSignBlock(Blocks.PURPLE_CONCRETE);

  @RegisterIdentifier
  public static final GlowingWallSignBlock GLOWING_BLUE_CONCRETE_WALL_SIGN =
      new GlowingWallSignBlock(Blocks.BLUE_CONCRETE);

  @RegisterIdentifier
  public static final GlowingWallSignBlock GLOWING_BROWN_CONCRETE_WALL_SIGN =
      new GlowingWallSignBlock(Blocks.BROWN_CONCRETE);

  @RegisterIdentifier
  public static final GlowingWallSignBlock GLOWING_GREEN_CONCRETE_WALL_SIGN =
      new GlowingWallSignBlock(Blocks.GREEN_CONCRETE);

  @RegisterIdentifier
  public static final GlowingWallSignBlock GLOWING_RED_CONCRETE_WALL_SIGN =
      new GlowingWallSignBlock(Blocks.RED_CONCRETE);

  @RegisterIdentifier
  public static final GlowingWallSignBlock GLOWING_BLACK_CONCRETE_WALL_SIGN =
      new GlowingWallSignBlock(Blocks.BLACK_CONCRETE);

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

  // 发光的陶瓦

  @RegisterIdentifier
  public static final GlowingWallSignBlock GLOWING_WHITE_TERRACOTTA_WALL_SIGN =
      new GlowingWallSignBlock(Blocks.WHITE_TERRACOTTA);

  @RegisterIdentifier
  public static final GlowingWallSignBlock GLOWING_ORANGE_TERRACOTTA_WALL_SIGN =
      new GlowingWallSignBlock(Blocks.ORANGE_TERRACOTTA);

  @RegisterIdentifier
  public static final GlowingWallSignBlock GLOWING_MAGENTA_TERRACOTTA_WALL_SIGN =
      new GlowingWallSignBlock(Blocks.MAGENTA_TERRACOTTA);

  @RegisterIdentifier
  public static final GlowingWallSignBlock GLOWING_LIGHT_BLUE_TERRACOTTA_WALL_SIGN =
      new GlowingWallSignBlock(Blocks.LIGHT_BLUE_TERRACOTTA);

  @RegisterIdentifier
  public static final GlowingWallSignBlock GLOWING_YELLOW_TERRACOTTA_WALL_SIGN =
      new GlowingWallSignBlock(Blocks.YELLOW_TERRACOTTA);

  @RegisterIdentifier
  public static final GlowingWallSignBlock GLOWING_LIME_TERRACOTTA_WALL_SIGN =
      new GlowingWallSignBlock(Blocks.LIME_TERRACOTTA);

  @RegisterIdentifier
  public static final GlowingWallSignBlock GLOWING_PINK_TERRACOTTA_WALL_SIGN =
      new GlowingWallSignBlock(Blocks.PINK_TERRACOTTA);

  @RegisterIdentifier
  public static final GlowingWallSignBlock GLOWING_GRAY_TERRACOTTA_WALL_SIGN =
      new GlowingWallSignBlock(Blocks.GRAY_TERRACOTTA);

  @RegisterIdentifier
  public static final GlowingWallSignBlock GLOWING_LIGHT_GRAY_TERRACOTTA_WALL_SIGN =
      new GlowingWallSignBlock(Blocks.LIGHT_GRAY_TERRACOTTA);

  @RegisterIdentifier
  public static final GlowingWallSignBlock GLOWING_CYAN_TERRACOTTA_WALL_SIGN =
      new GlowingWallSignBlock(Blocks.CYAN_TERRACOTTA);

  @RegisterIdentifier
  public static final GlowingWallSignBlock GLOWING_PURPLE_TERRACOTTA_WALL_SIGN =
      new GlowingWallSignBlock(Blocks.PURPLE_TERRACOTTA);

  @RegisterIdentifier
  public static final GlowingWallSignBlock GLOWING_BLUE_TERRACOTTA_WALL_SIGN =
      new GlowingWallSignBlock(Blocks.BLUE_TERRACOTTA);

  @RegisterIdentifier
  public static final GlowingWallSignBlock GLOWING_BROWN_TERRACOTTA_WALL_SIGN =
      new GlowingWallSignBlock(Blocks.BROWN_TERRACOTTA);

  @RegisterIdentifier
  public static final GlowingWallSignBlock GLOWING_GREEN_TERRACOTTA_WALL_SIGN =
      new GlowingWallSignBlock(Blocks.GREEN_TERRACOTTA);

  @RegisterIdentifier
  public static final GlowingWallSignBlock GLOWING_RED_TERRACOTTA_WALL_SIGN =
      new GlowingWallSignBlock(Blocks.RED_TERRACOTTA);

  @RegisterIdentifier
  public static final GlowingWallSignBlock GLOWING_BLACK_TERRACOTTA_WALL_SIGN =
      new GlowingWallSignBlock(Blocks.BLACK_TERRACOTTA);

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

  // 一些比较杂项的
  /// 石头
  @RegisterIdentifier
  @ApiStatus.AvailableSince
      ("0.1.7")
  public static final WallSignBlock STONE_WALL_SIGN = new WallSignBlock(Blocks.STONE);
  @RegisterIdentifier
  @ApiStatus.AvailableSince("0.1.7")
  public static final GlowingWallSignBlock GLOWING_STONE_WALL_SIGN = new GlowingWallSignBlock(Blocks.STONE);
  /// 石砖
  @RegisterIdentifier
  @ApiStatus.AvailableSince
      ("0.1.7")
  public static final WallSignBlock STONE_BRICK_WALL_SIGN = new WallSignBlock(Blocks.STONE_BRICKS);
  @RegisterIdentifier
  @ApiStatus.AvailableSince("0.1.7")
  public static final GlowingWallSignBlock GLOWING_STONE_BRICK_WALL_SIGN = new GlowingWallSignBlock(Blocks.STONE_BRICKS);
  // 铁块
  @RegisterIdentifier
  @ApiStatus.AvailableSince
      ("0.1.7")
  public static final WallSignBlock IRON_WALL_SIGN = new WallSignBlock(Blocks.IRON_BLOCK);
  @RegisterIdentifier
  @ApiStatus.AvailableSince("0.1.7")
  public static final GlowingWallSignBlock GLOWING_IRON_WALL_SIGN = new GlowingWallSignBlock(Blocks.IRON_BLOCK);
  // 金块
  @RegisterIdentifier
  @ApiStatus.AvailableSince
      ("0.1.7")
  public static final WallSignBlock GOLD_WALL_SIGN = new WallSignBlock(Blocks.GOLD_BLOCK);
  @RegisterIdentifier
  @ApiStatus.AvailableSince("0.1.7")
  public static final GlowingWallSignBlock GLOWING_GOLD_WALL_SIGN = new GlowingWallSignBlock(Blocks.GOLD_BLOCK);
  // 钻石块
  @RegisterIdentifier
  @ApiStatus.AvailableSince
      ("0.1.7")
  public static final WallSignBlock DIAMOND_WALL_SIGN = new WallSignBlock(Blocks.DIAMOND_BLOCK);
  @RegisterIdentifier
  @ApiStatus.AvailableSince("0.1.7")
  public static final GlowingWallSignBlock GLOWING_DIAMOND_WALL_SIGN = new GlowingWallSignBlock(Blocks.DIAMOND_BLOCK);
  // 绿宝石块
  @RegisterIdentifier
  @ApiStatus.AvailableSince
      ("0.1.7")
  public static final WallSignBlock EMERALD_WALL_SIGN = new WallSignBlock(Blocks.EMERALD_BLOCK);
  @RegisterIdentifier
  @ApiStatus.AvailableSince("0.1.7")
  public static final GlowingWallSignBlock GLOWING_EMERALD_WALL_SIGN = new GlowingWallSignBlock(Blocks.EMERALD_BLOCK);
  // 青金石块
  @RegisterIdentifier
  @ApiStatus.AvailableSince
      ("0.1.7")
  public static final WallSignBlock LAPIS_WALL_SIGN = new WallSignBlock(Blocks.LAPIS_BLOCK);
  @RegisterIdentifier
  @ApiStatus.AvailableSince("0.1.7")
  public static final GlowingWallSignBlock GLOWING_LAPIS_WALL_SIGN = new GlowingWallSignBlock(Blocks.LAPIS_BLOCK);
  // 下界岩
  @RegisterIdentifier
  @ApiStatus.AvailableSince
      ("0.1.7")
  public static final WallSignBlock NETHERRACK_WALL_SIGN = new WallSignBlock(Blocks.NETHERRACK);
  @RegisterIdentifier
  @ApiStatus.AvailableSince("0.1.7")
  public static final GlowingWallSignBlock GLOWING_NETHERRACK_WALL_SIGN = new GlowingWallSignBlock(Blocks.NETHERRACK);
  // 下界砖
  @RegisterIdentifier
  @ApiStatus.AvailableSince
      ("0.1.7")
  public static final WallSignBlock NETHER_BRICK_WALL_SIGN = new WallSignBlock(Blocks.NETHER_BRICKS);
  @RegisterIdentifier
  @ApiStatus.AvailableSince("0.1.7")
  public static final GlowingWallSignBlock GLOWING_NETHER_BRICK_WALL_SIGN = new GlowingWallSignBlock(Blocks.NETHER_BRICKS);
  // 黑石
  @RegisterIdentifier
  @ApiStatus.AvailableSince
      ("0.1.7")
  public static final WallSignBlock BLACKSTONE_WALL_SIGN = new WallSignBlock(Blocks.BLACKSTONE);
  @RegisterIdentifier
  @ApiStatus.AvailableSince("0.1.7")
  public static final GlowingWallSignBlock GLOWING_BLACKSTONE_WALL_SIGN = new GlowingWallSignBlock(Blocks.BLACKSTONE);
  // 磨制黑石
  @RegisterIdentifier
  @ApiStatus.AvailableSince
      ("0.1.7")
  public static final WallSignBlock POLISHED_BLACKSTONE_WALL_SIGN = new WallSignBlock(Blocks.POLISHED_BLACKSTONE);
  @RegisterIdentifier
  @ApiStatus.AvailableSince("0.1.7")
  public static final GlowingWallSignBlock GLOWING_POLISHED_BLACKSTONE_WALL_SIGN = new GlowingWallSignBlock(Blocks.POLISHED_BLACKSTONE);

  static {
    GLOWING_NETHERRACK_WALL_SIGN.glowTexture = "block/lava_still";
    GLOWING_NETHER_BRICK_WALL_SIGN.glowTexture = "block/lava_still";
    GLOWING_BLACKSTONE_WALL_SIGN.glowTexture = "block/glowstone";
    GLOWING_POLISHED_BLACKSTONE_WALL_SIGN.glowTexture = "block/glowstone";
  }

  // 雪
  @RegisterIdentifier
  @ApiStatus.AvailableSince("0.1.7")
  public static final WallSignBlock SNOW_WALL_SIGN = new WallSignBlock(Blocks.SNOW_BLOCK);
  @RegisterIdentifier
  @ApiStatus.AvailableSince("0.1.7")
  public static final GlowingWallSignBlock GLOWING_SNOW_WALL_SIGN = new GlowingWallSignBlock(Blocks.SNOW_BLOCK);
  // 冰
  @RegisterIdentifier
  @ApiStatus.AvailableSince("0.1.7")
  @Translucent
  public static final WallSignBlock ICE_WALL_SIGN = new WallSignBlock(Blocks.ICE);
  @RegisterIdentifier
  @ApiStatus.AvailableSince("0.1.7")
  public static final WallSignBlock PACKED_ICE_WALL_SIGN = new WallSignBlock(Blocks.PACKED_ICE);
  @RegisterIdentifier
  @ApiStatus.AvailableSince("0.1.7")
  public static final GlowingWallSignBlock GLOWING_PACKED_ICE_WALL_SIGN = new GlowingWallSignBlock(Blocks.PACKED_ICE);
  @RegisterIdentifier
  @ApiStatus.AvailableSince("0.1.7")
  public static final WallSignBlock BLUE_ICE_WALL_SIGN = new WallSignBlock(Blocks.BLUE_ICE);
  @RegisterIdentifier
  @ApiStatus.AvailableSince("0.1.7")
  public static final GlowingWallSignBlock GLOWING_BLUE_ICE_WALL_SIGN = new GlowingWallSignBlock(Blocks.BLUE_ICE);

  static {
    COLORED_WOODEN_WALL_SIGN.texture = "mishanguc:block/pale_planks";
    COLORED_TERRACOTTA_WALL_SIGN.texture = "block/white_terracotta";
    COLORED_CONCRETE_WALL_SIGN.texture = "block/white_concrete";
    SNOW_WALL_SIGN.texture = "block/snow";
    GLOWING_SNOW_WALL_SIGN.texture = "block/snow";
  }

  // 完整的混凝土

  @RegisterIdentifier
  public static final FullWallSignBlock FULL_WHITE_CONCRETE_WALL_SIGN =
      new FullWallSignBlock(Blocks.WHITE_CONCRETE);

  @RegisterIdentifier
  public static final FullWallSignBlock FULL_ORANGE_CONCRETE_WALL_SIGN =
      new FullWallSignBlock(Blocks.ORANGE_CONCRETE);

  @RegisterIdentifier
  public static final FullWallSignBlock FULL_MAGENTA_CONCRETE_WALL_SIGN =
      new FullWallSignBlock(Blocks.MAGENTA_CONCRETE);

  @RegisterIdentifier
  public static final FullWallSignBlock FULL_LIGHT_BLUE_CONCRETE_WALL_SIGN =
      new FullWallSignBlock(Blocks.LIGHT_BLUE_CONCRETE);

  @RegisterIdentifier
  public static final FullWallSignBlock FULL_YELLOW_CONCRETE_WALL_SIGN =
      new FullWallSignBlock(Blocks.YELLOW_CONCRETE);

  @RegisterIdentifier
  public static final FullWallSignBlock FULL_LIME_CONCRETE_WALL_SIGN =
      new FullWallSignBlock(Blocks.LIME_CONCRETE);

  @RegisterIdentifier
  public static final FullWallSignBlock FULL_PINK_CONCRETE_WALL_SIGN =
      new FullWallSignBlock(Blocks.PINK_CONCRETE);

  @RegisterIdentifier
  public static final FullWallSignBlock FULL_GRAY_CONCRETE_WALL_SIGN =
      new FullWallSignBlock(Blocks.GRAY_CONCRETE);

  @RegisterIdentifier
  public static final FullWallSignBlock FULL_LIGHT_GRAY_CONCRETE_WALL_SIGN =
      new FullWallSignBlock(Blocks.LIGHT_GRAY_CONCRETE);

  @RegisterIdentifier
  public static final FullWallSignBlock FULL_CYAN_CONCRETE_WALL_SIGN =
      new FullWallSignBlock(Blocks.CYAN_CONCRETE);

  @RegisterIdentifier
  public static final FullWallSignBlock FULL_PURPLE_CONCRETE_WALL_SIGN =
      new FullWallSignBlock(Blocks.PURPLE_CONCRETE);

  @RegisterIdentifier
  public static final FullWallSignBlock FULL_BLUE_CONCRETE_WALL_SIGN =
      new FullWallSignBlock(Blocks.BLUE_CONCRETE);

  @RegisterIdentifier
  public static final FullWallSignBlock FULL_BROWN_CONCRETE_WALL_SIGN =
      new FullWallSignBlock(Blocks.BROWN_CONCRETE);

  @RegisterIdentifier
  public static final FullWallSignBlock FULL_GREEN_CONCRETE_WALL_SIGN =
      new FullWallSignBlock(Blocks.GREEN_CONCRETE);

  @RegisterIdentifier
  public static final FullWallSignBlock FULL_RED_CONCRETE_WALL_SIGN =
      new FullWallSignBlock(Blocks.RED_CONCRETE);

  @RegisterIdentifier
  public static final FullWallSignBlock FULL_BLACK_CONCRETE_WALL_SIGN =
      new FullWallSignBlock(Blocks.BLACK_CONCRETE);

  public static final Map<DyeColor, FullWallSignBlock> FULL_CONCRETE_WALL_SIGNS =
      Util.make(
          new EnumMap<>(DyeColor.class),
          map -> {
            map.put(DyeColor.WHITE, FULL_WHITE_CONCRETE_WALL_SIGN);
            map.put(DyeColor.ORANGE, FULL_ORANGE_CONCRETE_WALL_SIGN);
            map.put(DyeColor.MAGENTA, FULL_MAGENTA_CONCRETE_WALL_SIGN);
            map.put(DyeColor.LIGHT_BLUE, FULL_LIGHT_BLUE_CONCRETE_WALL_SIGN);
            map.put(DyeColor.YELLOW, FULL_YELLOW_CONCRETE_WALL_SIGN);
            map.put(DyeColor.LIME, FULL_LIME_CONCRETE_WALL_SIGN);
            map.put(DyeColor.PINK, FULL_PINK_CONCRETE_WALL_SIGN);
            map.put(DyeColor.GRAY, FULL_GRAY_CONCRETE_WALL_SIGN);
            map.put(DyeColor.LIGHT_GRAY, FULL_LIGHT_GRAY_CONCRETE_WALL_SIGN);
            map.put(DyeColor.CYAN, FULL_CYAN_CONCRETE_WALL_SIGN);
            map.put(DyeColor.PURPLE, FULL_PURPLE_CONCRETE_WALL_SIGN);
            map.put(DyeColor.BLUE, FULL_BLUE_CONCRETE_WALL_SIGN);
            map.put(DyeColor.BROWN, FULL_BROWN_CONCRETE_WALL_SIGN);
            map.put(DyeColor.GREEN, FULL_GREEN_CONCRETE_WALL_SIGN);
            map.put(DyeColor.RED, FULL_RED_CONCRETE_WALL_SIGN);
            map.put(DyeColor.BLACK, FULL_BLACK_CONCRETE_WALL_SIGN);
          });

  // 完整的陶瓦

  @RegisterIdentifier
  public static final FullWallSignBlock FULL_WHITE_TERRACOTTA_WALL_SIGN =
      new FullWallSignBlock(Blocks.WHITE_TERRACOTTA);

  @RegisterIdentifier
  public static final FullWallSignBlock FULL_ORANGE_TERRACOTTA_WALL_SIGN =
      new FullWallSignBlock(Blocks.ORANGE_TERRACOTTA);

  @RegisterIdentifier
  public static final FullWallSignBlock FULL_MAGENTA_TERRACOTTA_WALL_SIGN =
      new FullWallSignBlock(Blocks.MAGENTA_TERRACOTTA);

  @RegisterIdentifier
  public static final FullWallSignBlock FULL_LIGHT_BLUE_TERRACOTTA_WALL_SIGN =
      new FullWallSignBlock(Blocks.LIGHT_BLUE_TERRACOTTA);

  @RegisterIdentifier
  public static final FullWallSignBlock FULL_YELLOW_TERRACOTTA_WALL_SIGN =
      new FullWallSignBlock(Blocks.YELLOW_TERRACOTTA);

  @RegisterIdentifier
  public static final FullWallSignBlock FULL_LIME_TERRACOTTA_WALL_SIGN =
      new FullWallSignBlock(Blocks.LIME_TERRACOTTA);

  @RegisterIdentifier
  public static final FullWallSignBlock FULL_PINK_TERRACOTTA_WALL_SIGN =
      new FullWallSignBlock(Blocks.PINK_TERRACOTTA);

  @RegisterIdentifier
  public static final FullWallSignBlock FULL_GRAY_TERRACOTTA_WALL_SIGN =
      new FullWallSignBlock(Blocks.GRAY_TERRACOTTA);

  @RegisterIdentifier
  public static final FullWallSignBlock FULL_LIGHT_GRAY_TERRACOTTA_WALL_SIGN =
      new FullWallSignBlock(Blocks.LIGHT_GRAY_TERRACOTTA);

  @RegisterIdentifier
  public static final FullWallSignBlock FULL_CYAN_TERRACOTTA_WALL_SIGN =
      new FullWallSignBlock(Blocks.CYAN_TERRACOTTA);

  @RegisterIdentifier
  public static final FullWallSignBlock FULL_PURPLE_TERRACOTTA_WALL_SIGN =
      new FullWallSignBlock(Blocks.PURPLE_TERRACOTTA);

  @RegisterIdentifier
  public static final FullWallSignBlock FULL_BLUE_TERRACOTTA_WALL_SIGN =
      new FullWallSignBlock(Blocks.BLUE_TERRACOTTA);

  @RegisterIdentifier
  public static final FullWallSignBlock FULL_BROWN_TERRACOTTA_WALL_SIGN =
      new FullWallSignBlock(Blocks.BROWN_TERRACOTTA);

  @RegisterIdentifier
  public static final FullWallSignBlock FULL_GREEN_TERRACOTTA_WALL_SIGN =
      new FullWallSignBlock(Blocks.GREEN_TERRACOTTA);

  @RegisterIdentifier
  public static final FullWallSignBlock FULL_RED_TERRACOTTA_WALL_SIGN =
      new FullWallSignBlock(Blocks.RED_TERRACOTTA);

  @RegisterIdentifier
  public static final FullWallSignBlock FULL_BLACK_TERRACOTTA_WALL_SIGN =
      new FullWallSignBlock(Blocks.BLACK_TERRACOTTA);

  public static final Map<DyeColor, FullWallSignBlock> FULL_TERRACOTTA_WALL_SIGNS =
      Util.make(
          new EnumMap<>(DyeColor.class),
          map -> {
            map.put(DyeColor.WHITE, FULL_WHITE_TERRACOTTA_WALL_SIGN);
            map.put(DyeColor.ORANGE, FULL_ORANGE_TERRACOTTA_WALL_SIGN);
            map.put(DyeColor.MAGENTA, FULL_MAGENTA_TERRACOTTA_WALL_SIGN);
            map.put(DyeColor.LIGHT_BLUE, FULL_LIGHT_BLUE_TERRACOTTA_WALL_SIGN);
            map.put(DyeColor.YELLOW, FULL_YELLOW_TERRACOTTA_WALL_SIGN);
            map.put(DyeColor.LIME, FULL_LIME_TERRACOTTA_WALL_SIGN);
            map.put(DyeColor.PINK, FULL_PINK_TERRACOTTA_WALL_SIGN);
            map.put(DyeColor.GRAY, FULL_GRAY_TERRACOTTA_WALL_SIGN);
            map.put(DyeColor.LIGHT_GRAY, FULL_LIGHT_GRAY_TERRACOTTA_WALL_SIGN);
            map.put(DyeColor.CYAN, FULL_CYAN_TERRACOTTA_WALL_SIGN);
            map.put(DyeColor.PURPLE, FULL_PURPLE_TERRACOTTA_WALL_SIGN);
            map.put(DyeColor.BLUE, FULL_BLUE_TERRACOTTA_WALL_SIGN);
            map.put(DyeColor.BROWN, FULL_BROWN_TERRACOTTA_WALL_SIGN);
            map.put(DyeColor.GREEN, FULL_GREEN_TERRACOTTA_WALL_SIGN);
            map.put(DyeColor.RED, FULL_RED_TERRACOTTA_WALL_SIGN);
            map.put(DyeColor.BLACK, FULL_BLACK_TERRACOTTA_WALL_SIGN);
          });

  /**
   * 隐形的告示牌。
   */
  @RegisterIdentifier
  public static final FullWallSignBlock INVISIBLE_WALL_SIGN =
      new FullWallSignBlock(null, FabricBlockSettings.copyOf(Blocks.BARRIER).noCollision());

  @RegisterIdentifier
  public static final FullWallSignBlock INVISIBLE_GLOWING_WALL_SIGN = new FullWallSignBlock(null, FabricBlockSettings.copyOf(Blocks.BARRIER).noCollision().luminance(15));
}
