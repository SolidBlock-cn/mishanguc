package pers.solid.mishang.uc.data;

import net.minecraft.data.server.tag.TagProvider;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.tag.TagBuilder;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.util.Identifier;

import java.util.Arrays;
import java.util.function.Function;
import java.util.stream.Stream;

public class MishangucTagBuilder<T> extends TagProvider.ProvidedTagBuilder<T> {
  private final TagKey<T> tagKey;
  private final Function<T, RegistryKey<T>> valueToKey;

  protected MishangucTagBuilder(TagKey<T> tagKey, TagBuilder builder, Function<T, RegistryKey<T>> valueToKey) {
    super(builder);
    this.tagKey = tagKey;
    this.valueToKey = valueToKey;
  }

  public MishangucTagBuilder<T> add(T value) {
    add(valueToKey.apply(value));
    return this;
  }

  @SafeVarargs
  public final MishangucTagBuilder<T> add(T... values) {
    Stream.of(values).map(this.valueToKey).forEach(this::add);
    return this;
  }


  @SafeVarargs
  @Override
  public final MishangucTagBuilder<T> add(RegistryKey<T>... keys) {
    super.add(keys);
    return this;
  }

  @Override
  public MishangucTagBuilder<T> addOptional(Identifier id) {
    super.addOptional(id);
    return this;
  }

  @Override
  public MishangucTagBuilder<T> addTag(TagKey<T> identifiedTag) {
    super.addTag(identifiedTag);
    return this;
  }

  @SafeVarargs
  public final MishangucTagBuilder<T> addTag(TagKey<T>... tags) {
    for (TagKey<T> tag : tags) {
      super.addTag(tag);
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
  public MishangucTagBuilder<T> addOptionalTag(Identifier id) {
    super.addOptionalTag(id);
    return this;
  }
}
