package pers.solid.mishang.uc.item;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.api.EnvironmentInterface;
import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderContext;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.util.*;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import pers.solid.mishang.uc.mixin.WorldRendererInvoker;
import pers.solid.mishang.uc.render.RendersBeforeOutline;
import pers.solid.mishang.uc.util.TextBridge;

import java.util.List;

@EnvironmentInterface(value = EnvType.CLIENT, itf = RendersBeforeOutline.class)
public class IdCheckerToolItem extends BlockToolItem implements InteractsWithEntity, RendersBeforeOutline {
  public IdCheckerToolItem(Settings settings, @Nullable Boolean includesFluid) {
    super(settings, includesFluid);
  }

  public ActionResult getIdOf(PlayerEntity player, World world, BlockPos blockPos) {
    BlockState blockState = world.getBlockState(blockPos);
    if (player != null) {
      final Block block = blockState.getBlock();
      final Identifier identifier = Registry.BLOCK.getId(block);
      final int rawId = Registry.BLOCK.getRawId(block);
      player.sendMessage(
          TextBridge.literal("")
              .append(TextBridge.translatable("debug.mishanguc.blockId.header", String.format(
                      "%s %s %s", blockPos.getX(), blockPos.getY(), blockPos.getZ()))
                  .formatted(Formatting.YELLOW)), false);
      broadcastId(player, block.getName(), identifier, rawId);
      return ActionResult.SUCCESS;
    }
    return ActionResult.SUCCESS;
  }

  /**
   * 发送一个方块、实体或其他事物的id。
   */
  private void broadcastId(
      PlayerEntity player, Text name, @Nullable Identifier identifier, int rawId) {
    player.sendMessage(
        TextBridge.literal("  ").append(TextBridge.translatable("debug.mishanguc.id.name", name))
            .append("\n  ")
            .append(TextBridge.translatable("debug.mishanguc.id.id", identifier == null
                ? TextBridge.translatable("gui.none")
                : TextBridge.literal(identifier.toString())))
            .append("\n  ")
            .append(TextBridge.translatable("debug.mishanguc.id.rawId", TextBridge.literal(Integer.toString(rawId)))), false);
  }

  @Override
  public ActionResult useOnBlock(
      ItemStack stack, PlayerEntity player,
      World world,
      BlockHitResult blockHitResult,
      Hand hand,
      boolean fluidIncluded) {
    if (world.isClient) return getIdOf(player, world, blockHitResult.getBlockPos());
    else return ActionResult.SUCCESS;
  }

  @Override
  public ActionResult beginAttackBlock(
      ItemStack stack, PlayerEntity player, World world, Hand hand, BlockPos pos, Direction direction, boolean fluidIncluded) {
    if (world.isClient) return getIdOf(player, world, pos);
    else return ActionResult.SUCCESS;
  }

  @Override
  public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
    if (world.isClient) {
      final BlockPos blockPos = user.getBlockPos();
      final Biome biome = user.getEntityWorld().getBiome(blockPos).value();
      final Registry<Biome> biomes = world.getRegistryManager().get(Registry.BIOME_KEY);
      final Identifier identifier = biomes.getId(biome);
      final int rawId = biomes.getRawId(biome);
      user.sendMessage(
          TextBridge.literal("").append(
              TextBridge.translatable("debug.mishanguc.biomeId.header", String.format(
                      "%s %s %s", blockPos.getX(), blockPos.getY(), blockPos.getZ()))
                  .formatted(Formatting.YELLOW)), false);
      broadcastId(
          user,
          TextBridge.translatable(Util.createTranslationKey("biome", identifier)),
          identifier,
          rawId);
    }
    return super.use(world, user, hand);
  }

  @Override
  public void appendTooltip(
      ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
    super.appendTooltip(stack, world, tooltip, context);
    tooltip.add(
        TextBridge.translatable("item.mishanguc.id_checker_tool.tooltip.1")
            .formatted(Formatting.GRAY));
    final @Nullable Boolean includesFluid = includesFluid(stack);
    if (includesFluid == null) {
      tooltip.add(
          TextBridge.translatable("item.mishanguc.id_checker_tool.tooltip.2")
              .formatted(Formatting.GRAY));
    } else if (includesFluid) {
      tooltip.add(
          TextBridge.translatable("item.mishanguc.id_checker_tool.tooltip.3")
              .formatted(Formatting.GRAY));
    }
  }

  @Override
  public @NotNull ActionResult attackEntityCallback(
      PlayerEntity player,
      World world,
      Hand hand,
      Entity entity,
      @Nullable EntityHitResult hitResult) {
    return useEntityCallback(player, world, hand, entity, hitResult);
  }

  @Override
  public @NotNull ActionResult useEntityCallback(
      PlayerEntity player,
      World world,
      Hand hand,
      Entity entity,
      @Nullable EntityHitResult hitResult) {
    if (player.isSpectator()) return ActionResult.PASS;
    if (!world.isClient) return ActionResult.SUCCESS;
    final BlockPos blockPos = entity.getBlockPos();
    player.sendMessage(
        TextBridge.literal("").append(
            TextBridge.translatable("debug.mishanguc.entityId.header", String.format(
                    "%s %s %s", blockPos.getX(), blockPos.getY(), blockPos.getZ()))
                .formatted(Formatting.YELLOW)), false);
    final EntityType<?> type = entity.getType();
    broadcastId(
        player,
        entity.getName(),
        Registry.ENTITY_TYPE.getId(type),
        Registry.ENTITY_TYPE.getRawId(type));
    return ActionResult.SUCCESS;
  }

  @Environment(EnvType.CLIENT)
  @Override
  public void renderBeforeOutline(WorldRenderContext context, HitResult hitResult, ClientPlayerEntity player, Hand hand) {
    if (hitResult instanceof EntityHitResult entityHitResult && !player.isSpectator()) {
      final Entity entity = entityHitResult.getEntity();
      final MatrixStack matrices = context.matrixStack();
      final VertexConsumerProvider consumers = context.consumers();
      if (consumers == null) return;
      final VertexConsumer vertexConsumer = consumers.getBuffer(RenderLayer.getLines());
      final Vec3d cameraPos = context.camera().getPos();
      WorldRendererInvoker.drawShapeOutline(matrices, vertexConsumer, VoxelShapes.cuboid(entity.getBoundingBox()), -cameraPos.x, -cameraPos.y, -cameraPos.z, 0f, 1f, 0f, 0.8f);
    }
  }
}
