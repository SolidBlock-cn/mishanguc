package pers.solid.mishang.uc.block;

import com.mojang.serialization.DataResult;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.world.level.redstone.Orientation;
import org.jetbrains.annotations.Nullable;
import pers.solid.mishang.uc.util.RoadConnectionState;

import java.util.EnumMap;

public class RoadSlabBlockWithAutoLine extends SmartRoadSlabBlock<RoadBlockWithAutoLine>
    implements RoadWithAutoLine {
  public static final MapCodec<RoadSlabBlockWithAutoLine> CODEC = RecordCodecBuilder.mapCodec(i -> i.group(BuiltInRegistries.BLOCK.byNameCodec().flatXmap(block -> block instanceof RoadBlockWithAutoLine roadBlockWithAutoLine ? DataResult.success(roadBlockWithAutoLine) : DataResult.error(() -> block + " must be instance of " + RoadBlockWithAutoLine.class.getName()), DataResult::success).fieldOf("base_block").forGetter(b -> b.baseBlock), propertiesCodec()).apply(i, RoadSlabBlockWithAutoLine::new));

  public RoadSlabBlockWithAutoLine(RoadBlockWithAutoLine baseBlock, Properties settings) {
    super(baseBlock, settings);
  }

  @Override
  public BlockState makeState(
      EnumMap<Direction, RoadConnectionState> connectionStateMap, BlockState defaultState) {
    final BlockState baseState = baseBlock.makeState(connectionStateMap, defaultState);
    AbstractRoadBlock block = (AbstractRoadBlock) baseState.getBlock();
    BlockState state = block.getRoadSlab().defaultBlockState();
    for (Property<?> property : baseState.getProperties()) {
      if (state.hasProperty(property)) {
        state = sendProperty(baseState, state, property);
      }
    }
    return state
        .setValue(WATERLOGGED, defaultState.getValue(WATERLOGGED))
        .setValue(TYPE, defaultState.getValue(TYPE));
  }

  @Override
  protected void neighborChanged(BlockState state, Level world, BlockPos pos, Block sourceBlock, @Nullable Orientation wireOrientation, boolean notify) {
    super.neighborChanged(state, world, pos, sourceBlock, wireOrientation, notify);
    neighborRoadUpdate(state, world, pos, sourceBlock, wireOrientation, notify);
  }

  private <T extends Comparable<T>> BlockState sendProperty(
      BlockState fromState, BlockState toState, Property<T> property) {
    return toState.setValue(property, fromState.getValue(property));
  }

  @Override
  public MapCodec<? extends RoadSlabBlockWithAutoLine> codec() {
    return CODEC;
  }
}
