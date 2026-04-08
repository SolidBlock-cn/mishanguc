package pers.solid.mishang.uc.screen;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.Tooltip;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.network.chat.Component;
import org.jetbrains.annotations.Unmodifiable;
import pers.solid.mishang.uc.blockentity.HungSignBlockEntity;
import pers.solid.mishang.uc.text.TextContext;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
      HolderLookup.Provider registryLookup, BlockPos blockPos, Direction direction, HungSignBlockEntity entity) {
    super(registryLookup, entity, blockPos, entity.texts.get(direction));
    this.backedUpTexts = entity.texts;
    this.direction = direction;
    // 此时的 entity.texts 是可修改的，忽略 @Unmodifiable 注解。
    entity.texts = new HashMap<>(entity.texts);
    entity.texts.put(direction, textFieldListWidget.getTextContexts());
  }

  @Override
  protected void init() {
    super.init();
    entity.editedSide = direction;
  }

  @Override
  protected List<Button> getTextHolders() {
    return List.of(placeHolder, copyFromBackButton);
  }

  @Override
  public void removed() {
    super.removed();
    entity.editedSide = null;
    if (changed) {
      // 固化 texts 字段
      final HashMap<Direction, @Unmodifiable List<TextContext>> map = new HashMap<>(entity.texts);
      map.put(direction, ImmutableList.copyOf(textFieldListWidget.getTextContexts()));
      entity.texts = ImmutableMap.copyOf(map);
    } else {
      entity.texts = backedUpTexts;
    }
  }

  /**
   * 从背面复制文本的按钮。复制过程中会进行镜像。
   */
  public final Button copyFromBackButton
      = new Button.Builder(
      Component.translatable("message.mishanguc.copy_from_back"),
          button -> {
            final HungSignBlockEntity entity = this.entity;
            if (entity.editedSide == null) {
              return;
            }
            final List<TextContext> otherSide =
                entity.texts.get(entity.editedSide.getOpposite());
            if (otherSide == null)
              return;
            otherSide.forEach(
                textContext -> {
                  final TextContext flip = textContext.clone().flip();
                  // 留意添加到的位置是列表末尾。
                  textFieldListWidget.addTextField(-1, flip, false);
                });
          }).bounds(this.width / 2 - 80, 35, 160, 20).tooltip(Tooltip.create(Component.translatable("message.mishanguc.copy_from_back.description"))).build();
}
