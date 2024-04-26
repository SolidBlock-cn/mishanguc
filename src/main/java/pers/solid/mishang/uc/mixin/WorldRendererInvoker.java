package pers.solid.mishang.uc.mixin;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.WorldRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.shape.VoxelShape;
import org.jetbrains.annotations.Contract;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Environment(EnvType.CLIENT)
@Mixin(WorldRenderer.class)
public interface WorldRendererInvoker {
  /**
   * 在指定位置渲染指定外观。
   *
   * @see WorldRenderer
   */
  @Contract
  @Invoker("drawCuboidShapeOutline")
  static void drawCuboidShapeOutline(
      MatrixStack matrices,
      VertexConsumer vertexConsumer,
      VoxelShape shape,
      double offsetX,
      double offsetY,
      double offsetZ,
      float red,
      float green,
      float blue,
      float alpha) {
    throw new AssertionError();
  }
}
