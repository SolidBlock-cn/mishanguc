package pers.solid.mishang.uc.block;

import net.devtech.arrp.json.blockstate.JBlockModel;
import net.devtech.arrp.json.blockstate.JBlockStates;
import net.devtech.arrp.json.blockstate.JVariants;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemStack;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.EnumProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.*;
import net.minecraft.util.math.Direction;
import net.minecraft.world.BlockView;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import pers.solid.mishang.uc.util.HorizontalCornerDirection;
import pers.solid.mishang.uc.util.LineColor;
import pers.solid.mishang.uc.util.LineType;

import java.util.List;
import java.util.Objects;

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
  default BlockState withPlacementState(BlockState state, ItemPlacementContext ctx) {
    return RoadWithAngleLine.super
        .withPlacementState(state, ctx)
        .with(AXIS, ctx.getPlayerFacing().getAxis());
  }

  @Override
  default void appendRoadTooltip(
      ItemStack stack, @Nullable BlockView world, List<Text> tooltip, TooltipContext options) {
    RoadWithAngleLine.super.appendRoadTooltip(stack, world, tooltip, options);
    tooltip.add(
        new TranslatableText("block.mishanguc.tooltip.road_with_angle_line_with_one_part_offset.1")
            .formatted(Formatting.GRAY));
    tooltip.add(
        new TranslatableText("block.mishanguc.tooltip.road_with_angle_line_with_one_part_offset.2")
            .formatted(Formatting.GRAY));
  }

  class Impl extends RoadWithAngleLine.Impl implements RoadWithAngleLineWithOnePartOffset {
    public Impl(Settings settings, LineColor lineColor, boolean isBevel) {
      super(settings, lineColor, LineType.NORMAL, isBevel);
    }

    @Environment(EnvType.CLIENT)
    @Override
    public @NotNull JBlockStates getBlockStates() {
      final Identifier id = getBlockModelId();
      JVariants variant = new JVariants();
      for (Direction direction : Direction.Type.HORIZONTAL) {
        // direction：正中线所朝的方向
        final Direction offsetDirection1 = direction.rotateYClockwise();
        final Direction offsetDirection2 = direction.rotateYCounterclockwise();
        variant.addVariant(
            String.format(
                "facing=%s,axis=%s",
                Objects.requireNonNull(
                        HorizontalCornerDirection.fromDirections(direction, offsetDirection1))
                    .asString(),
                direction.getAxis().asString()),
            new JBlockModel(id).y((int) (direction.asRotation())));
        variant.addVariant(
            String.format(
                "facing=%s,axis=%s",
                Objects.requireNonNull(
                        HorizontalCornerDirection.fromDirections(direction, offsetDirection2))
                    .asString(),
                direction.getAxis().asString()),
            new JBlockModel(id.brrp_append("_mirrored"))
                .y((int) (direction.asRotation())));
      }
      return JBlockStates.ofVariants(variant);
    }
  }
}
