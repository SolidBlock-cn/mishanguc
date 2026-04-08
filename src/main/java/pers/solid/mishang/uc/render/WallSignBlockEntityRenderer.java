package pers.solid.mishang.uc.render;

import com.google.common.collect.ImmutableSet;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.ShapeRenderer;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.feature.ModelFeatureRenderer;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.ARGB;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.AttachFace;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;
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

  private final BlockEntityRendererProvider.Context ctx;

  public WallSignBlockEntityRenderer(BlockEntityRendererProvider.Context ctx) {
    this.ctx = ctx;
  }

  @Override
  public WallSignBlockEntityRenderState createRenderState() {
    return new WallSignBlockEntityRenderState();
  }

  @Override
  public void extractRenderState(T blockEntity, WallSignBlockEntityRenderState state, float tickProgress, Vec3 cameraPos, @Nullable ModelFeatureRenderer.CrumblingOverlay crumblingOverlay) {
    BlockEntityRenderer.super.extractRenderState(blockEntity, state, tickProgress, cameraPos, crumblingOverlay);
    state.textContexts = blockEntity.textContexts;
    state.glowing = blockEntity.glowing;
    state.height = blockEntity.getHeight();
    final BlockState blockState = blockEntity.getBlockState();
    state.voxelShape = blockState.getShape(blockEntity.getLevel(), state.blockPos, CollisionContext.of(Minecraft.getInstance().player));
    state.face = blockState.getValue(WallSignBlock.FACE);
    state.facing = blockState.getValue(WallSignBlock.FACING);
    state.invisible = INVISIBLE_BLOCKS.contains(blockState.getBlock());
    state.isGlowingBlock = blockState.is(WallSignBlocks.INVISIBLE_GLOWING_WALL_SIGN);
  }

  @Override
  public void submit(WallSignBlockEntityRenderState state, PoseStack matrices, SubmitNodeCollector queue, CameraRenderState cameraState) {
    final BlockPos pos = state.blockPos;
    // 若方块为隐形方块，且玩家手中拿着该方块，则显示该方块轮廓。
    final LocalPlayer player = Minecraft.getInstance().player;
    if (state.invisible && player != null) {
      final Item mainHandStackItem = player.getMainHandItem().getItem();
      if (mainHandStackItem instanceof final BlockItem blockItem
          && INVISIBLE_BLOCKS.contains(blockItem.getBlock())) {
        boolean glowing = state.isGlowingBlock;
        queue.submitCustomGeometry(matrices, RenderTypes.LINES, (matricesEntry, vertexConsumer) -> ShapeRenderer.renderShape(
            matrices,
            vertexConsumer,
            state.voxelShape,
            pos.getX() - cameraState.pos.x,
            pos.getY() - cameraState.pos.y,
            pos.getZ() - cameraState.pos.z,
            ARGB.colorFromFloat(0.9f, glowing ? 0.9f : 0.3f,
                0.8f,
                glowing ? 0.3f : 0.9f),
            Minecraft.getInstance().getWindow().getAppropriateLineWidth())
        );
      }
    }

    matrices.translate(0.5, 0.5, 0.5);
    final Direction facing = state.facing;
    final AttachFace face = state.face;
    matrices.mulPose(Axis.YP.rotationDegrees(-facing.toYRot()));
    matrices.mulPose(
        Axis.XP.rotationDegrees(
            face == AttachFace.CEILING ? 90 : face == AttachFace.FLOOR ? -90 : 0));
    if (face != AttachFace.WALL) {
      matrices.mulPose(Axis.ZP.rotationDegrees(180));
    }
    matrices.scale(1 / 16f, -1 / 16f, 1 / 16f);
    matrices.translate(0, 0, (state.invisible ? -8 : -7) + .0125);
    for (TextContext textContext : state.textContexts) {
      textContext.draw(
          ctx.font(), matrices, queue, state.glowing ? 15728880 : state.lightCoords, 16, state.height);
    }
  }
}
