package pers.solid.mishang.uc.item;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.api.EnvironmentInterface;
import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderContext;
import net.minecraft.block.BlockState;
import net.minecraft.block.ShapeContext;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.component.ComponentMap;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import pers.solid.mishang.uc.Mishanguc;
import pers.solid.mishang.uc.components.MishangucComponents;
import pers.solid.mishang.uc.mixin.WorldRendererInvoker;
import pers.solid.mishang.uc.render.RendersBlockOutline;

import java.util.Objects;

@EnvironmentInterface(value = EnvType.CLIENT, itf = RendersBlockOutline.class)
public abstract class BlockToolItem extends Item implements RendersBlockOutline {
  /**
   * 该物品是否包括流体。<br>
   * 如果该值为 <code>null</code>，则一般表示“视情况”，通常情况下是仅潜行时包括流体。 Whether fluids are included.<br>
   * If the value of it is <code>null</code>, it usually means "it depends", typically "does while
   * sneaking".
   */
  protected final @Nullable Boolean includesFluid;

  public BlockToolItem(Settings settings, @Nullable Boolean includesFluid) {
    super(settings);
    this.includesFluid = includesFluid;
  }

  /**
   * 玩家手持物品点击方块的行为。通常此时准星已经指向一个非流体方块。<br>
   * Behaviour when a player clicks a block holding the item. Usually the crossbar has already
   * focused on a non-fluid block.<br>
   * 如果 {@link #includesFluid} 为 <code>true</code>，则该方法不会执行，因为 {@link #use} 会执行，并执行可以包含流体的视线追踪。<br>
   * If {@link #includesFluid} is <code>false</code>, it does not execute, because {@link #use}
   * executes, and performs raycast that may include fluids.<br>
   * 会在服务端和客户端同时执行。Executes both on the client and server side.
   *
   * @see Item#useOnBlock(ItemUsageContext)
   */
  @Override
  public ActionResult useOnBlock(ItemUsageContext context) {
    return ActionResult.PASS;
  }

  /**
   * 默认情况下，该方法仅在 {@link #includesFluid} 为 <code>false</code> 的情况下执行，此时会进行视线追踪并获取可能为流体的方块触及结果。<br>
   * By default these methods only perform when {@link #includesFluid} returns <code>false</code>,
   * when it performs raycast and get the {@link BlockHitResult} that may be of a fluid.
   * 会在服务端和客户端同时执行。Executes both on the client and server side.
   *
   * @see #raycast
   * @see Item#use(World, PlayerEntity, Hand)
   */
  @Override
  public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
    return super.use(world, user, hand);
  }

  /**
   * 使用此物品右键单击物品时的反应。 The reaction when right-clicking the block with the item.
   */
  public abstract ActionResult useOnBlock(
      ItemStack stack, PlayerEntity player,
      World world,
      BlockHitResult blockHitResult,
      Hand hand,
      boolean fluidIncluded);

  /**
   * 使用此物品开始破坏方块时的反应。
   *
   * @see Mishanguc#BEGIN_ATTACK_BLOCK_EVENT
   */
  public abstract ActionResult beginAttackBlock(
      ItemStack stack, PlayerEntity player, World world, Hand hand, BlockPos pos, Direction direction, boolean fluidIncluded);

  /**
   * 使用此物品中途破坏方块时的反应。
   *
   * @see Mishanguc#PROGRESS_ATTACK_BLOCK_EVENT
   */
  public ActionResult progressAttackBlock(
      PlayerEntity player, World world, Hand hand, BlockPos pos, Direction direction, boolean fluidIncluded) {
    return ActionResult.FAIL;
  }

  public boolean includesFluid(ItemStack stack, boolean def) {
    final @Nullable Boolean includesFluid = this.includesFluid(stack);
    return Objects.requireNonNullElse(includesFluid, def);
  }

  /**
   * 如果物品堆的物品标签包含 {@link MishangucComponents#INCLUDES_FIELD} 物品组件，则返回其值，否则返回物品对象中的 {@link #includesFluid}。
   *
   * @param stack The item stack.
   * @return Whether it can detect fluid. May be {@code null}able, which means it depends.
   */
  public @Nullable Boolean includesFluid(ItemStack stack) {
    final ComponentMap components = stack.getComponents();
    if (!components.contains(MishangucComponents.INCLUDES_FIELD)) {
      return this.includesFluid;
    } else {
      return components.get(MishangucComponents.INCLUDES_FIELD);
    }
  }

  @Environment(EnvType.CLIENT)
  @Override
  public boolean renderBlockOutline(
      PlayerEntity player,
      ItemStack itemStack,
      WorldRenderContext worldRenderContext,
      WorldRenderContext.BlockOutlineContext blockOutlineContext, Hand hand) {
    final VertexConsumerProvider consumers = worldRenderContext.consumers();
    if (consumers == null) return true;
    final VertexConsumer vertexConsumer = consumers.getBuffer(RenderLayer.LINES);
    final ClientWorld world = worldRenderContext.world();
    final BlockPos blockPos = blockOutlineContext.blockPos();
    final BlockState state = blockOutlineContext.blockState();
    WorldRendererInvoker.drawCuboidShapeOutline(
        worldRenderContext.matrixStack(),
        vertexConsumer,
        state.getOutlineShape(world, blockPos, ShapeContext.of(player)),
        blockPos.getX() - blockOutlineContext.cameraX(),
        blockPos.getY() - blockOutlineContext.cameraY(),
        blockPos.getZ() - blockOutlineContext.cameraZ(),
        0,
        1,
        0,
        0.8f);
    if (includesFluid(itemStack, player.isSneaking())) {
      WorldRendererInvoker.drawCuboidShapeOutline(
          worldRenderContext.matrixStack(),
          vertexConsumer,
          state.getFluidState().getShape(world, blockPos),
          blockPos.getX() - blockOutlineContext.cameraX(),
          blockPos.getY() - blockOutlineContext.cameraY(),
          blockPos.getZ() - blockOutlineContext.cameraZ(),
          0,
          1,
          0.5f,
          0.5f);
    }
    return false;
  }
}
