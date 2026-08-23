package pers.solid.mishang.uc.data;

import net.minecraft.data.tags.TagAppender;
import net.minecraft.tags.TagKey;

import java.util.Arrays;

/**
 * @since 1.21.6 随原版进行了变更，由直接继承改变了实现 {@link TagAppender}，其底层实现方式为对 {@link #parent} 字段进行操作。
 */
public class MishangucTagBuilder<T> implements TagAppender<T, T> {
  private final TagKey<T> tagKey;
  private final TagAppender<T, T> parent;

  protected MishangucTagBuilder(TagKey<T> tagKey, TagAppender<T, T> parent) {
    this.tagKey = tagKey;
    this.parent = parent;
  }

  public MishangucTagBuilder<T> add(T value) {
    parent.add(value);
    return this;
  }

  @SafeVarargs
  public final MishangucTagBuilder<T> add(T... values) {
    parent.add(values);
    return this;
  }

  @Override
  public TagAppender<T, T> addOptional(T value) {
    parent.addOptional(value);
    return this;
  }

  @Override
  public MishangucTagBuilder<T> addTag(TagKey<T> tag) {
    parent.addTag(tag);
    return this;
  }

  @SafeVarargs
  public final MishangucTagBuilder<T> addTag(TagKey<T>... tags) {
    for (TagKey<T> tag : tags) {
      parent.addTag(tag);
    }
    return this;
  }

  public MishangucTagBuilder<T> addTag(MishangucTagBuilder<T> builder) {
    return addTag(builder.tagKey);
  }

  @SafeVarargs
  public final MishangucTagBuilder<T> addTag(MishangucTagBuilder<T>... builders) {
    Arrays.stream(builders).map(b -> b.tagKey).forEach(this::addTag);
    return this;
  }

  @Override
  public TagAppender<T, T> addOptionalTag(TagKey<T> tag) {
    parent.addOptionalTag(tag);
    return this;
  }
}
