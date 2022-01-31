package pers.solid.mishang.uc.blockentity;

import com.google.common.collect.ImmutableList;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.block.BlockState;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtList;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Texts;
import net.minecraft.util.Util;
import org.jetbrains.annotations.NotNull;
import pers.solid.mishang.uc.util.TextContext;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @see pers.solid.mishang.uc.block.TextPadBlock
 * @see pers.solid.mishang.uc.renderer.TextPadBlockEntityRenderer
 */
public class TextPadBlockEntity extends BlockEntityWithText {
  public @NotNull TextContext textContext = new TextContext();
  public @NotNull List<@NotNull TextContext> otherTextContexts = ImmutableList.of();

  public TextPadBlockEntity() {
    super(MishangucBlockEntities.TEXT_PAD_BLOCK_ENTITY);
  }

  @Override
  public NbtCompound writeNbt(NbtCompound nbt) {
    super.writeNbt(nbt);
    textContext.writeNbt(nbt);
    nbt.put(
        "texts",
        Util.make(
            new NbtList(),
            list -> {
              for (TextContext otherTextContext : otherTextContexts) {
                list.add(otherTextContext.writeNbt(new NbtCompound()));
              }
            }));
    return nbt;
  }

  @Override
  public void fromTag(BlockState state, NbtCompound tag) {
    super.fromTag(state, tag);
    textContext = TextContext.fromNbt(tag);
    final NbtElement nbtTexts = tag.get("texts");
    otherTextContexts =
        nbtTexts instanceof NbtList
            ? ((NbtList) nbtTexts).stream().map(TextContext::fromNbt).collect(Collectors.toList())
            : ImmutableList.of();
    if (this.world instanceof ServerWorld) {
      final ServerCommandSource commandSource = this.getCommandSource(null);
      if (textContext.text != null) {
        try {
          textContext.text = Texts.parse(commandSource, textContext.text, null, 0);
        } catch (CommandSyntaxException ignored) {
        }
      }
      for (TextContext otherTextContext : otherTextContexts) {
        if (otherTextContext.text != null) {
          try {
            otherTextContext.text = Texts.parse(commandSource, otherTextContext.text, null, 0);
          } catch (CommandSyntaxException ignored) {
          }
        }
      }
    }
  }
}
