package pers.solid.mishang.uc.item;

import pers.solid.brrp.v1.generator.ItemResourceGenerator;
import pers.solid.brrp.v1.model.ModelJsonBuilder;

public interface MishangucItem extends ItemResourceGenerator {

  @Deprecated(forRemoval = true)
  @Override
  default ModelJsonBuilder getItemModel() {
    return ItemResourceGenerator.super.getItemModel();
  }
}
