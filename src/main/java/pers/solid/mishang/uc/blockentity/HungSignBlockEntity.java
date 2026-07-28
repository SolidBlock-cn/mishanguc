package pers.solid.mishang.uc.blockentity;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import com.mojang.serialization.Codec;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.component.ComponentMap;
import net.minecraft.component.ComponentsAccess;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.storage.ReadView;
import net.minecraft.storage.WriteView;
import net.minecraft.util.BlockMirror;
import net.minecraft.util.BlockRotation;
import net.minecraft.util.Util;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.Unmodifiable;
import pers.solid.mishang.uc.components.MishangucComponents;
import pers.solid.mishang.uc.render.HungSignBlockEntityRenderer;
import pers.solid.mishang.uc.text.TextContext;

import java.util.*;

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
  protected void readData(ReadView view) {
    super.readData(view);
    ImmutableMap.Builder<Direction, List<TextContext>> builder = new ImmutableMap.Builder<>();
    final Codec<List<TextContext>> listCodec = TextContext.createListCodec(this::createDefaultTextContext);
    for (Direction direction : Direction.Type.HORIZONTAL) {
      final Optional<List<TextContext>> optionalValue = view.read(direction.asString(), listCodec);
      optionalValue.ifPresent(textContexts -> builder.put(direction, textContexts));
    }
    texts = builder.build();
    waxed = view.read("waxed", Direction.CODEC.listOf()).map(ImmutableSet::copyOf).orElseGet(ImmutableSet::of);
    glowing = view.read("glowing", Direction.CODEC.listOf()).map(ImmutableSet::copyOf).orElseGet(ImmutableSet::of);
  }

  @Override
  protected void writeData(WriteView view) {
    super.writeData(view);
    for (Direction direction : Direction.Type.HORIZONTAL) {
      final List<@NotNull TextContext> textContexts = texts.get(direction);
      if (textContexts == null || textContexts.isEmpty()) {
        continue;
      }
      view.put(direction.asString(), TextContext.createListCodec(this::createDefaultTextContext), textContexts);
    }
    view.put("waxed", Direction.CODEC.listOf(), ImmutableList.copyOf(waxed));
    view.put("glowing", Direction.CODEC.listOf(), ImmutableList.copyOf(glowing));
  }

  @Override
  protected void readComponents(ComponentsAccess components) {
    super.readComponents(components);
    texts = components.getOrDefault(MishangucComponents.TEXT_MAP, ImmutableMap.of());
  }

  @Override
  protected void addComponents(ComponentMap.Builder componentMapBuilder) {
    super.addComponents(componentMapBuilder);
    componentMapBuilder.add(MishangucComponents.TEXT_MAP, texts);
  }

  @SuppressWarnings("deprecation")
  @Override
  public void removeFromCopiedStackData(WriteView view) {
    super.removeFromCopiedStackData(view);
    for (Direction direction : Direction.Type.HORIZONTAL) {
      view.remove(direction.asString());
    }
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
