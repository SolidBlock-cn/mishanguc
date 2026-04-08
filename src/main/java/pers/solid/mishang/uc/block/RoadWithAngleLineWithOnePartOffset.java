package pers.solid.mishang.uc.block;

import com.mojang.math.Quadrant;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.ChatFormatting;
import net.minecraft.client.data.models.BlockModelGenerators;
import net.minecraft.client.data.models.blockstates.MultiVariantGenerator;
import net.minecraft.client.data.models.blockstates.PropertyDispatch;
import net.minecraft.client.renderer.block.dispatch.VariantMutator;
import net.minecraft.core.Direction;
import net.minecraft.data.recipes.RecipeBuilder;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.util.Util;
import net.minecraft.world.item.Item.TooltipContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import pers.solid.mishang.uc.MishangUtils;
import pers.solid.mishang.uc.data.FasterTextureMap;
import pers.solid.mishang.uc.data.MishangucTextureKeys;
import pers.solid.mishang.uc.util.*;

import java.util.List;

public interface RoadWithAngleLineWithOnePartOffset extends RoadWithAngleLine {
  /**
   * 该道路方块的直角两边中，哪个轴上的保持中心（另一个轴的将会偏移）。
   */
  EnumProperty<Direction.Axis> AXIS = BlockStateProperties.HORIZONTAL_AXIS;

  @Override
  default void appendRoadProperties(StateDefinition.Builder<Block, BlockState> builder) {
    RoadWithAngleLine.super.appendRoadProperties(builder);
    builder.add(AXIS);
  }

  @Override
  default BlockState mirrorRoad(BlockState state, Mirror mirror) {
    return RoadWithAngleLine.super.mirrorRoad(state, mirror);
  }

  @Override
  default BlockState rotateRoad(BlockState state, Rotation rotation) {
    return RoadWithAngleLine.super
        .rotateRoad(state, rotation)
        .setValue(
            AXIS,
            Util.make(
                () -> {
                  final Direction.Axis axis = state.getValue(AXIS);
                  return switch (rotation) {
                    case COUNTERCLOCKWISE_90, CLOCKWISE_90 -> switch (axis) {
                      case X -> (Direction.Axis.Z);
                      case Z -> (Direction.Axis.X);
                      default -> axis;
                    };
                    default -> axis;
                  };
                }));
  }

  @Override
  default RoadConnectionState getConnectionStateOf(BlockState state, Direction direction) {
    final RoadConnectionState connectionState = RoadWithAngleLine.super.getConnectionStateOf(state, direction);
    if (connectionState.mayConnect() && direction.getAxis() != state.getValue(AXIS)) {
      return connectionState.createWithOffset(LineOffset.of(state.getValue(FACING).getDirectionInAxis(direction.getClockWise().getAxis()).getOpposite(), offsetOutwards()));
    } else {
      return connectionState;
    }
  }

  @Override
  default BlockState withPlacementState(BlockState state, BlockPlaceContext ctx) {
    return RoadWithAngleLine.super
        .withPlacementState(state, ctx)
        .setValue(AXIS, ctx.getHorizontalDirection().getAxis());
  }

  @Override
  default void appendRoadTooltip(
      ItemStack stack, TooltipContext context, List<Component> tooltip, TooltipFlag options) {
    RoadWithAngleLine.super.appendRoadTooltip(stack, context, tooltip, options);
    tooltip.add(
        Component.translatable("block.mishanguc.tooltip.road_with_angle_line_with_one_part_offset.1")
            .withStyle(ChatFormatting.GRAY));
    tooltip.add(
        Component.translatable("block.mishanguc.tooltip.road_with_angle_line_with_one_part_offset.2")
            .withStyle(ChatFormatting.GRAY));
  }

  int offsetOutwards();

  class Impl extends RoadWithAngleLine.Impl implements RoadWithAngleLineWithOnePartOffset {
    public static final MapCodec<RoadWithAngleLineWithOnePartOffset.Impl> CODEC = RecordCodecBuilder.mapCodec(i -> i.group(propertiesCodec(), lineColorFieldCodec(), RoadWithAngleLine.isBevelCodec(), Codec.INT.fieldOf("offset_outwards").forGetter(b -> b.offsetOutwards)).apply(i, (settings, lineColor, isBevel, offsetOutwards) -> new RoadWithAngleLineWithOnePartOffset.Impl(settings, lineColor, isBevel, null, null, offsetOutwards)));
    private final String lineSide2;
    private final int offsetOutwards;

    public Impl(Properties settings, LineColor lineColor, boolean isBevel, String lineSide, String lineTop, int offsetOutwards) {
      super(settings, lineColor, LineType.NORMAL, lineSide, isBevel, lineTop);
      this.lineSide2 = MishangUtils.composeStraightLineTexture(lineColor, LineType.NORMAL);
      this.offsetOutwards = offsetOutwards;
    }

    @Override
    public int offsetOutwards() {
      return offsetOutwards;
    }

    @Environment(EnvType.CLIENT)
    @Override
    protected <B extends Block & Road> void registerBaseOrSlabModels(B road, BlockModelGenerators blockStateModelGenerator) {
      final FasterTextureMap textures = new FasterTextureMap().base("asphalt")
          .lineSide(lineSide)
          .lineSide2(lineSide2)
          .lineTop(lineTop);
      final Identifier modelId = road.uploadModel("_with_angle_line", textures, blockStateModelGenerator, MishangucTextureKeys.BASE, MishangucTextureKeys.LINE_SIDE, MishangucTextureKeys.LINE_SIDE2, MishangucTextureKeys.LINE_TOP);
      final Identifier mirroredModelId = road.uploadModel("_with_angle_line_mirrored", "_mirrored", textures, blockStateModelGenerator, MishangucTextureKeys.BASE, MishangucTextureKeys.LINE_SIDE, MishangucTextureKeys.LINE_SIDE2, MishangucTextureKeys.LINE_TOP);
      final var map = PropertyDispatch.initial(FACING, AXIS);
      for (Direction direction : Direction.Plane.HORIZONTAL) {
        // direction：正中线所朝的方向
        final Direction offsetDirection1 = direction.getClockWise();
        final Direction offsetDirection2 = direction.getCounterClockWise();


        final Quadrant axisRotation = switch (direction) {
          case WEST -> Quadrant.R90;
          case NORTH -> Quadrant.R180;
          case EAST -> Quadrant.R270;
          default -> Quadrant.R0;
        };
        map.select(
            HorizontalCornerDirection.fromDirections(direction, offsetDirection1),
            direction.getAxis(),
            BlockModelGenerators.plainVariant(modelId).with(VariantMutator.Y_ROT.withValue(axisRotation)));
        map.select(
            HorizontalCornerDirection.fromDirections(direction, offsetDirection2),
            direction.getAxis(),
            BlockModelGenerators.plainVariant(mirroredModelId).with(VariantMutator.Y_ROT.withValue(axisRotation)));
      }
      blockStateModelGenerator.blockStateOutput.accept(road.composeState(MultiVariantGenerator.dispatch(road).with(map)));
    }

    @Override
    protected MapCodec<? extends RoadWithAngleLineWithOnePartOffset.Impl> codec() {
      return CODEC;
    }

    @Override
    public RecipeBuilder getPaintingRecipe(Block base, Block self, RecipeProvider recipeGenerator) {
      if (isBevel()) {
        throw new UnsupportedOperationException("Recipes for bevel line with one part offset is not supported!");
      }
      return recipeGenerator.shaped(RecipeCategory.BUILDING_BLOCKS, self, 3)
          .pattern("  *")
          .pattern("*XX")
          .pattern(" X ")
          .define('*', lineColor.getIngredient())
          .define('X', base)
          .unlockedBy("has_paint", recipeGenerator.has(lineColor.getIngredient()))
          .unlockedBy(RecipeProvider.getHasName(base), recipeGenerator.has(base));
    }
  }
}
