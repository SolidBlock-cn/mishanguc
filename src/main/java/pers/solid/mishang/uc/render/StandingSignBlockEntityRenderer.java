package pers.solid.mishang.uc.render;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.BlockState;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.entity.BlockEntityRenderDispatcher;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.Vec3f;
import org.jetbrains.annotations.ApiStatus;
import pers.solid.mishang.uc.block.StandingSignBlock;
import pers.solid.mishang.uc.blockentity.StandingSignBlockEntity;
import pers.solid.mishang.uc.text.TextContext;

@ApiStatus.AvailableSince("1.0.2")
@Environment(EnvType.CLIENT)
public class StandingSignBlockEntityRenderer<T extends StandingSignBlockEntity> extends BlockEntityRenderer<T> {
  public StandingSignBlockEntityRenderer(BlockEntityRenderDispatcher dispatcher) {
    super(dispatcher);
  }

  @Override
  public void render(T entity, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlay) {
    matrices.translate(0.5, 0.75, 0.5);
    final BlockState state = entity.getCachedState();
    final int rotation = state.get(StandingSignBlock.ROTATION);
    matrices.multiply(Vec3f.POSITIVE_Y.getDegreesQuaternion(-rotation * 22.5f));
    matrices.scale(1 / 16f, -1 / 16f, 1 / 16f);

    matrices.push();
    matrices.translate(0, 0, 0.5125);
    for (TextContext textContext : entity.frontTexts) {
      textContext.draw(dispatcher.getTextRenderer(), matrices, vertexConsumers, light, 16, entity.getHeight());
    }
    matrices.pop();
    matrices.push();
    matrices.translate(0, 0, -0.5125);
    matrices.multiply(Vec3f.POSITIVE_Y.getDegreesQuaternion(180));
    for (TextContext textContext : entity.backTexts) {
      textContext.draw(dispatcher.getTextRenderer(), matrices, vertexConsumers, light, 16, entity.getHeight());
    }
    matrices.pop();
  }
}
