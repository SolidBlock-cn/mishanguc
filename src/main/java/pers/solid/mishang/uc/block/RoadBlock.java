package pers.solid.mishang.uc.block;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.SlabBlock;
import net.minecraft.client.data.BlockStateModelGenerator;
import net.minecraft.client.data.Models;
import net.minecraft.client.data.TextureMap;
import net.minecraft.data.recipe.CraftingRecipeJsonBuilder;
import net.minecraft.data.recipe.RecipeGenerator;
import net.minecraft.item.Item;
import net.minecraft.item.Item.TooltipContext;
import net.minecraft.item.Items;
import net.minecraft.recipe.book.RecipeCategory;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.tag.ItemTags;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Direction;
import pers.solid.mishang.uc.Mishanguc;
import pers.solid.mishang.uc.util.LineColor;
import pers.solid.mishang.uc.util.LineType;
import pers.solid.mishang.uc.util.RoadConnectionState;

import java.util.List;

public class RoadBlock extends AbstractRoadBlock {
  public static final MapCodec<RoadBlock> CODEC = RecordCodecBuilder.mapCodec(i -> i.group(createSettingsCodec(), Identifier.CODEC.fieldOf("texture").forGetter(b -> b.texture), lineColorFieldCodec()).apply(i, RoadBlock::new));
  private final Identifier texture;

  public RoadBlock(Settings settings, Identifier texture, LineColor lineColor) {
    super(settings, lineColor, LineType.NORMAL);
    this.texture = texture;
  }

  @Override
  public RoadConnectionState getConnectionStateOf(BlockState state, Direction direction) {
    return RoadConnectionState.empty();
  }

  @Override
  protected <B extends Block & Road> void registerBaseOrSlabModels(B road, BlockStateModelGenerator blockStateModelGenerator) {
    final TextureMap textures = TextureMap.all(texture);
    final Identifier modelId;
    if (road instanceof SlabBlock) {
      modelId = Models.SLAB.upload(road, textures, blockStateModelGenerator.modelCollector);
      Models.SLAB_TOP.upload(road, textures, blockStateModelGenerator.modelCollector);
    } else {
      modelId = Models.CUBE_ALL.upload(road, textures, blockStateModelGenerator.modelCollector);
    }
    blockStateModelGenerator.blockStateCollector.accept(road.composeState(BlockStateModelGenerator.createBlockStateWithRandomHorizontalRotations(road, modelId)));
  }

  @Override
  public void appendDescriptionTooltip(List<Text> tooltip, TooltipContext options) {

  }

  @Override
  protected MapCodec<? extends RoadBlock> getCodec() {
    return CODEC;
  }

  @Override
  public CraftingRecipeJsonBuilder getCraftingRecipe(RecipeGenerator recipeGenerator) {
    if (lineColor != LineColor.NONE) return null;
    final TagKey<Item> tag = TagKey.of(RegistryKeys.ITEM, Mishanguc.id("road_materials"));
    return recipeGenerator.createShaped(RecipeCategory.BUILDING_BLOCKS, this, 9)
        .pattern("***")
        .pattern("|X|")
        .pattern("***")
        .input('*', ItemTags.COALS)
        .input('|', Items.FLINT)
        .input('X', tag)
        .criterion("has_coal", recipeGenerator.conditionsFromTag(ItemTags.COALS))
        .criterion(RecipeGenerator.hasItem(Items.FLINT), recipeGenerator.conditionsFromItem(Items.FLINT))
        .criterion("has_proper_concrete", recipeGenerator.conditionsFromTag(tag));
  }

  @Override
  public CraftingRecipeJsonBuilder getPaintingRecipe(Block base, Block self, RecipeGenerator recipeGenerator) {
    if (lineColor == LineColor.NONE) return null;
    return recipeGenerator.createShaped(RecipeCategory.BUILDING_BLOCKS, self)
        .pattern("***")
        .pattern(" X ")
        .input('*', lineColor.getIngredient())
        .input('X', base)
        .criterion("has_paint", recipeGenerator.conditionsFromTag(lineColor.getIngredient()))
        .criterion(RecipeGenerator.hasItem(base), recipeGenerator.conditionsFromItem(base));
  }
}
