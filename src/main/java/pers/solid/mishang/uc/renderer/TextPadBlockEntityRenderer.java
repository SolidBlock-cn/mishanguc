package pers.solid.mishang.uc.renderer;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.entity.SignBlockEntity;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.entity.BlockEntityRenderDispatcher;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.Vec3f;
import pers.solid.mishang.uc.block.TextPadBlock;
import pers.solid.mishang.uc.blockentity.TextPadBlockEntity;
import pers.solid.mishang.uc.util.TextContext;

/**
 * 用于渲染写字板方块。
 *
 * @see pers.solid.mishang.uc.block.TextPadBlock
 * @see TextPadBlockEntity
 */
@Environment(EnvType.CLIENT)
public class TextPadBlockEntityRenderer extends BlockEntityRenderer<TextPadBlockEntity> {
  public TextPadBlockEntityRenderer(BlockEntityRenderDispatcher dispatcher) {
    super(dispatcher);
  }

  /**
   * 本方法在编写时会适当参考 {@link
   * net.minecraft.client.render.block.entity.SignBlockEntityRenderer#render(SignBlockEntity, float,
   * MatrixStack, VertexConsumerProvider, int, int)}
   */
  @Override
  public void render(
      TextPadBlockEntity entity,
      float tickDelta,
      MatrixStack matrices,
      VertexConsumerProvider vertexConsumers,
      int light,
      int overlay) {
    final TextRenderer textRenderer = dispatcher.getTextRenderer();
    matrices.translate(0.5, 0.5, 0.5);
    matrices.multiply(
        Vec3f.POSITIVE_Y.getDegreesQuaternion(
            -entity.getCachedState().get(TextPadBlock.FACING).asRotation()));
    matrices.translate(0, 0, -0.3745);
    matrices.scale(1f / 16, -1f / 16, 1f / 16);
    entity.textContext.draw(textRenderer, matrices, vertexConsumers, light, 16, 16);
    for (TextContext otherTextContext : entity.otherTextContexts) {
      otherTextContext.draw(textRenderer, matrices, vertexConsumers, light, 16, 16);
    }
  }
}
