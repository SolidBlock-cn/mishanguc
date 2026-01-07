package pers.solid.mishang.uc.render;

import com.google.common.collect.ImmutableSet;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.ShapeContext;
import net.minecraft.block.enums.BlockFace;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.render.RenderLayers;
import net.minecraft.client.render.VertexRendering;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactory;
import net.minecraft.client.render.command.ModelCommandRenderer;
import net.minecraft.client.render.command.OrderedRenderCommandQueue;
import net.minecraft.client.render.state.CameraRenderState;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.util.math.ColorHelper;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.RotationAxis;
import net.minecraft.util.math.Vec3d;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.Unmodifiable;
import pers.solid.mishang.uc.block.WallSignBlock;
import pers.solid.mishang.uc.blockentity.WallSignBlockEntity;
import pers.solid.mishang.uc.blocks.WallSignBlocks;
import pers.solid.mishang.uc.text.TextContext;

import java.util.Collection;

@Environment(EnvType.CLIENT)
public class WallSignBlockEntityRenderer<T extends WallSignBlockEntity> implements BlockEntityRenderer<T, WallSignBlockEntityRenderState> {

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
  public WallSignBlockEntityRenderState createRenderState() {
    return new WallSignBlockEntityRenderState();
  }

  @Override
  public void updateRenderState(T blockEntity, WallSignBlockEntityRenderState state, float tickProgress, Vec3d cameraPos, @Nullable ModelCommandRenderer.CrumblingOverlayCommand crumblingOverlay) {
    BlockEntityRenderer.super.updateRenderState(blockEntity, state, tickProgress, cameraPos, crumblingOverlay);
    state.textContexts = blockEntity.textContexts;
    state.glowing = blockEntity.glowing;
    state.height = blockEntity.getHeight();
    state.voxelShape = state.blockState.getOutlineShape(blockEntity.getWorld(), state.pos, ShapeContext.of(MinecraftClient.getInstance().player));
  }

  @Override
  public void render(WallSignBlockEntityRenderState state, MatrixStack matrices, OrderedRenderCommandQueue queue, CameraRenderState cameraState) {
    final Block block = state.blockState.getBlock();
    // 若方块为隐形方块，且玩家手中拿着该方块，则显示该方块轮廓。
    final ClientPlayerEntity player = MinecraftClient.getInstance().player;
    if (INVISIBLE_BLOCKS.contains(block) && player != null) {
      final Item mainHandStackItem = player.getMainHandStack().getItem();
      if (mainHandStackItem instanceof final BlockItem blockItem
          && INVISIBLE_BLOCKS.contains(blockItem.getBlock())) {
        boolean glowing = state.blockState.isOf(WallSignBlocks.INVISIBLE_GLOWING_WALL_SIGN);
        queue.submitCustom(matrices, RenderLayers.LINES, (matricesEntry, vertexConsumer) -> VertexRendering.drawOutline(
            matrices,
            vertexConsumer,
            state.voxelShape,
            0,
            0,
            0,
            ColorHelper.fromFloats(0.9f, glowing ? 0.9f : 0.3f,
                0.8f,
                glowing ? 0.3f : 0.9f),
            MinecraftClient.getInstance().getWindow().getMinimumLineWidth())
        );
      }
    }

    matrices.translate(0.5, 0.5, 0.5);
    final BlockState blockState = state.blockState;
    final Direction facing = blockState.get(WallSignBlock.FACING);
    final BlockFace face = blockState.get(WallSignBlock.FACE);
    matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(-facing.getPositiveHorizontalDegrees()));
    matrices.multiply(
        RotationAxis.POSITIVE_X.rotationDegrees(
            face == BlockFace.CEILING ? 90 : face == BlockFace.FLOOR ? -90 : 0));
    if (face != BlockFace.WALL) {
      matrices.multiply(RotationAxis.POSITIVE_Z.rotationDegrees(180));
    }
    matrices.scale(1 / 16f, -1 / 16f, 1 / 16f);
    matrices.translate(0, 0, (INVISIBLE_BLOCKS.contains(block) ? -8 : -7) + .0125);
    for (TextContext textContext : state.textContexts) {
      textContext.draw(
          ctx.textRenderer(), matrices, queue, state.glowing ? 15728880 : state.lightmapCoordinates, 16, state.height);
    }
  }
}
