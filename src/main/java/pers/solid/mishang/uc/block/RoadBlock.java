package pers.solid.mishang.uc.block;

import net.devtech.arrp.json.blockstate.JBlockStates;
import net.devtech.arrp.json.models.JModel;
import net.devtech.arrp.json.models.JTextures;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.BlockState;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Direction;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import pers.solid.brrp.v1.api.RuntimeResourcePack;
import pers.solid.mishang.uc.util.LineColor;
import pers.solid.mishang.uc.util.LineType;
import pers.solid.mishang.uc.util.RoadConnectionState;

import java.util.List;

public class RoadBlock extends AbstractRoadBlock {
  private final String texture;

  public RoadBlock(Settings settings, String texture) {
    super(settings, LineColor.NONE, LineType.NORMAL);
    this.texture = texture;
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
  public @Nullable JModel getBlockModel() {
    return new JModel("block/cube_all").addTexture("all", texture);
  }

  @Environment(EnvType.CLIENT)
  @Override
  public void writeBlockModel(RuntimeResourcePack pack) {
    final JModel blockModel = getBlockModel();
    final Identifier blockModelId = getBlockModelId();
    final AbstractRoadSlabBlock slabBlock = getRoadSlab();
    final Identifier slabBlockModelId = slabBlock.getBlockModelId();
    pack.addModel(blockModel, blockModelId);
    final JModel slabModel = getSlabBlockModel();
    pack.addModel(slabModel, slabBlockModelId);
    pack.addModel(slabModel.parent("block/slab_top"), slabBlockModelId.brrp_append("_top"));
  }

  @Environment(EnvType.CLIENT)
  private JModel getSlabBlockModel() {
    return new JModel("block/slab").textures(JTextures.ofSides(texture, texture, texture));
  }

  @Override
  public void appendDescriptionTooltip(List<Text> tooltip, TooltipContext options) {

  }
}
