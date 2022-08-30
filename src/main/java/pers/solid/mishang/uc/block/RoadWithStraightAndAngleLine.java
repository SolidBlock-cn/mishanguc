package pers.solid.mishang.uc.block;

import net.devtech.arrp.api.RuntimeResourcePack;
import net.devtech.arrp.json.blockstate.JBlockModel;
import net.devtech.arrp.json.blockstate.JBlockStates;
import net.devtech.arrp.json.blockstate.JVariants;
import net.devtech.arrp.json.models.JModel;
import net.devtech.arrp.json.models.JTextures;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.client.item.TooltipContext;
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
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import pers.solid.mishang.uc.MishangUtils;
import pers.solid.mishang.uc.MishangucProperties;
import pers.solid.mishang.uc.arrp.BRRPHelper;
import pers.solid.mishang.uc.arrp.FasterJTextures;
import pers.solid.mishang.uc.blocks.RoadSlabBlocks;
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
    return RoadWithStraightLine.super.mirrorRoad(
        RoadWithAngleLine.super.mirrorRoad(state, mirror), mirror);
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
      ItemStack stack, @Nullable BlockView world, List<Text> tooltip, TooltipContext options) {
    RoadWithAngleLine.super.appendRoadTooltip(stack, world, tooltip, options);
    RoadWithStraightLine.super.appendRoadTooltip(stack, world, tooltip, options);
  }

  class Impl extends AbstractRoadBlock implements RoadWithStraightAndAngleLine {
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
        setDefaultState(stateManager.getDefaultState().with(BEVEL_TOP, false));
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
    public @NotNull JBlockStates getBlockStates() {
      final JVariants variants = new JVariants();
      final Identifier blockModelId = getBlockModelId();
      final Identifier mirroredBlockModelId = blockModelId.brrp_append("_mirrored");
      final Identifier bevelTopBlockModelId, bevelTopMirroredBlockModelId;
      final boolean hasBevelTop = lineColor != lineColorSide;
      if (hasBevelTop) {
        bevelTopBlockModelId = blockModelId.brrp_append("_bevel-top");
        bevelTopMirroredBlockModelId = blockModelId.brrp_append("_bevel-top_mirrored");
      } else {
        bevelTopBlockModelId = bevelTopMirroredBlockModelId = null;
      }
      for (Direction direction : Direction.Type.HORIZONTAL) {
        final int rotation = (int) direction.asRotation();
        final String axis = direction.getAxis().asString();
        final String facing1 = HorizontalCornerDirection.fromDirections(direction, direction.rotateYClockwise()).asString();
        final String facing2 = HorizontalCornerDirection.fromDirections(direction, direction.rotateYCounterclockwise()).asString();
        if (hasBevelTop) {
          variants.addVariant(
              String.format("axis=%s,facing=%s,bevel_top=false", axis, facing1),
              new JBlockModel(blockModelId).y(rotation));
          variants.addVariant(
              String.format("axis=%s,facing=%s,bevel_top=true", axis, facing1),
              new JBlockModel(bevelTopBlockModelId).y(rotation));
          variants.addVariant(
              String.format("axis=%s,facing=%s,bevel_top=false", axis, facing2),
              new JBlockModel(mirroredBlockModelId).y(rotation - 90));
          variants.addVariant(
              String.format("axis=%s,facing=%s,bevel_top=true", axis, facing2),
              new JBlockModel(bevelTopMirroredBlockModelId).y(rotation - 90));
        } else {
          variants.addVariant(
              String.format("axis=%s,facing=%s", axis, facing1),
              new JBlockModel(blockModelId).y(rotation));
          variants.addVariant(
              String.format("axis=%s,facing=%s", axis, facing2),
              new JBlockModel(mirroredBlockModelId).y(rotation - 90));
        }
      }
      return JBlockStates.ofVariants(variants);
    }

    @Override
    public @NotNull JModel getBlockModel() {
      final JModel model = new JModel("mishanguc:block/road_with_straight_and_angle_line");
      final String lineTopStraight = MishangUtils.composeStraightLineTexture(lineColor, lineType);
      final String lineTopAngle = MishangUtils.composeAngleLineTexture(lineColorSide, true);
      final String lineSide = lineTopStraight;
      final String lineSide2 = MishangUtils.composeStraightLineTexture(lineColorSide, lineTypeSide);
      model.textures(FasterJTextures.ofP(
              "base", "asphalt",
              "line_top_layer1", lineTopAngle,
              "line_top_layer2", lineTopStraight)
          .lineSide(lineSide)
          .lineSide2(lineSide2));
      return model;
    }

    @Override
    public void writeBlockModel(RuntimeResourcePack pack) {
      // 此方法会同时写入 slab 的模型。
      final JModel model = getBlockModel();
      final Identifier blockModelId = getBlockModelId();
      final AbstractRoadSlabBlock slabBlock = RoadSlabBlocks.BLOCK_TO_SLABS.get(this);
      final Identifier slabModelId = slabBlock == null ? null : slabBlock.getBlockModelId();
      BRRPHelper.addModelWithSlab(pack, model, blockModelId, slabModelId);
      final JTextures textures = model.textures;
      BRRPHelper.addModelWithSlab(pack, model.clone().parent(model.parent + "_mirrored"), blockModelId.brrp_append("_mirrored"), slabModelId == null ? null : slabModelId.brrp_append("_mirrored"));

      if (stateManager.getProperties().contains(BEVEL_TOP)) {
        JTextures textures2 = textures.clone();
        final String line_top_layer2 = textures.get("line_top_layer2");
        final String line_top_layer1 = textures.get("line_top_layer1");
        textures2.put("line_top_layer1", line_top_layer2);
        textures2.put("line_top_layer2", line_top_layer1);
        textures2.put("line_side3", textures.get("line_side2"));

        BRRPHelper.addModelWithSlab(pack, model.clone().textures(textures2), blockModelId.brrp_append("_bevel-top"), slabModelId == null ? null : slabModelId.brrp_append("_bevel-top"));
        BRRPHelper.addModelWithSlab(pack, model.clone().textures(textures2).parent(model.parent + "_mirrored"), blockModelId.brrp_append("_bevel-top_mirrored"), slabModelId == null ? null : slabModelId.brrp_append("_bevel-top_mirrored"));
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
  }
}
