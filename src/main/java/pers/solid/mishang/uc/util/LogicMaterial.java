package pers.solid.mishang.uc.util;

import com.mojang.datafixers.util.Either;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.resources.model.sprite.Material;
import net.minecraft.resources.Identifier;

public record LogicMaterial(Identifier sprite, boolean forceTranslucent) {
    private static final Codec<LogicMaterial> SIMPLE_CODEC;
    private static final Codec<LogicMaterial> FULL_CODEC;
    public static final Codec<LogicMaterial> CODEC;

    public LogicMaterial(final Identifier sprite) {
        this(sprite, false);
    }

    public LogicMaterial withForceTranslucent(final boolean forceTranslucent) {
        return new LogicMaterial(this.sprite, forceTranslucent);
    }

    @Environment(EnvType.CLIENT)
    public Material toClientMaterial() {
        return new Material(this.sprite, this.forceTranslucent);
    }

    static {
        SIMPLE_CODEC = Identifier.CODEC.xmap(LogicMaterial::new, LogicMaterial::sprite);
        FULL_CODEC = RecordCodecBuilder.create((i) -> i.group(
                Identifier.CODEC.fieldOf("sprite").forGetter(LogicMaterial::sprite),
                Codec.BOOL.optionalFieldOf("force_translucent", false).forGetter(LogicMaterial::forceTranslucent)
        ).apply(i, LogicMaterial::new));
        CODEC = Codec.either(SIMPLE_CODEC, FULL_CODEC).xmap(Either::unwrap, (material) -> material.forceTranslucent ? Either.right(material) : Either.left(material));
    }

}
