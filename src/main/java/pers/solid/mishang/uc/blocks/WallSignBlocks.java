package pers.solid.mishang.uc.blocks;

import com.google.common.collect.ImmutableMap;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.block.MapColor;
import net.minecraft.util.DyeColor;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.ApiStatus;
import pers.solid.mishang.uc.annotations.MiningLevel;
import pers.solid.mishang.uc.annotations.Translucent;
import pers.solid.mishang.uc.block.*;
import pers.solid.mishang.uc.mixin.AbstractBlockAccessor;
import pers.solid.mishang.uc.mixin.AbstractBlockSettingsAccessor;

/**
 * <h1>墙上的告示牌方块</h1>
 * 包括一般的墙上告示牌和完整的墙上告示牌。二者对应不同的方块实体类型。
 */
public final class WallSignBlocks extends MishangucBlocks {

  /**
   * 隐形的告示牌。
   */
  @MiningLevel(MiningLevel.Tool.NONE)
  public static final FullWallSignBlock INVISIBLE_WALL_SIGN = registerFull("invisible_wall_sign", null, Block.Settings.create().mapColor(MapColor.CLEAR).noCollision().strength(0, 1f));

  @MiningLevel(MiningLevel.Tool.NONE)
  public static final FullWallSignBlock INVISIBLE_GLOWING_WALL_SIGN = registerFull("invisible_glowing_wall_sign", null, Block.Settings.create().mapColor(MapColor.CLEAR).noCollision().luminance(x -> 15).strength(0, 1f));

  // 木质
  @ApiStatus.AvailableSince("1.2.4")
  public static final WallSignBlock OAK_WOOD_WALL_SIGN = register("oak_wood_wall_sign", Blocks.OAK_WOOD);
  @ApiStatus.AvailableSince("1.2.4")
  public static final WallSignBlock SPRUCE_WOOD_WALL_SIGN = register("spruce_wood_wall_sign", Blocks.SPRUCE_WOOD);
  @ApiStatus.AvailableSince("1.2.4")
  public static final WallSignBlock BIRCH_WOOD_WALL_SIGN = register("birch_wood_wall_sign", Blocks.BIRCH_WOOD);
  @ApiStatus.AvailableSince("1.2.4")
  public static final WallSignBlock JUNGLE_WOOD_WALL_SIGN = register("jungle_wood_wall_sign", Blocks.JUNGLE_WOOD);
  @ApiStatus.AvailableSince("1.2.4")
  public static final WallSignBlock ACACIA_WOOD_WALL_SIGN = register("acacia_wood_wall_sign", Blocks.ACACIA_WOOD);
  @ApiStatus.AvailableSince("1.2.4")
  public static final WallSignBlock CHERRY_WOOD_WALL_SIGN = register("cherry_wood_wall_sign", Blocks.CHERRY_WOOD);
  @ApiStatus.AvailableSince("1.2.4")
  public static final WallSignBlock DARK_OAK_WOOD_WALL_SIGN = register("dark_oak_wood_wall_sign", Blocks.DARK_OAK_WOOD);
  public static final WallSignBlock PALE_OAK_WOOD_WALL_SIGN = register("pale_oak_wood_wall_sign", Blocks.PALE_OAK_WOOD);

  @ApiStatus.AvailableSince("1.2.4")
  public static final WallSignBlock MANGROVE_WOOD_WALL_SIGN = register("mangrove_wood_wall_sign", Blocks.MANGROVE_WOOD);

  public static final WallSignBlock CRIMSON_HYPHAE_WALL_SIGN = register("crimson_hyphae_wall_sign", Blocks.CRIMSON_HYPHAE);

  public static final WallSignBlock WARPED_HYPHAE_WALL_SIGN = register("warped_hyphae_wall_sign", Blocks.WARPED_HYPHAE);
  @ApiStatus.AvailableSince("1.2.4")
  public static final WallSignBlock STRIPPED_OAK_WOOD_WALL_SIGN = register("stripped_oak_wood_wall_sign", Blocks.STRIPPED_OAK_WOOD);
  @ApiStatus.AvailableSince("1.2.4")
  public static final WallSignBlock STRIPPED_SPRUCE_WOOD_WALL_SIGN = register("stripped_spruce_wood_wall_sign", Blocks.STRIPPED_SPRUCE_WOOD);
  @ApiStatus.AvailableSince("1.2.4")
  public static final WallSignBlock STRIPPED_BIRCH_WOOD_WALL_SIGN = register("stripped_birch_wood_wall_sign", Blocks.STRIPPED_BIRCH_WOOD);
  @ApiStatus.AvailableSince("1.2.4")
  public static final WallSignBlock STRIPPED_JUNGLE_WOOD_WALL_SIGN = register("stripped_jungle_wood_wall_sign", Blocks.STRIPPED_JUNGLE_WOOD);
  @ApiStatus.AvailableSince("1.2.4")
  public static final WallSignBlock STRIPPED_ACACIA_WOOD_WALL_SIGN = register("stripped_acacia_wood_wall_sign", Blocks.STRIPPED_ACACIA_WOOD);
  @ApiStatus.AvailableSince("1.2.4")
  public static final WallSignBlock STRIPPED_CHERRY_WOOD_WALL_SIGN = register("stripped_cherry_wood_wall_sign", Blocks.STRIPPED_CHERRY_WOOD);
  @ApiStatus.AvailableSince("1.2.4")
  public static final WallSignBlock STRIPPED_DARK_OAK_WOOD_WALL_SIGN = register("stripped_dark_oak_wood_wall_sign", Blocks.STRIPPED_DARK_OAK_WOOD);
  public static final WallSignBlock STRIPPED_PALE_OAK_WOOD_WALL_SIGN = register("stripped_pale_oak_wood_wall_sign", Blocks.STRIPPED_PALE_OAK_WOOD);

  @ApiStatus.AvailableSince("1.2.4")
  public static final WallSignBlock STRIPPED_MANGROVE_WOOD_WALL_SIGN = register("stripped_mangrove_wood_wall_sign", Blocks.STRIPPED_MANGROVE_WOOD, Block.Settings.copy(Blocks.STRIPPED_MANGROVE_WOOD).mapColor(MapColor.RED));

  public static final WallSignBlock STRIPPED_CRIMSON_HYPHAE_WALL_SIGN = register("stripped_crimson_hyphae_wall_sign", Blocks.STRIPPED_CRIMSON_HYPHAE);

  public static final WallSignBlock STRIPPED_WARPED_HYPHAE_WALL_SIGN = register("stripped_warped_hyphae_wall_sign", Blocks.STRIPPED_WARPED_HYPHAE);

  static {
    OAK_WOOD_WALL_SIGN.texture = Identifier.ofVanilla("block/oak_log");
    SPRUCE_WOOD_WALL_SIGN.texture = Identifier.ofVanilla("block/spruce_log");
    BIRCH_WOOD_WALL_SIGN.texture = Identifier.ofVanilla("block/birch_log");
    JUNGLE_WOOD_WALL_SIGN.texture = Identifier.ofVanilla("block/jungle_log");
    ACACIA_WOOD_WALL_SIGN.texture = Identifier.ofVanilla("block/acacia_log");
    CHERRY_WOOD_WALL_SIGN.texture = Identifier.ofVanilla("block/cherry_log");
    DARK_OAK_WOOD_WALL_SIGN.texture = Identifier.ofVanilla("block/dark_oak_log");
    PALE_OAK_WOOD_WALL_SIGN.texture = Identifier.ofVanilla("block/pale_oak_log");
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
    STRIPPED_PALE_OAK_WOOD_WALL_SIGN.texture = Identifier.ofVanilla("block/stripped_pale_oak_log");
    STRIPPED_MANGROVE_WOOD_WALL_SIGN.texture = Identifier.ofVanilla("block/stripped_mangrove_log");
    STRIPPED_CRIMSON_HYPHAE_WALL_SIGN.texture = Identifier.ofVanilla("block/stripped_crimson_stem");
    STRIPPED_WARPED_HYPHAE_WALL_SIGN.texture = Identifier.ofVanilla("block/stripped_warped_stem");
  }

  public static final WallSignBlock OAK_WALL_SIGN = register("oak_wall_sign", Blocks.OAK_PLANKS);

  public static final WallSignBlock SPRUCE_WALL_SIGN = register("spruce_wall_sign", Blocks.SPRUCE_PLANKS);

  public static final WallSignBlock BIRCH_WALL_SIGN = register("birch_wall_sign", Blocks.BIRCH_PLANKS);

  public static final WallSignBlock JUNGLE_WALL_SIGN = register("jungle_wall_sign", Blocks.JUNGLE_PLANKS);

  public static final WallSignBlock ACACIA_WALL_SIGN = register("acacia_wall_sign", Blocks.ACACIA_PLANKS);
  @ApiStatus.AvailableSince("1.1.1-mc1.19.4")
  public static final WallSignBlock CHERRY_WALL_SIGN = register("cherry_wall_sign", Blocks.CHERRY_PLANKS);

  public static final WallSignBlock DARK_OAK_WALL_SIGN = register("dark_oak_wall_sign", Blocks.DARK_OAK_PLANKS);

  public static final WallSignBlock PALE_OAK_WALL_SIGN = register("pale_oak_wall_sign", Blocks.PALE_OAK_PLANKS);

  @ApiStatus.AvailableSince("0.2.0-mc1.19")
  public static final WallSignBlock MANGROVE_WALL_SIGN = register("mangrove_wall_sign", Blocks.MANGROVE_PLANKS);

  public static final WallSignBlock CRIMSON_WALL_SIGN = register("crimson_wall_sign", Blocks.CRIMSON_PLANKS);

  public static final WallSignBlock WARPED_WALL_SIGN = register("warped_wall_sign", Blocks.WARPED_PLANKS);

  @ApiStatus.AvailableSince("1.0.4-mc1.19.3")
  @MiningLevel(MiningLevel.Tool.AXE)
  public static final WallSignBlock BAMBOO_WALL_SIGN = register("bamboo_wall_sign", Blocks.BAMBOO_BLOCK, Block.Settings.copy(Blocks.BAMBOO_BLOCK).mapColor(MapColor.DARK_GREEN));

  @ApiStatus.AvailableSince("1.0.4-mc1.19.3")
  @MiningLevel(MiningLevel.Tool.AXE)
  public static final WallSignBlock BAMBOO_PLANK_WALL_SIGN = register("bamboo_plank_wall_sign", Blocks.BAMBOO_PLANKS, Block.Settings.copy(Blocks.BAMBOO_PLANKS));

  @ApiStatus.AvailableSince("1.0.4-mc1.19.3")
  @MiningLevel(MiningLevel.Tool.AXE)
  public static final WallSignBlock BAMBOO_MOSAIC_WALL_SIGN = register("bamboo_mosaic_wall_sign", Blocks.BAMBOO_MOSAIC, Block.Settings.copy(Blocks.BAMBOO_MOSAIC));

  @ApiStatus.AvailableSince("0.2.2")
  public static final ColoredWallSignBlock COLORED_WOODEN_WALL_SIGN = registerColored("colored_wooden_wall_sign", ColoredBlocks.COLORED_PLANKS);

  // 混凝土

  public static final WallSignBlock WHITE_CONCRETE_WALL_SIGN = register("white_concrete_wall_sign", Blocks.WHITE_CONCRETE);

  public static final WallSignBlock ORANGE_CONCRETE_WALL_SIGN = register("orange_concrete_wall_sign", Blocks.ORANGE_CONCRETE);

  public static final WallSignBlock MAGENTA_CONCRETE_WALL_SIGN = register("magenta_concrete_wall_sign", Blocks.MAGENTA_CONCRETE);

  public static final WallSignBlock LIGHT_BLUE_CONCRETE_WALL_SIGN = register("light_blue_concrete_wall_sign", Blocks.LIGHT_BLUE_CONCRETE);

  public static final WallSignBlock YELLOW_CONCRETE_WALL_SIGN = register("yellow_concrete_wall_sign", Blocks.YELLOW_CONCRETE);

  public static final WallSignBlock LIME_CONCRETE_WALL_SIGN = register("lime_concrete_wall_sign", Blocks.LIME_CONCRETE);

  public static final WallSignBlock PINK_CONCRETE_WALL_SIGN = register("pink_concrete_wall_sign", Blocks.PINK_CONCRETE);

  public static final WallSignBlock GRAY_CONCRETE_WALL_SIGN = register("gray_concrete_wall_sign", Blocks.GRAY_CONCRETE);

  public static final WallSignBlock LIGHT_GRAY_CONCRETE_WALL_SIGN = register("light_gray_concrete_wall_sign", Blocks.LIGHT_GRAY_CONCRETE);

  public static final WallSignBlock CYAN_CONCRETE_WALL_SIGN = register("cyan_concrete_wall_sign", Blocks.CYAN_CONCRETE);

  public static final WallSignBlock PURPLE_CONCRETE_WALL_SIGN = register("purple_concrete_wall_sign", Blocks.PURPLE_CONCRETE);

  public static final WallSignBlock BLUE_CONCRETE_WALL_SIGN = register("blue_concrete_wall_sign", Blocks.BLUE_CONCRETE);

  public static final WallSignBlock BROWN_CONCRETE_WALL_SIGN = register("brown_concrete_wall_sign", Blocks.BROWN_CONCRETE);

  public static final WallSignBlock GREEN_CONCRETE_WALL_SIGN = register("green_concrete_wall_sign", Blocks.GREEN_CONCRETE);

  public static final WallSignBlock RED_CONCRETE_WALL_SIGN = register("red_concrete_wall_sign", Blocks.RED_CONCRETE);

  public static final WallSignBlock BLACK_CONCRETE_WALL_SIGN = register("black_concrete_wall_sign", Blocks.BLACK_CONCRETE);

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
  public static final ColoredWallSignBlock COLORED_CONCRETE_WALL_SIGN = registerColored("colored_concrete_wall_sign", ColoredBlocks.COLORED_CONCRETE);

  // 陶瓦

  public static final WallSignBlock WHITE_TERRACOTTA_WALL_SIGN = register("white_terracotta_wall_sign", Blocks.WHITE_TERRACOTTA);

  public static final WallSignBlock ORANGE_TERRACOTTA_WALL_SIGN = register("orange_terracotta_wall_sign", Blocks.ORANGE_TERRACOTTA);

  public static final WallSignBlock MAGENTA_TERRACOTTA_WALL_SIGN = register("magenta_terracotta_wall_sign", Blocks.MAGENTA_TERRACOTTA);

  public static final WallSignBlock LIGHT_BLUE_TERRACOTTA_WALL_SIGN = register("light_blue_terracotta_wall_sign", Blocks.LIGHT_BLUE_TERRACOTTA);

  public static final WallSignBlock YELLOW_TERRACOTTA_WALL_SIGN = register("yellow_terracotta_wall_sign", Blocks.YELLOW_TERRACOTTA);

  public static final WallSignBlock LIME_TERRACOTTA_WALL_SIGN = register("lime_terracotta_wall_sign", Blocks.LIME_TERRACOTTA);

  public static final WallSignBlock PINK_TERRACOTTA_WALL_SIGN = register("pink_terracotta_wall_sign", Blocks.PINK_TERRACOTTA);

  public static final WallSignBlock GRAY_TERRACOTTA_WALL_SIGN = register("gray_terracotta_wall_sign", Blocks.GRAY_TERRACOTTA);

  public static final WallSignBlock LIGHT_GRAY_TERRACOTTA_WALL_SIGN = register("light_gray_terracotta_wall_sign", Blocks.LIGHT_GRAY_TERRACOTTA);

  public static final WallSignBlock CYAN_TERRACOTTA_WALL_SIGN = register("cyan_terracotta_wall_sign", Blocks.CYAN_TERRACOTTA);

  public static final WallSignBlock PURPLE_TERRACOTTA_WALL_SIGN = register("purple_terracotta_wall_sign", Blocks.PURPLE_TERRACOTTA);

  public static final WallSignBlock BLUE_TERRACOTTA_WALL_SIGN = register("blue_terracotta_wall_sign", Blocks.BLUE_TERRACOTTA);

  public static final WallSignBlock BROWN_TERRACOTTA_WALL_SIGN = register("brown_terracotta_wall_sign", Blocks.BROWN_TERRACOTTA);

  public static final WallSignBlock GREEN_TERRACOTTA_WALL_SIGN = register("green_terracotta_wall_sign", Blocks.GREEN_TERRACOTTA);

  public static final WallSignBlock RED_TERRACOTTA_WALL_SIGN = register("red_terracotta_wall_sign", Blocks.RED_TERRACOTTA);

  public static final WallSignBlock BLACK_TERRACOTTA_WALL_SIGN = register("black_terracotta_wall_sign", Blocks.BLACK_TERRACOTTA);

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
  public static final ColoredWallSignBlock COLORED_TERRACOTTA_WALL_SIGN = registerColored("colored_terracotta_wall_sign", ColoredBlocks.COLORED_TERRACOTTA);

  // 发光的混凝土

  public static final GlowingWallSignBlock GLOWING_WHITE_CONCRETE_WALL_SIGN = registerGlowing("glowing_white_concrete_wall_sign", Blocks.WHITE_CONCRETE);

  public static final GlowingWallSignBlock GLOWING_ORANGE_CONCRETE_WALL_SIGN = registerGlowing("glowing_orange_concrete_wall_sign", Blocks.ORANGE_CONCRETE);

  public static final GlowingWallSignBlock GLOWING_MAGENTA_CONCRETE_WALL_SIGN = registerGlowing("glowing_magenta_concrete_wall_sign", Blocks.MAGENTA_CONCRETE);

  public static final GlowingWallSignBlock GLOWING_LIGHT_BLUE_CONCRETE_WALL_SIGN = registerGlowing("glowing_light_blue_concrete_wall_sign", Blocks.LIGHT_BLUE_CONCRETE);

  public static final GlowingWallSignBlock GLOWING_YELLOW_CONCRETE_WALL_SIGN = registerGlowing("glowing_yellow_concrete_wall_sign", Blocks.YELLOW_CONCRETE);

  public static final GlowingWallSignBlock GLOWING_LIME_CONCRETE_WALL_SIGN = registerGlowing("glowing_lime_concrete_wall_sign", Blocks.LIME_CONCRETE);

  public static final GlowingWallSignBlock GLOWING_PINK_CONCRETE_WALL_SIGN = registerGlowing("glowing_pink_concrete_wall_sign", Blocks.PINK_CONCRETE);

  public static final GlowingWallSignBlock GLOWING_GRAY_CONCRETE_WALL_SIGN = registerGlowing("glowing_gray_concrete_wall_sign", Blocks.GRAY_CONCRETE);

  public static final GlowingWallSignBlock GLOWING_LIGHT_GRAY_CONCRETE_WALL_SIGN = registerGlowing("glowing_light_gray_concrete_wall_sign", Blocks.LIGHT_GRAY_CONCRETE);

  public static final GlowingWallSignBlock GLOWING_CYAN_CONCRETE_WALL_SIGN = registerGlowing("glowing_cyan_concrete_wall_sign", Blocks.CYAN_CONCRETE);

  public static final GlowingWallSignBlock GLOWING_PURPLE_CONCRETE_WALL_SIGN = registerGlowing("glowing_purple_concrete_wall_sign", Blocks.PURPLE_CONCRETE);

  public static final GlowingWallSignBlock GLOWING_BLUE_CONCRETE_WALL_SIGN = registerGlowing("glowing_blue_concrete_wall_sign", Blocks.BLUE_CONCRETE);

  public static final GlowingWallSignBlock GLOWING_BROWN_CONCRETE_WALL_SIGN = registerGlowing("glowing_brown_concrete_wall_sign", Blocks.BROWN_CONCRETE);

  public static final GlowingWallSignBlock GLOWING_GREEN_CONCRETE_WALL_SIGN = registerGlowing("glowing_green_concrete_wall_sign", Blocks.GREEN_CONCRETE);

  public static final GlowingWallSignBlock GLOWING_RED_CONCRETE_WALL_SIGN = registerGlowing("glowing_red_concrete_wall_sign", Blocks.RED_CONCRETE);

  public static final GlowingWallSignBlock GLOWING_BLACK_CONCRETE_WALL_SIGN = registerGlowing("glowing_black_concrete_wall_sign", Blocks.BLACK_CONCRETE);

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
  public static final ColoredGlowingWallSignBlock COLORED_GLOWING_CONCRETE_WALL_SIGN = registerColoredGlowing("colored_glowing_concrete_wall_sign", ColoredBlocks.COLORED_CONCRETE);

  // 发光的陶瓦

  public static final GlowingWallSignBlock GLOWING_WHITE_TERRACOTTA_WALL_SIGN = registerGlowing("glowing_white_terracotta_wall_sign", Blocks.WHITE_TERRACOTTA);

  public static final GlowingWallSignBlock GLOWING_ORANGE_TERRACOTTA_WALL_SIGN = registerGlowing("glowing_orange_terracotta_wall_sign", Blocks.ORANGE_TERRACOTTA);

  public static final GlowingWallSignBlock GLOWING_MAGENTA_TERRACOTTA_WALL_SIGN = registerGlowing("glowing_magenta_terracotta_wall_sign", Blocks.MAGENTA_TERRACOTTA);

  public static final GlowingWallSignBlock GLOWING_LIGHT_BLUE_TERRACOTTA_WALL_SIGN = registerGlowing("glowing_light_blue_terracotta_wall_sign", Blocks.LIGHT_BLUE_TERRACOTTA);

  public static final GlowingWallSignBlock GLOWING_YELLOW_TERRACOTTA_WALL_SIGN = registerGlowing("glowing_yellow_terracotta_wall_sign", Blocks.YELLOW_TERRACOTTA);

  public static final GlowingWallSignBlock GLOWING_LIME_TERRACOTTA_WALL_SIGN = registerGlowing("glowing_lime_terracotta_wall_sign", Blocks.LIME_TERRACOTTA);

  public static final GlowingWallSignBlock GLOWING_PINK_TERRACOTTA_WALL_SIGN = registerGlowing("glowing_pink_terracotta_wall_sign", Blocks.PINK_TERRACOTTA);

  public static final GlowingWallSignBlock GLOWING_GRAY_TERRACOTTA_WALL_SIGN = registerGlowing("glowing_gray_terracotta_wall_sign", Blocks.GRAY_TERRACOTTA);

  public static final GlowingWallSignBlock GLOWING_LIGHT_GRAY_TERRACOTTA_WALL_SIGN = registerGlowing("glowing_light_gray_terracotta_wall_sign", Blocks.LIGHT_GRAY_TERRACOTTA);

  public static final GlowingWallSignBlock GLOWING_CYAN_TERRACOTTA_WALL_SIGN = registerGlowing("glowing_cyan_terracotta_wall_sign", Blocks.CYAN_TERRACOTTA);

  public static final GlowingWallSignBlock GLOWING_PURPLE_TERRACOTTA_WALL_SIGN = registerGlowing("glowing_purple_terracotta_wall_sign", Blocks.PURPLE_TERRACOTTA);

  public static final GlowingWallSignBlock GLOWING_BLUE_TERRACOTTA_WALL_SIGN = registerGlowing("glowing_blue_terracotta_wall_sign", Blocks.BLUE_TERRACOTTA);

  public static final GlowingWallSignBlock GLOWING_BROWN_TERRACOTTA_WALL_SIGN = registerGlowing("glowing_brown_terracotta_wall_sign", Blocks.BROWN_TERRACOTTA);

  public static final GlowingWallSignBlock GLOWING_GREEN_TERRACOTTA_WALL_SIGN = registerGlowing("glowing_green_terracotta_wall_sign", Blocks.GREEN_TERRACOTTA);

  public static final GlowingWallSignBlock GLOWING_RED_TERRACOTTA_WALL_SIGN = registerGlowing("glowing_red_terracotta_wall_sign", Blocks.RED_TERRACOTTA);

  public static final GlowingWallSignBlock GLOWING_BLACK_TERRACOTTA_WALL_SIGN = registerGlowing("glowing_black_terracotta_wall_sign", Blocks.BLACK_TERRACOTTA);

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
  public static final ColoredGlowingWallSignBlock COLORED_GLOWING_TERRACOTTA_WALL_SIGN = registerColoredGlowing("colored_glowing_terracotta_wall_sign", ColoredBlocks.COLORED_TERRACOTTA);

  // 一些比较杂项的
  /// 石头
  @ApiStatus.AvailableSince("0.1.7")
  public static final WallSignBlock STONE_WALL_SIGN = register("stone_wall_sign", Blocks.STONE);
  @ApiStatus.AvailableSince("0.1.7")
  public static final GlowingWallSignBlock GLOWING_STONE_WALL_SIGN = registerGlowing("glowing_stone_wall_sign", Blocks.STONE);
  @ApiStatus.AvailableSince("1.0.2")
  public static final ColoredWallSignBlock COLORED_STONE_WALL_SIGN = registerColored("colored_stone_wall_sign", ColoredBlocks.COLORED_STONE);
  @ApiStatus.AvailableSince("1.0.2")
  public static final ColoredGlowingWallSignBlock COLORED_GLOWING_STONE_WALL_SIGN = registerColoredGlowing("colored_glowing_stone_wall_sign", ColoredBlocks.COLORED_STONE);
  /// 圆石
  @ApiStatus.AvailableSince("0.2.4")
  public static final WallSignBlock COBBLESTONE_WALL_SIGN = register("cobblestone_wall_sign", Blocks.COBBLESTONE);
  @ApiStatus.AvailableSince("0.2.4")
  public static final GlowingWallSignBlock GLOWING_COBBLESTONE_WALL_SIGN = registerGlowing("glowing_cobblestone_wall_sign", Blocks.COBBLESTONE);
  @ApiStatus.AvailableSince("1.0.2")
  public static final ColoredWallSignBlock COLORED_COBBLESTONE_WALL_SIGN = registerColored("colored_cobblestone_wall_sign", ColoredBlocks.COLORED_COBBLESTONE);
  @ApiStatus.AvailableSince("1.0.2")
  public static final ColoredGlowingWallSignBlock COLORED_GLOWING_COBBLESTONE_WALL_SIGN = registerColoredGlowing("colored_glowing_cobblestone_wall_sign", ColoredBlocks.COLORED_COBBLESTONE);
  /// 石砖
  @ApiStatus.AvailableSince("0.1.7")
  public static final WallSignBlock STONE_BRICK_WALL_SIGN = register("stone_brick_wall_sign", Blocks.STONE_BRICKS);
  @ApiStatus.AvailableSince("0.1.7")
  public static final GlowingWallSignBlock GLOWING_STONE_BRICK_WALL_SIGN = registerGlowing("glowing_stone_brick_wall_sign", Blocks.STONE_BRICKS);
  @ApiStatus.AvailableSince("1.0.2")
  public static final ColoredWallSignBlock COLORED_STONE_BRICK_WALL_SIGN = registerColored("colored_stone_brick_wall_sign", ColoredBlocks.COLORED_STONE_BRICKS);
  @ApiStatus.AvailableSince("1.0.2")
  public static final ColoredGlowingWallSignBlock COLORED_GLOWING_STONE_BRICK_WALL_SIGN = registerColoredGlowing("colored_glowing_stone_brick_wall_sign", ColoredBlocks.COLORED_STONE_BRICKS);
  // 铁块
  @ApiStatus.AvailableSince("0.1.7")
  @MiningLevel(level = MiningLevel.Level.STONE)
  public static final WallSignBlock IRON_WALL_SIGN = register("iron_wall_sign", Blocks.IRON_BLOCK);
  @ApiStatus.AvailableSince("0.1.7")
  @MiningLevel(level = MiningLevel.Level.STONE)
  public static final GlowingWallSignBlock GLOWING_IRON_WALL_SIGN = registerGlowing("glowing_iron_wall_sign", Blocks.IRON_BLOCK);
  @ApiStatus.AvailableSince("1.0.2")
  @MiningLevel(level = MiningLevel.Level.STONE)
  public static final ColoredWallSignBlock COLORED_IRON_WALL_SIGN = registerColored("colored_iron_wall_sign", ColoredBlocks.COLORED_IRON_BLOCK);
  @ApiStatus.AvailableSince("1.0.2")
  @MiningLevel(level = MiningLevel.Level.STONE)
  public static final ColoredGlowingWallSignBlock COLORED_GLOWING_IRON_WALL_SIGN = registerColoredGlowing("colored_glowing_iron_wall_sign", ColoredBlocks.COLORED_IRON_BLOCK);
  // 金块
  @ApiStatus.AvailableSince("0.1.7")
  @MiningLevel(level = MiningLevel.Level.IRON)
  public static final WallSignBlock GOLD_WALL_SIGN = register("gold_wall_sign", Blocks.GOLD_BLOCK);
  @ApiStatus.AvailableSince("0.1.7")
  @MiningLevel(level = MiningLevel.Level.IRON)
  public static final GlowingWallSignBlock GLOWING_GOLD_WALL_SIGN = registerGlowing("glowing_gold_wall_sign", Blocks.GOLD_BLOCK);
  // 钻石块
  @ApiStatus.AvailableSince("0.1.7")
  @MiningLevel(level = MiningLevel.Level.IRON)
  public static final WallSignBlock DIAMOND_WALL_SIGN = register("diamond_wall_sign", Blocks.DIAMOND_BLOCK);
  @ApiStatus.AvailableSince("0.1.7")
  @MiningLevel(level = MiningLevel.Level.IRON)
  public static final GlowingWallSignBlock GLOWING_DIAMOND_WALL_SIGN = registerGlowing("glowing_diamond_wall_sign", Blocks.DIAMOND_BLOCK);
  // 绿宝石块
  @ApiStatus.AvailableSince("0.1.7")
  @MiningLevel(level = MiningLevel.Level.IRON)
  public static final WallSignBlock EMERALD_WALL_SIGN = register("emerald_wall_sign", Blocks.EMERALD_BLOCK);
  @ApiStatus.AvailableSince("0.1.7")
  @MiningLevel(level = MiningLevel.Level.IRON)
  public static final GlowingWallSignBlock GLOWING_EMERALD_WALL_SIGN = registerGlowing("glowing_emerald_wall_sign", Blocks.EMERALD_BLOCK);
  // 青金石块
  @ApiStatus.AvailableSince("0.1.7")
  @MiningLevel(level = MiningLevel.Level.STONE)
  public static final WallSignBlock LAPIS_WALL_SIGN = register("lapis_wall_sign", Blocks.LAPIS_BLOCK);
  @ApiStatus.AvailableSince("0.1.7")
  @MiningLevel(level = MiningLevel.Level.STONE)
  public static final GlowingWallSignBlock GLOWING_LAPIS_WALL_SIGN = registerGlowing("glowing_lapis_wall_sign", Blocks.LAPIS_BLOCK);
  // 下界合金块
  @ApiStatus.AvailableSince("0.2.4")
  @MiningLevel(level = MiningLevel.Level.DIAMOND)
  public static final WallSignBlock NETHERITE_WALL_SIGN = register("netherite_wall_sign", Blocks.NETHERITE_BLOCK);
  @ApiStatus.AvailableSince("0.2.4")
  @MiningLevel(level = MiningLevel.Level.DIAMOND)
  public static final GlowingWallSignBlock GLOWING_NETHERITE_WALL_SIGN = registerGlowing("glowing_netherite_wall_sign", Blocks.NETHERITE_BLOCK);
  // 黑曜石
  @ApiStatus.AvailableSince("0.2.4")
  @MiningLevel(level = MiningLevel.Level.DIAMOND)
  public static final WallSignBlock OBSIDIAN_WALL_SIGN = register("obsidian_wall_sign", Blocks.OBSIDIAN);
  @ApiStatus.AvailableSince("0.2.4")
  @MiningLevel(level = MiningLevel.Level.DIAMOND)
  public static final GlowingWallSignBlock GLOWING_OBSIDIAN_WALL_SIGN = registerGlowing("glowing_obsidian_wall_sign", Blocks.OBSIDIAN);
  // 哭泣的黑曜石
  @ApiStatus.AvailableSince("0.2.4")
  @MiningLevel(level = MiningLevel.Level.DIAMOND)
  public static final WallSignBlock CRYING_OBSIDIAN_WALL_SIGN = register("crying_obsidian_wall_sign", Blocks.CRYING_OBSIDIAN);
  @ApiStatus.AvailableSince("0.2.4")
  @MiningLevel(level = MiningLevel.Level.DIAMOND)
  public static final GlowingWallSignBlock GLOWING_CRYING_OBSIDIAN_WALL_SIGN = registerGlowing("glowing_crying_obsidian_wall_sign", Blocks.CRYING_OBSIDIAN);
  // 下界岩
  @ApiStatus.AvailableSince("0.1.7")
  public static final WallSignBlock NETHERRACK_WALL_SIGN = register("netherrack_wall_sign", Blocks.NETHERRACK);
  @ApiStatus.AvailableSince("0.1.7")
  public static final GlowingWallSignBlock GLOWING_NETHERRACK_WALL_SIGN = registerGlowing("glowing_netherrack_wall_sign", Blocks.NETHERRACK);
  // 下界砖
  @ApiStatus.AvailableSince("0.1.7")
  public static final WallSignBlock NETHER_BRICK_WALL_SIGN = register("nether_brick_wall_sign", Blocks.NETHER_BRICKS);
  @ApiStatus.AvailableSince("0.1.7")
  public static final GlowingWallSignBlock GLOWING_NETHER_BRICK_WALL_SIGN = registerGlowing("glowing_nether_brick_wall_sign", Blocks.NETHER_BRICKS);
  // 黑石
  @ApiStatus.AvailableSince("0.1.7")
  public static final WallSignBlock BLACKSTONE_WALL_SIGN = register("blackstone_wall_sign", Blocks.BLACKSTONE);
  @ApiStatus.AvailableSince("0.1.7")
  public static final GlowingWallSignBlock GLOWING_BLACKSTONE_WALL_SIGN = registerGlowing("glowing_blackstone_wall_sign", Blocks.BLACKSTONE);
  // 磨制黑石
  @ApiStatus.AvailableSince("0.1.7")
  public static final WallSignBlock POLISHED_BLACKSTONE_WALL_SIGN = register("polished_blackstone_wall_sign", Blocks.POLISHED_BLACKSTONE);
  @ApiStatus.AvailableSince("0.1.7")
  public static final GlowingWallSignBlock GLOWING_POLISHED_BLACKSTONE_WALL_SIGN = registerGlowing("glowing_polished_blackstone_wall_sign", Blocks.POLISHED_BLACKSTONE);

  static {
    GLOWING_NETHERRACK_WALL_SIGN.glowTexture = Identifier.ofVanilla("block/lava_still");
    GLOWING_NETHER_BRICK_WALL_SIGN.glowTexture = Identifier.ofVanilla("block/lava_still");
    GLOWING_BLACKSTONE_WALL_SIGN.glowTexture = Identifier.ofVanilla("block/glowstone");
    GLOWING_POLISHED_BLACKSTONE_WALL_SIGN.glowTexture = Identifier.ofVanilla("block/glowstone");
  }

  // 雪
  @ApiStatus.AvailableSince("0.1.7")
  @MiningLevel(MiningLevel.Tool.SHOVEL)
  public static final WallSignBlock SNOW_WALL_SIGN = register("snow_wall_sign", Blocks.SNOW_BLOCK);
  @ApiStatus.AvailableSince("0.1.7")
  @MiningLevel(MiningLevel.Tool.SHOVEL)
  public static final GlowingWallSignBlock GLOWING_SNOW_WALL_SIGN = registerGlowing("glowing_snow_wall_sign", Blocks.SNOW_BLOCK);
  // 冰
  @ApiStatus.AvailableSince("0.1.7")
  @Translucent
  public static final WallSignBlock ICE_WALL_SIGN = register("ice_wall_sign", Blocks.ICE);
  @ApiStatus.AvailableSince("0.1.7")
  public static final WallSignBlock PACKED_ICE_WALL_SIGN = register("packed_ice_wall_sign", Blocks.PACKED_ICE);
  @ApiStatus.AvailableSince("0.1.7")
  public static final GlowingWallSignBlock GLOWING_PACKED_ICE_WALL_SIGN = registerGlowing("glowing_packed_ice_wall_sign", Blocks.PACKED_ICE);
  @ApiStatus.AvailableSince("0.1.7")
  public static final WallSignBlock BLUE_ICE_WALL_SIGN = register("blue_ice_wall_sign", Blocks.BLUE_ICE);
  @ApiStatus.AvailableSince("0.1.7")
  public static final GlowingWallSignBlock GLOWING_BLUE_ICE_WALL_SIGN = registerGlowing("glowing_blue_ice_wall_sign", Blocks.BLUE_ICE);

  static {
    SNOW_WALL_SIGN.texture = Identifier.ofVanilla("block/snow");
    GLOWING_SNOW_WALL_SIGN.texture = Identifier.ofVanilla("block/snow");
  }

  // 完整的混凝土

  public static final FullWallSignBlock FULL_WHITE_CONCRETE_WALL_SIGN = registerFull("full_white_concrete_wall_sign", Blocks.WHITE_CONCRETE);

  public static final FullWallSignBlock FULL_ORANGE_CONCRETE_WALL_SIGN = registerFull("full_orange_concrete_wall_sign", Blocks.ORANGE_CONCRETE);

  public static final FullWallSignBlock FULL_MAGENTA_CONCRETE_WALL_SIGN = registerFull("full_magenta_concrete_wall_sign", Blocks.MAGENTA_CONCRETE);

  public static final FullWallSignBlock FULL_LIGHT_BLUE_CONCRETE_WALL_SIGN = registerFull("full_light_blue_concrete_wall_sign", Blocks.LIGHT_BLUE_CONCRETE);

  public static final FullWallSignBlock FULL_YELLOW_CONCRETE_WALL_SIGN = registerFull("full_yellow_concrete_wall_sign", Blocks.YELLOW_CONCRETE);

  public static final FullWallSignBlock FULL_LIME_CONCRETE_WALL_SIGN = registerFull("full_lime_concrete_wall_sign", Blocks.LIME_CONCRETE);

  public static final FullWallSignBlock FULL_PINK_CONCRETE_WALL_SIGN = registerFull("full_pink_concrete_wall_sign", Blocks.PINK_CONCRETE);

  public static final FullWallSignBlock FULL_GRAY_CONCRETE_WALL_SIGN = registerFull("full_gray_concrete_wall_sign", Blocks.GRAY_CONCRETE);

  public static final FullWallSignBlock FULL_LIGHT_GRAY_CONCRETE_WALL_SIGN = registerFull("full_light_gray_concrete_wall_sign", Blocks.LIGHT_GRAY_CONCRETE);

  public static final FullWallSignBlock FULL_CYAN_CONCRETE_WALL_SIGN = registerFull("full_cyan_concrete_wall_sign", Blocks.CYAN_CONCRETE);

  public static final FullWallSignBlock FULL_PURPLE_CONCRETE_WALL_SIGN = registerFull("full_purple_concrete_wall_sign", Blocks.PURPLE_CONCRETE);

  public static final FullWallSignBlock FULL_BLUE_CONCRETE_WALL_SIGN = registerFull("full_blue_concrete_wall_sign", Blocks.BLUE_CONCRETE);

  public static final FullWallSignBlock FULL_BROWN_CONCRETE_WALL_SIGN = registerFull("full_brown_concrete_wall_sign", Blocks.BROWN_CONCRETE);

  public static final FullWallSignBlock FULL_GREEN_CONCRETE_WALL_SIGN = registerFull("full_green_concrete_wall_sign", Blocks.GREEN_CONCRETE);

  public static final FullWallSignBlock FULL_RED_CONCRETE_WALL_SIGN = registerFull("full_red_concrete_wall_sign", Blocks.RED_CONCRETE);

  public static final FullWallSignBlock FULL_BLACK_CONCRETE_WALL_SIGN = registerFull("full_black_concrete_wall_sign", Blocks.BLACK_CONCRETE);

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

  public static final FullWallSignBlock FULL_WHITE_TERRACOTTA_WALL_SIGN = registerFull("full_white_terracotta_wall_sign", Blocks.WHITE_TERRACOTTA);

  public static final FullWallSignBlock FULL_ORANGE_TERRACOTTA_WALL_SIGN = registerFull("full_orange_terracotta_wall_sign", Blocks.ORANGE_TERRACOTTA);

  public static final FullWallSignBlock FULL_MAGENTA_TERRACOTTA_WALL_SIGN = registerFull("full_magenta_terracotta_wall_sign", Blocks.MAGENTA_TERRACOTTA);

  public static final FullWallSignBlock FULL_LIGHT_BLUE_TERRACOTTA_WALL_SIGN = registerFull("full_light_blue_terracotta_wall_sign", Blocks.LIGHT_BLUE_TERRACOTTA);

  public static final FullWallSignBlock FULL_YELLOW_TERRACOTTA_WALL_SIGN = registerFull("full_yellow_terracotta_wall_sign", Blocks.YELLOW_TERRACOTTA);

  public static final FullWallSignBlock FULL_LIME_TERRACOTTA_WALL_SIGN = registerFull("full_lime_terracotta_wall_sign", Blocks.LIME_TERRACOTTA);

  public static final FullWallSignBlock FULL_PINK_TERRACOTTA_WALL_SIGN = registerFull("full_pink_terracotta_wall_sign", Blocks.PINK_TERRACOTTA);

  public static final FullWallSignBlock FULL_GRAY_TERRACOTTA_WALL_SIGN = registerFull("full_gray_terracotta_wall_sign", Blocks.GRAY_TERRACOTTA);

  public static final FullWallSignBlock FULL_LIGHT_GRAY_TERRACOTTA_WALL_SIGN = registerFull("full_light_gray_terracotta_wall_sign", Blocks.LIGHT_GRAY_TERRACOTTA);

  public static final FullWallSignBlock FULL_CYAN_TERRACOTTA_WALL_SIGN = registerFull("full_cyan_terracotta_wall_sign", Blocks.CYAN_TERRACOTTA);

  public static final FullWallSignBlock FULL_PURPLE_TERRACOTTA_WALL_SIGN = registerFull("full_purple_terracotta_wall_sign", Blocks.PURPLE_TERRACOTTA);

  public static final FullWallSignBlock FULL_BLUE_TERRACOTTA_WALL_SIGN = registerFull("full_blue_terracotta_wall_sign", Blocks.BLUE_TERRACOTTA);

  public static final FullWallSignBlock FULL_BROWN_TERRACOTTA_WALL_SIGN = registerFull("full_brown_terracotta_wall_sign", Blocks.BROWN_TERRACOTTA);

  public static final FullWallSignBlock FULL_GREEN_TERRACOTTA_WALL_SIGN = registerFull("full_green_terracotta_wall_sign", Blocks.GREEN_TERRACOTTA);

  public static final FullWallSignBlock FULL_RED_TERRACOTTA_WALL_SIGN = registerFull("full_red_terracotta_wall_sign", Blocks.RED_TERRACOTTA);

  public static final FullWallSignBlock FULL_BLACK_TERRACOTTA_WALL_SIGN = registerFull("full_black_terracotta_wall_sign", Blocks.BLACK_TERRACOTTA);

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

  private static WallSignBlock register(String name, Block baseBlock, AbstractBlock.Settings settings) {
    return MishangucBlocks.register(name, settings1 -> new WallSignBlock(baseBlock, settings1), settings);
  }

  private static WallSignBlock register(String name, Block baseBlock) {
    return register(name, baseBlock, AbstractBlock.Settings.copy(baseBlock));
  }

  private static FullWallSignBlock registerFull(String name, Block baseBlock, AbstractBlock.Settings settings) {
    return MishangucBlocks.register(name, settings1 -> new FullWallSignBlock(baseBlock, settings1), settings);
  }

  private static FullWallSignBlock registerFull(String name, Block baseBlock) {
    return registerFull(name, baseBlock, AbstractBlock.Settings.copy(baseBlock));
  }

  private static GlowingWallSignBlock registerGlowing(String name, Block baseBlock) {
    return MishangucBlocks.register(name, settings1 -> new GlowingWallSignBlock(baseBlock, settings1), AbstractBlock.Settings.copy(baseBlock));
  }

  private static ColoredWallSignBlock registerColored(String name, Block baseBlock) {
    return MishangucBlocks.register(name, settings -> new ColoredWallSignBlock(baseBlock, settings), AbstractBlock.Settings.copy(baseBlock));
  }

  private static ColoredGlowingWallSignBlock registerColoredGlowing(String name, Block baseBlock) {
    return MishangucBlocks.register(name, settings -> new ColoredGlowingWallSignBlock(baseBlock, settings), AbstractBlock.Settings.copy(baseBlock));
  }
}
