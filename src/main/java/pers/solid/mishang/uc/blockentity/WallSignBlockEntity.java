package pers.solid.mishang.uc.blockentity;

import com.google.common.collect.ImmutableList;
import net.fabricmc.fabric.api.util.NbtType;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtList;
import net.minecraft.nbt.NbtString;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.Unmodifiable;
import pers.solid.mishang.uc.render.WallSignBlockEntityRenderer;
import pers.solid.mishang.uc.text.TextContext;

import java.util.List;

/**
 * @see pers.solid.mishang.uc.block.WallSignBlock
 * @see WallSignBlockEntityRenderer
 */
public class WallSignBlockEntity extends BlockEntityWithText {
  public static final TextContext DEFAULT_TEXT_CONTEXT = new TextContext();
  /**
   * 正在编辑该告示牌的玩家。若为 <code>null</code>，则表示该告示牌为空闲模式。
   */
  public @Nullable PlayerEntity editor;

  public @Unmodifiable List<TextContext> textContexts = ImmutableList.of();

  public WallSignBlockEntity() {
    super(MishangucBlockEntities.WALL_SIGN_BLOCK_ENTITY);
  }

  protected WallSignBlockEntity(BlockEntityType<?> type) {
    super(type);
  }

  @Override
  public void fromTag(BlockState state, NbtCompound nbt) {
    super.fromTag(state, nbt);
    final @Nullable NbtElement nbtText = nbt.get("text");
    if (nbtText instanceof NbtString || nbt.contains("textJson", NbtType.STRING)) {
      // 如果 text 是个字符串，则读取整个 nbt 作为 TextContext。
      // 例如，整个 nbt 可以是 {text: "abc", color: "red", size: 5}、
      textContexts = ImmutableList.of(TextContext.fromNbt(nbt, getDefaultTextContext()));
    } else if (nbtText instanceof NbtCompound) {
      // 如果 text 是个复合标签，则读取这个复合标签。
      // 例如，整个 nbt 可以是 {text: {text: "abc", color: "red", size: 5}}。
      textContexts = ImmutableList.of(TextContext.fromNbt(nbtText, getDefaultTextContext()));
    } else if (nbtText instanceof NbtList) {
      ImmutableList.Builder<TextContext> builder = new ImmutableList.Builder<>();
      for (NbtElement nbtElement : ((NbtList) nbtText)) {
        builder.add(TextContext.fromNbt(nbtElement, getDefaultTextContext()));
      }
      textContexts = builder.build();
    }
  }

  @Override
  public NbtCompound writeNbt(NbtCompound nbt) {
    final NbtCompound nbtCompound = super.writeNbt(nbt);
    if (textContexts.size() == 1) {
      textContexts.get(0).writeNbt(nbtCompound);
      nbt.put("text", nbtCompound);
    } else {
      final NbtList nbtList = new NbtList();
      for (TextContext textContext : textContexts) {
        nbtList.add(textContext.createNbt());
      }
      nbt.put("text", nbtList);
    }
    return nbtCompound;
  }

  @Override
  public float getHeight() {
    return 8;
  }

  @Override
  public TextContext getDefaultTextContext() {
    final TextContext textContext = new TextContext();
    textContext.size = 6;
    return textContext;
  }

  @Override
  public @Nullable PlayerEntity getEditor() {
    return editor;
  }

  @Override
  public void setEditor(@Nullable PlayerEntity editor) {
    this.editor = editor;
  }
}
