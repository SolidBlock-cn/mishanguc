package pers.solid.mishang.uc.item;

import net.minecraft.block.entity.BlockEntity;
import net.minecraft.command.BlockDataObject;
import net.minecraft.command.EntityDataObject;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.text.LiteralText;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Formatting;
import net.minecraft.util.Hand;
import net.minecraft.util.Util;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import pers.solid.mishang.uc.util.NbtPrettyPrinter;

public class DataTagToolItem extends BlockToolItem implements InteractsWithEntity {
  public DataTagToolItem(Settings settings, @Nullable Boolean includesFluid) {
    super(settings, includesFluid);
  }

  @Override
  public ActionResult useOnBlock(
      PlayerEntity player,
      World world,
      BlockHitResult blockHitResult,
      Hand hand,
      boolean fluidIncluded) {
    if (!world.isClient) {
      return getBlockDataOf(player, world, blockHitResult.getBlockPos());
    } else {
      return ActionResult.SUCCESS;
    }
  }

  @Override
  public ActionResult beginAttackBlock(
      PlayerEntity player, World world, BlockPos pos, Direction direction, boolean fluidIncluded) {
    if (!world.isClient) return getBlockDataOf(player, world, pos);
    else return ActionResult.SUCCESS;
  }

  public ActionResult getBlockDataOf(PlayerEntity player, World world, BlockPos blockPos) {
    final @Nullable BlockEntity blockEntity = world.getBlockEntity(blockPos);
    if (blockEntity == null) {
      player.sendSystemMessage(
          new TranslatableText(
                  "debug.mishanguc.dataTag.block.null",
                  String.format("%s %s %s", blockPos.getX(), blockPos.getY(), blockPos.getZ()),
                  world.getBlockState(blockPos).getBlock().getName().formatted(Formatting.BOLD))
              .formatted(Formatting.RED),
          Util.NIL_UUID);
    } else {
      final BlockDataObject blockDataObject =
          new BlockDataObject(world.getBlockEntity(blockPos), blockPos);
      player.sendSystemMessage(
          new TranslatableText(
                  "debug.mishanguc.dataTag.block.header",
                  String.format("%s %s %s", blockPos.getX(), blockPos.getY(), blockPos.getZ()),
                  world.getBlockState(blockPos).getBlock().getName().formatted(Formatting.BOLD))
              .formatted(Formatting.YELLOW),
          Util.NIL_UUID);
      player.sendSystemMessage(NbtPrettyPrinter.serialize(blockDataObject.getNbt()), Util.NIL_UUID);
    }
    return ActionResult.SUCCESS;
  }

  public ActionResult getEntityDataOf(
      PlayerEntity player,
      World world,
      Hand hand,
      Entity entity,
      @Nullable EntityHitResult hitResult) {
    final EntityDataObject entityDataObject = new EntityDataObject(entity);
    final NbtCompound nbt = entityDataObject.getNbt();
    player.sendSystemMessage(
        new TranslatableText(
                "debug.mishanguc.dataTag.entity.entity",
                String.format(
                    "%s %s %s",
                    ((int) entity.getX()), ((int) entity.getY()), ((int) entity.getZ())),
                new LiteralText("").append(entity.getName()).formatted(Formatting.BOLD))
            .formatted(Formatting.YELLOW),
        Util.NIL_UUID);
    player.sendSystemMessage(NbtPrettyPrinter.serialize(nbt), Util.NIL_UUID);
    return ActionResult.SUCCESS;
  }

  @Override
  public ActionResult attackEntityCallback(
      PlayerEntity player,
      World world,
      Hand hand,
      Entity entity,
      @Nullable EntityHitResult hitResult) {
    if (!world.isClient) return getEntityDataOf(player, world, hand, entity, hitResult);
    else return ActionResult.SUCCESS;
  }

  @Override
  public ActionResult useEntityCallback(
      PlayerEntity player,
      World world,
      Hand hand,
      Entity entity,
      @Nullable EntityHitResult hitResult) {
    if (!world.isClient) return getEntityDataOf(player, world, hand, entity, hitResult);
    else return ActionResult.SUCCESS;
  }
}
