package pers.solid.mishang.uc.block;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.data.models.BlockModelGenerators;
import net.minecraft.client.data.models.blockstates.MultiVariantGenerator;
import net.minecraft.client.data.models.blockstates.PropertyDispatch;
import net.minecraft.client.renderer.block.dispatch.VariantMutator;
import net.minecraft.core.Direction;
import net.minecraft.data.recipes.RecipeBuilder;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.resources.Identifier;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Nullable;
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
      return connectionState.createWithOffset(LineOffset.of(state.getValue(FACING).getDirectionInAxis(direction.getClockWise().getAxis()).getOpposite(), offsetOutwards()));
    } else {
      return connectionState;
    }
  }

  class Impl extends RoadWithAngleLine.Impl implements RoadWithAngleLineWithTwoPartsOffset {
    protected final @Nullable String lineSide;
    protected final @Nullable String lineSide2;
    private final int offsetOutwards;

    public static final MapCodec<RoadWithAngleLineWithTwoPartsOffset.Impl> CODEC = RecordCodecBuilder.mapCodec(i -> i.group(propertiesCodec(), lineColorFieldCodec(), lineTypeFieldCodec(), RoadWithAngleLine.isBevelCodec(), Codec.INT.fieldOf("offset_outwards").forGetter(b -> b.offsetOutwards)).apply(i, (settings, lineColor, lineType, isBevel, offsetOutwards) -> new RoadWithAngleLineWithTwoPartsOffset.Impl(settings, lineColor, lineType, isBevel, null, null, null, offsetOutwards)));

    public Impl(Properties settings, LineColor lineColor, LineType lineType, boolean isBevel, @Nullable String lineTop, @Nullable String lineSide, @Nullable String lineSide2, int offsetOutwards) {
      super(settings, lineColor, lineType, isBevel, lineTop);
      this.lineSide = lineSide;
      this.lineSide2 = lineSide2;
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
      blockStateModelGenerator.blockStateOutput.accept(road.composeState(MultiVariantGenerator.dispatch(road, BlockModelGenerators.plainVariant(modelId)).with(PropertyDispatch.modify(FACING).generate(direction -> VariantMutator.Y_ROT.withValue(direction.asAxisRotationCCW45())))));
    }

    @Override
    protected MapCodec<? extends RoadWithAngleLineWithTwoPartsOffset.Impl> codec() {
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
    public RecipeBuilder getPaintingRecipe(Block base, Block self, RecipeProvider recipeGenerator) {
      final String[] patterns = switch (offsetOutwards) {
        case 2 -> isBevel() ? OUTER_OFFSET_BEVEL_PATTERN : OUTER_OFFSET_RIGHT_ANGLE_PATTERN;
        case -2 -> isBevel() ? INNER_OFFSET_BEVEL_PATTERN : INNER_OFFSET_RIGHT_ANGLE_PATTERN;
        default -> throw new IllegalStateException("Unexpected value: " + offsetOutwards);
      };
      return recipeGenerator.shaped(RecipeCategory.BUILDING_BLOCKS, self, 3)
          .pattern(patterns[0])
          .pattern(patterns[1])
          .pattern(patterns[2])
          .define('*', lineColor.getIngredient())
          .define('X', base)
          .unlockedBy("has_paint", recipeGenerator.has(lineColor.getIngredient()))
          .unlockedBy(RecipeProvider.getHasName(base), recipeGenerator.has(base));
    }
  }
}
