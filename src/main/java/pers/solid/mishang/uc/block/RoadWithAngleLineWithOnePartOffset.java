package pers.solid.mishang.uc.block;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.data.client.*;
import net.minecraft.data.server.recipe.CraftingRecipeJsonBuilder;
import net.minecraft.data.server.recipe.RecipeProvider;
import net.minecraft.data.server.recipe.ShapedRecipeJsonBuilder;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.book.RecipeCategory;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.EnumProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.text.Text;
import net.minecraft.util.*;
import net.minecraft.util.math.Direction;
import net.minecraft.world.BlockView;
import org.jetbrains.annotations.Nullable;
import pers.solid.mishang.uc.MishangUtils;
import pers.solid.mishang.uc.data.FasterTextureMap;
import pers.solid.mishang.uc.data.MishangucTextureKeys;
import pers.solid.mishang.uc.util.*;

import java.util.List;

public interface RoadWithAngleLineWithOnePartOffset extends RoadWithAngleLine {
  /**
   * 该道路方块的直角两边中，哪个轴上的保持中心（另一个轴的将会偏移）。
   */
  EnumProperty<Direction.Axis> AXIS = Properties.HORIZONTAL_AXIS;

  @Override
  default void appendRoadProperties(StateManager.Builder<Block, BlockState> builder) {
    RoadWithAngleLine.super.appendRoadProperties(builder);
    builder.add(AXIS);
  }

  @Override
  default BlockState mirrorRoad(BlockState state, BlockMirror mirror) {
    return RoadWithAngleLine.super.mirrorRoad(state, mirror);
  }

  @Override
  default BlockState rotateRoad(BlockState state, BlockRotation rotation) {
    return RoadWithAngleLine.super
        .rotateRoad(state, rotation)
        .with(
            AXIS,
            Util.make(
                () -> {
                  final Direction.Axis axis = state.get(AXIS);
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
    if (connectionState.mayConnect() && direction.getAxis() != state.get(AXIS)) {
      return connectionState.createWithOffset(LineOffset.of(state.get(FACING).getDirectionInAxis(direction.rotateYClockwise().getAxis()).getOpposite(), offsetOutwards()));
    } else {
      return connectionState;
    }
  }

  @Override
  default BlockState withPlacementState(BlockState state, ItemPlacementContext ctx) {
    return RoadWithAngleLine.super
        .withPlacementState(state, ctx)
        .with(AXIS, ctx.getHorizontalPlayerFacing().getAxis());
  }

  @Override
  default void appendRoadTooltip(
      ItemStack stack, @Nullable BlockView world, List<Text> tooltip, TooltipContext options) {
    RoadWithAngleLine.super.appendRoadTooltip(stack, world, tooltip, options);
    tooltip.add(
        TextBridge.translatable("block.mishanguc.tooltip.road_with_angle_line_with_one_part_offset.1")
            .formatted(Formatting.GRAY));
    tooltip.add(
        TextBridge.translatable("block.mishanguc.tooltip.road_with_angle_line_with_one_part_offset.2")
            .formatted(Formatting.GRAY));
  }

  int offsetOutwards();

  class Impl extends RoadWithAngleLine.Impl implements RoadWithAngleLineWithOnePartOffset {
    public static final MapCodec<RoadWithAngleLineWithOnePartOffset.Impl> CODEC = RecordCodecBuilder.mapCodec(i -> i.group(createSettingsCodec(), lineColorFieldCodec(), RoadWithAngleLine.isBevelCodec(), Codec.INT.fieldOf("offset_outwards").forGetter(b -> b.offsetOutwards)).apply(i, (settings, lineColor, isBevel, offsetOutwards) -> new RoadWithAngleLineWithOnePartOffset.Impl(settings, lineColor, isBevel, null, null, offsetOutwards)));
    private final String lineSide2;
    private final int offsetOutwards;

    public Impl(Settings settings, LineColor lineColor, boolean isBevel, String lineSide, String lineTop, int offsetOutwards) {
      super(settings, lineColor, LineType.NORMAL, lineSide, isBevel, lineTop);
      this.lineSide2 = MishangUtils.composeStraightLineTexture(lineColor, LineType.NORMAL);
      this.offsetOutwards = offsetOutwards;
    }

    @Override
    public int offsetOutwards() {
      return offsetOutwards;
    }

    @Override
    protected <B extends Block & Road> void registerBaseOrSlabModels(B road, BlockStateModelGenerator blockStateModelGenerator) {
      final FasterTextureMap textures = new FasterTextureMap().base("asphalt")
          .lineSide(lineSide)
          .lineSide2(lineSide2)
          .lineTop(lineTop);
      final Identifier modelId = road.uploadModel("_with_angle_line", textures, blockStateModelGenerator, MishangucTextureKeys.BASE, MishangucTextureKeys.LINE_SIDE, MishangucTextureKeys.LINE_SIDE2, MishangucTextureKeys.LINE_TOP);
      final Identifier mirroredModelId = road.uploadModel("_with_angle_line_mirrored", "_mirrored", textures, blockStateModelGenerator, MishangucTextureKeys.BASE, MishangucTextureKeys.LINE_SIDE, MishangucTextureKeys.LINE_SIDE2, MishangucTextureKeys.LINE_TOP);
      final BlockStateVariantMap.DoubleProperty<HorizontalCornerDirection, Direction.Axis> map = BlockStateVariantMap.create(FACING, AXIS);
      for (Direction direction : Direction.Type.HORIZONTAL) {
        // direction：正中线所朝的方向
        final Direction offsetDirection1 = direction.rotateYClockwise();
        final Direction offsetDirection2 = direction.rotateYCounterclockwise();
        map.register(
            HorizontalCornerDirection.fromDirections(direction, offsetDirection1),
            direction.getAxis(),
            BlockStateVariant.create().put(VariantSettings.MODEL, modelId).put(MishangUtils.DIRECTION_Y_VARIANT, direction));
        map.register(
            HorizontalCornerDirection.fromDirections(direction, offsetDirection2),
            direction.getAxis(),
            BlockStateVariant.create().put(VariantSettings.MODEL, mirroredModelId)
                .put(MishangUtils.DIRECTION_Y_VARIANT, direction));
      }
      blockStateModelGenerator.blockStateCollector.accept(road.composeState(VariantsBlockStateSupplier.create(road).coordinate(map)));
    }

    @Override
    protected MapCodec<? extends RoadWithAngleLineWithOnePartOffset.Impl> getCodec() {
      return CODEC;
    }

    @Override
    public CraftingRecipeJsonBuilder getPaintingRecipe(Block base, Block self) {
      if (isBevel()) {
        throw new UnsupportedOperationException("Recipes for bevel line with one part offset is not supported!");
      }
      return ShapedRecipeJsonBuilder.create(RecipeCategory.BUILDING_BLOCKS, self, 3)
          .pattern("  *")
          .pattern("*XX")
          .pattern(" X ")
          .input('*', lineColor.getIngredient())
          .input('X', base)
          .criterion("has_paint", RecipeProvider.conditionsFromTag(lineColor.getIngredient()))
          .criterion(RecipeProvider.hasItem(base), RecipeProvider.conditionsFromItem(base));
    }
  }
}
