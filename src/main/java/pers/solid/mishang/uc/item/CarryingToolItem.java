package pers.solid.mishang.uc.item;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.api.EnvironmentInterface;
import net.fabricmc.fabric.api.client.rendering.v1.world.WorldExtractionContext;
import net.fabricmc.fabric.api.client.rendering.v1.world.WorldRenderContext;
import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexRendering;
import net.minecraft.client.render.state.OutlineRenderState;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.*;
import net.minecraft.entity.boss.dragon.EnderDragonPart;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.tooltip.TooltipType;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.storage.NbtWriteView;
import net.minecraft.text.Text;
import net.minecraft.util.*;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.*;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.World;
import net.minecraft.world.WorldView;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import pers.solid.mishang.uc.MishangucClient;
import pers.solid.mishang.uc.MishangucRules;
import pers.solid.mishang.uc.components.CarryingToolData;
import pers.solid.mishang.uc.components.MishangucComponents;
import pers.solid.mishang.uc.render.RendersBeforeOutline;
import pers.solid.mishang.uc.render.state.CarryingToolState;
import pers.solid.mishang.uc.render.state.MishangRenderState;
import pers.solid.mishang.uc.util.BlockPlacementContext;
import pers.solid.mishang.uc.util.TextBridge;
import pers.solid.mishang.uc.util.WithMishangTooltip;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@EnvironmentInterface(value = EnvType.CLIENT, itf = RendersBeforeOutline.class)
public class CarryingToolItem extends BlockToolItem
    implements MishangucItem, InteractsWithEntity, RendersBeforeOutline, WithMishangTooltip {

  private static final int OUTLINE_COLOR_CYAN = ColorHelper.fromFloats(0.8f, 0, 1, 1);
  private static final int OUTLINE_COLOR_AO = ColorHelper.fromFloats(0.5f, 0, 0.5f, 1);
  private static final int OUTLINE_COLOR_AKA = ColorHelper.fromFloats(0.8f, 1, 0, 0);
  private static final int OUTLINE_COLOR_ORANGE = ColorHelper.fromFloats(0.5f, 1, 0.5f, 0);

  public CarryingToolItem(Settings settings, @Nullable Boolean includesFluid) {
    super(settings, includesFluid);
  }

  @Contract(pure = true)
  public static @Nullable BlockState getHoldingBlockState(@NotNull ItemStack stack, WorldView world) {
    final CarryingToolData carryingToolData = stack.get(MishangucComponents.CARRYING_TOOL_DATA);
    return carryingToolData instanceof CarryingToolData.HoldingBlockState holdingBlockState ? holdingBlockState.state() : null;
  }

  /**
   * 避免重复召唤实体时，因为 UUID 雷同而无法召唤。
   */
  @Contract(mutates = "param1")
  private static void setHoldingEntityUUID(ItemStack stack, UUID uuid) {
    final CarryingToolData carryingToolData = stack.get(MishangucComponents.CARRYING_TOOL_DATA);
    final NbtCompound entityTag = carryingToolData instanceof CarryingToolData.HoldingEntity holdingEntity ? holdingEntity.entityTag().orElse(null) : null;
    if (entityTag != null) {
      entityTag.put("UUID", Uuids.INT_STREAM_CODEC, uuid);
    }
  }

  @Contract(pure = true)
  public static @Nullable Entity createHoldingEntity(@NotNull CarryingToolData.HoldingEntity data, ServerWorld world, PlayerEntity player) {
    final EntityType<?> entityType = data.entityType();
    return entityType.create(world, entity -> data.entityTag().ifPresent(nbtCompound -> TypedEntityData.create(entityType, nbtCompound).applyToEntity(entity)), player.getBlockPos(), SpawnReason.EVENT, false, false);
  }

  private static Text getEntityName(@NotNull ItemStack stack) {
    final CarryingToolData carryingToolData = stack.get(MishangucComponents.CARRYING_TOOL_DATA);
    if (carryingToolData instanceof CarryingToolData.HoldingEntity holdingEntity) {
      return holdingEntity.name();
    } else {
      return TextBridge.empty();
    }
  }

  @Override
  public Text getName(ItemStack stack) {
    final Text name = super.getName(stack);
    final CarryingToolData carryingToolData = stack.get(MishangucComponents.CARRYING_TOOL_DATA);
    if (carryingToolData instanceof CarryingToolData.HoldingEntity holdingEntity) {
      return TextBridge.translatable("item.mishanguc.carrying_tool.holding", name, holdingEntity.name());
    } else if (carryingToolData instanceof CarryingToolData.HoldingBlockState holdingBlockState) {
      return TextBridge.translatable("item.mishanguc.carrying_tool.holding", name, holdingBlockState.state().getBlock().getName());
    } else {
      return TextBridge.translatable("item.mishanguc.carrying_tool.empty", name);
    }
  }

  @Override
  public void getMishangTooltip(ItemStack stack, TooltipContext context, List<Text> tooltip, TooltipType options) {
    tooltip.add(TextBridge.translatable("item.mishanguc.carrying_tool.tooltip.1").formatted(Formatting.GRAY));
    tooltip.add(TextBridge.translatable("item.mishanguc.carrying_tool.tooltip.2").formatted(Formatting.GRAY));
    tooltip.add(TextBridge.translatable("item.mishanguc.carrying_tool.tooltip.3").formatted(Formatting.GRAY));
  }


  @Override
  public ActionResult useOnBlock(ItemStack stack, PlayerEntity player, World world, BlockHitResult blockHitResult, Hand hand, boolean fluidIncluded) {
    if (!hasAccess(player, world, true)) {
      return ActionResult.PASS;
    }
    final CarryingToolData carryingToolData = stack.get(MishangucComponents.CARRYING_TOOL_DATA);
    if (carryingToolData instanceof CarryingToolData.HoldingBlockState) {
      final BlockPlacementContext blockPlacementContext = new BlockPlacementContext(world, blockHitResult.getBlockPos(), player, stack, blockHitResult, fluidIncluded);
      if (blockPlacementContext.canPlace()) {
        blockPlacementContext.setBlockState(3);
        blockPlacementContext.setBlockEntity();
        if (world.isClient()) {
          blockPlacementContext.playSound();
        } else {
          player.sendMessage(TextBridge.translatable(player.isCreative() ? "item.mishanguc.carrying_tool.message.placed_creative" : "item.mishanguc.carrying_tool.message.placed", blockPlacementContext.stateToPlace.getBlock().getName()), true);
        }
        if (!player.isCreative()) {
          stack.remove(MishangucComponents.CARRYING_TOOL_DATA);
        }
        return ActionResult.SUCCESS;
      } else {
        return ActionResult.PASS;
      }
    } else if (carryingToolData instanceof CarryingToolData.HoldingEntity holdingEntity) {
      if (world instanceof ServerWorld serverWorld) {
        final Entity entity = createHoldingEntity(holdingEntity, serverWorld, player);
        if (entity == null)
          return ActionResult.PASS;
        final Vec3d pos = blockHitResult.getPos();
        entity.updatePosition(pos.x, pos.y, pos.z);
        final boolean spawnEntity = world.spawnEntity(entity);
        if (spawnEntity) {
          player.sendMessage(TextBridge.translatable(player.isCreative() ? "item.mishanguc.carrying_tool.message.spawned_creative" : "item.mishanguc.carrying_tool.message.spawned", getEntityName(stack)), true);
          if (!player.isCreative()) {
            stack.remove(MishangucComponents.CARRYING_TOOL_DATA);
          } else {
            setHoldingEntityUUID(stack, MathHelper.randomUuid());
          }
          return ActionResult.SUCCESS;
        } else {
          return ActionResult.FAIL;
        }
      } else {
        // 客户端部分。
        return ActionResult.SUCCESS;
      }
    } else {
      final BlockState blockState = world.getBlockState(blockHitResult.getBlockPos());
      final ActionResult actionResult = blockState.onUse(world, player, blockHitResult);
      if (actionResult.isAccepted()) {
        return actionResult;
      } else {
        if (world.isClient()) {
          return ActionResult.PASS;
        } else {
          player.sendMessage(TextBridge.translatable("item.mishanguc.carrying_tool.message.no_placing").formatted(Formatting.RED), true);
          return ActionResult.FAIL;
        }
      }
    }
  }


  private boolean hasAccess(PlayerEntity player, World world, boolean warn) {
    if (!(world instanceof ServerWorld serverWorld)) {
      return MishangucClient.CLIENT_CARRYING_TOOL_ACCESS.get().hasAccess(player);
    } else {
      final MishangucRules.ToolAccess toolAccess = serverWorld.getGameRules().get(MishangucRules.CARRYING_TOOL_ACCESS).get();
      return toolAccess.hasAccess(player, warn);
    }
  }

  @Override
  public ActionResult beginAttackBlock(ItemStack stack, PlayerEntity player, World world, Hand hand, BlockPos pos, Direction direction, boolean fluidIncluded) {
    if (!hasAccess(player, world, true))
      return ActionResult.PASS;
    final CarryingToolData carryingToolData = stack.get(MishangucComponents.CARRYING_TOOL_DATA);
    if (carryingToolData instanceof CarryingToolData.HoldingBlockState holdingBlockState && !player.isCreative()) {
      if (!world.isClient()) {
        player.sendMessage(TextBridge.translatable("item.mishanguc.carrying_tool.message.no_picking", Optional.of(holdingBlockState.state().getBlock()).map(Block::getName).orElse(TextBridge.empty())).formatted(Formatting.RED), true);
        return ActionResult.FAIL;
      } else {
        return ActionResult.CONSUME;
      }
    }
    if (carryingToolData instanceof CarryingToolData.HoldingEntity holdingEntity && !player.isCreative()) {
      if (world.isClient())
        return ActionResult.CONSUME;
      else {
        player.sendMessage(TextBridge.translatable("item.mishanguc.carrying_tool.message.no_picking", holdingEntity.name()).formatted(Formatting.RED), true);
        return ActionResult.FAIL;
      }
    }
    final BlockState removed = world.getBlockState(pos);
    if (removed.getBlock() instanceof OperatorBlock && !player.hasPermissionLevel(2)) {
      return ActionResult.FAIL;
    }
    final BlockEntity blockEntity = world.getBlockEntity(pos);
    if (blockEntity != null) {
      stack.set(MishangucComponents.CARRYING_TOOL_DATA, new CarryingToolData.HoldingBlockState(removed, Optional.of(blockEntity.createComponentlessNbt(world.getRegistryManager()))));
    } else {
      stack.set(MishangucComponents.CARRYING_TOOL_DATA, new CarryingToolData.HoldingBlockState(removed, Optional.empty()));
    }
    world.removeBlockEntity(pos);
    world.setBlockState(pos, Blocks.AIR.getDefaultState());
    if (world.isClient()) {
      world.syncWorldEvent(2001, pos, Block.getRawIdFromState(removed));
    }
    if (!world.isClient()) {
      if (carryingToolData instanceof CarryingToolData.HoldingEntity holdingEntity) {
        player.sendMessage(TextBridge.translatable("item.mishanguc.carrying_tool.message.picked_overriding", holdingEntity.name()), true);
      } else if (carryingToolData instanceof CarryingToolData.HoldingBlockState holdingBlockState) {
        player.sendMessage(TextBridge.translatable("item.mishanguc.carrying_tool.message.picked_overriding", removed.getBlock().getName(), holdingBlockState.state().getBlock().getName()), true);
      } else {
        player.sendMessage(TextBridge.translatable("item.mishanguc.carrying_tool.message.pick", removed.getBlock().getName()), true);
      }
    }
    return ActionResult.SUCCESS;
  }


  @Override
  public ActionResult use(World world, PlayerEntity user, Hand hand) {
    final ActionResult use = super.use(world, user, hand);
    if (use.isAccepted() || !hasAccess(user, world, true)) {
      return use;
    }
    final ItemStack stack = user.getStackInHand(hand);
    final CarryingToolData carryingToolData = stack.get(MishangucComponents.CARRYING_TOOL_DATA);
    if (carryingToolData instanceof CarryingToolData.HoldingBlockState(BlockState state, Optional<NbtCompound> blockEntityTag)) {
      if (state.getBlock() instanceof OperatorBlock && !user.hasPermissionLevel(2)) {
        return ActionResult.FAIL;
      }
      if (world.isClient()) {
        return ActionResult.SUCCESS;
      }
      final FallingBlockEntity fallingBlockEntity = new FallingBlockEntity(EntityType.FALLING_BLOCK, world);
      NbtCompound nbt = new NbtCompound();
      nbt.put("BlockState", BlockState.CODEC, state);
      TypedEntityData.create(fallingBlockEntity.getType(), nbt).applyToEntity(fallingBlockEntity);
      final Vec3d eyePos = user.getEyePos();
      fallingBlockEntity.updatePositionAndAngles(eyePos.x, eyePos.y, eyePos.z, user.getYaw(), user.getPitch());
      fallingBlockEntity.setVelocity(Vec3d.fromPolar(user.getPitch(), user.getYaw()).multiply(2).add(user.getVelocity()));
      fallingBlockEntity.dropItem = true;
      fallingBlockEntity.blockEntityData = blockEntityTag.orElse(null);
      fallingBlockEntity.setHurtEntities(state.getBlock().getBlastResistance(), Integer.MAX_VALUE);
      final boolean spawnEntity = world.spawnEntity(fallingBlockEntity);
      if (spawnEntity) {
        if (!user.isCreative()) {
          stack.remove(MishangucComponents.CARRYING_TOOL_DATA);
        }
        user.sendMessage(TextBridge.translatable(user.isCreative() ? "item.mishanguc.carrying_tool.message.block_thrown_creative" : "item.mishanguc.carrying_tool.message.block_thrown", state.getBlock().getName()), true);
        return ActionResult.SUCCESS;
      } else {
        return ActionResult.FAIL;
      }
    } else if (carryingToolData instanceof CarryingToolData.HoldingEntity holdingEntity) {
      if (world instanceof ServerWorld serverWorld) {
        final Entity entity = createHoldingEntity(holdingEntity, serverWorld, user);
        if (entity == null)
          return use;
        final Vec3d pos = user.getEntityPos();
        entity.updatePositionAndAngles(pos.x, pos.y, pos.z, user.getYaw(), user.getPitch());
        entity.setVelocity(Vec3d.fromPolar(user.getPitch(), user.getYaw()).multiply(2).add(user.getVelocity()));
        final boolean spawnEntity = world.spawnEntity(entity);
        if (spawnEntity) {
          user.sendMessage(TextBridge.translatable(user.isCreative() ? "item.mishanguc.carrying_tool.message.entity_thrown_creative" : "item.mishanguc.carrying_tool.message.entity_thrown", getEntityName(stack)), true);
          if (!user.isCreative()) {
            stack.remove(MishangucComponents.CARRYING_TOOL_DATA);
          } else {
            setHoldingEntityUUID(stack, MathHelper.randomUuid());
          }
          return ActionResult.SUCCESS;
        } else {
          return ActionResult.FAIL;
        }
      } else {
        return ActionResult.SUCCESS;
      }
    } else {
      return use;
    }
  }

  @Override
  public @NotNull ActionResult attackEntityCallback(PlayerEntity player, World world, Hand hand, Entity entity, @Nullable EntityHitResult hitResult) {
    if (!hasAccess(player, world, true) || player.isSpectator())
      return ActionResult.PASS;
    final ItemStack stack = player.getStackInHand(hand);
    final CarryingToolData carryingToolData = stack.get(MishangucComponents.CARRYING_TOOL_DATA);
    if (entity instanceof PlayerEntity) {
      if (world.isClient()) {
        return ActionResult.PASS;
      } else {
        player.sendMessage(TextBridge.translatable("item.mishanguc.carrying_tool.message.pick_player").formatted(Formatting.RED), false);
        return ActionResult.FAIL;
      }
    } else if (carryingToolData instanceof CarryingToolData.HoldingEntity holdingEntity && !player.isCreative()) {
      if (world.isClient())
        return ActionResult.SUCCESS;
      else {
        player.sendMessage(TextBridge.translatable("item.mishanguc.carrying_tool.message.no_picking", holdingEntity.name()).formatted(Formatting.RED), true);
        return ActionResult.FAIL;
      }
    } else if (carryingToolData instanceof CarryingToolData.HoldingBlockState holdingBlockState && !player.isCreative()) {
      if (world.isClient())
        return ActionResult.SUCCESS;
      else {
        player.sendMessage(TextBridge.translatable("item.mishanguc.carrying_tool.message.no_picking", Optional.ofNullable(holdingBlockState.state().getBlock()).map(Block::getName).orElse(TextBridge.empty())).formatted(Formatting.RED), true);
        return ActionResult.FAIL;
      }
    }
    if (world instanceof ServerWorld serverWorld) {
      if (carryingToolData instanceof CarryingToolData.HoldingEntity holdingEntity) {
        player.sendMessage(TextBridge.translatable("item.mishanguc.carrying_tool.message.pick_entity_overriding", entity.getName(), holdingEntity.name()), true);
      } else if (carryingToolData instanceof CarryingToolData.HoldingBlockState holdingBlockState) {
        player.sendMessage(TextBridge.translatable("item.mishanguc.carrying_tool.message.pick_entity_overriding", entity.getName(), holdingBlockState.state().getBlock().getName()), true);
      } else {
        player.sendMessage(TextBridge.translatable("item.mishanguc.carrying_tool.message.pick_entity", entity.getName()), true);
      }
      final NbtWriteView nbtWriteView = NbtWriteView.create(ErrorReporter.EMPTY, entity.getRegistryManager());
      entity.writeData(nbtWriteView);
      stack.set(MishangucComponents.CARRYING_TOOL_DATA, new CarryingToolData.HoldingEntity(entity.getType(), Optional.of(nbtWriteView.getNbt()), entity.getName(), entity.getWidth(), entity.getHeight()));
      entity.remove(Entity.RemovalReason.DISCARDED);
      if (entity instanceof EnderDragonPart enderDragonPart) {
        enderDragonPart.owner.kill(serverWorld);
      }
    }
    return ActionResult.SUCCESS;
  }

  @Environment(EnvType.CLIENT)
  @Override
  public @Nullable CarryingToolState getMishangRenderState(@Nullable MishangRenderState previous, ClientPlayerEntity player, Hand hand, ItemStack stack, WorldExtractionContext context, @Nullable HitResult result) {
    final CarryingToolState state = previous instanceof CarryingToolState carryingToolState ? carryingToolState : new CarryingToolState();
    state.clear();

    final ClientWorld world = context.world();
    if (!hasAccess(player, world, true)) {
      return state;
    }

    final CarryingToolData carryingToolData = stack.get(MishangucComponents.CARRYING_TOOL_DATA);

    if (result instanceof BlockHitResult blockHitResult) {
      final boolean includesFluid = this.includesFluid(stack, player.isSneaking());
      final BlockPos pos = blockHitResult.getBlockPos();

      if (carryingToolData instanceof CarryingToolData.HoldingBlockState) {
        final BlockPlacementContext blockPlacementContext = new BlockPlacementContext(world, pos, player, stack, blockHitResult, includesFluid);
        if (blockPlacementContext.canPlace()) {
          state.cyanShape = blockPlacementContext.stateToPlace.getOutlineShape(world, blockPlacementContext.posToPlace, ShapeContext.of(player));
          state.cyanPos = blockPlacementContext.posToPlace;
          state.blueShape = blockPlacementContext.stateToPlace.getFluidState().getShape(blockPlacementContext.world, blockPlacementContext.posToPlace);
          state.bluePos = blockPlacementContext.posToPlace;
        }
      }
      if (hand == Hand.MAIN_HAND && (carryingToolData == null || player.isCreative())) {
        final BlockState hitState = world.getBlockState(pos);
        // 只有当主手持有此物品时，才绘制红色边框。
        state.redShape = hitState.getOutlineShape(world, pos, ShapeContext.of(player));
        state.redPos = pos;
        state.orangeShape = hitState.getFluidState().getShape(world, pos);
        state.orangePos = pos;
      }
    }
    if (hand != Hand.MAIN_HAND || player.isSpectator()) { // hasAccess 已经在前面检查过
      return state;
    }
    if (result != null && result.getType() == HitResult.Type.BLOCK && carryingToolData instanceof CarryingToolData.HoldingEntity holdingEntity) {
      state.cyanEntityWidth = holdingEntity.width();
      state.cyanEntityHeight = holdingEntity.height();
      state.cyanEntityPos = result.getPos();
    }
    if (!player.isCreative() && (carryingToolData != null)) {
      return state;
    }
    if (result instanceof EntityHitResult entityHitResult) {
      final Entity entity = entityHitResult.getEntity();
      state.redEntityShape = VoxelShapes.cuboid(entity.getBoundingBox());
    }

    return state;
  }

  @Environment(EnvType.CLIENT)
  @Override
  public boolean renderBlockOutline(PlayerEntity player, ItemStack itemStack, WorldRenderContext context, OutlineRenderState outlineRenderState) {
    final MishangRenderState data = context.worldState().getData(MISHANG_BLOCK_OUTLINE);
    if (!(data instanceof CarryingToolState state)) return true;

    final MatrixStack matrices = context.matrices();
    final VertexConsumer vertexConsumer = context.consumers().getBuffer(RenderLayer.getLines());
    final Vec3d cameraPos = context.worldState().cameraRenderState.pos;

    if (state.cyanShape != null && state.cyanPos != null) {
      VertexRendering.drawOutline(matrices, vertexConsumer, state.cyanShape, state.cyanPos.getX() - cameraPos.x, state.cyanPos.getY() - cameraPos.y, state.cyanPos.getZ() - cameraPos.z, OUTLINE_COLOR_CYAN);
    }

    if (state.blueShape != null && state.bluePos != null) {
      VertexRendering.drawOutline(matrices, vertexConsumer, state.blueShape, state.bluePos.getX() - cameraPos.x, state.bluePos.getY() - cameraPos.y, state.bluePos.getZ() - cameraPos.z, OUTLINE_COLOR_AO);
    }

    if (state.redShape != null && state.redPos != null) {
      VertexRendering.drawOutline(matrices, vertexConsumer, state.redShape, state.redPos.getX() - cameraPos.x, state.redPos.getY() - cameraPos.y, state.redPos.getZ() - cameraPos.z, OUTLINE_COLOR_AKA);
    }

    if (state.redShape != null && state.orangePos != null) {
      VertexRendering.drawOutline(matrices, vertexConsumer, state.redShape, state.orangePos.getX() - cameraPos.x, state.orangePos.getY() - cameraPos.y, state.orangePos.getZ() - cameraPos.z, OUTLINE_COLOR_ORANGE);
    }

    return false;
  }

  @Environment(EnvType.CLIENT)
  @Override
  public void renderBeforeOutline(ClientPlayerEntity player, ItemStack stack, WorldRenderContext context) {
    // 只在使用主手且有权限时持有此物品时进行渲染。
    final MishangRenderState data = context.worldState().getData(MISHANG_BLOCK_OUTLINE);
    if (!(data instanceof CarryingToolState state)) return;

    final MatrixStack matrices = context.matrices();
    final VertexConsumer vertexConsumer = context.consumers().getBuffer(RenderLayer.getLines());
    final Vec3d cameraPos = context.worldState().cameraRenderState.pos;

    if (state.cyanEntityPos != null) {
      final float width = state.cyanEntityWidth;
      final float height = state.cyanEntityHeight;
      final Vec3d pos = state.cyanEntityPos;
      VertexRendering.drawOutline(matrices, vertexConsumer, VoxelShapes.cuboid(pos.x - width / 2, pos.y, pos.z - width / 2, pos.x + width / 2, pos.y + height, pos.z + width / 2), -cameraPos.x, -cameraPos.y, -cameraPos.z, OUTLINE_COLOR_CYAN);
    }
    if (state.redEntityShape != null) {
      VertexRendering.drawOutline(matrices, vertexConsumer, state.redEntityShape, -cameraPos.x, -cameraPos.y, -cameraPos.z, OUTLINE_COLOR_AKA);
    }
  }
}
