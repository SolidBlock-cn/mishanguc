package pers.solid.mishang.uc.block;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import net.devtech.arrp.api.RuntimeResourcePack;
import net.devtech.arrp.json.blockstate.JBlockModel;
import net.devtech.arrp.json.blockstate.JState;
import net.devtech.arrp.json.blockstate.JVariant;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.BlockState;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Direction;
import org.jetbrains.annotations.Nullable;
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
  public @Nullable JState getBlockStates() {
    final Identifier id = getBlockModelIdentifier();
    return new JState().add(new JVariant().put("", new JBlockModel(id)));
  }

  @Environment(EnvType.CLIENT)
  @Override
  public void writeBlockModel(RuntimeResourcePack pack) {
    final JsonObject object = new JsonObject();
    final JsonObject variants = new JsonObject();
    final JsonArray variant = new JsonArray();
    final String id = getBlockModelIdentifier().toString();
    for (int i = 0; i < 360; i += 90) {
      final JsonObject blockModel = new JsonObject();
      variant.add(blockModel);
      blockModel.addProperty("model", id);
      blockModel.addProperty("y", i);
    }
    variants.add("", variant);
    object.add("variants", variants);
    pack.addAsset(getIdentifier(), new Gson().toJson(object).getBytes());
  }
}
