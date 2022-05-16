package pers.solid.mishang.uc.util;

import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.nbt.NbtCompound;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface TextExtra {
  TextExtra INVALID = new TextExtra() {
    @Override
    public void drawExtra(TextRenderer textRenderer, MatrixStack matrixStack, VertexConsumerProvider vertexConsumers, int light, float x, float y) {
    }

    @Override
    public String getId() {
      return "invalid";
    }
  };

  void drawExtra(TextRenderer textRenderer, MatrixStack matrixStack, VertexConsumerProvider vertexConsumers, int light, float x, float y);

  String getId();

  @Contract("_ -> param1")
  default NbtCompound writeNbt(NbtCompound nbt) {
    nbt.putString("id", getId());
    return nbt;
  }

  static @Nullable TextExtra fromNbt(TextContext textContext, @NotNull NbtCompound nbt) {
    final String id = nbt.getString("id");
    if (id == null || id.isBlank()) {
      return null;
    } else if (id.equals("rect")) {
      final RectTextExtra rectTextExtra = new RectTextExtra(textContext);
      rectTextExtra.readNbt(nbt);
      return rectTextExtra;
    }
    return null;
  }

  default String describeArgs() {
    return "";
  }

  static @Nullable TextExtra fromDescription(TextContext textContext, String id, String args) {
    if (id == null || id.isBlank()) return null;
    else if (id.equals("rect")) {
      final RectTextExtra rect = new RectTextExtra(textContext);
      final String[] split = args.split(" ");
      if (split.length < 2) return INVALID;
      try {
        rect.minX = Float.parseFloat(split[0]);
        rect.minY = Float.parseFloat(split[1]);
        if (split.length < 4) {
          rect.minY = Math.abs(rect.minY) / 2;
          rect.maxY = -rect.minY;
          rect.maxX = rect.minX;
          rect.minX = 0;
          rect.zIndex = split.length > 2 ? Float.parseFloat(split[2]) : 0;
        } else {
          rect.maxX = Float.parseFloat(split[2]);
          rect.maxY = Float.parseFloat(split[3]);
          rect.zIndex = split.length > 4 ? Float.parseFloat(split[4]) : 0;
        }
      } catch (NumberFormatException e) {
        return INVALID;
      }
      return rect;
    }
    return null;
  }

  default float getWidth() {
    return 0;
  }
}
