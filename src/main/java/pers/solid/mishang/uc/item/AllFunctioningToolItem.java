package pers.solid.mishang.uc.item;

import net.devtech.arrp.generator.ItemResourceGenerator;
import net.devtech.arrp.json.models.JModel;
import net.devtech.arrp.json.recipe.JRecipe;
import net.devtech.arrp.json.recipe.JShapelessRecipe;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
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

public class AllFunctioningToolItem extends MiningToolItem implements ItemResourceGenerator {
  protected static final AnyFunctioningToolMaterial MATERIAL = new AnyFunctioningToolMaterial();


  public AllFunctioningToolItem(Settings settings) {
    super(Float.POSITIVE_INFINITY, Float.POSITIVE_INFINITY, MATERIAL, TagKey.of(Registry.BLOCK_KEY, new Identifier("minecraft", "mineable/pickaxe")), settings);
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
    world.addParticle(new BlockStateParticleEffect(FabricParticleTypes.complex(false, BlockStateParticleEffect.PARAMETERS_FACTORY), state), pos.getX(), pos.getY(), pos.getZ(), 3, -3, 3);
    return super.postMine(stack, world, state, pos, miner);
  }

  @Override
  public ActionResult useOnEntity(ItemStack stack, PlayerEntity user, LivingEntity entity, Hand hand) {
    entity.heal(Float.POSITIVE_INFINITY);
    return ActionResult.SUCCESS;
  }

  /**
   * 由于该物品的模型是暂时由 {@link pers.solid.mishang.uc.annotations.SimpleModel} 注解确定的，故这里暂不生成模型。
   */
  @Environment(EnvType.CLIENT)
  @Override
  public @Nullable JModel getItemModel() {
    return null;
  }

  /**
   * 此物品的合成方式，就是这么变态……
   */
  @Override
  public @Nullable JRecipe getCraftingRecipe() {
    return new JShapelessRecipe(this, Items.NETHERITE_AXE, Items.NETHERITE_HOE, Items.NETHERITE_SHOVEL, Items.NETHERITE_PICKAXE, Items.NETHERITE_SWORD, Items.BEDROCK, Items.COMMAND_BLOCK, Items.CHAIN_COMMAND_BLOCK, Items.REPEATING_COMMAND_BLOCK).addInventoryChangedCriterion("has_bedrock", Items.BEDROCK).addInventoryChangedCriterion("has_command_block", Items.COMMAND_BLOCK).addInventoryChangedCriterion("has_chain_command_block", Items.CHAIN_COMMAND_BLOCK)
        .addInventoryChangedCriterion("has_repeating_command_block", Items.REPEATING_COMMAND_BLOCK);
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
