package pers.solid.mishang.uc.arrp;

import com.google.errorprone.annotations.CanIgnoreReturnValue;
import net.devtech.arrp.api.RRPCallbackConditional;
import net.devtech.arrp.api.RRPPreGenEntrypoint;
import net.devtech.arrp.api.RuntimeResourcePack;
import net.devtech.arrp.generator.BlockResourceGenerator;
import net.devtech.arrp.json.models.JModel;
import net.devtech.arrp.json.models.JTextures;
import net.devtech.arrp.json.tags.IdentifiedTag;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.resource.ResourceType;
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

  private static void addItemModels(RuntimeResourcePack pack) {
    Arrays.stream(MishangucItems.class.getFields())
        .filter(field -> {
          int modifier = field.getModifiers();
          return Modifier.isPublic(modifier)
              && Modifier.isStatic(modifier)
              && Item.class.isAssignableFrom(field.getType())
              && field.isAnnotationPresent(RegisterIdentifier.class)
              && field.isAnnotationPresent(SimpleModel.class);
        })
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
          pack.addModel(
              new JModel(parent)
                  .textures(new JTextures().layer0(texture.isEmpty() ? "mishanguc:item/" + name : texture)),
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
    final boolean dev = FabricLoader.getInstance().isDevelopmentEnvironment();
    if (!dev) generateResources(true, true);
    RRPCallbackConditional.BEFORE_VANILLA.register((resourceType, builder) -> builder.add(dev ? generateResources(resourceType == ResourceType.CLIENT_RESOURCES, resourceType == ResourceType.SERVER_DATA) : PACK));
  }

  /**
   * 为运行时资源包生成资源。在开发环境中，每次加载资源就会重新生成一次。在非开发环境中，游戏开始时生成一次，此后不再生成。
   */
  @CanIgnoreReturnValue
  private RuntimeResourcePack generateResources(boolean client, boolean server) {
    if (client) PACK.clearResources(ResourceType.CLIENT_RESOURCES);
    if (server) PACK.clearResources(ResourceType.SERVER_DATA);

    // 客户端部分
    if (client) {
      addBlockModels(PACK);
      addItemModels(PACK);
    }

    MishangUtils.blockStream().forEach(field -> {
      Object o;
      try {
        o = field.get(null);
      } catch (IllegalAccessException e) {
        return;
      }
      if (o instanceof BlockResourceGenerator) {
        final BlockResourceGenerator arrp = (BlockResourceGenerator) o;
        if (client) {
          arrp.writeBlockModel(PACK);
          arrp.writeBlockStates(PACK);
          arrp.writeItemModel(PACK);
        }
        if (server) {
          arrp.writeLootTable(PACK);
        }
      }
    });

    // 服务端部分
    if (server) {
      addTags();
    }

    return PACK;
  }

  @Override
  public void onInitialize() {
    pregen();
  }
}
