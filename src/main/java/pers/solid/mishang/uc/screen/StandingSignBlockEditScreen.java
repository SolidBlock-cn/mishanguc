package pers.solid.mishang.uc.screen;

import com.google.common.collect.Lists;
import net.minecraft.client.gui.tooltip.Tooltip;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.util.math.BlockPos;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import pers.solid.mishang.uc.blockentity.StandingSignBlockEntity;
import pers.solid.mishang.uc.text.TextContext;
import pers.solid.mishang.uc.util.TextBridge;

import java.util.Collection;
import java.util.List;

@ApiStatus.AvailableSince("1.0.2")
public class StandingSignBlockEditScreen extends AbstractSignBlockEditScreen<StandingSignBlockEntity> {

  private final boolean isFront;
  private final List<TextContext> backedUpTexts;

  public StandingSignBlockEditScreen(StandingSignBlockEntity entity, BlockPos blockPos, boolean isFront) {
    super(entity, blockPos, Lists.newArrayList(entity.getTextsOnSide(isFront).stream().map(TextContext::clone).iterator()));
    this.isFront = isFront;
    this.backedUpTexts = entity.getTextsOnSide(isFront);
    entity.setTextsOnSide(isFront, textContextsEditing);
  }

  @Override
  protected void init() {
    super.init();
    entity.editedSide = isFront;
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
      entity.setTextsOnSide(isFront, textContextsEditing);
    } else {
      entity.setTextsOnSide(isFront, backedUpTexts);
    }
  }


  public final ButtonWidget copyFromBackButton = new ButtonWidget.Builder(TextBridge.translatable("message.mishanguc.copy_from_back"), button -> {
    final StandingSignBlockEntity entity = this.entity;
    if (entity.editedSide == null) {
      return;
    }
    final List<@NotNull TextContext> otherSide = entity.getTextsOnSide(!entity.editedSide);
    if (otherSide == null)
      return;
    otherSide.forEach(
        textContext -> {
          final TextContext flip = textContext.clone().flip();
          // 留意添加到的位置是列表末尾。
          addTextField(textContextsEditing.size(), flip, false);
        });
  }).dimensions(this.width / 2 - 100, 90, 200, 20).tooltip(Tooltip.of(TextBridge.translatable("message.mishanguc.copy_from_back.description"))).build();
}
