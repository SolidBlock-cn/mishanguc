package pers.solid.mishang.uc.blockentity;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtList;
import net.minecraft.util.BlockMirror;
import net.minecraft.util.BlockRotation;
import net.minecraft.util.Util;
import net.minecraft.util.math.Direction;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.Unmodifiable;
import pers.solid.mishang.uc.render.HungSignBlockEntityRenderer;
import pers.solid.mishang.uc.text.TextContext;

import java.util.List;
import java.util.Map;

/**
 * @see pers.solid.mishang.uc.block.HungSignBlock
 * @see HungSignBlockEntityRenderer
 */
public class HungSignBlockEntity extends BlockEntityWithText {
  public static final TextContext DEFAULT_TEXT_CONTEXT =
      Util.make(new TextContext(), textContext1 -> textContext1.size = 6);
  /**
   * 该方块正在被编辑的方向。同时存在于客户端与服务器。<br>
   * 若未被编辑则为 {@code null}。<br>
   * The direction being edited of the block. Exists on both client and server sides.<br>
   * {@code null} if not edited.
   */
  public @Nullable Direction editedSide;

  public @Unmodifiable Map<@NotNull Direction, @Unmodifiable @NotNull List<@NotNull TextContext>>
      texts = ImmutableMap.of();
  /**
   * 编辑该方块的玩家。若为非 <code>null</code>，则其他玩家不可编辑。<br>
   * The player editing the block. non-<code>null</code> means other players cannot edit.
   */
  @Nullable
  private PlayerEntity editor;

  public HungSignBlockEntity() {
    super(MishangucBlockEntities.HUNG_SIGN_BLOCK_ENTITY);
  }

  protected HungSignBlockEntity(BlockEntityType<?> type) {
    super(type);
  }

  @Override
  public void fromTag(BlockState state, NbtCompound nbt) {
    super.fromTag(state, nbt);
    ImmutableMap.Builder<Direction, List<TextContext>> builder = new ImmutableMap.Builder<>();
    for (Direction direction : Direction.Type.HORIZONTAL) {
      final NbtElement element = nbt.get(direction.asString());
      if (element instanceof NbtList) {
        ImmutableList.Builder<TextContext> listBuilder = new ImmutableList.Builder<>();
        for (NbtElement nbtElement : ((NbtList) element)) {
          TextContext textContext = TextContext.fromNbt(nbtElement);
          listBuilder.add(textContext);
        }
        final ImmutableList<TextContext> build = listBuilder.build();
        if (!build.isEmpty()) builder.put(direction, build);
      } else {
        builder.put(
            direction, ImmutableList.of(TextContext.fromNbt(element, getDefaultTextContext())));
      }
    }
    texts = builder.build();
  }

  @Override
  public NbtCompound writeNbt(NbtCompound nbt) {
    super.writeNbt(nbt);
    for (Direction direction : Direction.Type.HORIZONTAL) {
      final List<@NotNull TextContext> textContexts = texts.get(direction);
      if (textContexts == null || textContexts.isEmpty()) {
        continue;
      }
      final NbtList nbtList = new NbtList();
      for (TextContext textContext : textContexts) {
        nbtList.add(textContext.createNbt());
      }
      nbt.put(direction.asString(), nbtList);
    }
    if (nbt.isEmpty()) {
      // 表示该 nbt 是空的，但游戏不会认为是空的。因为如果 nbt 真的是空的，生成 packet 的时候会直接将其忽略，因此即使告示牌没有文本，也不能让其 nbt 真的为空。
      nbt.putBoolean("empty", true);
    } else {
      nbt.remove("empty");
    }
    return nbt;
  }

  @Override
  public void applyRotation(BlockRotation rotation) {
    super.applyRotation(rotation);
    final ImmutableMap.Builder<Direction, List<TextContext>> builder = new ImmutableMap.Builder<>();
    texts.forEach((direction, list) -> builder.put(rotation.rotate(direction), list));
    texts = builder.build();
  }

  @Override
  public void applyMirror(BlockMirror mirror) {
    super.applyMirror(mirror);
    final ImmutableMap.Builder<Direction, List<TextContext>> builder = new ImmutableMap.Builder<>();
    texts.forEach((direction, list) -> builder.put(mirror.apply(direction), list));
    texts = builder.build();
  }

  @Override
  public float getHeight() {
    return 8;
  }

  @Override
  public TextContext getDefaultTextContext() {
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
