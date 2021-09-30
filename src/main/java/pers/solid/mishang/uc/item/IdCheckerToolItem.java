package pers.solid.mishang.uc.item;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Style;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Formatting;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.World;

public class IdCheckerToolItem extends Item {
    public IdCheckerToolItem(Settings settings) {
        super(settings);
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
}
