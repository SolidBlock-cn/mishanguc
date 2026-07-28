package pers.solid.mishang.uc.item;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * 实现此方法的物品在与实体交互时可以用特殊的效果。这些回调都是在 {@link pers.solid.mishang.uc.Mishanguc} 中注册的。
 */
public interface InteractsWithEntity {
  /**
   * 攻击实体前的回调。当玩家持有此物品攻击实体时，在实体受到攻击之前就会调用此方法。如果方法返回的结果不为 pass，则实体不会受到默认的攻击。
   *
   * @param player    准备攻击该实体的玩家。
   * @param world     事件发生所在的世界。
   * @param hand      玩家准备攻击时所使用的手。
   * @param entity    将要被攻击的实体。
   * @param hitResult 玩家进行攻击时的准星碰撞结果。
   * @return 回调结果。如果不为 pass，则实体会受到默认的攻击。
   * @see net.fabricmc.fabric.api.event.player.AttackEntityCallback
   */
  @NotNull
  default ActionResult attackEntityCallback(
      PlayerEntity player,
      World world,
      Hand hand,
      Entity entity,
      @Nullable EntityHitResult hitResult) {
    return ActionResult.PASS;
  }

  /**
   * 对实体“使用”该物品的回调。当玩家持有此物品对实体“使用”物品时，在触发操作之前就会调用此方法。如果方法返回的结果不为 pass，则默认会触发的操作（例如命令狼起立或者坐下、与村民交易）就不会执行。
   *
   * @param player    准备使用该物品的玩家。
   * @param world     事件发生所在的世界。
   * @param hand      玩家准备使用时所使用的手。
   * @param entity    玩家持有该物品准备对该实体使用物品。
   * @param hitResult 玩家进行使用时的准星碰撞结果。
   * @return 回调结果。如果不为 pass，则实体会触发默认的“使用”操作。
   * @see net.fabricmc.fabric.api.event.player.UseEntityCallback
   */
  @NotNull
  default ActionResult useEntityCallback(
      PlayerEntity player,
      World world,
      Hand hand,
      Entity entity,
      @Nullable EntityHitResult hitResult) {
    return ActionResult.PASS;
  }
}
