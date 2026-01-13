package pers.solid.mishang.uc.screen;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.JsonOps;
import net.fabricmc.loader.api.FabricLoader;
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
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

public final class SignPresets {
  public static final Logger LOGGER = LoggerFactory.getLogger("Mishang Urban Construction/Sign Presets");
  public static final Path PATH = FabricLoader.getInstance().getConfigDir().resolve("mishanguc_sign_presets");
  private static final Map<String, SignPreset> REGISTRY = new LinkedHashMap<>();

  // region text entries
  private static final TextContext DEFAULT_TEXT = new TextContext();
  private static final TextContext HALF_SIZE_TEXT = Util.make(new TextContext(), textContext -> {
    textContext.size /= 2;
  });
  private static final TextContext ARROW_LEFT = Util.make(new TextContext(), textContext -> {
    textContext.size += 2;
    textContext.extra = new PatternSpecialDrawable(textContext, RectanglePatterns.ARROW_LEFT);
    textContext.offsetX = -4;
    textContext.absolute = true;
  });
  private static final TextContext DEFAULT_TEXT_LEFT = Util.make(new TextContext(), textContext -> {
    textContext.offsetX = 8;
    textContext.horizontalAlign = HorizontalAlign.LEFT;
  });
  private static final TextContext HALF_SIZE_TEXT_LEFT = Util.make(new TextContext(), textContext -> {
    textContext.offsetX = 8;
    textContext.horizontalAlign = HorizontalAlign.LEFT;
    textContext.size /= 2;
  });
  private static final TextContext ARROW_RIGHT = Util.make(new TextContext(), textContext -> {
    textContext.size += 2;
    textContext.extra = new PatternSpecialDrawable(textContext, RectanglePatterns.ARROW_RIGHT);
    textContext.offsetX = 4;
    textContext.absolute = true;
  });
  private static final TextContext DEFAULT_TEXT_RIGHT = Util.make(new TextContext(), textContext -> {
    textContext.offsetX = -8;
    textContext.horizontalAlign = HorizontalAlign.RIGHT;
  });
  private static final TextContext HALF_SIZE_TEXT_RIGHT = Util.make(new TextContext(), textContext -> {
    textContext.offsetX = -8;
    textContext.horizontalAlign = HorizontalAlign.RIGHT;
    textContext.size /= 2;
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

  private static void registerAll() {
    // 确保顺序不被打乱
    register(LEFT_ARROW_ONE_LINE);
    register(ONE_LINE);
    register(RIGHT_ARROW_ONE_LINE);
    register(LEFT_ARROW_TWO_LINES);
    register(TWO_LINES);
    register(RIGHT_ARROW_TWO_LINES);
  }

  public static int loadAll() {
    LOGGER.info("Loading Mishang Urban Construction Sign Presets");
    REGISTRY.clear();
    registerAll();

    Gson gson = new GsonBuilder().setPrettyPrinting().create();
    MutableInt counter = new MutableInt();
    if (Files.exists(PATH)) {
      try (final Stream<Path> stream = Files.walk(PATH)) {
        stream
            .filter(Files::isRegularFile)
            .filter(path -> path.getFileName().toString().endsWith(".json"))
            .forEach(path -> {
              final String name = PathUtils.getBaseName(path); // 不带扩展名的文件名
              try (final FileReader reader = new FileReader(path.toFile())) {
                final JsonElement element = gson.fromJson(reader, JsonElement.class);
                final DataResult<SignPreset.Info> parse = SignPreset.Info.CODEC.parse(JsonOps.INSTANCE, element);
                if (parse.isSuccess()) {
                  counter.increment();
                }
                parse.ifSuccess(info -> register(info.create(name)));
                parse.ifError(error -> LOGGER.warn("Failed to parse sign preset JSON: {}", error.message()));
              } catch (IOException e) {
                LOGGER.error("Error reading file {}", path, e);
              }
            });
      } catch (IOException e) {
        LOGGER.error("Error reading file {}", PATH, e);
        return -1;
      }

      if (counter.intValue() > 0) {
        LOGGER.info("Mishang Urban Construction: Loaded {} custom sign presets.", counter.intValue());
      }
    }
    return counter.intValue();
  }

  public static SignPreset register(SignPreset preset) {
    REGISTRY.put(preset.id(), preset);
    return preset;
  }

  /**
   * 按指定的顺序排序已注册的所有告示牌预设的流。
   */
  public static Stream<SignPreset> streamValues() {
    return REGISTRY.values().stream().sorted(Comparator.comparingInt(SignPreset::order));
  }
}
