package pers.solid.mishang.uc.block;

import com.google.common.collect.ImmutableSet;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.EnumProperty;
import net.minecraft.util.BlockMirror;
import net.minecraft.util.math.Direction;
import org.jetbrains.annotations.Nullable;

public class RoadBlockCornerLine extends AbstractRoadBlock{
    public static final EnumProperty<HorizontalCornerDirection> FACING = EnumProperty.of("facing",
            HorizontalCornerDirection.class);
    public RoadBlockCornerLine(Settings settings) {
        super(settings);
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        super.appendProperties(builder);
        builder.add(FACING);
    }

    @Nullable
    @Override
    public BlockState getPlacementState(ItemPlacementContext ctx) {
        BlockState state = super.getPlacementState(ctx);
        if (state==null) return null;
        return state.with(FACING, HorizontalCornerDirection.fromRotation(ctx.getPlayerYaw()));
    }

    @Override
    public RoadConnectionState getConnectionStateOf(BlockState state, Direction direction) {
        return state.get(FACING).hasDirection(direction) ? RoadConnectionState.CONNECTED_TO : RoadConnectionState.NOT_CONNECTED_TO;
    }

    @Override
    public BlockState mirror(BlockState state, BlockMirror mirror) {
        return super.mirror(state, mirror).with(FACING,state.get(FACING).mirror(mirror));
    }
}
