package pers.solid.mishang.uc.block;

import com.google.common.collect.ImmutableMap;
import net.devtech.arrp.json.models.JModel;
import net.devtech.arrp.json.models.JTextures;
import net.devtech.arrp.json.recipe.JRecipe;
import net.devtech.arrp.json.recipe.JShapedRecipe;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.ShapeContext;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.enums.WallMountLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.Unmodifiable;
import pers.solid.mishang.uc.MishangUtils;
import pers.solid.mishang.uc.blockentity.FullWallSignBlockEntity;
import pers.solid.mishang.uc.blocks.WallSignBlocks;

import java.util.Map;

public class FullWallSignBlock extends WallSignBlock {
  public static final Map<Direction, VoxelShape> SHAPES_WHEN_WALL =
      MishangUtils.createHorizontalDirectionToShape(0, 0, 0, 16, 16, 1);
  public static final Map<Direction, VoxelShape> SHAPES_WHEN_FLOOR =
      MishangUtils.createHorizontalDirectionToShape(0, 0, 0, 16, 1, 16);
  public static final Map<Direction, VoxelShape> SHAPES_WHEN_CEILING =
      MishangUtils.createHorizontalDirectionToShape(0, 15, 0, 16, 16, 16);

  @Unmodifiable
  public static final Map<WallMountLocation, Map<Direction, VoxelShape>>
      SHAPE_PER_WALL_MOUNT_LOCATION =
      ImmutableMap.of(
          WallMountLocation.CEILING,
          SHAPES_WHEN_CEILING,
          WallMountLocation.FLOOR,
          SHAPES_WHEN_FLOOR,
          WallMountLocation.WALL,
          SHAPES_WHEN_WALL);

  public FullWallSignBlock(@Nullable Block baseBlock, Settings settings) {
    super(baseBlock, settings);
  }

  @ApiStatus.AvailableSince("0.1.7")
  public FullWallSignBlock(@NotNull Block baseBlock) {
    this(baseBlock, FabricBlockSettings.copyOf(baseBlock));
  }

  @Override
  public VoxelShape getOutlineShape(
      BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
    return SHAPE_PER_WALL_MOUNT_LOCATION.get(state.get(FACE)).get(state.get(FACING));
  }

  @Override
  public @Nullable BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
    return new FullWallSignBlockEntity(pos, state);
  }

  @Override
  public @Nullable JRecipe getCraftingRecipe() {
    if (baseBlock == null) return null;
    final JShapedRecipe recipe = new JShapedRecipe(this)
        .pattern("-#-", "###", "-#-")
        .addKey("#", baseBlock).addKey("-", WallSignBlocks.INVISIBLE_WALL_SIGN)
        .resultCount(4);
    recipe.addInventoryChangedCriterion("has_base_block", baseBlock).addInventoryChangedCriterion("has_sign", WallSignBlocks.INVISIBLE_WALL_SIGN);
    return recipe;
  }

  @Override
  @Environment(EnvType.CLIENT)
  public @Nullable JModel getBlockModel() {
    return new JModel("mishanguc:block/full_wall_sign").textures(new JTextures().var("texture", getBaseTexture()));
  }
}
