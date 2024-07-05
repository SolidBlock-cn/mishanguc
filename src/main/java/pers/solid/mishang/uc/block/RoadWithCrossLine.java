package pers.solid.mishang.uc.block;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.data.client.BlockStateModelGenerator;
import net.minecraft.data.client.BlockStateSupplier;
import net.minecraft.data.server.recipe.CraftingRecipeJsonBuilder;
import net.minecraft.data.server.recipe.ShapedRecipeJsonBuilder;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.math.Direction;
import org.jetbrains.annotations.NotNull;
import pers.solid.brrp.v1.api.RuntimeResourcePack;
import pers.solid.brrp.v1.model.ModelJsonBuilder;
import pers.solid.mishang.uc.MishangUtils;
import pers.solid.mishang.uc.arrp.BRRPHelper;
import pers.solid.mishang.uc.arrp.FasterJTextures;
import pers.solid.mishang.uc.util.*;

import java.util.List;

public interface RoadWithCrossLine extends Road {
  @Override
  default RoadConnectionState getConnectionStateOf(BlockState state, Direction direction) {
    return Road.super
        .getConnectionStateOf(state, direction)
        .or(new RoadConnectionState(RoadConnectionState.WhetherConnected.CONNECTED, getLineColor(state, direction), EightHorizontalDirection.of(direction), LineType.NORMAL));
  }

  class Impl extends AbstractRoadBlock implements RoadWithCrossLine {
    public Impl(Settings settings, LineColor lineColor) {
      super(settings, lineColor, LineType.NORMAL);
    }

    @Environment(EnvType.CLIENT)
    @Override
    public @NotNull BlockStateSupplier getBlockStates() {
      return BlockStateModelGenerator.createBlockStateWithRandomHorizontalRotations(this, getBlockModelId());
    }

    @Override
    public void appendDescriptionTooltip(List<Text> tooltip, TooltipContext options) {
      tooltip.add(TextBridge.translatable("lineType.cross.composed", lineColor.getName(), lineType.getName()).formatted(Formatting.BLUE));
    }

    @Environment(EnvType.CLIENT)
    @Override
    public @NotNull ModelJsonBuilder getBlockModel() {
      return ModelJsonBuilder.create("mishanguc", "block/road_with_cross_line")
          .setTextures(new FasterJTextures().base("asphalt")
              .lineSide(MishangUtils.composeStraightLineTexture(lineColor, LineType.NORMAL))
              .lineTop(lineColor.asString() + "_cross_line"));
    }

    @Environment(EnvType.CLIENT)
    @Override
    public void writeBlockModel(RuntimeResourcePack pack) {
      BRRPHelper.addModelWithSlab(pack, Impl.this);
    }

    @Override
    public CraftingRecipeJsonBuilder getPaintingRecipe(Block base, Block self) {
      return ShapedRecipeJsonBuilder.create(getRecipeCategory(), self, 4)
          .pattern("*X*")
          .pattern("X*X")
          .pattern("*X*")
          .input('*', lineColor.getIngredient())
          .input('X', base)
          .criterionFromItemTag("has_ingredient", lineColor.getIngredient())
          .criterionFromItem(base)
          .setCustomRecipeCategory("roads");
    }
  }
}
