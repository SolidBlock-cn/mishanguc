package pers.solid.mishang.uc.block;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.SlabBlock;
import net.minecraft.client.item.TooltipType;
import net.minecraft.data.client.*;
import net.minecraft.data.server.recipe.CraftingRecipeJsonBuilder;
import net.minecraft.data.server.recipe.ShapedRecipeJsonBuilder;
import net.minecraft.item.Item.TooltipContext;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemStack;
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
import pers.solid.brrp.v1.api.RuntimeResourcePack;
import pers.solid.brrp.v1.model.ModelJsonBuilder;
import pers.solid.mishang.uc.MishangUtils;
import pers.solid.mishang.uc.MishangucProperties;
import pers.solid.mishang.uc.arrp.BRRPHelper;
import pers.solid.mishang.uc.arrp.FasterJTextures;
import pers.solid.mishang.uc.blocks.RoadBlocks;
import pers.solid.mishang.uc.util.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
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

    @Environment(EnvType.CLIENT)
    @Override
    public @NotNull BlockStateSupplier getBlockStates() {
      final Identifier blockModelId = getBlockModelId();
      final Identifier mirroredBlockModelId = blockModelId.brrp_suffixed("_mirrored");
      final Identifier bevelTopBlockModelId, bevelTopMirroredBlockModelId;
      final boolean hasBevelTop = lineColor != lineColorSide;
      if (hasBevelTop) {
        bevelTopBlockModelId = blockModelId.brrp_suffixed("_bevel-top");
        bevelTopMirroredBlockModelId = blockModelId.brrp_suffixed("_bevel-top_mirrored");
      } else {
        bevelTopBlockModelId = bevelTopMirroredBlockModelId = null;
      }
      final BlockStateVariantMap.DoubleProperty<Direction.Axis, HorizontalCornerDirection> map1 = hasBevelTop ? null : BlockStateVariantMap.create(AXIS, FACING);
      final BlockStateVariantMap.TripleProperty<Direction.Axis, HorizontalCornerDirection, Boolean> map2 = hasBevelTop ? BlockStateVariantMap.create(AXIS, FACING, BEVEL_TOP) : null;
      for (Direction direction : Direction.Type.HORIZONTAL) {
        final int rotation = (int) direction.asRotation();
        final Direction.Axis axis = direction.getAxis();
        final @NotNull HorizontalCornerDirection facing1 = HorizontalCornerDirection.fromDirections(direction, direction.rotateYClockwise());
        final @NotNull HorizontalCornerDirection facing2 = HorizontalCornerDirection.fromDirections(direction, direction.rotateYCounterclockwise());
        if (hasBevelTop) {
          map2.register(axis, facing1, false,
              BlockStateVariant.create().put(VariantSettings.MODEL, blockModelId).put(MishangUtils.INT_Y_VARIANT, rotation));
          map2.register(axis, facing1, true,
              BlockStateVariant.create().put(VariantSettings.MODEL, bevelTopBlockModelId).put(MishangUtils.INT_Y_VARIANT, rotation));
          map2.register(axis, facing2, false,
              BlockStateVariant.create().put(VariantSettings.MODEL, mirroredBlockModelId).put(MishangUtils.INT_Y_VARIANT, rotation - 90));
          map2.register(axis, facing2, true,
              BlockStateVariant.create().put(VariantSettings.MODEL, bevelTopMirroredBlockModelId).put(MishangUtils.INT_Y_VARIANT, rotation - 90));
        } else {
          map1.register(
              axis, facing1,
              BlockStateVariant.create().put(VariantSettings.MODEL, blockModelId).put(MishangUtils.INT_Y_VARIANT, rotation));
          map1.register(
              axis, facing2,
              BlockStateVariant.create().put(VariantSettings.MODEL, mirroredBlockModelId).put(MishangUtils.INT_Y_VARIANT, rotation - 90));
        }
      }
      return VariantsBlockStateSupplier.create(this).coordinate(hasBevelTop ? map2 : map1);
    }

    @Environment(EnvType.CLIENT)
    @Override
    public @NotNull ModelJsonBuilder getBlockModel() {
      final ModelJsonBuilder model = ModelJsonBuilder.create(new Identifier("mishanguc:block/road_with_straight_and_angle_line"));
      final String lineTopStraight = MishangUtils.composeStraightLineTexture(lineColor, lineType);
      final String lineTopAngle = MishangUtils.composeAngleLineTexture(lineColorSide, LineType.NORMAL, true);
      final String lineSide = lineTopStraight;
      final String lineSide2 = MishangUtils.composeStraightLineTexture(lineColorSide, lineTypeSide);
      model.setTextures(FasterJTextures.ofP(
              "base", "asphalt",
              "line_top_layer1", lineTopAngle,
              "line_top_layer2", lineTopStraight)
          .lineSide(lineSide)
          .lineSide2(lineSide2));
      return model;
    }

    @Environment(EnvType.CLIENT)
    @Override
    public void writeBlockModel(RuntimeResourcePack pack) {
      // 此方法会同时写入 slab 的模型。
      final ModelJsonBuilder model = getBlockModel();
      final Identifier blockModelId = getBlockModelId();
      final AbstractRoadSlabBlock slabBlock = getRoadSlab();
      final Identifier slabModelId = slabBlock == null ? null : slabBlock.getBlockModelId();
      BRRPHelper.addModelWithSlab(pack, model, blockModelId, slabModelId);
      final Map<String, String> textures = model.textures;
      BRRPHelper.addModelWithSlab(pack, model.withParent(model.parentId.brrp_suffixed("_mirrored")), blockModelId.brrp_suffixed("_mirrored"), slabModelId == null ? null : slabModelId.brrp_suffixed("_mirrored"));

      if (stateManager.getProperties().contains(BEVEL_TOP)) {
        Map<String, String> textures2 = new HashMap<>(textures);
        final String line_top_layer2 = textures.get("line_top_layer2");
        final String line_top_layer1 = textures.get("line_top_layer1");
        textures2.put("line_top_layer1", line_top_layer2);
        textures2.put("line_top_layer2", line_top_layer1);
        textures2.put("line_side3", textures.get("line_side2"));

        BRRPHelper.addModelWithSlab(pack, model.clone().setTextures(textures2), blockModelId.brrp_suffixed("_bevel-top"), slabModelId == null ? null : slabModelId.brrp_suffixed("_bevel-top"));
        BRRPHelper.addModelWithSlab(pack, model.clone().setTextures(textures2).parent(model.parentId.brrp_suffixed("_mirrored")), blockModelId.brrp_suffixed("_bevel-top_mirrored"), slabModelId == null ? null : slabModelId.brrp_suffixed("_bevel-top_mirrored"));
      }
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
