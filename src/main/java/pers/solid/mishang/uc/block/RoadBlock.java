package pers.solid.mishang.uc.block;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.data.models.BlockModelGenerators;
import net.minecraft.client.data.models.blockstates.MultiVariantGenerator;
import net.minecraft.client.data.models.model.ModelTemplates;
import net.minecraft.client.data.models.model.TextureMapping;
import net.minecraft.client.resources.model.sprite.Material;
import net.minecraft.core.Direction;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.recipes.RecipeBuilder;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Item.TooltipContext;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SlabBlock;
import net.minecraft.world.level.block.state.BlockState;
import org.jspecify.annotations.Nullable;
import pers.solid.mishang.uc.Mishanguc;
import pers.solid.mishang.uc.util.LineColor;
import pers.solid.mishang.uc.util.LineType;
import pers.solid.mishang.uc.util.RoadConnectionState;

import java.util.List;

public class RoadBlock extends AbstractRoadBlock {
  public static final MapCodec<RoadBlock> CODEC = RecordCodecBuilder.mapCodec(i -> i.group(propertiesCodec(), Identifier.CODEC.fieldOf("texture").forGetter(b -> b.texture), lineColorFieldCodec()).apply(i, RoadBlock::new));
  private final Identifier texture;

  public RoadBlock(Properties settings, Identifier texture, LineColor lineColor) {
    super(settings, lineColor, LineType.NORMAL);
    this.texture = texture;
  }

  @Override
  public RoadConnectionState getConnectionStateOf(BlockState state, Direction direction) {
    return RoadConnectionState.empty();
  }

  @Environment(EnvType.CLIENT)
  @Override
  protected <B extends Block & Road> void registerBaseOrSlabModels(B road, BlockModelGenerators blockStateModelGenerator) {
    final TextureMapping textures = TextureMapping.cube(new Material(texture));
    final Identifier modelId;
    if (road instanceof SlabBlock) {
      modelId = ModelTemplates.SLAB_BOTTOM.create(road, textures, blockStateModelGenerator.modelOutput);
      ModelTemplates.SLAB_TOP.create(road, textures, blockStateModelGenerator.modelOutput);
    } else {
      modelId = ModelTemplates.CUBE_ALL.create(road, textures, blockStateModelGenerator.modelOutput);
    }
    blockStateModelGenerator.blockStateOutput.accept(road.composeState(MultiVariantGenerator.dispatch(road, BlockModelGenerators.createRotatedVariants(BlockModelGenerators.plainModel(modelId)))));
  }

  @Override
  public void appendDescriptionTooltip(List<Component> tooltip, TooltipContext options) {

  }

  @Override
  protected MapCodec<? extends RoadBlock> codec() {
    return CODEC;
  }

  @Override
  public @Nullable RecipeBuilder getCraftingRecipe(RecipeProvider recipeGenerator) {
    if (lineColor != LineColor.NONE) return null;
    final TagKey<Item> tag = TagKey.create(Registries.ITEM, Mishanguc.id("road_materials"));
    return recipeGenerator.shaped(RecipeCategory.BUILDING_BLOCKS, this, 9)
        .pattern("***")
        .pattern("|X|")
        .pattern("***")
        .define('*', ItemTags.COALS)
        .define('|', Items.FLINT)
        .define('X', tag)
        .unlockedBy("has_coal", recipeGenerator.has(ItemTags.COALS))
        .unlockedBy(RecipeProvider.getHasName(Items.FLINT), recipeGenerator.has(Items.FLINT))
        .unlockedBy("has_proper_concrete", recipeGenerator.has(tag));
  }

  @Override
  public @Nullable RecipeBuilder getPaintingRecipe(Block base, Block self, RecipeProvider recipeGenerator) {
    if (lineColor == LineColor.NONE) return null;
    return recipeGenerator.shaped(RecipeCategory.BUILDING_BLOCKS, self)
        .pattern("***")
        .pattern(" X ")
        .define('*', lineColor.getIngredient())
        .define('X', base)
        .unlockedBy("has_paint", recipeGenerator.has(lineColor.getIngredient()))
        .unlockedBy(RecipeProvider.getHasName(base), recipeGenerator.has(base));
  }
}
