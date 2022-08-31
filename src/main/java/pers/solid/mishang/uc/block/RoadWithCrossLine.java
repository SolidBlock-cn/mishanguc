package pers.solid.mishang.uc.block;

import com.mojang.datafixers.util.Either;
import net.devtech.arrp.json.blockstate.JBlockStates;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.BlockState;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.math.Direction;
import org.jetbrains.annotations.NotNull;
import pers.solid.mishang.uc.util.LineColor;
import pers.solid.mishang.uc.util.LineType;
import pers.solid.mishang.uc.util.RoadConnectionState;
import pers.solid.mishang.uc.util.TextBridge;

import java.util.List;

public interface RoadWithCrossLine extends Road {
  @Override
  default RoadConnectionState getConnectionStateOf(BlockState state, Direction direction) {
    return Road.super
        .getConnectionStateOf(state, direction)
        .or(
            RoadConnectionState.connectedTo(
                getLineColor(state, direction), Either.left(direction), LineType.NORMAL));
  }

  class Impl extends AbstractRoadBlock implements RoadWithCrossLine {
    public Impl(Settings settings, LineColor lineColor) {
      super(settings, lineColor, LineType.NORMAL);
    }

    @Environment(EnvType.CLIENT)
    @Override
    public @NotNull JBlockStates getBlockStates() {
      return JBlockStates.simpleRandomRotation(getBlockModelId());
    }

    @Override
    public void appendDescriptionTooltip(List<Text> tooltip, TooltipContext options) {
      tooltip.add(TextBridge.translatable("lineType.cross.composed", lineColor.getName(), lineType.getName()).formatted(Formatting.BLUE));
    }
  }
}
