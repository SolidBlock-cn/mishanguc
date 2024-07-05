package pers.solid.mishang.uc.block;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.BlockState;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Direction;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import pers.solid.brrp.v1.api.RuntimeResourcePack;
import pers.solid.brrp.v1.model.ModelJsonBuilder;
import pers.solid.mishang.uc.arrp.BRRPHelper;
import pers.solid.mishang.uc.arrp.FasterJTextures;
import pers.solid.mishang.uc.util.LineColor;
import pers.solid.mishang.uc.util.LineOffset;
import pers.solid.mishang.uc.util.LineType;
import pers.solid.mishang.uc.util.RoadConnectionState;

@ApiStatus.AvailableSince("1.1.0")
public interface RoadWithAngleLineWithTwoPartsOffset extends RoadWithAngleLine {
  int offsetOutwards();

  @Override
  default RoadConnectionState getConnectionStateOf(BlockState state, Direction direction) {
    final RoadConnectionState connectionState = RoadWithAngleLine.super.getConnectionStateOf(state, direction);
    if (connectionState.mayConnect()) {
      return connectionState.createWithOffset(LineOffset.of(state.get(FACING).getDirectionInAxis(direction.rotateYClockwise().getAxis()).getOpposite(), offsetOutwards()));
    } else {
      return connectionState;
    }
  }

  class Impl extends RoadWithAngleLine.Impl implements RoadWithAngleLineWithTwoPartsOffset {
    protected final String lineSide;
    protected final String lineSide2;
    private final int offsetOutwards;

    public static final MapCodec<RoadWithAngleLineWithTwoPartsOffset.Impl> CODEC = RecordCodecBuilder.mapCodec(i -> i.group(createSettingsCodec(), lineColorFieldCodec(), lineTypeFieldCodec(), RoadWithAngleLine.isBevelCodec(), Codec.INT.fieldOf("offset_outwards").forGetter(b -> b.offsetOutwards)).apply(i, (settings, lineColor, lineType, isBevel, offsetOutwards) -> new RoadWithAngleLineWithTwoPartsOffset.Impl(settings, lineColor, lineType, isBevel, null, null, null, offsetOutwards)));

    public Impl(Settings settings, LineColor lineColor, LineType lineType, boolean isBevel, String lineTop, String lineSide, String lineSide2, int offsetOutwards) {
      super(settings, lineColor, lineType, isBevel, lineTop);
      this.lineSide = lineSide;
      this.lineSide2 = lineSide2;
      this.offsetOutwards = offsetOutwards;
    }

    @Override
    public int offsetOutwards() {
      return offsetOutwards;
    }

    @Environment(EnvType.CLIENT)
    @Override
    public @NotNull ModelJsonBuilder getBlockModel() {
      return ModelJsonBuilder.create(Identifier.of("mishanguc:block/road_with_angle_line"))
          .setTextures(new FasterJTextures().base("asphalt")
              .lineSide(lineSide)
              .lineSide2(lineSide2)
              .lineTop(lineTop));
    }

    @Environment(EnvType.CLIENT)
    @Override
    public void writeBlockModel(RuntimeResourcePack pack) {
      BRRPHelper.addModelWithSlabWithMirrored(pack, this);
    }

    @Override
    protected MapCodec<? extends RoadWithAngleLineWithTwoPartsOffset.Impl> getCodec() {
      return CODEC;
    }
  }
}
