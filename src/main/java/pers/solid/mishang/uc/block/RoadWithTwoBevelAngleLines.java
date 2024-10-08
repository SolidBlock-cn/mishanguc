package pers.solid.mishang.uc.block;

import com.mojang.datafixers.util.Function3;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.SlabBlock;
import net.minecraft.client.item.TooltipType;
import net.minecraft.data.client.BlockStateModelGenerator;
import net.minecraft.data.client.TextureMap;
import net.minecraft.data.server.recipe.CraftingRecipeJsonBuilder;
import net.minecraft.data.server.recipe.RecipeProvider;
import net.minecraft.data.server.recipe.ShapedRecipeJsonBuilder;
import net.minecraft.item.Item.TooltipContext;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.book.RecipeCategory;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.DirectionProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.text.Text;
import net.minecraft.util.BlockMirror;
import net.minecraft.util.BlockRotation;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Direction;
import org.jetbrains.annotations.ApiStatus;
import pers.solid.mishang.uc.MishangUtils;
import pers.solid.mishang.uc.blocks.RoadBlocks;
import pers.solid.mishang.uc.data.FasterTextureMap;
import pers.solid.mishang.uc.data.MishangucTextureKeys;
import pers.solid.mishang.uc.util.*;

import java.util.List;

/**
 * <p>带有两个相邻斜线的道路，这两个斜线可以连成V字形。这样的双斜线道路又分为以下情况：
 * <p>是否还有一条中线：将决定道路显示是两条线还是三条线。
 * <p>是否需要适应双线连接，这种情况下道路不能是两个斜线材质的简单叠加，而应该进行特殊适应。
 */
@ApiStatus.AvailableSince("1.1.0")
public interface RoadWithTwoBevelAngleLines extends Road {
  DirectionProperty FACING = Properties.HORIZONTAL_FACING;

  @Override
  default void appendRoadProperties(StateManager.Builder<Block, BlockState> builder) {
    Road.super.appendRoadProperties(builder);
    builder.add(FACING);
  }

  @Override
  default void appendRoadTooltip(ItemStack stack, TooltipContext context, List<Text> tooltip, TooltipType options) {
    Road.super.appendRoadTooltip(stack, context, tooltip, options);
    tooltip.add(TextBridge.translatable("block.mishanguc.tooltip.road_with_bi_bevel_angle_line.1").formatted(Formatting.GRAY));
    tooltip.add(TextBridge.translatable("block.mishanguc.tooltip.road_with_bi_bevel_angle_line.2").formatted(Formatting.GRAY));
    tooltip.add(TextBridge.translatable("block.mishanguc.tooltip.road_with_bi_bevel_angle_line.3").formatted(Formatting.GRAY));
  }

  @Override
  default BlockState rotateRoad(BlockState state, BlockRotation rotation) {
    return Road.super.rotateRoad(state, rotation).with(FACING, rotation.rotate(state.get(FACING)));
  }

  @Override
  default BlockState mirrorRoad(BlockState state, BlockMirror mirror) {
    return Road.super.mirrorRoad(state, mirror).with(FACING, mirror.apply(state.get(FACING)));
  }

  @Override
  default BlockState withPlacementState(BlockState state, ItemPlacementContext ctx) {
    final Direction playerFacing = ctx.getHorizontalPlayerFacing();
    return Road.super.withPlacementState(state, ctx).with(FACING, ctx.getPlayer() != null && ctx.getPlayer().isSneaking() ? playerFacing.getOpposite() : playerFacing);
  }

  static <B extends AbstractRoadBlock & RoadWithTwoBevelAngleLines> MapCodec<B> createCodec(RecordCodecBuilder<B, AbstractBlock.Settings> settingsCodec, Function3<AbstractBlock.Settings, LineColor, LineType, B> function) {
    return RecordCodecBuilder.mapCodec(i -> i.group(settingsCodec, AbstractRoadBlock.lineColorFieldCodec(), AbstractRoadBlock.lineTypeFieldCodec()).apply(i, function));
  }

  class ImplWithTwoLayerTexture extends AbstractRoadBlock implements RoadWithTwoBevelAngleLines {
    public static final MapCodec<ImplWithTwoLayerTexture> CODEC = RoadWithTwoBevelAngleLines.createCodec(createSettingsCodec(), ImplWithTwoLayerTexture::new);

    public ImplWithTwoLayerTexture(Settings settings, LineColor lineColor, LineType lineType) {
      super(settings, lineColor, lineType);
      setDefaultState(getDefaultState().with(FACING, Direction.SOUTH));
    }

    @Override
    protected <B extends Block & Road> void registerBaseOrSlabModels(B road, BlockStateModelGenerator blockStateModelGenerator) {
      TextureMap textures = new FasterTextureMap()
          .base("asphalt")
          .lineTop(MishangUtils.composeAngleLineTexture(lineColor, lineType, true))
          .lineSide(MishangUtils.composeStraightLineTexture(lineColor, lineType));
      final Identifier modelId = road.uploadModel("_with_bi_angle_line", textures, blockStateModelGenerator, MishangucTextureKeys.BASE, MishangucTextureKeys.LINE_TOP, MishangucTextureKeys.LINE_SIDE);
      blockStateModelGenerator.blockStateCollector.accept(road.composeState(BlockStateModelGenerator.createSingletonBlockState(road, modelId).coordinate(BlockStateModelGenerator.createSouthDefaultHorizontalRotationStates())));
    }

    @Override
    public RoadConnectionState getConnectionStateOf(BlockState state, Direction direction) {
      final Direction facing = state.get(FACING);
      if (facing == direction) {
        return new RoadConnectionState(RoadConnectionState.WhetherConnected.CONNECTED, lineColor, EightHorizontalDirection.of(direction), lineType);
      } else if (facing != direction.getOpposite()) {
        return new RoadConnectionState(RoadConnectionState.WhetherConnected.CONNECTED, lineColor, EightHorizontalDirection.of(HorizontalCornerDirection.fromDirections(facing, direction.getOpposite())), lineType);
      }
      return super.getConnectionStateOf(state, direction);
    }

    @Override
    public void appendDescriptionTooltip(List<Text> tooltip, TooltipContext options) {
      tooltip.add(TextBridge.translatable("lineType.biBevelAngleLine", lineColor.getName(), lineType.getName()).formatted(Formatting.BLUE));
    }

    @Override
    protected MapCodec<? extends ImplWithTwoLayerTexture> getCodec() {
      return CODEC;
    }

    @Override
    public CraftingRecipeJsonBuilder getPaintingRecipe(Block base, Block self) {
      return ShapedRecipeJsonBuilder.create(RecipeCategory.BUILDING_BLOCKS, self, 3)
          .pattern(" **")
          .pattern("** ")
          .pattern("XXX")
          .input('*', lineColor.getIngredient())
          .input('X', base)
          .criterion("has_paint", RecipeProvider.conditionsFromTag(lineColor.getIngredient()))
          .criterion(RecipeProvider.hasItem(base), RecipeProvider.conditionsFromItem(base));
    }
  }

  class ImplWithThreeLayerTexture extends AbstractRoadBlock implements RoadWithTwoBevelAngleLines {
    public static final MapCodec<ImplWithThreeLayerTexture> CODEC = RoadWithTwoBevelAngleLines.createCodec(createSettingsCodec(), ImplWithThreeLayerTexture::new);

    public ImplWithThreeLayerTexture(Settings settings, LineColor lineColor, LineType lineType) {
      super(settings, lineColor, lineType);
      setDefaultState(getDefaultState().with(FACING, Direction.SOUTH));
    }

    @Override
    protected <B extends Block & Road> void registerBaseOrSlabModels(B road, BlockStateModelGenerator blockStateModelGenerator) {
      final TextureMap textures = new FasterTextureMap()
          .base("asphalt")
          .lineTop(MishangUtils.composeStraightLineTexture(lineColor, lineType))
          .lineSide(MishangUtils.composeStraightLineTexture(lineColor, lineType))
          .lineTop2(MishangUtils.composeAngleLineTexture(lineColor, lineType, true));
      final Identifier modelId = road.uploadModel("_with_straight_and_bi_angle_line", textures, blockStateModelGenerator, MishangucTextureKeys.BASE, MishangucTextureKeys.LINE_TOP, MishangucTextureKeys.LINE_SIDE, MishangucTextureKeys.LINE_TOP2);
      blockStateModelGenerator.blockStateCollector.accept(road.composeState(BlockStateModelGenerator.createSingletonBlockState(road, modelId).coordinate(BlockStateModelGenerator.createSouthDefaultHorizontalRotationStates())));
    }

    @Override
    public RoadConnectionState getConnectionStateOf(BlockState state, Direction direction) {
      final Direction facing = state.get(FACING);
      if (facing == direction || facing == direction.getOpposite()) {
        return new RoadConnectionState(RoadConnectionState.WhetherConnected.CONNECTED, lineColor, EightHorizontalDirection.of(direction), lineType);
      } else {
        return new RoadConnectionState(RoadConnectionState.WhetherConnected.CONNECTED, lineColor, EightHorizontalDirection.of(HorizontalCornerDirection.fromDirections(facing, direction.getOpposite())), lineType);
      }
    }

    @Override
    public void appendDescriptionTooltip(List<Text> tooltip, TooltipContext options) {
      tooltip.add(TextBridge.translatable("lineType.biBevelAngleLine", lineColor.getName(), lineType.getName()).formatted(Formatting.BLUE));
    }

    @Override
    protected MapCodec<? extends ImplWithThreeLayerTexture> getCodec() {
      return CODEC;
    }

    @Override
    public CraftingRecipeJsonBuilder getPaintingRecipe(Block base, Block self) {
      Block base2 = RoadBlocks.getRoadBlockWithLine(lineColor, lineType);
      if (base instanceof SlabBlock) {
        base2 = ((AbstractRoadBlock) base2).getRoadSlab();
      }
      return ShapedRecipeJsonBuilder.create(RecipeCategory.BUILDING_BLOCKS, self, 3)
          .pattern(" *X")
          .pattern("*X*")
          .pattern("X* ")
          .input('*', lineColor.getIngredient())
          .input('X', base2)
          .criterion("has_paint", RecipeProvider.conditionsFromTag(lineColor.getIngredient()))
          .criterion(RecipeProvider.hasItem(base2), RecipeProvider.conditionsFromItem(base2));
    }
  }
}
