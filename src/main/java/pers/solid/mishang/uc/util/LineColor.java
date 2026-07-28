package pers.solid.mishang.uc.util;

import com.mojang.serialization.Codec;
import net.fabricmc.fabric.api.tag.convention.v2.ConventionalItemTags;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.tags.TagKey;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.item.Item;

/**
 * 道路标线颜色，目前分为白色和黄色。
 */
public enum LineColor implements StringRepresentable {
  WHITE("white", ConventionalItemTags.WHITE_DYES),
  YELLOW("yellow", ConventionalItemTags.YELLOW_DYES),
  UNKNOWN("unknown", null),
  NONE("none", null);

  private final String name;
  private final TagKey<Item> ingredient;
  public static final Codec<LineColor> CODEC = StringRepresentable.fromEnum(LineColor::values);

  LineColor(String name, TagKey<Item> ingredient) {
    this.name = name;
    this.ingredient = ingredient;
  }

  @Override
  public String getSerializedName() {
    return name;
  }

  public MutableComponent getName() {
    return TextBridge.translatable("lineColor.mishanguc." + name);
  }

  public TagKey<Item> getIngredient() {
    return ingredient;
  }
}
