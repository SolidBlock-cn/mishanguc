package pers.solid.mishang.uc.block;

import com.mojang.datafixers.util.Either;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemStack;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.DirectionProperty;
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

public interface RoadWithJointLine extends Road {
  DirectionProperty FACING = Properties.HORIZONTAL_FACING;

  @Override
  default void appendRoadProperties(StateManager.Builder<Block, BlockState> builder) {
    Road.super.appendRoadProperties(builder);
    builder.add(FACING);
  }

  @Override
  default RoadConnectionState getConnectionStateOf(BlockState state, Direction direction) {
    return RoadConnectionState.or(
        Road.super.getConnectionStateOf(state, direction),
        RoadConnectionState.of(
            state.get(FACING) != direction.getOpposite(),
            getLineColor(state, direction),
            Either.left(direction),
            LineType.NORMAL));
  }

  @Override
  default BlockState mirrorRoad(BlockState state, BlockMirror mirror) {
    return Road.super.mirrorRoad(state, mirror).with(FACING, mirror.apply(state.get(FACING)));
  }

  @Override
  default BlockState rotateRoad(BlockState state, BlockRotation rotation) {
    return Road.super.rotateRoad(state, rotation).with(FACING, rotation.rotate(state.get(FACING)));
  }

  @Override
  default BlockState withPlacementState(BlockState state, ItemPlacementContext ctx) {
    final Direction rotation = Direction.fromRotation(ctx.getPlayerYaw());
    return Road.super
        .withPlacementState(state, ctx)
        .with(
            FACING,
            ctx.getPlayer() != null && ctx.getPlayer().isSneaking()
                ? rotation.getOpposite()
                : rotation);
  }

  @Override
  default void appendRoadTooltip(
      ItemStack stack, @Nullable BlockView world, List<Text> tooltip, TooltipContext options) {
    Road.super.appendRoadTooltip(stack, world, tooltip, options);
    tooltip.add(
        new TranslatableText("block.mishanguc.tooltip.road_with_joint_line.1")
            .formatted(Formatting.GRAY));
    tooltip.add(
        new TranslatableText("block.mishanguc.tooltip.road_with_joint_line.2")
            .formatted(Formatting.GRAY));
  }

  class Impl extends AbstractRoadBlock implements RoadWithJointLine {
    public final LineColor lineColorSide;
    public final LineType lineTypeSide;

    public Impl(
        Settings settings,
        LineColor lineColor,
        LineColor lineColorSide,
        LineType lineType,
        LineType lineTypeSide) {
      super(settings, lineColor, lineType);
      this.lineColorSide = lineColorSide;
      this.lineTypeSide = lineTypeSide;
    }

    @Override
    public LineColor getLineColor(BlockState blockState, Direction direction) {
      if (blockState.get(FACING) == direction) {
        return lineColorSide;
      } else {
        return lineColor;
      }
    }

    @Override
    public LineType getLineType(BlockState blockState, Direction direction) {
      if (blockState.get(FACING) == direction) {
        return lineTypeSide;
      } else {
        return lineType;
      }
    }
  }
}
