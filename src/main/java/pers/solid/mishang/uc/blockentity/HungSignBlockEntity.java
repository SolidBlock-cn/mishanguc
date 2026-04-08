package pers.solid.mishang.uc.blockentity;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import com.mojang.serialization.Codec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.component.DataComponentGetter;
import net.minecraft.core.component.DataComponentMap;
import net.minecraft.util.Util;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
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

  public @Unmodifiable Map<Direction, @Unmodifiable List<TextContext>>
      texts = ImmutableMap.of();
  /**
   * 编辑该方块的玩家。若为非 <code>null</code>，则其他玩家不可编辑。<br>
   * The player editing the block. non-<code>null</code> means other players cannot edit.
   */
  @Nullable
  private Player editor;

  /**
   * 涂蜡的侧面。为了节省内容，如果为空集，则直接使用不可变的 {@link Set#of()}，其他情况则为 {@link HashSet}。
   */
  public @Unmodifiable Set<Direction> waxed = Collections.emptySet();
  /**
   * 发光的侧面。为了节省内容，如果为空集，则直接使用不可变的 {@link Set#of()}，其他情况则为 {@link HashSet}。
   */
  public @Unmodifiable Set<Direction> glowing = Collections.emptySet();

  public HungSignBlockEntity(BlockPos pos, BlockState state) {
    super(MishangucBlockEntities.HUNG_SIGN_BLOCK_ENTITY, pos, state);
  }

  protected HungSignBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
    super(type, pos, state);
  }

  @Override
  protected void loadAdditional(ValueInput view) {
    super.loadAdditional(view);
    ImmutableMap.Builder<Direction, List<TextContext>> builder = new ImmutableMap.Builder<>();
    final Codec<List<TextContext>> listCodec = TextContext.createListCodec(this::createDefaultTextContext);
    for (Direction direction : Direction.Plane.HORIZONTAL) {
      final Optional<List<TextContext>> optionalValue = view.read(direction.getSerializedName(), listCodec);
      optionalValue.ifPresent(textContexts -> builder.put(direction, textContexts));
    }
    texts = builder.build();
    waxed = view.read("waxed", Direction.CODEC.listOf()).map(ImmutableSet::copyOf).orElseGet(ImmutableSet::of);
    glowing = view.read("glowing", Direction.CODEC.listOf()).map(ImmutableSet::copyOf).orElseGet(ImmutableSet::of);
  }

  @Override
  protected void saveAdditional(ValueOutput view) {
    super.saveAdditional(view);
    for (Direction direction : Direction.Plane.HORIZONTAL) {
      final List<TextContext> textContexts = texts.get(direction);
      if (textContexts == null || textContexts.isEmpty()) {
        continue;
      }
      view.store(direction.getSerializedName(), TextContext.createListCodec(this::createDefaultTextContext), textContexts);
    }
    view.store("waxed", Direction.CODEC.listOf(), ImmutableList.copyOf(waxed));
    view.store("glowing", Direction.CODEC.listOf(), ImmutableList.copyOf(glowing));
  }

  @Override
  protected void applyImplicitComponents(DataComponentGetter components) {
    super.applyImplicitComponents(components);
    texts = components.getOrDefault(MishangucComponents.TEXT_MAP, ImmutableMap.of());
  }

  @Override
  protected void collectImplicitComponents(DataComponentMap.Builder componentMapBuilder) {
    super.collectImplicitComponents(componentMapBuilder);
    componentMapBuilder.set(MishangucComponents.TEXT_MAP, texts);
  }

  @SuppressWarnings("deprecation")
  @Override
  public void removeComponentsFromTag(ValueOutput view) {
    super.removeComponentsFromTag(view);
    for (Direction direction : Direction.Plane.HORIZONTAL) {
      view.discard(direction.getSerializedName());
    }
  }

  //  @Override
  public void applyRotation(Rotation rotation) {
    //    super.applyRotation(rotation);
    final ImmutableMap.Builder<Direction, List<TextContext>> builder = new ImmutableMap.Builder<>();
    texts.forEach((direction, list) -> builder.put(rotation.rotate(direction), list));
    texts = builder.build();
  }

  //  @Override
  public void applyMirror(Mirror mirror) {
    //    super.applyMirror(mirror);
    final ImmutableMap.Builder<Direction, List<TextContext>> builder = new ImmutableMap.Builder<>();
    texts.forEach((direction, list) -> builder.put(mirror.mirror(direction), list));
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
  public @Nullable Player getEditor() {
    return editor;
  }

  @Override
  public void setEditor(@Nullable Player editor) {
    this.editor = editor;
  }

}
