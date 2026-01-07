package pers.solid.mishang.uc.item;

import it.unimi.dsi.fastutil.longs.LongObjectPair;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.rendering.v1.world.WorldExtractionContext;
import net.fabricmc.fabric.api.client.rendering.v1.world.WorldRenderContext;
import net.minecraft.block.*;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.render.state.OutlineRenderState;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.command.DefaultPermissions;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.item.tooltip.TooltipType;
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
          if (!world.isClient()) {
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
        if (world.getBlockState(pos1).getBlock() instanceof OperatorBlock && !player.getPermissions().hasPermission(DefaultPermissions.GAMEMASTERS)) {
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
  public void getMishangTooltip(ItemStack stack, TooltipContext context, List<Text> tooltip, TooltipType options) {
    tooltip.add(TextBridge.translatable("item.mishanguc.fast_building_tool.tooltip.1")
        .formatted(Formatting.GRAY));
    tooltip.add(TextBridge.translatable("item.mishanguc.fast_building_tool.tooltip.2").formatted(Formatting.GRAY));
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
  public @Nullable MishangRenderState getMishangRenderState(@Nullable MishangRenderState previous, ClientPlayerEntity player, Hand hand, ItemStack stack, WorldExtractionContext context, @Nullable HitResult result) {
    if (!player.isCreative()) {
      // 只有在创造模式下，才会绘制边框。
      return null;
    } else if (hand == Hand.OFF_HAND && player.getMainHandStack().getItem() instanceof BlockItem) {
      // 当玩家副手持有物品，主手持有方块时，直接跳过，不绘制。
      return null;
    }
    final boolean includesFluid = this.includesFluid(stack, player.isSneaking());
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
    final ClientWorld world = context.world();
    final BlockPlacementContext blockPlacementContext = new BlockPlacementContext(world, blockHitResult.getBlockPos(), player, stack, raycast, includesFluid);
    final ShapeContext shapeContext = ShapeContext.of(player);
    for (BlockPos pos : matchingRule.getPlainValidBlockPoss(world, raycast.getBlockPos(), raycast.getSide(), range)) {
      final BlockState state = world.getBlockState(pos);
      final BlockPlacementContext offsetBlockPlacementContext = new BlockPlacementContext(blockPlacementContext, pos);
      if (offsetBlockPlacementContext.canPlace() && offsetBlockPlacementContext.canReplace()) {
        buildingToolState.cyanShapes.add(LongObjectPair.of(offsetBlockPlacementContext.posToPlace.asLong(), offsetBlockPlacementContext.stateToPlace.getOutlineShape(world, offsetBlockPlacementContext.posToPlace, shapeContext)));
        if (includesFluid) {
          buildingToolState.blueShapes.add(LongObjectPair.of(offsetBlockPlacementContext.posToPlace.asLong(), offsetBlockPlacementContext.stateToPlace.getFluidState().getShape(world, offsetBlockPlacementContext.posToPlace)));
        }
      }
      if (hand == Hand.MAIN_HAND && !(state.getBlock() instanceof OperatorBlock && !player.getPermissions().hasPermission(DefaultPermissions.GAMEMASTERS))) {
        buildingToolState.redShapes.add(LongObjectPair.of(pos.asLong(), state.getOutlineShape(world, pos, shapeContext)));
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
      PlayerEntity player,
      ItemStack itemStack,
      WorldRenderContext context,
      OutlineRenderState outlineRenderState) {
    return BuildingToolState.render(context);
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
