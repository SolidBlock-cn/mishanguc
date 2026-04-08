package pers.solid.mishang.uc.block;

import com.google.common.collect.ImmutableList;
import com.mojang.math.Quadrant;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.data.models.BlockModelGenerators;
import net.minecraft.client.data.models.ModelProvider;
import net.minecraft.client.data.models.MultiVariant;
import net.minecraft.client.data.models.blockstates.MultiPartGenerator;
import net.minecraft.client.data.models.model.TextureMapping;
import net.minecraft.client.renderer.block.model.VariantMutator;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.recipes.RecipeBuilder;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.data.recipes.SingleItemRecipeBuilder;
import net.minecraft.resources.Identifier;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.ScheduledTickAccess;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;
import pers.solid.mishang.uc.MishangUtils;
import pers.solid.mishang.uc.Mishanguc;
import pers.solid.mishang.uc.data.MishangucModels;
import pers.solid.mishang.uc.data.MishangucTextureKeys;

import java.util.*;

public class AutoConnectWallLightBlock extends WallLightBlock implements LightConnectable {
  public static final MapCodec<AutoConnectWallLightBlock> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
      Codec.STRING.fieldOf("light_color").forGetter(b -> b.lightColor),
      Codec.STRING.fieldOf("shape").forGetter(b -> b.shape),
      propertiesCodec(),
      Codec.BOOL.fieldOf("large_shape").forGetter(b -> b.largeShape)
  ).apply(instance, AutoConnectWallLightBlock::new));
  /**
   * 每个朝向中，中心基础碰撞箱。任何自动连接的灯都会有此碰撞箱，且不进行任何连接的灯仅使用此碰撞箱。键为灯的 {@link #FACING} 属性。
   */
  private static final Map<Direction, VoxelShape> BASE_SHAPE_PER_FACING =
      MishangUtils.createDirectionToShape(4, 0, 4, 12, 1, 12);
  /**
   * 当 {@link #FACING} 为 {@link Direction#UP} 时，方块的各个连接物的碰撞箱。键为水平方向。
   */
  private static final Map<Direction, VoxelShape> SHAPE_PER_DIRECTION_WHEN_FACING_UP =
      MishangUtils.createHorizontalDirectionToShape(4, 0, 12, 12, 1, 16);
  /**
   * 当 {@link #FACING} 为 {@link Direction#DOWN} 时，方块的各个连接物的碰撞箱。键为水平方向。
   */
  private static final Map<Direction, VoxelShape> SHAPE_PER_DIRECTION_WHEN_FACING_DOWN =
      MishangUtils.createHorizontalDirectionToShape(4, 15, 4, 12, 16, 12);

  /**
   * 当 {@link #FACING} 为水平方向时，方块各个 direction 和 facing 对应的碰撞箱。第一个键为 direction 与 facing
   * 对应的关系（用数字表示），第二个键为 facing。<br>
   * 这样安排是为了方便。
   */
  private static final List<Map<Direction, VoxelShape>> SHAPE_PER_DIRECTION_PER_FACING_WHEN_FACING_HORIZONTALLY =
      ImmutableList.of(
          // 第一个元素为“上”。
          MishangUtils.createHorizontalDirectionToShape(4, 12, 0, 12, 16, 1),
          // 第二个元素为“下”。
          MishangUtils.createHorizontalDirectionToShape(4, 0, 0, 12, 4, 1),
          // 第三个元素为“右”，即 facing 逆时针旋转90度。
          MishangUtils.createHorizontalDirectionToShape(12, 4, 0, 16, 12, 1),
          // 第四个元素为“左”，即 facing 顺时针旋转90度。
          MishangUtils.createHorizontalDirectionToShape(0, 4, 0, 4, 12, 1));
  /**
   * 该灯光装饰方块对应的形状。
   */
  public final String shape;
  private final boolean largeShape;
  final Map<Direction, VoxelShape> LARGE_SHAPE_PER_DIRECTION =
      MishangUtils.createDirectionToShape(0, 0, 0, 16, 1, 16);

  public AutoConnectWallLightBlock(String lightColor, String shape, Properties settings, boolean largeShape) {
    super(lightColor, settings, false);
    this.shape = shape;
    this.largeShape = largeShape;
    this.registerDefaultState(defaultBlockState()
        .setValue(WEST, false)
        .setValue(EAST, false)
        .setValue(SOUTH, false)
        .setValue(NORTH, false)
        .setValue(UP, false)
        .setValue(DOWN, false));
  }

  @Override
  protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
    super.createBlockStateDefinition(builder);
    builder.add(WEST, EAST, SOUTH, NORTH, UP, DOWN);
  }

  @Override
  protected BlockState updateShape(BlockState state, LevelReader world, ScheduledTickAccess tickView, BlockPos pos, Direction direction, BlockPos neighborPos, BlockState neighborState, RandomSource random) {
    if (state.getValue(BlockStateProperties.WATERLOGGED)) {
      tickView.scheduleTick(pos, Fluids.WATER, Fluids.WATER.getTickDelay(world));
    }
    final BlockState newState = super.updateShape(state, world, tickView, pos, direction, neighborPos, neighborState, random);
    final Direction facing = state.getValue(FACING);
    final Block neighborBlock = neighborState.getBlock();
    boolean connect = false;

    // 检查该方向上与之直接毗邻的 LightConnectable 方块，如果符合，则修改自身。
    if (neighborBlock instanceof final LightConnectable lightConnectable) {
      connect =
          lightConnectable
              .isConnectedIn(neighborState, facing, direction.getOpposite());
    }

    // 检查该方向上不与之毗邻（与毗邻位置往 facing.getOpposite() 方向偏移一格）的方块。
    final BlockPos neighborPos2 = pos.relative(direction).relative(facing.getOpposite());
    final BlockState neighborState2 = world.getBlockState(neighborPos2);
    final Block neighborBlock2 = neighborState2.getBlock();
    if (neighborBlock2 instanceof final LightConnectable lightConnectable) {
      connect = connect || lightConnectable.isConnectedIn(neighborState2, direction, facing);
    }
    return newState.setValue(DIRECTION_TO_PROPERTY.get(direction), connect);
  }

  @Override
  public BlockState getStateForPlacement(BlockPlaceContext ctx) {
    BlockState placementState = super.getStateForPlacement(ctx);
    if (placementState == null) {
      return null;
    }
    final Direction facing = placementState.getValue(FACING);
    for (Direction direction : Direction.values()) {
      if (direction.getAxis() == facing.getAxis()) {
        continue;
      }
      final BlockPos blockPos = ctx.getClickedPos();
      final Level world = ctx.getLevel();
      final BlockPos offsetBlockPos = blockPos.relative(direction);
      placementState = updateShape(placementState, world, world, blockPos, direction, offsetBlockPos, world.getBlockState(offsetBlockPos), world.getRandom());
    }
    return placementState;
  }

  @Override
  public boolean isConnectedIn(BlockState blockState, Direction facing, Direction direction) {
    return blockState.getValue(FACING) == facing;
  }

  @Override
  public VoxelShape getShape(
      BlockState state, BlockGetter world, BlockPos pos, CollisionContext context) {
    final Direction facing = state.getValue(FACING);
    if (largeShape) {
      return LARGE_SHAPE_PER_DIRECTION.get(facing);
    }
    final VoxelShape baseShape = BASE_SHAPE_PER_FACING.get(facing);
    final VoxelShape[] extraShapes;
    switch (facing) {
      case UP -> extraShapes = Arrays.stream(Direction.values())
          .filter(direction -> state.getValue(DIRECTION_TO_PROPERTY.get(direction)))
          .map(SHAPE_PER_DIRECTION_WHEN_FACING_UP::get)
          .filter(Objects::nonNull)
          .toArray(VoxelShape[]::new);
      case DOWN -> extraShapes = Arrays.stream(Direction.values())
          .filter(direction -> state.getValue(DIRECTION_TO_PROPERTY.get(direction)))
          .map(SHAPE_PER_DIRECTION_WHEN_FACING_DOWN::get)
          .filter(Objects::nonNull)
          .toArray(VoxelShape[]::new);
      default -> {
        final List<VoxelShape> voxelShapeList = new ArrayList<>();
        if (state.getValue(UP)) {
          voxelShapeList.add(SHAPE_PER_DIRECTION_PER_FACING_WHEN_FACING_HORIZONTALLY.get(0).get(facing));
        }
        if (state.getValue(DOWN)) {
          voxelShapeList.add(SHAPE_PER_DIRECTION_PER_FACING_WHEN_FACING_HORIZONTALLY.get(1).get(facing));
        }
        if (state.getValue(DIRECTION_TO_PROPERTY.get(facing.getCounterClockWise()))) {
          voxelShapeList.add(SHAPE_PER_DIRECTION_PER_FACING_WHEN_FACING_HORIZONTALLY.get(2).get(facing));
        }
        if (state.getValue(DIRECTION_TO_PROPERTY.get(facing.getClockWise()))) {
          voxelShapeList.add(SHAPE_PER_DIRECTION_PER_FACING_WHEN_FACING_HORIZONTALLY.get(3).get(facing));
        }
        extraShapes = voxelShapeList.toArray(new VoxelShape[]{});
      }
    }
    return Shapes.or(baseShape, extraShapes);
  }

  @Environment(EnvType.CLIENT)
  @Override
  public void registerModels(ModelProvider modelProvider, BlockModelGenerators blockStateModelGenerator) {
    final TextureMapping textureMap = TextureMapping.singleSlot(MishangucTextureKeys.LIGHT, MishangucModels.texture(lightColor + "_light"));
    final Identifier modelId = MishangucModels.createBlock("wall_light_%s_decoration".formatted(shape), MishangucTextureKeys.LIGHT).create(this, textureMap, blockStateModelGenerator.modelOutput);
    final Identifier centerModelId = MishangucModels.createBlock("wall_light_%s_decoration_center".formatted(shape), "_center", MishangucTextureKeys.LIGHT).create(this, textureMap, blockStateModelGenerator.modelOutput);
    final Identifier connectionModelId = MishangucModels.createBlock("wall_light_%s_decoration_connection".formatted(shape), "_connection", MishangucTextureKeys.LIGHT).create(this, textureMap, blockStateModelGenerator.modelOutput);
    final Identifier connection2ModelId = MishangucModels.createBlock("wall_light_%s_decoration_connection2".formatted(shape), "_connection2", MishangucTextureKeys.LIGHT).create(this, textureMap, blockStateModelGenerator.modelOutput);
    blockStateModelGenerator.registerSimpleItemModel(this, modelId);

    final MultiPartGenerator blockStateSupplier = MultiPartGenerator.multiPart(this);
    for (Direction facing : Direction.values()) {
      // 中心装饰物
      MultiVariant central = BlockModelGenerators.plainVariant(centerModelId)
          .with(VariantMutator.Y_ROT.withValue(switch (facing) {
            case EAST -> Quadrant.R90;
            case SOUTH -> Quadrant.R180;
            case WEST -> Quadrant.R270;
            default -> Quadrant.R0;
          }))
          .with(VariantMutator.X_ROT.withValue(facing == Direction.DOWN ? Quadrant.R180 : facing == Direction.UP ? Quadrant.R0 : Quadrant.R90));
      blockStateSupplier.with(BlockModelGenerators.condition().term(FACING, facing), central);

      // 连接物
      // 共有两种连接物模型：一种是位于底部或顶部的朝南连接，可以通过x和y的旋转得到位于底部朝向任意方向的连接，以及位于侧面朝向垂直方向的连接。
      // 第二种是位于侧面的朝东连接，可以通过x和y的旋转得到任意水平方向上的，以及底部或顶部任意连接。
      for (Direction direction : Direction.values()) {
        final Direction.Axis axis = direction.getAxis();
        final Quadrant x;
        final int y;
        final Identifier modelName;
        if (axis == facing.getAxis()) {
          continue;
        }
        if (facing == Direction.UP) {
          modelName = connectionModelId;
          x = Quadrant.R0;
          y = (int) direction.toYRot();
        } else if (facing == Direction.DOWN) {
          modelName = connectionModelId;
          x = Quadrant.R180;
          y = (int) direction.toYRot() + 180;
        } else if (direction == Direction.UP) {
          modelName = connectionModelId;
          x = Quadrant.R90;
          y = (int) facing.toYRot() + 180;
        } else if (direction == Direction.DOWN) {
          modelName = connectionModelId;
          x = Quadrant.R270;
          y = (int) facing.toYRot();
        } else if (direction == facing.getCounterClockWise()) {
          modelName = connection2ModelId;
          x = Quadrant.R0;
          y = (int) facing.toYRot();
        } else if (direction == facing.getClockWise()) {
          modelName = connection2ModelId;
          x = Quadrant.R180;
          y = (int) facing.toYRot() + 180;
        } else {
          Mishanguc.MISHANG_LOGGER.error("Unknown state to generate models: facing={},direction={}", facing.getSerializedName(), direction.getSerializedName());
          continue;
        }
        blockStateSupplier.with(BlockModelGenerators.condition().term(FACING, facing).term(DIRECTION_TO_PROPERTY.get(direction), true), BlockModelGenerators.plainVariant(modelName).with(VariantMutator.X_ROT.withValue(x)).with(VariantMutator.Y_ROT.withValue(switch (Mth.positiveModulo(y, 360)) {
          case 90 -> Quadrant.R90;
          case 180 -> Quadrant.R180;
          case 270 -> Quadrant.R270;
          default -> Quadrant.R0;
        })));
      }
    }
    blockStateModelGenerator.blockStateOutput.accept(blockStateSupplier);
  }

  @Override
  protected MapCodec<? extends AutoConnectWallLightBlock> codec() {
    return CODEC;
  }

  @Override
  public RecipeBuilder getCraftingRecipe(RecipeProvider recipeGenerator) {
    final Identifier itemId = BuiltInRegistries.ITEM.getKey(asItem());
    final @NotNull Item fullLight = WallLightBlock.getBaseLight(itemId.getNamespace(), lightColor, this);
    final int outputCount;
    final String path = itemId.getPath();
    if (path.contains("_round_")) {
      outputCount = 9;
    } else if (path.contains("_point_")) {
      outputCount = 18;
    } else if (path.contains("_simple_")) {
      outputCount = 15;
    } else {
      outputCount = 12;
    }
    return SingleItemRecipeBuilder.stonecutting(Ingredient.of(fullLight), RecipeCategory.DECORATIONS, this, outputCount)
        .unlockedBy(RecipeProvider.getHasName(fullLight), recipeGenerator.has(fullLight));
  }
}
