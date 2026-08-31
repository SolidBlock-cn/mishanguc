package pers.solid.mishang.uc.item;

import it.unimi.dsi.fastutil.longs.LongObjectPair;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayConnectionEvents;
import net.fabricmc.fabric.api.client.rendering.v1.level.LevelExtractionContext;
import net.fabricmc.fabric.api.client.rendering.v1.level.LevelRenderContext;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.ChatFormatting;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.state.level.BlockOutlineRenderState;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.permissions.Permissions;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.TooltipDisplay;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.GameMasterBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import org.apache.commons.lang3.tuple.Triple;
import org.jetbrains.annotations.Nullable;
import pers.solid.mishang.uc.Mishanguc;
import pers.solid.mishang.uc.components.MishangucComponents;
import pers.solid.mishang.uc.render.state.BuildingToolState;
import pers.solid.mishang.uc.render.state.MishangRenderState;
import pers.solid.mishang.uc.util.BlockPlacementContext;
import pers.solid.mishang.uc.util.WithMishangTooltip;

import java.util.List;
import java.util.WeakHashMap;

public class ColumnBuildingTool extends BlockToolItem implements HotbarScrollInteraction, WithMishangTooltip {
  /**
   * 记录放置柱的操作记录。当玩家放置了柱之后，可以对其进行撤销，其操作记录就是存储在这个里面的。
   */
  private static final WeakHashMap<ServerPlayer, Triple<ServerLevel, Block, BoundingBox>> tempMemory = new WeakHashMap<>();
  private static @Nullable Triple<ClientLevel, Block, BoundingBox> clientTempMemory = null;

  public static void registerTempMemoryEvents() {
    ServerPlayConnectionEvents.DISCONNECT.register(Mishanguc.id("remove_column_building_tool_memory"), (handler, server) -> tempMemory.remove(handler.player));
    if (FabricLoader.getInstance().getEnvironmentType() == EnvType.CLIENT) {
      ClientPlayConnectionEvents.DISCONNECT.register(Mishanguc.id("remove_column_building_tool_memory"), (handler, client) -> clientTempMemory = null);
    }
  }

  public ColumnBuildingTool(Properties settings, @Nullable Boolean includesFluid) {
    super(settings.component(MishangucComponents.LENGTH, 8), includesFluid);
  }

  @Override
  public Component getName(ItemStack stack) {
    return Component.translatable("item.mishanguc.column_building_tool.format", super.getName(stack), Integer.toString(getLength(stack)));
  }

  @Override
  public void getMishangTooltip(ItemStack stack, TooltipContext context, List<Component> tooltip, TooltipFlag options) {
    tooltip.add(Component.translatable("item.mishanguc.column_building_tool.tooltip.1").withStyle(ChatFormatting.GRAY));
    tooltip.add(Component.translatable("item.mishanguc.column_building_tool.tooltip.2").withStyle(ChatFormatting.GRAY));
    tooltip.add(Component.translatable("item.mishanguc.column_building_tool.tooltip.3").withStyle(ChatFormatting.GRAY));
    if (stack.getOrDefault(DataComponents.TOOLTIP_DISPLAY, TooltipDisplay.DEFAULT).shows(MishangucComponents.LENGTH)) {
      tooltip.add(Component.translatable("item.mishanguc.column_building_tool.tooltip.length", Component.literal(Integer.toString(getLength(stack))).withStyle(ChatFormatting.YELLOW)).withStyle(ChatFormatting.GRAY));
    }
  }

  @Override
  public InteractionResult useOnBlock(ItemStack stack, Player player, Level world, BlockHitResult blockHitResult, InteractionHand hand, boolean fluidIncluded) {
    if (!player.isCreative()) {
      // 仅限创造模式玩家使用。
      return InteractionResult.PASS;
    }
    final Direction side = blockHitResult.getDirection();
    final BlockPos originBlockPos = blockHitResult.getBlockPos();
    final BlockPlacementContext blockPlacementContext = new BlockPlacementContext(world, originBlockPos, player, stack, blockHitResult, fluidIncluded);
    final int length = this.getLength(stack);
    boolean soundPlayed = false;
    final BlockPos.MutableBlockPos posToRely = new BlockPos.MutableBlockPos().set(originBlockPos);
    if (blockPlacementContext.canPlace()) {
      for (int i = 0; i < length; i++) {
        final BlockPlacementContext offsetBlockPlacementContext = new BlockPlacementContext(blockPlacementContext, posToRely);
        if (offsetBlockPlacementContext.canReplace()) {
          if (!world.isClientSide()) {
            offsetBlockPlacementContext.setBlockState(0b1011);
            offsetBlockPlacementContext.setBlockEntity();
          }
          if (!soundPlayed) blockPlacementContext.playSound();
          soundPlayed = true;
        } else {
          posToRely.move(side, -1);
          break;
        }
        posToRely.move(side);
      } // end for
    }
    if (soundPlayed) {
      if (!world.isClientSide()) {
        tempMemory.put(((ServerPlayer) player), Triple.of(((ServerLevel) world), blockPlacementContext.stateToPlace.getBlock(), BoundingBox.fromCorners(blockPlacementContext.posToPlace, posToRely.immutable())));
      } else if (FabricLoader.getInstance().getEnvironmentType() == EnvType.CLIENT) {
        clientTempMemory = Triple.of(((ClientLevel) world), blockPlacementContext.stateToPlace.getBlock(), BoundingBox.fromCorners(blockPlacementContext.posToPlace, posToRely.immutable()));
      }
    }
    return InteractionResult.SUCCESS;
  }

  public int getLength(ItemStack stack) {
    return stack.getOrDefault(MishangucComponents.LENGTH, 8);
  }

  @Override
  public InteractionResult beginAttackBlock(ItemStack stack, Player player, Level world, InteractionHand hand, BlockPos pos, Direction direction, boolean fluidIncluded) {
    @Nullable BoundingBox lastPlacedBox = null;
    @Nullable Block lastPlacedBlock = null;

    // 检查是否存在上次记录的区域。如果有，且点击的方块在该区域内，则直接删除这个区域的方块。
    // 注意：只要点击了，即使点击的位置不在该区域内，也会清除有关的记录。
    if (!world.isClientSide()) {
      final Triple<ServerLevel, Block, BoundingBox> pair = tempMemory.get(((ServerPlayer) player));
      if (pair != null && pair.getLeft().equals(world) && pair.getRight().isInside(pos)) {
        lastPlacedBox = pair.getRight();
        lastPlacedBlock = pair.getMiddle();
      }
      tempMemory.remove(player);
    } else if (FabricLoader.getInstance().getEnvironmentType() == EnvType.CLIENT) {
      if (clientTempMemory != null && clientTempMemory.getLeft().equals(world) && clientTempMemory.getRight().isInside(pos)) {
        lastPlacedBox = clientTempMemory.getRight();
        lastPlacedBlock = clientTempMemory.getMiddle();
      }
      clientTempMemory = null;
    }
    if (lastPlacedBox != null && lastPlacedBlock != null && !world.isClientSide()) {
      for (BlockPos posToRemove : BlockPos.betweenClosed(lastPlacedBox.minX(), lastPlacedBox.minY(), lastPlacedBox.minZ(), lastPlacedBox.maxX(), lastPlacedBox.maxY(), lastPlacedBox.maxZ())) {
        final BlockState existingState = world.getBlockState(posToRemove);
        if (lastPlacedBlock.equals(existingState.getBlock()) && !(existingState.getBlock() instanceof GameMasterBlock && !player.permissions().hasPermission(Permissions.COMMANDS_GAMEMASTER))) {
          // 非管理员不应该破坏管理方块。
          if (fluidIncluded) {
            world.setBlockAndUpdate(posToRemove, Blocks.AIR.defaultBlockState());
          } else {
            world.removeBlock(posToRemove, false);
          }
        }
      }
      return InteractionResult.SUCCESS;
    }
    return InteractionResult.PASS;
  }

  @Override
  public void onScroll(int selectedSlot, double scrollAmount, ServerPlayer player, ItemStack stack) {
    final int length = Mth.clamp(getLength(stack) - (int) scrollAmount, 1, 64);
    stack.set(MishangucComponents.LENGTH, length);
  }

  @Environment(EnvType.CLIENT)
  @Override
  public @Nullable MishangRenderState getMishangRenderState(LocalPlayer player, InteractionHand hand, ItemStack stack, LevelExtractionContext context, @Nullable HitResult result) {
    if (!player.isCreative()) {
      // 只有在创造模式下，才会绘制边框。
      return null;
    } else if (hand == InteractionHand.OFF_HAND && player.getMainHandItem().getItem() instanceof BlockItem) {
      // 当玩家副手持有物品，主手持有方块时，直接跳过，不绘制。
      return null;
    }
    final boolean includesFluid = this.includesFluid(stack, player.isShiftKeyDown());
    final int length = getLength(stack);
    final BlockHitResult raycast;
    if (result instanceof BlockHitResult blockHitResult && blockHitResult.getType() == HitResult.Type.BLOCK) {
      raycast = blockHitResult;
    } else {
      return null;
    }
    final BuildingToolState buildingToolState = new BuildingToolState();
    final ClientLevel world = context.level();
    final BlockPlacementContext blockPlacementContext = new BlockPlacementContext(world, blockHitResult.getBlockPos(), player, stack, raycast, includesFluid);

    // 绘制将要放置的方块。

    final Direction side = blockHitResult.getDirection();
    final BlockPos.MutableBlockPos posToPlace = new BlockPos.MutableBlockPos().set(blockPlacementContext.posToPlace);
    final CollisionContext shapeContext = CollisionContext.of(player);
    if (blockPlacementContext.canPlace()) {
      for (int i = 0; i < length; i++) {
        if (world.getBlockState(posToPlace).canBeReplaced(blockPlacementContext.placementContext)) {
          buildingToolState.cyanShapes.add(LongObjectPair.of(posToPlace.asLong(), blockPlacementContext.stateToPlace.getShape(world, posToPlace, shapeContext)));
          if (includesFluid) {
            buildingToolState.blueShapes.add(LongObjectPair.of(posToPlace.asLong(), blockPlacementContext.stateToPlace.getFluidState().getShape(world, posToPlace)));
          }
        } else {
          posToPlace.move(side, -1);
          break;
        }
        posToPlace.move(side);
      }
    }

    // 绘制上次移除过的方块。

    if (hand == InteractionHand.MAIN_HAND && clientTempMemory != null && clientTempMemory.getLeft().equals(world) && clientTempMemory.getRight().isInside(blockHitResult.getBlockPos())) {
      final BoundingBox lastPlacedBox = clientTempMemory.getRight();
      final Block lastPlacedBlock = clientTempMemory.getMiddle();
      for (BlockPos posToRemove : BlockPos.betweenClosed(lastPlacedBox.minX(), lastPlacedBox.minY(), lastPlacedBox.minZ(), lastPlacedBox.maxX(), lastPlacedBox.maxY(), lastPlacedBox.maxZ())) {
        final BlockState existingState = world.getBlockState(posToRemove);
        if (lastPlacedBlock.equals(existingState.getBlock()) && !(existingState.getBlock() instanceof GameMasterBlock && !player.permissions().hasPermission(Permissions.COMMANDS_GAMEMASTER))) {
          buildingToolState.redShapes.add(LongObjectPair.of(posToRemove.asLong(), existingState.getShape(world, posToRemove, shapeContext)));
          if (includesFluid) {
            buildingToolState.orangeShapes.add(LongObjectPair.of(posToRemove.asLong(), existingState.getFluidState().getShape(world, posToRemove)));
          }
        }
      }
      // 绘制了红色之后，就不再绘制原版的边框。
      buildingToolState.showVanillaOutline = false;
    } else {
      buildingToolState.showVanillaOutline = true;
    }
    return buildingToolState;
  }

  @Environment(EnvType.CLIENT)
  @Override
  public boolean renderBlockOutline(Player player, ItemStack itemStack, LevelRenderContext context, BlockOutlineRenderState outlineRenderState) {
    return BuildingToolState.render(context);
  }
}
