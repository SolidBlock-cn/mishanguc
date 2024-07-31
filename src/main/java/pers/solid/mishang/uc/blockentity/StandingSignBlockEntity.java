package pers.solid.mishang.uc.blockentity;

import com.google.common.collect.ImmutableList;
import it.unimi.dsi.fastutil.booleans.BooleanArraySet;
import it.unimi.dsi.fastutil.booleans.BooleanSet;
import it.unimi.dsi.fastutil.booleans.BooleanSets;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtList;
import net.minecraft.network.packet.s2c.play.BlockEntityUpdateS2CPacket;
import net.minecraft.util.Util;
import net.minecraft.util.math.BlockPos;
import org.jetbrains.annotations.*;
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
  public void readNbt(NbtCompound nbt) {
    super.readNbt(nbt);
    final NbtElement nbtFrontTexts = nbt.get("frontTexts");
    if (nbtFrontTexts instanceof NbtList nbtList) {
      frontTexts = nbtList.stream()
          .map(nbtElement -> TextContext.fromNbt(nbtElement, createDefaultTextContext()))
          .collect(ImmutableList.toImmutableList());
    } else {
      frontTexts = ImmutableList.of(TextContext.fromNbt(nbtFrontTexts, createDefaultTextContext()));
    }
    final NbtElement nbtBackTexts = nbt.get("backTexts");
    if (nbtBackTexts instanceof NbtList nbtList) {
      backTexts = nbtList.stream()
          .map(nbtElement -> TextContext.fromNbt(nbtElement, createDefaultTextContext()))
          .collect(ImmutableList.toImmutableList());
    } else {
      backTexts = ImmutableList.of(TextContext.fromNbt(nbtBackTexts, createDefaultTextContext()));
    }
    final boolean frontWaxed = nbt.getBoolean("frontWaxed");
    final boolean backWaxed = nbt.getBoolean("backWaxed");
    if (!frontWaxed && !backWaxed) {
      waxed = BooleanSets.emptySet();
    } else {
      waxed = new BooleanArraySet(2);
      if (frontWaxed) waxed.add(true);
      if (backWaxed) waxed.add(false);
    }
    final boolean frontGlowing = nbt.getBoolean("frontGlowing");
    final boolean backGlowing = nbt.getBoolean("backGlowing");
    if (!frontGlowing && !backGlowing) {
      glowing = BooleanSets.emptySet();
    } else {
      glowing = new BooleanArraySet(2);
      if (frontGlowing) glowing.add(true);
      if (backGlowing) glowing.add(false);
    }
  }

  @Override
  protected void writeNbt(NbtCompound nbt) {
    super.writeNbt(nbt);
    if (frontTexts.size() == 1) {
      nbt.put("frontTexts", frontTexts.get(0).createNbt());
    } else {
      final NbtList nbtList = new NbtList();
      frontTexts.forEach(textContext -> nbtList.add(textContext.createNbt()));
      nbt.put("frontTexts", nbtList);
    }
    if (backTexts.size() == 1) {
      nbt.put("backTexts", backTexts.get(0).createNbt());
    } else {
      final NbtList nbtList = new NbtList();
      backTexts.forEach(textContext -> nbtList.add(textContext.createNbt()));
      nbt.put("backTexts", nbtList);
    }
    nbt.putBoolean("frontWaxed", waxed.contains(true));
    nbt.putBoolean("backWaxed", waxed.contains(false));
    nbt.putBoolean("frontGlowing", glowing.contains(true));
    nbt.putBoolean("backGlowing", glowing.contains(false));
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
