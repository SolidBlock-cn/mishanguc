package pers.solid.mishang.uc.screen;

import com.mojang.blaze3d.platform.InputConstants;
import com.mojang.blaze3d.platform.Window;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Minecraft;
import org.lwjgl.glfw.GLFW;

/**
 * 用于替代自 Minecraft 1.21.10 中移除的 {@code Screen} 中的静态方法。
 */
@Environment(EnvType.CLIENT)
public final class MishangScreenUtil {
  public static boolean hasShiftDown() {
    final Window window = Minecraft.getInstance().getWindow();
    return InputConstants.isKeyDown(window, GLFW.GLFW_KEY_LEFT_SHIFT) || InputConstants.isKeyDown(window, GLFW.GLFW_KEY_RIGHT_SHIFT);
  }

  public static boolean hasControlDown() {
    final Window window = Minecraft.getInstance().getWindow();
    return InputConstants.isKeyDown(window, GLFW.GLFW_KEY_LEFT_CONTROL) || InputConstants.isKeyDown(window, GLFW.GLFW_KEY_RIGHT_CONTROL);
  }

  public static boolean hasAltDown() {
    final Window window = Minecraft.getInstance().getWindow();
    return InputConstants.isKeyDown(window, GLFW.GLFW_KEY_LEFT_ALT) || InputConstants.isKeyDown(window, GLFW.GLFW_KEY_RIGHT_ALT);
  }
}
