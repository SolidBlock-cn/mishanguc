package pers.solid.mishang.uc.block;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.data.client.*;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.EnumProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.text.Text;
import net.minecraft.util.BlockRotation;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Direction;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import pers.solid.brrp.v1.api.RuntimeResourcePack;
import pers.solid.brrp.v1.model.ModelJsonBuilder;
import pers.solid.mishang.uc.MishangUtils;
import pers.solid.mishang.uc.arrp.BRRPHelper;
import pers.solid.mishang.uc.arrp.FasterJTextures;
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
  EnumProperty<Direction.Axis> AXIS = Properties.HORIZONTAL_AXIS;

  @Override
  default void appendRoadProperties(StateManager.Builder<Block, BlockState> builder) {
    RoadWithAngleLine.super.appendRoadProperties(builder);
    builder.add(AXIS);
  }

  @Override
  default BlockState rotateRoad(BlockState state, BlockRotation rotation) {
    return RoadWithAngleLine.super
        .rotateRoad(state, rotation)
        .with(AXIS, MishangUtils.rotateAxis(rotation, state.get(AXIS)));
  }

  @Override
  default BlockState withPlacementState(BlockState state, ItemPlacementContext ctx) {
    return RoadWithAngleLine.super
        .withPlacementState(state, ctx)
        .with(AXIS, ctx.getHorizontalPlayerFacing().getAxis());
  }

  class Impl extends RoadWithAngleLine.Impl implements RoadWithDiffAngleLine {
    public final LineColor lineColor2;
    public final LineType lineType2;
    private final String lineSide2;

    public Impl(
        Settings settings,
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
      return state.get(AXIS) == direction.getAxis() ? lineColor2 : lineColor;
    }

    @Override
    public void appendDescriptionTooltip(List<Text> tooltip, TooltipContext options) {
      tooltip.add(TextBridge.translatable("lineType.diffAngleLine.composed.1", lineColor.getName(), lineType.getName()).formatted(Formatting.BLUE));
      tooltip.add(TextBridge.translatable("lineType.diffAngleLine.composed.2", lineColor2.getName(), lineType2.getName()).formatted(Formatting.BLUE));
    }

    @Override
    public LineType getLineType(BlockState state, Direction direction) {
      return state.get(AXIS) == direction.getAxis() ? lineType2 : lineType;
    }

    @Environment(EnvType.CLIENT)
    @Override
    public @Nullable BlockStateSupplier getBlockStates() {
      final Identifier id = getBlockModelId();
      final BlockStateVariantMap.DoubleProperty<HorizontalCornerDirection, Direction.Axis> map = BlockStateVariantMap.create(FACING, AXIS);
      // 一侧的短线所朝向的方向。
      for (Direction direction : Direction.Type.HORIZONTAL) {
        final @NotNull Direction offsetDirection1 = direction.rotateYClockwise();
        // direction 的右偏方向
        final @NotNull HorizontalCornerDirection facing1 = HorizontalCornerDirection.fromDirections(direction, offsetDirection1);
        final @NotNull Direction offsetDirection2 = direction.rotateYCounterclockwise();
        // direction 的左偏方向
        final @NotNull HorizontalCornerDirection facing2 = HorizontalCornerDirection.fromDirections(direction, offsetDirection2);
        map
            .register(
                facing1, direction.getAxis(),
                BlockStateVariant.create().put(VariantSettings.MODEL, id).put(MishangUtils.DIRECTION_Y_VARIANT, direction))
            .register(
                facing2, direction.getAxis(),
                BlockStateVariant.create().put(VariantSettings.MODEL, id.brrp_suffixed("_mirrored"))
                    .put(MishangUtils.DIRECTION_Y_VARIANT, direction));
      }
      return VariantsBlockStateSupplier.create(this).coordinate(map);
    }

    @Environment(EnvType.CLIENT)
    @Override
    public @NotNull ModelJsonBuilder getBlockModel() {
      return ModelJsonBuilder.create(new Identifier("mishanguc:block/road_with_angle_line")).setTextures(new FasterJTextures().base("asphalt").lineSide(lineSide).lineSide2(lineSide2).lineTop(lineTop));
    }

    @Environment(EnvType.CLIENT)
    @Override
    public void writeBlockModel(RuntimeResourcePack pack) {
      BRRPHelper.addModelWithSlabWithMirrored(pack, RoadWithDiffAngleLine.Impl.this);
    }
  }
}
