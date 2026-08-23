package pers.solid.mishang.uc.block;

import com.google.common.collect.ImmutableMap;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

import java.util.Map;

/**
 * 柱形灯块。
 */
public class ColumnWallLightBlock extends WallLightBlock {
  public static final MapCodec<ColumnWallLightBlock> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(Codec.STRING.fieldOf("light_color").forGetter(b -> b.lightColor), propertiesCodec(), Codec.INT.fieldOf("size_type").forGetter(block -> block.sizeType)).apply(instance, ColumnWallLightBlock::new));
  private final int sizeType;
  public static final Map<Direction.Axis, VoxelShape> SHAPES7 = createColumnShapes(7);
  public static final Map<Direction.Axis, VoxelShape> SHAPES6 = createColumnShapes(6);
  public static final Map<Direction.Axis, VoxelShape> SHAPES5 = createColumnShapes(5);
  public static final Map<Direction.Axis, VoxelShape> SHAPES4 = createColumnShapes(4);

  public ColumnWallLightBlock(String lightColor, Properties settings, int sizeType) {
    super(lightColor, settings, sizeType == 2);
    this.sizeType = sizeType;
  }

  @Override
  public VoxelShape getShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext context) {
    return (sizeType >= 2 ? SHAPES4 : sizeType == 1 ? SHAPES5 : SHAPES6).get(state.getValue(FACING).getAxis());
  }

  @Override
  public VoxelShape getCollisionShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext context) {
    return (sizeType >= 2 ? SHAPES5 : sizeType == 1 ? SHAPES6 : SHAPES7).get(state.getValue(FACING).getAxis());
  }

  private static Map<Direction.Axis, VoxelShape> createColumnShapes(int min) {
    return ImmutableMap.of(
        Direction.Axis.X, box(0, min, min, 16, 16 - min, 16 - min),
        Direction.Axis.Y, box(min, 0, min, 16 - min, 16, 16 - min),
        Direction.Axis.Z, box(min, min, 0, 16 - min, 16 - min, 16)
    );
  }

  @Override
  protected MapCodec<? extends ColumnWallLightBlock> codec() {
    return CODEC;
  }

  @Override
  protected boolean isPathfindable(BlockState state, PathComputationType type) {
    return false;
  }
}
