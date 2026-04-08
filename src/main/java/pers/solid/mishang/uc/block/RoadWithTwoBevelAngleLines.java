package pers.solid.mishang.uc.block;

import com.mojang.datafixers.util.Function3;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.ChatFormatting;
import net.minecraft.client.data.models.BlockModelGenerators;
import net.minecraft.client.data.models.model.TextureMapping;
import net.minecraft.core.Direction;
import net.minecraft.data.recipes.RecipeBuilder;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.Item.TooltipContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.SlabBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import org.jetbrains.annotations.ApiStatus;
import pers.solid.mishang.uc.MishangUtils;
import pers.solid.mishang.uc.blocks.RoadBlocks;
import pers.solid.mishang.uc.data.FasterTextureMap;
import pers.solid.mishang.uc.data.MishangucTextureKeys;
import pers.solid.mishang.uc.mixin.BlockStateModelGeneratorAccessor;
import pers.solid.mishang.uc.util.*;

import java.util.List;

/**
 * <p>带有两个相邻斜线的道路，这两个斜线可以连成V字形。这样的双斜线道路又分为以下情况：
 * <p>是否还有一条中线：将决定道路显示是两条线还是三条线。
 * <p>是否需要适应双线连接，这种情况下道路不能是两个斜线材质的简单叠加，而应该进行特殊适应。
 */
@ApiStatus.AvailableSince("1.1.0")
public interface RoadWithTwoBevelAngleLines extends Road {
  EnumProperty<Direction> FACING = BlockStateProperties.HORIZONTAL_FACING;

  @Override
  default void appendRoadProperties(StateDefinition.Builder<Block, BlockState> builder) {
    Road.super.appendRoadProperties(builder);
    builder.add(FACING);
  }

  @Override
  default void appendRoadTooltip(ItemStack stack, TooltipContext context, List<Component> tooltip, TooltipFlag options) {
    Road.super.appendRoadTooltip(stack, context, tooltip, options);
    tooltip.add(TextBridge.translatable("block.mishanguc.tooltip.road_with_bi_bevel_angle_line.1").withStyle(ChatFormatting.GRAY));
    tooltip.add(TextBridge.translatable("block.mishanguc.tooltip.road_with_bi_bevel_angle_line.2").withStyle(ChatFormatting.GRAY));
    tooltip.add(TextBridge.translatable("block.mishanguc.tooltip.road_with_bi_bevel_angle_line.3").withStyle(ChatFormatting.GRAY));
  }

  @Override
  default BlockState rotateRoad(BlockState state, Rotation rotation) {
    return Road.super.rotateRoad(state, rotation).setValue(FACING, rotation.rotate(state.getValue(FACING)));
  }

  @Override
  default BlockState mirrorRoad(BlockState state, Mirror mirror) {
    return Road.super.mirrorRoad(state, mirror).setValue(FACING, mirror.mirror(state.getValue(FACING)));
  }

  @Override
  default BlockState withPlacementState(BlockState state, BlockPlaceContext ctx) {
    final Direction playerFacing = ctx.getHorizontalDirection();
    return Road.super.withPlacementState(state, ctx).setValue(FACING, ctx.getPlayer() != null && ctx.getPlayer().isShiftKeyDown() ? playerFacing.getOpposite() : playerFacing);
  }

  static <B extends AbstractRoadBlock & RoadWithTwoBevelAngleLines> MapCodec<B> createCodec(RecordCodecBuilder<B, BlockBehaviour.Properties> settingsCodec, Function3<BlockBehaviour.Properties, LineColor, LineType, B> function) {
    return RecordCodecBuilder.mapCodec(i -> i.group(settingsCodec, AbstractRoadBlock.lineColorFieldCodec(), AbstractRoadBlock.lineTypeFieldCodec()).apply(i, function));
  }

  class ImplWithTwoLayerTexture extends AbstractRoadBlock implements RoadWithTwoBevelAngleLines {
    public static final MapCodec<ImplWithTwoLayerTexture> CODEC = RoadWithTwoBevelAngleLines.createCodec(propertiesCodec(), ImplWithTwoLayerTexture::new);

    public ImplWithTwoLayerTexture(Properties settings, LineColor lineColor, LineType lineType) {
      super(settings, lineColor, lineType);
      registerDefaultState(defaultBlockState().setValue(FACING, Direction.SOUTH));
    }

    @Environment(EnvType.CLIENT)
    @Override
    protected <B extends Block & Road> void registerBaseOrSlabModels(B road, BlockModelGenerators blockStateModelGenerator) {
      TextureMapping textures = new FasterTextureMap()
          .base("asphalt")
          .lineTop(MishangUtils.composeAngleLineTexture(lineColor, lineType, true))
          .lineSide(MishangUtils.composeStraightLineTexture(lineColor, lineType));
      final Identifier modelId = road.uploadModel("_with_bi_angle_line", textures, blockStateModelGenerator, MishangucTextureKeys.BASE, MishangucTextureKeys.LINE_TOP, MishangucTextureKeys.LINE_SIDE);
      blockStateModelGenerator.blockStateOutput.accept(road.composeState(BlockModelGenerators.createSimpleBlock(road, BlockModelGenerators.plainVariant(modelId)).with(BlockStateModelGeneratorAccessor.getROTATION_HORIZONTAL_FACING_ALT())));
    }

    @Override
    public RoadConnectionState getConnectionStateOf(BlockState state, Direction direction) {
      final Direction facing = state.getValue(FACING);
      if (facing == direction) {
        return new RoadConnectionState(RoadConnectionState.WhetherConnected.CONNECTED, lineColor, EightHorizontalDirection.of(direction), lineType);
      } else if (facing != direction.getOpposite()) {
        return new RoadConnectionState(RoadConnectionState.WhetherConnected.CONNECTED, lineColor, EightHorizontalDirection.of(HorizontalCornerDirection.fromDirections(facing, direction.getOpposite())), lineType);
      }
      return super.getConnectionStateOf(state, direction);
    }

    @Override
    public void appendDescriptionTooltip(List<Component> tooltip, TooltipContext options) {
      tooltip.add(TextBridge.translatable("lineType.biBevelAngleLine", lineColor.getName(), lineType.getName()).withStyle(ChatFormatting.BLUE));
    }

    @Override
    protected MapCodec<? extends ImplWithTwoLayerTexture> codec() {
      return CODEC;
    }

    @Override
    public RecipeBuilder getPaintingRecipe(Block base, Block self, RecipeProvider recipeGenerator) {
      return recipeGenerator.shaped(RecipeCategory.BUILDING_BLOCKS, self, 3)
          .pattern(" **")
          .pattern("** ")
          .pattern("XXX")
          .define('*', lineColor.getIngredient())
          .define('X', base)
          .unlockedBy("has_paint", recipeGenerator.has(lineColor.getIngredient()))
          .unlockedBy(RecipeProvider.getHasName(base), recipeGenerator.has(base));
    }
  }

  class ImplWithThreeLayerTexture extends AbstractRoadBlock implements RoadWithTwoBevelAngleLines {
    public static final MapCodec<ImplWithThreeLayerTexture> CODEC = RoadWithTwoBevelAngleLines.createCodec(propertiesCodec(), ImplWithThreeLayerTexture::new);

    public ImplWithThreeLayerTexture(Properties settings, LineColor lineColor, LineType lineType) {
      super(settings, lineColor, lineType);
      registerDefaultState(defaultBlockState().setValue(FACING, Direction.SOUTH));
    }

    @Environment(EnvType.CLIENT)
    @Override
    protected <B extends Block & Road> void registerBaseOrSlabModels(B road, BlockModelGenerators blockStateModelGenerator) {
      final TextureMapping textures = new FasterTextureMap()
          .base("asphalt")
          .lineTop(MishangUtils.composeStraightLineTexture(lineColor, lineType))
          .lineSide(MishangUtils.composeStraightLineTexture(lineColor, lineType))
          .lineTop2(MishangUtils.composeAngleLineTexture(lineColor, lineType, true));
      final Identifier modelId = road.uploadModel("_with_straight_and_bi_angle_line", textures, blockStateModelGenerator, MishangucTextureKeys.BASE, MishangucTextureKeys.LINE_TOP, MishangucTextureKeys.LINE_SIDE, MishangucTextureKeys.LINE_TOP2);
      blockStateModelGenerator.blockStateOutput.accept(road.composeState(BlockModelGenerators.createSimpleBlock(road, BlockModelGenerators.plainVariant(modelId)).with(BlockStateModelGeneratorAccessor.getROTATION_HORIZONTAL_FACING_ALT())));
    }

    @Override
    public RoadConnectionState getConnectionStateOf(BlockState state, Direction direction) {
      final Direction facing = state.getValue(FACING);
      if (facing == direction || facing == direction.getOpposite()) {
        return new RoadConnectionState(RoadConnectionState.WhetherConnected.CONNECTED, lineColor, EightHorizontalDirection.of(direction), lineType);
      } else {
        return new RoadConnectionState(RoadConnectionState.WhetherConnected.CONNECTED, lineColor, EightHorizontalDirection.of(HorizontalCornerDirection.fromDirections(facing, direction.getOpposite())), lineType);
      }
    }

    @Override
    public void appendDescriptionTooltip(List<Component> tooltip, TooltipContext options) {
      tooltip.add(TextBridge.translatable("lineType.biBevelAngleLine", lineColor.getName(), lineType.getName()).withStyle(ChatFormatting.BLUE));
    }

    @Override
    protected MapCodec<? extends ImplWithThreeLayerTexture> codec() {
      return CODEC;
    }

    @Override
    public RecipeBuilder getPaintingRecipe(Block base, Block self, RecipeProvider recipeGenerator) {
      Block base2 = RoadBlocks.getRoadBlockWithLine(lineColor, lineType);
      if (base instanceof SlabBlock) {
        base2 = ((AbstractRoadBlock) base2).getRoadSlab();
      }
      return recipeGenerator.shaped(RecipeCategory.BUILDING_BLOCKS, self, 3)
          .pattern(" *X")
          .pattern("*X*")
          .pattern("X* ")
          .define('*', lineColor.getIngredient())
          .define('X', base2)
          .unlockedBy("has_paint", recipeGenerator.has(lineColor.getIngredient()))
          .unlockedBy(RecipeProvider.getHasName(base2), recipeGenerator.has(base2));
    }
  }
}
