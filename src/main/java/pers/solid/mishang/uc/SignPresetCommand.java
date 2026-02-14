package pers.solid.mishang.uc;

import com.google.gson.JsonParseException;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.command.CommandRegistryAccess;
import net.minecraft.command.argument.NbtCompoundArgumentType;
import net.minecraft.command.argument.TextArgumentType;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.screen.ScreenTexts;
import net.minecraft.text.*;
import net.minecraft.util.Formatting;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import pers.solid.mishang.uc.block.StandingSignBlock;
import pers.solid.mishang.uc.blockentity.HungSignBlockEntity;
import pers.solid.mishang.uc.blockentity.StandingSignBlockEntity;
import pers.solid.mishang.uc.blockentity.WallSignBlockEntity;
import pers.solid.mishang.uc.screen.SignPreset;
import pers.solid.mishang.uc.screen.SignPresets;
import pers.solid.mishang.uc.text.TextContext;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import static com.mojang.brigadier.arguments.StringArgumentType.getString;
import static com.mojang.brigadier.arguments.StringArgumentType.string;
import static net.fabricmc.fabric.api.client.command.v2.ClientCommandManager.argument;
import static net.fabricmc.fabric.api.client.command.v2.ClientCommandManager.literal;

@Environment(EnvType.CLIENT)
public enum SignPresetCommand implements ClientCommandRegistrationCallback {
  INSTANCE;

  @Override
  public void register(@NotNull CommandDispatcher<FabricClientCommandSource> dispatcher, @NotNull CommandRegistryAccess registryAccess) {
    dispatcher.register(literal("mishanguc:signpreset")
        .then(literal("path")
            .executes(commandContext -> {
              commandContext.getSource().sendFeedback(Text.translatable("message.mishanguc.signPreset.path", Text.literal(SignPresets.PATH.getFileName().toString()).styled(style -> style
                  .withUnderline(true)
                  .withColor(Formatting.YELLOW)
                  .withHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, Text.translatable("message.mishanguc.signPreset.save.success.click_to_open")))
                  .withClickEvent(new ClickEvent(ClickEvent.Action.OPEN_FILE, SignPresets.PATH.toString())))));
              return 1;
            }))
        .then(literal("list")
            .executes(commandContext -> {
              final Collection<SignPreset> values = SignPresets.streamValues().toList();
              commandContext.getSource().sendFeedback(Text.translatable("message.mishanguc.signPreset.list", Texts.join(values, Texts.DEFAULT_SEPARATOR_TEXT, signPreset -> signPreset.name().copy().formatted(Formatting.YELLOW).styled(style -> {
                final MutableText text = Text.empty();
                final Text description = signPreset.description();
                if (description != null) {
                  text.append(description);
                  text.append(ScreenTexts.LINE_BREAK);
                }
                return style
                    .withHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, text.append(Text.translatable("message.mishanguc.signPreset.list.id_info", signPreset.name()).formatted(Formatting.GRAY))));
              }))));
              return values.size();
            }))
        .then(literal("reload")
            .executes(commandContext -> {
              final FabricClientCommandSource source = commandContext.getSource();
              final MinecraftClient client = source.getClient();
              source.sendFeedback(Text.translatable("message.mishanguc.signPreset.list.reload"));
              final Thread thread = new Thread(() -> {
                final int i = SignPresets.loadAll();
                if (i >= 0) {
                  client.execute(() -> source.sendFeedback(Text.translatable("message.mishanguc.signPreset.list.reload.success", i)));
                } else {
                  client.execute(() -> source.sendFeedback(Text.translatable("message.mishanguc.signPreset.list.reload.error", Text.literal(SignPresets.PATH.toString()).styled(style -> style.withClickEvent(new ClickEvent(ClickEvent.Action.OPEN_FILE, SignPresets.PATH.toString()))))));
                }
              });
              thread.start();
              return 1;
            }))
        .then(literal("save")
            .then(argument("id", string())
                .executes(commandContext -> executeSave(commandContext, null))
                .then(argument("args", NbtCompoundArgumentType.nbtCompound())
                    .executes(commandContext -> executeSave(commandContext, NbtCompoundArgumentType.getNbtCompound(commandContext, "args"))))))
        .then(literal("delete")
            .then(argument("id", string()).suggests(SignPresets.SUGGEST_KEYS)
                .executes(commandContext -> executeDelete(commandContext, true))))
        .then(literal("reset")
            .executes(SignPresetCommand::executeResetAll)
            .then(argument("id", string()).suggests(SignPresets.SUGGEST_KEYS_AND_BUILTIN)
                .executes(commandContext -> executeDelete(commandContext, false)))));
  }

  private static int executeSave(CommandContext<FabricClientCommandSource> commandContext, @Nullable NbtCompound args) throws CommandSyntaxException {
    final boolean force;
    final int order;
    final int initialFocus;

    if (args != null) {
      force = args.getBoolean("force");
      order = args.getInt("order");
      initialFocus = args.getInt("initial_focus");
    } else {
      force = false;
      order = 0;
      initialFocus = 0;
    }

    final FabricClientCommandSource source = commandContext.getSource();
    final HitResult hitResult = source.getClient().crosshairTarget;
    if (!(hitResult instanceof BlockHitResult blockHitResult)) {
      source.sendError(Text.translatable("message.mishanguc.signPreset.save.not_sign"));
      return -1;
    }
    final ClientWorld world = source.getWorld();
    final BlockPos blockPos = blockHitResult.getBlockPos();
    final BlockEntity blockEntity = world.getBlockEntity(blockPos);

    final List<TextContext> textContexts;
    if (blockEntity instanceof WallSignBlockEntity wallSignBlockEntity) {
      textContexts = wallSignBlockEntity.textContexts;
    } else if (blockEntity instanceof StandingSignBlockEntity standingSignBlockEntity) {
      final Boolean hitSide = StandingSignBlock.getHitSide(world.getBlockState(blockPos), blockHitResult);
      textContexts = hitSide == null ? null : standingSignBlockEntity.getTextsOnSide(hitSide);
    } else if (blockEntity instanceof HungSignBlockEntity hungSignBlockEntity) {
      textContexts = hungSignBlockEntity.texts.get(blockHitResult.getSide());
    } else {
      source.sendError(Text.translatable("message.mishanguc.signPreset.save.not_sign"));
      return -1;
    }

    if (textContexts == null) {
      source.sendError(Text.translatable("message.mishanguc.signPreset.save.fail.no_text"));
      return -2;
    } else if (!force && textContexts.isEmpty()) {
      source.sendError(Text.translatable("message.mishanguc.signPreset.save.fail.empty_text"));
      return -1;
    }

    final List<TextContext> textContextsCopy = textContexts.stream().map(TextContext::clone).toList();
    final String id = getString(commandContext, "id");
    final Optional<Text> name;
    if (args == null || !args.contains("name")) {
      name = Optional.empty();
    } else {
      try {
        name = Optional.of(Text.Serializer.fromJson(new StringReader(args.getString("name"))));
      } catch (JsonParseException e) {
        throw TextArgumentType.INVALID_COMPONENT_EXCEPTION.create(e.getMessage());
      }
    }
    final Optional<Text> description;
    if (args == null || !args.contains("description")) {
      description = Optional.empty();
    } else {
      try {
        description = Optional.of(Text.Serializer.fromJson(new StringReader(args.getString("description"))));
      } catch (JsonParseException e) {
        throw TextArgumentType.INVALID_COMPONENT_EXCEPTION.create(e.getMessage());
      }
    }
    if (initialFocus < 0 || (initialFocus >= textContextsCopy.size() && initialFocus > 0)) {
      source.sendError(Text.translatable("message.mishanguc.signPreset.save.initial_focus_invalid", initialFocus, textContextsCopy.size()));
      return -1;
    }
    final Thread thread = new Thread(() -> {
      final SignPreset.Info info = new SignPreset.Info(order, name, description, textContextsCopy, initialFocus);
      info.save(id, source, force);
    });
    thread.start();
    source.sendFeedback(Text.translatable("message.mishanguc.signPreset.save.start"));
    return 1;
  }

  private static int executeDelete(CommandContext<FabricClientCommandSource> commandContext, boolean hideIdBuiltin) {
    final FabricClientCommandSource source = commandContext.getSource();
    final String id = getString(commandContext, "id");
    final SignPreset signPreset = SignPresets.getOrBuiltin(id);
    if (signPreset == null) {
      source.sendError(Text.translatable("message.mishanguc.signPreset.delete.not_exist", id));
      return -1;
    }

    final Thread thread = new Thread(() -> {
      final SignPreset.Info info = signPreset.asInfo();
      info.delete(id, source, hideIdBuiltin);
    });
    thread.start();
    source.sendFeedback(Text.translatable("message.mishanguc.signPreset.delete.start", signPreset.name()));
    return 1;
  }

  private static int executeResetAll(CommandContext<FabricClientCommandSource> commandContext) {
    final FabricClientCommandSource source = commandContext.getSource();
    final MinecraftClient client = source.getClient();
    final Thread thread = new Thread(() -> {
      try (final Stream<Path> stream = Files.walk(SignPresets.PATH)) {
        stream
            .peek(System.out::println)
            .filter(Files::isRegularFile)
            .filter(path -> path.getFileName().toString().endsWith(".json"))
            .forEach(path -> {
              try {
                Files.delete(path);
              } catch (IOException e) {
                SignPresets.LOGGER.error("Failed to delete sign preset {}", path, e);
              }
            });
        client.execute(() -> source.sendFeedback(Text.translatable("message.mishanguc.signPreset.reset.success")));
        SignPresets.resetToBuiltin();
      } catch (IOException e) {
        SignPresets.LOGGER.error("Failed to delete sign presets", e);
        client.execute(() -> source.sendError(Text.translatable("message.mishanguc.signPreset.reset.fail.unknown")));
      }
    });
    thread.start();
    source.sendFeedback(Text.translatable("message.mishanguc.signPreset.reset.start"));
    return 1;
  }
}
