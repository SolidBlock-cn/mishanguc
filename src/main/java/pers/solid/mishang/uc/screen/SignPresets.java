package pers.solid.mishang.uc.screen;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.mojang.brigadier.suggestion.SuggestionProvider;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.JsonOps;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.MinecraftClient;
import net.minecraft.command.CommandSource;
import net.minecraft.nbt.NbtString;
import net.minecraft.util.Util;
import org.apache.commons.io.file.PathUtils;
import org.apache.commons.lang3.mutable.MutableInt;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pers.solid.mishang.uc.text.PatternSpecialDrawable;
import pers.solid.mishang.uc.text.RectanglePatterns;
import pers.solid.mishang.uc.text.TextContext;
import pers.solid.mishang.uc.util.HorizontalAlign;

import java.io.FileReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

@Environment(EnvType.CLIENT)
public final class SignPresets {
  public static final Logger LOGGER = LoggerFactory.getLogger("Mishang Urban Construction/Sign Presets");
  /**
   * 存储告示版预设文件的路径。所有预设文件都是 .json 结尾，且不支持子文件夹。
   */
  public static final Path PATH = FabricLoader.getInstance().getConfigDir().resolve("mishanguc_sign_presets");
  private static final Map<String, SignPreset> REGISTRY = new LinkedHashMap<>();
  /**
   * 模组内置的、非通过文件加载的告示牌预设的 id。
   */
  static final Map<String, SignPreset> BUILTIN = new LinkedHashMap<>();
  /**
   * 在命令中提供告示牌预设 id 的建议。
   */
  public static final SuggestionProvider<FabricClientCommandSource> SUGGEST_KEYS = (commandContext, suggestionsBuilder) -> CommandSource.suggestMatching(REGISTRY.keySet().stream().map(NbtString::escape), suggestionsBuilder);
  /**
   * 在命令中提供告示牌预设 id 包括内置预设 id（可能实际已从注册表中移除）的建议。
   */
  public static final SuggestionProvider<FabricClientCommandSource> SUGGEST_KEYS_AND_BUILTIN = (commandContext, suggestionsBuilder) -> CommandSource.suggestMatching(Stream.concat(REGISTRY.keySet().stream(), BUILTIN.keySet().stream()).distinct().map(NbtString::escape), suggestionsBuilder);

  // region text entries
  private static final TextContext DEFAULT_TEXT = Util.make(new TextContext(), textContext -> textContext.size = 6);
  private static final TextContext HALF_SIZE_TEXT = Util.make(new TextContext(), textContext -> textContext.size = 3);
  private static final TextContext ARROW_LEFT = Util.make(new TextContext(), textContext -> {
    textContext.size = 8;
    textContext.extra = new PatternSpecialDrawable(textContext, RectanglePatterns.ARROW_LEFT);
    textContext.offsetX = -4;
    textContext.absolute = true;
  });
  private static final TextContext DEFAULT_TEXT_LEFT = Util.make(new TextContext(), textContext -> {
    textContext.size = 6;
    textContext.offsetX = 8;
    textContext.horizontalAlign = HorizontalAlign.LEFT;
  });
  private static final TextContext HALF_SIZE_TEXT_LEFT = Util.make(new TextContext(), textContext -> {
    textContext.offsetX = 8;
    textContext.horizontalAlign = HorizontalAlign.LEFT;
    textContext.size = 3;
  });
  private static final TextContext ARROW_RIGHT = Util.make(new TextContext(), textContext -> {
    textContext.size = 8;
    textContext.extra = new PatternSpecialDrawable(textContext, RectanglePatterns.ARROW_RIGHT);
    textContext.offsetX = 4;
    textContext.absolute = true;
  });
  private static final TextContext DEFAULT_TEXT_RIGHT = Util.make(new TextContext(), textContext -> {
    textContext.size = 6;
    textContext.offsetX = -8;
    textContext.horizontalAlign = HorizontalAlign.RIGHT;
  });
  private static final TextContext HALF_SIZE_TEXT_RIGHT = Util.make(new TextContext(), textContext -> {
    textContext.offsetX = -8;
    textContext.horizontalAlign = HorizontalAlign.RIGHT;
    textContext.size = 3;
  });
  //endregion text entries

  //region presets
  public static final SignPreset LEFT_ARROW_ONE_LINE = new SignPreset(-6, "left_arrow_one_line", List.of(ARROW_LEFT, DEFAULT_TEXT_LEFT), 1);
  public static final SignPreset ONE_LINE = new SignPreset(-5, "one_line", List.of(DEFAULT_TEXT), 0);
  public static final SignPreset RIGHT_ARROW_ONE_LINE = new SignPreset(-4, "right_arrow_one_line", List.of(ARROW_RIGHT, DEFAULT_TEXT_RIGHT), 1);
  public static final SignPreset LEFT_ARROW_TWO_LINES = new SignPreset(-3, "left_arrow_two_lines", List.of(ARROW_LEFT, DEFAULT_TEXT_LEFT, HALF_SIZE_TEXT_LEFT), 1);
  public static final SignPreset TWO_LINES = new SignPreset(-2, "two_lines", List.of(DEFAULT_TEXT, HALF_SIZE_TEXT), 0);
  public static final SignPreset RIGHT_ARROW_TWO_LINES = new SignPreset(-1, "right_arrow_two_lines", List.of(ARROW_RIGHT, DEFAULT_TEXT_RIGHT, HALF_SIZE_TEXT_RIGHT), 1);
  //endregion presets

  private static void registerBuiltins() {
    // 确保顺序不被打乱
    registerBuiltin(LEFT_ARROW_ONE_LINE);
    registerBuiltin(ONE_LINE);
    registerBuiltin(RIGHT_ARROW_ONE_LINE);
    registerBuiltin(LEFT_ARROW_TWO_LINES);
    registerBuiltin(TWO_LINES);
    registerBuiltin(RIGHT_ARROW_TWO_LINES);
  }

  static {
    registerBuiltins();
  }

  /**
   * 清空注册表并恢复为内置状态，仅加载内置的。
   */
  public static void resetToBuiltin() {
    REGISTRY.clear();
    REGISTRY.putAll(BUILTIN);
  }

  public static int loadAll() {
    LOGGER.info("Loading Mishang Urban Construction Sign Presets");
    resetToBuiltin();

    Gson gson = new GsonBuilder().setPrettyPrinting().create();
    MutableInt counter = new MutableInt();
    if (Files.exists(PATH)) {
      try (final Stream<Path> stream = Files.walk(PATH)) {
        stream
            .filter(Files::isRegularFile)
            .filter(path -> path.getFileName().toString().endsWith(".json"))
            .forEach(path -> {
              final String id = PathUtils.getBaseName(path); // 不带扩展名的文件名
              try (final FileReader reader = new FileReader(path.toFile())) {
                final JsonElement element = gson.fromJson(reader, JsonElement.class);

                if (element instanceof JsonObject jsonObject && jsonObject.isEmpty()) {
                  // 表示这是一个空的告示牌预设，如果是内置的则将其删除。
                  REGISTRY.remove(id);
                  return;
                }

                final DataResult<SignPreset.Info> parse = SignPreset.Info.CODEC.parse(JsonOps.INSTANCE, element);
                if (parse.isSuccess()) {
                  counter.increment();
                }
                parse.ifSuccess(info -> MinecraftClient.getInstance().execute(() -> register(info.create(id))));
                parse.ifError(error -> LOGGER.warn("Failed to parse sign preset JSON for {}: {}", path.getFileName(), error.message()));
              } catch (Throwable e) {
                LOGGER.error("Error reading file {}", path, e);
              }
            });
      } catch (Throwable e) {
        LOGGER.error("Error reading file {}", PATH, e);
        return -1;
      }

      if (counter.intValue() > 0) {
        LOGGER.info("Mishang Urban Construction: Loaded {} custom sign presets.", counter.intValue());
      }
    }
    return counter.intValue();
  }

  public static void register(SignPreset preset) {
    REGISTRY.put(preset.id(), preset);
  }

  private static void registerBuiltin(SignPreset preset) {
    BUILTIN.put(preset.id(), preset);
  }

  public static void unregister(String id) {
    REGISTRY.remove(id);
  }

  public static void reset(String id) {
    REGISTRY.remove(id);
    if (BUILTIN.containsKey(id)) {
      REGISTRY.put(id, BUILTIN.get(id));
    }
  }

  public static SignPreset get(String id) {
    return REGISTRY.get(id);
  }

  public static SignPreset getOrBuiltin(String id) {
    SignPreset signPreset = REGISTRY.get(id);
    if (signPreset == null) {
      signPreset = BUILTIN.get(id);
    }
    return signPreset;
  }

  /**
   * 按指定的顺序排序已注册的所有告示牌预设的流。
   */
  public static Stream<SignPreset> streamValues() {
    return REGISTRY.values().stream().sorted(Comparator.comparingInt(SignPreset::order));
  }
}
