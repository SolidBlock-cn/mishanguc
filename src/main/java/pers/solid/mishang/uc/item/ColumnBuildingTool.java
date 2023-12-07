package pers.solid.mishang.uc.item;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayConnectionEvents;
import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderContext;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.data.client.Models;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Formatting;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockBox;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import org.apache.commons.lang3.tuple.Triple;
import org.jetbrains.annotations.Nullable;
import pers.solid.brrp.v1.generator.ItemResourceGenerator;
import pers.solid.brrp.v1.model.ModelJsonBuilder;
import pers.solid.mishang.uc.mixin.WorldRendererInvoker;
import pers.solid.mishang.uc.util.BlockPlacementContext;
import pers.solid.mishang.uc.util.TextBridge;

import java.util.List;
import java.util.WeakHashMap;

public class ColumnBuildingTool extends BlockToolItem implements HotbarScrollInteraction, ItemResourceGenerator {
  /**
   * 记录放置柱的操作记录。当玩家放置了柱之后，可以对其进行撤销，其操作记录就是存储在这个里面的。
   */
  private static final WeakHashMap<ServerPlayerEntity, Triple<ServerWorld, Block, BlockBox>> tempMemory = new WeakHashMap<>();
  private static @Nullable Triple<ClientWorld, Block, BlockBox> clientTempMemory = null;

  public static void registerTempMemoryEvents() {
    ServerPlayConnectionEvents.DISCONNECT.register(new Identifier("mishanguc", "remove_column_building_tool_memory"), (handler, server) -> tempMemory.remove(handler.player));
    if (FabricLoader.getInstance().getEnvironmentType() == EnvType.CLIENT) {
      ClientPlayConnectionEvents.DISCONNECT.register(new Identifier("mishanguc", "remove_column_building_tool_memory"), (handler, client) -> clientTempMemory = null);
    }
  }

  public ColumnBuildingTool(Settings settings, @Nullable Boolean includesFluid) {
    super(settings, includesFluid);
  }

  @Override
  public Text getName(ItemStack stack) {
    return TextBridge.translatable("item.mishanguc.column_building_tool.format", getName(), Integer.toString(getLength(stack)));
  }

  @Override
  public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
    super.appendTooltip(stack, world, tooltip, context);
    tooltip.add(TextBridge.translatable("item.mishanguc.column_building_tool.tooltip.1").formatted(Formatting.GRAY));
    tooltip.add(TextBridge.translatable("item.mishanguc.column_building_tool.tooltip.2").formatted(Formatting.GRAY));
    tooltip.add(TextBridge.translatable("item.mishanguc.column_building_tool.tooltip.3").formatted(Formatting.GRAY));
    tooltip.add(TextBridge.translatable("item.mishanguc.column_building_tool.tooltip.length", TextBridge.literal(Integer.toString(getLength(stack))).formatted(Formatting.YELLOW)).formatted(Formatting.GRAY));
  }

  @Override
  public ActionResult useOnBlock(ItemStack stack, PlayerEntity player, World world, BlockHitResult blockHitResult, Hand hand, boolean fluidIncluded) {
    if (!player.isCreative()) {
      // 仅限创造模式玩家使用。
      return ActionResult.PASS;
    }
    final Direction side = blockHitResult.getSide();
    final BlockPos originBlockPos = blockHitResult.getBlockPos();
    final BlockPlacementContext blockPlacementContext = new BlockPlacementContext(world, originBlockPos, player, stack, blockHitResult, fluidIncluded);
    final int length = this.getLength(stack);
    boolean soundPlayed = false;
    final BlockPos.Mutable posToPlace = new BlockPos.Mutable().set(blockPlacementContext.posToPlace);
    if (blockPlacementContext.canPlace()) {
      for (int i = 0; i < length; i++) {
        if (world.getBlockState(posToPlace).canReplace(blockPlacementContext.placementContext)) {
          if (!world.isClient) {
            world.setBlockState(posToPlace, blockPlacementContext.stateToPlace, 0b1011);
            BlockEntity entityToPlace = world.getBlockEntity(posToPlace);
            if (blockPlacementContext.stackInHand != null) {
              BlockItem.writeNbtToBlockEntity(world, player, posToPlace, blockPlacementContext.stackInHand);
            } else if (blockPlacementContext.hitEntity != null && entityToPlace != null) {
              entityToPlace.readNbt(blockPlacementContext.hitEntity.createNbt());
              entityToPlace.markDirty();
              world.updateListeners(posToPlace, entityToPlace.getCachedState(), entityToPlace.getCachedState(), Block.NOTIFY_ALL);
            }
          }
          if (!soundPlayed) blockPlacementContext.playSound();
          soundPlayed = true;
        } else {
          posToPlace.move(side, -1);
          break;
        }
        posToPlace.move(side);
      } // end for
    }
    if (soundPlayed) {
      if (!world.isClient) {
        tempMemory.put(((ServerPlayerEntity) player), Triple.of(((ServerWorld) world), blockPlacementContext.stateToPlace.getBlock(), BlockBox.create(blockPlacementContext.posToPlace, posToPlace.toImmutable())));
      } else if (FabricLoader.getInstance().getEnvironmentType() == EnvType.CLIENT) {
        clientTempMemory = Triple.of(((ClientWorld) world), blockPlacementContext.stateToPlace.getBlock(), BlockBox.create(blockPlacementContext.posToPlace, posToPlace.toImmutable()));
      }
    }
    return ActionResult.SUCCESS;
  }

  public int getLength(ItemStack stack) {
    final NbtCompound nbt = stack.getOrCreateNbt();
    return nbt.contains("Length", NbtElement.NUMBER_TYPE) ? MathHelper.clamp(1, nbt.getInt("Length"), 64) : 8;
  }

  @Override
  public ActionResult beginAttackBlock(ItemStack stack, PlayerEntity player, World world, Hand hand, BlockPos pos, Direction direction, boolean fluidIncluded) {
    @Nullable BlockBox lastPlacedBox = null;
    @Nullable Block lastPlacedBlock = null;

    // 检查是否存在上次记录的区域。如果有，且点击的方块在该区域内，则直接删除这个区域的方块。
    // 注意：只要点击了，即使点击的位置不在该区域内，也会清除有关的记录。
    if (!world.isClient) {
      final Triple<ServerWorld, Block, BlockBox> pair = tempMemory.get(((ServerPlayerEntity) player));
      if (pair != null && pair.getLeft().equals(world) && pair.getRight().contains(pos)) {
        lastPlacedBox = pair.getRight();
        lastPlacedBlock = pair.getMiddle();
      }
      tempMemory.remove(player);
    } else if (FabricLoader.getInstance().getEnvironmentType() == EnvType.CLIENT) {
      if (clientTempMemory != null && clientTempMemory.getLeft().equals(world) && clientTempMemory.getRight().contains(pos)) {
        lastPlacedBox = clientTempMemory.getRight();
        lastPlacedBlock = clientTempMemory.getMiddle();
      }
      clientTempMemory = null;
    }
    if (lastPlacedBox != null && lastPlacedBlock != null && !world.isClient) {
      for (BlockPos posToRemove : BlockPos.iterate(lastPlacedBox.getMinX(), lastPlacedBox.getMinY(), lastPlacedBox.getMinZ(), lastPlacedBox.getMaxX(), lastPlacedBox.getMaxY(), lastPlacedBox.getMaxZ())) {
        final BlockState existingState = world.getBlockState(posToRemove);
        if (lastPlacedBlock.equals(existingState.getBlock()) && !(existingState.getBlock() instanceof OperatorBlock && !player.hasPermissionLevel(2))) {
          // 非管理员不应该破坏管理方块。
          if (fluidIncluded) {
            world.setBlockState(posToRemove, Blocks.AIR.getDefaultState());
          } else {
            world.removeBlock(posToRemove, false);
          }
        }
      }
      return ActionResult.SUCCESS;
    }
    return ActionResult.PASS;
  }

  @Override
  public void onScroll(int selectedSlot, double scrollAmount, ServerPlayerEntity player, ItemStack stack) {
    final int length = MathHelper.clamp(getLength(stack) - (int) scrollAmount, 1, 64);
    stack.getOrCreateNbt().putInt("Length", length);
  }

  @Environment(EnvType.CLIENT)
  @Override
  public ModelJsonBuilder getItemModel() {
    return ItemResourceGenerator.super.getItemModel().parent(Models.HANDHELD);
  }

  @Environment(EnvType.CLIENT)
  @Override
  public boolean renderBlockOutline(PlayerEntity player, ItemStack itemStack, WorldRenderContext worldRenderContext, WorldRenderContext.BlockOutlineContext blockOutlineContext, Hand hand) {
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
    final int length = getLength(itemStack);
    final BlockHitResult raycast;
    if (client.crosshairTarget instanceof BlockHitResult blockHitResult && blockHitResult.getType() == HitResult.Type.BLOCK) {
      raycast = blockHitResult;
    } else {
      return true;
    }
    final ClientWorld world = worldRenderContext.world();
    final BlockPlacementContext blockPlacementContext = new BlockPlacementContext(world, blockOutlineContext.blockPos(), player, itemStack, raycast, includesFluid);

    // 绘制将要放置的方块。

    final Direction side = blockHitResult.getSide();
    final BlockPos.Mutable posToPlace = new BlockPos.Mutable().set(blockPlacementContext.posToPlace);
    if (blockPlacementContext.canPlace()) {
      for (int i = 0; i < length; i++) {
        if (world.getBlockState(posToPlace).canReplace(blockPlacementContext.placementContext)) {
          WorldRendererInvoker.drawCuboidShapeOutline(
              worldRenderContext.matrixStack(),
              vertexConsumer,
              blockPlacementContext.stateToPlace.getOutlineShape(world, posToPlace, ShapeContext.of(player)),
              posToPlace.getX() - blockOutlineContext.cameraX(),
              posToPlace.getY() - blockOutlineContext.cameraY(),
              posToPlace.getZ() - blockOutlineContext.cameraZ(),
              0,
              1,
              1,
              0.8f);
          if (includesFluid) {
            WorldRendererInvoker.drawCuboidShapeOutline(
                worldRenderContext.matrixStack(),
                vertexConsumer,
                blockPlacementContext.stateToPlace.getFluidState().getShape(world, posToPlace),
                posToPlace.getX() - blockOutlineContext.cameraX(),
                posToPlace.getY() - blockOutlineContext.cameraY(),
                posToPlace.getZ() - blockOutlineContext.cameraZ(),
                0,
                0.5f,
                1,
                0.5f);
          }
        } else {
          posToPlace.move(side, -1);
          break;
        }
        posToPlace.move(side);
      }
    }

    // 绘制上次移除过的方块。

    if (hand == Hand.MAIN_HAND && clientTempMemory != null && clientTempMemory.getLeft().equals(world) && clientTempMemory.getRight().contains(blockHitResult.getBlockPos())) {
      final BlockBox lastPlacedBox = clientTempMemory.getRight();
      final Block lastPlacedBlock = clientTempMemory.getMiddle();
      for (BlockPos posToRemove : BlockPos.iterate(lastPlacedBox.getMinX(), lastPlacedBox.getMinY(), lastPlacedBox.getMinZ(), lastPlacedBox.getMaxX(), lastPlacedBox.getMaxY(), lastPlacedBox.getMaxZ())) {
        final BlockState existingState = world.getBlockState(posToRemove);
        if (lastPlacedBlock.equals(existingState.getBlock()) && !(existingState.getBlock() instanceof OperatorBlock && !player.hasPermissionLevel(2))) {
          WorldRendererInvoker.drawCuboidShapeOutline(
              worldRenderContext.matrixStack(),
              vertexConsumer,
              existingState.getOutlineShape(world, posToRemove, ShapeContext.of(player)),
              posToRemove.getX() - blockOutlineContext.cameraX(),
              posToRemove.getY() - blockOutlineContext.cameraY(),
              posToRemove.getZ() - blockOutlineContext.cameraZ(),
              1,
              0,
              0,
              0.8f);
          if (includesFluid) {
            WorldRendererInvoker.drawCuboidShapeOutline(
                worldRenderContext.matrixStack(),
                vertexConsumer,
                existingState.getFluidState().getShape(world, posToRemove),
                posToRemove.getX() - blockOutlineContext.cameraX(),
                posToRemove.getY() - blockOutlineContext.cameraY(),
                posToRemove.getZ() - blockOutlineContext.cameraZ(),
                1,
                0.5f,
                0,
                0.5f);
          }
        }
      }
      // 绘制了红色之后，就不再绘制原版的边框。
      return false;
    }
    // 由于常规的破坏方便可能仍然有效，因此保留原先的边框绘制。
    return true;
  }
}
