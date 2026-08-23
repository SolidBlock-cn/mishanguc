package pers.solid.mishang.uc.blocks;

import com.google.common.annotations.Beta;
import net.minecraft.resources.Identifier;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.ColorCollection;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MapColor;
import org.jetbrains.annotations.ApiStatus;
import pers.solid.mishang.uc.annotations.MiningLevel;
import pers.solid.mishang.uc.block.*;
import pers.solid.mishang.uc.data.MishangucRecipeGenerator;

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
    OAK_WOOD_HUNG_SIGN.baseTexture = OAK_WOOD_HUNG_SIGN.barTexture = OAK_HUNG_SIGN.barTexture = Identifier.withDefaultNamespace("block/oak_log");
    SPRUCE_WOOD_HUNG_SIGN.baseTexture = SPRUCE_WOOD_HUNG_SIGN.barTexture = SPRUCE_HUNG_SIGN.barTexture = Identifier.withDefaultNamespace("block/spruce_log");
    BIRCH_WOOD_HUNG_SIGN.baseTexture = BIRCH_WOOD_HUNG_SIGN.barTexture = BIRCH_HUNG_SIGN.barTexture = Identifier.withDefaultNamespace("block/birch_log");
    JUNGLE_WOOD_HUNG_SIGN.baseTexture = JUNGLE_WOOD_HUNG_SIGN.barTexture = JUNGLE_HUNG_SIGN.barTexture = Identifier.withDefaultNamespace("block/jungle_log");
    ACACIA_WOOD_HUNG_SIGN.baseTexture = ACACIA_WOOD_HUNG_SIGN.barTexture = ACACIA_HUNG_SIGN.barTexture = Identifier.withDefaultNamespace("block/acacia_log");
    CHERRY_WOOD_HUNG_SIGN.baseTexture = CHERRY_WOOD_HUNG_SIGN.barTexture = CHERRY_HUNG_SIGN.barTexture = Identifier.withDefaultNamespace("block/cherry_log");
    DARK_OAK_WOOD_HUNG_SIGN.baseTexture = DARK_OAK_WOOD_HUNG_SIGN.barTexture = DARK_OAK_HUNG_SIGN.barTexture = Identifier.withDefaultNamespace("block/dark_oak_log");
    PALE_OAK_WOOD_HUNG_SIGN.baseTexture = PALE_OAK_WOOD_HUNG_SIGN.barTexture = PALE_OAK_HUNG_SIGN.barTexture = Identifier.withDefaultNamespace("block/pale_oak_log");
    MANGROVE_WOOD_HUNG_SIGN.baseTexture = MANGROVE_WOOD_HUNG_SIGN.barTexture = MANGROVE_HUNG_SIGN.barTexture = Identifier.withDefaultNamespace("block/mangrove_log");
    CRIMSON_HYPHAE_HUNG_SIGN.baseTexture = CRIMSON_HYPHAE_HUNG_SIGN.barTexture = CRIMSON_HUNG_SIGN.barTexture = Identifier.withDefaultNamespace("block/crimson_stem");
    WARPED_HYPHAE_HUNG_SIGN.baseTexture = WARPED_HYPHAE_HUNG_SIGN.barTexture = WARPED_HUNG_SIGN.barTexture = Identifier.withDefaultNamespace("block/warped_stem");
    STRIPPED_OAK_WOOD_HUNG_SIGN.baseTexture = Identifier.withDefaultNamespace("block/stripped_oak_log");
    STRIPPED_SPRUCE_WOOD_HUNG_SIGN.baseTexture = Identifier.withDefaultNamespace("block/stripped_spruce_log");
    STRIPPED_BIRCH_WOOD_HUNG_SIGN.baseTexture = Identifier.withDefaultNamespace("block/stripped_birch_log");
    STRIPPED_JUNGLE_WOOD_HUNG_SIGN.baseTexture = Identifier.withDefaultNamespace("block/stripped_jungle_log");
    STRIPPED_ACACIA_WOOD_HUNG_SIGN.baseTexture = Identifier.withDefaultNamespace("block/stripped_acacia_log");
    STRIPPED_CHERRY_WOOD_HUNG_SIGN.baseTexture = Identifier.withDefaultNamespace("block/stripped_cherry_log");
    STRIPPED_DARK_OAK_WOOD_HUNG_SIGN.baseTexture = Identifier.withDefaultNamespace("block/stripped_dark_oak_log");
    STRIPPED_PALE_OAK_WOOD_HUNG_SIGN.baseTexture = Identifier.withDefaultNamespace("block/stripped_pale_oak_log");
    STRIPPED_MANGROVE_WOOD_HUNG_SIGN.baseTexture = Identifier.withDefaultNamespace("block/stripped_mangrove_log");
    STRIPPED_CRIMSON_HYPHAE_HUNG_SIGN.baseTexture = Identifier.withDefaultNamespace("block/stripped_crimson_stem");
    STRIPPED_WARPED_HYPHAE_HUNG_SIGN.baseTexture = Identifier.withDefaultNamespace("block/stripped_warped_stem");
    OAK_HUNG_SIGN.textureTop = Identifier.withDefaultNamespace("block/oak_log");
    SPRUCE_HUNG_SIGN.textureTop = Identifier.withDefaultNamespace("block/spruce_log");
    BIRCH_HUNG_SIGN.textureTop = Identifier.withDefaultNamespace("block/birch_log");
    JUNGLE_HUNG_SIGN.textureTop = Identifier.withDefaultNamespace("block/jungle_log");
    ACACIA_HUNG_SIGN.textureTop = Identifier.withDefaultNamespace("block/acacia_log");
    CHERRY_HUNG_SIGN.textureTop = Identifier.withDefaultNamespace("block/cherry_log");
    DARK_OAK_HUNG_SIGN.textureTop = Identifier.withDefaultNamespace("block/dark_oak_log");
    PALE_OAK_HUNG_SIGN.textureTop = Identifier.withDefaultNamespace("block/pale_oak_log");
    MANGROVE_HUNG_SIGN.textureTop = Identifier.withDefaultNamespace("block/mangrove_log");
    CRIMSON_HUNG_SIGN.textureTop = Identifier.withDefaultNamespace("block/crimson_stem");
    WARPED_HUNG_SIGN.textureTop = Identifier.withDefaultNamespace("block/warped_stem");
    OAK_HUNG_SIGN_BAR.texture = Identifier.withDefaultNamespace("block/oak_log");
    SPRUCE_HUNG_SIGN_BAR.texture = Identifier.withDefaultNamespace("block/spruce_log");
    BIRCH_HUNG_SIGN_BAR.texture = Identifier.withDefaultNamespace("block/birch_log");
    JUNGLE_HUNG_SIGN_BAR.texture = Identifier.withDefaultNamespace("block/jungle_log");
    ACACIA_HUNG_SIGN_BAR.texture = Identifier.withDefaultNamespace("block/acacia_log");
    CHERRY_HUNG_SIGN_BAR.texture = Identifier.withDefaultNamespace("block/cherry_log");
    DARK_OAK_HUNG_SIGN_BAR.texture = Identifier.withDefaultNamespace("block/dark_oak_log");
    PALE_OAK_HUNG_SIGN_BAR.texture = Identifier.withDefaultNamespace("block/pale_oak_log");
    MANGROVE_HUNG_SIGN_BAR.texture = Identifier.withDefaultNamespace("block/mangrove_log");
    CRIMSON_HUNG_SIGN_BAR.texture = Identifier.withDefaultNamespace("block/crimson_stem");
    WARPED_HUNG_SIGN_BAR.texture = Identifier.withDefaultNamespace("block/warped_stem");
    STRIPPED_OAK_HUNG_SIGN_BAR.texture = Identifier.withDefaultNamespace("block/stripped_oak_log");
    STRIPPED_SPRUCE_HUNG_SIGN_BAR.texture = Identifier.withDefaultNamespace("block/stripped_spruce_log");
    STRIPPED_BIRCH_HUNG_SIGN_BAR.texture = Identifier.withDefaultNamespace("block/stripped_birch_log");
    STRIPPED_JUNGLE_HUNG_SIGN_BAR.texture = Identifier.withDefaultNamespace("block/stripped_jungle_log");
    STRIPPED_ACACIA_HUNG_SIGN_BAR.texture = Identifier.withDefaultNamespace("block/stripped_acacia_log");
    STRIPPED_CHERRY_HUNG_SIGN_BAR.texture = Identifier.withDefaultNamespace("block/stripped_cherry_log");
    STRIPPED_DARK_OAK_HUNG_SIGN_BAR.texture = Identifier.withDefaultNamespace("block/stripped_dark_oak_log");
    STRIPPED_PALE_OAK_HUNG_SIGN_BAR.texture = Identifier.withDefaultNamespace("block/stripped_pale_oak_log");
    STRIPPED_MANGROVE_HUNG_SIGN_BAR.texture = Identifier.withDefaultNamespace("block/stripped_mangrove_log");
    STRIPPED_CRIMSON_HUNG_SIGN_BAR.texture = Identifier.withDefaultNamespace("block/stripped_crimson_stem");
    STRIPPED_WARPED_HUNG_SIGN_BAR.texture = Identifier.withDefaultNamespace("block/stripped_warped_stem");
    BAMBOO_HUNG_SIGN.barTexture = BAMBOO_HUNG_SIGN.textureTop = BAMBOO_PLANK_HUNG_SIGN.barTexture = BAMBOO_PLANK_HUNG_SIGN.textureTop = BAMBOO_MOSAIC_HUNG_SIGN.barTexture = BAMBOO_MOSAIC_HUNG_SIGN.textureTop = BAMBOO_HUNG_SIGN_BAR.texture = Identifier.withDefaultNamespace("block/bamboo_block");
  }

  // 混凝土告示牌部分

  public static final ColorCollection<HungSignBlock> CONCRETE_HUNG_SIGN = ColorCollection.zipMap(ColorCollection.NAMES, ColorCollection.VALUES, (s, dyeColor) -> register(s + "_concrete_hung_sign", Blocks.CONCRETE.pick(dyeColor)));

  /**
   * 自定义颜色的混凝土悬挂告示牌。
   */
  public static final ColoredHungSignBlock COLORED_CONCRETE_HUNG_SIGN = registerColored("colored_concrete_hung_sign", ColoredBlocks.COLORED_CONCRETE);

  // 混凝土告示牌杆
  public static final ColorCollection<HungSignBarBlock> CONCRETE_HUNG_SIGN_BAR = ColorCollection.zipMap(ColorCollection.NAMES, ColorCollection.VALUES, (s, dyeColor) -> registerBar(s + "_concrete_hung_sign_bar", Blocks.CONCRETE.pick(dyeColor)));

  /**
   * 自定义颜色的混凝土悬挂告示牌杆。
   */
  public static final HungSignBarBlock COLORED_CONCRETE_HUNG_SIGN_BAR = registerColoredBar("colored_concrete_hung_sign_bar", ColoredBlocks.COLORED_CONCRETE);


  // 陶瓦告示牌部分

  public static final ColorCollection<HungSignBlock> DYED_TERRACOTTA_HUNG_SIGN = ColorCollection.zipMap(ColorCollection.NAMES, ColorCollection.VALUES, (s, dyeColor) -> register(s + "_terracotta_hung_sign", Blocks.DYED_TERRACOTTA.pick(dyeColor)));

  @Beta
  public static final ColoredHungSignBlock COLORED_TERRACOTTA_HUNG_SIGN = registerColored("colored_terracotta_hung_sign", ColoredBlocks.COLORED_TERRACOTTA);

  // 陶瓦告示牌杆

  public static final ColorCollection<HungSignBarBlock> DYED_TERRACOTTA_HUNG_SIGN_BAR = ColorCollection.zipMap(ColorCollection.NAMES, ColorCollection.VALUES, (s, dyeColor) -> registerBar(s + "_terracotta_hung_sign_bar", Blocks.DYED_TERRACOTTA.pick(dyeColor)));

  @Beta
  public static final ColoredHungSignBarBlock COLORED_TERRACOTTA_HUNG_SIGN_BAR = registerColoredBar("colored_terracotta_hung_sign_bar", ColoredBlocks.COLORED_TERRACOTTA);

  // 发光的混凝土告示牌

  public static final ColorCollection<GlowingHungSignBlock> GLOWING_CONCRETE_HUNG_SIGN = ColorCollection.zipMap(ColorCollection.NAMES, ColorCollection.VALUES, (s, dyeColor) -> registerGlowing("glowing_" + s + "_concrete_hung_sign", Blocks.CONCRETE.pick(dyeColor)));

  @ApiStatus.AvailableSince("1.0.2")
  public static final ColoredGlowingHungSignBlock COLORED_GLOWING_CONCRETE_HUNG_SIGN = registerColoredGlowing("colored_glowing_concrete_hung_sign", ColoredBlocks.COLORED_CONCRETE);

  // 发光的陶瓦告示牌

  public static final ColorCollection<GlowingHungSignBlock> GLOWING_DYED_TERRACOTTA_HUNG_SIGN = ColorCollection.zipMap(ColorCollection.NAMES, ColorCollection.VALUES, (s, dyeColor) -> registerGlowing("glowing_" + s + "_terracotta_hung_sign", Blocks.DYED_TERRACOTTA.pick(dyeColor)));

  @ApiStatus.AvailableSince("1.0.2")
  public static final ColoredGlowingHungSignBlock COLORED_GLOWING_TERRACOTTA_HUNG_SIGN = registerColoredGlowing("colored_glowing_terracotta_hung_sign", ColoredBlocks.COLORED_TERRACOTTA);

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
    GLOWING_NETHERRACK_HUNG_SIGN.glowTexture = Identifier.withDefaultNamespace("block/lava_still");
    GLOWING_NETHER_BRICK_HUNG_SIGN.glowTexture = Identifier.withDefaultNamespace("block/lava_still");
    GLOWING_BLACKSTONE_HUNG_SIGN.glowTexture = Identifier.withDefaultNamespace("block/glowstone");
    GLOWING_POLISHED_BLACKSTONE_HUNG_SIGN.glowTexture = Identifier.withDefaultNamespace("block/glowstone");
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
    SNOW_HUNG_SIGN.baseTexture = Identifier.withDefaultNamespace("block/snow");
    SNOW_HUNG_SIGN.barTexture = Identifier.withDefaultNamespace("block/packed_ice");
    SNOW_HUNG_SIGN.textureTop = Identifier.withDefaultNamespace("block/packed_ice");
    GLOWING_SNOW_HUNG_SIGN.baseTexture = Identifier.withDefaultNamespace("block/snow");
    GLOWING_SNOW_HUNG_SIGN.barTexture = Identifier.withDefaultNamespace("block/packed_ice");
    GLOWING_SNOW_HUNG_SIGN.textureTop = Identifier.withDefaultNamespace("block/packed_ice");
    ICE_HUNG_SIGN.textureTop = ICE_HUNG_SIGN.barTexture = Identifier.withDefaultNamespace("block/blue_ice");
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

  // 硫黄和朱砂
  public static final HungSignBlock SULFUR_HUNG_SIGN = register("sulfur_hung_sign", Blocks.SULFUR);
  public static final GlowingHungSignBlock GLOWING_SULFUR_HUNG_SIGN = registerGlowing("glowing_sulfur_hung_sign", Blocks.SULFUR);
  public static final HungSignBarBlock SULFUR_HUNG_SIGN_BAR = registerBar("sulfur_hung_sign_bar", Blocks.SULFUR);
  public static final HungSignBlock CINNABAR_HUNG_SIGN = register("cinnabar_hung_sign", Blocks.CINNABAR);
  public static final GlowingHungSignBlock GLOWING_CINNABAR_HUNG_SIGN = registerGlowing("glowing_cinnabar_hung_sign", Blocks.CINNABAR);
  public static final HungSignBarBlock CINNABAR_HUNG_SIGN_BAR = registerBar("cinnabar_hung_sign_bar", Blocks.CINNABAR);

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
