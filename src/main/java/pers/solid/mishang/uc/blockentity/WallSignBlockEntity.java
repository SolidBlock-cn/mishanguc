package pers.solid.mishang.uc.blockentity;

import com.google.common.collect.ImmutableList;
import net.minecraft.core.BlockPos;
import net.minecraft.core.component.DataComponentGetter;
import net.minecraft.core.component.DataComponentMap;
import net.minecraft.util.Util;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.Unmodifiable;
import pers.solid.mishang.uc.components.MishangucComponents;
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
  public @Nullable Player editor;

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
  protected void loadAdditional(ValueInput view) {
    super.loadAdditional(view);
    textContexts = view.read("text", TextContext.createListCodec(this::createDefaultTextContext)).orElseGet(ImmutableList::of);

    glowing = view.getBooleanOr("glowing", false);
    waxed = view.getBooleanOr("waxed", false);
  }

  @Override
  protected void saveAdditional(ValueOutput view) {
    super.saveAdditional(view);
    view.store("text", TextContext.createListCodec(this::createDefaultTextContext), textContexts);
    view.putBoolean("glowing", glowing);
    view.putBoolean("waxed", waxed);
  }

  @Override
  protected void applyImplicitComponents(DataComponentGetter components) {
    super.applyImplicitComponents(components);
    textContexts = components.getOrDefault(MishangucComponents.TEXTS, ImmutableList.of());
  }

  @Override
  protected void collectImplicitComponents(DataComponentMap.Builder componentMapBuilder) {
    super.collectImplicitComponents(componentMapBuilder);
    componentMapBuilder.set(MishangucComponents.TEXTS, textContexts);
  }

  @SuppressWarnings("deprecation")
  @Override
  public void removeComponentsFromTag(ValueOutput view) {
    super.removeComponentsFromTag(view);
    view.discard("text");
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
