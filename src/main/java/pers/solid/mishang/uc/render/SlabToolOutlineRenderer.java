package pers.solid.mishang.uc.render;

import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderContext;
import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderEvents;
import net.minecraft.block.BlockState;
import net.minecraft.block.ShapeContext;
import net.minecraft.block.enums.SlabType;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.state.property.Properties;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;
import pers.solid.mishang.uc.item.SlabToolItem;
import pers.solid.mishang.uc.mixin.WorldRendererInvoker;

public class SlabToolOutlineRenderer implements WorldRenderEvents.BlockOutline {
    @Override
    public boolean onBlockOutline(WorldRenderContext worldRenderContext, WorldRenderContext.BlockOutlineContext blockOutlineContext) {
        final ClientWorld world = worldRenderContext.world();
        final BlockState state = blockOutlineContext.blockState();
        if (blockOutlineContext.entity() instanceof PlayerEntity && ((PlayerEntity) blockOutlineContext.entity()).getMainHandStack().getItem() instanceof SlabToolItem && state.contains(Properties.SLAB_TYPE) && state.get(Properties.SLAB_TYPE) == SlabType.DOUBLE) {
            final HitResult crosshairTarget = MinecraftClient.getInstance().crosshairTarget;
            if (!(crosshairTarget instanceof BlockHitResult)) return true;
            boolean bl = crosshairTarget.getPos().y - (double) ((BlockHitResult) crosshairTarget).getBlockPos().getY() > 0.5D;
            // 渲染时需要使用的方块状态。
            final BlockState halfState = state.with(Properties.SLAB_TYPE, bl ? SlabType.TOP : SlabType.BOTTOM);
            final BlockPos blockPos = blockOutlineContext.blockPos();
            WorldRendererInvoker.drawShapeOutline(worldRenderContext.matrixStack(), blockOutlineContext.vertexConsumer(), halfState.getOutlineShape(world, blockPos, ShapeContext.of(blockOutlineContext.entity())), (double) blockPos.getX() - blockOutlineContext.cameraX(), (double) blockPos.getY() - blockOutlineContext.cameraY(), (double) blockPos.getZ() - blockOutlineContext.cameraZ(), 0.0F, 0.0F, 0.0F, 0.4F);
            return false;
        }
        return true;
    }
}
