package pers.solid.mishang.uc.item;

import net.fabricmc.fabric.api.particle.v1.FabricParticleTypes;
import net.minecraft.block.BlockState;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.MiningToolItem;
import net.minecraft.item.ToolMaterial;
import net.minecraft.particle.BlockStateParticleEffect;
import net.minecraft.recipe.Ingredient;
import net.minecraft.tag.TagKey;
import net.minecraft.text.KeybindText;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Formatting;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class AllFunctioningToolItem extends MiningToolItem {
  protected static final AnyFunctioningToolMaterial MATERIAL = new AnyFunctioningToolMaterial();


  public AllFunctioningToolItem(Settings settings) {
    super(114513, 114510, MATERIAL, TagKey.of(Registry.BLOCK_KEY, new Identifier("minecraft", "mineable/pickaxe")), settings);
  }

  @Override
  public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
    super.appendTooltip(stack, world, tooltip, context);
    tooltip.add(new TranslatableText("item.mishanguc.all_functioning_tool.tooltip.1", new KeybindText("key.attack").styled(style -> style.withColor(0xdddddd))).formatted(Formatting.GRAY));
    tooltip.add(new TranslatableText("item.mishanguc.all_functioning_tool.tooltip.2", new KeybindText("key.use").styled(style -> style.withColor(0xdddddd))).formatted(Formatting.GRAY));
  }

  @Override
  public boolean isSuitableFor(BlockState state) {
    return true;
  }

  @Override
  public float getMiningSpeedMultiplier(ItemStack stack, BlockState state) {
    return Float.POSITIVE_INFINITY;
  }

  @Override
  public boolean postMine(ItemStack stack, World world, BlockState state, BlockPos pos, LivingEntity miner) {
    world.addParticle(new BlockStateParticleEffect(FabricParticleTypes.complex(false, BlockStateParticleEffect.PARAMETERS_FACTORY), state), pos.getX(), pos.getY(), pos.getZ(), 1, -1, 1);
    return super.postMine(stack, world, state, pos, miner);
  }

  @Override
  public ActionResult useOnEntity(ItemStack stack, PlayerEntity user, LivingEntity entity, Hand hand) {
    entity.heal(Float.POSITIVE_INFINITY);
    return ActionResult.SUCCESS;
  }

  private static class AnyFunctioningToolMaterial implements ToolMaterial {
    private AnyFunctioningToolMaterial() {
    }

    @Override
    public int getDurability() {
      return Integer.MAX_VALUE;
    }

    @Override
    public float getMiningSpeedMultiplier() {
      return Float.POSITIVE_INFINITY;
    }

    @Override
    public float getAttackDamage() {
      return Float.POSITIVE_INFINITY;
    }

    @Override
    public int getMiningLevel() {
      return Integer.MAX_VALUE;
    }

    @Override
    public int getEnchantability() {
      return Integer.MAX_VALUE;
    }

    @Override
    public Ingredient getRepairIngredient() {
      return Ingredient.ofItems(Items.BEDROCK);
    }
  }
}
