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
import net.minecraft.client.renderer.block.model.VariantMutator;
import net.minecraft.core.Direction;
import net.minecraft.data.recipes.RecipeBuilder;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.data.recipes.ShapedRecipeBuilder;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.Item.TooltipContext;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import org.jetbrains.annotations.NotNull;
import pers.solid.mishang.uc.MishangUtils;
import pers.solid.mishang.uc.data.FasterTextureMap;
import pers.solid.mishang.uc.data.MishangucTextureKeys;
import pers.solid.mishang.uc.util.HorizontalCornerDirection;
import pers.solid.mishang.uc.util.LineColor;
import pers.solid.mishang.uc.util.LineType;
import pers.solid.mishang.uc.util.TextBridge;

import java.util.List;

/**
 * 类似于 {@link RoadWithAngleLine}，但是直角两边可能不同。
 */
public interface RoadWithDiffAngleLine extends RoadWithAngleLine {
  /**
   * 直角上该坐标轴上的边视为第二个边，另一个方向的边则视为第一个边。
   */
  EnumProperty<Direction.Axis> AXIS = BlockStateProperties.HORIZONTAL_AXIS;

  @Override
  default void appendRoadProperties(StateDefinition.Builder<Block, BlockState> builder) {
    RoadWithAngleLine.super.appendRoadProperties(builder);
    builder.add(AXIS);
  }

  @Override
  default BlockState rotateRoad(BlockState state, Rotation rotation) {
    return RoadWithAngleLine.super
        .rotateRoad(state, rotation)
        .setValue(AXIS, MishangUtils.rotateAxis(rotation, state.getValue(AXIS)));
  }

  @Override
  default BlockState withPlacementState(BlockState state, BlockPlaceContext ctx) {
    return RoadWithAngleLine.super
        .withPlacementState(state, ctx)
        .setValue(AXIS, ctx.getHorizontalDirection().getAxis());
  }

  class Impl extends RoadWithAngleLine.Impl implements RoadWithDiffAngleLine {
    public static final MapCodec<RoadWithDiffAngleLine.Impl> CODEC = RecordCodecBuilder.mapCodec(i -> i.group(propertiesCodec(), lineColorFieldCodec(), LineColor.CODEC.fieldOf("line_color2").forGetter(b -> b.lineColor2), lineTypeFieldCodec(), LineType.CODEC.fieldOf("line_type2").forGetter(b -> b.lineType2), RoadWithAngleLine.isBevelCodec()).apply(i, (settings, lineColor, lineColor2, lineType, lineType2, isBevel) -> new RoadWithDiffAngleLine.Impl(settings, lineColor, lineColor2, lineType, lineType2, isBevel, null, null)));
    public final LineColor lineColor2;
    public final LineType lineType2;
    private final String lineSide2;

    public Impl(
        Properties settings,
        LineColor lineColor,
        LineColor lineColor2,
        LineType lineType,
        LineType lineType2,
        boolean isBevel, String lineSide2, String lineTop) {
      super(settings, lineColor, lineType, isBevel, lineTop);
      this.lineColor2 = lineColor2;
      this.lineType2 = lineType2;
      this.lineSide2 = lineSide2;
    }

    @Override
    public LineColor getLineColor(BlockState state, Direction direction) {
      return state.getValue(AXIS) == direction.getAxis() ? lineColor2 : lineColor;
    }

    @Override
    public void appendDescriptionTooltip(List<Component> tooltip, TooltipContext options) {
      tooltip.add(TextBridge.translatable("lineType.diffAngleLine.composed.1", lineColor.getName(), lineType.getName()).withStyle(ChatFormatting.BLUE));
      tooltip.add(TextBridge.translatable("lineType.diffAngleLine.composed.2", lineColor2.getName(), lineType2.getName()).withStyle(ChatFormatting.BLUE));
    }

    @Override
    public LineType getLineType(BlockState state, Direction direction) {
      return state.getValue(AXIS) == direction.getAxis() ? lineType2 : lineType;
    }

    @Environment(EnvType.CLIENT)
    @Override
    protected <B extends Block & Road> void registerBaseOrSlabModels(B road, BlockModelGenerators blockStateModelGenerator) {
      final FasterTextureMap textures = new FasterTextureMap().base("asphalt").lineSide(lineSide).lineSide2(lineSide2).lineTop(lineTop);
      final Identifier id = road.uploadModel("_with_angle_line", textures, blockStateModelGenerator, MishangucTextureKeys.BASE, MishangucTextureKeys.LINE_SIDE, MishangucTextureKeys.LINE_SIDE2, MishangucTextureKeys.LINE_TOP);
      final Identifier mirroredId = road.uploadModel("_with_angle_line_mirrored", "_mirrored", textures, blockStateModelGenerator, MishangucTextureKeys.BASE, MishangucTextureKeys.LINE_SIDE, MishangucTextureKeys.LINE_SIDE2, MishangucTextureKeys.LINE_TOP);
      final var map = PropertyDispatch.initial(FACING, AXIS);
      // 一侧的短线所朝向的方向。
      for (Direction direction : Direction.Plane.HORIZONTAL) {
        final @NotNull Direction offsetDirection1 = direction.getClockWise();
        // direction 的右偏方向
        final @NotNull HorizontalCornerDirection facing1 = HorizontalCornerDirection.fromDirections(direction, offsetDirection1);
        final @NotNull Direction offsetDirection2 = direction.getCounterClockWise();
        // direction 的左偏方向
        final @NotNull HorizontalCornerDirection facing2 = HorizontalCornerDirection.fromDirections(direction, offsetDirection2);

        final Quadrant axisRotation = switch (direction) {
          case WEST -> Quadrant.R90;
          case NORTH -> Quadrant.R180;
          case EAST -> Quadrant.R270;
          default -> Quadrant.R0;
        };
        map
            .select(
                facing1, direction.getAxis(),
                BlockModelGenerators.plainVariant(id).with(VariantMutator.Y_ROT.withValue(axisRotation)))
            .select(
                facing2, direction.getAxis(),
                BlockModelGenerators.plainVariant(mirroredId)
                    .with(VariantMutator.Y_ROT.withValue(axisRotation)));
      }
      blockStateModelGenerator.blockStateOutput.accept(road.composeState(MultiVariantGenerator.dispatch(road).with(map)));
    }

    @Override
    protected MapCodec<? extends RoadWithDiffAngleLine.Impl> codec() {
      return CODEC;
    }

    private static final String[] NORMAL_PATTERN = {
        " a ",
        "bXX",
        " X "
    };
    private static final String[] HALF_THICK_PATTERN = {
        "aaa",
        "bXX",
        " X "
    };
    private static final String[] HALF_DOUBLE_PATTERN = {
        "ba ",
        " XX",
        "bX "
    };
    private static final String[] THICK_AND_DOUBLE_PATTERN = {
        "baa",
        "aXX",
        "bX "
    };

    private static String[] composePattern(LineType lineType, LineType lineType2) {
      if (lineType == LineType.THICK) {
        if (lineType2 == LineType.DOUBLE) {
          return THICK_AND_DOUBLE_PATTERN;
        } else if (lineType2 == LineType.NORMAL) {
          return HALF_THICK_PATTERN;
        }
      } else if (lineType == LineType.NORMAL) {
        if (lineType2 == LineType.DOUBLE) {
          return HALF_DOUBLE_PATTERN;
        } else if (lineType2 == LineType.NORMAL) {
          return NORMAL_PATTERN;
        }
      }
      throw new IllegalArgumentException(String.format("Cannot determine patterns for [%s, %s]", lineType.getSerializedName(), lineType2.getSerializedName()));
    }

    @Override
    public RecipeBuilder getPaintingRecipe(Block base, Block self, RecipeProvider recipeGenerator) {
      final String[] composePattern = composePattern(lineType, lineType2);
      final ShapedRecipeBuilder recipe = recipeGenerator.shaped(RecipeCategory.BUILDING_BLOCKS, self, 3)
          .pattern(composePattern[0])
          .pattern(composePattern[1])
          .pattern(composePattern[2])
          .define('a', lineColor.getIngredient())
          .define('b', lineColor2.getIngredient())
          .define('X', base)
          .unlockedBy("has_" + lineColor.getSerializedName() + "_paint", recipeGenerator.has(lineColor.getIngredient()))
          .unlockedBy(RecipeProvider.getHasName(base), recipeGenerator.has(base));
      if (lineColor != lineColor2) {
        recipe.unlockedBy("has_" + lineColor2.getSerializedName() + "_paint", recipeGenerator.has(lineColor2.getIngredient()));
      }
      return recipe;
    }
  }
}
