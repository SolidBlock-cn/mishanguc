package pers.solid.mishang.uc.blockentity;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtList;
import net.minecraft.nbt.NbtString;
import net.minecraft.util.BlockMirror;
import net.minecraft.util.BlockRotation;
import net.minecraft.util.Util;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.Unmodifiable;
import pers.solid.mishang.uc.render.HungSignBlockEntityRenderer;
import pers.solid.mishang.uc.text.TextContext;

import java.util.*;
import java.util.stream.Collectors;

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

  /**
   * 涂蜡的侧面。为了节省内容，如果为空集，则直接使用不可变的 {@link Set#of()}，其他情况则为 {@link HashSet}。
   */
  public @Unmodifiable Set<@NotNull Direction> waxed = Collections.emptySet();
  /**
   * 发光的侧面。为了节省内容，如果为空集，则直接使用不可变的 {@link Set#of()}，其他情况则为 {@link HashSet}。
   */
  public @Unmodifiable Set<@NotNull Direction> glowing = Collections.emptySet();

  public HungSignBlockEntity(BlockPos pos, BlockState state) {
    super(MishangucBlockEntities.HUNG_SIGN_BLOCK_ENTITY, pos, state);
  }

  protected HungSignBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
    super(type, pos, state);
  }

  @Override
  public void readNbt(NbtCompound nbt) {
    super.readNbt(nbt);
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
      } else if (element != null) {
        builder.put(
            direction, ImmutableList.of(TextContext.fromNbt(element, createDefaultTextContext())));
      }
    }
    texts = builder.build();
    if (nbt.contains("waxed", NbtElement.LIST_TYPE)) {
      final NbtList list = nbt.getList("waxed", NbtElement.STRING_TYPE);
      if (list.isEmpty()) {
        waxed = Set.of();
      } else {
        waxed = list.stream().map(nbtElement -> Direction.byName(nbtElement.asString())).filter(Objects::nonNull).collect(Collectors.toCollection(() -> new HashSet<>(2)));
      }
    } else {
      waxed = Set.of();
    }
    if (nbt.contains("glowing", NbtElement.LIST_TYPE)) {
      final NbtList list = nbt.getList("glowing", NbtElement.STRING_TYPE);
      if (list.isEmpty()) {
        glowing = Set.of();
      } else {
        glowing = list.stream().map(nbtElement -> Direction.byName(nbtElement.asString())).filter(Objects::nonNull).collect(Collectors.toCollection(() -> new HashSet<>(2)));
      }
    } else {
      glowing = Set.of();
    }
  }

  @Override
  public void writeNbt(NbtCompound nbt) {
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
    nbt.put("waxed", waxed.stream().map(Direction::asString).map(NbtString::of).collect(Collectors.toCollection(NbtList::new)));
    nbt.put("glowing", glowing.stream().map(Direction::asString).map(NbtString::of).collect(Collectors.toCollection(NbtList::new)));
  }

  //  @Override
  public void applyRotation(BlockRotation rotation) {
    //    super.applyRotation(rotation);
    final ImmutableMap.Builder<Direction, List<TextContext>> builder = new ImmutableMap.Builder<>();
    texts.forEach((direction, list) -> builder.put(rotation.rotate(direction), list));
    texts = builder.build();
  }

  //  @Override
  public void applyMirror(BlockMirror mirror) {
    //    super.applyMirror(mirror);
    final ImmutableMap.Builder<Direction, List<TextContext>> builder = new ImmutableMap.Builder<>();
    texts.forEach((direction, list) -> builder.put(mirror.apply(direction), list));
    texts = builder.build();
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
