package pers.solid.mishang.uc.item;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.*;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class IdCheckerToolItem extends Item {
    public IdCheckerToolItem(Settings settings) {
        super(settings);
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity player, Hand hand) {
        if (!world.isClient() && player!=null) {
            final BlockHitResult fluidHit = (BlockHitResult) player.raycast(20, 0, player.isSneaking());
            final BlockPos blockPos = fluidHit.getBlockPos();
            final BlockState blockState = world.getBlockState(blockPos);
            final Block block = blockState.getBlock();
            final Identifier identifier = Registry.BLOCK.getId(block);
            final int rawId = Registry.BLOCK.getRawId(block);
            player.sendMessage(new TranslatableText("debug.mishanguc.blockId",String.format("%s %s %s",blockPos.getX(),blockPos.getY(),blockPos.getZ())).setStyle(Style.EMPTY.withBold(true).withColor(Formatting.YELLOW)),false);
            player.sendMessage(new TranslatableText("debug.mishanguc.blockId.name",block.getName()),false);
            player.sendMessage(new TranslatableText("debug.mishanguc.blockId.id",new LiteralText(identifier.toString())),false);
            player.sendMessage(new TranslatableText("debug.mishanguc.blockId.rawId",new LiteralText(Integer.toString(rawId))),false);
        }
        return super.use(world,player,hand);
    }

    @Override
    public ActionResult useOnBlock(ItemUsageContext context) {
        final World world = context.getWorld();
        final PlayerEntity player = context.getPlayer();
        if (!world.isClient() && player!=null) {
            final BlockPos blockPos = context.getBlockPos();
            final BlockState blockState = world.getBlockState(blockPos);
            final Block block = blockState.getBlock();
            final Identifier identifier = Registry.BLOCK.getId(block);
            final int rawId = Registry.BLOCK.getRawId(block);
            player.sendMessage(new TranslatableText("debug.mishanguc.blockId",String.format("%s %s %s",blockPos.getX(),blockPos.getY(),blockPos.getZ())).setStyle(Style.EMPTY.withBold(true).withColor(Formatting.YELLOW)),false);
            player.sendMessage(new TranslatableText("debug.mishanguc.blockId.name",block.getName()),false);
            player.sendMessage(new TranslatableText("debug.mishanguc.blockId.id",new LiteralText(identifier.toString())),false);
            player.sendMessage(new TranslatableText("debug.mishanguc.blockId.rawId",new LiteralText(Integer.toString(rawId))),false);
        }
        return ActionResult.success(world.isClient);
    }

    @Override
    public ActionResult useOnEntity(ItemStack stack, PlayerEntity player, LivingEntity entity, Hand hand) {
        final EntityType<?> entityType = entity.getType();
        final Identifier identifier = Registry.ENTITY_TYPE.getId(entityType);
        final int rawId = Registry.ENTITY_TYPE.getRawId(entityType);
        final BlockPos blockPos = entity.getBlockPos();
        player.sendMessage(new TranslatableText("debug.mishanguc.entityId",String.format("%s %s %s",blockPos.getX(),blockPos.getY(),blockPos.getZ())).setStyle(Style.EMPTY.withBold(true).withColor(Formatting.YELLOW)),false);
        player.sendMessage(new TranslatableText("debug.mishanguc.entityId.name",entity.getName()),false);
        player.sendMessage(new TranslatableText("debug.mishanguc.entityId.id",new LiteralText(identifier.toString())),false);
        player.sendMessage(new TranslatableText("debug.mishanguc.entityId.rawId",new LiteralText(Integer.toString(rawId))),false);
        return ActionResult.SUCCESS;
    }

    @Override
    public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
        super.appendTooltip(stack, world, tooltip, context);
        tooltip.add(new TranslatableText("item.mishanguc.id_checker_tool.tooltip.1").setStyle(Style.EMPTY.withColor(Formatting.GRAY)));
        tooltip.add(new TranslatableText("item.mishanguc.id_checker_tool.tooltip.2").setStyle(Style.EMPTY.withColor(Formatting.GRAY)));
    }
}
