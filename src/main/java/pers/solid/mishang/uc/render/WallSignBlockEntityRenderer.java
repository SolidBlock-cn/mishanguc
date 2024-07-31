package pers.solid.mishang.uc.render;

import com.google.common.collect.ImmutableSet;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.ShapeContext;
import net.minecraft.block.enums.WallMountLocation;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactory;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.RotationAxis;
import org.jetbrains.annotations.Unmodifiable;
import pers.solid.mishang.uc.block.WallSignBlock;
import pers.solid.mishang.uc.blockentity.WallSignBlockEntity;
import pers.solid.mishang.uc.blocks.WallSignBlocks;
import pers.solid.mishang.uc.mixin.WorldRendererInvoker;
import pers.solid.mishang.uc.text.TextContext;

import java.util.Collection;

@Environment(EnvType.CLIENT)
public class WallSignBlockEntityRenderer<T extends WallSignBlockEntity> implements BlockEntityRenderer<T> {

  /**
   * 这个集合中的方块，在渲染时是视为没有厚度的，直接渲染在靠墙的位置，而不是离墙 1 格的位置。
   */
  private static final @Unmodifiable Collection<Block> INVISIBLE_BLOCKS =
      ImmutableSet.of(WallSignBlocks.INVISIBLE_WALL_SIGN, WallSignBlocks.INVISIBLE_GLOWING_WALL_SIGN);

  private final BlockEntityRendererFactory.Context ctx;

  public WallSignBlockEntityRenderer(BlockEntityRendererFactory.Context ctx) {
    this.ctx = ctx;
  }

  @Override
  public void render(T entity, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlay) {
    final Block block = entity.getCachedState().getBlock();
    // 若方块为隐形方块，且玩家手中拿着该方块，则显示该方块轮廓。
    final ClientPlayerEntity player = MinecraftClient.getInstance().player;
    if (INVISIBLE_BLOCKS.contains(block) && player != null) {
      final Item mainHandStackItem = player.getMainHandStack().getItem();
      if (mainHandStackItem instanceof final BlockItem blockItem
          && INVISIBLE_BLOCKS.contains(blockItem.getBlock())) {
        boolean glowing = entity.getCachedState().isOf(WallSignBlocks.INVISIBLE_GLOWING_WALL_SIGN);
        WorldRendererInvoker.drawCuboidShapeOutline(
            matrices,
            vertexConsumers.getBuffer(RenderLayer.LINES),
            entity.getCachedState().getOutlineShape(ctx.getRenderDispatcher().world, entity.getPos(), ShapeContext.of(player)),
            0,
            0,
            0,
            glowing ? 0.9f : 0.3f,
            0.8f,
            glowing ? 0.3f : 0.9f,
            0.9f);
      }
    }

    if (entity.glowing) {
      light = 15728880;
    }
    matrices.translate(0.5, 0.5, 0.5);
    final BlockState state = entity.getCachedState();
    final Direction facing = state.get(WallSignBlock.FACING);
    final WallMountLocation face = state.get(WallSignBlock.FACE);
    matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(-facing.asRotation()));
    matrices.multiply(
        RotationAxis.POSITIVE_X.rotationDegrees(
            face == WallMountLocation.CEILING ? 90 : face == WallMountLocation.FLOOR ? -90 : 0));
    if (face != WallMountLocation.WALL) {
      matrices.multiply(RotationAxis.POSITIVE_Z.rotationDegrees(180));
    }
    matrices.scale(1 / 16f, -1 / 16f, 1 / 16f);
    matrices.translate(0, 0, (INVISIBLE_BLOCKS.contains(block) ? -8 : -7) + .0125);
    for (TextContext textContext : entity.textContexts) {
      textContext.draw(
          ctx.getTextRenderer(), matrices, vertexConsumers, light, 16, entity.getHeight());
    }
  }
}
