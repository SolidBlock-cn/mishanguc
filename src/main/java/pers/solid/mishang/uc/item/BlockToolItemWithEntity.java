package pers.solid.mishang.uc.item;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.api.EnvironmentInterface;
import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderContext;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.render.*;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.ColorHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.tick.TickManager;
import org.jetbrains.annotations.Nullable;
import pers.solid.mishang.uc.render.RendersBeforeOutline;

@EnvironmentInterface(itf = RendersBeforeOutline.class, value = EnvType.CLIENT)
public abstract class BlockToolItemWithEntity extends BlockToolItem implements RendersBeforeOutline {

  private static final int OUTLINE_COLOR_GREEN = ColorHelper.fromFloats(0.8f, 0f, 1f, 0f);

  public BlockToolItemWithEntity(Settings settings, @Nullable Boolean includesFluid) {
    super(settings, includesFluid);
  }

  @Environment(EnvType.CLIENT)
  @Override
  public void renderBeforeOutline(WorldRenderContext context, HitResult hitResult, ClientPlayerEntity player, Hand hand) {
    if (hitResult instanceof EntityHitResult entityHitResult && !player.isSpectator()) {
      final Entity entity = entityHitResult.getEntity();
      final MatrixStack matrices = context.matrixStack();
      final VertexConsumerProvider consumers = context.consumers();
      if (matrices == null || consumers == null) return;
      final VertexConsumer vertexConsumer = consumers.getBuffer(RenderLayer.getLines());
      final Vec3d cameraPos = context.camera().getPos();
      final RenderTickCounter deltaTracker = MinecraftClient.getInstance().getRenderTickCounter();
      final TickManager tickRateManager = context.world().getTickManager();
      final float tickProgress = deltaTracker.getTickDelta(!tickRateManager.shouldSkipTick(entity));
      VertexRendering.drawOutline(matrices, vertexConsumer, VoxelShapes.cuboid(entity.getBoundingBox().offset(entity.getLerpedPos(tickProgress).subtract(entity.getPos()))), -cameraPos.x, -cameraPos.y, -cameraPos.z, OUTLINE_COLOR_GREEN);
    }
  }
}
