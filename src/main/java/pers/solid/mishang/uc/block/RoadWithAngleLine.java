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
import net.minecraft.text.Text;
import net.minecraft.util.BlockMirror;
import net.minecraft.util.BlockRotation;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Direction;
import net.minecraft.world.BlockView;
import org.jetbrains.annotations.Nullable;
import pers.solid.mishang.uc.MishangucProperties;
import pers.solid.mishang.uc.util.*;

import java.util.List;

public interface RoadWithAngleLine extends Road {
  EnumProperty<HorizontalCornerDirection> FACING = MishangucProperties.HORIZONTAL_CORNER_FACING;

  @Override
  default void appendRoadProperties(StateManager.Builder<Block, BlockState> builder) {
    builder.add(FACING);
  }

  @Override
  default RoadConnectionState getConnectionStateOf(BlockState state, Direction direction) {
    return RoadConnectionState.of(
        state.get(FACING).hasDirection(direction),
        getLineColor(state, direction),
        isBevel() ? Either.right(state.get(FACING).mirror(direction)) : Either.left(direction),
        getLineType(state, direction));
  }

  @Override
  default BlockState mirrorRoad(BlockState state, BlockMirror mirror) {
    return state.with(FACING, state.get(FACING).mirror(mirror));
  }

  @Override
  default BlockState rotateRoad(BlockState state, BlockRotation rotation) {
    HorizontalCornerDirection facing = state.get(FACING);
    return state.with(FACING, facing.rotate(rotation));
  }

  @Override
  default BlockState withPlacementState(BlockState state, ItemPlacementContext ctx) {
    if (state == null) {
      return null;
    }
    final HorizontalCornerDirection rotation =
        HorizontalCornerDirection.fromRotation(ctx.getPlayerYaw());
    return state.with(
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
        TextBridge.translatable("block.mishanguc.tooltip.road_with_angle_line.1")
            .formatted(Formatting.GRAY));
    tooltip.add(
        TextBridge.translatable("block.mishanguc.tooltip.road_with_angle_line.2")
            .formatted(Formatting.GRAY));
  }

  boolean isBevel();

  class Impl extends AbstractRoadBlock implements RoadWithAngleLine {
    private final boolean isBevel;

    public Impl(Settings settings, LineColor lineColor, LineType lineType, boolean isBevel) {
      super(settings, lineColor, lineType);
      this.isBevel = isBevel;
    }

    @Override
    public boolean isBevel() {
      return isBevel;
    }

    @Environment(EnvType.CLIENT)
    @Override
    public @Nullable JBlockStates getBlockStates() {
      final Identifier id = getBlockModelId();
      JVariants variant = new JVariants();
      for (HorizontalCornerDirection direction : HorizontalCornerDirection.values()) {
        variant.addVariant("facing=" + direction.asString(), new JBlockModel(id).y(direction.asRotation() - 45));
      }
      return JBlockStates.ofVariants(variant);
    }

    @Override
    public void appendDescriptionTooltip(List<Text> tooltip, TooltipContext options) {
      if (isBevel()) {
        tooltip.add(TextBridge.translatable("lineType.angle.bevel").formatted(Formatting.BLUE));
      } else {
        tooltip.add(TextBridge.translatable("lineType.angle.right").formatted(Formatting.BLUE));
      }
      tooltip.add(TextBridge.translatable("lineType.angle.composed", lineColor.getName(), lineType.getName()).formatted(Formatting.BLUE));
    }
  }
}
