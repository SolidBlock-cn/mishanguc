package pers.solid.mishang.uc.render;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.feature.ModelFeatureRenderer;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.core.Direction;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;
import pers.solid.mishang.uc.block.HungSignBlock;
import pers.solid.mishang.uc.blockentity.HungSignBlockEntity;
import pers.solid.mishang.uc.text.TextContext;

import java.util.List;
import java.util.Map;

/**
 * @see pers.solid.mishang.uc.block.HungSignBlock
 * @see HungSignBlockEntity
 */
@Environment(EnvType.CLIENT)
public class HungSignBlockEntityRenderer<T extends HungSignBlockEntity> implements BlockEntityRenderer<T, HungSignBlockEntityRenderState> {

  private final BlockEntityRendererProvider.Context ctx;

  public HungSignBlockEntityRenderer(BlockEntityRendererProvider.Context ctx) {
    this.ctx = ctx;
  }

  @Override
  public HungSignBlockEntityRenderState createRenderState() {
    return new HungSignBlockEntityRenderState();
  }

  @Override
  public void extractRenderState(T blockEntity, HungSignBlockEntityRenderState state, float tickProgress, Vec3 cameraPos, @Nullable ModelFeatureRenderer.CrumblingOverlay crumblingOverlay) {
    BlockEntityRenderer.super.extractRenderState(blockEntity, state, tickProgress, cameraPos, crumblingOverlay);
    state.texts = blockEntity.texts;
    state.glowing = blockEntity.glowing;
    state.height = blockEntity.getHeight();
    state.axis = blockEntity.getBlockState().getValue(HungSignBlock.AXIS);
  }

  @Override
  public void submit(HungSignBlockEntityRenderState state, PoseStack matrices, SubmitNodeCollector queue, CameraRenderState cameraState) {
    matrices.translate(0.5, 9 / 16f, 0.5);
    final Direction.Axis axis = state.axis;
    for (Map.Entry<Direction, List<TextContext>> entry : state.texts.entrySet()) {
      final Direction direction = entry.getKey();
      final List<TextContext> textContexts = entry.getValue();
      if (direction.getAxis() != axis) {
        continue;
      }
      final boolean glowing = state.glowing.contains(direction);
      matrices.pushPose();
      matrices.mulPose(Axis.YP.rotationDegrees(-direction.toYRot()));
      matrices.translate(0, 0, 1.0125 / 32f);
      matrices.scale(1 / 16f, -1 / 16f, 1 / 16f);
      for (TextContext textContext : textContexts) {
        textContext.draw(ctx.font(), matrices, queue, glowing ? 15728880 : state.lightCoords, 16, state.height);
      }
      matrices.popPose();
    }
  }
}
