package pers.solid.mishang.uc.blockentity;

import com.google.common.collect.ImmutableList;
import com.google.gson.JsonElement;
import com.mojang.serialization.JsonOps;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.component.ComponentMap;
import net.minecraft.component.ComponentsAccess;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.storage.ReadView;
import net.minecraft.storage.WriteView;
import net.minecraft.text.MutableText;
import net.minecraft.text.TextCodecs;
import net.minecraft.util.Util;
import net.minecraft.util.math.BlockPos;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.Unmodifiable;
import pers.solid.mishang.uc.components.MishangucComponents;
import pers.solid.mishang.uc.render.WallSignBlockEntityRenderer;
import pers.solid.mishang.uc.text.TextContext;

import java.util.List;
import java.util.Optional;

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

  @NotNull
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
  protected void readData(ReadView view) {
    super.readData(view);
    final Optional<String> textJson = view.getOptionalString("textJson");
    if (textJson.isPresent()) {
      // 此部分仅用于兼容
      final TextContext defaultTextContext = createDefaultTextContext();
      defaultTextContext.text = (MutableText) TextCodecs.CODEC.parse(JsonOps.INSTANCE, TextContext.GSON.fromJson(textJson.get(), JsonElement.class)).getOrThrow();
      textContexts = ImmutableList.of(defaultTextContext);
    } else {
      textContexts = view.read("text", TextContext.createListCodec(this::createDefaultTextContext)).orElseGet(ImmutableList::of);
    }

    glowing = view.getBoolean("glowing", false);
    waxed = view.getBoolean("waxed", false);
  }

  @Override
  protected void writeData(WriteView view) {
    super.writeData(view);
    view.put("text", TextContext.createListCodec(this::createDefaultTextContext), textContexts);
    view.putBoolean("glowing", glowing);
    view.putBoolean("waxed", waxed);
  }

  @Override
  protected void readComponents(ComponentsAccess components) {
    super.readComponents(components);
    textContexts = components.getOrDefault(MishangucComponents.TEXTS, ImmutableList.of());
  }

  @Override
  protected void addComponents(ComponentMap.Builder componentMapBuilder) {
    super.addComponents(componentMapBuilder);
    componentMapBuilder.add(MishangucComponents.TEXTS, textContexts);
  }

  @SuppressWarnings("deprecation")
  @Override
  public void removeFromCopiedStackData(WriteView view) {
    super.removeFromCopiedStackData(view);
    view.remove("text");
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
