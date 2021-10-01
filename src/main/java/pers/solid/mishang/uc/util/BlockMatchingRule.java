package pers.solid.mishang.uc.util;

import com.mojang.serialization.Lifecycle;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.BlockState;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Identifier;
import net.minecraft.util.StringIdentifiable;
import net.minecraft.util.Util;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.util.registry.SimpleRegistry;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * 一个方块匹配规则，根据该规则来匹配两个方块是否匹配。
 */
public abstract class BlockMatchingRule implements StringIdentifiable {
    protected static final RegistryKey<Registry<BlockMatchingRule>> REGISTRY_KEY = RegistryKey.ofRegistry(new Identifier("mishanguc", "block_matching_rule"));
    public static final SimpleRegistry<BlockMatchingRule> REGISTRY = new SimpleRegistry<>(REGISTRY_KEY, Lifecycle.stable());
    public static final BlockMatchingRule SAME_STATE = new BlockMatchingRule() {
        @Override
        public boolean match(@NotNull BlockState state1, @NotNull BlockState state2) {
            return state1.equals(state2);
        }
    }.register("same_state");
    public static final BlockMatchingRule SAME_BLOCK = new BlockMatchingRule() {
        @Override
        public boolean match(@NotNull BlockState state1, @NotNull BlockState state2) {
            return state1.getBlock() == state2.getBlock();
        }
    }.register("same_block");
    public static final BlockMatchingRule SAME_MATERIAL = new BlockMatchingRule() {
        @Override
        public boolean match(@NotNull BlockState state1, @NotNull BlockState state2) {
            return state1.getMaterial() == state2.getMaterial();
        }
    }.register("same_material");

    public static @Nullable BlockMatchingRule fromString(String name) {
        return REGISTRY.get(new Identifier(name));
    }

    public static @NotNull BlockMatchingRule fromString(String name, BlockMatchingRule defaultValue) {
        final BlockMatchingRule value = fromString(name);
        return value == null ? defaultValue : value;
    }

    public abstract boolean match(@NotNull BlockState state1, @NotNull BlockState state2);

    public BlockMatchingRule register(Identifier identifier) {
        return Registry.register(REGISTRY, identifier, this);
    }

    @Override
    public @Nullable String asString() {
        final @Nullable Identifier id = REGISTRY.getId(this);
        return id == null ? null : id.toString();
    }

    /**
     * 类似于{@link #asString()}，但是未注册的会返回空字符串而不是null。
     */
    public @NotNull String asStringOrEmpty() {
        final String s = asString();
        return s == null ? "" : s;
    }

    /**
     * 将其注册到注册表，并使用本模组的命名空间。
     */
    public BlockMatchingRule register(String string) {
        return register(new Identifier("mishanguc", string));
    }

    @Environment(EnvType.CLIENT)
    public Text getName() {
        return new TranslatableText(Util.createTranslationKey("blockMatchingRule", REGISTRY.getId(this)));
    }
}
