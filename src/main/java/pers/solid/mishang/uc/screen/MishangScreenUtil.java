package pers.solid.mishang.uc.screen;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.util.InputUtil;
import net.minecraft.client.util.Window;
import org.lwjgl.glfw.GLFW;

/**
 * 用于替代自 Minecraft 1.21.10 中移除的 {@code Screen} 中的静态方法。
 */
@Environment(EnvType.CLIENT)
public final class MishangScreenUtil {
  public static boolean hasShiftDown() {
    final Window window = MinecraftClient.getInstance().getWindow();
    return InputUtil.isKeyPressed(window, GLFW.GLFW_KEY_LEFT_SHIFT) || InputUtil.isKeyPressed(window, GLFW.GLFW_KEY_RIGHT_SHIFT);
  }

  public static boolean hasControlDown() {
    final Window window = MinecraftClient.getInstance().getWindow();
    return InputUtil.isKeyPressed(window, GLFW.GLFW_KEY_LEFT_CONTROL) || InputUtil.isKeyPressed(window, GLFW.GLFW_KEY_RIGHT_CONTROL);
  }

  public static boolean hasAltDown() {
    final Window window = MinecraftClient.getInstance().getWindow();
    return InputUtil.isKeyPressed(window, GLFW.GLFW_KEY_LEFT_ALT) || InputUtil.isKeyPressed(window, GLFW.GLFW_KEY_RIGHT_ALT);
  }
}
