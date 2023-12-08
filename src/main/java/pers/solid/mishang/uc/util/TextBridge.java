package pers.solid.mishang.uc.util;

import net.minecraft.text.*;
import org.jetbrains.annotations.ApiStatus;

import java.util.Optional;

/**
 * 实用类，用于在不同版本之间减少代码差异。
 */
@SuppressWarnings("OptionalUsedAsFieldOrParameterType")
@ApiStatus.AvailableSince("0.2.4")
@ApiStatus.NonExtendable
public interface TextBridge extends Text {
  static MutableText literal(String string) {
    return Text.literal(string);
  }

  static MutableText translatable(String key) {
    return Text.translatable(key);
  }

  static MutableText translatable(String key, Object... args) {
    return Text.translatable(key, args);
  }

  static MutableText empty() {
    return Text.empty();
  }

  static MutableText keybind(String string) {
    return Text.keybind(string);
  }

  static MutableText nbt(String rawPath, boolean interpret, Optional<Text> separator, NbtDataSource dataSource) {
    return Text.nbt(rawPath, interpret, separator, dataSource);
  }

  static MutableText score(String name, String objective) {
    return Text.score(name, objective);
  }

  static MutableText selector(String pattern, Optional<Text> separator) {
    return Text.selector(pattern, separator);
  }

  static boolean isEmpty(Text text) {
    final TextContent content = text.getContent();
    return content == PlainTextContent.EMPTY || content instanceof final PlainTextContent plainTextContent && plainTextContent.string().isEmpty();
  }
}
