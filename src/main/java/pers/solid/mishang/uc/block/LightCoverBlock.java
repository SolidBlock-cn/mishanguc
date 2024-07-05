package pers.solid.mishang.uc.block;

import net.minecraft.block.BlockState;
import net.minecraft.block.ShapeContext;
import net.minecraft.data.server.recipe.CraftingRecipeJsonBuilder;
import net.minecraft.data.server.recipe.SingleItemRecipeJsonBuilder;
import net.minecraft.item.Item;
import net.minecraft.recipe.Ingredient;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;
import org.jetbrains.annotations.NotNull;
import pers.solid.mishang.uc.MishangUtils;

import java.util.Map;

public class LightCoverBlock extends WallLightBlock {
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
  public CraftingRecipeJsonBuilder getCraftingRecipe() {
    final Identifier itemId = getItemId();
    final @NotNull Item fullLight = getBaseLight(itemId.getNamespace(), lightColor, this);
    return SingleItemRecipeJsonBuilder.createStonecutting(Ingredient.ofItems(fullLight), this, 8)
        .criterionFromItem(fullLight)
        .setCustomRecipeCategory("light");
  }
}
