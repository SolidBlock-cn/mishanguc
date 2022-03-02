package pers.solid.mishang.uc.screen;

import it.unimi.dsi.fastutil.objects.Reference2ObjectArrayMap;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Util;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import pers.solid.mishang.uc.blockentity.HungSignBlockEntity;
import pers.solid.mishang.uc.util.TextContext;

import java.util.ArrayList;
import java.util.List;

@Environment(EnvType.CLIENT)
public class HungSignBlockEditScreen extends AbstractSignBlockEditScreen {
  /** 告示牌正在被编辑的方向。 */
  public final Direction direction;

  public HungSignBlockEditScreen(
      HungSignBlockEntity entity, Direction direction, BlockPos blockPos) {
    super(
        entity,
        blockPos,
        Util.make(
            () -> {
              final List<@NotNull TextContext> get = entity.texts.get(direction);
              return get == null ? new ArrayList<>() : new ArrayList<>(get);
            }));
    this.direction = direction;
    // 此时的 entity.texts 是可修改的，忽略 @Unmodifiable 注解。
    entity.texts =
        Util.make(
            new Reference2ObjectArrayMap<>(entity.texts),
            map -> map.put(direction, textContextsEditing));
  }
  /** 从背面复制文本的按钮。复制过程中会进行镜像。 */
  public final ButtonWidget copyFromBackButton =
      new ButtonWidget(
          this.width / 2 - 100,
          90,
          200,
          20,
          new TranslatableText("message.mishanguc.copy_from_back"),
          button -> {
            final HungSignBlockEntity entity = (HungSignBlockEntity) this.entity;
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
                  addTextField(textContextsEditing.size(), flip);
                  textContextsEditing.add(flip);
                });
          });

  @Override
  protected void init() {
    this.addButton(copyFromBackButton);
    super.init();
    ((HungSignBlockEntity) entity).editedSide = direction;
    copyFromBackButton.x = width / 2 - 100;
  }

  @Override
  public void addTextField(int index, @Nullable TextContext textContext) {
    super.addTextField(index, textContext);
    copyFromBackButton.visible = false;
  }

  @Override
  public void removeTextField(int index) {
    super.removeTextField(index);
    copyFromBackButton.visible = placeHolder.visible;
  }

  @Override
  public void removed() {
    super.removed();
    ((HungSignBlockEntity) entity).editedSide = null;
  }
}
