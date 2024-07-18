package pers.solid.mishang.uc.block;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.data.client.*;
import net.minecraft.data.server.recipe.CraftingRecipeJsonBuilder;
import net.minecraft.data.server.recipe.RecipeProvider;
import net.minecraft.data.server.recipe.ShapedRecipeJsonBuilder;
import net.minecraft.item.Item.TooltipContext;
import net.minecraft.item.Items;
import net.minecraft.predicate.item.ItemPredicate;
import net.minecraft.recipe.Ingredient;
import net.minecraft.registry.tag.ItemTags;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Direction;
import org.jetbrains.annotations.NotNull;
import pers.solid.brrp.v1.api.RuntimeResourcePack;
import pers.solid.brrp.v1.model.ModelJsonBuilder;
import pers.solid.mishang.uc.util.LineColor;
import pers.solid.mishang.uc.util.LineType;
import pers.solid.mishang.uc.util.RoadConnectionState;

import java.util.List;

public class RoadBlock extends AbstractRoadBlock {
  public static final MapCodec<RoadBlock> CODEC = RecordCodecBuilder.mapCodec(i -> i.group(createSettingsCodec(), Codec.STRING.fieldOf("texture").forGetter(b -> b.texture), lineColorFieldCodec()).apply(i, RoadBlock::new));
  private final String texture;

  public RoadBlock(Settings settings, String texture, LineColor lineColor) {
    super(settings, lineColor, LineType.NORMAL);
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
  protected MapCodec<? extends RoadBlock> getCodec() {
    return CODEC;
  }

  @Override
  public CraftingRecipeJsonBuilder getCraftingRecipe() {
    if (lineColor != null) return null;
    return ShapedRecipeJsonBuilder.create(getRecipeCategory(), this, 9)
        .pattern("***")
        .pattern("|X|")
        .pattern("***")
        .input('*', ItemTags.COALS)
        .input('|', Items.FLINT)
        .input('X', Ingredient.ofItems(Items.WHITE_CONCRETE, Items.GRAY_CONCRETE, Items.LIGHT_GRAY_CONCRETE, Items.BLACK_CONCRETE))
        .criterionFromItemTag("has_coal", ItemTags.COALS)
        .criterionFromItem(Items.FLINT)
        .criterion("has_proper_concrete", RecipeProvider.conditionsFromItemPredicates(ItemPredicate.Builder.create().items(Items.WHITE_CONCRETE, Items.GRAY_CONCRETE, Items.LIGHT_GRAY_CONCRETE, Items.BLACK_CONCRETE).build()))
        .setCustomRecipeCategory("roads");
  }

  @Override
  public CraftingRecipeJsonBuilder getPaintingRecipe(Block base, Block self) {
    if (lineColor == LineColor.NONE) return null;
    return ShapedRecipeJsonBuilder.create(getRecipeCategory(), self)
        .pattern("***")
        .pattern(" X ")
        .input('*', lineColor.getIngredient())
        .input('X', base)
        .criterionFromItemTag("has_paint", lineColor.getIngredient())
        .criterionFromItem(base)
        .setCustomRecipeCategory("roads");
  }
}
