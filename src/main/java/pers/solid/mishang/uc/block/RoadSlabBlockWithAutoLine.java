package pers.solid.mishang.uc.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.state.property.Properties;
import net.minecraft.util.math.Direction;
import org.jetbrains.annotations.NotNull;
import pers.solid.mishang.uc.LineColor;
import pers.solid.mishang.uc.ModProperties;
import pers.solid.mishang.uc.RoadTexture;

import java.util.Map;

public class RoadSlabBlockWithAutoLine extends AbstractRoadSlabBlock implements RoadWithAutoLine{
    public final RoadAutoLineType type;
    private final RoadTexture texture;
    public RoadSlabBlockWithAutoLine(Settings settings, RoadAutoLineType type, RoadTexture texture, LineColor lineColor) {
        super(settings,lineColor);
        this.type = type;
        this.texture = texture;
    }

    @Override
    public @NotNull BlockState makeState(Map<Direction,RoadConnectionState> connectionStateMap, BlockState defaultState) {
        int connected = 0;
        for (Map.Entry<Direction, RoadConnectionState> e : connectionStateMap.entrySet()) {
            Direction key = e.getKey();
            RoadConnectionState value = e.getValue();
            if (value.mayConnect()) connected++;
        }
        if (texture == RoadTexture.ASPHALT) {
            switch (connected) {
                case 0:
                    // 全都不连接的情况。
                    return MUBlocks.ASPHALT_ROAD_SLAB.getDefaultState()
                            .with(Properties.SLAB_TYPE,defaultState.get(Properties.SLAB_TYPE))
                            .with(WATERLOGGED,defaultState.get(WATERLOGGED));
                case 4:
                    // 全都连接的情况。
                    if (lineColor == LineColor.WHITE)
                        return MUBlocks.ASPHALT_ROAD_SLAB_WITH_WHITE_CROSS_LINE.getDefaultState()
                                .with(Properties.SLAB_TYPE,defaultState.get(Properties.SLAB_TYPE))
                                .with(WATERLOGGED,defaultState.get(WATERLOGGED));
                    else
                        return defaultState;
                case 2:
                    // 仅有两种方向的情况，又分为两种：直线和直角/斜线。
                    for (Map.Entry<Direction, RoadConnectionState> entry : connectionStateMap.entrySet()) {
                        if (!entry.getValue().mayConnect()) continue;
                        Direction direction = entry.getKey();
                        if (connectionStateMap.get(direction.getOpposite()).mayConnect()) {
                            // 如果对面方向也有的情况。
                            return MUBlocks.ASPHALT_ROAD_SLAB_WITH_WHITE_STRAIGHT_LINE.getDefaultState()
                                    .with(Properties.SLAB_TYPE,defaultState.get(Properties.SLAB_TYPE))
                                    .with(WATERLOGGED,defaultState.get(WATERLOGGED))
                                    .with(Properties.HORIZONTAL_AXIS, direction.getAxis());
                        } else {
                            // 直角或斜角的情况。
                            Direction direction2 = direction.rotateYClockwise();
                            Block block;
                            if (lineColor == LineColor.WHITE) {
                                switch (type) {
                                    case BEVEL:
                                        block = MUBlocks.ASPHALT_ROAD_SLAB_WITH_WHITE_BEVEL_ANGLE_LINE;
                                        break;
                                    case RIGHT_ANGLE:
                                        block = MUBlocks.ASPHALT_ROAD_SLAB_WITH_WHITE_RIGHT_ANGLE_LINE;
                                        break;
                                    default:
                                        throw new IllegalStateException("Unknown angle type: " + type);
                                }
                            } else block = null;
                            if (block == null) return defaultState;
                            // 现在已经确定好了block的类型，现在考虑方向。
                            BlockState state = block.getDefaultState()
                                    .with(Properties.SLAB_TYPE,defaultState.get(Properties.SLAB_TYPE))
                                    .with(WATERLOGGED,defaultState.get(WATERLOGGED));
                            if (state.contains(RoadWithAngleLine.FACING)) {
                                return state.with(RoadWithAngleLine.FACING, HorizontalCornerDirection.fromDirections(direction, connectionStateMap.get(direction2).mayConnect() ? direction2 : direction.rotateYCounterclockwise()));
                            }
                        }
                    }
                case 1:
                    // 只有一个方向连接的情况下，为直线。
                    for (Map.Entry<Direction, RoadConnectionState> entry : connectionStateMap.entrySet()) {
                        if (entry.getValue().mayConnect() && lineColor == LineColor.WHITE)
                            return MUBlocks.ASPHALT_ROAD_SLAB_WITH_WHITE_STRAIGHT_LINE.getDefaultState()
                                    .with(Properties.SLAB_TYPE,defaultState.get(Properties.SLAB_TYPE))
                                    .with(WATERLOGGED,defaultState.get(WATERLOGGED))
                                    .with(Properties.HORIZONTAL_AXIS, entry.getKey().getAxis());
                    }
                    return defaultState;
                case 3:
                    for (Map.Entry<Direction, RoadConnectionState> entry : connectionStateMap.entrySet()) {
                        if (!entry.getValue().mayConnect() && lineColor == LineColor.WHITE)
                        // 检测需要连接正向的方块对应的连接是否为斜线。
                        {
                            // 朝向的方块
                            Direction direction = entry.getKey().getOpposite();
                            RoadConnectionState state = connectionStateMap.get(direction);
                            if (state.direction==null || state.direction.left().isPresent() || type!=RoadAutoLineType.BEVEL)
                                // 连接直线
                                return MUBlocks.ASPHALT_ROAD_SLAB_WITH_WHITE_JOINT_LINE.getDefaultState()
                                        .with(Properties.SLAB_TYPE,defaultState.get(Properties.SLAB_TYPE))
                                        .with(WATERLOGGED,defaultState.get(WATERLOGGED))
                                        .with(Properties.HORIZONTAL_FACING, direction);
                            else if (state.direction.right().isPresent()){
                                // 连接斜线
                                return MUBlocks.ASPHALT_ROAD_SLAB_WITH_WHITE_STRAIGHT_AND_BEVEL_ANGLE_LINE.getDefaultState()
                                        .with(Properties.SLAB_TYPE,defaultState.get(Properties.SLAB_TYPE))
                                        .with(WATERLOGGED,defaultState.get(WATERLOGGED))
                                        .with(Properties.HORIZONTAL_AXIS,direction.rotateYClockwise().getAxis())
                                        .with(ModProperties.HORIZONTAL_CORNER_FACING,state.direction.right().get().getOpposite().mirror(direction));
                            }
                        }
                    }
                    return defaultState;
                default:
                    throw new IllegalStateException("Illegal connected number: " + connected);
            }
        } else
            throw new UnsupportedOperationException("Road auto placement of non-asphalt road is not supported yet! qwq");
    }
}
