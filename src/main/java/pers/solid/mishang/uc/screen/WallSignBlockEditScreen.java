package pers.solid.mishang.uc.screen;

import com.google.common.collect.ImmutableList;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.util.math.BlockPos;
import org.jetbrains.annotations.Unmodifiable;
import pers.solid.mishang.uc.blockentity.WallSignBlockEntity;
import pers.solid.mishang.uc.text.TextContext;

import java.util.List;
import java.util.stream.Collectors;

@Environment(EnvType.CLIENT)
public class WallSignBlockEditScreen extends AbstractSignBlockEditScreen<WallSignBlockEntity> {
  /**
   * 修改之前的文本内容。当编辑时取消编辑了，则使用修改之前的文本内容。
   */
  private final @Unmodifiable List<TextContext> backedUpTextContexts;

  public WallSignBlockEditScreen(WallSignBlockEntity entity, BlockPos blockPos) {
    super(
        entity,
        blockPos,
        entity.textContexts.stream().map(TextContext::clone).collect(Collectors.toList()));
    this.backedUpTextContexts = entity.textContexts;
    entity.textContexts = textContextsEditing;
  }

  @Override
  public void removed() {
    super.removed();
    if (changed) {
      // 固化 entity.textContexts
      entity.textContexts = ImmutableList.copyOf(entity.textContexts);
    } else {
      entity.textContexts = backedUpTextContexts;
    }
  }
}
