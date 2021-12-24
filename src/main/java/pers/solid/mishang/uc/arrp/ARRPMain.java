package pers.solid.mishang.uc.arrp;

import net.devtech.arrp.api.RRPCallback;
import net.devtech.arrp.api.RRPPreGenEntrypoint;
import net.devtech.arrp.api.RuntimeResourcePack;
import net.devtech.arrp.json.loot.JLootTable;
import net.devtech.arrp.json.models.*;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Direction;
import org.jetbrains.annotations.Nullable;
import pers.solid.mishang.uc.annotations.RegisterIdentifier;
import pers.solid.mishang.uc.annotations.SimpleModel;
import pers.solid.mishang.uc.block.MishangucBlocks;
import pers.solid.mishang.uc.item.MishangucItems;

import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.Map;

public class ARRPMain implements RRPPreGenEntrypoint {

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

  @Deprecated
  private static void addBlockModels(RuntimeResourcePack PACK) {
    PACK.addModel(
        JModel.model("block/cube_all").textures(new JTextures().var("all", blockString("asphalt"))),
        blockIdentifier("asphalt_road_block"));

    PACK.addModel(
        JModel.model("block/cube")
            .element(
                fullElement(faces("base")),
                fullElement(faces("line_bottom", "line_top", "line_side", null, null, "line_side")))
            .textures(JModel.textures().particle("#base").var("line_bottom", "#line_top")),
        blockIdentifier("road_with_corner_line"));

    PACK.addModel(
        JModel.model("block/cube")
            .element(
                fullElement(faces("base")),
                fullElement(faces("line_bottom", "line_top", "line_side", null, "line_side", null)))
            .textures(JModel.textures().particle("#base").var("line_bottom", "#line_top")),
        blockIdentifier("road_with_straight_line"));

    PACK.addModel(
        JModel.model(blockIdentifier("road_with_corner_line"))
            .textures(
                JModel.textures()
                    .var("line_top", blockString("white_corner_line"))
                    .var("line_side", blockString("white_straight_line"))
                    .var("base", blockIdentifier("asphalt").toString())),
        blockIdentifier("asphalt_road_with_white_corner_line"));

    PACK.addModel(
        JModel.model(blockIdentifier("road_with_corner_line"))
            .textures(
                JModel.textures()
                    .var("line_top", blockString("white_slope_line"))
                    .var("line_side", blockString("white_straight_line"))
                    .var("base", blockString("asphalt"))),
        blockIdentifier("asphalt_road_with_white_slope_line"));

    PACK.addModel(
        JModel.model(blockIdentifier("road_with_straight_line"))
            .textures(
                JModel.textures()
                    .var("line_top", blockString("white_straight_line"))
                    .var("line_side", blockString("white_straight_line"))
                    .var("base", blockString("asphalt"))),
        blockIdentifier("asphalt_road_with_white_straight_line"));
  }

  private static void addBlockItemModel(RuntimeResourcePack PACK, String name) {
    PACK.addModel(JModel.model(blockIdentifier(name)), itemIdentifier(name));
  }

  private static void addBlockLootTable(RuntimeResourcePack PACK, String name) {
    PACK.addLootTable(
        new Identifier("mishanguc", "blocks/" + name),
        JLootTable.loot("minecraft:block")
            .pool(
                JLootTable.pool()
                    .rolls(1)
                    .entry(JLootTable.entry().type("minecraft:item").name("mishanguc:" + name))
                    .condition(JLootTable.predicate("minecraft:survives_explosion"))));
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

  /** 运行此方法需确保其楼梯名称证号为 path + "_slab"。 */
  private static void addCubeAllWithSlab(final RuntimeResourcePack PACK, String path, String all) {
    addCubeAll(PACK, path, all);
    addSlabAll(PACK, plusSlab(path), all);
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
        JModel.model(blockIdentifier(parent) + "_slab").textures(textures),
        blockIdentifier(plusSlab(path)));
    PACK.addModel(
        JModel.model(blockIdentifier(parent) + "_slab_top").textures(textures),
        blockIdentifier(plusSlab(path) + "_top"));
  }

  private static String plusSlab(String string) {
    if (string.contains("_road")) {
      return string.replaceFirst("_road", "_road_slab");
    } else {
      return string + "_slab";
    }
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
    final RuntimeResourcePack PACK = RuntimeResourcePack.create("mishanguc");
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
        "asphalt_road_with_white_side_line",
        "road_with_straight_line",
        textures("asphalt", "white_side_line", "white_side_line"));
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

    // 利用反射，创建所有的方块物品。
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
              addBlockItemModel(PACK, name);
            });
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
    RRPCallback.BEFORE_VANILLA.register(a -> a.add(PACK));
  }
}
