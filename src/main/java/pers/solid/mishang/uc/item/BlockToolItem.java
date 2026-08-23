package pers.solid.mishang.uc.item;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.api.EnvironmentInterface;
import net.fabricmc.fabric.api.client.rendering.v1.level.LevelExtractionContext;
import net.fabricmc.fabric.api.client.rendering.v1.level.LevelRenderContext;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.client.renderer.state.level.BlockOutlineRenderState;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.component.DataComponentMap;
import net.minecraft.util.ARGB;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;
import pers.solid.mishang.uc.Mishanguc;
import pers.solid.mishang.uc.components.MishangucComponents;
import pers.solid.mishang.uc.render.RendersBlockOutline;
import pers.solid.mishang.uc.render.state.BlockToolState;
import pers.solid.mishang.uc.render.state.MishangRenderState;

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
  private static final int OUTLINE_COLOR_GREEN = ARGB.colorFromFloat(0.8f, 0, 1, 0);
  private static final int OUTLINE_COLOR_LIGHT_GREEN = ARGB.colorFromFloat(0.5f, 0, 1, 0.5f);

  public BlockToolItem(Properties settings, @Nullable Boolean includesFluid) {
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
   * @see Item#useOn(UseOnContext)
   */
  @Override
  public InteractionResult useOn(UseOnContext context) {
    return InteractionResult.PASS;
  }

  /**
   * 默认情况下，该方法仅在 {@link #includesFluid} 为 <code>false</code> 的情况下执行，此时会进行视线追踪并获取可能为流体的方块触及结果。<br>
   * By default these methods only perform when {@link #includesFluid} returns <code>false</code>,
   * when it performs raycast and get the {@link BlockHitResult} that may be of a fluid.
   * 会在服务端和客户端同时执行。Executes both on the client and server side.
   *
   * @see #getPlayerPOVHitResult
   * @see Item#use(Level, Player, InteractionHand)
   */
  @Override
  public InteractionResult use(Level world, Player user, InteractionHand hand) {
    return super.use(world, user, hand);
  }

  /**
   * 使用此物品右键单击物品时的反应。 The reaction when right-clicking the block with the item.
   */
  public abstract InteractionResult useOnBlock(
      ItemStack stack, Player player,
      Level world,
      BlockHitResult blockHitResult,
      InteractionHand hand,
      boolean fluidIncluded);

  /**
   * 使用此物品开始破坏方块时的反应。
   *
   * @see Mishanguc#BEGIN_ATTACK_BLOCK_EVENT
   */
  public abstract InteractionResult beginAttackBlock(
      ItemStack stack, Player player, Level world, InteractionHand hand, BlockPos pos, Direction direction, boolean fluidIncluded);

  /**
   * 使用此物品中途破坏方块时的反应。
   *
   * @see Mishanguc#PROGRESS_ATTACK_BLOCK_EVENT
   */
  public InteractionResult progressAttackBlock(
      Player player, Level world, InteractionHand hand, BlockPos pos, Direction direction, boolean fluidIncluded) {
    return InteractionResult.FAIL;
  }

  public boolean includesFluid(ItemStack stack, boolean def) {
    final @Nullable Boolean includesFluid = this.includesFluid(stack);
    return Objects.requireNonNullElse(includesFluid, def);
  }

  /**
   * 如果物品堆的物品标签包含 {@link MishangucComponents#INCLUDES_FLUID} 物品组件，则返回其值，否则返回物品对象中的 {@link #includesFluid}。
   *
   * @param stack The item stack.
   * @return Whether it can detect fluid. May be {@code null}able, which means it depends.
   */
  public @Nullable Boolean includesFluid(ItemStack stack) {
    final DataComponentMap components = stack.getComponents();
    if (!components.has(MishangucComponents.INCLUDES_FLUID)) {
      return this.includesFluid;
    } else {
      return components.get(MishangucComponents.INCLUDES_FLUID);
    }
  }


  @Environment(EnvType.CLIENT)
  @Override
  public @Nullable MishangRenderState getMishangRenderState(LocalPlayer player, InteractionHand hand, ItemStack stack, LevelExtractionContext context, @Nullable HitResult result) {
    final BlockToolState state = new BlockToolState();

    final ClientLevel world = context.level();

    if (result instanceof BlockHitResult blockHitResult && includesFluid(stack, player.isShiftKeyDown())) {
      final BlockPos blockPos = blockHitResult.getBlockPos();
      state.lightGreenShape = world.getFluidState(blockPos).getShape(world, blockPos);
      state.lightGreenPos = blockPos;
    }

    return state;
  }

  @Environment(EnvType.CLIENT)
  @Override
  public boolean renderBlockOutline(
      Player player,
      ItemStack itemStack,
      LevelRenderContext context,
      BlockOutlineRenderState outlineRenderState) {
    final Vec3 cameraPos = context.levelState().cameraRenderState.pos;
    final BlockPos blockPos = outlineRenderState.pos(); // todo 这两个变量不需要了？

    if (!(context.levelState().getData(MISHANG_BLOCK_OUTLINE) instanceof final BlockToolState state)) {
      return false;
    }
    context.submitNodeCollector().submitShapeOutline(
        context.poseStack(),
        outlineRenderState.shape(),
        RenderTypes.lines(),
        OUTLINE_COLOR_GREEN,
        Minecraft.getInstance().getWindow().getAppropriateLineWidth(),
        outlineRenderState.isTranslucent() // todo 检查这里的 NPE
    );
    if (state.lightGreenPos != null && state.lightGreenShape != null) {
      context.submitNodeCollector().submitShapeOutline(
          context.poseStack(),
          state.lightGreenShape,
          RenderTypes.lines(),
          OUTLINE_COLOR_LIGHT_GREEN,
          Minecraft.getInstance().getWindow().getAppropriateLineWidth(),
          true
      );
    }
    return false;
  }
}
