package pers.solid.mishang.uc.arrp;

import com.google.common.base.Predicates;
import com.google.common.collect.ImmutableSet;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.tag.TagFactory;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.block.SlabBlock;
import net.minecraft.data.client.model.Texture;
import net.minecraft.data.server.RecipesProvider;
import net.minecraft.data.server.recipe.ShapedRecipeJsonFactory;
import net.minecraft.data.server.recipe.ShapelessRecipeJsonFactory;
import net.minecraft.data.server.recipe.SingleItemRecipeJsonFactory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemConvertible;
import net.minecraft.item.Items;
import net.minecraft.recipe.Ingredient;
import net.minecraft.resource.ResourceType;
import net.minecraft.tag.BlockTags;
import net.minecraft.util.Identifier;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.Validate;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import pers.solid.brrp.v1.BRRPUtils;
import pers.solid.brrp.v1.api.RuntimeResourcePack;
import pers.solid.brrp.v1.fabric.api.SidedRRPCallback;
import pers.solid.brrp.v1.generator.BlockResourceGenerator;
import pers.solid.brrp.v1.generator.ItemResourceGenerator;
import pers.solid.brrp.v1.model.ModelJsonBuilder;
import pers.solid.brrp.v1.tag.IdentifiedTagBuilder;
import pers.solid.mishang.uc.MishangUtils;
import pers.solid.mishang.uc.block.*;
import pers.solid.mishang.uc.blocks.*;

/**
 * @since 0.1.7 本类应当在 onInitialize 的入口点中执行，而非 pregen 中。
 */
public class ARRPMain implements ModInitializer {
  private static final RuntimeResourcePack PACK = RuntimeResourcePack.create(new Identifier("mishanguc", "pack"));

  private static Identifier blockIdentifier(String path) {
    return new Identifier("mishanguc", "block/" + path);
  }

  /**
   * 添加一个方块以及其台阶方块的方块模型。仅用于此模组。
   *
   * @param block    方块。必须是道路方块，且在 {@link RoadSlabBlocks#BLOCK_TO_SLABS} 中有对应的台阶版本。
   * @param parent   资源包的 parent。应当保证 parent、parent+"_slab" 和 parent+"_slab_top"都要存在。
   * @param textures 纹理变量。三个 parent 都应该使用相同的纹理。
   */
  private static void writeRoadBlockModelWithSlab(
      AbstractRoadBlock block, String parent, Texture textures) {
    final Identifier id = block.getBlockModelId();
    final AbstractRoadSlabBlock slab = block.getRoadSlab();
    final Identifier slabId = slab == null ? null : slab.getBlockModelId();
    writeRoadBlockModelWithSlab(parent, textures, id, slabId);
  }

  /**
   * 添加一个方块以及其台阶方块的方块模型。仅用于此模组。
   *
   * @param parent   资源包的 parent。应当保证 parent、parent+"_slab" 和 parent+"_slab_top"都要存在。
   * @param textures 纹理变量。三个 parent 都应该使用相同的纹理。
   * @param id       道路方块的完整id。
   * @param slabId   该方块对应的台阶方块的完整id。
   */
  private static void writeRoadBlockModelWithSlab(String parent, Texture textures, @NotNull Identifier id, @Nullable Identifier slabId) {
    PACK.addModel(id, ModelJsonBuilder.create(blockIdentifier(parent)).setTextures(textures));
    if (slabId != null) {
      PACK.addModel(slabId, ModelJsonBuilder.create(BRRPHelper.slabOf(blockIdentifier(parent))).setTextures(textures));
      PACK.addModel(slabId.brrp_suffixed("_top"), ModelJsonBuilder.create(Identifier.tryParse(BRRPHelper.slabOf(blockIdentifier(parent)) + "_top")).setTextures(textures));
    }
  }

  private static IdentifiedTagBuilder<Block> blockTag(String path) {
    return IdentifiedTagBuilder.createBlock(new Identifier("mishanguc", path));
  }

  private static void addTags() {
    // mineable 部分；大多数 mineable 标签都是手动生成，目前仅对栏杆部分的 mineable 标签实行自动生成。
    final IdentifiedTagBuilder<Block> pickaxeMineable = IdentifiedTagBuilder.createBlock(BlockTags.PICKAXE_MINEABLE);
    final IdentifiedTagBuilder<Block> axeMineable = IdentifiedTagBuilder.createBlock(BlockTags.AXE_MINEABLE);
    final IdentifiedTagBuilder<Block> needsStoneTool = IdentifiedTagBuilder.createBlock(BlockTags.NEEDS_STONE_TOOL);
    final IdentifiedTagBuilder<Block> needsIronTool = IdentifiedTagBuilder.createBlock(BlockTags.NEEDS_IRON_TOOL);
    final IdentifiedTagBuilder<Block> needsDiamondTool = IdentifiedTagBuilder.createBlock(BlockTags.NEEDS_DIAMOND_TOOL);

    // 道路部分
    final IdentifiedTagBuilder<Block> roadBlocks = blockTag("road_blocks");
    final IdentifiedTagBuilder<Block> roadSlabs = blockTag("road_slabs");
    final IdentifiedTagBuilder<Block> roadMarks = blockTag("road_marks");

    // 灯光部分
    final IdentifiedTagBuilder<Block> whiteStripWallLights = blockTag("white_strip_wall_lights");
    final IdentifiedTagBuilder<Block> whiteWallLights = blockTag("white_wall_lights");
    final IdentifiedTagBuilder<Block> whiteCornerLights = blockTag("white_corner_lights");
    final IdentifiedTagBuilder<Block> whiteLightDecorations = blockTag("white_light_decorations");
    final IdentifiedTagBuilder<Block> whiteColumnLights = blockTag("white_column_lights");
    final IdentifiedTagBuilder<Block> yellowStripWallLights = blockTag("yellow_strip_wall_lights");
    final IdentifiedTagBuilder<Block> yellowWallLights = blockTag("yellow_wall_lights");
    final IdentifiedTagBuilder<Block> yellowCornerLights = blockTag("yellow_corner_lights");
    final IdentifiedTagBuilder<Block> yellowLightDecorations = blockTag("yellow_light_decorations");
    final IdentifiedTagBuilder<Block> yellowColumnLights = blockTag("yellow_column_lights");
    final IdentifiedTagBuilder<Block> orangeStripWallLights = blockTag("orange_strip_wall_lights");
    final IdentifiedTagBuilder<Block> orangeWallLights = blockTag("orange_wall_lights");
    final IdentifiedTagBuilder<Block> orangeCornerLights = blockTag("orange_corner_lights");
    final IdentifiedTagBuilder<Block> orangeLightDecorations = blockTag("orange_light_decorations");
    final IdentifiedTagBuilder<Block> orangeColumnLights = blockTag("orange_column_lights");
    final IdentifiedTagBuilder<Block> greenStripWallLights = blockTag("green_strip_wall_lights");
    final IdentifiedTagBuilder<Block> greenWallLights = blockTag("green_wall_lights");
    final IdentifiedTagBuilder<Block> greenCornerLights = blockTag("green_corner_lights");
    final IdentifiedTagBuilder<Block> greenLightDecorations = blockTag("green_light_decorations");
    final IdentifiedTagBuilder<Block> greenColumnLights = blockTag("green_column_lights");
    final IdentifiedTagBuilder<Block> cyanStripWallLights = blockTag("cyan_strip_wall_lights");
    final IdentifiedTagBuilder<Block> cyanWallLights = blockTag("cyan_wall_lights");
    final IdentifiedTagBuilder<Block> cyanCornerLights = blockTag("cyan_corner_lights");
    final IdentifiedTagBuilder<Block> cyanLightDecorations = blockTag("cyan_light_decorations");
    final IdentifiedTagBuilder<Block> cyanColumnLights = blockTag("cyan_column_lights");
    final IdentifiedTagBuilder<Block> pinkStripWallLights = blockTag("pink_strip_wall_lights");
    final IdentifiedTagBuilder<Block> pinkWallLights = blockTag("pink_wall_lights");
    final IdentifiedTagBuilder<Block> pinkCornerLights = blockTag("pink_corner_lights");
    final IdentifiedTagBuilder<Block> pinkLightDecorations = blockTag("pink_light_decorations");
    final IdentifiedTagBuilder<Block> pinkColumnLights = blockTag("pink_column_lights");
    final IdentifiedTagBuilder<Block> lightSlabs = blockTag("light_slabs");
    final IdentifiedTagBuilder<Block> lightCovers = blockTag("light_covers");

    // 墙上的告示牌部分
    final IdentifiedTagBuilder<Block> woodenWallSigns = blockTag("wooden_wall_signs");
    final IdentifiedTagBuilder<Block> concreteWallSigns = blockTag("concrete_wall_signs");
    final IdentifiedTagBuilder<Block> terracottaWallSigns = blockTag("terracotta_wall_signs");
    final IdentifiedTagBuilder<Block> wallSigns = blockTag("wall_signs");
    final IdentifiedTagBuilder<Block> glowingConcreteWallSigns = blockTag("glowing_concrete_wall_signs");
    final IdentifiedTagBuilder<Block> glowingTerracottaWallSigns = blockTag("glowing_terracotta_wall_signs");
    final IdentifiedTagBuilder<Block> glowingWallSigns = blockTag("glowing_wall_signs");
    final IdentifiedTagBuilder<Block> fullConcreteWallSigns = blockTag("full_concrete_wall_signs");
    final IdentifiedTagBuilder<Block> fullTerracottaWallSigns = blockTag("full_terracotta_wall_signs");
    final IdentifiedTagBuilder<Block> fullWallSigns = blockTag("full_wall_signs");

    // 悬挂的告示牌部分
    final IdentifiedTagBuilder<Block> woodenHungSigns = blockTag("wooden_hung_signs");
    final IdentifiedTagBuilder<Block> concreteHungSigns = blockTag("concrete_hung_signs");
    final IdentifiedTagBuilder<Block> terracottaHungSigns = blockTag("terracotta_hung_signs");
    final IdentifiedTagBuilder<Block> hungSigns = blockTag("hung_signs");
    final IdentifiedTagBuilder<Block> glowingConcreteHungSigns = blockTag("glowing_concrete_hung_signs");
    final IdentifiedTagBuilder<Block> glowingTerracottaHungSigns = blockTag("glowing_terracotta_hung_signs");
    final IdentifiedTagBuilder<Block> glowingHungSigns = blockTag("glowing_hung_signs");

    // 悬挂的告示牌部分
    final IdentifiedTagBuilder<Block> woodenStandingSigns = blockTag("wooden_standing_signs");
    final IdentifiedTagBuilder<Block> concreteStandingSigns = blockTag("concrete_standing_signs");
    final IdentifiedTagBuilder<Block> terracottaStandingSigns = blockTag("terracotta_standing_signs");
    final IdentifiedTagBuilder<Block> standingSigns = blockTag("standing_signs");
    final IdentifiedTagBuilder<Block> glowingConcreteStandingSigns = blockTag("glowing_concrete_standing_signs");
    final IdentifiedTagBuilder<Block> glowingTerracottaStandingSigns = blockTag("glowing_terracotta_standing_signs");
    final IdentifiedTagBuilder<Block> glowingStandingSigns = blockTag("glowing_standing_signs");

    // 悬挂的告示牌杆部分
    final IdentifiedTagBuilder<Block> woodenHungSignBars = blockTag("wooden_hung_sign_bars");
    final IdentifiedTagBuilder<Block> concreteHungSignBars = blockTag("concrete_hung_sign_bars");
    final IdentifiedTagBuilder<Block> terracottaHungSignBars = blockTag("terracotta_hung_sign_bars");
    final IdentifiedTagBuilder<Block> hungSignBars = blockTag("hung_sign_bars");

    // 栏杆部分
    final IdentifiedTagBuilder<Block> handrails = blockTag("handrails");
    final IdentifiedTagBuilder<Item> handrailItems = IdentifiedTagBuilder.createItem(new Identifier("mishanguc", "handrails"));
    final IdentifiedTagBuilder<Block> normalHandrails = blockTag("normal_handrails");
    final IdentifiedTagBuilder<Block> centralHandrails = blockTag("central_handrails");
    final IdentifiedTagBuilder<Block> cornerHandrails = blockTag("corner_handrails");
    final IdentifiedTagBuilder<Block> outerHandrails = blockTag("outer_handrails");
    final IdentifiedTagBuilder<Block> stairHandrails = blockTag("stair_handrails");

    // 混凝土栏杆部分
    final IdentifiedTagBuilder<Block> simpleConcreteHandrails = blockTag("simple_concrete_handrails");
    final IdentifiedTagBuilder<Block> simpleConcreteNormalHandrails = blockTag("simple_concrete_normal_handrails");
    final IdentifiedTagBuilder<Block> simpleConcreteCentralHandrails = blockTag("simple_concrete_central_handrails");
    final IdentifiedTagBuilder<Block> simpleConcreteCornerHandrails = blockTag("simple_concrete_corner_handrails");
    final IdentifiedTagBuilder<Block> simpleConcreteOuterHandrails = blockTag("simple_concrete_outer_handrails");
    final IdentifiedTagBuilder<Block> simpleConcreteStairHandrails = blockTag("simple_concrete_stair_handrails");

    // 陶瓦栏杆部分
    final IdentifiedTagBuilder<Block> simpleTerracottaHandrails = blockTag("simple_terracotta_handrails");
    final IdentifiedTagBuilder<Block> simpleTerracottaNormalHandrails = blockTag("simple_terracotta_normal_handrails");
    final IdentifiedTagBuilder<Block> simpleTerracottaCentralHandrails = blockTag("simple_terracotta_central_handrails");
    final IdentifiedTagBuilder<Block> simpleTerracottaCornerHandrails = blockTag("simple_terracotta_corner_handrails");
    final IdentifiedTagBuilder<Block> simpleTerracottaOuterHandrails = blockTag("simple_terracotta_outer_handrails");
    final IdentifiedTagBuilder<Block> simpleTerracottaStairHandrails = blockTag("simple_terracotta_stair_handrails");

    // 染色玻璃栏杆部分
    final IdentifiedTagBuilder<Block> simpleStainedGlassHandrails = blockTag("simple_stained_glass_handrails");
    final IdentifiedTagBuilder<Block> simpleStainedGlassNormalHandrails = blockTag("simple_stained_glass_normal_handrails");
    final IdentifiedTagBuilder<Block> simpleStainedGlassCentralHandrails = blockTag("simple_stained_glass_central_handrails");
    final IdentifiedTagBuilder<Block> simpleStainedGlassCornerHandrails = blockTag("simple_stained_glass_corner_handrails");
    final IdentifiedTagBuilder<Block> simpleStainedGlassOuterHandrails = blockTag("simple_stained_glass_outer_handrails");
    final IdentifiedTagBuilder<Block> simpleStainedGlassStairHandrails = blockTag("simple_stained_glass_stair_handrails");

    // 染色木头部分
    final IdentifiedTagBuilder<Block> simpleWoodenHandrails = blockTag("simple_wooden_handrails");
    final IdentifiedTagBuilder<Block> simpleWoodenNormalHandrails = blockTag("simple_wooden_normal_handrails");
    final IdentifiedTagBuilder<Block> simpleWoodenCentralHandrails = blockTag("simple_wooden_central_handrails");
    final IdentifiedTagBuilder<Block> simpleWoodenCornerHandrails = blockTag("simple_wooden_corner_handrails");
    final IdentifiedTagBuilder<Block> simpleWoodenOuterHandrails = blockTag("simple_wooden_outer_handrails");
    final IdentifiedTagBuilder<Block> simpleWoodenStairHandrails = blockTag("simple_wooden_stair_handrails");

    // 玻璃栏杆部分
    final IdentifiedTagBuilder<Block> glassHandrails = blockTag("glass_handrails");
    final IdentifiedTagBuilder<Block> glassNormalHandrails = blockTag("glass_normal_handrails");
    final IdentifiedTagBuilder<Block> glassCentralHandrails = blockTag("glass_central_handrails");
    final IdentifiedTagBuilder<Block> glassCornerHandrails = blockTag("glass_corner_handrails");
    final IdentifiedTagBuilder<Block> glassOuterHandrails = blockTag("glass_outer_handrails");
    final IdentifiedTagBuilder<Block> glassStairHandrails = blockTag("glass_stair_handrails");


    // 道路部分
    MishangUtils.instanceStream(RoadBlocks.class, Block.class).forEach(
        block -> {
          if (block instanceof AbstractRoadBlock) {
            roadBlocks.add(block);
          }
        }
    );
    RoadSlabBlocks.SLABS.forEach(
        block -> {
          if (block != null) {
            roadSlabs.add(block);
          }
        }
    );
    MishangUtils.instanceStream(RoadMarkBlocks.class, Block.class).forEach(roadMarks::add);

    // 灯光部分
    MishangUtils.instanceStream(LightBlocks.class, Block.class).forEach(
        block -> {
          if (block instanceof StripWallLightBlock) {
            switch (((StripWallLightBlock) block).lightColor) {
              case "white" -> whiteStripWallLights.add(block);
              case "yellow" -> yellowStripWallLights.add(block);
              case "cyan" -> cyanStripWallLights.add(block);
              case "orange" -> orangeStripWallLights.add(block);
              case "green" -> greenStripWallLights.add(block);
              case "pink" -> pinkStripWallLights.add(block);
            }
          } else if (block instanceof AutoConnectWallLightBlock) {
            switch (((AutoConnectWallLightBlock) block).lightColor) {
              case "white" -> whiteLightDecorations.add(block);
              case "yellow" -> yellowLightDecorations.add(block);
              case "cyan" -> cyanLightDecorations.add(block);
              case "orange" -> orangeLightDecorations.add(block);
              case "green" -> greenLightDecorations.add(block);
              case "pink" -> pinkLightDecorations.add(block);
            }
          } else if (block instanceof ColumnLightBlock || block instanceof ColumnWallLightBlock) {
            switch (block instanceof ColumnLightBlock ? ((ColumnLightBlock) block).lightColor : ((ColumnWallLightBlock) block).lightColor) {
              case "white" -> whiteColumnLights.add(block);
              case "yellow" -> yellowColumnLights.add(block);
              case "cyan" -> cyanColumnLights.add(block);
              case "orange" -> orangeColumnLights.add(block);
              case "green" -> greenColumnLights.add(block);
              case "pink" -> pinkColumnLights.add(block);
            }
          } else if (block instanceof WallLightBlock) {
            switch (((WallLightBlock) block).lightColor) {
              case "white" -> whiteWallLights.add(block);
              case "yellow" -> yellowWallLights.add(block);
              case "cyan" -> cyanWallLights.add(block);
              case "orange" -> orangeWallLights.add(block);
              case "green" -> greenWallLights.add(block);
              case "pink" -> pinkWallLights.add(block);
            }
          } else if (block instanceof CornerLightBlock) {
            switch (((CornerLightBlock) block).lightColor) {
              case "white" -> whiteCornerLights.add(block);
              case "yellow" -> yellowCornerLights.add(block);
              case "cyan" -> cyanCornerLights.add(block);
              case "orange" -> orangeCornerLights.add(block);
              case "green" -> greenCornerLights.add(block);
              case "pink" -> pinkCornerLights.add(block);
            }
          }
          if (block instanceof SlabBlock) {
            lightSlabs.add(block);
          } else if (block instanceof LightCoverBlock) {
            lightCovers.add(block);
          }
        }
    );

    // 悬挂的告示牌部分
    MishangUtils.instanceStream(HungSignBlocks.class, Block.class).forEach(block -> {
      if (block instanceof GlowingHungSignBlock) {
        if (MishangUtils.isConcrete(((GlowingHungSignBlock) block).baseBlock)) {
          glowingConcreteHungSigns.add(block);
        } else if (MishangUtils.isTerracotta(((GlowingHungSignBlock) block).baseBlock)) {
          glowingTerracottaHungSigns.add(block);
        } else {
          glowingHungSigns.add(block);
        }
      } else if (block instanceof HungSignBlock) {
        if (MishangUtils.isConcrete(((HungSignBlock) block).baseBlock)) {
          concreteHungSigns.add(block);
        } else if (MishangUtils.isTerracotta(((HungSignBlock) block).baseBlock)) {
          terracottaHungSigns.add(block);
        } else if (MishangUtils.isPlanks(((HungSignBlock) block).baseBlock)) {
          woodenHungSigns.add(block);
        } else {
          hungSigns.add(block);
        }
      } else if (block instanceof HungSignBarBlock) {
        if (MishangUtils.isConcrete(((HungSignBarBlock) block).baseBlock)) {
          concreteHungSignBars.add(block);
        } else if (MishangUtils.isTerracotta(((HungSignBarBlock) block).baseBlock)) {
          terracottaHungSignBars.add(block);
        } else if (MishangUtils.isWood(((HungSignBarBlock) block).baseBlock)) {
          woodenHungSignBars.add(block);
        } else {
          hungSignBars.add(block);
        }
      }
    });

    // 墙上的告示牌部分
    MishangUtils.instanceStream(WallSignBlocks.class, Block.class).forEach(block -> {
      if (block instanceof GlowingWallSignBlock) {
        if (MishangUtils.isConcrete(((GlowingWallSignBlock) block).baseBlock)) {
          glowingConcreteWallSigns.add(block);
        } else if (MishangUtils.isTerracotta(((GlowingWallSignBlock) block).baseBlock)) {
          glowingTerracottaWallSigns.add(block);
        } else {
          glowingWallSigns.add(block);
        }
      } else if (block instanceof FullWallSignBlock) {
        if (MishangUtils.isConcrete(((FullWallSignBlock) block).baseBlock)) {
          fullConcreteWallSigns.add(block);
        } else if (MishangUtils.isTerracotta(((FullWallSignBlock) block).baseBlock)) {
          fullTerracottaWallSigns.add(block);
        } else {
          fullWallSigns.add(block);
        }
      } else if (block instanceof WallSignBlock) {
        if (MishangUtils.isConcrete(((WallSignBlock) block).baseBlock)) {
          concreteWallSigns.add(block);
        } else if (MishangUtils.isTerracotta(((WallSignBlock) block).baseBlock)) {
          terracottaWallSigns.add(block);
        } else if (MishangUtils.isPlanks(((WallSignBlock) block).baseBlock)) {
          woodenWallSigns.add(block);
        } else {
          wallSigns.add(block);
        }
      }
    });

    MishangUtils.instanceStream(StandingSignBlocks.class, Block.class).forEach(block -> {
      if (block instanceof GlowingStandingSignBlock) {
        if (MishangUtils.isConcrete(((GlowingStandingSignBlock) block).baseBlock)) {
          glowingConcreteStandingSigns.add(block);
        } else if (MishangUtils.isTerracotta(((GlowingStandingSignBlock) block).baseBlock)) {
          glowingTerracottaStandingSigns.add(block);
        } else {
          glowingStandingSigns.add(block);
        }
      } else if (block instanceof StandingSignBlock) {
        if (MishangUtils.isConcrete(((StandingSignBlock) block).baseBlock)) {
          concreteStandingSigns.add(block);
        } else if (MishangUtils.isTerracotta(((StandingSignBlock) block).baseBlock)) {
          terracottaStandingSigns.add(block);
        } else if (MishangUtils.isPlanks(((StandingSignBlock) block).baseBlock)) {
          woodenStandingSigns.add(block);
        } else {
          standingSigns.add(block);
        }
      }
    });

    // 栏杆部分
    MishangUtils.instanceStream(HandrailBlocks.class, Block.class).forEach(block -> {
      if (block instanceof SimpleHandrailBlock simpleHandrailBlock) {
        if (MishangUtils.isStained_glass(((SimpleHandrailBlock) block).baseBlock)) {
          simpleStainedGlassNormalHandrails.add(simpleHandrailBlock);
          simpleStainedGlassCentralHandrails.add(simpleHandrailBlock.central);
          simpleStainedGlassCornerHandrails.add(simpleHandrailBlock.corner);
          simpleStainedGlassOuterHandrails.add(simpleHandrailBlock.outer);
          simpleStainedGlassStairHandrails.add(simpleHandrailBlock.stair);
        } else if (MishangUtils.isConcrete(((SimpleHandrailBlock) block).baseBlock)) {
          simpleConcreteNormalHandrails.add(simpleHandrailBlock);
          simpleConcreteCentralHandrails.add(simpleHandrailBlock.central);
          simpleConcreteCornerHandrails.add(simpleHandrailBlock.corner);
          simpleConcreteOuterHandrails.add(simpleHandrailBlock.outer);
          simpleConcreteStairHandrails.add(simpleHandrailBlock.stair);
        } else if (MishangUtils.isTerracotta(((SimpleHandrailBlock) block).baseBlock)) {
          simpleTerracottaNormalHandrails.add(simpleHandrailBlock);
          simpleTerracottaCentralHandrails.add(simpleHandrailBlock.central);
          simpleTerracottaCornerHandrails.add(simpleHandrailBlock.corner);
          simpleTerracottaOuterHandrails.add(simpleHandrailBlock.outer);
          simpleTerracottaStairHandrails.add(simpleHandrailBlock.stair);
        } else if (MishangUtils.isWood(((SimpleHandrailBlock) block).baseBlock)) {
          simpleWoodenNormalHandrails.add(simpleHandrailBlock);
          simpleWoodenCentralHandrails.add(simpleHandrailBlock.central);
          simpleWoodenCornerHandrails.add(simpleHandrailBlock.corner);
          simpleWoodenOuterHandrails.add(simpleHandrailBlock.outer);
          simpleWoodenStairHandrails.add(simpleHandrailBlock.stair);
        } else {
          handrailItems.add(simpleHandrailBlock.asItem());
          normalHandrails.add(simpleHandrailBlock);
          centralHandrails.add(simpleHandrailBlock.central);
          cornerHandrails.add(simpleHandrailBlock.corner);
          outerHandrails.add(simpleHandrailBlock.outer);
          stairHandrails.add(simpleHandrailBlock.stair);
        }
      } else if (block instanceof GlassHandrailBlock glassHandrailBlock) {
        glassNormalHandrails.add(glassHandrailBlock);
        glassCentralHandrails.add(glassHandrailBlock.central());
        glassCornerHandrails.add(glassHandrailBlock.corner());
        glassOuterHandrails.add(glassHandrailBlock.outer());
        glassStairHandrails.add(glassHandrailBlock.stair());
        final Block[] blocks = glassHandrailBlock.selfAndVariants();
        final Block baseBlock = glassHandrailBlock.baseBlock();
        if (MishangUtils.isWood(baseBlock)) {
          axeMineable.add(blocks);
        } else {
          pickaxeMineable.add(blocks);
          if (baseBlock == Blocks.GOLD_BLOCK || baseBlock == Blocks.EMERALD_BLOCK || baseBlock == Blocks.DIAMOND_BLOCK) {
            needsIronTool.add(blocks);
          } else if (baseBlock == Blocks.OBSIDIAN || baseBlock == Blocks.CRYING_OBSIDIAN || baseBlock == Blocks.NETHERITE_BLOCK) {
            needsDiamondTool.add(blocks);
          } else if (baseBlock == Blocks.IRON_BLOCK || baseBlock == Blocks.LAPIS_BLOCK) {
            needsStoneTool.add(blocks);
          }
        }
      }
    });
    simpleStainedGlassHandrails
        .addTag(simpleStainedGlassNormalHandrails)
        .addTag(simpleStainedGlassCentralHandrails)
        .addTag(simpleStainedGlassCornerHandrails)
        .addTag(simpleStainedGlassOuterHandrails)
        .addTag(simpleStainedGlassStairHandrails);
    simpleConcreteHandrails
        .addTag(simpleConcreteNormalHandrails)
        .addTag(simpleConcreteCentralHandrails)
        .addTag(simpleConcreteCornerHandrails)
        .addTag(simpleConcreteOuterHandrails)
        .addTag(simpleConcreteStairHandrails);
    simpleTerracottaHandrails
        .addTag(simpleTerracottaNormalHandrails)
        .addTag(simpleTerracottaCentralHandrails)
        .addTag(simpleTerracottaCornerHandrails)
        .addTag(simpleTerracottaOuterHandrails)
        .addTag(simpleTerracottaStairHandrails);
    simpleWoodenHandrails
        .addTag(simpleWoodenNormalHandrails)
        .addTag(simpleWoodenCentralHandrails)
        .addTag(simpleWoodenCornerHandrails)
        .addTag(simpleWoodenOuterHandrails)
        .addTag(simpleWoodenStairHandrails);
    normalHandrails
        .addTag(glassNormalHandrails)
        .addTag(simpleStainedGlassNormalHandrails)
        .addTag(simpleTerracottaNormalHandrails)
        .addTag(simpleConcreteNormalHandrails)
        .addTag(simpleWoodenNormalHandrails);
    handrailItems
        .addTag(simpleStainedGlassHandrails.identifier)
        .addTag(simpleTerracottaHandrails.identifier)
        .addTag(simpleConcreteHandrails.identifier)
        .addTag(simpleWoodenHandrails.identifier);
    centralHandrails
        .addTag(glassCentralHandrails)
        .addTag(simpleStainedGlassCentralHandrails)
        .addTag(simpleTerracottaCentralHandrails)
        .addTag(simpleConcreteCentralHandrails)
        .addTag(simpleWoodenCentralHandrails);
    cornerHandrails
        .addTag(glassCornerHandrails)
        .addTag(simpleStainedGlassCornerHandrails)
        .addTag(simpleTerracottaCornerHandrails)
        .addTag(simpleConcreteCornerHandrails)
        .addTag(simpleWoodenCornerHandrails);
    outerHandrails
        .addTag(glassOuterHandrails)
        .addTag(simpleStainedGlassOuterHandrails)
        .addTag(simpleTerracottaOuterHandrails)
        .addTag(simpleConcreteOuterHandrails)
        .addTag(simpleWoodenOuterHandrails);
    stairHandrails
        .addTag(glassStairHandrails)
        .addTag(simpleStainedGlassStairHandrails)
        .addTag(simpleTerracottaStairHandrails)
        .addTag(simpleConcreteStairHandrails)
        .addTag(simpleWoodenStairHandrails);
    glassHandrails.addTag(glassNormalHandrails, glassCentralHandrails, glassCornerHandrails, glassOuterHandrails, glassStairHandrails);
    handrails
        .addTag(normalHandrails)
        .addTag(centralHandrails)
        .addTag(cornerHandrails)
        .addTag(outerHandrails)
        .addTag(stairHandrails);

    // mineable 部分
    registerTagBlockOnly(pickaxeMineable);
    registerTagBlockOnly(axeMineable);
    registerTagBlockOnly(needsDiamondTool);
    registerTagBlockOnly(needsIronTool);
    registerTagBlockOnly(needsStoneTool);

    // 道路部分
    registerTag(roadBlocks, roadSlabs, roadMarks);

    // 灯光部分
    whiteWallLights.addTag(whiteStripWallLights);
    yellowWallLights.addTag(yellowStripWallLights);
    cyanWallLights.addTag(cyanStripWallLights);
    orangeWallLights.addTag(orangeStripWallLights);
    greenWallLights.addTag(greenStripWallLights);
    pinkWallLights.addTag(pinkStripWallLights);

    registerTag(
        whiteWallLights, whiteStripWallLights, whiteCornerLights, whiteLightDecorations,
        yellowWallLights, yellowStripWallLights, yellowCornerLights, yellowLightDecorations,
        cyanWallLights, cyanStripWallLights, cyanCornerLights, cyanLightDecorations,
        orangeWallLights, orangeStripWallLights, orangeCornerLights, orangeLightDecorations,
        greenWallLights, greenStripWallLights, greenCornerLights, greenLightDecorations,
        pinkWallLights, pinkStripWallLights, pinkCornerLights, pinkLightDecorations,

        whiteColumnLights, yellowColumnLights, cyanColumnLights, orangeColumnLights, pinkColumnLights, greenColumnLights
    );
    registerTag(lightSlabs);
    registerTag(lightCovers);

    // 墙上的告示牌部分
    wallSigns
        .addTag(woodenWallSigns)
        .addTag(concreteWallSigns)
        .addTag(terracottaWallSigns);
    glowingWallSigns
        .addTag(glowingConcreteWallSigns)
        .addTag(glowingTerracottaWallSigns);
    fullWallSigns
        .addTag(fullConcreteWallSigns)
        .addTag(fullTerracottaWallSigns);

    registerTag(woodenWallSigns);
    registerTag(concreteWallSigns);
    registerTag(terracottaWallSigns);
    registerTag(wallSigns);
    registerTag(glowingConcreteWallSigns);
    registerTag(glowingTerracottaWallSigns);
    registerTag(glowingWallSigns);
    registerTag(fullConcreteWallSigns);
    registerTag(fullTerracottaWallSigns);
    registerTag(fullWallSigns);

    registerTagBlockOnly(handrails);
    registerTagBlockOnly(normalHandrails);
    PACK.addTag(handrailItems);
    registerTagBlockOnly(centralHandrails);
    registerTagBlockOnly(cornerHandrails);
    registerTagBlockOnly(outerHandrails);
    registerTagBlockOnly(stairHandrails);
    registerTagBlockOnly(simpleConcreteHandrails);
    registerTagBlockOnly(simpleConcreteNormalHandrails);
    PACK.addTag(new Identifier("mishanguc", "items/simple_concrete_handrails"), simpleConcreteNormalHandrails);
    registerTagBlockOnly(simpleConcreteCentralHandrails);
    registerTagBlockOnly(simpleConcreteCornerHandrails);
    registerTagBlockOnly(simpleConcreteOuterHandrails);
    registerTagBlockOnly(simpleConcreteStairHandrails);
    registerTagBlockOnly(simpleTerracottaHandrails);
    registerTagBlockOnly(simpleTerracottaNormalHandrails);
    PACK.addTag(TagFactory.ITEM.create(new Identifier("mishanguc", "simple_terracotta_handrails")), simpleTerracottaNormalHandrails, "items");
    registerTagBlockOnly(simpleTerracottaCentralHandrails);
    registerTagBlockOnly(simpleTerracottaCornerHandrails);
    registerTagBlockOnly(simpleTerracottaOuterHandrails);
    registerTagBlockOnly(simpleTerracottaStairHandrails);
    registerTagBlockOnly(simpleStainedGlassHandrails);
    registerTagBlockOnly(simpleStainedGlassNormalHandrails);
    PACK.addTag(TagFactory.ITEM.create(new Identifier("mishanguc", "simple_stained_glass_handrails")), simpleStainedGlassNormalHandrails, "items");
    registerTagBlockOnly(simpleStainedGlassCentralHandrails);
    registerTagBlockOnly(simpleStainedGlassCornerHandrails);
    registerTagBlockOnly(simpleStainedGlassOuterHandrails);
    registerTagBlockOnly(simpleStainedGlassStairHandrails);
    registerTagBlockOnly(simpleWoodenHandrails);
    registerTagBlockOnly(simpleWoodenNormalHandrails);
    PACK.addTag(TagFactory.ITEM.create(new Identifier("mishanguc", "simple_wooden_handrails")), simpleWoodenNormalHandrails, "items");
    registerTagBlockOnly(simpleWoodenCentralHandrails);
    registerTagBlockOnly(simpleWoodenCornerHandrails);
    registerTagBlockOnly(simpleWoodenOuterHandrails);
    registerTagBlockOnly(simpleWoodenStairHandrails);
    registerTagBlockOnly(glassHandrails);
    registerTagBlockOnly(glassNormalHandrails);
    PACK.addTag(TagFactory.ITEM.create(new Identifier("mishanguc", "glass_handrails")), glassNormalHandrails, "items");
    registerTagBlockOnly(glassCentralHandrails);
    registerTagBlockOnly(glassCornerHandrails);
    registerTagBlockOnly(glassOuterHandrails);
    registerTagBlockOnly(glassStairHandrails);

    // 悬挂的告示牌部分
    hungSigns
        .addTag(woodenHungSigns)
        .addTag(concreteHungSigns)
        .addTag(terracottaHungSigns);
    glowingHungSigns
        .addTag(glowingConcreteHungSigns)
        .addTag(glowingTerracottaHungSigns);
    hungSignBars
        .addTag(woodenHungSignBars)
        .addTag(concreteHungSignBars)
        .addTag(terracottaHungSignBars);
    standingSigns.addTag(woodenStandingSigns, concreteStandingSigns, terracottaStandingSigns);
    glowingStandingSigns.addTag(glowingConcreteStandingSigns, glowingTerracottaStandingSigns);
    registerTag(woodenHungSigns);
    registerTag(concreteHungSigns);
    registerTag(terracottaHungSigns);
    registerTag(hungSigns);
    registerTag(glowingConcreteHungSigns);
    registerTag(glowingTerracottaHungSigns);
    registerTag(glowingHungSigns);
    registerTag(woodenHungSignBars);
    registerTag(concreteHungSignBars);
    registerTag(terracottaHungSignBars);
    registerTag(hungSignBars);
    registerTag(woodenStandingSigns, concreteStandingSigns, terracottaStandingSigns, glowingConcreteStandingSigns, glowingTerracottaStandingSigns, standingSigns, glowingStandingSigns);

    // 染色方块部分
    final IdentifiedTagBuilder<Block> colored = blockTag("colored");
    MishangUtils.blocks().stream().filter(Predicates.instanceOf(ColoredBlock.class)).forEach(colored::add);
    registerTag(colored);
  }

  private static void registerTag(IdentifiedTagBuilder<Block> blockTag) {
    PACK.addTag(blockTag);
    PACK.addTag(blockTag.identifier.brrp_prefixed("items/"), blockTag);
  }

  @SafeVarargs
  private static void registerTag(IdentifiedTagBuilder<Block>... tags) {
    for (IdentifiedTagBuilder<Block> tag : tags) {
      registerTag(tag);
    }
  }

  private static void registerTagBlockOnly(IdentifiedTagBuilder<Block> tag) {
    PACK.addTag(tag.identifier.brrp_prefixed("blocks/"), tag);
  }

  private static void registerTagItemOnly(IdentifiedTagBuilder<Block> tag) {
    PACK.addTag(tag.identifier.brrp_prefixed("items/"), tag);
  }


  /**
   * 为运行时资源包生成资源。在开发环境中，每次加载资源就会重新生成一次。在非开发环境中，游戏开始时生成一次，此后不再生成。
   */
  private static void generateResources(boolean includesClient, boolean includesServer) {
    if (FabricLoader.getInstance().getEnvironmentType() != EnvType.CLIENT) {
      Validate.isTrue(!includesClient, "The parameter 'includesClient' cannot be true when in dedicated server!", ArrayUtils.EMPTY_OBJECT_ARRAY);
    }
    if (includesClient) PACK.clearResources(ResourceType.CLIENT_RESOURCES);
    if (includesServer) PACK.clearResources(ResourceType.SERVER_DATA);

    for (Block block : MishangUtils.blocks()) {
      if (block instanceof final BlockResourceGenerator generator) {
        if (includesClient) {
          generator.writeBlockModel(PACK);
          generator.writeBlockStates(PACK);
          generator.writeItemModel(PACK);
        }
        if (includesServer) {
          generator.writeLootTable(PACK);
          generator.writeRecipes(PACK);
        }
      }
    }
    for (Item item : MishangUtils.items()) {
      if (item instanceof final ItemResourceGenerator generator) {

        if (includesClient) {
          generator.writeItemModel(PACK);
        }

        if (includesServer) {
          generator.writeRecipes(PACK);
        }
      }
    }

    // 服务器部分
    if (includesServer) {
      addTags();
      addRecipes();
    }

  }

  /**
   * 为本模组内的物品添加配方。该方法只会生成部分配方，还有很多配方是在 {@link ItemResourceGenerator#writeRecipes(RuntimeResourcePack)} 的子方法中定义的。
   */
  private static void addRecipes() {
    addRecipesForWallSigns();
    addRecipesForLights();
  }

  private static void addRecipesForWallSigns() {
    // 隐形告示牌是合成其他告示牌的基础。
    { // invisible wall sign
      final ShapedRecipeJsonFactory recipe = ShapedRecipeJsonFactory.create(WallSignBlocks.INVISIBLE_WALL_SIGN, 6)
          .pattern(".#.").pattern("#o#").pattern(".#.")
          .input('.', Items.IRON_NUGGET)
          .input('#', Items.FEATHER)
          .input('o', Items.GOLD_INGOT)
          .criterion("has_iron_nugget", RecipesProvider.conditionsFromItem(Items.IRON_NUGGET))
          .criterion("has_feather", RecipesProvider.conditionsFromItem(Items.FEATHER))
          .criterion("has_gold_ingot", RecipesProvider.conditionsFromItem(Items.GOLD_INGOT));
      final Identifier id = BRRPUtils.getItemId(WallSignBlocks.INVISIBLE_WALL_SIGN);
      PACK.addRecipeAndAdvancement(id, recipe);  // recipeCategory should be "signs"
    }
    { // invisible glowing wall sign
      final ShapedRecipeJsonFactory recipe = ShapedRecipeJsonFactory.create(WallSignBlocks.INVISIBLE_GLOWING_WALL_SIGN, 3)
          .pattern("---").pattern("###")
          .input('-', Items.GLOWSTONE_DUST)
          .input('#', WallSignBlocks.INVISIBLE_WALL_SIGN)
          .criterion("has_base_block", RecipesProvider.conditionsFromItem(WallSignBlocks.INVISIBLE_WALL_SIGN));
      final Identifier id = BRRPUtils.getItemId(WallSignBlocks.INVISIBLE_GLOWING_WALL_SIGN);
      PACK.addRecipeAndAdvancement(id, recipe);  // recipeCategory should be "signs"
    }
  }

  private static void addRecipesForLights() {
    // 先是三个完整方块的合成表。
    { // white light
      final ShapedRecipeJsonFactory recipe = ShapedRecipeJsonFactory.create(LightBlocks.WHITE_LIGHT, 8)
          .pattern("*#*").pattern("#C#").pattern("*#*")
          .input('*', Items.WHITE_DYE)
          .input('#', Items.GLOWSTONE)
          .input('C', Items.WHITE_CONCRETE)
          .criterion("has_dye", RecipesProvider.conditionsFromItem(Items.WHITE_DYE))
          .criterion("has_glowstone", RecipesProvider.conditionsFromItem(Items.GLOWSTONE))
          .criterion("has_concrete", RecipesProvider.conditionsFromItem(Items.WHITE_CONCRETE));
      final Identifier id = BRRPUtils.getItemId(LightBlocks.WHITE_LIGHT);
      PACK.addRecipeAndAdvancement(id, recipe);
    }
    { // yellow light
      final ShapedRecipeJsonFactory recipe = ShapedRecipeJsonFactory.create(LightBlocks.YELLOW_LIGHT, 8)
          .pattern("*#*").pattern("#C#").pattern("*#*")
          .input('*', Items.YELLOW_DYE)
          .input('#', Items.GLOWSTONE)
          .input('C', Items.YELLOW_CONCRETE)
          .criterion("has_dye", RecipesProvider.conditionsFromItem(Items.YELLOW_DYE))
          .criterion("has_glowstone", RecipesProvider.conditionsFromItem(Items.GLOWSTONE))
          .criterion("has_concrete", RecipesProvider.conditionsFromItem(Items.YELLOW_CONCRETE));
      final Identifier id = BRRPUtils.getItemId(LightBlocks.YELLOW_LIGHT);
      PACK.addRecipeAndAdvancement(id, recipe);
    }
    { // cyan light
      final ShapedRecipeJsonFactory recipe = ShapedRecipeJsonFactory.create(LightBlocks.CYAN_LIGHT, 8)
          .pattern("*#*").pattern("#C#").pattern("*#*")
          .input('*', Items.CYAN_DYE)
          .input('#', Items.GLOWSTONE)
          .input('C', Items.CYAN_CONCRETE)
          .criterion("has_dye", RecipesProvider.conditionsFromItem(Items.CYAN_DYE))
          .criterion("has_glowstone", RecipesProvider.conditionsFromItem(Items.GLOWSTONE))
          .criterion("has_concrete", RecipesProvider.conditionsFromItem(Items.CYAN_CONCRETE));
      final Identifier id = BRRPUtils.getItemId(LightBlocks.CYAN_LIGHT);
      PACK.addRecipeAndAdvancement(id, recipe);  // the recipe category should be "light"
    }


    // 白色灯光

    addShapelessRecipeForLight(LightBlocks.WHITE_SMALL_WALL_LIGHT, 1, LightBlocks.WHITE_SMALL_WALL_LIGHT_TUBE, Blocks.GRAY_CONCRETE);
    addStonecuttingRecipeForLight(LightBlocks.WHITE_LIGHT, LightBlocks.WHITE_SMALL_WALL_LIGHT_TUBE, 16);
    addShapelessRecipeForLight(LightBlocks.WHITE_LARGE_WALL_LIGHT, 1, LightBlocks.WHITE_LARGE_WALL_LIGHT_TUBE, Blocks.GRAY_CONCRETE);
    addStonecuttingRecipeForLight(LightBlocks.WHITE_LIGHT, LightBlocks.WHITE_LARGE_WALL_LIGHT_TUBE, 12);
    addShapelessRecipeForLight(LightBlocks.WHITE_THIN_STRIP_WALL_LIGHT, 1, LightBlocks.WHITE_THIN_STRIP_WALL_LIGHT_TUBE, Blocks.GRAY_CONCRETE);
    addStonecuttingRecipeForLight(LightBlocks.WHITE_LIGHT, LightBlocks.WHITE_THIN_STRIP_WALL_LIGHT_TUBE, 12);
    addShapelessRecipeForLight(LightBlocks.WHITE_THICK_STRIP_WALL_LIGHT, 1, LightBlocks.WHITE_THICK_STRIP_WALL_LIGHT_TUBE, Blocks.GRAY_CONCRETE);
    addStonecuttingRecipeForLight(LightBlocks.WHITE_LIGHT, LightBlocks.WHITE_THICK_STRIP_WALL_LIGHT_TUBE, 8);
    addStonecuttingRecipeForLight(LightBlocks.WHITE_LIGHT, LightBlocks.WHITE_DOUBLE_STRIP_WALL_LIGHT_TUBE, 10);
    // 角落灯管方块由两个普通灯管方块合成。
    addShapelessRecipeForLight(LightBlocks.WHITE_THIN_STRIP_CORNER_LIGHT_TUBE, 1, LightBlocks.WHITE_THIN_STRIP_WALL_LIGHT_TUBE, LightBlocks.WHITE_THIN_STRIP_WALL_LIGHT_TUBE);
    addShapelessRecipeForLight(LightBlocks.WHITE_THICK_STRIP_CORNER_LIGHT_TUBE, 1, LightBlocks.WHITE_THICK_STRIP_WALL_LIGHT_TUBE, LightBlocks.WHITE_THICK_STRIP_WALL_LIGHT_TUBE);
    addShapelessRecipeForLight(LightBlocks.WHITE_DOUBLE_STRIP_CORNER_LIGHT_TUBE, 1, LightBlocks.WHITE_DOUBLE_STRIP_WALL_LIGHT_TUBE, LightBlocks.WHITE_DOUBLE_STRIP_WALL_LIGHT_TUBE);
    // 灯光装饰方块，一律由切石形成。
    addStonecuttingRecipeForLight(LightBlocks.WHITE_LIGHT, LightBlocks.WHITE_WALL_LIGHT_SIMPLE_DECORATION, 6);
    addStonecuttingRecipeForLight(LightBlocks.WHITE_LIGHT, LightBlocks.WHITE_WALL_LIGHT_POINT_DECORATION, 6);
    addStonecuttingRecipeForLight(LightBlocks.WHITE_LIGHT, LightBlocks.WHITE_WALL_LIGHT_RHOMBUS_DECORATION, 6);
    addStonecuttingRecipeForLight(LightBlocks.WHITE_LIGHT, LightBlocks.WHITE_WALL_LIGHT_HASH_DECORATION, 6);
    addStonecuttingRecipeForLight(LightBlocks.WHITE_LIGHT, LightBlocks.WHITE_WALL_LIGHT_ROUND_DECORATION, 4);

    // 黄色灯光

    addShapelessRecipeForLight(LightBlocks.YELLOW_SMALL_WALL_LIGHT, 1, LightBlocks.YELLOW_SMALL_WALL_LIGHT_TUBE, Blocks.GRAY_CONCRETE);
    addStonecuttingRecipeForLight(LightBlocks.YELLOW_LIGHT, LightBlocks.YELLOW_SMALL_WALL_LIGHT_TUBE, 16);
    addShapelessRecipeForLight(LightBlocks.YELLOW_LARGE_WALL_LIGHT, 1, LightBlocks.YELLOW_LARGE_WALL_LIGHT_TUBE, Blocks.GRAY_CONCRETE);
    addStonecuttingRecipeForLight(LightBlocks.YELLOW_LIGHT, LightBlocks.YELLOW_LARGE_WALL_LIGHT_TUBE, 12);
    addShapelessRecipeForLight(LightBlocks.YELLOW_THIN_STRIP_WALL_LIGHT, 1, LightBlocks.YELLOW_THIN_STRIP_WALL_LIGHT_TUBE, Blocks.GRAY_CONCRETE);
    addStonecuttingRecipeForLight(LightBlocks.YELLOW_LIGHT, LightBlocks.YELLOW_THIN_STRIP_WALL_LIGHT_TUBE, 12);
    addShapelessRecipeForLight(LightBlocks.YELLOW_THICK_STRIP_WALL_LIGHT, 1, LightBlocks.YELLOW_THICK_STRIP_WALL_LIGHT_TUBE, Blocks.GRAY_CONCRETE);
    addStonecuttingRecipeForLight(LightBlocks.YELLOW_LIGHT, LightBlocks.YELLOW_THICK_STRIP_WALL_LIGHT_TUBE, 8);
    addStonecuttingRecipeForLight(LightBlocks.YELLOW_LIGHT, LightBlocks.YELLOW_DOUBLE_STRIP_WALL_LIGHT_TUBE, 10);
    // 角落灯管方块由两个普通灯管方块合成。
    addShapelessRecipeForLight(LightBlocks.YELLOW_THIN_STRIP_CORNER_LIGHT_TUBE, 1, LightBlocks.YELLOW_THIN_STRIP_WALL_LIGHT_TUBE, LightBlocks.YELLOW_THIN_STRIP_WALL_LIGHT_TUBE);
    addShapelessRecipeForLight(LightBlocks.YELLOW_THICK_STRIP_CORNER_LIGHT_TUBE, 1, LightBlocks.YELLOW_THICK_STRIP_WALL_LIGHT_TUBE, LightBlocks.YELLOW_THICK_STRIP_WALL_LIGHT_TUBE);
    addShapelessRecipeForLight(LightBlocks.YELLOW_DOUBLE_STRIP_CORNER_LIGHT_TUBE, 1, LightBlocks.YELLOW_DOUBLE_STRIP_WALL_LIGHT_TUBE, LightBlocks.YELLOW_DOUBLE_STRIP_WALL_LIGHT_TUBE);
    // 灯光装饰方块，一律由切石形成。
    addStonecuttingRecipeForLight(LightBlocks.YELLOW_LIGHT, LightBlocks.YELLOW_WALL_LIGHT_SIMPLE_DECORATION, 6);
    addStonecuttingRecipeForLight(LightBlocks.YELLOW_LIGHT, LightBlocks.YELLOW_WALL_LIGHT_POINT_DECORATION, 6);
    addStonecuttingRecipeForLight(LightBlocks.YELLOW_LIGHT, LightBlocks.YELLOW_WALL_LIGHT_RHOMBUS_DECORATION, 6);
    addStonecuttingRecipeForLight(LightBlocks.YELLOW_LIGHT, LightBlocks.YELLOW_WALL_LIGHT_HASH_DECORATION, 6);

    // 青色

    addShapelessRecipeForLight(LightBlocks.CYAN_SMALL_WALL_LIGHT, 1, LightBlocks.CYAN_SMALL_WALL_LIGHT_TUBE, Blocks.GRAY_CONCRETE);
    addStonecuttingRecipeForLight(LightBlocks.CYAN_LIGHT, LightBlocks.CYAN_SMALL_WALL_LIGHT_TUBE, 16);
    addShapelessRecipeForLight(LightBlocks.CYAN_LARGE_WALL_LIGHT, 1, LightBlocks.CYAN_LARGE_WALL_LIGHT_TUBE, Blocks.GRAY_CONCRETE);
    addStonecuttingRecipeForLight(LightBlocks.CYAN_LIGHT, LightBlocks.CYAN_LARGE_WALL_LIGHT_TUBE, 12);
    addShapelessRecipeForLight(LightBlocks.CYAN_THIN_STRIP_WALL_LIGHT, 1, LightBlocks.CYAN_THIN_STRIP_WALL_LIGHT_TUBE, Blocks.GRAY_CONCRETE);
    addStonecuttingRecipeForLight(LightBlocks.CYAN_LIGHT, LightBlocks.CYAN_THIN_STRIP_WALL_LIGHT_TUBE, 12);
    addShapelessRecipeForLight(LightBlocks.CYAN_THICK_STRIP_WALL_LIGHT, 1, LightBlocks.CYAN_THICK_STRIP_WALL_LIGHT_TUBE, Blocks.GRAY_CONCRETE);
    addStonecuttingRecipeForLight(LightBlocks.CYAN_LIGHT, LightBlocks.CYAN_THICK_STRIP_WALL_LIGHT_TUBE, 8);
    addStonecuttingRecipeForLight(LightBlocks.CYAN_LIGHT, LightBlocks.CYAN_DOUBLE_STRIP_WALL_LIGHT_TUBE, 10);
    // 角落灯管方块由两个普通灯管方块合成。
    addShapelessRecipeForLight(LightBlocks.CYAN_THIN_STRIP_CORNER_LIGHT_TUBE, 1, LightBlocks.CYAN_THIN_STRIP_WALL_LIGHT_TUBE, LightBlocks.CYAN_THIN_STRIP_WALL_LIGHT_TUBE);
    addShapelessRecipeForLight(LightBlocks.CYAN_THICK_STRIP_CORNER_LIGHT_TUBE, 1, LightBlocks.CYAN_THICK_STRIP_WALL_LIGHT_TUBE, LightBlocks.CYAN_THICK_STRIP_WALL_LIGHT_TUBE);
    addShapelessRecipeForLight(LightBlocks.CYAN_DOUBLE_STRIP_CORNER_LIGHT_TUBE, 1, LightBlocks.CYAN_DOUBLE_STRIP_WALL_LIGHT_TUBE, LightBlocks.CYAN_DOUBLE_STRIP_WALL_LIGHT_TUBE);
    // 灯光装饰方块，一律由切石形成。
    addStonecuttingRecipeForLight(LightBlocks.CYAN_LIGHT, LightBlocks.CYAN_WALL_LIGHT_SIMPLE_DECORATION, 6);
    addStonecuttingRecipeForLight(LightBlocks.CYAN_LIGHT, LightBlocks.CYAN_WALL_LIGHT_POINT_DECORATION, 6);
    addStonecuttingRecipeForLight(LightBlocks.CYAN_LIGHT, LightBlocks.CYAN_WALL_LIGHT_RHOMBUS_DECORATION, 6);
    addStonecuttingRecipeForLight(LightBlocks.CYAN_LIGHT, LightBlocks.CYAN_WALL_LIGHT_HASH_DECORATION, 6);
  }

  private static void addStonecuttingRecipeForLight(ItemConvertible ingredient, ItemConvertible result, int count) {
    PACK.addRecipeAndAdvancement(BRRPUtils.getRecipeId(result), SingleItemRecipeJsonFactory.createStonecutting(Ingredient.ofItems(ingredient), result, count).criterionFromItem(ingredient));
  }

  private static void addShapelessRecipeForLight(ItemConvertible result, int count, ItemConvertible... ingredients) {
    final ShapelessRecipeJsonFactory recipe = ShapelessRecipeJsonFactory.create(result, count).input(Ingredient.ofItems(ingredients));
    for (ItemConvertible ingredient : ImmutableSet.copyOf(ingredients)) {
      recipe.criterion("has_" + BRRPUtils.getItemId(ingredient).getPath(), RecipesProvider.conditionsFromItem(ingredient));
    }
    final Identifier id = BRRPUtils.getItemId(result);
    PACK.addRecipeAndAdvancement(id, recipe);
  }

  @Override
  public void onInitialize() {
    generateResources(FabricLoader.getInstance().getEnvironmentType() == EnvType.CLIENT, true);
    PACK.setSidedRegenerationCallback(ResourceType.CLIENT_RESOURCES, () -> generateResources(true, false));
    PACK.setSidedRegenerationCallback(ResourceType.SERVER_DATA, () -> generateResources(false, true));
    SidedRRPCallback.BEFORE_VANILLA.register((resourceType, builder) -> builder.add(PACK));
  }
}
