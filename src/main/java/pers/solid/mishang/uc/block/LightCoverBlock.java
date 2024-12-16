package pers.solid.mishang.uc.block;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.block.BlockState;
import net.minecraft.block.ShapeContext;
import net.minecraft.data.server.recipe.CraftingRecipeJsonBuilder;
import net.minecraft.data.server.recipe.RecipeGenerator;
import net.minecraft.data.server.recipe.StonecuttingRecipeJsonBuilder;
import net.minecraft.entity.ai.pathing.NavigationType;
import net.minecraft.item.Item;
import net.minecraft.recipe.Ingredient;
import net.minecraft.recipe.book.RecipeCategory;
import net.minecraft.registry.Registries;
import net.minecraft.registry.tag.FluidTags;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;
import org.jetbrains.annotations.NotNull;
import pers.solid.mishang.uc.MishangUtils;

import java.util.Map;

public class LightCoverBlock extends WallLightBlock {
  public static final MapCodec<LightCoverBlock> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(Codec.STRING.fieldOf("light_color").forGetter(s -> s.lightColor), createSettingsCodec()).apply(instance, LightCoverBlock::new));
  private static final Map<Direction, VoxelShape> SHAPE_PER_DIRECTION = MishangUtils.createDirectionToShape(0, 0, 0, 16, 1, 16);

  public LightCoverBlock(String lightColor, Settings settings) {
    super(lightColor, settings, true);
    setDefaultState(getDefaultState().with(FACING, Direction.SOUTH));
  }

  @Override
  public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
    return SHAPE_PER_DIRECTION.get(state.get(FACING));
  }

  @Override
  protected MapCodec<? extends LightCoverBlock> getCodec() {
    return CODEC;
  }

  @Override
  public CraftingRecipeJsonBuilder getCraftingRecipe(RecipeGenerator recipeGenerator) {
    final Identifier itemId = Registries.ITEM.getId(asItem());
    final @NotNull Item fullLight = getBaseLight(itemId.getNamespace(), lightColor, this);
    return StonecuttingRecipeJsonBuilder.createStonecutting(Ingredient.ofItems(fullLight), RecipeCategory.DECORATIONS, this, 8)
        .criterion(RecipeGenerator.hasItem(fullLight), recipeGenerator.conditionsFromItem(fullLight));
  }

  @Override
  protected boolean canPathfindThrough(BlockState state, NavigationType type) {
    return type == NavigationType.WATER && state.getFluidState().isIn(FluidTags.WATER);
  }
}
