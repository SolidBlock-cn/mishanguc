package pers.solid.mishang.uc.text;

import java.util.HashMap;
import java.util.Map;

public final class RectanglePatterns {
  private static final Map<String, RectanglePattern> REGISTRY = new HashMap<>();

  public static final RectanglePattern EMPTY = register("empty", new float[][]{});
  public static final RectanglePattern ARROW_LEFT = register("arrow-left", new float[][]{
      // 箭头的中间部分：
      {0, 3, 7, 4},
      // 箭头的上边：
      {2, 1, 3, 3},
      {1, 2, 2, 3},
      // 箭头的下边：
      {1, 4, 2, 5},
      {2, 4, 3, 6}
  }, "al");
  public static final RectanglePattern ARROW_RIGHT = register("arrow-right", new float[][]{
      // 箭头的中间部分：
      {0, 3, 7, 4},
      // 箭头的上边：
      {4, 1, 5, 3},
      {5, 2, 6, 3},
      // 箭头的下边：
      {5, 4, 6, 5},
      {4, 4, 5, 6}
  }, "ar");
  public static final RectanglePattern ARROW_UP = register("arrow-up", new float[][]{
      // 箭头的中间部分：
      {3, 0, 4, 7},
      // 箭头的左边：
      {2, 1, 3, 2},
      {1, 2, 3, 3},
      // 箭头的右边：
      {4, 1, 5, 2},
      {4, 2, 6, 3},
  }, "arrow-top", "au", "at");
  public static final RectanglePattern ARROW_BOTTOM = register("arrow-down", new float[][]{
      // 箭头的中间部分：
      {3, 0, 4, 7},
      // 箭头的左边：
      {2, 5, 3, 6},
      {1, 4, 3, 5},
      // 箭头的右边：
      {4, 5, 5, 6},
      {4, 4, 6, 5},
  }, "arrow-bottom", "ad", "ab");
  public static final RectanglePattern ARROW_LEFT_THIN = register("arrow-left-thin", new float[][]{
      // 箭头的中间部分：
      {0, 3, 7, 4},
      // 箭头的上边：
      {2, 1, 3, 2},
      {1, 2, 2, 3},
      // 箭头的下边：
      {1, 4, 2, 5},
      {2, 5, 3, 6}
  });
  public static final RectanglePattern ARROW_RIGHT_THIN = register(ARROW_LEFT_THIN.flipLeftRight("arrow-right-thin"));
  public static final RectanglePattern ARROW_UP_THIN = register("arrow-up-thin", new float[][]{
      // 箭头的中间部分：
      {3, 0, 4, 7},
      // 箭头的左边：
      {2, 1, 3, 2},
      {1, 2, 2, 3},
      // 箭头的右边：
      {4, 1, 5, 2},
      {5, 2, 6, 3},
  });
  public static final RectanglePattern ARROW_DOWN_THIN = register(ARROW_UP_THIN.flipLeftRight("arrow-down-thin"));
  public static final RectanglePattern ARROW_LEFT_UP = register("arrow-left-up", new float[][]{
      // 箭头的左尾：
      {1, 1, 2, 5},
      // 箭头的右尾：
      {2, 1, 5, 2},
      // 箭头的杆：
      {2, 2, 3, 3},
      {3, 3, 4, 4},
      {4, 4, 5, 5},
      {5, 5, 6, 6},
      {6, 6, 7, 7}
  }, "arrow-left-top", "alu", "alt");
  public static final RectanglePattern ARROW_RIGHT_UP = register("arrow-right-up", new float[][]{
      // 箭头的左尾：
      {5, 1, 6, 5},
      // 箭头的右尾：
      {2, 1, 5, 2},
      // 箭头的杆：
      {4, 2, 5, 3},
      {3, 3, 4, 4},
      {2, 4, 3, 5},
      {1, 5, 2, 6},
      {0, 6, 1, 7}
  }, "arrow-right-top", "aru", "art");
  public static final RectanglePattern ARROW_LEFT_DOWN = register("arrow-left-down", new float[][]{
      // 箭头的左尾：
      {1, 2, 2, 6},
      // 箭头的右尾：
      {2, 5, 5, 6},
      // 箭头的杆：
      {2, 4, 3, 5},
      {3, 3, 4, 4},
      {4, 2, 5, 3},
      {5, 1, 6, 2},
      {6, 0, 7, 1}
  }, "arrow-left-bottom", "ald", "alb");
  public static final RectanglePattern ARROW_RIGHT_DOWN = register("arrow-right-down", new float[][]{
      // 箭头的左尾：
      {5, 2, 6, 6},
      // 箭头的右尾：
      {2, 5, 5, 6},
      // 箭头的杆：
      {4, 4, 5, 5},
      {3, 3, 4, 4},
      {2, 2, 3, 3},
      {1, 1, 2, 2},
      {0, 0, 1, 1}
  }, "arrow-right-bottom", "ard", "arb");
  public static final RectanglePattern ARROW_LEFT_TURN_UP = register("arrow-left-turn-up", new float[][]{
      // 箭头的竖直部分
      {2, 0, 3, 6},
      // 箭头的水平部分
      {3, 5, 7, 6},
      // 箭头的左尾
      {1, 1, 2, 2},
      {0, 2, 1, 3},
      // 箭头的右尾
      {3, 1, 4, 2},
      {4, 2, 5, 3}
  }, "altu");
  public static final RectanglePattern ARROW_RIGHT_TURN_UP = register(ARROW_LEFT_TURN_UP.flipLeftRight("arrow-right-turn-up"), "artu");
  public static final RectanglePattern ARROW_LEFT_TURN_DOWN = register(ARROW_LEFT_TURN_UP.flipUpDown("arrow-left-turn-down"), "altd");
  public static final RectanglePattern ARROW_RIGHT_TURN_DOWN = register(ARROW_LEFT_TURN_UP.flipAll("arrow-right-turn-down"), "artd");
  public static final RectanglePattern ARROW_LEFT_RIGHT = register("arrow-left-right", new float[][]{
      // 箭头的中间部分
      {-1, 3, 8, 4},
      // 箭头的左边上下两尾
      {1, 1, 2, 2},
      {0, 2, 2, 3},
      {0, 4, 2, 5},
      {1, 5, 2, 6},
      // 箭头的右边上下两尾
      {5, 1, 6, 2},
      {5, 2, 7, 3},
      {5, 4, 7, 5},
      {5, 5, 6, 6},
  }, "alr");
  public static final RectanglePattern ARROW_UP_DOWN = register("arrow-up-down", new float[][]{
      // 箭头的中间部分
      {3, -1, 4, 8},
      // 箭头的上边左右两尾
      {1, 1, 3, 2},
      {2, 0, 3, 1},
      {4, 0, 5, 1},
      {4, 1, 6, 2},
      // 箭头的下边左右两尾
      {1, 5, 3, 6},
      {2, 6, 3, 7},
      {4, 6, 5, 7},
      {4, 5, 6, 6},
  }, "aud");
  public static final RectanglePattern CIRCLE_SMALL = register("circle-small", new float[][]{
      {2, 1, 5, 2},
      {1, 2, 2, 5},
      {5, 2, 6, 5},
      {2, 5, 5, 6}
  }, "small-circle");
  public static final RectanglePattern CIRCLE_MEDIUM = register("circle-medium", new float[][]{
      {2, 0, 5, 1},
      {2, 6, 5, 7},
      {0, 2, 1, 5},
      {6, 2, 7, 5},
      {1, 1, 2, 2},
      {5, 1, 6, 2},
      {1, 5, 2, 6},
      {5, 5, 6, 6}
  }, "medium-circle", "circle", "O");
  public static final RectanglePattern BAN = register("ban", new float[][]{
      {2, 0, 5, 1},
      {2, 6, 5, 7},
      {0, 2, 1, 5},
      {6, 2, 7, 5},
      {1, 1, 2, 2},
      {5, 1, 6, 2},
      {1, 5, 2, 6},
      {5, 5, 6, 6},
      {2, 2, 3, 3},
      {3, 3, 4, 4},
      {4, 4, 5, 5}
  });

  public static final RectanglePattern U_TURN_LEFT_DOWN = register("u-turn-left-down", new float[][]{
      {3, 0, 6, 1},
      {6, 1, 7, 6},
      {2, 1, 3, 7},
      {0, 4, 1, 5},
      {1, 5, 2, 6},
      {4, 4, 5, 5},
      {3, 5, 4, 6}
  }, "u-turn-left-bottom", "uld", "ulb");
  public static final RectanglePattern U_TURN_RIGHT_DOWN = register(U_TURN_LEFT_DOWN.flipLeftRight("u-turn-right-down"), "u-turn-right-bottom", "urd", "urb");
  public static final RectanglePattern U_TURN_LEFT_UP = register(U_TURN_LEFT_DOWN.flipUpDown("u-turn-left-up"), "u-turn-left-top", "ulu", "ult");
  public static final RectanglePattern U_TURN_RIGHT_UP = register(U_TURN_RIGHT_DOWN.flipAll("u-turn-right-up"), "u-turn-right-top", "uru", "urt");
  public static final RectanglePattern CROSS_SMALL = register("cross-small", new float[][]{
      {2, 2, 3, 3},
      {3, 3, 4, 4},
      {4, 4, 5, 5},
      {4, 2, 5, 3},
      {2, 4, 3, 5},
  }, "small-cross");
  public static final RectanglePattern CROSS_MEDIUM = register("cross-medium", new float[][]{
      {1, 1, 2, 2},
      {2, 2, 3, 3},
      {3, 3, 4, 4},
      {4, 4, 5, 5},
      {5, 5, 6, 6},
      {4, 2, 5, 3},
      {5, 1, 6, 2},
      {2, 4, 3, 5},
      {1, 5, 2, 6}
  }, "medium-cross", "cross", "X");
  public static final RectanglePattern CROSS_LARGE = register("cross-large", new float[][]{
      {0, 0, 1, 1},
      {1, 1, 2, 2},
      {2, 2, 3, 3},
      {3, 3, 4, 4},
      {4, 4, 5, 5},
      {5, 5, 6, 6},
      {6, 6, 7, 7},
      {4, 2, 5, 3},
      {5, 1, 6, 2},
      {6, 2, 7, 3},
      {2, 4, 3, 5},
      {1, 5, 2, 6},
      {0, 6, 1, 7},
  }, "large-cross");

  public static final RectanglePattern SQUARE_SMALL = register("square-small", new float[][]{
      {2, 2, 5, 3},
      {2, 4, 5, 5},
      {2, 3, 3, 4},
      {4, 3, 5, 4}
  }, "small-square");
  public static final RectanglePattern SQUARE_MEDIUM = register("square-medium", new float[][]{
      {1, 1, 6, 2},
      {1, 5, 6, 6},
      {1, 2, 2, 5},
      {5, 2, 6, 5}
  }, "medium-square", "square");
  public static final RectanglePattern SQUARE_LARGE = register("square-large", new float[][]{
      {0, 0, 7, 1},
      {0, 6, 7, 7},
      {0, 1, 1, 6},
      {6, 1, 7, 6},
  }, "large-square");
  public static final RectanglePattern SQUARE_SLANT_SMALL = register("square-slant-small", new float[][]{
      {3, 2, 4, 3},
      {2, 3, 3, 2},
      {4, 3, 5, 4},
      {3, 4, 4, 5}
  }, "small-slant-square");
  public static final RectanglePattern SQUARE_SLANT_MEDIUM = register("square-slant-medium", new float[][]{
      {3, 1, 4, 2},
      {2, 2, 3, 3},
      {1, 3, 2, 4},
      {4, 2, 5, 3},
      {5, 3, 6, 4},
      {2, 4, 3, 5},
      {4, 4, 6, 5},
      {3, 5, 4, 6},
  }, "medium-slant-square");
  public static final RectanglePattern SQUARE_SLANT_LARGE = register("square-slant-large", new float[][]{
      {3, 0, 4, 1},
      {2, 1, 3, 2},
      {1, 2, 2, 3},
      {0, 3, 1, 4},
      {4, 1, 5, 2},
      {5, 2, 6, 3},
      {6, 3, 7, 4},
      {1, 4, 2, 5},
      {2, 5, 3, 6},
      {3, 6, 4, 7},
      {4, 5, 5, 6},
      {5, 4, 6, 5},
  }, "large-slant-square");

  public static RectanglePattern get(String name) {
    return REGISTRY.get(name);
  }

  public static RectanglePattern getOrDefault(String name, RectanglePattern defaultPattern) {
    return REGISTRY.getOrDefault(name, defaultPattern);
  }

  public static RectanglePattern register(RectanglePattern rectanglePattern) {
    final String name = rectanglePattern.name();
    if (REGISTRY.containsKey(name)) {
      throw new IllegalArgumentException("Duplicate name: " + name);
    }
    REGISTRY.put(name, rectanglePattern);
    return rectanglePattern;
  }

  public static RectanglePattern register(RectanglePattern rectanglePattern, String... aliases) {
    REGISTRY.put(rectanglePattern.name(), rectanglePattern);
    for (String alias : aliases) {
      if (REGISTRY.containsKey(alias)) {
        throw new IllegalArgumentException("Duplicate alias: " + alias);
      }
      REGISTRY.put(alias, rectanglePattern);
    }
    return rectanglePattern;
  }

  public static RectanglePattern register(String name, float[][] rectangles) {
    return register(new RectanglePattern(name, rectangles));
  }

  public static RectanglePattern register(String name, float[][] rectangles, String... aliases) {
    return register(new RectanglePattern(name, rectangles), aliases);
  }
}
