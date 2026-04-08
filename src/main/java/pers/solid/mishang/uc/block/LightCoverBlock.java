package pers.solid.mishang.uc.block;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import org.jetbrains.annotations.NotNull;
import pers.solid.mishang.uc.MishangUtils;

import java.util.Map;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.recipes.RecipeBuilder;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.data.recipes.SingleItemRecipeBuilder;
import net.minecraft.resources.Identifier;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

public class LightCoverBlock extends WallLightBlock {
  public static final MapCodec<LightCoverBlock> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(Codec.STRING.fieldOf("light_color").forGetter(s -> s.lightColor), propertiesCodec()).apply(instance, LightCoverBlock::new));
  private static final Map<Direction, VoxelShape> SHAPE_PER_DIRECTION = MishangUtils.createDirectionToShape(0, 0, 0, 16, 1, 16);

  public LightCoverBlock(String lightColor, Properties settings) {
    super(lightColor, settings, true);
    registerDefaultState(defaultBlockState().setValue(FACING, Direction.SOUTH));
  }

  @Override
  public VoxelShape getShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext context) {
    return SHAPE_PER_DIRECTION.get(state.getValue(FACING));
  }

  @Override
  protected MapCodec<? extends LightCoverBlock> codec() {
    return CODEC;
  }

  @Override
  public RecipeBuilder getCraftingRecipe(RecipeProvider recipeGenerator) {
    final Identifier itemId = BuiltInRegistries.ITEM.getKey(asItem());
    final @NotNull Item fullLight = getBaseLight(itemId.getNamespace(), lightColor, this);
    return SingleItemRecipeBuilder.stonecutting(Ingredient.of(fullLight), RecipeCategory.DECORATIONS, this, 8)
        .unlockedBy(RecipeProvider.getHasName(fullLight), recipeGenerator.has(fullLight));
  }

  @Override
  protected boolean isPathfindable(BlockState state, PathComputationType type) {
    return type == PathComputationType.WATER && state.getFluidState().is(FluidTags.WATER);
  }
}
