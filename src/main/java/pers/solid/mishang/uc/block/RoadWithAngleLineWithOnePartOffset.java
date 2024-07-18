package pers.solid.mishang.uc.block;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.data.client.*;
import net.minecraft.data.server.recipe.CraftingRecipeJsonBuilder;
import net.minecraft.data.server.recipe.ShapedRecipeJsonBuilder;
import net.minecraft.item.Item.TooltipContext;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemStack;
import net.minecraft.item.tooltip.TooltipType;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.EnumProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.text.Text;
import net.minecraft.util.*;
import net.minecraft.util.math.Direction;
import org.jetbrains.annotations.NotNull;
import pers.solid.brrp.v1.api.RuntimeResourcePack;
import pers.solid.brrp.v1.model.ModelJsonBuilder;
import pers.solid.mishang.uc.MishangUtils;
import pers.solid.mishang.uc.arrp.BRRPHelper;
import pers.solid.mishang.uc.arrp.FasterJTextures;
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
      ItemStack stack, TooltipContext context, List<Text> tooltip, TooltipType options) {
    RoadWithAngleLine.super.appendRoadTooltip(stack, context, tooltip, options);
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

    @Environment(EnvType.CLIENT)
    @Override
    public @NotNull BlockStateSupplier getBlockStates() {
      final Identifier id = getBlockModelId();
      final BlockStateVariantMap.DoubleProperty<HorizontalCornerDirection, Direction.Axis> map = BlockStateVariantMap.create(FACING, AXIS);
      for (Direction direction : Direction.Type.HORIZONTAL) {
        // direction：正中线所朝的方向
        final Direction offsetDirection1 = direction.rotateYClockwise();
        final Direction offsetDirection2 = direction.rotateYCounterclockwise();
        map.register(
            HorizontalCornerDirection.fromDirections(direction, offsetDirection1),
            direction.getAxis(),
            BlockStateVariant.create().put(VariantSettings.MODEL, id).put(MishangUtils.DIRECTION_Y_VARIANT, direction));
        map.register(
            HorizontalCornerDirection.fromDirections(direction, offsetDirection2),
            direction.getAxis(),
            BlockStateVariant.create().put(VariantSettings.MODEL, id.brrp_suffixed("_mirrored"))
                .put(MishangUtils.DIRECTION_Y_VARIANT, direction));
      }
      return VariantsBlockStateSupplier.create(this).coordinate(map);
    }

    @Environment(EnvType.CLIENT)
    @Override
    public @NotNull ModelJsonBuilder getBlockModel() {
      return ModelJsonBuilder.create(Identifier.of("mishanguc:block/road_with_angle_line"))
          .setTextures(new FasterJTextures().base("asphalt")
              .lineSide(lineSide)
              .lineSide2(lineSide2)
              .lineTop(lineTop));
    }

    @Environment(EnvType.CLIENT)
    @Override
    public void writeBlockModel(RuntimeResourcePack pack) {
      BRRPHelper.addModelWithSlabWithMirrored(pack, RoadWithAngleLineWithOnePartOffset.Impl.this);
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
      return ShapedRecipeJsonBuilder.create(getRecipeCategory(), self, 3)
          .pattern("  *")
          .pattern("*XX")
          .pattern(" X ")
          .input('*', lineColor.getIngredient())
          .input('X', base)
          .criterionFromItemTag("has_paint", lineColor.getIngredient())
          .criterionFromItem(base)
          .setCustomRecipeCategory("roads");
    }
  }
}
