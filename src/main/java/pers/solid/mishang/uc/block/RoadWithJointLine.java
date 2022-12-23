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
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Direction;
import net.minecraft.world.BlockView;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import pers.solid.mishang.uc.MishangUtils;
import pers.solid.mishang.uc.arrp.BRRPHelper;
import pers.solid.mishang.uc.arrp.FasterJTextures;
import pers.solid.mishang.uc.util.*;

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
            EightHorizontalDirection.of(direction),
            getLineType(state, direction), null));
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
    final Direction rotation = ctx.getPlayerFacing();
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
        TextBridge.translatable("block.mishanguc.tooltip.road_with_joint_line.1")
            .formatted(Formatting.GRAY));
    tooltip.add(
        TextBridge.translatable("block.mishanguc.tooltip.road_with_joint_line.2")
            .formatted(Formatting.GRAY));
  }

  class Impl extends AbstractRoadBlock implements RoadWithJointLine {
    public final LineColor lineColorSide;
    public final LineType lineTypeSide;
    private final String lineTop;
    protected final String lineSide;
    protected final String lineSide2;

    public Impl(
        Settings settings,
        LineColor lineColor,
        LineColor lineColorSide,
        LineType lineType,
        LineType lineTypeSide, String lineTop) {
      super(settings, lineColor, lineType);
      this.lineColorSide = lineColorSide;
      this.lineTypeSide = lineTypeSide;
      this.lineTop = lineTop;
      lineSide = MishangUtils.composeStraightLineTexture(this.lineColor, this.lineType);
      lineSide2 = MishangUtils.composeStraightLineTexture(this.lineColorSide, this.lineTypeSide);
    }

    @Override
    public LineColor getLineColor(BlockState blockState, Direction direction) {
      final Direction facing = blockState.get(FACING);
      if (facing == direction) {
        return lineColorSide;
      } else if (facing == direction.getOpposite()) {
        return LineColor.NONE;
      } else {
        return lineColor;
      }
    }

    @Override
    public void appendDescriptionTooltip(List<Text> tooltip, TooltipContext options) {
      tooltip.add(TextBridge.translatable("lineType.joint.composed", lineColor.getName(), lineType.getName(), lineColorSide.getName(), lineTypeSide.getName()).formatted(Formatting.BLUE));
    }

    @Override
    public LineType getLineType(BlockState blockState, Direction direction) {
      final Direction facing = blockState.get(FACING);
      if (facing == direction) {
        return lineTypeSide;
      } else if (facing == direction.getOpposite()) {
        return LineType.NORMAL;
      } else {
        return lineType;
      }
    }

    @Environment(EnvType.CLIENT)
    @Override
    public @NotNull JBlockStates getBlockStates() {
      final Identifier id = getBlockModelId();
      JVariants variant = new JVariants();
      for (Direction direction : Direction.Type.HORIZONTAL) {
        variant.addVariant("facing", direction.asString(),
            new JBlockModel(id).y((int) direction.asRotation()));
      }
      return JBlockStates.ofVariants(variant);
    }

    @Environment(EnvType.CLIENT)
    @Override
    public @NotNull JModel getBlockModel() {
      return new JModel("mishanguc:block/road_with_joint_line")
          .textures(new FasterJTextures().base("asphalt").lineSide(lineSide).lineSide2(lineSide2).lineTop(lineTop));
    }

    @Environment(EnvType.CLIENT)
    @Override
    public void writeBlockModel(RuntimeResourcePack pack) {
      BRRPHelper.addModelWithSlab(pack, this);
    }
  }
}
