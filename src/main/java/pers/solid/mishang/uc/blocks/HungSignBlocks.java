package pers.solid.mishang.uc.blocks;

import com.google.common.annotations.Beta;
import com.google.common.collect.ImmutableMap;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.fabricmc.fabric.mixin.object.builder.AbstractBlockAccessor;
import net.fabricmc.fabric.mixin.object.builder.AbstractBlockSettingsAccessor;
import net.minecraft.block.Blocks;
import net.minecraft.block.MapColor;
import net.minecraft.state.property.Properties;
import net.minecraft.util.DyeColor;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Direction;
import org.jetbrains.annotations.ApiStatus;
import pers.solid.mishang.uc.annotations.MiningLevel;
import pers.solid.mishang.uc.annotations.Translucent;
import pers.solid.mishang.uc.block.*;
import pers.solid.mishang.uc.data.MishangucRecipeProvider;

import java.util.Map;

/**
 * <h1>告示牌类方块</h1>
 * 具有多种不同颜色和纹理。每一种告示牌都有对应的告示牌杆，且部分的告示牌都有对应的发光告示牌方块。<br>
 * 每个告示牌都要在 {@link pers.solid.mishang.uc.blockentity.MishangucBlockEntities#HUNG_SIGN_BLOCK_ENTITY} 中能够识别，因此添加新的告示牌需要在该字段的相关参数中添加。<br>
 * 同时，还需要注意在 {@link MishangucRecipeProvider} 中添加此方块。
 *
 * @see HungSignBlock
 * @see HungSignBarBlock
 * @see GlowingHungSignBlock
 */
public final class HungSignBlocks extends MishangucBlocks {

  // 木告示牌部分。仅有不发光的告示牌。
  @ApiStatus.AvailableSince("1.2.4")
  public static final HungSignBlock OAK_WOOD_HUNG_SIGN = new HungSignBlock(Blocks.OAK_WOOD);
  @ApiStatus.AvailableSince("1.2.4")
  public static final HungSignBlock SPRUCE_WOOD_HUNG_SIGN = new HungSignBlock(Blocks.SPRUCE_WOOD);
  @ApiStatus.AvailableSince("1.2.4")
  public static final HungSignBlock BIRCH_WOOD_HUNG_SIGN = new HungSignBlock(Blocks.BIRCH_WOOD);
  @ApiStatus.AvailableSince("1.2.4")
  public static final HungSignBlock JUNGLE_WOOD_HUNG_SIGN = new HungSignBlock(Blocks.JUNGLE_WOOD);
  @ApiStatus.AvailableSince("1.2.4")
  public static final HungSignBlock ACACIA_WOOD_HUNG_SIGN = new HungSignBlock(Blocks.ACACIA_WOOD);
  @ApiStatus.AvailableSince("1.2.4")
  public static final HungSignBlock CHERRY_WOOD_HUNG_SIGN = new HungSignBlock(Blocks.CHERRY_WOOD);
  @ApiStatus.AvailableSince("1.2.4")
  public static final HungSignBlock DARK_OAK_WOOD_HUNG_SIGN = new HungSignBlock(Blocks.DARK_OAK_WOOD);
  @ApiStatus.AvailableSince("1.2.4")
  public static final HungSignBlock MANGROVE_WOOD_HUNG_SIGN = new HungSignBlock(Blocks.MANGROVE_WOOD);
  @ApiStatus.AvailableSince("0.1.7")
  public static final HungSignBlock CRIMSON_HYPHAE_HUNG_SIGN = new HungSignBlock(Blocks.CRIMSON_HYPHAE);
  @ApiStatus.AvailableSince("0.1.7")
  public static final HungSignBlock WARPED_HYPHAE_HUNG_SIGN = new HungSignBlock(Blocks.WARPED_HYPHAE);
  @ApiStatus.AvailableSince("1.2.4")
  public static final HungSignBlock STRIPPED_OAK_WOOD_HUNG_SIGN = new HungSignBlock(Blocks.STRIPPED_OAK_WOOD);
  @ApiStatus.AvailableSince("1.2.4")
  public static final HungSignBlock STRIPPED_SPRUCE_WOOD_HUNG_SIGN = new HungSignBlock(Blocks.STRIPPED_SPRUCE_WOOD);
  @ApiStatus.AvailableSince("1.2.4")
  public static final HungSignBlock STRIPPED_BIRCH_WOOD_HUNG_SIGN = new HungSignBlock(Blocks.STRIPPED_BIRCH_WOOD);
  @ApiStatus.AvailableSince("1.2.4")
  public static final HungSignBlock STRIPPED_JUNGLE_WOOD_HUNG_SIGN = new HungSignBlock(Blocks.STRIPPED_JUNGLE_WOOD);
  @ApiStatus.AvailableSince("1.2.4")
  public static final HungSignBlock STRIPPED_ACACIA_WOOD_HUNG_SIGN = new HungSignBlock(Blocks.STRIPPED_ACACIA_WOOD);
  @ApiStatus.AvailableSince("1.2.4")
  public static final HungSignBlock STRIPPED_CHERRY_WOOD_HUNG_SIGN = new HungSignBlock(Blocks.STRIPPED_CHERRY_WOOD);
  @ApiStatus.AvailableSince("1.2.4")
  public static final HungSignBlock STRIPPED_DARK_OAK_WOOD_HUNG_SIGN = new HungSignBlock(Blocks.STRIPPED_DARK_OAK_WOOD);
  @ApiStatus.AvailableSince("1.2.4")
  public static final HungSignBlock STRIPPED_MANGROVE_WOOD_HUNG_SIGN = new HungSignBlock(Blocks.STRIPPED_MANGROVE_WOOD, FabricBlockSettings.copyOf(Blocks.STRIPPED_MANGROVE_WOOD).mapColor(MapColor.RED));
  @ApiStatus.AvailableSince("0.1.7")
  public static final HungSignBlock STRIPPED_CRIMSON_HYPHAE_HUNG_SIGN = new HungSignBlock(Blocks.STRIPPED_CRIMSON_HYPHAE);
  @ApiStatus.AvailableSince("0.1.7")
  public static final HungSignBlock STRIPPED_WARPED_HYPHAE_HUNG_SIGN = new HungSignBlock(Blocks.STRIPPED_WARPED_HYPHAE);

  @ApiStatus.AvailableSince("0.1.7")
  public static final HungSignBlock OAK_HUNG_SIGN = new HungSignBlock(Blocks.OAK_PLANKS);
  @ApiStatus.AvailableSince("0.1.7")
  public static final HungSignBlock SPRUCE_HUNG_SIGN = new HungSignBlock(Blocks.SPRUCE_PLANKS);
  @ApiStatus.AvailableSince("0.1.7")
  public static final HungSignBlock BIRCH_HUNG_SIGN = new HungSignBlock(Blocks.BIRCH_PLANKS);
  @ApiStatus.AvailableSince("0.1.7")
  public static final HungSignBlock JUNGLE_HUNG_SIGN = new HungSignBlock(Blocks.JUNGLE_PLANKS);
  @ApiStatus.AvailableSince("0.1.7")
  public static final HungSignBlock ACACIA_HUNG_SIGN = new HungSignBlock(Blocks.ACACIA_PLANKS);
  @ApiStatus.AvailableSince("1.1.1-mc1.19.4")
  public static final HungSignBlock CHERRY_HUNG_SIGN = new HungSignBlock(Blocks.CHERRY_PLANKS);
  @ApiStatus.AvailableSince("0.1.7")
  public static final HungSignBlock DARK_OAK_HUNG_SIGN = new HungSignBlock(Blocks.DARK_OAK_PLANKS);
  @ApiStatus.AvailableSince("0.2.0-mc1.19")
  public static final HungSignBlock MANGROVE_HUNG_SIGN = new HungSignBlock(Blocks.MANGROVE_PLANKS);
  @ApiStatus.AvailableSince("0.1.7")
  public static final HungSignBlock CRIMSON_HUNG_SIGN = new HungSignBlock(Blocks.CRIMSON_PLANKS);
  @ApiStatus.AvailableSince("0.1.7")
  public static final HungSignBlock WARPED_HUNG_SIGN = new HungSignBlock(Blocks.WARPED_PLANKS);

  @ApiStatus.AvailableSince("1.0.4-mc1.19.3")
  @MiningLevel(MiningLevel.Tool.AXE)
  public static final HungSignBlock BAMBOO_HUNG_SIGN = new HungSignBlock(Blocks.BAMBOO_BLOCK, FabricBlockSettings.copyOf(Blocks.BAMBOO_BLOCK).mapColor(((AbstractBlockSettingsAccessor) ((AbstractBlockAccessor) Blocks.BAMBOO_BLOCK).getSettings()).getMapColorProvider().apply(Blocks.BAMBOO_BLOCK.getDefaultState().with(Properties.AXIS, Direction.Axis.X))));
  @ApiStatus.AvailableSince("1.0.4-mc1.19.3")
  @MiningLevel(MiningLevel.Tool.AXE)
  public static final HungSignBlock BAMBOO_PLANK_HUNG_SIGN = new HungSignBlock(Blocks.BAMBOO_PLANKS, FabricBlockSettings.copyOf(Blocks.BAMBOO_PLANKS));
  @ApiStatus.AvailableSince("1.0.4-mc1.19.3")
  @MiningLevel(MiningLevel.Tool.AXE)
  public static final HungSignBlock BAMBOO_MOSAIC_HUNG_SIGN = new HungSignBlock(Blocks.BAMBOO_MOSAIC, FabricBlockSettings.copyOf(Blocks.BAMBOO_MOSAIC));

  // 木告示牌杆部分。
  @ApiStatus.AvailableSince("0.1.7")
  public static final HungSignBarBlock OAK_HUNG_SIGN_BAR = new HungSignBarBlock(Blocks.OAK_WOOD);
  @ApiStatus.AvailableSince("0.1.7")
  public static final HungSignBarBlock SPRUCE_HUNG_SIGN_BAR = new HungSignBarBlock(Blocks.SPRUCE_WOOD);
  @ApiStatus.AvailableSince("0.1.7")
  public static final HungSignBarBlock BIRCH_HUNG_SIGN_BAR = new HungSignBarBlock(Blocks.BIRCH_WOOD);
  @ApiStatus.AvailableSince("0.1.7")
  public static final HungSignBarBlock JUNGLE_HUNG_SIGN_BAR = new HungSignBarBlock(Blocks.JUNGLE_WOOD);
  @ApiStatus.AvailableSince("0.1.7")
  public static final HungSignBarBlock ACACIA_HUNG_SIGN_BAR = new HungSignBarBlock(Blocks.ACACIA_WOOD);
  @ApiStatus.AvailableSince("1.1.1-mc1.19.4")
  public static final HungSignBarBlock CHERRY_HUNG_SIGN_BAR = new HungSignBarBlock(Blocks.CHERRY_WOOD);
  @ApiStatus.AvailableSince("0.1.7")
  public static final HungSignBarBlock DARK_OAK_HUNG_SIGN_BAR = new HungSignBarBlock(Blocks.DARK_OAK_WOOD);
  @ApiStatus.AvailableSince("0.2.0-mc1.19")
  public static final HungSignBarBlock MANGROVE_HUNG_SIGN_BAR = new HungSignBarBlock(Blocks.MANGROVE_WOOD);
  @ApiStatus.AvailableSince("0.1.7")
  public static final HungSignBarBlock CRIMSON_HUNG_SIGN_BAR = new HungSignBarBlock(Blocks.CRIMSON_HYPHAE);
  @ApiStatus.AvailableSince("0.1.7")
  public static final HungSignBarBlock WARPED_HUNG_SIGN_BAR = new HungSignBarBlock(Blocks.WARPED_HYPHAE);
  @ApiStatus.AvailableSince("1.2.4")
  public static final HungSignBarBlock STRIPPED_OAK_HUNG_SIGN_BAR = new HungSignBarBlock(Blocks.OAK_WOOD);
  @ApiStatus.AvailableSince("1.2.4")
  public static final HungSignBarBlock STRIPPED_SPRUCE_HUNG_SIGN_BAR = new HungSignBarBlock(Blocks.STRIPPED_SPRUCE_WOOD);
  @ApiStatus.AvailableSince("1.2.4")
  public static final HungSignBarBlock STRIPPED_BIRCH_HUNG_SIGN_BAR = new HungSignBarBlock(Blocks.STRIPPED_BIRCH_WOOD);
  @ApiStatus.AvailableSince("1.2.4")
  public static final HungSignBarBlock STRIPPED_JUNGLE_HUNG_SIGN_BAR = new HungSignBarBlock(Blocks.STRIPPED_JUNGLE_WOOD);
  @ApiStatus.AvailableSince("1.2.4")
  public static final HungSignBarBlock STRIPPED_ACACIA_HUNG_SIGN_BAR = new HungSignBarBlock(Blocks.STRIPPED_ACACIA_WOOD);
  @ApiStatus.AvailableSince("1.2.4")
  public static final HungSignBarBlock STRIPPED_CHERRY_HUNG_SIGN_BAR = new HungSignBarBlock(Blocks.STRIPPED_CHERRY_WOOD);
  @ApiStatus.AvailableSince("1.2.4")
  public static final HungSignBarBlock STRIPPED_DARK_OAK_HUNG_SIGN_BAR = new HungSignBarBlock(Blocks.STRIPPED_DARK_OAK_WOOD);
  @ApiStatus.AvailableSince("1.2.4")
  public static final HungSignBarBlock STRIPPED_MANGROVE_HUNG_SIGN_BAR = new HungSignBarBlock(Blocks.STRIPPED_MANGROVE_WOOD, FabricBlockSettings.copyOf(Blocks.STRIPPED_MANGROVE_WOOD).mapColor(MapColor.RED));
  @ApiStatus.AvailableSince("1.2.4")
  public static final HungSignBarBlock STRIPPED_CRIMSON_HUNG_SIGN_BAR = new HungSignBarBlock(Blocks.STRIPPED_CRIMSON_HYPHAE);
  @ApiStatus.AvailableSince("1.2.4")
  public static final HungSignBarBlock STRIPPED_WARPED_HUNG_SIGN_BAR = new HungSignBarBlock(Blocks.STRIPPED_WARPED_HYPHAE);

  @ApiStatus.AvailableSince("1.0.4-mc1.19.3")
  @MiningLevel(MiningLevel.Tool.AXE)
  public static final HungSignBarBlock BAMBOO_HUNG_SIGN_BAR = new HungSignBarBlock(Blocks.BAMBOO_BLOCK, FabricBlockSettings.copyOf(Blocks.BAMBOO_BLOCK).mapColor(((AbstractBlockSettingsAccessor) ((AbstractBlockAccessor) Blocks.BAMBOO_BLOCK).getSettings()).getMapColorProvider().apply(Blocks.BAMBOO_BLOCK.getDefaultState().with(Properties.AXIS, Direction.Axis.X))));

  static {
    OAK_WOOD_HUNG_SIGN.baseTexture = OAK_WOOD_HUNG_SIGN.barTexture = OAK_HUNG_SIGN.barTexture = new Identifier("block/oak_log");
    SPRUCE_WOOD_HUNG_SIGN.baseTexture = SPRUCE_WOOD_HUNG_SIGN.barTexture = SPRUCE_HUNG_SIGN.barTexture = new Identifier("block/spruce_log");
    BIRCH_WOOD_HUNG_SIGN.baseTexture = BIRCH_WOOD_HUNG_SIGN.barTexture = BIRCH_HUNG_SIGN.barTexture = new Identifier("block/birch_log");
    JUNGLE_WOOD_HUNG_SIGN.baseTexture = JUNGLE_WOOD_HUNG_SIGN.barTexture = JUNGLE_HUNG_SIGN.barTexture = new Identifier("block/jungle_log");
    ACACIA_WOOD_HUNG_SIGN.baseTexture = ACACIA_WOOD_HUNG_SIGN.barTexture = ACACIA_HUNG_SIGN.barTexture = new Identifier("block/acacia_log");
    CHERRY_WOOD_HUNG_SIGN.baseTexture = CHERRY_WOOD_HUNG_SIGN.barTexture = CHERRY_HUNG_SIGN.barTexture = new Identifier("block/cherry_log");
    DARK_OAK_WOOD_HUNG_SIGN.baseTexture = DARK_OAK_WOOD_HUNG_SIGN.barTexture = DARK_OAK_HUNG_SIGN.barTexture = new Identifier("block/dark_oak_log");
    MANGROVE_WOOD_HUNG_SIGN.baseTexture = MANGROVE_WOOD_HUNG_SIGN.barTexture = MANGROVE_HUNG_SIGN.barTexture = new Identifier("block/mangrove_log");
    CRIMSON_HYPHAE_HUNG_SIGN.baseTexture = CRIMSON_HYPHAE_HUNG_SIGN.barTexture = CRIMSON_HUNG_SIGN.barTexture = new Identifier("block/crimson_stem");
    WARPED_HYPHAE_HUNG_SIGN.baseTexture = WARPED_HYPHAE_HUNG_SIGN.barTexture = WARPED_HUNG_SIGN.barTexture = new Identifier("block/warped_stem");
    STRIPPED_OAK_WOOD_HUNG_SIGN.baseTexture = new Identifier("block/stripped_oak_log");
    STRIPPED_SPRUCE_WOOD_HUNG_SIGN.baseTexture = new Identifier("block/stripped_spruce_log");
    STRIPPED_BIRCH_WOOD_HUNG_SIGN.baseTexture = new Identifier("block/stripped_birch_log");
    STRIPPED_JUNGLE_WOOD_HUNG_SIGN.baseTexture = new Identifier("block/stripped_jungle_log");
    STRIPPED_ACACIA_WOOD_HUNG_SIGN.baseTexture = new Identifier("block/stripped_acacia_log");
    STRIPPED_CHERRY_WOOD_HUNG_SIGN.baseTexture = new Identifier("block/stripped_cherry_log");
    STRIPPED_DARK_OAK_WOOD_HUNG_SIGN.baseTexture = new Identifier("block/stripped_dark_oak_log");
    STRIPPED_MANGROVE_WOOD_HUNG_SIGN.baseTexture = new Identifier("block/stripped_mangrove_log");
    STRIPPED_CRIMSON_HYPHAE_HUNG_SIGN.baseTexture = new Identifier("block/stripped_crimson_stem");
    STRIPPED_WARPED_HYPHAE_HUNG_SIGN.baseTexture = new Identifier("block/stripped_warped_stem");
    OAK_HUNG_SIGN.textureTop = new Identifier("block/oak_log");
    SPRUCE_HUNG_SIGN.textureTop = new Identifier("block/spruce_log");
    BIRCH_HUNG_SIGN.textureTop = new Identifier("block/birch_log");
    JUNGLE_HUNG_SIGN.textureTop = new Identifier("block/jungle_log");
    ACACIA_HUNG_SIGN.textureTop = new Identifier("block/acacia_log");
    CHERRY_HUNG_SIGN.textureTop = new Identifier("block/cherry_log");
    DARK_OAK_HUNG_SIGN.textureTop = new Identifier("block/dark_oak_log");
    MANGROVE_HUNG_SIGN.textureTop = new Identifier("block/mangrove_log");
    CRIMSON_HUNG_SIGN.textureTop = new Identifier("block/crimson_stem");
    WARPED_HUNG_SIGN.textureTop = new Identifier("block/warped_stem");
    OAK_HUNG_SIGN_BAR.texture = new Identifier("block/oak_log");
    SPRUCE_HUNG_SIGN_BAR.texture = new Identifier("block/spruce_log");
    BIRCH_HUNG_SIGN_BAR.texture = new Identifier("block/birch_log");
    JUNGLE_HUNG_SIGN_BAR.texture = new Identifier("block/jungle_log");
    ACACIA_HUNG_SIGN_BAR.texture = new Identifier("block/acacia_log");
    CHERRY_HUNG_SIGN_BAR.texture = new Identifier("block/cherry_log");
    DARK_OAK_HUNG_SIGN_BAR.texture = new Identifier("block/dark_oak_log");
    MANGROVE_HUNG_SIGN_BAR.texture = new Identifier("block/mangrove_log");
    CRIMSON_HUNG_SIGN_BAR.texture = new Identifier("block/crimson_stem");
    WARPED_HUNG_SIGN_BAR.texture = new Identifier("block/warped_stem");
    STRIPPED_OAK_HUNG_SIGN_BAR.texture = new Identifier("block/stripped_oak_log");
    STRIPPED_SPRUCE_HUNG_SIGN_BAR.texture = new Identifier("block/stripped_spruce_log");
    STRIPPED_BIRCH_HUNG_SIGN_BAR.texture = new Identifier("block/stripped_birch_log");
    STRIPPED_JUNGLE_HUNG_SIGN_BAR.texture = new Identifier("block/stripped_jungle_log");
    STRIPPED_ACACIA_HUNG_SIGN_BAR.texture = new Identifier("block/stripped_acacia_log");
    STRIPPED_CHERRY_HUNG_SIGN_BAR.texture = new Identifier("block/stripped_cherry_log");
    STRIPPED_DARK_OAK_HUNG_SIGN_BAR.texture = new Identifier("block/stripped_dark_oak_log");
    STRIPPED_MANGROVE_HUNG_SIGN_BAR.texture = new Identifier("block/stripped_mangrove_log");
    STRIPPED_CRIMSON_HUNG_SIGN_BAR.texture = new Identifier("block/stripped_crimson_stem");
    STRIPPED_WARPED_HUNG_SIGN_BAR.texture = new Identifier("block/stripped_warped_stem");
    BAMBOO_HUNG_SIGN.barTexture = BAMBOO_HUNG_SIGN.textureTop = BAMBOO_PLANK_HUNG_SIGN.barTexture = BAMBOO_PLANK_HUNG_SIGN.textureTop = BAMBOO_MOSAIC_HUNG_SIGN.barTexture = BAMBOO_MOSAIC_HUNG_SIGN.textureTop = BAMBOO_HUNG_SIGN_BAR.texture = new Identifier("block/bamboo_block");
  }

  // 混凝土告示牌部分

  public static final HungSignBlock WHITE_CONCRETE_HUNG_SIGN =
      new HungSignBlock(Blocks.WHITE_CONCRETE);

  public static final HungSignBlock ORANGE_CONCRETE_HUNG_SIGN =
      new HungSignBlock(Blocks.ORANGE_CONCRETE);

  public static final HungSignBlock MAGENTA_CONCRETE_HUNG_SIGN =
      new HungSignBlock(Blocks.MAGENTA_CONCRETE);

  public static final HungSignBlock LIGHT_BLUE_CONCRETE_HUNG_SIGN =
      new HungSignBlock(Blocks.LIGHT_BLUE_CONCRETE);

  public static final HungSignBlock YELLOW_CONCRETE_HUNG_SIGN =
      new HungSignBlock(Blocks.YELLOW_CONCRETE);

  public static final HungSignBlock LIME_CONCRETE_HUNG_SIGN =
      new HungSignBlock(Blocks.LIME_CONCRETE);

  public static final HungSignBlock PINK_CONCRETE_HUNG_SIGN =
      new HungSignBlock(Blocks.PINK_CONCRETE);

  public static final HungSignBlock GRAY_CONCRETE_HUNG_SIGN =
      new HungSignBlock(Blocks.GRAY_CONCRETE);

  public static final HungSignBlock LIGHT_GRAY_CONCRETE_HUNG_SIGN =
      new HungSignBlock(Blocks.LIGHT_GRAY_CONCRETE);

  public static final HungSignBlock CYAN_CONCRETE_HUNG_SIGN =
      new HungSignBlock(Blocks.CYAN_CONCRETE);

  public static final HungSignBlock PURPLE_CONCRETE_HUNG_SIGN =
      new HungSignBlock(Blocks.PURPLE_CONCRETE);

  public static final HungSignBlock BLUE_CONCRETE_HUNG_SIGN =
      new HungSignBlock(Blocks.BLUE_CONCRETE);

  public static final HungSignBlock BROWN_CONCRETE_HUNG_SIGN =
      new HungSignBlock(Blocks.BROWN_CONCRETE);

  public static final HungSignBlock GREEN_CONCRETE_HUNG_SIGN =
      new HungSignBlock(Blocks.GREEN_CONCRETE);

  public static final HungSignBlock RED_CONCRETE_HUNG_SIGN =
      new HungSignBlock(Blocks.RED_CONCRETE);

  public static final HungSignBlock BLACK_CONCRETE_HUNG_SIGN =
      new HungSignBlock(Blocks.BLACK_CONCRETE);

  /**
   * 自定义颜色的混凝土悬挂告示牌。
   */
  public static final ColoredHungSignBlock COLORED_CONCRETE_HUNG_SIGN = new ColoredHungSignBlock(ColoredBlocks.COLORED_CONCRETE);

  /**
   * 由所有混凝土告示牌组成的映射。
   */
  public static final ImmutableMap<DyeColor, HungSignBlock> CONCRETE_HUNG_SIGNS = new ImmutableMap.Builder<DyeColor, HungSignBlock>()
      .put(DyeColor.WHITE, WHITE_CONCRETE_HUNG_SIGN)
      .put(DyeColor.ORANGE, ORANGE_CONCRETE_HUNG_SIGN)
      .put(DyeColor.MAGENTA, MAGENTA_CONCRETE_HUNG_SIGN)
      .put(DyeColor.LIGHT_BLUE, LIGHT_BLUE_CONCRETE_HUNG_SIGN)
      .put(DyeColor.YELLOW, YELLOW_CONCRETE_HUNG_SIGN)
      .put(DyeColor.LIME, LIME_CONCRETE_HUNG_SIGN)
      .put(DyeColor.PINK, PINK_CONCRETE_HUNG_SIGN)
      .put(DyeColor.GRAY, GRAY_CONCRETE_HUNG_SIGN)
      .put(DyeColor.LIGHT_GRAY, LIGHT_GRAY_CONCRETE_HUNG_SIGN)
      .put(DyeColor.CYAN, CYAN_CONCRETE_HUNG_SIGN)
      .put(DyeColor.PURPLE, PURPLE_CONCRETE_HUNG_SIGN)
      .put(DyeColor.BLUE, BLUE_CONCRETE_HUNG_SIGN)
      .put(DyeColor.BROWN, BROWN_CONCRETE_HUNG_SIGN)
      .put(DyeColor.GREEN, GREEN_CONCRETE_HUNG_SIGN)
      .put(DyeColor.RED, RED_CONCRETE_HUNG_SIGN)
      .put(DyeColor.BLACK, BLACK_CONCRETE_HUNG_SIGN)
      .build();

  // 混凝土告示牌杆

  public static final HungSignBarBlock WHITE_CONCRETE_HUNG_SIGN_BAR =
      new HungSignBarBlock(Blocks.WHITE_CONCRETE);

  public static final HungSignBarBlock ORANGE_CONCRETE_HUNG_SIGN_BAR =
      new HungSignBarBlock(Blocks.ORANGE_CONCRETE);

  public static final HungSignBarBlock MAGENTA_CONCRETE_HUNG_SIGN_BAR =
      new HungSignBarBlock(Blocks.MAGENTA_CONCRETE);

  public static final HungSignBarBlock LIGHT_BLUE_CONCRETE_HUNG_SIGN_BAR =
      new HungSignBarBlock(Blocks.LIGHT_BLUE_CONCRETE);

  public static final HungSignBarBlock YELLOW_CONCRETE_HUNG_SIGN_BAR =
      new HungSignBarBlock(Blocks.YELLOW_CONCRETE);

  public static final HungSignBarBlock LIME_CONCRETE_HUNG_SIGN_BAR =
      new HungSignBarBlock(Blocks.LIME_CONCRETE);

  public static final HungSignBarBlock PINK_CONCRETE_HUNG_SIGN_BAR =
      new HungSignBarBlock(Blocks.PINK_CONCRETE);

  public static final HungSignBarBlock GRAY_CONCRETE_HUNG_SIGN_BAR =
      new HungSignBarBlock(Blocks.GRAY_CONCRETE);

  public static final HungSignBarBlock LIGHT_GRAY_CONCRETE_HUNG_SIGN_BAR =
      new HungSignBarBlock(Blocks.LIGHT_GRAY_CONCRETE);

  public static final HungSignBarBlock CYAN_CONCRETE_HUNG_SIGN_BAR =
      new HungSignBarBlock(Blocks.CYAN_CONCRETE);

  public static final HungSignBarBlock PURPLE_CONCRETE_HUNG_SIGN_BAR =
      new HungSignBarBlock(Blocks.PURPLE_CONCRETE);

  public static final HungSignBarBlock BLUE_CONCRETE_HUNG_SIGN_BAR =
      new HungSignBarBlock(Blocks.BLUE_CONCRETE);

  public static final HungSignBarBlock BROWN_CONCRETE_HUNG_SIGN_BAR =
      new HungSignBarBlock(Blocks.BROWN_CONCRETE);

  public static final HungSignBarBlock GREEN_CONCRETE_HUNG_SIGN_BAR =
      new HungSignBarBlock(Blocks.GREEN_CONCRETE);

  public static final HungSignBarBlock RED_CONCRETE_HUNG_SIGN_BAR =
      new HungSignBarBlock(Blocks.RED_CONCRETE);

  public static final HungSignBarBlock BLACK_CONCRETE_HUNG_SIGN_BAR =
      new HungSignBarBlock(Blocks.BLACK_CONCRETE);

  /**
   * 自定义颜色的混凝土悬挂告示牌杆。
   */
  public static final HungSignBarBlock COLORED_CONCRETE_HUNG_SIGN_BAR = new ColoredHungSignBarBlock(ColoredBlocks.COLORED_CONCRETE);

  /**
   * 由所有混凝土告示牌杆组成的映射。
   */
  @ApiStatus.AvailableSince("0.1.7")
  public static final ImmutableMap<DyeColor, HungSignBarBlock> CONCRETE_HUNG_SIGN_BARS = new ImmutableMap.Builder<DyeColor, HungSignBarBlock>()
      .put(DyeColor.WHITE, WHITE_CONCRETE_HUNG_SIGN_BAR)
      .put(DyeColor.ORANGE, ORANGE_CONCRETE_HUNG_SIGN_BAR)
      .put(DyeColor.MAGENTA, MAGENTA_CONCRETE_HUNG_SIGN_BAR)
      .put(DyeColor.LIGHT_BLUE, LIGHT_BLUE_CONCRETE_HUNG_SIGN_BAR)
      .put(DyeColor.YELLOW, YELLOW_CONCRETE_HUNG_SIGN_BAR)
      .put(DyeColor.LIME, LIME_CONCRETE_HUNG_SIGN_BAR)
      .put(DyeColor.PINK, PINK_CONCRETE_HUNG_SIGN_BAR)
      .put(DyeColor.GRAY, GRAY_CONCRETE_HUNG_SIGN_BAR)
      .put(DyeColor.LIGHT_GRAY, LIGHT_GRAY_CONCRETE_HUNG_SIGN_BAR)
      .put(DyeColor.CYAN, CYAN_CONCRETE_HUNG_SIGN_BAR)
      .put(DyeColor.PURPLE, PURPLE_CONCRETE_HUNG_SIGN_BAR)
      .put(DyeColor.BLUE, BLUE_CONCRETE_HUNG_SIGN_BAR)
      .put(DyeColor.BROWN, BROWN_CONCRETE_HUNG_SIGN_BAR)
      .put(DyeColor.GREEN, GREEN_CONCRETE_HUNG_SIGN_BAR)
      .put(DyeColor.RED, RED_CONCRETE_HUNG_SIGN_BAR)
      .put(DyeColor.BLACK, BLACK_CONCRETE_HUNG_SIGN_BAR)
      .build();

  // 陶瓦告示牌部分

  public static final HungSignBlock WHITE_TERRACOTTA_HUNG_SIGN =
      new HungSignBlock(Blocks.WHITE_TERRACOTTA);

  public static final HungSignBlock ORANGE_TERRACOTTA_HUNG_SIGN =
      new HungSignBlock(Blocks.ORANGE_TERRACOTTA);

  public static final HungSignBlock MAGENTA_TERRACOTTA_HUNG_SIGN =
      new HungSignBlock(Blocks.MAGENTA_TERRACOTTA);

  public static final HungSignBlock LIGHT_BLUE_TERRACOTTA_HUNG_SIGN =
      new HungSignBlock(Blocks.LIGHT_BLUE_TERRACOTTA);

  public static final HungSignBlock YELLOW_TERRACOTTA_HUNG_SIGN =
      new HungSignBlock(Blocks.YELLOW_TERRACOTTA);

  public static final HungSignBlock LIME_TERRACOTTA_HUNG_SIGN =
      new HungSignBlock(Blocks.LIME_TERRACOTTA);

  public static final HungSignBlock PINK_TERRACOTTA_HUNG_SIGN =
      new HungSignBlock(Blocks.PINK_TERRACOTTA);

  public static final HungSignBlock GRAY_TERRACOTTA_HUNG_SIGN =
      new HungSignBlock(Blocks.GRAY_TERRACOTTA);

  public static final HungSignBlock LIGHT_GRAY_TERRACOTTA_HUNG_SIGN =
      new HungSignBlock(Blocks.LIGHT_GRAY_TERRACOTTA);

  public static final HungSignBlock CYAN_TERRACOTTA_HUNG_SIGN =
      new HungSignBlock(Blocks.CYAN_TERRACOTTA);

  public static final HungSignBlock PURPLE_TERRACOTTA_HUNG_SIGN =
      new HungSignBlock(Blocks.PURPLE_TERRACOTTA);

  public static final HungSignBlock BLUE_TERRACOTTA_HUNG_SIGN =
      new HungSignBlock(Blocks.BLUE_TERRACOTTA);

  public static final HungSignBlock BROWN_TERRACOTTA_HUNG_SIGN =
      new HungSignBlock(Blocks.BROWN_TERRACOTTA);

  public static final HungSignBlock GREEN_TERRACOTTA_HUNG_SIGN =
      new HungSignBlock(Blocks.GREEN_TERRACOTTA);

  public static final HungSignBlock RED_TERRACOTTA_HUNG_SIGN =
      new HungSignBlock(Blocks.RED_TERRACOTTA);

  public static final HungSignBlock BLACK_TERRACOTTA_HUNG_SIGN =
      new HungSignBlock(Blocks.BLACK_TERRACOTTA);

  @Beta
  public static final ColoredHungSignBlock COLORED_TERRACOTTA_HUNG_SIGN = new ColoredHungSignBlock(ColoredBlocks.COLORED_TERRACOTTA);

  /**
   * 由所有陶瓦告示牌组成的映射。
   */
  public static final ImmutableMap<DyeColor, HungSignBlock> TERRACOTTA_HUNG_SIGNS = new ImmutableMap.Builder<DyeColor, HungSignBlock>()
      .put(DyeColor.WHITE, WHITE_TERRACOTTA_HUNG_SIGN)
      .put(DyeColor.ORANGE, ORANGE_TERRACOTTA_HUNG_SIGN)
      .put(DyeColor.MAGENTA, MAGENTA_TERRACOTTA_HUNG_SIGN)
      .put(DyeColor.LIGHT_BLUE, LIGHT_BLUE_TERRACOTTA_HUNG_SIGN)
      .put(DyeColor.YELLOW, YELLOW_TERRACOTTA_HUNG_SIGN)
      .put(DyeColor.LIME, LIME_TERRACOTTA_HUNG_SIGN)
      .put(DyeColor.PINK, PINK_TERRACOTTA_HUNG_SIGN)
      .put(DyeColor.GRAY, GRAY_TERRACOTTA_HUNG_SIGN)
      .put(DyeColor.LIGHT_GRAY, LIGHT_GRAY_TERRACOTTA_HUNG_SIGN)
      .put(DyeColor.CYAN, CYAN_TERRACOTTA_HUNG_SIGN)
      .put(DyeColor.PURPLE, PURPLE_TERRACOTTA_HUNG_SIGN)
      .put(DyeColor.BLUE, BLUE_TERRACOTTA_HUNG_SIGN)
      .put(DyeColor.BROWN, BROWN_TERRACOTTA_HUNG_SIGN)
      .put(DyeColor.GREEN, GREEN_TERRACOTTA_HUNG_SIGN)
      .put(DyeColor.RED, RED_TERRACOTTA_HUNG_SIGN)
      .put(DyeColor.BLACK, BLACK_TERRACOTTA_HUNG_SIGN)
      .build();

  // 陶瓦告示牌杆

  public static final HungSignBarBlock WHITE_TERRACOTTA_HUNG_SIGN_BAR =
      new HungSignBarBlock(Blocks.WHITE_TERRACOTTA);

  public static final HungSignBarBlock ORANGE_TERRACOTTA_HUNG_SIGN_BAR =
      new HungSignBarBlock(Blocks.ORANGE_TERRACOTTA);

  public static final HungSignBarBlock MAGENTA_TERRACOTTA_HUNG_SIGN_BAR =
      new HungSignBarBlock(Blocks.MAGENTA_TERRACOTTA);

  public static final HungSignBarBlock LIGHT_BLUE_TERRACOTTA_HUNG_SIGN_BAR =
      new HungSignBarBlock(Blocks.LIGHT_BLUE_TERRACOTTA);

  public static final HungSignBarBlock YELLOW_TERRACOTTA_HUNG_SIGN_BAR =
      new HungSignBarBlock(Blocks.YELLOW_TERRACOTTA);

  public static final HungSignBarBlock LIME_TERRACOTTA_HUNG_SIGN_BAR =
      new HungSignBarBlock(Blocks.LIME_TERRACOTTA);

  public static final HungSignBarBlock PINK_TERRACOTTA_HUNG_SIGN_BAR =
      new HungSignBarBlock(Blocks.PINK_TERRACOTTA);

  public static final HungSignBarBlock GRAY_TERRACOTTA_HUNG_SIGN_BAR =
      new HungSignBarBlock(Blocks.GRAY_TERRACOTTA);

  public static final HungSignBarBlock LIGHT_GRAY_TERRACOTTA_HUNG_SIGN_BAR =
      new HungSignBarBlock(Blocks.LIGHT_GRAY_TERRACOTTA);

  public static final HungSignBarBlock CYAN_TERRACOTTA_HUNG_SIGN_BAR =
      new HungSignBarBlock(Blocks.CYAN_TERRACOTTA);

  public static final HungSignBarBlock PURPLE_TERRACOTTA_HUNG_SIGN_BAR =
      new HungSignBarBlock(Blocks.PURPLE_TERRACOTTA);

  public static final HungSignBarBlock BLUE_TERRACOTTA_HUNG_SIGN_BAR =
      new HungSignBarBlock(Blocks.BLUE_TERRACOTTA);

  public static final HungSignBarBlock BROWN_TERRACOTTA_HUNG_SIGN_BAR =
      new HungSignBarBlock(Blocks.BROWN_TERRACOTTA);

  public static final HungSignBarBlock GREEN_TERRACOTTA_HUNG_SIGN_BAR =
      new HungSignBarBlock(Blocks.GREEN_TERRACOTTA);

  public static final HungSignBarBlock RED_TERRACOTTA_HUNG_SIGN_BAR =
      new HungSignBarBlock(Blocks.RED_TERRACOTTA);

  public static final HungSignBarBlock BLACK_TERRACOTTA_HUNG_SIGN_BAR =
      new HungSignBarBlock(Blocks.BLACK_TERRACOTTA);

  @Beta
  public static final ColoredHungSignBarBlock COLORED_TERRACOTTA_HUNG_SIGN_BAR = new ColoredHungSignBarBlock(ColoredBlocks.COLORED_TERRACOTTA);

  /**
   * 由所有陶瓦告示牌杆组成的映射。
   */
  @ApiStatus.AvailableSince("0.1.7")
  public static final ImmutableMap<DyeColor, HungSignBarBlock> TERRACOTTA_HUNG_SIGN_BARS = new ImmutableMap.Builder<DyeColor, HungSignBarBlock>()
      .put(DyeColor.WHITE, WHITE_TERRACOTTA_HUNG_SIGN_BAR)
      .put(DyeColor.ORANGE, ORANGE_TERRACOTTA_HUNG_SIGN_BAR)
      .put(DyeColor.MAGENTA, MAGENTA_TERRACOTTA_HUNG_SIGN_BAR)
      .put(DyeColor.LIGHT_BLUE, LIGHT_BLUE_TERRACOTTA_HUNG_SIGN_BAR)
      .put(DyeColor.YELLOW, YELLOW_TERRACOTTA_HUNG_SIGN_BAR)
      .put(DyeColor.LIME, LIME_TERRACOTTA_HUNG_SIGN_BAR)
      .put(DyeColor.PINK, PINK_TERRACOTTA_HUNG_SIGN_BAR)
      .put(DyeColor.GRAY, GRAY_TERRACOTTA_HUNG_SIGN_BAR)
      .put(DyeColor.LIGHT_GRAY, LIGHT_GRAY_TERRACOTTA_HUNG_SIGN_BAR)
      .put(DyeColor.CYAN, CYAN_TERRACOTTA_HUNG_SIGN_BAR)
      .put(DyeColor.PURPLE, PURPLE_TERRACOTTA_HUNG_SIGN_BAR)
      .put(DyeColor.BLUE, BLUE_TERRACOTTA_HUNG_SIGN_BAR)
      .put(DyeColor.BROWN, BROWN_TERRACOTTA_HUNG_SIGN_BAR)
      .put(DyeColor.GREEN, GREEN_TERRACOTTA_HUNG_SIGN_BAR)
      .put(DyeColor.RED, RED_TERRACOTTA_HUNG_SIGN_BAR)
      .put(DyeColor.BLACK, BLACK_TERRACOTTA_HUNG_SIGN_BAR)
      .build();

  // 发光的混凝土告示牌

  public static final GlowingHungSignBlock GLOWING_WHITE_CONCRETE_HUNG_SIGN =
      new GlowingHungSignBlock(Blocks.WHITE_CONCRETE);

  public static final GlowingHungSignBlock GLOWING_ORANGE_CONCRETE_HUNG_SIGN =
      new GlowingHungSignBlock(Blocks.ORANGE_CONCRETE);

  public static final GlowingHungSignBlock GLOWING_MAGENTA_CONCRETE_HUNG_SIGN =
      new GlowingHungSignBlock(Blocks.MAGENTA_CONCRETE);

  public static final GlowingHungSignBlock GLOWING_LIGHT_BLUE_CONCRETE_HUNG_SIGN =
      new GlowingHungSignBlock(Blocks.LIGHT_BLUE_CONCRETE);

  public static final GlowingHungSignBlock GLOWING_YELLOW_CONCRETE_HUNG_SIGN =
      new GlowingHungSignBlock(Blocks.YELLOW_CONCRETE);

  public static final GlowingHungSignBlock GLOWING_LIME_CONCRETE_HUNG_SIGN =
      new GlowingHungSignBlock(Blocks.LIME_CONCRETE);

  public static final GlowingHungSignBlock GLOWING_PINK_CONCRETE_HUNG_SIGN =
      new GlowingHungSignBlock(Blocks.PINK_CONCRETE);

  public static final GlowingHungSignBlock GLOWING_GRAY_CONCRETE_HUNG_SIGN =
      new GlowingHungSignBlock(Blocks.GRAY_CONCRETE);

  public static final GlowingHungSignBlock GLOWING_LIGHT_GRAY_CONCRETE_HUNG_SIGN =
      new GlowingHungSignBlock(Blocks.LIGHT_GRAY_CONCRETE);

  public static final GlowingHungSignBlock GLOWING_CYAN_CONCRETE_HUNG_SIGN =
      new GlowingHungSignBlock(Blocks.CYAN_CONCRETE);

  public static final GlowingHungSignBlock GLOWING_PURPLE_CONCRETE_HUNG_SIGN =
      new GlowingHungSignBlock(Blocks.PURPLE_CONCRETE);

  public static final GlowingHungSignBlock GLOWING_BLUE_CONCRETE_HUNG_SIGN =
      new GlowingHungSignBlock(Blocks.BLUE_CONCRETE);

  public static final GlowingHungSignBlock GLOWING_BROWN_CONCRETE_HUNG_SIGN =
      new GlowingHungSignBlock(Blocks.BROWN_CONCRETE);

  public static final GlowingHungSignBlock GLOWING_GREEN_CONCRETE_HUNG_SIGN =
      new GlowingHungSignBlock(Blocks.GREEN_CONCRETE);

  public static final GlowingHungSignBlock GLOWING_RED_CONCRETE_HUNG_SIGN =
      new GlowingHungSignBlock(Blocks.RED_CONCRETE);

  public static final GlowingHungSignBlock GLOWING_BLACK_CONCRETE_HUNG_SIGN =
      new GlowingHungSignBlock(Blocks.BLACK_CONCRETE);

  @ApiStatus.AvailableSince("1.0.2")
  public static final ColoredGlowingHungSignBlock COLORED_GLOWING_CONCRETE_HUNG_SIGN = new ColoredGlowingHungSignBlock(ColoredBlocks.COLORED_CONCRETE);

  /**
   * 由发光的混凝土告示牌组成的映射。
   */
  public static final ImmutableMap<DyeColor, GlowingHungSignBlock> GLOWING_CONCRETE_HUNG_SIGNS = new ImmutableMap.Builder<DyeColor, GlowingHungSignBlock>()
      .put(DyeColor.WHITE, GLOWING_WHITE_CONCRETE_HUNG_SIGN)
      .put(DyeColor.ORANGE, GLOWING_ORANGE_CONCRETE_HUNG_SIGN)
      .put(DyeColor.MAGENTA, GLOWING_MAGENTA_CONCRETE_HUNG_SIGN)
      .put(DyeColor.LIGHT_BLUE, GLOWING_LIGHT_BLUE_CONCRETE_HUNG_SIGN)
      .put(DyeColor.YELLOW, GLOWING_YELLOW_CONCRETE_HUNG_SIGN)
      .put(DyeColor.LIME, GLOWING_LIME_CONCRETE_HUNG_SIGN)
      .put(DyeColor.PINK, GLOWING_PINK_CONCRETE_HUNG_SIGN)
      .put(DyeColor.GRAY, GLOWING_GRAY_CONCRETE_HUNG_SIGN)
      .put(DyeColor.LIGHT_GRAY, GLOWING_LIGHT_GRAY_CONCRETE_HUNG_SIGN)
      .put(DyeColor.CYAN, GLOWING_CYAN_CONCRETE_HUNG_SIGN)
      .put(DyeColor.PURPLE, GLOWING_PURPLE_CONCRETE_HUNG_SIGN)
      .put(DyeColor.BLUE, GLOWING_BLUE_CONCRETE_HUNG_SIGN)
      .put(DyeColor.BROWN, GLOWING_BROWN_CONCRETE_HUNG_SIGN)
      .put(DyeColor.GREEN, GLOWING_GREEN_CONCRETE_HUNG_SIGN)
      .put(DyeColor.RED, GLOWING_RED_CONCRETE_HUNG_SIGN)
      .put(DyeColor.BLACK, GLOWING_BLACK_CONCRETE_HUNG_SIGN)
      .build();

  // 发光的陶瓦告示牌

  public static final GlowingHungSignBlock GLOWING_WHITE_TERRACOTTA_HUNG_SIGN =
      new GlowingHungSignBlock(Blocks.WHITE_TERRACOTTA);

  public static final GlowingHungSignBlock GLOWING_ORANGE_TERRACOTTA_HUNG_SIGN =
      new GlowingHungSignBlock(Blocks.ORANGE_TERRACOTTA);

  public static final GlowingHungSignBlock GLOWING_MAGENTA_TERRACOTTA_HUNG_SIGN =
      new GlowingHungSignBlock(Blocks.MAGENTA_TERRACOTTA);

  public static final GlowingHungSignBlock GLOWING_LIGHT_BLUE_TERRACOTTA_HUNG_SIGN =
      new GlowingHungSignBlock(Blocks.LIGHT_BLUE_TERRACOTTA);

  public static final GlowingHungSignBlock GLOWING_YELLOW_TERRACOTTA_HUNG_SIGN =
      new GlowingHungSignBlock(Blocks.YELLOW_TERRACOTTA);

  public static final GlowingHungSignBlock GLOWING_LIME_TERRACOTTA_HUNG_SIGN =
      new GlowingHungSignBlock(Blocks.LIME_TERRACOTTA);

  public static final GlowingHungSignBlock GLOWING_PINK_TERRACOTTA_HUNG_SIGN =
      new GlowingHungSignBlock(Blocks.PINK_TERRACOTTA);

  public static final GlowingHungSignBlock GLOWING_GRAY_TERRACOTTA_HUNG_SIGN =
      new GlowingHungSignBlock(Blocks.GRAY_TERRACOTTA);

  public static final GlowingHungSignBlock GLOWING_LIGHT_GRAY_TERRACOTTA_HUNG_SIGN =
      new GlowingHungSignBlock(Blocks.LIGHT_GRAY_TERRACOTTA);

  public static final GlowingHungSignBlock GLOWING_CYAN_TERRACOTTA_HUNG_SIGN =
      new GlowingHungSignBlock(Blocks.CYAN_TERRACOTTA);

  public static final GlowingHungSignBlock GLOWING_PURPLE_TERRACOTTA_HUNG_SIGN =
      new GlowingHungSignBlock(Blocks.PURPLE_TERRACOTTA);

  public static final GlowingHungSignBlock GLOWING_BLUE_TERRACOTTA_HUNG_SIGN =
      new GlowingHungSignBlock(Blocks.BLUE_TERRACOTTA);

  public static final GlowingHungSignBlock GLOWING_BROWN_TERRACOTTA_HUNG_SIGN =
      new GlowingHungSignBlock(Blocks.BROWN_TERRACOTTA);

  public static final GlowingHungSignBlock GLOWING_GREEN_TERRACOTTA_HUNG_SIGN =
      new GlowingHungSignBlock(Blocks.GREEN_TERRACOTTA);

  public static final GlowingHungSignBlock GLOWING_RED_TERRACOTTA_HUNG_SIGN =
      new GlowingHungSignBlock(Blocks.RED_TERRACOTTA);

  public static final GlowingHungSignBlock GLOWING_BLACK_TERRACOTTA_HUNG_SIGN =
      new GlowingHungSignBlock(Blocks.BLACK_TERRACOTTA);

  @ApiStatus.AvailableSince("1.0.2")
  public static final ColoredGlowingHungSignBlock COLORED_GLOWING_TERRACOTTA_HUNG_SIGN = new ColoredGlowingHungSignBlock(ColoredBlocks.COLORED_TERRACOTTA);

  /**
   * 由发光的陶瓦告示牌组成的映射。
   */
  public static final Map<DyeColor, GlowingHungSignBlock> GLOWING_TERRACOTTA_HUNG_SIGNS = new ImmutableMap.Builder<DyeColor, GlowingHungSignBlock>()
      .put(DyeColor.WHITE, GLOWING_WHITE_TERRACOTTA_HUNG_SIGN)
      .put(DyeColor.ORANGE, GLOWING_ORANGE_TERRACOTTA_HUNG_SIGN)
      .put(DyeColor.MAGENTA, GLOWING_MAGENTA_TERRACOTTA_HUNG_SIGN)
      .put(DyeColor.LIGHT_BLUE, GLOWING_LIGHT_BLUE_TERRACOTTA_HUNG_SIGN)
      .put(DyeColor.YELLOW, GLOWING_YELLOW_TERRACOTTA_HUNG_SIGN)
      .put(DyeColor.LIME, GLOWING_LIME_TERRACOTTA_HUNG_SIGN)
      .put(DyeColor.PINK, GLOWING_PINK_TERRACOTTA_HUNG_SIGN)
      .put(DyeColor.GRAY, GLOWING_GRAY_TERRACOTTA_HUNG_SIGN)
      .put(DyeColor.LIGHT_GRAY, GLOWING_LIGHT_GRAY_TERRACOTTA_HUNG_SIGN)
      .put(DyeColor.CYAN, GLOWING_CYAN_TERRACOTTA_HUNG_SIGN)
      .put(DyeColor.PURPLE, GLOWING_PURPLE_TERRACOTTA_HUNG_SIGN)
      .put(DyeColor.BLUE, GLOWING_BLUE_TERRACOTTA_HUNG_SIGN)
      .put(DyeColor.BROWN, GLOWING_BROWN_TERRACOTTA_HUNG_SIGN)
      .put(DyeColor.GREEN, GLOWING_GREEN_TERRACOTTA_HUNG_SIGN)
      .put(DyeColor.RED, GLOWING_RED_TERRACOTTA_HUNG_SIGN)
      .put(DyeColor.BLACK, GLOWING_BLACK_TERRACOTTA_HUNG_SIGN)
      .build();

  // 以下是比较杂项的一些发光悬挂告示牌方块。

  // 石头

  @ApiStatus.AvailableSince("0.1.7")
  public static final HungSignBlock STONE_HUNG_SIGN = new HungSignBlock(Blocks.STONE);

  @ApiStatus.AvailableSince("0.1.7")
  public static final GlowingHungSignBlock GLOWING_STONE_HUNG_SIGN = new GlowingHungSignBlock(Blocks.STONE);

  @ApiStatus.AvailableSince("0.1.7")
  public static final HungSignBarBlock STONE_HUNG_SIGN_BAR = new HungSignBarBlock(Blocks.STONE);

  @ApiStatus.AvailableSince("1.0.2")
  public static final ColoredHungSignBlock COLORED_STONE_HUNG_SIGN = new ColoredHungSignBlock(ColoredBlocks.COLORED_STONE);

  @ApiStatus.AvailableSince("1.0.2")
  public static final ColoredGlowingHungSignBlock COLORED_GLOWING_STONE_HUNG_SIGN = new ColoredGlowingHungSignBlock(ColoredBlocks.COLORED_STONE);

  @ApiStatus.AvailableSince("1.0.2")
  public static final ColoredHungSignBarBlock COLORED_STONE_HUNG_SIGN_BAR = new ColoredHungSignBarBlock(ColoredBlocks.COLORED_STONE);

  // 圆石

  @ApiStatus.AvailableSince("0.2.4")
  public static final HungSignBlock COBBLESTONE_HUNG_SIGN = new HungSignBlock(Blocks.COBBLESTONE);

  @ApiStatus.AvailableSince("0.2.4")
  public static final GlowingHungSignBlock GLOWING_COBBLESTONE_HUNG_SIGN = new GlowingHungSignBlock(Blocks.COBBLESTONE);

  @ApiStatus.AvailableSince("0.2.4")
  public static final HungSignBarBlock COBBLESTONE_HUNG_SIGN_BAR = new HungSignBarBlock(Blocks.COBBLESTONE);

  @ApiStatus.AvailableSince("1.0.2")
  public static final ColoredHungSignBlock COLORED_COBBLESTONE_HUNG_SIGN = new ColoredHungSignBlock(ColoredBlocks.COLORED_COBBLESTONE);

  @ApiStatus.AvailableSince("1.0.2")
  public static final ColoredGlowingHungSignBlock COLORED_GLOWING_COBBLESTONE_HUNG_SIGN = new ColoredGlowingHungSignBlock(ColoredBlocks.COLORED_COBBLESTONE);

  @ApiStatus.AvailableSince("1.0.2")
  public static final ColoredHungSignBarBlock COLORED_COBBLESTONE_HUNG_SIGN_BAR = new ColoredHungSignBarBlock(ColoredBlocks.COLORED_COBBLESTONE);

  // 石砖

  @ApiStatus.AvailableSince("0.1.7")
  public static final HungSignBlock STONE_BRICK_HUNG_SIGN = new HungSignBlock(Blocks.STONE_BRICKS);

  @ApiStatus.AvailableSince("0.1.7")
  public static final GlowingHungSignBlock GLOWING_STONE_BRICK_HUNG_SIGN = new GlowingHungSignBlock(Blocks.STONE_BRICKS);

  @ApiStatus.AvailableSince("0.1.7")
  public static final HungSignBarBlock STONE_BRICK_HUNG_SIGN_BAR = new HungSignBarBlock(Blocks.STONE_BRICKS);

  @ApiStatus.AvailableSince("1.0.2")
  public static final ColoredHungSignBlock COLORED_STONE_BRICK_HUNG_SIGN = new ColoredHungSignBlock(ColoredBlocks.COLORED_STONE_BRICKS);

  @ApiStatus.AvailableSince("1.0.2")
  public static final ColoredGlowingHungSignBlock COLORED_GLOWING_STONE_BRICK_HUNG_SIGN = new ColoredGlowingHungSignBlock(ColoredBlocks.COLORED_STONE_BRICKS);

  @ApiStatus.AvailableSince("1.0.2")
  public static final ColoredHungSignBarBlock COLORED_STONE_BRICK_HUNG_SIGN_BAR = new ColoredHungSignBarBlock(ColoredBlocks.COLORED_STONE_BRICKS);

  // 铁块

  @ApiStatus.AvailableSince("0.1.7")
  @MiningLevel(level = MiningLevel.Level.STONE)
  public static final HungSignBlock IRON_HUNG_SIGN = new HungSignBlock(Blocks.IRON_BLOCK);

  @ApiStatus.AvailableSince("0.1.7")
  @MiningLevel(level = MiningLevel.Level.STONE)
  public static final HungSignBlock GLOWING_IRON_HUNG_SIGN = new GlowingHungSignBlock(Blocks.IRON_BLOCK);

  @ApiStatus.AvailableSince("0.1.7")
  @MiningLevel(level = MiningLevel.Level.STONE)
  public static final HungSignBarBlock IRON_HUNG_SIGN_BAR = new HungSignBarBlock(Blocks.IRON_BLOCK);

  @ApiStatus.AvailableSince("1.0.2")
  @MiningLevel(level = MiningLevel.Level.STONE)
  public static final ColoredHungSignBlock COLORED_IRON_HUNG_SIGN = new ColoredHungSignBlock(ColoredBlocks.COLORED_IRON_BLOCK);

  @ApiStatus.AvailableSince("1.0.2")
  @MiningLevel(level = MiningLevel.Level.STONE)
  public static final ColoredGlowingHungSignBlock COLORED_GLOWING_IRON_HUNG_SIGN = new ColoredGlowingHungSignBlock(ColoredBlocks.COLORED_IRON_BLOCK);

  @ApiStatus.AvailableSince("1.0.2")
  @MiningLevel(level = MiningLevel.Level.STONE)
  public static final ColoredHungSignBarBlock COLORED_IRON_HUNG_SIGN_BAR = new ColoredHungSignBarBlock(ColoredBlocks.COLORED_IRON_BLOCK);

  // 金块

  @ApiStatus.AvailableSince("0.1.7")
  @MiningLevel(level = MiningLevel.Level.IRON)
  public static final HungSignBlock GOLD_HUNG_SIGN = new HungSignBlock(Blocks.GOLD_BLOCK);

  @ApiStatus.AvailableSince("0.1.7")
  @MiningLevel(level = MiningLevel.Level.IRON)
  public static final GlowingHungSignBlock GLOWING_GOLD_HUNG_SIGN = new GlowingHungSignBlock(Blocks.GOLD_BLOCK);

  @ApiStatus.AvailableSince("0.1.7")
  @MiningLevel(level = MiningLevel.Level.IRON)
  public static final HungSignBarBlock GOLD_HUNG_SIGN_BAR = new HungSignBarBlock(Blocks.GOLD_BLOCK);

  // 钻石块

  @ApiStatus.AvailableSince("0.1.7")
  @MiningLevel(level = MiningLevel.Level.IRON)
  public static final HungSignBlock DIAMOND_HUNG_SIGN = new HungSignBlock(Blocks.DIAMOND_BLOCK);

  @ApiStatus.AvailableSince("0.1.7")
  @MiningLevel(level = MiningLevel.Level.IRON)
  public static final GlowingHungSignBlock GLOWING_DIAMOND_HUNG_SIGN = new GlowingHungSignBlock(Blocks.DIAMOND_BLOCK);

  @ApiStatus.AvailableSince("0.1.7")
  @MiningLevel(level = MiningLevel.Level.IRON)
  public static final HungSignBarBlock DIAMOND_HUNG_SIGN_BAR = new HungSignBarBlock(Blocks.DIAMOND_BLOCK);

  // 绿宝石块

  @ApiStatus.AvailableSince("0.1.7")
  @MiningLevel(level = MiningLevel.Level.IRON)
  public static final HungSignBlock EMERALD_HUNG_SIGN = new HungSignBlock(Blocks.EMERALD_BLOCK);

  @ApiStatus.AvailableSince("0.1.7")
  @MiningLevel(level = MiningLevel.Level.IRON)
  public static final GlowingHungSignBlock GLOWING_EMERALD_HUNG_SIGN = new GlowingHungSignBlock(Blocks.EMERALD_BLOCK);

  @ApiStatus.AvailableSince("0.1.7")
  @MiningLevel(level = MiningLevel.Level.IRON)
  public static final HungSignBarBlock EMERALD_HUNG_SIGN_BAR = new HungSignBarBlock(Blocks.EMERALD_BLOCK);

  // 青金石块

  @ApiStatus.AvailableSince("0.1.7")
  @MiningLevel(level = MiningLevel.Level.STONE)
  public static final HungSignBlock LAPIS_HUNG_SIGN = new HungSignBlock(Blocks.LAPIS_BLOCK);

  @ApiStatus.AvailableSince("0.1.7")
  @MiningLevel(level = MiningLevel.Level.STONE)
  public static final GlowingHungSignBlock GLOWING_LAPIS_HUNG_SIGN = new GlowingHungSignBlock(Blocks.LAPIS_BLOCK);

  @ApiStatus.AvailableSince("0.1.7")
  @MiningLevel(level = MiningLevel.Level.STONE)
  public static final HungSignBarBlock LAPIS_HUNG_SIGN_BAR = new HungSignBarBlock(Blocks.LAPIS_BLOCK);

  // 下界合金

  @ApiStatus.AvailableSince("0.2.4")
  @MiningLevel(level = MiningLevel.Level.DIAMOND)
  public static final HungSignBlock NETHERITE_HUNG_SIGN = new HungSignBlock(Blocks.NETHERITE_BLOCK);

  @ApiStatus.AvailableSince("0.2.4")
  @MiningLevel(level = MiningLevel.Level.DIAMOND)
  public static final GlowingHungSignBlock GLOWING_NETHERITE_HUNG_SIGN = new GlowingHungSignBlock(Blocks.NETHERITE_BLOCK);

  @ApiStatus.AvailableSince("0.2.4")
  @MiningLevel(level = MiningLevel.Level.DIAMOND)
  public static final HungSignBarBlock NETHERITE_HUNG_SIGN_BAR = new HungSignBarBlock(Blocks.NETHERITE_BLOCK);

  // 黑曜石

  @ApiStatus.AvailableSince("0.2.4")
  @MiningLevel(level = MiningLevel.Level.DIAMOND)
  public static final HungSignBlock OBSIDIAN_HUNG_SIGN = new HungSignBlock(Blocks.OBSIDIAN);

  @ApiStatus.AvailableSince("0.2.4")
  @MiningLevel(level = MiningLevel.Level.DIAMOND)
  public static final GlowingHungSignBlock GLOWING_OBSIDIAN_HUNG_SIGN = new GlowingHungSignBlock(Blocks.OBSIDIAN);

  @ApiStatus.AvailableSince("0.2.4")
  @MiningLevel(level = MiningLevel.Level.DIAMOND)
  public static final HungSignBarBlock OBSIDIAN_HUNG_SIGN_BAR = new HungSignBarBlock(Blocks.OBSIDIAN);

  // 哭泣的黑曜石

  @ApiStatus.AvailableSince("0.2.4")
  @MiningLevel(level = MiningLevel.Level.DIAMOND)
  public static final HungSignBlock CRYING_OBSIDIAN_HUNG_SIGN = new HungSignBlock(Blocks.CRYING_OBSIDIAN);

  @ApiStatus.AvailableSince("0.2.4")
  @MiningLevel(level = MiningLevel.Level.DIAMOND)
  public static final GlowingHungSignBlock GLOWING_CRYING_OBSIDIAN_HUNG_SIGN = new GlowingHungSignBlock(Blocks.CRYING_OBSIDIAN);

  @ApiStatus.AvailableSince("0.2.4")
  @MiningLevel(level = MiningLevel.Level.DIAMOND)
  public static final HungSignBarBlock CRYING_OBSIDIAN_HUNG_SIGN_BAR = new HungSignBarBlock(Blocks.CRYING_OBSIDIAN);

  // 下界岩

  @ApiStatus.AvailableSince("0.1.7")
  public static final HungSignBlock NETHERRACK_HUNG_SIGN = new HungSignBlock(Blocks.NETHERRACK);

  public static final GlowingHungSignBlock GLOWING_NETHERRACK_HUNG_SIGN =
      new GlowingHungSignBlock(Blocks.NETHERRACK);

  public static final HungSignBarBlock NETHERRACK_HUNG_SIGN_BAR =
      new HungSignBarBlock(Blocks.NETHERRACK);

  // 下界砖

  @ApiStatus.AvailableSince("0.1.7")
  public static final HungSignBlock NETHER_BRICK_HUNG_SIGN = new HungSignBlock(Blocks.NETHER_BRICKS);

  public static final GlowingHungSignBlock GLOWING_NETHER_BRICK_HUNG_SIGN =
      new GlowingHungSignBlock(Blocks.NETHER_BRICKS);

  public static final HungSignBarBlock NETHER_BRICK_HUNG_SIGN_BAR =
      new HungSignBarBlock(Blocks.NETHER_BRICKS);

  // 黑石

  @ApiStatus.AvailableSince("0.1.7")
  public static final HungSignBlock BLACKSTONE_HUNG_SIGN = new HungSignBlock(Blocks.BLACKSTONE);

  public static final GlowingHungSignBlock GLOWING_BLACKSTONE_HUNG_SIGN =
      new GlowingHungSignBlock(Blocks.BLACKSTONE);

  public static final HungSignBarBlock BLACKSTONE_HUNG_SIGN_BAR =
      new HungSignBarBlock(Blocks.BLACKSTONE);

  // 磨制黑石

  @ApiStatus.AvailableSince("0.1.7")
  public static final HungSignBlock POLISHED_BLACKSTONE_HUNG_SIGN = new HungSignBlock(Blocks.POLISHED_BLACKSTONE);

  public static final GlowingHungSignBlock GLOWING_POLISHED_BLACKSTONE_HUNG_SIGN =
      new GlowingHungSignBlock(Blocks.POLISHED_BLACKSTONE);

  public static final HungSignBarBlock POLISHED_BLACKSTONE_HUNG_SIGN_BAR =
      new HungSignBarBlock(Blocks.POLISHED_BLACKSTONE);

  static {
    GLOWING_NETHERRACK_HUNG_SIGN.glowTexture = new Identifier("block/lava_still");
    GLOWING_NETHER_BRICK_HUNG_SIGN.glowTexture = new Identifier("block/lava_still");
    GLOWING_BLACKSTONE_HUNG_SIGN.glowTexture = new Identifier("block/glowstone");
    GLOWING_POLISHED_BLACKSTONE_HUNG_SIGN.glowTexture = new Identifier("block/glowstone");
  }

  // 雪块

  @ApiStatus.AvailableSince("0.1.7")
  @MiningLevel(MiningLevel.Tool.SHOVEL)
  public static final HungSignBlock SNOW_HUNG_SIGN = new HungSignBlock(Blocks.SNOW_BLOCK);

  @MiningLevel(MiningLevel.Tool.SHOVEL)
  public static final GlowingHungSignBlock GLOWING_SNOW_HUNG_SIGN = new GlowingHungSignBlock(Blocks.SNOW_BLOCK);

  // 冰

  @ApiStatus.AvailableSince("0.1.7")
  @Translucent
  public static final HungSignBlock ICE_HUNG_SIGN = new HungSignBlock(Blocks.ICE);

  static {
    SNOW_HUNG_SIGN.baseTexture = new Identifier("block/snow");
    SNOW_HUNG_SIGN.barTexture = new Identifier("block/packed_ice");
    SNOW_HUNG_SIGN.textureTop = new Identifier("block/packed_ice");
    GLOWING_SNOW_HUNG_SIGN.baseTexture = new Identifier("block/snow");
    GLOWING_SNOW_HUNG_SIGN.barTexture = new Identifier("block/packed_ice");
    GLOWING_SNOW_HUNG_SIGN.textureTop = new Identifier("block/packed_ice");
    ICE_HUNG_SIGN.textureTop = ICE_HUNG_SIGN.barTexture = new Identifier("block/blue_ice");
  }

  // 浮冰

  @ApiStatus.AvailableSince("0.1.7")
  public static final HungSignBlock PACKED_ICE_HUNG_SIGN = new HungSignBlock(Blocks.PACKED_ICE);

  @ApiStatus.AvailableSince("0.1.7")
  public static final GlowingHungSignBlock GLOWING_PACKED_ICE_HUNG_SIGN = new GlowingHungSignBlock(Blocks.PACKED_ICE);

  @ApiStatus.AvailableSince("0.1.7")
  public static final HungSignBarBlock PACKED_ICE_HUNG_SIGN_BAR = new HungSignBarBlock(Blocks.PACKED_ICE);

  // 蓝冰

  @ApiStatus.AvailableSince("0.1.7")
  public static final HungSignBlock BLUE_ICE_HUNG_SIGN = new HungSignBlock(Blocks.BLUE_ICE);

  @ApiStatus.AvailableSince("0.1.7")
  public static final GlowingHungSignBlock GLOWING_BLUE_ICE_HUNG_SIGN = new GlowingHungSignBlock(Blocks.BLUE_ICE);

  @ApiStatus.AvailableSince("0.1.7")
  public static final HungSignBarBlock BLUE_ICE_HUNG_SIGN_BAR = new HungSignBarBlock(Blocks.BLUE_ICE);

}
