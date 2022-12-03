package pers.solid.mishang.uc.blockentity;

import net.minecraft.block.MapColor;

public interface ColoredBlockEntity {

  default MapColor getNearestMapColor() {
    final int color = getColor();
    int lastDistance = Integer.MAX_VALUE;
    MapColor nearestMapColor = MapColor.CLEAR;
    for (int i = 0; i < 64; i++) {
      final MapColor mapColor1 = MapColor.get(i);
      if (mapColor1 == null) continue;
      final int mapColor = mapColor1.color;
      final int distance = Math.abs((mapColor << 4) % 255 - (color << 4) % 255) + Math.abs((mapColor << 2) % 255 - (color << 2) % 255) + Math.abs(mapColor % 255 - color % 255);
      if (distance < lastDistance) {
        lastDistance = distance;
        nearestMapColor = mapColor1;
      }
    }
    return nearestMapColor;
  }

  int getColor();

  void setColor(int color);
}
