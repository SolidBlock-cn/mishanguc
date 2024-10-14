package pers.solid.mishang.uc.block;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.SlabBlock;
import net.minecraft.data.client.*;
import net.minecraft.data.server.recipe.CraftingRecipeJsonBuilder;
import net.minecraft.data.server.recipe.ShapedRecipeJsonBuilder;
import net.minecraft.item.Item.TooltipContext;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemStack;
import net.minecraft.item.tooltip.TooltipType;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.text.Text;
import net.minecraft.util.BlockMirror;
import net.minecraft.util.BlockRotation;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import pers.solid.mishang.uc.MishangUtils;
import pers.solid.mishang.uc.MishangucProperties;
import pers.solid.mishang.uc.data.FasterTextureMap;
import pers.solid.mishang.uc.blocks.RoadBlocks;
import pers.solid.mishang.uc.data.MishangucTextureKeys;
import pers.solid.mishang.uc.util.*;

import java.util.List;
import java.util.function.Supplier;

public interface RoadWithStraightAndAngleLine extends RoadWithAngleLine, RoadWithStraightLine {
  BooleanProperty BEVEL_TOP = MishangucProperties.BEVEL_TOP;

  @Override
  default void appendRoadProperties(StateManager.Builder<Block, BlockState> builder) {
    RoadWithAngleLine.super.appendRoadProperties(builder);
    RoadWithStraightLine.super.appendRoadProperties(builder);
  }

  @Override
  default RoadConnectionState getConnectionStateOf(BlockState state, Direction direction) {
    return RoadConnectionState.or(
        RoadWithStraightLine.super.getConnectionStateOf(state, direction),
        RoadWithAngleLine.super.getConnectionStateOf(state, direction));
  }

  @Override
  default BlockState mirrorRoad(BlockState state, BlockMirror mirror) {
    return RoadWithAngleLine.super.mirrorRoad(state, mirror);
  }

  @Override
  default BlockState rotateRoad(BlockState state, BlockRotation rotation) {
    return RoadWithStraightLine.super.rotateRoad(
        RoadWithAngleLine.super.rotateRoad(state, rotation), rotation);
  }

  @Override
  default BlockState withPlacementState(BlockState state, ItemPlacementContext ctx) {
    return RoadWithStraightLine.super.withPlacementState(
        RoadWithAngleLine.super.withPlacementState(state, ctx), ctx);
  }

  @Override
  default void appendRoadTooltip(
      ItemStack stack, TooltipContext context, List<Text> tooltip, TooltipType options) {
    RoadWithAngleLine.super.appendRoadTooltip(stack, context, tooltip, options);
    RoadWithStraightLine.super.appendRoadTooltip(stack, context, tooltip, options);
  }

  class Impl extends AbstractRoadBlock implements RoadWithStraightAndAngleLine {
    public static final MapCodec<RoadWithStraightAndAngleLine.Impl> CODEC = RecordCodecBuilder.mapCodec(i -> i.group(createSettingsCodec(), lineColorFieldCodec(), LineColor.CODEC.fieldOf("line_color_side").forGetter(b -> b.lineColorSide), lineTypeFieldCodec(), LineType.CODEC.fieldOf("line_type_side").forGetter(b -> b.lineTypeSide)).apply(i, RoadWithStraightAndAngleLine.Impl::new));
    /**
     * 用于构造函数，道路是否拥有 {@link #BEVEL_TOP} 属性。在构造函数调用之前就应该被计算。
     */
    private static boolean hasBevelTopProperty;
    private final LineColor lineColorSide;
    private final LineType lineTypeSide;

    public Impl(Settings settings, LineColor lineColor, LineColor lineColorSide, LineType lineType, LineType lineTypeSide) {
      super(settings, lineColor, ((Supplier<LineType>) () -> {
        hasBevelTopProperty = lineColor != lineColorSide;
        return lineType;
      }).get());
      this.lineColorSide = lineColorSide;
      this.lineTypeSide = lineTypeSide;
      if (hasBevelTopProperty) {
        setDefaultState(getDefaultState().with(BEVEL_TOP, false));
      }
    }

    public Impl(Settings settings, LineColor lineColor, LineType lineType) {
      this(settings, lineColor, lineColor, lineType, lineType);
    }

    @Override
    public boolean isBevel() {
      return true;
    }

    @Override
    public void appendRoadProperties(StateManager.Builder<Block, BlockState> builder) {
      RoadWithStraightAndAngleLine.super.appendRoadProperties(builder);
      if (hasBevelTopProperty) {
        builder.add(BEVEL_TOP);
      }
    }

    @Override
    public @Nullable BlockState getPlacementState(ItemPlacementContext ctx) {
      final BlockState placementState = super.getPlacementState(ctx);
      if (placementState == null) return null;
      final Direction direction = placementState.get(FACING).getDirectionInAxis(placementState.get(AXIS));
      final BlockPos blockPos = ctx.getBlockPos();
      final BlockPos neighborPos = blockPos.offset(direction);
      final World world = ctx.getWorld();
      return getStateForNeighborUpdate(placementState, direction, world.getBlockState(neighborPos), world, blockPos, neighborPos);
    }

    @Override
    public BlockState getStateForNeighborUpdate(BlockState state, Direction direction, BlockState neighborState, WorldAccess world, BlockPos pos, BlockPos neighborPos) {
      BlockState stateForNeighborUpdate = super.getStateForNeighborUpdate(state, direction, neighborState, world, pos, neighborPos);
      if (stateForNeighborUpdate.contains(BEVEL_TOP) && stateForNeighborUpdate.get(AXIS).test(direction) && stateForNeighborUpdate.get(FACING).hasDirection(direction)) {
        // 如果连接的那个方块在连接部分的道路标线与当前道路的斜线部分颜色一致，那么 bevel_top = true。
        final Block neighborBlock = neighborState.getBlock();
        final boolean bevelTop = neighborBlock instanceof Road road && road.getLineColor(neighborState, direction.getOpposite()) == lineColorSide;
        if (bevelTop) {
          return stateForNeighborUpdate.with(BEVEL_TOP, true);
        } else {
          final BlockPos up = neighborPos.up();
          final BlockState upState = world.getBlockState(up);
          if (upState.getBlock() instanceof Road road && road.getLineColor(upState, direction.getOpposite()) == lineColorSide) {
            return stateForNeighborUpdate.with(BEVEL_TOP, true);
          }
          final BlockPos down = neighborPos.down();
          final BlockState downState = world.getBlockState(down);
          if (downState.getBlock() instanceof Road road && road.getLineColor(downState, direction.getOpposite()) == lineColorSide) {
            return stateForNeighborUpdate.with(BEVEL_TOP, true);
          }
        }
        return stateForNeighborUpdate.with(BEVEL_TOP, false);
      }
      return stateForNeighborUpdate;
    }

    @Override
    protected <B extends Block & Road> void registerBaseOrSlabModels(B road, BlockStateModelGenerator blockStateModelGenerator) {
      final String lineTopStraight = MishangUtils.composeStraightLineTexture(lineColor, lineType);
      final String lineTopAngle = MishangUtils.composeAngleLineTexture(lineColorSide, LineType.NORMAL, true);
      final String lineSide = lineTopStraight;
      final String lineSide2 = MishangUtils.composeStraightLineTexture(lineColorSide, lineTypeSide);
      final FasterTextureMap textures = new FasterTextureMap()
          .base("asphalt")
          .lineTop(lineTopAngle)
          .lineTop2(lineTopStraight)
          .lineSide(lineSide)
          .lineSide2(lineSide2);
      final Identifier modelId = road.uploadModel("_with_straight_and_angle_line", textures, blockStateModelGenerator, MishangucTextureKeys.BASE, MishangucTextureKeys.LINE_TOP, MishangucTextureKeys.LINE_TOP2, MishangucTextureKeys.LINE_SIDE, MishangucTextureKeys.LINE_SIDE2);
      final Identifier mirroredModelId = road.uploadModel("_with_straight_and_angle_line_mirrored", "_mirrored", textures, blockStateModelGenerator, MishangucTextureKeys.BASE, MishangucTextureKeys.LINE_TOP, MishangucTextureKeys.LINE_TOP2, MishangucTextureKeys.LINE_SIDE, MishangucTextureKeys.LINE_SIDE2);

      final Identifier beveledTopModelId, beveledTopMirroredModelId;
      if (stateManager.getProperties().contains(BEVEL_TOP)) {
        TextureMap textures2 = new FasterTextureMap()
            .base("asphalt")
            .lineTop(lineTopStraight)
            .lineTop2(lineTopAngle)
            .lineSide(lineSide)
            .lineSide2(lineSide2)
            .varP(MishangucTextureKeys.LINE_SIDE3, lineSide2);

        beveledTopModelId = road.uploadModel("_with_straight_and_angle_line", "_bevel_top", textures2, blockStateModelGenerator, MishangucTextureKeys.BASE, MishangucTextureKeys.LINE_TOP, MishangucTextureKeys.LINE_TOP2, MishangucTextureKeys.LINE_SIDE, MishangucTextureKeys.LINE_SIDE2, MishangucTextureKeys.LINE_SIDE3);
        beveledTopMirroredModelId = road.uploadModel("_with_straight_and_angle_line_mirrored", "_bevel_top_mirrored", textures2, blockStateModelGenerator, MishangucTextureKeys.BASE, MishangucTextureKeys.LINE_TOP, MishangucTextureKeys.LINE_TOP2, MishangucTextureKeys.LINE_SIDE, MishangucTextureKeys.LINE_SIDE2, MishangucTextureKeys.LINE_SIDE3);
      } else {
        beveledTopModelId = beveledTopMirroredModelId = null;
      }

      final boolean hasBevelTop = lineColor != lineColorSide;
      final BlockStateVariantMap.DoubleProperty<Direction.Axis, HorizontalCornerDirection> map1 = hasBevelTop ? null : BlockStateVariantMap.create(AXIS, FACING);
      final BlockStateVariantMap.TripleProperty<Direction.Axis, HorizontalCornerDirection, Boolean> map2 = hasBevelTop ? BlockStateVariantMap.create(AXIS, FACING, BEVEL_TOP) : null;
      for (Direction direction : Direction.Type.HORIZONTAL) {
        final int rotation = (int) direction.asRotation();
        final Direction.Axis axis = direction.getAxis();
        final @NotNull HorizontalCornerDirection facing1 = HorizontalCornerDirection.fromDirections(direction, direction.rotateYClockwise());
        final @NotNull HorizontalCornerDirection facing2 = HorizontalCornerDirection.fromDirections(direction, direction.rotateYCounterclockwise());
        if (hasBevelTop) {
          map2.register(axis, facing1, false,
              BlockStateVariant.create().put(VariantSettings.MODEL, modelId).put(MishangUtils.INT_Y_VARIANT, rotation));
          map2.register(axis, facing1, true,
              BlockStateVariant.create().put(VariantSettings.MODEL, beveledTopModelId).put(MishangUtils.INT_Y_VARIANT, rotation));
          map2.register(axis, facing2, false,
              BlockStateVariant.create().put(VariantSettings.MODEL, mirroredModelId).put(MishangUtils.INT_Y_VARIANT, rotation - 90));
          map2.register(axis, facing2, true,
              BlockStateVariant.create().put(VariantSettings.MODEL, beveledTopMirroredModelId).put(MishangUtils.INT_Y_VARIANT, rotation - 90));
        } else {
          map1.register(
              axis, facing1,
              BlockStateVariant.create().put(VariantSettings.MODEL, modelId).put(MishangUtils.INT_Y_VARIANT, rotation));
          map1.register(
              axis, facing2,
              BlockStateVariant.create().put(VariantSettings.MODEL, mirroredModelId).put(MishangUtils.INT_Y_VARIANT, rotation - 90));
        }
      }
      blockStateModelGenerator.blockStateCollector.accept(road.composeState(VariantsBlockStateSupplier.create(road).coordinate(hasBevelTop ? map2 : map1)));
    }

    @Override
    public LineColor getLineColor(BlockState state, Direction direction) {
      if (state.get(FACING).hasDirection(direction) && (state.contains(BEVEL_TOP) && state.get(BEVEL_TOP) || !state.get(AXIS).test(direction))) {
        return lineColorSide;
      }
      return super.getLineColor(state, direction);
    }

    @Override
    public LineType getLineType(BlockState state, Direction direction) {
      if (state.get(FACING).hasDirection(direction) && !state.get(AXIS).test(direction)) {
        return lineTypeSide;
      }
      return super.getLineType(state, direction);
    }

    @Override
    public void appendDescriptionTooltip(List<Text> tooltip, TooltipContext options) {
      if (lineColor == lineColorSide && lineType == lineTypeSide) {
        tooltip.add(TextBridge.translatable("lineType.straightAndAngle.same", lineColor.getName(), lineType.getName()).formatted(Formatting.BLUE));
      } else {
        tooltip.add(TextBridge.translatable("lineType.straightAndAngle.straight", lineColor.getName(), lineType.getName()).formatted(Formatting.BLUE));
        tooltip.add(TextBridge.translatable("lineType.straightAndAngle.bevel", lineColorSide.getName(), lineTypeSide.getName()).formatted(Formatting.BLUE));
      }
    }

    @Override
    protected MapCodec<? extends RoadWithStraightAndAngleLine.Impl> getCodec() {
      return CODEC;
    }

    @Override
    public CraftingRecipeJsonBuilder getPaintingRecipe(Block base, Block self) {
      if (lineTypeSide != LineType.NORMAL) {
        throw new UnsupportedOperationException();
      }
      Block base2 = RoadBlocks.getRoadBlockWithLine(lineColor, lineType);
      if (base instanceof SlabBlock) {
        base2 = ((AbstractRoadBlock) base2).getRoadSlab();
      }
      return ShapedRecipeJsonBuilder.create(getRecipeCategory(), self, 3)
          .pattern(" *X")
          .pattern("*X ")
          .pattern("X  ")
          .input('*', lineColorSide.getIngredient())
          .input('X', base2)
          .criterionFromItemTag("has_paint", lineColorSide.getIngredient())
          .criterionFromItem(base2)
          .setCustomRecipeCategory("roads");
    }
  }
}
