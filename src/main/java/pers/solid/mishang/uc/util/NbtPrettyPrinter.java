package pers.solid.mishang.uc.util;

import com.google.common.base.Strings;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.ChatFormatting;
import net.minecraft.nbt.*;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.HoverEvent;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.Style;
import pers.solid.mishang.uc.mixin.NbtCompoundAccessor;

import java.util.Iterator;
import java.util.Map;
import java.util.function.UnaryOperator;

/**
 * <p>This class is used to pretty-print any NBT data, for {@link pers.solid.mishang.uc.item.DataTagToolItem}.</p>
 * <p><b>Note: </b>This class is <i>client-side only</i> since 0.1.7, as it uses client-side {@link NbtClickEvent}. It means that the server does not pretty-print any NBT.</p>
 * <p>You can let the server send the NBT through a packet, and let the client receive the packet and present NBT.</p>
 */
@Environment(EnvType.CLIENT)
public final class NbtPrettyPrinter {
  public static Component serialize(Tag element) {
    return serialize(element, 0, "  ", 1);
  }

  /**
   * These methods prettify an {@link Tag} to a {@link Component} in a way that is better than
   * {@link NbtUtils#toPrettyComponent(Tag)}. It dispatches to other methods by detecting and casting
   * the type of <code>element</code> arg.
   *
   * @param element The NBT element.
   * @param layer   Usually 0. If it's shown in another compound or list, the layer is 1. The greater
   *                the layer, the shorter the prettified serialized result represents.
   * @param indent  Indention to prettify the NBT tag. Usually it's two spaces.
   * @param depth   Usually 0. To indent as a sub=indention of a compound or list.
   * @return The prettified serialized text.
   * @see NbtUtils#toPrettyComponent(Tag)
   */
  public static Component serialize(Tag element, int layer, String indent, int depth) {
    if (element instanceof final CompoundTag nbtCompound) {
      return serialize(nbtCompound, layer, indent, depth);
    } else if (element instanceof final ListTag nbtList) {
      return serialize(nbtList, layer, indent, depth);
    } else if (element instanceof final StringTag nbtString) {
      return serialize(nbtString, layer);
    } else if (element instanceof final ByteTag nbtByte) {
      return TextBridge.literal(String.valueOf(nbtByte.byteValue())).append(TextBridge.literal("b").withStyle(ChatFormatting.GRAY));
    } else if (element instanceof final ShortTag nbtShort) {
      return TextBridge.literal(String.valueOf(nbtShort.shortValue())).append(TextBridge.literal("s").withStyle(ChatFormatting.GRAY));
    } else if (element instanceof final IntTag nbtInt) {
      return TextBridge.literal(String.valueOf(nbtInt.intValue()));
    } else if (element instanceof final LongTag nbtLong) {
      return TextBridge.literal(String.valueOf(nbtLong.longValue())).append(TextBridge.literal("l").withStyle(ChatFormatting.GRAY));
    } else if (element instanceof final FloatTag nbtFloat) {
      return TextBridge.literal(String.valueOf(nbtFloat.floatValue())).append(TextBridge.literal("f").withStyle(ChatFormatting.GRAY));
    } else if (element instanceof final DoubleTag nbtDouble) {
      return TextBridge.literal(String.valueOf(nbtDouble.doubleValue())).append(TextBridge.literal("d").withStyle(ChatFormatting.GRAY));
    }
    return NbtUtils.toPrettyComponent(element);
  }

  /**
   * @see NbtUtils#toPrettyComponent
   */
  public static Component serialize(CompoundTag compound, int layer, String indent, int depth) {
    final Map<String, Tag> entries = ((NbtCompoundAccessor) (Object) compound).getEntries();
    switch (layer) {
      case 0 -> {
        // 第0层的情况，整个数据的前一部分会完整展示。每一项显示在单独一行。
        int n = 0;
        MutableComponent text = TextBridge.literal("");
        if (compound.isEmpty()) {
          return text.append(Strings.repeat(indent, depth))
              .append(TextBridge.translatable("debug.mishanguc.nbt.compound_empty"));
        }
        text.append("{");
        for (Iterator<Map.Entry<String, Tag>> iterator = entries.entrySet().iterator();
             iterator.hasNext(); ) {
          text.append(
              n <= 0
                  ? Strings.repeat(indent, depth).replaceAll(" $", "")
                  : Strings.repeat(indent, depth));
          Map.Entry<String, Tag> entry = iterator.next();
          text.append(TextBridge.literal(entry.getKey()).withStyle(ChatFormatting.AQUA)).append(": ");
          text.append(serialize(entry.getValue(), layer + 1, indent, depth + 1));
          if (iterator.hasNext()) {
            text.append(",");
            if (!indent.isEmpty()) {
              text.append("\n");
            }
          }
          if (n >= 7) {
            // 超过8个元素时，折叠这些元素，放到新的复合标签中。
            CompoundTag remains = new CompoundTag();
            final Map<String, Tag> remainsEntries = ((NbtCompoundAccessor) (Object) remains).getEntries();
            while (iterator.hasNext()) {
              final Map.Entry<String, Tag> next = iterator.next();
              remainsEntries.put(next.getKey(), next.getValue());
            }
            text.append(TextBridge.translatable("debug.mishanguc.nbt.compound_eclipse", entries.size() - n)
                .withStyle(ChatFormatting.GRAY)
                .withStyle(style -> style
                    .withClickEvent(new NbtClickEvent(remains))
                    .withHoverEvent(new HoverEvent.ShowText(TextBridge.translatable("debug.mishanguc.nbt.compound_display_remains")))));
            break;
          }
          n++;
        }
        return text.append(" }");
      }
      case 1 -> {
        // 第1层的情况，应简短显示，尽可能保持在两行以内。
        final int size = compound.size();
        MutableComponent text = TextBridge.literal("{");
        int n = 0;
        for (Iterator<Map.Entry<String, Tag>> iterator = entries.entrySet().iterator();
             iterator.hasNext(); ) {
          Map.Entry<String, Tag> entry = iterator.next();
          text.append(TextBridge.literal(entry.getKey())
                  .withStyle(style -> style.withColor(0x99ffff)))
              .append(": ");
          // 如果该元素为唯一元素，则layer不+1。
          text.append(
              serialize(entry.getValue(), size <= 1 ? layer : layer + 1, indent, depth + 1));
          n++;
          if (iterator.hasNext()) {
            text.append(", ");
          }
          if (n >= 5) {
            text.append(TextBridge.translatable(" ")
                .withStyle(ChatFormatting.GRAY)
                .append(TextBridge.translatable("debug.mishanguc.nbt.compound_total", entries.size()))
                .withStyle(style -> style
                    .withHoverEvent(new HoverEvent.ShowText(TextBridge.translatable("debug.mishanguc.nbt.compound_display_full")))
                    .withClickEvent(new NbtClickEvent(compound))));
            break;
          }
        }
        if (1 < n && n < 5) {
          text.append(
              TextBridge.translatable(" ")
                  .withStyle(ChatFormatting.GRAY)
                  .append(TextBridge.translatable("debug.mishanguc.nbt.compound_expand", entries.size()))
                  .withStyle(style -> style.withHoverEvent(
                          new HoverEvent.ShowText(TextBridge.translatable("debug.mishanguc.nbt.compound_display_full")))
                      .withClickEvent(new NbtClickEvent(compound))));
        }
        return text.append("}");
      }
      default -> {
        MutableComponent text = TextBridge.literal("{");
        if (compound.size() > 1) {
          text.append(
              TextBridge.translatable("debug.mishanguc.nbt.compound_brief", compound.size())
                  .withStyle(ChatFormatting.GRAY)
                  .withStyle(style -> style
                      .withHoverEvent(new HoverEvent.ShowText(TextBridge.translatable(
                          "debug.mishanguc.nbt.compound_display_full")))
                      .withClickEvent(new NbtClickEvent(compound))));
        }
        text.append("}");
        return text;
      }
    }
  }

  /**
   * @see NbtUtils#toPrettyComponent
   */
  public static Component serialize(ListTag nbtList, int layer, String indent, int depth) {
    switch (layer) {
      case 0 -> {
        MutableComponent text = TextBridge.literal("");
        int n = 0;
        if (nbtList.isEmpty()) {
          return text.append(Strings.repeat(indent, depth))
              .append(TextBridge.translatable("debug.mishanguc.nbt.list_empty"));
        }
        text.append("[");
        for (Iterator<Tag> iterator = nbtList.iterator(); iterator.hasNext(); ) {
          text.append(
              n <= 0
                  ? Strings.repeat(indent, depth).replaceAll(" $", "")
                  : Strings.repeat(indent, depth));
          Tag nbtElement = iterator.next();
          text.append(serialize(nbtElement, layer + 1, indent, depth + 1));
          if (iterator.hasNext()) {
            text.append(",");
            if (!indent.isEmpty()) {
              text.append("\n");
            }
          }
          if (n >= 7) {
            ListTag remains = new ListTag();
            while (iterator.hasNext()) {
              remains.add(iterator.next());
            }
            text.append(TextBridge.translatable("debug.mishanguc.nbt.list_eclipse", nbtList.size() - n)
                .withStyle(ChatFormatting.GRAY)
                .withStyle(style -> style
                    .withHoverEvent(new HoverEvent.ShowText(TextBridge.translatable("debug.mishanguc.nbt.list_display_remains")))
                    .withClickEvent(new NbtClickEvent(remains))));
            return text;
          }
          n++;
        }
        return text.append(" ]");
      }
      case 1 -> {
        MutableComponent text = TextBridge.literal("[");
        final int size = nbtList.size();
        int n = 0;
        for (Iterator<Tag> iterator = nbtList.iterator(); iterator.hasNext(); ) {
          Tag nbtElement = iterator.next();
          text.append(serialize(nbtElement, layer + (size <= 1 ? 0 : 1), indent, depth + 1));
          n++;
          if (iterator.hasNext()) {
            text.append(", ");
          }
          if (n >= 5) {
            text.append(TextBridge.literal(" ")
                .withStyle(ChatFormatting.GRAY)
                .append(TextBridge.translatable("debug.mishanguc.nbt.list_total", nbtList.size()))
                .withStyle(style -> style
                    .withHoverEvent(new HoverEvent.ShowText(TextBridge.translatable("debug.mishanguc.nbt.list_display_full")))
                    .withClickEvent(new NbtClickEvent(nbtList))));
            break;
          }
        }
        if (n < 5 && n > 1) {
          text.append(TextBridge.literal(" ")
              .withStyle(ChatFormatting.GRAY)
              .append(TextBridge.translatable("debug.mishanguc.nbt.list_expand", nbtList.size()))
              .withStyle(style -> style
                  .withHoverEvent(new HoverEvent.ShowText(TextBridge.translatable("debug.mishanguc.nbt.list_display_full")))
                  .withClickEvent(new NbtClickEvent(nbtList))));
        }
        text.append("]");
        return text;
      }
      default -> {
        MutableComponent text = TextBridge.literal("[");
        if (nbtList.size() > 1) {
          text.append(TextBridge.translatable("debug.mishanguc.nbt.list_brief", nbtList.size())
              .withStyle(ChatFormatting.GRAY)
              .withStyle(style -> style
                  .withHoverEvent(new HoverEvent.ShowText(TextBridge.translatable("debug.mishanguc.nbt.list_display_full")))
                  .withClickEvent(new NbtClickEvent(nbtList))));
        }
        text.append("]");
        return text;
      }
    }
  }

  public static Component serialize(StringTag nbtString, int layer) {
    final String string = nbtString.value();
    final UnaryOperator<Style> strStyle = style -> style.withColor(0xcccccc);
    if (layer == 0) {
      return TextBridge.literal(string).withStyle(strStyle);
    } else if (layer == 1) {
      return string.length() > 160
          ? TextBridge.literal(string.substring(0, 155))
          .withStyle(strStyle)
          .append(TextBridge.translatable("debug.mishanguc.nbt.string.eclipse", string.length()))
          : TextBridge.literal(string).withStyle(strStyle);
    } else {
      return string.length() > 40
          ? TextBridge.literal(string.substring(0, 35)).withStyle(strStyle).append("……")
          : TextBridge.literal(string).withStyle(strStyle);
    }
  }
}
