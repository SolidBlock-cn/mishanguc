package pers.solid.mishang.uc;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.Dynamic2CommandExceptionType;
import net.minecraft.command.argument.BlockPosArgumentType;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.ServerTask;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.world.ChunkHolder;
import net.minecraft.server.world.ServerChunkManager;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.ClickEvent;
import net.minecraft.text.MutableText;
import net.minecraft.util.Formatting;
import net.minecraft.util.math.BlockBox;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.LightType;
import pers.solid.mishang.uc.mixin.ChunkHolderAccessor;
import pers.solid.mishang.uc.mixin.ServerChunkManagerAccessor;
import pers.solid.mishang.uc.util.TextBridge;

import java.util.Iterator;

public final class MishangucCommands {

  private static final MutableText NOT_STABLE_WARN = TextBridge.translatable("commands.mishanguc:update-light.warn_not_stable").formatted(Formatting.GRAY);
  private static final MutableText MASS_WARN = TextBridge.translatable("commands.mishanguc:update-light.mass").formatted(Formatting.YELLOW);
  private static final Dynamic2CommandExceptionType TOO_BIG_EXCEPTION = new Dynamic2CommandExceptionType(
      (maxCount, count) -> TextBridge.translatable("commands.mishanguc:update-light.to_big", maxCount, count)
  );

  /**
   * 注册 {@code /mishanguc:update-light} 命令。
   */
  static void registerUpdateLightCommand(CommandDispatcher<ServerCommandSource> dispatcher) {
    dispatcher.register(CommandManager.literal("mishanguc:update-light")
        .requires(source -> source.hasPermissionLevel(4))
        .executes(context -> {
          final BlockPos pos = new BlockPos(context.getSource().getPosition());
          executeUpdateLight(context.getSource().getWorld(), pos);
          context.getSource().sendFeedback(TextBridge.translatable("commands.mishanguc:update-light.success", pos.getX(), pos.getY(), pos.getZ()), true);
          context.getSource().sendFeedback(NOT_STABLE_WARN, false);
          return 1;
        })
        .then(CommandManager.literal("help")
            .executes(context -> {
              executeUpdateLightHelp(context.getSource(), 0);
              return 1;
            })
            .then(CommandManager.argument("page", IntegerArgumentType.integer(1, 2))
                .executes(context -> {
                  executeUpdateLightHelp(context.getSource(), IntegerArgumentType.getInteger(context, "page"));
                  return 1;
                })))
        .then(CommandManager.argument("range", IntegerArgumentType.integer(0, 64))
            .executes(context -> {
              final ServerCommandSource source = context.getSource();
              final BlockPos pos = new BlockPos(source.getPosition());
              final int range = IntegerArgumentType.getInteger(context, "range");
              return executeUpdateLightWithinRange(source.getWorld(), pos, range, source);
            }))
        .then(CommandManager.argument("pos", BlockPosArgumentType.blockPos())
            .executes(context -> {
              final BlockPos pos = BlockPosArgumentType.getBlockPos(context, "pos");
              executeUpdateLight(context.getSource().getWorld(), pos);
              context.getSource().sendFeedback(TextBridge.translatable("commands.mishanguc:update-light.success", pos.getX(), pos.getY(), pos.getZ()), true);
              context.getSource().sendFeedback(NOT_STABLE_WARN, false);
              return 1;
            }).then(CommandManager.argument("range", IntegerArgumentType.integer(0, 64))
                .executes(context -> {
                  final BlockPos pos = BlockPosArgumentType.getBlockPos(context, "pos");
                  final int range = IntegerArgumentType.getInteger(context, "range");
                  final ServerCommandSource source = context.getSource();
                  return executeUpdateLightWithinRange(source.getWorld(), pos, range, source);
                })))
        .then(CommandManager.argument("from", BlockPosArgumentType.blockPos())
            .then(CommandManager.argument("to", BlockPosArgumentType.blockPos())
                .executes(context -> {
                  final BlockPos fromPos = BlockPosArgumentType.getBlockPos(context, "from");
                  final BlockPos toPos = BlockPosArgumentType.getBlockPos(context, "to");
                  final ServerCommandSource source = context.getSource();
                  return executeUpdateLightWithinRange(source.getWorld(), fromPos, toPos, source);
                })))
    );
  }

  private static void executeUpdateLight(ServerWorld world, BlockPos pos) {
    final ServerChunkManager chunkManager = world.getChunkManager();
    final ChunkHolder chunkHolder = ((ServerChunkManagerAccessor) chunkManager).callGetChunkHolder(new ChunkPos(pos).toLong());
    world.getLightingProvider().checkBlock(pos);
    chunkHolder.markForLightUpdate(LightType.BLOCK, pos.getY());
    chunkHolder.markForLightUpdate(LightType.SKY, pos.getY());
    ((ChunkHolderAccessor) chunkHolder).setNoLightingUpdates(true);
    chunkHolder.flushUpdates(chunkHolder.getWorldChunk());
  }

  /**
   * 在特定的位置执行光照更新，并推送给客户端。注意，对于一个位置而言，该位置所有高度的地方都会被更新光照，以确保相应的更新能够发送至客户端。。
   */
  private static void executeUpdateLight(ServerWorld world, BlockPos pos, int minY, int maxY) {
    final ServerChunkManager chunkManager = world.getChunkManager();
    final ChunkHolder chunkHolder = ((ServerChunkManagerAccessor) chunkManager).callGetChunkHolder(new ChunkPos(pos).toLong());
    for (int i = minY; i < maxY; i++) {
      world.getLightingProvider().checkBlock(pos.withY(i));
      chunkHolder.markForLightUpdate(LightType.BLOCK, i);
      chunkHolder.markForLightUpdate(LightType.SKY, i);
    }
    ((ChunkHolderAccessor) chunkHolder).setNoLightingUpdates(true);
    chunkHolder.flushUpdates(chunkHolder.getWorldChunk());
  }

  /**
   * 在特定位置中心，周围一定范围内执行光照更新。
   */
  private static int executeUpdateLightWithinRange(ServerWorld world, BlockPos centerPos, int range, ServerCommandSource source) {
    final Iterator<BlockPos> iterator = BlockPos.iterateOutwards(centerPos, range, 0, range).iterator();
    final int blocksAffected = (int) Math.pow(2 * range + 1, 3);
    if (Math.pow(2 * range + 1, 3) > 32768) {
      source.sendFeedback(MASS_WARN, false);
    }
    final MinecraftServer server = world.getServer();
    final Runnable runnable = new Runnable() {
      private int ticks;
      private int iterated;

      @Override
      public void run() {
        if (server.getTicks() > ticks) {
          for (int i = 0; i < 512; i++) {
            // 为加快速度，一次可以执行 128 次循环。
            if (iterator.hasNext()) {
              final BlockPos pos = iterator.next();
              executeUpdateLight(world, pos, pos.getY() - range, pos.getY() + range);
              iterated++;
              if (iterated % 16384 == 0) {
                Mishanguc.MISHANG_LOGGER.info("Finished {} updates for {} positions.", iterated, blocksAffected);
              }
            } else break;
          }
        }
        ticks = server.getTicks();
        if (iterator.hasNext())
          server.send(new ServerTask(0, this));
        else {
          source.sendFeedback(TextBridge.translatable("commands.mishanguc:update-light.success_range", centerPos.getX(), centerPos.getY(), centerPos.getZ(), range, blocksAffected), true);
          source.sendFeedback(NOT_STABLE_WARN, false);
        }
      }
    };
    runnable.run();
    return blocksAffected;
  }

  private static int executeUpdateLightWithinRange(ServerWorld world, BlockPos fromPos, BlockPos toPos, ServerCommandSource source) throws CommandSyntaxException {
    final MinecraftServer server = world.getServer();
    final BlockBox blockBox = BlockBox.create(fromPos, toPos);
    final int blocksAffected = blockBox.getBlockCountX() * blockBox.getBlockCountY() * blockBox.getBlockCountZ();
    final Iterator<BlockPos> iterator = BlockPos.iterate(fromPos, toPos.withY(fromPos.getY())).iterator();
    if (blocksAffected > 2097152) {
      throw TOO_BIG_EXCEPTION.create(2097152, blocksAffected);
    }
    final Runnable runnable = new Runnable() {
      private int ticks;
      private int iterated;

      @Override
      public void run() {
        if (server.getTicks() > ticks) {
          for (int i = 0; i < 512; i++) {
            // 为加快速度，一次可以执行 128 次循环。
            if (iterator.hasNext()) {
              final BlockPos pos = iterator.next();
              executeUpdateLight(world, pos, blockBox.getMinY(), blockBox.getMaxY());
              iterated++;
              if (iterated % 16384 == 0) {
                Mishanguc.MISHANG_LOGGER.info("Finished {} updates for {} positions.", iterated, blocksAffected);
              }
            } else break;
          }
        }
        ticks = server.getTicks();
        if (iterator.hasNext())
          server.send(new ServerTask(0, this));
        else {
          source.sendFeedback(TextBridge.translatable("commands.mishanguc:update-light.success_box", fromPos.getX(), fromPos.getY(), fromPos.getZ(), toPos.getX(), toPos.getY(), toPos.getZ(), blocksAffected), true);
          source.sendFeedback(NOT_STABLE_WARN, false);
        }
      }
    };
    runnable.run();
    return blocksAffected;
  }

  private static void executeUpdateLightHelp(ServerCommandSource source, int page) {
    final MutableText lb = TextBridge.literal("\n");
    if (page < 1) source.sendFeedback(TextBridge.empty().formatted(Formatting.GRAY)
            .append(TextBridge.translatable("commands.mishanguc:update-light.description.title", "/mishanguc:update-light").formatted(Formatting.GREEN, Formatting.BOLD, Formatting.UNDERLINE))
            .append(lb)
            .append(TextBridge.translatable("commands.mishanguc:update-light.description.summary"))
            .append(lb)
            .append(TextBridge.literal("- ")
                .append(TextBridge.literal("/mishanguc:update-light [").formatted(Formatting.YELLOW).append(TextBridge.translatable("commands.mishanguc:update-light.argument.pos").append("]")))
                .append("\n")
                .append(TextBridge.translatable("commands.mishanguc:update-light.description.syntax1")))
            .append(lb)
            .append(TextBridge.literal("- ")
                .append(TextBridge.literal("/mishanguc:update-light [").formatted(Formatting.YELLOW).append(TextBridge.translatable("commands.mishanguc:update-light.argument.centerPos")).append("] <").append(TextBridge.translatable("commands.mishanguc:update-light.argument.range")).append(">"))
                .append("\n")
                .append(TextBridge.translatable("commands.mishanguc:update-light.description.syntax2", 64, TextBridge.translatable("commands.mishanguc:update-light.argument.range"))))
            .append(lb)
            .append(TextBridge.translatable("commands.mishanguc:update-light.continue")
                .styled(style -> style
                    .withColor(Formatting.WHITE).withUnderline(true)
                    .withClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/mishanguc:update-light help 2")))),
        false);
    if (page >= 1) source.sendFeedback(TextBridge.empty().formatted(Formatting.GRAY)
            .append(TextBridge.literal("- ")
                .append(TextBridge.literal("/mishanguc:update light <").formatted(Formatting.YELLOW).append(TextBridge.translatable("commands.mishanguc:update-light.argument.fromPos")).append("> <").append(TextBridge.translatable("commands.mishanguc:update-light.argument.fromPos").append(">")))
                .append("\n")
                .append(TextBridge.translatable("commands.mishanguc:update-light.description.syntax3", 2097152)))
            .append(lb).append(lb)
            .append(TextBridge.translatable("commands.mishanguc:update-light.description.detail")),
        false);
  }
}
