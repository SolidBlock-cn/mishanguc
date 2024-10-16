package pers.solid.mishang.uc.block;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.fabricmc.fabric.api.tag.convention.v2.ConventionalItemTags;
import net.minecraft.block.*;
import net.minecraft.data.client.*;
import net.minecraft.data.server.recipe.CraftingRecipeJsonBuilder;
import net.minecraft.data.server.recipe.RecipeProvider;
import net.minecraft.data.server.recipe.StonecuttingRecipeJsonBuilder;
import net.minecraft.fluid.FluidState;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.recipe.Ingredient;
import net.minecraft.recipe.book.RecipeCategory;
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
import pers.solid.mishang.uc.MishangUtils;
import pers.solid.mishang.uc.blocks.RoadMarkBlocks;
import pers.solid.mishang.uc.data.MishangucModels;
import pers.solid.mishang.uc.util.EightHorizontalDirection;
import pers.solid.mishang.uc.util.FourHorizontalAxis;

@ApiStatus.AvailableSince("1.0.4")
public class RoadMarkBlock extends Block implements Waterloggable, MishangucBlock {
  public static final VoxelShape SHAPE = createCuboidShape(0, 0, 0, 16, 1, 16);
  public static final VoxelShape SHAPE_X = createCuboidShape(0, 0, 2, 16, 1, 14);
  public static final VoxelShape SHAPE_Z = createCuboidShape(2, 0, 0, 14, 1, 16);
  public static final VoxelShape SHAPE_ON_SLAB = createCuboidShape(0, -8, 0, 16, -7, 16);
  public static final VoxelShape SHAPE_ON_SLAB_X = createCuboidShape(0, -8, 2, 16, -7, 14);
  public static final VoxelShape SHAPE_ON_SLAB_Z = createCuboidShape(2, -8, 0, 14, -7, 16);
  public static final BooleanProperty ON_SLAB = BooleanProperty.of("on_slab");
  protected final Identifier texture;
  private static final VoxelShape SHAPE_TOP_MASK = createCuboidShape(0, 15.5, 0, 16, 16, 16);
  private static final VoxelShape SHAPE_SLAB_TOP_MASK = createCuboidShape(0, 7.5, 0, 16, 8, 16);

  public static final MapCodec<RoadMarkBlock> CODEC = RecordCodecBuilder.mapCodec(i -> i.group(Identifier.CODEC.fieldOf("texture").forGetter(b -> b.texture), createSettingsCodec()).apply(i, RoadMarkBlock::new));

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

  @Override
  public FluidState getFluidState(BlockState state) {
    return state.get(Properties.WATERLOGGED) ? Fluids.WATER.getStill(false) : super.getFluidState(state);
  }

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

  @Override
  public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
    return state.get(ON_SLAB) ? SHAPE_ON_SLAB : SHAPE;
  }

  public static RoadMarkBlock createAxisFacing(Identifier texture, Settings settings) {
    return new AxisFacing(texture, settings);
  }

  public static RoadMarkBlock createDirectionalFacing(Identifier texture, Settings settings) {
    return new DirectionalFacing(texture, settings);
  }

  @Override
  protected MapCodec<? extends RoadMarkBlock> getCodec() {
    return CODEC;
  }

  @Override
  public CraftingRecipeJsonBuilder getCraftingRecipe() {
    return StonecuttingRecipeJsonBuilder.createStonecutting(Ingredient.fromTag(ConventionalItemTags.WHITE_DYES), RecipeCategory.DECORATIONS, this)
        .criterion("has_white_dye", RecipeProvider.conditionsFromTag(ConventionalItemTags.WHITE_DYES));
  }

  @Override
  public void registerModels(ModelProvider modelProvider, BlockStateModelGenerator blockStateModelGenerator) {
    final TextureMap textures = TextureMap.all(texture);
    final Identifier modelId = MishangucModels.ROAD_MARK.upload(this, textures, blockStateModelGenerator.modelCollector);
    final Identifier onSlabModelId = MishangucModels.ROAD_MARK_ON_SLAB.upload(this, textures, blockStateModelGenerator.modelCollector);
    blockStateModelGenerator.blockStateCollector.accept(VariantsBlockStateSupplier.create(this)
        .coordinate(BlockStateVariantMap.create(ON_SLAB)
            .register(false, new BlockStateVariant().put(VariantSettings.MODEL, modelId))
            .register(true, new BlockStateVariant().put(VariantSettings.MODEL, onSlabModelId))));
    Models.HANDHELD.upload(ModelIds.getItemModelId(asItem()), TextureMap.layer0(texture), blockStateModelGenerator.modelCollector);
  }

  @Override
  public String customRecipeCategory() {
    return "road_marks";
  }

  protected static class AxisFacing extends RoadMarkBlock {
    public static final MapCodec<AxisFacing> CODEC = RecordCodecBuilder.mapCodec(i -> i.group(Identifier.CODEC.fieldOf("texture").forGetter(b -> b.texture), createSettingsCodec()).apply(i, AxisFacing::new));
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

    @Override
    public BlockState rotate(BlockState state, BlockRotation rotation) {
      return super.rotate(state, rotation).with(AXIS, state.get(AXIS).rotate(rotation));
    }

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

    @Override
    public void registerModels(ModelProvider modelProvider, BlockStateModelGenerator blockStateModelGenerator) {
      final TextureMap textures = TextureMap.all(texture);
      final Identifier modelId = MishangucModels.ROAD_MARK.upload(this, textures, blockStateModelGenerator.modelCollector);
      final Identifier rotatedModelId = MishangucModels.ROAD_MARK_ROTATED.upload(this, textures, blockStateModelGenerator.modelCollector);
      final Identifier onSlabModelId = MishangucModels.ROAD_MARK_ON_SLAB.upload(this, textures, blockStateModelGenerator.modelCollector);
      final Identifier onSlabRotatedModelId = MishangucModels.ROAD_MARK_ON_SLAB_ROTATED.upload(this, textures, blockStateModelGenerator.modelCollector);
      final BlockStateVariantMap.DoubleProperty<Boolean, FourHorizontalAxis> map = BlockStateVariantMap.create(ON_SLAB, AXIS)
          .register(false, FourHorizontalAxis.X, BlockStateVariant.create().put(VariantSettings.MODEL, modelId).put(MishangUtils.INT_Y_VARIANT, 90))
          .register(false, FourHorizontalAxis.NW_SE, BlockStateVariant.create().put(VariantSettings.MODEL, rotatedModelId).put(MishangUtils.INT_Y_VARIANT, 90))
          .register(false, FourHorizontalAxis.Z, BlockStateVariant.create().put(VariantSettings.MODEL, modelId).put(MishangUtils.INT_Y_VARIANT, 0))
          .register(false, FourHorizontalAxis.NE_SW, BlockStateVariant.create().put(VariantSettings.MODEL, rotatedModelId).put(MishangUtils.INT_Y_VARIANT, 0))
          .register(true, FourHorizontalAxis.X, BlockStateVariant.create().put(VariantSettings.MODEL, onSlabModelId).put(MishangUtils.INT_Y_VARIANT, 90))
          .register(true, FourHorizontalAxis.NW_SE, BlockStateVariant.create().put(VariantSettings.MODEL, onSlabRotatedModelId).put(MishangUtils.INT_Y_VARIANT, 90))
          .register(true, FourHorizontalAxis.Z, BlockStateVariant.create().put(VariantSettings.MODEL, onSlabModelId).put(MishangUtils.INT_Y_VARIANT, 0))
          .register(true, FourHorizontalAxis.NE_SW, BlockStateVariant.create().put(VariantSettings.MODEL, onSlabRotatedModelId).put(MishangUtils.INT_Y_VARIANT, 0));
      blockStateModelGenerator.blockStateCollector.accept(VariantsBlockStateSupplier.create(this).coordinate(map));
      Models.HANDHELD.upload(ModelIds.getItemModelId(asItem()), TextureMap.layer0(texture), blockStateModelGenerator.modelCollector);
    }

    @Override
    protected MapCodec<? extends AxisFacing> getCodec() {
      return CODEC;
    }
  }

  protected static class DirectionalFacing extends RoadMarkBlock {
    public static final MapCodec<DirectionalFacing> CODEC = RecordCodecBuilder.mapCodec(i -> i.group(Identifier.CODEC.fieldOf("texture").forGetter(b -> b.texture), createSettingsCodec()).apply(i, DirectionalFacing::new));
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

    @Override
    public BlockState rotate(BlockState state, BlockRotation rotation) {
      return super.rotate(state, rotation).with(FACING, state.get(FACING).rotate(rotation));
    }

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

    @Override
    public void registerModels(ModelProvider modelProvider, BlockStateModelGenerator blockStateModelGenerator) {
      final TextureMap textures = TextureMap.all(texture);
      final Identifier modelId = MishangucModels.ROAD_MARK.upload(this, textures, blockStateModelGenerator.modelCollector);
      final Identifier rotatedModelId = MishangucModels.ROAD_MARK_ROTATED.upload(this, textures, blockStateModelGenerator.modelCollector);
      final Identifier onSlabModelId = MishangucModels.ROAD_MARK_ON_SLAB.upload(this, textures, blockStateModelGenerator.modelCollector);
      final Identifier onSlabRotatedModelId = MishangucModels.ROAD_MARK_ON_SLAB_ROTATED.upload(this, textures, blockStateModelGenerator.modelCollector);
      final BlockStateVariantMap.DoubleProperty<Boolean, EightHorizontalDirection> map = BlockStateVariantMap.create(ON_SLAB, FACING);
      for (EightHorizontalDirection direction : EightHorizontalDirection.VALUES) {
        int rotation = (int) direction.asRotation();
        boolean rotated = direction.right().isPresent();
        if (rotated) {
          rotation -= 45;
        }
        map.register(false, direction, BlockStateVariant.create().put(VariantSettings.MODEL, rotated ? rotatedModelId : modelId).put(MishangUtils.INT_Y_VARIANT, rotation));
        map.register(true, direction, BlockStateVariant.create().put(VariantSettings.MODEL, rotated ? onSlabRotatedModelId : onSlabModelId).put(MishangUtils.INT_Y_VARIANT, rotation));
      }
      blockStateModelGenerator.blockStateCollector.accept(VariantsBlockStateSupplier.create(this).coordinate(map));
      Models.HANDHELD.upload(ModelIds.getItemModelId(asItem()), TextureMap.layer0(texture), blockStateModelGenerator.modelCollector);
    }

    @Override
    protected MapCodec<? extends DirectionalFacing> getCodec() {
      return CODEC;
    }
  }
}
