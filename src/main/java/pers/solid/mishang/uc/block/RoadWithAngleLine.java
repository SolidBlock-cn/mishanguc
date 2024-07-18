package pers.solid.mishang.uc.block;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.client.item.TooltipType;
import net.minecraft.data.client.*;
import net.minecraft.data.server.recipe.CraftingRecipeJsonBuilder;
import net.minecraft.data.server.recipe.ShapedRecipeJsonBuilder;
import net.minecraft.item.Item.TooltipContext;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemStack;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.EnumProperty;
import net.minecraft.text.Text;
import net.minecraft.util.BlockMirror;
import net.minecraft.util.BlockRotation;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Direction;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import pers.solid.brrp.v1.api.RuntimeResourcePack;
import pers.solid.brrp.v1.model.ModelJsonBuilder;
import pers.solid.mishang.uc.MishangUtils;
import pers.solid.mishang.uc.MishangucProperties;
import pers.solid.mishang.uc.arrp.BRRPHelper;
import pers.solid.mishang.uc.arrp.FasterJTextures;
import pers.solid.mishang.uc.util.*;

import java.util.List;

public interface RoadWithAngleLine extends Road {
  EnumProperty<HorizontalCornerDirection> FACING = MishangucProperties.HORIZONTAL_CORNER_FACING;

  @Override
  default void appendRoadProperties(StateManager.Builder<Block, BlockState> builder) {
    builder.add(FACING);
  }

  @Override
  default RoadConnectionState getConnectionStateOf(BlockState state, Direction direction) {
    return RoadConnectionState.of(
        state.get(FACING).hasDirection(direction),
        getLineColor(state, direction),
        isBevel() ? EightHorizontalDirection.of(state.get(FACING).mirror(direction)) : EightHorizontalDirection.of(direction),
        getLineType(state, direction),
        null);
  }

  @Override
  default BlockState mirrorRoad(BlockState state, BlockMirror mirror) {
    return state.with(FACING, state.get(FACING).mirror(mirror));
  }

  @Override
  default BlockState rotateRoad(BlockState state, BlockRotation rotation) {
    HorizontalCornerDirection facing = state.get(FACING);
    return state.with(FACING, facing.rotate(rotation));
  }

  @Override
  default BlockState withPlacementState(BlockState state, ItemPlacementContext ctx) {
    if (state == null) {
      return null;
    }
    final HorizontalCornerDirection rotation =
        HorizontalCornerDirection.fromRotation(ctx.getPlayerYaw());
    return state.with(
        FACING,
        ctx.getPlayer() != null && ctx.getPlayer().isSneaking()
            ? rotation.getOpposite()
            : rotation);
  }

  @Override
  default void appendRoadTooltip(
      ItemStack stack, TooltipContext context, List<Text> tooltip, TooltipType options) {
    Road.super.appendRoadTooltip(stack, context, tooltip, options);
    tooltip.add(
        TextBridge.translatable("block.mishanguc.tooltip.road_with_angle_line.1")
            .formatted(Formatting.GRAY));
    tooltip.add(
        TextBridge.translatable("block.mishanguc.tooltip.road_with_angle_line.2")
            .formatted(Formatting.GRAY));
  }

  boolean isBevel();

  @NotNull
  static <B extends RoadWithAngleLine> RecordCodecBuilder<B, Boolean> isBevelCodec() {
    return Codec.BOOL.fieldOf("is_bevel").forGetter(RoadWithAngleLine::isBevel);
  }

  class Impl extends AbstractRoadBlock implements RoadWithAngleLine {
    public static final MapCodec<Impl> CODEC = RecordCodecBuilder.mapCodec(i -> i.group(createSettingsCodec(), lineColorFieldCodec(), lineTypeFieldCodec(), isBevelCodec()).apply(i, (settings, lineColor, lineType, isBevel) -> new Impl(settings, lineColor, lineType, null, isBevel, null)));


    private final boolean isBevel;
    protected final String lineSide;
    protected final String lineTop;

    public Impl(Settings settings, LineColor lineColor, LineType lineType, boolean isBevel, String lineTop) {
      this(settings, lineColor, lineType, MishangUtils.composeStraightLineTexture(lineColor, lineType), isBevel, lineTop);
    }

    public Impl(Settings settings, LineColor lineColor, LineType lineType, String lineSide, boolean isBevel, String lineTop) {
      super(settings, lineColor, lineType);
      this.isBevel = isBevel;
      this.lineSide = lineSide;
      this.lineTop = lineTop;
    }

    @Override
    public boolean isBevel() {
      return isBevel;
    }

    @Environment(EnvType.CLIENT)
    @Override
    public @Nullable BlockStateSupplier getBlockStates() {
      final Identifier id = getBlockModelId();
      return VariantsBlockStateSupplier.create(this, BlockStateVariant.create().put(VariantSettings.MODEL, id)).coordinate(BlockStateVariantMap.create(FACING).register(direction -> BlockStateVariant.create().put(MishangUtils.INT_Y_VARIANT, direction.asRotation() - 45)));
    }

    @Environment(EnvType.CLIENT)
    @Override
    public ModelJsonBuilder getBlockModel() {
      if (lineSide == null || lineTop == null) return null;
      return ModelJsonBuilder.create(new Identifier("mishanguc:block/road_with_angle_line")).setTextures(new FasterJTextures().base("asphalt").lineSide(lineSide).lineTop(lineTop));
    }

    @Override
    public void writeBlockModel(RuntimeResourcePack pack) {
      BRRPHelper.addModelWithSlab(pack, Impl.this);
    }

    @Override
    public void appendDescriptionTooltip(List<Text> tooltip, TooltipContext options) {
      if (isBevel()) {
        tooltip.add(TextBridge.translatable("lineType.angle.bevel").formatted(Formatting.BLUE));
      } else {
        tooltip.add(TextBridge.translatable("lineType.angle.right").formatted(Formatting.BLUE));
      }
      tooltip.add(TextBridge.translatable("lineType.angle.composed", lineColor.getName(), lineType.getName()).formatted(Formatting.BLUE));
    }

    @Override
    protected MapCodec<? extends Impl> getCodec() {
      return CODEC;
    }

    private static final String[] NORMAL_BEVEL_PATTERN = {
        " *X",
        "*X ",
        "X  "
    };
    private static final String[] DOUBLE_BEVEL_PATTERN = {
        " *X",
        "*X*",
        "X* "
    };
    private static final String[] THICK_BEVEL_PATTERN = {
        "**X",
        "*X*",
        "X**"
    };
    private static final String[] NORMAL_RIGHT_ANGLE_PATTERN = {
        " * ",
        "*XX",
        " X "
    };

    @Override
    public CraftingRecipeJsonBuilder getPaintingRecipe(Block base, Block self) {
      final String[] patterns = isBevel ? switch (lineType) {
        case NORMAL -> NORMAL_BEVEL_PATTERN;
        case DOUBLE -> DOUBLE_BEVEL_PATTERN;
        case THICK -> THICK_BEVEL_PATTERN;
      } : NORMAL_RIGHT_ANGLE_PATTERN;
      return ShapedRecipeJsonBuilder.create(getRecipeCategory(), self, 3)
          .patterns(patterns)
          .input('*', lineColor.getIngredient())
          .input('X', base)
          .criterionFromItemTag("*", lineColor.getIngredient())
          .criterionFromItem(base)
          .setCustomRecipeCategory("roads");
    }
  }
}
