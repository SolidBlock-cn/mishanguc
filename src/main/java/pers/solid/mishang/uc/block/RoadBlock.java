package pers.solid.mishang.uc.block;

import net.devtech.arrp.json.blockstate.JBlockStates;
import net.devtech.arrp.json.models.JModel;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.BlockState;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Direction;
import org.jetbrains.annotations.NotNull;
import pers.solid.mishang.uc.util.LineColor;
import pers.solid.mishang.uc.util.LineType;
import pers.solid.mishang.uc.util.RoadConnectionState;

public class RoadBlock extends AbstractRoadBlock {
  public RoadBlock(Settings settings) {
    super(settings, LineColor.NONE, LineType.NORMAL);
  }

  @Override
  public RoadConnectionState getConnectionStateOf(BlockState state, Direction direction) {
    return RoadConnectionState.empty();
  }

  @Environment(EnvType.CLIENT)
  @Override
  public @NotNull JBlockStates getBlockStates() {
    final Identifier blockModelId = getBlockModelId();
    return JBlockStates.simpleRandomRotation(blockModelId);
  }

  @Environment(EnvType.CLIENT)
  @Override
  public @NotNull JModel getBlockModel() {
    return new JModel("block/cube_all").addTexture("all", "mishanguc:block/asphalt");
  }
}
