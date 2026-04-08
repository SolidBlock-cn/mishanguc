package pers.solid.mishang.uc.block;

import com.mojang.math.Quadrant;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.data.models.BlockModelGenerators;
import net.minecraft.client.data.models.ModelProvider;
import net.minecraft.client.data.models.blockstates.MultiVariantGenerator;
import net.minecraft.client.data.models.blockstates.PropertyDispatch;
import net.minecraft.client.data.models.model.TextureMapping;
import net.minecraft.client.renderer.block.dispatch.VariantMutator;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.resources.Identifier;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import pers.solid.mishang.uc.MishangUtils;

import java.util.Map;

/**
 * 类似于墙上的灯方块，但是是条状的，因此具有多一个属性。
 */
public class StripWallLightBlock extends WallLightBlock implements LightConnectable {
  public static final MapCodec<StripWallLightBlock> CODEC = RecordCodecBuilder.mapCodec(i -> i.group(Codec.STRING.fieldOf("light_color").forGetter(b -> b.lightColor), propertiesCodec()).apply(i, StripWallLightBlock::new));
  protected static final EnumProperty<StripType> STRIP_TYPE =
      EnumProperty.create("strip_type", StripType.class);
  private static final Map<Direction, VoxelShape> SHAPE_PER_DIRECTION_WHEN_HORIZONTAL =
      MishangUtils.createDirectionToShape(0, 0, 4, 16, 2, 12);
  private static final Map<Direction, VoxelShape> SHAPE_PER_DIRECTION_WHEN_VERTICAL =
      MishangUtils.createDirectionToShape(4, 0, 0, 12, 2, 16);

  public StripWallLightBlock(String lightColor, Properties settings) {
    super(lightColor, settings, false);
  }

  @Override
  public boolean isConnectedIn(BlockState blockState, Direction facing, Direction direction) {
    final StripType stripType = blockState.getValue(STRIP_TYPE);
    if (facing != blockState.getValue(FACING) || direction.getAxis() == facing.getAxis()) {
      return false;
    }
    return switch (stripType) {
      case VERTICAL -> facing.getAxis() == Direction.Axis.Y
          ? direction.getAxis() == Direction.Axis.Z
          : direction.getAxis() == Direction.Axis.Y;
      case HORIZONTAL -> facing.getAxis() == Direction.Axis.Y
          ? direction.getAxis() == Direction.Axis.X
          : direction.getAxis() != Direction.Axis.Y;
    };
  }

  @Override
  public BlockState rotate(BlockState state, Rotation rotation) {
    final BlockState rotate = super.rotate(state, rotation);
    if (rotate.getValue(FACING).getAxis().isVertical() && (rotation == Rotation.CLOCKWISE_90 || rotation == Rotation.COUNTERCLOCKWISE_90)) {
      return rotate.setValue(STRIP_TYPE, rotate.getValue(STRIP_TYPE).another());
    }
    return rotate;
  }

  @Override
  protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
    super.createBlockStateDefinition(builder);
    builder.add(STRIP_TYPE);
  }

  @Override
  public BlockState getStateForPlacement(BlockPlaceContext ctx) {
    final BlockState placementState = super.getStateForPlacement(ctx);
    if (placementState == null) {
      return null;
    }
    final Player player = ctx.getPlayer();
    return placementState.setValue(
        STRIP_TYPE,
        ctx.getClickedFace().getAxis() == Direction.Axis.Y
            ? (ctx.getHorizontalDirection().getAxis() == Direction.Axis.X
            ? StripType.HORIZONTAL
            : StripType.VERTICAL)
            : (player != null && player.isShiftKeyDown() ? StripType.VERTICAL : StripType.HORIZONTAL));
  }

  @Override
  public VoxelShape getShape(
      BlockState state, BlockGetter world, BlockPos pos, CollisionContext context) {
    return (state.getValue(STRIP_TYPE) == StripType.VERTICAL
        ? SHAPE_PER_DIRECTION_WHEN_VERTICAL
        : SHAPE_PER_DIRECTION_WHEN_HORIZONTAL)
        .get(state.getValue(FACING));
  }

  public enum StripType implements StringRepresentable {
    /**
     * 水平的，对于天花板上或地上的表示为东西方向。
     */
    HORIZONTAL,
    /**
     * 垂直的，对于天花板上或地上的表示为南北方向。
     */
    VERTICAL;

    @Override
    public String getSerializedName() {
      return switch (this) {
        case HORIZONTAL -> "horizontal";
        case VERTICAL -> "vertical";
      };
    }

    public StripType another() {
      return switch (this) {
        case HORIZONTAL -> VERTICAL;
        case VERTICAL -> HORIZONTAL;
      };
    }
  }

  @Override
  public boolean skipRendering(BlockState state, BlockState stateFrom, Direction direction) {
    return stateFrom.is(this) && ((LightConnectable) stateFrom.getBlock()).isConnectedIn(stateFrom, state.getValue(FACING), direction.getOpposite()) || super.skipRendering(state, stateFrom, direction);
  }


  @Environment(EnvType.CLIENT)
  @Override
  public void registerModels(ModelProvider modelProvider, BlockModelGenerators blockStateModelGenerator) {
    final var map = PropertyDispatch.initial(FACING, STRIP_TYPE);

    final TextureMapping textureMap = getTextureMap();
    final Identifier id = getModelType().create(this, textureMap, blockStateModelGenerator.modelOutput);
    final Identifier idVertical = getModelType("_vertical").create(this, textureMap, blockStateModelGenerator.modelOutput);

    map.select(Direction.UP, StripType.HORIZONTAL, BlockModelGenerators.plainVariant(id));
    map.select(Direction.UP, StripType.VERTICAL, BlockModelGenerators.plainVariant(idVertical));
    map.select(Direction.DOWN, StripType.HORIZONTAL, BlockModelGenerators.plainVariant(id).with(BlockModelGenerators.X_ROT_180));
    map.select(Direction.DOWN, StripType.VERTICAL, BlockModelGenerators.plainVariant(idVertical).with(BlockModelGenerators.X_ROT_180));
    for (Direction direction : Direction.Plane.HORIZONTAL) {
      final Quadrant axisRotation = switch (direction) {
        case WEST -> Quadrant.R90;
        case NORTH -> Quadrant.R180;
        case EAST -> Quadrant.R270;
        default -> Quadrant.R0;
      };
      map.select(direction, StripType.HORIZONTAL, BlockModelGenerators.plainVariant(id).with(BlockModelGenerators.X_ROT_270).with(VariantMutator.Y_ROT.withValue(axisRotation)));
      map.select(direction, StripType.VERTICAL, BlockModelGenerators.plainVariant(idVertical).with(BlockModelGenerators.X_ROT_270).with(VariantMutator.Y_ROT.withValue(axisRotation)));
    }
    blockStateModelGenerator.blockStateOutput.accept(MultiVariantGenerator.dispatch(this).with(map).with(BlockModelGenerators.UV_LOCK));
  }

  @Override
  protected MapCodec<? extends StripWallLightBlock> codec() {
    return CODEC;
  }
}
