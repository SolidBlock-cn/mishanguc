package pers.solid.mishang.uc.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.EnumProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.util.BlockRotation;
import net.minecraft.util.math.Direction;
import org.jetbrains.annotations.Nullable;

public class RoadBlockStraightLine extends AbstractRoadBlock {
    protected static final EnumProperty<Direction.Axis> AXIS = Properties.HORIZONTAL_AXIS;

    public RoadBlockStraightLine(Settings settings) {
        super(settings);
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        super.appendProperties(builder.add(AXIS));
    }

    @Nullable
    @Override
    public BlockState getPlacementState(ItemPlacementContext ctx) {
        BlockState state = super.getPlacementState(ctx);
        if (state==null) return null;
        return state.with(AXIS,ctx.getPlayerFacing().getAxis());
    }

    @Override
    public RoadConnectionState getConnectionStateOf(BlockState state,Direction direction) {
        Direction.Axis axis = state.get(AXIS);
        return direction.getAxis() == axis ? RoadConnectionState.CONNECTED_TO : RoadConnectionState.NOT_CONNECTED_TO;
    }

    @Override
    public BlockState rotate(BlockState state, BlockRotation rotation) {
        Direction.Axis axis = state.get(AXIS);
        Direction.Axis rotatedAxis;
        switch (rotation) {
            case CLOCKWISE_90:
            case COUNTERCLOCKWISE_90:
                rotatedAxis = axis.isHorizontal() ? axis : axis== Direction.Axis.X ? Direction.Axis.Z :
                        Direction.Axis.X;
            default:
                rotatedAxis = axis;
        }
        return super.rotate(state, rotation).with(AXIS,axis);
    }
}
