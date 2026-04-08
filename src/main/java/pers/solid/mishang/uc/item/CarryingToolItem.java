package pers.solid.mishang.uc.item;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.api.EnvironmentInterface;
import net.fabricmc.fabric.api.client.rendering.v1.level.LevelExtractionContext;
import net.fabricmc.fabric.api.client.rendering.v1.level.LevelRenderContext;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.ShapeRenderer;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.client.renderer.state.level.BlockOutlineRenderState;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.UUIDUtil;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.permissions.Permissions;
import net.minecraft.util.ARGB;
import net.minecraft.util.Mth;
import net.minecraft.util.ProblemReporter;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.boss.enderdragon.EnderDragonPart;
import net.minecraft.world.entity.item.FallingBlockEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.TypedEntityData;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.GameMasterBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.TagValueOutput;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.Nullable;
import pers.solid.mishang.uc.MishangucClient;
import pers.solid.mishang.uc.MishangucRules;
import pers.solid.mishang.uc.components.CarryingToolData;
import pers.solid.mishang.uc.components.MishangucComponents;
import pers.solid.mishang.uc.render.RendersBeforeOutline;
import pers.solid.mishang.uc.render.state.CarryingToolState;
import pers.solid.mishang.uc.render.state.MishangRenderState;
import pers.solid.mishang.uc.util.BlockPlacementContext;
import pers.solid.mishang.uc.util.WithMishangTooltip;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@EnvironmentInterface(value = EnvType.CLIENT, itf = RendersBeforeOutline.class)
public class CarryingToolItem extends BlockToolItem
    implements MishangucItem, InteractsWithEntity, RendersBeforeOutline, WithMishangTooltip {

  private static final int OUTLINE_COLOR_CYAN = ARGB.colorFromFloat(0.8f, 0, 1, 1);
  private static final int OUTLINE_COLOR_AO = ARGB.colorFromFloat(0.5f, 0, 0.5f, 1);
  private static final int OUTLINE_COLOR_AKA = ARGB.colorFromFloat(0.8f, 1, 0, 0);
  private static final int OUTLINE_COLOR_ORANGE = ARGB.colorFromFloat(0.5f, 1, 0.5f, 0);

  public CarryingToolItem(Properties settings, @Nullable Boolean includesFluid) {
    super(settings, includesFluid);
  }

  @Contract(pure = true)
  public static @Nullable BlockState getHoldingBlockState(ItemStack stack, LevelReader world) {
    final CarryingToolData carryingToolData = stack.get(MishangucComponents.CARRYING_TOOL_DATA);
    return carryingToolData instanceof CarryingToolData.HoldingBlockState holdingBlockState ? holdingBlockState.state() : null;
  }

  /**
   * 避免重复召唤实体时，因为 UUID 雷同而无法召唤。
   */
  @Contract(mutates = "param1")
  private static void setHoldingEntityUUID(ItemStack stack, UUID uuid) {
    final CarryingToolData carryingToolData = stack.get(MishangucComponents.CARRYING_TOOL_DATA);
    final CompoundTag entityTag = carryingToolData instanceof CarryingToolData.HoldingEntity holdingEntity ? holdingEntity.entityTag().orElse(null) : null;
    if (entityTag != null) {
      entityTag.store("UUID", UUIDUtil.CODEC, uuid);
    }
  }

  @Contract(pure = true)
  public static @Nullable Entity createHoldingEntity(CarryingToolData.HoldingEntity data, ServerLevel world, Player player) {
    final EntityType<?> entityType = data.entityType();
    return entityType.create(world, entity -> data.entityTag().ifPresent(nbtCompound -> TypedEntityData.of(entityType, nbtCompound).loadInto(entity)), player.blockPosition(), EntitySpawnReason.EVENT, false, false);
  }

  private static Component getEntityName(ItemStack stack) {
    final CarryingToolData carryingToolData = stack.get(MishangucComponents.CARRYING_TOOL_DATA);
    if (carryingToolData instanceof CarryingToolData.HoldingEntity holdingEntity) {
      return holdingEntity.name();
    } else {
      return Component.empty();
    }
  }

  @Override
  public Component getName(ItemStack stack) {
    final Component name = super.getName(stack);
    final CarryingToolData carryingToolData = stack.get(MishangucComponents.CARRYING_TOOL_DATA);
    if (carryingToolData instanceof CarryingToolData.HoldingEntity holdingEntity) {
      return Component.translatable("item.mishanguc.carrying_tool.holding", name, holdingEntity.name());
    } else if (carryingToolData instanceof CarryingToolData.HoldingBlockState holdingBlockState) {
      return Component.translatable("item.mishanguc.carrying_tool.holding", name, holdingBlockState.state().getBlock().getName());
    } else {
      return Component.translatable("item.mishanguc.carrying_tool.empty", name);
    }
  }

  @Override
  public void getMishangTooltip(ItemStack stack, TooltipContext context, List<Component> tooltip, TooltipFlag options) {
    tooltip.add(Component.translatable("item.mishanguc.carrying_tool.tooltip.1").withStyle(ChatFormatting.GRAY));
    tooltip.add(Component.translatable("item.mishanguc.carrying_tool.tooltip.2").withStyle(ChatFormatting.GRAY));
    tooltip.add(Component.translatable("item.mishanguc.carrying_tool.tooltip.3").withStyle(ChatFormatting.GRAY));
  }


  @Override
  public InteractionResult useOnBlock(ItemStack stack, Player player, Level world, BlockHitResult blockHitResult, InteractionHand hand, boolean fluidIncluded) {
    if (!hasAccess(player, world, true)) {
      return InteractionResult.PASS;
    }
    final CarryingToolData carryingToolData = stack.get(MishangucComponents.CARRYING_TOOL_DATA);
    if (carryingToolData instanceof CarryingToolData.HoldingBlockState) {
      final BlockPlacementContext blockPlacementContext = new BlockPlacementContext(world, blockHitResult.getBlockPos(), player, stack, blockHitResult, fluidIncluded);
      if (blockPlacementContext.canPlace()) {
        blockPlacementContext.setBlockState(3);
        blockPlacementContext.setBlockEntity();
        if (world.isClientSide()) {
          blockPlacementContext.playSound();
        } else {
          String key = player.isCreative() ? "item.mishanguc.carrying_tool.message.placed_creative" : "item.mishanguc.carrying_tool.message.placed";
          player.sendOverlayMessage(Component.translatable(key, blockPlacementContext.stateToPlace.getBlock().getName()));
        }
        if (!player.isCreative()) {
          stack.remove(MishangucComponents.CARRYING_TOOL_DATA);
        }
        return InteractionResult.SUCCESS;
      } else {
        return InteractionResult.PASS;
      }
    } else if (carryingToolData instanceof CarryingToolData.HoldingEntity holdingEntity) {
      if (world instanceof ServerLevel serverWorld) {
        final Entity entity = createHoldingEntity(holdingEntity, serverWorld, player);
        if (entity == null)
          return InteractionResult.PASS;
        final Vec3 pos = blockHitResult.getLocation();
        entity.absSnapTo(pos.x, pos.y, pos.z);
        final boolean spawnEntity = world.addFreshEntity(entity);
        if (spawnEntity) {
          String key = player.isCreative() ? "item.mishanguc.carrying_tool.message.spawned_creative" : "item.mishanguc.carrying_tool.message.spawned";
          player.sendOverlayMessage(Component.translatable(key, getEntityName(stack)));
          if (!player.isCreative()) {
            stack.remove(MishangucComponents.CARRYING_TOOL_DATA);
          } else {
            setHoldingEntityUUID(stack, Mth.createInsecureUUID(player.getRandom()));
          }
          return InteractionResult.SUCCESS;
        } else {
          return InteractionResult.FAIL;
        }
      } else {
        // 客户端部分。
        return InteractionResult.SUCCESS;
      }
    } else {
      final BlockState blockState = world.getBlockState(blockHitResult.getBlockPos());
      final InteractionResult actionResult = blockState.useWithoutItem(world, player, blockHitResult);
      if (actionResult.consumesAction()) {
        return actionResult;
      } else {
        if (world.isClientSide()) {
          return InteractionResult.PASS;
        } else {
          player.sendOverlayMessage(Component.translatable("item.mishanguc.carrying_tool.message.no_placing").withStyle(ChatFormatting.RED));
          return InteractionResult.FAIL;
        }
      }
    }
  }


  private boolean hasAccess(Player player, Level world, boolean warn) {
    if (!(world instanceof ServerLevel serverWorld)) {
      return MishangucClient.CLIENT_CARRYING_TOOL_ACCESS.get().hasAccess(player);
    } else {
      final MishangucRules.ToolAccess toolAccess = serverWorld.getGameRules().get(MishangucRules.CARRYING_TOOL_ACCESS);
      return toolAccess.hasAccess(player, warn);
    }
  }

  @Override
  public InteractionResult beginAttackBlock(ItemStack stack, Player player, Level world, InteractionHand hand, BlockPos pos, Direction direction, boolean fluidIncluded) {
    if (!hasAccess(player, world, true))
      return InteractionResult.PASS;
    final CarryingToolData carryingToolData = stack.get(MishangucComponents.CARRYING_TOOL_DATA);
    if (carryingToolData instanceof CarryingToolData.HoldingBlockState holdingBlockState && !player.isCreative()) {
      if (!world.isClientSide()) {
        player.sendOverlayMessage(Component.translatable("item.mishanguc.carrying_tool.message.no_picking", Optional.of(holdingBlockState.state().getBlock()).map(Block::getName).orElse(Component.empty())).withStyle(ChatFormatting.RED));
        return InteractionResult.FAIL;
      } else {
        return InteractionResult.CONSUME;
      }
    }
    if (carryingToolData instanceof CarryingToolData.HoldingEntity holdingEntity && !player.isCreative()) {
      if (world.isClientSide())
        return InteractionResult.CONSUME;
      else {
        player.sendOverlayMessage(Component.translatable("item.mishanguc.carrying_tool.message.no_picking", holdingEntity.name()).withStyle(ChatFormatting.RED));
        return InteractionResult.FAIL;
      }
    }
    final BlockState removed = world.getBlockState(pos);
    if (removed.getBlock() instanceof GameMasterBlock && !player.permissions().hasPermission(Permissions.COMMANDS_GAMEMASTER)) {
      return InteractionResult.FAIL;
    }
    final BlockEntity blockEntity = world.getBlockEntity(pos);
    if (blockEntity != null) {
      stack.set(MishangucComponents.CARRYING_TOOL_DATA, new CarryingToolData.HoldingBlockState(removed, Optional.of(blockEntity.saveCustomOnly(world.registryAccess()))));
    } else {
      stack.set(MishangucComponents.CARRYING_TOOL_DATA, new CarryingToolData.HoldingBlockState(removed, Optional.empty()));
    }
    world.removeBlockEntity(pos);
    world.setBlockAndUpdate(pos, Blocks.AIR.defaultBlockState());
    if (world.isClientSide()) {
      world.levelEvent(2001, pos, Block.getId(removed));
    }
    if (!world.isClientSide()) {
      if (carryingToolData instanceof CarryingToolData.HoldingEntity holdingEntity) {
        player.sendOverlayMessage(Component.translatable("item.mishanguc.carrying_tool.message.picked_overriding", holdingEntity.name()));
      } else if (carryingToolData instanceof CarryingToolData.HoldingBlockState holdingBlockState) {
        player.sendOverlayMessage(Component.translatable("item.mishanguc.carrying_tool.message.picked_overriding", removed.getBlock().getName(), holdingBlockState.state().getBlock().getName()));
      } else {
        player.sendOverlayMessage(Component.translatable("item.mishanguc.carrying_tool.message.pick", removed.getBlock().getName()));
      }
    }
    return InteractionResult.SUCCESS;
  }


  @Override
  public InteractionResult use(Level world, Player user, InteractionHand hand) {
    final InteractionResult use = super.use(world, user, hand);
    if (use.consumesAction() || !hasAccess(user, world, true)) {
      return use;
    }
    final ItemStack stack = user.getItemInHand(hand);
    final CarryingToolData carryingToolData = stack.get(MishangucComponents.CARRYING_TOOL_DATA);
    if (carryingToolData instanceof CarryingToolData.HoldingBlockState(BlockState state, Optional<CompoundTag> blockEntityTag)) {
      if (state.getBlock() instanceof GameMasterBlock && !user.permissions().hasPermission(Permissions.COMMANDS_GAMEMASTER)) {
        return InteractionResult.FAIL;
      }
      if (world.isClientSide()) {
        return InteractionResult.SUCCESS;
      }
      final FallingBlockEntity fallingBlockEntity = new FallingBlockEntity(EntityType.FALLING_BLOCK, world);
      CompoundTag nbt = new CompoundTag();
      nbt.store("BlockState", BlockState.CODEC, state);
      TypedEntityData.of(fallingBlockEntity.getType(), nbt).loadInto(fallingBlockEntity);
      final Vec3 eyePos = user.getEyePosition();
      fallingBlockEntity.absSnapTo(eyePos.x, eyePos.y, eyePos.z, user.getYRot(), user.getXRot());
      fallingBlockEntity.setDeltaMovement(Vec3.directionFromRotation(user.getXRot(), user.getYRot()).scale(2).add(user.getDeltaMovement()));
      fallingBlockEntity.dropItem = true;
      fallingBlockEntity.blockData = blockEntityTag.orElse(null);
      fallingBlockEntity.setHurtsEntities(state.getBlock().getExplosionResistance(), Integer.MAX_VALUE);
      final boolean spawnEntity = world.addFreshEntity(fallingBlockEntity);
      if (spawnEntity) {
        if (!user.isCreative()) {
          stack.remove(MishangucComponents.CARRYING_TOOL_DATA);
        }
        String key = user.isCreative() ? "item.mishanguc.carrying_tool.message.block_thrown_creative" : "item.mishanguc.carrying_tool.message.block_thrown";
        user.sendOverlayMessage(Component.translatable(key, state.getBlock().getName()));
        return InteractionResult.SUCCESS;
      } else {
        return InteractionResult.FAIL;
      }
    } else if (carryingToolData instanceof CarryingToolData.HoldingEntity holdingEntity) {
      if (world instanceof ServerLevel serverWorld) {
        final Entity entity = createHoldingEntity(holdingEntity, serverWorld, user);
        if (entity == null)
          return use;
        final Vec3 pos = user.position();
        entity.absSnapTo(pos.x, pos.y, pos.z, user.getYRot(), user.getXRot());
        entity.setDeltaMovement(Vec3.directionFromRotation(user.getXRot(), user.getYRot()).scale(2).add(user.getDeltaMovement()));
        final boolean spawnEntity = world.addFreshEntity(entity);
        if (spawnEntity) {
          String key = user.isCreative() ? "item.mishanguc.carrying_tool.message.entity_thrown_creative" : "item.mishanguc.carrying_tool.message.entity_thrown";
          user.sendOverlayMessage(Component.translatable(key, getEntityName(stack)));
          if (!user.isCreative()) {
            stack.remove(MishangucComponents.CARRYING_TOOL_DATA);
          } else {
            setHoldingEntityUUID(stack, Mth.createInsecureUUID(world.getRandom()));
          }
          return InteractionResult.SUCCESS;
        } else {
          return InteractionResult.FAIL;
        }
      } else {
        return InteractionResult.SUCCESS;
      }
    } else {
      return use;
    }
  }

  @Override
  public InteractionResult attackEntityCallback(Player player, Level world, InteractionHand hand, Entity entity, @Nullable EntityHitResult hitResult) {
    if (!hasAccess(player, world, true) || player.isSpectator())
      return InteractionResult.PASS;
    final ItemStack stack = player.getItemInHand(hand);
    final CarryingToolData carryingToolData = stack.get(MishangucComponents.CARRYING_TOOL_DATA);
    if (entity instanceof Player) {
      if (world.isClientSide()) {
        return InteractionResult.PASS;
      } else {
        player.sendSystemMessage(Component.translatable("item.mishanguc.carrying_tool.message.pick_player").withStyle(ChatFormatting.RED));
        return InteractionResult.FAIL;
      }
    } else if (carryingToolData instanceof CarryingToolData.HoldingEntity holdingEntity && !player.isCreative()) {
      if (world.isClientSide())
        return InteractionResult.SUCCESS;
      else {
        player.sendOverlayMessage(Component.translatable("item.mishanguc.carrying_tool.message.no_picking", holdingEntity.name()).withStyle(ChatFormatting.RED));
        return InteractionResult.FAIL;
      }
    } else if (carryingToolData instanceof CarryingToolData.HoldingBlockState holdingBlockState && !player.isCreative()) {
      if (world.isClientSide())
        return InteractionResult.SUCCESS;
      else {
        player.sendOverlayMessage(Component.translatable("item.mishanguc.carrying_tool.message.no_picking", Optional.ofNullable(holdingBlockState.state().getBlock()).map(Block::getName).orElse(Component.empty())).withStyle(ChatFormatting.RED));
        return InteractionResult.FAIL;
      }
    }
    if (world instanceof ServerLevel serverWorld) {
      if (carryingToolData instanceof CarryingToolData.HoldingEntity holdingEntity) {
        player.sendOverlayMessage(Component.translatable("item.mishanguc.carrying_tool.message.pick_entity_overriding", entity.getName(), holdingEntity.name()));
      } else if (carryingToolData instanceof CarryingToolData.HoldingBlockState holdingBlockState) {
        player.sendOverlayMessage(Component.translatable("item.mishanguc.carrying_tool.message.pick_entity_overriding", entity.getName(), holdingBlockState.state().getBlock().getName()));
      } else {
        player.sendOverlayMessage(Component.translatable("item.mishanguc.carrying_tool.message.pick_entity", entity.getName()));
      }
      final TagValueOutput nbtWriteView = TagValueOutput.createWithContext(ProblemReporter.DISCARDING, entity.registryAccess());
      entity.saveWithoutId(nbtWriteView);
      stack.set(MishangucComponents.CARRYING_TOOL_DATA, new CarryingToolData.HoldingEntity(entity.getType(), Optional.of(nbtWriteView.buildResult()), entity.getName(), entity.getBbWidth(), entity.getBbHeight()));
      entity.remove(Entity.RemovalReason.DISCARDED);
      if (entity instanceof EnderDragonPart enderDragonPart) {
        enderDragonPart.parentMob.kill(serverWorld);
      }
    }
    return InteractionResult.SUCCESS;
  }

  @Environment(EnvType.CLIENT)
  @Override
  public @Nullable CarryingToolState getMishangRenderState(LocalPlayer player, InteractionHand hand, ItemStack stack, LevelExtractionContext context, @Nullable HitResult result) {
    final CarryingToolState state = new CarryingToolState();

    final ClientLevel world = context.level();
    if (!hasAccess(player, world, true)) {
      return state;
    }

    final CarryingToolData carryingToolData = stack.get(MishangucComponents.CARRYING_TOOL_DATA);

    if (result instanceof BlockHitResult blockHitResult) {
      final boolean includesFluid = this.includesFluid(stack, player.isShiftKeyDown());
      final BlockPos pos = blockHitResult.getBlockPos();

      if (carryingToolData instanceof CarryingToolData.HoldingBlockState) {
        final BlockPlacementContext blockPlacementContext = new BlockPlacementContext(world, pos, player, stack, blockHitResult, includesFluid);
        if (blockPlacementContext.canPlace()) {
          state.cyanShape = blockPlacementContext.stateToPlace.getShape(world, blockPlacementContext.posToPlace, CollisionContext.of(player));
          state.cyanPos = blockPlacementContext.posToPlace;
          state.blueShape = blockPlacementContext.stateToPlace.getFluidState().getShape(blockPlacementContext.world, blockPlacementContext.posToPlace);
          state.bluePos = blockPlacementContext.posToPlace;
        }
      }
      if (hand == InteractionHand.MAIN_HAND && (carryingToolData == null || player.isCreative())) {
        final BlockState hitState = world.getBlockState(pos);
        // 只有当主手持有此物品时，才绘制红色边框。
        state.redShape = hitState.getShape(world, pos, CollisionContext.of(player));
        state.redPos = pos;
        state.orangeShape = hitState.getFluidState().getShape(world, pos);
        state.orangePos = pos;
      }
    }
    if (hand != InteractionHand.MAIN_HAND || player.isSpectator()) { // hasAccess 已经在前面检查过
      return state;
    }
    if (result != null && result.getType() == HitResult.Type.BLOCK && carryingToolData instanceof CarryingToolData.HoldingEntity holdingEntity) {
      state.cyanEntityWidth = holdingEntity.width();
      state.cyanEntityHeight = holdingEntity.height();
      state.cyanEntityPos = result.getLocation();
    }
    if (!player.isCreative() && (carryingToolData != null)) {
      return state;
    }
    if (result instanceof EntityHitResult entityHitResult) {
      final Entity entity = entityHitResult.getEntity();
      state.redEntityShape = Shapes.create(entity.getBoundingBox());
    }

    return state;
  }

  @Environment(EnvType.CLIENT)
  @Override
  public boolean renderBlockOutline(Player player, ItemStack itemStack, LevelRenderContext context, BlockOutlineRenderState outlineRenderState) {
    final MishangRenderState data = context.levelState().getData(MISHANG_BLOCK_OUTLINE);
    if (!(data instanceof CarryingToolState state)) return true;

    final PoseStack matrices = context.poseStack();
    final VertexConsumer vertexConsumer = context.bufferSource().getBuffer(RenderTypes.lines());
    final Vec3 cameraPos = context.levelState().cameraRenderState.pos;

    if (state.cyanShape != null && state.cyanPos != null) {
      ShapeRenderer.renderShape(matrices, vertexConsumer, state.cyanShape, state.cyanPos.getX() - cameraPos.x, state.cyanPos.getY() - cameraPos.y, state.cyanPos.getZ() - cameraPos.z, OUTLINE_COLOR_CYAN, Minecraft.getInstance().getWindow().getAppropriateLineWidth());
    }

    if (state.blueShape != null && state.bluePos != null) {
      ShapeRenderer.renderShape(matrices, vertexConsumer, state.blueShape, state.bluePos.getX() - cameraPos.x, state.bluePos.getY() - cameraPos.y, state.bluePos.getZ() - cameraPos.z, OUTLINE_COLOR_AO, Minecraft.getInstance().getWindow().getAppropriateLineWidth());
    }

    if (state.redShape != null && state.redPos != null) {
      ShapeRenderer.renderShape(matrices, vertexConsumer, state.redShape, state.redPos.getX() - cameraPos.x, state.redPos.getY() - cameraPos.y, state.redPos.getZ() - cameraPos.z, OUTLINE_COLOR_AKA, Minecraft.getInstance().getWindow().getAppropriateLineWidth());
    }

    if (state.redShape != null && state.orangePos != null) {
      ShapeRenderer.renderShape(matrices, vertexConsumer, state.orangeShape, state.orangePos.getX() - cameraPos.x, state.orangePos.getY() - cameraPos.y, state.orangePos.getZ() - cameraPos.z, OUTLINE_COLOR_ORANGE, Minecraft.getInstance().getWindow().getAppropriateLineWidth());
    }

    return false;
  }

  @Environment(EnvType.CLIENT)
  @Override
  public void renderBeforeOutline(LocalPlayer player, ItemStack stack, LevelRenderContext context) {
    // 只在使用主手且有权限时持有此物品时进行渲染。
    final MishangRenderState data = context.levelState().getData(MISHANG_BLOCK_OUTLINE);
    if (!(data instanceof CarryingToolState state)) return;

    final PoseStack matrices = context.poseStack();
    final VertexConsumer vertexConsumer = context.bufferSource().getBuffer(RenderTypes.lines());
    final Vec3 cameraPos = context.levelState().cameraRenderState.pos;

    if (state.cyanEntityPos != null) {
      final float width = state.cyanEntityWidth;
      final float height = state.cyanEntityHeight;
      final Vec3 pos = state.cyanEntityPos;
      ShapeRenderer.renderShape(matrices, vertexConsumer, Shapes.box(pos.x - width / 2, pos.y, pos.z - width / 2, pos.x + width / 2, pos.y + height, pos.z + width / 2), -cameraPos.x, -cameraPos.y, -cameraPos.z, OUTLINE_COLOR_CYAN, Minecraft.getInstance().getWindow().getAppropriateLineWidth());
    }
    if (state.redEntityShape != null) {
      ShapeRenderer.renderShape(matrices, vertexConsumer, state.redEntityShape, -cameraPos.x, -cameraPos.y, -cameraPos.z, OUTLINE_COLOR_AKA, Minecraft.getInstance().getWindow().getAppropriateLineWidth());
    }
  }
}
