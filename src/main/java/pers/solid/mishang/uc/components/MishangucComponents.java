package pers.solid.mishang.uc.components;

import com.mojang.serialization.Codec;
import net.minecraft.core.Direction;
import net.minecraft.core.Registry;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import pers.solid.mishang.uc.MishangUtils;
import pers.solid.mishang.uc.Mishanguc;
import pers.solid.mishang.uc.text.TextContext;
import pers.solid.mishang.uc.util.ColorMixtureType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @see net.minecraft.core.component.DataComponents
 */
public final class MishangucComponents {
  public static final DataComponentType<CarryingToolData> CARRYING_TOOL_DATA = register("carrying_tool_data", CarryingToolData.CODEC, CarryingToolData.PACKET_CODEC);

  /**
   * 颜色工具需要应用的颜色。
   */
  public static final DataComponentType<Integer> COLOR = register("color", MishangUtils.COLOR_CODEC, ByteBufCodecs.INT);

  /**
   * 颜色工具应用颜色的不透明度，对所有颜色混合类型都有效。
   */
  public static final DataComponentType<Float> OPACITY = register("opacity", Codec.FLOAT, ByteBufCodecs.FLOAT);

  /**
   * 颜色工具的颜色混合类型。
   */
  public static final DataComponentType<ColorMixtureType> COLOR_MIXTURE_TYPE = register("color_mixture_type", ColorMixtureType.CODEC, ColorMixtureType.PACKET_CODEC);

  /**
   * 颜色工具对颜色的修改数量，仅限于部分混合类型。
   */
  public static final DataComponentType<Float> COLOR_CHANGE_AMOUNT = register("color_change_amount", Codec.FLOAT, ByteBufCodecs.FLOAT);

  public static final DataComponentType<ExplosionToolComponent> EXPLOSION_TOOL_DATA = register("explosion_tool_data", ExplosionToolComponent.CODEC, ExplosionToolComponent.PACKET_CODEC);
  public static final DataComponentType<FastBuildingToolData> FAST_BUILDING_TOOL_DATA = register("fast_building_tool_data", FastBuildingToolData.CODEC, FastBuildingToolData.PACKET_CODEC);
  public static final DataComponentType<Boolean> INCLUDES_FLUID = register("includes_fluid", Codec.BOOL, ByteBufCodecs.BOOL);
  public static final DataComponentType<Integer> LENGTH = register("length", Codec.intRange(1, 64), ByteBufCodecs.INT);
  public static final DataComponentType<Integer> STRENGTH = register("strength", Codec.intRange(0, 10), ByteBufCodecs.INT);
  public static final DataComponentType<TextCopyToolComponent> TEXT_COPY_TOOL_PROPERTIES = register("text_copy_tool_properties", TextCopyToolComponent.CODEC, TextCopyToolComponent.PACKET_CODEC);
  public static final DataComponentType<List<TextContext>> TEXTS = register("texts", Codec.list(TextContext.CODEC), ByteBufCodecs.collection(ArrayList::new, TextContext.PACKET_CODEC));
  public static final DataComponentType<List<TextContext>> FRONT_TEXTS = register("front_texts", Codec.list(TextContext.CODEC), ByteBufCodecs.collection(ArrayList::new, TextContext.PACKET_CODEC));
  public static final DataComponentType<List<TextContext>> BACK_TEXTS = register("back_texts", Codec.list(TextContext.CODEC), ByteBufCodecs.collection(ArrayList::new, TextContext.PACKET_CODEC));
  public static final DataComponentType<Map<Direction, List<TextContext>>> TEXT_MAP = register("text_map", Codec.unboundedMap(Direction.CODEC, Codec.list(TextContext.CODEC)), ByteBufCodecs.map(HashMap::new, Direction.STREAM_CODEC, ByteBufCodecs.collection(ArrayList::new, TextContext.PACKET_CODEC)));

  private MishangucComponents() {
  }

  private static <T> DataComponentType<T> register(String id, Codec<T> codec, StreamCodec<? super RegistryFriendlyByteBuf, T> packetCodec) {
    return Registry.register(BuiltInRegistries.DATA_COMPONENT_TYPE, Mishanguc.id(id), DataComponentType.<T>builder().persistent(codec).networkSynchronized(packetCodec).build());
  }
}
