package pers.solid.mishang.uc.block;

import com.mojang.datafixers.util.Either;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemStack;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.EnumProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.BlockMirror;
import net.minecraft.util.BlockRotation;
import net.minecraft.util.Formatting;
import net.minecraft.util.math.Direction;
import net.minecraft.world.BlockView;
import org.jetbrains.annotations.Nullable;
import pers.solid.mishang.uc.util.LineColor;
import pers.solid.mishang.uc.util.LineType;
import pers.solid.mishang.uc.util.RoadConnectionState;

import java.util.List;

public interface RoadWithStraightLine extends Road {
  EnumProperty<Direction.Axis> AXIS = Properties.HORIZONTAL_AXIS;

  @Override
  default void appendRoadProperties(StateManager.Builder<Block, BlockState> builder) {
    builder.add(AXIS);
  }

  @Override
  default RoadConnectionState getConnectionStateOf(BlockState state, Direction direction) {
    Direction.Axis axis = state.get(AXIS);
    return RoadConnectionState.of(
        direction.getAxis() == axis,
        getLineColor(state, direction),
        Either.left(direction),
        LineType.NORMAL);
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
  default BlockState mirrorRoad(BlockState state, BlockMirror mirror) {
    return state;
  }

  @Override
  default BlockState withPlacementState(BlockState state, ItemPlacementContext ctx) {
    final PlayerEntity player = ctx.getPlayer();
    final Direction playerFacing = ctx.getPlayerFacing();
    return state.with(
        AXIS,
        (player != null && player.isSneaking() ? playerFacing.rotateYClockwise() : playerFacing)
            .getAxis());
  }

  @Override
  default void appendRoadTooltip(
      ItemStack stack, @Nullable BlockView world, List<Text> tooltip, TooltipContext options) {
    Road.super.appendRoadTooltip(stack, world, tooltip, options);
    tooltip.add(
        new TranslatableText("block.mishanguc.tooltip.road_with_straight_line.1")
            .formatted(Formatting.GRAY));
    tooltip.add(
        new TranslatableText("block.mishanguc.tooltip.road_with_straight_line.2")
            .formatted(Formatting.GRAY));
  }

  class Impl extends AbstractRoadBlock implements RoadWithStraightLine {
    public Impl(Settings settings, LineColor lineColor, LineType lineType) {
      super(settings, lineColor, lineType);
    }

    @Override
    public void appendRoadProperties(StateManager.Builder<Block, BlockState> builder) {
      super.appendRoadProperties(builder);
      RoadWithStraightLine.super.appendRoadProperties(builder);
    }
  }
}
