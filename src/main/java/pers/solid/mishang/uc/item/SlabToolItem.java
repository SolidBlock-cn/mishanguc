package pers.solid.mishang.uc.item;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.api.EnvironmentInterface;
import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderContext;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.ShapeContext;
import net.minecraft.block.enums.SlabType;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.state.property.Properties;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import pers.solid.mishang.uc.mixin.WorldRendererInvoker;
import pers.solid.mishang.uc.render.RendersBlockOutline;

/**
 * 用于处理台阶的工具。
 */
@EnvironmentInterface(value = EnvType.CLIENT, itf = RendersBlockOutline.class)
public class SlabToolItem extends Item implements RendersBlockOutline {
  public SlabToolItem(Settings settings) {
    super(settings);
  }

  /**
   * 破坏台阶的一部分。
   *
   * @see Item#canMine
   */
  @Override
  public boolean canMine(BlockState state, World world, BlockPos pos, PlayerEntity miner) {
    Block block = state.getBlock();
    try {
      if (state.contains(Properties.SLAB_TYPE)
          && state.get(Properties.SLAB_TYPE) == SlabType.DOUBLE) {
        final BlockHitResult raycast = ((BlockHitResult) miner.raycast(20, 0, false));
        boolean bl = raycast.getPos().y - (double) raycast.getBlockPos().getY() > 0.5D;
        final boolean bl1;
        final BlockState brokenState;
        if (bl) {
          // 破坏上半砖的情况。
          bl1 = world.setBlockState(pos, state.with(Properties.SLAB_TYPE, SlabType.BOTTOM));
          brokenState = state.with(Properties.SLAB_TYPE, SlabType.TOP);
        } else {
          // 破坏下半砖的情况
          bl1 = world.setBlockState(pos, state.with(Properties.SLAB_TYPE, SlabType.TOP));
          brokenState = state.with(Properties.SLAB_TYPE, SlabType.BOTTOM);
        }
        block.onBreak(world, pos, brokenState, miner);
        if (bl1) {
          block.onBroken(world, pos, brokenState);
          if (miner instanceof ServerPlayerEntity && !((ServerPlayerEntity) miner).interactionManager.isCreative()) {
            block.afterBreak(world, miner, pos, brokenState, null, new ItemStack(this));
          }
        }
        // 此处还需要模拟 ClientPlayerInteractionManager 和 ServerPlayerInteractionManager 中的情形。
        return false;
      }
    } catch (IllegalArgumentException | ClassCastException ignored) {
    }
    return super.canMine(state, world, pos, miner);
  }

  @Environment(EnvType.CLIENT)
  @Override
  public boolean rendersBlockOutline(
      PlayerEntity player,
      ItemStack itemStack,
      WorldRenderContext worldRenderContext,
      WorldRenderContext.BlockOutlineContext blockOutlineContext) {
    final VertexConsumerProvider consumers = worldRenderContext.consumers();
    if (consumers == null) return true;
    final ClientWorld world = worldRenderContext.world();
    final BlockState state = blockOutlineContext.blockState();
    if (state.contains(Properties.SLAB_TYPE)
        && state.get(Properties.SLAB_TYPE) == SlabType.DOUBLE) {
      final HitResult crosshairTarget = MinecraftClient.getInstance().crosshairTarget;
      if (!(crosshairTarget instanceof BlockHitResult)) {
        return true;
      }
      boolean isTop =
          crosshairTarget.getPos().y
              - (double) ((BlockHitResult) crosshairTarget).getBlockPos().getY()
              > 0.5D;
      // 渲染时需要使用的方块状态。
      final BlockState halfState =
          state.with(Properties.SLAB_TYPE, isTop ? SlabType.TOP : SlabType.BOTTOM);
      final BlockPos blockPos = blockOutlineContext.blockPos();
      WorldRendererInvoker.drawShapeOutline(
          worldRenderContext.matrixStack(),
          consumers.getBuffer(RenderLayer.LINES),
          halfState.getOutlineShape(world, blockPos, ShapeContext.of(blockOutlineContext.entity())),
          (double) blockPos.getX() - blockOutlineContext.cameraX(),
          (double) blockPos.getY() - blockOutlineContext.cameraY(),
          (double) blockPos.getZ() - blockOutlineContext.cameraZ(),
          0.0F,
          0.0F,
          0.0F,
          0.4F);
      return false;
    }
    return true;
  }
}
