package pers.solid.mishang.uc.screen;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.tooltip.Tooltip;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Unmodifiable;
import pers.solid.mishang.uc.blockentity.HungSignBlockEntity;
import pers.solid.mishang.uc.text.TextContext;
import pers.solid.mishang.uc.util.TextBridge;

import java.util.*;
import java.util.stream.Collectors;

@Environment(EnvType.CLIENT)
public class HungSignBlockEditScreen extends AbstractSignBlockEditScreen<HungSignBlockEntity> {
  /**
   * 告示牌正在被编辑的方向。
   */
  public final Direction direction;
  /**
   * 备份的文本。如果取消编辑，则还是使用此处的文本。
   */
  protected final @Unmodifiable Map<Direction, List<TextContext>> backedUpTexts;

  public HungSignBlockEditScreen(
      RegistryWrapper.WrapperLookup registryLookup, BlockPos blockPos, Direction direction, HungSignBlockEntity entity) {
    super(registryLookup, entity, blockPos, Optional.ofNullable(entity.texts.get(direction)).map(textContexts -> textContexts.stream().map(TextContext::clone).collect(Collectors.toList())).orElseGet(ArrayList::new));
    this.backedUpTexts = entity.texts;
    this.direction = direction;
    // 此时的 entity.texts 是可修改的，忽略 @Unmodifiable 注解。
    entity.texts = new HashMap<>(entity.texts);
    entity.texts.put(direction, textContextsEditing);
  }

  @Override
  protected void init() {
    super.init();
    entity.editedSide = direction;
    copyFromBackButton.setX(width / 2 - 100);
  }

  @Override
  protected Collection<ButtonWidget> getTextHolders() {
    return List.of(placeHolder, applyLeftArrowTemplateButton, applyDoubleLineTemplateButton, applyRightArrowTemplateButton, copyFromBackButton);
  }

  @Override
  public void removed() {
    super.removed();
    entity.editedSide = null;
    if (changed) {
      // 固化 texts 字段
      final HashMap<@NotNull Direction, @Unmodifiable @NotNull List<@NotNull TextContext>> map = new HashMap<>(entity.texts);
      map.put(direction, ImmutableList.copyOf(textContextsEditing));
      entity.texts = ImmutableMap.copyOf(map);
    } else {
      entity.texts = backedUpTexts;
    }
  }

  /**
   * 从背面复制文本的按钮。复制过程中会进行镜像。
   */
  public final ButtonWidget copyFromBackButton =
      new ButtonWidget.Builder(
          TextBridge.translatable("message.mishanguc.copy_from_back"),
          button -> {
            final HungSignBlockEntity entity = this.entity;
            if (entity.editedSide == null) {
              return;
            }
            final List<@NotNull TextContext> otherSide =
                entity.texts.get(entity.editedSide.getOpposite());
            if (otherSide == null)
              return;
            otherSide.forEach(
                textContext -> {
                  final TextContext flip = textContext.clone().flip();
                  // 留意添加到的位置是列表末尾。
                  addTextField(textContextsEditing.size(), flip, false);
                });
          }).dimensions(this.width / 2 - 100,
          90,
          200,
          20).tooltip(Tooltip.of(TextBridge.translatable("message.mishanguc.copy_from_back.description"))).build();
}
