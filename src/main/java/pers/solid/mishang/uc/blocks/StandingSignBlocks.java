package pers.solid.mishang.uc.blocks;

import com.google.common.collect.ImmutableMap;
import net.minecraft.block.Blocks;
import net.minecraft.util.DyeColor;
import net.minecraft.util.SignType;
import org.jetbrains.annotations.ApiStatus;
import pers.solid.mishang.uc.annotations.RegisterIdentifier;
import pers.solid.mishang.uc.annotations.Translucent;
import pers.solid.mishang.uc.block.*;

/**
 * <h1>直立的告示牌方块</h1>
 * 此类包含本模组中的所有直立告示牌方块。
 *
 * @since 1.0.2
 */
@ApiStatus.AvailableSince("1.0.2")
public final class StandingSignBlocks extends MishangucBlocks {
  private StandingSignBlocks() {
  }

  @RegisterIdentifier
  public static final StandingSignBlock OAK_STANDING_SIGN = new StandingSignBlock(Blocks.OAK_PLANKS);
  @RegisterIdentifier
  public static final StandingSignBlock SPRUCE_STANDING_SIGN = new StandingSignBlock(Blocks.SPRUCE_PLANKS);
  @RegisterIdentifier
  public static final StandingSignBlock BIRCH_STANDING_SIGN = new StandingSignBlock(Blocks.BIRCH_PLANKS);
  @RegisterIdentifier
  public static final StandingSignBlock ACACIA_STANDING_SIGN = new StandingSignBlock(Blocks.ACACIA_PLANKS);
  @RegisterIdentifier
  public static final StandingSignBlock JUNGLE_STANDING_SIGN = new StandingSignBlock(Blocks.JUNGLE_PLANKS);
  @RegisterIdentifier
  public static final StandingSignBlock DARK_OAK_STANDING_SIGN = new StandingSignBlock(Blocks.DARK_OAK_PLANKS);
  @RegisterIdentifier
  public static final StandingSignBlock CRIMSON_STANDING_SIGN = new StandingSignBlock(Blocks.CRIMSON_PLANKS);
  @RegisterIdentifier
  public static final StandingSignBlock WARPED_STANDING_SIGN = new StandingSignBlock(Blocks.WARPED_PLANKS);
  @RegisterIdentifier
  public static final StandingSignBlock MANGROVE_STANDING_SIGN = new StandingSignBlock(Blocks.MANGROVE_PLANKS);

  static {
    OAK_STANDING_SIGN.barTexture = "block/oak_log";
    SPRUCE_STANDING_SIGN.barTexture = "block/spruce_log";
    BIRCH_STANDING_SIGN.barTexture = "block/birch_log";
    ACACIA_STANDING_SIGN.barTexture = "block/acacia_log";
    JUNGLE_STANDING_SIGN.barTexture = "block/jungle_log";
    DARK_OAK_STANDING_SIGN.barTexture = "block/dark_oak_log";
    CRIMSON_STANDING_SIGN.barTexture = "block/crimson_stem";
    WARPED_STANDING_SIGN.barTexture = "block/warped_stem";
    MANGROVE_STANDING_SIGN.barTexture = "block/mangrove_log";
  }

  public static final ImmutableMap<SignType, StandingSignBlock> WOODEN_SIGNS = new ImmutableMap.Builder<SignType, StandingSignBlock>()
      .put(SignType.OAK, OAK_STANDING_SIGN)
      .put(SignType.SPRUCE, SPRUCE_STANDING_SIGN)
      .put(SignType.BIRCH, BIRCH_STANDING_SIGN)
      .put(SignType.ACACIA, ACACIA_STANDING_SIGN)
      .put(SignType.JUNGLE, JUNGLE_STANDING_SIGN)
      .put(SignType.DARK_OAK, DARK_OAK_STANDING_SIGN)
      .put(SignType.CRIMSON, CRIMSON_STANDING_SIGN)
      .put(SignType.WARPED, WARPED_STANDING_SIGN)
      .put(SignType.MANGROVE, MANGROVE_STANDING_SIGN)
      .build();

  @RegisterIdentifier
  public static final StandingSignBlock WHITE_CONCRETE_STANDING_SIGN = new StandingSignBlock(Blocks.WHITE_CONCRETE);
  @RegisterIdentifier
  public static final StandingSignBlock ORANGE_CONCRETE_STANDING_SIGN = new StandingSignBlock(Blocks.ORANGE_CONCRETE);
  @RegisterIdentifier
  public static final StandingSignBlock MAGENTA_CONCRETE_STANDING_SIGN = new StandingSignBlock(Blocks.MAGENTA_CONCRETE);
  @RegisterIdentifier
  public static final StandingSignBlock LIGHT_BLUE_CONCRETE_STANDING_SIGN = new StandingSignBlock(Blocks.LIGHT_BLUE_CONCRETE);
  @RegisterIdentifier
  public static final StandingSignBlock YELLOW_CONCRETE_STANDING_SIGN = new StandingSignBlock(Blocks.YELLOW_CONCRETE);
  @RegisterIdentifier
  public static final StandingSignBlock LIME_CONCRETE_STANDING_SIGN = new StandingSignBlock(Blocks.LIME_CONCRETE);
  @RegisterIdentifier
  public static final StandingSignBlock PINK_CONCRETE_STANDING_SIGN = new StandingSignBlock(Blocks.PINK_CONCRETE);
  @RegisterIdentifier
  public static final StandingSignBlock GRAY_CONCRETE_STANDING_SIGN = new StandingSignBlock(Blocks.GRAY_CONCRETE);
  @RegisterIdentifier
  public static final StandingSignBlock LIGHT_GRAY_CONCRETE_STANDING_SIGN = new StandingSignBlock(Blocks.LIGHT_GRAY_CONCRETE);
  @RegisterIdentifier
  public static final StandingSignBlock CYAN_CONCRETE_STANDING_SIGN = new StandingSignBlock(Blocks.CYAN_CONCRETE);
  @RegisterIdentifier
  public static final StandingSignBlock PURPLE_CONCRETE_STANDING_SIGN = new StandingSignBlock(Blocks.PURPLE_CONCRETE);
  @RegisterIdentifier
  public static final StandingSignBlock BLUE_CONCRETE_STANDING_SIGN = new StandingSignBlock(Blocks.BLUE_CONCRETE);
  @RegisterIdentifier
  public static final StandingSignBlock BROWN_CONCRETE_STANDING_SIGN = new StandingSignBlock(Blocks.BROWN_CONCRETE);
  @RegisterIdentifier
  public static final StandingSignBlock GREEN_CONCRETE_STANDING_SIGN = new StandingSignBlock(Blocks.GREEN_CONCRETE);
  @RegisterIdentifier
  public static final StandingSignBlock RED_CONCRETE_STANDING_SIGN = new StandingSignBlock(Blocks.RED_CONCRETE);
  @RegisterIdentifier
  public static final StandingSignBlock BLACK_CONCRETE_STANDING_SIGN = new StandingSignBlock(Blocks.BLACK_CONCRETE);

  @RegisterIdentifier
  public static final ColoredStandingSignBlock COLORED_CONCRETE_STANDING_SIGN = new ColoredStandingSignBlock(ColoredBlocks.COLORED_CONCRETE);

  public static final ImmutableMap<DyeColor, StandingSignBlock> CONCRETE_STANDING_SIGNS = new ImmutableMap.Builder<DyeColor, StandingSignBlock>()
      .put(DyeColor.WHITE, WHITE_CONCRETE_STANDING_SIGN)
      .put(DyeColor.ORANGE, ORANGE_CONCRETE_STANDING_SIGN)
      .put(DyeColor.MAGENTA, MAGENTA_CONCRETE_STANDING_SIGN)
      .put(DyeColor.LIGHT_BLUE, LIGHT_BLUE_CONCRETE_STANDING_SIGN)
      .put(DyeColor.YELLOW, YELLOW_CONCRETE_STANDING_SIGN)
      .put(DyeColor.LIME, LIME_CONCRETE_STANDING_SIGN)
      .put(DyeColor.PINK, PINK_CONCRETE_STANDING_SIGN)
      .put(DyeColor.GRAY, GRAY_CONCRETE_STANDING_SIGN)
      .put(DyeColor.LIGHT_GRAY, LIGHT_GRAY_CONCRETE_STANDING_SIGN)
      .put(DyeColor.CYAN, CYAN_CONCRETE_STANDING_SIGN)
      .put(DyeColor.PURPLE, PURPLE_CONCRETE_STANDING_SIGN)
      .put(DyeColor.BLUE, BLUE_CONCRETE_STANDING_SIGN)
      .put(DyeColor.BROWN, BROWN_CONCRETE_STANDING_SIGN)
      .put(DyeColor.GREEN, GREEN_CONCRETE_STANDING_SIGN)
      .put(DyeColor.RED, RED_CONCRETE_STANDING_SIGN)
      .put(DyeColor.BLACK, BLACK_CONCRETE_STANDING_SIGN)
      .build();
  @RegisterIdentifier
  public static final StandingSignBlock WHITE_TERRACOTTA_STANDING_SIGN = new StandingSignBlock(Blocks.WHITE_TERRACOTTA);
  @RegisterIdentifier
  public static final StandingSignBlock ORANGE_TERRACOTTA_STANDING_SIGN = new StandingSignBlock(Blocks.ORANGE_TERRACOTTA);
  @RegisterIdentifier
  public static final StandingSignBlock MAGENTA_TERRACOTTA_STANDING_SIGN = new StandingSignBlock(Blocks.MAGENTA_TERRACOTTA);
  @RegisterIdentifier
  public static final StandingSignBlock LIGHT_BLUE_TERRACOTTA_STANDING_SIGN = new StandingSignBlock(Blocks.LIGHT_BLUE_TERRACOTTA);
  @RegisterIdentifier
  public static final StandingSignBlock YELLOW_TERRACOTTA_STANDING_SIGN = new StandingSignBlock(Blocks.YELLOW_TERRACOTTA);
  @RegisterIdentifier
  public static final StandingSignBlock LIME_TERRACOTTA_STANDING_SIGN = new StandingSignBlock(Blocks.LIME_TERRACOTTA);
  @RegisterIdentifier
  public static final StandingSignBlock PINK_TERRACOTTA_STANDING_SIGN = new StandingSignBlock(Blocks.PINK_TERRACOTTA);
  @RegisterIdentifier
  public static final StandingSignBlock GRAY_TERRACOTTA_STANDING_SIGN = new StandingSignBlock(Blocks.GRAY_TERRACOTTA);
  @RegisterIdentifier
  public static final StandingSignBlock LIGHT_GRAY_TERRACOTTA_STANDING_SIGN = new StandingSignBlock(Blocks.LIGHT_GRAY_TERRACOTTA);
  @RegisterIdentifier
  public static final StandingSignBlock CYAN_TERRACOTTA_STANDING_SIGN = new StandingSignBlock(Blocks.CYAN_TERRACOTTA);
  @RegisterIdentifier
  public static final StandingSignBlock PURPLE_TERRACOTTA_STANDING_SIGN = new StandingSignBlock(Blocks.PURPLE_TERRACOTTA);
  @RegisterIdentifier
  public static final StandingSignBlock BLUE_TERRACOTTA_STANDING_SIGN = new StandingSignBlock(Blocks.BLUE_TERRACOTTA);
  @RegisterIdentifier
  public static final StandingSignBlock BROWN_TERRACOTTA_STANDING_SIGN = new StandingSignBlock(Blocks.BROWN_TERRACOTTA);
  @RegisterIdentifier
  public static final StandingSignBlock GREEN_TERRACOTTA_STANDING_SIGN = new StandingSignBlock(Blocks.GREEN_TERRACOTTA);
  @RegisterIdentifier
  public static final StandingSignBlock RED_TERRACOTTA_STANDING_SIGN = new StandingSignBlock(Blocks.RED_TERRACOTTA);
  @RegisterIdentifier
  public static final StandingSignBlock BLACK_TERRACOTTA_STANDING_SIGN = new StandingSignBlock(Blocks.BLACK_TERRACOTTA);

  @RegisterIdentifier
  public static final ColoredStandingSignBlock COLORED_TERRACOTTA_STANDING_SIGN = new ColoredStandingSignBlock(ColoredBlocks.COLORED_TERRACOTTA);

  public static final ImmutableMap<DyeColor, StandingSignBlock> TERRACOTTA_STANDING_SIGNS = new ImmutableMap.Builder<DyeColor, StandingSignBlock>()
      .put(DyeColor.WHITE, WHITE_TERRACOTTA_STANDING_SIGN)
      .put(DyeColor.ORANGE, ORANGE_TERRACOTTA_STANDING_SIGN)
      .put(DyeColor.MAGENTA, MAGENTA_TERRACOTTA_STANDING_SIGN)
      .put(DyeColor.LIGHT_BLUE, LIGHT_BLUE_TERRACOTTA_STANDING_SIGN)
      .put(DyeColor.YELLOW, YELLOW_TERRACOTTA_STANDING_SIGN)
      .put(DyeColor.LIME, LIME_TERRACOTTA_STANDING_SIGN)
      .put(DyeColor.PINK, PINK_TERRACOTTA_STANDING_SIGN)
      .put(DyeColor.GRAY, GRAY_TERRACOTTA_STANDING_SIGN)
      .put(DyeColor.LIGHT_GRAY, LIGHT_GRAY_TERRACOTTA_STANDING_SIGN)
      .put(DyeColor.CYAN, CYAN_TERRACOTTA_STANDING_SIGN)
      .put(DyeColor.PURPLE, PURPLE_TERRACOTTA_STANDING_SIGN)
      .put(DyeColor.BLUE, BLUE_TERRACOTTA_STANDING_SIGN)
      .put(DyeColor.BROWN, BROWN_TERRACOTTA_STANDING_SIGN)
      .put(DyeColor.GREEN, GREEN_TERRACOTTA_STANDING_SIGN)
      .put(DyeColor.RED, RED_TERRACOTTA_STANDING_SIGN)
      .put(DyeColor.BLACK, BLACK_TERRACOTTA_STANDING_SIGN)
      .build();
  @RegisterIdentifier
  public static final GlowingStandingSignBlock GLOWING_WHITE_CONCRETE_STANDING_SIGN = new GlowingStandingSignBlock(Blocks.WHITE_CONCRETE);
  @RegisterIdentifier
  public static final GlowingStandingSignBlock GLOWING_ORANGE_CONCRETE_STANDING_SIGN = new GlowingStandingSignBlock(Blocks.ORANGE_CONCRETE);
  @RegisterIdentifier
  public static final GlowingStandingSignBlock GLOWING_MAGENTA_CONCRETE_STANDING_SIGN = new GlowingStandingSignBlock(Blocks.MAGENTA_CONCRETE);
  @RegisterIdentifier
  public static final GlowingStandingSignBlock GLOWING_LIGHT_BLUE_CONCRETE_STANDING_SIGN = new GlowingStandingSignBlock(Blocks.LIGHT_BLUE_CONCRETE);
  @RegisterIdentifier
  public static final GlowingStandingSignBlock GLOWING_YELLOW_CONCRETE_STANDING_SIGN = new GlowingStandingSignBlock(Blocks.YELLOW_CONCRETE);
  @RegisterIdentifier
  public static final GlowingStandingSignBlock GLOWING_LIME_CONCRETE_STANDING_SIGN = new GlowingStandingSignBlock(Blocks.LIME_CONCRETE);
  @RegisterIdentifier
  public static final GlowingStandingSignBlock GLOWING_PINK_CONCRETE_STANDING_SIGN = new GlowingStandingSignBlock(Blocks.PINK_CONCRETE);
  @RegisterIdentifier
  public static final GlowingStandingSignBlock GLOWING_GRAY_CONCRETE_STANDING_SIGN = new GlowingStandingSignBlock(Blocks.GRAY_CONCRETE);
  @RegisterIdentifier
  public static final GlowingStandingSignBlock GLOWING_LIGHT_GRAY_CONCRETE_STANDING_SIGN = new GlowingStandingSignBlock(Blocks.LIGHT_GRAY_CONCRETE);
  @RegisterIdentifier
  public static final GlowingStandingSignBlock GLOWING_CYAN_CONCRETE_STANDING_SIGN = new GlowingStandingSignBlock(Blocks.CYAN_CONCRETE);
  @RegisterIdentifier
  public static final GlowingStandingSignBlock GLOWING_PURPLE_CONCRETE_STANDING_SIGN = new GlowingStandingSignBlock(Blocks.PURPLE_CONCRETE);
  @RegisterIdentifier
  public static final GlowingStandingSignBlock GLOWING_BLUE_CONCRETE_STANDING_SIGN = new GlowingStandingSignBlock(Blocks.BLUE_CONCRETE);
  @RegisterIdentifier
  public static final GlowingStandingSignBlock GLOWING_BROWN_CONCRETE_STANDING_SIGN = new GlowingStandingSignBlock(Blocks.BROWN_CONCRETE);
  @RegisterIdentifier
  public static final GlowingStandingSignBlock GLOWING_GREEN_CONCRETE_STANDING_SIGN = new GlowingStandingSignBlock(Blocks.GREEN_CONCRETE);
  @RegisterIdentifier
  public static final GlowingStandingSignBlock GLOWING_RED_CONCRETE_STANDING_SIGN = new GlowingStandingSignBlock(Blocks.RED_CONCRETE);
  @RegisterIdentifier
  public static final GlowingStandingSignBlock GLOWING_BLACK_CONCRETE_STANDING_SIGN = new GlowingStandingSignBlock(Blocks.BLACK_CONCRETE);

  @RegisterIdentifier
  public static final ColoredGlowingStandingSignBlock COLORED_GLOWING_CONCRETE_STANDING_SIGN = new ColoredGlowingStandingSignBlock(ColoredBlocks.COLORED_CONCRETE);

  public static final ImmutableMap<DyeColor, StandingSignBlock> GLOWING_CONCRETE_STANDING_SIGNS = new ImmutableMap.Builder<DyeColor, StandingSignBlock>()
      .put(DyeColor.WHITE, GLOWING_WHITE_CONCRETE_STANDING_SIGN)
      .put(DyeColor.ORANGE, GLOWING_ORANGE_CONCRETE_STANDING_SIGN)
      .put(DyeColor.MAGENTA, GLOWING_MAGENTA_CONCRETE_STANDING_SIGN)
      .put(DyeColor.LIGHT_BLUE, GLOWING_LIGHT_BLUE_CONCRETE_STANDING_SIGN)
      .put(DyeColor.YELLOW, GLOWING_YELLOW_CONCRETE_STANDING_SIGN)
      .put(DyeColor.LIME, GLOWING_LIME_CONCRETE_STANDING_SIGN)
      .put(DyeColor.PINK, GLOWING_PINK_CONCRETE_STANDING_SIGN)
      .put(DyeColor.GRAY, GLOWING_GRAY_CONCRETE_STANDING_SIGN)
      .put(DyeColor.LIGHT_GRAY, GLOWING_LIGHT_GRAY_CONCRETE_STANDING_SIGN)
      .put(DyeColor.CYAN, GLOWING_CYAN_CONCRETE_STANDING_SIGN)
      .put(DyeColor.PURPLE, GLOWING_PURPLE_CONCRETE_STANDING_SIGN)
      .put(DyeColor.BLUE, GLOWING_BLUE_CONCRETE_STANDING_SIGN)
      .put(DyeColor.BROWN, GLOWING_BROWN_CONCRETE_STANDING_SIGN)
      .put(DyeColor.GREEN, GLOWING_GREEN_CONCRETE_STANDING_SIGN)
      .put(DyeColor.RED, GLOWING_RED_CONCRETE_STANDING_SIGN)
      .put(DyeColor.BLACK, GLOWING_BLACK_CONCRETE_STANDING_SIGN)
      .build();
  @RegisterIdentifier
  public static final GlowingStandingSignBlock GLOWING_WHITE_TERRACOTTA_STANDING_SIGN = new GlowingStandingSignBlock(Blocks.WHITE_TERRACOTTA);
  @RegisterIdentifier
  public static final GlowingStandingSignBlock GLOWING_ORANGE_TERRACOTTA_STANDING_SIGN = new GlowingStandingSignBlock(Blocks.ORANGE_TERRACOTTA);
  @RegisterIdentifier
  public static final GlowingStandingSignBlock GLOWING_MAGENTA_TERRACOTTA_STANDING_SIGN = new GlowingStandingSignBlock(Blocks.MAGENTA_TERRACOTTA);
  @RegisterIdentifier
  public static final GlowingStandingSignBlock GLOWING_LIGHT_BLUE_TERRACOTTA_STANDING_SIGN = new GlowingStandingSignBlock(Blocks.LIGHT_BLUE_TERRACOTTA);
  @RegisterIdentifier
  public static final GlowingStandingSignBlock GLOWING_YELLOW_TERRACOTTA_STANDING_SIGN = new GlowingStandingSignBlock(Blocks.YELLOW_TERRACOTTA);
  @RegisterIdentifier
  public static final GlowingStandingSignBlock GLOWING_LIME_TERRACOTTA_STANDING_SIGN = new GlowingStandingSignBlock(Blocks.LIME_TERRACOTTA);
  @RegisterIdentifier
  public static final GlowingStandingSignBlock GLOWING_PINK_TERRACOTTA_STANDING_SIGN = new GlowingStandingSignBlock(Blocks.PINK_TERRACOTTA);
  @RegisterIdentifier
  public static final GlowingStandingSignBlock GLOWING_GRAY_TERRACOTTA_STANDING_SIGN = new GlowingStandingSignBlock(Blocks.GRAY_TERRACOTTA);
  @RegisterIdentifier
  public static final GlowingStandingSignBlock GLOWING_LIGHT_GRAY_TERRACOTTA_STANDING_SIGN = new GlowingStandingSignBlock(Blocks.LIGHT_GRAY_TERRACOTTA);
  @RegisterIdentifier
  public static final GlowingStandingSignBlock GLOWING_CYAN_TERRACOTTA_STANDING_SIGN = new GlowingStandingSignBlock(Blocks.CYAN_TERRACOTTA);
  @RegisterIdentifier
  public static final GlowingStandingSignBlock GLOWING_PURPLE_TERRACOTTA_STANDING_SIGN = new GlowingStandingSignBlock(Blocks.PURPLE_TERRACOTTA);
  @RegisterIdentifier
  public static final GlowingStandingSignBlock GLOWING_BLUE_TERRACOTTA_STANDING_SIGN = new GlowingStandingSignBlock(Blocks.BLUE_TERRACOTTA);
  @RegisterIdentifier
  public static final GlowingStandingSignBlock GLOWING_BROWN_TERRACOTTA_STANDING_SIGN = new GlowingStandingSignBlock(Blocks.BROWN_TERRACOTTA);
  @RegisterIdentifier
  public static final GlowingStandingSignBlock GLOWING_GREEN_TERRACOTTA_STANDING_SIGN = new GlowingStandingSignBlock(Blocks.GREEN_TERRACOTTA);
  @RegisterIdentifier
  public static final GlowingStandingSignBlock GLOWING_RED_TERRACOTTA_STANDING_SIGN = new GlowingStandingSignBlock(Blocks.RED_TERRACOTTA);
  @RegisterIdentifier
  public static final GlowingStandingSignBlock GLOWING_BLACK_TERRACOTTA_STANDING_SIGN = new GlowingStandingSignBlock(Blocks.BLACK_TERRACOTTA);

  @RegisterIdentifier
  public static final ColoredGlowingStandingSignBlock COLORED_GLOWING_TERRACOTTA_STANDING_SIGN = new ColoredGlowingStandingSignBlock(ColoredBlocks.COLORED_TERRACOTTA);

  public static final ImmutableMap<DyeColor, StandingSignBlock> GLOWING_TERRACOTTA_STANDING_SIGNS = new ImmutableMap.Builder<DyeColor, StandingSignBlock>()
      .put(DyeColor.WHITE, GLOWING_WHITE_TERRACOTTA_STANDING_SIGN)
      .put(DyeColor.ORANGE, GLOWING_ORANGE_TERRACOTTA_STANDING_SIGN)
      .put(DyeColor.MAGENTA, GLOWING_MAGENTA_TERRACOTTA_STANDING_SIGN)
      .put(DyeColor.LIGHT_BLUE, GLOWING_LIGHT_BLUE_TERRACOTTA_STANDING_SIGN)
      .put(DyeColor.YELLOW, GLOWING_YELLOW_TERRACOTTA_STANDING_SIGN)
      .put(DyeColor.LIME, GLOWING_LIME_TERRACOTTA_STANDING_SIGN)
      .put(DyeColor.PINK, GLOWING_PINK_TERRACOTTA_STANDING_SIGN)
      .put(DyeColor.GRAY, GLOWING_GRAY_TERRACOTTA_STANDING_SIGN)
      .put(DyeColor.LIGHT_GRAY, GLOWING_LIGHT_GRAY_TERRACOTTA_STANDING_SIGN)
      .put(DyeColor.CYAN, GLOWING_CYAN_TERRACOTTA_STANDING_SIGN)
      .put(DyeColor.PURPLE, GLOWING_PURPLE_TERRACOTTA_STANDING_SIGN)
      .put(DyeColor.BLUE, GLOWING_BLUE_TERRACOTTA_STANDING_SIGN)
      .put(DyeColor.BROWN, GLOWING_BROWN_TERRACOTTA_STANDING_SIGN)
      .put(DyeColor.GREEN, GLOWING_GREEN_TERRACOTTA_STANDING_SIGN)
      .put(DyeColor.RED, GLOWING_RED_TERRACOTTA_STANDING_SIGN)
      .put(DyeColor.BLACK, GLOWING_BLACK_TERRACOTTA_STANDING_SIGN)
      .build();

  // 以下是一些比较杂项的。
  /// 石头
  @RegisterIdentifier
  public static final StandingSignBlock STONE_STANDING_SIGN = new StandingSignBlock(Blocks.STONE);
  @RegisterIdentifier
  public static final GlowingStandingSignBlock GLOWING_STONE_STANDING_SIGN = new GlowingStandingSignBlock(Blocks.STONE);
  @RegisterIdentifier
  public static final ColoredStandingSignBlock COLORED_STONE_STANDING_SIGN = new ColoredStandingSignBlock(ColoredBlocks.COLORED_STONE);
  @RegisterIdentifier
  public static final ColoredGlowingStandingSignBlock COLORED_GLOWING_STONE_STANDING_SIGN = new ColoredGlowingStandingSignBlock(ColoredBlocks.COLORED_STONE);
  /// 圆石
  @RegisterIdentifier
  public static final StandingSignBlock COBBLESTONE_STANDING_SIGN = new StandingSignBlock(Blocks.COBBLESTONE);
  @RegisterIdentifier
  public static final GlowingStandingSignBlock GLOWING_COBBLESTONE_STANDING_SIGN = new GlowingStandingSignBlock(Blocks.COBBLESTONE);
  @RegisterIdentifier
  public static final ColoredStandingSignBlock COLORED_COBBLESTONE_STANDING_SIGN = new ColoredStandingSignBlock(ColoredBlocks.COLORED_COBBLESTONE);
  @RegisterIdentifier
  public static final ColoredGlowingStandingSignBlock COLORED_GLOWING_COBBLESTONE_STANDING_SIGN = new ColoredGlowingStandingSignBlock(ColoredBlocks.COLORED_COBBLESTONE);
  /// 石砖
  @RegisterIdentifier
  public static final StandingSignBlock STONE_BRICK_STANDING_SIGN = new StandingSignBlock(Blocks.STONE_BRICKS);
  @RegisterIdentifier
  public static final GlowingStandingSignBlock GLOWING_STONE_BRICK_STANDING_SIGN = new GlowingStandingSignBlock(Blocks.STONE_BRICKS);
  @RegisterIdentifier
  public static final ColoredStandingSignBlock COLORED_STONE_BRICK_STANDING_SIGN = new ColoredStandingSignBlock(ColoredBlocks.COLORED_STONE_BRICKS);
  @RegisterIdentifier
  public static final ColoredGlowingStandingSignBlock COLORED_GLOWING_STONE_BRICK_STANDING_SIGN = new ColoredGlowingStandingSignBlock(ColoredBlocks.COLORED_STONE_BRICKS);
  /// 铁块
  @RegisterIdentifier
  public static final StandingSignBlock IRON_STANDING_SIGN = new StandingSignBlock(Blocks.IRON_BLOCK);
  @RegisterIdentifier
  public static final GlowingStandingSignBlock GLOWING_IRON_STANDING_SIGN = new GlowingStandingSignBlock(Blocks.IRON_BLOCK);
  @RegisterIdentifier
  public static final ColoredStandingSignBlock COLORED_IRON_STANDING_SIGN = new ColoredStandingSignBlock(ColoredBlocks.COLORED_IRON_BLOCK);
  @RegisterIdentifier
  public static final ColoredGlowingWallSignBlock COLORED_GLOWING_IRON_STANDING_SIGN = new ColoredGlowingWallSignBlock(ColoredBlocks.COLORED_IRON_BLOCK);
  /// 金块
  @RegisterIdentifier
  public static final StandingSignBlock GOLD_STANDING_SIGN = new StandingSignBlock(Blocks.GOLD_BLOCK);
  @RegisterIdentifier
  public static final GlowingStandingSignBlock GLOWING_GOLD_STANDING_SIGN = new GlowingStandingSignBlock(Blocks.GOLD_BLOCK);
  /// 钻石块
  @RegisterIdentifier
  public static final StandingSignBlock DIAMOND_STANDING_SIGN = new StandingSignBlock(Blocks.DIAMOND_BLOCK);
  @RegisterIdentifier
  public static final GlowingStandingSignBlock GLOWING_DIAMOND_STANDING_SIGN = new GlowingStandingSignBlock(Blocks.DIAMOND_BLOCK);


  // 绿宝石块
  @RegisterIdentifier
  public static final StandingSignBlock EMERALD_STANDING_SIGN = new StandingSignBlock(Blocks.EMERALD_BLOCK);
  @RegisterIdentifier
  public static final GlowingStandingSignBlock GLOWING_EMERALD_STANDING_SIGN = new GlowingStandingSignBlock(Blocks.EMERALD_BLOCK);
  // 青金石块
  @RegisterIdentifier
  public static final StandingSignBlock LAPIS_STANDING_SIGN = new StandingSignBlock(Blocks.LAPIS_BLOCK);
  @RegisterIdentifier
  public static final GlowingStandingSignBlock GLOWING_LAPIS_STANDING_SIGN = new GlowingStandingSignBlock(Blocks.LAPIS_BLOCK);
  // 下界合金块
  @RegisterIdentifier
  public static final StandingSignBlock NETHERITE_STANDING_SIGN = new StandingSignBlock(Blocks.NETHERITE_BLOCK);
  @RegisterIdentifier
  public static final GlowingStandingSignBlock GLOWING_NETHERITE_STANDING_SIGN = new GlowingStandingSignBlock(Blocks.NETHERITE_BLOCK);
  // 黑曜石
  @RegisterIdentifier
  public static final StandingSignBlock OBSIDIAN_STANDING_SIGN = new StandingSignBlock(Blocks.OBSIDIAN);
  @RegisterIdentifier
  public static final GlowingStandingSignBlock GLOWING_OBSIDIAN_STANDING_SIGN = new GlowingStandingSignBlock(Blocks.OBSIDIAN);
  // 哭泣的黑曜石
  @RegisterIdentifier
  public static final StandingSignBlock CRYING_OBSIDIAN_STANDING_SIGN = new StandingSignBlock(Blocks.CRYING_OBSIDIAN);
  @RegisterIdentifier
  public static final GlowingStandingSignBlock GLOWING_CRYING_OBSIDIAN_STANDING_SIGN = new GlowingStandingSignBlock(Blocks.CRYING_OBSIDIAN);
  // 下界岩
  @RegisterIdentifier
  public static final StandingSignBlock NETHERRACK_STANDING_SIGN = new StandingSignBlock(Blocks.NETHERRACK);
  @RegisterIdentifier
  public static final GlowingStandingSignBlock GLOWING_NETHERRACK_STANDING_SIGN = new GlowingStandingSignBlock(Blocks.NETHERRACK);
  // 下界砖
  @RegisterIdentifier
  public static final StandingSignBlock NETHER_BRICK_STANDING_SIGN = new StandingSignBlock(Blocks.NETHER_BRICKS);
  @RegisterIdentifier
  public static final GlowingStandingSignBlock GLOWING_NETHER_BRICK_STANDING_SIGN = new GlowingStandingSignBlock(Blocks.NETHER_BRICKS);
  // 黑石
  @RegisterIdentifier
  public static final StandingSignBlock BLACKSTONE_STANDING_SIGN = new StandingSignBlock(Blocks.BLACKSTONE);
  @RegisterIdentifier
  public static final GlowingStandingSignBlock GLOWING_BLACKSTONE_STANDING_SIGN = new GlowingStandingSignBlock(Blocks.BLACKSTONE);
  // 磨制黑石
  @RegisterIdentifier
  public static final StandingSignBlock POLISHED_BLACKSTONE_STANDING_SIGN = new StandingSignBlock(Blocks.POLISHED_BLACKSTONE);
  @RegisterIdentifier
  public static final GlowingStandingSignBlock GLOWING_POLISHED_BLACKSTONE_STANDING_SIGN = new GlowingStandingSignBlock(Blocks.POLISHED_BLACKSTONE);

  static {
    GLOWING_NETHERRACK_STANDING_SIGN.glowTexture = "block/lava_still";
    GLOWING_NETHER_BRICK_STANDING_SIGN.glowTexture = "block/lava_still";
    GLOWING_BLACKSTONE_STANDING_SIGN.glowTexture = "block/glowstone";
    GLOWING_POLISHED_BLACKSTONE_STANDING_SIGN.glowTexture = "block/glowstone";
  }


  // 雪
  @RegisterIdentifier
  public static final StandingSignBlock SNOW_STANDING_SIGN = new StandingSignBlock(Blocks.SNOW_BLOCK);
  @RegisterIdentifier
  public static final GlowingStandingSignBlock GLOWING_SNOW_STANDING_SIGN = new GlowingStandingSignBlock(Blocks.SNOW_BLOCK);
  // 冰
  @RegisterIdentifier
  @Translucent
  public static final StandingSignBlock ICE_STANDING_SIGN = new StandingSignBlock(Blocks.ICE);
  @RegisterIdentifier
  public static final StandingSignBlock PACKED_ICE_STANDING_SIGN = new StandingSignBlock(Blocks.PACKED_ICE);
  @RegisterIdentifier
  public static final GlowingStandingSignBlock GLOWING_PACKED_ICE_STANDING_SIGN = new GlowingStandingSignBlock(Blocks.PACKED_ICE);
  @RegisterIdentifier
  public static final StandingSignBlock BLUE_ICE_STANDING_SIGN = new StandingSignBlock(Blocks.BLUE_ICE);
  @RegisterIdentifier
  public static final GlowingStandingSignBlock GLOWING_BLUE_ICE_STANDING_SIGN = new GlowingStandingSignBlock(Blocks.BLUE_ICE);

  static {
    SNOW_STANDING_SIGN.baseTexture = "block/snow";
    SNOW_STANDING_SIGN.barTexture = "block/packed_ice";
    GLOWING_SNOW_STANDING_SIGN.baseTexture = "block/snow";
    GLOWING_SNOW_STANDING_SIGN.barTexture = "block/packed_ice";
    ICE_STANDING_SIGN.barTexture = "block/blue_ice";
  }
}