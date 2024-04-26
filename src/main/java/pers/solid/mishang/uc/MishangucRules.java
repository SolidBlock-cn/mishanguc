package pers.solid.mishang.uc;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.gamerule.v1.GameRuleFactory;
import net.fabricmc.fabric.api.gamerule.v1.GameRuleRegistry;
import net.fabricmc.fabric.api.gamerule.v1.rule.EnumRule;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.MutableText;
import net.minecraft.util.Formatting;
import net.minecraft.util.StringIdentifiable;
import net.minecraft.world.GameRules;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.Nullable;
import pers.solid.mishang.uc.networking.RuleChangedPayload;
import pers.solid.mishang.uc.util.TextBridge;

/**
 * 迷上城建模组新增加的游戏规则。
 *
 * @see GameRules
 */
@ApiStatus.AvailableSince("1.0.0")
public final class MishangucRules {

  public static final GameRules.Key<EnumRule<ToolAccess>> FORCE_PLACING_TOOL_ACCESS = register("force_placing_tool_access", GameRuleFactory.createEnumRule(ToolAccess.CREATIVE_ONLY, (server, rule) -> sync(server, rule, (short) 0)));

  public static final GameRules.Key<EnumRule<ToolAccess>> CARRYING_TOOL_ACCESS = register("carrying_tool_access", GameRuleFactory.createEnumRule(ToolAccess.ALL, (server, rule) -> sync(server, rule, (short) 1)));

  public static final GameRules.Key<EnumRule<ToolAccess>> EXPLOSION_TOOL_ACCESS = register("explosion_tool_access", GameRuleFactory.createEnumRule(ToolAccess.ALL));

  private static void sync(MinecraftServer server, EnumRule<ToolAccess> rule, short type) {
    for (ServerPlayerEntity serverPlayerEntity : server.getPlayerManager().getPlayerList()) {
      sync(rule, type, serverPlayerEntity);
    }
  }

  static void sync(EnumRule<ToolAccess> rule, short type, ServerPlayerEntity serverPlayerEntity) {
    ServerPlayNetworking.send(serverPlayerEntity, new RuleChangedPayload(type, rule.get()));
  }

  private static <T extends GameRules.Rule<T>> GameRules.Key<T> register(String name, GameRules.Type<T> ruleType) {
    return GameRuleRegistry.register("mishanguc:" + name, GameRules.Category.MISC, ruleType);
  }

  static void handle(RuleChangedPayload payload, ClientPlayNetworking.Context context) {
    context.client().execute(() -> {
      switch (payload.type()) {
        case 0 -> MishangucClient.CLIENT_FORCE_PLACING_TOOL_ACCESS.set(payload.toolAccess());
        case 1 -> MishangucClient.CLIENT_CARRYING_TOOL_ACCESS.set(payload.toolAccess());
      }
    });
  }

  public enum ToolAccess implements StringIdentifiable {
    ALL {
      @Override
      public boolean hasAccess(@Nullable PlayerEntity player) {
        return true;
      }
    }, CREATIVE_ONLY {
      @Override
      public boolean hasAccess(@Nullable PlayerEntity player) {
        return player != null && player.isCreative();
      }
    }, OP_ONLY {
      @Override
      public boolean hasAccess(@Nullable PlayerEntity player) {
        return player != null && player.hasPermissionLevel(2);
      }
    }, CREATIVE_OP_ONLY {
      @Override
      public boolean hasAccess(@Nullable PlayerEntity player) {
        return player != null && player.isCreative() && player.hasPermissionLevel(2);
      }
    };
    private final String name;

    ToolAccess() {
      this.name = name().toLowerCase();
    }

    @Override
    public String asString() {
      return name;
    }

    @Contract(pure = true)
    public abstract boolean hasAccess(@Nullable PlayerEntity player);

    public boolean hasAccess(PlayerEntity player, boolean warn) {
      final boolean hasAccess = hasAccess(player);
      if (warn && !hasAccess && !player.getWorld().isClient) {
        player.sendMessage(createWarnText(), true);
      }
      return hasAccess;
    }

    public MutableText createWarnText() {
      return TextBridge.translatable("message.tool_access", TextBridge.translatable("message.tool_access." + asString())).formatted(Formatting.RED);
    }
  }
}
