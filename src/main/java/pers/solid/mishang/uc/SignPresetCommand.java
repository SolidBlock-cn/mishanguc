package pers.solid.mishang.uc;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.command.CommandRegistryAccess;
import net.minecraft.command.argument.NbtCompoundArgumentType;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.screen.ScreenTexts;
import net.minecraft.text.*;
import net.minecraft.util.Formatting;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;
import pers.solid.mishang.uc.block.StandingSignBlock;
import pers.solid.mishang.uc.blockentity.HungSignBlockEntity;
import pers.solid.mishang.uc.blockentity.StandingSignBlockEntity;
import pers.solid.mishang.uc.blockentity.WallSignBlockEntity;
import pers.solid.mishang.uc.screen.SignPreset;
import pers.solid.mishang.uc.screen.SignPresets;
import pers.solid.mishang.uc.text.TextContext;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

import static com.mojang.brigadier.arguments.StringArgumentType.getString;
import static com.mojang.brigadier.arguments.StringArgumentType.string;
import static net.fabricmc.fabric.api.client.command.v2.ClientCommandManager.argument;
import static net.fabricmc.fabric.api.client.command.v2.ClientCommandManager.literal;

@Environment(EnvType.CLIENT)
public enum SignPresetCommand implements ClientCommandRegistrationCallback {
  INSTANCE;

  @Override
  public void register(@NonNull CommandDispatcher<FabricClientCommandSource> dispatcher, @NonNull CommandRegistryAccess registryAccess) {
    dispatcher.register(literal("mishanguc:signpreset")
        .then(literal("path")
            .executes(commandContext -> {
              commandContext.getSource().sendFeedback(Text.translatable("message.mishanguc.signPreset.path", Text.literal(SignPresets.PATH.getFileName().toString()).styled(style -> style
                  .withUnderline(true)
                  .withColor(Formatting.YELLOW)
                  .withHoverEvent(new HoverEvent.ShowText(Text.translatable("message.mishanguc.signPreset.save.success.click_to_open")))
                  .withClickEvent(new ClickEvent.OpenFile(SignPresets.PATH)))));
              return 1;
            }))
        .then(literal("list")
            .executes(commandContext -> {
              final Collection<SignPreset> values = SignPresets.all().values();
              commandContext.getSource().sendFeedback(Text.translatable("message.mishanguc.signPreset.list", Texts.join(values, Texts.DEFAULT_SEPARATOR_TEXT, signPreset -> signPreset.name().copy().formatted(Formatting.YELLOW).styled(style -> {
                final MutableText text = Text.empty();
                final Text description = signPreset.description();
                if (description != null) {
                  text.append(description);
                  text.append(ScreenTexts.LINE_BREAK);
                }
                return style
                    .withHoverEvent(new HoverEvent.ShowText(text.append(Text.translatable("message.mishanguc.signPreset.list.id_info", signPreset.name()).formatted(Formatting.GRAY))));
              }))));
              return values.size();
            }))
        .then(literal("reload")
            .executes(commandContext -> {
              final FabricClientCommandSource source = commandContext.getSource();
              source.sendFeedback(Text.translatable("message.mishanguc.signPreset.list.reload"));
              final Thread thread = new Thread(() -> {
                final int i = SignPresets.loadAll();
                if (i >= 0) {
                  source.sendFeedback(Text.translatable("message.mishanguc.signPreset.list.reload.success", i));
                } else {
                  source.sendFeedback(Text.translatable("message.mishanguc.signPreset.list.reload.error", Text.literal(SignPresets.PATH.toString()).styled(style -> style.withClickEvent(new ClickEvent.OpenFile(SignPresets.PATH)))));
                }
              });
              thread.start();
              return 1;
            }))
        .then(literal("save")
            .then(argument("id", string())
                .executes(commandContext -> executeSave(commandContext, null))
                .then(argument("args", NbtCompoundArgumentType.nbtCompound())
                    .executes(commandContext -> executeSave(commandContext, NbtCompoundArgumentType.getNbtCompound(commandContext, "args")))))));
  }

  private int executeSave(CommandContext<FabricClientCommandSource> commandContext, @Nullable NbtCompound args) {
    final boolean force;
    final boolean reload;
    final int order;
    final int initialFocus;

    if (args != null) {
      force = args.getBoolean("force", false);
      reload = args.getBoolean("reload", true);
      order = args.getInt("order", 0);
      initialFocus = args.getInt("initial_focus", 0);
    } else {
      force = false;
      reload = true;
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
    switch (blockEntity) {
      case WallSignBlockEntity wallSignBlockEntity -> textContexts = wallSignBlockEntity.textContexts;
      case StandingSignBlockEntity standingSignBlockEntity -> {
        final Boolean hitSide = StandingSignBlock.getHitSide(world.getBlockState(blockPos), blockHitResult);
        textContexts = hitSide == null ? null : standingSignBlockEntity.getTextsOnSide(hitSide);
      }
      case HungSignBlockEntity hungSignBlockEntity -> {
        textContexts = hungSignBlockEntity.texts.get(blockHitResult.getSide());
      }
      case null, default -> {
        source.sendError(Text.translatable("message.mishanguc.signPreset.save.not_sign"));
        return -1;
      }
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
    final Optional<Text> name = args == null ? Optional.empty() : args.get("name", TextCodecs.CODEC);
    final Optional<Text> description = args == null ? Optional.empty() : args.get("description", TextCodecs.CODEC);
    if (initialFocus < 0 || initialFocus >= textContextsCopy.size()) {
      source.sendError(Text.translatable("message.mishanguc.signPreset.save.initial_focus_invalid", initialFocus, textContextsCopy.size()));
      return -1;
    }
    final Thread thread = new Thread(() -> {
      final SignPreset.Info info = new SignPreset.Info(order, name, description, textContextsCopy, initialFocus);
      info.save(id, source, force);
      if (reload) {
        SignPresets.loadAll();
      }
    });
    thread.start();
    source.sendFeedback(Text.translatable("message.mishanguc.signPreset.save.start"));
    return 1;
  }
}
