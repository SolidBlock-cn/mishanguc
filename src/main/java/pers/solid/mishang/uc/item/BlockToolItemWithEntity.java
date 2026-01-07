package pers.solid.mishang.uc.item;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.api.EnvironmentInterface;
import net.fabricmc.fabric.api.client.rendering.v1.world.WorldExtractionContext;
import net.fabricmc.fabric.api.client.rendering.v1.world.WorldRenderContext;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.VertexRendering;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ColorHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.shape.VoxelShapes;
import org.jetbrains.annotations.Nullable;
import pers.solid.mishang.uc.render.RendersBeforeOutline;
import pers.solid.mishang.uc.render.state.BlockToolStateWithEntity;
import pers.solid.mishang.uc.render.state.MishangRenderState;

/**
 * @since Minecraft 1.21.10
 */
@EnvironmentInterface(itf = RendersBeforeOutline.class, value = EnvType.CLIENT)
public abstract class BlockToolItemWithEntity extends BlockToolItem implements RendersBeforeOutline {
  public BlockToolItemWithEntity(Settings settings, @Nullable Boolean includesFluid) {
    super(settings, includesFluid);
  }

  @Environment(EnvType.CLIENT)
  @Override
  public @Nullable BlockToolStateWithEntity getMishangRenderState(@Nullable MishangRenderState previous, ClientPlayerEntity player, Hand hand, ItemStack stack, WorldExtractionContext context, @Nullable HitResult result) {
    final BlockToolStateWithEntity state = previous instanceof BlockToolStateWithEntity blockToolState ? blockToolState : new BlockToolStateWithEntity();

    final ClientWorld world = context.world();

    if (result instanceof BlockHitResult blockHitResult && includesFluid(stack, player.isSneaking())) {
      final BlockPos blockPos = blockHitResult.getBlockPos();
      state.lightGreenShape = world.getFluidState(blockPos).getShape(world, blockPos);
      state.lightGreenPos = blockPos;
    } else if (result instanceof EntityHitResult entityHitResult && !player.isSpectator()) {
      final Entity entity = entityHitResult.getEntity();
      state.greenEntityShape = VoxelShapes.cuboid(entity.getBoundingBox());
    }

    return state;
  }

  @Environment(EnvType.CLIENT)
  @Override
  public void renderBeforeOutline(ClientPlayerEntity player, ItemStack stack, WorldRenderContext context) {
    if (!(context.worldState().getData(MISHANG_BLOCK_OUTLINE) instanceof BlockToolStateWithEntity state)) {
      return;
    }
    if (state.greenEntityShape == null) {
      return;
    }
    final MatrixStack matrices = context.matrices();
    final VertexConsumerProvider consumers = context.consumers();
    if (consumers == null) return;
    final VertexConsumer vertexConsumer = consumers.getBuffer(RenderLayer.getLines());
    final Vec3d cameraPos = context.worldState().cameraRenderState.pos;
    VertexRendering.drawOutline(matrices, vertexConsumer, state.greenEntityShape, -cameraPos.x, -cameraPos.y, -cameraPos.z, ColorHelper.fromFloats(0.8f, 0f, 1f, 0f));
  }
}
