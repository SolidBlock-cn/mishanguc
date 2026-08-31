package pers.solid.mishang.uc.item;

import com.google.common.collect.BiMap;
import com.google.common.collect.ImmutableBiMap;
import com.google.common.collect.Maps;
import it.unimi.dsi.fastutil.Pair;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.api.EnvironmentInterface;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.client.rendering.v1.level.LevelExtractionContext;
import net.fabricmc.fabric.api.client.rendering.v1.level.LevelRenderContext;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.fabricmc.fabric.api.tag.convention.v2.ConventionalItemTags;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.ShapeRenderer;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.client.renderer.state.level.BlockOutlineRenderState;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.BlockFamilies;
import net.minecraft.data.BlockFamily;
import net.minecraft.data.recipes.RecipeBuilder;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.ARGB;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.TypedEntityData;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.pattern.BlockInWorld;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.SlabType;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;
import org.apache.commons.lang3.reflect.FieldUtils;
import org.apache.commons.lang3.reflect.MethodUtils;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Nullable;
import pers.solid.mishang.uc.Mishanguc;
import pers.solid.mishang.uc.block.AbstractRoadBlock;
import pers.solid.mishang.uc.blocks.RoadSlabBlocks;
import pers.solid.mishang.uc.networking.SlabToolPayload;
import pers.solid.mishang.uc.render.RendersBlockOutline;
import pers.solid.mishang.uc.render.state.MishangRenderState;
import pers.solid.mishang.uc.render.state.SlabToolState;
import pers.solid.mishang.uc.util.WithMishangTooltip;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * 用于处理台阶的工具。
 */
@EnvironmentInterface(value = EnvType.CLIENT, itf = RendersBlockOutline.class)
public class SlabToolItem extends Item implements RendersBlockOutline, MishangucItem, WithMishangTooltip {
  /**
   * 从原版的 {@link BlockFamilies} 提取的方块至台阶方块的映射。
   */
  @ApiStatus.AvailableSince("0.1.3")
  protected static final BiMap<Block, Block> BLOCK_TO_SLAB = BlockFamilies.getAllFamilies()
      .filter(blockFamily -> blockFamily.get(BlockFamily.Variant.SLAB) != null)
      .map(blockFamily -> {
        final Block variant = blockFamily.get(BlockFamily.Variant.SLAB);
        final Block baseBlock = blockFamily.getBaseBlock();
        return baseBlock == null || variant == null ? null : Maps.immutableEntry(baseBlock, variant);
      })
      .filter(Objects::nonNull)
      .collect(ImmutableBiMap.toImmutableBiMap(Map.Entry::getKey, Map.Entry::getValue));
  /**
   * @since 1.0.3 用于协调处理 canMine 与 performBreak。服务器不知道客户端的 crosshairTarget，需要由客户端发送。服务器先判断为允许挖掘，再根据这里面的内容还原该方块。
   */
  private static final Map<Pair<ServerLevel, BlockPos>, Runnable> SERVER_BLOCK_BREAKING_BRIDGE = new Object2ObjectOpenHashMap<>();
  private static final int OUTLINE_COLOR_TRANSLUCENT_BLACK = ARGB.colorFromFloat(0.4f, 0.0F, 0.0F, 0.0F);

  public SlabToolItem(Properties settings) {
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
    final BlockState slabState = slabBlock.withPropertiesOf(baseBlockState);
    return slabState.hasProperty(BlockStateProperties.SLAB_TYPE) ? slabState.setValue(BlockStateProperties.SLAB_TYPE, SlabType.DOUBLE) : slabState;
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
        final Identifier id = BuiltInRegistries.BLOCK.getKey(block);
        final String idPath = id.getPath();
        final Identifier slabId = Identifier.fromNamespaceAndPath(id.getNamespace(), idPath + "_slab");
        if (BuiltInRegistries.BLOCK.containsKey(slabId)) {
          state = toDoubleSlab(state, BuiltInRegistries.BLOCK.getValue(slabId));
        } else {
          final Identifier slabId2;
          if (idPath.endsWith("_bricks") || idPath.endsWith("_tiles")) {
            slabId2 = Identifier.fromNamespaceAndPath(id.getNamespace(), idPath.substring(0, idPath.length() - 1) + "_slab");
          } else if (idPath.endsWith("_planks")) {
            slabId2 = Identifier.fromNamespaceAndPath(id.getNamespace(), idPath.substring(0, idPath.length() - 7) + "_slab");
          } else {
            slabId2 = null;
          }
          if (slabId2 != null && BuiltInRegistries.BLOCK.containsKey(slabId2)) {
            state = toDoubleSlab(state, BuiltInRegistries.BLOCK.getValue(slabId2));
          }
        }
      }
    }
    if (state.hasProperty(BlockStateProperties.SLAB_TYPE) && state.getValue(BlockStateProperties.SLAB_TYPE) == SlabType.DOUBLE) {
      return state;
    } else {
      return null;
    }
  }

  private static boolean performBreak(Level world, BlockPos pos, Player user, boolean isTop) {
    BlockState state = world.getBlockState(pos);
    final Block block = state.getBlock();
    final BlockState doubleSlabState = tryToDoubleSlab(state);
    if (doubleSlabState != null) {
      state = doubleSlabState;
    }
    if (state.hasProperty(BlockStateProperties.SLAB_TYPE) && state.getValue(BlockStateProperties.SLAB_TYPE) == SlabType.DOUBLE) {
      final SlabType slabTypeToSet = isTop ? SlabType.BOTTOM : SlabType.TOP;
      final SlabType slabTypeBroken = isTop ? SlabType.TOP : SlabType.BOTTOM;
      // 破坏方块
      final BlockEntity blockEntity = world.getBlockEntity(pos);
      final CompoundTag nbt;
      if (blockEntity != null) {
        nbt = blockEntity.saveWithoutMetadata(world.registryAccess());
        world.removeBlockEntity(pos);
      } else {
        nbt = null;
      }
      final boolean bl1 = world.setBlockAndUpdate(pos, state.setValue(BlockStateProperties.SLAB_TYPE, slabTypeToSet));
      final BlockEntity newBlockEntity = world.getBlockEntity(pos);
      if (newBlockEntity != null && nbt != null) {
        TypedEntityData.of(newBlockEntity, nbt).loadInto(newBlockEntity, world.registryAccess());
      }
      final BlockState brokenState = state.setValue(BlockStateProperties.SLAB_TYPE, slabTypeBroken);
      block.playerWillDestroy(world, pos, brokenState, user);
      if (bl1) {
        block.destroy(world, pos, brokenState);
        if (!user.isCreative()) {
          block.playerDestroy(world, user, pos, brokenState, world.getBlockEntity(pos), user.getMainHandItem().copy());
        }
        user.getItemInHand(InteractionHand.MAIN_HAND).hurtAndBreak(1, user, EquipmentSlot.MAINHAND);
      }
      return bl1;
    }
    return false;
  }

  @Override
  public void getMishangTooltip(ItemStack stack, TooltipContext context, List<Component> tooltip, TooltipFlag options) {
    tooltip.add(Component.translatable("item.mishanguc.slab_tool.tooltip").withStyle(ChatFormatting.GRAY));
  }

  /**
   * 破坏台阶的一部分。
   *
   * @see Handler#receive
   */


  @Override
  public boolean canDestroyBlock(ItemStack stack, BlockState state, Level world, BlockPos pos, LivingEntity user) {
    // 处理双台阶的情况。
    if (world.isClientSide() && user instanceof LocalPlayer) {
      final HitResult raycast = Minecraft.getInstance().hitResult;
      if (!(raycast instanceof BlockHitResult) || raycast.getType() == HitResult.Type.MISS) return false;
      boolean isTop = raycast.getLocation().y - (double) ((BlockHitResult) raycast).getBlockPos().getY() > 0.5D;
      final boolean bl1 = performBreak(world, pos, ((Player) user), isTop);
      ClientPlayNetworking.send(new SlabToolPayload(pos, isTop));
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
        if (remove == null && !b) SERVER_BLOCK_BREAKING_BRIDGE.put(Pair.of((ServerLevel) world, pos), CAN_MINE_CALLED_FIRST);
        return b;
      }
    }
  }

  @Environment(EnvType.CLIENT)
  @Override
  public @Nullable MishangRenderState getMishangRenderState(LocalPlayer player, InteractionHand hand, ItemStack stack, LevelExtractionContext context, @Nullable HitResult result) {
    if (!(result instanceof final BlockHitResult blockHitResult) || hand != InteractionHand.MAIN_HAND) {
      return null;
    }

    boolean isTop = result.getLocation().y - (double) blockHitResult.getBlockPos().getY() > 0.5D;
    BlockState blockState = context.level().getBlockState(blockHitResult.getBlockPos());
    blockState = tryToDoubleSlab(blockState);
    if (blockState != null) {
      final SlabToolState state = new SlabToolState();
      // 渲染时需要使用的方块状态。
      final BlockState halfState = blockState.setValue(BlockStateProperties.SLAB_TYPE, isTop ? SlabType.TOP : SlabType.BOTTOM);
      state.slabShape = halfState.getShape(context.level(), blockHitResult.getBlockPos(), CollisionContext.of(player));
      return state;
    } else {
      return null;
    }
  }

  @Environment(EnvType.CLIENT)
  @Override
  public boolean renderBlockOutline(Player player, ItemStack itemStack, LevelRenderContext context, BlockOutlineRenderState outlineRenderState) {
    if (!(context.levelState().getData(MISHANG_BLOCK_OUTLINE) instanceof SlabToolState state)) {
      return true;
    }
    final MultiBufferSource consumers = context.bufferSource();
    if (state.slabShape != null) {
      final BlockPos pos = outlineRenderState.pos();
      final Vec3 cameraPos = context.levelState().cameraRenderState.pos;
      ShapeRenderer.renderShape(
          context.poseStack(),
          consumers.getBuffer(RenderTypes.LINES),
          state.slabShape,
          (double) pos.getX() - cameraPos.x(),
          (double) pos.getY() - cameraPos.y(),
          (double) pos.getZ() - cameraPos.z(),
          OUTLINE_COLOR_TRANSLUCENT_BLACK,
          Minecraft.getInstance().getWindow().getAppropriateLineWidth());
      return false;
    }
    return true;
  }

  @Override
  public RecipeBuilder getCraftingRecipe(RecipeProvider recipeGenerator) {
    return recipeGenerator.shaped(RecipeCategory.TOOLS, this)
        .pattern("SCS")
        .pattern(" | ")
        .pattern(" | ")
        .define('S', Items.SHEARS)
        .define('C', ConventionalItemTags.STONES)
        .define('|', Items.STICK)
        .unlockedBy("has_shears", recipeGenerator.has(Items.SHEARS))
        .unlockedBy("has_stone", recipeGenerator.has(ConventionalItemTags.STONES));
  }

  @ApiStatus.AvailableSince("1.0.3")
  public enum Handler implements ServerPlayNetworking.PlayPayloadHandler<SlabToolPayload> {
    INSTANCE;


    /**
     * @see #canDestroyBlock(ItemStack, BlockState, Level, BlockPos, LivingEntity)
     */
    @Override
    public void receive(SlabToolPayload payload, ServerPlayNetworking.Context context) {
      final BlockPos blockPos = payload.blockPos();
      final boolean isTop = payload.isTop();
      final ServerPlayer player = context.player();
      player.level().getServer().execute(() -> {
        if (!player.isWithinBlockInteractionRange(blockPos, 0)) {
          return;
        }
        final ItemStack stack = player.getMainHandItem();
        if (!(stack.getItem() instanceof SlabToolItem) || !(player.getAbilities().mayBuild || stack.canBreakBlockInAdventureMode(new BlockInWorld(player.level(), blockPos, false)))) {
          return;
        }
        final Runnable remove = SERVER_BLOCK_BREAKING_BRIDGE.remove(Pair.of(player.level(), blockPos));
        if (remove == CAN_MINE_CALLED_FIRST) {
          performBreak(player.level(), blockPos, player, isTop);
        } else if (tryToDoubleSlab(player.level().getBlockState(blockPos)) != null) {
          // 收到封包之后，送到 canMine 中执行。
          SERVER_BLOCK_BREAKING_BRIDGE.put(Pair.of(player.level(), blockPos), (PacketReceivedFirst) () -> performBreak(player.level(), blockPos, player, isTop));
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
