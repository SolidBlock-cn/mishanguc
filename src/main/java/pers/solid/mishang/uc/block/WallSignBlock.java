package pers.solid.mishang.uc.block;

import com.google.common.collect.ImmutableMap;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.enums.BlockFace;
import net.minecraft.data.client.*;
import net.minecraft.data.server.recipe.CraftingRecipeJsonBuilder;
import net.minecraft.data.server.recipe.RecipeProvider;
import net.minecraft.data.server.recipe.ShapedRecipeJsonBuilder;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.fluid.FluidState;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.*;
import net.minecraft.item.tooltip.TooltipType;
import net.minecraft.recipe.book.RecipeCategory;
import net.minecraft.registry.Registries;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.*;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.*;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.Unmodifiable;
import pers.solid.mishang.uc.MishangUtils;
import pers.solid.mishang.uc.blockentity.BlockEntityWithText;
import pers.solid.mishang.uc.blockentity.WallSignBlockEntity;
import pers.solid.mishang.uc.blocks.WallSignBlocks;
import pers.solid.mishang.uc.data.MishangucModels;
import pers.solid.mishang.uc.data.ModelHelper;
import pers.solid.mishang.uc.networking.EditSignPayload;
import pers.solid.mishang.uc.render.WallSignBlockEntityRenderer;
import pers.solid.mishang.uc.util.TextBridge;

import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * 与 Minecraft 原版的 {@link net.minecraft.block.WallSignBlock} 不同，这里的 {@code WallSignBlock}
 * 更加强大，可以编辑，且可以放在地上或者天花板上。
 *
 * @see WallSignBlocks
 * @see WallSignBlockEntity
 * @see WallSignBlockEntityRenderer
 */
public class WallSignBlock extends WallMountedBlock implements Waterloggable, BlockEntityProvider, MishangucBlock {
  protected static <B extends WallSignBlock> RecordCodecBuilder<B, Block> createBaseBlockCodec() {
    return Registries.BLOCK.getCodec().fieldOf("base_block").forGetter(b -> b.baseBlock);
  }

  public static final MapCodec<WallSignBlock> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(createBaseBlockCodec(), createSettingsCodec()).apply(instance, WallSignBlock::new));

  public static final BooleanProperty WATERLOGGED = Properties.WATERLOGGED;
  public static final Map<Direction, VoxelShape> SHAPES_WHEN_WALL =
      MishangUtils.createHorizontalDirectionToShape(0, 4, 0, 16, 12, 1);
  public static final Map<Direction, VoxelShape> SHAPES_WHEN_FLOOR =
      MishangUtils.createHorizontalDirectionToShape(0, 0, 4, 16, 1, 12);
  public static final Map<Direction, VoxelShape> SHAPES_WHEN_CEILING =
      MishangUtils.createHorizontalDirectionToShape(0, 15, 4, 16, 16, 12);
  @Unmodifiable
  public static final Map<BlockFace, Map<Direction, VoxelShape>>
      SHAPE_PER_WALL_MOUNT_LOCATION =
      ImmutableMap.of(
          BlockFace.CEILING,
          SHAPES_WHEN_CEILING,
          BlockFace.FLOOR,
          SHAPES_WHEN_FLOOR,
          BlockFace.WALL,
          SHAPES_WHEN_WALL);
  public final Block baseBlock;
  /**
   * 告示牌自身的纹理。默认为 {@code null}，可在后期修改。若为 {@code null}，则直接根据其基础方块 {@link #baseBlock} 推断纹理。
   */
  @ApiStatus.AvailableSince("0.1.7")
  public @Nullable Identifier texture;

  public WallSignBlock(@Nullable Block baseBlock, Settings settings) {
    super(settings);
    this.baseBlock = baseBlock;
    setDefaultState(getDefaultState()
        .with(FACING, Direction.SOUTH)
        .with(FACE, BlockFace.WALL)
        .with(WATERLOGGED, false));
  }

  @ApiStatus.AvailableSince("0.1.7")
  public WallSignBlock(@NotNull Block baseBlock) {
    this(baseBlock, Block.Settings.copy(baseBlock));
  }

  @Override
  protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
    super.appendProperties(builder);
    builder.add(FACE, FACING, WATERLOGGED);
  }

  @Nullable
  @Override
  public BlockState getPlacementState(ItemPlacementContext ctx) {
    final BlockState placementState = super.getPlacementState(ctx);
    return placementState != null
        ? placementState.with(
        WATERLOGGED, ctx.getWorld().getFluidState(ctx.getBlockPos()).getFluid() == Fluids.WATER)
        : null;
  }

  @Override
  public boolean canPlaceAt(BlockState state, WorldView world, BlockPos pos) {
    return true;
  }

  @Override
  public VoxelShape getOutlineShape(
      BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
    return SHAPE_PER_WALL_MOUNT_LOCATION.get(state.get(FACE)).get(state.get(FACING));
  }

  @Override
  public FluidState getFluidState(BlockState state) {
    return state.get(WATERLOGGED) ? Fluids.WATER.getStill(false) : super.getFluidState(state);
  }

  @Override
  public BlockState getStateForNeighborUpdate(
      BlockState state,
      Direction direction,
      BlockState neighborState,
      WorldAccess world,
      BlockPos pos,
      BlockPos neighborPos) {
    super.getStateForNeighborUpdate(state, direction, neighborState, world, pos, neighborPos);
    if (state.get(WATERLOGGED)) {
      world.scheduleFluidTick(pos, Fluids.WATER, Fluids.WATER.getTickRate(world));
    }
    return state;
  }

  @Override
  public MutableText getName() {
    return baseBlock == null
        ? super.getName()
        : TextBridge.translatable("block.mishanguc.wall_sign", baseBlock.getName());
  }

  @Override
  public void appendTooltip(ItemStack stack, Item.TooltipContext context, List<Text> tooltip, TooltipType options) {
    super.appendTooltip(stack, context, tooltip, options);
    tooltip.add(TextBridge.translatable("block.mishanguc.wall_sign.tooltip.1").formatted(Formatting.GRAY));
    tooltip.add(TextBridge.translatable("block.mishanguc.wall_sign.tooltip.2").formatted(Formatting.GRAY));
  }

  @Override
  protected ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, BlockHitResult hit) {
    final ActionResult actionResult = super.onUse(state, world, pos, player, hit);
    if (actionResult != ActionResult.PASS) return actionResult;
    // 在服务端触发打开告示牌编辑界面。Open the edit interface, triggered in the server side.
    final BlockEntity blockEntity = world.getBlockEntity(pos);
    if (!(blockEntity instanceof final WallSignBlockEntity entity)) {
      return ActionResult.PASS;
    } else if (!player.getAbilities().allowModifyWorld) {
      // 冒险模式玩家无权编辑。Adventure players have no permission to edit.
      return ActionResult.FAIL;
    } else if (world.isClient) {
      return ActionResult.SUCCESS;
    }

    entity.checkEditorValidity();
    PlayerEntity editor = entity.getEditor();
    if (editor != null && editor != player) {
      // 这种情况下，告示牌被占用，玩家无权编辑。
      // In this case, the sign is occupied, and the player has no editing
      // permission.
      player.sendMessage(TextBridge.translatable("message.mishanguc.no_editing_permission.occupied", editor.getName()), false);
      return ActionResult.FAIL;
    }
    // 此时告示牌已被编辑。
    entity.setEditor(player);
    ServerPlayNetworking.send(((ServerPlayerEntity) player), new EditSignPayload(pos, Optional.of(hit.getSide()), Optional.empty()));
    return ActionResult.SUCCESS;
  }

  @Override
  protected ItemActionResult onUseWithItem(ItemStack stack, BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
    // 在服务端触发打开告示牌编辑界面。
    final BlockEntity blockEntity = world.getBlockEntity(pos);
    if (!(blockEntity instanceof final WallSignBlockEntity entity)) {
      return ItemActionResult.SKIP_DEFAULT_BLOCK_INTERACTION;
    } else if (!player.getAbilities().allowModifyWorld) {
      // 冒险模式玩家无权编辑。Adventure players have no permission to edit.
      return ItemActionResult.FAIL;
    } else if (world.isClient) {
      return ItemActionResult.SUCCESS;
    } else {
      if (stack.getItem() instanceof HoneycombItem) {
        // 处理告示牌的涂蜡。
        if (!entity.waxed) {
          entity.waxed = true;
          player.sendMessage(BlockEntityWithText.MESSAGE_WAX_ON, true);
          world.syncWorldEvent(null, WorldEvents.BLOCK_WAXED, entity.getPos(), 0);
          entity.markDirtyAndUpdate();
          if (!player.isCreative()) stack.decrement(1);
          return ItemActionResult.SUCCESS;
        } else if (player.isCreative()) {
          entity.waxed = false;
          player.sendMessage(BlockEntityWithText.MESSAGE_WAX_OFF, true);
          world.syncWorldEvent(null, WorldEvents.WAX_REMOVED, entity.getPos(), 0);
          entity.markDirtyAndUpdate();
          return ItemActionResult.SUCCESS;
        }
      }
      if (entity.waxed) {
        // 涂蜡的告示牌不应该进行操作。
        world.playSound(null, entity.getPos(), SoundEvents.BLOCK_SIGN_WAXED_INTERACT_FAIL, SoundCategory.BLOCKS);
        return ItemActionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
      } else if (stack.isOf(Items.MAGMA_CREAM)) {
        MishangUtils.rearrange(entity.textContexts);
        entity.markDirtyAndUpdate();
        if (!player.isCreative()) stack.decrement(1);
        return ItemActionResult.SUCCESS;
      } else if (stack.getItem() instanceof GlowInkSacItem) {
        if (!entity.glowing) {
          entity.glowing = true;
          player.sendMessage(BlockEntityWithText.MESSAGE_GLOW_ON, true);
          world.playSound(null, entity.getPos(), SoundEvents.ITEM_GLOW_INK_SAC_USE, SoundCategory.BLOCKS, 1.0F, 1.0F);
          entity.markDirtyAndUpdate();
          if (!player.isCreative()) stack.decrement(1);
          return ItemActionResult.SUCCESS;
        }
      } else if (stack.getItem() instanceof InkSacItem) {
        if (entity.glowing) {
          entity.glowing = false;
          player.sendMessage(BlockEntityWithText.MESSAGE_GLOW_OFF, true);
          world.playSound(null, entity.getPos(), SoundEvents.ITEM_INK_SAC_USE, SoundCategory.BLOCKS, 1.0F, 1.0F);
          entity.markDirtyAndUpdate();
          if (!player.isCreative()) stack.decrement(1);
          return ItemActionResult.SUCCESS;
        }
      }
    }

    return ItemActionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
  }

  @Nullable
  @Override
  public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
    return new WallSignBlockEntity(pos, state);
  }

  @Override
  public void registerModels(ModelProvider modelProvider, BlockStateModelGenerator blockStateModelGenerator) {
    final TextureMap textures = TextureMap.texture(getBaseTexture());
    final Identifier modelId = MishangucModels.WALL_SIGN.upload(this, textures, blockStateModelGenerator.modelCollector);
    blockStateModelGenerator.blockStateCollector.accept(createBlockStates(modelId));
  }

  public VariantsBlockStateSupplier createBlockStates(Identifier modelId) {
    return BlockStateModelGenerator.createSingletonBlockState(this, modelId).coordinate(BlockStateVariantMap.create(FACE, FACING).register((wallMountLocation, direction) -> {
      final int x = switch (wallMountLocation) {
        case WALL -> 0;
        case FLOOR -> 90;
        default -> -90;
      };
      return BlockStateVariant.create().put(VariantSettings.MODEL, modelId)
          .put(MishangUtils.INT_X_VARIANT, x)
          .put(MishangUtils.DIRECTION_Y_VARIANT, direction)
          .put(VariantSettings.UVLOCK, true);
    }));
  }

  public Identifier getBaseTexture() {
    if (texture != null) return texture;
    return ModelHelper.getTextureOf(baseBlock == null ? this : baseBlock);
  }

  private @Nullable String getRecipeGroup() {
    if (baseBlock instanceof ColoredBlock) return null;
    if (MishangUtils.isWood(baseBlock)) return "mishanguc:wood_wall_sign";
    if (MishangUtils.isStrippedWood(baseBlock)) return "mishanguc:stripped_wood_wall_sign";
    if (MishangUtils.isPlanks(baseBlock)) return "mishanguc:plank_wall_sign";
    if (MishangUtils.isConcrete(baseBlock)) return "mishanguc:concrete_wall_sign";
    if (MishangUtils.isTerracotta(baseBlock)) return "mishanguc:terracotta_wall_sign";
    if (baseBlock == Blocks.ICE || baseBlock == Blocks.PACKED_ICE || baseBlock == Blocks.BLUE_ICE) {
      return "mishanguc:ice_wall_sign";
    }
    return null;
  }

  @Override
  public @Nullable CraftingRecipeJsonBuilder getCraftingRecipe() {
    if (baseBlock == null) return null;
    return ShapedRecipeJsonBuilder.create(RecipeCategory.DECORATIONS, this, 6)
        .pattern("---")
        .pattern("###")
        .pattern("---")
        .input('#', baseBlock).input('-', WallSignBlocks.INVISIBLE_WALL_SIGN)
        .criterion("has_base_block", RecipeProvider.conditionsFromItem(baseBlock))
        .criterion("has_sign", RecipeProvider.conditionsFromItem(WallSignBlocks.INVISIBLE_WALL_SIGN))
        .group(getRecipeGroup());
  }

  @Override
  public boolean isSideInvisible(BlockState state, BlockState stateFrom, Direction direction) {
    if (direction.getAxis().isHorizontal() && state.getBlock() instanceof WallSignBlock && stateFrom.getBlock() instanceof WallSignBlock wallSignBlockFrom && state.get(FACING) == stateFrom.get(FACING) && direction.getAxis() != state.get(FACING).getAxis()) {
      if (wallSignBlockFrom.baseBlock instanceof TransparentBlock) {
        if (baseBlock instanceof TransparentBlock) {
          // 自身和相邻方块都为透明方块，则双方均为同一方块时隐藏。
          return baseBlock == wallSignBlockFrom.baseBlock;
        } else {
          return false;
        }
      }
      return true;
    } else {
      return super.isSideInvisible(state, stateFrom, direction);
    }
  }

  @Override
  protected MapCodec<? extends WallSignBlock> getCodec() {
    return CODEC;
  }

  @Override
  public String customRecipeCategory() {
    return "wall_signs";
  }
}
