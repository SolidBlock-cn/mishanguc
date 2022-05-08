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
import pers.solid.mishang.uc.blockentity.HungSignBlockEntity;
import pers.solid.mishang.uc.blocks.WallSignBlocks;
import pers.solid.mishang.uc.mixin.EntityShapeContextAccessor;
import pers.solid.mishang.uc.mixin.ItemUsageContextInvoker;
import pers.solid.mishang.uc.util.TextContext;

import java.util.List;
import java.util.Map;

/**
 * @see HungSignBlockEntity
 * @see pers.solid.mishang.uc.renderer.HungSignBlockEntityRenderer
 */
public class HungSignBlock extends Block implements Waterloggable, BlockEntityProvider, BlockResourceGenerator {
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

  @NotNull
  @Override
  public BlockEntity createBlockEntity(BlockView world) {
    return HungSignBlockEntity.create(world);
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
      final Item heldItem = ((EntityShapeContextAccessor) context).getHeldItem();
      if (heldItem instanceof BlockItem) {
        final Block block = ((BlockItem) heldItem).getBlock();
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
    state = super.getStateForNeighborUpdate(state, direction, neighborState, world, pos, neighborPos);
    if (state.get(WATERLOGGED)) {
      world.getFluidTickScheduler().schedule(pos, Fluids.WATER, Fluids.WATER.getTickRate(world));
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
    switch (axis) {
      case X:
        property =
            direction == Direction.SOUTH ? LEFT : direction == Direction.NORTH ? RIGHT : null;
        break;
      case Z:
        property = direction == Direction.WEST ? LEFT : direction == Direction.EAST ? RIGHT : null;
        break;
      default:
        property = null;
    }
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
    if (!(blockEntity instanceof HungSignBlockEntity)) {
      return ActionResult.PASS;
    }
    final HungSignBlockEntity entity = (HungSignBlockEntity) blockEntity;
    // 若方块实体不对应，或者编辑的这一侧不可编辑，则在客户端和服务器均略过。
    // Skip if the block entity does not correspond, or the side is not editable.
    if (!state.get(AXIS).test(hit.getSide())) {
      return ActionResult.PASS;
    }
    if (!player.abilities.allowModifyWorld) {
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

  // 不要注解为 @Environment(EnvType.CLIENT)
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
  @Environment(EnvType.CLIENT)
  @Deprecated
  @Override
  public boolean isSideInvisible(BlockState state, BlockState stateFrom, Direction direction) {
    return direction.getAxis().isHorizontal() && state.getBlock() instanceof HungSignBlock && stateFrom.getBlock() instanceof HungSignBlock && state.get(AXIS) == stateFrom.get(AXIS) && direction.getAxis() != state.get(AXIS);
  }
}
