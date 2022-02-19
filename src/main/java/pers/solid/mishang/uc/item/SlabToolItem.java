package pers.solid.mishang.uc.item;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderContext;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.ShapeContext;
import net.minecraft.block.enums.SlabType;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.state.property.Properties;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Formatting;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import pers.solid.mishang.uc.mixin.WorldRendererInvoker;
import pers.solid.mishang.uc.render.RendersBlockOutline;

import java.util.List;

/** 用于处理台阶的工具。 */
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
        if (bl) {
          // 破坏上半砖的情况。
          world.setBlockState(pos, state.with(Properties.SLAB_TYPE, SlabType.BOTTOM));
          block.onBreak(world, pos, state.with(Properties.SLAB_TYPE, SlabType.TOP), miner);
          block.onBreak(world, pos, state.with(Properties.SLAB_TYPE, SlabType.TOP), miner);
        } else {
          // 破坏下半砖的情况
          world.setBlockState(pos, state.with(Properties.SLAB_TYPE, SlabType.TOP));
          block.onBreak(world, pos, state.with(Properties.SLAB_TYPE, SlabType.BOTTOM), miner);
          block.onBreak(world, pos, state.with(Properties.SLAB_TYPE, SlabType.BOTTOM), miner);
        }
        // 此处还需要模拟 ClientPlayerInteractionManager 和 ServerPlayerInteractionManager 中的情形。
        return false;
      }
    } catch (IllegalArgumentException | ClassCastException ignored) {
    }
    return super.canMine(state, world, pos, miner);
  }

  @Override
  public void appendTooltip(
      ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
    super.appendTooltip(stack, world, tooltip, context);
    tooltip.add(
        new TranslatableText("item.mishanguc.slab_tool.tooltip")
            .setStyle(Style.EMPTY.withColor(Formatting.GRAY)));
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
      boolean bl =
          crosshairTarget.getPos().y
                  - (double) ((BlockHitResult) crosshairTarget).getBlockPos().getY()
              > 0.5D;
      // 渲染时需要使用的方块状态。
      final BlockState halfState =
          state.with(Properties.SLAB_TYPE, bl ? SlabType.TOP : SlabType.BOTTOM);
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
