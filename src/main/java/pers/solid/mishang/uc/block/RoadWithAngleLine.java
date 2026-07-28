package pers.solid.mishang.uc.block;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.ChatFormatting;
import net.minecraft.client.data.models.BlockModelGenerators;
import net.minecraft.client.data.models.blockstates.MultiVariantGenerator;
import net.minecraft.client.data.models.blockstates.PropertyDispatch;
import net.minecraft.client.renderer.block.model.VariantMutator;
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
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import pers.solid.mishang.uc.MishangUtils;
import pers.solid.mishang.uc.MishangucProperties;
import pers.solid.mishang.uc.data.FasterTextureMap;
import pers.solid.mishang.uc.data.MishangucTextureKeys;
import pers.solid.mishang.uc.util.*;

import java.util.List;

public interface RoadWithAngleLine extends Road {
  EnumProperty<HorizontalCornerDirection> FACING = MishangucProperties.HORIZONTAL_CORNER_FACING;

  @Override
  default void appendRoadProperties(StateDefinition.Builder<Block, BlockState> builder) {
    builder.add(FACING);
  }

  @Override
  default RoadConnectionState getConnectionStateOf(BlockState state, Direction direction) {
    return RoadConnectionState.of(
        state.getValue(FACING).hasDirection(direction),
        getLineColor(state, direction),
        isBevel() ? EightHorizontalDirection.of(state.getValue(FACING).mirror(direction)) : EightHorizontalDirection.of(direction),
        getLineType(state, direction),
        null);
  }

  @Override
  default BlockState mirrorRoad(BlockState state, Mirror mirror) {
    return state.setValue(FACING, state.getValue(FACING).mirror(mirror));
  }

  @Override
  default BlockState rotateRoad(BlockState state, Rotation rotation) {
    HorizontalCornerDirection facing = state.getValue(FACING);
    return state.setValue(FACING, facing.rotate(rotation));
  }

  @Override
  default BlockState withPlacementState(BlockState state, BlockPlaceContext ctx) {
    if (state == null) {
      return null;
    }
    final HorizontalCornerDirection rotation =
        HorizontalCornerDirection.fromRotation(ctx.getRotation());
    return state.setValue(
        FACING,
        ctx.getPlayer() != null && ctx.getPlayer().isShiftKeyDown()
            ? rotation.getOpposite()
            : rotation);
  }

  @Override
  default void appendRoadTooltip(
      ItemStack stack, TooltipContext context, List<Component> tooltip, TooltipFlag options) {
    Road.super.appendRoadTooltip(stack, context, tooltip, options);
    tooltip.add(
        TextBridge.translatable("block.mishanguc.tooltip.road_with_angle_line.1")
            .withStyle(ChatFormatting.GRAY));
    tooltip.add(
        TextBridge.translatable("block.mishanguc.tooltip.road_with_angle_line.2")
            .withStyle(ChatFormatting.GRAY));
  }

  boolean isBevel();

  static <B extends RoadWithAngleLine> RecordCodecBuilder<B, Boolean> isBevelCodec() {
    return Codec.BOOL.fieldOf("is_bevel").forGetter(RoadWithAngleLine::isBevel);
  }

  class Impl extends AbstractRoadBlock implements RoadWithAngleLine {
    public static final MapCodec<Impl> CODEC = RecordCodecBuilder.mapCodec(i -> i.group(propertiesCodec(), lineColorFieldCodec(), lineTypeFieldCodec(), isBevelCodec()).apply(i, (settings, lineColor, lineType, isBevel) -> new Impl(settings, lineColor, lineType, null, isBevel, null)));


    private final boolean isBevel;
    protected final String lineSide;
    protected final String lineTop;

    public Impl(Properties settings, LineColor lineColor, LineType lineType, boolean isBevel, String lineTop) {
      this(settings, lineColor, lineType, MishangUtils.composeStraightLineTexture(lineColor, lineType), isBevel, lineTop);
    }

    public Impl(Properties settings, LineColor lineColor, LineType lineType, String lineSide, boolean isBevel, String lineTop) {
      super(settings, lineColor, lineType);
      this.isBevel = isBevel;
      this.lineSide = lineSide;
      this.lineTop = lineTop;
    }

    @Override
    public boolean isBevel() {
      return isBevel;
    }

    @Environment(EnvType.CLIENT)
    @Override
    protected <B extends Block & Road> void registerBaseOrSlabModels(B road, BlockModelGenerators blockStateModelGenerator) {
      final FasterTextureMap textures = new FasterTextureMap().base("asphalt").lineSide(lineSide).lineTop(lineTop);
      final Identifier modelId = road.uploadModel("_with_angle_line", textures, blockStateModelGenerator, MishangucTextureKeys.BASE, MishangucTextureKeys.LINE_SIDE, MishangucTextureKeys.LINE_TOP);
      blockStateModelGenerator.blockStateOutput.accept(road.composeState(MultiVariantGenerator.dispatch(road, BlockModelGenerators.plainVariant(modelId)).with(PropertyDispatch.modify(FACING).generate(direction -> VariantMutator.Y_ROT.withValue(direction.asAxisRotationCCW45())))));
    }

    @Override
    public void appendDescriptionTooltip(List<Component> tooltip, TooltipContext options) {
      if (isBevel()) {
        tooltip.add(TextBridge.translatable("lineType.angle.bevel").withStyle(ChatFormatting.BLUE));
      } else {
        tooltip.add(TextBridge.translatable("lineType.angle.right").withStyle(ChatFormatting.BLUE));
      }
      tooltip.add(TextBridge.translatable("lineType.angle.composed", lineColor.getName(), lineType.getName()).withStyle(ChatFormatting.BLUE));
    }

    @Override
    protected MapCodec<? extends Impl> codec() {
      return CODEC;
    }

    private static final String[] NORMAL_BEVEL_PATTERN = {
        " *X",
        "*X ",
        "X  "
    };
    private static final String[] DOUBLE_BEVEL_PATTERN = {
        " *X",
        "*X*",
        "X* "
    };
    private static final String[] THICK_BEVEL_PATTERN = {
        "**X",
        "*X*",
        "X**"
    };
    private static final String[] NORMAL_RIGHT_ANGLE_PATTERN = {
        " * ",
        "*XX",
        " X "
    };

    @Override
    public RecipeBuilder getPaintingRecipe(Block base, Block self, RecipeProvider recipeGenerator) {
      final String[] patterns = isBevel ? switch (lineType) {
        case NORMAL -> NORMAL_BEVEL_PATTERN;
        case DOUBLE -> DOUBLE_BEVEL_PATTERN;
        case THICK -> THICK_BEVEL_PATTERN;
      } : NORMAL_RIGHT_ANGLE_PATTERN;
      return recipeGenerator.shaped(RecipeCategory.BUILDING_BLOCKS, self, 3)
          .pattern(patterns[0])
          .pattern(patterns[1])
          .pattern(patterns[2])
          .define('*', lineColor.getIngredient())
          .define('X', base)
          .unlockedBy("*", recipeGenerator.has(lineColor.getIngredient()))
          .unlockedBy(RecipeProvider.getHasName(base), recipeGenerator.has(base));
    }
  }
}
