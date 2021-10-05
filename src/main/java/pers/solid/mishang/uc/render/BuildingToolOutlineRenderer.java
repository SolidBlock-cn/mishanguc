package pers.solid.mishang.uc.render;

import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderContext;
import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderEvents;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.debug.DebugRenderer;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.shape.VoxelShape;
import pers.solid.mishang.uc.item.FastBuildingToolItem;
import pers.solid.mishang.uc.mixin.WorldRendererAccessor;
import pers.solid.mishang.uc.util.BlockMatchingRule;

public class BuildingToolOutlineRenderer implements WorldRenderEvents.BlockOutline{
    @Override
    public boolean onBlockOutline(WorldRenderContext worldRenderContext, WorldRenderContext.BlockOutlineContext blockOutlineContext) {
        final BlockPos blockPos = blockOutlineContext.blockPos();
        DebugRenderer.drawBox(new Box(blockPos),0.2f,1,1,1);
//        Matrix4f matrix4f = worldRenderContext.matrixStack().peek().getModel();
        final VertexConsumer vertexConsumer = worldRenderContext.consumers().getBuffer(RenderLayer.getLines());
        Vec3d vec3d = worldRenderContext.camera().getPos();
        PlayerEntity player;
        try {
            player = (PlayerEntity) blockOutlineContext.entity();
        } catch (ClassCastException e) {
            return true;
        }
        final ItemStack mainHandStack = player.getMainHandStack();
        final Item item = mainHandStack.getItem();
        if (item instanceof FastBuildingToolItem) {
            final BlockMatchingRule matchingRule = ((FastBuildingToolItem) item).getMatchingRule(mainHandStack);
            final int range = ((FastBuildingToolItem) item).getRange(mainHandStack);
            final BlockHitResult raycast;
            try {
                raycast = (BlockHitResult) player.raycast(20, 0, false);
            } catch (ClassCastException e) {
                return true;
            }
            final ClientWorld world = worldRenderContext.world();
            for (BlockPos pos : matchingRule.getPlainValidBlockPoss(world, raycast.getBlockPos(), raycast.getSide(), range)) {
                BlockPos newPos = pos.offset(raycast.getSide());
//                double d = newPos.getX() - vec3d.getX();
//                double e = newPos.getY() - vec3d.getY();
//                double f = newPos.getZ() - vec3d.getZ();
//                double d2 = pos.getX() - vec3d.getX();
//                double e2 = pos.getY() - vec3d.getY();
//                double f2 = pos.getZ() - vec3d.getZ();
                final VoxelShape outlineShape = world.getBlockState(pos).getOutlineShape(world, pos);
//                outlineShape.forEachEdge((k, l, m, n, o, p) -> {
//                    vertexConsumer.vertex(matrix4f, (float) (k + d), (float) (l + e), (float) (m + f)).color(0, 1, 1, 0.8f).next();
//                    vertexConsumer.vertex(matrix4f, (float) (n + d), (float) (o + e), (float) (p + f)).color(0, 1, 1, 0.8f).next();
//                    vertexConsumer.vertex(matrix4f, (float) (k + d2), (float) (l + e2), (float) (m + f2)).color(1,0,0,0.8f).next();
//                    vertexConsumer.vertex(matrix4f, (float) (n + d2), (float) (o + e2), (float) (p + f2)).color(1,0,0,0.8f).next();
//                });
                WorldRendererAccessor.drawShapeOutline(worldRenderContext.matrixStack(), vertexConsumer,outlineShape, newPos.getX()- vec3d.getX(), newPos.getY()- vec3d.getY(), newPos.getZ()- vec3d.getZ(), 0,1,1,0.8f);
                WorldRendererAccessor.drawShapeOutline(worldRenderContext.matrixStack(), vertexConsumer,outlineShape, pos.getX()- vec3d.getX(), pos.getY()- vec3d.getY(), pos.getZ()- vec3d.getZ(), 1,0,0,0.8f);
            }
            return false;
        }
        return true;
    }
}
