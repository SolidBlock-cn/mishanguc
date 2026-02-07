package pers.solid.mishang.uc;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.gamerule.v1.CustomGameRuleCategory;
import net.fabricmc.fabric.api.gamerule.v1.GameRuleBuilder;
import net.fabricmc.fabric.api.gamerule.v1.GameRuleEvents;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.command.DefaultPermissions;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.StringIdentifiable;
import net.minecraft.world.rule.GameRule;
import net.minecraft.world.rule.GameRules;
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
  public static final CustomGameRuleCategory MISHANG_CATEGORY = new CustomGameRuleCategory(Mishanguc.id("mishanguc"), Text.translatable("modmenu.nameTranslation.mishanguc"));

  public static final GameRule<ToolAccess> FORCE_PLACING_TOOL_ACCESS = GameRuleBuilder.forEnum(ToolAccess.CREATIVE_ONLY)
      .category(MISHANG_CATEGORY)
      .buildAndRegister(Mishanguc.id("force_placing_tool_access"));
  public static final GameRule<ToolAccess> CARRYING_TOOL_ACCESS = GameRuleBuilder.forEnum(ToolAccess.ALL)
      .category(MISHANG_CATEGORY)
      .buildAndRegister(Mishanguc.id("carrying_tool_access"));
  public static final GameRule<ToolAccess> EXPLOSION_TOOL_ACCESS = GameRuleBuilder.forEnum(ToolAccess.ALL)
      .category(MISHANG_CATEGORY)
      .buildAndRegister(Mishanguc.id("explosion_tool_access"));

  private static void registerRuleChangeCallbackFor(GameRule<ToolAccess> rule, short type) {
    GameRuleEvents.changeCallback(rule).register((value, server) -> sync(server, type, value));
  }

  public static void registerRuleChangeCallbacks() {
    registerRuleChangeCallbackFor(FORCE_PLACING_TOOL_ACCESS, (short) 0);
    registerRuleChangeCallbackFor(CARRYING_TOOL_ACCESS, (short) 1);
    registerRuleChangeCallbackFor(EXPLOSION_TOOL_ACCESS, (short) 2);
  }

  private static void sync(MinecraftServer server, short type, ToolAccess newValue) {
    for (ServerPlayerEntity serverPlayerEntity : server.getPlayerManager().getPlayerList()) {
      sync(serverPlayerEntity, type, newValue);
    }
  }

  static void sync(ServerPlayerEntity serverPlayerEntity, short type, ToolAccess newValue) {
    ServerPlayNetworking.send(serverPlayerEntity, new RuleChangedPayload(type, newValue));
  }


  @Environment(EnvType.CLIENT)
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
        return player != null && player.getPermissions().hasPermission(DefaultPermissions.GAMEMASTERS);
      }
    }, CREATIVE_OP_ONLY {
      @Override
      public boolean hasAccess(@Nullable PlayerEntity player) {
        return player != null && player.isCreative() && player.getPermissions().hasPermission(DefaultPermissions.GAMEMASTERS);
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
      if (warn && !hasAccess && !player.getEntityWorld().isClient()) {
        player.sendMessage(createWarnText(), true);
      }
      return hasAccess;
    }

    public MutableText createWarnText() {
      return TextBridge.translatable("message.tool_access", TextBridge.translatable("message.tool_access." + asString())).formatted(Formatting.RED);
    }
  }
}
