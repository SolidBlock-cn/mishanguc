package pers.solid.mishang.uc.mixin;

import net.minecraft.client.renderer.SubmitNodeCollection;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import pers.solid.mishang.uc.render.SpecialDrawableFeatureRenderer;
import pers.solid.mishang.uc.render.SubmitNodeCollectionExtension;

import java.util.ArrayList;
import java.util.List;

@Mixin(SubmitNodeCollection.class)
public abstract class SubmitNodeCollectionMixin implements SubmitNodeCollectionExtension {
  @Unique
  private @Nullable List<SpecialDrawableFeatureRenderer.SpecialDrawableSubmit> specialDrawableSubmits = null;

  @Override
  public void submitSpecialDrawable$mishang(SpecialDrawableFeatureRenderer.SpecialDrawableSubmit specialDrawableSubmit) {
    if (this.specialDrawableSubmits == null) {
      this.specialDrawableSubmits = new ArrayList<>();
    }
    specialDrawableSubmits.add(specialDrawableSubmit);
  }

  @Override
  public void clearSpecialDrawableSubmits$mishang() {
    this.specialDrawableSubmits = null;
  }

  @Override
  public List<SpecialDrawableFeatureRenderer.SpecialDrawableSubmit> getSpecialDrawableSubmits$mishang() {
    return specialDrawableSubmits == null ? List.of() : this.specialDrawableSubmits;
  }
}
