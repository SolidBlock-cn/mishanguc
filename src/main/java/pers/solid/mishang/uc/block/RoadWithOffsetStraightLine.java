package pers.solid.mishang.uc.block;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.ChatFormatting;
import net.minecraft.client.data.models.BlockModelGenerators;
import net.minecraft.client.data.models.blockstates.BlockModelDefinitionGenerator;
import net.minecraft.client.data.models.blockstates.MultiVariantGenerator;
import net.minecraft.client.renderer.block.dispatch.VariantMutator;
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
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import org.jetbrains.annotations.Contract;
import pers.solid.mishang.uc.data.FasterTextureMap;
import pers.solid.mishang.uc.data.MishangucTextureKeys;
import pers.solid.mishang.uc.mixin.BlockStateModelGeneratorAccessor;
import pers.solid.mishang.uc.util.*;

import java.util.List;

/**
 * 类似于 {@link RoadWithStraightLine}，不过道路的直线是偏移的，而非正中的。
 */
public interface RoadWithOffsetStraightLine extends Road {
  /**
   * 道路偏移直线所偏移的反方向。例如道路有一条南北方向的向西偏移的直线，则该道路朝向东。
   */
  EnumProperty<Direction> FACING = BlockStateProperties.HORIZONTAL_FACING;

  @Override
  default void appendRoadProperties(StateDefinition.Builder<Block, BlockState> builder) {
    Road.super.appendRoadProperties(builder);
    builder.add(FACING);
  }

  @Override
  default RoadConnectionState getConnectionStateOf(BlockState state, Direction direction) {
    return RoadConnectionState.or(
        Road.super.getConnectionStateOf(state, direction),
        RoadConnectionState.of(
            direction.getAxis() != state.getValue(FACING).getAxis(),
            getLineColor(state, direction),
            EightHorizontalDirection.of(direction),
            getLineType(state, direction),
            new LineOffset(state.getValue(FACING).getOpposite(), offsetLevel())));
  }

  @Override
  default BlockState mirrorRoad(BlockState state, Mirror mirror) {
    return Road.super.mirrorRoad(state, mirror).setValue(FACING, mirror.mirror(state.getValue(FACING)));
  }

  @Override
  default BlockState rotateRoad(BlockState state, Rotation rotation) {
    return Road.super.rotateRoad(state, rotation).setValue(FACING, rotation.rotate(state.getValue(FACING)));
  }

  @Override
  default BlockState withPlacementState(BlockState state, BlockPlaceContext ctx) {
    return Road.super
        .withPlacementState(state, ctx)
        .setValue(
            FACING,
            ctx.getPlayer() != null && ctx.getPlayer().isShiftKeyDown()
                ? ctx.getHorizontalDirection().getCounterClockWise()
                : ctx.getHorizontalDirection().getClockWise());
  }

  @Override
  default void appendRoadTooltip(
      ItemStack stack, TooltipContext context, List<Component> tooltip, TooltipFlag options) {
    Road.super.appendRoadTooltip(stack, context, tooltip, options);
    final int offsetLevel = offsetLevel();
    if (offsetLevel == 114514) {
      tooltip.add(Component.translatable("block.mishanguc.tooltip.road_with_white_and_yellow_double_line.1").withStyle(ChatFormatting.GRAY));
      tooltip.add(Component.translatable("block.mishanguc.tooltip.road_with_white_and_yellow_double_line.2").withStyle(ChatFormatting.GRAY));
      tooltip.add(Component.translatable("block.mishanguc.tooltip.road_with_white_and_yellow_double_line.3").withStyle(ChatFormatting.GRAY));
    } else {
      tooltip.add(
          Component.translatable("block.mishanguc.tooltip.road_with_offset_straight_line")
              .withStyle(ChatFormatting.GRAY));
    }
  }

  @Environment(EnvType.CLIENT)
  default BlockModelDefinitionGenerator createBlockStates(Block block, Identifier modelId) {
    return MultiVariantGenerator.dispatch(block, BlockModelGenerators.plainVariant(modelId).with(VariantMutator.UV_LOCK.withValue(false))).with(BlockStateModelGeneratorAccessor.getROTATION_TORCH()); // 检查一下是否确实为 east_default
  }

  @Contract(pure = true)
  int offsetLevel();

  class Impl extends AbstractRoadBlock implements RoadWithOffsetStraightLine {
    public static final MapCodec<Impl> CODEC = RecordCodecBuilder.mapCodec(i -> i.group(propertiesCodec(), lineColorFieldCodec(), lineTypeFieldCodec(), Codec.INT.fieldOf("offset_level").forGetter(b -> b.offsetLevel)).apply(i, (settings, lineColor, lineTYpe, offsetLevel) -> new Impl(settings, lineColor, lineTYpe, null, offsetLevel)));
    private final String lineTexture;
    private final int offsetLevel;

    public Impl(Properties settings, LineColor lineColor, LineType lineType, String lineTexture, int offsetLevel) {
      super(settings, lineColor, lineType);
      this.lineTexture = lineTexture;
      this.offsetLevel = offsetLevel;
    }

    @Override
    public void appendDescriptionTooltip(List<Component> tooltip, TooltipContext options) {
      if (offsetLevel == 0) {
        tooltip.add(Component.translatable("tbd")
            .withStyle(ChatFormatting.BLUE));
      } else {
        tooltip.add(Component.translatable("lineType.offsetStraight.composed", lineColor.getName(), lineType.getName()).withStyle(ChatFormatting.BLUE));
      }
    }

    @Environment(EnvType.CLIENT)
    @Override
    protected <B extends Block & Road> void registerBaseOrSlabModels(B road, BlockModelGenerators blockStateModelGenerator) {
      final FasterTextureMap textures = new FasterTextureMap().base("asphalt").lineSide(lineTexture).lineTop(lineTexture);
      final Identifier modelId = road.uploadModel("_with_straight_line", textures, blockStateModelGenerator, MishangucTextureKeys.BASE, MishangucTextureKeys.LINE_SIDE, MishangucTextureKeys.LINE_TOP);
      blockStateModelGenerator.blockStateOutput.accept(road.composeState(createBlockStates(road, modelId)));
    }

    @Override
    public RecipeBuilder getPaintingRecipe(Block base, Block self, RecipeProvider recipeGenerator) {
      if (offsetLevel == 114514) {
        return recipeGenerator.shaped(RecipeCategory.BUILDING_BLOCKS, self, 3)
            .pattern("w y")
            .pattern("XXX")
            .pattern("w y")
            .define('w', LineColor.WHITE.getIngredient())
            .define('y', LineColor.YELLOW.getIngredient())
            .define('X', base)
            .unlockedBy("has_white_paint", recipeGenerator.has(LineColor.WHITE.getIngredient()))
            .unlockedBy("has_yellow_paint", recipeGenerator.has(LineColor.YELLOW.getIngredient()))
            .unlockedBy(RecipeProvider.getHasName(base), recipeGenerator.has(base));
      } else {
        final String[] patterns = switch (offsetLevel) {
          case 2 -> new String[]{
              "*  ",
              "XXX",
              "*  "
          };
          case 1 -> new String[]{
              "*  ",
              "XXX",
              " * "
          };
          default -> throw new IllegalStateException("Unexpected value: " + offsetLevel);
        };
        return recipeGenerator.shaped(RecipeCategory.BUILDING_BLOCKS, self, 3)
            .pattern(patterns[0])
            .pattern(patterns[1])
            .pattern(patterns[2])
            .define('*', lineColor.getIngredient())
            .define('X', base)
            .unlockedBy("has_paint", recipeGenerator.has(lineColor.getIngredient()))
            .unlockedBy(RecipeProvider.getHasName(base), recipeGenerator.has(base));
      }
    }

    @Override
    public int offsetLevel() {
      return offsetLevel;
    }

    @Override
    protected MapCodec<? extends Impl> codec() {
      return CODEC;
    }
  }
}
