package pers.solid.mishang.uc.mixin;

import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.item.Item;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import pers.solid.mishang.uc.util.MishangItemSettings;

@Mixin(Item.class)
public abstract class ItemMixin {
  /**
   * 修改将要传入物品组件的名称，使之直接使用方块名称，而不是使用物品名称。
   */
  @ModifyArg(method = "<init>", at = @At(value = "INVOKE", target = "Lnet/minecraft/item/Item$Settings;getValidatedComponents(Lnet/minecraft/text/Text;Lnet/minecraft/util/Identifier;)Lnet/minecraft/component/ComponentMap;"), index = 0)
  private Text modifyItemName(Text name, @Local(argsOnly = true) Item.Settings settings) {
    if (settings instanceof MishangItemSettings mishangItemSettings) {
      final Text forceName = mishangItemSettings.getForceItemName$mishang();
      if (forceName != null) {
        return forceName;
      }
    }
    return name;
  }

  @Mixin(Item.Settings.class)
  public abstract static class SettingsMixin implements MishangItemSettings {
    @Unique
    private Text forceItemName$mishang;

    @Override
    public Text getForceItemName$mishang() {
      return forceItemName$mishang;
    }

    @Override
    public void setForceItemName$mishang(Text name) {
      forceItemName$mishang = name;
    }
  }
}
