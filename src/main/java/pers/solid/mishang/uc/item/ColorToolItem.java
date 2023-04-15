package pers.solid.mishang.uc.item;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.data.client.model.Models;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.state.property.Property;
import net.minecraft.text.Text;
import net.minecraft.text.TextColor;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Formatting;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import pers.solid.brrp.v1.generator.ItemResourceGenerator;
import pers.solid.brrp.v1.model.ModelJsonBuilder;
import pers.solid.mishang.uc.MishangUtils;
import pers.solid.mishang.uc.block.ColoredBlock;
import pers.solid.mishang.uc.blockentity.ColoredBlockEntity;
import pers.solid.mishang.uc.util.TextBridge;

import java.awt.*;
import java.util.List;
import java.util.Map;

public class ColorToolItem extends BlockToolItem implements ItemResourceGenerator {
  public ColorToolItem(Settings settings, @Nullable Boolean includesFluid) {
    super(settings, includesFluid);
  }

  @Override
  public Text getName(ItemStack stack) {
    final NbtCompound nbt = stack.getTag();
    if (nbt != null && nbt.contains("color")) {
      final int color = MishangUtils.readColorFromNbtElement(nbt.get("color"));
      return TextBridge.translatable("block.mishanguc.colored_block.color", super.getName(stack), MishangUtils.describeColor(color));
    } else {
      return super.getName(stack);
    }
  }
  @Environment(EnvType.CLIENT)
  @Override
  public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
    final NbtCompound nbt = stack.getTag();
    tooltip.add(TextBridge.translatable("item.mishanguc.color_tool.tooltip.1", TextBridge.keybind("key.attack").styled(style -> style.withColor(TextColor.fromRgb(0xdddddd)))).formatted(Formatting.GRAY));
    tooltip.add(TextBridge.translatable("item.mishanguc.color_tool.tooltip.2", TextBridge.keybind("key.use").styled(style -> style.withColor(TextColor.fromRgb(0xdddddd)))).formatted(Formatting.GRAY));
    if (nbt != null && nbt.contains("color")) {
      // 此时该对象已经定义了颜色。
      final int color = MishangUtils.readColorFromNbtElement(nbt.get("color"));
      Color colorObject = new Color(color);
      tooltip.add(TextBridge.translatable("block.mishanguc.colored_block.tooltip.color",
          TextBridge.empty()
              .append(TextBridge.literal("■").styled(style -> style.withColor(TextColor.fromRgb(color))))
              .append(Integer.toHexString(color))
      ).formatted(Formatting.GRAY));
      tooltip.add(TextBridge.translatable("block.mishanguc.colored_block.tooltip.color_components", colorObject.getRed(), colorObject.getGreen(), colorObject.getBlue(), colorObject.getAlpha()).formatted(Formatting.GRAY));
    }
  }

  @SuppressWarnings({"unchecked", "rawtypes"})
  @Override
  public ActionResult useOnBlock(ItemStack stack, PlayerEntity player, World world, BlockHitResult blockHitResult, Hand hand, boolean fluidIncluded) {
    final BlockPos blockPos = blockHitResult.getBlockPos();
    BlockEntity blockEntity = world.getBlockEntity(blockPos);
    final NbtCompound nbt = stack.getTag();
    if (nbt == null || !nbt.contains("color")) {
      if (!world.isClient) {
        player.sendMessage(TextBridge.translatable("item.mishanguc.color_tool.message.no_data").formatted(Formatting.RED), true);
        return ActionResult.FAIL;
      }
      return ActionResult.PASS;
    }
    if (!(blockEntity instanceof ColoredBlockEntity)) {
      final BlockState blockState = world.getBlockState(blockPos);
      final Block block = blockState.getBlock();
      final Block coloredBlock;
      if (ColoredBlock.BASE_TO_COLORED.containsKey(block)) {
        coloredBlock = ColoredBlock.BASE_TO_COLORED.get(block);
      } else {
        coloredBlock = ColoredBlock.BASE_TAG_TO_COLORED.entrySet().stream()
            .filter(entry -> blockState.isIn(entry.getKey()))
            .findAny()
            .map(Map.Entry::getValue)
            .orElse(null);
      }

      if (coloredBlock != null) {
        BlockState coloredState = coloredBlock.getDefaultState();
        for (Property<?> property : blockState.getProperties()) {
          if (coloredState.contains(property)) {
            coloredState = coloredState.with((Property) property, blockState.get(property));
          }
        }
        world.setBlockState(blockPos, coloredState);
        final BlockEntity oldBlockEntity = blockEntity;
        blockEntity = world.getBlockEntity(blockPos);
        if (oldBlockEntity != null && blockEntity != null) {
          blockEntity.fromTag(world.getBlockState(blockPos), oldBlockEntity.writeNbt(new NbtCompound()));
        }
      }
    }
    if (blockEntity instanceof ColoredBlockEntity) {
      final int color = MishangUtils.readColorFromNbtElement(nbt.get("color"));
      ((ColoredBlockEntity) blockEntity).setColor(color);
      world.updateListeners(blockPos, blockEntity.getCachedState(), blockEntity.getCachedState(), 2);
      if (!world.isClient) {
        player.sendMessage(TextBridge.translatable("item.mishanguc.color_tool.message.success_set", MishangUtils.describeColor(color)), true);
      }
      stack.damage(1, player, p -> p.sendToolBreakStatus(hand));
      return ActionResult.success(world.isClient);
    } else {
      if (!world.isClient) {
        player.sendMessage(TextBridge.translatable("item.mishanguc.color_tool.message.not_colored").formatted(Formatting.RED), true);
        return ActionResult.FAIL;
      }
      return ActionResult.PASS;
    }
  }

  @Override
  public ActionResult beginAttackBlock(ItemStack stack, PlayerEntity player, World world, Hand hand, BlockPos pos, Direction direction, boolean fluidIncluded) {
    final BlockState blockState = world.getBlockState(pos);
    final BlockEntity blockEntity = world.getBlockEntity(pos);
    final int color;
    if (blockEntity instanceof ColoredBlockEntity) {
      ColoredBlockEntity coloredBlockEntity = (ColoredBlockEntity) blockEntity;
      stack.getOrCreateTag().putInt("color", color = coloredBlockEntity.getColor());
    } else {
      stack.getOrCreateTag().putInt("color", color = blockState.getTopMaterialColor(world, pos).color);
    }
    if (!world.isClient) {
      player.sendMessage(TextBridge.translatable("item.mishanguc.color_tool.message.success_copied", MishangUtils.describeColor(color)), true);
    }
    return null;
  }

  @Environment(EnvType.CLIENT)
  @Override
  public @NotNull ModelJsonBuilder getItemModel() {
    return ModelJsonBuilder.create(Models.HANDHELD).addTexture("layer0", getTextureId());
  }
}
