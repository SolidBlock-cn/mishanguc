package pers.solid.mishang.uc.block;

import it.unimi.dsi.fastutil.objects.Reference2ReferenceMap;
import it.unimi.dsi.fastutil.objects.Reference2ReferenceOpenHashMap;
import net.devtech.arrp.api.RuntimeResourcePack;
import net.devtech.arrp.generator.BlockResourceGenerator;
import net.devtech.arrp.json.blockstate.JBlockModel;
import net.devtech.arrp.json.blockstate.JBlockStates;
import net.devtech.arrp.json.blockstate.JMultipart;
import net.devtech.arrp.json.blockstate.JWhenProperties;
import net.devtech.arrp.json.models.JModel;
import net.devtech.arrp.json.models.JTextures;
import net.devtech.arrp.json.recipe.JRecipe;
import net.devtech.arrp.json.recipe.JShapedRecipe;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.SignBlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.fluid.FluidState;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.state.property.EnumProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.text.MutableText;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.*;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;
import net.minecraft.world.chunk.WorldChunk;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import pers.solid.mishang.uc.MishangUtils;
import pers.solid.mishang.uc.blockentity.HungSignBlockEntity;
import pers.solid.mishang.uc.blocks.WallSignBlocks;
import pers.solid.mishang.uc.mixin.ItemUsageContextInvoker;
import pers.solid.mishang.uc.render.HungSignBlockEntityRenderer;
import pers.solid.mishang.uc.text.TextContext;

import java.util.List;
import java.util.Map;

/**
 * @see HungSignBlockEntity
 * @see HungSignBlockEntityRenderer
 */
public class HungSignBlock extends Block implements Waterloggable, BlockEntityProvider, BlockResourceGenerator {
  /**
   * ????????????????????????????????????????????????????????????
   */
  @ApiStatus.AvailableSince("0.1.7")
  public static final Reference2ReferenceMap<Block, HungSignBlock> BASE_TO_HUNG_SIGN = new Reference2ReferenceOpenHashMap<>();
  public static final EnumProperty<Direction.Axis> AXIS = Properties.HORIZONTAL_AXIS;
  public static final BooleanProperty WATERLOGGED = Properties.WATERLOGGED;
  /**
   * ???????????????????????????????????? axis=x?????? left ?????????????????? axis=z?????? left ???????????????<br>
   * Whether the sign is connected to the left. The "left" represents "south" if "axis=x", or "west"
   * if "axis=z".
   */
  public static final BooleanProperty LEFT = BooleanProperty.of("left");
  /**
   * ???????????????????????????????????? axis=x?????? right ?????????????????? axis=z?????? right ???????????????<br>
   * Whether the sign is connected to the right. The "right" represents "north" if "axis=x", or
   * "east" if "axis=z".
   */
  public static final BooleanProperty RIGHT = BooleanProperty.of("right");

  private static final VoxelShape SHAPE_X =
      VoxelShapes.union(
          createCuboidShape(7.5, 6, 0, 8.5, 12, 16), createCuboidShape(7.25, 12, 0, 8.75, 13, 16));
  private static final VoxelShape SHAPE_Z =
      VoxelShapes.union(
          createCuboidShape(0, 6, 7.5, 16, 12, 8.5), createCuboidShape(0, 12, 7.25, 16, 13, 8.75));
  private static final Map<Direction, @Nullable VoxelShape> BAR_SHAPES =
      MishangUtils.createHorizontalDirectionToShape(7.5, 13, 11, 8.5, 16, 12);
  private static final Map<Direction, @Nullable VoxelShape> BAR_SHAPES_EDGE =
      MishangUtils.createHorizontalDirectionToShape(7.5, 13, 13, 8.5, 16, 14);
  private static final VoxelShape SHAPE_WIDENED_X = createCuboidShape(6.5, 6, 0, 9.5, 16, 16);
  private static final VoxelShape SHAPE_WIDENED_Z = createCuboidShape(0, 6, 6.5, 16, 16, 9.5);
  public final @Nullable Block baseBlock;

  /**
   * ????????????????????????{@link #getBaseTexture()} ???????????????????????????????????? {@code null}???????????? {@link #baseBlock} ??????????????????<br>
   * ??? final???????????????????????????
   */
  @ApiStatus.AvailableSince("0.1.7")
  public @Nullable String baseTexture;
  /**
   * ????????????????????????????????? {@code null}???????????????????????????????????? null ?????????????????? json ??????????????????
   */
  @ApiStatus.AvailableSince("0.1.7")
  public @Nullable String barTexture;
  /**
   * ???????????????????????????????????? {@code null}???????????????????????????????????? null ?????????????????? json ??????????????????
   */
  @ApiStatus.AvailableSince("0.1.7")
  public @Nullable String textureTop;

  public HungSignBlock(@Nullable Block baseBlock, Settings settings) {
    super(settings);
    this.baseBlock = baseBlock;
    this.putToMap();
    this.setDefaultState(
        this.stateManager
            .getDefaultState()
            .with(WATERLOGGED, false)
            .with(AXIS, Direction.Axis.X)
            .with(LEFT, false)
            .with(RIGHT, false));
  }

  @ApiStatus.AvailableSince("0.1.7")
  public HungSignBlock(@NotNull Block baseBlock) {
    this(baseBlock, FabricBlockSettings.copyOf(baseBlock));
  }

  @Override
  protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
    super.appendProperties(builder);
    builder.add(AXIS, WATERLOGGED, LEFT, RIGHT);
  }

  @ApiStatus.AvailableSince("0.1.7")
  protected void putToMap() {
    BASE_TO_HUNG_SIGN.put(baseBlock, this);
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
        .with(
            AXIS,
            blockState.getBlock() instanceof HungSignBlock && blockState.contains(AXIS)
                ? blockState.get(AXIS)
                : ctx.getPlayerFacing().getAxis())
        .with(WATERLOGGED, world.getFluidState(blockPos).getFluid() == Fluids.WATER)
        .getStateForNeighborUpdate(
            Direction.UP, world.getBlockState(blockPos.up()), world, blockPos, blockPos.up());
  }

  @NotNull
  @Override
  public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
    return HungSignBlockEntity.create(pos, state);
  }

  @SuppressWarnings("deprecation")
  @Override
  public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
    final Direction.Axis axis = state.get(AXIS);
    final boolean left = state.get(LEFT);
    final boolean right = state.get(RIGHT);
    final boolean shouldCollideWide = context != ShapeContext.absent();
    switch (axis) {
      case X:
        if (shouldCollideWide) return SHAPE_WIDENED_X;
        else if (!left && !right)
          return VoxelShapes.union(
              SHAPE_X, BAR_SHAPES_EDGE.get(Direction.SOUTH), BAR_SHAPES_EDGE.get(Direction.NORTH));
        else
          return VoxelShapes.union(
              SHAPE_X,
              !left ? BAR_SHAPES.get(Direction.SOUTH) : VoxelShapes.empty(),
              !right ? BAR_SHAPES.get(Direction.NORTH) : VoxelShapes.empty());
      case Z:
        if (shouldCollideWide) return SHAPE_WIDENED_Z;
        else if (!left && !right)
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

  @SuppressWarnings("deprecation")
  @Override
  public FluidState getFluidState(BlockState state) {
    return state.get(WATERLOGGED) ? Fluids.WATER.getStill(false) : super.getFluidState(state);
  }

  /**
   * ??????????????????????????????????????????????????????????????????left ??? right???????????? true???????????????????????????????????????<br>
   * ????????????????????????????????????????????????????????????????????????????????????????????????????????? true???
   */
  @SuppressWarnings("deprecation")
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
      world.createAndScheduleFluidTick(pos, Fluids.WATER, Fluids.WATER.getTickRate(world));
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
   * ????????????????????????????????????????????????????????????????????????????????????????????? {@link #getStateForNeighborUpdate} ?????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????
   *
   * @param state         ?????????????????????????????????
   * @param direction     ??????????????????????????????
   * @param neighborState ????????????????????????
   * @return ????????????????????????
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

  @SuppressWarnings("deprecation")
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

  @SuppressWarnings("deprecation")
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
   * ???????????????????????????????????????????????????????????????????????????????????????????????????????????? <br>
   * ??????????????????????????????????????? {@link net.minecraft.item.SignItem#postPlacement(BlockPos, World, PlayerEntity,
   * ItemStack, BlockState)}???<br>
   * ????????????????????????????????????????????? {@link
   * net.minecraft.client.network.ClientPlayerEntity#openEditSignScreen(SignBlockEntity)} ??? {@link
   * net.minecraft.server.network.ServerPlayerEntity#openEditSignScreen(SignBlockEntity)}???
   */
  @SuppressWarnings({"deprecation", "JavadocReference"})
  @Override
  public ActionResult onUse(
      BlockState state,
      World world,
      BlockPos pos,
      PlayerEntity player,
      Hand hand,
      BlockHitResult hit) {
    final ActionResult actionResult = super.onUse(state, world, pos, player, hand, hit);
    if (actionResult != ActionResult.PASS) return actionResult;
    final BlockEntity blockEntity = world.getBlockEntity(pos);
    if (!(blockEntity instanceof final HungSignBlockEntity entity)) {
      return ActionResult.PASS;
    } else if (!state.get(AXIS).test(hit.getSide())) {
      // ?????????????????????????????????????????????????????????????????????????????????????????????????????????
      // Skip if the block entity does not correspond, or the side is not editable.
      return ActionResult.PASS;
    } else if (!player.getAbilities().allowModifyWorld) {
      // ?????????????????????????????????Adventure players has no permission to edit.
      return ActionResult.FAIL;
    } else if (player.getMainHandStack().getItem() == Items.MAGMA_CREAM) {
      // ???????????????????????????????????????????????????
      final List<@NotNull TextContext> textContexts = entity.texts.get(hit.getSide());
      if (textContexts != null) MishangUtils.rearrange(textContexts);
      entity.markDirty();
      return ActionResult.SUCCESS;
    } else if (player.getMainHandStack().getItem() == Items.SLIME_BALL) {
      // ?????????????????????????????????????????????????????????
      final List<@NotNull TextContext> textContexts = entity.texts.get(hit.getSide());
      if (textContexts != null) MishangUtils.replaceArrows(textContexts);
      entity.markDirty();
      return ActionResult.SUCCESS;
    } else if (player.getMainHandStack().getItem() == Items.SLIME_BLOCK) {
      final WorldChunk worldChunk = world.getWorldChunk(pos);
      for (BlockEntity value : worldChunk.getBlockEntities().values()) {
        if (value instanceof HungSignBlockEntity hungSignBlockEntity) {
          hungSignBlockEntity.texts.values().forEach(MishangUtils::replaceArrows);
          hungSignBlockEntity.markDirty();
        }
      }
      return ActionResult.SUCCESS;
    } else if (world.isClient) {
      return ActionResult.SUCCESS;
    }

    entity.checkEditorValidity();
    final PlayerEntity editor = entity.getEditor();
    if (editor != null && editor != player) {
      // ????????????????????????????????????????????????????????????
      // In this case, the sign is occupied, and the player has no editing permission.
      player.sendMessage(new TranslatableText("message.mishanguc.no_editing_permission.occupied", editor.getName()), false);
      return ActionResult.FAIL;
    }
    entity.editedSide = hit.getSide();
    entity.setEditor(player);
    ServerPlayNetworking.send(
        ((ServerPlayerEntity) player),
        new Identifier("mishanguc", "edit_sign"),
        PacketByteBufs.create().writeBlockPos(pos).writeEnumConstant(hit.getSide()));
    return ActionResult.SUCCESS;
  }

  @Override
  public MutableText getName() {
    if (baseBlock != null) {
      return new TranslatableText("block.mishanguc.hung_sign", baseBlock.getName());
    }
    return super.getName();
  }

  @Environment(EnvType.CLIENT)
  public String getBaseTexture() {
    if (baseTexture != null) return baseTexture;
    final Identifier id = Registry.BLOCK.getId(baseBlock);
    return String.format("%s:block/%s", id.getNamespace(), id.getPath());
  }

  @Environment(EnvType.CLIENT)
  @Override
  public void writeBlockModel(RuntimeResourcePack pack) {
    final String texture = getBaseTexture();
    final Identifier id = getBlockModelId();
    final JTextures textures = new JTextures().var("texture", texture).var("bar", barTexture).var("texture_top", textureTop);
    pack.addModel(new JModel(new Identifier("mishanguc", "block/hung_sign")).textures(textures),
        id);
    pack.addModel(new JModel(new Identifier("mishanguc", "block/hung_sign_body")).textures(textures),
        id.brrp_append("_body"));
    pack.addModel(new JModel(new Identifier("mishanguc", "block/hung_sign_top_bar")).textures(textures),
        id.brrp_append("_top_bar"));
    pack.addModel(new JModel(new Identifier("mishanguc", "block/hung_sign_top_bar_edge")).textures(textures),
        id.brrp_append("_top_bar_edge"));
  }

  @Environment(EnvType.CLIENT)
  @Override
  public @NotNull JBlockStates getBlockStates() {
    final Identifier id = getBlockModelId();
    return JBlockStates.ofMultiparts(
        new JMultipart(JWhenProperties.of("axis", "z"), new JBlockModel(id.brrp_append("_body")).uvlock()),
        new JMultipart(JWhenProperties.of("axis", "x"), new JBlockModel(id.brrp_append("_body")).uvlock().y(90)),
        new JMultipart(JWhenProperties.of("axis", "z").add("left", "false").add("right", "true"), new JBlockModel(id.brrp_append("_top_bar")).uvlock()),
        new JMultipart(JWhenProperties.of("axis", "z").add("left", "true").add("right", "false"), new JBlockModel(id.brrp_append("_top_bar")).uvlock().y(180)),
        new JMultipart(JWhenProperties.of("axis", "x").add("left", "false").add("right", "true"), new JBlockModel(id.brrp_append("_top_bar")).uvlock().y(-90)),
        new JMultipart(JWhenProperties.of("axis", "x").add("left", "true").add("right", "false"), new JBlockModel(id.brrp_append("_top_bar")).uvlock().y(90)),
        new JMultipart(JWhenProperties.of("axis", "z").add("left", "false").add("right", "false"), new JBlockModel(id.brrp_append("_top_bar_edge")).uvlock()),
        new JMultipart(JWhenProperties.of("axis", "z").add("left", "false").add("right", "false"), new JBlockModel(id.brrp_append("_top_bar_edge")).uvlock().y(180)),
        new JMultipart(JWhenProperties.of("axis", "x").add("left", "false").add("right", "false"), new JBlockModel(id.brrp_append("_top_bar_edge")).uvlock().y(90)),
        new JMultipart(JWhenProperties.of("axis", "x").add("left", "false").add("right", "false"), new JBlockModel(id.brrp_append("_top_bar_edge")).uvlock().y(270)));
  }

  @Override
  public @Nullable JRecipe getCraftingRecipe() {
    if (baseBlock == null) return null;
    final JShapedRecipe recipe = new JShapedRecipe(this)
        .pattern("-#-", "-#-", "-#-")
        .addKey("#", baseBlock).addKey("-", WallSignBlocks.INVISIBLE_WALL_SIGN)
        .resultCount(6);
    recipe.addInventoryChangedCriterion("has_base_block", baseBlock).addInventoryChangedCriterion("has_sign", WallSignBlocks.INVISIBLE_WALL_SIGN);
    return recipe;
  }

  @Override
  public Identifier getAdvancementIdForRecipe(Identifier recipeId) {
    return recipeId.brrp_prepend("recipes/signs/");
  }

  @SuppressWarnings("deprecation")
  @Override
  public boolean isSideInvisible(BlockState state, BlockState stateFrom, Direction direction) {
    return direction.getAxis().isHorizontal() && state.getBlock() instanceof HungSignBlock && stateFrom.getBlock() instanceof HungSignBlock && state.get(AXIS) == stateFrom.get(AXIS) && direction.getAxis() != state.get(AXIS);
  }
}
