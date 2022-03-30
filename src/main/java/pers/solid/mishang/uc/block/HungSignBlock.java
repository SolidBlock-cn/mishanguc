package pers.solid.mishang.uc.block;

import it.unimi.dsi.fastutil.objects.Reference2ReferenceMap;
import it.unimi.dsi.fastutil.objects.Reference2ReferenceOpenHashMap;
import net.devtech.arrp.api.RuntimeResourcePack;
import net.devtech.arrp.json.blockstate.JBlockModel;
import net.devtech.arrp.json.blockstate.JMultipart;
import net.devtech.arrp.json.blockstate.JState;
import net.devtech.arrp.json.blockstate.JWhen;
import net.devtech.arrp.json.models.JModel;
import net.devtech.arrp.json.models.JTextures;
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
import net.minecraft.item.*;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.state.property.EnumProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.text.MutableText;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.ActionResult;
import net.minecraft.util.BlockRotation;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import pers.solid.mishang.uc.MishangUtils;
import pers.solid.mishang.uc.arrp.ARRPGenerator;
import pers.solid.mishang.uc.arrp.FixedWhen;
import pers.solid.mishang.uc.blockentity.HungSignBlockEntity;
import pers.solid.mishang.uc.mixin.EntityShapeContextAccessor;
import pers.solid.mishang.uc.mixin.ItemUsageContextInvoker;
import pers.solid.mishang.uc.util.TextContext;

import java.util.List;
import java.util.Map;

/**
 * @see HungSignBlockEntity
 * @see pers.solid.mishang.uc.renderer.HungSignBlockEntityRenderer
 */
public class HungSignBlock extends Block implements Waterloggable, BlockEntityProvider, ARRPGenerator {
  /**
   * 由基础方块映射到对应的悬挂告示牌的方块。
   */
  @ApiStatus.AvailableSince("0.1.7")
  public static final Reference2ReferenceMap<Block, HungSignBlock> BASE_TO_HUNG_SIGN = new Reference2ReferenceOpenHashMap<>();
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
          createCuboidShape(7.5, 6, 0, 8.5, 12, 16), createCuboidShape(7.25, 12, 0, 8.75, 13, 16));
  private static final VoxelShape SHAPE_Z =
      VoxelShapes.union(
          createCuboidShape(0, 6, 7.5, 16, 12, 8.5), createCuboidShape(0, 12, 7.25, 16, 13, 8.75));
  private static final Map<Direction, @Nullable VoxelShape> BAR_SHAPES =
      MishangUtils.createHorizontalDirectionToShape(7.5, 13, 11, 8.5, 16, 12);
  private static final Map<Direction, @Nullable VoxelShape> BAR_SHAPES_EDGE =
      MishangUtils.createHorizontalDirectionToShape(7.5, 13, 13, 8.5, 16, 14);
  private static final VoxelShape SHAPE_WIDENED_X = createCuboidShape(5.5, 6, 0, 10.5, 16, 16);
  private static final VoxelShape SHAPE_WIDENED_Z = createCuboidShape(0, 6, 5.5, 16, 16, 10.5);
  public final @Nullable Block baseBlock;

  /**
   * 基础方块的纹理。{@link #getBaseTexture()} 会使用到此值。如果此值为 {@code null}，则根据 {@link #baseBlock} 来推断纹理。<br>
   * 非 final，可直接进行修改。
   */
  @ApiStatus.AvailableSince("0.1.7")
  public @Nullable String baseTexture;
  /**
   * 告示牌杆的纹理。可能为 {@code null}。生成模型时，可直接作为 null 传入，转化为 json 时会被忽略。
   */
  @ApiStatus.AvailableSince("0.1.7")
  public @Nullable String barTexture;
  /**
   * 告示牌顶部的纹理。可能为 {@code null}。生成模型时，可直接作为 null 传入，转化为 json 时会被忽略。
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

  @Nullable
  @Override
  public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
    return new HungSignBlockEntity(pos, state);
  }

  @SuppressWarnings("deprecation")
  @Override
  public VoxelShape getOutlineShape(
      BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
    final Direction.Axis axis = state.get(AXIS);
    final boolean left = state.get(LEFT);
    final boolean right = state.get(RIGHT);
    final boolean isHoldingHungSigns;
    if (context instanceof EntityShapeContext) {
      final Item heldItem = ((EntityShapeContextAccessor) context).getHeldItem().getItem();
      if (heldItem instanceof final BlockItem blockItem) {
        final Block block = blockItem.getBlock();
        isHoldingHungSigns = block instanceof HungSignBlock || block instanceof HungSignBarBlock;
      } else isHoldingHungSigns = false;
    } else isHoldingHungSigns = false;
    switch (axis) {
      case X:
        if (isHoldingHungSigns) return SHAPE_WIDENED_X;
        else if (!left && !right)
          return VoxelShapes.union(
              SHAPE_X, BAR_SHAPES_EDGE.get(Direction.SOUTH), BAR_SHAPES_EDGE.get(Direction.NORTH));
        else
          return VoxelShapes.union(
              SHAPE_X,
              !left ? BAR_SHAPES.get(Direction.SOUTH) : VoxelShapes.empty(),
              !right ? BAR_SHAPES.get(Direction.NORTH) : VoxelShapes.empty());
      case Z:
        if (isHoldingHungSigns) return SHAPE_WIDENED_Z;
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
   * 当这个指定的方向连接有同类方块时，这个方块（left 和 right）就会为 true，此时上方将不会显示栏杆。<br>
   * 如果连接有非同类方块，且上方没有连接带有碰撞箱的方块，则这个方向也会为 true。
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
    state =
        super.getStateForNeighborUpdate(state, direction, neighborState, world, pos, neighborPos);
    if (state.get(WATERLOGGED)) {
      world.getFluidTickScheduler().schedule(pos, Fluids.WATER, Fluids.WATER.getTickRate(world));
    }
    final @Nullable BooleanProperty property;
    final Direction.Axis axis = state.get(AXIS);
    property = switch (axis) {
      case X -> direction == Direction.SOUTH ? LEFT : direction == Direction.NORTH ? RIGHT : null;
      case Z -> direction == Direction.WEST ? LEFT : direction == Direction.EAST ? RIGHT : null;
      default -> null;
    };
    if (property != null) {
      state =
          state.with(
              property,
              neighborState.getBlock() instanceof HungSignBlock && neighborState.get(AXIS) == axis
                  || world
                  .getBlockState(pos.up())
                  .getCollisionShape(world, pos.up())
                  .getMin(Direction.Axis.Y)
                  != 0);
    } else if (direction == Direction.UP) {
      for (Direction horizontalDirection : Direction.Type.HORIZONTAL) {
        final BlockPos offset = pos.offset(horizontalDirection);
        state =
            getStateForNeighborUpdate(
                state, horizontalDirection, world.getBlockState(offset), world, pos, offset);
      }
    }

    return state;
  }

  @SuppressWarnings("deprecation")
  @Override
  public BlockState rotate(BlockState state, BlockRotation rotation) {
    final Direction.Axis oldAxis = state.get(AXIS);
    state =
        super.rotate(state, rotation)
            .with(
                AXIS,
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

  /**
   * 点击告示牌方块时，允许玩家对告示牌进行编辑。冒险模式的玩家无权进行编辑。 <br>
   * 本方法在编写时，适当参照了 {@link net.minecraft.item.SignItem#postPlacement(BlockPos, World, PlayerEntity,
   * ItemStack, BlockState)}。<br>
   * 告示牌界面的打开逻辑又可以参考 {@link
   * net.minecraft.client.network.ClientPlayerEntity#openEditSignScreen(SignBlockEntity)} 和 {@link
   * net.minecraft.server.network.ServerPlayerEntity#openEditSignScreen(SignBlockEntity)}。
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
    final BlockEntity blockEntity = world.getBlockEntity(pos);
    if (!(blockEntity instanceof final HungSignBlockEntity entity)) {
      return ActionResult.PASS;
    }
    // 若方块实体不对应，或者编辑的这一侧不可编辑，则在客户端和服务器均略过。
    // Skip if the block entity does not correspond, or the side is not editable.
    if (!state.get(AXIS).test(hit.getSide())) {
      return ActionResult.PASS;
    }
    if (!player.getAbilities().allowModifyWorld) {
      return ActionResult.PASS;
    }
    if (player.getMainHandStack().getItem() == Items.MAGMA_CREAM) {
      // 玩家手持岩浆膏时，可快速进行重整。
      final List<@NotNull TextContext> textContexts = entity.texts.get(hit.getSide());
      if (textContexts != null) MishangUtils.rearrange(textContexts);
      return ActionResult.SUCCESS;
    }
    if (actionResult == ActionResult.PASS && !world.isClient) {
      entity.checkEditorValidity();
      final PlayerEntity editor = entity.getEditor();
      if (editor != null && editor != player) {
        // 这种情况下，告示牌被占用，玩家无权编辑。
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
    }
    return ActionResult.SUCCESS;
  }

  @Environment(EnvType.CLIENT)
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
    final Identifier id = getBlockModelIdentifier();
    final JTextures textures = new JTextures().var("texture", texture).var("bar", barTexture).var("texture_top", textureTop);
    pack.addModel(JModel.model(new Identifier("mishanguc", "block/hung_sign")).textures(textures),
        id);
    pack.addModel(JModel.model(new Identifier("mishanguc", "block/hung_sign_body")).textures(textures),
        MishangUtils.identifierSuffix(id, "_body"));
    pack.addModel(JModel.model(new Identifier("mishanguc", "block/hung_sign_top_bar")).textures(textures),
        MishangUtils.identifierSuffix(id, "_top_bar"));
    pack.addModel(JModel.model(new Identifier("mishanguc", "block/hung_sign_top_bar_edge")).textures(textures),
        MishangUtils.identifierSuffix(id, "_top_bar_edge"));
  }

  @Environment(EnvType.CLIENT)
  @Override
  public @NotNull JState getBlockStates() {
    final Identifier id = getBlockModelIdentifier();
    return JState.state(
        new JMultipart()
            .addModel(new JBlockModel(MishangUtils.identifierSuffix(id, "_body")).uvlock())
            .when(new JWhen().add("axis", "z")),
        new JMultipart()
            .addModel(new JBlockModel(MishangUtils.identifierSuffix(id, "_body")).uvlock().y(90))
            .when(new JWhen().add("axis", "x")),
        new JMultipart()
            .addModel(new JBlockModel(MishangUtils.identifierSuffix(id, "_top_bar")).uvlock())
            .when(new FixedWhen().add("axis", "z").add("left", "false").add("right", "true")),
        new JMultipart()
            .addModel(new JBlockModel(MishangUtils.identifierSuffix(id, "_top_bar")).uvlock().y(180))
            .when(new FixedWhen().add("axis", "z").add("left", "true").add("right", "false")),
        new JMultipart()
            .addModel(new JBlockModel(MishangUtils.identifierSuffix(id, "_top_bar")).uvlock().y(-90))
            .when(new FixedWhen().add("axis", "x").add("left", "false").add("right", "true")),
        new JMultipart()
            .addModel(new JBlockModel(MishangUtils.identifierSuffix(id, "_top_bar")).uvlock().y(90))
            .when(new FixedWhen().add("axis", "x").add("left", "true").add("right", "false")),
        new JMultipart()
            .addModel(new JBlockModel(MishangUtils.identifierSuffix(id, "_top_bar_edge")).uvlock())
            .when(new FixedWhen().add("axis", "z").add("left", "false").add("right", "false")),
        new JMultipart()
            .addModel(new JBlockModel(MishangUtils.identifierSuffix(id, "_top_bar_edge")).uvlock().y(180))
            .when(new FixedWhen().add("axis", "z").add("left", "false").add("right", "false")),
        new JMultipart()
            .addModel(new JBlockModel(MishangUtils.identifierSuffix(id, "_top_bar_edge")).uvlock().y(90))
            .when(new FixedWhen().add("axis", "x").add("left", "false").add("right", "false")),
        new JMultipart()
            .addModel(new JBlockModel(MishangUtils.identifierSuffix(id, "_top_bar_edge")).uvlock().y(270))
            .when(new FixedWhen().add("axis", "x").add("left", "false").add("right", "false")));
  }


  @SuppressWarnings("deprecation")
  @Environment(EnvType.CLIENT)
  @Deprecated
  @Override
  public boolean isSideInvisible(BlockState state, BlockState stateFrom, Direction direction) {
    return direction.getAxis().isHorizontal() && state.getBlock() instanceof HungSignBlock && stateFrom.getBlock() instanceof HungSignBlock && state.get(AXIS) == stateFrom.get(AXIS) && direction.getAxis() != state.get(AXIS);
  }
}
