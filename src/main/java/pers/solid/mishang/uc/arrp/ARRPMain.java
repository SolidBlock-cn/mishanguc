package pers.solid.mishang.uc.arrp;

import com.google.common.collect.Lists;
import com.google.gson.JsonObject;
import net.devtech.arrp.api.RRPCallback;
import net.devtech.arrp.api.RRPPreGenEntrypoint;
import net.devtech.arrp.api.RuntimeResourcePack;
import net.devtech.arrp.json.blockstate.JBlockModel;
import net.devtech.arrp.json.blockstate.JState;
import net.devtech.arrp.json.blockstate.JVariant;
import net.devtech.arrp.json.loot.JCondition;
import net.devtech.arrp.json.loot.JFunction;
import net.devtech.arrp.json.loot.JLootTable;
import net.devtech.arrp.json.models.*;
import net.minecraft.block.Block;
import net.minecraft.block.SlabBlock;
import net.minecraft.item.Item;
import net.minecraft.util.Identifier;
import net.minecraft.util.Util;
import net.minecraft.util.math.Direction;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import pers.solid.mishang.uc.annotations.RegisterIdentifier;
import pers.solid.mishang.uc.annotations.SimpleModel;
import pers.solid.mishang.uc.block.HorizontalCornerDirection;
import pers.solid.mishang.uc.block.MishangucBlocks;
import pers.solid.mishang.uc.item.MishangucItems;
import pers.solid.mishang.uc.mixin.JBlockModelAccessor;
import pers.solid.mishang.uc.mixin.JStateAccessor;
import pers.solid.mishang.uc.mixin.JVariantAccessor;

import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class ARRPMain implements RRPPreGenEntrypoint {
  private static final RuntimeResourcePack PACK = RuntimeResourcePack.create("mishanguc");

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

  @Override
  public void pregen() {
    addBlockStates();
    addBlockModels();
    addBlockItemModels();
    addBlockLootTables();
    addItemModels();
    RRPCallback.BEFORE_VANILLA.register(a -> a.add(PACK));
  }

  private static void addBlockLootTables() {
    Arrays.stream(MishangucBlocks.class.getFields())
        .filter(
            field -> {
              int modifier = field.getModifiers();
              return Modifier.isPublic(modifier)
                  && Modifier.isStatic(modifier)
                  && Block.class.isAssignableFrom(field.getType())
                  && field.isAnnotationPresent(RegisterIdentifier.class);
            })
        .forEach(
            field -> {
              String name = field.getAnnotation(RegisterIdentifier.class).value();
              if (name.isEmpty()) {
                name = field.getName().toLowerCase();
              }
              if (SlabBlock.class.isAssignableFrom(field.getType())) {
                addSlabBlockLootTable(name);
              } else {
                addSimpleBlockLootTable(name);
              }
            });
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
  }

  @NotNull
  private static JState stateForAngleLineWithOnePartOffset(@NotNull String moduleName) {
    JVariant variant = new JVariant();
    for (Direction direction : Direction.Type.HORIZONTAL) {
      final Direction offsetDirection1 = direction.rotateYCounterclockwise();
      final Direction offsetDirection2 = direction.rotateYClockwise();
      variant.put(
          String.format(
              "facing=%s,axis=%s",
              Objects.requireNonNull(
                      HorizontalCornerDirection.fromDirections(direction, offsetDirection1))
                  .asString(),
              direction.getAxis().asString()),
          JState.model("mishanguc:block/" + moduleName).y((int) (direction.asRotation() + 90)));
      variant.put(
          String.format(
              "facing=%s,axis=%s",
              Objects.requireNonNull(
                      HorizontalCornerDirection.fromDirections(direction, offsetDirection2))
                  .asString(),
              direction.getAxis().asString()),
          JState.model("mishanguc:block/" + moduleName + "_mirrored")
              .y((int) (direction.asRotation()) + 90));
    }
    return JState.state(variant);
  }

  @NotNull
  private static JState stateForJointLineWithOffsetSide(@NotNull String moduleName) {
    JVariant variant = new JVariant();
    // 一侧的短线所朝向的方向。
    for (Direction direction : Direction.Type.HORIZONTAL) {
      final @NotNull Direction offsetDirection1 = direction.rotateYCounterclockwise();
      final @NotNull HorizontalCornerDirection facing1 =
          Objects.requireNonNull(
              HorizontalCornerDirection.fromDirections(direction, offsetDirection1));
      final @NotNull Direction offsetDirection2 = direction.rotateYClockwise();
      final @NotNull HorizontalCornerDirection facing2 =
          Objects.requireNonNull(
              HorizontalCornerDirection.fromDirections(direction, offsetDirection2));
      variant
          .put(
              String.format(
                  "facing=%s,axis=%s", facing1.asString(), offsetDirection1.getAxis().asString()),
              JState.model(blockIdentifier(moduleName)).y((int) (direction.asRotation() - 180)))
          .put(
              String.format(
                  "facing=%s,axis=%s", facing2.asString(), offsetDirection2.getAxis().asString()),
              JState.model(blockIdentifier(moduleName + "_mirrored"))
                  .y((int) (direction.asRotation() - 180)));
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
    Arrays.stream(MishangucBlocks.class.getFields())
        .filter(
            field -> {
              int modifier = field.getModifiers();
              return Modifier.isPublic(modifier)
                  && Modifier.isStatic(modifier)
                  && Block.class.isAssignableFrom(field.getType())
                  && field.isAnnotationPresent(RegisterIdentifier.class);
            })
        .forEach(
            field -> {
              String name = field.getAnnotation(RegisterIdentifier.class).value();
              if (name.isEmpty()) {
                name = field.getName().toLowerCase();
              }
              addBlockItemModel(name);
            });
  }

  private static void addBlockModels() {
    addCubeAll(PACK, "asphalt_road_block", "asphalt");
    addSlabAll(PACK, "asphalt_road_slab", "asphalt");
    addCubeAllWithSlab(PACK, "asphalt_road_filled_with_white", "white_ink");
    addRoadWithSlab(
        PACK,
        "asphalt_road_with_white_bevel_angle_line",
        "road_with_angle_line",
        textures("asphalt", "white_straight_line", "white_bevel_angle_line"));
    addRoadWithSlab(
        PACK,
        "asphalt_road_with_white_joint_line",
        "road_with_joint_line",
        textures("asphalt", "white_straight_line", "white_joint_line"));
    addRoadWithSlab(
        PACK,
        "asphalt_road_with_white_right_angle_line",
        "road_with_angle_line",
        textures("asphalt", "white_straight_line", "white_right_angle_line"));
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
        "asphalt_road_with_white_straight_line",
        "road_with_straight_line",
        textures("asphalt", "white_straight_line", "white_straight_line"));
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
        "asphalt_road_with_white_right_angle_line_with_one_part_offset_out",
        "road_with_angle_line",
        textures2(
            "asphalt",
            "white_straight_line",
            "white_offset_straight_line2",
            "white_right_angle_line_with_one_part_offset_out"));
    addRoadWithSlab(
        PACK,
        "asphalt_road_with_white_right_angle_line_with_one_part_offset_out_mirrored",
        "road_with_angle_line_mirrored",
        textures2(
            "asphalt",
            "white_straight_line",
            "white_offset_straight_line2",
            "white_right_angle_line_with_one_part_offset_out"));
    addRoadWithSlab(
        PACK,
        "asphalt_road_with_white_right_angle_line_with_one_part_offset_in",
        "road_with_angle_line",
        textures2(
            "asphalt",
            "white_straight_line",
            "white_offset_straight_line",
            "white_right_angle_line_with_one_part_offset_in"));
    addRoadWithSlab(
        PACK,
        "asphalt_road_with_white_right_angle_line_with_one_part_offset_in_mirrored",
        "road_with_angle_line_mirrored",
        textures2(
            "asphalt",
            "white_straight_line",
            "white_offset_straight_line",
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
        JModel.model(blockIdentifier("lamp"))
            .textures(
                new JTextures()
                    .var("base", blockString("white_lamp"))
                    .var("emission", blockString("white_lamp_emission"))),
        blockIdentifier("white_lamp"));
  }
}
