package pers.solid.mishang.uc.render.state;

import it.unimi.dsi.fastutil.longs.LongObjectPair;
import net.fabricmc.fabric.api.client.rendering.v1.world.WorldRenderContext;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.VertexRendering;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ColorHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.shape.VoxelShape;

import java.util.ArrayList;
import java.util.List;

public class BuildingToolState implements MishangRenderState {
  public final List<LongObjectPair<VoxelShape>> cyanShapes = new ArrayList<>();
  public final List<LongObjectPair<VoxelShape>> blueShapes = new ArrayList<>();
  public final List<LongObjectPair<VoxelShape>> redShapes = new ArrayList<>();
  public final List<LongObjectPair<VoxelShape>> orangeShapes = new ArrayList<>();
  public boolean showVanillaOutline = false;

  public static boolean render(WorldRenderContext context) {
    if (!(context.worldState().getData(MishangRenderStateProvider.MISHANG_BLOCK_OUTLINE) instanceof BuildingToolState state)) {
      return true;
    }
    final VertexConsumerProvider consumers = context.consumers();
    if (consumers == null) return true;
    final VertexConsumer vertexConsumer = consumers.getBuffer(RenderLayer.LINES);

    final BlockPos.Mutable mutable = new BlockPos.Mutable();
    final Vec3d cameraPos = context.worldState().cameraRenderState.pos;

    for (LongObjectPair<VoxelShape> pair : state.cyanShapes) {
      mutable.set(pair.leftLong());

      VertexRendering.drawOutline(
          context.matrices(),
          vertexConsumer,
          pair.right(),
          mutable.getX() - cameraPos.getX(),
          mutable.getY() - cameraPos.getY(),
          mutable.getZ() - cameraPos.getZ(),
          ColorHelper.fromFloats(0.8f,
              0,
              1,
              1));
    }
    for (LongObjectPair<VoxelShape> pair : state.blueShapes) {
      mutable.set(pair.leftLong());

      VertexRendering.drawOutline(
          context.matrices(),
          vertexConsumer,
          pair.right(),
          mutable.getX() - cameraPos.getX(),
          mutable.getY() - cameraPos.getY(),
          mutable.getZ() - cameraPos.getZ(),
          ColorHelper.fromFloats(0.5f,
              0,
              0.5f,
              1));
    }
    for (LongObjectPair<VoxelShape> pair : state.redShapes) {
      mutable.set(pair.leftLong());

      VertexRendering.drawOutline(
          context.matrices(),
          vertexConsumer,
          pair.right(),
          mutable.getX() - cameraPos.getX(),
          mutable.getY() - cameraPos.getY(),
          mutable.getZ() - cameraPos.getZ(),
          ColorHelper.fromFloats(0.5f,
              1,
              0,
              0));
    }
    for (LongObjectPair<VoxelShape> pair : state.orangeShapes) {
      mutable.set(pair.leftLong());

      VertexRendering.drawOutline(
          context.matrices(),
          vertexConsumer,
          pair.right(),
          mutable.getX() - cameraPos.getX(),
          mutable.getY() - cameraPos.getY(),
          mutable.getZ() - cameraPos.getZ(),
          ColorHelper.fromFloats(0.8f,
              1f,
              0.5f,
              0));
    }

    return state.showVanillaOutline;
  }

  @Override
  public void clear() {
    cyanShapes.clear();
    blueShapes.clear();
    redShapes.clear();
    orangeShapes.clear();
    showVanillaOutline = false;
  }
}
