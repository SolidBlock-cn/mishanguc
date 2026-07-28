package pers.solid.mishang.uc.block;

import com.mojang.math.Quadrant;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.ChatFormatting;
import net.minecraft.client.data.models.BlockModelGenerators;
import net.minecraft.client.data.models.blockstates.MultiVariantGenerator;
import net.minecraft.client.data.models.blockstates.PropertyDispatch;
import net.minecraft.client.data.models.model.TextureMapping;
import net.minecraft.client.renderer.block.model.VariantMutator;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.data.recipes.RecipeBuilder;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.Item.TooltipContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.ScheduledTickAccess;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.SlabBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import pers.solid.mishang.uc.MishangUtils;
import pers.solid.mishang.uc.MishangucProperties;
import pers.solid.mishang.uc.blocks.RoadBlocks;
import pers.solid.mishang.uc.data.FasterTextureMap;
import pers.solid.mishang.uc.data.MishangucTextureKeys;
import pers.solid.mishang.uc.util.*;

import java.util.List;
import java.util.function.Supplier;

public interface RoadWithStraightAndAngleLine extends RoadWithAngleLine, RoadWithStraightLine {
  BooleanProperty BEVEL_TOP = MishangucProperties.BEVEL_TOP;

  @Override
  default void appendRoadProperties(StateDefinition.Builder<Block, BlockState> builder) {
    RoadWithAngleLine.super.appendRoadProperties(builder);
    RoadWithStraightLine.super.appendRoadProperties(builder);
  }

  @Override
  default RoadConnectionState getConnectionStateOf(BlockState state, Direction direction) {
    return RoadConnectionState.or(
        RoadWithStraightLine.super.getConnectionStateOf(state, direction),
        RoadWithAngleLine.super.getConnectionStateOf(state, direction));
  }

  @Override
  default BlockState mirrorRoad(BlockState state, Mirror mirror) {
    return RoadWithAngleLine.super.mirrorRoad(state, mirror);
  }

  @Override
  default BlockState rotateRoad(BlockState state, Rotation rotation) {
    return RoadWithStraightLine.super.rotateRoad(
        RoadWithAngleLine.super.rotateRoad(state, rotation), rotation);
  }

  @Override
  default BlockState withPlacementState(BlockState state, BlockPlaceContext ctx) {
    return RoadWithStraightLine.super.withPlacementState(
        RoadWithAngleLine.super.withPlacementState(state, ctx), ctx);
  }

  @Override
  default void appendRoadTooltip(
      ItemStack stack, TooltipContext context, List<Component> tooltip, TooltipFlag options) {
    RoadWithAngleLine.super.appendRoadTooltip(stack, context, tooltip, options);
    RoadWithStraightLine.super.appendRoadTooltip(stack, context, tooltip, options);
  }

  class Impl extends AbstractRoadBlock implements RoadWithStraightAndAngleLine {
    public static final MapCodec<RoadWithStraightAndAngleLine.Impl> CODEC = RecordCodecBuilder.mapCodec(i -> i.group(propertiesCodec(), lineColorFieldCodec(), LineColor.CODEC.fieldOf("line_color_side").forGetter(b -> b.lineColorSide), lineTypeFieldCodec(), LineType.CODEC.fieldOf("line_type_side").forGetter(b -> b.lineTypeSide)).apply(i, RoadWithStraightAndAngleLine.Impl::new));
    /**
     * 用于构造函数，道路是否拥有 {@link #BEVEL_TOP} 属性。在构造函数调用之前就应该被计算。
     */
    private static boolean hasBevelTopProperty;
    private final LineColor lineColorSide;
    private final LineType lineTypeSide;

    public Impl(Properties settings, LineColor lineColor, LineColor lineColorSide, LineType lineType, LineType lineTypeSide) {
      super(settings, lineColor, ((Supplier<LineType>) () -> {
        hasBevelTopProperty = lineColor != lineColorSide;
        return lineType;
      }).get());
      this.lineColorSide = lineColorSide;
      this.lineTypeSide = lineTypeSide;
      if (hasBevelTopProperty) {
        registerDefaultState(defaultBlockState().setValue(BEVEL_TOP, false));
      }
    }

    public Impl(Properties settings, LineColor lineColor, LineType lineType) {
      this(settings, lineColor, lineColor, lineType, lineType);
    }

    @Override
    public boolean isBevel() {
      return true;
    }

    @Override
    public void appendRoadProperties(StateDefinition.Builder<Block, BlockState> builder) {
      RoadWithStraightAndAngleLine.super.appendRoadProperties(builder);
      if (hasBevelTopProperty) {
        builder.add(BEVEL_TOP);
      }
    }

    @Override
    public @Nullable BlockState getStateForPlacement(BlockPlaceContext ctx) {
      final BlockState placementState = super.getStateForPlacement(ctx);
      if (placementState == null) return null;
      final Direction direction = placementState.getValue(FACING).getDirectionInAxis(placementState.getValue(AXIS));
      final BlockPos blockPos = ctx.getClickedPos();
      final BlockPos neighborPos = blockPos.relative(direction);
      final Level world = ctx.getLevel();
      return updateShape(placementState, world, world, blockPos, direction, neighborPos, world.getBlockState(neighborPos), world.random);
    }

    @Override
    protected BlockState updateShape(BlockState state, LevelReader world, ScheduledTickAccess tickView, BlockPos pos, Direction direction, BlockPos neighborPos, BlockState neighborState, RandomSource random) {
      BlockState stateForNeighborUpdate = super.updateShape(state, world, tickView, pos, direction, neighborPos, neighborState, random);
      if (stateForNeighborUpdate.hasProperty(BEVEL_TOP) && stateForNeighborUpdate.getValue(AXIS).test(direction) && stateForNeighborUpdate.getValue(FACING).hasDirection(direction)) {
        // 如果连接的那个方块在连接部分的道路标线与当前道路的斜线部分颜色一致，那么 bevel_top = true。
        final Block neighborBlock = neighborState.getBlock();
        final boolean bevelTop = neighborBlock instanceof Road road && road.getLineColor(neighborState, direction.getOpposite()) == lineColorSide;
        if (bevelTop) {
          return stateForNeighborUpdate.setValue(BEVEL_TOP, true);
        } else {
          final BlockPos up = neighborPos.above();
          final BlockState upState = world.getBlockState(up);
          if (upState.getBlock() instanceof Road road && road.getLineColor(upState, direction.getOpposite()) == lineColorSide) {
            return stateForNeighborUpdate.setValue(BEVEL_TOP, true);
          }
          final BlockPos down = neighborPos.below();
          final BlockState downState = world.getBlockState(down);
          if (downState.getBlock() instanceof Road road && road.getLineColor(downState, direction.getOpposite()) == lineColorSide) {
            return stateForNeighborUpdate.setValue(BEVEL_TOP, true);
          }
        }
        return stateForNeighborUpdate.setValue(BEVEL_TOP, false);
      }
      return stateForNeighborUpdate;
    }

    @Environment(EnvType.CLIENT)
    @Override
    protected <B extends Block & Road> void registerBaseOrSlabModels(B road, BlockModelGenerators blockStateModelGenerator) {
      final String lineTopStraight = MishangUtils.composeStraightLineTexture(lineColor, lineType);
      final String lineTopAngle = MishangUtils.composeAngleLineTexture(lineColorSide, LineType.NORMAL, true);
      final String lineSide = lineTopStraight;
      final String lineSide2 = MishangUtils.composeStraightLineTexture(lineColorSide, lineTypeSide);
      final FasterTextureMap textures = new FasterTextureMap()
          .base("asphalt")
          .lineTop(lineTopAngle)
          .lineTop2(lineTopStraight)
          .lineSide(lineSide)
          .lineSide2(lineSide2);
      final Identifier modelId = road.uploadModel("_with_straight_and_angle_line", textures, blockStateModelGenerator, MishangucTextureKeys.BASE, MishangucTextureKeys.LINE_TOP, MishangucTextureKeys.LINE_TOP2, MishangucTextureKeys.LINE_SIDE, MishangucTextureKeys.LINE_SIDE2);
      final Identifier mirroredModelId = road.uploadModel("_with_straight_and_angle_line_mirrored", "_mirrored", textures, blockStateModelGenerator, MishangucTextureKeys.BASE, MishangucTextureKeys.LINE_TOP, MishangucTextureKeys.LINE_TOP2, MishangucTextureKeys.LINE_SIDE, MishangucTextureKeys.LINE_SIDE2);

      final Identifier beveledTopModelId, beveledTopMirroredModelId;
      if (stateDefinition.getProperties().contains(BEVEL_TOP)) {
        TextureMapping textures2 = new FasterTextureMap()
            .base("asphalt")
            .lineTop(lineTopStraight)
            .lineTop2(lineTopAngle)
            .lineSide(lineSide)
            .lineSide2(lineSide2)
            .varP(MishangucTextureKeys.LINE_SIDE3, lineSide2);

        beveledTopModelId = road.uploadModel("_with_straight_and_angle_line", "_bevel_top", textures2, blockStateModelGenerator, MishangucTextureKeys.BASE, MishangucTextureKeys.LINE_TOP, MishangucTextureKeys.LINE_TOP2, MishangucTextureKeys.LINE_SIDE, MishangucTextureKeys.LINE_SIDE2, MishangucTextureKeys.LINE_SIDE3);
        beveledTopMirroredModelId = road.uploadModel("_with_straight_and_angle_line_mirrored", "_bevel_top_mirrored", textures2, blockStateModelGenerator, MishangucTextureKeys.BASE, MishangucTextureKeys.LINE_TOP, MishangucTextureKeys.LINE_TOP2, MishangucTextureKeys.LINE_SIDE, MishangucTextureKeys.LINE_SIDE2, MishangucTextureKeys.LINE_SIDE3);
      } else {
        beveledTopModelId = beveledTopMirroredModelId = null;
      }

      final boolean hasBevelTop = lineColor != lineColorSide;
      final var map1 = hasBevelTop ? null : PropertyDispatch.initial(AXIS, FACING);
      final var map2 = hasBevelTop ? PropertyDispatch.initial(AXIS, FACING, BEVEL_TOP) : null;
      for (Direction direction : Direction.Plane.HORIZONTAL) {
        final Quadrant axisRotation = switch (direction) {
          case WEST -> Quadrant.R90;
          case NORTH -> Quadrant.R180;
          case EAST -> Quadrant.R270;
          default -> Quadrant.R0;
        };
        final Quadrant axisRotationCCW90 = switch (direction) {
          case WEST -> Quadrant.R0;
          case NORTH -> Quadrant.R90;
          case EAST -> Quadrant.R180;
          default -> Quadrant.R270;
        };
        final Direction.Axis axis = direction.getAxis();
        final @NotNull HorizontalCornerDirection facing1 = HorizontalCornerDirection.fromDirections(direction, direction.getClockWise());
        final @NotNull HorizontalCornerDirection facing2 = HorizontalCornerDirection.fromDirections(direction, direction.getCounterClockWise());
        if (hasBevelTop) {
          map2.select(axis, facing1, false,
              BlockModelGenerators.plainVariant(modelId).with(VariantMutator.Y_ROT.withValue(axisRotation)));
          map2.select(axis, facing1, true,
              BlockModelGenerators.plainVariant(beveledTopModelId).with(VariantMutator.Y_ROT.withValue(axisRotation)));
          map2.select(axis, facing2, false,
              BlockModelGenerators.plainVariant(mirroredModelId).with(VariantMutator.Y_ROT.withValue(axisRotationCCW90)));
          map2.select(axis, facing2, true,
              BlockModelGenerators.plainVariant(beveledTopMirroredModelId).with(VariantMutator.Y_ROT.withValue(axisRotationCCW90)));
        } else {
          map1.select(
              axis, facing1,
              BlockModelGenerators.plainVariant(modelId).with(VariantMutator.Y_ROT.withValue(axisRotation)));
          map1.select(
              axis, facing2,
              BlockModelGenerators.plainVariant(mirroredModelId).with(VariantMutator.Y_ROT.withValue(axisRotationCCW90)));
        }
      }
      blockStateModelGenerator.blockStateOutput.accept(road.composeState(MultiVariantGenerator.dispatch(road).with(hasBevelTop ? map2 : map1)));
    }

    @Override
    public LineColor getLineColor(BlockState state, Direction direction) {
      if (state.getValue(FACING).hasDirection(direction) && (state.hasProperty(BEVEL_TOP) && state.getValue(BEVEL_TOP) || !state.getValue(AXIS).test(direction))) {
        return lineColorSide;
      }
      return super.getLineColor(state, direction);
    }

    @Override
    public LineType getLineType(BlockState state, Direction direction) {
      if (state.getValue(FACING).hasDirection(direction) && !state.getValue(AXIS).test(direction)) {
        return lineTypeSide;
      }
      return super.getLineType(state, direction);
    }

    @Override
    public void appendDescriptionTooltip(List<Component> tooltip, TooltipContext options) {
      if (lineColor == lineColorSide && lineType == lineTypeSide) {
        tooltip.add(TextBridge.translatable("lineType.straightAndAngle.same", lineColor.getName(), lineType.getName()).withStyle(ChatFormatting.BLUE));
      } else {
        tooltip.add(TextBridge.translatable("lineType.straightAndAngle.straight", lineColor.getName(), lineType.getName()).withStyle(ChatFormatting.BLUE));
        tooltip.add(TextBridge.translatable("lineType.straightAndAngle.bevel", lineColorSide.getName(), lineTypeSide.getName()).withStyle(ChatFormatting.BLUE));
      }
    }

    @Override
    protected MapCodec<? extends RoadWithStraightAndAngleLine.Impl> codec() {
      return CODEC;
    }

    @Override
    public RecipeBuilder getPaintingRecipe(Block base, Block self, RecipeProvider recipeGenerator) {
      if (lineTypeSide != LineType.NORMAL) {
        throw new UnsupportedOperationException();
      }
      Block base2 = RoadBlocks.getRoadBlockWithLine(lineColor, lineType);
      if (base instanceof SlabBlock) {
        base2 = ((AbstractRoadBlock) base2).getRoadSlab();
      }
      return recipeGenerator.shaped(RecipeCategory.BUILDING_BLOCKS, self, 3)
          .pattern(" *X")
          .pattern("*X ")
          .pattern("X  ")
          .define('*', lineColorSide.getIngredient())
          .define('X', base2)
          .unlockedBy("has_paint", recipeGenerator.has(lineColorSide.getIngredient()))
          .unlockedBy(RecipeProvider.getHasName(base2), recipeGenerator.has(base2));
    }
  }
}
