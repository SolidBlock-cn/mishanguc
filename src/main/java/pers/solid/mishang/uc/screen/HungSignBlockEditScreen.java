package pers.solid.mishang.uc.screen;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.util.Util;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import org.jetbrains.annotations.NotNull;
import pers.solid.mishang.uc.blockentity.HungSignBlockEntity;
import pers.solid.mishang.uc.util.TextContext;

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
              return get == null ? Lists.newArrayList() : Lists.newArrayList(get);
            }));
    this.direction = direction;
    // 此时的 entity.texts 是 HashMap，是可修改的，忽略 @Unmodifiable 注解。
    entity.texts =
        Util.make(
            Maps.newHashMap(entity.texts), map -> map.put(direction, getTextContextsEditing()));
  }

  @Override
  protected void init() {
    super.init();
    ((HungSignBlockEntity) entity).editedSide = direction;
  }

  @Override
  public void removed() {
    super.removed();
    ((HungSignBlockEntity) entity).editedSide = null;
  }
}
