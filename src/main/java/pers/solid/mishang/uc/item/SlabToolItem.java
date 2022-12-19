package pers.solid.mishang.uc.item;

import com.google.common.collect.BiMap;
import com.google.common.collect.ImmutableBiMap;
import com.google.common.collect.Maps;
import it.unimi.dsi.fastutil.Pair;
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
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.ShapeContext;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.enums.SlabType;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.data.family.BlockFamilies;
import net.minecraft.data.family.BlockFamily;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.state.property.Properties;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.World;
import org.apache.commons.lang3.reflect.FieldUtils;
import org.apache.commons.lang3.reflect.MethodUtils;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import pers.solid.mishang.uc.Mishanguc;
import pers.solid.mishang.uc.block.AbstractRoadBlock;
import pers.solid.mishang.uc.blocks.RoadSlabBlocks;
import pers.solid.mishang.uc.mixin.WorldRendererInvoker;
import pers.solid.mishang.uc.render.RendersBlockOutline;
import pers.solid.mishang.uc.util.TextBridge;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * 用于处理台阶的工具。
 */
@EnvironmentInterface(value = EnvType.CLIENT, itf = RendersBlockOutline.class)
public class SlabToolItem extends Item implements RendersBlockOutline, ItemResourceGenerator {
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
  /**
   * @since 1.0.3 用于协调处理 canMine 与 performBreak。服务器不知道客户端的 crosshairTarget，需要由客户端发送。服务器先判断为允许挖掘，再根据这里面的内容还原该方块。
   */
  private static final Map<Pair<ServerWorld, BlockPos>, Runnable> SERVER_BLOCK_BREAKING_BRIDGE = new Object2ObjectOpenHashMap<>();

  public SlabToolItem(Settings settings) {
    super(settings);
  }

  /**
   * 将基础方块的方块状态转化为台阶方块，并尝试移植相应的方块状态属性。
   *
   * @param baseBlockState 基础方块的方块状态。
   * @param slabBlock      台阶方块，不是具体的方块状态。
   * @return 台阶方块的方块状态。
   */
  protected static BlockState toDoubleSlab(BlockState baseBlockState, Block slabBlock) {
    final BlockState slabState = slabBlock.getStateWithProperties(baseBlockState);
    return slabState.contains(Properties.SLAB_TYPE) ? slabState.with(Properties.SLAB_TYPE, SlabType.DOUBLE) : slabState;
  }

  /**
   * 尝试将 blockState 转化为双台阶。当它可以转化为双台阶，或者自身已经就是双台阶时，返回这个双台阶，否会返回 {@code null}。
   */
  protected static BlockState tryToDoubleSlab(BlockState state) {
    final Block block = state.getBlock();
    if (BLOCK_TO_SLAB.containsKey(block)) {
      state = toDoubleSlab(state, BLOCK_TO_SLAB.get(block));
    } else if (block instanceof AbstractRoadBlock && RoadSlabBlocks.BLOCK_TO_SLABS.containsKey(block)) {
      state = toDoubleSlab(state, RoadSlabBlocks.BLOCK_TO_SLABS.get(block));
    } else {
      final Block slab = ExtShapeBridge.getExtShapeSlabBlock(block);
      if (slab != null) {
        state = toDoubleSlab(state, slab);
      } else {
        // 尝试根据方块的 id 来判断对应的台阶方块。
        final Identifier id = Registry.BLOCK.getId(block);
        final String idPath = id.getPath();
        final Identifier slabId = new Identifier(id.getNamespace(), idPath + "_slab");
        if (Registry.BLOCK.containsId(slabId)) {
          state = toDoubleSlab(state, Registry.BLOCK.get(slabId));
        } else {
          final Identifier slabId2;
          if (idPath.endsWith("_bricks") || idPath.endsWith("_tiles")) {
            slabId2 = new Identifier(id.getNamespace(), idPath.substring(0, idPath.length() - 1) + "_slab");
          } else if (idPath.endsWith("_planks")) {
            slabId2 = new Identifier(id.getNamespace(), idPath.substring(0, idPath.length() - 7) + "_slab");
          } else {
            slabId2 = null;
          }
          if (slabId2 != null && Registry.BLOCK.containsId(slabId2)) {
            state = toDoubleSlab(state, Registry.BLOCK.get(slabId2));
          }
        }
      }
    }
    if (state.contains(Properties.SLAB_TYPE) && state.get(Properties.SLAB_TYPE) == SlabType.DOUBLE) {
      return state;
    } else {
      return null;
    }
  }

  private static boolean performBreak(World world, BlockPos pos, PlayerEntity miner, boolean isTop) {
    BlockState state = world.getBlockState(pos);
    final Block block = state.getBlock();
    final BlockState doubleSlabState = tryToDoubleSlab(state);
    if (doubleSlabState != null) {
      state = doubleSlabState;
    }
    if (state.contains(Properties.SLAB_TYPE) && state.get(Properties.SLAB_TYPE) == SlabType.DOUBLE) {
      final SlabType slabTypeToSet = isTop ? SlabType.BOTTOM : SlabType.TOP;
      final SlabType slabTypeBroken = isTop ? SlabType.TOP : SlabType.BOTTOM;
      // 破坏方块
      final BlockEntity blockEntity = world.getBlockEntity(pos);
      final NbtCompound nbt;
      if (blockEntity != null) {
        nbt = blockEntity.createNbt();
        world.removeBlockEntity(pos);
      } else {
        nbt = null;
      }
      final boolean bl1 = world.setBlockState(pos, state.with(Properties.SLAB_TYPE, slabTypeToSet));
      final BlockEntity newBlockEntity = world.getBlockEntity(pos);
      if (newBlockEntity != null && nbt != null) {
        newBlockEntity.readNbt(nbt);
      }
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

  @Override
  public void appendTooltip(
      ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
    super.appendTooltip(stack, world, tooltip, context);
    tooltip.add(TextBridge.translatable("item.mishanguc.slab_tool.tooltip").formatted(Formatting.GRAY));
  }

  /**
   * 破坏台阶的一部分。
   *
   * @see Handler#receive
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
      final Runnable remove = SERVER_BLOCK_BREAKING_BRIDGE.remove(Pair.of(world, pos));
      if (remove instanceof PacketReceivedFirst) {
        // 执行从封包的 receive 中推迟过来的。
        remove.run();
        return false;
      } else {
        // 服务器还没有执行 performBreak。可能它根本就不是台阶，也有可能是本来就在 canMine 完成之后再执行 performBreak。
        final boolean b = tryToDoubleSlab(state) == null;
        if (remove == null && !b) SERVER_BLOCK_BREAKING_BRIDGE.put(Pair.of((ServerWorld) world, pos), CAN_MINE_CALLED_FIRST);
        return b;
      }
    }
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
    BlockState state = blockOutlineContext.blockState();
    final HitResult crosshairTarget = MinecraftClient.getInstance().crosshairTarget;
    if (!(crosshairTarget instanceof final BlockHitResult blockHitResult)) {
      return true;
    }
    boolean isTop = crosshairTarget.getPos().y - (double) blockHitResult.getBlockPos().getY() > 0.5D;
    state = tryToDoubleSlab(state);
    if (state != null) {
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

    /**
     * @see #canMine(BlockState, World, BlockPos, PlayerEntity)
     */
    @Override
    public void receive(MinecraftServer server, ServerPlayerEntity player, ServerPlayNetworkHandler handler, PacketByteBuf buf, PacketSender responseSender) {
      final BlockPos blockPos = buf.readBlockPos();
      final boolean isTop = buf.readBoolean();
      server.execute(() -> {
        if (player.getEyePos().squaredDistanceTo(Vec3d.ofCenter(blockPos)) > 36) {
          return;
        }
        if (!(player.getMainHandStack().getItem() instanceof SlabToolItem) || !player.getAbilities().allowModifyWorld) {
          return;
        }
        final Runnable remove = SERVER_BLOCK_BREAKING_BRIDGE.remove(Pair.of(player.getWorld(), blockPos));
        if (remove == CAN_MINE_CALLED_FIRST) {
          performBreak(player.world, blockPos, player, isTop);
        } else if (tryToDoubleSlab(player.world.getBlockState(blockPos)) != null) {
          // 收到封包之后，送到 canMine 中执行。
          SERVER_BLOCK_BREAKING_BRIDGE.put(Pair.of(player.getWorld(), blockPos), (PacketReceivedFirst) () -> performBreak(player.world, blockPos, player, isTop));
        }
      });
    }
  }

  private interface PacketReceivedFirst extends Runnable {
  }

  private static final Runnable CAN_MINE_CALLED_FIRST = () -> {
  };

  @ApiStatus.AvailableSince("1.0.4")
  private static final class ExtShapeBridge {
    private static final Class<?> extshape_BlockMappings_class;
    private static final Method extshape_getBlockOf_method;
    private static final Object extshape_slab_shape;

    static {
      Object extshape_slab_shape1 = null;
      Method extshape_getBlockOf_method1 = null;
      Class<?> extshape_BlockMappings_class1 = null;
      Class<?> extshape_BlockShape_class;

      if (FabricLoader.getInstance().isModLoaded("extshape")) try {
        extshape_BlockMappings_class1 = Class.forName("pers.solid.extshape.util.BlockBiMaps");
        extshape_BlockShape_class = Class.forName("pers.solid.extshape.builder.BlockShape");
        extshape_getBlockOf_method1 = MethodUtils.getAccessibleMethod(extshape_BlockMappings_class1, "getBlockOf", extshape_BlockShape_class, Block.class);
        extshape_slab_shape1 = FieldUtils.getDeclaredField(extshape_BlockShape_class, "SLAB").get(null);
      } catch (Throwable e) {
        extshape_BlockMappings_class1 = null;
        extshape_getBlockOf_method1 = null;
        if (!(e instanceof ClassNotFoundException || e instanceof ClassCastException)) {
          Mishanguc.MISHANG_LOGGER.error("Unknown exception when trying to connect with Extended Block Shape mod:", e);
        }
      }
      extshape_slab_shape = extshape_slab_shape1;
      extshape_getBlockOf_method = extshape_getBlockOf_method1;
      extshape_BlockMappings_class = extshape_BlockMappings_class1;
      if (extshape_slab_shape != null && extshape_getBlockOf_method != null) {
        Mishanguc.MISHANG_LOGGER.info("Mishang Urban Construction mod has successfully created bridged into Extended Block Shapes mod!");
      }
    }

    public static @Nullable Block getExtShapeSlabBlock(Block baseBlock) {
      if (extshape_BlockMappings_class == null || extshape_getBlockOf_method == null || extshape_slab_shape == null) {
        return null;
      }
      try {
        return (Block) extshape_getBlockOf_method.invoke(null, extshape_slab_shape, baseBlock);
      } catch (IllegalAccessException | InvocationTargetException | IllegalArgumentException | ClassCastException e) {
        Mishanguc.MISHANG_LOGGER.error("Unexpected error when trying to get slab of block {}. This should not happen no matter whether you have installed Mishang Urban Construction mod.", baseBlock, e);
        return null;
      }
    }
  }
}
