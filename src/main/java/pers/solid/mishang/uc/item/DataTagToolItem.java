package pers.solid.mishang.uc.item;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.api.EnvironmentInterface;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderContext;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.block.Block;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.command.BlockDataObject;
import net.minecraft.command.EntityDataObject;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Formatting;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.World;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import pers.solid.mishang.uc.mixin.WorldRendererInvoker;
import pers.solid.mishang.uc.render.RendersBeforeOutline;
import pers.solid.mishang.uc.util.NbtPrettyPrinter;

import java.util.List;

@EnvironmentInterface(value = EnvType.CLIENT, itf = RendersBeforeOutline.class)
public class DataTagToolItem extends BlockToolItem implements InteractsWithEntity, RendersBeforeOutline {
  public DataTagToolItem(Settings settings, @Nullable Boolean includesFluid) {
    super(settings, includesFluid);
  }

  @Environment(EnvType.CLIENT)
  @Override
  public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
    super.appendTooltip(stack, world, tooltip, context);
    tooltip.add(new TranslatableText("item.mishanguc.data_tag_tool.tooltip").formatted(Formatting.GRAY));
  }

  @Override
  public ActionResult useOnBlock(
      PlayerEntity player,
      World world,
      BlockHitResult blockHitResult,
      Hand hand,
      boolean fluidIncluded) {
    if (!world.isClient) {
      return getBlockDataOf((ServerPlayerEntity) player, (ServerWorld) world, blockHitResult.getBlockPos());
    } else {
      return ActionResult.SUCCESS;
    }
  }

  @Override
  public ActionResult beginAttackBlock(
      PlayerEntity player, World world, Hand hand, BlockPos pos, Direction direction, boolean fluidIncluded) {
    if (!world.isClient) return getBlockDataOf((ServerPlayerEntity) player, (ServerWorld) world, pos);
    else return ActionResult.SUCCESS;
  }

  public ActionResult getBlockDataOf(ServerPlayerEntity player, ServerWorld world, BlockPos blockPos) {
    final @Nullable BlockEntity blockEntity = world.getBlockEntity(blockPos);
    final PacketByteBuf buf = PacketByteBufs.create();
    buf.writeIdentifier(Registry.BLOCK.getId(world.getBlockState(blockPos).getBlock()));
    buf.writeBlockPos(blockPos);
    if (blockEntity == null) {
      buf.writeBoolean(false);
      ServerPlayNetworking.send(player, new Identifier("mishanguc", "get_block_data"), buf);
    } else {
      final BlockDataObject blockDataObject = new BlockDataObject(world.getBlockEntity(blockPos), blockPos);
      buf.writeBoolean(true);
      buf.writeNbt(blockDataObject.getNbt());
      ServerPlayNetworking.send(player, new Identifier("mishanguc", "get_block_data"), buf);
    }
    return ActionResult.SUCCESS;
  }

  public ActionResult getEntityDataOf(ServerPlayerEntity player, Entity entity) {
    final EntityDataObject entityDataObject = new EntityDataObject(entity);
    final NbtCompound nbt = entityDataObject.getNbt();
    final PacketByteBuf buf = PacketByteBufs.create();
    buf.writeText(entity.getName());
    buf.writeBlockPos(entity.getBlockPos());
    buf.writeNbt(nbt);
    ServerPlayNetworking.send(player, new Identifier("mishanguc", "get_entity_data"), buf);
    return ActionResult.SUCCESS;
  }

  @Override
  public @NotNull ActionResult attackEntityCallback(
      PlayerEntity player,
      World world,
      Hand hand,
      Entity entity,
      @Nullable EntityHitResult hitResult) {
    if (player.isSpectator()) return ActionResult.PASS;
    else if (!world.isClient) return getEntityDataOf((ServerPlayerEntity) player, entity);
    else return ActionResult.SUCCESS;
  }

  @Override
  public @NotNull ActionResult useEntityCallback(
      PlayerEntity player,
      World world,
      Hand hand,
      Entity entity,
      @Nullable EntityHitResult hitResult) {
    if (!world.isClient && !player.isSpectator()) return getEntityDataOf((ServerPlayerEntity) player, entity);
    else return ActionResult.SUCCESS;
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

  /**
   * 用于接收服务器的 {@code mishanguc:get_block_data} 的数据包。用户使用该工具点击方块后，服务器获取其数据并传给客户端，客户端收到数据后，将消息反馈至聊天框。
   */
  @Environment(EnvType.CLIENT)
  @ApiStatus.AvailableSince("0.1.7")
  public static class BlockDataReceiver implements ClientPlayNetworking.PlayChannelHandler {
    @Override
    public void receive(MinecraftClient client, ClientPlayNetworkHandler handler, PacketByteBuf buf, PacketSender responseSender) {
      final Identifier blockId = buf.readIdentifier();
      final BlockPos blockPos = buf.readBlockPos();
      final boolean hasData = buf.readBoolean();
      final Block block = Registry.BLOCK.get(blockId);
      if (hasData) {
        // 由于此处仅限客户端执行，因此可以放心调用 Block#getName。
        final NbtCompound blockData = buf.readNbt();
        client.execute(() -> {
          client.inGameHud.getChatHud().addMessage(
              new TranslatableText(
                  "debug.mishanguc.dataTag.block.header",
                  String.format("%s %s %s", blockPos.getX(), blockPos.getY(), blockPos.getZ()),
                  block.getName().formatted(Formatting.BOLD))
                  .formatted(Formatting.YELLOW));
          client.inGameHud.getChatHud().addMessage(NbtPrettyPrinter.serialize(blockData));
        });
      } else {
        // 此时认为该方块没有数据。
        client.execute(() -> client.inGameHud.getChatHud().addMessage(
            new TranslatableText(
                "debug.mishanguc.dataTag.block.null",
                String.format("%s %s %s", blockPos.getX(), blockPos.getY(), blockPos.getZ()),
                block.getName().formatted(Formatting.BOLD))
                .formatted(Formatting.RED)));
      }
    }
  }

  /**
   * 用于接收服务器的 {@code mishanguc:get_entity_data} 数据包。用户使用该工具点击实体后，服务器获取其数据并传给客户端，客户端收到数据后，将消息反馈至聊天框。
   */
  @Environment(EnvType.CLIENT)
  @ApiStatus.AvailableSince("0.1.7")
  public static class EntityDataReceiver implements ClientPlayNetworking.PlayChannelHandler {
    @Override
    public void receive(MinecraftClient client, ClientPlayNetworkHandler handler, PacketByteBuf buf, PacketSender responseSender) {
      final Text entityName = buf.readText();
      final BlockPos entityPos = buf.readBlockPos();
      final NbtCompound entityNbt = buf.readNbt();
      client.inGameHud.getChatHud().addMessage(new TranslatableText(
          "debug.mishanguc.dataTag.entity.entity",
          String.format(
              "%s %s %s", entityPos.getX(), entityPos.getY(), entityPos.getZ()),
          new LiteralText("").append(entityName).formatted(Formatting.BOLD))
          .formatted(Formatting.YELLOW));
      client.inGameHud.getChatHud().addMessage(NbtPrettyPrinter.serialize(entityNbt));
    }
  }
}
