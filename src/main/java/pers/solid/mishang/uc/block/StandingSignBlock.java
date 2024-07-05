package pers.solid.mishang.uc.block;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import it.unimi.dsi.fastutil.booleans.BooleanArraySet;
import it.unimi.dsi.fastutil.booleans.BooleanSet;
import it.unimi.dsi.fastutil.booleans.BooleanSets;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
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
import net.minecraft.state.property.IntProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.*;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;
import net.minecraft.world.WorldEvents;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import pers.solid.brrp.v1.BRRPUtils;
import pers.solid.brrp.v1.api.RuntimeResourcePack;
import pers.solid.brrp.v1.generator.BlockResourceGenerator;
import pers.solid.brrp.v1.model.ModelJsonBuilder;
import pers.solid.mishang.uc.MishangUtils;
import pers.solid.mishang.uc.blockentity.BlockEntityWithText;
import pers.solid.mishang.uc.blockentity.StandingSignBlockEntity;
import pers.solid.mishang.uc.blocks.WallSignBlocks;
import pers.solid.mishang.uc.mixin.ItemUsageContextInvoker;
import pers.solid.mishang.uc.networking.EditSignPayload;
import pers.solid.mishang.uc.util.TextBridge;

import java.util.List;
import java.util.Optional;

/**
 * 本模组中的直立告示牌方块。
 *
 * @see StandingSignBlockEntity
 * @see pers.solid.mishang.uc.blocks.StandingSignBlocks
 * @see pers.solid.mishang.uc.render.StandingSignBlockEntityRenderer
 */
@ApiStatus.AvailableSince("1.0.2")
public class StandingSignBlock extends Block implements BlockEntityProvider, Waterloggable, BlockResourceGenerator {
  public static final MapCodec<StandingSignBlock> CODEC = RecordCodecBuilder.mapCodec(i -> i.group(baseBlockCodec(), createSettingsCodec()).apply(i, StandingSignBlock::new));

  public static final IntProperty ROTATION = Properties.ROTATION;
  public static final BooleanProperty WATERLOGGED = Properties.WATERLOGGED;
  /**
   * 指定告示牌底部是否有杆子。默认取决于底部方块的侧面形状。按住 Shift 并点击告示牌可以切换。
   */
  public static final BooleanProperty DOWN = Properties.DOWN;
  protected static final VoxelShape SHAPE_NS = createCuboidShape(0, 8, 6.5, 16, 16, 9.5);
  protected static final VoxelShape SHAPE_WE = createCuboidShape(6.5, 8, 0, 9.5, 16, 16);
  protected static final VoxelShape SHAPE_NS_WIDE = createCuboidShape(2, 8, 5, 14, 16, 11);
  protected static final VoxelShape SHAPE_WE_WIDE = createCuboidShape(5, 8, 2, 11, 16, 14);
  protected static final VoxelShape SHAPE_CENTER = createCuboidShape(2.5, 8, 2.5, 13.5, 16, 13.5);
  protected static final VoxelShape CULLING_SHAPE = createCuboidShape(7.5, 0, 7.5, 8.5, 8, 8.5);
  protected static final VoxelShape BAR_SHAPE = createCuboidShape(6.5, 0, 6.5, 9.5, 8, 9.5);
  protected static final RecordCodecBuilder<? extends StandingSignBlock, Block> BASE_BLOCK_CODEC = Registries.BLOCK.getCodec().fieldOf("base_block").forGetter(b -> b.baseBlock);

  @SuppressWarnings("unchecked")
  protected static <B extends StandingSignBlock> RecordCodecBuilder<B, Block> baseBlockCodec() {
    return (RecordCodecBuilder<B, Block>) BASE_BLOCK_CODEC;
  }

  public final @Nullable Block baseBlock;
  public @Nullable Identifier baseTexture, barTexture;

  public StandingSignBlock(@Nullable Block baseBlock, Settings settings) {
    super(settings);
    this.baseBlock = baseBlock;
    setDefaultState(getDefaultState().with(WATERLOGGED, false).with(ROTATION, 0).with(DOWN, true));
  }

  public StandingSignBlock(@NotNull Block baseBlock) {
    this(baseBlock, Block.Settings.copy(baseBlock));
  }

  /**
   * 根据 BlockHitResult 来判断玩家点击的告示牌是点击的哪一面（front 或 back）。如果点击的是顶部而无法判断哪一面，则返回 {@code null}。
   */
  @Contract(pure = true)
  public static @Nullable Boolean getHitSide(BlockState blockState, BlockHitResult blockHitResult) {
    final Direction side = blockHitResult.getSide();
    if (side.getAxis().isVertical()) {
      final Vec3d pos = blockHitResult.getPos();
      double minAngle = MathHelper.RADIANS_PER_DEGREE * (360 / 16f * blockState.get(ROTATION));
      double clickAngle = MathHelper.atan2(MathHelper.floorMod(pos.z, 1) - 0.5, MathHelper.floorMod(pos.x, 1) - 0.5);
      return (minAngle < clickAngle && clickAngle < minAngle + MathHelper.PI)
          || (minAngle - 2 * MathHelper.PI < clickAngle && clickAngle < minAngle - MathHelper.PI);
    }
    return getHitSide(blockState, side);
  }

  @Contract(pure = true)
  public static @Nullable Boolean getHitSide(BlockState blockState, Direction side) {
    final int rotation = blockState.get(ROTATION);
    return switch (rotation) {
      case 0 -> switch (side) {
        case NORTH -> Boolean.FALSE;
        case SOUTH -> Boolean.TRUE;
        default -> null;
      };
      case 8 -> switch (side) {
        case SOUTH -> Boolean.FALSE;
        case NORTH -> Boolean.TRUE;
        default -> null;
      };
      case 4 -> switch (side) {
        case EAST -> Boolean.FALSE;
        case WEST -> Boolean.TRUE;
        default -> null;
      };
      case 12 -> switch (side) {
        case WEST -> Boolean.FALSE;
        case EAST -> Boolean.TRUE;
        default -> null;
      };
      case 1, 2, 3 -> switch (side) {
        case WEST, SOUTH -> Boolean.TRUE;
        case EAST, NORTH -> Boolean.FALSE;
        default -> null;
      };
      case 5, 6, 7 -> switch (side) {
        case WEST, NORTH -> Boolean.TRUE;
        case EAST, SOUTH -> Boolean.FALSE;
        default -> null;
      };
      case 9, 10, 11 -> switch (side) {
        case EAST, NORTH -> Boolean.TRUE;
        case WEST, SOUTH -> Boolean.FALSE;
        default -> null;
      };
      case 13, 14, 15 -> switch (side) {
        case EAST, SOUTH -> Boolean.TRUE;
        case WEST, NORTH -> Boolean.FALSE;
        default -> null;
      };
      default -> null;
    };
  }

  @Environment(EnvType.CLIENT)
  public Identifier getBaseTexture() {
    if (baseTexture != null) return baseTexture;
    return BRRPUtils.getTextureId(baseBlock == null ? this : baseBlock, TextureKey.ALL);
  }

  @Override
  protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
    super.appendProperties(builder);
    builder.add(WATERLOGGED, ROTATION, DOWN);
  }

  @Override
  public BlockState getPlacementState(ItemPlacementContext ctx) {
    final World world = ctx.getWorld();
    final BlockPos blockPos = ctx.getBlockPos();
    final BlockState blockState = world.getBlockState(((ItemUsageContextInvoker) ctx).invokeGetHitResult().getBlockPos());
    FluidState fluidState = world.getFluidState(blockPos);
    return this.getDefaultState()
        // 毗邻直立的告示牌放置时，使用相同的方向。
        .with(ROTATION, blockState.getBlock() instanceof StandingSignBlock ? blockState.get(ROTATION) : MathHelper.floor((double) ((180.0F + ctx.getPlayerYaw()) * 16.0F / 360.0F) + 0.5) & 15)
        .with(DOWN, world.getBlockState(blockPos.down()).isSideSolid(world, blockPos.down(), Direction.UP, SideShapeType.CENTER))
        .with(WATERLOGGED, fluidState.getFluid() == Fluids.WATER);
  }

  @Override
  public BlockState getStateForNeighborUpdate(BlockState state, Direction direction, BlockState neighborState, WorldAccess world, BlockPos pos, BlockPos neighborPos) {
    if (state.get(WATERLOGGED)) {
      world.scheduleFluidTick(pos, Fluids.WATER, Fluids.WATER.getTickRate(world));
    }
    final BlockState state1 = super.getStateForNeighborUpdate(state, direction, neighborState, world, pos, neighborPos);
    return direction == Direction.DOWN ? state1.with(DOWN, neighborState.isSideSolid(world, neighborPos, Direction.UP, SideShapeType.CENTER)) : state1;
  }

  @Override
  public FluidState getFluidState(BlockState state) {
    return state.get(WATERLOGGED) ? Fluids.WATER.getStill(false) : super.getFluidState(state);
  }

  @Override
  public BlockState rotate(BlockState state, BlockRotation rotation) {
    return state.with(ROTATION, rotation.rotate(state.get(ROTATION), 16));
  }

  @Override
  public BlockState mirror(BlockState state, BlockMirror mirror) {
    return state.with(ROTATION, mirror.mirror(state.get(ROTATION), 16));
  }

  @Override
  public MutableText getName() {
    if (baseBlock != null) return TextBridge.translatable("block.mishanguc.standing_sign", baseBlock.getName());
    return super.getName();
  }

  @Override
  public void appendTooltip(ItemStack stack, Item.TooltipContext context, List<Text> tooltip, TooltipType options) {
    super.appendTooltip(stack, context, tooltip, options);
    tooltip.add(TextBridge.translatable("block.mishanguc.standing_sign.tooltip.1").formatted(Formatting.GRAY));
    tooltip.add(TextBridge.translatable("block.mishanguc.standing_sign.tooltip.2").formatted(Formatting.GRAY));
  }

  @Environment(EnvType.CLIENT)
  @Override
  public @NotNull ModelJsonBuilder getBlockModel() {
    final Identifier texture = getBaseTexture();
    return ModelJsonBuilder.create(Identifier.of("mishanguc:block/standing_sign")).addTexture("texture", texture).addTexture("bar", barTexture == null ? null : barTexture.toString());
  }

  @Environment(EnvType.CLIENT)
  @Override
  public void writeBlockModel(RuntimeResourcePack pack) {
    final ModelJsonBuilder model = getBlockModel();
    final Identifier modelId = getBlockModelId();
    pack.addModel(modelId, model);
    pack.addModel(modelId.brrp_suffixed("_1"), model.withParent(model.parentId.brrp_suffixed("_1")));
    pack.addModel(modelId.brrp_suffixed("_2"), model.withParent(model.parentId.brrp_suffixed("_2")));
    pack.addModel(modelId.brrp_suffixed("_3"), model.withParent(model.parentId.brrp_suffixed("_3")));
    pack.addModel(modelId.brrp_suffixed("_barred"), model.withParent(model.parentId.brrp_suffixed("_barred")));
    pack.addModel(modelId.brrp_suffixed("_barred_1"), model.withParent(model.parentId.brrp_suffixed("_barred_1")));
    pack.addModel(modelId.brrp_suffixed("_barred_2"), model.withParent(model.parentId.brrp_suffixed("_barred_2")));
    pack.addModel(modelId.brrp_suffixed("_barred_3"), model.withParent(model.parentId.brrp_suffixed("_barred_3")));
  }

  @Environment(EnvType.CLIENT)
  @Override
  public @Nullable BlockStateSupplier getBlockStates() {
    final Identifier modelId = getBlockModelId();
    final BlockStateVariantMap.DoubleProperty<Boolean, Integer> map = BlockStateVariantMap.create(DOWN, ROTATION);
    for (int i = 0; i < 16; i += 4) {
      final int y = i * 90 / 4;
      map.register(false, i, BlockStateVariant.create().put(VariantSettings.MODEL, modelId).put(MishangUtils.INT_Y_VARIANT, y));
      map.register(false, (i + 1), BlockStateVariant.create().put(VariantSettings.MODEL, modelId.brrp_suffixed("_1")).put(MishangUtils.INT_Y_VARIANT, y));
      map.register(false, (i + 2), BlockStateVariant.create().put(VariantSettings.MODEL, modelId.brrp_suffixed("_2")).put(MishangUtils.INT_Y_VARIANT, y));
      map.register(false, (i + 3), BlockStateVariant.create().put(VariantSettings.MODEL, modelId.brrp_suffixed("_3")).put(MishangUtils.INT_Y_VARIANT, y + 90));
      map.register(true, i, BlockStateVariant.create().put(VariantSettings.MODEL, modelId.brrp_suffixed("_barred")).put(MishangUtils.INT_Y_VARIANT, y));
      map.register(true, (i + 1), BlockStateVariant.create().put(VariantSettings.MODEL, modelId.brrp_suffixed("_barred_1")).put(MishangUtils.INT_Y_VARIANT, y));
      map.register(true, (i + 2), BlockStateVariant.create().put(VariantSettings.MODEL, modelId.brrp_suffixed("_barred_2")).put(MishangUtils.INT_Y_VARIANT, y));
      map.register(true, (i + 3), BlockStateVariant.create().put(VariantSettings.MODEL, modelId.brrp_suffixed("_barred_3")).put(MishangUtils.INT_Y_VARIANT, y + 90));
    }
    return VariantsBlockStateSupplier.create(this, BlockStateVariant.create().put(VariantSettings.UVLOCK, true)).coordinate(map);
  }

  @Environment(EnvType.CLIENT)
  @Override
  public @Nullable ModelJsonBuilder getItemModel() {
    return ModelJsonBuilder.create(getBlockModelId().brrp_suffixed("_barred"));
  }

  @Override
  public CraftingRecipeJsonBuilder getCraftingRecipe() {
    if (baseBlock == null) return null;
    return ShapedRecipeJsonBuilder.create(getRecipeCategory(), this, 4)
        .patterns("---", "###", " | ")
        .input('#', baseBlock).input('-', WallSignBlocks.INVISIBLE_WALL_SIGN).input('|', Items.STICK)
        .setCustomRecipeCategory("signs")
        .criterionFromItem("has_base_block", baseBlock).criterionFromItem("has_sign", WallSignBlocks.INVISIBLE_WALL_SIGN);
  }

  @Override
  public @Nullable RecipeCategory getRecipeCategory() {
    return RecipeCategory.DECORATIONS;
  }

  @Override
  public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
    final VoxelShape bodyShape = switch (state.get(ROTATION)) {
      case 0, 8 -> SHAPE_NS;
      case 1, 7, 9, 15 -> SHAPE_NS_WIDE;
      default -> SHAPE_CENTER;
      case 3, 5, 11, 13 -> SHAPE_WE_WIDE;
      case 4, 12 -> SHAPE_WE;
    };
    return state.get(DOWN) ? VoxelShapes.union(bodyShape, BAR_SHAPE) : bodyShape;
  }

  @Nullable
  @Override
  public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
    return new StandingSignBlockEntity(pos, state);
  }

  @Override
  public VoxelShape getCullingShape(BlockState state, BlockView world, BlockPos pos) {
    return state.get(ROTATION) % 4 == 0 && state.get(DOWN) ? CULLING_SHAPE : VoxelShapes.empty();
  }

  @Override
  public VoxelShape getCollisionShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
    return VoxelShapes.empty();
  }

  /**
   * 鉴于其实际外观与碰撞形状不一致，告示牌使用手动的侧面隐形判断。
   */
  @Override
  public boolean isSideInvisible(BlockState state, BlockState stateFrom, Direction direction) {
    if (direction.getAxis().isHorizontal() && stateFrom.getBlock() instanceof StandingSignBlock standingSignBlockFrom) {
      final int r1 = state.get(ROTATION);
      final int r2 = stateFrom.get(ROTATION);
      if ((r1 - r2) % 8 == 0) {
        if (direction.getAxis() == Direction.Axis.X && (r1 == 0 || r1 == 8) || direction.getAxis() == Direction.Axis.Z && (r1 == 4 || r1 == 12)) {
          if (standingSignBlockFrom.baseBlock instanceof TransparentBlock) {
            if (baseBlock instanceof TransparentBlock) {
              // 自身和相邻方块都为透明方块，则双方均为同一方块时隐藏。
              return baseBlock == standingSignBlockFrom.baseBlock;
            } else {
              return false;
            }
          }
          return true;
        }
      }
    }
    return super.isSideInvisible(state, stateFrom, direction);
  }

  @Override
  protected ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, BlockHitResult hit) {
    final ActionResult actionResult = super.onUse(state, world, pos, player, hit);
    if (actionResult.isAccepted()) {
      return actionResult;
    }
    final BlockEntity blockEntity = world.getBlockEntity(pos);
    final Boolean isFront = getHitSide(state, hit);
    if (!(blockEntity instanceof StandingSignBlockEntity entity)) {
      return ActionResult.PASS;
    } else if (player.isSneaking()) {
      // 潜行时点击告示牌，可以切换底部杆子的显示。
      world.setBlockState(pos, state.with(DOWN, !state.get(DOWN)));
      return ActionResult.SUCCESS;
    } else if (isFront == null) return ActionResult.PASS;
    else if (!player.getAbilities().allowModifyWorld) {
      // 冒险模式玩家无权编辑。Adventure players has no permission to edit.
      return ActionResult.FAIL;
    } else if (world.isClient) {
      return ActionResult.SUCCESS;
    }

    entity.checkEditorValidity();
    final PlayerEntity editor = entity.getEditor();
    if (editor != null && editor != player) {
      // 这种情况下，告示牌被占用，玩家无权编辑。
      player.sendMessage(TextBridge.translatable("message.mishanguc.no_editing_permission.occupied", editor.getName()), false);
      return ActionResult.FAIL;
    }
    entity.editedSide = isFront;
    entity.setEditor(player);
    ServerPlayNetworking.send((ServerPlayerEntity) player, new EditSignPayload(pos, Optional.empty(), Optional.of(hit)));
    return ActionResult.SUCCESS;
  }

  @Override
  protected ItemActionResult onUseWithItem(ItemStack stack, BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
    final BlockEntity blockEntity = world.getBlockEntity(pos);
    final Boolean isFront = getHitSide(state, hit);
    if (!(blockEntity instanceof StandingSignBlockEntity entity)) {
      return ItemActionResult.SKIP_DEFAULT_BLOCK_INTERACTION;
    } else if (player.isSneaking()) {
      // 潜行时点击告示牌，可以切换底部杆子的显示。
      world.setBlockState(pos, state.with(DOWN, !state.get(DOWN)));
      return ItemActionResult.SUCCESS;
    } else if (isFront == null) {
      return ItemActionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
    } else if (!player.getAbilities().allowModifyWorld) {
      // 冒险模式玩家无权编辑。Adventure players has no permission to edit.
      return ItemActionResult.FAIL;
    } else if (world.isClient) {
      return ItemActionResult.SUCCESS;
    } else {
      final ItemStack stackInHand = player.getStackInHand(hand);
      if (stackInHand.getItem() instanceof HoneycombItem) {
        // 处理告示牌的涂蜡。
        if (!entity.waxed.contains(isFront.booleanValue())) {
          entity.waxed = addToSet(entity.waxed, isFront);
          player.sendMessage(BlockEntityWithText.MESSAGE_WAX_ON, true);
          world.syncWorldEvent(null, WorldEvents.BLOCK_WAXED, entity.getPos(), 0);
          entity.markDirtyAndUpdate();
          if (!player.isCreative()) stackInHand.decrement(1);
          return ItemActionResult.SUCCESS;
        } else if (player.isCreative()) {
          entity.waxed = removeFromSet(entity.waxed, isFront);
          player.sendMessage(BlockEntityWithText.MESSAGE_WAX_OFF, true);
          world.syncWorldEvent(null, WorldEvents.WAX_REMOVED, entity.getPos(), 0);
          entity.markDirtyAndUpdate();
          return ItemActionResult.SUCCESS;
        }
      }
      if (entity.waxed.contains(isFront.booleanValue())) {
        // 涂蜡的告示牌不应该进行操作。
        world.playSound(null, entity.getPos(), SoundEvents.BLOCK_SIGN_WAXED_INTERACT_FAIL, SoundCategory.BLOCKS);
        return ItemActionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
      } else if (stackInHand.isOf(Items.MAGMA_CREAM)) {
        // 玩家手持岩浆膏时，可快速进行重整。
        MishangUtils.rearrange(entity.getTextsOnSide(isFront));
        entity.markDirtyAndUpdate();
        return ItemActionResult.SUCCESS;
      } else if (stackInHand.getItem() instanceof GlowInkSacItem) {
        if (!entity.glowing.contains(isFront.booleanValue())) {
          entity.glowing = addToSet(entity.glowing, isFront);
          player.sendMessage(BlockEntityWithText.MESSAGE_GLOW_ON, true);
          world.playSound(null, entity.getPos(), SoundEvents.ITEM_GLOW_INK_SAC_USE, SoundCategory.BLOCKS, 1.0F, 1.0F);
          entity.markDirtyAndUpdate();
          if (!player.isCreative()) stackInHand.decrement(1);
          return ItemActionResult.SUCCESS;
        }
      } else if (stackInHand.getItem() instanceof InkSacItem) {
        if (entity.glowing.contains(isFront.booleanValue())) {
          entity.glowing = removeFromSet(entity.glowing, isFront);
          player.sendMessage(BlockEntityWithText.MESSAGE_GLOW_OFF, true);
          world.playSound(null, entity.getPos(), SoundEvents.ITEM_INK_SAC_USE, SoundCategory.BLOCKS, 1.0F, 1.0F);
          entity.markDirtyAndUpdate();
          if (!player.isCreative()) stackInHand.decrement(1);
          return ItemActionResult.SUCCESS;
        }
      }
    }
    return ItemActionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
  }

  @Override
  protected MapCodec<? extends StandingSignBlock> getCodec() {
    return CODEC;
  }

  private static BooleanSet addToSet(BooleanSet set, boolean element) {
    if (set.isEmpty()) {
      final BooleanSet newSet = new BooleanArraySet(2);
      newSet.add(element);
      return newSet;
    } else {
      set.add(element);
      return set;
    }
  }

  private static BooleanSet removeFromSet(BooleanSet set, boolean element) {
    if (set.isEmpty()) {
      return set;
    } else if (set.remove(element) && set.isEmpty()) {
      return BooleanSets.emptySet();
    } else {
      set.remove(element);
      return set;
    }
  }
}
