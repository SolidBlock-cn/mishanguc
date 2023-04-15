package pers.solid.mishang.uc.block;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.*;
import net.minecraft.data.client.model.*;
import net.minecraft.fluid.FluidState;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.state.property.EnumProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.util.BlockMirror;
import net.minecraft.util.BlockRotation;
import net.minecraft.util.Identifier;
import net.minecraft.util.function.BooleanBiFunction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;
import net.minecraft.world.WorldView;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import pers.solid.brrp.v1.api.RuntimeResourcePack;
import pers.solid.brrp.v1.generator.BlockResourceGenerator;
import pers.solid.brrp.v1.model.ModelJsonBuilder;
import pers.solid.mishang.uc.MishangUtils;
import pers.solid.mishang.uc.blocks.RoadMarkBlocks;
import pers.solid.mishang.uc.util.EightHorizontalDirection;
import pers.solid.mishang.uc.util.FourHorizontalAxis;

@ApiStatus.AvailableSince("1.0.4")
public class RoadMarkBlock extends Block implements Waterloggable, BlockResourceGenerator {
  public static final VoxelShape SHAPE = createCuboidShape(0, 0, 0, 16, 1, 16);
  public static final VoxelShape SHAPE_X = createCuboidShape(0, 0, 2, 16, 1, 14);
  public static final VoxelShape SHAPE_Z = createCuboidShape(2, 0, 0, 14, 1, 16);
  public static final VoxelShape SHAPE_ON_SLAB = createCuboidShape(0, -8, 0, 16, -7, 16);
  public static final VoxelShape SHAPE_ON_SLAB_X = createCuboidShape(0, -8, 2, 16, -7, 14);
  public static final VoxelShape SHAPE_ON_SLAB_Z = createCuboidShape(2, -8, 0, 14, -7, 16);
  public static final BooleanProperty ON_SLAB = BooleanProperty.of("on_slab");
  private static final Identifier MODEL_PARENT = new Identifier("mishanguc", "block/road_mark");
  private final Identifier texture;
  private static final VoxelShape SHAPE_TOP_MASK = createCuboidShape(0, 15.5, 0, 16, 16, 16);
  private static final VoxelShape SHAPE_SLAB_TOP_MASK = createCuboidShape(0, 7.5, 0, 16, 8, 16);

  public RoadMarkBlock(@NotNull Identifier texture, Settings settings) {
    super(settings);
    this.texture = texture;
    setDefaultState(getDefaultState()
        .with(Properties.WATERLOGGED, false)
        .with(ON_SLAB, false));
  }

  @Override
  protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
    super.appendProperties(builder);
    builder.add(Properties.WATERLOGGED, ON_SLAB);
  }

  @SuppressWarnings("deprecation")
  @Override
  public boolean canPlaceAt(BlockState state, WorldView world, BlockPos pos) {
    final BlockPos downPos = pos.down();
    final BlockState downState = world.getBlockState(downPos);
    final VoxelShape downShape = downState.getSidesShape(world, downPos);
    return !VoxelShapes.matchesAnywhere(downShape, SHAPE_TOP_MASK, BooleanBiFunction.ONLY_SECOND) || !VoxelShapes.matchesAnywhere(downShape, SHAPE_SLAB_TOP_MASK, BooleanBiFunction.ONLY_SECOND);
  }

  @Nullable
  @Override
  public BlockState getPlacementState(ItemPlacementContext ctx) {
    BlockState state = super.getPlacementState(ctx);
    if (state != null) {
      final BlockPos blockPos = ctx.getBlockPos();
      final World world = ctx.getWorld();
      state = state.with(Properties.WATERLOGGED, world.getFluidState(blockPos).getFluid() == Fluids.WATER);
      final BlockPos downPos = blockPos.down();
      final BlockState downState = world.getBlockState(downPos);
      final VoxelShape downShape = downState.getSidesShape(world, downPos);
      if (VoxelShapes.matchesAnywhere(downShape, SHAPE_TOP_MASK, BooleanBiFunction.ONLY_SECOND) && !VoxelShapes.matchesAnywhere(downShape, SHAPE_SLAB_TOP_MASK, BooleanBiFunction.ONLY_SECOND)) {
        state = state.with(ON_SLAB, true);
      }
    }
    return state;
  }

  @SuppressWarnings("deprecation")
  @Override
  public FluidState getFluidState(BlockState state) {
    return state.get(Properties.WATERLOGGED) ? Fluids.WATER.getStill(false) : super.getFluidState(state);
  }

  @SuppressWarnings("deprecation")
  @Override
  public BlockState getStateForNeighborUpdate(BlockState state, Direction direction, BlockState neighborState, WorldAccess world, BlockPos pos, BlockPos neighborPos) {
    if (state.get(Properties.WATERLOGGED)) {
      world.createAndScheduleFluidTick(pos, Fluids.WATER, Fluids.WATER.getTickRate(world));
    }
    if (direction == Direction.DOWN) {
      if (!this.canPlaceAt(state, world, pos)) {
        return Blocks.AIR.getDefaultState();
      } else {
        return super.getStateForNeighborUpdate(state, direction, neighborState, world, pos, neighborPos)
            .with(ON_SLAB, VoxelShapes.matchesAnywhere(world.getBlockState(neighborPos).getOutlineShape(world, neighborPos), SHAPE_TOP_MASK, BooleanBiFunction.ONLY_SECOND));
      }
    }
    return super.getStateForNeighborUpdate(state, direction, neighborState, world, pos, neighborPos);
  }

  @SuppressWarnings("deprecation")
  @Override
  public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
    return state.get(ON_SLAB) ? SHAPE_ON_SLAB : SHAPE;
  }

  @Environment(EnvType.CLIENT)
  @Override
  public @NotNull ModelJsonBuilder getBlockModel() {
    return ModelJsonBuilder.create(MODEL_PARENT).addTexture("texture", getTextureId(TextureKey.TEXTURE));
  }

  @Environment(EnvType.CLIENT)
  @Override
  public void writeBlockModel(RuntimeResourcePack pack) {
    final ModelJsonBuilder model = getBlockModel();
    final Identifier blockModelId = getBlockModelId();
    pack.addModel(blockModelId, model);
    pack.addModel(blockModelId.brrp_suffixed("_on_slab"), model.withParent(model.parentId.brrp_suffixed("_on_slab")));
    pack.addModel(blockModelId.brrp_suffixed("_rotated"), model.withParent(model.parentId.brrp_suffixed("_rotated")));
    pack.addModel(blockModelId.brrp_suffixed("_on_slab_rotated"), model.withParent(model.parentId.brrp_suffixed("_on_slab_rotated")));
  }

  @Override
  public @Nullable ModelJsonBuilder getItemModel() {
    return ModelJsonBuilder.create(Models.HANDHELD).addTexture("layer0", texture);
  }

  @Environment(EnvType.CLIENT)
  @Override
  public @NotNull Identifier getTextureId(@NotNull TextureKey textureKey) {
    return texture;
  }

  public static RoadMarkBlock createAxisFacing(Identifier texture, Settings settings) {
    return new AxisFacing(texture, settings);
  }

  public static RoadMarkBlock createDirectionalFacing(Identifier texture, Settings settings) {
    return new DirectionalFacing(texture, settings);
  }

  protected static class AxisFacing extends RoadMarkBlock {
    public static final EnumProperty<FourHorizontalAxis> AXIS = EnumProperty.of("axis", FourHorizontalAxis.class);

    protected AxisFacing(Identifier texture, Settings settings) {
      super(texture, settings);
      setDefaultState(getDefaultState().with(AXIS, FourHorizontalAxis.X));
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
      super.appendProperties(builder);
      builder.add(AXIS);
    }

    @Override
    public @Nullable BlockState getPlacementState(ItemPlacementContext ctx) {
      final BlockState state = super.getPlacementState(ctx);
      if (state != null) {
        return state.with(AXIS, EightHorizontalDirection.fromRotation(ctx.getPlayerYaw()).axis);
      }
      return null;
    }

    @Override
    public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
      return switch (state.get(AXIS)) {
        case X -> state.get(ON_SLAB) ? SHAPE_ON_SLAB_X : SHAPE_X;
        case Z -> state.get(ON_SLAB) ? SHAPE_ON_SLAB_Z : SHAPE_Z;
        default -> super.getOutlineShape(state, world, pos, context);
      };
    }

    @SuppressWarnings("deprecation")
    @Override
    public BlockState rotate(BlockState state, BlockRotation rotation) {
      return super.rotate(state, rotation).with(AXIS, state.get(AXIS).rotate(rotation));
    }

    @SuppressWarnings("deprecation")
    @Override
    public BlockState mirror(BlockState state, BlockMirror mirror) {
      BlockState mirror1 = super.mirror(state, mirror);
      if (RoadMarkBlocks.LEFT_TO_RIGHT.containsKey(this)) {
        mirror1 = RoadMarkBlocks.LEFT_TO_RIGHT.get(this).getStateWithProperties(mirror1);
      } else if (RoadMarkBlocks.LEFT_TO_RIGHT.inverse().containsKey(this)) {
        mirror1 = RoadMarkBlocks.LEFT_TO_RIGHT.inverse().get(this).getStateWithProperties(mirror1);
      }
      return mirror1.with(AXIS, state.get(AXIS).mirror());
    }

    @Environment(EnvType.CLIENT)
    @Override
    public @NotNull BlockStateSupplier getBlockStates() {
      final Identifier blockModelId = getBlockModelId();
      final BlockStateVariantMap.DoubleProperty<Boolean, FourHorizontalAxis> map = BlockStateVariantMap.create(ON_SLAB, AXIS)
          .register(false, FourHorizontalAxis.X, BlockStateVariant.create().put(VariantSettings.MODEL, blockModelId).put(MishangUtils.INT_Y_VARIANT, 90))
          .register(false, FourHorizontalAxis.NW_SE, BlockStateVariant.create().put(VariantSettings.MODEL, blockModelId.brrp_suffixed("_rotated")).put(MishangUtils.INT_Y_VARIANT, 90))
          .register(false, FourHorizontalAxis.Z, BlockStateVariant.create().put(VariantSettings.MODEL, blockModelId).put(MishangUtils.INT_Y_VARIANT, 0))
          .register(false, FourHorizontalAxis.NE_SW, BlockStateVariant.create().put(VariantSettings.MODEL, blockModelId.brrp_suffixed("_rotated")).put(MishangUtils.INT_Y_VARIANT, 0))
          .register(true, FourHorizontalAxis.X, BlockStateVariant.create().put(VariantSettings.MODEL, blockModelId.brrp_suffixed("_on_slab")).put(MishangUtils.INT_Y_VARIANT, 90))
          .register(true, FourHorizontalAxis.NW_SE, BlockStateVariant.create().put(VariantSettings.MODEL, blockModelId.brrp_suffixed("_on_slab_rotated")).put(MishangUtils.INT_Y_VARIANT, 90))
          .register(true, FourHorizontalAxis.Z, BlockStateVariant.create().put(VariantSettings.MODEL, blockModelId.brrp_suffixed("_on_slab")).put(MishangUtils.INT_Y_VARIANT, 0))
          .register(true, FourHorizontalAxis.NE_SW, BlockStateVariant.create().put(VariantSettings.MODEL, blockModelId.brrp_suffixed("_on_slab_rotated")).put(MishangUtils.INT_Y_VARIANT, 0));
      return VariantsBlockStateSupplier.create(this).coordinate(map);
    }
  }

  protected static class DirectionalFacing extends RoadMarkBlock {
    public static final EnumProperty<EightHorizontalDirection> FACING = EnumProperty.of("facing", EightHorizontalDirection.class);

    public DirectionalFacing(Identifier texture, Settings settings) {
      super(texture, settings);
      setDefaultState(getDefaultState().with(FACING, EightHorizontalDirection.SOUTH));
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
      super.appendProperties(builder);
      builder.add(FACING);
    }

    @Override
    public @Nullable BlockState getPlacementState(ItemPlacementContext ctx) {
      final BlockState state = super.getPlacementState(ctx);
      if (state != null) {
        return state.with(FACING, EightHorizontalDirection.fromRotation(ctx.getPlayerYaw()));
      }
      return null;
    }

    @Override
    public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
      return switch (state.get(FACING).axis) {
        case X -> state.get(ON_SLAB) ? SHAPE_ON_SLAB_X : SHAPE_X;
        case Z -> state.get(ON_SLAB) ? SHAPE_ON_SLAB_Z : SHAPE_Z;
        default -> super.getOutlineShape(state, world, pos, context);
      };
    }

    @SuppressWarnings("deprecation")
    @Override
    public BlockState rotate(BlockState state, BlockRotation rotation) {
      return super.rotate(state, rotation).with(FACING, state.get(FACING).rotate(rotation));
    }

    @SuppressWarnings("deprecation")
    @Override
    public BlockState mirror(BlockState state, BlockMirror mirror) {
      BlockState mirror1 = super.mirror(state, mirror);
      if (RoadMarkBlocks.LEFT_TO_RIGHT.containsKey(this)) {
        mirror1 = RoadMarkBlocks.LEFT_TO_RIGHT.get(this).getStateWithProperties(mirror1);
      } else if (RoadMarkBlocks.LEFT_TO_RIGHT.inverse().containsKey(this)) {
        mirror1 = RoadMarkBlocks.LEFT_TO_RIGHT.inverse().get(this).getStateWithProperties(mirror1);
      }
      return mirror1.with(FACING, state.get(FACING).mirror(mirror));
    }

    @Environment(EnvType.CLIENT)
    @Override
    public @Nullable BlockStateSupplier getBlockStates() {
      final Identifier blockModelId = getBlockModelId();
      final BlockStateVariantMap.DoubleProperty<Boolean, EightHorizontalDirection> map = BlockStateVariantMap.create(ON_SLAB, FACING);
      for (EightHorizontalDirection direction : EightHorizontalDirection.VALUES) {
        int rotation = (int) direction.asRotation();
        Identifier id1 = blockModelId;
        String append;
        if (direction.right().isPresent()) {
          rotation -= 45;
          append = "_rotated";
        } else {
          append = "";
        }
        map.register(false, direction, BlockStateVariant.create().put(VariantSettings.MODEL, id1.brrp_suffixed(append)).put(MishangUtils.INT_Y_VARIANT, rotation));
        map.register(true, direction, BlockStateVariant.create().put(VariantSettings.MODEL, id1.brrp_suffixed("_on_slab" + append)).put(MishangUtils.INT_Y_VARIANT, rotation));
      }
      return VariantsBlockStateSupplier.create(this).coordinate(map);
    }
  }
}
