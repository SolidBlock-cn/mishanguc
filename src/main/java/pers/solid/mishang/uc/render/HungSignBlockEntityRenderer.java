package pers.solid.mishang.uc.render;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactory;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3f;
import org.jetbrains.annotations.NotNull;
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
public class HungSignBlockEntityRenderer implements BlockEntityRenderer<HungSignBlockEntity> {

  private final BlockEntityRendererFactory.Context ctx;

  public HungSignBlockEntityRenderer(BlockEntityRendererFactory.Context ctx) {
    this.ctx = ctx;
  }

  @Override
  public void render(
      HungSignBlockEntity entity,
      float tickDelta,
      MatrixStack matrices,
      VertexConsumerProvider vertexConsumers,
      int light,
      int overlay) {
    matrices.translate(0.5, 9 / 16f, 0.5);
    final Direction.Axis axis = entity.getCachedState().get(HungSignBlock.AXIS);
    for (Map.Entry<@NotNull Direction, @NotNull List<@NotNull TextContext>> entry :
        entity.texts.entrySet()) {
      final Direction direction = entry.getKey();
      final List<@NotNull TextContext> textContexts = entry.getValue();
      if (direction.getAxis() != axis) {
        continue;
      }
      matrices.push();
      matrices.multiply(Vec3f.POSITIVE_Y.getDegreesQuaternion(-direction.asRotation()));
      matrices.translate(0, 0, 1.0125 / 32f);
      matrices.scale(1 / 16f, -1 / 16f, 1 / 16f);
      for (TextContext textContext : textContexts) {
        textContext.draw(ctx.getTextRenderer(), matrices, vertexConsumers, light, 16, entity.getHeight());
      }
      matrices.pop();
    }
  }
}
