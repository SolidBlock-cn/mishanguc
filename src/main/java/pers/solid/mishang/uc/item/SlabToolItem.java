package pers.solid.mishang.uc.item;

import com.google.common.collect.ImmutableBiMap;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import net.devtech.arrp.generator.ItemResourceGenerator;
import net.devtech.arrp.json.models.JModel;
import net.devtech.arrp.json.recipe.JRecipe;
import net.devtech.arrp.json.recipe.JShapedRecipe;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.api.EnvironmentInterface;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderContext;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.ShapeContext;
import net.minecraft.block.enums.SlabType;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.state.property.Properties;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import pers.solid.mishang.uc.block.AbstractRoadBlock;
import pers.solid.mishang.uc.blocks.RoadSlabBlocks;
import pers.solid.mishang.uc.mixin.WorldRendererInvoker;
import pers.solid.mishang.uc.render.RendersBlockOutline;

import java.util.Map;
import java.util.Objects;

/**
 * 用于处理台阶的工具。
 */
@EnvironmentInterface(value = EnvType.CLIENT, itf = RendersBlockOutline.class)
public class SlabToolItem extends Item implements RendersBlockOutline, ItemResourceGenerator {
  private static final Map<Pair<ServerWorld, BlockPos>, Runnable> SERVER_PERFORM_BREAK_WAITING = new Object2ObjectOpenHashMap<>();
  /**
   * 从原版的 {@link BlockFamilies} 提取的方块至台阶方块的映射。
   */
  @ApiStatus.AvailableSince("0.1.3")
  protected static final BiMap<Block, Block> BLOCK_TO_SLAB = BlockFamilies.getFamilies()
      .filter(blockFamily -> blockFamily.getVariant(BlockFamily.Variant.SLAB) != null)
      .map(blockFamily -> {
        final Block variant = blockFamily.getVariant(BlockFamily.Variant.SLAB);
        final Block baseBlock = blockFamily.getBaseBlock();
        return baseBlock == null || variant == null ? null : Maps.immutableEntry(baseBlock, variant);
      })
      .filter(Objects::nonNull)
      .collect(ImmutableBiMap.toImmutableBiMap(Map.Entry::getKey, Map.Entry::getValue));

  public SlabToolItem(Settings settings) {
    super(settings);
  }

  /**
   * 破坏台阶的一部分。
   *
   * @see Item#canMine
   */
  @Override
  public boolean canMine(BlockState state, World world, BlockPos pos, PlayerEntity miner) {
    // 处理双台阶的情况。
    if (world.isClient && miner instanceof ClientPlayerEntity) {
      final HitResult raycast = MinecraftClient.getInstance().crosshairTarget;
      if (!(raycast instanceof BlockHitResult) || raycast.getType() == HitResult.Type.MISS) return false;
      boolean isTop = raycast.getPos().y - (double) ((BlockHitResult) raycast).getBlockPos().getY() > 0.5D;
      final boolean bl1 = performBreak(world, pos, miner, isTop);
      final PacketByteBuf buf = PacketByteBufs.create();
      buf.writeBlockPos(pos);
      buf.writeBoolean(isTop);
      ClientPlayNetworking.send(new Identifier("mishanguc", "slab_tool"), buf);
      return !bl1;
    } else {
      // 注意：需要考虑这样的情况：
      // 客户端使用工具破坏方块后，发送 mishanguc:slab_tool 的 packet 到服务器
      // 服务器收到 packet 之后，执行 performBreak，然后再收到原版 packet，执行此处的 canMine，得出不准确的结果。
      // 因此，需要确保服务器上的 canMine 在 performBreak 之前执行。
      final Runnable remove = SERVER_PERFORM_BREAK_WAITING.remove(Pair.of(((ServerWorld) miner.getWorld()), pos));
      if (remove != null) {
        // 服务器已经处理了封包，将 performBreak 推迟到 canMine 之后执行。
        remove.run();
        return false;
      } else {
        // 服务器还没有执行 performBreak。可能它根本就不是台阶。
        return true;
      }
    }
  }

  private static boolean performBreak(World world, BlockPos pos, PlayerEntity miner, boolean isTop) {
    BlockState state = world.getBlockState(pos);
    final Block block = state.getBlock();
    if (state.contains(Properties.SLAB_TYPE) && state.get(Properties.SLAB_TYPE) == SlabType.DOUBLE) {
      final SlabType slabTypeToSet = isTop ? SlabType.BOTTOM : SlabType.TOP;
      final SlabType slabTypeBroken = isTop ? SlabType.TOP : SlabType.BOTTOM;
      // 破坏上半砖的情况。
      final boolean bl1 = world.setBlockState(pos, state.with(Properties.SLAB_TYPE, slabTypeToSet));
      final BlockState brokenState = state.with(Properties.SLAB_TYPE, slabTypeBroken);
      block.onBreak(world, pos, brokenState, miner);
      if (bl1) {
        block.onBroken(world, pos, brokenState);
        if (!miner.isCreative()) {
          block.afterBreak(world, miner, pos, brokenState, world.getBlockEntity(pos), miner.getMainHandStack().copy());
        }
        miner.getStackInHand(Hand.MAIN_HAND).damage(1, miner, player -> player.sendToolBreakStatus(Hand.MAIN_HAND));
      }
      return bl1;
    }
    return false;
  }

  @Environment(EnvType.CLIENT)
  @Override
  public boolean renderBlockOutline(
      PlayerEntity player,
      ItemStack itemStack,
      WorldRenderContext worldRenderContext,
      WorldRenderContext.BlockOutlineContext blockOutlineContext, Hand hand) {
    final VertexConsumerProvider consumers = worldRenderContext.consumers();
    if (consumers == null || hand != Hand.MAIN_HAND) return true;
    final ClientWorld world = worldRenderContext.world();
    final BlockState state = blockOutlineContext.blockState();
    if (state.contains(Properties.SLAB_TYPE)
        && state.get(Properties.SLAB_TYPE) == SlabType.DOUBLE) {
      final HitResult crosshairTarget = MinecraftClient.getInstance().crosshairTarget;
      if (!(crosshairTarget instanceof BlockHitResult)) {
        return true;
      }
      boolean isTop =
          crosshairTarget.getPos().y
              - (double) ((BlockHitResult) crosshairTarget).getBlockPos().getY()
              > 0.5D;
      // 渲染时需要使用的方块状态。
      final BlockState halfState =
          state.with(Properties.SLAB_TYPE, isTop ? SlabType.TOP : SlabType.BOTTOM);
      final BlockPos blockPos = blockOutlineContext.blockPos();
      WorldRendererInvoker.drawShapeOutline(
          worldRenderContext.matrixStack(),
          consumers.getBuffer(RenderLayer.LINES),
          halfState.getOutlineShape(world, blockPos, ShapeContext.of(blockOutlineContext.entity())),
          (double) blockPos.getX() - blockOutlineContext.cameraX(),
          (double) blockPos.getY() - blockOutlineContext.cameraY(),
          (double) blockPos.getZ() - blockOutlineContext.cameraZ(),
          0.0F,
          0.0F,
          0.0F,
          0.4F);
      return false;
    }
    return true;
  }

  @Environment(EnvType.CLIENT)
  @Override
  public @Nullable JModel getItemModel() {
    return null;
  }

  @Override
  public @NotNull JRecipe getCraftingRecipe() {
    return new JShapedRecipe(this)
        .pattern("SCS", " | ", " | ")
        .addKey("S", Items.SHEARS)
        .addKey("C", Items.STONE)
        .addKey("|", Items.STICK)
        .addInventoryChangedCriterion("has_shears", Items.SHEARS)
        .addInventoryChangedCriterion("has_stone", Items.STONE);
  }

  @ApiStatus.AvailableSince("1.0.3")
  public enum Handler implements ServerPlayNetworking.PlayChannelHandler {
    INSTANCE;

    @Override
    public void receive(MinecraftServer server, ServerPlayerEntity player, ServerPlayNetworkHandler handler, PacketByteBuf buf, PacketSender responseSender) {
      final BlockPos blockPos = buf.readBlockPos();
      final boolean isTop = buf.readBoolean();
      server.execute(() -> {
        if (player.getEyePos().squaredDistanceTo(Vec3d.ofCenter(blockPos)) > 36) {
          return;
        }
        BlockState state = player.world.getBlockState(blockPos);
        final Block block = state.getBlock();
        if (BLOCK_TO_SLAB.containsKey(block)) {
          state = toSlab(state, BLOCK_TO_SLAB.get(block));
        } else if (block instanceof AbstractRoadBlock && RoadSlabBlocks.BLOCK_TO_SLABS.containsKey(block)) {
          state = toSlab(state, RoadSlabBlocks.BLOCK_TO_SLABS.get(block));
        }
        if (state.contains(Properties.SLAB_TYPE) && state.get(Properties.SLAB_TYPE) == SlabType.DOUBLE) {
          // canMine 还没有执行，需要放到 canMine 后面执行。
          SERVER_PERFORM_BREAK_WAITING.put(Pair.of(player.getWorld(), blockPos), () -> performBreak(player.world, blockPos, player, isTop));
        } else {
          // canMine 已经执行，此时直接执行。
          performBreak(player.world, blockPos, player, isTop);
        }
      });
    }
  }
}
