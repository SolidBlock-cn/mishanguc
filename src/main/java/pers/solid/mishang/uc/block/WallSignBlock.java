package pers.solid.mishang.uc.block;

import com.google.common.collect.ImmutableMap;
import com.mojang.math.Quadrant;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.ChatFormatting;
import net.minecraft.client.data.models.BlockModelGenerators;
import net.minecraft.client.data.models.ModelProvider;
import net.minecraft.client.data.models.blockstates.MultiVariantGenerator;
import net.minecraft.client.data.models.blockstates.PropertyDispatch;
import net.minecraft.client.data.models.model.ItemModelUtils;
import net.minecraft.client.data.models.model.TextureMapping;
import net.minecraft.client.renderer.block.model.VariantMutator;
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
import net.minecraft.world.level.block.state.properties.AttachFace;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.Unmodifiable;
import pers.solid.mishang.uc.MishangUtils;
import pers.solid.mishang.uc.blockentity.BlockEntityWithText;
import pers.solid.mishang.uc.blockentity.WallSignBlockEntity;
import pers.solid.mishang.uc.blocks.WallSignBlocks;
import pers.solid.mishang.uc.data.MishangucModels;
import pers.solid.mishang.uc.data.ModelHelper;
import pers.solid.mishang.uc.item.ColoredTintSource;
import pers.solid.mishang.uc.mixin.BlockStateModelGeneratorAccessor;
import pers.solid.mishang.uc.networking.EditSignPayload;
import pers.solid.mishang.uc.render.WallSignBlockEntityRenderer;
import pers.solid.mishang.uc.util.TextBridge;
import pers.solid.mishang.uc.util.WithMishangTooltip;

import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * 与 Minecraft 原版的 {@link net.minecraft.world.level.block.WallSignBlock} 不同，这里的 {@code WallSignBlock}
 * 更加强大，可以编辑，且可以放在地上或者天花板上。
 *
 * @see WallSignBlocks
 * @see WallSignBlockEntity
 * @see WallSignBlockEntityRenderer
 */
public class WallSignBlock extends FaceAttachedHorizontalDirectionalBlock implements SimpleWaterloggedBlock, EntityBlock, MishangucBlock, WithMishangTooltip {
  protected static <B extends WallSignBlock> RecordCodecBuilder<B, Block> createBaseBlockCodec() {
    return BuiltInRegistries.BLOCK.byNameCodec().fieldOf("base_block").forGetter(b -> b.baseBlock);
  }

  public static final MapCodec<WallSignBlock> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(createBaseBlockCodec(), propertiesCodec()).apply(instance, WallSignBlock::new));

  public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;
  public static final Map<Direction, VoxelShape> SHAPES_WHEN_WALL =
      MishangUtils.createHorizontalDirectionToShape(0, 4, 0, 16, 12, 1);
  public static final Map<Direction, VoxelShape> SHAPES_WHEN_FLOOR =
      MishangUtils.createHorizontalDirectionToShape(0, 0, 4, 16, 1, 12);
  public static final Map<Direction, VoxelShape> SHAPES_WHEN_CEILING =
      MishangUtils.createHorizontalDirectionToShape(0, 15, 4, 16, 16, 12);
  @Unmodifiable
  public static final Map<AttachFace, Map<Direction, VoxelShape>>
      SHAPE_PER_WALL_MOUNT_LOCATION =
      ImmutableMap.of(
          AttachFace.CEILING,
          SHAPES_WHEN_CEILING,
          AttachFace.FLOOR,
          SHAPES_WHEN_FLOOR,
          AttachFace.WALL,
          SHAPES_WHEN_WALL);
  public final Block baseBlock;
  /**
   * 告示牌自身的纹理。默认为 {@code null}，可在后期修改。若为 {@code null}，则直接根据其基础方块 {@link #baseBlock} 推断纹理。
   */
  @ApiStatus.AvailableSince("0.1.7")
  public @Nullable Identifier texture;

  public WallSignBlock(@Nullable Block baseBlock, Properties settings) {
    super(settings);
    this.baseBlock = baseBlock;
    registerDefaultState(defaultBlockState()
        .setValue(FACING, Direction.SOUTH)
        .setValue(FACE, AttachFace.WALL)
        .setValue(WATERLOGGED, false));
  }

  @Override
  protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
    super.createBlockStateDefinition(builder);
    builder.add(FACE, FACING, WATERLOGGED);
  }

  @Nullable
  @Override
  public BlockState getStateForPlacement(BlockPlaceContext ctx) {
    final BlockState placementState = super.getStateForPlacement(ctx);
    return placementState != null
        ? placementState.setValue(
        WATERLOGGED, ctx.getLevel().getFluidState(ctx.getClickedPos()).getType() == Fluids.WATER)
        : null;
  }

  @Override
  public boolean canSurvive(BlockState state, LevelReader world, BlockPos pos) {
    return true;
  }

  @Override
  public VoxelShape getShape(
      BlockState state, BlockGetter world, BlockPos pos, CollisionContext context) {
    return SHAPE_PER_WALL_MOUNT_LOCATION.get(state.getValue(FACE)).get(state.getValue(FACING));
  }

  @Override
  public FluidState getFluidState(BlockState state) {
    return state.getValue(WATERLOGGED) ? Fluids.WATER.getSource(false) : super.getFluidState(state);
  }

  @Override
  protected BlockState updateShape(BlockState state, LevelReader world, ScheduledTickAccess tickView, BlockPos pos, Direction direction, BlockPos neighborPos, BlockState neighborState, RandomSource random) {
    state = super.updateShape(state, world, tickView, pos, direction, neighborPos, neighborState, random);
    if (state.getValue(WATERLOGGED)) {
      tickView.scheduleTick(pos, Fluids.WATER, Fluids.WATER.getTickDelay(world));
    }
    return state;
  }

  @Override
  public MutableComponent getName() {
    return baseBlock == null
        ? super.getName()
        : TextBridge.translatable("block.mishanguc.wall_sign", baseBlock.getName());
  }

  @Override
  public void getMishangTooltip(ItemStack stack, Item.TooltipContext context, List<Component> tooltip, TooltipFlag options) {
    tooltip.add(TextBridge.translatable("block.mishanguc.wall_sign.tooltip.1").withStyle(ChatFormatting.GRAY));
    tooltip.add(TextBridge.translatable("block.mishanguc.wall_sign.tooltip.2").withStyle(ChatFormatting.GRAY));
  }

  @Override
  protected InteractionResult useWithoutItem(BlockState state, Level world, BlockPos pos, Player player, BlockHitResult hit) {
    final InteractionResult actionResult = super.useWithoutItem(state, world, pos, player, hit);
    if (actionResult != InteractionResult.PASS) return actionResult;
    // 在服务端触发打开告示牌编辑界面。Open the edit interface, triggered in the server side.
    final BlockEntity blockEntity = world.getBlockEntity(pos);
    if (!(blockEntity instanceof final WallSignBlockEntity entity)) {
      return InteractionResult.PASS;
    } else if (!player.getAbilities().mayBuild) {
      // 冒险模式玩家无权编辑。Adventure players have no permission to edit.
      return InteractionResult.FAIL;
    } else if (world.isClientSide()) {
      return InteractionResult.SUCCESS;
    }

    entity.checkEditorValidity();
    Player editor = entity.getEditor();
    if (editor != null && editor != player) {
      // 这种情况下，告示牌被占用，玩家无权编辑。
      // In this case, the sign is occupied, and the player has no editing
      // permission.
      player.displayClientMessage(TextBridge.translatable("message.mishanguc.no_editing_permission.occupied", editor.getName()), false);
      return InteractionResult.FAIL;
    }
    // 此时告示牌已被编辑。
    entity.setEditor(player);
    ServerPlayNetworking.send(((ServerPlayer) player), new EditSignPayload(pos, Optional.of(hit.getDirection()), Optional.empty()));
    return InteractionResult.SUCCESS;
  }

  @Override
  protected InteractionResult useItemOn(ItemStack stack, BlockState state, Level world, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
    // 在服务端触发打开告示牌编辑界面。
    final BlockEntity blockEntity = world.getBlockEntity(pos);
    if (!(blockEntity instanceof final WallSignBlockEntity entity)) {
      return InteractionResult.TRY_WITH_EMPTY_HAND;
    } else if (!player.getAbilities().mayBuild) {
      // 冒险模式玩家无权编辑。Adventure players have no permission to edit.
      return InteractionResult.FAIL;
    } else if (world.isClientSide()) {
      return InteractionResult.SUCCESS;
    } else {
      if (stack.getItem() instanceof HoneycombItem) {
        // 处理告示牌的涂蜡。
        if (!entity.waxed) {
          entity.waxed = true;
          player.displayClientMessage(BlockEntityWithText.MESSAGE_WAX_ON, true);
          world.levelEvent(null, LevelEvent.PARTICLES_AND_SOUND_WAX_ON, entity.getBlockPos(), 0);
          entity.markDirtyAndUpdate();
          if (!player.isCreative()) stack.shrink(1);
          return InteractionResult.SUCCESS;
        } else if (player.isCreative()) {
          entity.waxed = false;
          player.displayClientMessage(BlockEntityWithText.MESSAGE_WAX_OFF, true);
          world.levelEvent(null, LevelEvent.PARTICLES_WAX_OFF, entity.getBlockPos(), 0);
          entity.markDirtyAndUpdate();
          return InteractionResult.SUCCESS;
        }
      }
      if (entity.waxed) {
        // 涂蜡的告示牌不应该进行操作。
        world.playSound(null, entity.getBlockPos(), SoundEvents.WAXED_SIGN_INTERACT_FAIL, SoundSource.BLOCKS);
        return InteractionResult.TRY_WITH_EMPTY_HAND;
      } else if (stack.is(Items.MAGMA_CREAM)) {
        MishangUtils.rearrange(entity.textContexts);
        entity.markDirtyAndUpdate();
        if (!player.isCreative()) stack.shrink(1);
        return InteractionResult.SUCCESS;
      } else if (stack.getItem() instanceof GlowInkSacItem) {
        if (!entity.glowing) {
          entity.glowing = true;
          player.displayClientMessage(BlockEntityWithText.MESSAGE_GLOW_ON, true);
          world.playSound(null, entity.getBlockPos(), SoundEvents.GLOW_INK_SAC_USE, SoundSource.BLOCKS, 1.0F, 1.0F);
          entity.markDirtyAndUpdate();
          if (!player.isCreative()) stack.shrink(1);
          return InteractionResult.SUCCESS;
        }
      } else if (stack.getItem() instanceof InkSacItem) {
        if (entity.glowing) {
          entity.glowing = false;
          player.displayClientMessage(BlockEntityWithText.MESSAGE_GLOW_OFF, true);
          world.playSound(null, entity.getBlockPos(), SoundEvents.INK_SAC_USE, SoundSource.BLOCKS, 1.0F, 1.0F);
          entity.markDirtyAndUpdate();
          if (!player.isCreative()) stack.shrink(1);
          return InteractionResult.SUCCESS;
        }
      }
    }

    return InteractionResult.TRY_WITH_EMPTY_HAND;
  }

  @Nullable
  @Override
  public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
    return new WallSignBlockEntity(pos, state);
  }

  @Environment(EnvType.CLIENT)
  @Override
  public void registerModels(ModelProvider modelProvider, BlockModelGenerators blockStateModelGenerator) {
    final TextureMapping textures = TextureMapping.defaultTexture(getBaseTexture());
    final Identifier modelId = MishangucModels.WALL_SIGN.create(this, textures, blockStateModelGenerator.modelOutput);
    blockStateModelGenerator.blockStateOutput.accept(createBlockStates(modelId));
    if (this instanceof ColoredBlock) {
      blockStateModelGenerator.itemModelOutput.accept(asItem(), ItemModelUtils.tintedModel(modelId, ColoredTintSource.INSTANCE));
    }
  }

  @Environment(EnvType.CLIENT)
  public MultiVariantGenerator createBlockStates(Identifier modelId) {
    return BlockModelGenerators.createSimpleBlock(this, BlockModelGenerators.plainVariant(modelId))
        .with(PropertyDispatch.modify(FACE).generate((wallMountLocation) -> {
          final Quadrant x = switch (wallMountLocation) {
            case WALL -> Quadrant.R0;
            case FLOOR -> Quadrant.R90;
            default -> Quadrant.R270;
          };
          return VariantMutator.X_ROT.withValue(x);
        }))
        .with(BlockStateModelGeneratorAccessor.getROTATION_HORIZONTAL_FACING_ALT())
        .with(BlockModelGenerators.UV_LOCK);
  }

  @Environment(EnvType.CLIENT)
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
  public @Nullable RecipeBuilder getCraftingRecipe(RecipeProvider recipeGenerator) {
    if (baseBlock == null) return null;
    return recipeGenerator.shaped(RecipeCategory.DECORATIONS, this, 6)
        .pattern("---")
        .pattern("###")
        .pattern("---")
        .define('#', baseBlock).define('-', WallSignBlocks.INVISIBLE_WALL_SIGN)
        .unlockedBy("has_base_block", recipeGenerator.has(baseBlock))
        .unlockedBy("has_sign", recipeGenerator.has(WallSignBlocks.INVISIBLE_WALL_SIGN))
        .group(getRecipeGroup());
  }

  @Override
  public boolean skipRendering(BlockState state, BlockState stateFrom, Direction direction) {
    if (direction.getAxis().isHorizontal() && state.getBlock() instanceof WallSignBlock && stateFrom.getBlock() instanceof WallSignBlock wallSignBlockFrom && state.getValue(FACING) == stateFrom.getValue(FACING) && direction.getAxis() != state.getValue(FACING).getAxis()) {
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
      return super.skipRendering(state, stateFrom, direction);
    }
  }

  @Override
  protected MapCodec<? extends WallSignBlock> codec() {
    return CODEC;
  }

  @Override
  public String customRecipeCategory() {
    return "wall_signs";
  }

  @Override
  protected boolean isPathfindable(BlockState state, PathComputationType type) {
    return false;
  }
}
