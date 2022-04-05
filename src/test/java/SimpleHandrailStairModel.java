import com.google.common.collect.Maps;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import net.minecraft.util.Util;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.MathHelper;
import pers.solid.mishang.uc.block.HandrailStairBlock;

import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.Map;
import java.util.Objects;

public class SimpleHandrailStairModel {
  public static final Gson GSON = new GsonBuilder().create();

  public static void main(String[] args) throws IOException {
    for (HandrailStairBlock.Shape shape : HandrailStairBlock.Shape.values()) {
      for (HandrailStairBlock.Position position : HandrailStairBlock.Position.values()) {
        final JsonObject model = generateModel(position, shape);
        final FileWriter fileWriter = new FileWriter(String.format("simple_handrail_stair_%s_%s.json", shape.asString(), position.asString()));
        GSON.toJson(model, fileWriter);
        fileWriter.close();
      }
    }
  }

  public static JsonObject generateModel(HandrailStairBlock.Position type, HandrailStairBlock.Shape shape) {
    final JsonObject model = new JsonObject();
    model.addProperty("parent", "block/block");
    final JsonObject textures = new JsonObject();
    textures.addProperty("bottom", "#texture");
    textures.addProperty("top", "#texture");
    textures.addProperty("particle", "#texture");
    model.add("textures", textures);
    final JsonArray elements = new JsonArray();
    model.add("elements", elements);

    final double x1 = switch (type) {
      case LEFT -> 1;
      case CENTER -> 7.5;
      default -> 14;
    };

    // 扶手部分
    for (int i = shape == HandrailStairBlock.Shape.BOTTOM ? 8 : 0; i < (shape == HandrailStairBlock.Shape.TOP ? 7 : 16); i++) {
      final JsonObject element = new JsonObject();
      final double y1 = 5 + i;
      final double z1 = 15 - i;
      element.add("from", GSON.toJsonTree(new double[]{x1, y1, z1}));
      element.add("to", GSON.toJsonTree(new double[]{x1 + 1, y1 + 1, z1 + 1}));
      final Map<String, JsonObject> faces = Maps.toMap(Arrays.stream(Direction.values()).map(Direction::asString).iterator(), ignored -> {
        final JsonObject face = new JsonObject();
        face.addProperty("texture", "#top");
        return face;
      });
      // 调整侧面方位的 uv。
      final double floorMod = MathHelper.floorMod(16 - y1 - 1, 16);
      faces.get(Direction.NORTH.asString()).add("uv",
          GSON.toJsonTree(new double[]{16 - x1 - 1, floorMod, 16 - x1, floorMod + 1}));
      faces.get(Direction.SOUTH.asString()).add("uv",
          GSON.toJsonTree(new double[]{x1, floorMod, x1 + 1, floorMod + 1}));
      faces.get(Direction.EAST.asString()).add("uv",
          GSON.toJsonTree(new double[]{i, floorMod, i + 1, floorMod + 1}));
      faces.get(Direction.WEST.asString()).add("uv",
          GSON.toJsonTree(new double[]{z1, floorMod, z1 + 1, floorMod + 1}));

      element.add("faces", GSON.toJsonTree(faces));
      elements.add(element);
    }

    // 栏杆部分
    for (int i = shape == HandrailStairBlock.Shape.BOTTOM ? 2 : 0; i < (shape == HandrailStairBlock.Shape.TOP ? 2 : 4); i++) {
      final JsonObject element1 = new JsonObject();
      final JsonObject element2 = new JsonObject();
      elements.add(element1);
      elements.add(element2);
      final double y1 = -6 + 4 * i;
      final double z1 = 14 - 4 * i;
      element1.add("from",
          GSON.toJsonTree(new double[]{x1, y1, z1}));
      element1.add("to",
          GSON.toJsonTree(new double[]{x1 + 1, y1 + 12, z1 + 0.5}));
      element2.add("from",
          GSON.toJsonTree(new double[]{x1, y1 + 1, z1 - 0.5}));
      element2.add("to",
          GSON.toJsonTree(new double[]{x1 + 1, y1 + 13, z1}));

      final JsonObject faces1 = new JsonObject();
      faces1.add("west", Util.make(new JsonObject(), jsonObject -> {
        jsonObject.addProperty("texture", "#texture");
        jsonObject.add("uv",
            GSON.toJsonTree(new double[]{16 - z1 - 0.5, 3, 16 - z1, 15}));
      }));
      faces1.add("east", Util.make(new JsonObject(), jsonObject -> {
        jsonObject.addProperty("texture", "#texture");
        jsonObject.add("uv",
            GSON.toJsonTree(new double[]{z1, 3, z1 + 0.5, 15}));
      }));
      faces1.add("south", Util.make(new JsonObject(), jsonObject -> {
        jsonObject.addProperty("texture", "#texture");
        jsonObject.add("uv",
            GSON.toJsonTree(new double[]{x1, 3, x1 + 1, 15}));
      }));
      element1.add("faces", faces1);
      final JsonObject faces2 = new JsonObject();
      faces2.add("west", Util.make(new JsonObject(), jsonObject -> {
        jsonObject.addProperty("texture", "#texture");
        jsonObject.add("uv",
            GSON.toJsonTree(new double[]{16 - z1 - 0.5, 2, 16 - z1, 14}));
      }));
      faces2.add("east", Util.make(new JsonObject(), jsonObject -> {
        jsonObject.addProperty("texture", "#texture");
        jsonObject.add("uv",
            GSON.toJsonTree(new double[]{z1, 2, z1 + 0.5, 14}));
      }));
      faces2.add("north", Util.make(new JsonObject(), jsonObject -> {
        jsonObject.addProperty("texture", "#texture");
        jsonObject.add("uv",
            GSON.toJsonTree(new double[]{16 - x1 - 1, 2, 16 - x1, 14}));
      }));
      element2.add("faces", faces2);
    }

    // 底座边缘部分
    for (int i = shape == HandrailStairBlock.Shape.BOTTOM ? 9 : 0; i < (shape == HandrailStairBlock.Shape.TOP ? 8 : 16); i++) {
      final JsonObject element = new JsonObject();
      elements.add(element);
      final double y1 = -8 + i;
      final double z1 = 15 - i;
      final double floorMod = MathHelper.floorMod(16 - y1 - 1, 16);
      element.add("from", GSON.toJsonTree(new double[]{x1, y1, z1}));
      element.add("to", GSON.toJsonTree(new double[]{x1 + 1, y1 + 1, z1 + 1}));
      element.add("faces", Util.make(new JsonObject(), faces -> {
        faces.add("up", Util.make(new JsonObject(), jsonObject -> jsonObject.addProperty("texture", "#bottom")));
        faces.add("south", Util.make(new JsonObject(), jsonObject -> {
          jsonObject.addProperty("texture", "#bottom");
          jsonObject.add("uv", GSON.toJsonTree(new double[]{x1, floorMod, x1 + 1, floorMod + 1}));
        }));
      }));
    }
    if (shape != HandrailStairBlock.Shape.BOTTOM) for (int i = 0; i < 8; i++) {
      final JsonObject element = new JsonObject();
      elements.add(element);
      final int ii = i;
      final double y1 = -8;
      final double z1 = 15 - i;
      element.add("from", GSON.toJsonTree(new double[]{x1, y1, z1}));
      element.add("to", GSON.toJsonTree(new double[]{x1 + 1, y1 + 1 + i, z1 + 1}));
      element.add("faces", Util.make(new JsonObject(), faces -> {
        faces.add("east", Util.make(new JsonObject(), face -> {
          face.add("uv", GSON.toJsonTree(new double[]{ii, ii, ii + 1, 8}));
          face.addProperty("texture", "#bottom");
        }));
        faces.add("west", Util.make(new JsonObject(), face -> {
          face.add("uv", GSON.toJsonTree(new double[]{15 - ii, ii, 16 - ii, 8}));
          face.addProperty("texture", "#bottom");
        }));
      }));
    }
    if (shape != HandrailStairBlock.Shape.TOP) for (int i = 0; i < 8; i++) {
      final JsonObject element = new JsonObject();
      elements.add(element);
      final double z1 = 7 - i;
      element.add("from", GSON.toJsonTree(new double[]{x1, 0, z1}));
      element.add("to", GSON.toJsonTree(new double[]{x1 + 1, 1 + i, z1 + 1}));
      element.add("faces", Util.make(new JsonObject(), faces -> {
        faces.add("east", Util.make(new JsonObject(), face -> face.addProperty("texture", "#bottom")));
        faces.add("west", Util.make(new JsonObject(), face -> face.addProperty("texture", "#bottom")));
      }));
    }
    if (shape != HandrailStairBlock.Shape.BOTTOM) elements.add(Util.make(new JsonObject(), element -> {
      element.add("from", GSON.toJsonTree(new double[]{x1, -8, 8}));
      element.add("to", GSON.toJsonTree(new double[]{x1 + 1, -0, 16}));
      element.add("faces", Util.make(new JsonObject(), faces -> {
        faces.add("north", Util.make(new JsonObject(), face -> {
          face.addProperty("texture", "#bottom");
          face.add("uv", GSON.toJsonTree(new double[]{15 - x1, 0, 16 - x1, 8}));
        }));
        faces.add("down", Util.make(new JsonObject(), face -> face.addProperty("texture", "#bottom")));
      }));
    }));
    if (shape != HandrailStairBlock.Shape.TOP) elements.add(Util.make(new JsonObject(), element -> {
      element.add("from", GSON.toJsonTree(new double[]{x1, 0, 0}));
      element.add("to", GSON.toJsonTree(new double[]{x1 + 1, 8, 8}));
      element.add("faces", Util.make(new JsonObject(), faces -> {
        faces.add("north", Util.make(new JsonObject(), face -> face.addProperty("texture", "#bottom")));
        faces.add("down", Util.make(new JsonObject(), face -> face.addProperty("texture", "#bottom")));
      }));
    }));

    if (shape == HandrailStairBlock.Shape.BOTTOM) {
      // 栏杆顶部
      elements.add(Util.make(new JsonObject(), element -> {
        element.add("from", GSON.toJsonTree(new double[]{x1, 12, 8}));
        element.add("to", GSON.toJsonTree(new double[]{x1 + 1, 13, 16}));
        element.add("faces", GSON.toJsonTree(Maps.toMap(Arrays.stream(Direction.values()).map(Direction::asString).iterator(), direction -> Util.make(new JsonObject(), face -> {
          face.addProperty("texture", "#top");
          if (direction.equals("south")) {
            face.addProperty("cullface", direction);
          }
        }))));
      }));
      // 栏杆底部
      elements.add(Util.make(new JsonObject(), element -> {
        element.add("from", GSON.toJsonTree(new double[]{x1, 0, 7}));
        element.add("to", GSON.toJsonTree(new double[]{x1 + 1, 1, 16}));
        element.add("faces", GSON.toJsonTree(Maps.toMap(Arrays.stream(Direction.values()).filter(direction -> direction != Direction.NORTH).map(Direction::asString).iterator(), direction -> Util.make(new JsonObject(), face -> {
          face.addProperty("texture", "#top");
          if (Objects.equals(direction, Direction.DOWN.asString()) || Objects.equals(direction, Direction.SOUTH.asString())) {
            face.addProperty("cullface", direction);
          }
        }))));
      }));
      elements.add(Util.make(new JsonObject(), element -> {
        element.add("from", GSON.toJsonTree(new double[]{x1, 1, 9.5}));
        element.add("to", GSON.toJsonTree(new double[]{x1 + 1, 12, 10.5}));
        element.add("faces", GSON.toJsonTree(Maps.toMap(Direction.Type.HORIZONTAL.stream().map(Direction::asString).iterator(), ignored -> Util.make(new JsonObject(), face -> face.addProperty("texture", "#texture")))));
      }));
      elements.add(Util.make(new JsonObject(), element -> {
        element.add("from", GSON.toJsonTree(new double[]{x1, 1, 13.5}));
        element.add("to", GSON.toJsonTree(new double[]{x1 + 1, 12, 14.5}));
        element.add("faces", GSON.toJsonTree(Maps.toMap(Direction.Type.HORIZONTAL.stream().map(Direction::asString).iterator(), ignored -> Util.make(new JsonObject(), face -> face.addProperty("texture", "#texture")))));
      }));
    }
    if (shape == HandrailStairBlock.Shape.TOP) {
      // 栏杆顶部
      elements.add(Util.make(new JsonObject(), element -> {
        element.add("from", GSON.toJsonTree(new double[]{x1, 12, 0}));
        element.add("to", GSON.toJsonTree(new double[]{x1 + 1, 13, 9}));
        element.add("faces", GSON.toJsonTree(Maps.toMap(Arrays.stream(Direction.values()).map(Direction::asString).iterator(), direction -> Util.make(new JsonObject(), face -> {
          face.addProperty("texture", "#top");
          if (direction.equals("north")) {
            face.addProperty("cullface", direction);
          }
        }))));
      }));
      // 栏杆底部
      elements.add(Util.make(new JsonObject(), element -> {
        element.add("from", GSON.toJsonTree(new double[]{x1, 0, 0}));
        element.add("to", GSON.toJsonTree(new double[]{x1 + 1, 1, 8}));
        element.add("faces", GSON.toJsonTree(Maps.toMap(Arrays.stream(Direction.values()).map(Direction::asString).iterator(), direction -> Util.make(new JsonObject(), face -> {
          face.addProperty("texture", "#top");
          if (Objects.equals(direction, Direction.DOWN.asString()) || Objects.equals(direction, Direction.NORTH.asString())) {
            face.addProperty("cullface", direction);
          }
        }))));
      }));
      elements.add(Util.make(new JsonObject(), element -> {
        element.add("from", GSON.toJsonTree(new double[]{x1, 1, 1.5}));
        element.add("to", GSON.toJsonTree(new double[]{x1 + 1, 12, 2.5}));
        element.add("faces", GSON.toJsonTree(Maps.toMap(Direction.Type.HORIZONTAL.stream().map(Direction::asString).iterator(), ignored -> Util.make(new JsonObject(), face -> face.addProperty("texture", "#texture")))));
      }));
      elements.add(Util.make(new JsonObject(), element -> {
        element.add("from", GSON.toJsonTree(new double[]{x1, 1, 5.5}));
        element.add("to", GSON.toJsonTree(new double[]{x1 + 1, 12, 6.5}));
        element.add("faces", GSON.toJsonTree(Maps.toMap(Direction.Type.HORIZONTAL.stream().map(Direction::asString).iterator(), ignored -> Util.make(new JsonObject(), face -> face.addProperty("texture", "#texture")))));
      }));
    }

    return model;
  }
}
