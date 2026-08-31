package pers.solid.mishang.uc.render.state;

import com.mojang.blaze3d.vertex.PoseStack;
import it.unimi.dsi.fastutil.longs.LongObjectPair;
import net.fabricmc.fabric.api.client.rendering.v1.level.LevelRenderContext;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.VoxelShape;
import pers.solid.mishang.uc.item.BlockToolItem;

import java.util.ArrayList;
import java.util.List;

public class BuildingToolState implements MishangRenderState {
  public final List<LongObjectPair<VoxelShape>> cyanShapes = new ArrayList<>();
  public final List<LongObjectPair<VoxelShape>> blueShapes = new ArrayList<>();
  public final List<LongObjectPair<VoxelShape>> redShapes = new ArrayList<>();
  public final List<LongObjectPair<VoxelShape>> orangeShapes = new ArrayList<>();
  public boolean showVanillaOutline = false;

  public static boolean render(LevelRenderContext context) {
    if (!(context.levelState().getData(MishangRenderStateProvider.MISHANG_BLOCK_OUTLINE) instanceof BuildingToolState state)) {
      return true;
    }

    final BlockPos.MutableBlockPos mutable = new BlockPos.MutableBlockPos();
    final Vec3 cameraPos = context.levelState().cameraRenderState.pos;
    final PoseStack poseStack = context.poseStack();

    for (LongObjectPair<VoxelShape> pair : state.cyanShapes) {
      mutable.set(pair.leftLong());

      poseStack.pushPose();
      poseStack.translate(mutable.getX() - cameraPos.x, mutable.getY() - cameraPos.y, mutable.getZ() - cameraPos.z);
      context.submitNodeCollector().submitShapeOutline(
          poseStack,
          pair.right(),
          RenderTypes.lines(),
          BlockToolItem.OUTLINE_COLOR_CYAN,
          Minecraft.getInstance().getWindow().getAppropriateLineWidth(),
          true);// todo 检查 afterTerrain 参数（下同）
      poseStack.popPose();
    }
    for (LongObjectPair<VoxelShape> pair : state.blueShapes) {
      mutable.set(pair.leftLong());

      poseStack.pushPose();
      poseStack.translate(mutable.getX() - cameraPos.x, mutable.getY() - cameraPos.y, mutable.getZ() - cameraPos.z);
      context.submitNodeCollector().submitShapeOutline(
          poseStack,
          pair.right(),
          RenderTypes.lines(),
          BlockToolItem.OUTLINE_COLOR_BLUE,
          Minecraft.getInstance().getWindow().getAppropriateLineWidth(),
          true);
      poseStack.popPose();
    }
    for (LongObjectPair<VoxelShape> pair : state.redShapes) {
      mutable.set(pair.leftLong());

      poseStack.pushPose();
      poseStack.translate(mutable.getX() - cameraPos.x, mutable.getY() - cameraPos.y, mutable.getZ() - cameraPos.z);
      context.submitNodeCollector().submitShapeOutline(
          poseStack,
          pair.right(),
          RenderTypes.lines(),
          BlockToolItem.OUTLINE_COLOR_RED,
          Minecraft.getInstance().getWindow().getAppropriateLineWidth(),
          true);
      poseStack.popPose();
    }
    for (LongObjectPair<VoxelShape> pair : state.orangeShapes) {
      mutable.set(pair.leftLong());

      poseStack.pushPose();
      poseStack.translate(mutable.getX() - cameraPos.x, mutable.getY() - cameraPos.y, mutable.getZ() - cameraPos.z);
      context.submitNodeCollector().submitShapeOutline(
          poseStack,
          pair.right(),
          RenderTypes.lines(),
          BlockToolItem.OUTLINE_COLOR_ORANGE,
          Minecraft.getInstance().getWindow().getAppropriateLineWidth(),
          true);
      poseStack.popPose();
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
