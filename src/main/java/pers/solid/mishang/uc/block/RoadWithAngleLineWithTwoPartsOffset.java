package pers.solid.mishang.uc.block;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.data.client.*;
import net.minecraft.data.server.recipe.CraftingRecipeJsonBuilder;
import net.minecraft.data.server.recipe.RecipeProvider;
import net.minecraft.data.server.recipe.ShapedRecipeJsonBuilder;
import net.minecraft.recipe.book.RecipeCategory;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Direction;
import org.jetbrains.annotations.ApiStatus;
import pers.solid.mishang.uc.MishangUtils;
import pers.solid.mishang.uc.data.FasterTextureMap;
import pers.solid.mishang.uc.data.MishangucTextureKeys;
import pers.solid.mishang.uc.util.LineColor;
import pers.solid.mishang.uc.util.LineOffset;
import pers.solid.mishang.uc.util.LineType;
import pers.solid.mishang.uc.util.RoadConnectionState;

@ApiStatus.AvailableSince("1.1.0")
public interface RoadWithAngleLineWithTwoPartsOffset extends RoadWithAngleLine {
  int offsetOutwards();

  @Override
  default RoadConnectionState getConnectionStateOf(BlockState state, Direction direction) {
    final RoadConnectionState connectionState = RoadWithAngleLine.super.getConnectionStateOf(state, direction);
    if (connectionState.mayConnect()) {
      return connectionState.createWithOffset(LineOffset.of(state.get(FACING).getDirectionInAxis(direction.rotateYClockwise().getAxis()).getOpposite(), offsetOutwards()));
    } else {
      return connectionState;
    }
  }

  class Impl extends RoadWithAngleLine.Impl implements RoadWithAngleLineWithTwoPartsOffset {
    protected final String lineSide;
    protected final String lineSide2;
    private final int offsetOutwards;

    public static final MapCodec<RoadWithAngleLineWithTwoPartsOffset.Impl> CODEC = RecordCodecBuilder.mapCodec(i -> i.group(createSettingsCodec(), lineColorFieldCodec(), lineTypeFieldCodec(), RoadWithAngleLine.isBevelCodec(), Codec.INT.fieldOf("offset_outwards").forGetter(b -> b.offsetOutwards)).apply(i, (settings, lineColor, lineType, isBevel, offsetOutwards) -> new RoadWithAngleLineWithTwoPartsOffset.Impl(settings, lineColor, lineType, isBevel, null, null, null, offsetOutwards)));

    public Impl(Settings settings, LineColor lineColor, LineType lineType, boolean isBevel, String lineTop, String lineSide, String lineSide2, int offsetOutwards) {
      super(settings, lineColor, lineType, isBevel, lineTop);
      this.lineSide = lineSide;
      this.lineSide2 = lineSide2;
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
      blockStateModelGenerator.blockStateCollector.accept(road.composeState(VariantsBlockStateSupplier.create(road, BlockStateVariant.create().put(VariantSettings.MODEL, modelId)).coordinate(BlockStateVariantMap.create(FACING).register(direction -> BlockStateVariant.create().put(MishangUtils.INT_Y_VARIANT, direction.asRotation() - 45)))));
    }

    @Override
    protected MapCodec<? extends RoadWithAngleLineWithTwoPartsOffset.Impl> getCodec() {
      return CODEC;
    }


    private static final String[] OUTER_OFFSET_BEVEL_PATTERN = {
        "**X",
        "*X ",
        " X "
    };
    private static final String[] INNER_OFFSET_BEVEL_PATTERN = {
        " *X",
        " X ",
        " X "
    };
    private static final String[] OUTER_OFFSET_RIGHT_ANGLE_PATTERN = {
        "** ",
        "*XX",
        "X  "
    };
    private static final String[] INNER_OFFSET_RIGHT_ANGLE_PATTERN = {
        " * ",
        "*XX",
        "X  "
    };

    @Override
    public CraftingRecipeJsonBuilder getPaintingRecipe(Block base, Block self) {
      final String[] patterns = switch (offsetOutwards) {
        case 2 -> isBevel() ? OUTER_OFFSET_BEVEL_PATTERN : OUTER_OFFSET_RIGHT_ANGLE_PATTERN;
        case -2 -> isBevel() ? INNER_OFFSET_BEVEL_PATTERN : INNER_OFFSET_RIGHT_ANGLE_PATTERN;
        default -> throw new IllegalStateException("Unexpected value: " + offsetOutwards);
      };
      return ShapedRecipeJsonBuilder.create(RecipeCategory.BUILDING_BLOCKS, self, 3)
          .pattern(patterns[0])
          .pattern(patterns[1])
          .pattern(patterns[2])
          .input('*', lineColor.getIngredient())
          .input('X', base)
          .criterion("has_paint", RecipeProvider.conditionsFromTag(lineColor.getIngredient()))
          .criterion(RecipeProvider.hasItem(base), RecipeProvider.conditionsFromItem(base));
    }
  }
}
