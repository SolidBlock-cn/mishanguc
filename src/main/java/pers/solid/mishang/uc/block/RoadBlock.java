package pers.solid.mishang.uc.block;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.SlabBlock;
import net.minecraft.data.client.BlockStateModelGenerator;
import net.minecraft.data.client.Models;
import net.minecraft.data.client.TextureMap;
import net.minecraft.data.server.recipe.CraftingRecipeJsonBuilder;
import net.minecraft.data.server.recipe.RecipeProvider;
import net.minecraft.data.server.recipe.ShapedRecipeJsonBuilder;
import net.minecraft.item.Item.TooltipContext;
import net.minecraft.item.Items;
import net.minecraft.predicate.item.ItemPredicate;
import net.minecraft.recipe.Ingredient;
import net.minecraft.recipe.book.RecipeCategory;
import net.minecraft.registry.tag.ItemTags;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Direction;
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
  public CraftingRecipeJsonBuilder getCraftingRecipe() {
    if (lineColor != LineColor.NONE) return null;
    return ShapedRecipeJsonBuilder.create(RecipeCategory.BUILDING_BLOCKS, this, 9)
        .pattern("***")
        .pattern("|X|")
        .pattern("***")
        .input('*', ItemTags.COALS)
        .input('|', Items.FLINT)
        .input('X', Ingredient.ofItems(Items.WHITE_CONCRETE, Items.GRAY_CONCRETE, Items.LIGHT_GRAY_CONCRETE, Items.BLACK_CONCRETE))
        .criterion("has_coal", RecipeProvider.conditionsFromTag(ItemTags.COALS))
        .criterion(RecipeProvider.hasItem(Items.FLINT), RecipeProvider.conditionsFromItem(Items.FLINT))
        .criterion("has_proper_concrete", RecipeProvider.conditionsFromItemPredicates(ItemPredicate.Builder.create().items(Items.WHITE_CONCRETE, Items.GRAY_CONCRETE, Items.LIGHT_GRAY_CONCRETE, Items.BLACK_CONCRETE).build()));
  }

  @Override
  public CraftingRecipeJsonBuilder getPaintingRecipe(Block base, Block self) {
    if (lineColor == LineColor.NONE) return null;
    return ShapedRecipeJsonBuilder.create(RecipeCategory.BUILDING_BLOCKS, self)
        .pattern("***")
        .pattern(" X ")
        .input('*', lineColor.getIngredient())
        .input('X', base)
        .criterion("has_paint", RecipeProvider.conditionsFromTag(lineColor.getIngredient()))
        .criterion(RecipeProvider.hasItem(base), RecipeProvider.conditionsFromItem(base));
  }
}
