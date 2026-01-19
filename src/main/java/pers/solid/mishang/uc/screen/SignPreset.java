package pers.solid.mishang.uc.screen;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.JsonOps;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;
import net.minecraft.client.MinecraftClient;
import net.minecraft.text.ClickEvent;
import net.minecraft.text.HoverEvent;
import net.minecraft.text.Text;
import net.minecraft.text.TextCodecs;
import net.minecraft.util.Formatting;
import net.minecraft.util.dynamic.Codecs;
import org.jspecify.annotations.Nullable;
import pers.solid.mishang.uc.text.TextContext;
import pers.solid.mishang.uc.util.TextBridge;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.InvalidPathException;
import java.nio.file.Path;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.UnaryOperator;

/**
 * 告示牌的文本预设。
 *
 * @param order        用于排序的索引，可以相同，默认为 0。
 * @param name         名称。
 * @param textContexts 文本的列表，每个元素是可将默认的文本（会因告示牌而异）添加内容的 {@link UnaryOperator}。
 * @param initialFocus 在告示牌界面中应用预设后，应将焦点选中在第几个元素上。
 */
public record SignPreset(int order, String id, Text name, @Nullable Text description, List<TextContext> textContexts, int initialFocus) {
  public SignPreset(int order, String id, List<TextContext> textContexts, int initialFocus) {
    this(order, id, TextBridge.translatable("signPreset.mishanguc." + id + ".name"), TextBridge.translatable("signPreset.mishanguc." + id + ".description"), textContexts, initialFocus);
  }

  public Info asInfo() {
    return new Info(order, Optional.of(name), Optional.ofNullable(description), textContexts, initialFocus);
  }

  /**
   * 用于存储在模组配置中的信息。注意：此信息用于与 json 文件对应，不存储名称，因为名称是通过文件名体现的。
   */
  public record Info(int order, Optional<Text> name, Optional<Text> description, List<TextContext> textContexts, int initialFocus) {
    public static final Codec<Info> CODEC = RecordCodecBuilder.<Info>create(i -> i.group(
        Codec.INT.optionalFieldOf("order", 0).forGetter(Info::order),
        TextCodecs.CODEC.optionalFieldOf("display_name").forGetter(Info::name),
        TextCodecs.CODEC.optionalFieldOf("description").forGetter(Info::description),
        TextContext.CODEC.listOf().fieldOf("text_contexts").forGetter(Info::textContexts),
        Codecs.rangedInt(0, Integer.MAX_VALUE).optionalFieldOf("initial_focus", 0).forGetter(Info::initialFocus)
    ).apply(i, Info::new)).comapFlatMap(o -> {
      if (o.initialFocus > 0 && o.initialFocus >= o.textContexts.size()) {
        return DataResult.error(() -> "initial focus (" + o.initialFocus + ") must be less than the size of text contexts (" + o.textContexts.size() + ")");
      } else {
        return DataResult.success(o);
      }
    }, Function.identity());

    /**
     * 将此预设存储在文件夹中。如果存储成功，则加入注册表。
     *
     * @param id    文件名称。
     * @param force 如果为 true，则会覆盖已知文件。
     */
    public void save(String id, FabricClientCommandSource source, boolean force) {
      final MinecraftClient client = source.getClient();
      try {
        Files.createDirectories(SignPresets.PATH);

        final Path filePath = SignPresets.PATH.resolve(id + ".json");
        if (!filePath.getParent().equals(SignPresets.PATH)) {
          client.execute(() -> source.sendError(Text.translatable("message.mishanguc.signPreset.save.invalid_name", id)));
          return;
        }
        if (!force && (Files.exists(filePath) || SignPresets.BUILTIN.containsKey(id))) {
          final String newCommand = "/mishanguc:signpreset save " + id + " {force:true}";
          client.execute(() -> source.sendError(Text.translatable("message.mishanguc.signPreset.save.fail.already_exist", Text.literal(newCommand).styled(style -> style
              .withUnderline(true)
              .withColor(Formatting.AQUA)
              .withHoverEvent(new HoverEvent.ShowText(Text.translatable("message.mishanguc.signPreset.save.fail.already_exist.click_to_type")))
              .withClickEvent(new ClickEvent.SuggestCommand(newCommand))))));
          return;
        }

        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        final DataResult<JsonElement> result = CODEC.encodeStart(JsonOps.INSTANCE, this);
        if (result.isError()) {
          client.execute(() -> source.sendError(Text.translatable("message.mishanguc.signPreset.save.fail.encoding_failure")));
          SignPresets.LOGGER.error("Failed to encode sign preset {}: {}", id, result.error().orElseThrow().message());
          return;
        }
        try (FileWriter writer = new FileWriter(filePath.toFile())) {
          gson.toJson(result.result().orElseThrow(), writer);
        }
        client.execute(() -> {
          SignPresets.register(create(id));
          source.sendFeedback(Text.translatable("message.mishanguc.signPreset.save.success", Text.literal(filePath.getFileName().toString()).styled(style -> style
              .withUnderline(true)
              .withColor(Formatting.YELLOW)
              .withHoverEvent(new HoverEvent.ShowText(Text.translatable("message.mishanguc.signPreset.save.success.click_to_open")))
              .withClickEvent(new ClickEvent.OpenFile(filePath)))));
        });

      } catch (InvalidPathException e) {
        client.execute(() -> source.sendError(Text.translatable("message.mishanguc.signPreset.save.invalid_name", id)));
        SignPresets.LOGGER.error("Invalid path {}: {}", id, e.getMessage());
      } catch (IOException e) {
        client.execute(() -> source.sendError(Text.translatable("message.mishanguc.signPreset.save.fail.unknown")));
        SignPresets.LOGGER.error("Failed to save sign preset {}:", id, e);
      }
    }

    /**
     * 从磁盘中删除此告示牌预设。如果删除成功，则在注册表中进行相应操作（删除或重置为默认）。
     *
     * @param id            告示牌调取的 id。
     * @param hideIfBuiltin 若为 true，对于内置告示牌预设，将存储一个空白 json 以表示在告示牌编辑界面中不呈现；若为 false，则直接删除文件，内置告示牌预设将在告示牌编辑界面中以默认状态呈现。
     */
    public void delete(String id, FabricClientCommandSource source, boolean hideIfBuiltin) {
      final MinecraftClient client = source.getClient();

      try {
        Files.createDirectories(SignPresets.PATH);

        final Path filePath = SignPresets.PATH.resolve(id + ".json");
        if (!filePath.getParent().equals(SignPresets.PATH)) {
          client.execute(() -> source.sendError(Text.translatable("message.mishanguc.signPreset.save.invalid_name", id)));
          return;
        }

        final boolean isBuiltin = SignPresets.BUILTIN.containsKey(id);

        Files.deleteIfExists(filePath);

        if (isBuiltin && hideIfBuiltin) {
          Gson gson = new GsonBuilder().setPrettyPrinting().create();
          try (FileWriter writer = new FileWriter(filePath.toFile())) {
            gson.toJson(new JsonObject(), writer);
          }
        }

        client.execute(() -> {
          if (hideIfBuiltin) {
            SignPresets.unregister(id);
          } else {
            SignPresets.reset(id);
          }
          source.sendFeedback(Text.translatable("message.mishanguc.signPreset.delete.success", Text.literal(filePath.getFileName().toString()).formatted(Formatting.YELLOW)));
        });
      } catch (InvalidPathException e) {
        client.execute(() -> source.sendError(Text.translatable("message.mishanguc.signPreset.save.invalid_name", id)));
        SignPresets.LOGGER.error("Invalid path when deleting sign preset {}: {}", id, e.getMessage());
      } catch (IOException e) {
        client.execute(() -> source.sendError(Text.translatable("message.mishanguc.signPreset.save.delete.unknown")));
        SignPresets.LOGGER.error("Failed to delete sign preset {}:", id, e);
      }
    }

    public SignPreset create(String id) {
      return new SignPreset(order, id, name.orElseGet(() -> Text.translatableWithFallback("signPreset.mishanguc." + id + ".name", id)), description.orElse(null), textContexts, initialFocus);
    }
  }
}
