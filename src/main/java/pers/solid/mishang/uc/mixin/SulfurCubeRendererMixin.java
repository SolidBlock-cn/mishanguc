package pers.solid.mishang.uc.mixin;

import com.llamalad7.mixinextras.sugar.Local;
import it.unimi.dsi.fastutil.ints.IntList;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.renderer.entity.SulfurCubeRenderer;
import net.minecraft.client.renderer.entity.state.SulfurCubeRenderState;
import net.minecraft.world.entity.monster.cubemob.SulfurCube;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import pers.solid.mishang.uc.block.ColoredBlock;
import pers.solid.mishang.uc.item.ColoredTintSource;

@Environment(EnvType.CLIENT)
@Mixin(SulfurCubeRenderer.class)
public abstract class SulfurCubeRendererMixin {
  /**
   * 在提取原版的渲染数据后，修改染色方块的着色颜色，使迷上城建的染色方块在硫方怪中能正确着色。
   */
  @Inject(method = "extractRenderState(Lnet/minecraft/world/entity/monster/cubemob/SulfurCube;Lnet/minecraft/client/renderer/entity/state/SulfurCubeRenderState;F)V", at = @At("TAIL"))
  private static void updateColoredBlockTint(SulfurCube entity, SulfurCubeRenderState state, float partialTicks, CallbackInfo ci, @Local ItemStack containedBlock) {
    if (containedBlock.getItem() instanceof BlockItem blockItem && blockItem.getBlock() instanceof ColoredBlock && !containedBlock.isEmpty()) {
      final IntList tintLayers = state.containedBlock.tintLayers();
      final int calculatedColor = ColoredTintSource.INSTANCE.calculate(containedBlock, null, null);

      for (int i = 0; i < tintLayers.size(); i++) {
        tintLayers.set(i, calculatedColor);
      }
    }
  }
}
