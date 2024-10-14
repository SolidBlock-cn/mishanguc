package pers.solid.mishang.uc.block;

import com.google.common.collect.ImmutableMap;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.ShapeContext;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.enums.BlockFace;
import net.minecraft.data.client.BlockStateModelGenerator;
import net.minecraft.data.client.ModelIds;
import net.minecraft.data.client.ModelProvider;
import net.minecraft.data.client.TextureMap;
import net.minecraft.data.server.recipe.CraftingRecipeJsonBuilder;
import net.minecraft.data.server.recipe.RecipeProvider;
import net.minecraft.data.server.recipe.ShapedRecipeJsonBuilder;
import net.minecraft.recipe.book.RecipeCategory;
import net.minecraft.text.MutableText;
import net.minecraft.util.Identifier;
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
import pers.solid.mishang.uc.data.MishangucModels;
import pers.solid.mishang.uc.data.ModelHelper;
import pers.solid.mishang.uc.util.TextBridge;

import java.util.Map;

public class FullWallSignBlock extends WallSignBlock {
  public static final MapCodec<FullWallSignBlock> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(createBaseBlockCodec(), createSettingsCodec()).apply(instance, FullWallSignBlock::new));

  public static final Map<Direction, VoxelShape> SHAPES_WHEN_WALL =
      MishangUtils.createHorizontalDirectionToShape(0, 0, 0, 16, 16, 1);
  public static final Map<Direction, VoxelShape> SHAPES_WHEN_FLOOR =
      MishangUtils.createHorizontalDirectionToShape(0, 0, 0, 16, 1, 16);
  public static final Map<Direction, VoxelShape> SHAPES_WHEN_CEILING =
      MishangUtils.createHorizontalDirectionToShape(0, 15, 0, 16, 16, 16);

  @Unmodifiable
  public static final Map<BlockFace, Map<Direction, VoxelShape>>
      SHAPE_PER_WALL_MOUNT_LOCATION =
      ImmutableMap.of(
          BlockFace.CEILING,
          SHAPES_WHEN_CEILING,
          BlockFace.FLOOR,
          SHAPES_WHEN_FLOOR,
          BlockFace.WALL,
          SHAPES_WHEN_WALL);

  public FullWallSignBlock(@Nullable Block baseBlock, Settings settings) {
    super(baseBlock, settings);
  }

  @ApiStatus.AvailableSince("0.1.7")
  public FullWallSignBlock(@NotNull Block baseBlock) {
    this(baseBlock, Block.Settings.copy(baseBlock));
  }

  @Override
  public MutableText getName() {
    return baseBlock == null
        ? super.getName()
        : TextBridge.translatable("block.mishanguc.full_wall_sign", baseBlock.getName());
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

  private @Nullable String getRecipeGroup() {
    if (baseBlock instanceof ColoredBlock) return null;
    if (MishangUtils.isConcrete(baseBlock)) return "mishanguc:full_concrete_wall_sign";
    if (MishangUtils.isTerracotta(baseBlock)) return "mishanguc:full_terracotta_wall_sign";
    return null;
  }

  @Override
  public @Nullable CraftingRecipeJsonBuilder getCraftingRecipe() {
    if (baseBlock == null) return null;
    return ShapedRecipeJsonBuilder.create(RecipeCategory.DECORATIONS, this, 4)
        .pattern("-#-")
        .pattern("###")
        .pattern("-#-")
        .input('#', baseBlock).input('-', WallSignBlocks.INVISIBLE_WALL_SIGN)
        .criterion("has_base_block", RecipeProvider.conditionsFromItem(baseBlock))
        .criterion("has_sign", RecipeProvider.conditionsFromItem(WallSignBlocks.INVISIBLE_WALL_SIGN))
        .group(getRecipeGroup());
  }

  @Override
  public void registerModels(ModelProvider modelProvider, BlockStateModelGenerator blockStateModelGenerator) {
    if (this == WallSignBlocks.INVISIBLE_WALL_SIGN || this == WallSignBlocks.INVISIBLE_GLOWING_WALL_SIGN) {
      blockStateModelGenerator.blockStateCollector.accept(createBlockStates(ModelIds.getBlockModelId(this)));
      return;
    }
    final TextureMap textures = TextureMap.texture(ModelHelper.getTextureOf(baseBlock));
    final Identifier modelId = MishangucModels.FULL_WALL_SIGN.upload(this, textures, blockStateModelGenerator.modelCollector);
    blockStateModelGenerator.blockStateCollector.accept(createBlockStates(modelId));
  }

  @Override
  protected MapCodec<? extends FullWallSignBlock> getCodec() {
    return CODEC;
  }
}
