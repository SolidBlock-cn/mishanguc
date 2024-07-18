package pers.solid.mishang.uc.block;

import com.google.common.collect.ImmutableList;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.client.item.TooltipType;
import net.minecraft.data.client.*;
import net.minecraft.data.server.recipe.CraftingRecipeJsonBuilder;
import net.minecraft.data.server.recipe.ShapedRecipeJsonBuilder;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item.TooltipContext;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemStack;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.EnumProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.text.Text;
import net.minecraft.util.BlockRotation;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Direction;
import org.jetbrains.annotations.NotNull;
import pers.solid.brrp.v1.api.RuntimeResourcePack;
import pers.solid.brrp.v1.model.ModelJsonBuilder;
import pers.solid.mishang.uc.arrp.BRRPHelper;
import pers.solid.mishang.uc.arrp.FasterJTextures;
import pers.solid.mishang.uc.util.*;

import java.util.List;

public interface RoadWithStraightLine extends Road {
  EnumProperty<Direction.Axis> AXIS = Properties.HORIZONTAL_AXIS;

  @Override
  default void appendRoadProperties(StateManager.Builder<Block, BlockState> builder) {
    Road.super.appendRoadProperties(builder);
    builder.add(AXIS);
  }

  @Override
  default RoadConnectionState getConnectionStateOf(BlockState state, Direction direction) {
    Direction.Axis axis = state.get(AXIS);
    return RoadConnectionState.of(
        direction.getAxis() == axis,
        getLineColor(state, direction),
        EightHorizontalDirection.of(direction),
        getLineType(state, direction), null);
  }

  @Override
  default BlockState rotateRoad(BlockState state, BlockRotation rotation) {
    Direction.Axis axis = state.get(AXIS);
    Direction.Axis rotatedAxis = switch (rotation) {
      case CLOCKWISE_90, COUNTERCLOCKWISE_90 -> axis == Direction.Axis.X
          ? Direction.Axis.Z
          : axis == Direction.Axis.Z ? Direction.Axis.X : axis;
      default -> axis;
    };
    return state.with(AXIS, rotatedAxis);
  }

  @Override
  default BlockState withPlacementState(BlockState state, ItemPlacementContext ctx) {
    final PlayerEntity player = ctx.getPlayer();
    final Direction playerFacing = ctx.getHorizontalPlayerFacing();
    return state.with(
        AXIS,
        (player != null && player.isSneaking() ? playerFacing.rotateYClockwise() : playerFacing)
            .getAxis());
  }

  @Override
  default void appendRoadTooltip(
      ItemStack stack, TooltipContext context, List<Text> tooltip, TooltipType options) {
    Road.super.appendRoadTooltip(stack, context, tooltip, options);
    tooltip.add(
        TextBridge.translatable("block.mishanguc.tooltip.road_with_straight_line.1")
            .formatted(Formatting.GRAY));
    tooltip.add(
        TextBridge.translatable("block.mishanguc.tooltip.road_with_straight_line.2")
            .formatted(Formatting.GRAY));
  }

  class Impl extends AbstractRoadBlock implements RoadWithStraightLine {
    public static final MapCodec<Impl> CODEC = RecordCodecBuilder.mapCodec(i -> i.group(createSettingsCodec(), lineColorFieldCodec(), lineTypeFieldCodec()).apply(i, (s, c, t) -> new Impl(s, c, t, null)));
    private final String lineTexture;

    public Impl(Settings settings, LineColor lineColor, LineType lineType, String lineTexture) {
      super(settings, lineColor, lineType);
      this.lineTexture = lineTexture;
    }

    @Override
    public void appendDescriptionTooltip(List<Text> tooltip, TooltipContext options) {
      tooltip.add(TextBridge.translatable("lineType.straight.composed", lineColor.getName(), lineType.getName()).formatted(Formatting.BLUE));
    }

    @Environment(EnvType.CLIENT)
    @Override
    public @NotNull BlockStateSupplier getBlockStates() {
      final Identifier blockModelId = getBlockModelId();
      return VariantsBlockStateSupplier.create(this, BlockStateVariant.create().put(VariantSettings.MODEL, blockModelId)).coordinate(BlockStateVariantMap.create(AXIS)
          .register(Direction.Axis.X, ImmutableList.of(BlockStateVariant.create().put(VariantSettings.Y, VariantSettings.Rotation.R90), BlockStateVariant.create().put(VariantSettings.Y, VariantSettings.Rotation.R270)))
          .register(Direction.Axis.Z, ImmutableList.of(BlockStateVariant.create().put(VariantSettings.Y, VariantSettings.Rotation.R0), BlockStateVariant.create().put(VariantSettings.Y, VariantSettings.Rotation.R180))));
    }

    @Environment(EnvType.CLIENT)
    @Override
    public @NotNull ModelJsonBuilder getBlockModel() {
      if (lineTexture == null) return null;
      return ModelJsonBuilder.create(new Identifier("mishanguc:block/road_with_straight_line")).setTextures(new FasterJTextures().base("asphalt").lineSide(lineTexture).lineTop(lineTexture));
    }

    @Environment(EnvType.CLIENT)
    @Override
    public void writeBlockModel(RuntimeResourcePack pack) {
      BRRPHelper.addModelWithSlab(pack, Impl.this);
    }

    @Override
    public CraftingRecipeJsonBuilder getPaintingRecipe(Block base, Block self) {
      final String[] patterns = switch (lineType) {
        case NORMAL -> new String[]{
            " * ",
            "XXX",
            " * "
        };
        case DOUBLE -> new String[]{
            "* *",
            "XXX",
            "* *"
        };
        case THICK -> new String[]{
            "***",
            "XXX",
            "***"
        };
      };
      return ShapedRecipeJsonBuilder.create(getRecipeCategory(), self, 3)
          .patterns(patterns)
          .input('*', lineColor.getIngredient())
          .input('X', base)
          .criterionFromItemTag("has_paint", lineColor.getIngredient())
          .criterionFromItem(base)
          .setCustomRecipeCategory("roads");
    }

    @Override
    protected MapCodec<? extends Impl> getCodec() {
      return CODEC;
    }
  }
}
