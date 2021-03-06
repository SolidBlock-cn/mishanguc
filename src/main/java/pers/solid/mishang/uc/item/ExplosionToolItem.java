package pers.solid.mishang.uc.item;

import net.devtech.arrp.api.RuntimeResourcePack;
import net.devtech.arrp.generator.ItemResourceGenerator;
import net.devtech.arrp.json.recipe.JIngredient;
import net.devtech.arrp.json.recipe.JRecipe;
import net.devtech.arrp.json.recipe.JShapedRecipe;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.util.NbtType;
import net.minecraft.advancement.criterion.InventoryChangedCriterion;
import net.minecraft.block.DispenserBlock;
import net.minecraft.block.dispenser.DispenserBehavior;
import net.minecraft.client.gui.screen.ScreenTexts;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.Entity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.predicate.entity.EntityFlagsPredicate;
import net.minecraft.predicate.entity.EntityPredicates;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.KeybindText;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Formatting;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.*;
import net.minecraft.world.GameRules;
import net.minecraft.world.World;
import net.minecraft.world.explosion.Explosion;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class ExplosionToolItem extends Item implements HotbarScrollInteraction, DispenserBehavior, ItemResourceGenerator {
  public ExplosionToolItem(Settings settings) {
    super(settings);
    DispenserBlock.registerBehavior(this, this);
  }

  @Override
  public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
    final ItemStack stack = user.getStackInHand(hand);
    final HitResult raycast = user.raycast(128, 0, user.isSneaking());
    if (raycast.getType() == HitResult.Type.MISS) {
      return TypedActionResult.fail(stack);
    }
    if (world.isClient) return TypedActionResult.success(stack);
    final Vec3d pos = raycast.getPos();
    final GameRules.BooleanRule booleanRule = world.getGameRules().get(GameRules.DO_TILE_DROPS);
    final boolean backup = booleanRule.get();
    if (user.isCreative()) {
      // ?????????????????????????????????????????????????????????
      booleanRule.set(false, null);
    }
    world.createExplosion(user, DamageSource.explosion(user.isSneaking() ? null : user), null, pos.x, pos.y, pos.z, power(stack), createFire(stack), destructionType(stack));
    stack.damage((int) power(stack), user, e -> e.sendToolBreakStatus(hand));
    if (user.isCreative()) {
      booleanRule.set(backup, null);
    }
    return TypedActionResult.success(stack);
  }

  @Override
  public Text getName(ItemStack stack) {
    return new TranslatableText(getTranslationKey(stack) + ".formatted",
        /* explosion power: */power(stack),
        /* with fire: */new TranslatableText("item.mishanguc.explosion_tool.createFire." + createFire(stack)),
        /* destruction type: */new TranslatableText("item.mishanguc.explosion_tool.destructionType." + destructionType(stack).name().toLowerCase())
    );
  }

  /**
   * @return ?????????????????????????????????
   */
  public Explosion.DestructionType destructionType(ItemStack stack) {
    final String destructionType = stack.getOrCreateNbt().getString("destructionType");
    return switch (destructionType) {
      case "none" -> Explosion.DestructionType.NONE;
      case "destroy" -> Explosion.DestructionType.DESTROY;
      default -> Explosion.DestructionType.BREAK;
    };
  }

  /**
   * @return ?????????????????????????????????????????????
   */
  public boolean createFire(ItemStack stack) {
    return stack.getOrCreateNbt().getBoolean("createFire");
  }

  /**
   * @return ??????????????????????????????????????????????????????????????? 4???
   */
  public float power(ItemStack stack) {
    final NbtCompound nbt = stack.getOrCreateNbt();
    return nbt.contains("power", NbtType.NUMBER) ? MathHelper.clamp(nbt.getFloat("power"), -128, 128) : 4;
  }

  @Override
  public void appendStacks(ItemGroup group, DefaultedList<ItemStack> stacks) {
    if (isIn(group)) {
      stacks.add(new ItemStack(this));
      {
        ItemStack stack = new ItemStack(this);
        stack.getOrCreateNbt().putFloat("power", 8);
        stacks.add(stack);
      }
      {
        ItemStack stack = new ItemStack(this);
        stack.getOrCreateNbt().putFloat("power", 16);
        stacks.add(stack);
      }
      {
        ItemStack stack = new ItemStack(this);
        stack.getOrCreateNbt().putFloat("power", 32);
        stacks.add(stack);
      }
      {
        ItemStack stack = new ItemStack(this);
        stack.getOrCreateNbt().putBoolean("createFire", true);
        stack.getOrCreateNbt().putFloat("power", 4);
        stacks.add(stack);
      }
      {
        ItemStack stack = new ItemStack(this);
        stack.getOrCreateNbt().putBoolean("createFire", true);
        stack.getOrCreateNbt().putFloat("power", 8);
        stacks.add(stack);
      }
      {
        ItemStack stack = new ItemStack(this);
        stack.getOrCreateNbt().putBoolean("createFire", true);
        stack.getOrCreateNbt().putFloat("power", 16);
        stacks.add(stack);
      }
      {
        ItemStack stack = new ItemStack(this);
        stack.getOrCreateNbt().putBoolean("createFire", true);
        stack.getOrCreateNbt().putFloat("power", 32);
        stacks.add(stack);
      }

      ItemStack stack4 = new ItemStack(this);
      stack4.getOrCreateNbt().putString("destructionType", "none");
      stack4.getOrCreateNbt().putFloat("power", 8);
      stacks.add(stack4);

      ItemStack stack5 = new ItemStack(this);
      stack5.getOrCreateNbt().putString("destructionType", "destroy");
      stacks.add(stack5);
    }
  }

  @Override
  public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
    super.appendTooltip(stack, world, tooltip, context);
    tooltip.add(new TranslatableText("item.mishanguc.explosion_tool.tooltip.1", new KeybindText("key.use").styled(style -> style.withColor(0xdddddd))).formatted(Formatting.GRAY));
    tooltip.add(new TranslatableText("item.mishanguc.explosion_tool.tooltip.2").formatted(Formatting.GRAY));
    tooltip.add(new TranslatableText("item.mishanguc.explosion_tool.tooltip.3").formatted(Formatting.GRAY));
    tooltip.add(new TranslatableText("item.mishanguc.explosion_tool.tooltip.4").formatted(Formatting.GRAY));
    tooltip.add(new TranslatableText("item.mishanguc.explosion_tool.tooltip.5").formatted(Formatting.GRAY));
    tooltip.add(new TranslatableText("item.mishanguc.explosion_tool.tooltip.power", new LiteralText(String.valueOf(power(stack))).formatted(Formatting.YELLOW)).formatted(Formatting.GRAY));
    tooltip.add(new TranslatableText("item.mishanguc.explosion_tool.tooltip.createFire", createFire(stack) ? ScreenTexts.YES.copy().formatted(Formatting.GREEN) : ScreenTexts.NO.copy().formatted(Formatting.RED)).formatted(Formatting.GRAY));
    tooltip.add(new TranslatableText("item.mishanguc.explosion_tool.tooltip.destructionType", new TranslatableText("item.mishanguc.explosion_tool.destructionType." + destructionType(stack).name().toLowerCase()).styled(style -> style.withColor(0x779999))).formatted(Formatting.GRAY));
  }

  @Override
  public void onScroll(int selectedSlot, double scrollAmount, ServerPlayerEntity player, ItemStack stack) {
    final boolean creative = player.isCreative();
    final float power = MathHelper.clamp(power(stack) - (float) scrollAmount, creative ? -128 : 0, creative ? 128 : 64);
    stack.getOrCreateNbt().putFloat("power", power);
  }

  @Override
  public ItemStack dispense(BlockPointer pointer, ItemStack stack) {
    final BlockPos basePos = pointer.getPos();
    final Direction direction = pointer.getBlockState().get(DispenserBlock.FACING);
    final ServerWorld world = pointer.getWorld();
    for (int i = 1; i < 33; i++) {
      final BlockPos pos = basePos.offset(direction, i);
      if (world.getBlockState(pos).getCollisionShape(world, pos).isEmpty()
          && world.getEntitiesByClass(Entity.class, new Box(pos), EntityPredicates.EXCEPT_SPECTATOR.and(Entity::collides).and(EntityFlagsPredicate.Builder.create().sneaking(false).build()::test)).isEmpty()
      ) {
        continue;
      }
      world.createExplosion(null, pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, power(stack), createFire(stack), destructionType(stack));
      if (stack.damage((int) power(stack), pointer.getWorld().getRandom(), null)) {
        stack.setCount(0);
      }
    }
    return stack;
  }

  @Environment(EnvType.CLIENT)
  @Override
  public void writeItemModel(RuntimeResourcePack pack) {
    // void
  }

  @Override
  public @Nullable JRecipe getCraftingRecipe() {
    final JShapedRecipe recipe = new JShapedRecipe(this)
        .pattern("TCT", " | ", " | ")
        .addKey("T", Items.TNT)
        .addKey("C", JIngredient.ofItems(Items.COMMAND_BLOCK, Items.CHAIN_COMMAND_BLOCK, Items.REPEATING_COMMAND_BLOCK))
        .addKey("|", Items.STICK);
    recipe.advancementBuilder.criterion("has_command_block", InventoryChangedCriterion.Conditions.items(Items.COMMAND_BLOCK, Items.CHAIN_COMMAND_BLOCK, Items.REPEATING_COMMAND_BLOCK));
    return recipe;
  }
}
