package pers.solid.mishang.uc.render;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.debug.DebugRenderer;
import net.minecraft.client.util.math.MatrixStack;
import org.jetbrains.annotations.NotNull;

@Environment(EnvType.CLIENT)
public class BuildingTooltipRenderer implements DebugRenderer.Renderer{
    private final @NotNull MinecraftClient client;

    public BuildingTooltipRenderer(@NotNull MinecraftClient client) {
        this.client = client;
    }

    @Override
    public void render(MatrixStack matrices, VertexConsumerProvider vertexConsumers, double cameraX, double cameraY, double cameraZ) {
//        assert this.client.player != null;
//        BlockView blockView = this.client.player.world;
//        final BlockPos pos = new BlockPos(this.client.player.raycast(20, 0, false).getPos());
//        final Box box = new Box(pos);
//        final Tessellator tessellator = Tessellator.getInstance();
//        final BufferBuilder bufferBuilder = tessellator.getBuffer();
//        bufferBuilder.begin(5, VertexFormats.POSITION_COLOR);
//        WorldRenderer.drawBox(bufferBuilder,box.minX,box.minY,box.minZ,box.maxX,box.maxY,box.maxZ,0F, 1F,0F,1F);
//        tessellator.draw();
//        DebugRenderer.drawBox(box.minX,box.minY,box.minZ,box.maxX,box.maxY,box.maxZ,0F, 1F,1F,1F);
    }
}
