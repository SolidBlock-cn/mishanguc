package pers.solid.mishang.uc.block;

import net.devtech.arrp.api.RuntimeResourcePack;
import net.devtech.arrp.generator.BlockResourceGenerator;
import net.devtech.arrp.json.blockstate.JBlockModel;
import net.devtech.arrp.json.blockstate.JBlockStates;
import net.devtech.arrp.json.blockstate.JVariants;
import net.devtech.arrp.json.models.JModel;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.*;
import net.minecraft.data.client.TextureKey;
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
  private final String texture;
  private static final VoxelShape SHAPE_TOP_MASK = createCuboidShape(0, 15.5, 0, 16, 16, 16);
  private static final VoxelShape SHAPE_SLAB_TOP_MASK = createCuboidShape(0, 7.5, 0, 16, 8, 16);

  public RoadMarkBlock(@NotNull String texture, Settings settings) {
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
      world.scheduleFluidTick(pos, Fluids.WATER, Fluids.WATER.getTickRate(world));
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
  public @NotNull JModel getBlockModel() {
    return new JModel(MODEL_PARENT).addTexture("texture", getTextureId(TextureKey.TEXTURE));
  }

  @Environment(EnvType.CLIENT)
  @Override
  public void writeBlockModel(RuntimeResourcePack pack) {
    final JModel model = getBlockModel();
    final Identifier blockModelId = getBlockModelId();
    pack.addModel(model, blockModelId);
    pack.addModel(model.clone().parent(model.parent + "_on_slab"), blockModelId.brrp_append("_on_slab"));
    pack.addModel(model.clone().parent(model.parent + "_rotated"), blockModelId.brrp_append("_rotated"));
    pack.addModel(model.clone().parent(model.parent + "_on_slab_rotated"), blockModelId.brrp_append("_on_slab_rotated"));
  }

  @Override
  public @Nullable JModel getItemModel() {
    return new JModel("item/handheld").addTexture("layer0", texture);
  }

  @Environment(EnvType.CLIENT)
  @Override
  public @NotNull String getTextureId(@NotNull TextureKey textureKey) {
    return texture;
  }

  public static RoadMarkBlock createAxisFacing(String texture, Settings settings) {
    return new AxisFacing(texture, settings);
  }

  public static RoadMarkBlock createDirectionalFacing(String texture, Settings settings) {
    return new DirectionalFacing(texture, settings);
  }

  protected static class AxisFacing extends RoadMarkBlock {
    public static final EnumProperty<FourHorizontalAxis> AXIS = EnumProperty.of("axis", FourHorizontalAxis.class);

    protected AxisFacing(String texture, Settings settings) {
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
    public @NotNull JBlockStates getBlockStates() {
      final Identifier blockModelId = getBlockModelId();
      final JVariants variants = new JVariants()
          .addVariant("on_slab=false,axis", FourHorizontalAxis.X, new JBlockModel(blockModelId).y(90))
          .addVariant("on_slab=false,axis", FourHorizontalAxis.NW_SE, new JBlockModel(blockModelId.brrp_append("_rotated")).y(90))
          .addVariant("on_slab=false,axis", FourHorizontalAxis.Z, new JBlockModel(blockModelId).y(0))
          .addVariant("on_slab=false,axis", FourHorizontalAxis.NE_SW, new JBlockModel(blockModelId.brrp_append("_rotated")).y(0))
          .addVariant("on_slab=true,axis", FourHorizontalAxis.X, new JBlockModel(blockModelId.brrp_append("_on_slab")).y(90))
          .addVariant("on_slab=true,axis", FourHorizontalAxis.NW_SE, new JBlockModel(blockModelId.brrp_append("_on_slab_rotated")).y(90))
          .addVariant("on_slab=true,axis", FourHorizontalAxis.Z, new JBlockModel(blockModelId.brrp_append("_on_slab")).y(0))
          .addVariant("on_slab=true,axis", FourHorizontalAxis.NE_SW, new JBlockModel(blockModelId.brrp_append("_on_slab_rotated")).y(0));
      return JBlockStates.ofVariants(variants);
    }
  }

  protected static class DirectionalFacing extends RoadMarkBlock {
    public static final EnumProperty<EightHorizontalDirection> FACING = EnumProperty.of("facing", EightHorizontalDirection.class);

    public DirectionalFacing(String texture, Settings settings) {
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
    public @Nullable JBlockStates getBlockStates() {
      final Identifier blockModelId = getBlockModelId();
      final JVariants variants = new JVariants();
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
        variants.addVariant("on_slab=false,facing", direction, new JBlockModel(id1.brrp_append(append)).y(rotation));
        variants.addVariant("on_slab=true,facing", direction, new JBlockModel(id1.brrp_append("_on_slab" + append)).y(rotation));
      }
      return JBlockStates.ofVariants(variants);
    }
  }
}
