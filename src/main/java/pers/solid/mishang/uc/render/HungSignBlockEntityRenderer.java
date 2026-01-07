package pers.solid.mishang.uc.render;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactory;
import net.minecraft.client.render.command.ModelCommandRenderer;
import net.minecraft.client.render.command.OrderedRenderCommandQueue;
import net.minecraft.client.render.state.CameraRenderState;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.RotationAxis;
import net.minecraft.util.math.Vec3d;
import org.jetbrains.annotations.NotNull;
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

  private final BlockEntityRendererFactory.Context ctx;

  public HungSignBlockEntityRenderer(BlockEntityRendererFactory.Context ctx) {
    this.ctx = ctx;
  }

  @Override
  public HungSignBlockEntityRenderState createRenderState() {
    return new HungSignBlockEntityRenderState();
  }

  @Override
  public void updateRenderState(T blockEntity, HungSignBlockEntityRenderState state, float tickProgress, Vec3d cameraPos, @Nullable ModelCommandRenderer.CrumblingOverlayCommand crumblingOverlay) {
    BlockEntityRenderer.super.updateRenderState(blockEntity, state, tickProgress, cameraPos, crumblingOverlay);
    state.texts = blockEntity.texts;
    state.glowing = blockEntity.glowing;
    state.height = blockEntity.getHeight();
  }

  @Override
  public void render(HungSignBlockEntityRenderState state, MatrixStack matrices, OrderedRenderCommandQueue queue, CameraRenderState cameraState) {
    matrices.translate(0.5, 9 / 16f, 0.5);
    final Direction.Axis axis = state.blockState.get(HungSignBlock.AXIS);
    for (Map.Entry<@NotNull Direction, @NotNull List<@NotNull TextContext>> entry : state.texts.entrySet()) {
      final Direction direction = entry.getKey();
      final List<@NotNull TextContext> textContexts = entry.getValue();
      if (direction.getAxis() != axis) {
        continue;
      }
      final boolean glowing = state.glowing.contains(direction);
      matrices.push();
      matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(-direction.getPositiveHorizontalDegrees()));
      matrices.translate(0, 0, 1.0125 / 32f);
      matrices.scale(1 / 16f, -1 / 16f, 1 / 16f);
      for (TextContext textContext : textContexts) {
        textContext.draw(ctx.textRenderer(), matrices, queue, glowing ? 15728880 : state.lightmapCoordinates, 16, state.height);
      }
      matrices.pop();
    }
  }
}
