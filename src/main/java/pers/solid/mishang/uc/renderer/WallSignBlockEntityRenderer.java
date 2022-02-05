package pers.solid.mishang.uc.renderer;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.BlockState;
import net.minecraft.block.enums.WallMountLocation;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.entity.BlockEntityRenderDispatcher;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3f;
import pers.solid.mishang.uc.block.WallSignBlock;
import pers.solid.mishang.uc.blockentity.WallSignBlockEntity;
import pers.solid.mishang.uc.util.TextContext;

@Environment(EnvType.CLIENT)
public class WallSignBlockEntityRenderer extends BlockEntityRenderer<WallSignBlockEntity> {
  public WallSignBlockEntityRenderer(BlockEntityRenderDispatcher dispatcher) {
    super(dispatcher);
  }

  @Override
  public void render(
      WallSignBlockEntity entity,
      float tickDelta,
      MatrixStack matrices,
      VertexConsumerProvider vertexConsumers,
      int light,
      int overlay) {
    matrices.translate(0.5, 0.5, 0.5);
    final BlockState state = entity.getCachedState();
    final Direction facing = state.get(WallSignBlock.FACING);
    final WallMountLocation face = state.get(WallSignBlock.FACE);
    matrices.multiply(Vec3f.POSITIVE_Y.getDegreesQuaternion(-facing.asRotation()));
    matrices.multiply(
        Vec3f.POSITIVE_X.getDegreesQuaternion(
            face == WallMountLocation.CEILING ? 90 : face == WallMountLocation.FLOOR ? -90 : 0));
    if (face != WallMountLocation.WALL) {
      matrices.multiply(Vec3f.POSITIVE_Z.getDegreesQuaternion(180));
    }
    matrices.scale(1 / 16f, -1 / 16f, 1 / 16f);
    matrices.translate(0, 0, -7 + .001);
    for (TextContext textContext : entity.textContexts) {
      textContext.draw(dispatcher.getTextRenderer(), matrices, vertexConsumers, light, 16, 6);
    }
  }
}
