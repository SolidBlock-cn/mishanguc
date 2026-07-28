package pers.solid.mishang.uc.render;

import it.unimi.dsi.fastutil.booleans.BooleanSet;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.BlockState;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactory;
import net.minecraft.client.render.command.ModelCommandRenderer;
import net.minecraft.client.render.command.OrderedRenderCommandQueue;
import net.minecraft.client.render.state.CameraRenderState;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.RotationAxis;
import net.minecraft.util.math.Vec3d;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Nullable;
import pers.solid.mishang.uc.block.StandingSignBlock;
import pers.solid.mishang.uc.blockentity.StandingSignBlockEntity;
import pers.solid.mishang.uc.text.TextContext;

@ApiStatus.AvailableSince("1.0.2")
@Environment(EnvType.CLIENT)
public record StandingSignBlockEntityRenderer<T extends StandingSignBlockEntity>(BlockEntityRendererFactory.Context ctx) implements BlockEntityRenderer<T, StandingBlockEntityRenderState> {

  @Override
  public void render(StandingBlockEntityRenderState state, MatrixStack matrices, OrderedRenderCommandQueue queue, CameraRenderState cameraState) {
    final BooleanSet glowing = state.glowing;
    matrices.translate(0.5, 0.75, 0.5);
    final BlockState blockState = state.blockState;
    final int rotation = blockState.get(StandingSignBlock.ROTATION);
    matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(-rotation * 22.5f));
    matrices.scale(1 / 16f, -1 / 16f, 1 / 16f);

    matrices.push();
    matrices.translate(0, 0, 0.5125);
    for (TextContext textContext : state.frontTexts) {
      textContext.draw(ctx.textRenderer(), matrices, queue, glowing.contains(true) ? 15728880 : state.lightmapCoordinates, 16, state.height);
    }
    matrices.pop();
    matrices.push();
    matrices.translate(0, 0, -0.5125);
    matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(180));
    for (TextContext textContext : state.backTexts) {
      textContext.draw(ctx.textRenderer(), matrices, queue, glowing.contains(true) ? 15728880 : state.lightmapCoordinates, 16, state.height);
    }
    matrices.pop();
  }

  @Override
  public StandingBlockEntityRenderState createRenderState() {
    return new StandingBlockEntityRenderState();
  }

  @Override
  public void updateRenderState(T blockEntity, StandingBlockEntityRenderState state, float tickProgress, Vec3d cameraPos, @Nullable ModelCommandRenderer.CrumblingOverlayCommand crumblingOverlay) {
    BlockEntityRenderer.super.updateRenderState(blockEntity, state, tickProgress, cameraPos, crumblingOverlay);
    state.backTexts = blockEntity.backTexts;
    state.frontTexts = blockEntity.frontTexts;
    state.height = blockEntity.getHeight();
    state.glowing = blockEntity.glowing;
  }
}
