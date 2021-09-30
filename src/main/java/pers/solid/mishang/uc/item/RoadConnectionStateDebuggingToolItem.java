package pers.solid.mishang.uc.item;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Style;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Formatting;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import pers.solid.mishang.uc.block.Road;
import pers.solid.mishang.uc.block.RoadConnectionState;

public class RoadConnectionStateDebuggingToolItem extends Item {
    public RoadConnectionStateDebuggingToolItem(Settings settings) {
        super(settings);
    }

    /**
     * 向聊天框广播特定方向的连接状态。
     *
     * @param direction 获取此方向上的连接状态。
     */
    public static void sendMessageOfState(PlayerEntity playerEntity, BlockState blockState, BlockPos blockPos, Direction direction) {
        Block block = blockState.getBlock();
        if (!(block instanceof Road)) {
            playerEntity.sendMessage(new TranslatableText("debug.mishanguc.notRoad"), false);
            return;
        }
        final RoadConnectionState connectionState = ((Road) block).getConnectionStateOf(blockState, direction);
        playerEntity.sendMessage(new LiteralText("")
                        .append(new TranslatableText("debug.mishanguc.roadConnectionState", String.format("%s %s %s", blockPos.getX(), blockPos.getY(), blockPos.getZ()), RoadConnectionState.text(direction)).setStyle(Style.EMPTY.withBold(true).withColor(Formatting.YELLOW)))
                        .append("\n")
                        .append(new TranslatableText("debug.mishanguc.roadConnectionState.direction", RoadConnectionState.text(connectionState.direction)))
                        .append("\n")
                        .append(new TranslatableText("debug.mishanguc.roadConnectionState.lineColor", RoadConnectionState.text(connectionState.lineColor)))
                        .append("\n")
                        .append(new TranslatableText("debug.mishanguc.roadConnectionState.probability", RoadConnectionState.text(connectionState.probability)))
                , false);
    }

    /**
     * 向聊天框广播各个方向的道路连接状态。
     */
    public static void sendMessageOfState(PlayerEntity playerEntity, BlockState blockState, BlockPos blockPos) {
        Block block = blockState.getBlock();
        if (!(block instanceof Road)) {
            playerEntity.sendMessage(new TranslatableText("debug.mishanguc.notRoad"), false);
            return;
        }
        playerEntity.sendMessage(new TranslatableText("debug.mishanguc.roadConnectionState.allDir", String.format("%s %s %s", blockPos.getX(), blockPos.getY(), blockPos.getZ())).setStyle(Style.EMPTY.withBold(true).withColor(Formatting.YELLOW)), false);
        Direction.Type.HORIZONTAL.forEach(direction -> {
            final RoadConnectionState connectionState = ((Road) block).getConnectionStateOf(blockState, direction);
            playerEntity.sendMessage(new TranslatableText("debug.mishanguc.roadConnectionState.brief", RoadConnectionState.text(direction), RoadConnectionState.text(connectionState.direction), RoadConnectionState.text(connectionState.lineColor), RoadConnectionState.text(connectionState.probability)), false);
        });
    }

    @Override
    public ActionResult useOnBlock(ItemUsageContext context) {
        PlayerEntity playerEntity = context.getPlayer();
        final World world = context.getWorld();
        if (!world.isClient && playerEntity != null) {
            final BlockPos blockPos = context.getBlockPos();
            final BlockState blockState = world.getBlockState(blockPos);
            final Block block = blockState.getBlock();
            if (block instanceof Road) {
                final Direction side = context.getSide();
                if (side == null || Direction.Type.VERTICAL.test(side)) {
                    sendMessageOfState(playerEntity, blockState, blockPos);
                } else {
                    sendMessageOfState(playerEntity, blockState, blockPos, side);
                }
            }
        }

        return ActionResult.success(world.isClient);
    }
}
