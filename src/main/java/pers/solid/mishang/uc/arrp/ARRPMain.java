package pers.solid.mishang.uc.arrp;

import net.devtech.arrp.api.RRPCallback;
import net.devtech.arrp.api.RRPPreGenEntrypoint;
import net.devtech.arrp.api.RuntimeResourcePack;
import net.devtech.arrp.json.models.JModel;
import net.devtech.arrp.json.models.JTextures;
import net.devtech.arrp.json.tags.JTag;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.item.Item;
import net.minecraft.util.Identifier;
import pers.solid.mishang.uc.MishangUtils;
import pers.solid.mishang.uc.annotations.RegisterIdentifier;
import pers.solid.mishang.uc.annotations.SimpleModel;
import pers.solid.mishang.uc.block.*;
import pers.solid.mishang.uc.blocks.RoadBlocks;
import pers.solid.mishang.uc.blocks.RoadSlabBlocks;
import pers.solid.mishang.uc.item.MishangucItems;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Arrays;

/**
 * @since 0.1.7 本类应当在 onInitialize 的入口点中执行，而非 pregen 中。
 */
@SuppressWarnings({"SameParameterValue"})
public class ARRPMain implements RRPPreGenEntrypoint, ModInitializer {
  private static final RuntimeResourcePack PACK = RuntimeResourcePack.create("mishanguc");
  private static final Field[] FIELDS = MishangUtils.blockStream().toArray(Field[]::new);

  private static Identifier blockIdentifier(String path) {
    return new Identifier("mishanguc", "block/" + path);
  }

  private static String blockString(String path) {
    return blockIdentifier(path).toString();
  }

  private static void writeBlockModelForCubeAll(RuntimeResourcePack pack, ARRPGenerator block, String all) {
    pack.addModel(
        JModel.model("block/cube_all").textures(new FasterJTextures().varP("all", all)),
        block.getBlockModelIdentifier());
  }

  private static void writeBlockModelForSlabAll(RuntimeResourcePack pack, ARRPGenerator block, String all) {
    pack.addModel(
        JModel.model("block/slab")
            .textures(
                new FasterJTextures().top(all).side(all).bottom(all)),
        block.getBlockModelIdentifier());
    pack.addModel(
        JModel.model("block/slab_top")
            .textures(
                new FasterJTextures().top(all).side(all).bottom(all)),
        MishangUtils.identifierSuffix(block.getBlockModelIdentifier(), "_top"));
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
    final Identifier id = block.getBlockModelIdentifier();
    final Identifier slabId = RoadSlabBlocks.BLOCK_TO_SLABS.get(block).getBlockModelIdentifier();
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
    final Identifier id = block.getBlockModelIdentifier();
    final Identifier slabId = RoadSlabBlocks.BLOCK_TO_SLABS.get(block).getBlockModelIdentifier();
    writeRoadBlockModelWithSlab(pack, parent, textures, id, slabId);
    writeRoadBlockModelWithSlab(pack, parent + "_mirrored", textures, MishangUtils.identifierSuffix(id, "_mirrored"), MishangUtils.identifierSuffix(slabId, "_mirrored"));
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
    pack.addModel(JModel.model(blockIdentifier(parent)).textures(textures), id);
    pack.addModel(JModel.model(ARRPGenerator.slabOf(blockIdentifier(parent))).textures(textures), slabId);
    pack.addModel(JModel.model(ARRPGenerator.slabOf(blockIdentifier(parent)) + "_top").textures(textures), MishangUtils.identifierSuffix(slabId, "_top"));
  }

  private static void addTags() {
    final JTag asphaltRoadBlocks = new JTag();
    final JTag asphaltRoadSlabs = new JTag();
    final JTag whiteStripWallLights = new JTag();
    final JTag whiteWallLights = new JTag();
    final JTag whiteCornerLights = new JTag();
    final JTag whiteLightDecorations = new JTag();
    final JTag yellowStripWallLights = new JTag();
    final JTag yellowWallLights = new JTag();
    final JTag yellowCornerLights = new JTag();
    final JTag yellowLightDecorations = new JTag();
    final JTag cyanStripWallLights = new JTag();
    final JTag cyanWallLights = new JTag();
    final JTag cyanCornerLights = new JTag();
    final JTag cyanLightDecorations = new JTag();
    final JTag woodenWallSigns = new JTag();
    final JTag concreteWallSigns = new JTag();
    final JTag terracottaWallSigns = new JTag();
    final JTag wallSigns = new JTag();
    final JTag glowingConcreteWallSigns = new JTag();
    final JTag glowingTerracottaWallSigns = new JTag();
    final JTag glowingWallSigns = new JTag();
    for (Field field : FIELDS) {
      final Class<?> type = field.getType();
      final String name = field.getName().toLowerCase();
      final Identifier identifier = new Identifier("mishanguc", name);
      if (AbstractRoadBlock.class.isAssignableFrom(type) && name.startsWith("asphalt_")) {
        asphaltRoadBlocks.add(identifier);
      } else if (AbstractRoadSlabBlock.class.isAssignableFrom(type)
          && name.startsWith("asphalt_")) {
        asphaltRoadSlabs.add(identifier);
      } else if (StripWallLightBlock.class.isAssignableFrom(type) && name.startsWith("white_")) {
        whiteStripWallLights.add(identifier);
      } else if (WallLightBlock.class.isAssignableFrom(type) && name.startsWith("white_")) {
        whiteWallLights.add(identifier);
      } else if (CornerLightBlock.class.isAssignableFrom(type) && name.startsWith("white_")) {
        whiteCornerLights.add(identifier);
      } else if (StripWallLightBlock.class.isAssignableFrom(type) && name.startsWith("yellow_")) {
        yellowStripWallLights.add(identifier);
      } else if (WallLightBlock.class.isAssignableFrom(type) && name.startsWith("yellow_")) {
        yellowWallLights.add(identifier);
      } else if (CornerLightBlock.class.isAssignableFrom(type) && name.startsWith("yellow_")) {
        yellowCornerLights.add(identifier);
      } else if (StripWallLightBlock.class.isAssignableFrom(type) && name.startsWith("cyan_")) {
        cyanStripWallLights.add(identifier);
      } else if (WallLightBlock.class.isAssignableFrom(type) && name.startsWith("cyan_")) {
        cyanWallLights.add(identifier);
      } else if (CornerLightBlock.class.isAssignableFrom(type) && name.startsWith("cyan_")) {
        cyanCornerLights.add(identifier);
      } else if (AutoConnectWallLightBlock.class.isAssignableFrom(type)
          && name.startsWith("white_")) {
        whiteLightDecorations.add(identifier);
      } else if (GlowingWallSignBlock.class.isAssignableFrom(type)) {
        if (name.contains("concrete")) glowingConcreteWallSigns.add(identifier);
        else if (name.contains("terracotta")) glowingTerracottaWallSigns.add(identifier);
        else glowingWallSigns.add(identifier);
      } else if (WallSignBlock.class.isAssignableFrom(type)) {
        if (name.contains("concrete")) concreteWallSigns.add(identifier);
        else if (name.contains("terracotta")) terracottaWallSigns.add(identifier);
        else if (name.contains("oak_")
            || name.contains("birch_")
            || name.contains("spruce_")
            || name.contains("jungle")
            || name.contains("acacia_")
            || name.contains("warped_")
            || name.contains("crimson")) woodenWallSigns.tag(identifier);
        else wallSigns.add(identifier);
      }
    }
    whiteWallLights.tag(new Identifier("mishanguc", "white_strip_wall_lights"));
    yellowWallLights.tag(new Identifier("mishanguc", "yellow_strip_wall_lights"));
    cyanWallLights.tag(new Identifier("mishanguc", "cyan_strip_wall_lights"));
    wallSigns
        .tag(new Identifier("mishanguc", "wooden_wall_signs"))
        .tag(new Identifier("mishanguc", "concrete_wall_signs"))
        .tag(new Identifier("mishanguc", "terracotta_wall_signs"));
    glowingWallSigns
        .tag(new Identifier("mishanguc", "glowing_concrete_wall_signs"))
        .tag(new Identifier("mishanguc", "glowing_terracotta_wall_signs"));
    PACK.addTag(new Identifier("mishanguc", "blocks/road_blocks"), asphaltRoadBlocks);
    PACK.addTag(new Identifier("mishanguc", "items/road_blocks"), asphaltRoadBlocks);
    PACK.addTag(new Identifier("mishanguc", "blocks/road_slabs"), asphaltRoadSlabs);
    PACK.addTag(new Identifier("mishanguc", "items/road_slabs"), asphaltRoadSlabs);
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
                  JModel.model(parent)
                      .textures(
                          JModel.textures()
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
        JModel.model(blockIdentifier("light"))
            .textures(new JTextures().var("base", blockString("white_light"))),
        blockIdentifier("white_light"));
    pack.addModel(
        JModel.model(blockIdentifier("light"))
            .textures(new JTextures().var("base", blockString("yellow_light"))),
        blockIdentifier("yellow_light"));
    pack.addModel(
        JModel.model(blockIdentifier("light"))
            .textures(new JTextures().var("base", blockString("cyan_light"))),
        blockIdentifier("cyan_light"));

  }

  @Override
  public void pregen() {
    // 客户端部分
//    addBlockStates(PACK);
    addBlockModels(PACK);
    addItemModels(PACK);
//    addBlockItemModels(PACK);

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
    RRPCallback.BEFORE_VANILLA.register(a -> a.add(PACK));
  }

  @Override
  public void onInitialize() {
    pregen();
  }
}
