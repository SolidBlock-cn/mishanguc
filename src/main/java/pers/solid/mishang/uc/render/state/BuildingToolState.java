package pers.solid.mishang.uc.render.state;

import com.mojang.blaze3d.vertex.VertexConsumer;
import it.unimi.dsi.fastutil.longs.LongObjectPair;
import net.fabricmc.fabric.api.client.rendering.v1.world.WorldRenderContext;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.ShapeRenderer;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.util.ARGB;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.VoxelShape;
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
    final MultiBufferSource consumers = context.consumers();
    if (consumers == null) return true;
    final VertexConsumer vertexConsumer = consumers.getBuffer(RenderTypes.lines());

    final BlockPos.MutableBlockPos mutable = new BlockPos.MutableBlockPos();
    final Vec3 cameraPos = context.worldState().cameraRenderState.pos;

    for (LongObjectPair<VoxelShape> pair : state.cyanShapes) {
      mutable.set(pair.leftLong());

      ShapeRenderer.renderShape(
          context.matrices(),
          vertexConsumer,
          pair.right(),
          mutable.getX() - cameraPos.x(),
          mutable.getY() - cameraPos.y(),
          mutable.getZ() - cameraPos.z(),
          ARGB.colorFromFloat(0.8f,
              0,
              1,
              1),
          Minecraft.getInstance().getWindow().getAppropriateLineWidth());
    }
    for (LongObjectPair<VoxelShape> pair : state.blueShapes) {
      mutable.set(pair.leftLong());

      ShapeRenderer.renderShape(
          context.matrices(),
          vertexConsumer,
          pair.right(),
          mutable.getX() - cameraPos.x(),
          mutable.getY() - cameraPos.y(),
          mutable.getZ() - cameraPos.z(),
          ARGB.colorFromFloat(0.5f,
              0,
              0.5f,
              1),
          Minecraft.getInstance().getWindow().getAppropriateLineWidth());
    }
    for (LongObjectPair<VoxelShape> pair : state.redShapes) {
      mutable.set(pair.leftLong());

      ShapeRenderer.renderShape(
          context.matrices(),
          vertexConsumer,
          pair.right(),
          mutable.getX() - cameraPos.x(),
          mutable.getY() - cameraPos.y(),
          mutable.getZ() - cameraPos.z(),
          ARGB.colorFromFloat(0.5f,
              1,
              0,
              0),
          Minecraft.getInstance().getWindow().getAppropriateLineWidth());
    }
    for (LongObjectPair<VoxelShape> pair : state.orangeShapes) {
      mutable.set(pair.leftLong());

      ShapeRenderer.renderShape(
          context.matrices(),
          vertexConsumer,
          pair.right(),
          mutable.getX() - cameraPos.x(),
          mutable.getY() - cameraPos.y(),
          mutable.getZ() - cameraPos.z(),
          ARGB.colorFromFloat(0.8f,
              1f,
              0.5f,
              0),
          Minecraft.getInstance().getWindow().getAppropriateLineWidth());
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
