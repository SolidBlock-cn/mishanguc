package pers.solid.mishang.uc.screen;

import com.mojang.blaze3d.platform.InputConstants;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

/**
 * 用于替代自 Minecraft 1.21.10 中移除的 {@code Screen} 中的静态方法。
 */
@Environment(EnvType.CLIENT)
public final class MishangScreenUtil {
  public static boolean hasShiftDown() {
    return InputConstants.isKeyDown(InputConstants.KEY_LSHIFT) || InputConstants.isKeyDown(InputConstants.KEY_RSHIFT);
  }

  public static boolean hasControlDown() {
    return InputConstants.isKeyDown(InputConstants.KEY_LCONTROL) || InputConstants.isKeyDown(InputConstants.KEY_RCONTROL);
  }

  public static boolean hasAltDown() {
    return InputConstants.isKeyDown(InputConstants.KEY_LALT) || InputConstants.isKeyDown(InputConstants.KEY_RALT);
  }
}
