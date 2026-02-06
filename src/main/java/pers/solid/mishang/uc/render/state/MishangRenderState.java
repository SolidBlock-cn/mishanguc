package pers.solid.mishang.uc.render.state;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

/**
 * <p>用于在客户端世界逻辑和渲染线程之前传递数据的 render state。
 * <p>物品可以实现 {@link MishangRenderStateProvider} 接口，这样会在 {@link MishangRenderStateProvider#getMishangRenderState} 方法中返回 render state，从而用于渲染。
 *
 * @see net.fabricmc.fabric.api.client.rendering.v1.FabricRenderState#getData
 * @see net.fabricmc.fabric.api.client.rendering.v1.FabricRenderState#setData
 * @since Minecraft 1.21.10
 */
@Environment(EnvType.CLIENT)
public interface MishangRenderState {
  /**
   * 重置此 render state 的所有数据。目前不起作用，因为各 render state 会在每帧渲染结束后直接不再使用，而非共享同一对象。可能并未被使用，但考虑到未来有使用的可能性，故保留。
   */
  void clear();
}
