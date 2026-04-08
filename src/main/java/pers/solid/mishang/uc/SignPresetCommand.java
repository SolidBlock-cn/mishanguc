package pers.solid.mishang.uc;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.serialization.DataResult;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.commands.CommandBuildContext;
import net.minecraft.commands.arguments.ComponentArgument;
import net.minecraft.commands.arguments.CompoundTagArgument;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.*;
import net.minecraft.resources.RegistryOps;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import org.jspecify.annotations.Nullable;
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
  public void register(CommandDispatcher<FabricClientCommandSource> dispatcher, CommandBuildContext registryAccess) {
    dispatcher.register(literal("mishanguc:signpreset")
        .then(literal("path")
            .executes(commandContext -> {
              commandContext.getSource().sendFeedback(Component.translatable("message.mishanguc.signPreset.path", Component.literal(SignPresets.PATH.getFileName().toString()).withStyle(style -> style
                  .withUnderlined(true)
                  .withColor(ChatFormatting.YELLOW)
                  .withHoverEvent(new HoverEvent.ShowText(Component.translatable("message.mishanguc.signPreset.save.success.click_to_open")))
                  .withClickEvent(new ClickEvent.OpenFile(SignPresets.PATH)))));
              return 1;
            }))
        .then(literal("list")
            .executes(commandContext -> {
              final Collection<SignPreset> values = SignPresets.streamValues().toList();
              commandContext.getSource().sendFeedback(Component.translatable("message.mishanguc.signPreset.list", ComponentUtils.formatList(values, ComponentUtils.DEFAULT_NO_STYLE_SEPARATOR, signPreset -> signPreset.name().copy().withStyle(ChatFormatting.YELLOW).withStyle(style -> {
                final MutableComponent text = Component.empty();
                final Component description = signPreset.description();
                if (description != null) {
                  text.append(description);
                  text.append(CommonComponents.NEW_LINE);
                }
                return style
                    .withHoverEvent(new HoverEvent.ShowText(text.append(Component.translatable("message.mishanguc.signPreset.list.id_info", signPreset.name()).withStyle(ChatFormatting.GRAY))));
              }))));
              return values.size();
            }))
        .then(literal("reload")
            .executes(commandContext -> {
              final FabricClientCommandSource source = commandContext.getSource();
              final Minecraft client = source.getClient();
              source.sendFeedback(Component.translatable("message.mishanguc.signPreset.list.reload"));
              final Thread thread = new Thread(() -> {
                final int i = SignPresets.loadAll();
                if (i >= 0) {
                  client.execute(() -> source.sendFeedback(Component.translatable("message.mishanguc.signPreset.list.reload.success", i)));
                } else {
                  client.execute(() -> source.sendFeedback(Component.translatable("message.mishanguc.signPreset.list.reload.error", Component.literal(SignPresets.PATH.toString()).withStyle(style -> style.withClickEvent(new ClickEvent.OpenFile(SignPresets.PATH))))));
                }
              });
              thread.start();
              return 1;
            }))
        .then(literal("save")
            .then(argument("id", string())
                .executes(commandContext -> executeSave(commandContext, null))
                .then(argument("args", CompoundTagArgument.compoundTag())
                    .executes(commandContext -> executeSave(commandContext, CompoundTagArgument.getCompoundTag(commandContext, "args"))))))
        .then(literal("delete")
            .then(argument("id", string()).suggests(SignPresets.SUGGEST_KEYS)
                .executes(commandContext -> executeDelete(commandContext, true))))
        .then(literal("reset")
            .executes(SignPresetCommand::executeResetAll)
            .then(argument("id", string()).suggests(SignPresets.SUGGEST_KEYS_AND_BUILTIN)
                .executes(commandContext -> executeDelete(commandContext, false)))));
  }

  private static int executeSave(CommandContext<FabricClientCommandSource> commandContext, @Nullable CompoundTag args) throws CommandSyntaxException {
    final boolean force;
    final int order;
    final int initialFocus;

    if (args != null) {
      force = args.getBooleanOr("force", false);
      order = args.getIntOr("order", 0);
      initialFocus = args.getIntOr("initial_focus", 0);
    } else {
      force = false;
      order = 0;
      initialFocus = 0;
    }

    final FabricClientCommandSource source = commandContext.getSource();
    final HitResult hitResult = source.getClient().hitResult;
    if (!(hitResult instanceof BlockHitResult blockHitResult)) {
      source.sendError(Component.translatable("message.mishanguc.signPreset.save.not_sign"));
      return -1;
    }
    final ClientLevel world = source.getWorld();
    final BlockPos blockPos = blockHitResult.getBlockPos();
    final BlockEntity blockEntity = world.getBlockEntity(blockPos);

    final List<TextContext> textContexts;
    switch (blockEntity) {
      case WallSignBlockEntity wallSignBlockEntity -> textContexts = wallSignBlockEntity.textContexts;
      case StandingSignBlockEntity standingSignBlockEntity -> {
        final Boolean hitSide = StandingSignBlock.getHitSide(world.getBlockState(blockPos), blockHitResult);
        textContexts = hitSide == null ? null : standingSignBlockEntity.getTextsOnSide(hitSide);
      }
      case HungSignBlockEntity hungSignBlockEntity -> {
        textContexts = hungSignBlockEntity.texts.get(blockHitResult.getDirection());
      }
      case null, default -> {
        source.sendError(Component.translatable("message.mishanguc.signPreset.save.not_sign"));
        return -1;
      }
    }

    if (textContexts == null) {
      source.sendError(Component.translatable("message.mishanguc.signPreset.save.fail.no_text"));
      return -2;
    } else if (!force && textContexts.isEmpty()) {
      source.sendError(Component.translatable("message.mishanguc.signPreset.save.fail.empty_text"));
      return -1;
    }

    final List<TextContext> textContextsCopy = textContexts.stream().map(TextContext::clone).toList();
    final String id = getString(commandContext, "id");
    final RegistryOps<Tag> nbtOps = source.registryAccess().createSerializationContext(NbtOps.INSTANCE);
    final Optional<Component> name;
    if (args == null || !args.contains("name")) {
      name = Optional.empty();
    } else {
      final DataResult<Component> result = ComponentSerialization.CODEC.parse(nbtOps, args.get("name"));
      if (result instanceof DataResult.Error<Component> error) {
        throw ComponentArgument.ERROR_INVALID_COMPONENT.create(error.message());
      }
      name = result.result();
    }
    final Optional<Component> description;
    if (args == null || !args.contains("description")) {
      description = Optional.empty();
    } else {
      final DataResult<Component> result = ComponentSerialization.CODEC.parse(nbtOps, args.get("description"));
      if (result instanceof DataResult.Error<Component> error) {
        throw ComponentArgument.ERROR_INVALID_COMPONENT.create(error.message());
      }
      description = result.result();
    }
    if (initialFocus < 0 || (initialFocus >= textContextsCopy.size() && initialFocus > 0)) {
      source.sendError(Component.translatable("message.mishanguc.signPreset.save.initial_focus_invalid", initialFocus, textContextsCopy.size()));
      return -1;
    }
    final Thread thread = new Thread(() -> {
      final SignPreset.Info info = new SignPreset.Info(order, name, description, textContextsCopy, initialFocus);
      info.save(id, source, force);
    });
    thread.start();
    source.sendFeedback(Component.translatable("message.mishanguc.signPreset.save.start"));
    return 1;
  }

  private static int executeDelete(CommandContext<FabricClientCommandSource> commandContext, boolean hideIdBuiltin) {
    final FabricClientCommandSource source = commandContext.getSource();
    final String id = getString(commandContext, "id");
    final SignPreset signPreset = SignPresets.getOrBuiltin(id);
    if (signPreset == null) {
      source.sendError(Component.translatable("message.mishanguc.signPreset.delete.not_exist", id));
      return -1;
    }

    final Thread thread = new Thread(() -> {
      final SignPreset.Info info = signPreset.asInfo();
      info.delete(id, source, hideIdBuiltin);
    });
    thread.start();
    source.sendFeedback(Component.translatable("message.mishanguc.signPreset.delete.start", signPreset.name()));
    return 1;
  }

  private static int executeResetAll(CommandContext<FabricClientCommandSource> commandContext) {
    final FabricClientCommandSource source = commandContext.getSource();
    final Minecraft client = source.getClient();
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
        client.execute(() -> source.sendFeedback(Component.translatable("message.mishanguc.signPreset.reset.success")));
        SignPresets.resetToBuiltin();
      } catch (IOException e) {
        SignPresets.LOGGER.error("Failed to delete sign presets", e);
        client.execute(() -> source.sendError(Component.translatable("message.mishanguc.signPreset.reset.fail.unknown")));
      }
    });
    thread.start();
    source.sendFeedback(Component.translatable("message.mishanguc.signPreset.reset.start"));
    return 1;
  }
}
