package pers.solid.mishang.uc.data;

import net.minecraft.data.tags.TagAppender;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.TagKey;

import java.util.Arrays;
import java.util.function.Function;

/**
 * @since 1.21.6 随原版进行了变更，由直接继承改变了实现 {@link TagAppender}，其底层实现方式为对 {@link #parent} 字段进行操作。
 */
public class MishangucTagBuilder<T> implements TagAppender<T> {
  private final TagKey<T> tagKey;
  private final TagAppender<T> parent;
  private final Function<T, ResourceKey<T>> keyFunction;

  protected MishangucTagBuilder(TagKey<T> tagKey, TagAppender<T> parent, Function<T, ResourceKey<T>> keyFunction) {
    this.tagKey = tagKey;
    this.parent = parent;
    this.keyFunction = keyFunction;
  }

  public MishangucTagBuilder<T> add(T value) {
    parent.add(keyFunction.apply(value));
    return this;
  }

  @SafeVarargs
  public final MishangucTagBuilder<T> add(T... values) {
    for (T value : values) {
      parent.add(keyFunction.apply(value));
    }
    return this;
  }

  public MishangucTagBuilder<T> addOptional(T value) {
    parent.addOptional(keyFunction.apply(value));
    return this;
  }

  @Override
  public MishangucTagBuilder<T> add(ResourceKey<T> element) {
    parent.add(element);
    return this;
  }

  @Override
  public MishangucTagBuilder<T> addOptional(ResourceKey<T> element) {
    parent.addOptional(element);
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
  public TagAppender<T> addOptionalTag(TagKey<T> tag) {
    parent.addOptionalTag(tag);
    return this;
  }
}
