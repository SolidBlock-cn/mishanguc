package pers.solid.mishang.uc.data;

import com.google.common.collect.ImmutableMap;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.data.models.BlockModelGenerators;
import net.minecraft.client.data.models.blockstates.BlockModelDefinitionGenerator;
import net.minecraft.client.data.models.blockstates.MultiVariantGenerator;
import net.minecraft.client.data.models.blockstates.PropertyDispatch;
import net.minecraft.client.data.models.model.TextureMapping;
import net.minecraft.client.data.models.model.TextureSlot;
import net.minecraft.client.renderer.block.dispatch.*;
import net.minecraft.client.resources.model.sprite.Material;
import net.minecraft.resources.Identifier;
import net.minecraft.util.random.WeightedList;
import net.minecraft.world.level.block.Block;
import pers.solid.mishang.uc.MishangucProperties;
import pers.solid.mishang.uc.block.MishangucBlock;

import java.util.Map;
import java.util.Optional;
import java.util.function.UnaryOperator;

@Environment(EnvType.CLIENT)
public final class ModelHelper {

  /**
   * 返回水平角落方向方块的方块状态。
   *
   * @param modelIdentifier 模型 id。
   * @param uvlock          是否锁定纹理。
   * @return 方块状态。
   */
  public static BlockModelDefinitionGenerator stateForHorizontalCornerFacingBlock(Block block, Identifier modelIdentifier, boolean uvlock) {
    return MultiVariantGenerator.dispatch(block, BlockModelGenerators.plainVariant(modelIdentifier)).with(PropertyDispatch.modify(MishangucProperties.HORIZONTAL_CORNER_FACING).generate(direction -> VariantMutator.Y_ROT.withValue(direction.asAxisRotationCCW45()).then(VariantMutator.UV_LOCK.withValue(uvlock))));
  }

  public static BlockStateModelDispatcher composeStateForSlab(BlockStateModelDispatcher modelForFull) {
    final Optional<BlockStateModelDispatcher.SimpleModelSelectors> simpleModels = modelForFull.simpleModels();
    final Optional<BlockStateModelDispatcher.SimpleModelSelectors> newSimpleModels;
    if (simpleModels.isEmpty()) {
      newSimpleModels = Optional.empty();
    } else {
      final BlockStateModelDispatcher.SimpleModelSelectors variants = simpleModels.get();
      final Map<String, BlockStateModel.Unbaked> models = variants.models();
      final ImmutableMap.Builder<String, BlockStateModel.Unbaked> newModelsBuilder = new ImmutableMap.Builder<>();

      for (Map.Entry<String, BlockStateModel.Unbaked> entry : models.entrySet()) {
        final String key = entry.getKey();
        final BlockStateModel.Unbaked unbaked = entry.getValue();
        newModelsBuilder.put(key.isEmpty() ? "type=bottom" : key + ",type=bottom", unbaked);
        newModelsBuilder.put(key.isEmpty() ? "type=top" : key + ",type=top", transformUnbakedModel(unbaked, modelVariant -> modelVariant.withModel(modelVariant.modelLocation().withSuffix("_top"))));
        newModelsBuilder.put(key.isEmpty() ? "type=double" : key + ",type=double", transformUnbakedModel(unbaked, modelVariant -> modelVariant.withModel(modelVariant.modelLocation().withPath(s -> s.endsWith("_slab") ? s.replace("_slab", "_block") : s.replace("_slab", "")))));
      }

      newSimpleModels = Optional.of(new BlockStateModelDispatcher.SimpleModelSelectors(newModelsBuilder.build()));
    }

    // mubltipart 的部分，目前不用动
    return new BlockStateModelDispatcher(newSimpleModels, modelForFull.multiPart());
  }

  public static BlockStateModel.Unbaked transformUnbakedModel(BlockStateModel.Unbaked unbaked, UnaryOperator<Variant> operator) {
    if (unbaked instanceof SingleVariant.Unbaked(Variant variant)) {
      return new SingleVariant.Unbaked(operator.apply(variant));
    } else if (unbaked instanceof WeightedVariants.Unbaked(WeightedList<BlockStateModel.Unbaked> entries)) {
      return new WeightedVariants.Unbaked(entries.map(unbaked1 -> transformUnbakedModel(unbaked1, operator)));
    } else {
      return unbaked;
    }
  }

  public static BlockModelDefinitionGenerator composeStateForSlab(BlockModelDefinitionGenerator stateForFull) {
    return new Forwarding(stateForFull);
  }

  public static Material getMaterialOf(Block block) {
    if (block instanceof MishangucBlock mishangucBlock) {
      return mishangucBlock.getMaterial(TextureSlot.TEXTURE);
    } else {
      return TextureMapping.getBlockTexture(block);
    }
  }

  @Environment(EnvType.CLIENT)
  private static class Forwarding implements BlockModelDefinitionGenerator {
    private final BlockModelDefinitionGenerator stateForFull;

    public Forwarding(BlockModelDefinitionGenerator stateForFull) {
      this.stateForFull = stateForFull;
    }

    @Override
    public Block block() {
      return stateForFull.block();
    }

    @Override
    public BlockStateModelDispatcher create() {
      return composeStateForSlab(stateForFull.create());
    }
  }
}
