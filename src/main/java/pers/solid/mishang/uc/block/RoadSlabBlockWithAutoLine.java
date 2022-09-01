package pers.solid.mishang.uc.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.state.property.Property;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;
import pers.solid.mishang.uc.blocks.RoadSlabBlocks;
import pers.solid.mishang.uc.util.RoadConnectionState;

import java.util.EnumMap;

public class RoadSlabBlockWithAutoLine extends SmartRoadSlabBlock<RoadBlockWithAutoLine>
    implements RoadWithAutoLine {

  public RoadSlabBlockWithAutoLine(RoadBlockWithAutoLine baseBlock) {
    super(baseBlock);
  }

  @Override
  public @NotNull BlockState makeState(
      EnumMap<Direction, RoadConnectionState> connectionStateMap, BlockState defaultState) {
    final BlockState baseState = baseBlock.makeState(connectionStateMap, defaultState);
    BlockState state = RoadSlabBlocks.BLOCK_TO_SLABS.get((AbstractRoadBlock) baseState.getBlock()).getDefaultState();
    for (Property<?> property : baseState.getProperties()) {
      if (state.contains(property)) {
        state = sendProperty(baseState, state, property);
      }
    }
    return state
        .with(WATERLOGGED, defaultState.get(WATERLOGGED))
        .with(TYPE, defaultState.get(TYPE));
  }

  @Override
  public void neighborUpdate(
      BlockState state, World world, BlockPos pos, Block block, BlockPos sourcePos, boolean notify) {
    super.neighborUpdate(state, world, pos, block, sourcePos, notify);
    neighborRoadUpdate(state, world, pos, block, sourcePos, notify);
  }

  private <T extends Comparable<T>> BlockState sendProperty(
      BlockState fromState, BlockState toState, Property<T> property) {
    return toState.with(property, fromState.get(property));
  }
}
