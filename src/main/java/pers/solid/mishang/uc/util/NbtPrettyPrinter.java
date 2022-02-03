package pers.solid.mishang.uc.util;

import com.google.common.base.Strings;
import net.minecraft.nbt.*;
import net.minecraft.text.*;
import net.minecraft.util.Formatting;
import pers.solid.mishang.uc.mixin.NbtCompoundAccessor;

import java.util.Iterator;
import java.util.Map;
import java.util.function.UnaryOperator;

public final class NbtPrettyPrinter {

  public static Text serialize(NbtElement element) {
    return serialize(element, 0, "  ", 1);
  }

  /**
   * These methods prettify an {@link NbtElement} to a {@link Text} in a way that is better than
   * {@link NbtHelper#toPrettyPrintedText(NbtElement)}. It dispatches to other methods by detecting
   * and casting the type of <code>element</code> arg.
   *
   * @param element The NBT element.
   * @param layer Usually 0. If it's shown in another compound or list, the layer is 1. The greater
   *     the layer, the shorter the prettified serialized result represents.
   * @param indent Indention to prettify the NBT tag. Usually it's two spaces.
   * @param depth Usually 0. To indent as a sub=indention of a compound or list.
   * @return The prettified serialized text.
   * @see NbtHelper#toPrettyPrintedText(NbtElement)
   */
  public static Text serialize(NbtElement element, int layer, String indent, int depth) {
    if (element instanceof NbtCompound) {
      return serialize(((NbtCompound) element), layer, indent, depth);
    } else if (element instanceof NbtList) {
      return serialize(((NbtList) element), layer, indent, depth);
    } else if (element instanceof NbtString) {
      return serialize(((NbtString) element), layer);
    }
    return NbtHelper.toPrettyPrintedText(element);
  }

  /** @see NbtHelper#toPrettyPrintedText */
  public static Text serialize(NbtCompound compound, int layer, String indent, int depth) {
    final Map<String, NbtElement> entries = ((NbtCompoundAccessor) compound).getEntries();
    switch (layer) {
      case 0 -> {
        // 第0层的情况，整个数据的前一部分会完整展示。每一项显示在单独一行。
        int n = 0;
        MutableText text = new LiteralText("");
        if (compound.isEmpty()) {
          return text.append(Strings.repeat(indent, depth))
              .append(new TranslatableText("debug.mishanguc.nbt.compound_empty"));
        }
        text.append("{");
        for (Iterator<Map.Entry<String, NbtElement>> iterator = entries.entrySet().iterator();
             iterator.hasNext(); ) {
          text.append(
              n <= 0
                  ? Strings.repeat(indent, depth).replaceAll(" $", "")
                  : Strings.repeat(indent, depth));
          Map.Entry<String, NbtElement> entry = iterator.next();
          text.append(new LiteralText(entry.getKey()).formatted(Formatting.AQUA)).append(": ");
          text.append(serialize(entry.getValue(), layer + 1, indent, depth + 1));
          if (iterator.hasNext()) {
            text.append(",");
            if (!indent.isEmpty()) {
              text.append("\n");
            }
          }
          if (n >= 7) {
            // 超过8个元素时，折叠这些元素，放到新的复合标签中。
            NbtCompound remains = new NbtCompound();
            final Map<String, NbtElement> remainsEntries =
                ((NbtCompoundAccessor) remains).getEntries();
            while (iterator.hasNext()) {
              final Map.Entry<String, NbtElement> next = iterator.next();
              remainsEntries.put(next.getKey(), next.getValue());
            }
            text.append(
                new TranslatableText("debug.mishanguc.nbt.compound_eclipse", entries.size() - n)
                    .formatted(Formatting.GRAY)
                    .styled(
                        style ->
                            style
                                .withClickEvent(new ExtendedClickEvent(serialize(remains)))
                                .withHoverEvent(
                                    new HoverEvent(
                                        HoverEvent.Action.SHOW_TEXT,
                                        new TranslatableText(
                                            "debug.mishanguc.nbt.compound_display_remains")))));
            break;
          }
          n++;
        }
        return text.append(" }");
      }
      case 1 -> {
        // 第1层的情况，应简短显示，尽可能保持在两行以内。
        final int size = compound.getSize();
        MutableText text = new LiteralText("{");
        int n = 0;
        for (Iterator<Map.Entry<String, NbtElement>> iterator = entries.entrySet().iterator();
             iterator.hasNext(); ) {
          Map.Entry<String, NbtElement> entry = iterator.next();
          text.append(
                  new LiteralText(entry.getKey())
                      .styled(style -> style.withColor(TextColor.fromRgb(0x99ffff))))
              .append(": ");
          // 如果该元素为唯一元素，则layer不+1。
          text.append(
              serialize(entry.getValue(), size <= 1 ? layer : layer + 1, indent, depth + 1));
          n++;
          if (iterator.hasNext()) {
            text.append(", ");
          }
          if (n >= 5) {
            text.append(
                new TranslatableText(" ")
                    .formatted(Formatting.GRAY)
                    .append(
                        new TranslatableText(
                            "debug.mishanguc.nbt.compound_total", entries.size()))
                    .styled(
                        style ->
                            style
                                .withHoverEvent(
                                    new HoverEvent(
                                        HoverEvent.Action.SHOW_TEXT,
                                        new TranslatableText(
                                            "debug.mishanguc.nbt.compound_display_full")))
                                .withClickEvent(new ExtendedClickEvent(serialize(compound)))));
            break;
          }
        }
        if (1 < n && n < 5) {
          text.append(
              new TranslatableText(" ")
                  .formatted(Formatting.GRAY)
                  .append(
                      new TranslatableText("debug.mishanguc.nbt.compound_expand", entries.size()))
                  .styled(
                      style ->
                          style
                              .withHoverEvent(
                                  new HoverEvent(
                                      HoverEvent.Action.SHOW_TEXT,
                                      new TranslatableText(
                                          "debug.mishanguc.nbt.compound_display_full")))
                              .withClickEvent(new ExtendedClickEvent(serialize(compound)))));
        }
        return text.append("}");
      }
      default -> {
        MutableText text = new LiteralText("{");
        if (compound.getSize() > 1) {
          text.append(
              new TranslatableText("debug.mishanguc.nbt.compound_brief", compound.getSize())
                  .formatted(Formatting.GRAY)
                  .styled(
                      style ->
                          style
                              .withHoverEvent(
                                  new HoverEvent(
                                      HoverEvent.Action.SHOW_TEXT,
                                      new TranslatableText(
                                          "debug.mishanguc.nbt.compound_display_full")))
                              .withClickEvent(new ExtendedClickEvent(serialize(compound)))));
        }
        text.append("}");
        return text;
      }
    }
  }

  /** @see NbtHelper#toPrettyPrintedText */
  public static Text serialize(NbtList nbtList, int layer, String indent, int depth) {
    switch (layer) {
      case 0 -> {
        MutableText text = new LiteralText("");
        int n = 0;
        if (nbtList.isEmpty()) {
          return text.append(Strings.repeat(indent, depth))
              .append(new TranslatableText("debug.mishanguc.nbt.list_empty"));
        }
        text.append("[");
        for (Iterator<NbtElement> iterator = nbtList.iterator(); iterator.hasNext(); ) {
          text.append(
              n <= 0
                  ? Strings.repeat(indent, depth).replaceAll(" $", "")
                  : Strings.repeat(indent, depth));
          NbtElement nbtElement = iterator.next();
          text.append(serialize(nbtElement, layer + 1, indent, depth + 1));
          if (iterator.hasNext()) {
            text.append(",");
            if (!indent.isEmpty()) {
              text.append("\n");
            }
          }
          if (n >= 7) {
            NbtList remains = new NbtList();
            while (iterator.hasNext()) {
              remains.add(iterator.next());
            }
            text.append(
                new TranslatableText("debug.mishanguc.nbt.list_eclipse", nbtList.size() - n)
                    .formatted(Formatting.GRAY)
                    .styled(
                        style ->
                            style
                                .withHoverEvent(
                                    new HoverEvent(
                                        HoverEvent.Action.SHOW_TEXT,
                                        new TranslatableText(
                                            "debug.mishanguc.nbt.list_display_remains")))
                                .withClickEvent(new ExtendedClickEvent(serialize(remains)))));
            return text;
          }
          n++;
        }
        return text.append(" ]");
      }
      case 1 -> {
        MutableText text = new LiteralText("[");
        final int size = nbtList.size();
        int n = 0;
        for (Iterator<NbtElement> iterator = nbtList.iterator(); iterator.hasNext(); ) {
          NbtElement nbtElement = iterator.next();
          text.append(serialize(nbtElement, layer + (size <= 1 ? 0 : 1), indent, depth + 1));
          n++;
          if (iterator.hasNext()) {
            text.append(", ");
          }
          if (n >= 5) {
            text.append(
                new LiteralText(" ")
                    .formatted(Formatting.GRAY)
                    .append(
                        new TranslatableText("debug.mishanguc.nbt.list_total", nbtList.size()))
                    .styled(
                        style ->
                            style
                                .withHoverEvent(
                                    new HoverEvent(
                                        HoverEvent.Action.SHOW_TEXT,
                                        new TranslatableText(
                                            "debug.mishanguc.nbt.list_display_full")))
                                .withClickEvent(new ExtendedClickEvent(serialize(nbtList)))));
            break;
          }
        }
        if (n < 5 && n > 1) {
          text.append(
              new LiteralText(" ")
                  .formatted(Formatting.GRAY)
                  .append(new TranslatableText("debug.mishanguc.nbt.list_expand", nbtList.size()))
                  .styled(
                      style ->
                          style
                              .withHoverEvent(
                                  new HoverEvent(
                                      HoverEvent.Action.SHOW_TEXT,
                                      new TranslatableText(
                                          "debug.mishanguc.nbt.list_display_full")))
                              .withClickEvent(new ExtendedClickEvent(serialize(nbtList)))));
        }
        text.append("]");
        return text;
      }
      default -> {
        MutableText text = new LiteralText("[");
        if (nbtList.size() > 1) {
          text.append(
              new TranslatableText("debug.mishanguc.nbt.list_brief", nbtList.size())
                  .formatted(Formatting.GRAY)
                  .styled(
                      style ->
                          style
                              .withHoverEvent(
                                  new HoverEvent(
                                      HoverEvent.Action.SHOW_TEXT,
                                      new TranslatableText(
                                          "debug.mishanguc.nbt.list_display_full")))
                              .withClickEvent(new ExtendedClickEvent(serialize(nbtList)))));
        }
        text.append("]");
        return text;
      }
    }
  }

  public static Text serialize(NbtString nbtString, int layer) {
    final String string = nbtString.asString();
    final UnaryOperator<Style> strStyle = style -> style.withColor(TextColor.fromRgb(0xcccccc));
    if (layer == 0) {
      return new LiteralText(string).styled(strStyle);
    } else if (layer == 1) {
      return string.length() > 160
          ? new LiteralText(string.substring(0, 155))
              .styled(strStyle)
              .append(new TranslatableText("debug.mishanguc.nbt.string.eclipse", string.length()))
          : new LiteralText(string).styled(strStyle);
    } else {
      return string.length() > 40
          ? new LiteralText(string.substring(0, 35)).styled(strStyle).append("……")
          : new LiteralText(string).styled(strStyle);
    }
  }
}
