package pers.solid.mishang.uc.blockentity;

import com.google.common.collect.ImmutableList;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtList;
import net.minecraft.nbt.NbtString;
import net.minecraft.util.Util;
import net.minecraft.util.math.BlockPos;
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
  public static final TextContext DEFAULT_TEXT_CONTEXT = Util.make(new TextContext(), textContext -> textContext.size = 6);
  /**
   * 正在编辑该告示牌的玩家。若为 <code>null</code>，则表示该告示牌为空闲模式。
   */
  public @Nullable PlayerEntity editor;

  public @Unmodifiable List<TextContext> textContexts = ImmutableList.of();
  /**
   * 告示牌的文本是否正在发光，不影响文本的颜色和描边，只影响文本显示时的所使用的亮度。
   */
  public boolean glowing;
  /**
   * 告示牌是否已经被涂蜡。
   */
  public boolean waxed;

  public WallSignBlockEntity(BlockPos pos, BlockState state) {
    super(MishangucBlockEntities.WALL_SIGN_BLOCK_ENTITY, pos, state);
  }

  protected WallSignBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
    super(type, pos, state);
  }

  @Override
  public void readNbt(NbtCompound nbt) {
    super.readNbt(nbt);
    final @Nullable NbtElement nbtText = nbt.get("text");
    if (nbtText instanceof NbtString || nbt.contains("textJson", NbtElement.STRING_TYPE)) {
      // 如果 text 是个字符串，则读取整个 nbt 作为 TextContext。
      // 例如，整个 nbt 可以是 {text: "abc", color: "red", size: 5}。
      textContexts = ImmutableList.of(TextContext.fromNbt(nbt, createDefaultTextContext()));
    } else if (nbtText instanceof NbtCompound) {
      // 如果 text 是个复合标签，则读取这个复合标签。
      // 例如，整个 nbt 可以是 {text: {text: "abc", color: "red", size: 5}}。
      textContexts = ImmutableList.of(TextContext.fromNbt(nbtText, createDefaultTextContext()));
    } else if (nbtText instanceof NbtList) {
      ImmutableList.Builder<TextContext> builder = new ImmutableList.Builder<>();
      for (NbtElement nbtElement : ((NbtList) nbtText)) {
        builder.add(TextContext.fromNbt(nbtElement, createDefaultTextContext()));
      }
      textContexts = builder.build();
    }

    glowing = nbt.getBoolean("glowing");
    waxed = nbt.getBoolean("waxed");
  }

  @Override
  public void writeNbt(NbtCompound nbt) {
    super.writeNbt(nbt);
    if (textContexts.size() == 1) {
      final NbtCompound nbtCompound = new NbtCompound();
      textContexts.get(0).writeNbt(nbtCompound);
      nbt.put("text", nbtCompound);
    } else {
      final NbtList nbtList = new NbtList();
      for (TextContext textContext : textContexts) {
        nbtList.add(textContext.createNbt());
      }
      nbt.put("text", nbtList);
    }
    nbt.putBoolean("glowing", glowing);
    nbt.putBoolean("waxed", waxed);
  }

  @Override
  public float getHeight() {
    return 8;
  }

  @Override
  public TextContext createDefaultTextContext() {
    return DEFAULT_TEXT_CONTEXT.clone();
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
