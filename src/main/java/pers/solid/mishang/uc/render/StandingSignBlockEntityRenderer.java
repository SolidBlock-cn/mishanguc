package pers.solid.mishang.uc.render;

import it.unimi.dsi.fastutil.booleans.BooleanSet;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.BlockState;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactory;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.RotationAxis;
import org.jetbrains.annotations.ApiStatus;
import pers.solid.mishang.uc.block.StandingSignBlock;
import pers.solid.mishang.uc.blockentity.StandingSignBlockEntity;
import pers.solid.mishang.uc.text.TextContext;

@ApiStatus.AvailableSince("1.0.2")
@Environment(EnvType.CLIENT)
public record StandingSignBlockEntityRenderer<T extends StandingSignBlockEntity>(BlockEntityRendererFactory.Context ctx) implements BlockEntityRenderer<T> {

  @Override
  public void render(T entity, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlay) {
    final BooleanSet glowing = entity.glowing;
    matrices.translate(0.5, 0.75, 0.5);
    final BlockState state = entity.getCachedState();
    final int rotation = state.get(StandingSignBlock.ROTATION);
    matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(-rotation * 22.5f));
    matrices.scale(1 / 16f, -1 / 16f, 1 / 16f);

    matrices.push();
    matrices.translate(0, 0, 0.5125);
    for (TextContext textContext : entity.frontTexts) {
      textContext.draw(ctx.getTextRenderer(), matrices, vertexConsumers, glowing.contains(true) ? 15728880 : light, 16, entity.getHeight());
    }
    matrices.pop();
    matrices.push();
    matrices.translate(0, 0, -0.5125);
    matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(180));
    for (TextContext textContext : entity.backTexts) {
      textContext.draw(ctx.getTextRenderer(), matrices, vertexConsumers, glowing.contains(false) ? 15728880 : light, 16, entity.getHeight());
    }
    matrices.pop();
  }
}
