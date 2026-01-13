package pers.solid.mishang.uc.text;

import org.jetbrains.annotations.Unmodifiable;

public record RectanglePattern(String name, @Unmodifiable float[][] rectangles) {
  private float[][] flipLeftRight() {
    final float[][] flipped = new float[rectangles.length][];
    for (int i = 0; i < rectangles.length; i++) {
      final float[] rectanglesPiece = rectangles[i];
      flipped[i] = new float[]{7 - rectanglesPiece[2], rectanglesPiece[1], 7 - rectanglesPiece[0], rectanglesPiece[3]};
    }
    return flipped;
  }

  public RectanglePattern flipLeftRight(String name) {
    return new RectanglePattern(name, flipLeftRight());
  }

  private float[][] flipUpDown() {
    final float[][] flipped = new float[rectangles.length][];
    for (int i = 0; i < rectangles.length; i++) {
      final float[] rectanglesPiece = rectangles[i];
      flipped[i] = new float[]{rectanglesPiece[0], 7 - rectanglesPiece[3], rectanglesPiece[2], 7 - rectanglesPiece[1]};
    }
    return flipped;
  }

  public RectanglePattern flipUpDown(String name) {
    return new RectanglePattern(name, flipUpDown());
  }

  private float[][] flipAll() {
    final float[][] flipped = new float[rectangles.length][];
    for (int i = 0; i < rectangles.length; i++) {
      final float[] rectanglesPiece = rectangles[i];
      flipped[i] = new float[]{7 - rectanglesPiece[2], 7 - rectanglesPiece[3], 7 - rectanglesPiece[0], 7 - rectanglesPiece[1]};
    }
    return flipped;
  }

  public RectanglePattern flipAll(String name) {
    return new RectanglePattern(name, flipAll());
  }
}
