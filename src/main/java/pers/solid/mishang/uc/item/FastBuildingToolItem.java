package pers.solid.mishang.uc.item;

import it.unimi.dsi.fastutil.longs.LongObjectPair;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.rendering.v1.world.WorldExtractionContext;
import net.fabricmc.fabric.api.client.rendering.v1.world.WorldRenderContext;
import net.minecraft.ChatFormatting;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.state.BlockOutlineRenderState;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.permissions.Permissions;
import net.minecraft.util.CrudeIncrementalIntIdentityHashBiMap;
import net.minecraft.util.Mth;
import net.minecraft.util.Util;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.GameMasterBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import org.jetbrains.annotations.Nullable;
import pers.solid.mishang.uc.components.FastBuildingToolData;
import pers.solid.mishang.uc.components.MishangucComponents;
import pers.solid.mishang.uc.render.state.BuildingToolState;
import pers.solid.mishang.uc.render.state.MishangRenderState;
import pers.solid.mishang.uc.util.BlockMatchingRule;
import pers.solid.mishang.uc.util.BlockPlacementContext;
import pers.solid.mishang.uc.util.TextBridge;
import pers.solid.mishang.uc.util.WithMishangTooltip;

import java.util.Iterator;
import java.util.List;

/**
 * 该物品可以快速建造或者删除一个平面上的多个方块。
 *
 * @see BlockMatchingRule
 */
public class FastBuildingToolItem extends BlockToolItem implements HotbarScrollInteraction, WithMishangTooltip {

  private static final CrudeIncrementalIntIdentityHashBiMap<BlockMatchingRule> RULES_TO_CYCLE = Util.make(CrudeIncrementalIntIdentityHashBiMap.create(4), map -> {
    map.add(BlockMatchingRule.SAME_STATE);
    map.add(BlockMatchingRule.SAME_BLOCK);
    map.add(BlockMatchingRule.SAME_MATERIAL);
    map.add(BlockMatchingRule.ANY);
  });

  public FastBuildingToolItem(Properties settings, @Nullable Boolean includesFluid) {
    super(settings.component(MishangucComponents.FAST_BUILDING_TOOL_DATA, FastBuildingToolData.DEFAULT), includesFluid);
  }

  @Override
  public InteractionResult useOnBlock(
      ItemStack stack, Player player,
      Level world,
      BlockHitResult blockHitResult,
      InteractionHand hand,
      boolean fluidIncluded) {
    if (!player.isCreative()) {
      // 仅限创造模式玩家使用。
      return InteractionResult.PASS;
    }
    final Direction side = blockHitResult.getDirection();
    final BlockPos centerBlockPos = blockHitResult.getBlockPos();
    final BlockState centerState = world.getBlockState(centerBlockPos);
    final BlockPlacementContext blockPlacementContext = new BlockPlacementContext(world, centerBlockPos, player, stack, blockHitResult, fluidIncluded);
    final FastBuildingToolData data = stack.getOrDefault(MishangucComponents.FAST_BUILDING_TOOL_DATA, FastBuildingToolData.DEFAULT);
    final int range = data.range();
    final BlockMatchingRule matchingRule = data.matchingRule();
    boolean soundPlayed = false;
    for (BlockPos pos : matchingRule.getPlainValidBlockPoss(world, centerBlockPos, side, range)) {
      BlockState state = world.getBlockState(pos);
      if (matchingRule.match(centerState, state)) {
        final BlockPlacementContext offsetBlockPlacementContext = new BlockPlacementContext(blockPlacementContext, pos);
        if (offsetBlockPlacementContext.canPlace() && offsetBlockPlacementContext.canReplace()) {
          if (!world.isClientSide()) {
            offsetBlockPlacementContext.setBlockState(0b1011);
            offsetBlockPlacementContext.setBlockEntity();
          }
          if (!soundPlayed) offsetBlockPlacementContext.playSound();
          soundPlayed = true;
        }
      }
    } // end for
    return InteractionResult.SUCCESS;
  }

  @Override
  public InteractionResult beginAttackBlock(
      ItemStack stack, Player player, Level world, InteractionHand hand, BlockPos pos, Direction direction, boolean fluidIncluded) {
    if (!player.isCreative()) {
      // 仅限创造模式玩家使用。
      return InteractionResult.PASS;
    }
    if (!world.isClientSide()) {
      final FastBuildingToolData data = stack.getOrDefault(MishangucComponents.FAST_BUILDING_TOOL_DATA, FastBuildingToolData.DEFAULT);
      final int range = data.range();
      final BlockMatchingRule matchingRule = data.matchingRule();
      for (BlockPos pos1 : matchingRule.getPlainValidBlockPoss(world, pos, direction, range)) {
        if (world.getBlockState(pos1).getBlock() instanceof GameMasterBlock && !player.permissions().hasPermission(Permissions.COMMANDS_GAMEMASTER)) {
          // 非管理员不应该破坏管理方块。
        } else if (fluidIncluded) {
          world.setBlockAndUpdate(pos1, Blocks.AIR.defaultBlockState());
        } else {
          world.removeBlock(pos1, false);
        }
      }
    }
    world.levelEvent(player, 2001, pos, Block.getId(world.getBlockState(pos)));
    return InteractionResult.SUCCESS;
  }

  @Override
  public void getMishangTooltip(ItemStack stack, TooltipContext context, List<Component> tooltip, TooltipFlag options) {
    tooltip.add(TextBridge.translatable("item.mishanguc.fast_building_tool.tooltip.1")
        .withStyle(ChatFormatting.GRAY));
    tooltip.add(TextBridge.translatable("item.mishanguc.fast_building_tool.tooltip.2").withStyle(ChatFormatting.GRAY));
  }

  protected ItemStack createStack(int range, BlockMatchingRule blockMatchingRule) {
    final ItemStack stack = getDefaultInstance();
    stack.set(MishangucComponents.FAST_BUILDING_TOOL_DATA, new FastBuildingToolData(range, blockMatchingRule));
    return stack;
  }

  public void appendToEntries(CreativeModeTab.Output stacks) {
    stacks.accept(createStack(1, BlockMatchingRule.SAME_BLOCK));
    stacks.accept(createStack(16, BlockMatchingRule.SAME_BLOCK));
    stacks.accept(createStack(32, BlockMatchingRule.SAME_BLOCK));
    stacks.accept(createStack(64, BlockMatchingRule.SAME_BLOCK));
  }

  @Override
  public Component getName(ItemStack stack) {
    final FastBuildingToolData data = stack.getOrDefault(MishangucComponents.FAST_BUILDING_TOOL_DATA, FastBuildingToolData.DEFAULT);
    return TextBridge.translatable("item.mishanguc.fast_building_tool.format", getName(), Integer.toString(data.range()), data.matchingRule().getName());
  }


  @Environment(EnvType.CLIENT)
  @Override
  public @Nullable MishangRenderState getMishangRenderState(LocalPlayer player, InteractionHand hand, ItemStack stack, WorldExtractionContext context, @Nullable HitResult result) {
    if (!player.isCreative()) {
      // 只有在创造模式下，才会绘制边框。
      return null;
    } else if (hand == InteractionHand.OFF_HAND && player.getMainHandItem().getItem() instanceof BlockItem) {
      // 当玩家副手持有物品，主手持有方块时，直接跳过，不绘制。
      return null;
    }
    final boolean includesFluid = this.includesFluid(stack, player.isShiftKeyDown());
    final FastBuildingToolData data = stack.getOrDefault(MishangucComponents.FAST_BUILDING_TOOL_DATA, FastBuildingToolData.DEFAULT);
    final BlockMatchingRule matchingRule = data.matchingRule();
    final int range = data.range();
    final BlockHitResult raycast;
    if (result instanceof BlockHitResult blockHitResult && blockHitResult.getType() == HitResult.Type.BLOCK) {
      raycast = blockHitResult;
    } else {
      return null;
    }
    final BuildingToolState buildingToolState = new BuildingToolState();
    final ClientLevel world = context.world();
    final BlockPlacementContext blockPlacementContext = new BlockPlacementContext(world, blockHitResult.getBlockPos(), player, stack, raycast, includesFluid);
    final CollisionContext shapeContext = CollisionContext.of(player);
    for (BlockPos pos : matchingRule.getPlainValidBlockPoss(world, raycast.getBlockPos(), raycast.getDirection(), range)) {
      final BlockState state = world.getBlockState(pos);
      final BlockPlacementContext offsetBlockPlacementContext = new BlockPlacementContext(blockPlacementContext, pos);
      if (offsetBlockPlacementContext.canPlace() && offsetBlockPlacementContext.canReplace()) {
        buildingToolState.cyanShapes.add(LongObjectPair.of(offsetBlockPlacementContext.posToPlace.asLong(), offsetBlockPlacementContext.stateToPlace.getShape(world, offsetBlockPlacementContext.posToPlace, shapeContext)));
        if (includesFluid) {
          buildingToolState.blueShapes.add(LongObjectPair.of(offsetBlockPlacementContext.posToPlace.asLong(), offsetBlockPlacementContext.stateToPlace.getFluidState().getShape(world, offsetBlockPlacementContext.posToPlace)));
        }
      }
      if (hand == InteractionHand.MAIN_HAND && !(state.getBlock() instanceof GameMasterBlock && !player.permissions().hasPermission(Permissions.COMMANDS_GAMEMASTER))) {
        buildingToolState.redShapes.add(LongObjectPair.of(pos.asLong(), state.getShape(world, pos, shapeContext)));
        if (includesFluid) {
          buildingToolState.orangeShapes.add(LongObjectPair.of(pos.asLong(), state.getFluidState().getShape(world, pos)));
        }
      }
    }

    return buildingToolState;
  }

  @Environment(EnvType.CLIENT)
  @Override
  public boolean renderBlockOutline(
      Player player,
      ItemStack itemStack,
      WorldRenderContext context,
      BlockOutlineRenderState outlineRenderState) {
    return BuildingToolState.render(context);
  }

  @Override
  public void onScroll(int selectedSlot, double scrollAmount, ServerPlayer player, ItemStack stack) {
    final FastBuildingToolData data = stack.getOrDefault(MishangucComponents.FAST_BUILDING_TOOL_DATA, FastBuildingToolData.DEFAULT);
    final BlockMatchingRule currentRule = data.matchingRule();
    final int i = RULES_TO_CYCLE.getId(currentRule);
    if (i == -1) return;
    final int j = (int) Mth.positiveModulo(i - scrollAmount, RULES_TO_CYCLE.size());
    final BlockMatchingRule newRule = RULES_TO_CYCLE.byId(j);
    if (newRule != null) {
      stack.set(MishangucComponents.FAST_BUILDING_TOOL_DATA, new FastBuildingToolData(data.range(), newRule));
      final MutableComponent text = TextBridge.literal("[ ");
      for (Iterator<BlockMatchingRule> iterator = RULES_TO_CYCLE.iterator(); iterator.hasNext(); ) {
        BlockMatchingRule rule = iterator.next();
        final MutableComponent name = rule.getName();
        if (rule == newRule) name.withStyle(ChatFormatting.YELLOW, ChatFormatting.UNDERLINE);
        text.append(name);
        if (iterator.hasNext()) text.append(" | ");
      }
      text.append(" ]");
      player.displayClientMessage(text, true);
    }
  }
}
