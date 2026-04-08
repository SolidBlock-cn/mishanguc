package pers.solid.mishang.uc.block;

import com.google.common.collect.ImmutableMap;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.data.models.BlockModelGenerators;
import net.minecraft.client.data.models.ModelProvider;
import net.minecraft.client.data.models.model.ModelLocationUtils;
import net.minecraft.client.data.models.model.TextureMapping;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.data.recipes.RecipeBuilder;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.Identifier;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.TransparentBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.AttachFace;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.ApiStatus;
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
  public static final MapCodec<FullWallSignBlock> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(createBaseBlockCodec(), propertiesCodec()).apply(instance, FullWallSignBlock::new));

  public static final Map<Direction, VoxelShape> SHAPES_WHEN_WALL =
      MishangUtils.createHorizontalDirectionToShape(0, 0, 0, 16, 16, 1);
  public static final Map<Direction, VoxelShape> SHAPES_WHEN_FLOOR =
      MishangUtils.createHorizontalDirectionToShape(0, 0, 0, 16, 1, 16);
  public static final Map<Direction, VoxelShape> SHAPES_WHEN_CEILING =
      MishangUtils.createHorizontalDirectionToShape(0, 15, 0, 16, 16, 16);

  @Unmodifiable
  public static final Map<AttachFace, Map<Direction, VoxelShape>>
      SHAPE_PER_WALL_MOUNT_LOCATION =
      ImmutableMap.of(
          AttachFace.CEILING,
          SHAPES_WHEN_CEILING,
          AttachFace.FLOOR,
          SHAPES_WHEN_FLOOR,
          AttachFace.WALL,
          SHAPES_WHEN_WALL);

  public FullWallSignBlock(@Nullable Block baseBlock, Properties settings) {
    super(baseBlock, settings);
  }

  @ApiStatus.AvailableSince("0.1.7")
  public FullWallSignBlock(Block baseBlock) {
    this(baseBlock, Block.Properties.ofFullCopy(baseBlock));
  }

  @Override
  public MutableComponent getName() {
    return baseBlock == null
        ? super.getName()
        : TextBridge.translatable("block.mishanguc.full_wall_sign", baseBlock.getName());
  }

  @Override
  public VoxelShape getShape(
      BlockState state, BlockGetter world, BlockPos pos, CollisionContext context) {
    return SHAPE_PER_WALL_MOUNT_LOCATION.get(state.getValue(FACE)).get(state.getValue(FACING));
  }

  @Override
  public @Nullable BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
    return new FullWallSignBlockEntity(pos, state);
  }

  private @Nullable String getRecipeGroup() {
    if (baseBlock instanceof ColoredBlock) return null;
    if (MishangUtils.isConcrete(baseBlock)) return "mishanguc:full_concrete_wall_sign";
    if (MishangUtils.isTerracotta(baseBlock)) return "mishanguc:full_terracotta_wall_sign";
    return null;
  }

  @Override
  public @Nullable RecipeBuilder getCraftingRecipe(RecipeProvider recipeGenerator) {
    if (baseBlock == null) return null;
    return recipeGenerator.shaped(RecipeCategory.DECORATIONS, this, 4)
        .pattern("-#-")
        .pattern("###")
        .pattern("-#-")
        .define('#', baseBlock).define('-', WallSignBlocks.INVISIBLE_WALL_SIGN)
        .unlockedBy("has_base_block", recipeGenerator.has(baseBlock))
        .unlockedBy("has_sign", recipeGenerator.has(WallSignBlocks.INVISIBLE_WALL_SIGN))
        .group(getRecipeGroup());
  }

  @Environment(EnvType.CLIENT)
  @Override
  public void registerModels(ModelProvider modelProvider, BlockModelGenerators blockStateModelGenerator) {
    if (this == WallSignBlocks.INVISIBLE_WALL_SIGN || this == WallSignBlocks.INVISIBLE_GLOWING_WALL_SIGN) {
      blockStateModelGenerator.blockStateOutput.accept(createBlockStates(ModelLocationUtils.getModelLocation(this)));
      return;
    }
    final TextureMapping textures = TextureMapping.defaultTexture(ModelHelper.getTextureOf(baseBlock));
    final Identifier modelId = MishangucModels.FULL_WALL_SIGN.create(this, textures, blockStateModelGenerator.modelOutput);
    blockStateModelGenerator.blockStateOutput.accept(createBlockStates(modelId));
  }

  @Override
  public boolean skipRendering(BlockState state, BlockState stateFrom, Direction direction) {
    if (direction.getAxis().isHorizontal() && state.getBlock() instanceof FullWallSignBlock && stateFrom.getBlock() instanceof FullWallSignBlock wallSignBlockFrom && state.getValue(FACING) == stateFrom.getValue(FACING) && direction.getAxis() != state.getValue(FACING).getAxis()) {
      if (wallSignBlockFrom.baseBlock instanceof TransparentBlock) {
        if (baseBlock instanceof TransparentBlock) {
          // 自身和相邻方块都为透明方块，则双方均为同一方块时隐藏。
          return baseBlock == wallSignBlockFrom.baseBlock;
        } else {
          return false;
        }
      }
      return true;
    } else {
      return false;
    }
  }

  @Override
  protected MapCodec<? extends FullWallSignBlock> codec() {
    return CODEC;
  }
}
