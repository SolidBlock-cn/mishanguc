package pers.solid.mishang.uc.blockentity;

import com.google.common.collect.ImmutableList;
import com.mojang.serialization.Codec;
import it.unimi.dsi.fastutil.booleans.BooleanArraySet;
import it.unimi.dsi.fastutil.booleans.BooleanSet;
import it.unimi.dsi.fastutil.booleans.BooleanSets;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.component.ComponentMap;
import net.minecraft.component.ComponentsAccess;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.packet.s2c.play.BlockEntityUpdateS2CPacket;
import net.minecraft.storage.ReadView;
import net.minecraft.storage.WriteView;
import net.minecraft.util.Util;
import net.minecraft.util.math.BlockPos;
import org.jetbrains.annotations.*;
import pers.solid.mishang.uc.components.MishangucComponents;
import pers.solid.mishang.uc.text.TextContext;

import java.util.List;

/**
 * 直立的告示牌方块实体。两面都可以有文字。
 *
 * @see pers.solid.mishang.uc.block.StandingSignBlock
 * @see pers.solid.mishang.uc.blocks.StandingSignBlocks
 * @see pers.solid.mishang.uc.render.StandingSignBlockEntityRenderer
 */
@ApiStatus.AvailableSince("1.0.2")
public class StandingSignBlockEntity extends BlockEntityWithText {
  public static final TextContext DEFAULT_TEXT_CONTEXT = Util.make(new TextContext(), textContext -> textContext.size = 6);
  /**
   * 前面的文字，也就是放置时朝着玩家的这一面。
   * 通常情况下，是不可变的。当客户端正在进行编辑时，客户端的该字段是可变的。
   */
  public @NotNull List<TextContext> frontTexts = ImmutableList.of();
  /**
   * 后面的文字，也就是放置时背对玩家的这一面。
   * 通常情况下，是不可变的。当客户端正在进行编辑时，客户端的该字段是可变的。
   */
  public @NotNull List<TextContext> backTexts = ImmutableList.of();
  /**
   * 正在编辑该告示牌的玩家。
   */
  private @Nullable PlayerEntity editor;
  /**
   * 告示牌被编辑的那一侧。{@code true} 表示 front，{@code false} 表示 back，{@code null} 表示未被编辑。
   */
  public @Nullable Boolean editedSide;

  public BooleanSet waxed = BooleanSets.emptySet();
  public BooleanSet glowing = BooleanSets.emptySet();

  public StandingSignBlockEntity(BlockPos pos, BlockState state) {
    super(MishangucBlockEntities.STANDING_SIGN_BLOCK_ENTITY, pos, state);
  }

  protected StandingSignBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
    super(type, pos, state);
  }

  @Override
  protected void readData(ReadView view) {
    super.readData(view);
    final Codec<List<TextContext>> listCodec = TextContext.createListCodec(this::createDefaultTextContext);
    frontTexts = view.read("frontTexts", listCodec).orElseGet(ImmutableList::of);
    backTexts = view.read("backTexts", listCodec).orElseGet(ImmutableList::of);
    final boolean frontWaxed = view.getBoolean("frontWaxed", false);
    final boolean backWaxed = view.getBoolean("backWaxed", false);
    if (!frontWaxed && !backWaxed) {
      waxed = BooleanSets.emptySet();
    } else {
      waxed = new BooleanArraySet(2);
      if (frontWaxed) waxed.add(true);
      if (backWaxed) waxed.add(false);
    }
    final boolean frontGlowing = view.getBoolean("frontGlowing", false);
    final boolean backGlowing = view.getBoolean("backGlowing", false);
    if (!frontGlowing && !backGlowing) {
      glowing = BooleanSets.emptySet();
    } else {
      glowing = new BooleanArraySet(2);
      if (frontGlowing) glowing.add(true);
      if (backGlowing) glowing.add(false);
    }
  }

  @Override
  protected void writeData(WriteView view) {
    super.writeData(view);
    final Codec<List<TextContext>> listCodec = TextContext.createListCodec(this::createDefaultTextContext);
    view.put("frontTexts", listCodec, frontTexts);
    view.put("backTexts", listCodec, backTexts);
    view.putBoolean("frontWaxed", waxed.contains(true));
    view.putBoolean("backWaxed", waxed.contains(false));
    view.putBoolean("frontGlowing", glowing.contains(true));
    view.putBoolean("backGlowing", glowing.contains(false));
  }

  @Override
  protected void readComponents(ComponentsAccess components) {
    super.readComponents(components);
    frontTexts = components.getOrDefault(MishangucComponents.FRONT_TEXTS, ImmutableList.of());
    backTexts = components.getOrDefault(MishangucComponents.BACK_TEXTS, ImmutableList.of());
  }

  @Override
  protected void addComponents(ComponentMap.Builder componentMapBuilder) {
    super.addComponents(componentMapBuilder);
    componentMapBuilder.add(MishangucComponents.FRONT_TEXTS, frontTexts);
    componentMapBuilder.add(MishangucComponents.BACK_TEXTS, backTexts);
  }

  @SuppressWarnings("deprecation")
  @Override
  public void removeFromCopiedStackData(WriteView view) {
    super.removeFromCopiedStackData(view);
    view.remove("frontTexts");
    view.remove("backTexts");
  }

  @Override
  public @Range(from = 0, to = 16) float getHeight() {
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

  public BlockEntityUpdateS2CPacket toUpdatePacket() {
    return BlockEntityUpdateS2CPacket.create(this);
  }

  /**
   * 获取指定一侧的文本。
   *
   * @param isFront {@code true} 表示 front，即放置时朝着玩家的那一面。{@code false} 表示 back，即背对着玩家的那一面。
   */
  @Contract(pure = true)
  public List<TextContext> getTextsOnSide(boolean isFront) {
    return isFront ? frontTexts : backTexts;
  }

  /**
   * 设置指定一侧的文本。
   *
   * @param isFront {@code true} 表示 front，即放置时朝着玩家的那一面。{@code false} 表示 back，即背对着玩家的那一面。
   * @param texts   文本列表，将直接用作字段。
   */
  @Contract(mutates = "this")
  public void setTextsOnSide(boolean isFront, @NotNull List<TextContext> texts) {
    if (isFront) frontTexts = texts;
    else backTexts = texts;
  }
}
