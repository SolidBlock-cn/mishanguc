package pers.solid.mishang.uc.blockentity;

import com.google.common.collect.ImmutableList;
import com.mojang.serialization.Codec;
import it.unimi.dsi.fastutil.booleans.BooleanArraySet;
import it.unimi.dsi.fastutil.booleans.BooleanSet;
import it.unimi.dsi.fastutil.booleans.BooleanSets;
import net.minecraft.core.BlockPos;
import net.minecraft.core.component.DataComponentGetter;
import net.minecraft.core.component.DataComponentMap;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.util.Util;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.Range;
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
  public List<TextContext> frontTexts = ImmutableList.of();
  /**
   * 后面的文字，也就是放置时背对玩家的这一面。
   * 通常情况下，是不可变的。当客户端正在进行编辑时，客户端的该字段是可变的。
   */
  public List<TextContext> backTexts = ImmutableList.of();
  /**
   * 正在编辑该告示牌的玩家。
   */
  private @Nullable Player editor;
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
  protected void loadAdditional(ValueInput view) {
    super.loadAdditional(view);
    final Codec<List<TextContext>> listCodec = TextContext.createListCodec(this::createDefaultTextContext);
    frontTexts = view.read("frontTexts", listCodec).orElseGet(ImmutableList::of);
    backTexts = view.read("backTexts", listCodec).orElseGet(ImmutableList::of);
    final boolean frontWaxed = view.getBooleanOr("frontWaxed", false);
    final boolean backWaxed = view.getBooleanOr("backWaxed", false);
    if (!frontWaxed && !backWaxed) {
      waxed = BooleanSets.emptySet();
    } else {
      waxed = new BooleanArraySet(2);
      if (frontWaxed) waxed.add(true);
      if (backWaxed) waxed.add(false);
    }
    final boolean frontGlowing = view.getBooleanOr("frontGlowing", false);
    final boolean backGlowing = view.getBooleanOr("backGlowing", false);
    if (!frontGlowing && !backGlowing) {
      glowing = BooleanSets.emptySet();
    } else {
      glowing = new BooleanArraySet(2);
      if (frontGlowing) glowing.add(true);
      if (backGlowing) glowing.add(false);
    }
  }

  @Override
  protected void saveAdditional(ValueOutput view) {
    super.saveAdditional(view);
    final Codec<List<TextContext>> listCodec = TextContext.createListCodec(this::createDefaultTextContext);
    view.store("frontTexts", listCodec, frontTexts);
    view.store("backTexts", listCodec, backTexts);
    view.putBoolean("frontWaxed", waxed.contains(true));
    view.putBoolean("backWaxed", waxed.contains(false));
    view.putBoolean("frontGlowing", glowing.contains(true));
    view.putBoolean("backGlowing", glowing.contains(false));
  }

  @Override
  protected void applyImplicitComponents(DataComponentGetter components) {
    super.applyImplicitComponents(components);
    frontTexts = components.getOrDefault(MishangucComponents.FRONT_TEXTS, ImmutableList.of());
    backTexts = components.getOrDefault(MishangucComponents.BACK_TEXTS, ImmutableList.of());
  }

  @Override
  protected void collectImplicitComponents(DataComponentMap.Builder componentMapBuilder) {
    super.collectImplicitComponents(componentMapBuilder);
    componentMapBuilder.set(MishangucComponents.FRONT_TEXTS, frontTexts);
    componentMapBuilder.set(MishangucComponents.BACK_TEXTS, backTexts);
  }

  @SuppressWarnings("deprecation")
  @Override
  public void removeComponentsFromTag(ValueOutput view) {
    super.removeComponentsFromTag(view);
    view.discard("frontTexts");
    view.discard("backTexts");
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
  public @Nullable Player getEditor() {
    return editor;
  }

  @Override
  public void setEditor(@Nullable Player editor) {
    this.editor = editor;
  }

  public ClientboundBlockEntityDataPacket getUpdatePacket() {
    return ClientboundBlockEntityDataPacket.create(this);
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
  public void setTextsOnSide(boolean isFront, List<TextContext> texts) {
    if (isFront) frontTexts = texts;
    else backTexts = texts;
  }
}
