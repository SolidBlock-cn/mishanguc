package pers.solid.mishang.uc.arrp;

import net.devtech.arrp.api.RRPCallback;
import net.devtech.arrp.api.RRPPreGenEntrypoint;
import net.devtech.arrp.api.RuntimeResourcePack;
import net.devtech.arrp.generator.BlockResourceGenerator;
import net.devtech.arrp.json.models.JModel;
import net.devtech.arrp.json.models.JTextures;
import net.devtech.arrp.json.tags.JTag;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.util.Identifier;
import pers.solid.mishang.uc.MishangUtils;
import pers.solid.mishang.uc.annotations.RegisterIdentifier;
import pers.solid.mishang.uc.annotations.SimpleModel;
import pers.solid.mishang.uc.block.*;
import pers.solid.mishang.uc.blocks.*;
import pers.solid.mishang.uc.item.MishangucItems;

import java.lang.reflect.Modifier;
import java.util.Arrays;

/**
 * @since 0.1.7 本类应当在 onInitialize 的入口点中执行，而非 pregen 中。
 */
@SuppressWarnings({"SameParameterValue"})
public class ARRPMain implements RRPPreGenEntrypoint, ModInitializer {
  private static final RuntimeResourcePack PACK = RuntimeResourcePack.create(new Identifier("mishanguc", "pack"));

  private static Identifier blockIdentifier(String path) {
    return new Identifier("mishanguc", "block/" + path);
  }

  private static String blockString(String path) {
    return blockIdentifier(path).toString();
  }

  private static void writeBlockModelForCubeAll(RuntimeResourcePack pack, BlockResourceGenerator block, String all) {
    pack.addModel(
        new JModel("block/cube_all").textures(new FasterJTextures().varP("all", all)),
        block.getBlockModelId());
  }

  private static void writeBlockModelForSlabAll(RuntimeResourcePack pack, BlockResourceGenerator block, String all) {
    pack.addModel(
        new JModel("block/slab")
            .textures(
                new FasterJTextures().top(all).side(all).bottom(all)),
        block.getBlockModelId());
    pack.addModel(
        new JModel("block/slab_top")
            .textures(
                new FasterJTextures().top(all).side(all).bottom(all)),
        block.getBlockModelId().brrp_append("_top"));
  }

  /**
   * 运行此方法需确保其楼梯名称正好为 path + "_slab"。
   */
  private static void writeBlockModelForCubeAllWithSlab(final RuntimeResourcePack pack, AbstractRoadBlock block, String all) {
    writeBlockModelForCubeAll(pack, block, all);
    writeBlockModelForSlabAll(pack, RoadSlabBlocks.BLOCK_TO_SLABS.get(block), all);
  }

  /**
   * 添加一个方块以及其台阶方块的方块模型。仅用于此模组。
   *
   * @param pack     运行时资源包。
   * @param block    方块。必须是道路方块，且在 {@link RoadSlabBlocks#BLOCK_TO_SLABS} 中有对应的台阶版本。
   * @param parent   资源包的 parent。应当保证 parent、parent+"_slab" 和 parent+"_slab_top"都要存在。
   * @param textures 纹理变量。三个 parent 都应该使用相同的纹理。
   */
  private static void writeRoadBlockModelWithSlab(
      RuntimeResourcePack pack, AbstractRoadBlock block, String parent, JTextures textures) {
    final Identifier id = block.getBlockModelId();
    final Identifier slabId = RoadSlabBlocks.BLOCK_TO_SLABS.get(block).getBlockModelId();
    writeRoadBlockModelWithSlab(pack, parent, textures, id, slabId);
  }

  /**
   * 添加一个方块以及其台阶方块的方块模型，以及其对应的“mirrored”的方块模型。仅用于此模组。
   *
   * @param pack     运行时资源包。
   * @param block    方块。必须是道路方块，且在 {@link RoadSlabBlocks#BLOCK_TO_SLABS} 中有对应的台阶版本。
   * @param parent   资源包的 parent。应当保证 parent、parent+"_slab" 和 parent+"_slab_top"都要存在。
   * @param textures 纹理变量。三个 parent 都应该使用相同的纹理。
   */
  private static void writeRoadBlockModelWithSlabWithMirrored(
      RuntimeResourcePack pack, AbstractRoadBlock block, String parent, JTextures textures) {
    final Identifier id = block.getBlockModelId();
    final Identifier slabId = RoadSlabBlocks.BLOCK_TO_SLABS.get(block).getBlockModelId();
    writeRoadBlockModelWithSlab(pack, parent, textures, id, slabId);
    writeRoadBlockModelWithSlab(pack, parent + "_mirrored", textures, id.brrp_append("_mirrored"), slabId.brrp_append("_mirrored"));
  }

  /**
   * 添加一个方块以及其台阶方块的方块模型。仅用于此模组。
   *
   * @param pack     运行时资源包。
   * @param id       道路方块的完整id。
   * @param slabId   该方块对应的台阶方块的完整id。
   * @param parent   资源包的 parent。应当保证 parent、parent+"_slab" 和 parent+"_slab_top"都要存在。
   * @param textures 纹理变量。三个 parent 都应该使用相同的纹理。
   */
  private static void writeRoadBlockModelWithSlab(RuntimeResourcePack pack, String parent, JTextures textures, Identifier id, Identifier slabId) {
    pack.addModel(new JModel(blockIdentifier(parent)).textures(textures), id);
    pack.addModel(new JModel(BRRPHelper.slabOf(blockIdentifier(parent))).textures(textures), slabId);
    pack.addModel(new JModel(BRRPHelper.slabOf(blockIdentifier(parent)) + "_top").textures(textures), slabId.brrp_append("_top"));
  }

  private static void addTags() {
    // 道路部分
    final JImprovedTag roadBlocks = new JImprovedTag();
    final JImprovedTag roadSlabs = new JImprovedTag();

    // 灯光部分
    final JImprovedTag whiteStripWallLights = new JImprovedTag();
    final JImprovedTag whiteWallLights = new JImprovedTag();
    final JImprovedTag whiteCornerLights = new JImprovedTag();
    final JImprovedTag whiteLightDecorations = new JImprovedTag();
    final JImprovedTag yellowStripWallLights = new JImprovedTag();
    final JImprovedTag yellowWallLights = new JImprovedTag();
    final JImprovedTag yellowCornerLights = new JImprovedTag();
    final JImprovedTag yellowLightDecorations = new JImprovedTag();
    final JImprovedTag cyanStripWallLights = new JImprovedTag();
    final JImprovedTag cyanWallLights = new JImprovedTag();
    final JImprovedTag cyanCornerLights = new JImprovedTag();
    final JImprovedTag cyanLightDecorations = new JImprovedTag();

    // 墙上的告示牌部分
    final JImprovedTag woodenWallSigns = new JImprovedTag();
    final JImprovedTag concreteWallSigns = new JImprovedTag();
    final JImprovedTag terracottaWallSigns = new JImprovedTag();
    final JImprovedTag wallSigns = new JImprovedTag();
    final JImprovedTag glowingConcreteWallSigns = new JImprovedTag();
    final JImprovedTag glowingTerracottaWallSigns = new JImprovedTag();
    final JImprovedTag glowingWallSigns = new JImprovedTag();
    final JImprovedTag fullConcreteWallSigns = new JImprovedTag();
    final JImprovedTag fullTerracottaWallSigns = new JImprovedTag();
    final JImprovedTag fullWallSigns = new JImprovedTag();

    // 悬挂的告示牌部分
    final JImprovedTag concreteHungSigns = new JImprovedTag();
    final JImprovedTag terracottaHungSigns = new JImprovedTag();
    final JImprovedTag hungSigns = new JImprovedTag();
    final JImprovedTag glowingConcreteHungSigns = new JImprovedTag();
    final JImprovedTag glowingTerracottaHungSigns = new JImprovedTag();
    final JImprovedTag glowingHungSigns = new JImprovedTag();
    // 悬挂的告示牌杆部分
    final JImprovedTag concreteHungSignBars = new JImprovedTag();
    final JImprovedTag terracottaHungSignBars = new JImprovedTag();
    final JImprovedTag hungSignBars = new JImprovedTag();

    // 栏杆部分
    final JImprovedTag handrails = new JImprovedTag();
    final JImprovedTag handrailItems = new JImprovedTag();
    final JImprovedTag normalHandrails = new JImprovedTag();
    final JImprovedTag centralHandrails = new JImprovedTag();
    final JImprovedTag cornerHandrails = new JImprovedTag();
    final JImprovedTag outerHandrails = new JImprovedTag();
    final JImprovedTag stairHandrails = new JImprovedTag();

    // 混凝土栏杆部分
    final JImprovedTag simpleConcreteHandrails = new JImprovedTag();
    final JImprovedTag simpleConcreteNormalHandrails = new JImprovedTag();
    final JImprovedTag simpleConcreteCentralHandrails = new JImprovedTag();
    final JImprovedTag simpleConcreteCornerHandrails = new JImprovedTag();
    final JImprovedTag simpleConcreteOuterHandrails = new JImprovedTag();
    final JImprovedTag simpleConcreteStairHandrails = new JImprovedTag();
    // 陶瓦栏杆部分
    final JImprovedTag simpleTerracottaHandrails = new JImprovedTag();
    final JImprovedTag simpleTerracottaNormalHandrails = new JImprovedTag();
    final JImprovedTag simpleTerracottaCentralHandrails = new JImprovedTag();
    final JImprovedTag simpleTerracottaCornerHandrails = new JImprovedTag();
    final JImprovedTag simpleTerracottaOuterHandrails = new JImprovedTag();
    final JImprovedTag simpleTerracottaStairHandrails = new JImprovedTag();
    // 染色玻璃栏杆部分
    final JImprovedTag simpleStainedGlassHandrails = new JImprovedTag();
    final JImprovedTag simpleStainedGlassNormalHandrails = new JImprovedTag();
    final JImprovedTag simpleStainedGlassCentralHandrails = new JImprovedTag();
    final JImprovedTag simpleStainedGlassCornerHandrails = new JImprovedTag();
    final JImprovedTag simpleStainedGlassOuterHandrails = new JImprovedTag();
    final JImprovedTag simpleStainedGlassStairHandrails = new JImprovedTag();


    // 扶手部分，预留

    // 道路部分
    MishangUtils.<Block>blockInstanceStream(RoadBlocks.class).forEach(
        block -> {
          if (block instanceof AbstractRoadBlock) {
            roadBlocks.addBlock(block);
          }
        }
    );
    MishangUtils.<Block>blockInstanceStream(RoadSlabBlocks.class).forEach(
        block -> {
          if (block instanceof AbstractRoadSlabBlock) {
            roadSlabs.addBlock(block);
          }
        }
    );

    // 灯光部分
    MishangUtils.<Block>blockInstanceStream(LightBlocks.class).forEach(
        block -> {
          if (block instanceof StripWallLightBlock) {
            switch (((StripWallLightBlock) block).lightColor) {
              case "white":
                whiteStripWallLights.addBlock(block);
                break;
              case "yellow":
                yellowStripWallLights.addBlock(block);
                break;
              case "cyan":
                cyanStripWallLights.addBlock(block);
                break;
            }
          } else if (block instanceof AutoConnectWallLightBlock) {
            switch (((AutoConnectWallLightBlock) block).lightColor) {
              case "white":
                whiteLightDecorations.addBlock(block);
                break;
              case "yellow":
                yellowLightDecorations.addBlock(block);
                break;
              case "cyan":
                cyanLightDecorations.addBlock(block);
                break;
            }
          } else if (block instanceof WallLightBlock) {
            switch (((WallLightBlock) block).lightColor) {
              case "white":
                whiteWallLights.addBlock(block);
                break;
              case "yellow":
                yellowWallLights.addBlock(block);
                break;
              case "cyan":
                cyanWallLights.addBlock(block);
                break;
            }
          } else if (block instanceof CornerLightBlock) {
            switch (((CornerLightBlock) block).lightColor) {
              case "white":
                whiteCornerLights.addBlock(block);
                break;
              case "yellow":
                yellowCornerLights.addBlock(block);
                break;
              case "cyan":
                cyanCornerLights.addBlock(block);
            }
          }
        }
    );

    // 悬挂的告示牌部分
    MishangUtils.<Block>blockInstanceStream(HungSignBlocks.class).forEach(block -> {
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
    MishangUtils.<Block>blockInstanceStream(WallSignBlocks.class).forEach(block -> {
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
    MishangUtils.<Block>blockInstanceStream(HandrailBlocks.class).forEach(block -> {
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
        .add(new Identifier("mishanguc", "simple_stained_glass_normal_handrails"))
        .add(new Identifier("mishanguc", "simple_stained_glass_central_handrails"))
        .add(new Identifier("mishanguc", "simple_stained_glass_corner_handrails"))
        .add(new Identifier("mishanguc", "simple_stained_glass_outer_handrails"))
        .add(new Identifier("mishanguc", "simple_stained_glass_stair_handrails"));
    simpleConcreteHandrails
        .add(new Identifier("mishanguc", "simple_concrete_normal_handrails"))
        .add(new Identifier("mishanguc", "simple_concrete_central_handrails"))
        .add(new Identifier("mishanguc", "simple_concrete_corner_handrails"))
        .add(new Identifier("mishanguc", "simple_concrete_outer_handrails"))
        .add(new Identifier("mishanguc", "simple_concrete_stair_handrails"));
    simpleTerracottaHandrails
        .add(new Identifier("mishanguc", "simple_terracotta_normal_handrails"))
        .add(new Identifier("mishanguc", "simple_terracotta_central_handrails"))
        .add(new Identifier("mishanguc", "simple_terracotta_corner_handrails"))
        .add(new Identifier("mishanguc", "simple_terracotta_outer_handrails"))
        .add(new Identifier("mishanguc", "simple_terracotta_stair_handrails"));
    normalHandrails
        .add(new Identifier("mishanguc", "simple_stained_glass_normal_handrails"))
        .add(new Identifier("mishanguc", "simple_terracotta_normal_handrails"))
        .add(new Identifier("mishanguc", "simple_concrete_normal_handrails"));
    handrailItems
        .add(new Identifier("mishanguc", "simple_stained_glass_handrails"))
        .add(new Identifier("mishanguc", "simple_terracotta_handrails"))
        .add(new Identifier("mishanguc", "simple_concrete_handrails"));
    centralHandrails
        .add(new Identifier("mishanguc", "simple_stained_glass_central_handrails"))
        .add(new Identifier("mishanguc", "simple_terracotta_central_handrails"))
        .add(new Identifier("mishanguc", "simple_concrete_central_handrails"));
    cornerHandrails
        .add(new Identifier("mishanguc", "simple_stained_glass_corner_handrails"))
        .add(new Identifier("mishanguc", "simple_terracotta_corner_handrails"))
        .add(new Identifier("mishanguc", "simple_concrete_corner_handrails"));
    outerHandrails
        .add(new Identifier("mishanguc", "simple_stained_glass_outer_handrails"))
        .add(new Identifier("mishanguc", "simple_terracotta_outer_handrails"))
        .add(new Identifier("mishanguc", "simple_concrete_outer_handrails"));
    stairHandrails
        .add(new Identifier("mishanguc", "simple_stained_glass_stair_handrails"))
        .add(new Identifier("mishanguc", "simple_terracotta_stair_handrails"))
        .add(new Identifier("mishanguc", "simple_concrete_stair_handrails"));
    handrails
        .add(new Identifier("mishanguc", "normal_handrails"))
        .add(new Identifier("mishanguc", "central_handrails"))
        .add(new Identifier("mishanguc", "corner_handrails"))
        .add(new Identifier("mishanguc", "outer_handrails"))
        .add(new Identifier("mishanguc", "stair_handrails"));

    // 道路部分
    PACK.addTag(new Identifier("mishanguc", "blocks/road_blocks"), roadBlocks);
    PACK.addTag(new Identifier("mishanguc", "items/road_blocks"), roadBlocks);
    PACK.addTag(new Identifier("mishanguc", "blocks/road_slabs"), roadSlabs);
    PACK.addTag(new Identifier("mishanguc", "items/road_slabs"), roadSlabs);

    // 灯光部分
    whiteWallLights.add(new Identifier("mishanguc", "white_strip_wall_lights"));
    yellowWallLights.add(new Identifier("mishanguc", "yellow_strip_wall_lights"));
    cyanWallLights.add(new Identifier("mishanguc", "cyan_strip_wall_lights"));

    PACK.addTag(new Identifier("mishanguc", "blocks/white_strip_wall_lights"), whiteStripWallLights);
    PACK.addTag(new Identifier("mishanguc", "items/white_strip_wall_lights"), whiteStripWallLights);
    PACK.addTag(new Identifier("mishanguc", "blocks/white_wall_lights"), whiteWallLights);
    PACK.addTag(new Identifier("mishanguc", "items/white_wall_lights"), whiteWallLights);
    PACK.addTag(new Identifier("mishanguc", "blocks/white_corner_lights"), whiteCornerLights);
    PACK.addTag(new Identifier("mishanguc", "items/white_corner_lights"), whiteCornerLights);
    PACK.addTag(new Identifier("mishanguc", "blocks/white_light_decorations"), whiteLightDecorations);
    PACK.addTag(new Identifier("mishanguc", "items/white_light_decorations"), whiteLightDecorations);
    PACK.addTag(new Identifier("mishanguc", "blocks/yellow_strip_wall_lights"), yellowStripWallLights);
    PACK.addTag(new Identifier("mishanguc", "items/yellow_strip_wall_lights"), yellowStripWallLights);
    PACK.addTag(new Identifier("mishanguc", "blocks/yellow_wall_lights"), yellowWallLights);
    PACK.addTag(new Identifier("mishanguc", "items/yellow_wall_lights"), yellowWallLights);
    PACK.addTag(new Identifier("mishanguc", "blocks/yellow_corner_lights"), yellowCornerLights);
    PACK.addTag(new Identifier("mishanguc", "items/yellow_corner_lights"), yellowCornerLights);
    PACK.addTag(new Identifier("mishanguc", "blocks/yellow_light_decorations"), yellowLightDecorations);
    PACK.addTag(new Identifier("mishanguc", "items/yellow_light_decorations"), yellowLightDecorations);
    PACK.addTag(new Identifier("mishanguc", "blocks/cyan_strip_wall_lights"), cyanStripWallLights);
    PACK.addTag(new Identifier("mishanguc", "items/cyan_strip_wall_lights"), cyanStripWallLights);
    PACK.addTag(new Identifier("mishanguc", "blocks/cyan_wall_lights"), cyanWallLights);
    PACK.addTag(new Identifier("mishanguc", "items/cyan_wall_lights"), cyanWallLights);
    PACK.addTag(new Identifier("mishanguc", "blocks/cyan_corner_lights"), cyanCornerLights);
    PACK.addTag(new Identifier("mishanguc", "items/cyan_corner_lights"), cyanCornerLights);
    PACK.addTag(new Identifier("mishanguc", "blocks/cyan_light_decorations"), cyanLightDecorations);
    PACK.addTag(new Identifier("mishanguc", "items/cyan_light_decorations"), cyanLightDecorations);

    // 墙上的告示牌部分
    wallSigns
        .add(new Identifier("mishanguc", "wooden_wall_signs"))
        .add(new Identifier("mishanguc", "concrete_wall_signs"))
        .add(new Identifier("mishanguc", "terracotta_wall_signs"));
    glowingWallSigns
        .add(new Identifier("mishanguc", "glowing_concrete_wall_signs"))
        .add(new Identifier("mishanguc", "glowing_terracotta_wall_signs"));
    fullWallSigns
        .add(new Identifier("mishanguc", "full_concrete_wall_signs"))
        .add(new Identifier("mishanguc", "full_terracotta_wall_signs"));
    registerTag(woodenWallSigns, "wooden_wall_signs");
    registerTag(concreteWallSigns, "concrete_wall_signs");
    registerTag(terracottaWallSigns, "terracotta_wall_signs");
    registerTag(wallSigns, "wall_signs");
    registerTag(glowingConcreteWallSigns, "glowing_concrete_wall_signs");
    registerTag(glowingTerracottaWallSigns, "glowing_terracotta_wall_signs");
    registerTag(glowingWallSigns, "glowing_wall_signs");
    registerTag(fullConcreteWallSigns, "full_concrete_wall_signs");
    registerTag(fullTerracottaWallSigns, "full_terracotta_wall_signs");
    registerTag(fullWallSigns, "full_wall_signs");

    registerTagBlockOnly(handrails, "handrails");
    registerTagBlockOnly(normalHandrails, "normal_handrails");
    registerTagItemOnly(handrailItems, "handrails");
    registerTagBlockOnly(centralHandrails, "central_handrails");
    registerTagBlockOnly(cornerHandrails, "corner_handrails");
    registerTagBlockOnly(outerHandrails, "outer_handrails");
    registerTagBlockOnly(stairHandrails, "stair_handrails");
    registerTagBlockOnly(simpleConcreteHandrails, "simple_concrete_handrails");
    registerTagBlockOnly(simpleConcreteNormalHandrails, "simple_concrete_normal_handrails");
    registerTagItemOnly(simpleConcreteNormalHandrails, "simple_concrete_handrails");
    registerTagBlockOnly(simpleConcreteCentralHandrails, "simple_concrete_central_handrails");
    registerTagBlockOnly(simpleConcreteCornerHandrails, "simple_concrete_corner_handrails");
    registerTagBlockOnly(simpleConcreteOuterHandrails, "simple_concrete_outer_handrails");
    registerTagBlockOnly(simpleConcreteStairHandrails, "simple_concrete_stair_handrails");
    registerTagBlockOnly(simpleTerracottaHandrails, "simple_terracotta_handrails");
    registerTagBlockOnly(simpleTerracottaNormalHandrails, "simple_terracotta_normal_handrails");
    registerTagItemOnly(simpleTerracottaNormalHandrails, "simple_terracotta_handrails");
    registerTagBlockOnly(simpleTerracottaCentralHandrails, "simple_terracotta_central_handrails");
    registerTagBlockOnly(simpleTerracottaCornerHandrails, "simple_terracotta_corner_handrails");
    registerTagBlockOnly(simpleTerracottaOuterHandrails, "simple_terracotta_outer_handrails");
    registerTagBlockOnly(simpleTerracottaStairHandrails, "simple_terracotta_stair_handrails");
    registerTagBlockOnly(simpleStainedGlassHandrails, "simple_stained_glass_handrails");
    registerTagBlockOnly(simpleStainedGlassNormalHandrails, "simple_stained_glass_normal_handrails");
    registerTagItemOnly(simpleStainedGlassNormalHandrails, "simple_stained_glass_handrails");
    registerTagBlockOnly(simpleStainedGlassCentralHandrails, "simple_stained_glass_central_handrails");
    registerTagBlockOnly(simpleStainedGlassCornerHandrails, "simple_stained_glass_corner_handrails");
    registerTagBlockOnly(simpleStainedGlassOuterHandrails, "simple_stained_glass_outer_handrails");
    registerTagBlockOnly(simpleStainedGlassStairHandrails, "simple_stained_glass_stair_handrails");

    // 悬挂的告示牌部分
    hungSigns
        .add(new Identifier("mishanguc", "concrete_hung_signs"))
        .add(new Identifier("mishanguc", "terracotta_hung_signs"));
    glowingHungSigns
        .add(new Identifier("mishanguc", "glowing_concrete_hung_signs"))
        .add(new Identifier("mishanguc", "glowing_terracotta_hung_signs"));
    hungSignBars
        .add(new Identifier("mishanguc", "concrete_hung_sign_bars"))
        .add(new Identifier("mishanguc", "terracotta_hung_sign_bars"));
    registerTag(concreteHungSigns, "concrete_hung_signs");
    registerTag(terracottaHungSigns, "terracotta_hung_signs");
    registerTag(hungSigns, "hung_signs");
    registerTag(glowingConcreteHungSigns, "glowing_concrete_hung_signs");
    registerTag(glowingTerracottaHungSigns, "glowing_terracotta_hung_signs");
    registerTag(glowingHungSigns, "glowing_hung_signs");
    registerTag(concreteHungSignBars, "concrete_hung_sign_bars");
    registerTag(terracottaHungSignBars, "terracotta_hung_sign_bars");
    registerTag(hungSignBars, "hung_sign_bars");
  }

  private static void registerTag(JTag tag, String name) {
    PACK.addTag(new Identifier("mishanguc", "blocks/" + name), tag);
    PACK.addTag(new Identifier("mishanguc", "items/" + name), tag);
  }

  private static void registerTagBlockOnly(JTag tag, String name) {
    PACK.addTag(new Identifier("mishanguc", "blocks/" + name), tag);
  }

  private static void registerTagItemOnly(JTag tag, String name) {
    PACK.addTag(new Identifier("mishanguc", "items/" + name), tag);
  }

  private static void addItemModels(RuntimeResourcePack pack) {
    Arrays.stream(MishangucItems.class.getFields())
        .filter(
            field -> {
              int modifier = field.getModifiers();
              return Modifier.isPublic(modifier)
                  && Modifier.isStatic(modifier)
                  && Item.class.isAssignableFrom(field.getType())
                  && field.isAnnotationPresent(RegisterIdentifier.class)
                  && field.isAnnotationPresent(SimpleModel.class);
            })
        .forEach(
            field -> {
              String name = field.getAnnotation(RegisterIdentifier.class).value();
              String parent = field.getAnnotation(SimpleModel.class).parent();
              String texture = field.getAnnotation(SimpleModel.class).texture();
              if (name.isEmpty()) {
                name = field.getName().toLowerCase();
              }
              if (parent.isEmpty()) {
                name = "item/generated";
              }
              pack.addModel(
                  new JModel(parent)
                      .textures(
                          new JTextures()
                              .layer0(texture.isEmpty() ? "mishanguc:item/" + name : texture)),
                  new Identifier("mishanguc", "item/" + name));
            });
  }

  private static void addBlockModels(RuntimeResourcePack pack) {
    // 道路部分
    writeBlockModelForCubeAllWithSlab(pack, RoadBlocks.ROAD_BLOCK, "asphalt");
    writeBlockModelForCubeAllWithSlab(pack, RoadBlocks.ROAD_FILLED_WITH_WHITE, "white_ink");
    writeBlockModelForCubeAllWithSlab(pack, RoadBlocks.ROAD_FILLED_WITH_YELLOW, "yellow_ink");
    writeRoadBlockModelWithSlab(
        pack,
        RoadBlocks.ROAD_WITH_WHITE_LINE,
        "road_with_straight_line",
        new FasterJTextures().base("asphalt").lineSide("white_straight_line").lineTop("white_straight_line"));
    writeRoadBlockModelWithSlab(
        pack,
        RoadBlocks.ROAD_WITH_WHITE_RA_LINE,
        "road_with_angle_line",
        new FasterJTextures().base("asphalt").lineSide("white_straight_line").lineTop("white_right_angle_line"));
    writeRoadBlockModelWithSlab(
        pack,
        RoadBlocks.ROAD_WITH_WHITE_BA_LINE,
        "road_with_angle_line",
        new FasterJTextures().base("asphalt").lineSide("white_straight_line").lineTop("white_bevel_angle_line"));
    writeRoadBlockModelWithSlab(
        pack,
        RoadBlocks.ROAD_WITH_YELLOW_LINE,
        "road_with_straight_line",
        new FasterJTextures().base("asphalt").lineSide("yellow_straight_line").lineTop("yellow_straight_line"));
    writeRoadBlockModelWithSlab(
        pack,
        RoadBlocks.ROAD_WITH_YELLOW_RA_LINE,
        "road_with_angle_line",
        new FasterJTextures().base("asphalt").lineSide("yellow_straight_line").lineTop("yellow_right_angle_line"));
    writeRoadBlockModelWithSlab(
        pack,
        RoadBlocks.ROAD_WITH_YELLOW_BA_LINE,
        "road_with_angle_line",
        new FasterJTextures().base("asphalt").lineSide("yellow_straight_line").lineTop("yellow_bevel_angle_line"));
    writeRoadBlockModelWithSlabWithMirrored(
        pack,
        RoadBlocks.ROAD_WITH_W_Y_RA_LINE,
        "road_with_angle_line",
        new FasterJTextures().base("asphalt").lineSide("yellow_straight_line").lineSide2("white_straight_line").lineTop("white_and_yellow_right_angle_line"));
    writeRoadBlockModelWithSlabWithMirrored(
        pack,
        RoadBlocks.ROAD_WITH_WT_N_RA_LINE,
        "road_with_angle_line",
        new FasterJTextures().base("asphalt").lineSide("white_straight_line").lineSide2("white_straight_thick_line").lineTop("white_thick_and_normal_right_angle_line"));
    writeRoadBlockModelWithSlabWithMirrored(
        pack,
        RoadBlocks.ROAD_WITH_WT_Y_RA_LINE,
        "road_with_angle_line",
        new FasterJTextures().base("asphalt").lineSide("yellow_straight_line").lineSide2("white_straight_thick_line").lineTop("white_thick_and_yellow_right_angle_line"));
    writeRoadBlockModelWithSlabWithMirrored(
        pack,
        RoadBlocks.ROAD_WITH_W_YD_RA_LINE,
        "road_with_angle_line",
        new FasterJTextures().base("asphalt").lineSide("yellow_straight_double_line").lineSide2("white_straight_line").lineTop("white_and_yellow_double_right_angle_line"));
    writeRoadBlockModelWithSlabWithMirrored(
        pack,
        RoadBlocks.ROAD_WITH_WT_YD_RA_LINE,
        "road_with_angle_line",
        new FasterJTextures().base("asphalt").lineSide("yellow_straight_double_line").lineSide2("white_straight_thick_line").lineTop("white_thick_and_yellow_double_right_angle_line"));
    writeRoadBlockModelWithSlab(
        pack,
        RoadBlocks.ROAD_WITH_WHITE_TS_LINE,
        "road_with_joint_line",
        new FasterJTextures().base("asphalt").lineSide("white_straight_line").lineTop("white_joint_line"));
    writeRoadBlockModelWithSlabWithMirrored(
        pack,
        RoadBlocks.ROAD_WITH_WHITE_S_BA_LINE,
        "road_with_straight_and_angle_line",
        FasterJTextures.ofP(
            "line_top_straight", "white_straight_line",
            "line_top_angle", "white_bevel_angle_line").lineSide("white_straight_line").base("asphalt"));
    writeRoadBlockModelWithSlab(
        pack,
        RoadBlocks.ROAD_WITH_WHITE_CROSS_LINE,
        "road_with_cross_line",
        new FasterJTextures().base("asphalt").lineSide("white_straight_line").lineTop("white_cross_line"));
    writeRoadBlockModelWithSlab(
        pack,
        RoadBlocks.ROAD_WITH_WHITE_OFFSET_LINE,
        "road_with_straight_line",
        new FasterJTextures().base("asphalt").lineSide("white_offset_straight_line").lineTop("white_offset_straight_line"));
    writeRoadBlockModelWithSlab(
        pack,
        RoadBlocks.ROAD_WITH_WHITE_DOUBLE_LINE,
        "road_with_straight_line",
        new FasterJTextures().base("asphalt").lineSide("white_straight_double_line").lineTop("white_straight_double_line"));
    writeRoadBlockModelWithSlab(
        pack,
        RoadBlocks.ROAD_WITH_WHITE_THICK_LINE,
        "road_with_straight_line",
        new FasterJTextures().base("asphalt").lineSide("white_straight_thick_line").lineTop("white_straight_thick_line"));
    writeRoadBlockModelWithSlab(
        pack,
        RoadBlocks.ROAD_WITH_YELLOW_OFFSET_LINE,
        "road_with_straight_line",
        new FasterJTextures().base("asphalt").lineSide("yellow_offset_straight_line").lineTop("yellow_offset_straight_line"));
    writeRoadBlockModelWithSlab(
        pack,
        RoadBlocks.ROAD_WITH_YELLOW_DOUBLE_LINE,
        "road_with_straight_line",
        new FasterJTextures().base("asphalt").lineSide("yellow_straight_double_line").lineTop("yellow_straight_double_line"));
    writeRoadBlockModelWithSlab(
        pack,
        RoadBlocks.ROAD_WITH_YELLOW_THICK_LINE,
        "road_with_straight_line",
        new FasterJTextures().base("asphalt").lineSide("yellow_straight_thick_line").lineTop("yellow_straight_thick_line"));
    writeRoadBlockModelWithSlabWithMirrored(
        pack,
        RoadBlocks.ROAD_WITH_WHITE_RA_LINE_OFFSET_OUT,
        "road_with_angle_line",
        new FasterJTextures().base("asphalt").lineSide("white_straight_line").lineSide2("white_offset_straight_line").lineTop("white_right_angle_line_with_one_part_offset_out"));
    writeRoadBlockModelWithSlabWithMirrored(
        pack,
        RoadBlocks.ROAD_WITH_WHITE_RA_LINE_OFFSET_IN,
        "road_with_angle_line",
        new FasterJTextures().base("asphalt").lineSide("white_straight_line").lineSide2("white_offset_straight_line2").lineTop("white_right_angle_line_with_one_part_offset_in"));
    writeRoadBlockModelWithSlab(
        pack,
        RoadBlocks.ROAD_WITH_WHITE_TS_DOUBLE_LINE,
        "road_with_joint_line",
        new FasterJTextures().base("asphalt").lineSide("white_straight_line").lineSide2("white_straight_double_line").lineTop("white_joint_line_with_double_side"));
    writeRoadBlockModelWithSlab(
        pack,
        RoadBlocks.ROAD_WITH_WHITE_TS_THICK_LINE,
        "road_with_joint_line",
        new FasterJTextures().base("asphalt").lineSide("white_straight_line").lineSide2("white_straight_thick_line").lineTop("white_joint_line_with_thick_side"));
    writeRoadBlockModelWithSlabWithMirrored(
        pack,
        RoadBlocks.ROAD_WITH_WHITE_TS_OFFSET_LINE,
        "road_with_joint_line",
        new FasterJTextures().base("asphalt").lineSide("white_straight_line").lineSide2("white_offset_straight_line").lineTop("white_joint_line_with_offset_side"));
    writeRoadBlockModelWithSlab(
        pack,
        RoadBlocks.ROAD_WITH_WHITE_THICK_TS_LINE,
        "road_with_joint_line",
        new FasterJTextures().base("asphalt").lineSide("white_straight_thick_line").lineSide2("white_straight_line").lineTop("white_thick_joint_line"));
    writeRoadBlockModelWithSlab(
        pack,
        RoadBlocks.ROAD_WITH_WHITE_DOUBLE_TS_LINE,
        "road_with_joint_line",
        new FasterJTextures().base("asphalt").lineSide("white_straight_double_line").lineSide2("white_straight_line").lineTop("white_double_joint_line"));
    writeRoadBlockModelWithSlab(
        pack,
        RoadBlocks.ROAD_WITH_YELLOW_TS_LINE,
        "road_with_joint_line",
        new FasterJTextures().base("asphalt").lineSide("yellow_straight_line").lineTop("yellow_joint_line"));
    writeRoadBlockModelWithSlab(
        pack,
        RoadBlocks.ROAD_WITH_Y_TS_W_LINE,
        "road_with_joint_line",
        new FasterJTextures().base("asphalt").lineSide("yellow_straight_line").lineSide2("white_straight_line").lineTop("yellow_joint_line_with_white_side"));
    writeRoadBlockModelWithSlab(
        pack,
        RoadBlocks.ROAD_WITH_W_TS_Y_LINE,
        "road_with_joint_line",
        new FasterJTextures().base("asphalt").lineSide("white_straight_line").lineSide2("yellow_straight_line").lineTop("white_joint_line_with_yellow_side"));
    writeRoadBlockModelWithSlab(
        pack,
        RoadBlocks.ROAD_WITH_W_TS_YD_LINE,
        "road_with_joint_line",
        new FasterJTextures().base("asphalt").lineSide("white_straight_line").lineSide2("yellow_straight_double_line").lineTop("white_joint_line_with_yellow_double_side"));
    writeRoadBlockModelWithSlab(
        pack,
        RoadBlocks.ROAD_WITH_WT_TS_YD_LINE,
        "road_with_joint_line",
        new FasterJTextures().base("asphalt").lineSide("white_straight_thick_line").lineSide2("yellow_straight_double_line").lineTop("white_thick_joint_line_with_yellow_double_side"));

    writeRoadBlockModelWithSlab(
        pack,
        RoadBlocks.ROAD_WITH_WHITE_AUTO_BA_LINE,
        "road_with_auto_line",
        new FasterJTextures()
            .base("asphalt")
            .line("white_auto_bevel_angle_line")
            .particle("asphalt"));
    writeRoadBlockModelWithSlab(
        pack,
        RoadBlocks.ROAD_WITH_WHITE_AUTO_RA_LINE,
        "road_with_auto_line",
        new FasterJTextures()
            .base("asphalt")
            .line("white_auto_right_angle_line")
            .particle("asphalt"));

    // 光源部分

    pack.addModel(
        new JModel(blockIdentifier("light"))
            .textures(new JTextures().var("base", blockString("white_light"))),
        blockIdentifier("white_light"));
    pack.addModel(
        new JModel(blockIdentifier("light"))
            .textures(new JTextures().var("base", blockString("yellow_light"))),
        blockIdentifier("yellow_light"));
    pack.addModel(
        new JModel(blockIdentifier("light"))
            .textures(new JTextures().var("base", blockString("cyan_light"))),
        blockIdentifier("cyan_light"));

  }

  @Override
  public void pregen() {
    // 客户端部分
    addBlockModels(PACK);
    addItemModels(PACK);

    MishangUtils.blockStream().forEach(field -> {
      Object o;
      try {
        o = field.get(null);
      } catch (IllegalAccessException e) {
        return;
      }
      if (o instanceof ARRPGenerator) {
        final ARRPGenerator arrp = (ARRPGenerator) o;
        if (FabricLoader.getInstance().getEnvironmentType() == EnvType.CLIENT) {
          arrp.writeBlockModel(PACK);
          arrp.writeBlockStates(PACK);
          arrp.writeItemModel(PACK);
        }
        arrp.writeLootTable(PACK);
      }
    });

    // 服务端部分
    addTags();
    PACK.dump();
    RRPCallback.BEFORE_VANILLA.register(a -> a.add(PACK));
  }

  @Override
  public void onInitialize() {
    pregen();
  }
}
