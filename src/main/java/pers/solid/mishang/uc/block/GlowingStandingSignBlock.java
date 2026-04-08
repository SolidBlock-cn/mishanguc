package pers.solid.mishang.uc.block;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.data.models.BlockModelGenerators;
import net.minecraft.client.data.models.ModelProvider;
import net.minecraft.client.data.models.model.ItemModelUtils;
import net.minecraft.client.data.models.model.TextureMapping;
import net.minecraft.client.resources.model.sprite.Material;
import net.minecraft.data.recipes.RecipeBuilder;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import org.jetbrains.annotations.Nullable;
import pers.solid.mishang.uc.MishangUtils;
import pers.solid.mishang.uc.Mishanguc;
import pers.solid.mishang.uc.blocks.WallSignBlocks;
import pers.solid.mishang.uc.data.MishangucModels;
import pers.solid.mishang.uc.data.MishangucTextureKeys;
import pers.solid.mishang.uc.item.ColoredTintSource;

/**
 * 发光的直立告示牌。
 */
public class GlowingStandingSignBlock extends StandingSignBlock {
  public static final MapCodec<GlowingStandingSignBlock> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(baseBlockCodec(), propertiesCodec()).apply(instance, GlowingStandingSignBlock::new));
  protected static final Material DEFAULT_GLOW_MATERIAL = new Material(Mishanguc.id("block/white_light"));
  public Material glowMaterial = DEFAULT_GLOW_MATERIAL;

  public GlowingStandingSignBlock(Block baseBlock, Properties settings) {
    super(baseBlock, settings.lightLevel(x -> 15));
  }

  @Override
  public MutableComponent getName() {
    if (baseBlock != null) return Component.translatable("block.mishanguc.glowing_standing_sign", baseBlock.getName());
    return super.getName();
  }

  @Environment(EnvType.CLIENT)
  @Override
  public void registerModels(ModelProvider modelProvider, BlockModelGenerators blockStateModelGenerator) {
    final TextureMapping textures = TextureMapping.defaultTexture(getBaseMaterial()).put(MishangucTextureKeys.BAR, barMaterial).put(MishangucTextureKeys.GLOW, glowMaterial);
    final Identifier modelId = MishangucModels.GLOWING_STANDING_SIGN.create(this, textures, blockStateModelGenerator.modelOutput);
    final Identifier r1ModelId = MishangucModels.GLOWING_STANDING_SIGN_1.create(this, textures, blockStateModelGenerator.modelOutput);
    final Identifier r2ModelId = MishangucModels.GLOWING_STANDING_SIGN_2.create(this, textures, blockStateModelGenerator.modelOutput);
    final Identifier r3ModelId = MishangucModels.GLOWING_STANDING_SIGN_3.create(this, textures, blockStateModelGenerator.modelOutput);
    final Identifier barredModelId = MishangucModels.GLOWING_STANDING_SIGN_BARRED.create(this, textures, blockStateModelGenerator.modelOutput);
    final Identifier barredR1ModelId = MishangucModels.GLOWING_STANDING_SIGN_BARRED_1.create(this, textures, blockStateModelGenerator.modelOutput);
    final Identifier barredR2ModelId = MishangucModels.GLOWING_STANDING_SIGN_BARRED_2.create(this, textures, blockStateModelGenerator.modelOutput);
    final Identifier barredR3ModelId = MishangucModels.GLOWING_STANDING_SIGN_BARRED_3.create(this, textures, blockStateModelGenerator.modelOutput);
    blockStateModelGenerator.blockStateOutput.accept(createBlockStates(modelId, r1ModelId, r2ModelId, r3ModelId, barredModelId, barredR1ModelId, barredR2ModelId, barredR3ModelId));

    if (this instanceof ColoredBlock) {
      blockStateModelGenerator.itemModelOutput.accept(asItem(), ItemModelUtils.tintedModel(barredModelId, ColoredTintSource.INSTANCE, ColoredTintSource.INSTANCE));
    } else {
      blockStateModelGenerator.registerSimpleItemModel(this, barredModelId);
    }
  }

  private @Nullable String getRecipeGroup() {
    if (baseBlock instanceof ColoredBlock) return null;
    if (MishangUtils.isConcrete(baseBlock)) return "mishanguc:glowing_concrete_standing_sign";
    if (MishangUtils.isTerracotta(baseBlock)) return "mishanguc:glowing_terracotta_standing_sign";
    if (baseBlock == Blocks.BLUE_ICE || baseBlock == Blocks.PACKED_ICE) {
      return "mishanguc:glowing_ice_standing_sign";
    }
    return null;
  }

  @Override
  public RecipeBuilder getCraftingRecipe(RecipeProvider recipeGenerator) {
    if (baseBlock == null) return null;
    return recipeGenerator.shaped(RecipeCategory.BUILDING_BLOCKS, this, 4)
        .pattern("---")
        .pattern("###")
        .pattern(" | ")
        .define('#', baseBlock).define('-', WallSignBlocks.INVISIBLE_GLOWING_WALL_SIGN).define('|', Items.STICK)
        .unlockedBy("has_base_block", recipeGenerator.has(baseBlock))
        .unlockedBy("has_sign", recipeGenerator.has(WallSignBlocks.INVISIBLE_GLOWING_WALL_SIGN))
        .group(getRecipeGroup());
  }

  @Override
  protected MapCodec<? extends GlowingStandingSignBlock> codec() {
    return CODEC;
  }
}
