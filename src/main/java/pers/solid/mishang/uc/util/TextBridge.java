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
    return new LiteralText(string);
  }

  static MutableText translatable(String key) {
    return new TranslatableText(key);
  }

  static MutableText translatable(String key, Object... args) {
    return new TranslatableText(key, args);
  }

  static MutableText empty() {
    return new LiteralText("");
  }

  static MutableText keybind(String string) {
    return new KeybindText(string);
  }


  static MutableText score(String name, String objective) {
    return new ScoreText(name, objective);
  }

  static MutableText selector(String pattern, Optional<Text> separator) {
    return new SelectorText(pattern, separator);
  }

  static boolean isEmpty(Text text) {
    return text instanceof final LiteralText literalText && literalText.getRawString().isEmpty();
  }
}
