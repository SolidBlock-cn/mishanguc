package pers.solid.mishang.uc.arrp;

import com.google.common.collect.ImmutableSet;
import com.google.errorprone.annotations.CanIgnoreReturnValue;
import net.devtech.arrp.api.RRPCallbackConditional;
import net.devtech.arrp.api.RRPPreGenEntrypoint;
import net.devtech.arrp.api.RuntimeResourcePack;
import net.devtech.arrp.generator.BlockResourceGenerator;
import net.devtech.arrp.generator.ItemResourceGenerator;
import net.devtech.arrp.generator.ResourceGeneratorHelper;
import net.devtech.arrp.json.models.JModel;
import net.devtech.arrp.json.models.JTextures;
import net.devtech.arrp.json.recipe.JShapedRecipe;
import net.devtech.arrp.json.recipe.JShapelessRecipe;
import net.devtech.arrp.json.recipe.JStonecuttingRecipe;
import net.devtech.arrp.json.tags.IdentifiedTag;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemConvertible;
import net.minecraft.item.Items;
import net.minecraft.resource.ResourceType;
import net.minecraft.util.Identifier;
import pers.solid.mishang.uc.MishangUtils;
import pers.solid.mishang.uc.Mishanguc;
import pers.solid.mishang.uc.annotations.RegisterIdentifier;
import pers.solid.mishang.uc.annotations.SimpleModel;
import pers.solid.mishang.uc.block.*;
import pers.solid.mishang.uc.blocks.*;
import pers.solid.mishang.uc.item.MishangucItems;

/**
 * @since 0.1.7 本类应当在 onInitialize 的入口点中执行，而非 pregen 中。
 */
public class ARRPMain implements RRPPreGenEntrypoint, ModInitializer {
  private static final RuntimeResourcePack PACK = RuntimeResourcePack.create(new Identifier("mishanguc", "pack"));

  private static Identifier blockIdentifier(String path) {
    return new Identifier("mishanguc", "block/" + path);
  }

  private static String blockString(String path) {
    return blockIdentifier(path).toString();
  }

  private static void writeBlockModelForCubeAll(BlockResourceGenerator block, String all) {
    PACK.addModel(
        new JModel("block/cube_all").textures(new FasterJTextures().varP("all", all)),
        block.getBlockModelId());
  }

  private static void writeBlockModelForSlabAll(BlockResourceGenerator block, String all) {
    PACK.addModel(
        new JModel("block/slab")
            .textures(
                new FasterJTextures().top(all).side(all).bottom(all)),
        block.getBlockModelId());
    PACK.addModel(
        new JModel("block/slab_top")
            .textures(
                new FasterJTextures().top(all).side(all).bottom(all)),
        block.getBlockModelId().brrp_append("_top"));
  }

  /**
   * 运行此方法需确保其楼梯名称正好为 path + "_slab"。
   */
  private static void writeBlockModelForCubeAllWithSlab(AbstractRoadBlock block, String all) {
    writeBlockModelForCubeAll(block, all);
    writeBlockModelForSlabAll(RoadSlabBlocks.BLOCK_TO_SLABS.get(block), all);
  }

  /**
   * 添加一个方块以及其台阶方块的方块模型。仅用于此模组。
   *
   * @param block    方块。必须是道路方块，且在 {@link RoadSlabBlocks#BLOCK_TO_SLABS} 中有对应的台阶版本。
   * @param parent   资源包的 parent。应当保证 parent、parent+"_slab" 和 parent+"_slab_top"都要存在。
   * @param textures 纹理变量。三个 parent 都应该使用相同的纹理。
   */
  private static void writeRoadBlockModelWithSlab(
      AbstractRoadBlock block, String parent, JTextures textures) {
    final Identifier id = block.getBlockModelId();
    final Identifier slabId = RoadSlabBlocks.BLOCK_TO_SLABS.get(block).getBlockModelId();
    writeRoadBlockModelWithSlab(parent, textures, id, slabId);
  }

  /**
   * 添加一个方块以及其台阶方块的方块模型，以及其对应的“mirrored”的方块模型。仅用于此模组。
   *
   * @param block    方块。必须是道路方块，且在 {@link RoadSlabBlocks#BLOCK_TO_SLABS} 中有对应的台阶版本。
   * @param parent   资源包的 parent。应当保证 parent、parent+"_slab" 和 parent+"_slab_top"都要存在。
   * @param textures 纹理变量。三个 parent 都应该使用相同的纹理。
   */
  private static void writeRoadBlockModelWithSlabWithMirrored(
      AbstractRoadBlock block, String parent, JTextures textures) {
    final Identifier id = block.getBlockModelId();
    final Identifier slabId = RoadSlabBlocks.BLOCK_TO_SLABS.get(block).getBlockModelId();
    writeRoadBlockModelWithSlab(parent, textures, id, slabId);
    writeRoadBlockModelWithSlab(parent + "_mirrored", textures, id.brrp_append("_mirrored"), slabId.brrp_append("_mirrored"));
  }

  /**
   * 添加一个方块以及其台阶方块的方块模型。仅用于此模组。
   *
   * @param parent   资源包的 parent。应当保证 parent、parent+"_slab" 和 parent+"_slab_top"都要存在。
   * @param textures 纹理变量。三个 parent 都应该使用相同的纹理。
   * @param id       道路方块的完整id。
   * @param slabId   该方块对应的台阶方块的完整id。
   */
  private static void writeRoadBlockModelWithSlab(String parent, JTextures textures, Identifier id, Identifier slabId) {
    PACK.addModel(new JModel(blockIdentifier(parent)).textures(textures), id);
    PACK.addModel(new JModel(BRRPHelper.slabOf(blockIdentifier(parent))).textures(textures), slabId);
    PACK.addModel(new JModel(BRRPHelper.slabOf(blockIdentifier(parent)) + "_top").textures(textures), slabId.brrp_append("_top"));
  }

  private static IdentifiedTag blockTag(String path) {
    return new IdentifiedTag("mishanguc", "blocks", path);
  }

  private static void addTags() {
    // 道路部分
    final IdentifiedTag roadBlocks = blockTag("road_blocks");
    final IdentifiedTag roadSlabs = blockTag("road_slabs");

    // 灯光部分
    final IdentifiedTag whiteStripWallLights = blockTag("white_strip_wall_lights");
    final IdentifiedTag whiteWallLights = blockTag("white_wall_lights");
    final IdentifiedTag whiteCornerLights = blockTag("white_corner_lights");
    final IdentifiedTag whiteLightDecorations = blockTag("white_light_decorations");
    final IdentifiedTag yellowStripWallLights = blockTag("yellow_strip_wall_lights");
    final IdentifiedTag yellowWallLights = blockTag("yellow_wall_lights");
    final IdentifiedTag yellowCornerLights = blockTag("yellow_corner_lights");
    final IdentifiedTag yellowLightDecorations = blockTag("yellow_light_decorations");
    final IdentifiedTag cyanStripWallLights = blockTag("cyan_strip_wall_lights");
    final IdentifiedTag cyanWallLights = blockTag("cyan_wall_lights");
    final IdentifiedTag cyanCornerLights = blockTag("cyan_corner_lights");
    final IdentifiedTag cyanLightDecorations = blockTag("cyan_light_decorations");

    // 墙上的告示牌部分
    final IdentifiedTag woodenWallSigns = blockTag("wooden_wall_signs");
    final IdentifiedTag concreteWallSigns = blockTag("concrete_wall_signs");
    final IdentifiedTag terracottaWallSigns = blockTag("terracotta_wall_signs");
    final IdentifiedTag wallSigns = blockTag("wall_signs");
    final IdentifiedTag glowingConcreteWallSigns = blockTag("glowing_concrete_wall_signs");
    final IdentifiedTag glowingTerracottaWallSigns = blockTag("glowing_terracotta_wall_signs");
    final IdentifiedTag glowingWallSigns = blockTag("glowing_wall_signs");
    final IdentifiedTag fullConcreteWallSigns = blockTag("full_concrete_wall_signs");
    final IdentifiedTag fullTerracottaWallSigns = blockTag("full_terracotta_wall_signs");
    final IdentifiedTag fullWallSigns = blockTag("full_wall_signs");

    // 悬挂的告示牌部分
    final IdentifiedTag concreteHungSigns = blockTag("concrete_hung_signs");
    final IdentifiedTag terracottaHungSigns = blockTag("terracotta_hung_signs");
    final IdentifiedTag hungSigns = blockTag("hung_signs");
    final IdentifiedTag glowingConcreteHungSigns = blockTag("glowing_concrete_hung_signs");
    final IdentifiedTag glowingTerracottaHungSigns = blockTag("glowing_terracotta_hung_signs");
    final IdentifiedTag glowingHungSigns = blockTag("glowing_hung_signs");

    // 悬挂的告示牌杆部分
    final IdentifiedTag concreteHungSignBars = blockTag("concrete_hung_sign_bars");
    final IdentifiedTag terracottaHungSignBars = blockTag("terracotta_hung_sign_bars");
    final IdentifiedTag hungSignBars = blockTag("hung_sign_bars");

    // 栏杆部分
    final IdentifiedTag handrails = blockTag("handrails");
    final IdentifiedTag handrailItems = new IdentifiedTag("mishanguc", "items", "handrails");
    final IdentifiedTag normalHandrails = blockTag("normal_handrails");
    final IdentifiedTag centralHandrails = blockTag("central_handrails");
    final IdentifiedTag cornerHandrails = blockTag("corner_handrails");
    final IdentifiedTag outerHandrails = blockTag("outer_handrails");
    final IdentifiedTag stairHandrails = blockTag("stair_handrails");

    // 混凝土栏杆部分
    final IdentifiedTag simpleConcreteHandrails = blockTag("simple_concrete_handrails");
    final IdentifiedTag simpleConcreteNormalHandrails = blockTag("simple_concrete_normal_handrails");
    final IdentifiedTag simpleConcreteCentralHandrails = blockTag("simple_concrete_central_handrails");
    final IdentifiedTag simpleConcreteCornerHandrails = blockTag("simple_concrete_corner_handrails");
    final IdentifiedTag simpleConcreteOuterHandrails = blockTag("simple_concrete_outer_handrails");
    final IdentifiedTag simpleConcreteStairHandrails = blockTag("simple_concrete_stair_handrails");

    // 陶瓦栏杆部分
    final IdentifiedTag simpleTerracottaHandrails = blockTag("simple_terracotta_handrails");
    final IdentifiedTag simpleTerracottaNormalHandrails = blockTag("simple_terracotta_normal_handrails");
    final IdentifiedTag simpleTerracottaCentralHandrails = blockTag("simple_terracotta_central_handrails");
    final IdentifiedTag simpleTerracottaCornerHandrails = blockTag("simple_terracotta_corner_handrails");
    final IdentifiedTag simpleTerracottaOuterHandrails = blockTag("simple_terracotta_outer_handrails");
    final IdentifiedTag simpleTerracottaStairHandrails = blockTag("simple_terracotta_stair_handrails");

    // 染色玻璃栏杆部分
    final IdentifiedTag simpleStainedGlassHandrails = blockTag("simple_stained_glass_handrails");
    final IdentifiedTag simpleStainedGlassNormalHandrails = blockTag("simple_stained_glass_normal_handrails");
    final IdentifiedTag simpleStainedGlassCentralHandrails = blockTag("simple_stained_glass_central_handrails");
    final IdentifiedTag simpleStainedGlassCornerHandrails = blockTag("simple_stained_glass_corner_handrails");
    final IdentifiedTag simpleStainedGlassOuterHandrails = blockTag("simple_stained_glass_outer_handrails");
    final IdentifiedTag simpleStainedGlassStairHandrails = blockTag("simple_stained_glass_stair_handrails");


    // 扶手部分，预留

    // 道路部分
    MishangUtils.instanceStream(RoadBlocks.class, Block.class).forEach(
        block -> {
          if (block instanceof AbstractRoadBlock) {
            roadBlocks.addBlock(block);
          }
        }
    );
    MishangUtils.instanceStream(RoadSlabBlocks.class, Block.class).forEach(
        block -> {
          if (block instanceof AbstractRoadSlabBlock) {
            roadSlabs.addBlock(block);
          }
        }
    );

    // 灯光部分
    MishangUtils.instanceStream(LightBlocks.class, Block.class).forEach(
        block -> {
          if (block instanceof StripWallLightBlock) {
            switch (((StripWallLightBlock) block).lightColor) {
              case "white" -> whiteStripWallLights.addBlock(block);
              case "yellow" -> yellowStripWallLights.addBlock(block);
              case "cyan" -> cyanStripWallLights.addBlock(block);
            }
          } else if (block instanceof AutoConnectWallLightBlock) {
            switch (((AutoConnectWallLightBlock) block).lightColor) {
              case "white" -> whiteLightDecorations.addBlock(block);
              case "yellow" -> yellowLightDecorations.addBlock(block);
              case "cyan" -> cyanLightDecorations.addBlock(block);
            }
          } else if (block instanceof WallLightBlock) {
            switch (((WallLightBlock) block).lightColor) {
              case "white" -> whiteWallLights.addBlock(block);
              case "yellow" -> yellowWallLights.addBlock(block);
              case "cyan" -> cyanWallLights.addBlock(block);
            }
          } else if (block instanceof CornerLightBlock) {
            switch (((CornerLightBlock) block).lightColor) {
              case "white" -> whiteCornerLights.addBlock(block);
              case "yellow" -> yellowCornerLights.addBlock(block);
              case "cyan" -> cyanCornerLights.addBlock(block);
            }
          }
        }
    );

    // 悬挂的告示牌部分
    MishangUtils.instanceStream(HungSignBlocks.class, Block.class).forEach(block -> {
      if (block instanceof GlowingHungSignBlock) {
        if (HungSignBlocks.GLOWING_CONCRETE_HUNG_SIGNS.containsValue(block)) {
          glowingConcreteHungSigns.addBlock(block);
        } else if (HungSignBlocks.GLOWING_TERRACOTTA_HUNG_SIGNS.containsValue(block)) {
          glowingTerracottaHungSigns.addBlock(block);
        } else {
          glowingHungSigns.addBlock(block);
        }
      } else if (block instanceof HungSignBlock) {
        if (HungSignBlocks.CONCRETE_HUNG_SIGNS.containsValue(block)) {
          concreteHungSigns.addBlock(block);
        } else if (HungSignBlocks.TERRACOTTA_HUNG_SIGNS.containsValue(block)) {
          terracottaHungSigns.addBlock(block);
        } else {
          hungSigns.addBlock(block);
        }
      } else if (block instanceof HungSignBarBlock) {
        if (HungSignBlocks.CONCRETE_HUNG_SIGN_BARS.containsValue(block)) {
          concreteHungSignBars.addBlock(block);
        } else if (HungSignBlocks.TERRACOTTA_HUNG_SIGN_BARS.containsValue(block)) {
          terracottaHungSignBars.addBlock(block);
        } else {
          hungSignBars.addBlock(block);
        }
      }
    });

    // 墙上的告示牌部分
    MishangUtils.instanceStream(WallSignBlocks.class, Block.class).forEach(block -> {
      if (block instanceof GlowingWallSignBlock) {
        if (WallSignBlocks.GLOWING_CONCRETE_WALL_SIGNS.containsValue(block)) {
          glowingConcreteWallSigns.addBlock(block);
        } else if (WallSignBlocks.GLOWING_TERRACOTTA_WALL_SIGNS.containsValue(block)) {
          glowingTerracottaWallSigns.addBlock(block);
        } else {
          glowingWallSigns.addBlock(block);
        }
      } else if (block instanceof FullWallSignBlock) {
        if (WallSignBlocks.FULL_CONCRETE_WALL_SIGNS.containsValue(block)) {
          fullConcreteWallSigns.addBlock(block);
        } else if (WallSignBlocks.FULL_TERRACOTTA_WALL_SIGNS.containsValue(block)) {
          fullTerracottaWallSigns.addBlock(block);
        } else {
          fullWallSigns.addBlock(block);
        }
      } else if (block instanceof WallSignBlock) {
        if (WallSignBlocks.CONCRETE_WALL_SIGNS.containsValue(block)) {
          concreteWallSigns.addBlock(block);
        } else if (WallSignBlocks.TERRACOTTA_WALL_SIGNS.containsValue(block)) {
          terracottaWallSigns.addBlock(block);
        } else {
          wallSigns.addBlock(block);
        }
      }
    });

    // 栏杆部分
    MishangUtils.instanceStream(HandrailBlocks.class, Block.class).forEach(block -> {
      if (block instanceof SimpleHandrailBlock simpleHandrailBlock) {
        if (HandrailBlocks.SIMPLE_STAINED_GLASS_HANDRAILS.containsValue(block)) {
          simpleStainedGlassNormalHandrails.addBlock(simpleHandrailBlock);
          simpleStainedGlassCentralHandrails.addBlock(simpleHandrailBlock.central);
          simpleStainedGlassCornerHandrails.addBlock(simpleHandrailBlock.corner);
          simpleStainedGlassOuterHandrails.addBlock(simpleHandrailBlock.outer);
          simpleStainedGlassStairHandrails.addBlock(simpleHandrailBlock.stair);
        } else if (HandrailBlocks.SIMPLE_CONCRETE_HANDRAILS.containsValue(block)) {
          simpleConcreteNormalHandrails.addBlock(simpleHandrailBlock);
          simpleConcreteCentralHandrails.addBlock(simpleHandrailBlock.central);
          simpleConcreteCornerHandrails.addBlock(simpleHandrailBlock.corner);
          simpleConcreteOuterHandrails.addBlock(simpleHandrailBlock.outer);
          simpleConcreteStairHandrails.addBlock(simpleHandrailBlock.stair);
        } else if (HandrailBlocks.SIMPLE_TERRACOTTA_HANDRAILS.containsValue(block)) {
          simpleTerracottaNormalHandrails.addBlock(simpleHandrailBlock);
          simpleTerracottaCentralHandrails.addBlock(simpleHandrailBlock.central);
          simpleTerracottaCornerHandrails.addBlock(simpleHandrailBlock.corner);
          simpleTerracottaOuterHandrails.addBlock(simpleHandrailBlock.outer);
          simpleTerracottaStairHandrails.addBlock(simpleHandrailBlock.stair);
        } else {
          handrailItems.addBlock(simpleHandrailBlock);
          normalHandrails.addBlock(simpleHandrailBlock);
          centralHandrails.addBlock(simpleHandrailBlock.central);
          cornerHandrails.addBlock(simpleHandrailBlock.corner);
          outerHandrails.addBlock(simpleHandrailBlock.outer);
          stairHandrails.addBlock(simpleHandrailBlock.stair);
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
    normalHandrails
        .addTag(simpleStainedGlassNormalHandrails)
        .addTag(simpleTerracottaNormalHandrails)
        .addTag(simpleConcreteNormalHandrails);
    handrailItems
        .addTag(simpleStainedGlassHandrails)
        .addTag(simpleTerracottaHandrails)
        .addTag(simpleConcreteHandrails);
    centralHandrails
        .addTag(simpleStainedGlassCentralHandrails)
        .addTag(simpleTerracottaCentralHandrails)
        .addTag(simpleConcreteCentralHandrails);
    cornerHandrails
        .addTag(simpleStainedGlassCornerHandrails)
        .addTag(simpleTerracottaCornerHandrails)
        .addTag(simpleConcreteCornerHandrails);
    outerHandrails
        .addTag(simpleStainedGlassOuterHandrails)
        .addTag(simpleTerracottaOuterHandrails)
        .addTag(simpleConcreteOuterHandrails);
    stairHandrails
        .addTag(simpleStainedGlassStairHandrails)
        .addTag(simpleTerracottaStairHandrails)
        .addTag(simpleConcreteStairHandrails);
    handrails
        .addTag(normalHandrails)
        .addTag(centralHandrails)
        .addTag(cornerHandrails)
        .addTag(outerHandrails)
        .addTag(stairHandrails);

    // 道路部分
    registerTags(roadBlocks, roadSlabs);

    // 灯光部分
    whiteWallLights.addTag(whiteStripWallLights);
    yellowWallLights.addTag(yellowStripWallLights);
    cyanWallLights.addTag(cyanStripWallLights);

    registerTags(
        whiteWallLights, whiteStripWallLights, whiteCornerLights, whiteLightDecorations,
        yellowWallLights, yellowStripWallLights, yellowCornerLights, yellowLightDecorations,
        cyanWallLights, cyanStripWallLights, cyanCornerLights, cyanLightDecorations
    );

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
    handrailItems.write(PACK);
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
    PACK.addTag(new Identifier("mishanguc", "items/simple_terracotta_handrails"), simpleTerracottaNormalHandrails);
    registerTagBlockOnly(simpleTerracottaCentralHandrails);
    registerTagBlockOnly(simpleTerracottaCornerHandrails);
    registerTagBlockOnly(simpleTerracottaOuterHandrails);
    registerTagBlockOnly(simpleTerracottaStairHandrails);
    registerTagBlockOnly(simpleStainedGlassHandrails);
    registerTagBlockOnly(simpleStainedGlassNormalHandrails);
    PACK.addTag(new Identifier("mishanguc", "items/simple_stained_glass_handrails"), simpleStainedGlassNormalHandrails);
    registerTagBlockOnly(simpleStainedGlassCentralHandrails);
    registerTagBlockOnly(simpleStainedGlassCornerHandrails);
    registerTagBlockOnly(simpleStainedGlassOuterHandrails);
    registerTagBlockOnly(simpleStainedGlassStairHandrails);

    // 悬挂的告示牌部分
    hungSigns
        .addTag(concreteHungSigns)
        .addTag(terracottaHungSigns);
    glowingHungSigns
        .addTag(glowingConcreteHungSigns)
        .addTag(glowingTerracottaHungSigns);
    hungSignBars
        .addTag(concreteHungSignBars)
        .addTag(terracottaHungSignBars);
    registerTag(concreteHungSigns);
    registerTag(terracottaHungSigns);
    registerTag(hungSigns);
    registerTag(glowingConcreteHungSigns);
    registerTag(glowingTerracottaHungSigns);
    registerTag(glowingHungSigns);
    registerTag(concreteHungSignBars);
    registerTag(terracottaHungSignBars);
    registerTag(hungSignBars);
  }

  private static void registerTag(IdentifiedTag blockTag) {
    blockTag.write(PACK);
    PACK.addTag(blockTag.identifier.brrp_prepend("items/"), blockTag);
  }

  private static void registerTags(IdentifiedTag... tags) {
    for (IdentifiedTag tag : tags) {
      registerTag(tag);
    }
  }

  private static void registerTagBlockOnly(IdentifiedTag tag) {
    PACK.addTag(tag.identifier.brrp_prepend("blocks/"), tag);
  }

  private static void registerTagItemOnly(IdentifiedTag tag) {
    PACK.addTag(tag.identifier.brrp_prepend("items/"), tag);
  }

  @Environment(EnvType.CLIENT)
  private static void writeAllItemModels() {
    MishangUtils.fieldStream(MishangucItems.class)
        .filter(field -> Item.class.isAssignableFrom(field.getType())
            && field.isAnnotationPresent(RegisterIdentifier.class)
            && field.isAnnotationPresent(SimpleModel.class))
        .forEach(field -> {
          String name = field.getAnnotation(RegisterIdentifier.class).value();
          String parent = field.getAnnotation(SimpleModel.class).parent();
          String texture = field.getAnnotation(SimpleModel.class).texture();
          if (name.isEmpty()) {
            name = field.getName().toLowerCase();
          }
          if (parent.isEmpty()) {
            name = "item/generated";
          }
          PACK.addModel(
              new JModel(parent)
                  .textures(new JTextures().layer0(texture.isEmpty() ? "mishanguc:item/" + name : texture)),
              new Identifier("mishanguc", "item/" + name));
        });

    writeExplosionToolItemModels();
  }

  @Environment(EnvType.CLIENT)
  private static void writeExplosionToolItemModels() {
    for (final String name : new String[]{
        "explosion_tool_fire",
        "explosion_tool_4", "explosion_tool_4_fire",
        "explosion_tool_8", "explosion_tool_8_fire",
        "explosion_tool_16", "explosion_tool_16_fire",
        "explosion_tool_32", "explosion_tool_32_fire",
        "explosion_tool_64", "explosion_tool_64_fire",
        "explosion_tool_128", "explosion_tool_128_fire",
    }) {
      PACK.addModel(new JModel("item/handheld").addTexture("layer0", "mishanguc:item/" + name), new Identifier("mishanguc", "item/" + name));
    }
  }

  @Environment(EnvType.CLIENT)
  private static void writeAllBlockModels() {
    // 道路部分
    writeBlockModelForCubeAllWithSlab(RoadBlocks.ROAD_BLOCK, "asphalt");
    writeBlockModelForCubeAllWithSlab(RoadBlocks.ROAD_FILLED_WITH_WHITE, "white_ink");
    writeBlockModelForCubeAllWithSlab(RoadBlocks.ROAD_FILLED_WITH_YELLOW, "yellow_ink");
    writeRoadBlockModelWithSlab(
        RoadBlocks.ROAD_WITH_WHITE_LINE,
        "road_with_straight_line",
        new FasterJTextures().base("asphalt").lineSide("white_straight_line").lineTop("white_straight_line"));
    writeRoadBlockModelWithSlab(
        RoadBlocks.ROAD_WITH_WHITE_RA_LINE,
        "road_with_angle_line",
        new FasterJTextures().base("asphalt").lineSide("white_straight_line").lineTop("white_right_angle_line"));
    writeRoadBlockModelWithSlab(
        RoadBlocks.ROAD_WITH_WHITE_BA_LINE,
        "road_with_angle_line",
        new FasterJTextures().base("asphalt").lineSide("white_straight_line").lineTop("white_bevel_angle_line"));
    writeRoadBlockModelWithSlab(
        RoadBlocks.ROAD_WITH_YELLOW_LINE,
        "road_with_straight_line",
        new FasterJTextures().base("asphalt").lineSide("yellow_straight_line").lineTop("yellow_straight_line"));
    writeRoadBlockModelWithSlab(
        RoadBlocks.ROAD_WITH_YELLOW_RA_LINE,
        "road_with_angle_line",
        new FasterJTextures().base("asphalt").lineSide("yellow_straight_line").lineTop("yellow_right_angle_line"));
    writeRoadBlockModelWithSlab(
        RoadBlocks.ROAD_WITH_YELLOW_BA_LINE,
        "road_with_angle_line",
        new FasterJTextures().base("asphalt").lineSide("yellow_straight_line").lineTop("yellow_bevel_angle_line"));
    writeRoadBlockModelWithSlabWithMirrored(
        RoadBlocks.ROAD_WITH_W_Y_RA_LINE,
        "road_with_angle_line",
        new FasterJTextures().base("asphalt").lineSide("yellow_straight_line").lineSide2("white_straight_line").lineTop("white_and_yellow_right_angle_line"));
    writeRoadBlockModelWithSlabWithMirrored(
        RoadBlocks.ROAD_WITH_WT_N_RA_LINE,
        "road_with_angle_line",
        new FasterJTextures().base("asphalt").lineSide("white_straight_line").lineSide2("white_straight_thick_line").lineTop("white_thick_and_normal_right_angle_line"));
    writeRoadBlockModelWithSlabWithMirrored(
        RoadBlocks.ROAD_WITH_WT_Y_RA_LINE,
        "road_with_angle_line",
        new FasterJTextures().base("asphalt").lineSide("yellow_straight_line").lineSide2("white_straight_thick_line").lineTop("white_thick_and_yellow_right_angle_line"));
    writeRoadBlockModelWithSlabWithMirrored(
        RoadBlocks.ROAD_WITH_W_YD_RA_LINE,
        "road_with_angle_line",
        new FasterJTextures().base("asphalt").lineSide("yellow_straight_double_line").lineSide2("white_straight_line").lineTop("white_and_yellow_double_right_angle_line"));
    writeRoadBlockModelWithSlabWithMirrored(
        RoadBlocks.ROAD_WITH_WT_YD_RA_LINE,
        "road_with_angle_line",
        new FasterJTextures().base("asphalt").lineSide("yellow_straight_double_line").lineSide2("white_straight_thick_line").lineTop("white_thick_and_yellow_double_right_angle_line"));
    writeRoadBlockModelWithSlab(
        RoadBlocks.ROAD_WITH_WHITE_TS_LINE,
        "road_with_joint_line",
        new FasterJTextures().base("asphalt").lineSide("white_straight_line").lineTop("white_joint_line"));
    writeRoadBlockModelWithSlabWithMirrored(
        RoadBlocks.ROAD_WITH_WHITE_S_BA_LINE,
        "road_with_straight_and_angle_line",
        FasterJTextures.ofP(
            "line_top_straight", "white_straight_line",
            "line_top_angle", "white_bevel_angle_line").lineSide("white_straight_line").base("asphalt"));
    writeRoadBlockModelWithSlab(
        RoadBlocks.ROAD_WITH_WHITE_CROSS_LINE,
        "road_with_cross_line",
        new FasterJTextures().base("asphalt").lineSide("white_straight_line").lineTop("white_cross_line"));
    writeRoadBlockModelWithSlab(
        RoadBlocks.ROAD_WITH_YELLOW_TS_LINE,
        "road_with_joint_line",
        new FasterJTextures().base("asphalt").lineSide("yellow_straight_line").lineTop("yellow_joint_line"));
    writeRoadBlockModelWithSlabWithMirrored(
        RoadBlocks.ROAD_WITH_YELLOW_S_BA_LINE,
        "road_with_straight_and_angle_line",
        FasterJTextures.ofP(
            "line_top_straight", "yellow_straight_line",
            "line_top_angle", "yellow_bevel_angle_line").lineSide("yellow_straight_line").base("asphalt"));
    writeRoadBlockModelWithSlab(
        RoadBlocks.ROAD_WITH_YELLOW_CROSS_LINE,
        "road_with_cross_line",
        new FasterJTextures().base("asphalt").lineSide("yellow_straight_line").lineTop("yellow_cross_line"));
    writeRoadBlockModelWithSlab(
        RoadBlocks.ROAD_WITH_WHITE_OFFSET_LINE,
        "road_with_straight_line",
        new FasterJTextures().base("asphalt").lineSide("white_offset_straight_line").lineTop("white_offset_straight_line"));
    writeRoadBlockModelWithSlab(
        RoadBlocks.ROAD_WITH_WHITE_DOUBLE_LINE,
        "road_with_straight_line",
        new FasterJTextures().base("asphalt").lineSide("white_straight_double_line").lineTop("white_straight_double_line"));
    writeRoadBlockModelWithSlab(
        RoadBlocks.ROAD_WITH_WHITE_THICK_LINE,
        "road_with_straight_line",
        new FasterJTextures().base("asphalt").lineSide("white_straight_thick_line").lineTop("white_straight_thick_line"));
    writeRoadBlockModelWithSlab(
        RoadBlocks.ROAD_WITH_YELLOW_OFFSET_LINE,
        "road_with_straight_line",
        new FasterJTextures().base("asphalt").lineSide("yellow_offset_straight_line").lineTop("yellow_offset_straight_line"));
    writeRoadBlockModelWithSlab(
        RoadBlocks.ROAD_WITH_YELLOW_DOUBLE_LINE,
        "road_with_straight_line",
        new FasterJTextures().base("asphalt").lineSide("yellow_straight_double_line").lineTop("yellow_straight_double_line"));
    writeRoadBlockModelWithSlab(
        RoadBlocks.ROAD_WITH_YELLOW_THICK_LINE,
        "road_with_straight_line",
        new FasterJTextures().base("asphalt").lineSide("yellow_straight_thick_line").lineTop("yellow_straight_thick_line"));
    writeRoadBlockModelWithSlabWithMirrored(
        RoadBlocks.ROAD_WITH_WHITE_RA_LINE_OFFSET_OUT,
        "road_with_angle_line",
        new FasterJTextures().base("asphalt").lineSide("white_straight_line").lineSide2("white_offset_straight_line").lineTop("white_right_angle_line_with_one_part_offset_out"));
    writeRoadBlockModelWithSlabWithMirrored(
        RoadBlocks.ROAD_WITH_WHITE_RA_LINE_OFFSET_IN,
        "road_with_angle_line",
        new FasterJTextures().base("asphalt").lineSide("white_straight_line").lineSide2("white_offset_straight_line2").lineTop("white_right_angle_line_with_one_part_offset_in"));
    writeRoadBlockModelWithSlab(
        RoadBlocks.ROAD_WITH_WHITE_TS_DOUBLE_LINE,
        "road_with_joint_line",
        new FasterJTextures().base("asphalt").lineSide("white_straight_line").lineSide2("white_straight_double_line").lineTop("white_joint_line_with_double_side"));
    writeRoadBlockModelWithSlab(
        RoadBlocks.ROAD_WITH_WHITE_TS_THICK_LINE,
        "road_with_joint_line",
        new FasterJTextures().base("asphalt").lineSide("white_straight_line").lineSide2("white_straight_thick_line").lineTop("white_joint_line_with_thick_side"));
    writeRoadBlockModelWithSlabWithMirrored(
        RoadBlocks.ROAD_WITH_WHITE_TS_OFFSET_LINE,
        "road_with_joint_line",
        new FasterJTextures().base("asphalt").lineSide("white_straight_line").lineSide2("white_offset_straight_line").lineTop("white_joint_line_with_offset_side"));
    writeRoadBlockModelWithSlab(
        RoadBlocks.ROAD_WITH_WHITE_THICK_TS_LINE,
        "road_with_joint_line",
        new FasterJTextures().base("asphalt").lineSide("white_straight_thick_line").lineSide2("white_straight_line").lineTop("white_thick_joint_line"));
    writeRoadBlockModelWithSlab(
        RoadBlocks.ROAD_WITH_WHITE_DOUBLE_TS_LINE,
        "road_with_joint_line",
        new FasterJTextures().base("asphalt").lineSide("white_straight_double_line").lineSide2("white_straight_line").lineTop("white_double_joint_line"));
    writeRoadBlockModelWithSlab(
        RoadBlocks.ROAD_WITH_Y_TS_W_LINE,
        "road_with_joint_line",
        new FasterJTextures().base("asphalt").lineSide("yellow_straight_line").lineSide2("white_straight_line").lineTop("yellow_joint_line_with_white_side"));
    writeRoadBlockModelWithSlab(
        RoadBlocks.ROAD_WITH_W_TS_Y_LINE,
        "road_with_joint_line",
        new FasterJTextures().base("asphalt").lineSide("white_straight_line").lineSide2("yellow_straight_line").lineTop("white_joint_line_with_yellow_side"));
    writeRoadBlockModelWithSlab(
        RoadBlocks.ROAD_WITH_W_TS_YD_LINE,
        "road_with_joint_line",
        new FasterJTextures().base("asphalt").lineSide("white_straight_line").lineSide2("yellow_straight_double_line").lineTop("white_joint_line_with_yellow_double_side"));
    writeRoadBlockModelWithSlab(
        RoadBlocks.ROAD_WITH_WT_TS_Y_LINE,
        "road_with_joint_line",
        new FasterJTextures().base("asphalt").lineSide("white_straight_thick_line").lineSide2("yellow_straight_line").lineTop("white_thick_joint_line_with_yellow_side"));
    writeRoadBlockModelWithSlab(
        RoadBlocks.ROAD_WITH_WT_TS_YD_LINE,
        "road_with_joint_line",
        new FasterJTextures().base("asphalt").lineSide("white_straight_thick_line").lineSide2("yellow_straight_double_line").lineTop("white_thick_joint_line_with_yellow_double_side"));

    writeRoadBlockModelWithSlab(
        RoadBlocks.ROAD_WITH_WHITE_AUTO_BA_LINE,
        "road_with_auto_line",
        new FasterJTextures()
            .base("asphalt")
            .line("white_auto_bevel_angle_line")
            .particle("asphalt"));
    writeRoadBlockModelWithSlab(
        RoadBlocks.ROAD_WITH_WHITE_AUTO_RA_LINE,
        "road_with_auto_line",
        new FasterJTextures()
            .base("asphalt")
            .line("white_auto_right_angle_line")
            .particle("asphalt"));

    // 光源部分

    PACK.addModel(
        new JModel(blockIdentifier("light"))
            .textures(new JTextures().var("base", blockString("white_light"))),
        blockIdentifier("white_light"));
    PACK.addModel(
        new JModel(blockIdentifier("light"))
            .textures(new JTextures().var("base", blockString("yellow_light"))),
        blockIdentifier("yellow_light"));
    PACK.addModel(
        new JModel(blockIdentifier("light"))
            .textures(new JTextures().var("base", blockString("cyan_light"))),
        blockIdentifier("cyan_light"));

  }

  @Override
  public void pregen() {
    final boolean dev = FabricLoader.getInstance().isDevelopmentEnvironment();
    if (!dev) generateResources(true, true);
    RRPCallbackConditional.BEFORE_VANILLA.register((resourceType, builder) -> builder.add(dev ? generateResources(resourceType == ResourceType.CLIENT_RESOURCES, resourceType == ResourceType.SERVER_DATA) : PACK));
  }

  /**
   * 为运行时资源包生成资源。在开发环境中，每次加载资源就会重新生成一次。在非开发环境中，游戏开始时生成一次，此后不再生成。
   */
  @CanIgnoreReturnValue
  private RuntimeResourcePack generateResources(boolean includesClient, boolean includesServer) {
    if (includesClient) PACK.clearResources(ResourceType.CLIENT_RESOURCES);
    if (includesServer) PACK.clearResources(ResourceType.SERVER_DATA);

    // 客户端部分
    if (includesClient) {
      // 由于注解了 @Environment(CLIENT)，所以考虑到潜在漏洞，在这里进行防冲突检测。
      try {
        writeAllBlockModels();
        writeAllItemModels();
      } catch (NoSuchMethodError e) {
        Mishanguc.MISHANG_LOGGER.error("Not supported to load client resources in server environment.", e);
      }
    }

    for (Block block : MishangUtils.blocks().values()) {
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
    for (Item item : MishangUtils.items().values()) {
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

    return PACK;
  }

  /**
   * 为本模组内的物品添加配方。该方法只会生成部分配方，还有很多配方是在 {@link net.devtech.arrp.generator.ItemResourceGenerator#writeRecipes(RuntimeResourcePack)} 的子方法中定义的。
   */
  private void addRecipes() {
    addRecipesForWallSigns();
    addRecipesForLights();
  }

  private void addRecipesForWallSigns() {
    // 隐形告示牌是合成其他告示牌的基础。
    { // invisible wall sign
      final JShapedRecipe recipe = new JShapedRecipe(WallSignBlocks.INVISIBLE_WALL_SIGN)
          .pattern(".#.", "#o#", ".#.")
          .addKey(".", Items.IRON_NUGGET)
          .addKey("#", Items.FEATHER)
          .addKey("o", Items.GOLD_INGOT)
          .resultCount(6);
      recipe.addInventoryChangedCriterion("has_iron_nugget", Items.IRON_NUGGET).addInventoryChangedCriterion("has_feather", Items.FEATHER).addInventoryChangedCriterion("has_gold_ingot", Items.GOLD_INGOT);
      final Identifier id = ResourceGeneratorHelper.getItemId(WallSignBlocks.INVISIBLE_WALL_SIGN);
      PACK.addRecipe(id, recipe);
      PACK.addRecipeAdvancement(id, id.brrp_prepend("recipes/signs/"), recipe);
    }
    { // invisible glowing wall sign
      final JShapedRecipe recipe = new JShapedRecipe(WallSignBlocks.INVISIBLE_GLOWING_WALL_SIGN)
          .pattern("---", "###")
          .addKey("-", Items.GLOWSTONE_DUST)
          .addKey("#", WallSignBlocks.INVISIBLE_WALL_SIGN)
          .resultCount(3);
      recipe.addInventoryChangedCriterion("has_base_block", WallSignBlocks.INVISIBLE_WALL_SIGN);
      final Identifier id = ResourceGeneratorHelper.getItemId(WallSignBlocks.INVISIBLE_GLOWING_WALL_SIGN);
      PACK.addRecipe(id, recipe);
      PACK.addRecipeAdvancement(id, id.brrp_prepend("recipes/signs/"), recipe);
    }
  }

  private void addRecipesForLights() {
    // 先是三个完整方块的合成表。
    { // white light
      final JShapedRecipe recipe = new JShapedRecipe(LightBlocks.WHITE_LIGHT)
          .resultCount(8)
          .pattern("*#*", "#C#", "*#*")
          .addKey("*", Items.WHITE_DYE)
          .addKey("#", Items.GLOWSTONE)
          .addKey("C", Items.WHITE_CONCRETE);
      recipe
          .addInventoryChangedCriterion("has_dye", Items.WHITE_DYE)
          .addInventoryChangedCriterion("has_glowstone", Items.GLOWSTONE)
          .addInventoryChangedCriterion("has_concrete", Items.WHITE_CONCRETE);
      final Identifier id = ResourceGeneratorHelper.getItemId(LightBlocks.WHITE_LIGHT);
      PACK.addRecipe(id, recipe);
      PACK.addRecipeAdvancement(id, id.brrp_prepend("recipes/light/"), recipe);
    }
    { // yellow light
      final JShapedRecipe recipe = new JShapedRecipe(LightBlocks.YELLOW_LIGHT)
          .resultCount(8)
          .pattern("*#*", "#C#", "*#*")
          .addKey("*", Items.YELLOW_DYE)
          .addKey("#", Items.GLOWSTONE)
          .addKey("C", Items.YELLOW_CONCRETE);
      recipe
          .addInventoryChangedCriterion("has_dye", Items.YELLOW_DYE)
          .addInventoryChangedCriterion("has_glowstone", Items.GLOWSTONE)
          .addInventoryChangedCriterion("has_concrete", Items.YELLOW_CONCRETE);
      final Identifier id = ResourceGeneratorHelper.getItemId(LightBlocks.YELLOW_LIGHT);
      PACK.addRecipe(id, recipe);
      PACK.addRecipeAdvancement(id, id.brrp_prepend("recipes/light/"), recipe);
    }
    { // cyan light
      final JShapedRecipe recipe = new JShapedRecipe(LightBlocks.CYAN_LIGHT)
          .resultCount(8)
          .pattern("*#*", "#C#", "*#*")
          .addKey("*", Items.CYAN_DYE)
          .addKey("#", Items.GLOWSTONE)
          .addKey("C", Items.CYAN_CONCRETE);
      recipe
          .addInventoryChangedCriterion("has_dye", Items.CYAN_DYE)
          .addInventoryChangedCriterion("has_glowstone", Items.GLOWSTONE)
          .addInventoryChangedCriterion("has_concrete", Items.CYAN_CONCRETE);
      final Identifier id = ResourceGeneratorHelper.getItemId(LightBlocks.CYAN_LIGHT);
      PACK.addRecipe(id, recipe);
      PACK.addRecipeAdvancement(id, id.brrp_prepend("recipes/light/"), recipe);
    }


    // 白色灯光

    addShapelessRecipeForLight(LightBlocks.WHITE_SMALL_WALL_LIGHT, 1, LightBlocks.WHITE_SMALL_WALL_LIGHT_TUBE, Blocks.GRAY_CONCRETE);
    addStonecuttingRecipeForLight(LightBlocks.WHITE_LIGHT, LightBlocks.WHITE_SMALL_WALL_LIGHT_TUBE, 16);
    addShapelessRecipeForLight(LightBlocks.WHITE_LARGE_WALL_LIGHT, 1, LightBlocks.WHITE_LARGE_WALL_LIGHT_TUBE, Blocks.GRAY_CONCRETE);
    addStonecuttingRecipeForLight(LightBlocks.WHITE_LIGHT, LightBlocks.WHITE_LARGE_WALL_LIGHT_TUBE, 12);
    addShapelessRecipeForLight(LightBlocks.WHITE_THIN_STRIP_WALL_LIGHT, 1, LightBlocks.WHITE_THICK_STRIP_WALL_LIGHT_TUBE, Blocks.GRAY_CONCRETE);
    addStonecuttingRecipeForLight(LightBlocks.WHITE_LIGHT, LightBlocks.WHITE_THIN_STRIP_WALL_LIGHT_TUBE, 12);
    addShapelessRecipeForLight(LightBlocks.WHITE_THIN_STRIP_WALL_LIGHT, 1, LightBlocks.WHITE_THICK_STRIP_WALL_LIGHT_TUBE, Blocks.GRAY_CONCRETE);
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
    addShapelessRecipeForLight(LightBlocks.YELLOW_THIN_STRIP_WALL_LIGHT, 1, LightBlocks.YELLOW_THICK_STRIP_WALL_LIGHT_TUBE, Blocks.GRAY_CONCRETE);
    addStonecuttingRecipeForLight(LightBlocks.YELLOW_LIGHT, LightBlocks.YELLOW_THIN_STRIP_WALL_LIGHT_TUBE, 12);
    addShapelessRecipeForLight(LightBlocks.YELLOW_THIN_STRIP_WALL_LIGHT, 1, LightBlocks.YELLOW_THICK_STRIP_WALL_LIGHT_TUBE, Blocks.GRAY_CONCRETE);
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
    addShapelessRecipeForLight(LightBlocks.CYAN_THIN_STRIP_WALL_LIGHT, 1, LightBlocks.CYAN_THICK_STRIP_WALL_LIGHT_TUBE, Blocks.GRAY_CONCRETE);
    addStonecuttingRecipeForLight(LightBlocks.CYAN_LIGHT, LightBlocks.CYAN_THIN_STRIP_WALL_LIGHT_TUBE, 12);
    addShapelessRecipeForLight(LightBlocks.CYAN_THIN_STRIP_WALL_LIGHT, 1, LightBlocks.CYAN_THICK_STRIP_WALL_LIGHT_TUBE, Blocks.GRAY_CONCRETE);
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
    final JStonecuttingRecipe recipe = new JStonecuttingRecipe(ingredient, result, count);
    recipe.addInventoryChangedCriterion("has_the_ingredient", ingredient);
    final Identifier id = ResourceGeneratorHelper.getItemId(result);
    PACK.addRecipe(id, recipe);
    PACK.addRecipeAdvancement(id, id.brrp_prepend("recipes/light/"), recipe);
  }

  private static void addShapelessRecipeForLight(ItemConvertible result, int count, ItemConvertible... ingredients) {
    final JShapelessRecipe recipe = new JShapelessRecipe(result, ingredients).resultCount(count);
    for (ItemConvertible ingredient : ImmutableSet.copyOf(ingredients)) {
      recipe.addInventoryChangedCriterion("has_" + ResourceGeneratorHelper.getItemId(ingredient).getPath(), ingredient);
    }
    final Identifier id = ResourceGeneratorHelper.getItemId(result);
    PACK.addRecipe(id, recipe);
    PACK.addRecipeAdvancement(id, id.brrp_prepend("recipes/light/"), recipe);
  }

  @Override
  public void onInitialize() {
    pregen();
  }
}
