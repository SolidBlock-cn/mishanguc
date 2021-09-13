package pers.solid.mishang.uc.block;

import it.unimi.dsi.fastutil.objects.Object2BooleanMap;
import it.unimi.dsi.fastutil.objects.Object2BooleanOpenHashMap;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.state.StateManager;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.WorldAccess;

public interface RoadWithAutoLine extends Road {
    BlockState makeState(Object2BooleanMap<Direction> connectionStateMap, BlockState defaultState);

    /**
     * 道路自动连接的类型，分为直角和斜线。
     */
    enum RoadAutoLineType {
        RIGHT_ANGLE,
        BEVEL
    }

    /**
     * 道路材质，目前分为柏油路和水泥路。
     */
    enum RoadTexture {
        ASPHALT,
        CONCRETE
    }

    /**
     * 道路标线颜色，目前分为白色和黄色。
     */
    enum LineColor {
        WHITE,
        YELLOW
    }

    default Object2BooleanMap<Direction> getConnectionStateMap(BlockState state, WorldAccess world, BlockPos pos) {
        Object2BooleanMap<Direction> connectionStateMap = new Object2BooleanOpenHashMap<>();
        for (Direction direction : Direction.Type.HORIZONTAL) {
            BlockState nextState = world.getBlockState(pos.offset(direction, 1));
            Block nextBlock = nextState.getBlock();
            if (nextBlock instanceof Road) {
                RoadConnectionState connectionState = ((Road) nextBlock).getConnectionStateOf(nextState,direction.getOpposite());
                connectionStateMap.put(direction,connectionState.compareTo(RoadConnectionState.MAY_CONNECT_TO)>=0);
            } else {
                connectionStateMap.put(direction,false);
            }
        }
        return connectionStateMap;
    }

    @Override
    default RoadConnectionState getConnectionStateOf(BlockState state, Direction direction) {
        return Road.super.getConnectionStateOf(state, direction).or(RoadConnectionState.MAY_CONNECT_TO);
    }

    @Override
    default void appendRoadProperties(StateManager.Builder<Block, BlockState> builder) {
        Road.super.appendRoadProperties(builder);
    }
}
