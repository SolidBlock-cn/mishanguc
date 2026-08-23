package pers.solid.mishang.uc.components;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.ChatFormatting;
import net.minecraft.core.component.DataComponentGetter;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.TooltipProvider;
import net.minecraft.world.level.Explosion;
import pers.solid.mishang.uc.util.TextBridge;

import java.util.function.Consumer;

public record ExplosionToolComponent(float power, boolean createFire, Explosion.BlockInteraction destructionType) implements TooltipProvider {
  public static final ExplosionToolComponent DEFAULT = new ExplosionToolComponent(4, false, Explosion.BlockInteraction.DESTROY);

  public static final Codec<Explosion.BlockInteraction> DESTRUCTION_TYPE_CODEC = Codec.STRING.xmap(s -> switch (s) {
    case "none", "keep" -> Explosion.BlockInteraction.KEEP;
    case "trigger_block" -> Explosion.BlockInteraction.TRIGGER_BLOCK;
    case "destroy_with_decay" -> Explosion.BlockInteraction.DESTROY_WITH_DECAY;
    default -> Explosion.BlockInteraction.DESTROY;
  }, s -> switch (s) {
    case KEEP -> "keep";
    case DESTROY -> "destroy";
    case DESTROY_WITH_DECAY -> "destroy_with_decay";
    case TRIGGER_BLOCK -> "trigger_block";
  });
  public static final Codec<ExplosionToolComponent> CODEC = RecordCodecBuilder.create(i -> i.group(Codec.floatRange(-128, 128).optionalFieldOf("power", 4f).forGetter(ExplosionToolComponent::power), Codec.BOOL.optionalFieldOf("create_fire", false).forGetter(ExplosionToolComponent::createFire), DESTRUCTION_TYPE_CODEC.optionalFieldOf("destruction_type", Explosion.BlockInteraction.DESTROY).forGetter(ExplosionToolComponent::destructionType)).apply(i, ExplosionToolComponent::new));
  public static final StreamCodec<RegistryFriendlyByteBuf, ExplosionToolComponent> PACKET_CODEC = StreamCodec.composite(ByteBufCodecs.FLOAT, ExplosionToolComponent::power, ByteBufCodecs.BOOL, ExplosionToolComponent::createFire, StreamCodec.ofMember((value, buf) -> buf.writeEnum(value), buf -> buf.readEnum(Explosion.BlockInteraction.class)), ExplosionToolComponent::destructionType, ExplosionToolComponent::new);

  @Override
  public void addToTooltip(Item.TooltipContext context, Consumer<Component> textConsumer, TooltipFlag type, DataComponentGetter components) {
    textConsumer.accept(TextBridge.translatable("item.mishanguc.explosion_tool.tooltip.power", TextBridge.literal(String.valueOf(power)).withStyle(ChatFormatting.YELLOW)).withStyle(ChatFormatting.GRAY));
    textConsumer.accept(TextBridge.translatable("item.mishanguc.explosion_tool.tooltip.createFire", createFire ? CommonComponents.GUI_YES.copy().withStyle(ChatFormatting.GREEN) : CommonComponents.GUI_NO.copy().withStyle(ChatFormatting.RED)).withStyle(ChatFormatting.GRAY));
    textConsumer.accept(TextBridge.translatable("item.mishanguc.explosion_tool.tooltip.destructionType", TextBridge.translatable("item.mishanguc.explosion_tool.destructionType." + destructionType.name().toLowerCase()).withStyle(style -> style.withColor(0x779999))).withStyle(ChatFormatting.GRAY));
  }

  public ExplosionToolComponent withPower(float power) {
    return new ExplosionToolComponent(power, this.createFire, this.destructionType);
  }
}
