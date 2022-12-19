package pers.solid.mishang.uc.block;

import net.devtech.arrp.api.RuntimeResourcePack;
import net.devtech.arrp.generator.BRRPSlabBlock;
import net.devtech.arrp.json.models.JModel;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.Block;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;

@ApiStatus.AvailableSince("1.1.0")
public class LightSlabBlock extends BRRPSlabBlock {
  public LightSlabBlock(@NotNull Block baseBlock) {
    super(baseBlock);
  }

  @Environment(EnvType.CLIENT)
  @Override
  public @NotNull JModel getBlockModel() {
    return super.getBlockModel().parent(new Identifier("mishanguc", "block/light_slab"));
  }

  @Environment(EnvType.CLIENT)
  @Override
  public void writeBlockModel(RuntimeResourcePack pack) {
    final JModel model = getBlockModel();
    final Identifier id = getBlockModelId();
    pack.addModel(model, id);
    pack.addModel(model.clone().parent(new Identifier("mishanguc", "block/light_slab_top")), id.brrp_append("_top"));
  }
}
