package pers.solid.mishang.uc.util;

import net.fabricmc.fabric.api.tag.convention.v1.ConventionalItemTags;
import net.minecraft.item.Item;
import net.minecraft.tag.TagKey;
import net.minecraft.text.MutableText;
import net.minecraft.util.StringIdentifiable;

/**
 * 道路标线颜色，目前分为白色和黄色。
 */
public enum LineColor implements StringIdentifiable {
  WHITE("white", ConventionalItemTags.WHITE_DYES),
  YELLOW("yellow", ConventionalItemTags.YELLOW_DYES),
  UNKNOWN("unknown", null),
  NONE("none", null);

  private final String name;
  private final TagKey<Item> ingredient;
  public static final Codec<LineColor> CODEC = StringIdentifiable.createCodec(LineColor::values);

  LineColor(String name, TagKey<Item> ingredient) {
    this.name = name;
    this.ingredient = ingredient;
  }

  @Override
  public String asString() {
    return name;
  }

  public MutableText getName() {
    return TextBridge.translatable("lineColor.mishanguc." + name);
  }

  public TagKey<Item> getIngredient() {
    return ingredient;
  }
}
