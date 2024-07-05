package pers.solid.mishang.uc.block;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.data.server.recipe.CraftingRecipeJsonBuilder;
import net.minecraft.data.server.recipe.ShapedRecipeJsonBuilder;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Direction;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import pers.solid.brrp.v1.api.RuntimeResourcePack;
import pers.solid.brrp.v1.model.ModelJsonBuilder;
import pers.solid.mishang.uc.arrp.BRRPHelper;
import pers.solid.mishang.uc.arrp.FasterJTextures;
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

    @Environment(EnvType.CLIENT)
    @Override
    public @NotNull ModelJsonBuilder getBlockModel() {
      return ModelJsonBuilder.create(new Identifier("mishanguc:block/road_with_angle_line"))
          .setTextures(new FasterJTextures().base("asphalt")
              .lineSide(lineSide)
              .lineSide2(lineSide2)
              .lineTop(lineTop));
    }

    @Environment(EnvType.CLIENT)
    @Override
    public void writeBlockModel(RuntimeResourcePack pack) {
      BRRPHelper.addModelWithSlabWithMirrored(pack, this);
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
      return ShapedRecipeJsonBuilder.create(getRecipeCategory(), self, 3)
          .patterns(patterns)
          .input('*', lineColor.getIngredient())
          .input('X', base)
          .criterionFromItemTag("has_paint", lineColor.getIngredient())
          .criterionFromItem(base)
          .setCustomRecipeCategory("roads");
    }
  }
}
