package pers.solid.mishang.uc.blocks;

import com.google.common.collect.ImmutableMap;
import net.fabricmc.fabric.mixin.object.builder.AbstractBlockAccessor;
import net.fabricmc.fabric.mixin.object.builder.AbstractBlockSettingsAccessor;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.block.MapColor;
import net.minecraft.state.property.Properties;
import net.minecraft.util.DyeColor;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Direction;
import org.jetbrains.annotations.ApiStatus;
import pers.solid.mishang.uc.annotations.Translucent;
import pers.solid.mishang.uc.block.*;

/**
 * <h1>墙上的告示牌方块</h1>
 * 包括一般的墙上告示牌和完整的墙上告示牌。二者对应不同的方块实体类型。
 */
public final class WallSignBlocks extends MishangucBlocks {

  /**
   * 隐形的告示牌。
   */
  public static final FullWallSignBlock INVISIBLE_WALL_SIGN =
      new FullWallSignBlock(null, Block.Settings.create().mapColor(MapColor.CLEAR).noCollision().strength(0, 1f));

  public static final FullWallSignBlock INVISIBLE_GLOWING_WALL_SIGN = new FullWallSignBlock(null, Block.Settings.copy(Blocks.BARRIER).noCollision().luminance(x -> 15).strength(0, 1f));

  // 木质
  @ApiStatus.AvailableSince("1.2.4")
  public static final WallSignBlock OAK_WOOD_WALL_SIGN = new WallSignBlock(Blocks.OAK_WOOD);
  @ApiStatus.AvailableSince("1.2.4")
  public static final WallSignBlock SPRUCE_WOOD_WALL_SIGN = new WallSignBlock(Blocks.SPRUCE_WOOD);
  @ApiStatus.AvailableSince("1.2.4")
  public static final WallSignBlock BIRCH_WOOD_WALL_SIGN = new WallSignBlock(Blocks.BIRCH_WOOD);
  @ApiStatus.AvailableSince("1.2.4")
  public static final WallSignBlock JUNGLE_WOOD_WALL_SIGN = new WallSignBlock(Blocks.JUNGLE_WOOD);
  @ApiStatus.AvailableSince("1.2.4")
  public static final WallSignBlock ACACIA_WOOD_WALL_SIGN = new WallSignBlock(Blocks.ACACIA_WOOD);
  @ApiStatus.AvailableSince("1.2.4")
  public static final WallSignBlock CHERRY_WOOD_WALL_SIGN = new WallSignBlock(Blocks.CHERRY_WOOD);
  @ApiStatus.AvailableSince("1.2.4")
  public static final WallSignBlock DARK_OAK_WOOD_WALL_SIGN = new WallSignBlock(Blocks.DARK_OAK_WOOD);

  @ApiStatus.AvailableSince("1.2.4")
  public static final WallSignBlock MANGROVE_WOOD_WALL_SIGN = new WallSignBlock(Blocks.MANGROVE_WOOD);

  public static final WallSignBlock CRIMSON_HYPHAE_WALL_SIGN = new WallSignBlock(Blocks.CRIMSON_HYPHAE);

  public static final WallSignBlock WARPED_HYPHAE_WALL_SIGN = new WallSignBlock(Blocks.WARPED_HYPHAE);
  @ApiStatus.AvailableSince("1.2.4")
  public static final WallSignBlock STRIPPED_OAK_WOOD_WALL_SIGN = new WallSignBlock(Blocks.STRIPPED_OAK_WOOD);
  @ApiStatus.AvailableSince("1.2.4")
  public static final WallSignBlock STRIPPED_SPRUCE_WOOD_WALL_SIGN = new WallSignBlock(Blocks.STRIPPED_SPRUCE_WOOD);
  @ApiStatus.AvailableSince("1.2.4")
  public static final WallSignBlock STRIPPED_BIRCH_WOOD_WALL_SIGN = new WallSignBlock(Blocks.STRIPPED_BIRCH_WOOD);
  @ApiStatus.AvailableSince("1.2.4")
  public static final WallSignBlock STRIPPED_JUNGLE_WOOD_WALL_SIGN = new WallSignBlock(Blocks.STRIPPED_JUNGLE_WOOD);
  @ApiStatus.AvailableSince("1.2.4")
  public static final WallSignBlock STRIPPED_ACACIA_WOOD_WALL_SIGN = new WallSignBlock(Blocks.STRIPPED_ACACIA_WOOD);
  @ApiStatus.AvailableSince("1.2.4")
  public static final WallSignBlock STRIPPED_CHERRY_WOOD_WALL_SIGN = new WallSignBlock(Blocks.STRIPPED_CHERRY_WOOD);
  @ApiStatus.AvailableSince("1.2.4")
  public static final WallSignBlock STRIPPED_DARK_OAK_WOOD_WALL_SIGN = new WallSignBlock(Blocks.STRIPPED_DARK_OAK_WOOD);

  @ApiStatus.AvailableSince("1.2.4")
  public static final WallSignBlock STRIPPED_MANGROVE_WOOD_WALL_SIGN = new WallSignBlock(Blocks.STRIPPED_MANGROVE_WOOD, Block.Settings.copy(Blocks.STRIPPED_MANGROVE_WOOD).mapColor(MapColor.RED));

  public static final WallSignBlock STRIPPED_CRIMSON_HYPHAE_WALL_SIGN = new WallSignBlock(Blocks.STRIPPED_CRIMSON_HYPHAE);

  public static final WallSignBlock STRIPPED_WARPED_HYPHAE_WALL_SIGN = new WallSignBlock(Blocks.STRIPPED_WARPED_HYPHAE);

  static {
    OAK_WOOD_WALL_SIGN.texture = Identifier.ofVanilla("block/oak_log");
    SPRUCE_WOOD_WALL_SIGN.texture = Identifier.ofVanilla("block/spruce_log");
    BIRCH_WOOD_WALL_SIGN.texture = Identifier.ofVanilla("block/birch_log");
    JUNGLE_WOOD_WALL_SIGN.texture = Identifier.ofVanilla("block/jungle_log");
    ACACIA_WOOD_WALL_SIGN.texture = Identifier.ofVanilla("block/acacia_log");
    CHERRY_WOOD_WALL_SIGN.texture = Identifier.ofVanilla("block/cherry_log");
    DARK_OAK_WOOD_WALL_SIGN.texture = Identifier.ofVanilla("block/dark_oak_log");
    MANGROVE_WOOD_WALL_SIGN.texture = Identifier.ofVanilla("block/mangrove_log");
    CRIMSON_HYPHAE_WALL_SIGN.texture = Identifier.ofVanilla("block/crimson_stem");
    WARPED_HYPHAE_WALL_SIGN.texture = Identifier.ofVanilla("block/warped_stem");
    STRIPPED_OAK_WOOD_WALL_SIGN.texture = Identifier.ofVanilla("block/stripped_oak_log");
    STRIPPED_SPRUCE_WOOD_WALL_SIGN.texture = Identifier.ofVanilla("block/stripped_spruce_log");
    STRIPPED_BIRCH_WOOD_WALL_SIGN.texture = Identifier.ofVanilla("block/stripped_birch_log");
    STRIPPED_JUNGLE_WOOD_WALL_SIGN.texture = Identifier.ofVanilla("block/stripped_jungle_log");
    STRIPPED_ACACIA_WOOD_WALL_SIGN.texture = Identifier.ofVanilla("block/stripped_acacia_log");
    STRIPPED_CHERRY_WOOD_WALL_SIGN.texture = Identifier.ofVanilla("block/stripped_cherry_log");
    STRIPPED_DARK_OAK_WOOD_WALL_SIGN.texture = Identifier.ofVanilla("block/stripped_dark_oak_log");
    STRIPPED_MANGROVE_WOOD_WALL_SIGN.texture = Identifier.ofVanilla("block/stripped_mangrove_log");
    STRIPPED_CRIMSON_HYPHAE_WALL_SIGN.texture = Identifier.ofVanilla("block/stripped_crimson_stem");
    STRIPPED_WARPED_HYPHAE_WALL_SIGN.texture = Identifier.ofVanilla("block/stripped_warped_stem");
  }

  public static final WallSignBlock OAK_WALL_SIGN =
      new WallSignBlock(Blocks.OAK_PLANKS);

  public static final WallSignBlock SPRUCE_WALL_SIGN =
      new WallSignBlock(Blocks.SPRUCE_PLANKS);

  public static final WallSignBlock BIRCH_WALL_SIGN =
      new WallSignBlock(Blocks.BIRCH_PLANKS);

  public static final WallSignBlock JUNGLE_WALL_SIGN =
      new WallSignBlock(Blocks.JUNGLE_PLANKS);

  public static final WallSignBlock ACACIA_WALL_SIGN =
      new WallSignBlock(Blocks.ACACIA_PLANKS);
  @ApiStatus.AvailableSince("1.1.1-mc1.19.4")
  public static final WallSignBlock CHERRY_WALL_SIGN = new WallSignBlock(Blocks.CHERRY_PLANKS);

  public static final WallSignBlock DARK_OAK_WALL_SIGN =
      new WallSignBlock(Blocks.DARK_OAK_PLANKS);

  @ApiStatus.AvailableSince("0.2.0-mc1.19")
  public static final WallSignBlock MANGROVE_WALL_SIGN = new WallSignBlock(Blocks.MANGROVE_PLANKS);

  public static final WallSignBlock CRIMSON_WALL_SIGN =
      new WallSignBlock(Blocks.CRIMSON_PLANKS);

  public static final WallSignBlock WARPED_WALL_SIGN =
      new WallSignBlock(Blocks.WARPED_PLANKS);

  @ApiStatus.AvailableSince("1.0.4-mc1.19.3")
  public static final WallSignBlock BAMBOO_WALL_SIGN = new WallSignBlock(Blocks.BAMBOO_BLOCK, Block.Settings.copy(Blocks.BAMBOO_BLOCK).mapColor(((AbstractBlockSettingsAccessor) ((AbstractBlockAccessor) Blocks.BAMBOO_BLOCK).getSettings()).getMapColorProvider().apply(Blocks.BAMBOO_BLOCK.getDefaultState().with(Properties.AXIS, Direction.Axis.X))));

  @ApiStatus.AvailableSince("1.0.4-mc1.19.3")
  public static final WallSignBlock BAMBOO_PLANK_WALL_SIGN = new WallSignBlock(Blocks.BAMBOO_PLANKS, Block.Settings.copy(Blocks.BAMBOO_PLANKS));

  @ApiStatus.AvailableSince("1.0.4-mc1.19.3")
  public static final WallSignBlock BAMBOO_MOSAIC_WALL_SIGN = new WallSignBlock(Blocks.BAMBOO_MOSAIC, Block.Settings.copy(Blocks.BAMBOO_MOSAIC));

  @ApiStatus.AvailableSince("0.2.2")
  public static final ColoredWallSignBlock COLORED_WOODEN_WALL_SIGN = new ColoredWallSignBlock(ColoredBlocks.COLORED_PLANKS);

  // 混凝土

  public static final WallSignBlock WHITE_CONCRETE_WALL_SIGN =
      new WallSignBlock(Blocks.WHITE_CONCRETE);

  public static final WallSignBlock ORANGE_CONCRETE_WALL_SIGN =
      new WallSignBlock(Blocks.ORANGE_CONCRETE);

  public static final WallSignBlock MAGENTA_CONCRETE_WALL_SIGN =
      new WallSignBlock(Blocks.MAGENTA_CONCRETE);

  public static final WallSignBlock LIGHT_BLUE_CONCRETE_WALL_SIGN =
      new WallSignBlock(Blocks.LIGHT_BLUE_CONCRETE);

  public static final WallSignBlock YELLOW_CONCRETE_WALL_SIGN =
      new WallSignBlock(Blocks.YELLOW_CONCRETE);

  public static final WallSignBlock LIME_CONCRETE_WALL_SIGN =
      new WallSignBlock(Blocks.LIME_CONCRETE);

  public static final WallSignBlock PINK_CONCRETE_WALL_SIGN =
      new WallSignBlock(Blocks.PINK_CONCRETE);

  public static final WallSignBlock GRAY_CONCRETE_WALL_SIGN =
      new WallSignBlock(Blocks.GRAY_CONCRETE);

  public static final WallSignBlock LIGHT_GRAY_CONCRETE_WALL_SIGN =
      new WallSignBlock(Blocks.LIGHT_GRAY_CONCRETE);

  public static final WallSignBlock CYAN_CONCRETE_WALL_SIGN =
      new WallSignBlock(Blocks.CYAN_CONCRETE);

  public static final WallSignBlock PURPLE_CONCRETE_WALL_SIGN =
      new WallSignBlock(Blocks.PURPLE_CONCRETE);

  public static final WallSignBlock BLUE_CONCRETE_WALL_SIGN =
      new WallSignBlock(Blocks.BLUE_CONCRETE);

  public static final WallSignBlock BROWN_CONCRETE_WALL_SIGN =
      new WallSignBlock(Blocks.BROWN_CONCRETE);

  public static final WallSignBlock GREEN_CONCRETE_WALL_SIGN =
      new WallSignBlock(Blocks.GREEN_CONCRETE);

  public static final WallSignBlock RED_CONCRETE_WALL_SIGN =
      new WallSignBlock(Blocks.RED_CONCRETE);

  public static final WallSignBlock BLACK_CONCRETE_WALL_SIGN =
      new WallSignBlock(Blocks.BLACK_CONCRETE);

  public static final ImmutableMap<DyeColor, WallSignBlock> CONCRETE_WALL_SIGNS = new ImmutableMap.Builder<DyeColor, WallSignBlock>()
      .put(DyeColor.WHITE, WHITE_CONCRETE_WALL_SIGN)
      .put(DyeColor.ORANGE, ORANGE_CONCRETE_WALL_SIGN)
      .put(DyeColor.MAGENTA, MAGENTA_CONCRETE_WALL_SIGN)
      .put(DyeColor.LIGHT_BLUE, LIGHT_BLUE_CONCRETE_WALL_SIGN)
      .put(DyeColor.YELLOW, YELLOW_CONCRETE_WALL_SIGN)
      .put(DyeColor.LIME, LIME_CONCRETE_WALL_SIGN)
      .put(DyeColor.PINK, PINK_CONCRETE_WALL_SIGN)
      .put(DyeColor.GRAY, GRAY_CONCRETE_WALL_SIGN)
      .put(DyeColor.LIGHT_GRAY, LIGHT_GRAY_CONCRETE_WALL_SIGN)
      .put(DyeColor.CYAN, CYAN_CONCRETE_WALL_SIGN)
      .put(DyeColor.PURPLE, PURPLE_CONCRETE_WALL_SIGN)
      .put(DyeColor.BLUE, BLUE_CONCRETE_WALL_SIGN)
      .put(DyeColor.BROWN, BROWN_CONCRETE_WALL_SIGN)
      .put(DyeColor.GREEN, GREEN_CONCRETE_WALL_SIGN)
      .put(DyeColor.RED, RED_CONCRETE_WALL_SIGN)
      .put(DyeColor.BLACK, BLACK_CONCRETE_WALL_SIGN)
      .build();

  @ApiStatus.AvailableSince("0.2.2")
  public static final ColoredWallSignBlock COLORED_CONCRETE_WALL_SIGN = new ColoredWallSignBlock(ColoredBlocks.COLORED_CONCRETE);

  // 陶瓦

  public static final WallSignBlock WHITE_TERRACOTTA_WALL_SIGN =
      new WallSignBlock(Blocks.WHITE_TERRACOTTA);

  public static final WallSignBlock ORANGE_TERRACOTTA_WALL_SIGN =
      new WallSignBlock(Blocks.ORANGE_TERRACOTTA);

  public static final WallSignBlock MAGENTA_TERRACOTTA_WALL_SIGN =
      new WallSignBlock(Blocks.MAGENTA_TERRACOTTA);

  public static final WallSignBlock LIGHT_BLUE_TERRACOTTA_WALL_SIGN =
      new WallSignBlock(Blocks.LIGHT_BLUE_TERRACOTTA);

  public static final WallSignBlock YELLOW_TERRACOTTA_WALL_SIGN =
      new WallSignBlock(Blocks.YELLOW_TERRACOTTA);

  public static final WallSignBlock LIME_TERRACOTTA_WALL_SIGN =
      new WallSignBlock(Blocks.LIME_TERRACOTTA);

  public static final WallSignBlock PINK_TERRACOTTA_WALL_SIGN =
      new WallSignBlock(Blocks.PINK_TERRACOTTA);

  public static final WallSignBlock GRAY_TERRACOTTA_WALL_SIGN =
      new WallSignBlock(Blocks.GRAY_TERRACOTTA);

  public static final WallSignBlock LIGHT_GRAY_TERRACOTTA_WALL_SIGN =
      new WallSignBlock(Blocks.LIGHT_GRAY_TERRACOTTA);

  public static final WallSignBlock CYAN_TERRACOTTA_WALL_SIGN =
      new WallSignBlock(Blocks.CYAN_TERRACOTTA);

  public static final WallSignBlock PURPLE_TERRACOTTA_WALL_SIGN =
      new WallSignBlock(Blocks.PURPLE_TERRACOTTA);

  public static final WallSignBlock BLUE_TERRACOTTA_WALL_SIGN =
      new WallSignBlock(Blocks.BLUE_TERRACOTTA);

  public static final WallSignBlock BROWN_TERRACOTTA_WALL_SIGN =
      new WallSignBlock(Blocks.BROWN_TERRACOTTA);

  public static final WallSignBlock GREEN_TERRACOTTA_WALL_SIGN =
      new WallSignBlock(Blocks.GREEN_TERRACOTTA);

  public static final WallSignBlock RED_TERRACOTTA_WALL_SIGN =
      new WallSignBlock(Blocks.RED_TERRACOTTA);

  public static final WallSignBlock BLACK_TERRACOTTA_WALL_SIGN =
      new WallSignBlock(Blocks.BLACK_TERRACOTTA);

  public static final ImmutableMap<DyeColor, WallSignBlock> TERRACOTTA_WALL_SIGNS = new ImmutableMap.Builder<DyeColor, WallSignBlock>()
      .put(DyeColor.WHITE, WHITE_TERRACOTTA_WALL_SIGN)
      .put(DyeColor.ORANGE, ORANGE_TERRACOTTA_WALL_SIGN)
      .put(DyeColor.MAGENTA, MAGENTA_TERRACOTTA_WALL_SIGN)
      .put(DyeColor.LIGHT_BLUE, LIGHT_BLUE_TERRACOTTA_WALL_SIGN)
      .put(DyeColor.YELLOW, YELLOW_TERRACOTTA_WALL_SIGN)
      .put(DyeColor.LIME, LIME_TERRACOTTA_WALL_SIGN)
      .put(DyeColor.PINK, PINK_TERRACOTTA_WALL_SIGN)
      .put(DyeColor.GRAY, GRAY_TERRACOTTA_WALL_SIGN)
      .put(DyeColor.LIGHT_GRAY, LIGHT_GRAY_TERRACOTTA_WALL_SIGN)
      .put(DyeColor.CYAN, CYAN_TERRACOTTA_WALL_SIGN)
      .put(DyeColor.PURPLE, PURPLE_TERRACOTTA_WALL_SIGN)
      .put(DyeColor.BLUE, BLUE_TERRACOTTA_WALL_SIGN)
      .put(DyeColor.BROWN, BROWN_TERRACOTTA_WALL_SIGN)
      .put(DyeColor.GREEN, GREEN_TERRACOTTA_WALL_SIGN)
      .put(DyeColor.RED, RED_TERRACOTTA_WALL_SIGN)
      .put(DyeColor.BLACK, BLACK_TERRACOTTA_WALL_SIGN)
      .build();

  @ApiStatus.AvailableSince("0.2.2")
  public static final ColoredWallSignBlock COLORED_TERRACOTTA_WALL_SIGN = new ColoredWallSignBlock(ColoredBlocks.COLORED_TERRACOTTA);

  // 发光的混凝土

  public static final GlowingWallSignBlock GLOWING_WHITE_CONCRETE_WALL_SIGN =
      new GlowingWallSignBlock(Blocks.WHITE_CONCRETE);

  public static final GlowingWallSignBlock GLOWING_ORANGE_CONCRETE_WALL_SIGN =
      new GlowingWallSignBlock(Blocks.ORANGE_CONCRETE);

  public static final GlowingWallSignBlock GLOWING_MAGENTA_CONCRETE_WALL_SIGN =
      new GlowingWallSignBlock(Blocks.MAGENTA_CONCRETE);

  public static final GlowingWallSignBlock GLOWING_LIGHT_BLUE_CONCRETE_WALL_SIGN =
      new GlowingWallSignBlock(Blocks.LIGHT_BLUE_CONCRETE);

  public static final GlowingWallSignBlock GLOWING_YELLOW_CONCRETE_WALL_SIGN =
      new GlowingWallSignBlock(Blocks.YELLOW_CONCRETE);

  public static final GlowingWallSignBlock GLOWING_LIME_CONCRETE_WALL_SIGN =
      new GlowingWallSignBlock(Blocks.LIME_CONCRETE);

  public static final GlowingWallSignBlock GLOWING_PINK_CONCRETE_WALL_SIGN =
      new GlowingWallSignBlock(Blocks.PINK_CONCRETE);

  public static final GlowingWallSignBlock GLOWING_GRAY_CONCRETE_WALL_SIGN =
      new GlowingWallSignBlock(Blocks.GRAY_CONCRETE);

  public static final GlowingWallSignBlock GLOWING_LIGHT_GRAY_CONCRETE_WALL_SIGN =
      new GlowingWallSignBlock(Blocks.LIGHT_GRAY_CONCRETE);

  public static final GlowingWallSignBlock GLOWING_CYAN_CONCRETE_WALL_SIGN =
      new GlowingWallSignBlock(Blocks.CYAN_CONCRETE);

  public static final GlowingWallSignBlock GLOWING_PURPLE_CONCRETE_WALL_SIGN =
      new GlowingWallSignBlock(Blocks.PURPLE_CONCRETE);

  public static final GlowingWallSignBlock GLOWING_BLUE_CONCRETE_WALL_SIGN =
      new GlowingWallSignBlock(Blocks.BLUE_CONCRETE);

  public static final GlowingWallSignBlock GLOWING_BROWN_CONCRETE_WALL_SIGN =
      new GlowingWallSignBlock(Blocks.BROWN_CONCRETE);

  public static final GlowingWallSignBlock GLOWING_GREEN_CONCRETE_WALL_SIGN =
      new GlowingWallSignBlock(Blocks.GREEN_CONCRETE);

  public static final GlowingWallSignBlock GLOWING_RED_CONCRETE_WALL_SIGN =
      new GlowingWallSignBlock(Blocks.RED_CONCRETE);

  public static final GlowingWallSignBlock GLOWING_BLACK_CONCRETE_WALL_SIGN =
      new GlowingWallSignBlock(Blocks.BLACK_CONCRETE);

  public static final ImmutableMap<DyeColor, GlowingWallSignBlock> GLOWING_CONCRETE_WALL_SIGNS = new ImmutableMap.Builder<DyeColor, GlowingWallSignBlock>()
      .put(DyeColor.WHITE, GLOWING_WHITE_CONCRETE_WALL_SIGN)
      .put(DyeColor.ORANGE, GLOWING_ORANGE_CONCRETE_WALL_SIGN)
      .put(DyeColor.MAGENTA, GLOWING_MAGENTA_CONCRETE_WALL_SIGN)
      .put(DyeColor.LIGHT_BLUE, GLOWING_LIGHT_BLUE_CONCRETE_WALL_SIGN)
      .put(DyeColor.YELLOW, GLOWING_YELLOW_CONCRETE_WALL_SIGN)
      .put(DyeColor.LIME, GLOWING_LIME_CONCRETE_WALL_SIGN)
      .put(DyeColor.PINK, GLOWING_PINK_CONCRETE_WALL_SIGN)
      .put(DyeColor.GRAY, GLOWING_GRAY_CONCRETE_WALL_SIGN)
      .put(DyeColor.LIGHT_GRAY, GLOWING_LIGHT_GRAY_CONCRETE_WALL_SIGN)
      .put(DyeColor.CYAN, GLOWING_CYAN_CONCRETE_WALL_SIGN)
      .put(DyeColor.PURPLE, GLOWING_PURPLE_CONCRETE_WALL_SIGN)
      .put(DyeColor.BLUE, GLOWING_BLUE_CONCRETE_WALL_SIGN)
      .put(DyeColor.BROWN, GLOWING_BROWN_CONCRETE_WALL_SIGN)
      .put(DyeColor.GREEN, GLOWING_GREEN_CONCRETE_WALL_SIGN)
      .put(DyeColor.RED, GLOWING_RED_CONCRETE_WALL_SIGN)
      .put(DyeColor.BLACK, GLOWING_BLACK_CONCRETE_WALL_SIGN)
      .build();

  @ApiStatus.AvailableSince("1.0.2")
  public static final ColoredGlowingWallSignBlock COLORED_GLOWING_CONCRETE_WALL_SIGN = new ColoredGlowingWallSignBlock(ColoredBlocks.COLORED_CONCRETE);

  // 发光的陶瓦

  public static final GlowingWallSignBlock GLOWING_WHITE_TERRACOTTA_WALL_SIGN =
      new GlowingWallSignBlock(Blocks.WHITE_TERRACOTTA);

  public static final GlowingWallSignBlock GLOWING_ORANGE_TERRACOTTA_WALL_SIGN =
      new GlowingWallSignBlock(Blocks.ORANGE_TERRACOTTA);

  public static final GlowingWallSignBlock GLOWING_MAGENTA_TERRACOTTA_WALL_SIGN =
      new GlowingWallSignBlock(Blocks.MAGENTA_TERRACOTTA);

  public static final GlowingWallSignBlock GLOWING_LIGHT_BLUE_TERRACOTTA_WALL_SIGN =
      new GlowingWallSignBlock(Blocks.LIGHT_BLUE_TERRACOTTA);

  public static final GlowingWallSignBlock GLOWING_YELLOW_TERRACOTTA_WALL_SIGN =
      new GlowingWallSignBlock(Blocks.YELLOW_TERRACOTTA);

  public static final GlowingWallSignBlock GLOWING_LIME_TERRACOTTA_WALL_SIGN =
      new GlowingWallSignBlock(Blocks.LIME_TERRACOTTA);

  public static final GlowingWallSignBlock GLOWING_PINK_TERRACOTTA_WALL_SIGN =
      new GlowingWallSignBlock(Blocks.PINK_TERRACOTTA);

  public static final GlowingWallSignBlock GLOWING_GRAY_TERRACOTTA_WALL_SIGN =
      new GlowingWallSignBlock(Blocks.GRAY_TERRACOTTA);

  public static final GlowingWallSignBlock GLOWING_LIGHT_GRAY_TERRACOTTA_WALL_SIGN =
      new GlowingWallSignBlock(Blocks.LIGHT_GRAY_TERRACOTTA);

  public static final GlowingWallSignBlock GLOWING_CYAN_TERRACOTTA_WALL_SIGN =
      new GlowingWallSignBlock(Blocks.CYAN_TERRACOTTA);

  public static final GlowingWallSignBlock GLOWING_PURPLE_TERRACOTTA_WALL_SIGN =
      new GlowingWallSignBlock(Blocks.PURPLE_TERRACOTTA);

  public static final GlowingWallSignBlock GLOWING_BLUE_TERRACOTTA_WALL_SIGN =
      new GlowingWallSignBlock(Blocks.BLUE_TERRACOTTA);

  public static final GlowingWallSignBlock GLOWING_BROWN_TERRACOTTA_WALL_SIGN =
      new GlowingWallSignBlock(Blocks.BROWN_TERRACOTTA);

  public static final GlowingWallSignBlock GLOWING_GREEN_TERRACOTTA_WALL_SIGN =
      new GlowingWallSignBlock(Blocks.GREEN_TERRACOTTA);

  public static final GlowingWallSignBlock GLOWING_RED_TERRACOTTA_WALL_SIGN =
      new GlowingWallSignBlock(Blocks.RED_TERRACOTTA);

  public static final GlowingWallSignBlock GLOWING_BLACK_TERRACOTTA_WALL_SIGN =
      new GlowingWallSignBlock(Blocks.BLACK_TERRACOTTA);

  public static final ImmutableMap<DyeColor, GlowingWallSignBlock> GLOWING_TERRACOTTA_WALL_SIGNS = new ImmutableMap.Builder<DyeColor, GlowingWallSignBlock>()
      .put(DyeColor.WHITE, GLOWING_WHITE_TERRACOTTA_WALL_SIGN)
      .put(DyeColor.ORANGE, GLOWING_ORANGE_TERRACOTTA_WALL_SIGN)
      .put(DyeColor.MAGENTA, GLOWING_MAGENTA_TERRACOTTA_WALL_SIGN)
      .put(DyeColor.LIGHT_BLUE, GLOWING_LIGHT_BLUE_TERRACOTTA_WALL_SIGN)
      .put(DyeColor.YELLOW, GLOWING_YELLOW_TERRACOTTA_WALL_SIGN)
      .put(DyeColor.LIME, GLOWING_LIME_TERRACOTTA_WALL_SIGN)
      .put(DyeColor.PINK, GLOWING_PINK_TERRACOTTA_WALL_SIGN)
      .put(DyeColor.GRAY, GLOWING_GRAY_TERRACOTTA_WALL_SIGN)
      .put(DyeColor.LIGHT_GRAY, GLOWING_LIGHT_GRAY_TERRACOTTA_WALL_SIGN)
      .put(DyeColor.CYAN, GLOWING_CYAN_TERRACOTTA_WALL_SIGN)
      .put(DyeColor.PURPLE, GLOWING_PURPLE_TERRACOTTA_WALL_SIGN)
      .put(DyeColor.BLUE, GLOWING_BLUE_TERRACOTTA_WALL_SIGN)
      .put(DyeColor.BROWN, GLOWING_BROWN_TERRACOTTA_WALL_SIGN)
      .put(DyeColor.GREEN, GLOWING_GREEN_TERRACOTTA_WALL_SIGN)
      .put(DyeColor.RED, GLOWING_RED_TERRACOTTA_WALL_SIGN)
      .put(DyeColor.BLACK, GLOWING_BLACK_TERRACOTTA_WALL_SIGN)
      .build();

  @ApiStatus.AvailableSince("1.0.2")
  public static final ColoredGlowingWallSignBlock COLORED_GLOWING_TERRACOTTA_WALL_SIGN = new ColoredGlowingWallSignBlock(ColoredBlocks.COLORED_TERRACOTTA);

  // 一些比较杂项的
  /// 石头
  @ApiStatus.AvailableSince("0.1.7")
  public static final WallSignBlock STONE_WALL_SIGN = new WallSignBlock(Blocks.STONE);
  @ApiStatus.AvailableSince("0.1.7")
  public static final GlowingWallSignBlock GLOWING_STONE_WALL_SIGN = new GlowingWallSignBlock(Blocks.STONE);
  @ApiStatus.AvailableSince("1.0.2")
  public static final ColoredWallSignBlock COLORED_STONE_WALL_SIGN = new ColoredWallSignBlock(ColoredBlocks.COLORED_STONE);
  @ApiStatus.AvailableSince("1.0.2")
  public static final ColoredGlowingWallSignBlock COLORED_GLOWING_STONE_WALL_SIGN = new ColoredGlowingWallSignBlock(ColoredBlocks.COLORED_STONE);
  /// 圆石
  @ApiStatus.AvailableSince("0.2.4")
  public static final WallSignBlock COBBLESTONE_WALL_SIGN = new WallSignBlock(Blocks.COBBLESTONE);
  @ApiStatus.AvailableSince("0.2.4")
  public static final GlowingWallSignBlock GLOWING_COBBLESTONE_WALL_SIGN = new GlowingWallSignBlock(Blocks.COBBLESTONE);
  @ApiStatus.AvailableSince("1.0.2")
  public static final ColoredWallSignBlock COLORED_COBBLESTONE_WALL_SIGN = new ColoredWallSignBlock(ColoredBlocks.COLORED_COBBLESTONE);
  @ApiStatus.AvailableSince("1.0.2")
  public static final ColoredGlowingWallSignBlock COLORED_GLOWING_COBBLESTONE_WALL_SIGN = new ColoredGlowingWallSignBlock(ColoredBlocks.COLORED_COBBLESTONE);
  /// 石砖
  @ApiStatus.AvailableSince("0.1.7")
  public static final WallSignBlock STONE_BRICK_WALL_SIGN = new WallSignBlock(Blocks.STONE_BRICKS);
  @ApiStatus.AvailableSince("0.1.7")
  public static final GlowingWallSignBlock GLOWING_STONE_BRICK_WALL_SIGN = new GlowingWallSignBlock(Blocks.STONE_BRICKS);
  @ApiStatus.AvailableSince("1.0.2")
  public static final ColoredWallSignBlock COLORED_STONE_BRICK_WALL_SIGN = new ColoredWallSignBlock(ColoredBlocks.COLORED_STONE_BRICKS);
  @ApiStatus.AvailableSince("1.0.2")
  public static final ColoredGlowingWallSignBlock COLORED_GLOWING_STONE_BRICK_WALL_SIGN = new ColoredGlowingWallSignBlock(ColoredBlocks.COLORED_STONE_BRICKS);
  // 铁块
  @ApiStatus.AvailableSince("0.1.7")
  public static final WallSignBlock IRON_WALL_SIGN = new WallSignBlock(Blocks.IRON_BLOCK);
  @ApiStatus.AvailableSince("0.1.7")
  public static final GlowingWallSignBlock GLOWING_IRON_WALL_SIGN = new GlowingWallSignBlock(Blocks.IRON_BLOCK);
  @ApiStatus.AvailableSince("1.0.2")
  public static final ColoredWallSignBlock COLORED_IRON_WALL_SIGN = new ColoredWallSignBlock(ColoredBlocks.COLORED_IRON_BLOCK);
  @ApiStatus.AvailableSince("1.0.2")
  public static final ColoredGlowingWallSignBlock COLORED_GLOWING_IRON_WALL_SIGN = new ColoredGlowingWallSignBlock(ColoredBlocks.COLORED_IRON_BLOCK);
  // 金块
  @ApiStatus.AvailableSince("0.1.7")
  public static final WallSignBlock GOLD_WALL_SIGN = new WallSignBlock(Blocks.GOLD_BLOCK);
  @ApiStatus.AvailableSince("0.1.7")
  public static final GlowingWallSignBlock GLOWING_GOLD_WALL_SIGN = new GlowingWallSignBlock(Blocks.GOLD_BLOCK);
  // 钻石块
  @ApiStatus.AvailableSince("0.1.7")
  public static final WallSignBlock DIAMOND_WALL_SIGN = new WallSignBlock(Blocks.DIAMOND_BLOCK);
  @ApiStatus.AvailableSince("0.1.7")
  public static final GlowingWallSignBlock GLOWING_DIAMOND_WALL_SIGN = new GlowingWallSignBlock(Blocks.DIAMOND_BLOCK);
  // 绿宝石块
  @ApiStatus.AvailableSince("0.1.7")
  public static final WallSignBlock EMERALD_WALL_SIGN = new WallSignBlock(Blocks.EMERALD_BLOCK);
  @ApiStatus.AvailableSince("0.1.7")
  public static final GlowingWallSignBlock GLOWING_EMERALD_WALL_SIGN = new GlowingWallSignBlock(Blocks.EMERALD_BLOCK);
  // 青金石块
  @ApiStatus.AvailableSince("0.1.7")
  public static final WallSignBlock LAPIS_WALL_SIGN = new WallSignBlock(Blocks.LAPIS_BLOCK);
  @ApiStatus.AvailableSince("0.1.7")
  public static final GlowingWallSignBlock GLOWING_LAPIS_WALL_SIGN = new GlowingWallSignBlock(Blocks.LAPIS_BLOCK);
  // 下界合金块
  @ApiStatus.AvailableSince("0.2.4")
  public static final WallSignBlock NETHERITE_WALL_SIGN = new WallSignBlock(Blocks.NETHERITE_BLOCK);
  @ApiStatus.AvailableSince("0.2.4")
  public static final GlowingWallSignBlock GLOWING_NETHERITE_WALL_SIGN = new GlowingWallSignBlock(Blocks.NETHERITE_BLOCK);
  // 黑曜石
  @ApiStatus.AvailableSince("0.2.4")
  public static final WallSignBlock OBSIDIAN_WALL_SIGN = new WallSignBlock(Blocks.OBSIDIAN);
  @ApiStatus.AvailableSince("0.2.4")
  public static final GlowingWallSignBlock GLOWING_OBSIDIAN_WALL_SIGN = new GlowingWallSignBlock(Blocks.OBSIDIAN);
  // 哭泣的黑曜石
  @ApiStatus.AvailableSince("0.2.4")
  public static final WallSignBlock CRYING_OBSIDIAN_WALL_SIGN = new WallSignBlock(Blocks.CRYING_OBSIDIAN);
  @ApiStatus.AvailableSince("0.2.4")
  public static final GlowingWallSignBlock GLOWING_CRYING_OBSIDIAN_WALL_SIGN = new GlowingWallSignBlock(Blocks.CRYING_OBSIDIAN);
  // 下界岩
  @ApiStatus.AvailableSince("0.1.7")
  public static final WallSignBlock NETHERRACK_WALL_SIGN = new WallSignBlock(Blocks.NETHERRACK);
  @ApiStatus.AvailableSince("0.1.7")
  public static final GlowingWallSignBlock GLOWING_NETHERRACK_WALL_SIGN = new GlowingWallSignBlock(Blocks.NETHERRACK);
  // 下界砖
  @ApiStatus.AvailableSince("0.1.7")
  public static final WallSignBlock NETHER_BRICK_WALL_SIGN = new WallSignBlock(Blocks.NETHER_BRICKS);
  @ApiStatus.AvailableSince("0.1.7")
  public static final GlowingWallSignBlock GLOWING_NETHER_BRICK_WALL_SIGN = new GlowingWallSignBlock(Blocks.NETHER_BRICKS);
  // 黑石
  @ApiStatus.AvailableSince("0.1.7")
  public static final WallSignBlock BLACKSTONE_WALL_SIGN = new WallSignBlock(Blocks.BLACKSTONE);
  @ApiStatus.AvailableSince("0.1.7")
  public static final GlowingWallSignBlock GLOWING_BLACKSTONE_WALL_SIGN = new GlowingWallSignBlock(Blocks.BLACKSTONE);
  // 磨制黑石
  @ApiStatus.AvailableSince("0.1.7")
  public static final WallSignBlock POLISHED_BLACKSTONE_WALL_SIGN = new WallSignBlock(Blocks.POLISHED_BLACKSTONE);
  @ApiStatus.AvailableSince("0.1.7")
  public static final GlowingWallSignBlock GLOWING_POLISHED_BLACKSTONE_WALL_SIGN = new GlowingWallSignBlock(Blocks.POLISHED_BLACKSTONE);

  static {
    GLOWING_NETHERRACK_WALL_SIGN.glowTexture = Identifier.ofVanilla("block/lava_still");
    GLOWING_NETHER_BRICK_WALL_SIGN.glowTexture = Identifier.ofVanilla("block/lava_still");
    GLOWING_BLACKSTONE_WALL_SIGN.glowTexture = Identifier.ofVanilla("block/glowstone");
    GLOWING_POLISHED_BLACKSTONE_WALL_SIGN.glowTexture = Identifier.ofVanilla("block/glowstone");
  }

  // 雪
  @ApiStatus.AvailableSince("0.1.7")
  public static final WallSignBlock SNOW_WALL_SIGN = new WallSignBlock(Blocks.SNOW_BLOCK);
  @ApiStatus.AvailableSince("0.1.7")
  public static final GlowingWallSignBlock GLOWING_SNOW_WALL_SIGN = new GlowingWallSignBlock(Blocks.SNOW_BLOCK);
  // 冰
  @ApiStatus.AvailableSince("0.1.7")
  @Translucent
  public static final WallSignBlock ICE_WALL_SIGN = new WallSignBlock(Blocks.ICE);
  @ApiStatus.AvailableSince("0.1.7")
  public static final WallSignBlock PACKED_ICE_WALL_SIGN = new WallSignBlock(Blocks.PACKED_ICE);
  @ApiStatus.AvailableSince("0.1.7")
  public static final GlowingWallSignBlock GLOWING_PACKED_ICE_WALL_SIGN = new GlowingWallSignBlock(Blocks.PACKED_ICE);
  @ApiStatus.AvailableSince("0.1.7")
  public static final WallSignBlock BLUE_ICE_WALL_SIGN = new WallSignBlock(Blocks.BLUE_ICE);
  @ApiStatus.AvailableSince("0.1.7")
  public static final GlowingWallSignBlock GLOWING_BLUE_ICE_WALL_SIGN = new GlowingWallSignBlock(Blocks.BLUE_ICE);

  static {
    SNOW_WALL_SIGN.texture = Identifier.ofVanilla("block/snow");
    GLOWING_SNOW_WALL_SIGN.texture = Identifier.ofVanilla("block/snow");
  }

  // 完整的混凝土

  public static final FullWallSignBlock FULL_WHITE_CONCRETE_WALL_SIGN =
      new FullWallSignBlock(Blocks.WHITE_CONCRETE);

  public static final FullWallSignBlock FULL_ORANGE_CONCRETE_WALL_SIGN =
      new FullWallSignBlock(Blocks.ORANGE_CONCRETE);

  public static final FullWallSignBlock FULL_MAGENTA_CONCRETE_WALL_SIGN =
      new FullWallSignBlock(Blocks.MAGENTA_CONCRETE);

  public static final FullWallSignBlock FULL_LIGHT_BLUE_CONCRETE_WALL_SIGN =
      new FullWallSignBlock(Blocks.LIGHT_BLUE_CONCRETE);

  public static final FullWallSignBlock FULL_YELLOW_CONCRETE_WALL_SIGN =
      new FullWallSignBlock(Blocks.YELLOW_CONCRETE);

  public static final FullWallSignBlock FULL_LIME_CONCRETE_WALL_SIGN =
      new FullWallSignBlock(Blocks.LIME_CONCRETE);

  public static final FullWallSignBlock FULL_PINK_CONCRETE_WALL_SIGN =
      new FullWallSignBlock(Blocks.PINK_CONCRETE);

  public static final FullWallSignBlock FULL_GRAY_CONCRETE_WALL_SIGN =
      new FullWallSignBlock(Blocks.GRAY_CONCRETE);

  public static final FullWallSignBlock FULL_LIGHT_GRAY_CONCRETE_WALL_SIGN =
      new FullWallSignBlock(Blocks.LIGHT_GRAY_CONCRETE);

  public static final FullWallSignBlock FULL_CYAN_CONCRETE_WALL_SIGN =
      new FullWallSignBlock(Blocks.CYAN_CONCRETE);

  public static final FullWallSignBlock FULL_PURPLE_CONCRETE_WALL_SIGN =
      new FullWallSignBlock(Blocks.PURPLE_CONCRETE);

  public static final FullWallSignBlock FULL_BLUE_CONCRETE_WALL_SIGN =
      new FullWallSignBlock(Blocks.BLUE_CONCRETE);

  public static final FullWallSignBlock FULL_BROWN_CONCRETE_WALL_SIGN =
      new FullWallSignBlock(Blocks.BROWN_CONCRETE);

  public static final FullWallSignBlock FULL_GREEN_CONCRETE_WALL_SIGN =
      new FullWallSignBlock(Blocks.GREEN_CONCRETE);

  public static final FullWallSignBlock FULL_RED_CONCRETE_WALL_SIGN =
      new FullWallSignBlock(Blocks.RED_CONCRETE);

  public static final FullWallSignBlock FULL_BLACK_CONCRETE_WALL_SIGN =
      new FullWallSignBlock(Blocks.BLACK_CONCRETE);

  public static final ImmutableMap<DyeColor, FullWallSignBlock> FULL_CONCRETE_WALL_SIGNS = new ImmutableMap.Builder<DyeColor, FullWallSignBlock>()
      .put(DyeColor.WHITE, FULL_WHITE_CONCRETE_WALL_SIGN)
      .put(DyeColor.ORANGE, FULL_ORANGE_CONCRETE_WALL_SIGN)
      .put(DyeColor.MAGENTA, FULL_MAGENTA_CONCRETE_WALL_SIGN)
      .put(DyeColor.LIGHT_BLUE, FULL_LIGHT_BLUE_CONCRETE_WALL_SIGN)
      .put(DyeColor.YELLOW, FULL_YELLOW_CONCRETE_WALL_SIGN)
      .put(DyeColor.LIME, FULL_LIME_CONCRETE_WALL_SIGN)
      .put(DyeColor.PINK, FULL_PINK_CONCRETE_WALL_SIGN)
      .put(DyeColor.GRAY, FULL_GRAY_CONCRETE_WALL_SIGN)
      .put(DyeColor.LIGHT_GRAY, FULL_LIGHT_GRAY_CONCRETE_WALL_SIGN)
      .put(DyeColor.CYAN, FULL_CYAN_CONCRETE_WALL_SIGN)
      .put(DyeColor.PURPLE, FULL_PURPLE_CONCRETE_WALL_SIGN)
      .put(DyeColor.BLUE, FULL_BLUE_CONCRETE_WALL_SIGN)
      .put(DyeColor.BROWN, FULL_BROWN_CONCRETE_WALL_SIGN)
      .put(DyeColor.GREEN, FULL_GREEN_CONCRETE_WALL_SIGN)
      .put(DyeColor.RED, FULL_RED_CONCRETE_WALL_SIGN)
      .put(DyeColor.BLACK, FULL_BLACK_CONCRETE_WALL_SIGN)
      .build();

  // 完整的陶瓦

  public static final FullWallSignBlock FULL_WHITE_TERRACOTTA_WALL_SIGN =
      new FullWallSignBlock(Blocks.WHITE_TERRACOTTA);

  public static final FullWallSignBlock FULL_ORANGE_TERRACOTTA_WALL_SIGN =
      new FullWallSignBlock(Blocks.ORANGE_TERRACOTTA);

  public static final FullWallSignBlock FULL_MAGENTA_TERRACOTTA_WALL_SIGN =
      new FullWallSignBlock(Blocks.MAGENTA_TERRACOTTA);

  public static final FullWallSignBlock FULL_LIGHT_BLUE_TERRACOTTA_WALL_SIGN =
      new FullWallSignBlock(Blocks.LIGHT_BLUE_TERRACOTTA);

  public static final FullWallSignBlock FULL_YELLOW_TERRACOTTA_WALL_SIGN =
      new FullWallSignBlock(Blocks.YELLOW_TERRACOTTA);

  public static final FullWallSignBlock FULL_LIME_TERRACOTTA_WALL_SIGN =
      new FullWallSignBlock(Blocks.LIME_TERRACOTTA);

  public static final FullWallSignBlock FULL_PINK_TERRACOTTA_WALL_SIGN =
      new FullWallSignBlock(Blocks.PINK_TERRACOTTA);

  public static final FullWallSignBlock FULL_GRAY_TERRACOTTA_WALL_SIGN =
      new FullWallSignBlock(Blocks.GRAY_TERRACOTTA);

  public static final FullWallSignBlock FULL_LIGHT_GRAY_TERRACOTTA_WALL_SIGN =
      new FullWallSignBlock(Blocks.LIGHT_GRAY_TERRACOTTA);

  public static final FullWallSignBlock FULL_CYAN_TERRACOTTA_WALL_SIGN =
      new FullWallSignBlock(Blocks.CYAN_TERRACOTTA);

  public static final FullWallSignBlock FULL_PURPLE_TERRACOTTA_WALL_SIGN =
      new FullWallSignBlock(Blocks.PURPLE_TERRACOTTA);

  public static final FullWallSignBlock FULL_BLUE_TERRACOTTA_WALL_SIGN =
      new FullWallSignBlock(Blocks.BLUE_TERRACOTTA);

  public static final FullWallSignBlock FULL_BROWN_TERRACOTTA_WALL_SIGN =
      new FullWallSignBlock(Blocks.BROWN_TERRACOTTA);

  public static final FullWallSignBlock FULL_GREEN_TERRACOTTA_WALL_SIGN =
      new FullWallSignBlock(Blocks.GREEN_TERRACOTTA);

  public static final FullWallSignBlock FULL_RED_TERRACOTTA_WALL_SIGN =
      new FullWallSignBlock(Blocks.RED_TERRACOTTA);

  public static final FullWallSignBlock FULL_BLACK_TERRACOTTA_WALL_SIGN =
      new FullWallSignBlock(Blocks.BLACK_TERRACOTTA);

  public static final ImmutableMap<DyeColor, FullWallSignBlock> FULL_TERRACOTTA_WALL_SIGNS = new ImmutableMap.Builder<DyeColor, FullWallSignBlock>()
      .put(DyeColor.WHITE, FULL_WHITE_TERRACOTTA_WALL_SIGN)
      .put(DyeColor.ORANGE, FULL_ORANGE_TERRACOTTA_WALL_SIGN)
      .put(DyeColor.MAGENTA, FULL_MAGENTA_TERRACOTTA_WALL_SIGN)
      .put(DyeColor.LIGHT_BLUE, FULL_LIGHT_BLUE_TERRACOTTA_WALL_SIGN)
      .put(DyeColor.YELLOW, FULL_YELLOW_TERRACOTTA_WALL_SIGN)
      .put(DyeColor.LIME, FULL_LIME_TERRACOTTA_WALL_SIGN)
      .put(DyeColor.PINK, FULL_PINK_TERRACOTTA_WALL_SIGN)
      .put(DyeColor.GRAY, FULL_GRAY_TERRACOTTA_WALL_SIGN)
      .put(DyeColor.LIGHT_GRAY, FULL_LIGHT_GRAY_TERRACOTTA_WALL_SIGN)
      .put(DyeColor.CYAN, FULL_CYAN_TERRACOTTA_WALL_SIGN)
      .put(DyeColor.PURPLE, FULL_PURPLE_TERRACOTTA_WALL_SIGN)
      .put(DyeColor.BLUE, FULL_BLUE_TERRACOTTA_WALL_SIGN)
      .put(DyeColor.BROWN, FULL_BROWN_TERRACOTTA_WALL_SIGN)
      .put(DyeColor.GREEN, FULL_GREEN_TERRACOTTA_WALL_SIGN)
      .put(DyeColor.RED, FULL_RED_TERRACOTTA_WALL_SIGN)
      .put(DyeColor.BLACK, FULL_BLACK_TERRACOTTA_WALL_SIGN)
      .build();

  private WallSignBlocks() {
  }
}
