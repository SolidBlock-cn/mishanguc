package pers.solid.mishang.uc.blocks;

import com.google.common.annotations.Beta;
import com.google.common.collect.ImmutableMap;
import net.minecraft.client.resources.model.sprite.Material;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MapColor;
import org.jetbrains.annotations.ApiStatus;
import pers.solid.mishang.uc.annotations.MiningLevel;
import pers.solid.mishang.uc.block.*;
import pers.solid.mishang.uc.data.MishangucRecipeGenerator;
import pers.solid.mishang.uc.util.LogicMaterial;

import java.util.Map;

/**
 * <h1>告示牌类方块</h1>
 * 具有多种不同颜色和纹理。每一种告示牌都有对应的告示牌杆，且部分的告示牌都有对应的发光告示牌方块。<br>
 * 每个告示牌都要在 {@link pers.solid.mishang.uc.blockentity.MishangucBlockEntities#HUNG_SIGN_BLOCK_ENTITY} 中能够识别，因此添加新的告示牌需要在该字段的相关参数中添加。<br>
 * 同时，还需要注意在 {@link MishangucRecipeGenerator} 中添加此方块。
 *
 * @see HungSignBlock
 * @see HungSignBarBlock
 * @see GlowingHungSignBlock
 */
public final class HungSignBlocks extends MishangucBlocks {

  // 木告示牌部分。仅有不发光的告示牌。
  @ApiStatus.AvailableSince("1.2.4")
  public static final HungSignBlock OAK_WOOD_HUNG_SIGN = register("oak_wood_hung_sign", Blocks.OAK_WOOD);
  @ApiStatus.AvailableSince("1.2.4")
  public static final HungSignBlock SPRUCE_WOOD_HUNG_SIGN = register("spruce_wood_hung_sign", Blocks.SPRUCE_WOOD);
  @ApiStatus.AvailableSince("1.2.4")
  public static final HungSignBlock BIRCH_WOOD_HUNG_SIGN = register("birch_wood_hung_sign", Blocks.BIRCH_WOOD);
  @ApiStatus.AvailableSince("1.2.4")
  public static final HungSignBlock JUNGLE_WOOD_HUNG_SIGN = register("jungle_wood_hung_sign", Blocks.JUNGLE_WOOD);
  @ApiStatus.AvailableSince("1.2.4")
  public static final HungSignBlock ACACIA_WOOD_HUNG_SIGN = register("acacia_wood_hung_sign", Blocks.ACACIA_WOOD);
  @ApiStatus.AvailableSince("1.2.4")
  public static final HungSignBlock CHERRY_WOOD_HUNG_SIGN = register("cherry_wood_hung_sign", Blocks.CHERRY_WOOD);
  @ApiStatus.AvailableSince("1.2.4")
  public static final HungSignBlock DARK_OAK_WOOD_HUNG_SIGN = register("dark_oak_wood_hung_sign", Blocks.DARK_OAK_WOOD);
  public static final HungSignBlock PALE_OAK_WOOD_HUNG_SIGN = register("pale_oak_wood_hung_sign", Blocks.PALE_OAK_WOOD);
  @ApiStatus.AvailableSince("1.2.4")
  public static final HungSignBlock MANGROVE_WOOD_HUNG_SIGN = register("mangrove_wood_hung_sign", Blocks.MANGROVE_WOOD);
  @ApiStatus.AvailableSince("0.1.7")
  public static final HungSignBlock CRIMSON_HYPHAE_HUNG_SIGN = register("crimson_hyphae_hung_sign", Blocks.CRIMSON_HYPHAE);
  @ApiStatus.AvailableSince("0.1.7")
  public static final HungSignBlock WARPED_HYPHAE_HUNG_SIGN = register("warped_hyphae_hung_sign", Blocks.WARPED_HYPHAE);
  @ApiStatus.AvailableSince("1.2.4")
  public static final HungSignBlock STRIPPED_OAK_WOOD_HUNG_SIGN = register("stripped_oak_wood_hung_sign", Blocks.STRIPPED_OAK_WOOD);
  @ApiStatus.AvailableSince("1.2.4")
  public static final HungSignBlock STRIPPED_SPRUCE_WOOD_HUNG_SIGN = register("stripped_spruce_wood_hung_sign", Blocks.STRIPPED_SPRUCE_WOOD);
  @ApiStatus.AvailableSince("1.2.4")
  public static final HungSignBlock STRIPPED_BIRCH_WOOD_HUNG_SIGN = register("stripped_birch_wood_hung_sign", Blocks.STRIPPED_BIRCH_WOOD);
  @ApiStatus.AvailableSince("1.2.4")
  public static final HungSignBlock STRIPPED_JUNGLE_WOOD_HUNG_SIGN = register("stripped_jungle_wood_hung_sign", Blocks.STRIPPED_JUNGLE_WOOD);
  @ApiStatus.AvailableSince("1.2.4")
  public static final HungSignBlock STRIPPED_ACACIA_WOOD_HUNG_SIGN = register("stripped_acacia_wood_hung_sign", Blocks.STRIPPED_ACACIA_WOOD);
  @ApiStatus.AvailableSince("1.2.4")
  public static final HungSignBlock STRIPPED_CHERRY_WOOD_HUNG_SIGN = register("stripped_cherry_wood_hung_sign", Blocks.STRIPPED_CHERRY_WOOD);
  @ApiStatus.AvailableSince("1.2.4")
  public static final HungSignBlock STRIPPED_DARK_OAK_WOOD_HUNG_SIGN = register("stripped_dark_oak_wood_hung_sign", Blocks.STRIPPED_DARK_OAK_WOOD);
  public static final HungSignBlock STRIPPED_PALE_OAK_WOOD_HUNG_SIGN = register("stripped_pale_oak_wood_hung_sign", Blocks.STRIPPED_PALE_OAK_WOOD);
  @ApiStatus.AvailableSince("1.2.4")
  public static final HungSignBlock STRIPPED_MANGROVE_WOOD_HUNG_SIGN = register("stripped_mangrove_wood_hung_sign", Blocks.STRIPPED_MANGROVE_WOOD, Block.Properties.ofFullCopy(Blocks.STRIPPED_MANGROVE_WOOD).mapColor(MapColor.COLOR_RED));
  @ApiStatus.AvailableSince("0.1.7")
  public static final HungSignBlock STRIPPED_CRIMSON_HYPHAE_HUNG_SIGN = register("stripped_crimson_hyphae_hung_sign", Blocks.STRIPPED_CRIMSON_HYPHAE);
  @ApiStatus.AvailableSince("0.1.7")
  public static final HungSignBlock STRIPPED_WARPED_HYPHAE_HUNG_SIGN = register("stripped_warped_hyphae_hung_sign", Blocks.STRIPPED_WARPED_HYPHAE);

  @ApiStatus.AvailableSince("0.1.7")
  public static final HungSignBlock OAK_HUNG_SIGN = register("oak_hung_sign", Blocks.OAK_PLANKS);
  @ApiStatus.AvailableSince("0.1.7")
  public static final HungSignBlock SPRUCE_HUNG_SIGN = register("spruce_hung_sign", Blocks.SPRUCE_PLANKS);
  @ApiStatus.AvailableSince("0.1.7")
  public static final HungSignBlock BIRCH_HUNG_SIGN = register("birch_hung_sign", Blocks.BIRCH_PLANKS);
  @ApiStatus.AvailableSince("0.1.7")
  public static final HungSignBlock JUNGLE_HUNG_SIGN = register("jungle_hung_sign", Blocks.JUNGLE_PLANKS);
  @ApiStatus.AvailableSince("0.1.7")
  public static final HungSignBlock ACACIA_HUNG_SIGN = register("acacia_hung_sign", Blocks.ACACIA_PLANKS);
  @ApiStatus.AvailableSince("1.1.1-mc1.19.4")
  public static final HungSignBlock CHERRY_HUNG_SIGN = register("cherry_hung_sign", Blocks.CHERRY_PLANKS);
  @ApiStatus.AvailableSince("0.1.7")
  public static final HungSignBlock DARK_OAK_HUNG_SIGN = register("dark_oak_hung_sign", Blocks.DARK_OAK_PLANKS);
  public static final HungSignBlock PALE_OAK_HUNG_SIGN = register("pale_oak_hung_sign", Blocks.PALE_OAK_PLANKS);
  @ApiStatus.AvailableSince("0.2.0-mc1.19")
  public static final HungSignBlock MANGROVE_HUNG_SIGN = register("mangrove_hung_sign", Blocks.MANGROVE_PLANKS);
  @ApiStatus.AvailableSince("0.1.7")
  public static final HungSignBlock CRIMSON_HUNG_SIGN = register("crimson_hung_sign", Blocks.CRIMSON_PLANKS);
  @ApiStatus.AvailableSince("0.1.7")
  public static final HungSignBlock WARPED_HUNG_SIGN = register("warped_hung_sign", Blocks.WARPED_PLANKS);

  @ApiStatus.AvailableSince("1.0.4-mc1.19.3")
  @MiningLevel(MiningLevel.Tool.AXE)
  public static final HungSignBlock BAMBOO_HUNG_SIGN = register("bamboo_hung_sign", Blocks.BAMBOO_BLOCK, Block.Properties.ofFullCopy(Blocks.BAMBOO_BLOCK).mapColor(MapColor.PLANT));
  @ApiStatus.AvailableSince("1.0.4-mc1.19.3")
  @MiningLevel(MiningLevel.Tool.AXE)
  public static final HungSignBlock BAMBOO_PLANK_HUNG_SIGN = register("bamboo_plank_hung_sign", Blocks.BAMBOO_PLANKS, Block.Properties.ofFullCopy(Blocks.BAMBOO_PLANKS));
  @ApiStatus.AvailableSince("1.0.4-mc1.19.3")
  @MiningLevel(MiningLevel.Tool.AXE)
  public static final HungSignBlock BAMBOO_MOSAIC_HUNG_SIGN = register("bamboo_mosaic_hung_sign", Blocks.BAMBOO_MOSAIC, Block.Properties.ofFullCopy(Blocks.BAMBOO_MOSAIC));

  // 木告示牌杆部分。
  @ApiStatus.AvailableSince("0.1.7")
  public static final HungSignBarBlock OAK_HUNG_SIGN_BAR = registerBar("oak_hung_sign_bar", Blocks.OAK_WOOD);
  @ApiStatus.AvailableSince("0.1.7")
  public static final HungSignBarBlock SPRUCE_HUNG_SIGN_BAR = registerBar("spruce_hung_sign_bar", Blocks.SPRUCE_WOOD);
  @ApiStatus.AvailableSince("0.1.7")
  public static final HungSignBarBlock BIRCH_HUNG_SIGN_BAR = registerBar("birch_hung_sign_bar", Blocks.BIRCH_WOOD);
  @ApiStatus.AvailableSince("0.1.7")
  public static final HungSignBarBlock JUNGLE_HUNG_SIGN_BAR = registerBar("jungle_hung_sign_bar", Blocks.JUNGLE_WOOD);
  @ApiStatus.AvailableSince("0.1.7")
  public static final HungSignBarBlock ACACIA_HUNG_SIGN_BAR = registerBar("acacia_hung_sign_bar", Blocks.ACACIA_WOOD);
  @ApiStatus.AvailableSince("1.1.1-mc1.19.4")
  public static final HungSignBarBlock CHERRY_HUNG_SIGN_BAR = registerBar("cherry_hung_sign_bar", Blocks.CHERRY_WOOD);
  @ApiStatus.AvailableSince("0.1.7")
  public static final HungSignBarBlock DARK_OAK_HUNG_SIGN_BAR = registerBar("dark_oak_hung_sign_bar", Blocks.DARK_OAK_WOOD);
  public static final HungSignBarBlock PALE_OAK_HUNG_SIGN_BAR = registerBar("pale_oak_hung_sign_bar", Blocks.PALE_OAK_WOOD);
  @ApiStatus.AvailableSince("0.2.0-mc1.19")
  public static final HungSignBarBlock MANGROVE_HUNG_SIGN_BAR = registerBar("mangrove_hung_sign_bar", Blocks.MANGROVE_WOOD);
  @ApiStatus.AvailableSince("0.1.7")
  public static final HungSignBarBlock CRIMSON_HUNG_SIGN_BAR = registerBar("crimson_hung_sign_bar", Blocks.CRIMSON_HYPHAE);
  @ApiStatus.AvailableSince("0.1.7")
  public static final HungSignBarBlock WARPED_HUNG_SIGN_BAR = registerBar("warped_hung_sign_bar", Blocks.WARPED_HYPHAE);
  @ApiStatus.AvailableSince("1.2.4")
  public static final HungSignBarBlock STRIPPED_OAK_HUNG_SIGN_BAR = registerBar("stripped_oak_hung_sign_bar", Blocks.OAK_WOOD);
  @ApiStatus.AvailableSince("1.2.4")
  public static final HungSignBarBlock STRIPPED_SPRUCE_HUNG_SIGN_BAR = registerBar("stripped_spruce_hung_sign_bar", Blocks.STRIPPED_SPRUCE_WOOD);
  @ApiStatus.AvailableSince("1.2.4")
  public static final HungSignBarBlock STRIPPED_BIRCH_HUNG_SIGN_BAR = registerBar("stripped_birch_hung_sign_bar", Blocks.STRIPPED_BIRCH_WOOD);
  @ApiStatus.AvailableSince("1.2.4")
  public static final HungSignBarBlock STRIPPED_JUNGLE_HUNG_SIGN_BAR = registerBar("stripped_jungle_hung_sign_bar", Blocks.STRIPPED_JUNGLE_WOOD);
  @ApiStatus.AvailableSince("1.2.4")
  public static final HungSignBarBlock STRIPPED_ACACIA_HUNG_SIGN_BAR = registerBar("stripped_acacia_hung_sign_bar", Blocks.STRIPPED_ACACIA_WOOD);
  @ApiStatus.AvailableSince("1.2.4")
  public static final HungSignBarBlock STRIPPED_CHERRY_HUNG_SIGN_BAR = registerBar("stripped_cherry_hung_sign_bar", Blocks.STRIPPED_CHERRY_WOOD);
  @ApiStatus.AvailableSince("1.2.4")
  public static final HungSignBarBlock STRIPPED_DARK_OAK_HUNG_SIGN_BAR = registerBar("stripped_dark_oak_hung_sign_bar", Blocks.STRIPPED_DARK_OAK_WOOD);
  public static final HungSignBarBlock STRIPPED_PALE_OAK_HUNG_SIGN_BAR = registerBar("stripped_pale_oak_hung_sign_bar", Blocks.STRIPPED_PALE_OAK_WOOD);
  @ApiStatus.AvailableSince("1.2.4")
  public static final HungSignBarBlock STRIPPED_MANGROVE_HUNG_SIGN_BAR = registerBar("stripped_mangrove_hung_sign_bar", Blocks.STRIPPED_MANGROVE_WOOD, Block.Properties.ofFullCopy(Blocks.STRIPPED_MANGROVE_WOOD).mapColor(MapColor.COLOR_RED));
  @ApiStatus.AvailableSince("1.2.4")
  public static final HungSignBarBlock STRIPPED_CRIMSON_HUNG_SIGN_BAR = registerBar("stripped_crimson_hung_sign_bar", Blocks.STRIPPED_CRIMSON_HYPHAE);
  @ApiStatus.AvailableSince("1.2.4")
  public static final HungSignBarBlock STRIPPED_WARPED_HUNG_SIGN_BAR = registerBar("stripped_warped_hung_sign_bar", Blocks.STRIPPED_WARPED_HYPHAE);

  @ApiStatus.AvailableSince("1.0.4-mc1.19.3")
  @MiningLevel(MiningLevel.Tool.AXE)
  public static final HungSignBarBlock BAMBOO_HUNG_SIGN_BAR = registerBar("bamboo_hung_sign_bar", Blocks.BAMBOO_BLOCK, Block.Properties.ofFullCopy(Blocks.BAMBOO_BLOCK).mapColor(MapColor.PLANT));

  static {
    OAK_WOOD_HUNG_SIGN.baseMaterial = OAK_WOOD_HUNG_SIGN.barMaterial = OAK_HUNG_SIGN.barMaterial = new LogicMaterial(Identifier.withDefaultNamespace("block/oak_log"));
    SPRUCE_WOOD_HUNG_SIGN.baseMaterial = SPRUCE_WOOD_HUNG_SIGN.barMaterial = SPRUCE_HUNG_SIGN.barMaterial = new LogicMaterial(Identifier.withDefaultNamespace("block/spruce_log"));
    BIRCH_WOOD_HUNG_SIGN.baseMaterial = BIRCH_WOOD_HUNG_SIGN.barMaterial = BIRCH_HUNG_SIGN.barMaterial = new LogicMaterial(Identifier.withDefaultNamespace("block/birch_log"));
    JUNGLE_WOOD_HUNG_SIGN.baseMaterial = JUNGLE_WOOD_HUNG_SIGN.barMaterial = JUNGLE_HUNG_SIGN.barMaterial = new LogicMaterial(Identifier.withDefaultNamespace("block/jungle_log"));
    ACACIA_WOOD_HUNG_SIGN.baseMaterial = ACACIA_WOOD_HUNG_SIGN.barMaterial = ACACIA_HUNG_SIGN.barMaterial = new LogicMaterial(Identifier.withDefaultNamespace("block/acacia_log"));
    CHERRY_WOOD_HUNG_SIGN.baseMaterial = CHERRY_WOOD_HUNG_SIGN.barMaterial = CHERRY_HUNG_SIGN.barMaterial = new LogicMaterial(Identifier.withDefaultNamespace("block/cherry_log"));
    DARK_OAK_WOOD_HUNG_SIGN.baseMaterial = DARK_OAK_WOOD_HUNG_SIGN.barMaterial = DARK_OAK_HUNG_SIGN.barMaterial = new LogicMaterial(Identifier.withDefaultNamespace("block/dark_oak_log"));
    PALE_OAK_WOOD_HUNG_SIGN.baseMaterial = PALE_OAK_WOOD_HUNG_SIGN.barMaterial = PALE_OAK_HUNG_SIGN.barMaterial = new LogicMaterial(Identifier.withDefaultNamespace("block/pale_oak_log"));
    MANGROVE_WOOD_HUNG_SIGN.baseMaterial = MANGROVE_WOOD_HUNG_SIGN.barMaterial = MANGROVE_HUNG_SIGN.barMaterial = new LogicMaterial(Identifier.withDefaultNamespace("block/mangrove_log"));
    CRIMSON_HYPHAE_HUNG_SIGN.baseMaterial = CRIMSON_HYPHAE_HUNG_SIGN.barMaterial = CRIMSON_HUNG_SIGN.barMaterial = new LogicMaterial(Identifier.withDefaultNamespace("block/crimson_stem"));
    WARPED_HYPHAE_HUNG_SIGN.baseMaterial = WARPED_HYPHAE_HUNG_SIGN.barMaterial = WARPED_HUNG_SIGN.barMaterial = new LogicMaterial(Identifier.withDefaultNamespace("block/warped_stem"));
    STRIPPED_OAK_WOOD_HUNG_SIGN.baseMaterial = new LogicMaterial(Identifier.withDefaultNamespace("block/stripped_oak_log"));
    STRIPPED_SPRUCE_WOOD_HUNG_SIGN.baseMaterial = new LogicMaterial(Identifier.withDefaultNamespace("block/stripped_spruce_log"));
    STRIPPED_BIRCH_WOOD_HUNG_SIGN.baseMaterial = new LogicMaterial(Identifier.withDefaultNamespace("block/stripped_birch_log"));
    STRIPPED_JUNGLE_WOOD_HUNG_SIGN.baseMaterial = new LogicMaterial(Identifier.withDefaultNamespace("block/stripped_jungle_log"));
    STRIPPED_ACACIA_WOOD_HUNG_SIGN.baseMaterial = new LogicMaterial(Identifier.withDefaultNamespace("block/stripped_acacia_log"));
    STRIPPED_CHERRY_WOOD_HUNG_SIGN.baseMaterial = new LogicMaterial(Identifier.withDefaultNamespace("block/stripped_cherry_log"));
    STRIPPED_DARK_OAK_WOOD_HUNG_SIGN.baseMaterial = new LogicMaterial(Identifier.withDefaultNamespace("block/stripped_dark_oak_log"));
    STRIPPED_PALE_OAK_WOOD_HUNG_SIGN.baseMaterial = new LogicMaterial(Identifier.withDefaultNamespace("block/stripped_pale_oak_log"));
    STRIPPED_MANGROVE_WOOD_HUNG_SIGN.baseMaterial = new LogicMaterial(Identifier.withDefaultNamespace("block/stripped_mangrove_log"));
    STRIPPED_CRIMSON_HYPHAE_HUNG_SIGN.baseMaterial = new LogicMaterial(Identifier.withDefaultNamespace("block/stripped_crimson_stem"));
    STRIPPED_WARPED_HYPHAE_HUNG_SIGN.baseMaterial = new LogicMaterial(Identifier.withDefaultNamespace("block/stripped_warped_stem"));
    OAK_HUNG_SIGN.materialTop = new LogicMaterial(Identifier.withDefaultNamespace("block/oak_log"));
    SPRUCE_HUNG_SIGN.materialTop = new LogicMaterial(Identifier.withDefaultNamespace("block/spruce_log"));
    BIRCH_HUNG_SIGN.materialTop = new LogicMaterial(Identifier.withDefaultNamespace("block/birch_log"));
    JUNGLE_HUNG_SIGN.materialTop = new LogicMaterial(Identifier.withDefaultNamespace("block/jungle_log"));
    ACACIA_HUNG_SIGN.materialTop = new LogicMaterial(Identifier.withDefaultNamespace("block/acacia_log"));
    CHERRY_HUNG_SIGN.materialTop = new LogicMaterial(Identifier.withDefaultNamespace("block/cherry_log"));
    DARK_OAK_HUNG_SIGN.materialTop = new LogicMaterial(Identifier.withDefaultNamespace("block/dark_oak_log"));
    PALE_OAK_HUNG_SIGN.materialTop = new LogicMaterial(Identifier.withDefaultNamespace("block/pale_oak_log"));
    MANGROVE_HUNG_SIGN.materialTop = new LogicMaterial(Identifier.withDefaultNamespace("block/mangrove_log"));
    CRIMSON_HUNG_SIGN.materialTop = new LogicMaterial(Identifier.withDefaultNamespace("block/crimson_stem"));
    WARPED_HUNG_SIGN.materialTop = new LogicMaterial(Identifier.withDefaultNamespace("block/warped_stem"));
    OAK_HUNG_SIGN_BAR.material = new LogicMaterial(Identifier.withDefaultNamespace("block/oak_log"));
    SPRUCE_HUNG_SIGN_BAR.material = new LogicMaterial(Identifier.withDefaultNamespace("block/spruce_log"));
    BIRCH_HUNG_SIGN_BAR.material = new LogicMaterial(Identifier.withDefaultNamespace("block/birch_log"));
    JUNGLE_HUNG_SIGN_BAR.material = new LogicMaterial(Identifier.withDefaultNamespace("block/jungle_log"));
    ACACIA_HUNG_SIGN_BAR.material = new LogicMaterial(Identifier.withDefaultNamespace("block/acacia_log"));
    CHERRY_HUNG_SIGN_BAR.material = new LogicMaterial(Identifier.withDefaultNamespace("block/cherry_log"));
    DARK_OAK_HUNG_SIGN_BAR.material = new LogicMaterial(Identifier.withDefaultNamespace("block/dark_oak_log"));
    PALE_OAK_HUNG_SIGN_BAR.material = new LogicMaterial(Identifier.withDefaultNamespace("block/pale_oak_log"));
    MANGROVE_HUNG_SIGN_BAR.material = new LogicMaterial(Identifier.withDefaultNamespace("block/mangrove_log"));
    CRIMSON_HUNG_SIGN_BAR.material = new LogicMaterial(Identifier.withDefaultNamespace("block/crimson_stem"));
    WARPED_HUNG_SIGN_BAR.material = new LogicMaterial(Identifier.withDefaultNamespace("block/warped_stem"));
    STRIPPED_OAK_HUNG_SIGN_BAR.material = new LogicMaterial(Identifier.withDefaultNamespace("block/stripped_oak_log"));
    STRIPPED_SPRUCE_HUNG_SIGN_BAR.material = new LogicMaterial(Identifier.withDefaultNamespace("block/stripped_spruce_log"));
    STRIPPED_BIRCH_HUNG_SIGN_BAR.material = new LogicMaterial(Identifier.withDefaultNamespace("block/stripped_birch_log"));
    STRIPPED_JUNGLE_HUNG_SIGN_BAR.material = new LogicMaterial(Identifier.withDefaultNamespace("block/stripped_jungle_log"));
    STRIPPED_ACACIA_HUNG_SIGN_BAR.material = new LogicMaterial(Identifier.withDefaultNamespace("block/stripped_acacia_log"));
    STRIPPED_CHERRY_HUNG_SIGN_BAR.material = new LogicMaterial(Identifier.withDefaultNamespace("block/stripped_cherry_log"));
    STRIPPED_DARK_OAK_HUNG_SIGN_BAR.material = new LogicMaterial(Identifier.withDefaultNamespace("block/stripped_dark_oak_log"));
    STRIPPED_PALE_OAK_HUNG_SIGN_BAR.material = new LogicMaterial(Identifier.withDefaultNamespace("block/stripped_pale_oak_log"));
    STRIPPED_MANGROVE_HUNG_SIGN_BAR.material = new LogicMaterial(Identifier.withDefaultNamespace("block/stripped_mangrove_log"));
    STRIPPED_CRIMSON_HUNG_SIGN_BAR.material = new LogicMaterial(Identifier.withDefaultNamespace("block/stripped_crimson_stem"));
    STRIPPED_WARPED_HUNG_SIGN_BAR.material = new LogicMaterial(Identifier.withDefaultNamespace("block/stripped_warped_stem"));
    BAMBOO_HUNG_SIGN.barMaterial = BAMBOO_HUNG_SIGN.materialTop = BAMBOO_PLANK_HUNG_SIGN.barMaterial = BAMBOO_PLANK_HUNG_SIGN.materialTop = BAMBOO_MOSAIC_HUNG_SIGN.barMaterial = BAMBOO_MOSAIC_HUNG_SIGN.materialTop = BAMBOO_HUNG_SIGN_BAR.material = new LogicMaterial(Identifier.withDefaultNamespace("block/bamboo_block"));
  }

  // 混凝土告示牌部分

  public static final HungSignBlock WHITE_CONCRETE_HUNG_SIGN = register("white_concrete_hung_sign", Blocks.WHITE_CONCRETE);

  public static final HungSignBlock ORANGE_CONCRETE_HUNG_SIGN = register("orange_concrete_hung_sign", Blocks.ORANGE_CONCRETE);

  public static final HungSignBlock MAGENTA_CONCRETE_HUNG_SIGN = register("magenta_concrete_hung_sign", Blocks.MAGENTA_CONCRETE);

  public static final HungSignBlock LIGHT_BLUE_CONCRETE_HUNG_SIGN = register("light_blue_concrete_hung_sign", Blocks.LIGHT_BLUE_CONCRETE);

  public static final HungSignBlock YELLOW_CONCRETE_HUNG_SIGN = register("yellow_concrete_hung_sign", Blocks.YELLOW_CONCRETE);

  public static final HungSignBlock LIME_CONCRETE_HUNG_SIGN = register("lime_concrete_hung_sign", Blocks.LIME_CONCRETE);

  public static final HungSignBlock PINK_CONCRETE_HUNG_SIGN = register("pink_concrete_hung_sign", Blocks.PINK_CONCRETE);

  public static final HungSignBlock GRAY_CONCRETE_HUNG_SIGN = register("gray_concrete_hung_sign", Blocks.GRAY_CONCRETE);

  public static final HungSignBlock LIGHT_GRAY_CONCRETE_HUNG_SIGN = register("light_gray_concrete_hung_sign", Blocks.LIGHT_GRAY_CONCRETE);

  public static final HungSignBlock CYAN_CONCRETE_HUNG_SIGN = register("cyan_concrete_hung_sign", Blocks.CYAN_CONCRETE);

  public static final HungSignBlock PURPLE_CONCRETE_HUNG_SIGN = register("purple_concrete_hung_sign", Blocks.PURPLE_CONCRETE);

  public static final HungSignBlock BLUE_CONCRETE_HUNG_SIGN = register("blue_concrete_hung_sign", Blocks.BLUE_CONCRETE);

  public static final HungSignBlock BROWN_CONCRETE_HUNG_SIGN = register("brown_concrete_hung_sign", Blocks.BROWN_CONCRETE);

  public static final HungSignBlock GREEN_CONCRETE_HUNG_SIGN = register("green_concrete_hung_sign", Blocks.GREEN_CONCRETE);

  public static final HungSignBlock RED_CONCRETE_HUNG_SIGN = register("red_concrete_hung_sign", Blocks.RED_CONCRETE);

  public static final HungSignBlock BLACK_CONCRETE_HUNG_SIGN = register("black_concrete_hung_sign", Blocks.BLACK_CONCRETE);

  /**
   * 自定义颜色的混凝土悬挂告示牌。
   */
  public static final ColoredHungSignBlock COLORED_CONCRETE_HUNG_SIGN = registerColored("colored_concrete_hung_sign", ColoredBlocks.COLORED_CONCRETE);

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

  public static final HungSignBarBlock WHITE_CONCRETE_HUNG_SIGN_BAR = registerBar("white_concrete_hung_sign_bar", Blocks.WHITE_CONCRETE);

  public static final HungSignBarBlock ORANGE_CONCRETE_HUNG_SIGN_BAR = registerBar("orange_concrete_hung_sign_bar", Blocks.ORANGE_CONCRETE);

  public static final HungSignBarBlock MAGENTA_CONCRETE_HUNG_SIGN_BAR = registerBar("magenta_concrete_hung_sign_bar", Blocks.MAGENTA_CONCRETE);

  public static final HungSignBarBlock LIGHT_BLUE_CONCRETE_HUNG_SIGN_BAR = registerBar("light_blue_concrete_hung_sign_bar", Blocks.LIGHT_BLUE_CONCRETE);

  public static final HungSignBarBlock YELLOW_CONCRETE_HUNG_SIGN_BAR = registerBar("yellow_concrete_hung_sign_bar", Blocks.YELLOW_CONCRETE);

  public static final HungSignBarBlock LIME_CONCRETE_HUNG_SIGN_BAR = registerBar("lime_concrete_hung_sign_bar", Blocks.LIME_CONCRETE);

  public static final HungSignBarBlock PINK_CONCRETE_HUNG_SIGN_BAR = registerBar("pink_concrete_hung_sign_bar", Blocks.PINK_CONCRETE);

  public static final HungSignBarBlock GRAY_CONCRETE_HUNG_SIGN_BAR = registerBar("gray_concrete_hung_sign_bar", Blocks.GRAY_CONCRETE);

  public static final HungSignBarBlock LIGHT_GRAY_CONCRETE_HUNG_SIGN_BAR = registerBar("light_gray_concrete_hung_sign_bar", Blocks.LIGHT_GRAY_CONCRETE);

  public static final HungSignBarBlock CYAN_CONCRETE_HUNG_SIGN_BAR = registerBar("cyan_concrete_hung_sign_bar", Blocks.CYAN_CONCRETE);

  public static final HungSignBarBlock PURPLE_CONCRETE_HUNG_SIGN_BAR = registerBar("purple_concrete_hung_sign_bar", Blocks.PURPLE_CONCRETE);

  public static final HungSignBarBlock BLUE_CONCRETE_HUNG_SIGN_BAR = registerBar("blue_concrete_hung_sign_bar", Blocks.BLUE_CONCRETE);

  public static final HungSignBarBlock BROWN_CONCRETE_HUNG_SIGN_BAR = registerBar("brown_concrete_hung_sign_bar", Blocks.BROWN_CONCRETE);

  public static final HungSignBarBlock GREEN_CONCRETE_HUNG_SIGN_BAR = registerBar("green_concrete_hung_sign_bar", Blocks.GREEN_CONCRETE);

  public static final HungSignBarBlock RED_CONCRETE_HUNG_SIGN_BAR = registerBar("red_concrete_hung_sign_bar", Blocks.RED_CONCRETE);

  public static final HungSignBarBlock BLACK_CONCRETE_HUNG_SIGN_BAR = registerBar("black_concrete_hung_sign_bar", Blocks.BLACK_CONCRETE);

  /**
   * 自定义颜色的混凝土悬挂告示牌杆。
   */
  public static final HungSignBarBlock COLORED_CONCRETE_HUNG_SIGN_BAR = registerColoredBar("colored_concrete_hung_sign_bar", ColoredBlocks.COLORED_CONCRETE);

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

  public static final HungSignBlock WHITE_TERRACOTTA_HUNG_SIGN = register("white_terracotta_hung_sign", Blocks.WHITE_TERRACOTTA);

  public static final HungSignBlock ORANGE_TERRACOTTA_HUNG_SIGN = register("orange_terracotta_hung_sign", Blocks.ORANGE_TERRACOTTA);

  public static final HungSignBlock MAGENTA_TERRACOTTA_HUNG_SIGN = register("magenta_terracotta_hung_sign", Blocks.MAGENTA_TERRACOTTA);

  public static final HungSignBlock LIGHT_BLUE_TERRACOTTA_HUNG_SIGN = register("light_blue_terracotta_hung_sign", Blocks.LIGHT_BLUE_TERRACOTTA);

  public static final HungSignBlock YELLOW_TERRACOTTA_HUNG_SIGN = register("yellow_terracotta_hung_sign", Blocks.YELLOW_TERRACOTTA);

  public static final HungSignBlock LIME_TERRACOTTA_HUNG_SIGN = register("lime_terracotta_hung_sign", Blocks.LIME_TERRACOTTA);

  public static final HungSignBlock PINK_TERRACOTTA_HUNG_SIGN = register("pink_terracotta_hung_sign", Blocks.PINK_TERRACOTTA);

  public static final HungSignBlock GRAY_TERRACOTTA_HUNG_SIGN = register("gray_terracotta_hung_sign", Blocks.GRAY_TERRACOTTA);

  public static final HungSignBlock LIGHT_GRAY_TERRACOTTA_HUNG_SIGN = register("light_gray_terracotta_hung_sign", Blocks.LIGHT_GRAY_TERRACOTTA);

  public static final HungSignBlock CYAN_TERRACOTTA_HUNG_SIGN = register("cyan_terracotta_hung_sign", Blocks.CYAN_TERRACOTTA);

  public static final HungSignBlock PURPLE_TERRACOTTA_HUNG_SIGN = register("purple_terracotta_hung_sign", Blocks.PURPLE_TERRACOTTA);

  public static final HungSignBlock BLUE_TERRACOTTA_HUNG_SIGN = register("blue_terracotta_hung_sign", Blocks.BLUE_TERRACOTTA);

  public static final HungSignBlock BROWN_TERRACOTTA_HUNG_SIGN = register("brown_terracotta_hung_sign", Blocks.BROWN_TERRACOTTA);

  public static final HungSignBlock GREEN_TERRACOTTA_HUNG_SIGN = register("green_terracotta_hung_sign", Blocks.GREEN_TERRACOTTA);

  public static final HungSignBlock RED_TERRACOTTA_HUNG_SIGN = register("red_terracotta_hung_sign", Blocks.RED_TERRACOTTA);

  public static final HungSignBlock BLACK_TERRACOTTA_HUNG_SIGN = register("black_terracotta_hung_sign", Blocks.BLACK_TERRACOTTA);

  @Beta
  public static final ColoredHungSignBlock COLORED_TERRACOTTA_HUNG_SIGN = registerColored("colored_terracotta_hung_sign", ColoredBlocks.COLORED_TERRACOTTA);

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

  public static final HungSignBarBlock WHITE_TERRACOTTA_HUNG_SIGN_BAR = registerBar("white_terracotta_hung_sign_bar", Blocks.WHITE_TERRACOTTA);

  public static final HungSignBarBlock ORANGE_TERRACOTTA_HUNG_SIGN_BAR = registerBar("orange_terracotta_hung_sign_bar", Blocks.ORANGE_TERRACOTTA);

  public static final HungSignBarBlock MAGENTA_TERRACOTTA_HUNG_SIGN_BAR = registerBar("magenta_terracotta_hung_sign_bar", Blocks.MAGENTA_TERRACOTTA);

  public static final HungSignBarBlock LIGHT_BLUE_TERRACOTTA_HUNG_SIGN_BAR = registerBar("light_blue_terracotta_hung_sign_bar", Blocks.LIGHT_BLUE_TERRACOTTA);

  public static final HungSignBarBlock YELLOW_TERRACOTTA_HUNG_SIGN_BAR = registerBar("yellow_terracotta_hung_sign_bar", Blocks.YELLOW_TERRACOTTA);

  public static final HungSignBarBlock LIME_TERRACOTTA_HUNG_SIGN_BAR = registerBar("lime_terracotta_hung_sign_bar", Blocks.LIME_TERRACOTTA);

  public static final HungSignBarBlock PINK_TERRACOTTA_HUNG_SIGN_BAR = registerBar("pink_terracotta_hung_sign_bar", Blocks.PINK_TERRACOTTA);

  public static final HungSignBarBlock GRAY_TERRACOTTA_HUNG_SIGN_BAR = registerBar("gray_terracotta_hung_sign_bar", Blocks.GRAY_TERRACOTTA);

  public static final HungSignBarBlock LIGHT_GRAY_TERRACOTTA_HUNG_SIGN_BAR = registerBar("light_gray_terracotta_hung_sign_bar", Blocks.LIGHT_GRAY_TERRACOTTA);

  public static final HungSignBarBlock CYAN_TERRACOTTA_HUNG_SIGN_BAR = registerBar("cyan_terracotta_hung_sign_bar", Blocks.CYAN_TERRACOTTA);

  public static final HungSignBarBlock PURPLE_TERRACOTTA_HUNG_SIGN_BAR = registerBar("purple_terracotta_hung_sign_bar", Blocks.PURPLE_TERRACOTTA);

  public static final HungSignBarBlock BLUE_TERRACOTTA_HUNG_SIGN_BAR = registerBar("blue_terracotta_hung_sign_bar", Blocks.BLUE_TERRACOTTA);

  public static final HungSignBarBlock BROWN_TERRACOTTA_HUNG_SIGN_BAR = registerBar("brown_terracotta_hung_sign_bar", Blocks.BROWN_TERRACOTTA);

  public static final HungSignBarBlock GREEN_TERRACOTTA_HUNG_SIGN_BAR = registerBar("green_terracotta_hung_sign_bar", Blocks.GREEN_TERRACOTTA);

  public static final HungSignBarBlock RED_TERRACOTTA_HUNG_SIGN_BAR = registerBar("red_terracotta_hung_sign_bar", Blocks.RED_TERRACOTTA);

  public static final HungSignBarBlock BLACK_TERRACOTTA_HUNG_SIGN_BAR = registerBar("black_terracotta_hung_sign_bar", Blocks.BLACK_TERRACOTTA);

  @Beta
  public static final ColoredHungSignBarBlock COLORED_TERRACOTTA_HUNG_SIGN_BAR = registerColoredBar("colored_terracotta_hung_sign_bar", ColoredBlocks.COLORED_TERRACOTTA);

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

  public static final GlowingHungSignBlock GLOWING_WHITE_CONCRETE_HUNG_SIGN = registerGlowing("glowing_white_concrete_hung_sign", Blocks.WHITE_CONCRETE);

  public static final GlowingHungSignBlock GLOWING_ORANGE_CONCRETE_HUNG_SIGN = registerGlowing("glowing_orange_concrete_hung_sign", Blocks.ORANGE_CONCRETE);

  public static final GlowingHungSignBlock GLOWING_MAGENTA_CONCRETE_HUNG_SIGN = registerGlowing("glowing_magenta_concrete_hung_sign", Blocks.MAGENTA_CONCRETE);

  public static final GlowingHungSignBlock GLOWING_LIGHT_BLUE_CONCRETE_HUNG_SIGN = registerGlowing("glowing_light_blue_concrete_hung_sign", Blocks.LIGHT_BLUE_CONCRETE);

  public static final GlowingHungSignBlock GLOWING_YELLOW_CONCRETE_HUNG_SIGN = registerGlowing("glowing_yellow_concrete_hung_sign", Blocks.YELLOW_CONCRETE);

  public static final GlowingHungSignBlock GLOWING_LIME_CONCRETE_HUNG_SIGN = registerGlowing("glowing_lime_concrete_hung_sign", Blocks.LIME_CONCRETE);

  public static final GlowingHungSignBlock GLOWING_PINK_CONCRETE_HUNG_SIGN = registerGlowing("glowing_pink_concrete_hung_sign", Blocks.PINK_CONCRETE);

  public static final GlowingHungSignBlock GLOWING_GRAY_CONCRETE_HUNG_SIGN = registerGlowing("glowing_gray_concrete_hung_sign", Blocks.GRAY_CONCRETE);

  public static final GlowingHungSignBlock GLOWING_LIGHT_GRAY_CONCRETE_HUNG_SIGN = registerGlowing("glowing_light_gray_concrete_hung_sign", Blocks.LIGHT_GRAY_CONCRETE);

  public static final GlowingHungSignBlock GLOWING_CYAN_CONCRETE_HUNG_SIGN = registerGlowing("glowing_cyan_concrete_hung_sign", Blocks.CYAN_CONCRETE);

  public static final GlowingHungSignBlock GLOWING_PURPLE_CONCRETE_HUNG_SIGN = registerGlowing("glowing_purple_concrete_hung_sign", Blocks.PURPLE_CONCRETE);

  public static final GlowingHungSignBlock GLOWING_BLUE_CONCRETE_HUNG_SIGN = registerGlowing("glowing_blue_concrete_hung_sign", Blocks.BLUE_CONCRETE);

  public static final GlowingHungSignBlock GLOWING_BROWN_CONCRETE_HUNG_SIGN = registerGlowing("glowing_brown_concrete_hung_sign", Blocks.BROWN_CONCRETE);

  public static final GlowingHungSignBlock GLOWING_GREEN_CONCRETE_HUNG_SIGN = registerGlowing("glowing_green_concrete_hung_sign", Blocks.GREEN_CONCRETE);

  public static final GlowingHungSignBlock GLOWING_RED_CONCRETE_HUNG_SIGN = registerGlowing("glowing_red_concrete_hung_sign", Blocks.RED_CONCRETE);

  public static final GlowingHungSignBlock GLOWING_BLACK_CONCRETE_HUNG_SIGN = registerGlowing("glowing_black_concrete_hung_sign", Blocks.BLACK_CONCRETE);

  @ApiStatus.AvailableSince("1.0.2")
  public static final ColoredGlowingHungSignBlock COLORED_GLOWING_CONCRETE_HUNG_SIGN = registerColoredGlowing("colored_glowing_concrete_hung_sign", ColoredBlocks.COLORED_CONCRETE);

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

  public static final GlowingHungSignBlock GLOWING_WHITE_TERRACOTTA_HUNG_SIGN = registerGlowing("glowing_white_terracotta_hung_sign", Blocks.WHITE_TERRACOTTA);

  public static final GlowingHungSignBlock GLOWING_ORANGE_TERRACOTTA_HUNG_SIGN = registerGlowing("glowing_orange_terracotta_hung_sign", Blocks.ORANGE_TERRACOTTA);

  public static final GlowingHungSignBlock GLOWING_MAGENTA_TERRACOTTA_HUNG_SIGN = registerGlowing("glowing_magenta_terracotta_hung_sign", Blocks.MAGENTA_TERRACOTTA);

  public static final GlowingHungSignBlock GLOWING_LIGHT_BLUE_TERRACOTTA_HUNG_SIGN = registerGlowing("glowing_light_blue_terracotta_hung_sign", Blocks.LIGHT_BLUE_TERRACOTTA);

  public static final GlowingHungSignBlock GLOWING_YELLOW_TERRACOTTA_HUNG_SIGN = registerGlowing("glowing_yellow_terracotta_hung_sign", Blocks.YELLOW_TERRACOTTA);

  public static final GlowingHungSignBlock GLOWING_LIME_TERRACOTTA_HUNG_SIGN = registerGlowing("glowing_lime_terracotta_hung_sign", Blocks.LIME_TERRACOTTA);

  public static final GlowingHungSignBlock GLOWING_PINK_TERRACOTTA_HUNG_SIGN = registerGlowing("glowing_pink_terracotta_hung_sign", Blocks.PINK_TERRACOTTA);

  public static final GlowingHungSignBlock GLOWING_GRAY_TERRACOTTA_HUNG_SIGN = registerGlowing("glowing_gray_terracotta_hung_sign", Blocks.GRAY_TERRACOTTA);

  public static final GlowingHungSignBlock GLOWING_LIGHT_GRAY_TERRACOTTA_HUNG_SIGN = registerGlowing("glowing_light_gray_terracotta_hung_sign", Blocks.LIGHT_GRAY_TERRACOTTA);

  public static final GlowingHungSignBlock GLOWING_CYAN_TERRACOTTA_HUNG_SIGN = registerGlowing("glowing_cyan_terracotta_hung_sign", Blocks.CYAN_TERRACOTTA);

  public static final GlowingHungSignBlock GLOWING_PURPLE_TERRACOTTA_HUNG_SIGN = registerGlowing("glowing_purple_terracotta_hung_sign", Blocks.PURPLE_TERRACOTTA);

  public static final GlowingHungSignBlock GLOWING_BLUE_TERRACOTTA_HUNG_SIGN = registerGlowing("glowing_blue_terracotta_hung_sign", Blocks.BLUE_TERRACOTTA);

  public static final GlowingHungSignBlock GLOWING_BROWN_TERRACOTTA_HUNG_SIGN = registerGlowing("glowing_brown_terracotta_hung_sign", Blocks.BROWN_TERRACOTTA);

  public static final GlowingHungSignBlock GLOWING_GREEN_TERRACOTTA_HUNG_SIGN = registerGlowing("glowing_green_terracotta_hung_sign", Blocks.GREEN_TERRACOTTA);

  public static final GlowingHungSignBlock GLOWING_RED_TERRACOTTA_HUNG_SIGN = registerGlowing("glowing_red_terracotta_hung_sign", Blocks.RED_TERRACOTTA);

  public static final GlowingHungSignBlock GLOWING_BLACK_TERRACOTTA_HUNG_SIGN = registerGlowing("glowing_black_terracotta_hung_sign", Blocks.BLACK_TERRACOTTA);

  @ApiStatus.AvailableSince("1.0.2")
  public static final ColoredGlowingHungSignBlock COLORED_GLOWING_TERRACOTTA_HUNG_SIGN = registerColoredGlowing("colored_glowing_terracotta_hung_sign", ColoredBlocks.COLORED_TERRACOTTA);

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
  public static final HungSignBlock STONE_HUNG_SIGN = register("stone_hung_sign", Blocks.STONE);

  @ApiStatus.AvailableSince("0.1.7")
  public static final GlowingHungSignBlock GLOWING_STONE_HUNG_SIGN = registerGlowing("glowing_stone_hung_sign", Blocks.STONE);

  @ApiStatus.AvailableSince("0.1.7")
  public static final HungSignBarBlock STONE_HUNG_SIGN_BAR = registerBar("stone_hung_sign_bar", Blocks.STONE);

  @ApiStatus.AvailableSince("1.0.2")
  public static final ColoredHungSignBlock COLORED_STONE_HUNG_SIGN = registerColored("colored_stone_hung_sign", ColoredBlocks.COLORED_STONE);

  @ApiStatus.AvailableSince("1.0.2")
  public static final ColoredGlowingHungSignBlock COLORED_GLOWING_STONE_HUNG_SIGN = registerColoredGlowing("colored_glowing_stone_hung_sign", ColoredBlocks.COLORED_STONE);

  @ApiStatus.AvailableSince("1.0.2")
  public static final ColoredHungSignBarBlock COLORED_STONE_HUNG_SIGN_BAR = registerColoredBar("colored_stone_hung_sign_bar", ColoredBlocks.COLORED_STONE);

  // 圆石

  @ApiStatus.AvailableSince("0.2.4")
  public static final HungSignBlock COBBLESTONE_HUNG_SIGN = register("cobblestone_hung_sign", Blocks.COBBLESTONE);

  @ApiStatus.AvailableSince("0.2.4")
  public static final GlowingHungSignBlock GLOWING_COBBLESTONE_HUNG_SIGN = registerGlowing("glowing_cobblestone_hung_sign", Blocks.COBBLESTONE);

  @ApiStatus.AvailableSince("0.2.4")
  public static final HungSignBarBlock COBBLESTONE_HUNG_SIGN_BAR = registerBar("cobblestone_hung_sign_bar", Blocks.COBBLESTONE);

  @ApiStatus.AvailableSince("1.0.2")
  public static final ColoredHungSignBlock COLORED_COBBLESTONE_HUNG_SIGN = registerColored("colored_cobblestone_hung_sign", ColoredBlocks.COLORED_COBBLESTONE);

  @ApiStatus.AvailableSince("1.0.2")
  public static final ColoredGlowingHungSignBlock COLORED_GLOWING_COBBLESTONE_HUNG_SIGN = registerColoredGlowing("colored_glowing_cobblestone_hung_sign", ColoredBlocks.COLORED_COBBLESTONE);

  @ApiStatus.AvailableSince("1.0.2")
  public static final ColoredHungSignBarBlock COLORED_COBBLESTONE_HUNG_SIGN_BAR = registerColoredBar("colored_cobblestone_hung_sign_bar", ColoredBlocks.COLORED_COBBLESTONE);

  // 石砖

  @ApiStatus.AvailableSince("0.1.7")
  public static final HungSignBlock STONE_BRICK_HUNG_SIGN = register("stone_brick_hung_sign", Blocks.STONE_BRICKS);

  @ApiStatus.AvailableSince("0.1.7")
  public static final GlowingHungSignBlock GLOWING_STONE_BRICK_HUNG_SIGN = registerGlowing("glowing_stone_brick_hung_sign", Blocks.STONE_BRICKS);

  @ApiStatus.AvailableSince("0.1.7")
  public static final HungSignBarBlock STONE_BRICK_HUNG_SIGN_BAR = registerBar("stone_brick_hung_sign_bar", Blocks.STONE_BRICKS);

  @ApiStatus.AvailableSince("1.0.2")
  public static final ColoredHungSignBlock COLORED_STONE_BRICK_HUNG_SIGN = registerColored("colored_stone_brick_hung_sign", ColoredBlocks.COLORED_STONE_BRICKS);

  @ApiStatus.AvailableSince("1.0.2")
  public static final ColoredGlowingHungSignBlock COLORED_GLOWING_STONE_BRICK_HUNG_SIGN = registerColoredGlowing("colored_glowing_stone_brick_hung_sign", ColoredBlocks.COLORED_STONE_BRICKS);

  @ApiStatus.AvailableSince("1.0.2")
  public static final ColoredHungSignBarBlock COLORED_STONE_BRICK_HUNG_SIGN_BAR = registerColoredBar("colored_stone_brick_hung_sign_bar", ColoredBlocks.COLORED_STONE_BRICKS);

  // 铁块

  @ApiStatus.AvailableSince("0.1.7")
  @MiningLevel(level = MiningLevel.Level.STONE)
  public static final HungSignBlock IRON_HUNG_SIGN = register("iron_hung_sign", Blocks.IRON_BLOCK);

  @ApiStatus.AvailableSince("0.1.7")
  @MiningLevel(level = MiningLevel.Level.STONE)
  public static final HungSignBlock GLOWING_IRON_HUNG_SIGN = registerGlowing("glowing_iron_hung_sign", Blocks.IRON_BLOCK);

  @ApiStatus.AvailableSince("0.1.7")
  @MiningLevel(level = MiningLevel.Level.STONE)
  public static final HungSignBarBlock IRON_HUNG_SIGN_BAR = registerBar("iron_hung_sign_bar", Blocks.IRON_BLOCK);

  @ApiStatus.AvailableSince("1.0.2")
  @MiningLevel(level = MiningLevel.Level.STONE)
  public static final ColoredHungSignBlock COLORED_IRON_HUNG_SIGN = registerColored("colored_iron_hung_sign", ColoredBlocks.COLORED_IRON_BLOCK);

  @ApiStatus.AvailableSince("1.0.2")
  @MiningLevel(level = MiningLevel.Level.STONE)
  public static final ColoredGlowingHungSignBlock COLORED_GLOWING_IRON_HUNG_SIGN = registerColoredGlowing("colored_glowing_iron_hung_sign", ColoredBlocks.COLORED_IRON_BLOCK);

  @ApiStatus.AvailableSince("1.0.2")
  @MiningLevel(level = MiningLevel.Level.STONE)
  public static final ColoredHungSignBarBlock COLORED_IRON_HUNG_SIGN_BAR = registerColoredBar("colored_iron_hung_sign_bar", ColoredBlocks.COLORED_IRON_BLOCK);

  // 金块

  @ApiStatus.AvailableSince("0.1.7")
  @MiningLevel(level = MiningLevel.Level.IRON)
  public static final HungSignBlock GOLD_HUNG_SIGN = register("gold_hung_sign", Blocks.GOLD_BLOCK);

  @ApiStatus.AvailableSince("0.1.7")
  @MiningLevel(level = MiningLevel.Level.IRON)
  public static final GlowingHungSignBlock GLOWING_GOLD_HUNG_SIGN = registerGlowing("glowing_gold_hung_sign", Blocks.GOLD_BLOCK);

  @ApiStatus.AvailableSince("0.1.7")
  @MiningLevel(level = MiningLevel.Level.IRON)
  public static final HungSignBarBlock GOLD_HUNG_SIGN_BAR = registerBar("gold_hung_sign_bar", Blocks.GOLD_BLOCK);

  // 钻石块

  @ApiStatus.AvailableSince("0.1.7")
  @MiningLevel(level = MiningLevel.Level.IRON)
  public static final HungSignBlock DIAMOND_HUNG_SIGN = register("diamond_hung_sign", Blocks.DIAMOND_BLOCK);

  @ApiStatus.AvailableSince("0.1.7")
  @MiningLevel(level = MiningLevel.Level.IRON)
  public static final GlowingHungSignBlock GLOWING_DIAMOND_HUNG_SIGN = registerGlowing("glowing_diamond_hung_sign", Blocks.DIAMOND_BLOCK);

  @ApiStatus.AvailableSince("0.1.7")
  @MiningLevel(level = MiningLevel.Level.IRON)
  public static final HungSignBarBlock DIAMOND_HUNG_SIGN_BAR = registerBar("diamond_hung_sign_bar", Blocks.DIAMOND_BLOCK);

  // 绿宝石块

  @ApiStatus.AvailableSince("0.1.7")
  @MiningLevel(level = MiningLevel.Level.IRON)
  public static final HungSignBlock EMERALD_HUNG_SIGN = register("emerald_hung_sign", Blocks.EMERALD_BLOCK);

  @ApiStatus.AvailableSince("0.1.7")
  @MiningLevel(level = MiningLevel.Level.IRON)
  public static final GlowingHungSignBlock GLOWING_EMERALD_HUNG_SIGN = registerGlowing("glowing_emerald_hung_sign", Blocks.EMERALD_BLOCK);

  @ApiStatus.AvailableSince("0.1.7")
  @MiningLevel(level = MiningLevel.Level.IRON)
  public static final HungSignBarBlock EMERALD_HUNG_SIGN_BAR = registerBar("emerald_hung_sign_bar", Blocks.EMERALD_BLOCK);

  // 青金石块

  @ApiStatus.AvailableSince("0.1.7")
  @MiningLevel(level = MiningLevel.Level.STONE)
  public static final HungSignBlock LAPIS_HUNG_SIGN = register("lapis_hung_sign", Blocks.LAPIS_BLOCK);

  @ApiStatus.AvailableSince("0.1.7")
  @MiningLevel(level = MiningLevel.Level.STONE)
  public static final GlowingHungSignBlock GLOWING_LAPIS_HUNG_SIGN = registerGlowing("glowing_lapis_hung_sign", Blocks.LAPIS_BLOCK);

  @ApiStatus.AvailableSince("0.1.7")
  @MiningLevel(level = MiningLevel.Level.STONE)
  public static final HungSignBarBlock LAPIS_HUNG_SIGN_BAR = registerBar("lapis_hung_sign_bar", Blocks.LAPIS_BLOCK);

  // 下界合金

  @ApiStatus.AvailableSince("0.2.4")
  @MiningLevel(level = MiningLevel.Level.DIAMOND)
  public static final HungSignBlock NETHERITE_HUNG_SIGN = register("netherite_hung_sign", Blocks.NETHERITE_BLOCK);

  @ApiStatus.AvailableSince("0.2.4")
  @MiningLevel(level = MiningLevel.Level.DIAMOND)
  public static final GlowingHungSignBlock GLOWING_NETHERITE_HUNG_SIGN = registerGlowing("glowing_netherite_hung_sign", Blocks.NETHERITE_BLOCK);

  @ApiStatus.AvailableSince("0.2.4")
  @MiningLevel(level = MiningLevel.Level.DIAMOND)
  public static final HungSignBarBlock NETHERITE_HUNG_SIGN_BAR = registerBar("netherite_hung_sign_bar", Blocks.NETHERITE_BLOCK);

  // 黑曜石

  @ApiStatus.AvailableSince("0.2.4")
  @MiningLevel(level = MiningLevel.Level.DIAMOND)
  public static final HungSignBlock OBSIDIAN_HUNG_SIGN = register("obsidian_hung_sign", Blocks.OBSIDIAN);

  @ApiStatus.AvailableSince("0.2.4")
  @MiningLevel(level = MiningLevel.Level.DIAMOND)
  public static final GlowingHungSignBlock GLOWING_OBSIDIAN_HUNG_SIGN = registerGlowing("glowing_obsidian_hung_sign", Blocks.OBSIDIAN);

  @ApiStatus.AvailableSince("0.2.4")
  @MiningLevel(level = MiningLevel.Level.DIAMOND)
  public static final HungSignBarBlock OBSIDIAN_HUNG_SIGN_BAR = registerBar("obsidian_hung_sign_bar", Blocks.OBSIDIAN);

  // 哭泣的黑曜石

  @ApiStatus.AvailableSince("0.2.4")
  @MiningLevel(level = MiningLevel.Level.DIAMOND)
  public static final HungSignBlock CRYING_OBSIDIAN_HUNG_SIGN = register("crying_obsidian_hung_sign", Blocks.CRYING_OBSIDIAN);

  @ApiStatus.AvailableSince("0.2.4")
  @MiningLevel(level = MiningLevel.Level.DIAMOND)
  public static final GlowingHungSignBlock GLOWING_CRYING_OBSIDIAN_HUNG_SIGN = registerGlowing("glowing_crying_obsidian_hung_sign", Blocks.CRYING_OBSIDIAN);

  @ApiStatus.AvailableSince("0.2.4")
  @MiningLevel(level = MiningLevel.Level.DIAMOND)
  public static final HungSignBarBlock CRYING_OBSIDIAN_HUNG_SIGN_BAR = registerBar("crying_obsidian_hung_sign_bar", Blocks.CRYING_OBSIDIAN);

  // 下界岩

  @ApiStatus.AvailableSince("0.1.7")
  public static final HungSignBlock NETHERRACK_HUNG_SIGN = register("netherrack_hung_sign", Blocks.NETHERRACK);

  public static final GlowingHungSignBlock GLOWING_NETHERRACK_HUNG_SIGN = registerGlowing("glowing_netherrack_hung_sign", Blocks.NETHERRACK);

  public static final HungSignBarBlock NETHERRACK_HUNG_SIGN_BAR = registerBar("netherrack_hung_sign_bar", Blocks.NETHERRACK);

  // 下界砖

  @ApiStatus.AvailableSince("0.1.7")
  public static final HungSignBlock NETHER_BRICK_HUNG_SIGN = register("nether_brick_hung_sign", Blocks.NETHER_BRICKS);

  public static final GlowingHungSignBlock GLOWING_NETHER_BRICK_HUNG_SIGN = registerGlowing("glowing_nether_brick_hung_sign", Blocks.NETHER_BRICKS);

  public static final HungSignBarBlock NETHER_BRICK_HUNG_SIGN_BAR = registerBar("nether_brick_hung_sign_bar", Blocks.NETHER_BRICKS);

  // 黑石

  @ApiStatus.AvailableSince("0.1.7")
  public static final HungSignBlock BLACKSTONE_HUNG_SIGN = register("blackstone_hung_sign", Blocks.BLACKSTONE);

  public static final GlowingHungSignBlock GLOWING_BLACKSTONE_HUNG_SIGN = registerGlowing("glowing_blackstone_hung_sign", Blocks.BLACKSTONE);

  public static final HungSignBarBlock BLACKSTONE_HUNG_SIGN_BAR = registerBar("blackstone_hung_sign_bar", Blocks.BLACKSTONE);

  // 磨制黑石

  @ApiStatus.AvailableSince("0.1.7")
  public static final HungSignBlock POLISHED_BLACKSTONE_HUNG_SIGN = register("polished_blackstone_hung_sign", Blocks.POLISHED_BLACKSTONE);

  public static final GlowingHungSignBlock GLOWING_POLISHED_BLACKSTONE_HUNG_SIGN = registerGlowing("glowing_polished_blackstone_hung_sign", Blocks.POLISHED_BLACKSTONE);

  public static final HungSignBarBlock POLISHED_BLACKSTONE_HUNG_SIGN_BAR = registerBar("polished_blackstone_hung_sign_bar", Blocks.POLISHED_BLACKSTONE);

  static {
    GLOWING_NETHERRACK_HUNG_SIGN.glowMaterial = new LogicMaterial(Identifier.withDefaultNamespace("block/lava_still"));
    GLOWING_NETHER_BRICK_HUNG_SIGN.glowMaterial = new LogicMaterial(Identifier.withDefaultNamespace("block/lava_still"));
    GLOWING_BLACKSTONE_HUNG_SIGN.glowMaterial = new LogicMaterial(Identifier.withDefaultNamespace("block/glowstone"));
    GLOWING_POLISHED_BLACKSTONE_HUNG_SIGN.glowMaterial = new LogicMaterial(Identifier.withDefaultNamespace("block/glowstone"));
  }

  // 雪块

  @ApiStatus.AvailableSince("0.1.7")
  @MiningLevel(MiningLevel.Tool.SHOVEL)
  public static final HungSignBlock SNOW_HUNG_SIGN = register("snow_hung_sign", Blocks.SNOW_BLOCK);

  @MiningLevel(MiningLevel.Tool.SHOVEL)
  public static final GlowingHungSignBlock GLOWING_SNOW_HUNG_SIGN = registerGlowing("glowing_snow_hung_sign", Blocks.SNOW_BLOCK);

  // 冰

  @ApiStatus.AvailableSince("0.1.7")
  public static final HungSignBlock ICE_HUNG_SIGN = register("ice_hung_sign", Blocks.ICE);

  static {
    SNOW_HUNG_SIGN.baseMaterial = new LogicMaterial(Identifier.withDefaultNamespace("block/snow"));
    SNOW_HUNG_SIGN.barMaterial = new LogicMaterial(Identifier.withDefaultNamespace("block/packed_ice"));
    SNOW_HUNG_SIGN.materialTop = new LogicMaterial(Identifier.withDefaultNamespace("block/packed_ice"));
    GLOWING_SNOW_HUNG_SIGN.baseMaterial = new LogicMaterial(Identifier.withDefaultNamespace("block/snow"));
    GLOWING_SNOW_HUNG_SIGN.barMaterial = new LogicMaterial(Identifier.withDefaultNamespace("block/packed_ice"));
    GLOWING_SNOW_HUNG_SIGN.materialTop = new LogicMaterial(Identifier.withDefaultNamespace("block/packed_ice"));
    ICE_HUNG_SIGN.materialTop = ICE_HUNG_SIGN.barMaterial = new LogicMaterial(Identifier.withDefaultNamespace("block/blue_ice"));
  }

  // 浮冰

  @ApiStatus.AvailableSince("0.1.7")
  public static final HungSignBlock PACKED_ICE_HUNG_SIGN = register("packed_ice_hung_sign", Blocks.PACKED_ICE);

  @ApiStatus.AvailableSince("0.1.7")
  public static final GlowingHungSignBlock GLOWING_PACKED_ICE_HUNG_SIGN = registerGlowing("glowing_packed_ice_hung_sign", Blocks.PACKED_ICE);

  @ApiStatus.AvailableSince("0.1.7")
  public static final HungSignBarBlock PACKED_ICE_HUNG_SIGN_BAR = registerBar("packed_ice_hung_sign_bar", Blocks.PACKED_ICE);

  // 蓝冰

  @ApiStatus.AvailableSince("0.1.7")
  public static final HungSignBlock BLUE_ICE_HUNG_SIGN = register("blue_ice_hung_sign", Blocks.BLUE_ICE);

  @ApiStatus.AvailableSince("0.1.7")
  public static final GlowingHungSignBlock GLOWING_BLUE_ICE_HUNG_SIGN = registerGlowing("glowing_blue_ice_hung_sign", Blocks.BLUE_ICE);

  @ApiStatus.AvailableSince("0.1.7")
  public static final HungSignBarBlock BLUE_ICE_HUNG_SIGN_BAR = registerBar("blue_ice_hung_sign_bar", Blocks.BLUE_ICE);

  public static HungSignBlock register(String name, Block baseBlock, BlockBehaviour.Properties settings) {
    return MishangucBlocks.register(name, settings1 -> new HungSignBlock(baseBlock, settings1), settings);
  }

  public static HungSignBlock register(String name, Block baseBlock) {
    return register(name, baseBlock, BlockBehaviour.Properties.ofFullCopy(baseBlock));
  }

  public static ColoredHungSignBlock registerColored(String name, Block baseBlock) {
    return MishangucBlocks.register(name, settings -> new ColoredHungSignBlock(baseBlock, settings), BlockBehaviour.Properties.ofFullCopy(baseBlock));
  }

  public static GlowingHungSignBlock registerGlowing(String name, Block baseBlock) {
    return MishangucBlocks.register(name, settings1 -> new GlowingHungSignBlock(baseBlock, settings1), BlockBehaviour.Properties.ofFullCopy(baseBlock));
  }

  public static ColoredGlowingHungSignBlock registerColoredGlowing(String name, Block baseBlock) {
    return MishangucBlocks.register(name, settings1 -> new ColoredGlowingHungSignBlock(baseBlock, settings1), BlockBehaviour.Properties.ofFullCopy(baseBlock));
  }

  public static HungSignBarBlock registerBar(String name, Block baseBlock, BlockBehaviour.Properties settings) {
    return MishangucBlocks.register(name, settings1 -> new HungSignBarBlock(baseBlock, settings1), settings);
  }

  public static HungSignBarBlock registerBar(String name, Block baseBlock) {
    return registerBar(name, baseBlock, BlockBehaviour.Properties.ofFullCopy(baseBlock));
  }

  public static ColoredHungSignBarBlock registerColoredBar(String name, Block baseBlock) {
    return MishangucBlocks.register(name, settings -> new ColoredHungSignBarBlock(baseBlock, settings), BlockBehaviour.Properties.ofFullCopy(baseBlock));
  }
}
