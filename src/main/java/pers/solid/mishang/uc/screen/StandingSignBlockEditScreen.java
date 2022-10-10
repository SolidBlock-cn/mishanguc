package pers.solid.mishang.uc.screen;

import com.google.common.collect.ImmutableList;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.util.math.BlockPos;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import pers.solid.mishang.uc.blockentity.StandingSignBlockEntity;
import pers.solid.mishang.uc.text.TextContext;
import pers.solid.mishang.uc.util.TextBridge;

import java.util.ArrayList;
import java.util.List;

@ApiStatus.AvailableSince("1.0.2")
public class StandingSignBlockEditScreen extends AbstractSignBlockEditScreen<StandingSignBlockEntity> {

  private final boolean isFront;
  private final List<TextContext> backedUpTexts;

  public StandingSignBlockEditScreen(StandingSignBlockEntity entity, BlockPos blockPos, boolean isFront) {
    super(entity, blockPos, new ArrayList<>(entity.getTextsOnSide(isFront)));
    this.isFront = isFront;
    this.backedUpTexts = ImmutableList.copyOf(entity.getTextsOnSide(isFront));
    entity.setTextsOnSide(isFront, textContextsEditing);
  }

  @Override
  protected void init() {
    super.init();
    entity.editedSide = isFront;
    copyFromBackButton.x = width / 2 - 100;
  }

  @Override
  protected void initTextHolders() {
    super.initTextHolders();
    this.addDrawableChild(copyFromBackButton);
  }

  @Override
  public void removed() {
    super.removed();
    entity.editedSide = null;
    if (changed) {
      // 固化 texts 字段
      entity.setTextsOnSide(isFront, textContextsEditing);
    } else {
      entity.setTextsOnSide(isFront, backedUpTexts);
    }
  }

  @Override
  public void addTextField(int index, @NotNull TextContext textContext, boolean isExisting) {
    super.addTextField(index, textContext, isExisting);
    copyFromBackButton.visible = false;
  }


  public final ButtonWidget copyFromBackButton = new ButtonWidget(this.width / 2 - 100, 90, 200, 20, TextBridge.translatable("message.mishanguc.copy_from_back"), button -> {
    final StandingSignBlockEntity entity = this.entity;
    if (entity.editedSide == null) {
      return;
    }
    final List<@NotNull TextContext> otherSide = entity.getTextsOnSide(!entity.editedSide);
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
