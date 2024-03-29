package pers.solid.mishang.uc.block;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.data.client.BlockStateModelGenerator;
import net.minecraft.data.client.BlockStateSupplier;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemStack;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.DirectionProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.text.Text;
import net.minecraft.util.BlockMirror;
import net.minecraft.util.BlockRotation;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Direction;
import net.minecraft.world.BlockView;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import pers.solid.brrp.v1.api.RuntimeResourcePack;
import pers.solid.brrp.v1.model.ModelJsonBuilder;
import pers.solid.mishang.uc.MishangUtils;
import pers.solid.mishang.uc.arrp.BRRPHelper;
import pers.solid.mishang.uc.arrp.FasterJTextures;
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
  default void appendRoadTooltip(ItemStack stack, @Nullable BlockView world, List<Text> tooltip, TooltipContext options) {
    Road.super.appendRoadTooltip(stack, world, tooltip, options);
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

  @Environment(EnvType.CLIENT)
  @Override
  @NotNull
  default BlockStateSupplier getBlockStates() {
    return BlockStateModelGenerator.createSingletonBlockState((Block) this, getBlockModelId()).coordinate(BlockStateModelGenerator.createSouthDefaultHorizontalRotationStates());
  }

  class ImplWithTwoLayerTexture extends AbstractRoadBlock implements RoadWithTwoBevelAngleLines {

    public ImplWithTwoLayerTexture(Settings settings, LineColor lineColor, LineType lineType) {
      super(settings, lineColor, lineType);
      setDefaultState(getDefaultState().with(FACING, Direction.SOUTH));
    }

    @Environment(EnvType.CLIENT)
    @Override
    public @NotNull ModelJsonBuilder getBlockModel() {
      return ModelJsonBuilder.create(new Identifier("mishanguc:block/road_with_bi_angle_line"))
          .setTextures(new FasterJTextures()
              .base("asphalt")
              .lineTop(MishangUtils.composeAngleLineTexture(lineColor, lineType, true))
              .lineSide(MishangUtils.composeStraightLineTexture(lineColor, lineType)));
    }

    @Environment(EnvType.CLIENT)
    @Override
    public void writeBlockModel(RuntimeResourcePack pack) {
      BRRPHelper.addModelWithSlab(pack, this);
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
  }

  class ImplWithThreeLayerTexture extends AbstractRoadBlock implements RoadWithTwoBevelAngleLines {

    public ImplWithThreeLayerTexture(Settings settings, LineColor lineColor, LineType lineType) {
      super(settings, lineColor, lineType);
      setDefaultState(getDefaultState().with(FACING, Direction.SOUTH));
    }

    @Environment(EnvType.CLIENT)
    @Override
    public @NotNull ModelJsonBuilder getBlockModel() {
      return ModelJsonBuilder.create(new Identifier("mishanguc:block/road_with_straight_and_bi_angle_line"))
          .setTextures(new FasterJTextures()
              .base("asphalt")
              .lineTop(MishangUtils.composeStraightLineTexture(lineColor, lineType))
              .lineSide(MishangUtils.composeStraightLineTexture(lineColor, lineType))
              .varP("line_top2", MishangUtils.composeAngleLineTexture(lineColor, lineType, true)));
    }

    @Environment(EnvType.CLIENT)
    @Override
    public void writeBlockModel(RuntimeResourcePack pack) {
      BRRPHelper.addModelWithSlab(pack, this);
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
  }
}
