package pers.solid.mishang.uc.item;

import com.mojang.blaze3d.vertex.PoseStack;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.api.EnvironmentInterface;
import net.fabricmc.fabric.api.client.rendering.v1.level.LevelExtractionContext;
import net.fabricmc.fabric.api.client.rendering.v1.level.LevelRenderContext;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.util.ARGB;
import net.minecraft.world.InteractionHand;
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

  private static final int OUTLINE_COLOR_GREEN = ARGB.colorFromFloat(0.8f, 0f, 1f, 0f);

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
      state.greenEntityShape = Shapes.create(entity.getBoundingBox());
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
    final PoseStack poseStack = context.poseStack();
    final Vec3 cameraPos = context.levelState().cameraRenderState.pos;
    poseStack.pushPose();
    poseStack.translate(-cameraPos.x, -cameraPos.y, -cameraPos.z);
    context.submitNodeCollector().submitShapeOutline(poseStack, state.greenEntityShape, RenderTypes.lines(), OUTLINE_COLOR_GREEN, Minecraft.getInstance().getWindow().getAppropriateLineWidth(), true); // todo 检查 afterTerrain 参数
    poseStack.popPose();
  }
}
