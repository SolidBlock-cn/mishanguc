package pers.solid.mishang.uc.block;

import com.mojang.math.Quadrant;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.tag.convention.v2.ConventionalItemTags;
import net.minecraft.client.data.models.BlockModelGenerators;
import net.minecraft.client.data.models.ModelProvider;
import net.minecraft.client.data.models.blockstates.MultiVariantGenerator;
import net.minecraft.client.data.models.blockstates.PropertyDispatch;
import net.minecraft.client.data.models.model.ItemModelUtils;
import net.minecraft.client.data.models.model.ModelLocationUtils;
import net.minecraft.client.data.models.model.ModelTemplates;
import net.minecraft.client.data.models.model.TextureMapping;
import net.minecraft.client.renderer.block.dispatch.VariantMutator;
import net.minecraft.client.resources.model.sprite.Material;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.data.recipes.RecipeBuilder;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.data.recipes.SingleItemRecipeBuilder;
import net.minecraft.resources.Identifier;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.ScheduledTickAccess;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.shapes.BooleanOp;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Nullable;
import pers.solid.mishang.uc.blocks.RoadMarkBlocks;
import pers.solid.mishang.uc.data.MishangucModels;
import pers.solid.mishang.uc.util.EightHorizontalDirection;
import pers.solid.mishang.uc.util.FourHorizontalAxis;

@ApiStatus.AvailableSince("1.0.4")
public class RoadMarkBlock extends Block implements SimpleWaterloggedBlock, MishangucBlock {
  public static final VoxelShape SHAPE = box(0, 0, 0, 16, 1, 16);
  public static final VoxelShape SHAPE_X = box(0, 0, 2, 16, 1, 14);
  public static final VoxelShape SHAPE_Z = box(2, 0, 0, 14, 1, 16);
  public static final VoxelShape SHAPE_ON_SLAB = box(0, -8, 0, 16, -7, 16);
  public static final VoxelShape SHAPE_ON_SLAB_X = box(0, -8, 2, 16, -7, 14);
  public static final VoxelShape SHAPE_ON_SLAB_Z = box(2, -8, 0, 14, -7, 16);
  public static final BooleanProperty ON_SLAB = BooleanProperty.create("on_slab");
  protected final Identifier texture;
  private static final VoxelShape SHAPE_TOP_MASK = box(0, 15.5, 0, 16, 16, 16);
  private static final VoxelShape SHAPE_SLAB_TOP_MASK = box(0, 7.5, 0, 16, 8, 16);

  public static final MapCodec<RoadMarkBlock> CODEC = RecordCodecBuilder.mapCodec(i -> i.group(Identifier.CODEC.fieldOf("texture").forGetter(b -> b.texture), propertiesCodec()).apply(i, RoadMarkBlock::new));

  public RoadMarkBlock(Identifier texture, Properties settings) {
    super(settings);
    this.texture = texture;
    registerDefaultState(defaultBlockState()
        .setValue(BlockStateProperties.WATERLOGGED, false)
        .setValue(ON_SLAB, false));
  }

  @Override
  protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
    super.createBlockStateDefinition(builder);
    builder.add(BlockStateProperties.WATERLOGGED, ON_SLAB);
  }

  @Override
  public boolean canSurvive(BlockState state, LevelReader world, BlockPos pos) {
    final BlockPos downPos = pos.below();
    final BlockState downState = world.getBlockState(downPos);
    final VoxelShape downShape = downState.getBlockSupportShape(world, downPos);
    return !Shapes.joinIsNotEmpty(downShape, SHAPE_TOP_MASK, BooleanOp.ONLY_SECOND) || !Shapes.joinIsNotEmpty(downShape, SHAPE_SLAB_TOP_MASK, BooleanOp.ONLY_SECOND);
  }

  @Nullable
  @Override
  public BlockState getStateForPlacement(BlockPlaceContext ctx) {
    BlockState state = super.getStateForPlacement(ctx);
    if (state != null) {
      final BlockPos blockPos = ctx.getClickedPos();
      final Level world = ctx.getLevel();
      state = state.setValue(BlockStateProperties.WATERLOGGED, world.getFluidState(blockPos).getType() == Fluids.WATER);
      final BlockPos downPos = blockPos.below();
      final BlockState downState = world.getBlockState(downPos);
      final VoxelShape downShape = downState.getBlockSupportShape(world, downPos);
      if (Shapes.joinIsNotEmpty(downShape, SHAPE_TOP_MASK, BooleanOp.ONLY_SECOND) && !Shapes.joinIsNotEmpty(downShape, SHAPE_SLAB_TOP_MASK, BooleanOp.ONLY_SECOND)) {
        state = state.setValue(ON_SLAB, true);
      }
    }
    return state;
  }

  @Override
  public FluidState getFluidState(BlockState state) {
    return state.getValue(BlockStateProperties.WATERLOGGED) ? Fluids.WATER.getSource(false) : super.getFluidState(state);
  }

  @Override
  protected BlockState updateShape(BlockState state, LevelReader world, ScheduledTickAccess tickView, BlockPos pos, Direction direction, BlockPos neighborPos, BlockState neighborState, RandomSource random) {
    if (state.getValue(BlockStateProperties.WATERLOGGED)) {
      tickView.scheduleTick(pos, Fluids.WATER, Fluids.WATER.getTickDelay(world));
    }
    if (direction == Direction.DOWN) {
      if (!this.canSurvive(state, world, pos)) {
        return Blocks.AIR.defaultBlockState();
      } else {
        return super.updateShape(state, world, tickView, pos, direction, neighborPos, neighborState, random)
            .setValue(ON_SLAB, Shapes.joinIsNotEmpty(world.getBlockState(neighborPos).getShape(world, neighborPos), SHAPE_TOP_MASK, BooleanOp.ONLY_SECOND));
      }
    }
    return super.updateShape(state, world, tickView, pos, direction, neighborPos, neighborState, random);
  }

  @Override
  public VoxelShape getShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext context) {
    return state.getValue(ON_SLAB) ? SHAPE_ON_SLAB : SHAPE;
  }

  public static RoadMarkBlock createAxisFacing(Identifier texture, Properties settings) {
    return new AxisFacing(texture, settings);
  }

  public static RoadMarkBlock createDirectionalFacing(Identifier texture, Properties settings) {
    return new DirectionalFacing(texture, settings);
  }

  @Override
  protected MapCodec<? extends RoadMarkBlock> codec() {
    return CODEC;
  }

  @Override
  public RecipeBuilder getCraftingRecipe(RecipeProvider recipeGenerator) {
    return SingleItemRecipeBuilder.stonecutting(recipeGenerator.tag(ConventionalItemTags.WHITE_DYES), RecipeCategory.DECORATIONS, this, 1)
        .unlockedBy("has_white_dye", recipeGenerator.has(ConventionalItemTags.WHITE_DYES));
  }

  @Environment(EnvType.CLIENT)
  @Override
  public void registerModels(ModelProvider modelProvider, BlockModelGenerators blockStateModelGenerator) {
    final TextureMapping textures = TextureMapping.cube(new Material(texture));
    final Identifier modelId = MishangucModels.ROAD_MARK.create(this, textures, blockStateModelGenerator.modelOutput);
    final Identifier onSlabModelId = MishangucModels.ROAD_MARK_ON_SLAB.create(this, textures, blockStateModelGenerator.modelOutput);
    blockStateModelGenerator.blockStateOutput.accept(MultiVariantGenerator.dispatch(this)
        .with(PropertyDispatch.initial(ON_SLAB)
            .select(false, BlockModelGenerators.plainVariant(modelId))
            .select(true, BlockModelGenerators.plainVariant(onSlabModelId))));
    final Identifier itemModelId = ModelTemplates.FLAT_HANDHELD_ITEM.create(asItem(), TextureMapping.layer0(new Material(texture)), blockStateModelGenerator.modelOutput);
    blockStateModelGenerator.itemModelOutput.accept(asItem(), ItemModelUtils.plainModel(itemModelId));
  }

  @Override
  public String customRecipeCategory() {
    return "road_marks";
  }

  protected static class AxisFacing extends RoadMarkBlock {
    public static final MapCodec<AxisFacing> CODEC = RecordCodecBuilder.mapCodec(i -> i.group(Identifier.CODEC.fieldOf("texture").forGetter(b -> b.texture), propertiesCodec()).apply(i, AxisFacing::new));
    public static final EnumProperty<FourHorizontalAxis> AXIS = EnumProperty.create("axis", FourHorizontalAxis.class);

    protected AxisFacing(Identifier texture, Properties settings) {
      super(texture, settings);
      registerDefaultState(defaultBlockState().setValue(AXIS, FourHorizontalAxis.X));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
      super.createBlockStateDefinition(builder);
      builder.add(AXIS);
    }

    @Override
    public @Nullable BlockState getStateForPlacement(BlockPlaceContext ctx) {
      final BlockState state = super.getStateForPlacement(ctx);
      if (state != null) {
        return state.setValue(AXIS, EightHorizontalDirection.fromRotation(ctx.getRotation()).axis);
      }
      return null;
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext context) {
      return switch (state.getValue(AXIS)) {
        case X -> state.getValue(ON_SLAB) ? SHAPE_ON_SLAB_X : SHAPE_X;
        case Z -> state.getValue(ON_SLAB) ? SHAPE_ON_SLAB_Z : SHAPE_Z;
        default -> super.getShape(state, world, pos, context);
      };
    }

    @Override
    public BlockState rotate(BlockState state, Rotation rotation) {
      return super.rotate(state, rotation).setValue(AXIS, state.getValue(AXIS).rotate(rotation));
    }

    @Override
    public BlockState mirror(BlockState state, Mirror mirror) {
      BlockState mirror1 = super.mirror(state, mirror);
      if (RoadMarkBlocks.LEFT_TO_RIGHT.containsKey(this)) {
        mirror1 = RoadMarkBlocks.LEFT_TO_RIGHT.get(this).withPropertiesOf(mirror1);
      } else if (RoadMarkBlocks.LEFT_TO_RIGHT.inverse().containsKey(this)) {
        mirror1 = RoadMarkBlocks.LEFT_TO_RIGHT.inverse().get(this).withPropertiesOf(mirror1);
      }
      return mirror1.setValue(AXIS, state.getValue(AXIS).mirror());
    }

    @Environment(EnvType.CLIENT)
    @Override
    public void registerModels(ModelProvider modelProvider, BlockModelGenerators blockStateModelGenerator) {
      final TextureMapping textures = TextureMapping.cube(new Material(texture));
      final Identifier modelId = MishangucModels.ROAD_MARK.create(this, textures, blockStateModelGenerator.modelOutput);
      final Identifier rotatedModelId = MishangucModels.ROAD_MARK_ROTATED.create(this, textures, blockStateModelGenerator.modelOutput);
      final Identifier onSlabModelId = MishangucModels.ROAD_MARK_ON_SLAB.create(this, textures, blockStateModelGenerator.modelOutput);
      final Identifier onSlabRotatedModelId = MishangucModels.ROAD_MARK_ON_SLAB_ROTATED.create(this, textures, blockStateModelGenerator.modelOutput);
      final var map = PropertyDispatch.initial(ON_SLAB, AXIS)
          .select(false, FourHorizontalAxis.X, BlockModelGenerators.plainVariant(modelId).with(BlockModelGenerators.Y_ROT_90))
          .select(false, FourHorizontalAxis.NW_SE, BlockModelGenerators.plainVariant(rotatedModelId).with(BlockModelGenerators.Y_ROT_90))
          .select(false, FourHorizontalAxis.Z, BlockModelGenerators.plainVariant(modelId).with(BlockModelGenerators.NOP))
          .select(false, FourHorizontalAxis.NE_SW, BlockModelGenerators.plainVariant(rotatedModelId).with(BlockModelGenerators.NOP))
          .select(true, FourHorizontalAxis.X, BlockModelGenerators.plainVariant(onSlabModelId).with(BlockModelGenerators.Y_ROT_90))
          .select(true, FourHorizontalAxis.NW_SE, BlockModelGenerators.plainVariant(onSlabRotatedModelId).with(BlockModelGenerators.Y_ROT_90))
          .select(true, FourHorizontalAxis.Z, BlockModelGenerators.plainVariant(onSlabModelId).with(BlockModelGenerators.NOP))
          .select(true, FourHorizontalAxis.NE_SW, BlockModelGenerators.plainVariant(onSlabRotatedModelId).with(BlockModelGenerators.NOP));
      blockStateModelGenerator.blockStateOutput.accept(MultiVariantGenerator.dispatch(this).with(map));
      final Identifier itemModelId = ModelTemplates.FLAT_HANDHELD_ITEM.create(ModelLocationUtils.getModelLocation(asItem()), TextureMapping.layer0(new Material(texture)), blockStateModelGenerator.modelOutput);
      blockStateModelGenerator.itemModelOutput.accept(asItem(), ItemModelUtils.plainModel(itemModelId));
    }

    @Override
    protected MapCodec<? extends AxisFacing> codec() {
      return CODEC;
    }
  }

  protected static class DirectionalFacing extends RoadMarkBlock {
    public static final MapCodec<DirectionalFacing> CODEC = RecordCodecBuilder.mapCodec(i -> i.group(Identifier.CODEC.fieldOf("texture").forGetter(b -> b.texture), propertiesCodec()).apply(i, DirectionalFacing::new));
    public static final EnumProperty<EightHorizontalDirection> FACING = EnumProperty.create("facing", EightHorizontalDirection.class);

    public DirectionalFacing(Identifier texture, Properties settings) {
      super(texture, settings);
      registerDefaultState(defaultBlockState().setValue(FACING, EightHorizontalDirection.SOUTH));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
      super.createBlockStateDefinition(builder);
      builder.add(FACING);
    }

    @Override
    public @Nullable BlockState getStateForPlacement(BlockPlaceContext ctx) {
      final BlockState state = super.getStateForPlacement(ctx);
      if (state != null) {
        return state.setValue(FACING, EightHorizontalDirection.fromRotation(ctx.getRotation()));
      }
      return null;
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext context) {
      return switch (state.getValue(FACING).axis) {
        case X -> state.getValue(ON_SLAB) ? SHAPE_ON_SLAB_X : SHAPE_X;
        case Z -> state.getValue(ON_SLAB) ? SHAPE_ON_SLAB_Z : SHAPE_Z;
        default -> super.getShape(state, world, pos, context);
      };
    }

    @Override
    public BlockState rotate(BlockState state, Rotation rotation) {
      return super.rotate(state, rotation).setValue(FACING, state.getValue(FACING).rotate(rotation));
    }

    @Override
    public BlockState mirror(BlockState state, Mirror mirror) {
      BlockState mirror1 = super.mirror(state, mirror);
      if (RoadMarkBlocks.LEFT_TO_RIGHT.containsKey(this)) {
        mirror1 = RoadMarkBlocks.LEFT_TO_RIGHT.get(this).withPropertiesOf(mirror1);
      } else if (RoadMarkBlocks.LEFT_TO_RIGHT.inverse().containsKey(this)) {
        mirror1 = RoadMarkBlocks.LEFT_TO_RIGHT.inverse().get(this).withPropertiesOf(mirror1);
      }
      return mirror1.setValue(FACING, state.getValue(FACING).mirror(mirror));
    }

    @Environment(EnvType.CLIENT)
    @Override
    public void registerModels(ModelProvider modelProvider, BlockModelGenerators blockStateModelGenerator) {
      final TextureMapping textures = TextureMapping.cube(new Material(texture));
      final Identifier modelId = MishangucModels.ROAD_MARK.create(this, textures, blockStateModelGenerator.modelOutput);
      final Identifier rotatedModelId = MishangucModels.ROAD_MARK_ROTATED.create(this, textures, blockStateModelGenerator.modelOutput);
      final Identifier onSlabModelId = MishangucModels.ROAD_MARK_ON_SLAB.create(this, textures, blockStateModelGenerator.modelOutput);
      final Identifier onSlabRotatedModelId = MishangucModels.ROAD_MARK_ON_SLAB_ROTATED.create(this, textures, blockStateModelGenerator.modelOutput);
      final var map = PropertyDispatch.initial(ON_SLAB, FACING);
      for (EightHorizontalDirection direction : EightHorizontalDirection.VALUES) {
        int rotation = (int) direction.asRotation();
        boolean rotated = direction.right().isPresent();
        if (rotated) {
          rotation -= 45;
        }
        final Quadrant axisRotation = switch (rotation) {
          case 90 -> Quadrant.R90;
          case 180 -> Quadrant.R180;
          case 270, -90 -> Quadrant.R270;
          default -> Quadrant.R0;
        };
        map.select(false, direction, BlockModelGenerators.plainVariant(rotated ? rotatedModelId : modelId).with(VariantMutator.Y_ROT.withValue(axisRotation)));
        map.select(true, direction, BlockModelGenerators.plainVariant(rotated ? onSlabRotatedModelId : onSlabModelId).with(VariantMutator.Y_ROT.withValue(axisRotation)));
      }
      blockStateModelGenerator.blockStateOutput.accept(MultiVariantGenerator.dispatch(this).with(map));
      final Identifier itemModelId = ModelTemplates.FLAT_HANDHELD_ITEM.create(ModelLocationUtils.getModelLocation(asItem()), TextureMapping.layer0(new Material(texture)), blockStateModelGenerator.modelOutput);
      blockStateModelGenerator.itemModelOutput.accept(asItem(), ItemModelUtils.plainModel(itemModelId));
    }

    @Override
    protected MapCodec<? extends DirectionalFacing> codec() {
      return CODEC;
    }
  }
}
