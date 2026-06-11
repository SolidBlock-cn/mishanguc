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
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Nullable;
import pers.solid.mishang.uc.MishangUtils;
import pers.solid.mishang.uc.Mishanguc;
import pers.solid.mishang.uc.blocks.WallSignBlocks;
import pers.solid.mishang.uc.data.MishangucModels;
import pers.solid.mishang.uc.data.MishangucTextureKeys;
import pers.solid.mishang.uc.item.ColoredTintSource;

import java.util.Optional;

public class GlowingHungSignBlock extends HungSignBlock {
  public static final MapCodec<GlowingHungSignBlock> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(baseBlockCodec(), propertiesCodec()).apply(instance, GlowingHungSignBlock::new));
  @ApiStatus.AvailableSince("0.1.7")
  protected static final Identifier DEFAULT_GLOW_TEXTURE = Mishanguc.id("block/white_light");
  public Identifier glowTexture;

  protected GlowingHungSignBlock(Optional<Block> baseBlock, Properties settings) {
    this(baseBlock.orElse(null), settings);
  }

  public GlowingHungSignBlock(@Nullable Block baseBlock, Properties settings) {
    super(baseBlock, settings.lightLevel(s -> 15));
    this.glowTexture = DEFAULT_GLOW_TEXTURE;
  }

  @Override
  public MutableComponent getName() {
    if (baseBlock != null) {
      return Component.translatable("block.mishanguc.glowing_hung_sign", baseBlock.getName());
    }
    return super.getName();
  }

  @Environment(EnvType.CLIENT)
  @Override
  public void registerModels(ModelProvider modelProvider, BlockModelGenerators blockStateModelGenerator) {
    final Material material = getBaseMaterial();
    final TextureMapping textures = TextureMapping.defaultTexture(material);
    if (barTexture != null) textures.put(MishangucTextureKeys.BAR, new Material(barTexture));
    if (textureTop != null) textures.put(MishangucTextureKeys.TEXTURE_TOP, new Material(textureTop));
    textures.put(MishangucTextureKeys.GLOW, new Material(glowTexture));

    final Identifier id = MishangucModels.GLOWING_HUNG_SIGN.create(this, textures, blockStateModelGenerator.modelOutput);
    final Identifier bodyId = MishangucModels.GLOWING_HUNG_SIGN_BODY.create(this, textures, blockStateModelGenerator.modelOutput);
    final Identifier topBarId = MishangucModels.HUNG_SIGN_TOP_BAR.create(this, textures, blockStateModelGenerator.modelOutput);
    final Identifier topBarEdgeId = MishangucModels.HUNG_SIGN_TOP_BAR_EDGE.create(this, textures, blockStateModelGenerator.modelOutput);

    blockStateModelGenerator.blockStateOutput.accept(createBlockStates(bodyId, topBarId, topBarEdgeId));
    if (this instanceof ColoredBlock) {
      blockStateModelGenerator.itemModelOutput.accept(asItem(), ItemModelUtils.tintedModel(id, ColoredTintSource.INSTANCE, ColoredTintSource.INSTANCE));
    }
  }


  private @Nullable String getRecipeGroup() {
    if (baseBlock instanceof ColoredBlock) return null;
    if (MishangUtils.isConcrete(baseBlock)) return "mishanguc:glowing_concrete_hung_sign";
    if (MishangUtils.isTerracotta(baseBlock)) return "mishanguc:glowing_terracotta_hung_sign";
    if (baseBlock == Blocks.BLUE_ICE || baseBlock == Blocks.PACKED_ICE) {
      return "mishanguc:glowing_ice_hung_sign";
    }
    return null;
  }

  @Override
  public @Nullable RecipeBuilder getCraftingRecipe(RecipeProvider recipeGenerator) {
    if (baseBlock == null) return null;
    return recipeGenerator.shaped(RecipeCategory.DECORATIONS, this, 6)
        .pattern("-#-")
        .pattern("-#-")
        .pattern("-#-")
        .define('#', baseBlock).define('-', WallSignBlocks.INVISIBLE_GLOWING_WALL_SIGN)
        .unlockedBy("has_base_block", recipeGenerator.has(baseBlock))
        .unlockedBy("has_sign", recipeGenerator.has(WallSignBlocks.INVISIBLE_GLOWING_WALL_SIGN))
        .group(getRecipeGroup());
  }

  @Override
  protected MapCodec<? extends GlowingHungSignBlock> codec() {
    return CODEC;
  }
}
