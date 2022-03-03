package pers.solid.mishang.uc.item;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderContext;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Formatting;
import net.minecraft.util.Hand;
import net.minecraft.util.Util;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import pers.solid.mishang.uc.mixin.WorldRendererInvoker;
import pers.solid.mishang.uc.util.BlockMatchingRule;
import pers.solid.mishang.uc.util.BlockPlacementContext;

import java.util.List;

/**
 * 该物品可以快速建造或者删除一个平面上的多个方块。
 *
 * @see BlockMatchingRule
 */
public class FastBuildingToolItem extends BlockToolItem {

  public FastBuildingToolItem(Settings settings, @Nullable Boolean includesFluid) {
    super(settings, includesFluid);
  }

  @Override
  public ActionResult useOnBlock(
      PlayerEntity player,
      World world,
      BlockHitResult blockHitResult,
      Hand hand,
      boolean fluidIncluded) {
    final Direction side = blockHitResult.getSide();
    final BlockPos centerBlockPos = blockHitResult.getBlockPos();
    final BlockState centerState = world.getBlockState(centerBlockPos);
    final ItemStack stack = player.getStackInHand(hand);
    final BlockPlacementContext blockPlacementContext =
        new BlockPlacementContext(
            world, centerBlockPos, player, stack, blockHitResult, fluidIncluded);
    final int range = this.getRange(stack);
    final BlockMatchingRule matchingRule = this.getMatchingRule(stack);
    boolean soundPlayed = false;
    for (BlockPos pos : matchingRule.getPlainValidBlockPoss(world, centerBlockPos, side, range)) {
      BlockState state = world.getBlockState(pos);
      if (matchingRule.match(centerState, state)) {
        final BlockPlacementContext offsetBlockPlacementContext =
            new BlockPlacementContext(blockPlacementContext, pos);
        if (offsetBlockPlacementContext.canPlace() && offsetBlockPlacementContext.canReplace()) {
          if (!world.isClient) {
            offsetBlockPlacementContext.setBlockState(0b1011);
            offsetBlockPlacementContext.setBlockEntity();
          }
          if (!soundPlayed) offsetBlockPlacementContext.playSound();
          soundPlayed = true;
        }
      }
    } // end for
    return ActionResult.SUCCESS;
  }

  @Override
  public ActionResult beginAttackBlock(
      PlayerEntity player, World world, BlockPos pos, Direction direction, boolean fluidIncluded) {
    if (!world.isClient()) {
      final ItemStack stack = player.getMainHandStack();
      final int range = this.getRange(stack);
      final BlockMatchingRule matchingRule = this.getMatchingRule(stack);
      for (BlockPos pos1 : matchingRule.getPlainValidBlockPoss(world, pos, direction, range)) {
        if (fluidIncluded) {
          world.setBlockState(pos1, Blocks.AIR.getDefaultState());
        } else {
          world.removeBlock(pos1, false);
        }
      }
    }
    world.syncWorldEvent(player, 2001, pos, Block.getRawIdFromState(world.getBlockState(pos)));
    return ActionResult.SUCCESS;
  }

  @Override
  public ItemStack getDefaultStack() {
    return Util.make(
        super.getDefaultStack(),
        stack -> {
          final NbtCompound tag = stack.getOrCreateNbt();
          tag.putInt("Range", 5);
          tag.putString("MatchingRule", "mishanguc:same_block");
        });
  }

  public int getRange(ItemStack stack) {
    final NbtCompound tag = stack.getOrCreateNbt();
    return tag.contains("Range", 99) ? Integer.min(tag.getInt("Range"), 128) : 8;
  }

  public @NotNull BlockMatchingRule getMatchingRule(ItemStack stack) {
    final NbtCompound tag = stack.getOrCreateNbt();
    final BlockMatchingRule matchingRule =
        BlockMatchingRule.fromString(tag.getString("MatchingRule"));
    return matchingRule == null ? BlockMatchingRule.SAME_BLOCK : matchingRule;
  }

  @Override
  public void appendTooltip(
      ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
    super.appendTooltip(stack, world, tooltip, context);
    tooltip.add(
        new TranslatableText("item.mishanguc.fast_building_tool.tooltip")
            .formatted(Formatting.GRAY));
    tooltip.add(
        new TranslatableText(
            "item.mishanguc.fast_building_tool.tooltip.range", this.getRange(stack))
            .formatted(Formatting.GRAY));
    tooltip.add(
        new TranslatableText(
            "item.mishanguc.fast_building_tool.tooltip.matchingRule",
            this.getMatchingRule(stack).getName())
            .formatted(Formatting.GRAY));
  }

  @Override
  public void appendStacks(ItemGroup group, DefaultedList<ItemStack> stacks) {
    if (this.isIn(group)) {
      stacks.add(
          Util.make(
              new ItemStack(this),
              stack -> {
                final NbtCompound tag = stack.getOrCreateNbt();
                tag.putInt("Range", 16);
                tag.putString("MatchingRule", BlockMatchingRule.SAME_STATE.asString());
              }));
      stacks.add(
          Util.make(
              new ItemStack(this),
              stack -> {
                final NbtCompound tag = stack.getOrCreateNbt();
                tag.putInt("Range", 64);
                tag.putString("MatchingRule", BlockMatchingRule.SAME_STATE.asString());
              }));
      stacks.add(
          Util.make(
              new ItemStack(this),
              stack -> {
                final NbtCompound tag = stack.getOrCreateNbt();
                tag.putInt("Range", 16);
                tag.putString("MatchingRule", BlockMatchingRule.SAME_BLOCK.asString());
              }));
      stacks.add(
          Util.make(
              new ItemStack(this),
              stack -> {
                final NbtCompound tag = stack.getOrCreateNbt();
                tag.putInt("Range", 64);
                tag.putString("MatchingRule", BlockMatchingRule.SAME_BLOCK.asString());
              }));
      stacks.add(
          Util.make(
              new ItemStack(this),
              stack -> {
                final NbtCompound tag = stack.getOrCreateNbt();
                tag.putInt("Range", 16);
                tag.putString("MatchingRule", BlockMatchingRule.SAME_MATERIAL.asString());
              }));
      stacks.add(
          Util.make(
              new ItemStack(this),
              stack -> {
                final NbtCompound tag = stack.getOrCreateNbt();
                tag.putInt("Range", 64);
                tag.putString("MatchingRule", BlockMatchingRule.SAME_MATERIAL.asString());
              }));
      stacks.add(
          Util.make(
              new ItemStack(this),
              stack -> {
                final NbtCompound tag = stack.getOrCreateNbt();
                tag.putInt("Range", 16);
                tag.putString("MatchingRule", BlockMatchingRule.ANY.asString());
              }));
      stacks.add(
          Util.make(
              new ItemStack(this),
              stack -> {
                final NbtCompound tag = stack.getOrCreateNbt();
                tag.putInt("Range", 64);
                tag.putString("MatchingRule", BlockMatchingRule.ANY.asString());
              }));
    }
  }

  @Override
  public Text getName(ItemStack stack) {
    return new LiteralText("")
        .append(super.getName(stack))
        .append(" - ")
        .append(getMatchingRule(stack).getName());
  }

  @Environment(EnvType.CLIENT)
  @Override
  public boolean rendersBlockOutline(
      PlayerEntity player,
      ItemStack mainHandStack,
      WorldRenderContext worldRenderContext,
      WorldRenderContext.BlockOutlineContext blockOutlineContext) {
    final VertexConsumerProvider consumers = worldRenderContext.consumers();
    if (consumers == null) return true;
    final VertexConsumer vertexConsumer = consumers.getBuffer(RenderLayer.LINES);
    final boolean includesFluid = this.includesFluid(mainHandStack, player.isSneaking());
    final BlockMatchingRule matchingRule = this.getMatchingRule(mainHandStack);
    final int range = this.getRange(mainHandStack);
    final BlockHitResult raycast;
    try {
      raycast = (BlockHitResult) MinecraftClient.getInstance().crosshairTarget;
      if (raycast == null) {
        return true;
      }
    } catch (ClassCastException e) {
      return true;
    }
    final ClientWorld world = worldRenderContext.world();
    final BlockPlacementContext blockPlacementContext =
        new BlockPlacementContext(
            world, blockOutlineContext.blockPos(), player, mainHandStack, raycast, false);
    for (BlockPos pos :
        matchingRule.getPlainValidBlockPoss(
            world, raycast.getBlockPos(), raycast.getSide(), range)) {
      final BlockState state = world.getBlockState(pos);
      final BlockPlacementContext offsetBlockPlacementContext =
          new BlockPlacementContext(blockPlacementContext, pos);
      if (offsetBlockPlacementContext.canPlace() && offsetBlockPlacementContext.canReplace()) {
        WorldRendererInvoker.drawShapeOutline(
            worldRenderContext.matrixStack(),
            vertexConsumer,
            offsetBlockPlacementContext.stateToPlace.getOutlineShape(world, pos),
            offsetBlockPlacementContext.posToPlace.getX() - blockOutlineContext.cameraX(),
            offsetBlockPlacementContext.posToPlace.getY() - blockOutlineContext.cameraY(),
            offsetBlockPlacementContext.posToPlace.getZ() - blockOutlineContext.cameraZ(),
            0,
            1,
            1,
            0.8f);
        if (includesFluid) {
          WorldRendererInvoker.drawShapeOutline(
              worldRenderContext.matrixStack(),
              vertexConsumer,
              offsetBlockPlacementContext.stateToPlace.getFluidState().getShape(world, pos),
              offsetBlockPlacementContext.posToPlace.getX() - blockOutlineContext.cameraX(),
              offsetBlockPlacementContext.posToPlace.getY() - blockOutlineContext.cameraY(),
              offsetBlockPlacementContext.posToPlace.getZ() - blockOutlineContext.cameraZ(),
              0,
              0.25f,
              1,
              0.4f);
        }
      }
      WorldRendererInvoker.drawShapeOutline(
          worldRenderContext.matrixStack(),
          vertexConsumer,
          state.getOutlineShape(world, pos),
          pos.getX() - blockOutlineContext.cameraX(),
          pos.getY() - blockOutlineContext.cameraY(),
          pos.getZ() - blockOutlineContext.cameraZ(),
          1,
          0,
          0,
          0.8f);
      if (includesFluid) {
        WorldRendererInvoker.drawShapeOutline(
            worldRenderContext.matrixStack(),
            vertexConsumer,
            state.getFluidState().getShape(world, pos),
            pos.getX() - blockOutlineContext.cameraX(),
            pos.getY() - blockOutlineContext.cameraY(),
            pos.getZ() - blockOutlineContext.cameraZ(),
            1,
            0.75f,
            0,
            0.4f);
      }
    }
    return false;
  }
}
