package pers.solid.mishang.uc.data;

import com.google.common.collect.ImmutableMap;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.Block;
import net.minecraft.client.data.*;
import net.minecraft.client.render.model.BlockStateModel;
import net.minecraft.client.render.model.SimpleBlockStateModel;
import net.minecraft.client.render.model.WeightedBlockStateModel;
import net.minecraft.client.render.model.json.BlockModelDefinition;
import net.minecraft.client.render.model.json.ModelVariant;
import net.minecraft.client.render.model.json.ModelVariantOperator;
import net.minecraft.util.Identifier;
import net.minecraft.util.collection.Pool;
import org.jetbrains.annotations.NotNull;
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
  @NotNull
  public static BlockModelDefinitionCreator stateForHorizontalCornerFacingBlock(@NotNull Block block, @NotNull Identifier modelIdentifier, boolean uvlock) {
    return VariantsBlockModelDefinitionCreator.of(block, BlockStateModelGenerator.createWeightedVariant(modelIdentifier)).coordinate(BlockStateVariantMap.operations(MishangucProperties.HORIZONTAL_CORNER_FACING).generate(direction -> ModelVariantOperator.ROTATION_Y.withValue(direction.asAxisRotationCCW45()).then(ModelVariantOperator.UV_LOCK.withValue(uvlock))));
  }

  public static BlockModelDefinition composeStateForSlab(@NotNull BlockModelDefinition modelForFull) {
    final Optional<BlockModelDefinition.Variants> simpleModels = modelForFull.simpleModels();
    final Optional<BlockModelDefinition.Variants> newSimpleModels;
    if (simpleModels.isEmpty()) {
      newSimpleModels = Optional.empty();
    } else {
      final BlockModelDefinition.Variants variants = simpleModels.get();
      final Map<String, BlockStateModel.Unbaked> models = variants.models();
      final ImmutableMap.Builder<String, BlockStateModel.Unbaked> newModelsBuilder = new ImmutableMap.Builder<>();

      for (Map.Entry<String, BlockStateModel.Unbaked> entry : models.entrySet()) {
        final String key = entry.getKey();
        final BlockStateModel.Unbaked unbaked = entry.getValue();
        newModelsBuilder.put(key.isEmpty() ? "type=bottom" : key + ",type=bottom", unbaked);
        newModelsBuilder.put(key.isEmpty() ? "type=top" : key + ",type=top", transformUnbakedModel(unbaked, modelVariant -> modelVariant.withModel(modelVariant.modelId().withSuffixedPath("_top"))));
        newModelsBuilder.put(key.isEmpty() ? "type=double" : key + ",type=double", transformUnbakedModel(unbaked, modelVariant -> modelVariant.withModel(modelVariant.modelId().withPath(s -> s.endsWith("_slab") ? s.replace("_slab", "_block") : s.replace("_slab", "")))));
      }

      newSimpleModels = Optional.of(new BlockModelDefinition.Variants(newModelsBuilder.build()));
    }

    // mubltipart 的部分，目前不用动
    return new BlockModelDefinition(newSimpleModels, modelForFull.multipartModel());
  }

  public static BlockStateModel.Unbaked transformUnbakedModel(BlockStateModel.Unbaked unbaked, UnaryOperator<ModelVariant> operator) {
    if (unbaked instanceof SimpleBlockStateModel.Unbaked(ModelVariant variant)) {
      return new SimpleBlockStateModel.Unbaked(operator.apply(variant));
    } else if (unbaked instanceof WeightedBlockStateModel.Unbaked(Pool<BlockStateModel.Unbaked> entries)) {
      return new WeightedBlockStateModel.Unbaked(entries.transform(unbaked1 -> transformUnbakedModel(unbaked1, operator)));
    } else {
      return unbaked;
    }
  }

  public static BlockModelDefinitionCreator composeStateForSlab(@NotNull BlockModelDefinitionCreator stateForFull) {
    return new Forwarding(stateForFull);
  }

  public static Identifier getTextureOf(Block block) {
    if (block instanceof MishangucBlock mishangucBlock) {
      return mishangucBlock.getTexture(TextureKey.TEXTURE);
    } else {
      return TextureMap.getId(block);
    }
  }

  @Environment(EnvType.CLIENT)
  private static class Forwarding implements BlockModelDefinitionCreator {
    private final @NotNull BlockModelDefinitionCreator stateForFull;

    public Forwarding(@NotNull BlockModelDefinitionCreator stateForFull) {
      this.stateForFull = stateForFull;
    }

    @Override
    public Block getBlock() {
      return stateForFull.getBlock();
    }

    @Override
    public BlockModelDefinition createBlockModelDefinition() {
      return composeStateForSlab(stateForFull.createBlockModelDefinition());
    }
  }
}
