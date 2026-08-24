package pers.solid.mishang.uc.item;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.api.EnvironmentInterface;
import net.fabricmc.fabric.api.client.rendering.v1.level.LevelExtractionContext;
import net.fabricmc.fabric.api.client.rendering.v1.level.LevelRenderContext;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.ShapeRenderer;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.util.ARGB;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.TickRateManager;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.Shapes;
import org.jetbrains.annotations.Nullable;
import pers.solid.mishang.uc.render.RendersBeforeOutline;
import pers.solid.mishang.uc.render.state.BlockToolStateWithEntity;

/**
 * @since Minecraft 1.21.10
 */
@EnvironmentInterface(itf = RendersBeforeOutline.class, value = EnvType.CLIENT)
public abstract class BlockToolItemWithEntity extends BlockToolItem implements RendersBeforeOutline {
  public BlockToolItemWithEntity(Properties settings, @Nullable Boolean includesFluid) {
    super(settings, includesFluid);
  }

  @Environment(EnvType.CLIENT)
  @Override
  public @Nullable BlockToolStateWithEntity getMishangRenderState(LocalPlayer player, InteractionHand hand, ItemStack stack, LevelExtractionContext context, @Nullable HitResult result) {
    final BlockToolStateWithEntity state = new BlockToolStateWithEntity();

    final ClientLevel world = context.level();

    if (result instanceof BlockHitResult blockHitResult && includesFluid(stack, player.isShiftKeyDown())) {
      final BlockPos blockPos = blockHitResult.getBlockPos();
      state.lightGreenShape = world.getFluidState(blockPos).getShape(world, blockPos);
      state.lightGreenPos = blockPos;
    } else if (result instanceof EntityHitResult entityHitResult && !player.isSpectator()) {
      final Entity entity = entityHitResult.getEntity();
      final DeltaTracker deltaTracker = Minecraft.getInstance().getDeltaTracker();
      final TickRateManager tickRateManager = context.level().tickRateManager();
      float entityPartialTicks = deltaTracker.getGameTimeDeltaPartialTick(!tickRateManager.isEntityFrozen(entity));
      final Vec3 position = entity.getPosition(entityPartialTicks);
      state.greenEntityShape = Shapes.create(entity.getBoundingBox().move(position.subtract(entity.position())));
    }

    return state;
  }

  @Environment(EnvType.CLIENT)
  @Override
  public void renderBeforeOutline(LocalPlayer player, ItemStack stack, LevelRenderContext context) {
    if (!(context.levelState().getData(MISHANG_BLOCK_OUTLINE) instanceof BlockToolStateWithEntity state)) {
      return;
    }
    if (state.greenEntityShape == null) {
      return;
    }
    final PoseStack matrices = context.poseStack();
    final MultiBufferSource consumers = context.bufferSource();
    if (consumers == null) return;
    final VertexConsumer vertexConsumer = consumers.getBuffer(RenderTypes.lines());
    final Vec3 cameraPos = context.levelState().cameraRenderState.pos;
    ShapeRenderer.renderShape(matrices, vertexConsumer, state.greenEntityShape.move(-cameraPos.x, -cameraPos.y, -cameraPos.z), 0, 0, 0, ARGB.colorFromFloat(0.8f, 0f, 1f, 0f), Minecraft.getInstance().getWindow().getAppropriateLineWidth());
  }
}
