package pers.solid.mishang.uc.item;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.util.*;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class IdCheckerToolItem extends BlockToolItem implements InteractsWithEntity {
  public IdCheckerToolItem(Settings settings, @Nullable Boolean includesFluid) {
    super(settings, includesFluid);
  }

  public ActionResult getIdOf(PlayerEntity player, World world, BlockPos blockPos) {
    BlockState blockState = world.getBlockState(blockPos);
    if (player != null) {
      final Block block = blockState.getBlock();
      final Identifier identifier = Registry.BLOCK.getId(block);
      final int rawId = Registry.BLOCK.getRawId(block);
      player.sendMessage(
          Text.literal("")
              .append(Text.translatable(
                      "debug.mishanguc.blockId.header",
                      String.format(
                          "%s %s %s", blockPos.getX(), blockPos.getY(), blockPos.getZ()))
                  .formatted(Formatting.YELLOW)));
      broadcastId(player, block.getName(), identifier, rawId);
      return ActionResult.SUCCESS;
    }
    return ActionResult.SUCCESS;
  }

  /**
   * 发送一个方块、实体或其他事物的id。
   */
  private void broadcastId(
      PlayerEntity player, Text name, @Nullable Identifier identifier, int rawId) {
    player.sendMessage(
        Text.literal("  ").append(Text.translatable("debug.mishanguc.id.name", name))
            .append("\n  ")
            .append(Text.translatable(
                "debug.mishanguc.id.id",
                identifier == null
                    ? Text.translatable("gui.none")
                    : Text.literal(identifier.toString())))
            .append("\n  ")
            .append(Text.translatable(
                "debug.mishanguc.id.rawId", Text.literal(Integer.toString(rawId)))));
  }

  @Override
  public ActionResult useOnBlock(
      PlayerEntity player,
      World world,
      BlockHitResult blockHitResult,
      Hand hand,
      boolean fluidIncluded) {
    if (world.isClient) return getIdOf(player, world, blockHitResult.getBlockPos());
    else return ActionResult.SUCCESS;
  }

  @Override
  public ActionResult beginAttackBlock(
      PlayerEntity player, World world, Hand hand, BlockPos pos, Direction direction, boolean fluidIncluded) {
    if (world.isClient) return getIdOf(player, world, pos);
    else return ActionResult.SUCCESS;
  }

  @Override
  public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
    if (world.isClient) {
      final BlockPos blockPos = user.getBlockPos();
      final Biome biome = user.getEntityWorld().getBiome(blockPos).value();
      final Registry<Biome> biomes = world.getRegistryManager().get(Registry.BIOME_KEY);
      final Identifier identifier = biomes.getId(biome);
      final int rawId = biomes.getRawId(biome);
      user.sendMessage(
          Text.literal("").append(
              Text.translatable(
                      "debug.mishanguc.biomeId.header",
                      String.format(
                          "%s %s %s", blockPos.getX(), blockPos.getY(), blockPos.getZ()))
                  .formatted(Formatting.YELLOW)));
      broadcastId(
          user,
          Text.translatable(Util.createTranslationKey("biome", identifier)),
          identifier,
          rawId);
    }
    return super.use(world, user, hand);
  }

  @Override
  public void appendTooltip(
      ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
    super.appendTooltip(stack, world, tooltip, context);
    tooltip.add(
        Text.translatable("item.mishanguc.id_checker_tool.tooltip.1")
            .formatted(Formatting.GRAY));
    final @Nullable Boolean includesFluid = includesFluid(stack);
    if (includesFluid == null) {
      tooltip.add(
          Text.translatable("item.mishanguc.id_checker_tool.tooltip.2")
              .formatted(Formatting.GRAY));
    } else if (includesFluid) {
      tooltip.add(
          Text.translatable("item.mishanguc.id_checker_tool.tooltip.3")
              .formatted(Formatting.GRAY));
    }
  }

  @Override
  public ActionResult attackEntityCallback(
      PlayerEntity player,
      World world,
      Hand hand,
      Entity entity,
      @Nullable EntityHitResult hitResult) {
    return useEntityCallback(player, world, hand, entity, hitResult);
  }

  @Override
  public ActionResult useEntityCallback(
      PlayerEntity player,
      World world,
      Hand hand,
      Entity entity,
      @Nullable EntityHitResult hitResult) {
    if (!world.isClient) return ActionResult.SUCCESS;
    final BlockPos blockPos = entity.getBlockPos();
    player.sendMessage(
        Text.literal("").append(
            Text.translatable(
                    "debug.mishanguc.entityId.header",
                    String.format(
                        "%s %s %s", blockPos.getX(), blockPos.getY(), blockPos.getZ()))
                .formatted(Formatting.YELLOW)));
    final EntityType<?> type = entity.getType();
    broadcastId(
        player,
        entity.getName(),
        Registry.ENTITY_TYPE.getId(type),
        Registry.ENTITY_TYPE.getRawId(type));
    return ActionResult.SUCCESS;
  }
}
