package pers.solid.mishang.uc.block;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.SignBlockEntity;
import net.minecraft.data.client.*;
import net.minecraft.data.server.recipe.CraftingRecipeJsonBuilder;
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
import net.minecraft.state.property.EnumProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.*;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;
import net.minecraft.world.WorldEvents;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import pers.solid.mishang.uc.MishangUtils;
import pers.solid.mishang.uc.data.ModelHelper;
import pers.solid.mishang.uc.blockentity.BlockEntityWithText;
import pers.solid.mishang.uc.blockentity.HungSignBlockEntity;
import pers.solid.mishang.uc.blocks.WallSignBlocks;
import pers.solid.mishang.uc.data.MishangucModels;
import pers.solid.mishang.uc.data.MishangucTextureKeys;
import pers.solid.mishang.uc.mixin.ItemUsageContextInvoker;
import pers.solid.mishang.uc.networking.EditSignPayload;
import pers.solid.mishang.uc.render.HungSignBlockEntityRenderer;
import pers.solid.mishang.uc.text.TextContext;
import pers.solid.mishang.uc.util.TextBridge;

import java.util.*;

/**
 * @see HungSignBlockEntity
 * @see HungSignBlockEntityRenderer
 */
public class HungSignBlock extends Block implements Waterloggable, BlockEntityProvider, MishangucBlock {
  public static final EnumProperty<Direction.Axis> AXIS = Properties.HORIZONTAL_AXIS;
  public static final BooleanProperty WATERLOGGED = Properties.WATERLOGGED;
  /**
   * 告示牌是否对左侧连接。若 axis=x，则 left 表示南方；若 axis=z，则 left 表示西方。<br>
   * Whether the sign is connected to the left. The "left" represents "south" if "axis=x", or "west"
   * if "axis=z".
   */
  public static final BooleanProperty LEFT = BooleanProperty.of("left");
  /**
   * 告示牌是否对右侧连接。若 axis=x，则 right 表示北方；若 axis=z，则 right 表示东方。<br>
   * Whether the sign is connected to the right. The "right" represents "north" if "axis=x", or
   * "east" if "axis=z".
   */
  public static final BooleanProperty RIGHT = BooleanProperty.of("right");

  private static final VoxelShape SHAPE_X =
      VoxelShapes.union(
          createCuboidShape(7.5, 5, 0, 8.5, 14, 16), createCuboidShape(7.25, 12, 0, 8.75, 13, 16));
  private static final VoxelShape SHAPE_Z =
      VoxelShapes.union(
          createCuboidShape(0, 5, 7.5, 16, 14, 8.5), createCuboidShape(0, 12, 7.25, 16, 13, 8.75));
  private static final Map<Direction, @Nullable VoxelShape> BAR_SHAPES =
      MishangUtils.createHorizontalDirectionToShape(7.5, 13, 11, 8.5, 16, 12);
  private static final Map<Direction, @Nullable VoxelShape> BAR_SHAPES_EDGE =
      MishangUtils.createHorizontalDirectionToShape(7.5, 13, 13, 8.5, 16, 14);
  private static final VoxelShape SHAPE_WIDENED_X = createCuboidShape(6.5, 5, 0, 9.5, 16, 16);
  private static final VoxelShape SHAPE_WIDENED_Z = createCuboidShape(0, 5, 6.5, 16, 16, 9.5);
  protected static final RecordCodecBuilder<HungSignBlock, Block> BASE_BLOCK_CODEC = Registries.BLOCK.getCodec().fieldOf("base_block").forGetter(b -> b.baseBlock);

  public static final MapCodec<HungSignBlock> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(baseBlockCodec(), createSettingsCodec()).apply(instance, HungSignBlock::new));

  @SuppressWarnings("unchecked")
  protected static <B extends HungSignBlock> RecordCodecBuilder<B, Block> baseBlockCodec() {
    return (RecordCodecBuilder<B, Block>) BASE_BLOCK_CODEC;
  }

  public final @Nullable Block baseBlock;

  /**
   * 基础方块的纹理。{@link #getBaseTexture()} 会使用到此值。如果此值为 {@code null}，则根据 {@link #baseBlock} 来推断纹理。<br>
   * 非 final，可直接进行修改。
   */
  @ApiStatus.AvailableSince("0.1.7")
  public @Nullable Identifier baseTexture;
  /**
   * 告示牌杆的纹理。可能为 {@code null}。生成模型时，可直接作为 null 传入，转化为 json 时会被忽略。
   */
  @ApiStatus.AvailableSince("0.1.7")
  public @Nullable Identifier barTexture;
  /**
   * 告示牌顶部的纹理。可能为 {@code null}。生成模型时，可直接作为 null 传入，转化为 json 时会被忽略。
   */
  @ApiStatus.AvailableSince("0.1.7")
  public @Nullable Identifier textureTop;

  public HungSignBlock(@Nullable Block baseBlock, Settings settings) {
    super(settings);
    this.baseBlock = baseBlock;
    this.setDefaultState(getDefaultState()
        .with(WATERLOGGED, false)
        .with(AXIS, Direction.Axis.X)
        .with(LEFT, false)
        .with(RIGHT, false));
  }

  @ApiStatus.AvailableSince("0.1.7")
  public HungSignBlock(@NotNull Block baseBlock) {
    this(baseBlock, Block.Settings.copy(baseBlock));
  }

  @Override
  protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
    super.appendProperties(builder);
    builder.add(AXIS, WATERLOGGED, LEFT, RIGHT);
  }

  @Nullable
  @Override
  public BlockState getPlacementState(ItemPlacementContext ctx) {
    final BlockState placementState = super.getPlacementState(ctx);
    if (placementState == null) {
      return null;
    }
    final World world = ctx.getWorld();
    final BlockPos blockPos = ctx.getBlockPos();
    final BlockState blockState =
        world.getBlockState(((ItemUsageContextInvoker) ctx).invokeGetHitResult().getBlockPos());
    return placementState
        .with(AXIS,
            blockState.getBlock() instanceof HungSignBlock && blockState.contains(AXIS)
                ? blockState.get(AXIS)
                : ctx.getHorizontalPlayerFacing().getAxis())
        .with(WATERLOGGED, world.getFluidState(blockPos).getFluid() == Fluids.WATER)
        .getStateForNeighborUpdate(
            Direction.UP, world.getBlockState(blockPos.up()), world, blockPos, blockPos.up());
  }

  @NotNull
  @Override
  public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
    return new HungSignBlockEntity(pos, state);
  }

  @Override
  public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
    final Direction.Axis axis = state.get(AXIS);
    return switch (axis) {
      case X -> SHAPE_WIDENED_X;
      case Z -> SHAPE_WIDENED_Z;
      default -> VoxelShapes.empty();
    };
  }

  @Override
  public VoxelShape getCollisionShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
    final Direction.Axis axis = state.get(AXIS);
    final boolean left = state.get(LEFT);
    final boolean right = state.get(RIGHT);
    switch (axis) {
      case X:
        if (!left && !right)
          return VoxelShapes.union(
              SHAPE_X, BAR_SHAPES_EDGE.get(Direction.SOUTH), BAR_SHAPES_EDGE.get(Direction.NORTH));
        else
          return VoxelShapes.union(
              SHAPE_X,
              !left ? BAR_SHAPES.get(Direction.SOUTH) : VoxelShapes.empty(),
              !right ? BAR_SHAPES.get(Direction.NORTH) : VoxelShapes.empty());
      case Z:
        if (!left && !right)
          return VoxelShapes.union(
              SHAPE_Z, BAR_SHAPES_EDGE.get(Direction.WEST), BAR_SHAPES_EDGE.get(Direction.EAST));
        else
          return VoxelShapes.union(
              SHAPE_Z,
              !left ? BAR_SHAPES.get(Direction.WEST) : VoxelShapes.empty(),
              !right ? BAR_SHAPES.get(Direction.EAST) : VoxelShapes.empty());
      default:
        return VoxelShapes.empty();
    }
  }

  @Override
  public VoxelShape getSidesShape(BlockState state, BlockView world, BlockPos pos) {
    return getOutlineShape(state, world, pos, ShapeContext.absent());
  }

  @Override
  public VoxelShape getCullingShape(BlockState state, BlockView world, BlockPos pos) {
    return getCollisionShape(state, world, pos, ShapeContext.absent());
  }

  @Override
  public FluidState getFluidState(BlockState state) {
    return state.get(WATERLOGGED) ? Fluids.WATER.getStill(false) : super.getFluidState(state);
  }

  /**
   * 当这个指定的方向连接有同类方块时，这个方块（left 和 right）就会为 true，此时上方将不会显示栏杆。<br>
   * 如果连接有非同类方块，且上方没有连接带有碰撞箱的方块，则这个方向也会为 true。
   */
  @Override
  public BlockState getStateForNeighborUpdate(
      BlockState state,
      Direction direction,
      BlockState neighborState,
      WorldAccess world,
      BlockPos pos,
      BlockPos neighborPos) {
    state = super.getStateForNeighborUpdate(state, direction, neighborState, world, pos, neighborPos);
    if (state.get(WATERLOGGED)) {
      world.scheduleFluidTick(pos, Fluids.WATER, Fluids.WATER.getTickRate(world));
    }
    if (Direction.Type.HORIZONTAL.test(direction)) {
      if (world.getBlockState(pos.up()).getCollisionShape(world, pos.up()).getMin(Direction.Axis.Y) == 0) {
        state = prepareNeighborState(state, direction, neighborState);
      } else {
        state = state.with(LEFT, true).with(RIGHT, true);
      }
    } else if (direction == Direction.UP) {
      if (neighborState.getCollisionShape(world, neighborPos).getMin(Direction.Axis.Y) == 0) {
        for (Direction horizontalDirection : Direction.Type.HORIZONTAL) {
          final BlockPos offsetPos = pos.offset(horizontalDirection);
          state = prepareNeighborState(state, horizontalDirection, world.getBlockState(offsetPos));
        }
      } else {
        state = state.with(LEFT, true).with(RIGHT, true);
      }
    }
    return state;
  }

  /**
   * 计算与周围某个方向连接时应有的方块状态，以确定杆子是否显示。与 {@link #getStateForNeighborUpdate} 相比，该方法不会检测上方方块是否已连接（调用此方法时，就假定上方已经连接了方块），也不会更新流体状态。
   *
   * @param state         该方块原先的方块状态。
   * @param direction     毗邻方块所属的方向。
   * @param neighborState 批零的方块状态。
   * @return 对应的方块状态。
   */
  public BlockState prepareNeighborState(BlockState state, Direction direction, BlockState neighborState) {
    if (Direction.Type.VERTICAL.test(direction)) return state;
    final @Nullable BooleanProperty property;
    final Direction.Axis axis = state.get(AXIS);
    property = switch (axis) {
      case X -> direction == Direction.SOUTH ? LEFT : direction == Direction.NORTH ? RIGHT : null;
      case Z -> direction == Direction.WEST ? LEFT : direction == Direction.EAST ? RIGHT : null;
      default -> null;
    };
    if (property != null) {
      return state.with(property, neighborState.getBlock() instanceof HungSignBlock && neighborState.get(AXIS) == axis);
    } else {
      return state;
    }
  }

  @Override
  public BlockState rotate(BlockState state, BlockRotation rotation) {
    final Direction.Axis oldAxis = state.get(AXIS);
    state = super.rotate(state, rotation)
        .with(AXIS,
            rotation == BlockRotation.CLOCKWISE_90
                || rotation == BlockRotation.COUNTERCLOCKWISE_90
                ? (oldAxis == Direction.Axis.X ? Direction.Axis.Z : Direction.Axis.X)
                : oldAxis);
    if (rotation == BlockRotation.CLOCKWISE_180
        || (oldAxis == Direction.Axis.X && rotation == BlockRotation.COUNTERCLOCKWISE_90)
        || (oldAxis == Direction.Axis.Z && rotation == BlockRotation.CLOCKWISE_90)) {
      state = state.with(LEFT, state.get(RIGHT)).with(RIGHT, state.get(LEFT));
    }
    return state;
  }

  @Override
  public BlockState mirror(BlockState state, BlockMirror mirror) {
    state = super.mirror(state, mirror);
    final Direction.Axis axis = state.get(AXIS);
    if ((axis == Direction.Axis.Z && mirror == BlockMirror.FRONT_BACK) || (axis == Direction.Axis.X && mirror == BlockMirror.LEFT_RIGHT)) {
      state = state.with(LEFT, state.get(RIGHT)).with(RIGHT, state.get(LEFT));
    }
    return state;
  }


  /**
   * 点击告示牌方块时，允许玩家对告示牌进行编辑。冒险模式的玩家无权进行编辑。 <br>
   * 本方法在编写时，适当参照了 {@link net.minecraft.item.SignItem#postPlacement(BlockPos, World, PlayerEntity,
   * ItemStack, BlockState)}。<br>
   * 告示牌界面的打开逻辑又可以参考 {@link
   * net.minecraft.client.network.ClientPlayerEntity#openEditSignScreen(SignBlockEntity, boolean)} 和 {@link
   * net.minecraft.server.network.ServerPlayerEntity#openEditSignScreen(SignBlockEntity, boolean)}。
   */
  @Override
  protected ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, BlockHitResult hit) {
    final BlockEntity blockEntity = world.getBlockEntity(pos);
    final Direction side = hit.getSide();
    if (!(blockEntity instanceof final HungSignBlockEntity entity)) {
      return ActionResult.PASS;
    } else if (!state.get(AXIS).test(side)) {
      // 若方块实体不对应，或者编辑的这一侧不可编辑，则在客户端和服务器均略过。
      // Skip if the block entity does not correspond, or the side is not editable.
      return ActionResult.PASS;
    } else if (!player.getAbilities().allowModifyWorld) {
      // 冒险模式玩家无权编辑。Adventure players have no permission to edit.
      return ActionResult.FAIL;
    } else if (world.isClient) {
      return ActionResult.SUCCESS;
    }

    entity.checkEditorValidity();
    final PlayerEntity editor = entity.getEditor();
    if (editor != null && editor != player) {
      // 这种情况下，告示牌被占用，玩家无权编辑。
      // In this case, the sign is occupied, and the player has no editing permission.
      player.sendMessage(TextBridge.translatable("message.mishanguc.no_editing_permission.occupied", editor.getName()), false);
      return ActionResult.FAIL;
    }
    entity.editedSide = side;
    entity.setEditor(player);
    ServerPlayNetworking.send((ServerPlayerEntity) player, new EditSignPayload(pos, Optional.of(side), Optional.empty()));
    return ActionResult.SUCCESS;
  }

  @Override
  protected ItemActionResult onUseWithItem(ItemStack stack, BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
    final ItemActionResult itemActionResult = super.onUseWithItem(stack, state, world, pos, player, hand, hit);
    if (itemActionResult != ItemActionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION) return itemActionResult;
    final BlockEntity blockEntity = world.getBlockEntity(pos);
    final Direction side = hit.getSide();
    if (!(blockEntity instanceof final HungSignBlockEntity entity)) {
      return ItemActionResult.SKIP_DEFAULT_BLOCK_INTERACTION;
    } else if (!state.get(AXIS).test(side)) {
      // 若方块实体不对应，或者编辑的这一侧不可编辑，则在客户端和服务器均略过。
      return ItemActionResult.SKIP_DEFAULT_BLOCK_INTERACTION;
    } else if (!player.getAbilities().allowModifyWorld) {
      // 冒险模式玩家无权编辑。Adventure players have no permission to edit.
      return ItemActionResult.FAIL;
    } else if (world.isClient) {
      return ItemActionResult.SUCCESS;
    } else {
      if (stack.getItem() instanceof HoneycombItem) {
        // 处理告示牌的涂蜡
        if (!entity.waxed.contains(side)) {
          entity.waxed = addToSet(entity.waxed, side);
          player.sendMessage(BlockEntityWithText.MESSAGE_WAX_ON, true);
          world.syncWorldEvent(null, WorldEvents.BLOCK_WAXED, entity.getPos(), 0);
          entity.markDirtyAndUpdate();
          if (!player.isCreative()) stack.decrement(1);
          return ItemActionResult.SUCCESS;
        } else if (player.isCreative()) {
          entity.waxed = removeFromSet(entity.waxed, side);
          player.sendMessage(BlockEntityWithText.MESSAGE_WAX_OFF, true);
          world.syncWorldEvent(null, WorldEvents.WAX_REMOVED, entity.getPos(), 0);
          entity.markDirtyAndUpdate();
          return ItemActionResult.SUCCESS;
        }
      }
      if (entity.waxed.contains(side)) {
        // 涂蜡的告示牌不应该进行操作。
        world.playSound(null, entity.getPos(), SoundEvents.BLOCK_SIGN_WAXED_INTERACT_FAIL, SoundCategory.BLOCKS);
        return ItemActionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
      } else if (stack.isOf(Items.MAGMA_CREAM)) {
        // 玩家手持岩浆膏时，可快速进行重整。
        final List<@NotNull TextContext> textContexts = entity.texts.get(side);
        if (textContexts != null) MishangUtils.rearrange(textContexts);
        entity.markDirtyAndUpdate();
        return ItemActionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
      } else if (stack.getItem() instanceof GlowInkSacItem) {
        if (!entity.glowing.contains(side)) {
          entity.glowing = addToSet(entity.glowing, side);
          player.sendMessage(BlockEntityWithText.MESSAGE_GLOW_ON, true);
          world.playSound(null, entity.getPos(), SoundEvents.ITEM_GLOW_INK_SAC_USE, SoundCategory.BLOCKS, 1.0F, 1.0F);
          entity.markDirtyAndUpdate();
          if (!player.isCreative()) stack.decrement(1);
          return ItemActionResult.SUCCESS;
        }
      } else if (stack.getItem() instanceof InkSacItem) {
        if (entity.glowing.contains(side)) {
          entity.glowing = removeFromSet(entity.glowing, side);
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

  @Override
  public MutableText getName() {
    if (baseBlock != null) {
      return TextBridge.translatable("block.mishanguc.hung_sign", baseBlock.getName());
    }
    return super.getName();
  }

  @Override
  public void appendTooltip(ItemStack stack, Item.TooltipContext context, List<Text> tooltip, TooltipType options) {
    super.appendTooltip(stack, context, tooltip, options);
    tooltip.add(TextBridge.translatable("block.mishanguc.hung_sign.tooltip.1").formatted(Formatting.GRAY));
    tooltip.add(TextBridge.translatable("block.mishanguc.hung_sign.tooltip.2").formatted(Formatting.GRAY));
  }

  public Identifier getBaseTexture() {
    if (baseTexture != null) return baseTexture;
    return ModelHelper.getTextureOf(baseBlock == null ? this : baseBlock);
  }

  @Override
  public void registerModels(ModelProvider modelProvider, BlockStateModelGenerator blockStateModelGenerator) {
    final Identifier texture = getBaseTexture();
    final TextureMap textures = TextureMap.texture(texture);
    if (barTexture != null) textures.put(MishangucTextureKeys.BAR, barTexture);
    if (textureTop != null) textures.put(MishangucTextureKeys.TEXTURE_TOP, textureTop);

    final Identifier id = MishangucModels.HUNG_SIGN.upload(this, textures, blockStateModelGenerator.modelCollector);
    final Identifier bodyId = MishangucModels.HUNG_SIGN_BODY.upload(this, textures, blockStateModelGenerator.modelCollector);
    final Identifier topBarId = MishangucModels.HUNG_SIGN_TOP_BAR.upload(this, textures, blockStateModelGenerator.modelCollector);
    final Identifier topBarEdgeId = MishangucModels.HUNG_SIGN_TOP_BAR_EDGE.upload(this, textures, blockStateModelGenerator.modelCollector);

    blockStateModelGenerator.blockStateCollector.accept(createBlockStates(bodyId, topBarId, topBarEdgeId));
    blockStateModelGenerator.registerParentedItemModel(this, id);
  }

  public @NotNull BlockStateSupplier createBlockStates(Identifier bodyId, Identifier topBarId, Identifier topBarEdgeId) {
    return MultipartBlockStateSupplier.create(this)
        .with(When.create().set(AXIS, Direction.Axis.Z), BlockStateVariant.create().put(VariantSettings.MODEL, bodyId).put(VariantSettings.UVLOCK, true))
        .with(When.create().set(AXIS, Direction.Axis.X), BlockStateVariant.create().put(VariantSettings.MODEL, bodyId).put(VariantSettings.UVLOCK, true).put(VariantSettings.Y, VariantSettings.Rotation.R90))
        .with(When.create().set(AXIS, Direction.Axis.Z).set(LEFT, false).set(RIGHT, true), BlockStateVariant.create().put(VariantSettings.MODEL, topBarId).put(VariantSettings.UVLOCK, true))
        .with(When.create().set(AXIS, Direction.Axis.Z).set(LEFT, true).set(RIGHT, false), BlockStateVariant.create().put(VariantSettings.MODEL, topBarId).put(VariantSettings.UVLOCK, true).put(MishangUtils.INT_Y_VARIANT, 180))
        .with(When.create().set(AXIS, Direction.Axis.X).set(LEFT, false).set(RIGHT, true), BlockStateVariant.create().put(VariantSettings.MODEL, topBarId).put(VariantSettings.UVLOCK, true).put(MishangUtils.INT_Y_VARIANT, -90))
        .with(When.create().set(AXIS, Direction.Axis.X).set(LEFT, true).set(RIGHT, false), BlockStateVariant.create().put(VariantSettings.MODEL, topBarId).put(VariantSettings.UVLOCK, true).put(MishangUtils.INT_Y_VARIANT, 90))
        .with(When.create().set(AXIS, Direction.Axis.Z).set(LEFT, false).set(RIGHT, false), BlockStateVariant.create().put(VariantSettings.MODEL, topBarEdgeId).put(VariantSettings.UVLOCK, true))
        .with(When.create().set(AXIS, Direction.Axis.Z).set(LEFT, false).set(RIGHT, false), BlockStateVariant.create().put(VariantSettings.MODEL, topBarEdgeId).put(VariantSettings.UVLOCK, true).put(MishangUtils.INT_Y_VARIANT, 180))
        .with(When.create().set(AXIS, Direction.Axis.X).set(LEFT, false).set(RIGHT, false), BlockStateVariant.create().put(VariantSettings.MODEL, topBarEdgeId).put(VariantSettings.UVLOCK, true).put(MishangUtils.INT_Y_VARIANT, 90))
        .with(When.create().set(AXIS, Direction.Axis.X).set(LEFT, false).set(RIGHT, false), BlockStateVariant.create().put(VariantSettings.MODEL, topBarEdgeId).put(VariantSettings.UVLOCK, true).put(MishangUtils.INT_Y_VARIANT, 270));
  }

  private @Nullable String getRecipeGroup() {
    if (baseBlock instanceof ColoredBlock) return null;
    if (MishangUtils.isWood(baseBlock)) return "mishanguc:wood_hung_sign";
    if (MishangUtils.isStrippedWood(baseBlock)) return "mishanguc:stripped_wood_hung_sign";
    if (MishangUtils.isPlanks(baseBlock)) return "mishanguc:plank_wood_hung_sign";
    if (MishangUtils.isConcrete(baseBlock)) return "mishanguc:concrete_hung_sign";
    if (MishangUtils.isTerracotta(baseBlock)) return "mishanguc:terracotta_hung_sign";
    if (baseBlock == Blocks.ICE || baseBlock == Blocks.PACKED_ICE || baseBlock == Blocks.BLUE_ICE) {
      return "mishanguc:ice_hung_sign";
    }
    return null;
  }

  @Override
  public CraftingRecipeJsonBuilder getCraftingRecipe() {
    if (baseBlock == null) return null;
    return ShapedRecipeJsonBuilder.create(getRecipeCategory(), this, 6)
        .patterns("-#-", "-#-", "-#-")
        .input('#', baseBlock)
        .input('-', WallSignBlocks.INVISIBLE_WALL_SIGN)
        .setCustomRecipeCategory("signs")
        .criterionFromItem("has_base_block", baseBlock)
        .criterionFromItem("has_sign", WallSignBlocks.INVISIBLE_WALL_SIGN)
        .group(getRecipeGroup());
  }

  @Override
  public @Nullable RecipeCategory getRecipeCategory() {
    return RecipeCategory.DECORATIONS;
  }

  @Override
  public boolean isSideInvisible(BlockState state, BlockState stateFrom, Direction direction) {
    if (direction.getAxis().isHorizontal() && state.getBlock() instanceof HungSignBlock && stateFrom.getBlock() instanceof HungSignBlock hungSignBlockFrom && state.get(AXIS) == stateFrom.get(AXIS) && direction.getAxis() != state.get(AXIS)) {
      if (hungSignBlockFrom.baseBlock instanceof TransparentBlock) {
        if (baseBlock instanceof TransparentBlock) {
          // 自身和相邻方块都为透明方块，则双方均为同一方块时隐藏。
          return baseBlock == hungSignBlockFrom.baseBlock;
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
  protected MapCodec<? extends HungSignBlock> getCodec() {
    return CODEC;
  }

  /**
   * 往集合中添加一个值，并返回添加后的集合（可能会是新集合）。这样做是为了避免使用空集时创建集合对象。
   */
  private static <T> Set<T> addToSet(Set<T> set, T element) {
    if (set.isEmpty()) {
      final HashSet<T> newSet = new HashSet<>(2);
      newSet.add(element);
      return newSet;
    } else {
      set.add(element);
      return set;
    }
  }

  /**
   * 从集合中移除一个值，如果移除后的集合为空集合，则返回不可变的空集合，以避免产生不必要的对象。
   */
  private static <T> Set<T> removeFromSet(Set<T> set, T element) {
    if (set.isEmpty()) {
      return set;
    } else if (set.remove(element) && set.isEmpty()) {
      return Set.of();
    } else {
      set.remove(element);
      return set;
    }
  }
}
