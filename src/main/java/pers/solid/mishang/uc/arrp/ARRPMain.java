package pers.solid.mishang.uc.arrp;

import com.google.common.collect.Lists;
import com.google.gson.JsonObject;
import net.devtech.arrp.api.RRPCallback;
import net.devtech.arrp.api.RRPPreGenEntrypoint;
import net.devtech.arrp.api.RuntimeResourcePack;
import net.devtech.arrp.json.blockstate.*;
import net.devtech.arrp.json.loot.JCondition;
import net.devtech.arrp.json.loot.JFunction;
import net.devtech.arrp.json.loot.JLootTable;
import net.devtech.arrp.json.models.*;
import net.devtech.arrp.json.tags.JTag;
import net.minecraft.block.SlabBlock;
import net.minecraft.block.enums.WallMountLocation;
import net.minecraft.item.Item;
import net.minecraft.util.DyeColor;
import net.minecraft.util.Identifier;
import net.minecraft.util.Util;
import net.minecraft.util.math.Direction;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import pers.solid.mishang.uc.MishangUc;
import pers.solid.mishang.uc.MishangUtils;
import pers.solid.mishang.uc.annotations.RegisterIdentifier;
import pers.solid.mishang.uc.annotations.SimpleModel;
import pers.solid.mishang.uc.block.*;
import pers.solid.mishang.uc.item.MishangucItems;
import pers.solid.mishang.uc.mixin.JBlockModelAccessor;
import pers.solid.mishang.uc.mixin.JStateAccessor;
import pers.solid.mishang.uc.mixin.JVariantAccessor;
import pers.solid.mishang.uc.util.HorizontalCornerDirection;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.*;

@SuppressWarnings({"SameParameterValue", "AlibabaClassNamingShouldBeCamel"})
public class ARRPMain implements RRPPreGenEntrypoint {
  private static final RuntimeResourcePack PACK = RuntimeResourcePack.create("mishanguc");
  private static final Field[] FIELDS = MishangUtils.blockStream().toArray(Field[]::new);

  private static Identifier blockIdentifier(String path) {
    return new Identifier("mishanguc", "block/" + path);
  }

  private static Identifier itemIdentifier(String path) {
    return new Identifier("mishanguc", "item/" + path);
  }

  private static String blockString(String path) {
    return blockIdentifier(path).toString();
  }

  private static JElement fullElement(JFaces faces) {
    JElement element = new JElement();
    element.from(0, 0, 0).to(16, 16, 16).faces(faces);
    return element;
  }

  private static @Nullable JFace face(@Nullable String texture, @Nullable Direction direction) {
    if (texture == null) {
      return null;
    }
    JFace face = new JFace(texture);
    if (direction != null) {
      face.cullface(direction);
    }
    return face;
  }

  private static JFaces faces(
      @Nullable String down,
      @Nullable String up,
      @Nullable String north,
      @Nullable String south,
      @Nullable String west,
      @Nullable String east) {
    JFaces faces = new JFaces();
    faces
        .down(face(down, Direction.DOWN))
        .up(face(up, Direction.UP))
        .north(face(north, Direction.NORTH))
        .south(face(south, Direction.SOUTH))
        .west(face(west, Direction.WEST))
        .east(face(east, Direction.EAST));
    return faces;
  }

  private static JFaces faces(Map<Direction, @Nullable String> map) {
    return faces(
        map.get(Direction.DOWN),
        map.get(Direction.UP),
        map.get(Direction.NORTH),
        map.get(Direction.SOUTH),
        map.get(Direction.WEST),
        map.get(Direction.EAST));
  }

  private static JFaces faces(@Nullable String all) {
    return faces(all, all, all, all, all, all);
  }

  private static void addBlockItemModel(String name) {
    ARRPMain.PACK.addModel(JModel.model(blockIdentifier(name)), itemIdentifier(name));
  }

  private static void addSimpleBlockLootTable(String name) {
    PACK.addLootTable(
        new Identifier("mishanguc", "blocks/" + name),
        JLootTable.loot("minecraft:block")
            .pool(
                JLootTable.pool()
                    .rolls(1)
                    .entry(JLootTable.entry().type("minecraft:item").name("mishanguc:" + name))
                    .condition(JLootTable.predicate("minecraft:survives_explosion"))));
  }

  private static void addSlabBlockLootTable(String name) {
    PACK.addLootTable(
        new Identifier("mishanguc", "blocks/" + name),
        JLootTable.loot("minecraft:block")
            .pool(
                JLootTable.pool()
                    .rolls(1)
                    .entry(
                        JLootTable.entry()
                            .type("minecraft:item")
                            .name("mishanguc:" + name)
                            .function(
                                new JFunction("set_count")
                                    .condition(
                                        new JCondition("block_state_property")
                                            .parameter("block", "mishanguc:" + name)
                                            .parameter(
                                                "properties",
                                                Util.make(
                                                    new JsonObject(),
                                                    jsonObject ->
                                                        jsonObject.addProperty("type", "double"))))
                                    .parameter("count", 2))
                            .function(new JFunction("explosion_decay")))));
  }

  private static void addCubeAll(RuntimeResourcePack PACK, String path, String all) {
    PACK.addModel(
        JModel.model("block/cube_all").textures(JModel.textures().var("all", blockString(all))),
        blockIdentifier(path));
  }

  private static void addSlabAll(RuntimeResourcePack PACK, String path, String all) {
    PACK.addModel(
        JModel.model("block/slab")
            .textures(
                JModel.textures()
                    .var("top", blockString(all))
                    .var("side", blockString(all))
                    .var("bottom", blockString(all))),
        blockIdentifier(path));
    PACK.addModel(
        JModel.model("block/slab_top")
            .textures(
                JModel.textures()
                    .var("top", blockString(all))
                    .var("side", blockString(all))
                    .var("bottom", blockString(all))),
        blockIdentifier(path + "_top"));
  }

  /** 运行此方法需确保其楼梯名称正好为 path + "_slab"。 */
  private static void addCubeAllWithSlab(final RuntimeResourcePack PACK, String path, String all) {
    addCubeAll(PACK, path, all);
    addSlabAll(PACK, slabOf(path), all);
  }

  /**
   * 添加一个方块以及其台阶方块的资源包。仅用于此模组。
   *
   * @param PACK 运行时资源包。
   * @param path 方块路径。省略命名空间和“block/”前缀，且确保 path+"_slab" 的方块要存在。
   * @param parent 资源包的 parent。应当保证 parent、parent+"_slab" 和 parent+"_slab_top"都要存在。
   * @param textures 材质变量。三个 parent 都应该使用相同的材质。
   */
  private static void addRoadWithSlab(
      RuntimeResourcePack PACK, String path, String parent, JTextures textures) {
    PACK.addModel(JModel.model(blockIdentifier(parent)).textures(textures), blockIdentifier(path));
    PACK.addModel(
        JModel.model(slabOf(blockIdentifier(parent))).textures(textures),
        blockIdentifier(slabOf(path)));
    PACK.addModel(
        JModel.model(slabOf(blockIdentifier(parent)) + "_top").textures(textures),
        blockIdentifier(slabOf(path) + "_top"));
  }

  private static String slabOf(String string) {
    if (string.contains("_road")) {
      return string.replaceFirst("_road", "_road_slab");
    } else {
      return string + "_slab";
    }
  }

  private static Identifier slabOf(Identifier identifier) {
    return new Identifier(identifier.getNamespace(), slabOf(identifier.getPath()));
  }

  private static JTextures textures(String all) {
    return new JTextures().var("all", blockString(all));
  }

  private static JTextures textures(String base, String line_side, String line_top) {
    return new JTextures()
        .var("base", blockString(base))
        .var("line_side", blockString(line_side))
        .var("line_top", blockString(line_top));
  }

  private static JTextures textures2(
      String base, String line_side, String line_side2, String line_top) {
    return new JTextures()
        .var("base", blockString(base))
        .var("line_side", blockString(line_side))
        .var("line_side2", blockString(line_side2))
        .var("line_top", blockString(line_top));
  }

  private static JTextures textures(
      String base, String line_side, String line_top_straight, String line_top_angle) {
    return new JTextures()
        .var("base", blockString(base))
        .var("line_side", blockString(line_side))
        .var("line_top_straight", blockString(line_top_straight))
        .var("line_top_angle", blockString(line_top_angle));
  }

  private static void addTags() {
    final JTag asphaltRoadBlocks = new JTag();
    final JTag asphaltRoadSlabs = new JTag();
    final JTag whiteStripWallLights = new JTag();
    final JTag whiteWallLights = new JTag();
    final JTag whiteCornerLights = new JTag();
    final JTag whiteLightDecorations = new JTag();
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
    wallSigns
        .tag(new Identifier("mishanguc", "wooden_wall_signs"))
        .tag(new Identifier("mishanguc", "concrete_wall_signs"))
        .tag(new Identifier("mishanguc", "terracotta_wall_signs"));
    glowingWallSigns
        .tag(new Identifier("mishanguc", "glowing_concrete_wall_signs"))
        .tag(new Identifier("mishanguc", "glowing_terracotta_wall_signs"));
    PACK.addTag(new Identifier("mishanguc", "blocks/asphalt_road_blocks"), asphaltRoadBlocks);
    PACK.addTag(new Identifier("mishanguc", "items/asphalt_road_blocks"), asphaltRoadBlocks);
    PACK.addTag(new Identifier("mishanguc", "blocks/asphalt_road_slabs"), asphaltRoadSlabs);
    PACK.addTag(new Identifier("mishanguc", "items/asphalt_road_slabs"), asphaltRoadSlabs);
    PACK.addTag(
        new Identifier("mishanguc", "blocks/white_strip_wall_lights"), whiteStripWallLights);
    PACK.addTag(new Identifier("mishanguc", "items/white_strip_wall_lights"), whiteStripWallLights);
    PACK.addTag(new Identifier("mishanguc", "blocks/white_wall_lights"), whiteWallLights);
    PACK.addTag(new Identifier("mishanguc", "items/white_wall_lights"), whiteWallLights);
    PACK.addTag(new Identifier("mishanguc", "blocks/white_corner_lights"), whiteCornerLights);
    PACK.addTag(new Identifier("mishanguc", "items/white_corner_lights"), whiteCornerLights);
    PACK.addTag(
        new Identifier("mishanguc", "blocks/white_light_decorations"), whiteLightDecorations);
    PACK.addTag(
        new Identifier("mishanguc", "items/white_light_decorations"), whiteLightDecorations);
  }

  private static void addBlockLootTables() {
    for (Field field : FIELDS) {
      String name = field.getAnnotation(RegisterIdentifier.class).value();
      if (name.isEmpty()) {
        name = field.getName().toLowerCase();
      }
      if (SlabBlock.class.isAssignableFrom(field.getType())) {
        addSlabBlockLootTable(name);
      } else {
        addSimpleBlockLootTable(name);
      }
    }
  }

  private static JState composeStateForSlab(JState stateForFull) {
    final List<JVariant> variants = ((JStateAccessor) (Object) stateForFull).getVariants();
    final List<JVariant> slabVariants = Lists.newArrayList();
    for (JVariant variant : variants) {
      final Map<String, JBlockModel> models = ((JVariantAccessor) (Object) variant).getModels();
      final JVariant slabVariant = new JVariant();
      for (Map.Entry<String, JBlockModel> entry : models.entrySet()) {
        final String key = entry.getKey();
        final JBlockModel value = entry.getValue();
        final Identifier model = ((JBlockModelAccessor) value).getModel();
        slabVariant
            .put(
                key.isEmpty() ? "type=bottom" : key + ",type=bottom",
                Util.make(
                    value.clone(),
                    jBlockModel ->
                        ((JBlockModelAccessor) jBlockModel)
                            .setModel(
                                new Identifier(model.getNamespace(), slabOf(model.getPath())))))
            .put(
                key.isEmpty() ? "type=bottom" : key + ",type=top",
                Util.make(
                    value.clone(),
                    jBlockModel ->
                        ((JBlockModelAccessor) jBlockModel)
                            .setModel(
                                new Identifier(
                                    model.getNamespace(), slabOf(model.getPath()) + "_top"))))
            .put(
                key.isEmpty() ? "type=bottom" : key + ",type=double",
                Util.make(
                    value.clone(),
                    jBlockModel ->
                        ((JBlockModelAccessor) jBlockModel)
                            .setModel(new Identifier(model.getNamespace(), (model.getPath())))));
      }
      slabVariants.add(slabVariant);
    }
    return JState.state(slabVariants.toArray(new JVariant[] {}));
  }

  private static void addBlockStates() {
    final JState state1 =
        stateForAngleLineWithOnePartOffset(
            "asphalt_road_with_white_right_angle_line_with_one_part_offset_out");
    PACK.addBlockState(
        state1,
        new Identifier(
            "mishanguc", "asphalt_road_with_white_right_angle_line_with_one_part_offset_out"));
    PACK.addBlockState(
        composeStateForSlab(state1),
        new Identifier(
            "mishanguc", "asphalt_road_slab_with_white_right_angle_line_with_one_part_offset_out"));
    final JState state2 =
        stateForAngleLineWithOnePartOffset(
            "asphalt_road_with_white_right_angle_line_with_one_part_offset_in");
    PACK.addBlockState(
        state2,
        new Identifier(
            "mishanguc", "asphalt_road_with_white_right_angle_line_with_one_part_offset_in"));
    PACK.addBlockState(
        composeStateForSlab(state2),
        new Identifier(
            "mishanguc", "asphalt_road_slab_with_white_right_angle_line_with_one_part_offset_in"));
    final JState state3 =
        stateForJointLineWithOffsetSide("asphalt_road_with_white_joint_line_with_offset_side");
    PACK.addBlockState(
        state3, new Identifier("mishanguc", "asphalt_road_with_white_joint_line_with_offset_side"));
    PACK.addBlockState(
        composeStateForSlab(state3),
        new Identifier("mishanguc", "asphalt_road_slab_with_white_joint_line_with_offset_side"));

    for (String modelName :
        new String[] {
          "asphalt_road_with_white_and_yellow_right_angle_line",
          "asphalt_road_with_white_and_yellow_double_right_angle_line",
          "asphalt_road_with_white_thick_and_yellow_double_right_angle_line"
        }) {
      final JState state = stateForDiffAngleLine(modelName);
      PACK.addBlockState(state, new Identifier("mishanguc", modelName));
      PACK.addBlockState(
          composeStateForSlab(state), new Identifier("mishanguc", slabOf(modelName)));
    }

    for (String modelName :
        new String[] {
          "asphalt_road_with_white_joint_line",
          "asphalt_road_with_white_joint_line_with_double_side",
          "asphalt_road_with_white_joint_line_with_thick_side",
          "asphalt_road_with_white_double_joint_line",
          "asphalt_road_with_white_thick_joint_line",
          "asphalt_road_with_yellow_joint_line",
          "asphalt_road_with_yellow_joint_line_with_white_side",
          "asphalt_road_with_white_joint_line_with_yellow_side",
          "asphalt_road_with_white_joint_line_with_yellow_double_side",
          "asphalt_road_with_white_thick_joint_line_with_yellow_double_side"
        }) {
      final JState state = stateForHorizontalFacingBlock(modelName);
      PACK.addBlockState(state, new Identifier("mishanguc", modelName));
      PACK.addBlockState(
          composeStateForSlab(state), new Identifier("mishanguc", slabOf(modelName)));
    }

    for (String modelName :
        new String[] {
          "asphalt_road_with_white_right_angle_line",
          "asphalt_road_with_white_bevel_angle_line",
          "asphalt_road_with_yellow_right_angle_line",
          "asphalt_road_with_yellow_bevel_angle_line",
          "asphalt_road_with_white_thick_and_normal_right_angle_line",
          "asphalt_road_with_white_thick_and_yellow_right_angle_line"
        }) {
      final JState state = stateForHorizontalCornerFacingBlock(modelName);
      PACK.addBlockState(state, new Identifier("mishanguc", modelName));
      PACK.addBlockState(
          composeStateForSlab(state), new Identifier("mishanguc", slabOf(modelName)));
    }

    PACK.addBlockState(
        composeStateForAutoConnectBlock("white_wall_light_simple_decoration"),
        new Identifier("mishanguc", "white_wall_light_simple_decoration"));
    PACK.addBlockState(
        composeStateForAutoConnectBlock("white_wall_light_point_decoration"),
        new Identifier("mishanguc", "white_wall_light_point_decoration"));
    PACK.addBlockState(
        composeStateForAutoConnectBlock("white_wall_light_rhombus_decoration"),
        new Identifier("mishanguc", "white_wall_light_rhombus_decoration"));
    PACK.addBlockState(
        composeStateForAutoConnectBlock("white_wall_light_hash_decoration"),
        new Identifier("mishanguc", "white_wall_light_hash_decoration"));
    PACK.addBlockState(
        composeStateForAutoConnectBlock("white_wall_light_round_decoration"),
        new Identifier("mishanguc", "white_wall_light_round_decoration"));

    for (DyeColor color : DyeColor.values()) {
      addStateForHungSign(PACK, color.asString() + "_concrete");
      addStateForHungSign(PACK, color.asString() + "_terracotta");
      addStateForHungSignBar(PACK, color.asString() + "_concrete");
      addStateForHungSignBar(PACK, color.asString() + "_terracotta");
      addStateForHungSign(PACK, "glowing_" + color.asString() + "_concrete");
      addStateForHungSign(PACK, "glowing_" + color.asString() + "_terracotta");
    }

    addStateForHungSign(PACK, "glowing_netherrack");
    addStateForHungSign(PACK, "glowing_nether_brick");
    addStateForHungSign(PACK, "glowing_blackstone");
    addStateForHungSign(PACK, "glowing_polished_blackstone");
    addStateForHungSignBar(PACK, "netherrack");
    addStateForHungSignBar(PACK, "nether_brick");
    addStateForHungSignBar(PACK, "blackstone");
    addStateForHungSignBar(PACK, "polished_blackstone");

    // 对于墙上的告示牌
    addStateForWallSign(PACK, "oak_wall_sign");
    addStateForWallSign(PACK, "spruce_wall_sign");
    addStateForWallSign(PACK, "birch_wall_sign");
    addStateForWallSign(PACK, "jungle_wall_sign");
    addStateForWallSign(PACK, "acacia_wall_sign");
    addStateForWallSign(PACK, "dark_oak_wall_sign");
    addStateForWallSign(PACK, "crimson_wall_sign");
    addStateForWallSign(PACK, "warped_wall_sign");
    for (DyeColor color : DyeColor.values()) {
      addStateForWallSign(PACK, color.asString() + "_concrete_wall_sign");
      addStateForWallSign(PACK, color.asString() + "_terracotta_wall_sign");
      addStateForWallSign(PACK, "glowing_" + color.asString() + "_concrete_wall_sign");
      addStateForWallSign(PACK, "glowing_" + color.asString() + "_terracotta_wall_sign");
      addStateForWallSign(PACK, "full_" + color.asString() + "_concrete_wall_sign");
      addStateForWallSign(PACK, "full_" + color.asString() + "_terracotta_wall_sign");
    }
    addStateForWallSign(PACK, "invisible_wall_sign");
  }

  private static JState stateForJointLineWithOffsetSide(String modelName) {
    JVariant variant = new JVariant();
    // 一侧的短线所朝向的方向。
    for (Direction direction : Direction.Type.HORIZONTAL) {
      final @NotNull Direction offsetDirection1 = direction.rotateYClockwise();
      // direction 的右偏方向
      final @NotNull HorizontalCornerDirection facing1 =
          Objects.requireNonNull(
              HorizontalCornerDirection.fromDirections(direction, offsetDirection1));
      final @NotNull Direction offsetDirection2 = direction.rotateYCounterclockwise();
      // direction 的左偏方向
      final @NotNull HorizontalCornerDirection facing2 =
          Objects.requireNonNull(
              HorizontalCornerDirection.fromDirections(direction, offsetDirection2));
      variant
          .put(
              String.format(
                  "facing=%s,axis=%s", facing1.asString(), offsetDirection1.getAxis().asString()),
              JState.model(blockIdentifier(modelName)).y((int) (direction.asRotation())))
          .put(
              String.format(
                  "facing=%s,axis=%s", facing2.asString(), offsetDirection2.getAxis().asString()),
              JState.model(blockIdentifier(modelName + "_mirrored"))
                  .y((int) (direction.asRotation())));
    }
    return JState.state(variant);
  }

  /**
   * 为告示牌创建方块状态文件。
   *
   * @see #addModelsForHungSign
   * @see #addModelsForGlowingHungSign
   * @param name 告示牌的非完整名称。
   */
  private static void addStateForHungSign(@NotNull RuntimeResourcePack PACK, String name) {
    final String path = name + "_hung_sign";
    PACK.addBlockState(
        JState.state(
            new JMultipart()
                .addModel(new JBlockModel(blockIdentifier(path + "_body")).uvlock())
                .when(new JWhen().add("axis", "z")),
            new JMultipart()
                .addModel(new JBlockModel(blockIdentifier(path + "_body")).uvlock().y(90))
                .when(new JWhen().add("axis", "x")),
            new JMultipart()
                .addModel(new JBlockModel(blockIdentifier(path + "_top_bar")).uvlock())
                .when(new FixedWhen().add("axis", "z").add("left", "false").add("right", "true")),
            new JMultipart()
                .addModel(new JBlockModel(blockIdentifier(path + "_top_bar")).uvlock().y(180))
                .when(new FixedWhen().add("axis", "z").add("left", "true").add("right", "false")),
            new JMultipart()
                .addModel(new JBlockModel(blockIdentifier(path + "_top_bar")).uvlock().y(-90))
                .when(new FixedWhen().add("axis", "x").add("left", "false").add("right", "true")),
            new JMultipart()
                .addModel(new JBlockModel(blockIdentifier(path + "_top_bar")).uvlock().y(90))
                .when(new FixedWhen().add("axis", "x").add("left", "true").add("right", "false")),
            new JMultipart()
                .addModel(new JBlockModel(blockIdentifier(path + "_top_bar_edge")).uvlock())
                .when(new FixedWhen().add("axis", "z").add("left", "false").add("right", "false")),
            new JMultipart()
                .addModel(new JBlockModel(blockIdentifier(path + "_top_bar_edge")).uvlock().y(180))
                .when(new FixedWhen().add("axis", "z").add("left", "false").add("right", "false")),
            new JMultipart()
                .addModel(new JBlockModel(blockIdentifier(path + "_top_bar_edge")).uvlock().y(90))
                .when(new FixedWhen().add("axis", "x").add("left", "false").add("right", "false")),
            new JMultipart()
                .addModel(new JBlockModel(blockIdentifier(path + "_top_bar_edge")).uvlock().y(270))
                .when(new FixedWhen().add("axis", "x").add("left", "false").add("right", "false"))),
        new Identifier("mishanguc", path));
  }

  private static void addStateForHungSignBar(@NotNull RuntimeResourcePack PACK, String name) {
    final String path = name + "_hung_sign";
    PACK.addBlockState(
        JState.state(
            new JMultipart()
                .addModel(new JBlockModel(blockIdentifier(path + "_bar_central")).uvlock())
                .when(new FixedWhen().add("left", "true").add("right", "true")),
            new JMultipart()
                .addModel(new JBlockModel(blockIdentifier(path + "_bar")).uvlock())
                .when(new FixedWhen().add("axis", "z").add("left", "false").add("right", "true")),
            new JMultipart()
                .addModel(new JBlockModel(blockIdentifier(path + "_bar")).uvlock().y(180))
                .when(new FixedWhen().add("axis", "z").add("left", "true").add("right", "false")),
            new JMultipart()
                .addModel(new JBlockModel(blockIdentifier(path + "_bar")).uvlock().y(-90))
                .when(new FixedWhen().add("axis", "x").add("left", "false").add("right", "true")),
            new JMultipart()
                .addModel(new JBlockModel(blockIdentifier(path + "_bar")).uvlock().y(90))
                .when(new FixedWhen().add("axis", "x").add("left", "true").add("right", "false")),
            new JMultipart()
                .addModel(new JBlockModel(blockIdentifier(path + "_bar_edge")).uvlock())
                .when(new FixedWhen().add("axis", "z").add("left", "false").add("right", "false")),
            new JMultipart()
                .addModel(new JBlockModel(blockIdentifier(path + "_bar_edge")).uvlock().y(180))
                .when(new FixedWhen().add("axis", "z").add("left", "false").add("right", "false")),
            new JMultipart()
                .addModel(new JBlockModel(blockIdentifier(path + "_bar_edge")).uvlock().y(90))
                .when(new FixedWhen().add("axis", "x").add("left", "false").add("right", "false")),
            new JMultipart()
                .addModel(new JBlockModel(blockIdentifier(path + "_bar_edge")).uvlock().y(270))
                .when(new FixedWhen().add("axis", "x").add("left", "false").add("right", "false"))),
        new Identifier("mishanguc", path + "_bar"));
  }

  private static void addStateForWallSign(@NotNull RuntimeResourcePack PACK, String name) {
    final JVariant jVariant = new JVariant();
    final JState state = JState.state(jVariant);
    for (WallMountLocation wallMountLocation : WallMountLocation.values()) {
      final int x;
      switch (wallMountLocation) {
        case WALL:
          x = 0;
          break;
        case FLOOR:
          x = 90;
          break;
        default:
          x = -90;
          break;
      }
      for (Direction direction : Direction.Type.HORIZONTAL) {
        float y = direction.asRotation();
        jVariant.put(
            String.format("face=%s,facing=%s", wallMountLocation.asString(), direction.asString()),
            new JBlockModel(blockIdentifier(name)).x(x).y((int) y).uvlock());
      }
    }
    PACK.addBlockState(state, new Identifier("mishanguc", name));
  }

  private static JState composeStateForAutoConnectBlock(String name) {
    List<JMultipart> parts = new ArrayList<>();
    for (Direction facing : Direction.values()) {
      // 中心装饰物
      parts.add(
          new JMultipart()
              .addModel(
                  new JBlockModel(blockIdentifier(name + "_center"))
                      .y(
                          facing.getAxis() == Direction.Axis.Y
                              ? 0
                              : ((int) (facing.asRotation() + 180)))
                      .x(facing == Direction.DOWN ? 180 : facing == Direction.UP ? 0 : 90))
              .when(new JWhen().add("facing", facing.asString())));

      // 连接物
      // 共有两种连接物模型：一种是位于底部或顶部的朝南连接，可以通过x和y的旋转得到位于底部朝向任意方向的连接，以及位于侧面朝向垂直方向的连接。
      // 第二种是位于侧面的朝东连接，可以通过x和y的旋转得到任意水平方向上的，以及底部或顶部任意连接。
      for (Direction direction : Direction.values()) {
        final Direction.Axis axis = direction.getAxis();
        final int x, y;
        final String modelName;
        if (axis == facing.getAxis()) {
          continue;
        }
        if (facing == Direction.UP) {
          modelName = name + "_connection";
          x = 0;
          y = (int) direction.asRotation();
        } else if (facing == Direction.DOWN) {
          modelName = name + "_connection";
          x = 180;
          y = (int) direction.asRotation() + 180;
        } else if (direction == Direction.UP) {
          modelName = name + "_connection";
          x = 90;
          y = (int) facing.asRotation() + 180;
        } else if (direction == Direction.DOWN) {
          modelName = name + "_connection";
          x = -90;
          y = (int) facing.asRotation();
        } else if (direction == facing.rotateYCounterclockwise()) {
          modelName = name + "_connection2";
          x = 0;
          y = (int) facing.asRotation();
        } else if (direction == facing.rotateYClockwise()) {
          modelName = name + "_connection2";
          x = 180;
          y = (int) facing.asRotation() + 180;
        } else {
          MishangUc.MISHANG_LOGGER.error(
              String.format(
                  "Unknown state to generate models: facing=%s,direction=%s",
                  facing.asString(), direction.asString()));
          continue;
        }
        parts.add(
            new JMultipart()
                .addModel(new JBlockModel(blockIdentifier(modelName)).x(x).y(y).uvlock())
                .when(
                    new FixedWhen()
                        .add("facing", facing.asString())
                        .add(direction.asString(), "true")));
      }
    }
    return JState.state(parts.toArray(new JMultipart[] {}));
  }

  @NotNull
  private static JState stateForAngleLineWithOnePartOffset(@NotNull String moduleName) {
    JVariant variant = new JVariant();
    for (Direction direction : Direction.Type.HORIZONTAL) {
      // direction：正中线所朝的方向
      final Direction offsetDirection1 = direction.rotateYClockwise();
      final Direction offsetDirection2 = direction.rotateYCounterclockwise();
      variant.put(
          String.format(
              "facing=%s,axis=%s",
              Objects.requireNonNull(
                      HorizontalCornerDirection.fromDirections(direction, offsetDirection1))
                  .asString(),
              direction.getAxis().asString()),
          JState.model("mishanguc:block/" + moduleName).y((int) (direction.asRotation())));
      variant.put(
          String.format(
              "facing=%s,axis=%s",
              Objects.requireNonNull(
                      HorizontalCornerDirection.fromDirections(direction, offsetDirection2))
                  .asString(),
              direction.getAxis().asString()),
          JState.model("mishanguc:block/" + moduleName + "_mirrored")
              .y((int) (direction.asRotation())));
    }
    return JState.state(variant);
  }

  @NotNull
  private static JState stateForDiffAngleLine(@NotNull String modelName) {
    JVariant variant = new JVariant();
    // 一侧的短线所朝向的方向。
    for (Direction direction : Direction.Type.HORIZONTAL) {
      final @NotNull Direction offsetDirection1 = direction.rotateYClockwise();
      // direction 的右偏方向
      final @NotNull HorizontalCornerDirection facing1 =
          Objects.requireNonNull(
              HorizontalCornerDirection.fromDirections(direction, offsetDirection1));
      final @NotNull Direction offsetDirection2 = direction.rotateYCounterclockwise();
      // direction 的左偏方向
      final @NotNull HorizontalCornerDirection facing2 =
          Objects.requireNonNull(
              HorizontalCornerDirection.fromDirections(direction, offsetDirection2));
      variant
          .put(
              String.format(
                  "facing=%s,axis=%s", facing1.asString(), direction.getAxis().asString()),
              JState.model(blockIdentifier(modelName)).y((int) (direction.asRotation())))
          .put(
              String.format(
                  "facing=%s,axis=%s", facing2.asString(), direction.getAxis().asString()),
              JState.model(blockIdentifier(modelName + "_mirrored"))
                  .y((int) (direction.asRotation())));
    }
    return JState.state(variant);
  }

  @NotNull
  private static JState stateForHorizontalFacingBlock(@NotNull String modelName) {
    JVariant variant = new JVariant();
    for (Direction direction : Direction.Type.HORIZONTAL) {
      variant.put(
          "facing",
          direction,
          new JBlockModel(blockIdentifier(modelName)).y((int) direction.asRotation()));
    }
    return JState.state(variant);
  }

  @NotNull
  private static JState stateForHorizontalCornerFacingBlock(@NotNull String modelName) {
    JVariant variant = new JVariant();
    for (HorizontalCornerDirection direction : HorizontalCornerDirection.values()) {
      variant.put(
          "facing",
          direction,
          new JBlockModel(blockIdentifier(modelName)).y(direction.asRotation() - 45));
    }
    return JState.state(variant);
  }

  private static void addItemModels() {
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
              PACK.addModel(
                  JModel.model(parent)
                      .textures(
                          JModel.textures()
                              .layer0(texture.isEmpty() ? "mishanguc:item/" + name : texture)),
                  new Identifier("mishanguc", "item/" + name));
            });
  }

  private static void addBlockItemModels() {
    for (Field field : FIELDS) {
      String name = field.getAnnotation(RegisterIdentifier.class).value();
      if (name.isEmpty()) {
        name = field.getName().toLowerCase();
      }
      addBlockItemModel(name);
    }
  }

  @SuppressWarnings("AlibabaMethodTooLong")
  private static void addBlockModels() {
    addCubeAll(PACK, "asphalt_road_block", "asphalt");
    addSlabAll(PACK, "asphalt_road_slab", "asphalt");
    addCubeAllWithSlab(PACK, "asphalt_road_filled_with_white", "white_ink");
    addCubeAllWithSlab(PACK, "asphalt_road_filled_with_yellow", "yellow_ink");
    addRoadWithSlab(
        PACK,
        "asphalt_road_with_white_straight_line",
        "road_with_straight_line",
        textures("asphalt", "white_straight_line", "white_straight_line"));
    addRoadWithSlab(
        PACK,
        "asphalt_road_with_white_right_angle_line",
        "road_with_angle_line",
        textures("asphalt", "white_straight_line", "white_right_angle_line"));
    addRoadWithSlab(
        PACK,
        "asphalt_road_with_white_bevel_angle_line",
        "road_with_angle_line",
        textures("asphalt", "white_straight_line", "white_bevel_angle_line"));
    addRoadWithSlab(
        PACK,
        "asphalt_road_with_yellow_straight_line",
        "road_with_straight_line",
        textures("asphalt", "yellow_straight_line", "yellow_straight_line"));
    addRoadWithSlab(
        PACK,
        "asphalt_road_with_yellow_right_angle_line",
        "road_with_angle_line",
        textures("asphalt", "yellow_straight_line", "yellow_right_angle_line"));
    addRoadWithSlab(
        PACK,
        "asphalt_road_with_yellow_bevel_angle_line",
        "road_with_angle_line",
        textures("asphalt", "yellow_straight_line", "yellow_bevel_angle_line"));
    addRoadWithSlab(
        PACK,
        "asphalt_road_with_white_and_yellow_right_angle_line",
        "road_with_angle_line",
        textures2(
            "asphalt",
            "yellow_straight_line",
            "white_straight_line",
            "white_and_yellow_right_angle_line"));
    addRoadWithSlab(
        PACK,
        "asphalt_road_with_white_and_yellow_right_angle_line_mirrored",
        "road_with_angle_line_mirrored",
        textures2(
            "asphalt",
            "yellow_straight_line",
            "white_straight_line",
            "white_and_yellow_right_angle_line"));
    addRoadWithSlab(
        PACK,
        "asphalt_road_with_white_thick_and_normal_right_angle_line",
        "road_with_angle_line",
        textures2(
            "asphalt",
            "white_straight_line",
            "white_straight_thick_line",
            "white_thick_and_normal_right_angle_line"));
    addRoadWithSlab(
        PACK,
        "asphalt_road_with_white_thick_and_normal_right_angle_line_mirrored",
        "road_with_angle_line_mirrored",
        textures2(
            "asphalt",
            "white_straight_line",
            "white_straight_thick_line",
            "white_thick_and_normal_right_angle_line"));
    addRoadWithSlab(
        PACK,
        "asphalt_road_with_white_thick_and_yellow_right_angle_line",
        "road_with_angle_line",
        textures2(
            "asphalt",
            "yellow_straight_line",
            "white_straight_thick_line",
            "white_thick_and_yellow_right_angle_line"));
    addRoadWithSlab(
        PACK,
        "asphalt_road_with_white_thick_and_yellow_right_angle_line_mirrored",
        "road_with_angle_line_mirrored",
        textures2(
            "asphalt",
            "yellow_straight_line",
            "white_straight_thick_line",
            "white_thick_and_yellow_right_angle_line"));
    addRoadWithSlab(
        PACK,
        "asphalt_road_with_white_and_yellow_double_right_angle_line",
        "road_with_angle_line",
        textures2(
            "asphalt",
            "yellow_straight_double_line",
            "white_straight_line",
            "white_and_yellow_double_right_angle_line"));
    addRoadWithSlab(
        PACK,
        "asphalt_road_with_white_and_yellow_double_right_angle_line_mirrored",
        "road_with_angle_line_mirrored",
        textures2(
            "asphalt",
            "yellow_straight_double_line",
            "white_straight_line",
            "white_and_yellow_double_right_angle_line"));
    addRoadWithSlab(
        PACK,
        "asphalt_road_with_white_thick_and_yellow_double_right_angle_line",
        "road_with_angle_line",
        textures2(
            "asphalt",
            "yellow_straight_double_line",
            "white_straight_thick_line",
            "white_thick_and_yellow_double_right_angle_line"));
    addRoadWithSlab(
        PACK,
        "asphalt_road_with_white_thick_and_yellow_double_right_angle_line_mirrored",
        "road_with_angle_line_mirrored",
        textures2(
            "asphalt",
            "yellow_straight_double_line",
            "white_straight_thick_line",
            "white_thick_and_yellow_double_right_angle_line"));
    addRoadWithSlab(
        PACK,
        "asphalt_road_with_white_joint_line",
        "road_with_joint_line",
        textures("asphalt", "white_straight_line", "white_joint_line"));
    addRoadWithSlab(
        PACK,
        "asphalt_road_with_white_straight_and_bevel_angle_line",
        "road_with_straight_and_angle_line",
        textures(
            "asphalt", "white_straight_line", "white_straight_line", "white_bevel_angle_line"));
    addRoadWithSlab(
        PACK,
        "asphalt_road_with_white_straight_and_bevel_angle_line_mirrored",
        "road_with_straight_and_angle_line_mirrored",
        textures(
            "asphalt", "white_straight_line", "white_straight_line", "white_bevel_angle_line"));
    addRoadWithSlab(
        PACK,
        "asphalt_road_with_white_cross_line",
        "road_with_cross_line",
        textures("asphalt", "white_straight_line", "white_cross_line"));
    addRoadWithSlab(
        PACK,
        "asphalt_road_with_white_offset_straight_line",
        "road_with_straight_line",
        textures("asphalt", "white_offset_straight_line", "white_offset_straight_line"));
    addRoadWithSlab(
        PACK,
        "asphalt_road_with_white_straight_double_line",
        "road_with_straight_line",
        textures("asphalt", "white_straight_double_line", "white_straight_double_line"));
    addRoadWithSlab(
        PACK,
        "asphalt_road_with_white_straight_thick_line",
        "road_with_straight_line",
        textures("asphalt", "white_straight_thick_line", "white_straight_thick_line"));
    addRoadWithSlab(
        PACK,
        "asphalt_road_with_yellow_offset_straight_line",
        "road_with_straight_line",
        textures("asphalt", "yellow_offset_straight_line", "yellow_offset_straight_line"));
    addRoadWithSlab(
        PACK,
        "asphalt_road_with_yellow_straight_double_line",
        "road_with_straight_line",
        textures("asphalt", "yellow_straight_double_line", "yellow_straight_double_line"));
    addRoadWithSlab(
        PACK,
        "asphalt_road_with_yellow_straight_thick_line",
        "road_with_straight_line",
        textures("asphalt", "yellow_straight_thick_line", "yellow_straight_thick_line"));
    addRoadWithSlab(
        PACK,
        "asphalt_road_with_white_right_angle_line_with_one_part_offset_out",
        "road_with_angle_line",
        textures2(
            "asphalt",
            "white_straight_line",
            "white_offset_straight_line",
            "white_right_angle_line_with_one_part_offset_out"));
    addRoadWithSlab(
        PACK,
        "asphalt_road_with_white_right_angle_line_with_one_part_offset_out_mirrored",
        "road_with_angle_line_mirrored",
        textures2(
            "asphalt",
            "white_straight_line",
            "white_offset_straight_line",
            "white_right_angle_line_with_one_part_offset_out"));
    addRoadWithSlab(
        PACK,
        "asphalt_road_with_white_right_angle_line_with_one_part_offset_in",
        "road_with_angle_line",
        textures2(
            "asphalt",
            "white_straight_line",
            "white_offset_straight_line2",
            "white_right_angle_line_with_one_part_offset_in"));
    addRoadWithSlab(
        PACK,
        "asphalt_road_with_white_right_angle_line_with_one_part_offset_in_mirrored",
        "road_with_angle_line_mirrored",
        textures2(
            "asphalt",
            "white_straight_line",
            "white_offset_straight_line2",
            "white_right_angle_line_with_one_part_offset_in"));
    addRoadWithSlab(
        PACK,
        "asphalt_road_with_white_joint_line_with_double_side",
        "road_with_joint_line",
        textures2(
            "asphalt",
            "white_straight_line",
            "white_straight_double_line",
            "white_joint_line_with_double_side"));
    addRoadWithSlab(
        PACK,
        "asphalt_road_with_white_joint_line_with_thick_side",
        "road_with_joint_line",
        textures2(
            "asphalt",
            "white_straight_line",
            "white_straight_thick_line",
            "white_joint_line_with_thick_side"));
    addRoadWithSlab(
        PACK,
        "asphalt_road_with_white_joint_line_with_offset_side",
        "road_with_joint_line",
        textures2(
            "asphalt",
            "white_straight_line",
            "white_offset_straight_line",
            "white_joint_line_with_offset_side"));
    addRoadWithSlab(
        PACK,
        "asphalt_road_with_white_joint_line_with_offset_side_mirrored",
        "road_with_joint_line_mirrored",
        textures2(
            "asphalt",
            "white_straight_line",
            "white_offset_straight_line",
            "white_joint_line_with_offset_side"));
    addRoadWithSlab(
        PACK,
        "asphalt_road_with_white_thick_joint_line",
        "road_with_joint_line",
        textures2(
            "asphalt",
            "white_straight_thick_line",
            "white_straight_line",
            "white_thick_joint_line"));
    addRoadWithSlab(
        PACK,
        "asphalt_road_with_white_double_joint_line",
        "road_with_joint_line",
        textures2(
            "asphalt",
            "white_straight_double_line",
            "white_straight_line",
            "white_double_joint_line"));
    addRoadWithSlab(
        PACK,
        "asphalt_road_with_yellow_joint_line",
        "road_with_joint_line",
        textures("asphalt", "yellow_straight_line", "yellow_joint_line"));
    addRoadWithSlab(
        PACK,
        "asphalt_road_with_yellow_joint_line_with_white_side",
        "road_with_joint_line",
        textures2(
            "asphalt",
            "yellow_straight_line",
            "white_straight_line",
            "yellow_joint_line_with_white_side"));
    addRoadWithSlab(
        PACK,
        "asphalt_road_with_white_joint_line_with_yellow_side",
        "road_with_joint_line",
        textures2(
            "asphalt",
            "white_straight_line",
            "yellow_straight_line",
            "white_joint_line_with_yellow_side"));
    addRoadWithSlab(
        PACK,
        "asphalt_road_with_white_joint_line_with_yellow_double_side",
        "road_with_joint_line",
        textures2(
            "asphalt",
            "white_straight_line",
            "yellow_straight_double_line",
            "white_joint_line_with_yellow_double_side"));
    addRoadWithSlab(
        PACK,
        "asphalt_road_with_white_thick_joint_line_with_yellow_double_side",
        "road_with_joint_line",
        textures2(
            "asphalt",
            "white_straight_thick_line",
            "yellow_straight_double_line",
            "white_thick_joint_line_with_yellow_double_side"));

    addRoadWithSlab(
        PACK,
        "asphalt_road_with_white_auto_bevel_angle_line",
        "road_with_auto_line",
        new JTextures()
            .var("base", blockString("asphalt"))
            .var("line", blockString("white_auto_bevel_angle_line"))
            .var("particle", blockString("asphalt")));
    addRoadWithSlab(
        PACK,
        "asphalt_road_with_white_auto_right_angle_line",
        "road_with_auto_line",
        new JTextures()
            .var("base", blockString("asphalt"))
            .var("line", blockString("white_auto_right_angle_line"))
            .var("particle", blockString("asphalt")));

    PACK.addModel(
        JModel.model(blockIdentifier("light"))
            .textures(
                new JTextures()
                    .var("base", blockString("white_light"))
                    .var("emission", blockString("white_light_emission"))),
        blockIdentifier("white_light"));
    PACK.addModel(
        JModel.model(blockIdentifier("small_wall_light_tube"))
            .textures(new JTextures().var("light", blockString("white_light"))),
        blockIdentifier("white_small_wall_light_tube"));
    PACK.addModel(
        JModel.model(blockIdentifier("large_wall_light_tube"))
            .textures(new JTextures().var("light", blockString("white_light"))),
        blockIdentifier("white_large_wall_light_tube"));
    PACK.addModel(
        JModel.model(blockIdentifier("thin_strip_wall_light_tube"))
            .textures(new JTextures().var("light", blockString("white_light"))),
        blockIdentifier("white_thin_strip_wall_light_tube"));
    PACK.addModel(
        JModel.model(blockIdentifier("thin_strip_wall_light_tube_vertical"))
            .textures(new JTextures().var("light", blockString("white_light"))),
        blockIdentifier("white_thin_strip_wall_light_tube_vertical"));
    PACK.addModel(
        JModel.model(blockIdentifier("thick_strip_wall_light_tube"))
            .textures(new JTextures().var("light", blockString("white_light"))),
        blockIdentifier("white_thick_strip_wall_light_tube"));
    PACK.addModel(
        JModel.model(blockIdentifier("thick_strip_wall_light_tube_vertical"))
            .textures(new JTextures().var("light", blockString("white_light"))),
        blockIdentifier("white_thick_strip_wall_light_tube_vertical"));
    PACK.addModel(
        JModel.model(blockIdentifier("double_strip_wall_light_tube"))
            .textures(new JTextures().var("light", blockString("white_light"))),
        blockIdentifier("white_double_strip_wall_light_tube"));
    PACK.addModel(
        JModel.model(blockIdentifier("double_strip_wall_light_tube_vertical"))
            .textures(new JTextures().var("light", blockString("white_light"))),
        blockIdentifier("white_double_strip_wall_light_tube_vertical"));
    PACK.addModel(
        JModel.model(blockIdentifier("thin_strip_corner_light_tube"))
            .textures(new JTextures().var("light", blockString("white_light"))),
        blockIdentifier("white_thin_strip_corner_light_tube"));
    PACK.addModel(
        JModel.model(blockIdentifier("thick_strip_corner_light_tube"))
            .textures(new JTextures().var("light", blockString("white_light"))),
        blockIdentifier("white_thick_strip_corner_light_tube"));
    PACK.addModel(
        JModel.model(blockIdentifier("double_strip_corner_light_tube"))
            .textures(new JTextures().var("light", blockString("white_light"))),
        blockIdentifier("white_double_strip_corner_light_tube"));

    PACK.addModel(
        JModel.model(blockIdentifier("small_wall_light"))
            .textures(
                new JTextures()
                    .var("background", "block/obsidian")
                    .var("light", blockString("white_light"))),
        blockIdentifier("white_small_wall_light"));
    PACK.addModel(
        JModel.model(blockIdentifier("large_wall_light"))
            .textures(
                new JTextures()
                    .var("background", "block/obsidian")
                    .var("light", blockString("white_light"))),
        blockIdentifier("white_large_wall_light"));

    addWallLightDecoration(PACK, "white", "simple");
    addWallLightDecoration(PACK, "white", "point");
    addWallLightDecoration(PACK, "white", "rhombus");
    addWallLightDecoration(PACK, "white", "hash");
    addWallLightDecoration(PACK, "white", "round");

    for (DyeColor color : DyeColor.values()) {
      addModelsForHungSign(
          PACK,
          String.format("%s_concrete", color.asString()),
          String.format("block/%s_concrete", color.asString()));
      addModelsForHungSign(
          PACK,
          String.format("%s_terracotta", color.asString()),
          String.format("block/%s_terracotta", color.asString()));
      addModelsForHungSignBar(
          PACK,
          String.format("%s_concrete", color.asString()),
          String.format("block/%s_concrete", color.asString()));
      addModelsForHungSignBar(
          PACK,
          String.format("%s_terracotta", color.asString()),
          String.format("block/%s_terracotta", color.asString()));
      addModelsForGlowingHungSign(
          PACK,
          String.format("%s_concrete", color.asString()),
          String.format("block/%s_concrete", color.asString()),
          blockString("white_light"));
      addModelsForGlowingHungSign(
          PACK,
          String.format("%s_terracotta", color.asString()),
          String.format("block/%s_terracotta", color.asString()),
          blockString("white_light"));
      PACK.addModel(
          JModel.model("mishanguc:block/wall_sign")
              .textures(
                  new JTextures()
                      .var("texture", String.format("block/%s_concrete", color.asString()))),
          blockIdentifier(color.asString() + "_concrete_wall_sign"));
      PACK.addModel(
          JModel.model("mishanguc:block/wall_sign")
              .textures(
                  new JTextures()
                      .var("texture", String.format("block/%s_terracotta", color.asString()))),
          blockIdentifier(color.asString() + "_terracotta_wall_sign"));
      PACK.addModel(
          JModel.model("mishanguc:block/glowing_wall_sign")
              .textures(
                  new JTextures()
                      .var("glow", "mishanguc:block/white_light")
                      .var("texture", String.format("block/%s_concrete", color.asString()))),
          blockIdentifier("glowing_" + color.asString() + "_concrete_wall_sign"));
      PACK.addModel(
          JModel.model("mishanguc:block/glowing_wall_sign")
              .textures(
                  new JTextures()
                      .var("glow", "mishanguc:block/white_light")
                      .var("texture", String.format("block/%s_terracotta", color.asString()))),
          blockIdentifier("glowing_" + color.asString() + "_terracotta_wall_sign"));
      PACK.addModel(
          JModel.model("mishanguc:block/full_wall_sign")
              .textures(
                  new JTextures()
                      .var("glow", "mishanguc:block/white_light")
                      .var("texture", String.format("block/%s_concrete", color.asString()))),
          blockIdentifier("full_" + color.asString() + "_concrete_wall_sign"));
      PACK.addModel(
          JModel.model("mishanguc:block/full_wall_sign")
              .textures(
                  new JTextures()
                      .var("glow", "mishanguc:block/white_light")
                      .var("texture", String.format("block/%s_terracotta", color.asString()))),
          blockIdentifier("full_" + color.asString() + "_terracotta_wall_sign"));
    }

    // 杂项的悬挂告示牌方块。
    addModelsForGlowingHungSign(PACK, "netherrack", "block/netherrack", "block/glowstone");
    addModelsForGlowingHungSign(PACK, "nether_brick", "block/nether_bricks", "block/glowstone");
    addModelsForGlowingHungSign(PACK, "blackstone", "block/blackstone", "block/glowstone");
    addModelsForGlowingHungSign(
        PACK, "polished_blackstone", "block/polished_blackstone", "block/glowstone");

    addModelsForHungSignBar(PACK, "netherrack", "block/netherrack");
    addModelsForHungSignBar(PACK, "nether_brick", "block/nether_bricks");
    addModelsForHungSignBar(PACK, "blackstone", "block/blackstone");
    addModelsForHungSignBar(PACK, "polished_blackstone", "block/polished_blackstone");

    // 木质告示牌
    for (String woodName :
        new String[] {
          "oak", "spruce", "birch", "jungle", "acacia", "dark_oak", "crimson", "warped",
        }) {
      PACK.addModel(
          JModel.model("mishanguc:block/wall_sign")
              .textures(new JTextures().var("texture", String.format("block/%s_planks", woodName))),
          blockIdentifier(woodName + "_wall_sign"));
    }
  }

  /**
   * @see #addModelsForGlowingHungSign
   * @see #addStateForHungSign
   */
  private static void addModelsForHungSign(
      @NotNull RuntimeResourcePack PACK, @NotNull String hungSignName, @NotNull String texture) {
    PACK.addModel(
        JModel.model(blockIdentifier("hung_sign"))
            .textures(new JTextures().var("texture", texture)),
        blockIdentifier(String.format("%s_hung_sign", hungSignName)));
    PACK.addModel(
        JModel.model(blockIdentifier("hung_sign_body"))
            .textures(new JTextures().var("texture", texture)),
        blockIdentifier(String.format("%s_hung_sign_body", hungSignName)));
    PACK.addModel(
        JModel.model(blockIdentifier("hung_sign_top_bar"))
            .textures(new JTextures().var("texture", texture)),
        blockIdentifier(String.format("%s_hung_sign_top_bar", hungSignName)));
    PACK.addModel(
        JModel.model(blockIdentifier("hung_sign_top_bar_edge"))
            .textures(new JTextures().var("texture", texture)),
        blockIdentifier(String.format("%s_hung_sign_top_bar_edge", hungSignName)));
  }

  private static void addModelsForHungSignBar(
      @NotNull RuntimeResourcePack PACK, @NotNull String hungSignName, @NotNull String texture) {
    PACK.addModel(
        JModel.model(blockIdentifier("hung_sign_bar_central"))
            .textures(new JTextures().var("texture", texture)),
        blockIdentifier(String.format("%s_hung_sign_bar_central", hungSignName)));
    PACK.addModel(
        JModel.model(blockIdentifier("hung_sign_bar"))
            .textures(new JTextures().var("texture", texture)),
        blockIdentifier(String.format("%s_hung_sign_bar", hungSignName)));
    PACK.addModel(
        JModel.model(blockIdentifier("hung_sign_bar_edge"))
            .textures(new JTextures().var("texture", texture)),
        blockIdentifier(String.format("%s_hung_sign_bar_edge", hungSignName)));
  }

  /**
   * 为发光告示牌创建并注册方块模型文件。
   *
   * @see #addStateForHungSign
   * @see #addModelsForHungSign
   * @param hungSignName 发光告示牌的非完整名称，如 black_concrete。需确保存在名为 <tt>mishanguc:hung_</tt><code>
   *     hungSignName</code><tt>_glowing_sign</tt> 的方块。
   * @param texture 发给告示牌方块的主体纹理。为纹理的命名空间id，如 {@code minecraft:block/black_concrete}。
   * @param glowTexture 发光告示牌方块的发光部分纹理。为纹理的命名空间id，如 {@code minecraft:block/glowstone}。
   */
  private static void addModelsForGlowingHungSign(
      @NotNull RuntimeResourcePack PACK,
      @NotNull String hungSignName,
      @NotNull String texture,
      @NotNull String glowTexture) {
    PACK.addModel(
        JModel.model(blockIdentifier("glowing_hung_sign"))
            .textures(new JTextures().var("texture", texture).var("glow", glowTexture)),
        blockIdentifier(String.format("glowing_%s_hung_sign", hungSignName)));
    PACK.addModel(
        JModel.model(blockIdentifier("glowing_hung_sign_body"))
            .textures(new JTextures().var("texture", texture).var("glow", glowTexture)),
        blockIdentifier(String.format("glowing_%s_hung_sign_body", hungSignName)));
    PACK.addModel(
        JModel.model(blockIdentifier("hung_sign_top_bar"))
            .textures(new JTextures().var("texture", texture).var("glow", glowTexture)),
        blockIdentifier(String.format("glowing_%s_hung_sign_top_bar", hungSignName)));
    PACK.addModel(
        JModel.model(blockIdentifier("hung_sign_top_bar_edge"))
            .textures(new JTextures().var("texture", texture).var("glow", glowTexture)),
        blockIdentifier(String.format("glowing_%s_hung_sign_top_bar_edge", hungSignName)));
  }

  private static void addWallLightDecoration(
      @NotNull RuntimeResourcePack PACK, @NotNull String color, @NotNull String shape) {
    PACK.addModel(
        JModel.model(blockIdentifier(String.format("wall_light_%s_decoration", shape)))
            .textures(new JTextures().var("light", blockString(color + "_light"))),
        blockIdentifier(String.format("%s_wall_light_%s_decoration", color, shape)));
    PACK.addModel(
        JModel.model(blockIdentifier(String.format("wall_light_%s_decoration_center", shape)))
            .textures(new JTextures().var("light", blockString(color + "_light"))),
        blockIdentifier(String.format("%s_wall_light_%s_decoration_center", color, shape)));
    PACK.addModel(
        JModel.model(blockIdentifier(String.format("wall_light_%s_decoration_connection", shape)))
            .textures(new JTextures().var("light", blockString(color + "_light"))),
        blockIdentifier(String.format("%s_wall_light_%s_decoration_connection", color, shape)));
    PACK.addModel(
        JModel.model(blockIdentifier(String.format("wall_light_%s_decoration_connection2", shape)))
            .textures(new JTextures().var("light", blockString(color + "_light"))),
        blockIdentifier(String.format("%s_wall_light_%s_decoration_connection2", color, shape)));
  }

  @Override
  public void pregen() {
    // 客户端部分
    addBlockStates();
    addBlockModels();
    addItemModels();
    addBlockItemModels();

    // 服务端部分
    addTags();
    addBlockLootTables();
    RRPCallback.BEFORE_VANILLA.register(a -> a.add(PACK));
  }
}
