package pers.solid.mishang.uc.item;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderContext;
import net.minecraft.block.*;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.item.TooltipType;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Formatting;
import net.minecraft.util.Hand;
import net.minecraft.util.Util;
import net.minecraft.util.collection.Int2ObjectBiMap;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import pers.solid.mishang.uc.components.FastBuildingToolData;
import pers.solid.mishang.uc.components.MishangucComponents;
import pers.solid.mishang.uc.mixin.WorldRendererInvoker;
import pers.solid.mishang.uc.util.BlockMatchingRule;
import pers.solid.mishang.uc.util.BlockPlacementContext;
import pers.solid.mishang.uc.util.TextBridge;

import java.util.Iterator;
import java.util.List;

/**
 * 该物品可以快速建造或者删除一个平面上的多个方块。
 *
 * @see BlockMatchingRule
 */
public class FastBuildingToolItem extends BlockToolItem implements HotbarScrollInteraction {

  private static final Int2ObjectBiMap<BlockMatchingRule> RULES_TO_CYCLE = Util.make(Int2ObjectBiMap.create(4), map -> {
    map.add(BlockMatchingRule.SAME_STATE);
    map.add(BlockMatchingRule.SAME_BLOCK);
    map.add(BlockMatchingRule.SAME_MATERIAL);
    map.add(BlockMatchingRule.ANY);
  });

  public FastBuildingToolItem(Settings settings, @Nullable Boolean includesFluid) {
    super(settings.component(MishangucComponents.FAST_BUILDING_TOOL_DATA, FastBuildingToolData.DEFAULT), includesFluid);
  }

  @Override
  public ActionResult useOnBlock(
      ItemStack stack, PlayerEntity player,
      World world,
      BlockHitResult blockHitResult,
      Hand hand,
      boolean fluidIncluded) {
    if (!player.isCreative()) {
      // 仅限创造模式玩家使用。
      return ActionResult.PASS;
    }
    final Direction side = blockHitResult.getSide();
    final BlockPos centerBlockPos = blockHitResult.getBlockPos();
    final BlockState centerState = world.getBlockState(centerBlockPos);
    final BlockPlacementContext blockPlacementContext = new BlockPlacementContext(
        world, centerBlockPos, player, stack, blockHitResult, fluidIncluded);
    final FastBuildingToolData data = stack.getOrDefault(MishangucComponents.FAST_BUILDING_TOOL_DATA, FastBuildingToolData.DEFAULT);
    final int range = data.range();
    final BlockMatchingRule matchingRule = data.matchingRule();
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
      ItemStack stack, PlayerEntity player, World world, Hand hand, BlockPos pos, Direction direction, boolean fluidIncluded) {
    if (!player.isCreative()) {
      // 仅限创造模式玩家使用。
      return ActionResult.PASS;
    }
    if (!world.isClient()) {
      final FastBuildingToolData data = stack.getOrDefault(MishangucComponents.FAST_BUILDING_TOOL_DATA, FastBuildingToolData.DEFAULT);
      final int range = data.range();
      final BlockMatchingRule matchingRule = data.matchingRule();
      for (BlockPos pos1 : matchingRule.getPlainValidBlockPoss(world, pos, direction, range)) {
        if (world.getBlockState(pos1).getBlock() instanceof OperatorBlock && !player.hasPermissionLevel(2)) {
          // 非管理员不应该破坏管理方块。
        } else if (fluidIncluded) {
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
  public void appendTooltip(ItemStack stack, TooltipContext context, List<Text> tooltip, TooltipType type) {
    super.appendTooltip(stack, context, tooltip, type);
    tooltip.add(TextBridge.translatable("item.mishanguc.fast_building_tool.tooltip.1")
        .formatted(Formatting.GRAY));
    tooltip.add(TextBridge.translatable("item.mishanguc.fast_building_tool.tooltip.2").formatted(Formatting.GRAY));
    final FastBuildingToolData fastBuildingToolData = stack.get(MishangucComponents.FAST_BUILDING_TOOL_DATA);
    if (fastBuildingToolData != null) {
      tooltip.add(TextBridge.translatable("item.mishanguc.fast_building_tool.tooltip.range", TextBridge.literal(Integer.toString(fastBuildingToolData.range())).formatted(Formatting.YELLOW))
          .formatted(Formatting.GRAY));
      tooltip.add(TextBridge.translatable("item.mishanguc.fast_building_tool.tooltip.matchingRule", fastBuildingToolData.matchingRule().getName().formatted(Formatting.YELLOW))
          .formatted(Formatting.GRAY));
    }
  }

  protected ItemStack createStack(int range, BlockMatchingRule blockMatchingRule) {
    final ItemStack stack = getDefaultStack();
    stack.set(MishangucComponents.FAST_BUILDING_TOOL_DATA, new FastBuildingToolData(range, blockMatchingRule));
    return stack;
  }

  public void appendToEntries(ItemGroup.Entries stacks) {
    stacks.add(createStack(1, BlockMatchingRule.SAME_BLOCK));
    stacks.add(createStack(16, BlockMatchingRule.SAME_BLOCK));
    stacks.add(createStack(32, BlockMatchingRule.SAME_BLOCK));
    stacks.add(createStack(64, BlockMatchingRule.SAME_BLOCK));
  }

  @Override
  public Text getName(ItemStack stack) {
    final FastBuildingToolData data = stack.getOrDefault(MishangucComponents.FAST_BUILDING_TOOL_DATA, FastBuildingToolData.DEFAULT);
    return TextBridge.translatable("item.mishanguc.fast_building_tool.format", getName(), Integer.toString(data.range()), data.matchingRule().getName());
  }

  @Environment(EnvType.CLIENT)
  @Override
  public boolean renderBlockOutline(
      PlayerEntity player,
      ItemStack itemStack,
      WorldRenderContext worldRenderContext,
      WorldRenderContext.BlockOutlineContext blockOutlineContext, Hand hand) {
    final MinecraftClient client = MinecraftClient.getInstance();
    if (!player.isCreative()) {
      // 只有在创造模式下，才会绘制边框。
      return true;
    } else if (hand == Hand.OFF_HAND && player.getMainHandStack().getItem() instanceof BlockItem) {
      // 当玩家副手持有物品，主手持有方块时，直接跳过，不绘制。
      return true;
    }
    final VertexConsumerProvider consumers = worldRenderContext.consumers();
    if (consumers == null) return true;
    final VertexConsumer vertexConsumer = consumers.getBuffer(RenderLayer.LINES);
    final boolean includesFluid = this.includesFluid(itemStack, player.isSneaking());
    final FastBuildingToolData data = itemStack.getOrDefault(MishangucComponents.FAST_BUILDING_TOOL_DATA, FastBuildingToolData.DEFAULT);
    final BlockMatchingRule matchingRule = data.matchingRule();
    final int range = data.range();
    final BlockHitResult raycast;
    if (client.crosshairTarget instanceof BlockHitResult blockHitResult && blockHitResult.getType() == HitResult.Type.BLOCK) {
      raycast = blockHitResult;
    } else {
      return true;
    }
    final ClientWorld world = worldRenderContext.world();
    final BlockPlacementContext blockPlacementContext = new BlockPlacementContext(world, blockOutlineContext.blockPos(), player, itemStack, raycast, includesFluid);
    for (BlockPos pos : matchingRule.getPlainValidBlockPoss(world, raycast.getBlockPos(), raycast.getSide(), range)) {
      final BlockState state = world.getBlockState(pos);
      final BlockPlacementContext offsetBlockPlacementContext = new BlockPlacementContext(blockPlacementContext, pos);
      if (offsetBlockPlacementContext.canPlace() && offsetBlockPlacementContext.canReplace()) {
        WorldRendererInvoker.drawCuboidShapeOutline(
            worldRenderContext.matrixStack(),
            vertexConsumer,
            offsetBlockPlacementContext.stateToPlace.getOutlineShape(world, pos, ShapeContext.of(player)),
            offsetBlockPlacementContext.posToPlace.getX() - blockOutlineContext.cameraX(),
            offsetBlockPlacementContext.posToPlace.getY() - blockOutlineContext.cameraY(),
            offsetBlockPlacementContext.posToPlace.getZ() - blockOutlineContext.cameraZ(),
            0,
            1,
            1,
            0.8f);
        if (includesFluid) {
          WorldRendererInvoker.drawCuboidShapeOutline(
              worldRenderContext.matrixStack(),
              vertexConsumer,
              offsetBlockPlacementContext.stateToPlace.getFluidState().getShape(world, pos),
              offsetBlockPlacementContext.posToPlace.getX() - blockOutlineContext.cameraX(),
              offsetBlockPlacementContext.posToPlace.getY() - blockOutlineContext.cameraY(),
              offsetBlockPlacementContext.posToPlace.getZ() - blockOutlineContext.cameraZ(),
              0,
              0.5f,
              1,
              0.5f);
        }
      }
      if (hand == Hand.MAIN_HAND && !(state.getBlock() instanceof OperatorBlock && !player.hasPermissionLevel(2))) {
        // 只有当主手持有此物品时，才绘制边框，且对非管理员玩家忽略管理员方块。
        WorldRendererInvoker.drawCuboidShapeOutline(
            worldRenderContext.matrixStack(),
            vertexConsumer,
            state.getOutlineShape(world, pos, ShapeContext.of(player)),
            pos.getX() - blockOutlineContext.cameraX(),
            pos.getY() - blockOutlineContext.cameraY(),
            pos.getZ() - blockOutlineContext.cameraZ(),
            1,
            0,
            0,
            0.8f);
        if (includesFluid) {
          WorldRendererInvoker.drawCuboidShapeOutline(
              worldRenderContext.matrixStack(),
              vertexConsumer,
              state.getFluidState().getShape(world, pos),
              pos.getX() - blockOutlineContext.cameraX(),
              pos.getY() - blockOutlineContext.cameraY(),
              pos.getZ() - blockOutlineContext.cameraZ(),
              1,
              0.5f,
              0,
              0.5f);
        }
      }
    }
    return false;
  }

  @Override
  public void onScroll(int selectedSlot, double scrollAmount, ServerPlayerEntity player, ItemStack stack) {
    final FastBuildingToolData data = stack.getOrDefault(MishangucComponents.FAST_BUILDING_TOOL_DATA, FastBuildingToolData.DEFAULT);
    final BlockMatchingRule currentRule = data.matchingRule();
    final int i = RULES_TO_CYCLE.getRawId(currentRule);
    if (i == -1) return;
    final int j = (int) MathHelper.floorMod(i - scrollAmount, RULES_TO_CYCLE.size());
    final BlockMatchingRule newRule = RULES_TO_CYCLE.get(j);
    if (newRule != null) {
      stack.set(MishangucComponents.FAST_BUILDING_TOOL_DATA, new FastBuildingToolData(data.range(), newRule));
      final MutableText text = TextBridge.literal("[ ");
      for (Iterator<BlockMatchingRule> iterator = RULES_TO_CYCLE.iterator(); iterator.hasNext(); ) {
        BlockMatchingRule rule = iterator.next();
        final MutableText name = rule.getName();
        if (rule == newRule) name.formatted(Formatting.YELLOW, Formatting.UNDERLINE);
        text.append(name);
        if (iterator.hasNext()) text.append(" | ");
      }
      text.append(" ]");
      player.sendMessage(text, true);
    }
  }
}
