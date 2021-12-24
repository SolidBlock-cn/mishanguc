package pers.solid.mishang.uc.items;

import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import pers.solid.mishang.uc.ModItemGroups;
import pers.solid.mishang.uc.annotations.RegisterIdentifier;
import pers.solid.mishang.uc.annotations.SimpleModel;
import pers.solid.mishang.uc.item.*;

public final class MishangucItems {
  @RegisterIdentifier
  @SimpleModel(parent = "item/handheld")
  public static final RoadConnectionStateDebuggingToolItem ROAD_CONNECTION_STATE_DEBUGGING_TOOL =
      new RoadConnectionStateDebuggingToolItem(
          new FabricItemSettings().group(ModItemGroups.TOOLS), false);

  @RegisterIdentifier
  @SimpleModel(parent = "item/handheld")
  public static final IdCheckerToolItem ID_CHECKER_TOOL =
      new IdCheckerToolItem(new FabricItemSettings().group(ModItemGroups.TOOLS), null);

  @RegisterIdentifier
  @SimpleModel(parent = "item/handheld")
  public static final IdCheckerToolItem FLUID_ID_CHECKER_TOOL =
      new IdCheckerToolItem(new FabricItemSettings().group(ModItemGroups.TOOLS), true);

  @RegisterIdentifier
  @SimpleModel(parent = "item/handheld")
  public static final FastBuildingToolItem FAST_BUILDING_TOOL =
      new FastBuildingToolItem(new FabricItemSettings().group(ModItemGroups.TOOLS), null);

  @RegisterIdentifier
  @SimpleModel(parent = "item/handheld")
  public static final RotatingToolItem ROTATING_TOOL =
      new RotatingToolItem(new FabricItemSettings().group(ModItemGroups.TOOLS), null);

  @RegisterIdentifier
  @SimpleModel(parent = "item/handheld")
  public static final MirroringToolItem MIRRORING_TOOL =
      new MirroringToolItem(new FabricItemSettings().group(ModItemGroups.TOOLS), null);

  @RegisterIdentifier
  @SimpleModel(parent = "item/handheld")
  public static final SlabToolItem SLAB_TOOL =
      new SlabToolItem(new FabricItemSettings().group(ModItemGroups.TOOLS));

  @RegisterIdentifier
  @SimpleModel(parent = "item/handheld")
  public static final ForcePlacingToolItem FORCE_PLACING_TOOL =
      new ForcePlacingToolItem(new FabricItemSettings().group(ModItemGroups.TOOLS), null);

  @RegisterIdentifier
  @SimpleModel(parent = "item/handheld")
  public static final ForcePlacingToolItem FLUID_FORCE_PLACING_TOOL =
      new ForcePlacingToolItem(new FabricItemSettings().group(ModItemGroups.TOOLS), true);

  @RegisterIdentifier
  @SimpleModel(parent = "item/handheld")
  public static final BlockStateToolItem BLOCK_STATE_TOOL =
      new BlockStateToolItem(new FabricItemSettings().group(ModItemGroups.TOOLS), null);

  @RegisterIdentifier
  @SimpleModel(parent = "item/handheld")
  public static final BlockStateToolItem FLUID_STATE_TOOL =
      new BlockStateToolItem(new FabricItemSettings().group(ModItemGroups.TOOLS), true);

  @RegisterIdentifier
  @SimpleModel(parent = "item/handheld")
  public static final DataTagToolItem DATA_TAG_TOOL =
      new DataTagToolItem(new FabricItemSettings().group(ModItemGroups.TOOLS), null);

  private MishangucItems() {}

  public static void init() {}
}
