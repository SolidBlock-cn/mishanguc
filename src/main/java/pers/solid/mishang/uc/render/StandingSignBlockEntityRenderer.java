package pers.solid.mishang.uc.render;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import it.unimi.dsi.fastutil.booleans.BooleanSet;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.feature.ModelFeatureRenderer;
import net.minecraft.client.renderer.state.CameraRenderState;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Nullable;
import pers.solid.mishang.uc.block.StandingSignBlock;
import pers.solid.mishang.uc.blockentity.StandingSignBlockEntity;
import pers.solid.mishang.uc.text.TextContext;

@ApiStatus.AvailableSince("1.0.2")
@Environment(EnvType.CLIENT)
public record StandingSignBlockEntityRenderer<T extends StandingSignBlockEntity>(BlockEntityRendererProvider.Context ctx) implements BlockEntityRenderer<T, StandingBlockEntityRenderState> {

  @Override
  public void submit(StandingBlockEntityRenderState state, PoseStack matrices, SubmitNodeCollector queue, CameraRenderState cameraState) {
    final BooleanSet glowing = state.glowing;
    matrices.translate(0.5, 0.75, 0.5);
    final BlockState blockState = state.blockState;
    final int rotation = blockState.getValue(StandingSignBlock.ROTATION);
    matrices.mulPose(Axis.YP.rotationDegrees(-rotation * 22.5f));
    matrices.scale(1 / 16f, -1 / 16f, 1 / 16f);

    matrices.pushPose();
    matrices.translate(0, 0, 0.5125);
    for (TextContext textContext : state.frontTexts) {
      textContext.draw(ctx.font(), matrices, queue, glowing.contains(true) ? 15728880 : state.lightCoords, 16, state.height);
    }
    matrices.popPose();
    matrices.pushPose();
    matrices.translate(0, 0, -0.5125);
    matrices.mulPose(Axis.YP.rotationDegrees(180));
    for (TextContext textContext : state.backTexts) {
      textContext.draw(ctx.font(), matrices, queue, glowing.contains(true) ? 15728880 : state.lightCoords, 16, state.height);
    }
    matrices.popPose();
  }

  @Override
  public StandingBlockEntityRenderState createRenderState() {
    return new StandingBlockEntityRenderState();
  }

  @Override
  public void extractRenderState(T blockEntity, StandingBlockEntityRenderState state, float tickProgress, Vec3 cameraPos, @Nullable ModelFeatureRenderer.CrumblingOverlay crumblingOverlay) {
    BlockEntityRenderer.super.extractRenderState(blockEntity, state, tickProgress, cameraPos, crumblingOverlay);
    state.backTexts = blockEntity.backTexts;
    state.frontTexts = blockEntity.frontTexts;
    state.height = blockEntity.getHeight();
    state.glowing = blockEntity.glowing;
  }
}
