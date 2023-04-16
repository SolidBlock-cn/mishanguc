package pers.solid.mishang.uc.block;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.BlockState;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.data.client.*;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Direction;
import net.minecraft.world.BlockView;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import pers.solid.brrp.v1.api.RuntimeResourcePack;
import pers.solid.brrp.v1.model.ModelJsonBuilder;
import pers.solid.mishang.uc.MishangucRules;
import pers.solid.mishang.uc.util.LineColor;
import pers.solid.mishang.uc.util.LineType;
import pers.solid.mishang.uc.util.RoadConnectionState;
import pers.solid.mishang.uc.util.TextBridge;

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
  public @NotNull BlockStateSupplier getBlockStates() {
    final Identifier blockModelId = getBlockModelId();
    return BlockStateModelGenerator.createBlockStateWithRandomHorizontalRotations(this, blockModelId);
  }

  @Environment(EnvType.CLIENT)
  @Override
  public ModelJsonBuilder getBlockModel() {
    return ModelJsonBuilder.create(Models.CUBE_ALL).setTextures(TextureMap.all(new Identifier(texture)));
  }

  @Environment(EnvType.CLIENT)
  @Override
  public void writeBlockModel(RuntimeResourcePack pack) {
    final ModelJsonBuilder blockModel = getBlockModel();
    final Identifier blockModelId = getBlockModelId();
    final AbstractRoadSlabBlock slabBlock = getRoadSlab();
    final Identifier slabBlockModelId = slabBlock.getBlockModelId();
    pack.addModel(blockModelId, blockModel);
    final ModelJsonBuilder slabModel = getSlabBlockModel();
    pack.addModel(slabBlockModelId, slabModel);
    pack.addModel(slabBlockModelId.brrp_suffixed("_top"), slabModel.withParent(Models.SLAB_TOP));
  }

  @Environment(EnvType.CLIENT)
  private ModelJsonBuilder getSlabBlockModel() {
    return ModelJsonBuilder.create(Models.SLAB).setTextures(TextureMap.topBottom(new Identifier(texture), new Identifier(texture)).put(TextureKey.SIDE, new Identifier(texture)));
  }

  @Override
  public void appendDescriptionTooltip(List<Text> tooltip, TooltipContext options) {

  }

  @Override
  public void appendRoadTooltip(ItemStack stack, @Nullable BlockView world, List<Text> tooltip, TooltipContext options) {
    super.appendRoadTooltip(stack, world, tooltip, options);
    tooltip.add(TextBridge.translatable("block.mishanguc.tooltip.road.rule.1", MishangucRules.ROAD_BOOST_SPEED.getName()).formatted(Formatting.GRAY));
    tooltip.add(TextBridge.translatable("block.mishanguc.tooltip.road.rule.2", 1.75).formatted(Formatting.GRAY));
  }
}
