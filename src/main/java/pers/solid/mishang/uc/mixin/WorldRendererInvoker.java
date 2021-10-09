package pers.solid.mishang.uc.mixin;

import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.WorldRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.shape.VoxelShape;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(WorldRenderer.class)
public interface WorldRendererInvoker {
    /**
     * 在指定位置渲染指定外观。
     *
     * @see WorldRenderer
     */
    @Invoker("drawShapeOutline")
    static void drawShapeOutline(MatrixStack matrices, VertexConsumer vertexConsumer, VoxelShape voxelShape, double x, double y, double z, float red, float green, float blue, float alpha) {
        throw new AssertionError();
    }
}
