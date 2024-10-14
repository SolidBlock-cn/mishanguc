package pers.solid.mishang.uc.block;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.SlabBlock;
import net.minecraft.data.client.*;
import net.minecraft.data.server.recipe.CraftingRecipeJsonBuilder;
import net.minecraft.data.server.recipe.RecipeProvider;
import net.minecraft.data.server.recipe.ShapedRecipeJsonBuilder;
import net.minecraft.item.Item.TooltipContext;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemStack;
import net.minecraft.item.tooltip.TooltipType;
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
import pers.solid.mishang.uc.MishangUtils;
import pers.solid.mishang.uc.blocks.RoadBlocks;
import pers.solid.mishang.uc.data.FasterTextureMap;
import pers.solid.mishang.uc.data.MishangucTextureKeys;
import pers.solid.mishang.uc.util.*;

import java.util.List;

public interface RoadWithJointLine extends Road {
  DirectionProperty FACING = Properties.HORIZONTAL_FACING;

  @Override
  default void appendRoadProperties(StateManager.Builder<Block, BlockState> builder) {
    Road.super.appendRoadProperties(builder);
    builder.add(FACING);
  }

  @Override
  default RoadConnectionState getConnectionStateOf(BlockState state, Direction direction) {
    return RoadConnectionState.or(
        Road.super.getConnectionStateOf(state, direction),
        RoadConnectionState.of(
            state.get(FACING) != direction.getOpposite(),
            getLineColor(state, direction),
            EightHorizontalDirection.of(direction),
            getLineType(state, direction), null));
  }

  @Override
  default BlockState mirrorRoad(BlockState state, BlockMirror mirror) {
    return Road.super.mirrorRoad(state, mirror).with(FACING, mirror.apply(state.get(FACING)));
  }

  @Override
  default BlockState rotateRoad(BlockState state, BlockRotation rotation) {
    return Road.super.rotateRoad(state, rotation).with(FACING, rotation.rotate(state.get(FACING)));
  }

  @Override
  default BlockState withPlacementState(BlockState state, ItemPlacementContext ctx) {
    final Direction rotation = ctx.getHorizontalPlayerFacing();
    return Road.super
        .withPlacementState(state, ctx)
        .with(
            FACING,
            ctx.getPlayer() != null && ctx.getPlayer().isSneaking()
                ? rotation.getOpposite()
                : rotation);
  }

  @Override
  default void appendRoadTooltip(
      ItemStack stack, TooltipContext context, List<Text> tooltip, TooltipType options) {
    Road.super.appendRoadTooltip(stack, context, tooltip, options);
    tooltip.add(
        TextBridge.translatable("block.mishanguc.tooltip.road_with_joint_line.1")
            .formatted(Formatting.GRAY));
    tooltip.add(
        TextBridge.translatable("block.mishanguc.tooltip.road_with_joint_line.2")
            .formatted(Formatting.GRAY));
  }

  class Impl extends AbstractRoadBlock implements RoadWithJointLine {
    public final LineColor lineColorSide;
    public final LineType lineTypeSide;
    private final String lineTop;
    protected final String lineSide;
    protected final String lineSide2;

    public static final MapCodec<Impl> CODEC = RecordCodecBuilder.mapCodec(i -> i.group(createSettingsCodec(), lineColorFieldCodec(), LineColor.CODEC.fieldOf("line_color_side").forGetter(b -> b.lineColorSide), lineTypeFieldCodec(), LineType.CODEC.fieldOf("line_type_side").forGetter(b -> b.lineTypeSide)).apply(i, (settings, lineColor, lineColorSide, lineType, lineTypeSide) -> new Impl(settings, lineColor, lineColorSide, lineType, lineTypeSide, null)));

    public Impl(
        Settings settings,
        LineColor lineColor,
        LineColor lineColorSide,
        LineType lineType,
        LineType lineTypeSide, String lineTop) {
      super(settings, lineColor, lineType);
      this.lineColorSide = lineColorSide;
      this.lineTypeSide = lineTypeSide;
      this.lineTop = lineTop;
      lineSide = MishangUtils.composeStraightLineTexture(this.lineColor, this.lineType);
      lineSide2 = MishangUtils.composeStraightLineTexture(this.lineColorSide, this.lineTypeSide);
    }

    @Override
    public LineColor getLineColor(BlockState blockState, Direction direction) {
      final Direction facing = blockState.get(FACING);
      if (facing == direction) {
        return lineColorSide;
      } else if (facing == direction.getOpposite()) {
        return LineColor.NONE;
      } else {
        return lineColor;
      }
    }

    @Override
    public void appendDescriptionTooltip(List<Text> tooltip, TooltipContext options) {
      tooltip.add(TextBridge.translatable("lineType.joint.composed", lineColor.getName(), lineType.getName(), lineColorSide.getName(), lineTypeSide.getName()).formatted(Formatting.BLUE));
    }

    @Override
    public LineType getLineType(BlockState blockState, Direction direction) {
      final Direction facing = blockState.get(FACING);
      if (facing == direction) {
        return lineTypeSide;
      } else if (facing == direction.getOpposite()) {
        return LineType.NORMAL;
      } else {
        return lineType;
      }
    }

    @Override
    protected <B extends Block & Road> void registerBaseOrSlabModels(B road, BlockStateModelGenerator blockStateModelGenerator) {
      final FasterTextureMap textures = new FasterTextureMap().base("asphalt").lineSide(lineSide).lineSide2(lineSide2).lineTop(lineTop);
      final Identifier modelId = road.uploadModel("_with_joint_line", textures, blockStateModelGenerator, MishangucTextureKeys.BASE, MishangucTextureKeys.LINE_SIDE, MishangucTextureKeys.LINE_TOP);
      blockStateModelGenerator.blockStateCollector.accept(road.composeState(VariantsBlockStateSupplier.create(road, BlockStateVariant.create().put(VariantSettings.MODEL, modelId)).coordinate(BlockStateVariantMap.create(FACING).register(direction -> BlockStateVariant.create().put(MishangUtils.DIRECTION_Y_VARIANT, direction)))));
    }

    @Override
    protected MapCodec<? extends Impl> getCodec() {
      return CODEC;
    }

    @Override
    public CraftingRecipeJsonBuilder getPaintingRecipe(Block base, Block self) {
      final String pattern1 = switch (lineTypeSide) {
        case NORMAL -> " a ";
        case DOUBLE -> "a a";
        case THICK -> "aaa";
      };
      Block base2 = RoadBlocks.getRoadBlockWithLine(lineColor, lineType);
      if (base instanceof SlabBlock) {
        base2 = ((AbstractRoadBlock) base2).getRoadSlab();
      }
      final ShapedRecipeJsonBuilder recipe = ShapedRecipeJsonBuilder.create(RecipeCategory.BUILDING_BLOCKS, self, 3)
          .pattern(pattern1)
          .pattern("XXX")
          .input('a', lineColorSide.getIngredient())
          .input('X', base2)
          .criterion("has_" + lineColorSide.asString() + "_paint", RecipeProvider.conditionsFromTag(lineColorSide.getIngredient()))
          .criterion(RecipeProvider.hasItem(base2), RecipeProvider.conditionsFromItem(base2));
      if (lineColorSide != lineColor) {
        recipe.criterion("has_" + lineColor.asString() + "_paint", RecipeProvider.conditionsFromTag(lineColor.getIngredient()));
      }
      return recipe;
    }
  }

}
