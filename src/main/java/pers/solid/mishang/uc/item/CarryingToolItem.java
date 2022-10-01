package pers.solid.mishang.uc.item;

import net.devtech.arrp.api.RuntimeResourcePack;
import net.devtech.arrp.generator.ItemResourceGenerator;
import net.devtech.arrp.json.models.JModel;
import net.devtech.arrp.json.models.JOverride;
import net.devtech.arrp.json.models.JTextures;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.api.EnvironmentInterface;
import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderContext;
import net.fabricmc.fabric.api.util.NbtType;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.ShapeContext;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.FallingBlockEntity;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.boss.dragon.EnderDragonPart;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtHelper;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.*;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.World;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import pers.solid.mishang.uc.MishangUtils;
import pers.solid.mishang.uc.MishangucClient;
import pers.solid.mishang.uc.MishangucRules;
import pers.solid.mishang.uc.mixin.WorldRendererInvoker;
import pers.solid.mishang.uc.render.RendersBeforeOutline;
import pers.solid.mishang.uc.util.BlockPlacementContext;
import pers.solid.mishang.uc.util.TextBridge;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

@EnvironmentInterface(value = EnvType.CLIENT, itf = RendersBeforeOutline.class)
@ApiStatus.AvailableSince("0.2.4")
public class CarryingToolItem extends BlockToolItem
    implements ItemResourceGenerator, InteractsWithEntity, RendersBeforeOutline {
  public CarryingToolItem(Settings settings, @Nullable Boolean includesFluid) {
    super(settings, includesFluid);
  }

  @Contract(pure = true)
  public static boolean hasHoldingBlockState(@NotNull ItemStack stack) {
    return stack.getSubTag("holdingBlockState") != null;
  }

  @Contract(pure = true)
  public static boolean hasHoldingEntity(@NotNull ItemStack stack) {
    final NbtCompound nbt = stack.getTag();
    return nbt != null && nbt.contains("holdingEntityType", NbtType.STRING);
  }

  @Contract(pure = true)
  public static @Nullable Block getHoldingBlock(@NotNull ItemStack stack) {
    final NbtCompound holdingBlockStateNbt = stack.getSubTag("holdingBlockState");
    if (holdingBlockStateNbt == null) return null;
    final Identifier identifier = Identifier.tryParse(holdingBlockStateNbt.getString("Name"));
    return Registry.BLOCK.get(identifier);
  }

  @Contract(pure = true)
  public static @Nullable BlockState getHoldingBlockState(@NotNull ItemStack stack) {
    final NbtCompound holdingBlockStateNbt = stack.getSubTag("holdingBlockState");
    if (holdingBlockStateNbt != null) {
      try {
        return NbtHelper.toBlockState(holdingBlockStateNbt);
      } catch (Exception e) {
        return null;
      }
    } else {
      return null;
    }
  }

  @Contract(mutates = "param1")
  public static void setHoldingEntity(@NotNull ItemStack stack, @Nullable Entity entity) {
    if (entity == null) {
      stack.removeSubTag("holdingEntityType");
      stack.removeSubTag("EntityTag");
      stack.removeSubTag("holdingEntityName");
      stack.removeSubTag("holdingEntityWidth");
      stack.removeSubTag("holdingEntityHeight");
    } else {
      NbtCompound entityTag = new NbtCompound();
      entity.saveSelfNbt(entityTag);
      final NbtCompound nbt = stack.getOrCreateTag();
      nbt.put("EntityTag", entityTag);
      nbt.putString("holdingEntityType", Registry.ENTITY_TYPE.getId(entity.getType()).toString());
      nbt.putString("holdingEntityName", Text.Serializer.toJson(entity.getName()));
      nbt.putFloat("holdingEntityWidth", entity.getWidth());
      nbt.putFloat("holdingEntityHeight", entity.getHeight());
    }
  }

  /**
   * 避免重复召唤实体时，因为 UUID 雷同而无法召唤。
   */
  @Contract(mutates = "param1")
  private static void setHoldingEntityUUID(ItemStack stack, UUID uuid) {
    final NbtCompound entityTag = stack.getSubTag("EntityTag");
    if (entityTag != null) {
      entityTag.putUuid("UUID", uuid);
    }
  }

  @Contract(mutates = "param1")
  public static void setHoldingBlockState(@NotNull ItemStack stack, @Nullable BlockState state) {
    if (state == null) {
      stack.removeSubTag("holdingBlockState");
    } else {
      stack.putSubTag("holdingBlockState", NbtHelper.fromBlockState(state));
    }
  }

  @Contract(pure = true)
  public static @Nullable Entity createHoldingEntity(@NotNull ItemStack stack, ServerWorld world, PlayerEntity player) {
    final NbtCompound nbt = stack.getTag();
    if (nbt != null) {
      final String holdingEntityType = nbt.getString("holdingEntityType");
      final Identifier entityTypeId = Identifier.tryParse(holdingEntityType);
      final Optional<EntityType<?>> entityType = Registry.ENTITY_TYPE.getOrEmpty(entityTypeId);
      if (entityTypeId == null || !entityType.isPresent()) {
        // 无效的 id，予以 null。
        return null;
      } else {
        return entityType.get().create(world, nbt, null, null, player.getBlockPos(), SpawnReason.EVENT, false, false);
      }
    }
    return null;
  }

  private static MutableText getEntityName(@NotNull ItemStack stack) {
    final NbtCompound nbt = stack.getTag();
    if (nbt == null) return TextBridge.empty();
    if (nbt.contains("holdingEntityName", NbtType.STRING)) {
      return Text.Serializer.fromJson(nbt.getString("holdingEntityName"));
    } else if (nbt.contains("holdingEntityType", NbtType.STRING)) {
      final Identifier holdingEntityType = Identifier.tryParse(nbt.getString("holdingEntityType"));
      return Registry.ENTITY_TYPE.getOrEmpty(holdingEntityType).map(entityType -> entityType.getName().shallowCopy()).orElseGet(() -> TextBridge.literal("" + holdingEntityType));
    } else {
      return TextBridge.empty();
    }
  }

  @Override
  public Text getName(ItemStack stack) {
    final Text name = super.getName(stack);
    final Block holdingBlock = getHoldingBlock(stack);
    if (hasHoldingEntity(stack)) {
      return TextBridge.translatable("item.mishanguc.carrying_tool.holding", name, getEntityName(stack));
    } else if (holdingBlock == null) {
      return TextBridge.translatable("item.mishanguc.carrying_tool.empty", name);
    } else {
      return TextBridge.translatable("item.mishanguc.carrying_tool.holding", name, MishangUtils.getBlockName(holdingBlock));
    }
  }

  @Override
  public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
    super.appendTooltip(stack, world, tooltip, context);
    tooltip.add(TextBridge.translatable("item.mishanguc.carrying_tool.tooltip.1").formatted(Formatting.GRAY));
    tooltip.add(TextBridge.translatable("item.mishanguc.carrying_tool.tooltip.2").formatted(Formatting.GRAY));
    tooltip.add(TextBridge.translatable("item.mishanguc.carrying_tool.tooltip.3").formatted(Formatting.GRAY));
    final Block holdingBlock = getHoldingBlock(stack);
    if (holdingBlock != null) {
      tooltip.add(TextBridge.translatable("item.mishanguc.carrying_tool.tooltip.currently", MishangUtils.getBlockName(holdingBlock).formatted(Formatting.YELLOW)).formatted(Formatting.GREEN));
    } else if (hasHoldingEntity(stack)) {
      tooltip.add(TextBridge.translatable("item.mishanguc.carrying_tool.tooltip.currently", getEntityName(stack).formatted(Formatting.YELLOW)).formatted(Formatting.GREEN));
    }
  }

  @Override
  public ActionResult useOnBlock(ItemStack stack, PlayerEntity player, World world, BlockHitResult blockHitResult, Hand hand, boolean fluidIncluded) {
    if (!hasAccess(player, world, true)) {
      return ActionResult.PASS;
    }
    if (hasHoldingBlockState(stack)) {
      final BlockPlacementContext blockPlacementContext = new BlockPlacementContext(world, blockHitResult.getBlockPos(), player, stack, blockHitResult, fluidIncluded);
      if (blockPlacementContext.canPlace()) {
        blockPlacementContext.setBlockState(3);
        blockPlacementContext.setBlockEntity();
        if (world.isClient) {
          blockPlacementContext.playSound();
        } else {
          player.sendMessage(TextBridge.translatable(player.isCreative() ? "item.mishanguc.carrying_tool.message.placed_creative" : "item.mishanguc.carrying_tool.message.placed", MishangUtils.getBlockName(blockPlacementContext.stateToPlace.getBlock())), true);
        }
        if (!player.isCreative()) {
          setHoldingBlockState(stack, null);
          stack.removeSubTag("BlockEntityTag");
        }
        return ActionResult.success(world.isClient);
      } else {
        return ActionResult.PASS;
      }
    } else if (hasHoldingEntity(stack)) {
      if (world instanceof ServerWorld) {
        final Entity entity = createHoldingEntity(stack, ((ServerWorld) world), player);
        if (entity == null) return ActionResult.PASS;
        final Vec3d pos = blockHitResult.getPos();
        entity.updatePosition(pos.x, pos.y, pos.z);
        final boolean spawnEntity = world.spawnEntity(entity);
        if (spawnEntity) {
          player.sendMessage(TextBridge.translatable(player.isCreative() ? "item.mishanguc.carrying_tool.message.spawned_creative" : "item.mishanguc.carrying_tool.message.spawned", getEntityName(stack)), true);
          if (!player.isCreative()) {
            setHoldingEntity(stack, null);
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
      final ActionResult actionResult = blockState.onUse(world, player, hand, blockHitResult);
      if (actionResult.isAccepted()) {
        return actionResult;
      } else {
        if (world.isClient) {
          return ActionResult.PASS;
        } else {
          player.sendMessage(TextBridge.translatable("item.mishanguc.carrying_tool.message.no_placing").formatted(Formatting.RED), true);
          return ActionResult.FAIL;
        }
      }
    }
  }

  private boolean hasAccess(PlayerEntity player, World world, boolean warn) {
    if (world.isClient) {
      return MishangucClient.CLIENT_CARRYING_TOOL_ACCESS.get().hasAccess(player);
    } else {
      final MishangucRules.ToolAccess toolAccess = world.getGameRules().get(MishangucRules.CARRYING_TOOL_ACCESS).get();
      return toolAccess.hasAccess(player, warn);
    }
  }

  @Override
  public ActionResult beginAttackBlock(ItemStack stack, PlayerEntity player, World world, Hand hand, BlockPos pos, Direction direction, boolean fluidIncluded) {
    if (!hasAccess(player, world, true)) return ActionResult.PASS;
    final Block alreadyHolding = getHoldingBlock(stack);
    if (alreadyHolding != null && !player.isCreative()) {
      if (!world.isClient) {
        player.sendMessage(TextBridge.translatable("item.mishanguc.carrying_tool.message.no_picking", Optional.ofNullable(getHoldingBlock(stack)).map(MishangUtils::getBlockName).orElse(TextBridge.empty())).formatted(Formatting.RED), true);
        return ActionResult.FAIL;
      } else {
        return ActionResult.CONSUME;
      }
    }
    final boolean alreadyHoldingEntity = hasHoldingEntity(stack);
    if (alreadyHoldingEntity && !player.isCreative()) {
      if (world.isClient) return ActionResult.CONSUME;
      else {
        player.sendMessage(TextBridge.translatable("item.mishanguc.carrying_tool.message.no_picking", getEntityName(stack)).formatted(Formatting.RED), true);
        return ActionResult.FAIL;
      }
    }
    final BlockState removed = world.getBlockState(pos);
    if (MishangUtils.isOperatorBlock(removed.getBlock()) && !player.hasPermissionLevel(2)) {
      return ActionResult.FAIL;
    }
    setHoldingBlockState(stack, removed);
    final BlockEntity blockEntity = world.getBlockEntity(pos);
    if (blockEntity != null) {
      blockEntity.writeNbt(stack.getOrCreateSubTag("BlockEntityTag"));
    } else {
      stack.removeSubTag("BlockEntityTag");
    }
    world.removeBlockEntity(pos);
    world.setBlockState(pos, Blocks.AIR.getDefaultState());
    if (world.isClient) {
      world.syncWorldEvent(2001, pos, Block.getRawIdFromState(removed));
    }
    if (!world.isClient) {
      if (alreadyHolding == null) {
        player.sendMessage(TextBridge.translatable("item.mishanguc.carrying_tool.message.pick", MishangUtils.getBlockName(removed.getBlock())), true);
      } else if (alreadyHoldingEntity) {
        player.sendMessage(TextBridge.translatable("item.mishanguc.carrying_tool.message.pick_overriding", getEntityName(stack)), true);
      } else {
        player.sendMessage(TextBridge.translatable("item.mishanguc.carrying_tool.message.picked_overriding", MishangUtils.getBlockName(removed.getBlock()), MishangUtils.getBlockName(alreadyHolding)), true);
      }
    }
    setHoldingEntity(stack, null);
    return ActionResult.SUCCESS;
  }

  @Override
  public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
    final TypedActionResult<ItemStack> use = super.use(world, user, hand);
    if (use.getResult().isAccepted() || !hasAccess(user, world, true)) {
      return use;
    }
    final ItemStack stack = user.getStackInHand(hand);
    final BlockState holdingBlockState = getHoldingBlockState(stack);
    final float pitch = user.getPitch(1);
    final float yaw = user.getYaw(1);
    final Vec3d fromPolar;
    {
      float f = MathHelper.cos(-yaw * 0.017453292F - 3.1415927F);
      float g = MathHelper.sin(-yaw * 0.017453292F - 3.1415927F);
      float h = -MathHelper.cos(-pitch * 0.017453292F);
      float i = MathHelper.sin(-pitch * 0.017453292F);
      fromPolar = new Vec3d((double) (g * h), (double) i, (double) (f * h));
    }
    final Vec3d velocity = fromPolar.multiply(2).add(user.getVelocity());
    if (holdingBlockState != null) {
      if (MishangUtils.isOperatorBlock(holdingBlockState.getBlock()) && !user.hasPermissionLevel(2)) {
        return TypedActionResult.fail(stack);
      }
      if (world.isClient) return TypedActionResult.success(use.getValue());
      final Vec3d eyePos = user.getPos().add(0, user.getStandingEyeHeight(), 0);
      final FallingBlockEntity fallingBlockEntity = new FallingBlockEntity(world, eyePos.x, eyePos.y, eyePos.z, Optional.ofNullable(stack.getSubTag("holdingBlockState")).map(NbtHelper::toBlockState).orElseGet(Blocks.AIR::getDefaultState));
      fallingBlockEntity.updatePositionAndAngles(eyePos.x, eyePos.y, eyePos.z, yaw, pitch);
      fallingBlockEntity.setVelocity(velocity);
      fallingBlockEntity.timeFalling = 1;
      fallingBlockEntity.dropItem = true;
      fallingBlockEntity.blockEntityData = stack.getSubTag("BlockEntityTag");
      fallingBlockEntity.setHurtEntities(true);
      final boolean spawnEntity = world.spawnEntity(fallingBlockEntity);
      if (spawnEntity) {
        if (!user.isCreative()) {
          setHoldingBlockState(stack, null);
          stack.removeSubTag("BlockEntityTag");
        }
        user.sendMessage(TextBridge.translatable(user.isCreative() ? "item.mishanguc.carrying_tool.message.block_thrown_creative" : "item.mishanguc.carrying_tool.message.block_thrown", MishangUtils.getBlockName(holdingBlockState.getBlock())), true);
        return TypedActionResult.success(use.getValue());
      } else {
        return TypedActionResult.fail(use.getValue());
      }
    } else if (hasHoldingEntity(stack)) {
      if (world instanceof ServerWorld) {
        final Entity entity = createHoldingEntity(stack, (ServerWorld) world, user);
        if (entity == null) return use;
        final Vec3d pos = user.getPos();
        entity.updatePositionAndAngles(pos.x, pos.y, pos.z, yaw, pitch);
        entity.setVelocity(velocity);
        final boolean spawnEntity = world.spawnEntity(entity);
        if (spawnEntity) {
          user.sendMessage(TextBridge.translatable(user.isCreative() ? "item.mishanguc.carrying_tool.message.entity_thrown_creative" : "item.mishanguc.carrying_tool.message.entity_thrown", getEntityName(stack)), true);
          if (!user.isCreative()) {
            setHoldingEntity(stack, null);
          } else {
            setHoldingEntityUUID(stack, MathHelper.randomUuid());
          }
          return TypedActionResult.success(use.getValue());
        } else {
          return TypedActionResult.fail(use.getValue());
        }
      } else {
        return TypedActionResult.success(use.getValue());
      }
    } else {
      return use;
    }
  }

  @Override
  public @NotNull ActionResult attackEntityCallback(PlayerEntity player, World world, Hand hand, Entity entity, @Nullable EntityHitResult hitResult) {
    if (!hasAccess(player, world, true) || player.isSpectator()) return ActionResult.PASS;
    final ItemStack stack = player.getStackInHand(hand);
    if (entity instanceof PlayerEntity) {
      if (world.isClient) {
        return ActionResult.PASS;
      } else {
        player.sendMessage(TextBridge.translatable("item.mishanguc.carrying_tool.message.pick_player").formatted(Formatting.RED), false);
        return ActionResult.FAIL;
      }
    } else if (hasHoldingEntity(stack) && !player.isCreative()) {
      if (world.isClient) return ActionResult.SUCCESS;
      else {
        player.sendMessage(TextBridge.translatable("item.mishanguc.carrying_tool.message.no_picking", getEntityName(stack)).formatted(Formatting.RED), true);
        return ActionResult.FAIL;
      }
    } else if (hasHoldingBlockState(stack) && !player.isCreative()) {
      if (world.isClient) return ActionResult.SUCCESS;
      else {
        player.sendMessage(TextBridge.translatable("item.mishanguc.carrying_tool.message.no_picking", Optional.ofNullable(getHoldingBlock(stack)).map(MishangUtils::getBlockName).orElse(TextBridge.empty())).formatted(Formatting.RED), true);
        return ActionResult.FAIL;
      }
    }
    if (!world.isClient) {
      if (hasHoldingEntity(stack)) {
        player.sendMessage(TextBridge.translatable("item.mishanguc.carrying_tool.message.pick_entity_overriding", entity.getName(), getEntityName(stack)), true);
      } else if (hasHoldingBlockState(stack)) {
        player.sendMessage(TextBridge.translatable("item.mishanguc.carrying_tool.message.pick_entity_overriding", entity.getName(), MishangUtils.getBlockName(Objects.requireNonNull(getHoldingBlock(stack)))), true);
      } else {
        player.sendMessage(TextBridge.translatable("item.mishanguc.carrying_tool.message.pick_entity", entity.getName()), true);
      }
      setHoldingBlockState(stack, null);
      setHoldingEntity(stack, entity);
      entity.remove();
      if (entity instanceof EnderDragonPart) {
        ((EnderDragonPart) entity).owner.kill();
      }
    }
    return ActionResult.SUCCESS;
  }

  @Environment(EnvType.CLIENT)
  @Override
  public @NotNull JModel getItemModel() {
    return new JModel("item/handheld").textures(JTextures.ofLayer0(getTextureId()))
        .addOverride(new JOverride("mishanguc:is_holding_block", 1f, getTextureId() + "_with_block"))
        .addOverride(new JOverride("mishanguc:is_holding_entity", 1f, getTextureId() + "_with_entity"));
  }

  @Environment(EnvType.CLIENT)
  @Override
  public void writeItemModel(RuntimeResourcePack pack) {
    ItemResourceGenerator.super.writeItemModel(pack);
    pack.addModel(new JModel("item/handheld").textures(JTextures.ofLayer0(getTextureId() + "_with_block")), getItemModelId().brrp_append("_with_block"));
    pack.addModel(new JModel("item/handheld").textures(JTextures.ofLayer0(getTextureId() + "_with_entity")), getItemModelId().brrp_append("_with_entity"));
  }

  @Environment(EnvType.CLIENT)
  @Override
  public boolean renderBlockOutline(PlayerEntity player, ItemStack itemStack, WorldRenderContext worldRenderContext, WorldRenderContext.BlockOutlineContext blockOutlineContext, Hand hand) {
    if (!hasAccess(player, worldRenderContext.world(), true)) return true;
    final MinecraftClient client = MinecraftClient.getInstance();
    final VertexConsumerProvider consumers = worldRenderContext.consumers();
    if (consumers == null) {
      return true;
    }
    final VertexConsumer vertexConsumer = consumers.getBuffer(RenderLayer.LINES);

    final BlockHitResult blockHitResult;
    final MatrixStack matrices = worldRenderContext.matrixStack();
    HitResult crosshairTarget = client.crosshairTarget;
    if (crosshairTarget instanceof BlockHitResult) {
      blockHitResult = (BlockHitResult) crosshairTarget;
    } else {
      return true;
    }
    final boolean includesFluid = this.includesFluid(itemStack, player.isSneaking());
    final BlockPos pos = blockOutlineContext.blockPos();
    if (hasHoldingBlockState(itemStack)) {
      final BlockPlacementContext blockPlacementContext =
          new BlockPlacementContext(worldRenderContext.world(), pos, player, itemStack, blockHitResult, includesFluid);
      if (blockPlacementContext.canPlace()) {
        WorldRendererInvoker.drawShapeOutline(matrices, vertexConsumer, blockPlacementContext.stateToPlace.getOutlineShape(
            blockPlacementContext.world, blockPlacementContext.posToPlace, ShapeContext.of(player)), blockPlacementContext.posToPlace.getX() - blockOutlineContext.cameraX(), blockPlacementContext.posToPlace.getY() - blockOutlineContext.cameraY(), blockPlacementContext.posToPlace.getZ() - blockOutlineContext.cameraZ(), 0, 1, 1, 0.8f);
        WorldRendererInvoker.drawShapeOutline(matrices, vertexConsumer, blockPlacementContext
            .stateToPlace
            .getFluidState()
            .getShape(blockPlacementContext.world, blockPlacementContext.posToPlace), blockPlacementContext.posToPlace.getX() - blockOutlineContext.cameraX(), blockPlacementContext.posToPlace.getY() - blockOutlineContext.cameraY(), blockPlacementContext.posToPlace.getZ() - blockOutlineContext.cameraZ(), 0, 0.5f, 1, 0.5f);
      }
    }
    if (hand == Hand.MAIN_HAND && ((!hasHoldingBlockState(itemStack) && !hasHoldingEntity(itemStack)) || player.isCreative())) {
      final BlockState hitState = worldRenderContext.world().getBlockState(pos);
      // 只有当主手持有此物品时，才绘制红色边框。
      WorldRendererInvoker.drawShapeOutline(matrices, vertexConsumer, hitState.getOutlineShape(
          worldRenderContext.world(), pos, ShapeContext.of(player)), pos.getX() - blockOutlineContext.cameraX(), pos.getY() - blockOutlineContext.cameraY(), pos.getZ() - blockOutlineContext.cameraZ(), 1, 0, 0, 0.8f);
      WorldRendererInvoker.drawShapeOutline(matrices, vertexConsumer, hitState
          .getFluidState()
          .getShape(worldRenderContext.world(), pos), pos.getX() - blockOutlineContext.cameraX(), pos.getY() - blockOutlineContext.cameraY(), pos.getZ() - blockOutlineContext.cameraZ(), 1, 0.5f, 0, 0.5f);
    }
    return false;
  }

  @Environment(EnvType.CLIENT)
  @Override
  public void renderBeforeOutline(WorldRenderContext context, HitResult hitResult, ClientPlayerEntity player, Hand hand) {
    // 只在使用主手且有权限时持有此物品时进行渲染。
    if (hand != Hand.MAIN_HAND || player.isSpectator() || !hasAccess(player, context.world(), true)) return;
    final ItemStack stack = player.getMainHandStack();
    final NbtCompound nbt = stack.getTag();
    final VertexConsumerProvider consumers = context.consumers();
    final MatrixStack matrices = context.matrixStack();
    if (consumers == null) return;
    final VertexConsumer vertexConsumer = consumers.getBuffer(RenderLayer.getLines());
    final Vec3d cameraPos = context.camera().getPos();
    if (hitResult.getType() == HitResult.Type.BLOCK && hasHoldingEntity(stack) && nbt != null) {
      final float width = nbt.getFloat("holdingEntityWidth");
      final float height = nbt.getFloat("holdingEntityHeight");
      final Vec3d pos = hitResult.getPos();
      WorldRendererInvoker.drawShapeOutline(matrices, vertexConsumer, VoxelShapes.cuboid(pos.x - width / 2, pos.y, pos.z - width / 2, pos.x + width / 2, pos.y + height, pos.z + width / 2), -cameraPos.x, -cameraPos.y, -cameraPos.z, 0, 1, 1, 0.8f);
    }
    if (!player.isCreative() && (hasHoldingBlockState(stack) || hasHoldingEntity(stack))) return;
    if (hitResult instanceof EntityHitResult) {
      final Entity entity = ((EntityHitResult) hitResult).getEntity();
      WorldRendererInvoker.drawShapeOutline(matrices, vertexConsumer, VoxelShapes.cuboid(entity.getBoundingBox()), -cameraPos.x, -cameraPos.y, -cameraPos.z, 1.0f, 0f, 0f, 0.8f);
    }
  }
}
