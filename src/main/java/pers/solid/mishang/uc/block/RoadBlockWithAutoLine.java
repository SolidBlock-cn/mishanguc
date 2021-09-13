package pers.solid.mishang.uc.block;

import it.unimi.dsi.fastutil.objects.Object2BooleanMap;
import net.minecraft.block.AirBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.state.property.Properties;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;

public class RoadBlockWithAutoLine extends AbstractRoadBlock implements RoadWithAutoLine {
    public final RoadAutoLineType type;
    private final RoadTexture texture;
    private final LineColor lineColor;

    public RoadBlockWithAutoLine(Settings settings, RoadAutoLineType type, RoadTexture texture, LineColor lineColor) {
        super(settings);
        this.type = type;
        this.texture = texture;
        this.lineColor = lineColor;
    }

    @Override
    public @NotNull BlockState makeState(Object2BooleanMap<Direction> connectionStateMap, BlockState defaultState) {
//        boolean north = connectionStateMap.getBoolean(Direction.NORTH);
//        boolean south = connectionStateMap.getBoolean(Direction.SOUTH);
//        boolean east = connectionStateMap.getBoolean(Direction.EAST);
//        boolean west = connectionStateMap.getBoolean(Direction.WEST);
        int connected = Collections.frequency(connectionStateMap.values(), true);
        if (texture == RoadTexture.ASPHALT) {
            switch (connected) {
                case 0:
                    // 全都不连接的情况。
                    return MUBlocks.ASPHALT_ROAD_BLOCK.getDefaultState();
                case 4:
                    // 全都连接的情况。
                    if (lineColor == LineColor.WHITE)
                        return MUBlocks.ASPHALT_ROAD_WITH_WHITE_CROSS_LINE.getDefaultState();
                    else
                        return defaultState;
                case 2:
                    // 仅有两种方向的情况，又分为两种：直线和直角/斜线。
                    for (Object2BooleanMap.Entry<Direction> entry : connectionStateMap.object2BooleanEntrySet()) {
                        if (!entry.getBooleanValue()) continue;
                        assert entry.getBooleanValue(); // 仅在该 direction 为 true 时继续运行。
                        Direction direction = entry.getKey();
                        if (connectionStateMap.getBoolean(direction.getOpposite())) {
                            // 如果对面方向也有的情况。
                            return MUBlocks.ASPHALT_ROAD_WITH_WHITE_STRAIGHT_LINE.getDefaultState()
                                    .with(Properties.HORIZONTAL_AXIS, direction.getAxis());
                        } else {
                            // 直角或斜角的情况。
                            Direction direction2 = direction.rotateYClockwise();
                            Block block;
                            if (lineColor == LineColor.WHITE) {
                                switch (type) {
                                    case BEVEL:
                                        block = MUBlocks.ASPHALT_ROAD_WITH_WHITE_BEVEL_ANGLE_LINE;
                                        break;
                                    case RIGHT_ANGLE:
                                        block = MUBlocks.ASPHALT_ROAD_WITH_WHITE_RIGHT_ANGLE_LINE;
                                        break;
                                    default:
                                        throw new IllegalStateException("Unknown angle type: " + type);
                                }
                            } else block = null;
                            if (block == null) return defaultState;
                            // 现在已经确定好了block的类型，现在考虑方向。
                            BlockState state = block.getDefaultState();
                            if (state.contains(RoadWithAngleLine.FACING)) {
                                return state
                                        .with(RoadWithAngleLine.FACING, HorizontalCornerDirection.fromDirections(direction, connectionStateMap.getBoolean(direction2) ? direction2 : direction.rotateYCounterclockwise()));
                            }
                        }
                    }
                case 1:
                    // 只有一个方向连接的情况下，为直线。
                    for (Object2BooleanMap.Entry<Direction> entry : connectionStateMap.object2BooleanEntrySet()) {
                        if (entry.getBooleanValue() && lineColor == LineColor.WHITE)
                            return MUBlocks.ASPHALT_ROAD_WITH_WHITE_STRAIGHT_LINE.getDefaultState()
                                    .with(Properties.HORIZONTAL_AXIS, entry.getKey().getAxis());
                    }
                    return defaultState;
                case 3:
                    for (Object2BooleanMap.Entry<Direction> entry : connectionStateMap.object2BooleanEntrySet()) {
                        if (!entry.getBooleanValue() && lineColor == LineColor.WHITE)
                            return MUBlocks.ASPHALT_ROAD_WITH_WHITE_JOINT_LINE.getDefaultState()
                                    .with(Properties.HORIZONTAL_FACING, entry.getKey().getOpposite());
                    }
                    return defaultState;
                default:
                    throw new IllegalStateException("Illegal connected number: " + connected);
            }
        } else
            throw new UnsupportedOperationException("Road auto placement of non-asphalt road is not supported yet! qwq");
    }

    @Override
    public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
        super.onUse(state, world, pos, player, hand, hit);
        final Item item = player.getStackInHand(hand).getItem();
        if (item instanceof BlockItem && ((BlockItem) item).getBlock() instanceof RoadWithAutoLine && !Direction.Type.VERTICAL.test(hit.getSide())) {
            return ActionResult.PASS;
        }
        world.setBlockState(pos, makeState(getConnectionStateMap(state, world, pos), state), 2);
        return ActionResult.SUCCESS;
    }

    @Override
    public void neighborUpdate(BlockState state, World world, BlockPos pos, Block block, BlockPos fromPos, boolean notify) {
        // 屏蔽上下方的更新。
        if (!fromPos.equals(pos.up()) && !fromPos.equals(pos.down()) && !(world.getBlockState(fromPos).getBlock() instanceof AirBlock))
            // flags设为2从而使得 <code>flags&1 !=0</code> 不成立，从而不递归更新邻居，参考 {@link World#setBlockState}。
            world.setBlockState(pos, makeState(getConnectionStateMap(state, world, pos), state), 2);
        super.neighborUpdate(state, world, pos, block, fromPos, notify);
    }
}
