package pers.solid.mishang.uc.screen;

import com.google.common.collect.ImmutableList;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.Tooltip;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.network.chat.Component;
import org.jetbrains.annotations.ApiStatus;
import pers.solid.mishang.uc.blockentity.StandingSignBlockEntity;
import pers.solid.mishang.uc.text.TextContext;

import java.util.List;

@Environment(EnvType.CLIENT)
@ApiStatus.AvailableSince("1.0.2")
public class StandingSignBlockEditScreen extends AbstractSignBlockEditScreen<StandingSignBlockEntity> {

  private final boolean isFront;
  private final List<TextContext> backedUpTexts;

  public StandingSignBlockEditScreen(HolderLookup.Provider registryLookup, StandingSignBlockEntity entity, BlockPos blockPos, boolean isFront) {
    super(registryLookup, entity, blockPos, entity.getTextsOnSide(isFront));
    this.isFront = isFront;
    this.backedUpTexts = entity.getTextsOnSide(isFront);
    entity.setTextsOnSide(isFront, textFieldListWidget.getTextContexts());
  }

  @Override
  protected void init() {
    super.init();
    entity.editedSide = isFront;
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
      entity.setTextsOnSide(isFront, ImmutableList.copyOf(textFieldListWidget.getTextContexts()));
    } else {
      entity.setTextsOnSide(isFront, backedUpTexts);
    }
  }


  public final Button copyFromBackButton = new Button.Builder(Component.translatable("message.mishanguc.copy_from_back"), button -> {
    final StandingSignBlockEntity entity = this.entity;
    if (entity.editedSide == null) {
      return;
    }
    final List<TextContext> otherSide = entity.getTextsOnSide(!entity.editedSide);
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
