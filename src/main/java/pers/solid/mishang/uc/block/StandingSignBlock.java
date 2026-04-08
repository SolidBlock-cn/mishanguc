package pers.solid.mishang.uc.block;

import com.mojang.math.Quadrant;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import it.unimi.dsi.fastutil.booleans.BooleanArraySet;
import it.unimi.dsi.fastutil.booleans.BooleanSet;
import it.unimi.dsi.fastutil.booleans.BooleanSets;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.ChatFormatting;
import net.minecraft.client.data.models.BlockModelGenerators;
import net.minecraft.client.data.models.ModelProvider;
import net.minecraft.client.data.models.blockstates.BlockModelDefinitionGenerator;
import net.minecraft.client.data.models.blockstates.MultiVariantGenerator;
import net.minecraft.client.data.models.blockstates.PropertyDispatch;
import net.minecraft.client.data.models.model.ItemModelUtils;
import net.minecraft.client.data.models.model.TextureMapping;
import net.minecraft.client.renderer.block.dispatch.VariantMutator;
import net.minecraft.client.resources.model.sprite.Material;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.recipes.RecipeBuilder;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.ScheduledTickAccess;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.Nullable;
import pers.solid.mishang.uc.MishangUtils;
import pers.solid.mishang.uc.blockentity.BlockEntityWithText;
import pers.solid.mishang.uc.blockentity.StandingSignBlockEntity;
import pers.solid.mishang.uc.blocks.WallSignBlocks;
import pers.solid.mishang.uc.data.MishangucModels;
import pers.solid.mishang.uc.data.MishangucTextureKeys;
import pers.solid.mishang.uc.data.ModelHelper;
import pers.solid.mishang.uc.item.ColoredTintSource;
import pers.solid.mishang.uc.mixin.ItemUsageContextInvoker;
import pers.solid.mishang.uc.networking.EditSignPayload;
import pers.solid.mishang.uc.util.WithMishangTooltip;

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
public class StandingSignBlock extends Block implements EntityBlock, SimpleWaterloggedBlock, MishangucBlock, WithMishangTooltip {
  public static final MapCodec<StandingSignBlock> CODEC = RecordCodecBuilder.mapCodec(i -> i.group(baseBlockCodec(), propertiesCodec()).apply(i, StandingSignBlock::new));

  public static final IntegerProperty ROTATION = BlockStateProperties.ROTATION_16;
  public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;
  /**
   * 指定告示牌底部是否有杆子。默认取决于底部方块的侧面形状。按住 Shift 并点击告示牌可以切换。
   */
  public static final BooleanProperty DOWN = BlockStateProperties.DOWN;
  protected static final VoxelShape SHAPE_NS = box(0, 8, 6.5, 16, 16, 9.5);
  protected static final VoxelShape SHAPE_WE = box(6.5, 8, 0, 9.5, 16, 16);
  protected static final VoxelShape SHAPE_NS_WIDE = box(2, 8, 5, 14, 16, 11);
  protected static final VoxelShape SHAPE_WE_WIDE = box(5, 8, 2, 11, 16, 14);
  protected static final VoxelShape SHAPE_CENTER = box(2.5, 8, 2.5, 13.5, 16, 13.5);
  protected static final VoxelShape CULLING_SHAPE = box(7.5, 0, 7.5, 8.5, 8, 8.5);
  protected static final VoxelShape BAR_SHAPE = box(6.5, 0, 6.5, 9.5, 8, 9.5);
  protected static final RecordCodecBuilder<? extends StandingSignBlock, Block> BASE_BLOCK_CODEC = BuiltInRegistries.BLOCK.byNameCodec().fieldOf("base_block").forGetter(b -> b.baseBlock);

  @SuppressWarnings("unchecked")
  protected static <B extends StandingSignBlock> RecordCodecBuilder<B, Block> baseBlockCodec() {
    return (RecordCodecBuilder<B, Block>) BASE_BLOCK_CODEC;
  }

  public final @Nullable Block baseBlock;
  public @Nullable Material baseMaterial, barMaterial;

  public StandingSignBlock(@Nullable Block baseBlock, Properties settings) {
    super(settings);
    this.baseBlock = baseBlock;
    registerDefaultState(defaultBlockState().setValue(WATERLOGGED, false).setValue(ROTATION, 0).setValue(DOWN, true));
  }

  /**
   * 根据 BlockHitResult 来判断玩家点击的告示牌是点击的哪一面（front 或 back）。如果点击的是顶部而无法判断哪一面，则返回 {@code null}。
   */
  @Contract(pure = true)
  public static @Nullable Boolean getHitSide(BlockState blockState, BlockHitResult blockHitResult) {
    final Direction side = blockHitResult.getDirection();
    if (side.getAxis().isVertical()) {
      final Vec3 pos = blockHitResult.getLocation();
      double minAngle = Mth.DEG_TO_RAD * (360 / 16f * blockState.getValueOrElse(ROTATION, 0));
      double clickAngle = Mth.atan2(Mth.positiveModulo(pos.z, 1) - 0.5, Mth.positiveModulo(pos.x, 1) - 0.5);
      return (minAngle < clickAngle && clickAngle < minAngle + Mth.PI)
          || (minAngle - 2 * Mth.PI < clickAngle && clickAngle < minAngle - Mth.PI);
    }
    return getHitSide(blockState, side);
  }

  @Contract(pure = true)
  public static @Nullable Boolean getHitSide(BlockState blockState, Direction side) {
    final int rotation = blockState.getValue(ROTATION);
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
  public Material getBaseMaterial() {
    if (baseMaterial != null) return baseMaterial;
    return ModelHelper.getMaterialOf(baseBlock == null ? this : baseBlock);
  }

  @Override
  protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
    super.createBlockStateDefinition(builder);
    builder.add(WATERLOGGED, ROTATION, DOWN);
  }

  @Override
  public BlockState getStateForPlacement(BlockPlaceContext ctx) {
    final Level world = ctx.getLevel();
    final BlockPos blockPos = ctx.getClickedPos();
    final BlockState blockState = world.getBlockState(((ItemUsageContextInvoker) ctx).invokeGetHitResult().getBlockPos());
    FluidState fluidState = world.getFluidState(blockPos);
    return this.defaultBlockState()
        // 毗邻直立的告示牌放置时，使用相同的方向。
        .setValue(ROTATION, blockState.getBlock() instanceof StandingSignBlock ? blockState.getValue(ROTATION) : Mth.floor((double) ((180.0F + ctx.getRotation()) * 16.0F / 360.0F) + 0.5) & 15)
        .setValue(DOWN, world.getBlockState(blockPos.below()).isFaceSturdy(world, blockPos.below(), Direction.UP, SupportType.CENTER))
        .setValue(WATERLOGGED, fluidState.getType() == Fluids.WATER);
  }

  @Override
  protected BlockState updateShape(BlockState state, LevelReader world, ScheduledTickAccess tickView, BlockPos pos, Direction direction, BlockPos neighborPos, BlockState neighborState, RandomSource random) {
    if (state.getValue(WATERLOGGED)) {
      tickView.scheduleTick(pos, Fluids.WATER, Fluids.WATER.getTickDelay(world));
    }
    final BlockState state1 = super.updateShape(state, world, tickView, pos, direction, neighborPos, neighborState, random);
    return direction == Direction.DOWN ? state1.setValue(DOWN, neighborState.isFaceSturdy(world, neighborPos, Direction.UP, SupportType.CENTER)) : state1;
  }

  @Override
  public FluidState getFluidState(BlockState state) {
    return state.getValue(WATERLOGGED) ? Fluids.WATER.getSource(false) : super.getFluidState(state);
  }

  @Override
  public BlockState rotate(BlockState state, Rotation rotation) {
    return state.setValue(ROTATION, rotation.rotate(state.getValue(ROTATION), 16));
  }

  @Override
  public BlockState mirror(BlockState state, Mirror mirror) {
    return state.setValue(ROTATION, mirror.mirror(state.getValue(ROTATION), 16));
  }

  @Override
  public MutableComponent getName() {
    if (baseBlock != null) return Component.translatable("block.mishanguc.standing_sign", baseBlock.getName());
    return super.getName();
  }

  @Override
  public void getMishangTooltip(ItemStack stack, Item.TooltipContext context, List<Component> tooltip, TooltipFlag options) {
    tooltip.add(Component.translatable("block.mishanguc.standing_sign.tooltip.1").withStyle(ChatFormatting.GRAY));
    tooltip.add(Component.translatable("block.mishanguc.standing_sign.tooltip.2").withStyle(ChatFormatting.GRAY));
  }

  @Environment(EnvType.CLIENT)
  @Override
  public void registerModels(ModelProvider modelProvider, BlockModelGenerators blockStateModelGenerator) {
    final TextureMapping textures = TextureMapping.defaultTexture(getBaseMaterial()).put(MishangucTextureKeys.BAR, barMaterial);
    final Identifier modelId = MishangucModels.STANDING_SIGN.create(this, textures, blockStateModelGenerator.modelOutput);
    final Identifier r1ModelId = MishangucModels.STANDING_SIGN_1.create(this, textures, blockStateModelGenerator.modelOutput);
    final Identifier r2ModelId = MishangucModels.STANDING_SIGN_2.create(this, textures, blockStateModelGenerator.modelOutput);
    final Identifier r3ModelId = MishangucModels.STANDING_SIGN_3.create(this, textures, blockStateModelGenerator.modelOutput);
    final Identifier barredModelId = MishangucModels.STANDING_SIGN_BARRED.create(this, textures, blockStateModelGenerator.modelOutput);
    final Identifier barredR1ModelId = MishangucModels.STANDING_SIGN_BARRED_1.create(this, textures, blockStateModelGenerator.modelOutput);
    final Identifier barredR2ModelId = MishangucModels.STANDING_SIGN_BARRED_2.create(this, textures, blockStateModelGenerator.modelOutput);
    final Identifier barredR3ModelId = MishangucModels.STANDING_SIGN_BARRED_3.create(this, textures, blockStateModelGenerator.modelOutput);
    blockStateModelGenerator.blockStateOutput.accept(createBlockStates(modelId, r1ModelId, r2ModelId, r3ModelId, barredModelId, barredR1ModelId, barredR2ModelId, barredR3ModelId));

    if (this instanceof ColoredBlock) {
      blockStateModelGenerator.itemModelOutput.accept(asItem(), ItemModelUtils.tintedModel(barredModelId, ColoredTintSource.INSTANCE, ColoredTintSource.INSTANCE));
    } else {
      blockStateModelGenerator.registerSimpleItemModel(this, barredModelId);
    }
  }

  @Environment(EnvType.CLIENT)
  public @Nullable BlockModelDefinitionGenerator createBlockStates(Identifier modelId, Identifier r1ModelId, Identifier r2ModelId, Identifier r3ModelId, Identifier barredModelId, Identifier barredR1ModelId, Identifier barredR2ModelId, Identifier barredR3ModelId) {
    final var map = PropertyDispatch.initial(DOWN, ROTATION);
    final Quadrant[] axisRotations = Quadrant.values();
    for (int i = 0; i < 16; i += 4) {
      final int y = i * 90 / 4;
      final Quadrant axisRotation = axisRotations[y / 90];
      final Quadrant axisRotationNext = axisRotations[(y / 90 + 1) % 4];
      map.select(false, i, BlockModelGenerators.plainVariant(modelId).with(VariantMutator.Y_ROT.withValue(axisRotation)));
      map.select(false, (i + 1), BlockModelGenerators.plainVariant(r1ModelId).with(VariantMutator.Y_ROT.withValue(axisRotation)));
      map.select(false, (i + 2), BlockModelGenerators.plainVariant(r2ModelId).with(VariantMutator.Y_ROT.withValue(axisRotation)));
      map.select(false, (i + 3), BlockModelGenerators.plainVariant(r3ModelId).with(VariantMutator.Y_ROT.withValue(axisRotationNext)));
      map.select(true, i, BlockModelGenerators.plainVariant(barredModelId).with(VariantMutator.Y_ROT.withValue(axisRotation)));
      map.select(true, (i + 1), BlockModelGenerators.plainVariant(barredR1ModelId).with(VariantMutator.Y_ROT.withValue(axisRotation)));
      map.select(true, (i + 2), BlockModelGenerators.plainVariant(barredR2ModelId).with(VariantMutator.Y_ROT.withValue(axisRotation)));
      map.select(true, (i + 3), BlockModelGenerators.plainVariant(barredR3ModelId).with(VariantMutator.Y_ROT.withValue(axisRotationNext)));
    }
    return MultiVariantGenerator.dispatch(this).with(map).with(BlockModelGenerators.UV_LOCK);
  }

  private @Nullable String getRecipeGroup() {
    if (baseBlock instanceof ColoredBlock) return null;
    if (MishangUtils.isWood(baseBlock)) return "mishanguc:wood_standing_sign";
    if (MishangUtils.isStrippedWood(baseBlock)) return "mishanguc:stripped_wood_standing_sign";
    if (MishangUtils.isPlanks(baseBlock)) return "mishanguc:plank_standing_sign";
    if (MishangUtils.isConcrete(baseBlock)) return "mishanguc:concrete_standing_sign";
    if (MishangUtils.isTerracotta(baseBlock)) return "mishanguc:terracotta_standing_sign";
    if (baseBlock == Blocks.ICE || baseBlock == Blocks.PACKED_ICE || baseBlock == Blocks.BLUE_ICE) {
      return "mishanguc:ice_standing_sign";
    }
    return null;
  }

  @Override
  public RecipeBuilder getCraftingRecipe(RecipeProvider recipeGenerator) {
    if (baseBlock == null) return null;
    return recipeGenerator.shaped(RecipeCategory.DECORATIONS, this, 4)
        .pattern("---")
        .pattern("###")
        .pattern(" | ")
        .define('#', baseBlock).define('-', WallSignBlocks.INVISIBLE_WALL_SIGN).define('|', Items.STICK)
        .unlockedBy("has_base_block", recipeGenerator.has(baseBlock))
        .unlockedBy("has_sign", recipeGenerator.has(WallSignBlocks.INVISIBLE_WALL_SIGN))
        .group(getRecipeGroup());
  }

  @Override
  public VoxelShape getShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext context) {
    final VoxelShape bodyShape = switch (state.getValue(ROTATION)) {
      case 0, 8 -> SHAPE_NS;
      case 1, 7, 9, 15 -> SHAPE_NS_WIDE;
      case 3, 5, 11, 13 -> SHAPE_WE_WIDE;
      case 4, 12 -> SHAPE_WE;
      default -> SHAPE_CENTER;
    };
    return state.getValue(DOWN) ? Shapes.or(bodyShape, BAR_SHAPE) : bodyShape;
  }

  @Nullable
  @Override
  public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
    return new StandingSignBlockEntity(pos, state);
  }

  @Override
  protected VoxelShape getOcclusionShape(BlockState state) {
    return state.getValue(ROTATION) % 4 == 0 && state.getValue(DOWN) ? CULLING_SHAPE : Shapes.empty();
  }

  @Override
  public VoxelShape getCollisionShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext context) {
    return Shapes.empty();
  }

  /**
   * 鉴于其实际外观与碰撞形状不一致，告示牌使用手动的侧面隐形判断。
   */
  @Override
  public boolean skipRendering(BlockState state, BlockState stateFrom, Direction direction) {
    if (direction.getAxis().isHorizontal() && stateFrom.getBlock() instanceof StandingSignBlock standingSignBlockFrom) {
      final int r1 = state.getValue(ROTATION);
      final int r2 = stateFrom.getValue(ROTATION);
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
    return super.skipRendering(state, stateFrom, direction);
  }

  @Override
  protected InteractionResult useWithoutItem(BlockState state, Level world, BlockPos pos, Player player, BlockHitResult hit) {
    final InteractionResult actionResult = super.useWithoutItem(state, world, pos, player, hit);
    if (actionResult.consumesAction()) {
      return actionResult;
    }
    final BlockEntity blockEntity = world.getBlockEntity(pos);
    final Boolean isFront = getHitSide(state, hit);
    if (!(blockEntity instanceof StandingSignBlockEntity entity)) {
      return InteractionResult.PASS;
    } else if (player.isShiftKeyDown()) {
      // 潜行时点击告示牌，可以切换底部杆子的显示。
      world.setBlockAndUpdate(pos, state.setValue(DOWN, !state.getValue(DOWN)));
      return InteractionResult.SUCCESS;
    } else if (isFront == null) return InteractionResult.PASS;
    else if (!player.getAbilities().mayBuild) {
      // 冒险模式玩家无权编辑。Adventure players have no permission to edit.
      return InteractionResult.FAIL;
    } else if (world.isClientSide()) {
      return InteractionResult.SUCCESS;
    }

    entity.checkEditorValidity();
    final Player editor = entity.getEditor();
    if (editor != null && editor != player) {
      // 这种情况下，告示牌被占用，玩家无权编辑。
      player.sendOverlayMessage(Component.translatable("message.mishanguc.no_editing_permission.occupied", editor.getName()));
      return InteractionResult.FAIL;
    }
    entity.editedSide = isFront;
    entity.setEditor(player);
    ServerPlayNetworking.send((ServerPlayer) player, new EditSignPayload(pos, Optional.empty(), Optional.of(hit)));
    return InteractionResult.SUCCESS;
  }

  @Override
  protected InteractionResult useItemOn(ItemStack stack, BlockState state, Level world, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
    final BlockEntity blockEntity = world.getBlockEntity(pos);
    final Boolean isFront = getHitSide(state, hit);
    if (!(blockEntity instanceof StandingSignBlockEntity entity)) {
      return InteractionResult.TRY_WITH_EMPTY_HAND;
    } else if (player.isShiftKeyDown()) {
      // 潜行时点击告示牌，可以切换底部杆子的显示。
      world.setBlockAndUpdate(pos, state.setValue(DOWN, !state.getValue(DOWN)));
      return InteractionResult.SUCCESS;
    } else if (isFront == null) {
      return InteractionResult.TRY_WITH_EMPTY_HAND;
    } else if (!player.getAbilities().mayBuild) {
      // 冒险模式玩家无权编辑。Adventure players have no permission to edit.
      return InteractionResult.FAIL;
    } else if (world.isClientSide()) {
      return InteractionResult.SUCCESS;
    } else {
      final ItemStack stackInHand = player.getItemInHand(hand);
      if (stackInHand.getItem() instanceof HoneycombItem) {
        // 处理告示牌的涂蜡。
        if (!entity.waxed.contains(isFront.booleanValue())) {
          entity.waxed = addToSet(entity.waxed, isFront);
          player.sendOverlayMessage(BlockEntityWithText.MESSAGE_WAX_ON);
          world.levelEvent(null, LevelEvent.PARTICLES_AND_SOUND_WAX_ON, entity.getBlockPos(), 0);
          entity.markDirtyAndUpdate();
          if (!player.isCreative()) stackInHand.shrink(1);
          return InteractionResult.SUCCESS;
        } else if (player.isCreative()) {
          entity.waxed = removeFromSet(entity.waxed, isFront);
          player.sendOverlayMessage(BlockEntityWithText.MESSAGE_WAX_OFF);
          world.levelEvent(null, LevelEvent.PARTICLES_WAX_OFF, entity.getBlockPos(), 0);
          entity.markDirtyAndUpdate();
          return InteractionResult.SUCCESS;
        }
      }
      if (entity.waxed.contains(isFront.booleanValue())) {
        // 涂蜡的告示牌不应该进行操作。
        world.playSound(null, entity.getBlockPos(), SoundEvents.WAXED_SIGN_INTERACT_FAIL, SoundSource.BLOCKS);
        return InteractionResult.TRY_WITH_EMPTY_HAND;
      } else if (stackInHand.is(Items.MAGMA_CREAM)) {
        // 玩家手持岩浆膏时，可快速进行重整。
        MishangUtils.rearrange(entity.getTextsOnSide(isFront));
        entity.markDirtyAndUpdate();
        return InteractionResult.SUCCESS;
      } else if (stackInHand.getItem() instanceof GlowInkSacItem) {
        if (!entity.glowing.contains(isFront.booleanValue())) {
          entity.glowing = addToSet(entity.glowing, isFront);
          player.sendOverlayMessage(BlockEntityWithText.MESSAGE_GLOW_ON);
          world.playSound(null, entity.getBlockPos(), SoundEvents.GLOW_INK_SAC_USE, SoundSource.BLOCKS, 1.0F, 1.0F);
          entity.markDirtyAndUpdate();
          if (!player.isCreative()) stackInHand.shrink(1);
          return InteractionResult.SUCCESS;
        }
      } else if (stackInHand.getItem() instanceof InkSacItem) {
        if (entity.glowing.contains(isFront.booleanValue())) {
          entity.glowing = removeFromSet(entity.glowing, isFront);
          player.sendOverlayMessage(BlockEntityWithText.MESSAGE_GLOW_OFF);
          world.playSound(null, entity.getBlockPos(), SoundEvents.INK_SAC_USE, SoundSource.BLOCKS, 1.0F, 1.0F);
          entity.markDirtyAndUpdate();
          if (!player.isCreative()) stackInHand.shrink(1);
          return InteractionResult.SUCCESS;
        }
      }
    }
    return InteractionResult.TRY_WITH_EMPTY_HAND;
  }

  @Override
  protected MapCodec<? extends StandingSignBlock> codec() {
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

  @Override
  public String customRecipeCategory() {
    return "signs";
  }
}
