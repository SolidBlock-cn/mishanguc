package pers.solid.mishang.uc.block;

import net.devtech.arrp.api.RuntimeResourcePack;
import net.devtech.arrp.json.blockstate.JBlockModel;
import net.devtech.arrp.json.blockstate.JBlockStates;
import net.devtech.arrp.json.blockstate.JVariants;
import net.devtech.arrp.json.models.JModel;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemStack;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.DirectionProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.text.Text;
import net.minecraft.util.BlockMirror;
import net.minecraft.util.BlockRotation;
import net.minecraft.util.Formatting;
import net.minecraft.util.math.Direction;
import net.minecraft.world.BlockView;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import pers.solid.mishang.uc.arrp.BRRPHelper;
import pers.solid.mishang.uc.arrp.FasterJTextures;
import pers.solid.mishang.uc.util.*;

import java.util.List;

/**
 * 类似于 {@link RoadWithStraightLine}，不过道路的直线是偏移的，而非正中的。
 */
public interface RoadWithOffsetStraightLine extends Road {
  /**
   * 道路偏移直线所偏移的反方向。例如道路有一条南北方向的向西偏移的直线，则该道路朝向东。
   */
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
            direction.getAxis() != state.get(FACING).getAxis(),
            getLineColor(state, direction),
            EightHorizontalDirection.of(direction),
            getLineType(state, direction),
            new LineOffset(state.get(FACING).getOpposite(), offsetLevel())));
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
    return Road.super
        .withPlacementState(state, ctx)
        .with(
            FACING,
            ctx.getPlayer() != null && ctx.getPlayer().isSneaking()
                ? ctx.getHorizontalPlayerFacing().rotateYCounterclockwise()
                : ctx.getHorizontalPlayerFacing().rotateYClockwise());
  }

  @Override
  default void appendRoadTooltip(
      ItemStack stack, @Nullable BlockView world, List<Text> tooltip, TooltipContext options) {
    Road.super.appendRoadTooltip(stack, world, tooltip, options);
    final int offsetLevel = offsetLevel();
    if (offsetLevel == 114514) {
      tooltip.add(TextBridge.translatable("block.mishanguc.tooltip.road_with_white_and_yellow_double_line.1").formatted(Formatting.GRAY));
      tooltip.add(TextBridge.translatable("block.mishanguc.tooltip.road_with_white_and_yellow_double_line.2").formatted(Formatting.GRAY));
      tooltip.add(TextBridge.translatable("block.mishanguc.tooltip.road_with_white_and_yellow_double_line.3").formatted(Formatting.GRAY));
    } else {
      tooltip.add(
          TextBridge.translatable("block.mishanguc.tooltip.road_with_offset_straight_line")
              .formatted(Formatting.GRAY));
    }
  }

  @Environment(EnvType.CLIENT)
  @Override
  default @NotNull JBlockStates getBlockStates() {
    final JVariants JVariants = new JVariants();
    for (Direction direction : Direction.Type.HORIZONTAL) {
      JVariants.addVariant(Properties.HORIZONTAL_FACING, direction, new JBlockModel(getBlockModelId()).uvlock(false).clone().y(((int) direction.rotateYClockwise().asRotation())));
    }
    return JBlockStates.ofVariants(JVariants);
  }

  @Contract(pure = true)
  int offsetLevel();

  class Impl extends AbstractRoadBlock implements RoadWithOffsetStraightLine {
    private final String lineTexture;
    private final int offsetLevel;

    public Impl(Settings settings, LineColor lineColor, LineType lineType, String lineTexture, int offsetLevel) {
      super(settings, lineColor, lineType);
      this.lineTexture = lineTexture;
      this.offsetLevel = offsetLevel;
    }

    @Override
    public void appendDescriptionTooltip(List<Text> tooltip, TooltipContext options) {
      if (offsetLevel == 0) {
        tooltip.add(TextBridge.translatable("tbd")
            .formatted(Formatting.BLUE));
      } else {
        tooltip.add(TextBridge.translatable("lineType.offsetStraight.composed", lineColor.getName(), lineType.getName()).formatted(Formatting.BLUE));
      }
    }

    @Environment(EnvType.CLIENT)
    @Override
    public @NotNull JModel getBlockModel() {
      return new JModel("mishanguc:block/road_with_straight_line").textures(new FasterJTextures().base("asphalt").lineSide(lineTexture).lineTop(lineTexture));
    }

    @Environment(EnvType.CLIENT)
    @Override
    public void writeBlockModel(RuntimeResourcePack pack) {
      BRRPHelper.addModelWithSlab(pack, Impl.this);
    }

    @Override
    public int offsetLevel() {
      return offsetLevel;
    }
  }
}
