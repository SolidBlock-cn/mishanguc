package pers.solid.mishang.uc.block;

import com.mojang.datafixers.util.Either;
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
import net.minecraft.state.property.Property;
import net.minecraft.text.Text;
import net.minecraft.util.BlockMirror;
import net.minecraft.util.BlockRotation;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Direction;
import net.minecraft.world.BlockView;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import pers.solid.mishang.uc.MishangUtils;
import pers.solid.mishang.uc.MishangucProperties;
import pers.solid.mishang.uc.util.*;

import java.util.List;

/**
 * 类似于 {@link RoadWithJointLine}，不过较短的那一条线是被偏移的。
 */
public interface RoadWithJointLineWithOffsetSide extends Road {
  /**
   * 道路方块中，偏移半线与正中直线围成的面积范围较小的那个直角。<br>
   * 不同于{@link RoadWithJointLine#FACING}，那个是正对的水平方向，而这个是斜角水平方向。
   */
  EnumProperty<HorizontalCornerDirection> FACING = MishangucProperties.HORIZONTAL_CORNER_FACING;
  /**
   * 道路方块中，正中直线所在的轴。
   */
  Property<Direction.Axis> AXIS = Properties.HORIZONTAL_AXIS;

  @Override
  LineColor getLineColor(BlockState blockState, Direction direction);

  @Override
  default void appendRoadProperties(StateManager.Builder<Block, BlockState> builder) {
    Road.super.appendRoadProperties(builder);
    builder.add(FACING, AXIS);
  }

  @Override
  default RoadConnectionState getConnectionStateOf(BlockState state, Direction direction) {
    return RoadConnectionState.of(
        state.get(FACING).hasDirection(direction) || state.get(AXIS).test(direction),
        getLineColor(state, direction),
        Either.left(direction.getOpposite()),
        LineType.NORMAL, state);
  }

  @Override
  default BlockState mirrorRoad(BlockState state, BlockMirror mirror) {
    return state.with(FACING, state.get(FACING).mirror(mirror));
  }

  @Override
  default BlockState rotateRoad(BlockState state, BlockRotation rotation) {
    final Direction.Axis axis = state.get(AXIS);
    return state
        .with(FACING, state.get(FACING).rotate(rotation))
        .with(AXIS, MishangUtils.rotateAxis(rotation, axis));
  }

  @Override
  default BlockState withPlacementState(BlockState state, ItemPlacementContext ctx) {
    final HorizontalCornerDirection facing =
        HorizontalCornerDirection.fromRotation(ctx.getPlayerYaw());
    return state
        .with(
            FACING,
            ctx.getPlayer() != null && ctx.getPlayer().isSneaking() ? facing.getOpposite() : facing)
        .with(AXIS, ctx.getPlayerFacing().getAxis());
  }

  @Override
  default void appendRoadTooltip(
      ItemStack stack, @Nullable BlockView world, List<Text> tooltip, TooltipContext options) {
    Road.super.appendRoadTooltip(stack, world, tooltip, options);
    tooltip.add(TextBridge.translatable("block.mishanguc.tooltip.road_with_joint_line_with_offset_side.1").formatted(Formatting.GRAY));
    tooltip.add(TextBridge.translatable("block.mishanguc.tooltip.road_with_joint_line_with_offset_side.2").formatted(Formatting.GRAY));
    tooltip.add(TextBridge.translatable("block.mishanguc.tooltip.road_with_joint_line_with_offset_side.3").formatted(Formatting.GRAY));
  }

  class Impl extends AbstractRoadBlock implements RoadWithJointLineWithOffsetSide {
    private final LineColor lineColorSide;
    private final LineType lineTypeSide;

    public Impl(Settings settings, LineColor lineColor, LineType lineType) {
      this(settings, lineColor, lineColor, lineType, lineType);
    }

    public Impl(Settings settings, LineColor lineColor, LineColor lineColorSide, LineType lineType, LineType lineTypeSide) {
      super(settings, lineColor, lineType);
      this.lineColorSide = lineColorSide;
      this.lineTypeSide = lineTypeSide;
    }

    @Environment(EnvType.CLIENT)
    @Override
    public @Nullable JBlockStates getBlockStates() {
      final Identifier id = getBlockModelId();
      JVariants variant = new JVariants();
      // 一侧的短线所朝向的方向。
      for (Direction direction : Direction.Type.HORIZONTAL) {
        final @NotNull Direction offsetDirection1 = direction.rotateYClockwise();
        // direction 的右偏方向
        final @NotNull HorizontalCornerDirection facing1 = HorizontalCornerDirection.fromDirections(direction, offsetDirection1);
        final @NotNull Direction offsetDirection2 = direction.rotateYCounterclockwise();
        // direction 的左偏方向
        final @NotNull HorizontalCornerDirection facing2 = HorizontalCornerDirection.fromDirections(direction, offsetDirection2);
        variant
            .addVariant(
                String.format(
                    "facing=%s,axis=%s", facing1.asString(), offsetDirection1.getAxis().asString()),
                new JBlockModel(id).y((int) direction.asRotation()))
            .addVariant(
                String.format(
                    "facing=%s,axis=%s", facing2.asString(), offsetDirection2.getAxis().asString()),
                new JBlockModel(id.brrp_append("_mirrored"))
                    .y((int) direction.asRotation()));
      }
      return JBlockStates.ofVariants(variant);
    }

    @Override
    public void appendDescriptionTooltip(List<Text> tooltip, TooltipContext options) {
      tooltip.add(TextBridge.translatable("lineType.jointWithOffsetSide.composed.1", lineColor.getName(), lineType.getName()).formatted(Formatting.BLUE));
      tooltip.add(TextBridge.translatable("lineType.jointWithOffsetSide.composed.2", lineColorSide.getName(), lineTypeSide.getName()).formatted(Formatting.BLUE));
    }

    @Override
    public LineColor getLineColor(BlockState state, Direction direction) {
      if (state.get(FACING).hasDirection(direction) && !state.get(AXIS).test(direction)) {
        return lineColorSide;
      }
      return super.getLineColor(state, direction);
    }

    @Override
    public LineType getLineType(BlockState state, Direction direction) {
      if (state.get(FACING).hasDirection(direction) && !state.get(AXIS).test(direction)) {
        return lineTypeSide;
      }
      return super.getLineType(state, direction);
    }
  }
}
