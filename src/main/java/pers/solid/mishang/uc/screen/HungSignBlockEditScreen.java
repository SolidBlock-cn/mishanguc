package pers.solid.mishang.uc.screen;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import it.unimi.dsi.fastutil.objects.Reference2ObjectArrayMap;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Util;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Unmodifiable;
import pers.solid.mishang.uc.blockentity.HungSignBlockEntity;
import pers.solid.mishang.uc.util.TextContext;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
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
      HungSignBlockEntity entity, Direction direction, BlockPos blockPos) {
    super(
        entity,
        blockPos,
        Util.make(
            () -> {
              final @Unmodifiable List<@NotNull TextContext> get = entity.texts.get(direction);
              return get == null
                  ? new ArrayList<>()
                  : get.stream().map(TextContext::clone).collect(Collectors.toList());
            }));
    this.backedUpTexts = entity.texts;
    this.direction = direction;
    // 此时的 entity.texts 是可修改的，忽略 @Unmodifiable 注解。
    entity.texts =
        Util.make(
            new Reference2ObjectArrayMap<>(entity.texts),
            map -> map.put(direction, textContextsEditing));
  }

  @Override
  protected void init() {
    this.addButton(copyFromBackButton);
    super.init();
    entity.editedSide = direction;
    copyFromBackButton.x = width / 2 - 100;
  }

  @Override
  public void removed() {
    super.removed();
    entity.editedSide = null;
    if (changed) {
      // 固化 texts 字段
      entity.texts =
          ImmutableMap.copyOf(
              Util.make(
                  entity.texts,
                  map -> map.put(direction, ImmutableList.copyOf(textContextsEditing))));
    } else {
      entity.texts = backedUpTexts;
    }
  }

  @Override
  public void addTextField(int index, @NotNull TextContext textContext, boolean isExisting) {
    super.addTextField(index, textContext, isExisting);
    copyFromBackButton.visible = false;
  }

  /**
   * 从背面复制文本的按钮。复制过程中会进行镜像。
   */
  public final ButtonWidget copyFromBackButton =
      new ButtonWidget(
          this.width / 2 - 100,
          90,
          200,
          20,
          new TranslatableText("message.mishanguc.copy_from_back"),
          button -> {
            final HungSignBlockEntity entity = this.entity;
            if (entity.editedSide == null) {
              return;
            }
            final List<@NotNull TextContext> otherSide =
                entity.texts.get(entity.editedSide.getOpposite());
            if (otherSide == null) return;
            otherSide.forEach(
                textContext -> {
                  final TextContext flip = textContext.clone().flip();
                  // 留意添加到的位置是列表末尾。
                  addTextField(textContextsEditing.size(), flip, false);
                });
          });

  @Override
  public void removeTextField(int index) {
    super.removeTextField(index);
    copyFromBackButton.visible = placeHolder.visible;
  }


}
