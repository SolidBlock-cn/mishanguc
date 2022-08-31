package pers.solid.mishang.uc.item;

import com.google.common.collect.BiMap;
import com.google.common.collect.ImmutableBiMap;
import com.google.common.collect.Maps;
import net.devtech.arrp.generator.ItemResourceGenerator;
import net.devtech.arrp.json.models.JModel;
import net.devtech.arrp.json.recipe.JRecipe;
import net.devtech.arrp.json.recipe.JShapedRecipe;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.api.EnvironmentInterface;
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
import net.minecraft.data.family.BlockFamilies;
import net.minecraft.data.family.BlockFamily;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.state.property.Properties;
import net.minecraft.state.property.Property;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import pers.solid.mishang.uc.mixin.WorldRendererInvoker;
import pers.solid.mishang.uc.render.RendersBlockOutline;
import pers.solid.mishang.uc.util.TextBridge;

import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * 用于处理台阶的工具。
 */
@EnvironmentInterface(value = EnvType.CLIENT, itf = RendersBlockOutline.class)
public class SlabToolItem extends Item implements RendersBlockOutline, ItemResourceGenerator {
  /**
   * 从原版的 {@link BlockFamilies} 提取的方块至台阶方块的映射。
   */
  @ApiStatus.AvailableSince("0.1.3")
  protected static final BiMap<Block, Block> BLOCK_TO_SLAB = BlockFamilies.getFamilies()
      .filter(blockFamily -> blockFamily.getVariant(BlockFamily.Variant.SLAB) != null)
      .map(blockFamily -> {
        final Block variant = blockFamily.getVariant(BlockFamily.Variant.SLAB);
        final Block baseBlock = blockFamily.getBaseBlock();
        return baseBlock == null || variant == null ? null : Maps.immutableEntry(baseBlock, variant);
      })
      .filter(Objects::nonNull)
      .collect(ImmutableBiMap.toImmutableBiMap(Map.Entry::getKey, Map.Entry::getValue));

  public SlabToolItem(Settings settings) {
    super(settings);
  }

  /**
   * 将基础方块的方块状态转化为台阶方块，并尝试移植相应的方块状态属性。
   *
   * @param baseBlockState 基础方块的方块状态。
   * @param slabBlock      台阶方块，不是具体的方块状态。
   * @return 台阶方块的方块状态。
   */
  @SuppressWarnings("unchecked")
  protected static BlockState toSlab(BlockState baseBlockState, Block slabBlock) {
    BlockState slabState = slabBlock.getDefaultState();
    for (@SuppressWarnings("rawtypes") Property property : baseBlockState.getProperties()) {
      try {
        slabState = slabState.with(property, baseBlockState.get(property));
      } catch (IllegalArgumentException ignored) {
      }
    }
    return slabState.with(Properties.SLAB_TYPE, SlabType.DOUBLE);
  }

  @Override
  public void appendTooltip(
      ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
    super.appendTooltip(stack, world, tooltip, context);
    tooltip.add(TextBridge.translatable("item.mishanguc.slab_tool.tooltip").formatted(Formatting.GRAY));
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
      if (BLOCK_TO_SLAB.containsKey(state.getBlock())) {
        state = toSlab(state, BLOCK_TO_SLAB.get(state.getBlock()));
      }
      if (state.contains(Properties.SLAB_TYPE)
          && state.get(Properties.SLAB_TYPE) == SlabType.DOUBLE) {
        final BlockHitResult raycast = ((BlockHitResult) miner.raycast(20, 0, false));
        boolean isTop = raycast.getPos().y - (double) raycast.getBlockPos().getY() > 0.5D;
        final SlabType slabTypeToSet = isTop ? SlabType.BOTTOM : SlabType.TOP;
        final SlabType slabTypeBroken = isTop ? SlabType.TOP : SlabType.BOTTOM;
        // 破坏上半砖的情况。
        final boolean bl1 = world.setBlockState(pos, state.with(Properties.SLAB_TYPE, slabTypeToSet));
        final BlockState brokenState = state.with(Properties.SLAB_TYPE, slabTypeBroken);
        block.onBreak(world, pos, brokenState, miner);
        if (bl1) {
          block.onBroken(world, pos, brokenState);
          if (!miner.isCreative()) {
            block.afterBreak(world, miner, pos, brokenState, world.getBlockEntity(pos), miner.getMainHandStack().copy());
          }
          miner.getStackInHand(Hand.MAIN_HAND).damage(1, miner, player -> player.sendToolBreakStatus(Hand.MAIN_HAND));
        }
        return !bl1;
      }
    } catch (IllegalArgumentException | ClassCastException ignored) {
    }
    return true;
  }

  @Environment(EnvType.CLIENT)
  @Override
  public boolean renderBlockOutline(
      PlayerEntity player,
      ItemStack itemStack,
      WorldRenderContext worldRenderContext,
      WorldRenderContext.BlockOutlineContext blockOutlineContext, Hand hand) {
    final VertexConsumerProvider consumers = worldRenderContext.consumers();
    if (consumers == null) return true;
    final ClientWorld world = worldRenderContext.world();
    BlockState state = blockOutlineContext.blockState();
    final HitResult crosshairTarget = MinecraftClient.getInstance().crosshairTarget;
    if (!(crosshairTarget instanceof final BlockHitResult blockHitResult)) {
      return true;
    }
    boolean isTop =
        crosshairTarget.getPos().y
            - (double) blockHitResult.getBlockPos().getY()
            > 0.5D;
    if (BLOCK_TO_SLAB.containsKey(state.getBlock())) {
      state = toSlab(state, BLOCK_TO_SLAB.get(state.getBlock()));
    }
    if (state.contains(Properties.SLAB_TYPE)
        && state.get(Properties.SLAB_TYPE) == SlabType.DOUBLE) {
      // 渲染时需要使用的方块状态。
      final BlockState halfState =
          state.with(Properties.SLAB_TYPE, isTop ? SlabType.TOP : SlabType.BOTTOM);
      final BlockPos blockPos = blockOutlineContext.blockPos();
      WorldRendererInvoker.drawCuboidShapeOutline(
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

  @Environment(EnvType.CLIENT)
  @Override
  public @Nullable JModel getItemModel() {
    return null;
  }

  @Override
  public @NotNull JRecipe getCraftingRecipe() {
    return new JShapedRecipe(this)
        .pattern("SCS", " | ", " | ")
        .addKey("S", Items.SHEARS)
        .addKey("C", Items.STONE)
        .addKey("|", Items.STICK)
        .addInventoryChangedCriterion("has_shears", Items.SHEARS)
        .addInventoryChangedCriterion("has_stone", Items.STONE);
  }
}
